/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;


import Entidad.Puntos;
import JavaBean.BeanConcordancia;
import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 *
 * @author hariasor
 */
public class publicacionesDAO {
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public publicacionesDAO(){
        super();
    }
	
	public JSONObject updateEstadoPublicacion(String user_id, String idPublicacion, String nuevoEstado) throws SQLException {
        JSONObject response_db = new JSONObject();
        Statement stmt = null;
        Cone cone = new Cone();
        String query;
        try {
            Connection mConexion = cone.getConnection();
            stmt = mConexion.createStatement(); 
            query = "UPDATE Persona_Publicacion SET "
                    + "EstadoPublicacion='" + nuevoEstado + "' "
                    + "WHERE idPersona='" + user_id + "' and idPublicacion='" + idPublicacion +"'";
            
            System.out.println("Query: " + query);
            stmt.execute(query);
            response_db.put("code", 0);
        } catch(Exception e) {
            System.out.println("Error: " + e);
            response_db.put("code", 9999);
            response_db.put("description", "Error en DataBase");
        } finally {
            if (stmt != null) stmt.close();
            //cone.desconectar();
        }
        return response_db;
    }
    
    public JSONObject desactivarPublicacion(String user_id, String idPublicacion) throws SQLException {
        JSONObject respuesta_db = new JSONObject();
        Statement stmt = null;
        Cone cone = new Cone();
        String query;
        try {
            Connection mConexion = cone.getConnection();
            stmt = mConexion.createStatement(); 
            query = "UPDATE Persona_Publicacion SET "
                    + "EstadoSistema='0' "
                    + "WHERE idPersona='" + user_id + "' and idPublicacion='" + idPublicacion +"'";
            
            System.out.println("Query: " + query);
            stmt.execute(query);
            respuesta_db.put("code", 0);
        } catch(SQLException e) {
            System.out.println("Error: " + e);
            respuesta_db.put("code", 9999);
            respuesta_db.put("description", "Error en DataBase");
        } finally {
            if (stmt != null) stmt.close();
            //cone.desconectar();
        }
        return respuesta_db;
    }
    
