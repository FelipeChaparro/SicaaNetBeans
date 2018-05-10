/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexionBD;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JUANCHO
 */
public class Cone {
    private static Connection conn;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "sql10231472";
    private static final String password = "v44jkXCd8H";
    private static final String url = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10231472";
    
    public Cone() {
        try {
            if (conn == null) {
                Class.forName(driver);
                conn = (Connection) DriverManager.getConnection(url, user, password);
            }
        } catch(Exception e) {
            System.out.println("Error al conectar: " + e);
        }
    }
    
    public Connection getConnection() {
        try {
            if (conn == null) {
                Class.forName(driver);
                conn = (Connection) DriverManager.getConnection(url, user, password);
            }    
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cone.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Cone.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    public void desconectar() {
        try {
            if (this.conn == null) {
                System.out.println("Conexion es null");
            }
            else {
                this.conn.close();
                conn = null;
                System.out.println("Conexion Terminada");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Cone.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

