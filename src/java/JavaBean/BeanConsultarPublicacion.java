/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.filtrosPublicacionDao;
import Entidad.Publicacion;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Administrador
 */
public class BeanConsultarPublicacion {
    public BeanConsultarPublicacion(){
        super();
    }
    
    public JSONObject buscarPorFiltros(String json){
        JSONObject respuesta = new JSONObject();
        JSONParser parser = new JSONParser();
        System.out.println(json);
        try {
            JSONObject filtros = (JSONObject) parser.parse(json);
            
            String fechaInicial=(String) filtros.get("fechaInicial");
            String fechaFinal=(String) filtros.get("fechaFinal");
            String nombre=(String) filtros.get("nombre");
            String departamento=(String) filtros.get("departamento");
            String titulo=(String) filtros.get("titulo");
            String tipo=(String) filtros.get("tipo");
            
            
            
           
            filtrosPublicacionDao dao= new filtrosPublicacionDao();
            List<Publicacion> publicaciones=dao.buscarPublicacion(fechaInicial, fechaFinal, nombre, titulo, departamento, tipo);
           
            JSONArray arr = new JSONArray();
            for(Publicacion p : publicaciones){
                JSONObject res = new JSONObject();
                
                res.put("titulo", p.getTitulo());
                res.put("fecha",p.getFecha());
                res.put("editorial", p.getEditorial());
                res.put("lugar",p.getLugar());
                res.put("ISSN", p.getISSN());
                res.put("ISBN", p.getISBN());
                arr.add(res);
                
            } 
            
            respuesta.put("publicaciones", arr);
            
            } catch (ParseException ex) {
            Logger.getLogger(BeanConsultarPublicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            
         return respuesta;
    }
}
