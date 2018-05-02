/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.loginDao;
import Dao.publicacionesDAO;
import java.sql.SQLException;
import org.json.simple.JSONObject;

/**
 *
 * @author juanchaparro
 */
public class BeanDesactivarPublicacion {
    public BeanDesactivarPublicacion() {
        super();
    }
    
    public JSONObject desactivarPublicacion(JSONObject mInfo) {   
        String user_id;
        JSONObject mUserObject;
        Integer code_respuesta;
        JSONObject respuesta_bean = new JSONObject();
        try {
            loginDao mLogin = new loginDao();
            publicacionesDAO mDataBase = new publicacionesDAO();
            mUserObject = mLogin.getUserIDByToken(mInfo.get("token").toString());
            code_respuesta = Integer.parseInt(mUserObject.get("code").toString());
            if (code_respuesta == 0) {
                user_id = mUserObject.get("id").toString();
                respuesta_bean = mDataBase.desactivarPublicacion(user_id, mInfo.get("idPublicacion").toString());
            }
            else {
                respuesta_bean.put("code", code_respuesta);
                respuesta_bean.put("description", mUserObject.get("description").toString());
            }
        } catch(SQLException e) {
            System.out.println("Error: " + e);
            respuesta_bean.put("code", 9999);
            respuesta_bean.put("description", "Error en BeanDesactivarPublicacion");
        }
        return respuesta_bean;
    }
}
