/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Controller.login_servelet;
import Dao.loginDao;
import Dao.parametrosSistemaDao;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;


/**
 *
 * @author hariasor
 */
public class BeanLogin {
    
    public BeanLogin() {
	super();
    }
    
    public JSONObject loginInByMail (JSONObject user_data){
        System.out.println("BeanLogin - loginInByMail - Data: "+user_data.toJSONString());
        JSONObject respuesta_parametros_sistemaVO = new JSONObject();
        
        JSONObject respuesta_loginVO = new JSONObject();
        JSONObject respuesta_generateTokenVO = new JSONObject();
        JSONObject respuesta_saveTokenVO = new JSONObject();
        JSONObject respuesta_roles_usuariosVO = new JSONObject();
        JSONObject respuesta_roles_completosVO = new JSONObject();
        JSONObject respuesta_datos_basicosVO = new JSONObject();
        JSONObject respuesta_medallasVO = new JSONObject();
        JSONObject respuesta_formacion_academicaVO = new JSONObject();
        JSONObject respuesta_publicaciones_recientesVO = new JSONObject();
        JSONObject respuesta_podio_publicacionesVO = new JSONObject();
        JSONObject respuesta_areas_actuacionVO = new JSONObject();

        String user_id = "";
        String user_id_departamento = "";
        JSONObject parametros = new JSONObject();
                
        try {
                        
            String SERVER_URL = user_data.get("server_url").toString();
            
            loginDao loginDao = new loginDao();
            parametrosSistemaDao parametrosDao = new parametrosSistemaDao();
            
            respuesta_parametros_sistemaVO = parametrosDao.getParametrosSistema();
            
            if (Integer.parseInt(respuesta_parametros_sistemaVO.get("code").toString()) == 0) {
                parametros = (JSONObject)respuesta_parametros_sistemaVO.get("parametros");
                /**
                * Login en base de datos
                */
                respuesta_loginVO = loginDao.login_user(user_data.get("user_name").toString(),user_data.get("user_pass").toString());
                
                /**
                 * Si logeo con exito genero token
                 */
                if (Integer.parseInt(respuesta_loginVO.get("code").toString()) == 0) {
                    user_id = respuesta_loginVO.get("id").toString();
                    respuesta_loginVO.remove("id");
                    //Genneracion de token
                    respuesta_generateTokenVO = generateTokenByMail(user_data.get("user_name").toString());
                    /**
                     * Si generaicon de token con exito lo guardo
                     */
                    if (Integer.parseInt(respuesta_generateTokenVO.get("code").toString()) == 0) {
                        System.out.println("Token: "+respuesta_generateTokenVO.get("token"));
                        respuesta_loginVO.put("token", respuesta_generateTokenVO.get("token"));

                        //Guardar token en sesion de usuario
                        respuesta_saveTokenVO = loginDao.save_token(user_data.get("user_name").toString(),respuesta_loginVO.get("token").toString());

                        respuesta_loginVO.put("code", respuesta_saveTokenVO.get("code"));
                        respuesta_loginVO.put("description", respuesta_saveTokenVO.get("description"));
                        /**
                         * Si se guarda token con exito se traen los roles del usuario
                         */
                        if (Integer.parseInt(respuesta_saveTokenVO.get("code").toString()) == 0) {
                            respuesta_roles_usuariosVO = loginDao.getRolesByUserMail(user_id);
                            /**
                             * Si trae roles con exito busco en tabla de roles el label y la referencia
                             */
                            if (Integer.parseInt(respuesta_roles_usuariosVO.get("code").toString()) == 0) {
                                respuesta_roles_completosVO = loginDao.getRoleDescriptionByID(respuesta_roles_usuariosVO,SERVER_URL);
                                /**
                                 * Si trae descripcion de roles completo busco datos de usuario
                                 */
                                if (Integer.parseInt(respuesta_roles_completosVO.get("code").toString()) == 0) {
                                    respuesta_loginVO.put("roles", respuesta_roles_completosVO.get("roles"));
                                    respuesta_datos_basicosVO = loginDao.getDatosBasicos(user_id);

                                    /**
                                     *Si trae datos basicos busco medallas 
                                     */
                                    if (Integer.parseInt(respuesta_datos_basicosVO.get("code").toString()) == 0) {
                                        user_id_departamento = respuesta_datos_basicosVO.get("IdDepartamento").toString();
                                        respuesta_loginVO.put("datosBasicos", respuesta_datos_basicosVO.get("datosBasicos"));                                    
                                        respuesta_medallasVO = loginDao.getMedallas(user_id);

                                        /**
                                         * Si trae medallas busco formacion academica
                                         */
                                        if (Integer.parseInt(respuesta_medallasVO.get("code").toString()) == 0) {
                                            respuesta_loginVO.put("medallas", respuesta_medallasVO.get("medallas"));
                                            respuesta_formacion_academicaVO = loginDao.getFormacionAcademica(user_id);

                                            /**
                                            * Si trae formacion academica busco formacion academica
                                            */
                                            if (Integer.parseInt(respuesta_formacion_academicaVO.get("code").toString()) == 0) {
                                                respuesta_loginVO.put("formacionAcademica", respuesta_formacion_academicaVO.get("formacionAcademica"));
                                                respuesta_publicaciones_recientesVO = loginDao.getPublicacionesRecientes(user_id,Integer.parseInt(parametros.get("numero_publicaciones_recientes").toString()));
                                                
                                                /**
                                                * Si trae publicaciones recientes busco publicaciones recientes
                                                */
                                                if (Integer.parseInt(respuesta_publicaciones_recientesVO.get("code").toString()) == 0) {
                                                    respuesta_loginVO.put("publicacionesRecientes", respuesta_publicaciones_recientesVO.get("publicacionesRecientes"));
                                                    respuesta_areas_actuacionVO = loginDao.getAreasActuacion(user_id);
                                                    
                                                    if (Integer.parseInt(respuesta_areas_actuacionVO.get("code").toString()) == 0) {
                                                        respuesta_loginVO.put("areasActuacion", respuesta_areas_actuacionVO.get("areasActuacion"));
                                                        respuesta_podio_publicacionesVO = loginDao.getPodioPublicaciones(Integer.parseInt(parametros.get("numero_podio_publicaciones").toString()),user_id_departamento);
                                                        
                                                        if (Integer.parseInt(respuesta_podio_publicacionesVO.get("code").toString()) == 0) {
                                                            respuesta_loginVO.put("podioPublicacionPrograma", respuesta_podio_publicacionesVO.get("podioPublicacionPrograma"));
                                                            respuesta_loginVO.put("podioPublicacionFacultad", respuesta_podio_publicacionesVO.get("podioPublicacionFacultad"));
                                                            respuesta_loginVO.put("podioPublicacionUniversidad", respuesta_podio_publicacionesVO.get("podioPublicacionUniversidad"));
                                                        }
                                                        else {
                                                            respuesta_loginVO.put("code", respuesta_podio_publicacionesVO.get("code"));
                                                            respuesta_loginVO.put("description", respuesta_podio_publicacionesVO.get("description"));
                                                        }                                                    
                                                    }
                                                    else {
                                                        respuesta_loginVO.put("code", respuesta_areas_actuacionVO.get("code"));
                                                        respuesta_loginVO.put("description", respuesta_areas_actuacionVO.get("description"));
                                                    }
                                                }
                                                else {
                                                    respuesta_loginVO.put("code", respuesta_publicaciones_recientesVO.get("code"));
                                                    respuesta_loginVO.put("description", respuesta_publicaciones_recientesVO.get("description"));
                                                }
                                                
                                            }
                                            else {
                                                respuesta_loginVO.put("code", respuesta_formacion_academicaVO.get("code"));
                                                respuesta_loginVO.put("description", respuesta_formacion_academicaVO.get("description"));
                                            }

                                        }
                                        else {
                                            respuesta_loginVO.put("code", respuesta_medallasVO.get("code"));
                                            respuesta_loginVO.put("description", respuesta_medallasVO.get("description"));
                                        }

                                    }
                                    else {
                                        respuesta_loginVO.put("code", respuesta_datos_basicosVO.get("code"));
                                        respuesta_loginVO.put("description", respuesta_datos_basicosVO.get("description"));
                                    }
                                }
                                else {
                                    respuesta_loginVO.put("code", respuesta_roles_completosVO.get("code"));
                                    respuesta_loginVO.put("description", respuesta_roles_completosVO.get("description"));
                                }
                            }
                            else {
                                respuesta_loginVO.put("code", respuesta_roles_usuariosVO.get("code"));
                                respuesta_loginVO.put("description", respuesta_roles_usuariosVO.get("description"));
                            }
                        }
                        else {
                            respuesta_loginVO.put("code", respuesta_saveTokenVO.get("code"));
                            respuesta_loginVO.put("description", respuesta_saveTokenVO.get("description"));
                        }
                    }
                    else {
                        respuesta_loginVO.put("code", respuesta_generateTokenVO.get("code"));
                        respuesta_loginVO.put("description", respuesta_generateTokenVO.get("description"));
                    }
                }
                else {
                    respuesta_loginVO.put("code", respuesta_loginVO.get("code"));
                    respuesta_loginVO.put("description", respuesta_loginVO.get("description"));
                }
            }
            else {
                respuesta_loginVO.put("code", respuesta_parametros_sistemaVO.get("code"));
                respuesta_loginVO.put("description", respuesta_parametros_sistemaVO.get("description"));
            }
           
            System.out.println("Respuesta BeanLogin: "+respuesta_loginVO);
                           
        } catch (SQLException ex) {
            respuesta_loginVO.put("code", 9999);
            respuesta_loginVO.put("description", "Error en base de datos");
            System.out.println("retrno: "+respuesta_loginVO);      
            Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (Exception ex) {
            respuesta_loginVO.put("code", 9997);
            respuesta_loginVO.put("description", "Error de sistema");
            System.out.println("retrno: "+respuesta_loginVO);
            Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta_loginVO;
    }
    
    public JSONObject cargarInformacionBasicaByToken (JSONObject user_data){
        System.out.println("BeanLogin - cargarInformacionBasicaByToken - Data: "+user_data.toJSONString());
        JSONObject respuesta_parametros_sistemaVO = new JSONObject();
        
        JSONObject respuesta_cargarInformacionBasicaVO = new JSONObject();
        JSONObject respuesta_getUserIdByToken = new JSONObject();
        JSONObject respuesta_roles_usuariosVO = new JSONObject();
        JSONObject respuesta_roles_completosVO = new JSONObject();
        JSONObject respuesta_datos_basicosVO = new JSONObject();
        JSONObject respuesta_medallasVO = new JSONObject();
        JSONObject respuesta_formacion_academicaVO = new JSONObject();
        JSONObject respuesta_publicaciones_recientesVO = new JSONObject();
        JSONObject respuesta_podio_publicacionesVO = new JSONObject();
        JSONObject respuesta_areas_actuacionVO = new JSONObject();

        String user_id = "";
        String user_id_departamento = "";
        JSONObject parametros = new JSONObject();
                
        try {
                        
            String SERVER_URL = user_data.get("server_url").toString();
            
            loginDao loginDao = new loginDao();
            parametrosSistemaDao parametrosDao = new parametrosSistemaDao();
            
            respuesta_parametros_sistemaVO = parametrosDao.getParametrosSistema();
            
            if (Integer.parseInt(respuesta_parametros_sistemaVO.get("code").toString()) == 0) {
                parametros = (JSONObject)respuesta_parametros_sistemaVO.get("parametros");
                
                respuesta_getUserIdByToken = loginDao.getUserIDByToken(user_data.get("token").toString());
                
                if ((int) respuesta_getUserIdByToken.get("code") == 0) {
                    
                    respuesta_cargarInformacionBasicaVO.put("code", respuesta_getUserIdByToken.get("code"));
                    respuesta_cargarInformacionBasicaVO.put("description", respuesta_getUserIdByToken.get("description"));
                    respuesta_cargarInformacionBasicaVO.put("token", user_data.get("token").toString());
                    
                    user_id = respuesta_getUserIdByToken.get("id").toString();

                    respuesta_roles_usuariosVO = loginDao.getRolesByUserMail(user_id);
                    
                    /**
                     * Si trae roles con exito busco en tabla de roles el label y la referencia
                     */
                    if (Integer.parseInt(respuesta_roles_usuariosVO.get("code").toString()) == 0) {
                        respuesta_roles_completosVO = loginDao.getRoleDescriptionByID(respuesta_roles_usuariosVO,SERVER_URL);
                        /**
                         * Si trae descripcion de roles completo busco datos de usuario
                         */
                        if (Integer.parseInt(respuesta_roles_completosVO.get("code").toString()) == 0) {
                            respuesta_cargarInformacionBasicaVO.put("roles", respuesta_roles_completosVO.get("roles"));
                            respuesta_datos_basicosVO = loginDao.getDatosBasicos(user_id);

                            /**
                             *Si trae datos basicos busco medallas 
                             */
                            if (Integer.parseInt(respuesta_datos_basicosVO.get("code").toString()) == 0) {
                                user_id_departamento = respuesta_datos_basicosVO.get("IdDepartamento").toString();
                                respuesta_cargarInformacionBasicaVO.put("datosBasicos", respuesta_datos_basicosVO.get("datosBasicos"));                                    
                                respuesta_medallasVO = loginDao.getMedallas(user_id);

                                /**
                                 * Si trae medallas busco formacion academica
                                 */
                                if (Integer.parseInt(respuesta_medallasVO.get("code").toString()) == 0) {
                                    respuesta_cargarInformacionBasicaVO.put("medallas", respuesta_medallasVO.get("medallas"));
                                    respuesta_formacion_academicaVO = loginDao.getFormacionAcademica(user_id);

                                    /**
                                    * Si trae formacion academica busco formacion academica
                                    */
                                    if (Integer.parseInt(respuesta_formacion_academicaVO.get("code").toString()) == 0) {
                                        respuesta_cargarInformacionBasicaVO.put("formacionAcademica", respuesta_formacion_academicaVO.get("formacionAcademica"));
                                        respuesta_publicaciones_recientesVO = loginDao.getPublicacionesRecientes(user_id,Integer.parseInt(parametros.get("numero_publicaciones_recientes").toString()));

                                        /**
                                        * Si trae publicaciones recientes busco publicaciones recientes
                                        */
                                        if (Integer.parseInt(respuesta_publicaciones_recientesVO.get("code").toString()) == 0) {
                                            respuesta_cargarInformacionBasicaVO.put("publicacionesRecientes", respuesta_publicaciones_recientesVO.get("publicacionesRecientes"));
                                            respuesta_areas_actuacionVO = loginDao.getAreasActuacion(user_id);

                                            if (Integer.parseInt(respuesta_areas_actuacionVO.get("code").toString()) == 0) {
                                                respuesta_cargarInformacionBasicaVO.put("areasActuacion", respuesta_areas_actuacionVO.get("areasActuacion"));
                                                respuesta_podio_publicacionesVO = loginDao.getPodioPublicaciones(Integer.parseInt(parametros.get("numero_podio_publicaciones").toString()),user_id_departamento);

                                                if (Integer.parseInt(respuesta_podio_publicacionesVO.get("code").toString()) == 0) {
                                                    respuesta_cargarInformacionBasicaVO.put("podioPublicacionPrograma", respuesta_podio_publicacionesVO.get("podioPublicacionPrograma"));
                                                    respuesta_cargarInformacionBasicaVO.put("podioPublicacionFacultad", respuesta_podio_publicacionesVO.get("podioPublicacionFacultad"));
                                                    respuesta_cargarInformacionBasicaVO.put("podioPublicacionUniversidad", respuesta_podio_publicacionesVO.get("podioPublicacionUniversidad"));
                                                }
                                                else {
                                                    respuesta_cargarInformacionBasicaVO.put("code", respuesta_podio_publicacionesVO.get("code"));
                                                    respuesta_cargarInformacionBasicaVO.put("description", respuesta_podio_publicacionesVO.get("description"));
                                                }                                                    
                                            }
                                            else {
                                                respuesta_cargarInformacionBasicaVO.put("code", respuesta_areas_actuacionVO.get("code"));
                                                respuesta_cargarInformacionBasicaVO.put("description", respuesta_areas_actuacionVO.get("description"));
                                            }
                                        }
                                        else {
                                            respuesta_cargarInformacionBasicaVO.put("code", respuesta_publicaciones_recientesVO.get("code"));
                                            respuesta_cargarInformacionBasicaVO.put("description", respuesta_publicaciones_recientesVO.get("description"));
                                        }

                                    }
                                    else {
                                        respuesta_cargarInformacionBasicaVO.put("code", respuesta_formacion_academicaVO.get("code"));
                                        respuesta_cargarInformacionBasicaVO.put("description", respuesta_formacion_academicaVO.get("description"));
                                    }

                                }
                                else {
                                    respuesta_cargarInformacionBasicaVO.put("code", respuesta_medallasVO.get("code"));
                                    respuesta_cargarInformacionBasicaVO.put("description", respuesta_medallasVO.get("description"));
                                }

                            }
                            else {
                                respuesta_cargarInformacionBasicaVO.put("code", respuesta_datos_basicosVO.get("code"));
                                respuesta_cargarInformacionBasicaVO.put("description", respuesta_datos_basicosVO.get("description"));
                            }
                        }
                        else {
                            respuesta_cargarInformacionBasicaVO.put("code", respuesta_roles_completosVO.get("code"));
                            respuesta_cargarInformacionBasicaVO.put("description", respuesta_roles_completosVO.get("description"));
                        }
                    }
                    else {
                        respuesta_cargarInformacionBasicaVO.put("code", respuesta_roles_usuariosVO.get("code"));
                        respuesta_cargarInformacionBasicaVO.put("description", respuesta_roles_usuariosVO.get("description"));
                    }
                    
                }
                else {
                    respuesta_cargarInformacionBasicaVO.put("code", respuesta_cargarInformacionBasicaVO.get("code"));
                    respuesta_cargarInformacionBasicaVO.put("description", respuesta_cargarInformacionBasicaVO.get("description"));
                }
            }
            else {
                respuesta_cargarInformacionBasicaVO.put("code", respuesta_parametros_sistemaVO.get("code"));
                respuesta_cargarInformacionBasicaVO.put("description", respuesta_parametros_sistemaVO.get("description"));
            }
           
            System.out.println("Respuesta BeanLogin: "+respuesta_cargarInformacionBasicaVO);
                           
        } catch (SQLException ex) {
            respuesta_cargarInformacionBasicaVO.put("code", 9999);
            respuesta_cargarInformacionBasicaVO.put("description", "Error en base de datos");
            System.out.println("retrno: "+respuesta_cargarInformacionBasicaVO);      
            Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (Exception ex) {
            respuesta_cargarInformacionBasicaVO.put("code", 9997);
            respuesta_cargarInformacionBasicaVO.put("description", "Error de sistema");
            System.out.println("retrno: "+respuesta_cargarInformacionBasicaVO);
            Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta_cargarInformacionBasicaVO;
    }
    
    /**
     * Genera el token unico de usuario
     * @param user_name
     * @return 
     */
    private static JSONObject generateTokenByMail(String user_name) {
        JSONObject respuesta_generate_token = new JSONObject();        
        int hash = 0;
        int i = 0;
        String token ="";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();            
        String s = user_name+dateFormat.format(date);
        System.out.println("s: "+s);
        while (i < 10 && i<s.length()) {
            token += String.valueOf((hash*31 + s.charAt(i)));
            i++;
        }
        respuesta_generate_token.put("code", 0);
        respuesta_generate_token.put("description", "Operacion exitosa");
        respuesta_generate_token.put("token",token);
        return respuesta_generate_token;
    }

}
