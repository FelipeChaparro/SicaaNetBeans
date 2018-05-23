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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrador
 */
public class publicacionesDudosasDAO {
    
    public publicacionesDudosasDAO(){
        
    }
    
    public JSONObject publicacionDudosas(String user_id) {
           
            JSONObject publicaciones = new JSONObject();
            JSONArray publicacion12 = new JSONArray();
            
            int code=0;
            Statement stmt;
            ResultSet rsA;
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
           try {
            stmt = conexion.createStatement();
            String query1="Select DISTINCT PublicacionDudosa.ID AS nump "+ 
                  "FROM PublicacionDudosa INNER JOIN Persona_Publicacion on PublicacionDudosa.ID=Persona_Publicacion.IdPublicacion WHERE Persona_Publicacion.IdPersona ="+user_id+" AND PublicacionDudosa.EstadoSistema="+1+" AND Persona_Publicacion.EstadoSistema="+1;
          
               System.out.println("Query publicacionDudosas: "+query1);
            rsA = stmt.executeQuery(query1);
            
            List<String> resultado = new ArrayList<String>();
            while (rsA.next()) {
                JSONObject pubTotales = new JSONObject();
                Statement stmt1;
                ResultSet rs1;
                JSONArray pub = new JSONArray();
                stmt1 = conexion.createStatement();
                String query="Select * FROM Publicacion WHERE ID="+rsA.getString("nump");
                rs1 = stmt1.executeQuery(query);
                
                while (rs1.next()) {
                    JSONObject pub2 = new JSONObject();
                    pub2.put("Titulo",rs1.getString("Titulo"));
                    pub2.put("Tipo",rs1.getString("Tipo"));
                    pub2.put("codigoPublicacion",rs1.getString("codigoPublicacion"));
                    pub2.put("Lugar",rs1.getString("Lugar"));
                    pub2.put("Editorial",rs1.getString("Editorial"));
                    pub2.put("FechaInicio",rs1.getString("FechaInicio"));
                    pub2.put("Extraido",rs1.getString("Extraido"));
                    pub2.put("userId",user_id);
                    pub2.put("fuente","original");
                    pub2.put("plataforma",rs1.getString("plataforma"));
                    pub2.put("duracion",rs1.getString("duracion"));
                    pub2.put("tipoEspecifico",rs1.getString("tipoEspecifico"));
                    pub2.put("publicacionID",rs1.getString("ID"));
                    
                    pub.add(pub2);
                }
                
                rs1.close();
                stmt1.close();
                 
                stmt1 = conexion.createStatement();
                query="Select * FROM PublicacionDudosa WHERE EstadoSistema=1 AND ID="+rsA.getString("nump");
                rs1 = stmt1.executeQuery(query);
               
                while (rs1.next()) {
                    JSONObject pub2 = new JSONObject();
                    pub2.put("Titulo",rs1.getString("Titulo"));
                    pub2.put("ID", rs1.getString("idPublicacionDudosa"));
                    pub2.put("Tipo",rs1.getString("Tipo"));
                    pub2.put("codigoPublicacion",rs1.getString("codigoPublicacion"));
                    pub2.put("Lugar",rs1.getString("Lugar"));
                    pub2.put("Editorial",rs1.getString("Editorial"));
                    pub2.put("FechaInicio",rs1.getString("FechaInicio"));
                    pub2.put("Extraido",rs1.getString("Extraido"));
                    pub2.put("userId",user_id);
                    pub2.put("fuente","dudosas");
                    pub2.put("plataforma",rs1.getString("plataforma"));
                    pub2.put("duracion",rs1.getString("duracion"));
                    pub2.put("tipoEspecifico",rs1.getString("tipoEspecifico"));
                    pub2.put("publicacionID",rs1.getString("ID"));
                    pub.add(pub2);
                }
       
                rs1.close();
                stmt1.close();

                pubTotales.put("publicacion",pub);
                publicacion12.add(pubTotales);
                resultado.add(rsA.getString("nump"));

               
            }
            rsA.close();
            stmt.close();

             } catch (SQLException ex) {
                    Logger.getLogger(publicacionesDudosasDAO.class.getName()).log(Level.SEVERE, null, ex);
                    code=9999;
             } 

            publicaciones.put("publicaciones",publicacion12);
            publicaciones.put("code",code);
   
        return publicaciones;   
    }