    public JSONObject actualizarPublicacion(JSONObject mNewData) throws SQLException {
        JSONObject respuesta_db = new JSONObject();
        Statement stmt = null;
        Cone cone = new Cone();
        JSONArray autores_actuales = (JSONArray) mNewData.get("Autores");
        JSONArray autores_eliminados = (JSONArray) mNewData.get("Autores_Eliminados");
        String query;
        String idPublicacion = mNewData.get("ID").toString();
        int idNuevoUsuario = 0;
        ResultSet rs;
        
        try {
            Connection mConexion = cone.getConnection();
            stmt = mConexion.createStatement(); 
            query = "UPDATE Publicacion SET "
                    + "Titulo='" + mNewData.get("Titulo")  + "',"
                    + "Tipo='" + mNewData.get("Tipo") + "',"
                    + "codigoPublicacion=" + ((mNewData.get("codigoPublicacion") == null) ? null : "'" + mNewData.get("codigoPublicacion") + "'") + ","
                    + "Lugar=" + ((mNewData.get("Lugar") == null) ? null : "'" + mNewData.get("Lugar") + "'") + ","
                    + "Editorial=" + ((mNewData.get("Editorial") == null) ? null : "'" + mNewData.get("Editorial") + "'") + ","
                    + "FechaInicio=" + ((mNewData.get("FechaInicio") == null) ? null : "'" + mNewData.get("FechaInicio") + "'") + ","
                    + "Duracion=" + ((mNewData.get("Duracion") == null) ? null : "'" + mNewData.get("Duracion") + "'") + ","
                    + "Plataforma=" + ((mNewData.get("Plataforma") == null) ? null : "'" + mNewData.get("Plataforma") + "'") + ","
                    + "tipoEspecifico=" + ((mNewData.get("tipoEspecifico") == null) ? null : "'" + mNewData.get("tipoEspecifico") + "'")
                    + " WHERE ID=" + idPublicacion;
            System.out.println("Query: " + query);
            stmt.execute(query);
            
            // Se actualizan los roles de autores
            for (int i = 0; i < autores_actuales.size(); i++) {
                JSONObject autor = (JSONObject) autores_actuales.get(i);
                if (!autor.get("ID").toString().equals("-1")) {
                    query = "UPDATE Persona_Publicacion SET "
                            + "Rol='" + autor.get("Rol") + "'"
                            + " WHERE IdPublicacion=" + idPublicacion + " and IdPersona=" + autor.get("ID");
                } else {
                    query = "INSERT INTO Persona (Nombre, Registrado, EstadoSistema) "
                            + "VALUES (" 
                            + "'" + StringUtils.stripAccents(autor.get("Nombre").toString()).trim().toUpperCase().replace(".","").replace("-", " ") + "',"
                            + "'" + 0 + "',"
                            + "'" + 1 + "')";
                    stmt.execute(query);
                    query = "SELECT LAST_INSERT_ID()";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) idNuevoUsuario = rs.getInt(1); rs.close();
                        query = "INSERT INTO Persona_Publicacion"
                                + " VALUES ("
                                + "'" + idNuevoUsuario + "',"
                                + "'" + idPublicacion + "',"
                                + "'" + autor.get("Rol") + "',"
                                + "'" + "Pendiente de verificacion" + "',"
                                + "'" + 1 + "')";
                }
                stmt.execute(query);
            }
            // Se desvinculan autores a la publicacion
            for (int i = 0; i < autores_eliminados.size(); i++) {
                query = "UPDATE Persona_Publicacion SET "
                        + "EstadoSistema='0'"
                        + " WHERE IdPublicacion=" + idPublicacion + " and IdPersona=" + autores_eliminados.get(i);
                stmt.execute(query);
            }
            respuesta_db.put("code", 0);
        } catch(SQLException e) {
            respuesta_db.put("code", 9999);
            respuesta_db.put("description", "Error en base de datos");
            System.out.println("Error: " + e);
        } finally {
            if (stmt != null) stmt.close();
            //cone.desconectar();
        }
        return respuesta_db;
    }	
    
    public JSONObject insertPublicacion(String user_id, JSONObject newPublicacion) throws SQLException {
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        Statement stmt;
        ResultSet rs;
        int idPublicacion = 0, idNuevoUsuario = 0;
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            stmt = conexion.createStatement();
            // Insertar publicacion
            String query = "INSERT INTO Publicacion (Titulo, Tipo, CodigoPublicacion, Lugar, Editorial, FechaInicio, Extraido, duracion, tipoEspecifico, plataforma)"
                    + " VALUES ("
                    + ((newPublicacion.get("Titulo") == null) ? null : "'" + StringUtils.stripAccents(newPublicacion.get("Titulo").toString()).trim().toUpperCase().replace(".","").replace("-", " ") +"'") + ","
                    + ((newPublicacion.get("Tipo") == null) ? null : "'" + newPublicacion.get("Tipo") +"'") + ","
                    + ((newPublicacion.get("CodigoPublicacion") == null) ? null : "'" + newPublicacion.get("CodigoPublicacion") +"'") + ","
                    + ((newPublicacion.get("Lugar") == null) ? null : "'" + newPublicacion.get("Lugar") +"'") + ","
                    + ((newPublicacion.get("Editorial") == null) ? null : "'" + newPublicacion.get("Editorial") +"'") + ","
                    + ((newPublicacion.get("FechaInicio") == null) ? null : "'" + newPublicacion.get("FechaInicio") +"'")+ ","
                    + ((newPublicacion.get("Extraido") == null) ? null : "'" + newPublicacion.get("Extraido") +"'") + ","
                    + ((newPublicacion.get("Duracion") == null) ? null : "'" + newPublicacion.get("Duracion") +"'") + ","
                    + ((newPublicacion.get("tipoEspecifico") == null) ? null : "'" + newPublicacion.get("tipoEspecifico") +"'")+ ","
                    + ((newPublicacion.get("Plataforma") == null) ? null : "'" + newPublicacion.get("Plataforma") +"'") + ")";
            System.out.println("Query insertar publicacion: " + query);
            stmt.execute(query);
            query = "SELECT LAST_INSERT_ID()";
            rs = stmt.executeQuery(query);
            if (rs.next()) idPublicacion = rs.getInt(1); rs.close();
            // Insertar autor principal
            query = "INSERT INTO Persona_Publicacion"
                    + " VALUES ("
                    + "'" + user_id + "',"
                    + "'" + idPublicacion + "',"
                    + "'" + "Profesor" + "',"
                    + "'" + "Verificado" + "',"
                    + "'" + 1 + "')";
            stmt.execute(query);
            // Insertar autores adicionales en Persona_Publicacion
            JSONArray autores = (JSONArray) newPublicacion.get("autores");
            if (autores != null) { 
                System.out.println(autores.toJSONString());
                for (int i = 0; i < autores.size(); i++) {
                    JSONObject autor = (JSONObject) autores.get(i);
                    if (!autor.get("ID").toString().equals(user_id)) {
                        if (!autor.get("ID").toString().equals("-1")) {
                            query = "INSERT INTO Persona_Publicacion"
                                    + " VALUES ("
                                    + "'" + autor.get("ID") + "',"
                                    + "'" + idPublicacion + "',"
                                    + "'" + autor.get("Rol") + "',"
                                    + "'" + "Pendiente de verificacion" + "',"
                                    + "'" + 1 + "')";

                        } else {
                            query = "INSERT INTO Persona (Nombre, Registrado, EstadoSistema) "
                                    + "VALUES (" 
                                    + "'" + StringUtils.stripAccents(autor.get("Nombre").toString()).trim().toUpperCase().replace(".","").replace("-", " ") + "',"
                                    + "'" + 0 + "',"
                                    + "'" + 1 + "')";
                            stmt.execute(query);
                            query = "SELECT LAST_INSERT_ID()";
                            rs = stmt.executeQuery(query);
                            if (rs.next()) idNuevoUsuario = rs.getInt(1); rs.close();
                            query = "INSERT INTO Persona_Publicacion"
                                    + " VALUES ("
                                    + "'" + idNuevoUsuario + "',"
                                    + "'" + idPublicacion + "',"
                                    + "'" + autor.get("Rol") + "',"
                                    + "'" + "Pendiente de verificacion" + "',"
                                    + "'" + 1 + "')";
                        }
                        stmt.execute(query);
                    }
                }   
            }
            stmt.close();
            //conex.desconectar();
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("insertPublicacion1: " + respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getPublicacionesByID (String user_id) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        JSONArray arregloPublicacionesID = new JSONArray();
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select IdPublicacion from Persona_Publicacion where IdPersona ="+user_id);
            rs = stmt.executeQuery("select IdPublicacion from Persona_Publicacion where IdPersona ="+user_id);
            while (rs.next()) {
                JSONObject publicacion = new JSONObject();
                publicacion.put("IdPublicacion", rs.getString("IdPublicacion").toString());
                arregloPublicacionesID.add(publicacion);
            }
            respuesta.put("publicacionesID", arregloPublicacionesID);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("Retorno: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Retorno: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getPublicacionesDecriptionByID (JSONObject user_publicaciones_id) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            String publicacionesABuscar = "";
            JSONObject publicacionIndividual = new JSONObject();
            JSONArray arregloPublicacionesDescriprion = new JSONArray();
            
            JSONArray arregloIDPublicaciones = (JSONArray) user_publicaciones_id.get("publicacionesID");
            arregloIDPublicaciones.toArray();
            for (int i=0;i<arregloIDPublicaciones.size();i++) {
               publicacionIndividual = (JSONObject) arregloIDPublicaciones.get(i);
               publicacionesABuscar += publicacionIndividual.get("IdPublicacion");
               if (i != arregloIDPublicaciones.size()-1)
                   publicacionesABuscar += ", ";
            }
            System.out.println("ID roles: "+publicacionesABuscar);
            
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from Publicacion where id in ("+publicacionesABuscar+")");
            rs = stmt.executeQuery("select * from Publicacion where id in ("+publicacionesABuscar+")");
            while (rs.next()) {
                JSONObject publicacionEncontrada = new JSONObject();
                publicacionEncontrada.put("titulo", rs.getString("Titulo"));
                publicacionEncontrada.put("tipo", rs.getString("Tipo"));
                publicacionEncontrada.put("ISBN", rs.getString("ISBN"));
                publicacionEncontrada.put("lugar", rs.getString("Lugar"));
                publicacionEncontrada.put("editorial", rs.getString("Editorial"));
                publicacionEncontrada.put("fInicio", rs.getString("FechaInicio"));
                publicacionEncontrada.put("Duracion", rs.getString("duracion"));
                publicacionEncontrada.put("tipoEspecifico", rs.getString("tipoEspecifico"));
                publicacionEncontrada.put("Plataforma", rs.getString("plataforma"));

                System.out.println("Publicacion en DB: "+publicacionEncontrada.toJSONString());
                arregloPublicacionesDescriprion.add(publicacionEncontrada);
            }
            respuesta.put("publicaciones", arregloPublicacionesDescriprion);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getPublicacionesDecriptionByID: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getPublicacionesDecriptionByID: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
       
    public JSONObject getAllPublicacionesDecription () throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            JSONArray arregloPublicaciones = new JSONArray();
      
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from Publicacion");
            rs = stmt.executeQuery("select * from Publicacion");
            while (rs.next()) {
                JSONObject publicacion = new JSONObject();
                publicacion.put("ID", rs.getString("ID"));
                publicacion.put("Titulo", rs.getString("Titulo"));
                publicacion.put("Tipo", rs.getString("Tipo"));
                publicacion.put("ISBN", rs.getString("ISBN"));
                publicacion.put("ISSN", rs.getString("ISSN"));
                publicacion.put("Lugar", rs.getString("Lugar"));
                publicacion.put("Editorial", rs.getString("Editorial"));
                publicacion.put("FechaInicio", rs.getString("FechaInicio"));
                publicacion.put("Duracion", rs.getString("duracion"));
                publicacion.put("tipoEspecifico", rs.getString("tipoEspecifico"));
                publicacion.put("Plataforma", rs.getString("plataforma"));
                arregloPublicaciones.add(publicacion);
            }
            respuesta.put("publicaciones", arregloPublicaciones);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAllPublicacionesDecription: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAllPublicacionesDecription: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getAutoresByPublicacionID (String idPublicacion) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            JSONArray arregloIdPersonas = new JSONArray();
      
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select IdPersona from Persona_Publicacion where IdPublicacion='"+idPublicacion+"'");
            rs = stmt.executeQuery("select IdPersona from Persona_Publicacion where IdPublicacion='"+idPublicacion+"'");
            while (rs.next()) {
                JSONObject persona = new JSONObject();
                persona.put("IdPersona", rs.getString("IdPersona"));
                arregloIdPersonas.add(persona);
            }
            respuesta.put("idPublicaciones", arregloIdPersonas);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAutoresByPublicacionID: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAutoresByPublicacionID: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getAllAutoresNamesById (JSONArray personas) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            String nombresABuscar = "";
            JSONObject idIndividual = new JSONObject();
            JSONArray arregloNombreAutores = new JSONArray();
            
            for (int i=0;i<personas.size();i++) {
               idIndividual = (JSONObject) personas.get(i);
               nombresABuscar += idIndividual.get("IdPersona");
               if (i != personas.size()-1)
                   nombresABuscar += ", ";
            }
            System.out.println("ID personas: "+nombresABuscar);
            
            if (!nombresABuscar.equalsIgnoreCase("")) {
                Cone conex = new Cone();
                Connection conexion= conex.getConnection();
                Statement stmt = null;
                ResultSet rs;
                stmt = conexion.createStatement();
                System.out.println("select Nombre from Persona where EstadoSistema=1 and ID in ("+nombresABuscar+")");
                rs = stmt.executeQuery("select Nombre from Persona where EstadoSistema=1 and ID in ("+nombresABuscar+")");
                while (rs.next()) {
                    JSONObject personaEncontrada = new JSONObject();
                    personaEncontrada.put("Nombre", rs.getString("Nombre"));
                    arregloNombreAutores.add(personaEncontrada);
                }
                rs.close();
                stmt.close();
                //conex.desconectar();
            }
            
            respuesta.put("autoresPublicacionSistema", arregloNombreAutores);
            
            System.out.println("getAllAutoresNamesById: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAllAutoresNamesById: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject publicacionExistenteDeProfesor (String publicacionID, String profesorID) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        boolean publicacionYaReferenciada = false;

        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("SELECT 1 FROM Persona_Publicacion WHERE IdPersona ="+ profesorID +"and IdPublicacion = "+publicacionID);
            rs = stmt.executeQuery("SELECT 1 FROM Persona_Publicacion WHERE IdPersona ="+ profesorID +"and IdPublicacion = "+publicacionID);
            while (rs.next()) {
                publicacionYaReferenciada = true;
            }
            respuesta.put("publicacionExiste", publicacionYaReferenciada);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("Retorno: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Retorno: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }

    public JSONObject getAllPublicacionesByPersonaID (String personaID) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            JSONArray arregloPublicaciones = new JSONArray();
      
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select P.ID, P.Titulo,P.Tipo ,P.codigoPublicacion, P.Lugar ,P.Editorial ,P.FechaInicio, P.Extraido, P.tipoEspecifico, P.duracion, P.plataforma, PP.EstadoPublicacion from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion where  PP.EstadoSistema = 1 && PP.IdPersona ="+personaID);
            rs = stmt.executeQuery("select P.ID, P.Titulo,P.Tipo ,P.codigoPublicacion, P.Lugar ,P.Editorial ,P.FechaInicio, P.Extraido, P.tipoEspecifico, P.duracion, P.plataforma, PP.EstadoPublicacion from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion where  PP.EstadoSistema = 1 && PP.IdPersona ="+personaID);
            while (rs.next()) {
                JSONObject publicacion = new JSONObject();
                publicacion.put("ID", rs.getString("ID"));
                publicacion.put("Titulo", rs.getString("Titulo"));
                publicacion.put("Tipo", rs.getString("Tipo"));
                publicacion.put("codigoPublicacion", rs.getString("codigoPublicacion"));
                publicacion.put("Lugar", rs.getString("Lugar"));
                publicacion.put("Editorial", rs.getString("Editorial"));
                publicacion.put("FechaInicio", rs.getString("FechaInicio"));
                publicacion.put("Extraido", rs.getString("Extraido"));
                publicacion.put("EstadoPublicacion", rs.getString("EstadoPublicacion"));
                publicacion.put("Duracion", rs.getString("duracion"));
                publicacion.put("tipoEspecifico", rs.getString("tipoEspecifico"));
                publicacion.put("Plataforma", rs.getString("plataforma"));
                arregloPublicaciones.add(publicacion);
            }
            respuesta.put("publicaciones", arregloPublicaciones);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAllPublicacionesByPersonaID: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAllPublicacionesByPersonaID: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getAlldudosas () throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        
        try {
            JSONArray arregloPublicaciones = new JSONArray();
      
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from PublicacionDudosa where");
            rs = stmt.executeQuery("select * from PublicacionDudosa where");
            while (rs.next()) {
                JSONObject publicacion = new JSONObject();
                publicacion.put("ID", rs.getString("ID"));
                publicacion.put("Titulo", rs.getString("Titulo"));
                publicacion.put("Tipo", rs.getString("Tipo"));
                publicacion.put("codigoPublicacion", rs.getString("codigoPublicacion"));
                publicacion.put("Lugar", rs.getString("Lugar"));
                publicacion.put("Editorial", rs.getString("Editorial"));
                publicacion.put("FechaInicio", rs.getString("FechaInicio"));
                publicacion.put("Extraido", rs.getString("Extraido"));
                publicacion.put("Duracion", rs.getString("duracion"));
                publicacion.put("tipoEspecifico", rs.getString("tipoEspecifico"));
                publicacion.put("Plataforma", rs.getString("plataforma"));
                arregloPublicaciones.add(publicacion);
            }
            respuesta.put("publicaciones", arregloPublicaciones);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAlldudosas: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAlldudosas: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject findPublicacionDudosaByTitulo (String nombrePublicacionNueva) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        
        try {
            boolean existeTitulo = false;
            
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("SELECT * FROM PublicacionDudosa WHERE Titulo = '"+nombrePublicacionNueva+"'");
            
            if (!nombrePublicacionNueva.isEmpty() && nombrePublicacionNueva!=null && !nombrePublicacionNueva.equals("")) {
                rs = stmt.executeQuery("SELECT * FROM PublicacionDudosa WHERE Titulo = '"+nombrePublicacionNueva+"'");
                while (rs.next())
                    existeTitulo = true;
                respuesta.put("existeTitulo", existeTitulo);
                rs.close();
                stmt.close();

            }
            else
                respuesta.put("existeTitulo", existeTitulo);
            
            //conex.desconectar();
            System.out.println("findPublicacionDudosaByTitulo: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("findPublicacionDudosaByTitulo: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
 public JSONObject insertPublicacionAutomatica (String user_id, JSONObject newPublicacion,JSONArray listaCoautores,String fuenteExtraccion) throws SQLException {
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        Statement stmt;
        ResultSet rs;
        int idPublicacion = 0, idNuevoUsuario = 0;
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            stmt = conexion.createStatement();
            // Insertar publicacion
            String query = "INSERT INTO Publicacion (Titulo, Tipo, CodigoPublicacion, Lugar, Editorial, FechaInicio,duracion,tipoEspecifico,plataforma, Extraido)"
                    + " VALUES ("
                    + ((newPublicacion.get("titulo") == null) ? null : "'" + StringUtils.stripAccents(newPublicacion.get("titulo").toString()).trim().toUpperCase().replace(".","").replace("-", " ").replace("'"," ") +"'") + ","
                    + ((newPublicacion.get("tipo") == null) ? null : "'" + newPublicacion.get("tipo") +"'") + ","
                    + ((newPublicacion.get("codigoPublicacion") == null) ? null : "'" + newPublicacion.get("codigoPublicacion") +"'") + ","
                    + ((newPublicacion.get("lugarPublicacion") == null) ? null : "'" + StringUtils.stripAccents(newPublicacion.get("lugarPublicacion").toString().replace("'"," ")) +"'") + ","
                    + ((newPublicacion.get("editorial") == null) ? null : "'" + newPublicacion.get("editorial") +"'") + ","
                    + (((newPublicacion.get("fecha") == null) || newPublicacion.get("fecha").equals("")) ? null : "'" + newPublicacion.get("fecha") +"'")+ ","
                    + (((newPublicacion.get("Duracion") == null) || newPublicacion.get("Duracion").equals("")) ? null : "'" + newPublicacion.get("Duracion") +"'")+ ","
                    + (((newPublicacion.get("tipoEspecifico") == null) || newPublicacion.get("tipoEspecifico").equals("")) ? null : "'" + newPublicacion.get("tipoEspecifico") +"'")+ ","
                    + (((newPublicacion.get("Plataforma") == null) || newPublicacion.get("Plataforma").equals("")) ? null : "'" + newPublicacion.get("Plataforma") +"'")+ ","
                    +  "'" + fuenteExtraccion +"'" + ")";
            System.out.println("Query insertar publicacion: " + query);
            stmt.execute(query);
            query = "SELECT LAST_INSERT_ID()";
            rs = stmt.executeQuery(query);
            if (rs.next()) 
                idPublicacion = rs.getInt(1); 
            rs.close();
            // Insertar autor principal
            query = "INSERT INTO Persona_Publicacion"
                    + " VALUES ("
                    + "'" + user_id + "',"
                    + "'" + idPublicacion + "',"
                    + null + ","
                    + "'" + "Pendiente de verificacion" + "',"
                    + "'" + 1 + "')";
            stmt.execute(query);
            // Insertar autores adicionales en Persona_Publicacion
            System.out.println(listaCoautores);
            if (listaCoautores != null) {
                for (int i = 0; i < listaCoautores.size(); i++) {
                    JSONObject autor = (JSONObject) listaCoautores.get(i);
                    if (!autor.get("id").toString().equals(user_id)) {
                        if (!autor.get("id").toString().equals("-1")) {
                            query = "INSERT INTO Persona_Publicacion"
                                    + " VALUES ("
                                    + "'" + autor.get("id") + "',"
                                    + "'" + idPublicacion + "',"
                                    + null + ","
                                    + "'" + "Pendiente de verificacion" + "',"
                                    + "'" + 1 + "')";

                        } else {
                            query = "INSERT INTO Persona (Nombre, Registrado, EstadoSistema) "
                                    + "VALUES (" 
                                    + "'" + StringUtils.stripAccents(autor.get("nombre").toString()).trim().toUpperCase().replace(".","").replace("-", " ") + "',"
                                    + "'" + 0 + "',"
                                    + "'" + 1 + "')";
                            stmt.execute(query);
                            query = "SELECT LAST_INSERT_ID()";
                            rs = stmt.executeQuery(query);
                            if (rs.next()) idNuevoUsuario = rs.getInt(1); rs.close();
                            query = "INSERT INTO Persona_Publicacion"
                                    + " VALUES ("
                                    + "'" + idNuevoUsuario + "',"
                                    + "'" + idPublicacion + "',"
                                    + null + ","
                                    + "'" + "Pendiente de verificacion" + "',"
                                    + "'" + 1 + "')";
                        }
                        stmt.execute(query);
                    }
                }
            }
            stmt.close();
            //conex.desconectar();
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("insertPublicacion1: " + respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
  
    
    public JSONObject insertPublicacionDudosa (JSONObject newPublicacion, JSONArray publicacionesSistema, String fuenteExtraccion) throws SQLException {
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        Statement stmt;
        JSONObject publicacionIndivisualSistema = new JSONObject();
        String query = "";
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            stmt = conexion.createStatement();
            // Insertar publicacion
            int tamanoListaPublicacionesSistema = publicacionesSistema.size();
            for (int i=0;i<tamanoListaPublicacionesSistema;i++) {
                publicacionIndivisualSistema = (JSONObject) publicacionesSistema.get(i);
                System.out.println("Publicacion nueva: "+newPublicacion.toJSONString());
                query = "INSERT INTO PublicacionDudosa (ID,Titulo, Tipo, CodigoPublicacion, Lugar, Editorial, FechaInicio,duracion,tipoEspecifico,plataforma,Extraido,EstadoSistema)"
                    + " VALUES ("
                    + publicacionIndivisualSistema.get("ID")+ ","
                    + ((newPublicacion.get("titulo") == null) ? null : "'" + StringUtils.stripAccents(newPublicacion.get("titulo").toString()).trim().toUpperCase().replace(".","").replace("-", " ") +"'") + ","
                    + ((newPublicacion.get("tipo") == null) ? null : "'" + newPublicacion.get("tipo") +"'") + ","
                    + ((newPublicacion.get("codigoPublicacion") == null) ? null : "'" + newPublicacion.get("codigoPublicacion") +"'") + ","
                    + ((newPublicacion.get("lugar") == null) ? null : "'" + newPublicacion.get("lugar") +"'") + ","
                    + ((newPublicacion.get("editorial") == null) ? null : "'" + newPublicacion.get("editorial") +"'") + ","
                    + ((newPublicacion.get("fechaInicio") == null) ? null : "'" + newPublicacion.get("fechaInicio") +"'")+ ","  
                    + ((newPublicacion.get("duracion") == null) ? null : "'" + newPublicacion.get("duracion") +"'") + ","
                    + ((newPublicacion.get("tipoEspecifico") == null) ? null : "'" + newPublicacion.get("tipoEspecifico") +"'") + ","
                    + ((newPublicacion.get("plataforma") == null) ? null : "'" + newPublicacion.get("plataforma") +"'")+ ","    
                    + "'" + fuenteExtraccion +"'" + ","
                    + "'" + 1 + "')";
                System.out.println("Query insertPublicacionDudosa() " + query);
                stmt.execute(query);
                
            }
            stmt.close();
            //conex.desconectar();
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("insertPublicacionDudosa: " + respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public int idPublicacionxAutores(String idAutores, String titulo,String codigo){
        int idPublicacion=-1; 
       
        Statement stmt = null;
        ResultSet rs;
        Cone conex = new Cone();
        try {
             
            Connection conexion= conex.getConnection();
            stmt = conexion.createStatement();


            String query = "Select *"+ 
                "FROM (Publicacion INNER JOIN Persona_Publicacion on Publicacion.ID=Persona_Publicacion.IdPublicacion) INNER JOIN Persona ON Persona_Publicacion.IdPersona=Persona.ID "+
                "WHERE Persona.ID in ("+idAutores+") AND Persona_Publicacion.EstadoSistema='"+1+"'";
            System.out.println(query);
            rs = stmt.executeQuery(query);

            BeanConcordancia bean = new BeanConcordancia();
            while (rs.next() && idPublicacion== -1 ) {
                System.out.println(rs.getString(2));
                double porcentaje = bean.getSimilarity(rs.getString(2), titulo, "NOMBRE_PUBLICACION");
                double max=0.99;
                double maxPub=0.90;
                if(porcentaje >= max)
                    idPublicacion=rs.getInt(1);
                else if(porcentaje >= maxPub){
                   if(!codigo.equals("")){
                       if(codigo.equals(rs.getString("codigoPublicacion")))
                           idPublicacion=rs.getInt(1);
                   } 
                }
            }

            rs.close();
            stmt.close();
          
        } catch (SQLException ex) {
            Logger.getLogger(publicacionesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
             //conex.desconectar();
        }     

        return idPublicacion;
    }
    
    public JSONObject insertReferenciaPublicacion (String user_id, int idPublicacionAReferenciar) throws SQLException {
        JSONObject respuesta = new JSONObject();
        Cone conex = new Cone();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        Statement stmt;
        ResultSet rs;
        try {
             Connection conexion= conex.getConnection();
            stmt = conexion.createStatement();
            // Insertar publicacion
            String query = "INSERT INTO Persona_Publicacion"
                + " VALUES ("
                + "'" + user_id + "',"
                + "'" + idPublicacionAReferenciar + "',"
                + null + ","
                + "'" + "Pendiente de verificacion" + "',"
                + "'" + 1 + "')";
            stmt.execute(query);
            stmt.close();
           
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("insertReferenciaPublicacion: " + respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }finally{
            //conex.desconectar();
        }
    }
	
    public Puntos buscarPuntosProfesor(String profesorId){
        Statement stmt = null;
        ResultSet rs;
        Cone conex = new Cone();
        Puntos puntos= new Puntos();
        Connection conexion= conex.getConnection();
        try {
            stmt = conexion.createStatement();
            
            String query = "Select * From PuntosPublicacion Where IdPersona='"+profesorId+"'";
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
               puntos.setId(rs.getInt("ID"));
               puntos.setIdPersona(rs.getInt("IdPersona"));
               puntos.setPuntos(rs.getInt("Puntos"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(publicacionesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            //conex.desconectar();
        }

          

        return puntos;
    }
    
    public JSONObject getAllPublicacionesByActionPersonaID (String personaID, String action) throws SQLException{
        JSONObject respuesta = new JSONObject();
        
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            JSONObject respuesta_agregarAutores = new JSONObject();
            JSONArray arregloPublicaciones = new JSONArray();
            String idPublicaciones = "(";
            Boolean hayPublciaciones = false;
            
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            String query = "";
            
            if (action.equalsIgnoreCase("VERIFICADAS")) {
                query = "select P.ID, P.Titulo,P.Tipo ,P.codigoPublicacion, P.Lugar ,P.Editorial ,P.FechaInicio, P.Extraido, P.tipoEspecifico, P.duracion, P.plataforma, PP.EstadoPublicacion from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion where  PP.EstadoSistema = 1 and PP.EstadoPublicacion = 'Verificado' and PP.IdPersona ="+personaID+" ORDER BY PP.IdPublicacion";
                System.out.println("Query getAllPublicacionesByActionPersonaID - VERIFICADAS: "+query);
            }
            else {
                query = "select P.ID, P.Titulo,P.Tipo ,P.codigoPublicacion, P.Lugar ,P.Editorial ,P.FechaInicio, P.Extraido, P.tipoEspecifico, P.duracion, P.plataforma, PP.EstadoPublicacion from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion where  PP.EstadoSistema = 1 and PP.EstadoPublicacion = 'Pendiente de verificacion' and  PP.IdPersona ="+personaID+" ORDER BY PP.IdPublicacion";
                System.out.println("Query getAllPublicacionesByActionPersonaID - NO_VERIFICADAS: "+query);
            }
            rs = stmt.executeQuery(query);
            
            String idPublciacionAnterior = "";
            while (rs.next()) {
                if (!idPublciacionAnterior.equalsIgnoreCase(rs.getString("ID"))) {
                    hayPublciaciones = true;
                    JSONObject publicacion = new JSONObject();
                    publicacion.put("ID", rs.getString("ID"));
                    publicacion.put("Titulo", rs.getString("Titulo"));
                    publicacion.put("Tipo", rs.getString("Tipo"));
                    publicacion.put("codigoPublicacion", rs.getString("codigoPublicacion"));
                    publicacion.put("Lugar", rs.getString("Lugar"));
                    publicacion.put("Editorial", rs.getString("Editorial"));
                    publicacion.put("FechaInicio", rs.getString("FechaInicio"));
                    publicacion.put("Extraido", rs.getString("Extraido"));
                    publicacion.put("EstadoPublicacion", rs.getString("EstadoPublicacion"));
                    publicacion.put("tipoEspecifico", rs.getString("tipoEspecifico"));
                    publicacion.put("Duracion", rs.getString("duracion"));
                    publicacion.put("Plataforma", rs.getString("plataforma"));

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
              
            //conex.desconectar();
            System.out.println("getAllPublicacionesByPersonaID: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAllPublicacionesByPersonaID: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
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
                        autor.put("ID", rs.getString("Id"));
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
  public JSONObject getAllTitulosByFiltros (String departamento, String facultad, String nombreProfesor,int tipoConsulta) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            JSONArray listaNombrePublicaciones = new JSONArray();
      
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            
            String query = "select P.Titulo from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion\n" +
                "INNER join Profesor PR on PR.IdPersona = PP.IdPersona\n" +
                "where  PP.EstadoSistema = 1 and PP.EstadoPublicacion = 'Verificado'\n" +
                "ORDER BY PP.IdPublicacion";
            
            switch (tipoConsulta) {
                //Solo por departamento
                case 1:
                    query = "select P.Titulo from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion\n" +
                        "INNER join Profesor PR on PR.IdPersona = PP.IdPersona\n" +
                        "INNER JOIN Departamento D on D.ID = PR.IdDepartamento\n" +
                        "where  PP.EstadoSistema = 1 and PP.EstadoPublicacion = 'Verificado' and D.Nombre LIKE '%"+departamento+"%'\n" +
                        "ORDER BY PP.IdPublicacion";
                break;
                //Solo por Facultad
                case 2:
                    query = "select P.Titulo from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion\n" +
                        "INNER join Profesor PR on PR.IdPersona = PP.IdPersona\n" +
                        "INNER JOIN Departamento D on D.ID = PR.IdDepartamento\n" +
                        "INNER JOIN Facultad F on F.ID = D.IdFacultad\n" +
                        "where  PP.EstadoSistema = 1 and PP.EstadoPublicacion = 'Verificado' and F.Nombre LIKE '%"+facultad+"%'\n" +
                        "ORDER BY PP.IdPublicacion";
                break;
                //Solo por nombre profesor
                case 3:
                    query = "select P.Titulo from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion\n" +
                        "INNER JOIN Persona PE on PE.ID = PP.IdPersona\n" +
                        "INNER join Profesor PR on PR.IdPersona = PP.IdPersona\n" +
                        "where  PP.EstadoSistema = 1 and PP.EstadoPublicacion = 'Verificado' and PE.Nombre LIKE '%"+nombreProfesor+"%'\n" +
                        "ORDER BY PP.IdPublicacion";
                break;
                //Profesor y departamento
                case 4:
                    query = "select P.Titulo from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion\n" +
                        "INNER JOIN Persona PE on PE.ID = PP.IdPersona\n" +
                        "INNER join Profesor PR on PR.IdPersona = PP.IdPersona\n" +
                        "INNER JOIN Departamento D on D.ID = PR.IdDepartamento\n" +
                        "where  PP.EstadoSistema = 1 and PP.EstadoPublicacion = 'Verificado' and D.Nombre LIKE '%"+departamento+"%' AND PE.Nombre LIKE '%"+nombreProfesor+"%'\n" +
                        "ORDER BY PP.IdPublicacion";
                break;
                //Profesor y facultad
                case 5:
                    query = "select P.Titulo from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion\n" +
                        "INNER JOIN Persona PE on PE.ID = PP.IdPersona\n" +
                        "INNER join Profesor PR on PR.IdPersona = PP.IdPersona\n" +
                        "INNER JOIN Departamento D on D.ID = PR.IdDepartamento\n" +
                        "INNER JOIN Facultad F on F.ID = D.IdFacultad\n" +
                        "where  PP.EstadoSistema = 1 and PP.EstadoPublicacion = 'Verificado' and F.Nombre LIKE '%"+facultad+"%' AND PE.Nombre LIKE '%"+nombreProfesor+"%'\n" +
                        "ORDER BY PP.IdPublicacion";
                break;
            }
            System.out.println("Query: "+query);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                listaNombrePublicaciones.add(rs.getString("Titulo"));
            }
            respuesta.put("titulos", listaNombrePublicaciones);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAllTitulosByFiltros(): "+respuesta.toString());
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAllTitulosByFiltros(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
        }
        return respuesta;
    }
   
    
}

