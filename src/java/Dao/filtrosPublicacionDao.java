/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrador
 */
public class filtrosPublicacionDao {
    
 public filtrosPublicacionDao(){
     super();
    
 }   
 
  public JSONObject getByFiltros (String fechaQueryStatement, String nombreQueryStatement, String rolQueryStatement, String tipoQueryStatement){
       Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        JSONObject respuesta = new JSONObject();
        
        JSONObject respuesta_agregarAutores = new JSONObject();
        JSONArray arregloPublicaciones = new JSONArray();
        String idPublicaciones = "(";
        Boolean hayPublciaciones = false;
        try {
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
            
            String selectStatemt = "select DISTINCT(PU.ID) as ID, PU.Titulo, PU.Tipo, PU.codigoPublicacion, PU.Lugar, PU.Editorial, PU.FechaInicio, PU.tipoEspecifico, PU.duracion, PU.plataforma";
            String fromStatement = " from Publicacion PU inner join Persona_Publicacion PP on PP.IdPublicacion = PU.ID";
            String whereStatement = " where PP.EstadoSistema = 1";
            String groupByStatement = "";
            Boolean existeGroupByAnterior = false;
      
            if (fechaQueryStatement != null) {
                whereStatement += " and "+fechaQueryStatement;
            }
            
            if (nombreQueryStatement != null) {
                //selectStatemt = selectStatemt.replace("P", ", P");
                fromStatement += " inner join Persona PE on PE.ID = PP.IdPersona";                
                whereStatement += " and "+nombreQueryStatement;
            }
            
            if (rolQueryStatement != null) {
                //selectStatemt = selectStatemt.replace("PP", ", PP");
                whereStatement += " and "+rolQueryStatement;
            }
            
            if (tipoQueryStatement != null) {
                //selectStatemt = selectStatemt.replace("PU", ", PU");
                whereStatement += " and "+tipoQueryStatement;
            }
            whereStatement += " and PP.EstadoPublicacion = 'Verificado' ORDER BY PP.IdPublicacion";
                           
            stmt = conexion.createStatement();
            String query = selectStatemt+fromStatement+whereStatement;
            System.out.println("Query: "+query);
            
            rs = stmt.executeQuery(query);                     
            JSONArray arr = new JSONArray();                       
    
         //--------------------------------------------------
         String idPublciacionAnterior = "";
            while (rs.next()) {
                if (!idPublciacionAnterior.equalsIgnoreCase(rs.getString("ID"))) {
                    hayPublciaciones = true;
                    JSONObject publicacion = new JSONObject();
                    
                    publicacion.put("ID", rs.getString("ID"));
                    publicacion.put("titulo", rs.getString("Titulo"));
                    publicacion.put("tipo", rs.getString("Tipo"));
                    publicacion.put("codigoPublicacion", rs.getString("codigoPublicacion"));
                    publicacion.put("lugar", rs.getString("Lugar"));
                    publicacion.put("editorial", rs.getString("Editorial"));
                    publicacion.put("fecha", rs.getString("FechaInicio"));
                    publicacion.put("tipoEspecifico", rs.getString("tipoEspecifico"));
                    publicacion.put("duracion", rs.getString("duracion"));
                    publicacion.put("plataforma", rs.getString("plataforma"));

                    idPublicaciones += rs.getString("ID").toString()+",";

                    arregloPublicaciones.add(publicacion);
                    // POR ALGUNA RAZON SE REPITIO UNA PUBLICACION, ENTONCES SE ASEGURA QUE NO SE INGRESE DOS VECES EN LA TABLA
                    idPublciacionAnterior = rs.getString("ID");
                }
            }
            
            if(idPublicaciones.endsWith(",")) {
                idPublicaciones = idPublicaciones.substring(0,idPublicaciones.length() - 1);
                idPublicaciones += ")";
            }
            else
                idPublicaciones += ")";
            
            System.out.println("IDS: "+idPublicaciones);
            
            respuesta.put("publicaciones", arregloPublicaciones);
            rs.close();
            stmt.close();
            
            if (hayPublciaciones)
                respuesta_agregarAutores = agregarAutoresAPublicaciones(arregloPublicaciones, idPublicaciones);
            else {
                respuesta_agregarAutores.put("code", 1);
                respuesta_agregarAutores.put("description", "No hay resultados en la busqueda");
            }      
            
            if ((int)respuesta_agregarAutores.get("code") == 0) {
                respuesta.put("publicaciones", respuesta_agregarAutores.get("publicaciones"));
            }
            else 
                respuesta.put("publicaciones", arregloPublicaciones);
                 
            
    /*
            while (rs.next()) {
                JSONObject obj= new JSONObject();
                obj.put("titulo",rs.getString(2));
                obj.put("tipo",rs.getString(3));
                obj.put("codigoPublicacion",rs.getString(4));
                obj.put("lugar",rs.getString(5));
                obj.put("editorial",rs.getString(6));
                obj.put("fecha",rs.getString(7));
                obj.put("tipoEspecifico",rs.getString("tipoEspecifico"));
                obj.put("duracion",rs.getString("duracion"));
                obj.put("plataforma",rs.getString("plataforma"));                
                System.out.println(rs.getString(1));
                arr.add(obj);
            }
            
            rs.close();
            stmt.close();
            respuesta.put("publicaciones", arr);
          */ 
         
        }  catch (SQLException ex) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getParametrosSistema(): "+respuesta.toString());
            Logger.getLogger(filtrosPublicacionDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
             //conex.desconectar();
         }
        
        return respuesta;
    }
  
