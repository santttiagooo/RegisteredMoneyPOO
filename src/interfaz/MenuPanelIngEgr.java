/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import sistematransaccional.CategoriaDinero;
import sistematransaccional.Dinero;
import sistematransaccional.Egreso;
import sistematransaccional.Ingreso;
import sistematransaccional.Usuario;
import utilidades.BaseDeDatos;
import utilidades.ComboItem;
import utilidades.MensajeSimple;

/**
 *
 * @author Santiago
 */
public final class MenuPanelIngEgr extends javax.swing.JPanel {

    Font font;
    MenuPanel menuPanel;
    byte tipo;

    /**
     * Creates new form MenuPanelIngEgr
     *
     * @param font
     * @param menuPanel
     */
    public MenuPanelIngEgr(Font font, MenuPanel menuPanel) {
        initComponents();
        this.font = font;
        this.menuPanel = menuPanel;
        for (JComponent comp : new JComponent[]{
            this.tipoLabel,
            this.valorLabel,
            this.descripcionLabel,
            this.agregarBoton,
            this.regresarBoton
        }) {
            comp.setFont(this.font.deriveFont(16f));
        }

        this.agregarLabel.setFont(font.deriveFont(30f));
        ((JLabel) categoriaComboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel) categoriaComboBox.getRenderer()).setFont(this.font.deriveFont(16f));

