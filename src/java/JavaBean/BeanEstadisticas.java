/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.estadisticasDao;
import Entidad.Publicacion;
import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author hariasor
 */
public class BeanEstadisticas {
    
    public BeanEstadisticas() {
	super();
    }
     
    public JSONObject getEstadisticasByFiltros(String json){
        JSONObject respuesta = new JSONObject();
        JSONParser parser = new JSONParser();
        System.out.println(json);
        try {
                    
            JSONObject filtros = (JSONObject) parser.parse(json);
            estadisticasDao estadisticasDao = new estadisticasDao();
            JSONObject respuesta_beanEstadisticas = new JSONObject();
                
            String FechaInicio=(String) filtros.get("FechaInicio");
            String FechaFin=(String) filtros.get("FechaFin");
            String Nombre=(String) filtros.get("Nombre");
            String Departamento=(String) filtros.get("Departamento");
            JSONArray Rol=(JSONArray) filtros.get("Rol");
            JSONArray Tipo=(JSONArray) filtros.get("Tipo");
            
            String fechaQueryStatement = null;
            String nombreQueryStatement = null;
            String rolQueryStatement = null;
            String tipoQueryStatement = null;

            if (Tipo.size() > 0)
                tipoQueryStatement = "";
             if (Rol.size() > 0)
                rolQueryStatement = "";
            
            if (FechaInicio != null && FechaFin != null)
                fechaQueryStatement = "(PU.FechaInicio between '" + FechaInicio + "' and '" + FechaFin +"')";
          
            if (Nombre != null)
                nombreQueryStatement = "(PE.Nombre LIKE '%"+ Nombre +"%')";
            
            for (int i = 0; i<Rol.size(); i++) {
                if (i == 0)
                    rolQueryStatement += "(";
                rolQueryStatement += "PP.rol = '"+Rol.get(i).toString()+"'";
                if (i != Rol.size() - 1)
                    rolQueryStatement += " or ";
                if (i == Rol.size() - 1)
                    rolQueryStatement += ")";
            }
            System.out.println("rolQueryStatement: "+rolQueryStatement);
            
            for (int i = 0; i<Tipo.size(); i++) {
                if (i == 0)
                    tipoQueryStatement += "(";
                tipoQueryStatement += "PU.tipo = '"+Tipo.get(i).toString()+"'";
                if (i != Tipo.size() - 1)
                    tipoQueryStatement += " or ";
                if (i == Tipo.size() - 1)
                    tipoQueryStatement += ")";
            }
            System.out.println("tipoQueryStatement: "+tipoQueryStatement);
            
            respuesta = estadisticasDao.getEstadisticasByFiltros(fechaQueryStatement,nombreQueryStatement,rolQueryStatement,tipoQueryStatement);
                        
        } catch (ParseException ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            Logger.getLogger(BeanConsultarPublicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return respuesta;
    }
    
}
