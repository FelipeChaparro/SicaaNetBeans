/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import Dao.loginDao;
import Dao.publicacionesDAO;
import Dao.puntosDAO;
import Entidad.Puntos;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
        
/**
 *
 * @author juanchaparro
 */
public class BeanGuardarPublicacion {
    
    public JSONObject guardarPublicacionDB(String myJson) throws ParseException {
        loginDao myLogin = new loginDao();
        publicacionesDAO myPublicacion = new publicacionesDAO();
        puntosDAO puntosdao=new puntosDAO();
        JSONObject respuestaBean = new JSONObject();
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(myJson);
            JSONObject publicacion = (JSONObject) json.get("publicacion");
            System.out.println(publicacion);
            String token = json.get("token").toString();
            JSONObject objUserID = myLogin.getUserIDByToken(token);
            if (Integer.parseInt(objUserID.get("code").toString()) == 0) {
                respuestaBean = myPublicacion.insertPublicacion(objUserID.get("id").toString(), publicacion, "Verificado");
                Puntos puntosPublicacion=myPublicacion.buscarPuntosProfesor((String) objUserID.get("id"));
                
                int puntos=0;
                if(puntosPublicacion.getIdPersona() !=-1){
                    puntos=puntosPublicacion.getPuntos();
                }
                 puntos+=20;
                 puntosPublicacion.setPuntos(puntos);
                 puntosPublicacion.setIdPersona(Integer.parseInt((String) objUserID.get("id")));
                 if(puntosPublicacion.getId()== -1){
                   puntosdao.insetarPuntos(puntosPublicacion);
                 }else{
                   puntosdao.actualizarPuntos(puntosPublicacion);
                }
            }
            else {
                respuestaBean.put("code", 101);
                respuestaBean.put("description", "Error en token");
            }
            
        } catch (SQLException ex) {
            respuestaBean.put("code", 9999);
            respuestaBean.put("description", "Error en base de datos");
            System.out.println("guardarPublicacionDB: " + respuestaBean.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuestaBean;
    }
}