    public void actualizarPublicacion(JSONObject mNewData) {
        System.out.println("actualizar"+mNewData.toJSONString());
        System.out.println(mNewData.get("publicacionID"));
        
        Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        Statement stmt = null;
        Statement stmt1 = null;
        
        String query;
        try {
            
            stmt = conexion.createStatement();
            /*query = "UPDATE Publicacion SET Titulo='"+ mNewData.get("Titulo")+
                    "',Tipo='"+ mNewData.get("Tipo")+
                    "',Extraido='"+ mNewData.get("Extraido")+
                    "',Lugar="+ mNewData.get("Lugar")+
                    ",codigoPublicacion='"+ mNewData.get("codigoPublicacion")+
                    "',Editorial='"+ mNewData.get("Editorial")+
                    "',FechaInicio="+ mNewData.get("FechaInicio")+
                    "WHERE ID=" + mNewData.get("publicacionID");*/
            query = "UPDATE Publicacion SET "
                    + "Titulo='" + mNewData.get("Titulo")  + "',"
                    + "Tipo='" + mNewData.get("Tipo") + "',"
                    + "FechaInicio=" + ((mNewData.get("FechaInicio") == null) ? null : "'" + mNewData.get("FechaInicio") + "'") + ","
                    + "codigoPublicacion=" + ((mNewData.get("codigoPublicacion") == null) ? null : "'" + mNewData.get("codigoPublicacion") + "'") + ","
                    + "Lugar=" + ((mNewData.get("Lugar") == null) ? null : "'" + mNewData.get("Lugar") + "'") + ","
                    + "Editorial=" + ((mNewData.get("Editorial") == null) ? null : "'" + mNewData.get("Editorial") + "'")
                    + "WHERE ID=" + mNewData.get("publicacionID");
            
            System.out.println(query);
            stmt.executeUpdate(query);
            System.out.println(query);
            stmt.close();
            
             stmt1 = conexion.createStatement(); 
             query = "UPDATE PublicacionDudosa SET "
                    + "EstadoSistema='" + 0 + "'"
                    + "WHERE ID=" + mNewData.get("publicacionID");
             System.out.println(query);
            stmt1.execute(query);
            
            stmt1.close();
            
            
            
        } catch(SQLException e) {
           
        } finally {
       
            //conex.desconectar();
     
        }
       
    }
    
    public void cambiarEstado (JSONObject mNewData){
         Cone conex = new Cone();
         Connection conexion= conex.getConnection();
      
        Statement stmt1 = null;
        
        String query;
        try {
            
             stmt1 = conexion.createStatement(); 
             query = "UPDATE PublicacionDudosa SET "
                    + "EstadoSistema='" + 0 + "'"
                    + "WHERE ID=" + mNewData.get("publicacionID")+" AND Titulo='" + mNewData.get("Titulo");
             System.out.println(query);
            stmt1.execute(query);
            
            stmt1.close();
            
            
            
        } catch(SQLException e) {
           
        } finally {
       
            //conex.desconectar();
     
        }
        
    }
    
