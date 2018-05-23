/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.loginDao;
import Dao.publicacionesDudosasDAO;
import Dao.publicacionesDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrador
 */
public class BeanPublicacionesDudosas {
    
    public  BeanPublicacionesDudosas(){
        
    } 
    
    public JSONObject obtenerPublicacionDudosa(String token){
        JSONObject respuesta = new JSONObject();
        loginDao login_dao = new loginDao();
        publicacionesDudosasDAO publicaciones_dudosas_dao = new publicacionesDudosasDAO();
   
        try {
            System.out.println("Token dudodasas: "+token);
            JSONObject objUserID = login_dao.getUserIDByToken(token);
            
            if ((int) objUserID.get("code") == 0) {
                respuesta = publicaciones_dudosas_dao.publicacionDudosas(objUserID.get("id").toString());
                respuesta.put("code", 0);
                respuesta.put("description", "Operacion exitosa");
            }
            else { 
                respuesta = objUserID;   
                respuesta.put("code", 9997);
                respuesta.put("description", "Error en sistema");
            }
        } catch (SQLException ex) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("obtenerPublicacionDudosa(): " + respuesta.toString());
            Logger.getLogger(BeanPublicacionesDudosas.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return respuesta;
    }
    
    public JSONObject insertarPublicacion(JSONObject jsonPublicacion){
        JSONObject obj= new JSONObject();
        return obj;
        
    }
        
    public JSONObject remplazarPublicacion (JSONObject publicaicon, String idPublicacionOriginal){
        JSONObject respuesta = new JSONObject();
        publicacionesDudosasDAO publicaciones_dudosas_dao = new publicacionesDudosasDAO();
   
        try {
            
            respuesta = publicaciones_dudosas_dao.eliminarTodasDudosas(idPublicacionOriginal);
            
            if ((int) respuesta.get("code") == 0)
                respuesta = publicaciones_dudosas_dao.remplazarDudosa(publicaicon, idPublicacionOriginal);
            
        } catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error en sistema");
            System.out.println("remplazarPublicacion(): " + respuesta.toString());
            Logger.getLogger(BeanPublicacionesDudosas.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return respuesta;
    }
    
     public JSONObject publicaiconDudosaDiferente (JSONObject publicaicon){
        JSONObject respuesta = new JSONObject();
        publicacionesDAO publicaciones_dao = new publicacionesDAO();
        publicacionesDudosasDAO publicaciones_dudosas_dao = new publicacionesDudosasDAO();
   
        try {
            
            respuesta = publicaciones_dao.insertPublicacion(publicaicon.get("userId").toString(), publicaicon,"NO_VERIFICADO");
            
            /**
             * Remover dudosa
             */
            if ((int)respuesta.get("code")==0)
                respuesta = publicaciones_dudosas_dao.eliminarDudosa(publicaicon.get("ID").toString());
                      
        } catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error en sistema");
            System.out.println("remplazarPublicacion(): " + respuesta.toString());
            Logger.getLogger(BeanPublicacionesDudosas.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return respuesta;
    }
    
    
}
