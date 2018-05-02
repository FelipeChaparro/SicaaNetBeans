/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.loginDao;
import Dao.publicacionesDAO;
import Dao.puntosDAO;
import Entidad.Puntos;
import java.sql.SQLException;
import org.json.simple.JSONObject;

/**
 *
 * @author juanchaparro
 */
public class BeanCambiarEstadoPublicacion {
    public BeanCambiarEstadoPublicacion() {
	super();
    }
    
    public JSONObject cambiarEstadoPublicacion(JSONObject mObject) {
        String user_id;
        JSONObject mUserObject;
        Integer reponse_code;
        JSONObject response_bean = new JSONObject();
        puntosDAO puntosdao=new puntosDAO();
        try {
            loginDao mLogin = new loginDao();
            publicacionesDAO mDataBase = new publicacionesDAO();
            mUserObject = mLogin.getUserIDByToken(mObject.get("token").toString());
            reponse_code = Integer.parseInt(mUserObject.get("code").toString());
            if (reponse_code == 0) {
                user_id = mUserObject.get("id").toString();
                response_bean = mDataBase.updateEstadoPublicacion(user_id,
                        mObject.get("idPublicacion").toString(),
                        mObject.get("nuevoEstado").toString());
                Puntos puntosPublicacion=mDataBase.buscarPuntosProfesor(user_id);
                int puntos=0;
                if(puntosPublicacion.getIdPersona() !=-1){
                    puntos=puntosPublicacion.getPuntos();
                }
                 puntos+=5;
                 puntosPublicacion.setPuntos(puntos);
                 puntosPublicacion.setIdPersona(Integer.parseInt(user_id));
                 if(puntosPublicacion.getId()== -1){
                   puntosdao.insetarPuntos(puntosPublicacion);
                 }else{
                   puntosdao.actualizarPuntos(puntosPublicacion);
                }
            }
            else {
                response_bean.put("code", reponse_code);
                response_bean.put("description", mUserObject.get("description").toString());
            }
        } catch(NumberFormatException | SQLException e) {
            System.out.println("Error: " + e);
            response_bean.put("code", 9999);
            response_bean.put("description", "Error en BeanCambiarEstadoPublicacion");
        }
        return response_bean;
    }
}
