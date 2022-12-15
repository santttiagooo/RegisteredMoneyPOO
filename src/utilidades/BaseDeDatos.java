/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Santiago
 */
public class BaseDeDatos {

    public static String connectionUrl = "jdbc:mysql://remotemysql.com:3306/9am4IRztfL";
    public static String USUARIO = "9am4IRztfL";
    public static String NOMBRE_DB = "9am4IRztfL";
    public static String CLAVE = "NGAkNuyvd9";

    Connection connection;

    public Connection conectar() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(connectionUrl, USUARIO, CLAVE);
        return connection;
    }

    public Connection getConexion() {
        return connection;
    }

    public boolean estaConectada() {
        try {
            return connection.isValid(5);
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean cerrarConexion() throws SQLException {
        if (estaConectada()) {
            connection.close();
        }
        return true;

    }
}
