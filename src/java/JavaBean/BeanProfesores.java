/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Controller.NubePalabrasServlet;
import Controller.PublicacionesServelet;
import Dao.loginDao;
import Dao.profesoresDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author hp
 */
public class BeanProfesores {
    
    public BeanProfesores() {
	super();
    }
    
    public JSONObject getInfoBasicaProfesoresByToken (String token) {
        
        JSONObject respuesta = new JSONObject();
        JSONObject respuesta_getUserIdByToken = new JSONObject();
        JSONObject respuesta_datos_basicosVO = new JSONObject();
        
        String user_id = "";
        String user_id_departamento = "";
        try {
            loginDao loginDao = new loginDao();
            profesoresDAO profesores_dao = new profesoresDAO();
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
                        
            respuesta_getUserIdByToken = loginDao.getUserIDByToken(token);
            
            if ((int) respuesta_getUserIdByToken.get("code") == 0) {
                user_id = respuesta_getUserIdByToken.get("id").toString();
                respuesta_datos_basicosVO = loginDao.getDatosBasicos(user_id);
                
                if ((int) respuesta_datos_basicosVO.get("code") == 0) {
                    
                    user_id_departamento = respuesta_datos_basicosVO.get("IdDepartamento").toString();                    
                    
                    respuesta = profesores_dao.getInfoBasicaProfesoresByDepartamento(user_id_departamento);
                }
                else
                    respuesta = respuesta_datos_basicosVO;
            }
            else
                respuesta = respuesta_getUserIdByToken;

            System.out.println("Respuesta BeanLogin: "+respuesta);
            return respuesta;        
        }catch (SQLException ex) {
            respuesta.put("code", 9998);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        } catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(NubePalabrasServlet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }
    
    public JSONObject getInfoBasicaProfesoresByDepartamento (String idDepartamento) {
        
        JSONObject respuesta = new JSONObject();
        
        try {
            profesoresDAO profesores_dao = new profesoresDAO();
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
                        
            respuesta = profesores_dao.getInfoBasicaProfesoresByDepartamento(idDepartamento);

            System.out.println("Respuesta BeanLogin: "+respuesta);
            return respuesta;        
        }catch (SQLException ex) {
            respuesta.put("code", 9998);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        } catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(NubePalabrasServlet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }
    
    public JSONObject getInfoBasicaProfesoresByFacultad (String idFacultad) {
        
        JSONObject respuesta = new JSONObject();
        
        try {
            profesoresDAO profesores_dao = new profesoresDAO();
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
                        
            respuesta = profesores_dao.getInfoBasicaProfesoresByFacultad(idFacultad);

            System.out.println("Respuesta BeanLogin: "+respuesta);
            return respuesta;        
        }catch (SQLException ex) {
            respuesta.put("code", 9998);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        } catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(NubePalabrasServlet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }
    
    public JSONObject getInfoBasicaProfesoresByName (String nombre) {
        
        JSONObject respuesta = new JSONObject();
        
        try {
            profesoresDAO profesores_dao = new profesoresDAO();
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
                        
            respuesta = profesores_dao.getInfoBasicaProfesoresByNombre(nombre);

            System.out.println("Respuesta BeanLogin: "+respuesta);
            return respuesta;        
        }catch (SQLException ex) {
            respuesta.put("code", 9998);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        } catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(NubePalabrasServlet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }
    
    public JSONObject getDetalleProfesorById (String idProfesor) {
        
        JSONObject respuesta = new JSONObject();
        JSONObject respuesta_login_medallas = new JSONObject();
        JSONObject respuesta_login_formacionAcademica = new JSONObject();
        JSONObject respuesta_login_areasActuacion = new JSONObject();
        JSONObject respuesta_totalPublicacionesXCategoria = new JSONObject();
        
        try {
            profesoresDAO profesores_dao = new profesoresDAO();
            loginDao login_dao = new loginDao();
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
                        
            respuesta_login_medallas = login_dao.getMedallas(idProfesor);
            
            if ((int)respuesta_login_medallas.get("code")==0) {
                respuesta.put("medallas", respuesta_login_medallas.get("medallas"));
                respuesta_login_formacionAcademica = login_dao.getFormacionAcademica(idProfesor);
                
                if ((int)respuesta_login_formacionAcademica.get("code")==0) {
                    respuesta.put("formacionAcademica", respuesta_login_formacionAcademica.get("formacionAcademica"));
                    respuesta_login_areasActuacion =login_dao.getAreasActuacion(idProfesor);
                    
                    if ((int)respuesta_login_areasActuacion.get("code")==0) {
                        respuesta.put("areasActuacion", respuesta_login_areasActuacion.get("areasActuacion"));   
                        respuesta_totalPublicacionesXCategoria = profesores_dao.getNumeroPublicacionesXCategoriaByProfesorId(idProfesor);
                        
                        if ((int)respuesta_totalPublicacionesXCategoria.get("code") == 0)
                            respuesta.put("totalPublicaciones", respuesta_totalPublicacionesXCategoria.get("totalPublicaciones"));
                        else {
                            respuesta.put("code",respuesta_totalPublicacionesXCategoria.get("code"));
                            respuesta.put("description", respuesta_totalPublicacionesXCategoria.get("description")); 
                        }
                    }
                    else {
                        respuesta.put("code",respuesta_login_areasActuacion.get("code"));
                        respuesta.put("description", respuesta_login_areasActuacion.get("description"));
                    }
                }
                else {
                    respuesta.put("code", respuesta_login_formacionAcademica.get("code"));
                    respuesta.put("description", respuesta_login_formacionAcademica.get("description"));
                }
            }
            else {
                respuesta.put("code", respuesta_login_formacionAcademica.get("code"));
                respuesta.put("description", respuesta_login_formacionAcademica.get("description"));
            }

            System.out.println("Respuesta BeanLogin: "+respuesta);
            return respuesta;        
        }catch (SQLException ex) {
            respuesta.put("code", 9998);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(PublicacionesServelet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        } catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("Respuesta BeanLogin: "+respuesta);
            Logger.getLogger(NubePalabrasServlet.class.getName()).log(Level.SEVERE, null, ex);
            return respuesta;
        }
    }
    
}
