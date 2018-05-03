/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.publicacionesDAO;
import Dao.publicacionesDudosasDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Administrador
 */
public class BeanPublicacionesDudosas {
    
    public  BeanPublicacionesDudosas(){
        
    } 
    
    public JSONObject obtenerPublicacionDudosa(String IdPersona){
        publicacionesDudosasDAO pubDudosas= new publicacionesDudosasDAO();
        JSONObject jsonDudosas= pubDudosas.publicacionDudosas(IdPersona);
        System.out.println(jsonDudosas);
        return jsonDudosas;
    }
    public JSONObject insertarPublicacion(String jsonPublicacion){
        JSONObject obj= new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(jsonPublicacion);
            publicacionesDudosasDAO pubDudosas= new publicacionesDudosasDAO();
            pubDudosas.actualizarPublicacion(json);
            obj.put("code",0);
        } catch (ParseException ex) {
            Logger.getLogger(BeanPublicacionesDudosas.class.getName()).log(Level.SEVERE, null, ex);
           obj.put("code",9999);
        }
        
        return obj;
        
        
    }
    
     public JSONObject cambiarPublicacion(String jsonPublicacion){
        JSONObject obj= new JSONObject();
        JSONParser parser = new JSONParser();
        publicacionesDAO pub = new publicacionesDAO();
        try {
            JSONObject json = (JSONObject) parser.parse(jsonPublicacion);
            obj= pub.insertPublicacion((String) json.get("userId"), json);
            publicacionesDudosasDAO pubDudosas= new publicacionesDudosasDAO();
            pubDudosas.cambiarEstado(json);
           
            
        } catch (ParseException ex) {
            Logger.getLogger(BeanPublicacionesDudosas.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (SQLException ex) {
            Logger.getLogger(BeanPublicacionesDudosas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return obj;
        
        
    }
    
}
