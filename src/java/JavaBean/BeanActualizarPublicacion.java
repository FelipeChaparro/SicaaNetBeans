/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.publicacionesDAO;
import Dao.puntosDAO;
import Entidad.Puntos;
import java.sql.SQLException;
import org.json.simple.JSONObject;

/**
 *
 * @author juanchaparro
 */
public class BeanActualizarPublicacion {
    public BeanActualizarPublicacion() {
        super();
    }
    
    public JSONObject actualizarPublicacion(JSONObject mNewData) {
        JSONObject respuesta_bean = new JSONObject();
        puntosDAO puntosdao=new puntosDAO();
        try {
            publicacionesDAO mDataBase = new publicacionesDAO();
            respuesta_bean = mDataBase.actualizarPublicacion(mNewData);
            Puntos puntosPublicacion=mDataBase.buscarPuntosProfesor((String) mNewData.get("ID"));
            int puntos=0;
                if(puntosPublicacion.getIdPersona() !=-1){
                    puntos=puntosPublicacion.getPuntos();
                }
                 puntos+=5;
                 puntosPublicacion.setPuntos(puntos);
                 puntosPublicacion.setIdPersona(Integer.parseInt((String) mNewData.get("ID")));
                 if(puntosPublicacion.getId()== -1){
                   puntosdao.insetarPuntos(puntosPublicacion);
                 }else{
                   puntosdao.actualizarPuntos(puntosPublicacion);
                }
        } catch(SQLException e) {
            System.out.println("Error: " + e);
        } 
        return respuesta_bean;
    }
}
