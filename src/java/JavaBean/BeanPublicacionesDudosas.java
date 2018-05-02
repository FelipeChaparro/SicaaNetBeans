/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.publicacionesDudosasDAO;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrador
 */
public class BeanPublicacionesDudosas {
    
    public  BeanPublicacionesDudosas(){
        
    } 
    
    public JSONObject obtenerPublicacionDudosa(String IdPersona){
        publicacionesDudosasDAO pubDudosas= new publicacionesDudosasDAO();
        JSONObject jsonDudosas= pubDudosas.publicacionDudosas("1");
        System.out.println(jsonDudosas);
        return jsonDudosas;
    }
    public JSONObject insertarPublicacion(JSONObject jsonPublicacion){
        JSONObject obj= new JSONObject();
        return obj;
        
    }
    
}