        // <editor-fold defaultstate="collapsed" desc="Agregar funciones botones"> 
        agregarBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        agregarBoton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                agregarBoton.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                agregarBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        });

        regresarBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        regresarBoton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                regresarBoton.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                regresarBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        });
        // </editor-fold>   

        this.resetear();

    }

    public MenuPanelIngEgr ingreso() {
        this.resetear();
        this.tipo = MenuPanel.MENU_INGRESO;
        this.agregarLabel.setText("Agregar Ingreso");
        this.icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scr_imagenes/add-money.png")));
        CategoriaDinero.getListaIngresosComboItem().forEach((i) -> {
            this.categoriaComboBox.addItem(i);
        });
        return this;
    }

    public MenuPanelIngEgr egreso() {
        this.resetear();
        this.tipo = MenuPanel.MENU_EGRESO;
        this.agregarLabel.setText("Agregar Egreso");
        this.icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scr_imagenes/quit-money.png")));

        CategoriaDinero.getListaEgresosComboItem().forEach((i) -> {
            this.categoriaComboBox.addItem(i);
        });
        return this;
    }

    public byte getTipo() {
        return tipo;
    }

    /**
     *
     * @param bd
     * @param u
     * @throws SQLException
     * @throws ClassNotFoundException
     *
     * Es la funcion que toma la informacion que el usuario puso en la interfaz
     * del software y la convierte en un objeto Dinero, posteriormente la carga
     * en la Base de Datos
     */
    public void agregarInformacionABaseDeDatos(BaseDeDatos bd, Usuario u) throws SQLException, ClassNotFoundException {

        //Verificar si el valor en el JTextField es un double
        double valorJFieldDouble = 0d;
        try {
            valorJFieldDouble = Double.parseDouble(valorTextField.getText());
            //Verificar si la categoria esta seleccionada
            if (categoriaComboBox.getSelectedIndex() == -1) {
                MensajeSimple.mostrar("Por favor selecciona una categoria");
                return;
            } else {
                //Despues de las  verificaciones proceder a enviar la información

                //Generar objeto ingreso o egreso de acuerdo al tipo de menu
                Dinero d = ((this.tipo == MenuPanel.MENU_INGRESO) ? new Ingreso() : new Egreso());
                d.setCantidad(valorJFieldDouble);
                d.setDescripcion(descripcionTextArea.getText().replaceAll("\n", " ").trim());
                d.setFecha(LocalDateTime.now());
                //Verificar si la base de datos esta conectada y conectar en caso contrario
                if (!bd.estaConectada()) {
                    bd.conectar();
                }

                //Hacer statement para la base de datos
                PreparedStatement pst = bd.getConexion()
                        .prepareStatement("INSERT INTO transacciones"
                                + " (id_usuario,fecha,valor,tipo,categoria,descripcion)"
                                + " VALUES (?,NOW(),?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                //Agregar informacion al Query
                pst.setInt(1, u.getId());
                pst.setDouble(2, d.getCantidad());
                pst.setInt(3, d.getTipo());
                pst.setInt(4, ((ComboItem) categoriaComboBox.getSelectedItem()).getValue());
                pst.setString(5, d.getDescripcion());
                //Ejecutar Query y obtener ID de la transaccion
                int affectedRows = pst.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("No se pudo ingresar la transaccion en la base de datos.");
                }

                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int transaccionId = generatedKeys.getInt(1);
                        //Agregar transaccion al Objeto usuario activo en la aplicacion
                        if (d.esIngreso()) {
                            this.menuPanel.getUsuarioActivo().getCuenta().agregarIngreso(transaccionId, (Ingreso) d);
                        } else {
                            this.menuPanel.getUsuarioActivo().getCuenta().agregarEgreso(transaccionId, (Egreso) d);
                        }
                    } else {
                        throw new SQLException("No se pudo ingresar la transaccion en la base de datos, no se genero ID");
                    }
                }
            }

        } catch (NumberFormatException e) {
            MensajeSimple.mostrar("Por favor ingresa un valor valido");
        }
        this.menuPanel.cambiarVistaMenu(MenuPanel.MENU_GENERAL);
    }

    public MenuPanelIngEgr resetear() {
        tipo = MenuPanel.MENU_INGRESO;
        categoriaComboBox.removeAllItems();
        valorTextField.setText("");
        descripcionTextArea.setText("");
        regresarBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        agregarBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        return this;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        agregarLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        categoriaComboBox = new javax.swing.JComboBox<>();
        descripcionLabel = new javax.swing.JLabel();
        tipoLabel = new javax.swing.JLabel();
        valorLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descripcionTextArea = new javax.swing.JTextArea();
        agregarBoton = new javax.swing.JButton();
        regresarBoton = new javax.swing.JButton();
        valorTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        icono = new javax.swing.JLabel();

        jLabel6.setText("jLabel6");

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(814, 50));

        agregarLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        agregarLabel.setText("Agregar Ingreso");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(agregarLabel)
                .addContainerGap(658, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(agregarLabel)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        categoriaComboBox.setBorder(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
        jPanel2.add(categoriaComboBox, gridBagConstraints);

        descripcionLabel.setText("Descripcion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel2.add(descripcionLabel, gridBagConstraints);

        tipoLabel.setText("Tipo*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel2.add(tipoLabel, gridBagConstraints);

        valorLabel.setText("Valor*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel2.add(valorLabel, gridBagConstraints);

        descripcionTextArea.setColumns(20);
        descripcionTextArea.setRows(5);
        jScrollPane1.setViewportView(descripcionTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        agregarBoton.setText("Agregar");
        agregarBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        agregarBoton.setContentAreaFilled(false);
        agregarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarBotonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel2.add(agregarBoton, gridBagConstraints);

        regresarBoton.setText("Regresar");
        regresarBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        regresarBoton.setContentAreaFilled(false);
        regresarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarBotonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel2.add(regresarBoton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel2.add(valorTextField, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        icono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scr_imagenes/add-money.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(icono)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(icono)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 6, 1);
        jPanel2.add(jPanel3, gridBagConstraints);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void regresarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarBotonActionPerformed
        this.resetear();
        this.menuPanel.cambiarVistaMenu(MenuPanel.MENU_GENERAL);

    }//GEN-LAST:event_regresarBotonActionPerformed

    private void agregarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarBotonActionPerformed
        try {
            // Agregar transaccion en base de datos y en Objeto Usuario
            this.agregarInformacionABaseDeDatos(this.menuPanel.framePrincipalParent.getBaseDeDatos(),
                    this.menuPanel.getUsuarioActivo());
        } catch (SQLException | ClassNotFoundException ex) {
            MensajeSimple.mostrar("Ha ocurrido un error al intentar cargar la transaccion a la base de datos:\n" + ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_agregarBotonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarBoton;
    private javax.swing.JLabel agregarLabel;
    private javax.swing.JComboBox<ComboItem> categoriaComboBox;
    private javax.swing.JLabel descripcionLabel;
    private javax.swing.JTextArea descripcionTextArea;
    private javax.swing.JLabel icono;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton regresarBoton;
    private javax.swing.JLabel tipoLabel;
    private javax.swing.JLabel valorLabel;
    private javax.swing.JTextField valorTextField;
    // End of variables declaration//GEN-END:variables
}
