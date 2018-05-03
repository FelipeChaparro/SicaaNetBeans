/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entidad.Publicacion;
import com.mysql.jdbc.Connection;
import com.sun.xml.ws.security.opt.impl.util.SOAPUtil;
import conexionBD.Cone;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrador
 */
public class filtrosPublicacionDao {
    
 public filtrosPublicacionDao(){
     super();
    
 }   
 
  public JSONObject getByFiltros (String fechaQueryStatement, String nombreQueryStatement, String rolQueryStatement, String tipoQueryStatement){
       Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        JSONObject respuesta = new JSONObject();
         try {
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
            
            String selectStatemt = "select DISTINCT(PU.ID) as id, PU.Titulo, PU.Tipo, PU.codigoPublicacion, PU.Lugar, PU.Editorial, PU.FechaInicio ";
            String fromStatement = " from Publicacion PU inner join Persona_Publicacion PP on PP.IdPublicacion = PU.ID";
            String whereStatement = " where PP.EstadoSistema = 1";
            String groupByStatement = "";
            Boolean existeGroupByAnterior = false;
            
            
            
            if (fechaQueryStatement != null) {
                whereStatement += " and "+fechaQueryStatement;
            }
            
            if (nombreQueryStatement != null) {
                
                //selectStatemt = selectStatemt.replace("P", ", P");
                
                fromStatement += " inner join Persona PE on PE.ID = PP.IdPersona";
                
                whereStatement += " and "+nombreQueryStatement;
                
                
                
            }
            
            if (rolQueryStatement != null) {
                
                //selectStatemt = selectStatemt.replace("PP", ", PP");
                
                whereStatement += " and "+rolQueryStatement;
                
                
            }
            
            if (tipoQueryStatement != null) {
                
                //selectStatemt = selectStatemt.replace("PU", ", PU");
                
                whereStatement += " and "+tipoQueryStatement;
                
               
            }
                                
            stmt = conexion.createStatement();
            String query = selectStatemt+fromStatement+whereStatement;
            System.out.println("Query: "+query);
            
            rs = stmt.executeQuery(query);                     
            JSONArray arr = new JSONArray();                       
            while (rs.next()) {
                JSONObject obj= new JSONObject();
                obj.put("titulo",rs.getString(2));
                obj.put("tipo",rs.getString(3));
                obj.put("codigoPublicacion",rs.getString(4));
                obj.put("lugar",rs.getString(5));
                obj.put("editorial",rs.getString(6));
                obj.put("fecha",rs.getString(7));
                System.out.println(rs.getString(1));
                arr.add(obj);
            }
            
            rs.close();
            stmt.close();
            respuesta.put("publicaciones", arr);
           
         
        }  catch (SQLException ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getParametrosSistema(): "+respuesta.toString());
            Logger.getLogger(filtrosPublicacionDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
             conex.desconectar();
         }
        
        return respuesta;
  }
}  
