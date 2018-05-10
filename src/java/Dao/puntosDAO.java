/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Entidad.Puntos;
import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class puntosDAO {

    public puntosDAO() {
    }
    
    public void insetarPuntos(Puntos puntos){
            Statement stmt;
            ResultSet rs;
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
        try {
            stmt = conexion.createStatement();
            String query = "INSERT INTO PuntosPublicacion (IdPersona,Puntos) values('"+ puntos.getIdPersona()+"','"+ puntos.getPuntos()+"')";            
            System.out.println("Query insertar puntos: " + query);
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(puntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            //conex.desconectar();
        }
         
            
    }
    
    public void actualizarPuntos(Puntos puntos){
            Statement stmt;
            ResultSet rs;
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
        try {
            stmt = conexion.createStatement();
            String query = "UPDATE PuntosPublicacion set Puntos ='"+ puntos.getPuntos()+"'WHERE IdPersona ='"+ puntos.getIdPersona()+"'";            
            System.out.println("Query UPDATE puntos: " + query);
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(puntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            //conex.desconectar();
        }
    }
    
     public void insetarMedallas(int identificacion,int Bilingue,int Cientifico,int Director,int Doctor,int Investigador,int Jefe,int Administrarivo){
            Statement stmt;
            ResultSet rs;
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
        try {
            stmt = conexion.createStatement();
            String query = "INSERT INTO Medallas (IdPersona,Bilingue,Cientifico,Director,Doctor,Investigador,Jefe, Administrativo) values"+
                           "('"+ identificacion+"','"+Bilingue+"','"+Cientifico+"','"+Director+"','"+Doctor+"','"+Investigador+"','"+Jefe+"','"+ Administrarivo+"')";            
            System.out.println("Query insertar medallas: " + query);
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(puntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            //conex.desconectar();
        }
         
            
    }
    public void actualizarMedallas(int identificacion,int Bilingue,int Cientifico,int Director,int Doctor,int Investigador,int Jefe,int Administrarivo){
            Statement stmt;
            ResultSet rs;
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
        try {
            stmt = conexion.createStatement();
            String query = "UPDATE Medallas set Bilingue="+Bilingue+",Cientifico="+Cientifico+",Director="+Director+",Doctor="+Doctor+",Investigador="+Investigador+",Jefe="+Jefe+",Administrativo="+ Administrarivo+" Where IdPersona ="+ identificacion+"";            
            System.out.println("Query update medallas: " + query);
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(puntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            //conex.desconectar();
        }
         
            
    }
    
}
