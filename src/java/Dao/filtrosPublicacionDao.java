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
import org.json.simple.JSONObject;

/**
 *
 * @author Administrador
 */
public class filtrosPublicacionDao {
    
 public filtrosPublicacionDao(){
     super();
    
 }   
 
            
  public List<Publicacion> buscarPublicacion(String fechaInicio, String fechaFin, String nombre, String titulo,String departamento, String tipo){
           List<Publicacion>publicaciones=new ArrayList<Publicacion>();
           Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs = null;
     try {
         stmt = conexion.createStatement();
         String query="";
         if(!fechaInicio.equals("")&& !fechaFin.equals("")&& !nombre.equals("") && !titulo.equals("") &&!departamento.equals("")&&!tipo.equals("")){
             System.out.println("entre 1");
             query="Select *"+ 
                  "FROM (Publicacion INNER JOIN Persona_Publicacion on Publicacion.ID=Persona_Publicacion.IdPublicacion) INNER JOIN Persona ON Persona_Publicacion.IdPersona=Persona.ID "+
                  "WHERE Publicacion.Titulo='"+ titulo + "' AND Persona.nombre='"+ nombre +"' AND Publicacion.Tipo='"+ tipo +"' AND Publicacion.FechaInicio BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"'";
                  System.out.println(query);
         }else if (fechaInicio.equals("") && fechaFin.equals("") && !nombre.equals("") && !titulo.equals("") && !departamento.equals("") && !tipo.equals("")){
              System.out.println("entre 2");    
             query="Select *"+ 
                  "FROM (Publicacion INNER JOIN Persona_Publicacion on Publicacion.ID=Persona_Publicacion.IdPublicacion) INNER JOIN Persona ON Persona_Publicacion.IdPersona=Persona.ID "+
                  "WHERE Publicacion.Titulo='"+ titulo + "' AND Persona.nombre='"+ nombre +"' AND Publicacion.Tipo='"+ tipo +"'";
         }else if (!fechaInicio.equals("")&& !fechaFin.equals("") && !nombre.equals("") && titulo.equals("") && !departamento.equals("")&&!tipo.equals("")){
              System.out.println("entre 3");    
             query="Select *"+ 
                  "FROM (Publicacion INNER JOIN Persona_Publicacion on Publicacion.ID=Persona_Publicacion.IdPublicacion) INNER JOIN Persona ON Persona_Publicacion.IdPersona=Persona.ID "+
                  "WHERE Persona.nombre='"+ nombre +"' AND Publicacion.Tipo='"+ tipo +"' AND Publicacion.FechaInicio BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"'";
          
             
         }else if (!fechaInicio.equals("")&& !fechaFin.equals("") && nombre.equals("") && titulo.equals("") && !departamento.equals("")&& !tipo.equals("")){
                System.out.println("entre 4");
             query="Select *"+ 
                  "FROM Publicacion"+
                  "WHERE  Publicacion.Tipo='"+ tipo +"' AND Publicacion.FechaInicio BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"'";
          
         }else if (fechaInicio.equals("")&& fechaFin.equals("") && !nombre.equals("") && titulo.equals("") && !departamento.equals("") && tipo.equals("")){
                 System.out.println("entre 5");
             query="Select *"+ 
                  "FROM (Publicacion INNER JOIN Persona_Publicacion on Publicacion.ID=Persona_Publicacion.IdPublicacion) INNER JOIN Persona ON Persona_Publicacion.IdPersona=Persona.ID "+
                  "WHERE Persona.nombre='"+ nombre +"'";
          
         }else if (fechaInicio.equals("")&& fechaFin.equals("") && nombre.equals("") && !titulo.equals("") && !departamento.equals("")&& tipo.equals("")){
                 System.out.println("entre 6");
             query="Select *"+ 
                  "FROM Publicacion"+
                  "WHERE Publicacion.Titulo='"+ titulo + "'";
          
         }else if (fechaInicio.equals("")&& fechaFin.equals("") && nombre.equals("") && titulo.equals("") && !departamento.equals("")&& !tipo.equals("")){
                 System.out.println("entre 6");
             query="Select *"+ 
                  "FROM Publicacion"+
                  "WHERE Publicacion.Tipo='"+ tipo +"'";
         }else if (!fechaInicio.equals("")&& !fechaFin.equals("") && nombre.equals("") && titulo.equals("") && !departamento.equals("")&& tipo.equals("")){
                 System.out.println("entre 6");
             query="Select *"+ 
                  "FROM Publicacion"+
                  "WHERE FechaInicio BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"'";
         }else if (!fechaInicio.equals("")&& !fechaFin.equals("") && !nombre.equals("") && titulo.equals("") && !departamento.equals("")&& tipo.equals("")){
                 System.out.println("entre 6");
             query="Select *"+ 
                  "FROM (Publicacion INNER JOIN Persona_Publicacion on Publicacion.ID=Persona_Publicacion.IdPublicacion) INNER JOIN Persona ON Persona_Publicacion.IdPersona=Persona.ID "+
                  "WHERE Persona.nombre='"+ nombre +"' AND Publicacion.FechaInicio BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"'";
             
         }else if (!fechaInicio.equals("")&& !fechaFin.equals("") && nombre.equals("") && !titulo.equals("") && !departamento.equals("")&& tipo.equals("")){
                 System.out.println("entre 6");
             query="Select *"+ 
                  "FROM Publicacion"+
                  "WHERE Publicacion.Titulo='"+ titulo + "'AND Publicacion.FechaInicio BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"'";
             
         }else if (!fechaInicio.equals("")&& !fechaFin.equals("") && nombre.equals("") && titulo.equals("") && !departamento.equals("")&& !tipo.equals("")){
                 System.out.println("entre 6");
             query="Select *"+ 
                  "FROM Publicacion"+
                  "WHERE Publicacion.Tipo='"+ tipo +"' AND Publicacion.FechaInicio BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"'";
         }else{
              query="Select *"+ 
                  "FROM Publicacion";
             
         }   
             rs = stmt.executeQuery(query);                     
                                  
                while (rs.next()) {
                    Publicacion pub= new Publicacion();
                    pub.setTitulo(rs.getString(2));
                    pub.setTipo(rs.getString(3));
                    pub.setFecha(rs.getString(8));
                    pub.setISBN(rs.getString(4));
                    pub.setISSN(rs.getString(5));
                    pub.setLugar(rs.getString(6));
                    pub.setEditorial(rs.getString(7));
                    publicaciones.add(pub);
                             
                 }
            
            rs.close();
            stmt.close();
            conex.desconectar();
         
      }  catch (SQLException ex) {
         Logger.getLogger(filtrosPublicacionDao.class.getName()).log(Level.SEVERE, null, ex);
     }
   return publicaciones;
  }
}  
