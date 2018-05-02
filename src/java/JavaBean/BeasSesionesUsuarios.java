/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;


import Dao.loginDao;
import Dao.parametrosSistemaDao;
import Dao.publicacionesDAO;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.json.simple.JSONObject;

/**
 *
 * @author hariasor
 */
public class BeasSesionesUsuarios {
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public BeasSesionesUsuarios(){
        super();
    }
    
    public JSONObject validar_token (String token) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONObject respuesta_validarTokenVO = new JSONObject();
        JSONObject respuesta_getUserIDByTokenVO = new JSONObject();
        JSONObject respuesta_getPublicacionesIDVO = new JSONObject();
        JSONObject respuesta_getPublicacionesDescripcionesVO = new JSONObject();
        
        loginDao loginDao = new loginDao();
        publicacionesDAO publicacionesDAO = new publicacionesDAO();
        
        respuesta_validarTokenVO = loginDao.validar_user_tokenDAO(token);
        
        if (Integer.parseInt(respuesta_validarTokenVO.get("code").toString()) == 0) {
            
            respuesta_getUserIDByTokenVO = loginDao.getUserIDByToken(token);
            if (Integer.parseInt(respuesta_getUserIDByTokenVO.get("code").toString()) == 0) {
            
                respuesta_getPublicacionesIDVO = publicacionesDAO.getAllPublicacionesByPersonaID(respuesta_getUserIDByTokenVO.get("id").toString());
                
                respuesta = respuesta_getPublicacionesIDVO;
                respuesta.put("token", token);
            
            }
            else
                respuesta = respuesta_getUserIDByTokenVO;
        }
        else 
            respuesta = respuesta_validarTokenVO;         
        
        return respuesta;
    }
 
}
