/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entidad.Publicacion;
import Entidad.Puntos;
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
            
            
            Statement stmt;
            ResultSet rsA;
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
           try {
            stmt = conexion.createStatement();
            String query1="Select DISTINCT PublicacionDudosa.ID AS nump "+ 
                  "FROM PublicacionDudosa INNER JOIN Persona_Publicacion on PublicacionDudosa.ID=Persona_Publicacion.IdPublicacion WHERE Persona_Publicacion.IdPersona ="+user_id+" AND PublicacionDudosa.EstadoSistema="+1+" AND Persona_Publicacion.EstadoSistema="+1;
          
            rsA = stmt.executeQuery(query1);
            
            List<String> resultado = new ArrayList<String>();
            while (rsA.next()) {
                JSONObject pubTotales = new JSONObject();
                Statement stmt1;
                ResultSet rs1;
                stmt1 = conexion.createStatement();
                String query="Select * FROM PublicacionDudosa WHERE ID="+rsA.getString("nump");
                rs1 = stmt1.executeQuery(query);
                JSONArray pub = new JSONArray();
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
                    pub2.put("publicacionID",rs1.getString("ID"));
                    
                    pub.add(pub2);
                }
                
                
                rs1.close();
                stmt1.close();
                
                stmt1 = conexion.createStatement();
                query="Select * FROM Publicacion WHERE ID="+rsA.getString("nump");
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
                    pub2.put("publicacionID",rs1.getString("ID"));
                    
                    pub.add(pub2);
                }
                
                
                 rs1.close();
                 stmt1.close();
                
                
               pubTotales.put("publicacion",pub);
               publicacion12.add(pubTotales);
                    resultado.add(rsA.getString("nump"));
                    System.out.println("......................");
                    System.out.println(rsA.getString("nump"));
               
            }
            rsA.close();
            stmt.close();
            
           
                
                        
            
            
             } catch (SQLException ex) {
                    Logger.getLogger(publicacionesDudosasDAO.class.getName()).log(Level.SEVERE, null, ex);
             } 
               /* rs1.close();
                stmt1.close();
                
                stmt1 = conexion.createStatement();
                query="Select * FROM Publicacion WHERE ID="+rs.getString("nump");
                rs1 = stmt.executeQuery(query);
                
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
                    pub2.put("publicacionID",rs1.getString("ID"));
                    
                    pub.add(pub2);
                }
                
                
                 rs1.close();
                 stmt1.close();
                 
                
                 pubTotales.put("publicacion",pub);
                 publicacion12.add(pubTotales);*/
                 
            
            
            
            publicaciones.put("publicaciones",publicacion12);
        
         
            
             
            
    
        return publicaciones;   
    }
    
}