    private JSONObject agregarAutoresAPublicaciones (JSONArray arregloPublicaciones,String idPublicaciones) {
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        
        try {
            
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            
            System.out.println("Query getAutoresByPubliacacion(): select PP.IdPersona as Id, PP.IdPublicacion as IdPublicacion, PP.Rol AS Rol, PP.EstadoPublicacion AS Estado, P.Nombre \n" +
                "from Persona_Publicacion PP INNER JOIN Persona P on PP.IdPersona = P.ID\n" +
                "WHERE IdPublicacion in "+idPublicaciones+" AND PP.EstadoSistema = 1 AND P.EstadoSistema = 1\n" +
                "ORDER by PP.IdPublicacion");
            rs = stmt.executeQuery("select PP.IdPersona as Id, PP.IdPublicacion as IdPublicacion, PP.Rol AS Rol, PP.EstadoPublicacion AS Estado, P.Nombre \n" +
                "from Persona_Publicacion PP INNER JOIN Persona P on PP.IdPersona = P.ID\n" +
                "WHERE IdPublicacion in "+idPublicaciones+" AND PP.EstadoSistema = 1 AND P.EstadoSistema = 1\n" +
                "ORDER by PP.IdPublicacion");

            int tamanoArregloPublicaciones = arregloPublicaciones.size();
            String idPublicacionDeArrelgo = "";
            String idPublicaiconPersonas = "";
            if  (rs.next()) {
                idPublicaiconPersonas = rs.getString("IdPublicacion");
                for (int i=0;i<tamanoArregloPublicaciones;i++) {
                    idPublicacionDeArrelgo = ((JSONObject) arregloPublicaciones.get(i)).get("ID").toString();
                    JSONArray autoresXPublicacion = new JSONArray();
                    while (idPublicacionDeArrelgo.equalsIgnoreCase(idPublicaiconPersonas)) {
                        JSONObject autor = new JSONObject();
                        autor.put("Rol", rs.getString("Rol"));
                        autor.put("Nombre", rs.getString("Nombre"));
                        autoresXPublicacion.add(autor);
                        if (rs.next())
                            idPublicaiconPersonas = rs.getString("IdPublicacion");
                        else 
                            idPublicaiconPersonas = "";
                    }
                    ((JSONObject) arregloPublicaciones.get(i)).put("Autores", autoresXPublicacion);
                    System.out.println("Autores: "+autoresXPublicacion.toJSONString());
                }
            }
           
            respuesta.put("publicaciones", arregloPublicaciones);
            rs.close();
            stmt.close();
            
        } catch (SQLException ex) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("agregarAutoresAPublicaciones: "+respuesta.toString());
            Logger.getLogger(publicacionesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta;
    }
  
}  
