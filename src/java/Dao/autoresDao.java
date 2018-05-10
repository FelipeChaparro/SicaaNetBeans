/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entidad.Autor;
import JavaBean.BeanConcordancia;
import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class autoresDao {
    
    public autoresDao(){
        super();
    }
    
    public List<Autor> busquedaAutorBD(String Autor){
        BeanConcordancia bean= new BeanConcordancia();
        
        Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        
            Statement stmt = null;
            ResultSet rs;
            
        List<Autor> coincidencias= new ArrayList<Autor>();
        try {
            stmt = conexion.createStatement();
             //System.out.println("select * from Persona where user_correo='"+user_name+"'");
            rs = stmt.executeQuery("SELECT * FROM Persona WHERE EstadoSistema='"+1+"'");
            boolean esta=false;
            while (rs.next() && !esta) {
               Autor autor= new Autor();
               String nombre =rs.getString("Nombre");
               autor.setNombre(nombre);
               int id=rs.getInt("ID");
               autor.setId(id);
               double porcentaje=bean.getSimilarity(nombre, Autor,"NOMBRE");
               double valor=0.90;
               double valorMax=1.0;
               if(valorMax==porcentaje){
                   coincidencias.clear();
                   coincidencias.add(autor);
                   esta=true;
                   //break;
               }else if(porcentaje >=  valor){
                   coincidencias.add(autor);
               }
            }
            rs.close();
            stmt.close();
            //conex.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(autoresDao.class.getName()).log(Level.SEVERE, null, ex);
        }
           
      return coincidencias;   
             
    }
    
    public List<Autor> getAllAutoresinDB (){
        
        Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        
        Statement stmt = null;
        ResultSet rs;
            
        List<Autor> coincidencias = new ArrayList<Autor>();
        try {
            stmt = conexion.createStatement();
             //System.out.println("select * from Persona where user_correo='"+user_name+"'");
            rs = stmt.executeQuery("SELECT * FROM Persona WHERE EstadoSistema='"+1+"'");
            boolean esta=false;
            while (rs.next() && !esta) {
               Autor autor = new Autor();
               String nombre = rs.getString("Nombre");
               autor.setNombre(nombre);
               int id=rs.getInt("ID");
               autor.setId(id);
               coincidencias.add(autor);
            }
            rs.close();
            stmt.close();
            //conex.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(autoresDao.class.getName()).log(Level.SEVERE, null, ex);
        }
           
      return coincidencias;   
             
    }
    
}
