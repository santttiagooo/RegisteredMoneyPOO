/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import sistematransaccional.Usuario;
import utilidades.MensajeSimple;

/**
 *
 * @author Santiago
 */
public final class MenuPanel extends javax.swing.JPanel {

    JProgressBar barraProgreso;
    FramePrincipal framePrincipalParent;
    Usuario usuarioActivo;
    DecimalFormat df;
    MenuPanelIngEgr menuPanelIngEgr;

    /**
     * Creates new form menuPanel
     *
     * @param font
     * @param barraProgreso
     * @param framePrincipalParent
     */
    public MenuPanel(Font font, JProgressBar barraProgreso, FramePrincipal framePrincipalParent) {
        initComponents();
        initComponents2(font, barraProgreso, framePrincipalParent);
        //tablaDeTransacciones.setFont(font.deriveFont(13f));
    }

    private void initComponents2(Font font, JProgressBar barraProgreso, FramePrincipal framePrincipalParent) {
        // <editor-fold defaultstate="collapsed" desc="Establecer variables">  
        this.df = new DecimalFormat("###,###,###,###,###,###.##");
        this.barraProgreso = barraProgreso;
        this.framePrincipalParent = framePrincipalParent;
        this.nombre.setFont(font.deriveFont(39f));
        this.saldoLabel.setFont(font.deriveFont(18f));
        this.menuPanelIngEgr = new MenuPanelIngEgr(font, this);
        //tablaDeTransacciones.setFont(font.deriveFont(14f));
        //saldo.setFont(font.deriveFont(48f));
        // </editor-fold>   
        // <editor-fold defaultstate="collapsed" desc="Agregar funciones botones"> 
        egresoBoton.setFont(font.deriveFont(18f));
        egresoBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        egresoBoton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                egresoBoton.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                egresoBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        });

        ingresoBoton.setFont(font.deriveFont(18f));
        ingresoBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ingresoBoton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ingresoBoton.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ingresoBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        });
        // </editor-fold>   
        // <editor-fold defaultstate="collapsed" desc="Cambio de JPanel para ingreso y egreso"> 

        // </editor-fold>  
    }

    public void cambiarVistaMenu(byte menu) {
        this.jPanel4.removeAll();
        switch (menu) {
            case MenuPanel.MENU_INGRESO:
                this.jPanel4.add(menuPanelIngEgr.ingreso());
                break;
            case MenuPanel.MENU_EGRESO:
                this.jPanel4.add(menuPanelIngEgr.egreso());
                break;
            default:
                actualizarDatosUsuario();
                this.jPanel4.add(this.PanelGeneral);
                break;
        }
        repaint();
        revalidate();
    }
    public static final byte MENU_GENERAL = 0;
    public static final byte MENU_INGRESO = 1;
    public static final byte MENU_EGRESO = 2;

    public void resetear() {
        setUsuarioActivo(Usuario.crearUsuarioVacio());
        actualizarDatosUsuario();
    }

    /**
     * Pone el usuario activo y actualiza los datos de este dentro del JPanel
     *
     * @param usuario
     */
    public void setUsuarioActivo(Usuario usuario) {
        this.usuarioActivo = usuario;
        actualizarDatosUsuario();
    }

    public void actualizarDatosUsuario() {
        this.barraProgreso.setValue(0);
        this.barraProgreso.setVisible(true);
        nombre.setText("Bienvenido, " + usuarioActivo.getNombre());
        saldo.setText("$  " + df.format(usuarioActivo.getCuenta().getSaldo()));
        this.barraProgreso.setValue(25);
        actualizarTablaDeTransacciones();
        this.barraProgreso.setVisible(false);
        this.barraProgreso.setValue(0);
    }

    /**
     * Actualiza la tabla de transacciones realizadas por el usuario
     */
    public void actualizarTablaDeTransacciones() {
        /*Borra las filas existentes en la tabla*/
        tablaDeTransacciones.setDefaultEditor(Object.class, null);
        DefaultTableModel dtm = (DefaultTableModel) tablaDeTransacciones.getModel();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        dtm.setRowCount(0);
        /**
         * Iteración sobre el mapa de transacciones de la cuenta del Usuario y
         * agregado de filas con la información
         */

        this.barraProgreso.setValue(45);
        usuarioActivo.getCuenta().getTransacciones().forEach((k, d) -> {
            dtm.addRow(new Object[]{
                String.valueOf(k),
                sdf.format(d.getFecha()),
                d.getTipoString(),
                df.format(d.getCantidad()),
                d.getDescripcion()
            });
        });

        this.barraProgreso.setValue(70);
        final Color ingColor = new Color(254, 115, 129);
        final Color EgrColor = new Color(204, 255, 204);
        //Poner fondo a las filas de acuerdo si son ingreso o egreso
        tablaDeTransacciones.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                String status = (String) table.getModel().getValueAt(row, 2);
                if ("Ingreso".equals(status)) {
                    setBackground(ingColor);
                } else {
                    setBackground(EgrColor);
                }
                return this;
            }
        });

        this.barraProgreso.setValue(80);
        //Poner ancho de columnas de la tabla de acuerdo a su contenido,
        for (int column = 0; column < tablaDeTransacciones.getColumnCount(); column++) {
            TableColumn tableColumn = tablaDeTransacciones.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < tablaDeTransacciones.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tablaDeTransacciones.getCellRenderer(row, column);
                Component c = tablaDeTransacciones.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + tablaDeTransacciones.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }

        this.barraProgreso.setValue(99);
    }

    /**
     * Hace que el objeto usuarioActivo sea igual a null
     */
    public void borrarUsuarioActivo() {
        usuarioActivo = null;
    }

    /**
     *
     * @return Objeto Usuario correspondiente al usuario Activo dentro de este
     * MenuPanel
     */
    public Usuario getUsuarioActivo() {
        return usuarioActivo;
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

        jPanel1 = new javax.swing.JPanel();
        nombre = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        saldo = new javax.swing.JLabel();
        saldoLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cerrarUsuario = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        PanelGeneral = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDeTransacciones = new javax.swing.JTable();
        egresoBoton = new javax.swing.JButton();
        ingresoBoton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 150));

        nombre.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        nombre.setText("Bienvenido, Santiago Lopez");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        saldo.setFont(new java.awt.Font("Tahoma", 2, 48)); // NOI18N
        saldo.setText("$ 0");

        saldoLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldoLabel.setText("Tu saldo es");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saldoLabel)
                .addGap(18, 18, 18)
                .addComponent(saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saldo)
                    .addComponent(saldoLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel1.setText("Info");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(nombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 346, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nombre)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(117, 77));

        cerrarUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cerrarUsuario.setText("Cerrar Sesión");
        cerrarUsuario.setContentAreaFilled(false);
        cerrarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(736, Short.MAX_VALUE)
                .addComponent(cerrarUsuario)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(cerrarUsuario)
                .addGap(19, 19, 19))
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        PanelGeneral.setBackground(new java.awt.Color(255, 255, 255));
        PanelGeneral.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PanelGeneral.setLayout(new java.awt.GridBagLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Transacciones Realizadas");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel5.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        PanelGeneral.add(jLabel5, gridBagConstraints);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jPanel5.setLayout(new java.awt.BorderLayout());

        tablaDeTransacciones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tablaDeTransacciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Fecha", "Tipo", "Valor", "Descripción"
            }
        ));
        jScrollPane1.setViewportView(tablaDeTransacciones);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.4;
        PanelGeneral.add(jPanel5, gridBagConstraints);

        egresoBoton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        egresoBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scr_imagenes/quit-money.png"))); // NOI18N
        egresoBoton.setText("Agregar Egreso");
        egresoBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        egresoBoton.setContentAreaFilled(false);
        egresoBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        egresoBoton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        egresoBoton.setVerifyInputWhenFocusTarget(false);
        egresoBoton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        egresoBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                egresoBotonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.3;
        PanelGeneral.add(egresoBoton, gridBagConstraints);

        ingresoBoton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ingresoBoton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scr_imagenes/add-money.png"))); // NOI18N
        ingresoBoton.setText("Agregar Ingreso");
        ingresoBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ingresoBoton.setContentAreaFilled(false);
        ingresoBoton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ingresoBoton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ingresoBoton.setVerifyInputWhenFocusTarget(false);
        ingresoBoton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ingresoBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingresoBotonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.3;
        PanelGeneral.add(ingresoBoton, gridBagConstraints);

        jPanel4.add(PanelGeneral, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cerrarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarUsuarioActionPerformed
        //Cerrar sesión
        if (JOptionPane.showConfirmDialog(null, "¿Estás seguro de querer cerrar la sesión?",
                "RegisteredMoney", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

            this.cambiarVistaMenu(MENU_GENERAL);
            this.resetear();
            this.framePrincipalParent.cambiarMenu(FramePrincipal.LOGIN_PANEL);
            this.menuPanelIngEgr.resetear();
        }

    }//GEN-LAST:event_cerrarUsuarioActionPerformed

    private void ingresoBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingresoBotonActionPerformed
        //Cambiar a panel de Ingreso
        this.cambiarVistaMenu(MENU_INGRESO);
        ingresoBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }//GEN-LAST:event_ingresoBotonActionPerformed

    private void egresoBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_egresoBotonActionPerformed
        //Cambiar a panel de egreso
        this.cambiarVistaMenu(MENU_EGRESO);
        egresoBoton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }//GEN-LAST:event_egresoBotonActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // Boton de Mostrar Info

        MensajeSimple.mostrar(
                "Registered Money 2022" + "\n\n"
                + "Tu DNI es: " + this.getUsuarioActivo().getDNI() + "\n"
                + "\n"
                + "Aplicacion diseñada por:\n"
                + "Daniel Santiago Lopez Giraldo\n"
                + "David Sebastian Ruiz Virgues\n"
                + "Diego Alejandro Granados Brand\n"
                +  "Alejandra Banquet Nisperuza\n"
                );


    }//GEN-LAST:event_jLabel1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelGeneral;
    private javax.swing.JButton cerrarUsuario;
    private javax.swing.JButton egresoBoton;
    private javax.swing.JButton ingresoBoton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel saldo;
    private javax.swing.JLabel saldoLabel;
    private javax.swing.JTable tablaDeTransacciones;
    // End of variables declaration//GEN-END:variables
}
