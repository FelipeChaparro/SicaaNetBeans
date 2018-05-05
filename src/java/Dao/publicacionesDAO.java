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
            cone.desconectar();
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
            cone.desconectar();
        }
        return respuesta_db;
    }
    
    public JSONObject actualizarPublicacion(JSONObject mNewData) throws SQLException {
        JSONObject respuesta_db = new JSONObject();
        Statement stmt = null;
        Cone cone = new Cone();
        String query;
        try {
            Connection mConexion = cone.getConnection();
            stmt = mConexion.createStatement(); 
            query = "UPDATE Publicacion SET "
                    + "Titulo='" + mNewData.get("Titulo")  + "',"
                    + "Tipo='" + mNewData.get("Tipo") + "',"
                    + "codigoPublicacion=" + ((mNewData.get("codigoPublicacion") == null) ? null : "'" + mNewData.get("codigoPublicacion") + "'") + ","
                    + "Lugar=" + ((mNewData.get("Lugar") == null) ? null : "'" + mNewData.get("Lugar") + "'") + ","
                    + "Editorial=" + ((mNewData.get("Editorial") == null) ? null : "'" + mNewData.get("Editorial") + "'") + ","
                    + "FechaInicio=" + ((mNewData.get("FechaInicio") == null) ? null : "'" + mNewData.get("FechaInicio") + "'")
                    + " WHERE ID=" + mNewData.get("ID");
            System.out.println("Query: " + query);
            stmt.execute(query);
            respuesta_db.put("code", 0);
        } catch(SQLException e) {
            respuesta_db.put("code", 9999);
            respuesta_db.put("description", "Error en base de datos");
            System.out.println("Error: " + e);
        } finally {
            if (stmt != null) stmt.close();
            cone.desconectar();
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
            String query = "INSERT INTO Publicacion (Titulo, Tipo, CodigoPublicacion, Lugar, Editorial, FechaInicio, Extraido)"
                    + " VALUES ("
                    + ((newPublicacion.get("Titulo") == null) ? null : "'" + StringUtils.stripAccents(newPublicacion.get("Titulo").toString()).trim().toUpperCase().replace(".","").replace("-", " ") +"'") + ","
                    + ((newPublicacion.get("Tipo") == null) ? null : "'" + newPublicacion.get("Tipo") +"'") + ","
                    + ((newPublicacion.get("CodigoPublicacion") == null) ? null : "'" + newPublicacion.get("CodigoPublicacion") +"'") + ","
                    + ((newPublicacion.get("Lugar") == null) ? null : "'" + newPublicacion.get("Lugar") +"'") + ","
                    + ((newPublicacion.get("Editorial") == null) ? null : "'" + newPublicacion.get("Editorial") +"'") + ","
                    + ((newPublicacion.get("FechaInicio") == null) ? null : "'" + newPublicacion.get("FechaInicio") +"'")+ ","
                    + ((newPublicacion.get("Extraido") == null) ? null : "'" + newPublicacion.get("Extraido") +"'") + ")";
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
            System.out.println(autores);
            if (autores != null) { 
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
            conex.desconectar();
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
            conex.desconectar();
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
                System.out.println("Publicacion en DB: "+publicacionEncontrada.toJSONString());
                arregloPublicacionesDescriprion.add(publicacionEncontrada);
            }
            respuesta.put("publicaciones", arregloPublicacionesDescriprion);
            rs.close();
            stmt.close();
            conex.desconectar();
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
                arregloPublicaciones.add(publicacion);
            }
            respuesta.put("publicaciones", arregloPublicaciones);
            rs.close();
            stmt.close();
            conex.desconectar();
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
            conex.desconectar();
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
                conex.desconectar();
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
            conex.desconectar();
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
            System.out.println("select P.ID, P.Titulo,P.Tipo ,P.codigoPublicacion, P.Lugar ,P.Editorial ,P.FechaInicio, P.Extraido, P.Extraido, PP.EstadoPublicacion from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion where  PP.EstadoSistema = 1 && PP.IdPersona ="+personaID);
            rs = stmt.executeQuery("select P.ID, P.Titulo,P.Tipo ,P.codigoPublicacion, P.Lugar ,P.Editorial ,P.FechaInicio, P.Extraido, P.Extraido, PP.EstadoPublicacion from Publicacion P inner join Persona_Publicacion PP on P.ID=PP.idPublicacion where  PP.EstadoSistema = 1 && PP.IdPersona ="+personaID);
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
                arregloPublicaciones.add(publicacion);
            }
            respuesta.put("publicaciones", arregloPublicaciones);
            rs.close();
            stmt.close();
            conex.desconectar();
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
                arregloPublicaciones.add(publicacion);
            }
            respuesta.put("publicaciones", arregloPublicaciones);
            rs.close();
            stmt.close();
            conex.desconectar();
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
            rs = stmt.executeQuery("SELECT * FROM PublicacionDudosa WHERE Titulo = '"+nombrePublicacionNueva+"'");
            while (rs.next())
                existeTitulo = true;
            respuesta.put("existeTitulo", existeTitulo);
            rs.close();
            stmt.close();
            conex.desconectar();
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
            String query = "INSERT INTO Publicacion (Titulo, Tipo, CodigoPublicacion, Lugar, Editorial, FechaInicio, Extraido)"
                    + " VALUES ("
                    + ((newPublicacion.get("titulo") == null) ? null : "'" + StringUtils.stripAccents(newPublicacion.get("titulo").toString()).trim().toUpperCase().replace(".","").replace("-", " ") +"'") + ","
                    + ((newPublicacion.get("tipo") == null) ? null : "'" + newPublicacion.get("tipo") +"'") + ","
                    + ((newPublicacion.get("codigoPublicacion") == null) ? null : "'" + newPublicacion.get("codigoPublicacion") +"'") + ","
                    + ((newPublicacion.get("lugarPublicacion") == null) ? null : "'" + newPublicacion.get("lugarPublicacion") +"'") + ","
                    + ((newPublicacion.get("editorial") == null) ? null : "'" + newPublicacion.get("editorial") +"'") + ","
                    + ((newPublicacion.get("fecha") == null) ? null : "'" + newPublicacion.get("fecha") +"'")+ ","
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
            conex.desconectar();
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
                query = "INSERT INTO PublicacionDudosa (ID,Titulo, Tipo, CodigoPublicacion, Lugar, Editorial, FechaInicio, Extraido,EstadoSistema)"
                    + " VALUES ("
                    + publicacionIndivisualSistema.get("ID")+ ","
                    + ((newPublicacion.get("titulo") == null) ? null : "'" + StringUtils.stripAccents(newPublicacion.get("titulo").toString()).trim().toUpperCase().replace(".","").replace("-", " ") +"'") + ","
                    + ((newPublicacion.get("tipo") == null) ? null : "'" + newPublicacion.get("tipo") +"'") + ","
                    + ((newPublicacion.get("codigoPublicacion") == null) ? null : "'" + newPublicacion.get("codigoPublicacion") +"'") + ","
                    + ((newPublicacion.get("lugar") == null) ? null : "'" + newPublicacion.get("lugar") +"'") + ","
                    + ((newPublicacion.get("editorial") == null) ? null : "'" + newPublicacion.get("editorial") +"'") + ","
                    + ((newPublicacion.get("fechaInicio") == null) ? null : "'" + newPublicacion.get("fechaInicio") +"'")+ ","
                    + "'" + fuenteExtraccion +"'" + ","
                    + "'" + 1 + "')";
                System.out.println("Query insertPublicacionDudosa() " + query);
                stmt.execute(query);
                
            }
            stmt.close();
            conex.desconectar();
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
             conex.desconectar();
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
            conex.desconectar();
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
             conex.desconectar();
        }

          

        return puntos;
    }
	
    
}

