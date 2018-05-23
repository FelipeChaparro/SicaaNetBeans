/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Controller.login_servelet;
import Dao.autoresDao;
import Dao.loginDao;

import Dao.publicacionesDAO;
import Dao.puntosDAO;
import Entidad.Autor;
import Entidad.Puntos;
import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hariasor
 */
public class BeanPublicaciones {
    
    public BeanPublicaciones() {
	super();
    }
    
    public JSONObject validarPublicacionesNuevas (JSONArray publicacionesNuevas, String profesorId,String fuenteExtraccion) {
        publicacionesDAO INEG1 = new publicacionesDAO();
        autoresDao bean = new autoresDao();
        puntosDAO puntosdao=new puntosDAO();
       
        JSONObject respuesta_getAllPublicacionesByPersonaID = new JSONObject();
        JSONObject respuesta_existePublicacion = new JSONObject();
        JSONObject respuesta_findPublicacionDudosaByTitulo = new JSONObject();
        JSONObject respuesta_insertPublicacionDudosa = new JSONObject();
        JSONObject respuesta_insertarPublicacionNueva = new JSONObject();
        List<Autor> respuesta_getAllAutoresinDB = new ArrayList<Autor>();
        
        JSONObject respuesta_comparacionPublicacionConLista = new JSONObject();
        
        
        Puntos puntosPublicacion=INEG1.buscarPuntosProfesor(profesorId);
        int puntos=0;
        if(puntosPublicacion.getId()== -1){
            puntos=200;
        }else{
            puntos=puntosPublicacion.getPuntos();
        }
        
        try {
                       
            respuesta_existePublicacion.put("code", 0);
            respuesta_existePublicacion.put("description", "Operacion exitosa");
            respuesta_existePublicacion.put("insertar",3);

            respuesta_getAllPublicacionesByPersonaID = INEG1.getAllPublicacionesByPersonaID(profesorId);
            //respuesta_getAllAutoresinDB = bean.getAllAutoresinDB();
                        
            if (Integer.parseInt(respuesta_getAllPublicacionesByPersonaID.get("code").toString()) == 0) {
                                
                JSONArray publicacionesSistemaDeUsuario = (JSONArray) respuesta_getAllPublicacionesByPersonaID.get("publicaciones");
                                                
                for (int i=0;i<publicacionesNuevas.size();i++) {
                    JSONObject publicacionIndividualNueva = (JSONObject) (publicacionesNuevas.get(i));
                    
                    /**
                     * COMPARAR CADA PUBLICACION NUEVA CON LAS DEL AUTOR
                     */
                    System.out.println("Titulo nuevo: "+publicacionIndividualNueva.get("titulo"));
                    //System.out.println("Titulo sistema: "+((JSONObject) publicacionesSistemaDeUsuario.get(i)).get("Titulo"));
                    respuesta_comparacionPublicacionConLista = comparacionPublicacionConLista(publicacionIndividualNueva, publicacionesSistemaDeUsuario);
                    
                    /**
                     * 0: Publicacion ya existe 
                     */
                    System.out.println("Valor: "+respuesta_comparacionPublicacionConLista.get("codigoEvaluacion").toString());
                    if (Integer.parseInt(respuesta_comparacionPublicacionConLista.get("codigoEvaluacion").toString()) == 0) {
                        System.out.println("YA EXISTE");
                    }
                    /**
                     * 1: La nueva publicacion es dudosa de publicaciones existentes
                     */
                    else if (Integer.parseInt(respuesta_comparacionPublicacionConLista.get("codigoEvaluacion").toString()) == 1) {
                        respuesta_findPublicacionDudosaByTitulo = INEG1.findPublicacionDudosaByTitulo(publicacionIndividualNueva.get("titulo").toString());
                        
                        if ((boolean) respuesta_findPublicacionDudosaByTitulo.get("existeTitulo") == false)  {
                            /**
                             * INSERTAR DUDOSA
                             */
                            System.out.println("INSERTAR DUDOSAS");
                            respuesta_insertPublicacionDudosa = INEG1.insertPublicacionDudosa(publicacionIndividualNueva, (JSONArray) respuesta_comparacionPublicacionConLista.get("publicacionesDudosas"),fuenteExtraccion);
                            
                            if ((int) respuesta_insertPublicacionDudosa.get("code")==0 && ((JSONArray) respuesta_comparacionPublicacionConLista.get("publicacionesDudosas")).size()>0) {
                                String idPublicaiconXCambiarEstado = ((JSONObject)((JSONArray) respuesta_comparacionPublicacionConLista.get("publicacionesDudosas")).get(0)).get("ID").toString();
                                respuesta_insertPublicacionDudosa = INEG1.changeStateXDudosa(idPublicaiconXCambiarEstado);
                            }
                            
                        }
                        else
                            System.out.println("YA EXISTE DUDOSAS"); 
                    }
                    /**
                     * 2: Ninguna publicacion coincidio -> REVISAR CO-AUTORES -> INSERTAR
                     */
                    else if (Integer.parseInt(respuesta_comparacionPublicacionConLista.get("codigoEvaluacion").toString()) == 2) {
                        System.out.println("REVISAR CO-AUOTRES - INSERTAR");
                        
                        JSONArray autores=(JSONArray) publicacionIndividualNueva.get("autor");
                        String codigos="";
                        JSONArray autoresInsertar = new JSONArray();
                        for(int k=0; k < autores.size();k++){
                            JSONObject autorN = (JSONObject) autores.get(k);
                            String nombreA=(String) autorN.get("nombre");
                            System.out.println("Nombre: "+nombreA);
                            List<Autor> autores2 = bean.busquedaAutorBD(nombreA);
                           
                            int con=0;
                            for (Autor a : autores2){
                                JSONObject obje=new JSONObject();
                                System.out.println("el is es :"+ a.getId() +"el nombre es:"+a.getNombre());
                                obje.put("nombre",a.getNombre());
                                obje.put("id",a.getId());
                                autoresInsertar.add(obje);
                                if(con==0){
                                   codigos=""+a.getId();
                                }else{
                                    codigos=codigos+","+a.getId(); 
                                }
                                con++;
                            }
                            
                            if(autores2.size()==0){
                                JSONObject obje=new JSONObject();
                                obje.put("nombre",nombreA);
                                obje.put("id",-1);
                                autoresInsertar.add(obje);                            
                            }                            
                        }
                        
                        int ExistePublicacion= -1;
                          
                        if(!codigos.equals("")){
                            System.out.println("Nueva publicaicon: "+publicacionIndividualNueva.toString());
                            String codigoReferencia="";
                            if(publicacionIndividualNueva.get("codigoPublicacion") != null){
                                codigoReferencia=(String) publicacionIndividualNueva.get("codigoPublicacion");
                            }
                            ExistePublicacion=INEG1.idPublicacionxAutores(codigos, (String) publicacionIndividualNueva.get("titulo"), codigoReferencia);
                            if(ExistePublicacion != -1){
                                System.out.println("Relacionar la publicacion con id"+ExistePublicacion);
                                respuesta_insertarPublicacionNueva = INEG1.insertReferenciaPublicacion(profesorId, ExistePublicacion);
                            }
                            else{
                                System.out.println("Titulo insertar: "+publicacionIndividualNueva.get("titulo"));;
                                System.out.println("Tipo insertar: "+publicacionIndividualNueva.get("tipo"));
                                respuesta_insertarPublicacionNueva = INEG1.insertPublicacionAutomatica(profesorId, publicacionIndividualNueva, autoresInsertar,fuenteExtraccion);
                                if(puntosPublicacion.getId() != -1){
                                    puntos+=20;
                                }
                                System.out.println("los autores que debe insertar son"+ autoresInsertar.toString());
                            }
                        }
                        else {
                            System.out.println("Tipo insertar: "+publicacionIndividualNueva.get("tipo"));
                            respuesta_insertarPublicacionNueva = INEG1.insertPublicacionAutomatica(profesorId, publicacionIndividualNueva, autoresInsertar,fuenteExtraccion);
                            if(puntosPublicacion.getId() != -1){
                                puntos+=20;
                            }
                            System.out.println("los autores que debe insertar son: "+ autoresInsertar.toString());
                        }
                    }
                    
                }              
            }
            else {
                respuesta_existePublicacion.put("code", Integer.parseInt(respuesta_getAllPublicacionesByPersonaID.get("code").toString()));
                respuesta_existePublicacion.put("description", Integer.parseInt(respuesta_getAllPublicacionesByPersonaID.get("description").toString()));
            }  

        } catch (SQLException ex) {
            respuesta_existePublicacion.put("code", 9999);
            respuesta_existePublicacion.put("description", "Error en base de datos");
            System.out.println("existePublicacion(): "+respuesta_existePublicacion);      
            Logger.getLogger(login_servelet.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
             
        }
        puntosPublicacion.setPuntos(puntos);
        puntosPublicacion.setIdPersona(Integer.parseInt(profesorId));
        
       if(puntosPublicacion.getId()== -1){
           puntosdao.insetarPuntos(puntosPublicacion);
        }else{
           puntosdao.actualizarPuntos(puntosPublicacion);
        }
        
        return respuesta_existePublicacion;
    }
    
    private List<Autor> concordanciaCoautorNuevoConSistema (String nombreCoautorNuevo, List<Autor> listaAutoresSistema) {
        
        BeanConcordancia bean= new BeanConcordancia();
        List<Autor> coincidencias = new ArrayList<Autor>();

        try {
            
            boolean esta=false;
            for (int i=0;i<listaAutoresSistema.size() && !esta;i++){
               Autor autor= new Autor();
               String nombre = listaAutoresSistema.get(i).getNombre();
               autor.setNombre(nombre);
               int id = listaAutoresSistema.get(i).getId();
               autor.setId(id);
               double porcentaje=bean.getSimilarity(nombre, nombreCoautorNuevo, "NOMBRE");
               double valor=0.90;
               double valorMax=1.0;
               if(valorMax == porcentaje){
                   coincidencias.clear();
                   coincidencias.add(autor);
                   esta=true;
               }
               else if(porcentaje >=  valor){
                   coincidencias.add(autor);
               }
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(autoresDao.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        return coincidencias;
    }
    
    private JSONObject comparacionPublicacionConLista (JSONObject publicacionIndividual, JSONArray listaPublicaciones) {
        
        BeanConcordancia beanConcordancia = new BeanConcordancia();
        
        JSONObject respuesta = new JSONObject();
        Double porcentajeConfianza = 0.0;
        JSONObject publicacionIndividualDeLista = new JSONObject();
        String codigoPublicacionExistente = "";
        String codigoPublicacionSistema = "";
        JSONArray publicacionesDudosas = new JSONArray();
        
        int tamListaPublicaciones = listaPublicaciones.size();
       
        for (int i=0;i<tamListaPublicaciones;i++) {
            
            publicacionIndividualDeLista = (JSONObject)listaPublicaciones.get(i);
            
            //System.out.println("Publicacion individual: "+publicacionIndividual.toJSONString());
            //System.out.println("Publciaion de lista: "+publicacionIndividualDeLista.toJSONString());
            porcentajeConfianza = beanConcordancia.getSimilarity(publicacionIndividual.get("titulo").toString(), publicacionIndividualDeLista.get("Titulo").toString(), "NOMBRE_PUBLICACION");
            
            if (porcentajeConfianza >= 0.98) {
                /** 
                 * SALE - NO SE INSERTA PORQUE YA EXISTE
                 */
                
                respuesta.put("codigoEvaluacion", 0);
                respuesta.put("publicacionExistente", publicacionIndividualDeLista);
                System.out.println("comparacionPublicacionConLista() - codigoEvaluacion: "+respuesta.get("codigoEvaluacion").toString() + " - IGUAL");
                return respuesta;
            }
            else if (porcentajeConfianza > 0.90) {
                
                if (publicacionIndividual.get("codigoPublicacion") != null && publicacionIndividualDeLista.get("codigoPublicacion") != null) {
                    codigoPublicacionExistente = publicacionIndividual.get("codigoPublicacion").toString().trim().replace("-","").replace("/","").replace(".","").replace(" ","");
                    codigoPublicacionSistema = publicacionIndividualDeLista.get("codigoPublicacion").toString().trim().replace("-","").replace("/","").replace(".","").replace(" ","");
                    
                    if (codigoPublicacionExistente.equalsIgnoreCase(codigoPublicacionSistema)) {
                        /**
                         * SALE - NO SE INSERTA PORQUE YA EXISTE
                         */
                        respuesta.put("codigoEvaluacion", 0);
                        respuesta.put("publicacionExistente", publicacionIndividualDeLista);
                        System.out.println("comparacionPublicacionConLista() - codigoEvaluacion: "+respuesta.get("codigoEvaluacion").toString()+ " - ISBN IGUAL");
                        return respuesta;
                    }
                }
                else {
                    /**
                     * PUBLICACION NUEVA ES DUDOSA DE ESTAS PUBLICACIONES DE SISTEMA
                     */
                    /**
                     * Si la publicacion esta en veriicada 
                     */
                    if (!publicacionIndividualDeLista.get("EstadoPublicacion").toString().equalsIgnoreCase("Verificado"))
                        publicacionesDudosas.add(publicacionIndividualDeLista);
                }
            }
        }
        
        if (publicacionesDudosas.size() > 0) {
            /**
             * EXISTEN VARIAS DUDOSAS
             */
            respuesta.put("codigoEvaluacion", 1);
            respuesta.put("publicacionesDudosas", publicacionesDudosas);
        }
        else {
            /**
             * NINGUNA PUBLICACION COINCIDIO -> REVISAR CO-AUTORES E INSERTAR
             */
            respuesta.put("codigoEvaluacion", 2);
        }
        
        System.out.println("comparacionPublicacionConLista() - codigoEvaluacion: "+respuesta.get("codigoEvaluacion").toString());
        return respuesta;
    }
    
    public JSONObject getAllPublicacionesByPersonaID (String personaID,String action) {
        JSONObject respuesta = new JSONObject();

        try {
            
            publicacionesDAO publicacionesDAO = new publicacionesDAO();
            
            respuesta = publicacionesDAO.getAllPublicacionesByActionPersonaID(personaID,action);
            
        } catch (SQLException ex) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            Logger.getLogger(BeanPublicaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta;        
    }
    
}