    public JSONObject eliminarTodasDudosas (String idPublicaiconDudosaOriginal){
         
        JSONObject respuesta = new JSONObject();
        Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        Statement stmt1 = null;
        String query = "UPDATE PublicacionDudosa SET EstadoSistema = 0 WHERE ID="+idPublicaiconDudosaOriginal;
        System.out.println("Query eliminarDudosas(): "+query);
        
        try {
            stmt1 = conexion.createStatement();  
            stmt1.execute(query);
            stmt1.close();
            respuesta.put("code", 0);
            respuesta.put("desciption", "Operacion exitosa");
        } catch(SQLException ex) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("eliminarDudosas(): "+respuesta.toString());
            Logger.getLogger(publicacionesDudosasDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta;
    }
    
    public JSONObject remplazarDudosa (JSONObject publicaicon,String idPublicaiconDudosaOriginal){
         
        JSONObject respuesta = new JSONObject();
        Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        Statement stmt1 = null;
        String query = "UPDATE Publicacion SET "
                    + "Titulo='" + publicaicon.get("Titulo")  + "',"
                    + "Tipo='" + publicaicon.get("Tipo") + "',"
                    + "codigoPublicacion=" + ((publicaicon.get("codigoPublicacion") == null) ? null : "'" + publicaicon.get("codigoPublicacion") + "'") + ","
                    + "Lugar=" + ((publicaicon.get("Lugar") == null) ? null : "'" + publicaicon.get("Lugar") + "'") + ","
                    + "Editorial=" + ((publicaicon.get("Editorial") == null) ? null : "'" + publicaicon.get("Editorial") + "'") + ","
                    + "Extraido=" + ((publicaicon.get("Extraido") == null) ? null : "'" + publicaicon.get("Extraido") + "'") + ","
                    + "FechaInicio=" + ((publicaicon.get("FechaInicio") == null) ? null : "'" + publicaicon.get("FechaInicio") + "'") + ","
                    + "Duracion=" + ((publicaicon.get("Duracion") == null) ? null : "'" + publicaicon.get("Duracion") + "'") + ","
                    + "Plataforma=" + ((publicaicon.get("Plataforma") == null) ? null : "'" + publicaicon.get("Plataforma") + "'") + ","
                    + "tipoEspecifico=" + ((publicaicon.get("tipoEspecifico") == null) ? null : "'" + publicaicon.get("tipoEspecifico") + "'")
                    + " WHERE ID=" + idPublicaiconDudosaOriginal;
        
        System.out.println("Query remplazarDudosa(): "+query);
        
        try {
            stmt1 = conexion.createStatement();  
            stmt1.execute(query);
            stmt1.close();
            respuesta.put("code", 0);
            respuesta.put("desciption", "Operacion exitosa");
            
            respuesta = cambiarEstadoDudosaXPendienteVirifica(publicaicon.get("publicacionID").toString(),publicaicon.get("userId").toString());
            
        } catch(SQLException ex) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("remplazarDudosa(): "+respuesta.toString());
            Logger.getLogger(publicacionesDudosasDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta;
    }
    
    private JSONObject cambiarEstadoDudosaXPendienteVirifica (String idPublicacion, String idPersona){
         
        JSONObject respuesta = new JSONObject();
        Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        Statement stmt1 = null;
        String query = "update Persona_Publicacion set EstadoPublicacion = 'Pendiente de verificacion' where IdPublicacion = "+idPublicacion+" and IdPersona ="+idPersona;
        
        System.out.println("Query cambiarEstadoDudosaXPendienteVirifica(): "+query);
        
        try {
            stmt1 = conexion.createStatement();  
            stmt1.execute(query);
            stmt1.close();
            respuesta.put("code", 0);
            respuesta.put("desciption", "Operacion exitosa");
        } catch(SQLException ex) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("cambiarEstadoDudosaXPendienteVirifica(): "+respuesta.toString());
            Logger.getLogger(publicacionesDudosasDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta;
    }
    
    public JSONObject eliminarDudosa (String idPublicaiconDudosa){
         
        JSONObject respuesta = new JSONObject();
        Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        Statement stmt1 = null;
        String query = "UPDATE PublicacionDudosa SET EstadoSistema = 0 WHERE idPublicacionDudosa="+idPublicaiconDudosa;
        System.out.println("Query eliminarDudosas(): "+query);
        
        try {
            stmt1 = conexion.createStatement();  
            stmt1.execute(query);
            stmt1.close();
            respuesta.put("code", 0);
            respuesta.put("desciption", "Operacion exitosa");
        } catch(SQLException ex) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("eliminarDudosas(): "+respuesta.toString());
            Logger.getLogger(publicacionesDudosasDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta;
    }
    
    
    
}
