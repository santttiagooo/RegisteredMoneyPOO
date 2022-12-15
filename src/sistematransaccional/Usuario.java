/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistematransaccional;

import excepciones.ExcepcionUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import utilidades.BaseDeDatos;

/**
 *
 * @author Santiago
 */
public class Usuario {

    private int id;
    private String DNI;
    private String nombre;
    private int edad;
    private Cuenta cuenta;

    public Usuario() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", DNI=" + DNI + ", nombre=" + nombre + ", edad=" + edad + ", cuenta=" + cuenta + '}';
    }

    /**
     * Este es uno de los metodos mas importantes, ya que lo que hace es crear
     * un objeto usuario trayendo toda la información desde la Base de datos
     * MySQL
     *
     * @param baseDeDatos Base de datos MySQL la cual contiene la información
     * @param id ID del usuario en la Base de Datos
     * @return Usuario correspondiente al usuario en la Base de Datos
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     * @throws excepciones.ExcepcionUsuario
     */
    public static Usuario crearDesdeBaseDeDatos(BaseDeDatos baseDeDatos, int id) throws ClassNotFoundException, SQLException, ExcepcionUsuario {

        if (baseDeDatos.estaConectada()) {
        } else {
            baseDeDatos.conectar();
        }
        PreparedStatement seleccionarUsuarioStatement = baseDeDatos.getConexion().prepareStatement(
                "SELECT * FROM usuarios WHERE id = ?");
        seleccionarUsuarioStatement.setInt(1, id);
        ResultSet rs = seleccionarUsuarioStatement.executeQuery();
        if (!rs.isBeforeFirst()) {
            throw new ExcepcionUsuario("El usuario no existe en la base de datos");
        } else {
            /*El usuario existe en la base de datos*/
            rs.next();
            /*Crear Objeto Usuario con los datos de la consulta en la tabla 'usuarios'*/
            Usuario usuarioBD = new Usuario();
            usuarioBD.setId(id);
            usuarioBD.setDNI(rs.getString("dni"));
            usuarioBD.setEdad(rs.getInt("edad"));
            usuarioBD.setNombre(rs.getString("nombre"));

            /*Crear Objeto Cuenta con los datos de la consulta en la tabla 'usuarios'*/
            PreparedStatement obtenerTransaccionesStatement = baseDeDatos.getConexion().prepareStatement(
                    "SELECT * FROM transacciones WHERE id_usuario = ? ORDER BY fecha ASC");
            obtenerTransaccionesStatement.setInt(1, id);
            ResultSet rs2 = obtenerTransaccionesStatement.executeQuery();

            Cuenta cuenta = new Cuenta();
            /*Iteracion sobre las transacciones del usuario en la base de datos*/
            while (rs2.next()) {
                /**
                 * Verificar si es un ingreso y egreso segun la columna 'tipo'
                 * de la base de Datos Si tipo == 1, es ingreso Si tipo != 1, es
                 * egreso (si tipo es cualquier numero diferente de 1 es egreso)
                 */
                Dinero transaccion;
                if (rs2.getInt("tipo") == 1) {
                    /* Crear objeto Ingreso con las columnas 'valor' y 'descripcion' */
                    transaccion = new Ingreso(rs2.getDouble("valor"), rs2.getString("descripcion"));
                    cuenta.agregarIngreso(rs2.getInt("id_transaccion"), (Ingreso) transaccion);
                } else {
                    /* Crear objeto Egreso con las columnas 'valor' y 'descripcion' */
                    transaccion = new Egreso(rs2.getDouble("valor"), rs2.getString("descripcion"));
                    cuenta.agregarEgreso(rs2.getInt("id_transaccion"), (Egreso) transaccion);
                }

                Date fecha = rs2.getDate("fecha");
                Time hora = rs2.getTime("fecha");
                transaccion.setFecha(LocalDateTime.of(
                        fecha.getYear() + 1900,
                        fecha.getMonth() + 1,
                        fecha.getDate(),
                        hora.getHours(),
                        hora.getMinutes(),
                        hora.getSeconds()));
            }
            /* Agregar objeto cuenta creado al Usuario*/
            cuenta.validarSaldo();
            usuarioBD.setCuenta(cuenta);
            /*Retornar usuario creado*/
            return usuarioBD;
        }
    }
    
    public static Usuario crearUsuarioVacio(){
        Usuario u = new Usuario();
        u.setNombre("");
        u.setDNI("");
        u.setEdad(0);
        u.setCuenta(new Cuenta());
        return u;
    }
}
