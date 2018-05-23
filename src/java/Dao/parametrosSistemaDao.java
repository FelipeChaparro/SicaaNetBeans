/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 *
 * @author hariasor
 */
public class parametrosSistemaDao {
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public parametrosSistemaDao(){
        super();
    }
    
    public JSONObject getParametrosSistema () throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONObject parametros = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from parametros_sistema");
            rs = stmt.executeQuery("select * from parametros_sistema");
            while (rs.next()) {                
                parametros.put("tiempo_sesion", rs.getString("tiempo_sesion"));
                parametros.put("numero_podio_publicaciones", rs.getString("numero_podio_publicaciones"));
                parametros.put("numero_publicaciones_recientes", rs.getString("numero_publicaciones_recientes"));
                parametros.put("numero_nube_palabras", rs.getString("numero_nube_palabras"));
                parametros.put("cookieResearch", rs.getString("cookieResearch"));
                parametros.put("cookieGoogle", rs.getString("cookieGoogle"));
            }
            respuesta.put("parametros", parametros);
            rs.close();
            stmt.close();
            System.out.println("getParametrosSistema(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getParametrosSistema(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getParametrosSistema(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
}
