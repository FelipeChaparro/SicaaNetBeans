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
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


/**
 *
 * @author hp
 */
public class profesoresDAO {
    
    public profesoresDAO(){
        super();
    }
    
    public JSONObject getInfoBasicaProfesoresByDepartamento (String idDepartamento) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONArray profesoresArray = new JSONArray();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            String query = "select PE.Nombre as nombre, PE.URLGoogleScholar as urlImagen, PE.ID as id, PF.Categoria as categoria," +
                " PU.Puntos as puntos,sub.numeroPublicaciones" +
                " from (" +
                "   select count(*) as numeroPublicaciones, PP.IdPersona as id" +
                "   from persona_publicacion PP" +
                "   inner join profesor PF on PF.IdPersona = PP.IdPersona" +
                "   where PP.EstadoPublicacion = 'Verificado' and PP.EstadoSistema = 1 and PF.IdDepartamento ="+idDepartamento +
                "   group by PP.IdPersona " +
                " ) sub," +
                " profesor PF" +
                " inner join persona PE on PE.ID = PF.IdPersona" +
                " inner join puntospublicacion PU on PU.IdPersona = PE.ID" +
                " where PE.Registrado = 1 and PE.EstadoSistema = 1 and sub.id=PE.ID and PF.IdDepartamento ="+idDepartamento;
            System.out.println("Query getInfoBasicaProfesoresByDepartamento: "+query);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                JSONObject profesorIndvidual = new JSONObject();
                profesorIndvidual.put("nombre", rs.getString("nombre"));
                profesorIndvidual.put("urlImagen", rs.getString("urlImagen"));
                profesorIndvidual.put("id", rs.getString("id"));
                profesorIndvidual.put("categoria", rs.getString("categoria"));
                profesorIndvidual.put("puntos", rs.getInt("puntos"));
                profesorIndvidual.put("numeroPublicaciones", rs.getInt("numeroPublicaciones"));
                
                profesoresArray.add(profesorIndvidual);
            }
            respuesta.put("profesores", profesoresArray);
            rs.close();
            stmt.close();
            System.out.println("getInfoBasicaProfesoresByDepartamentoDAO(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getInfoBasicaProfesoresByDepartamentoDAO(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getInfoBasicaProfesoresByDepartamentoDAO(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getInfoBasicaProfesoresByFacultad (String idFacultad) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONArray profesoresArray = new JSONArray();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            String query = "select PE.Nombre as nombre, PE.URLGoogleScholar as urlImagen, PE.ID as id, PF.Categoria as categoria, " +
                "PU.Puntos as puntos,sub.numeroPublicaciones " +
                "from ( " +
                "	select count(*) as numeroPublicaciones, PP.IdPersona as id " +
                "	from persona_publicacion PP " +
                "    inner join profesor PF on PF.IdPersona = PP.IdPersona " +
                "    inner join departamento D on D.ID = PF.IdDepartamento " +
                "	inner join facultad F on D.IdFacultad = F.ID " +
                "	where PP.EstadoPublicacion = 'Verificado' and PP.EstadoSistema = 1 and F.ID ="+idFacultad+
                "	group by PP.IdPersona " +
                ") sub, " +
                "profesor PF " +
                "inner join persona PE on PE.ID = PF.IdPersona " +
                "inner join puntospublicacion PU on PU.IdPersona = PE.ID " +
                "inner join departamento D on D.ID = PF.IdDepartamento " +
                "inner join facultad F on D.IdFacultad = F.ID " +
                "where PE.Registrado = 1 and PE.EstadoSistema = 1 and sub.id=PE.ID and F.ID ="+idFacultad;
            System.out.println("Query getInfoBasicaProfesoresByFacultad: "+query);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                JSONObject profesorIndvidual = new JSONObject();
                profesorIndvidual.put("nombre", rs.getString("nombre"));
                profesorIndvidual.put("urlImagen", rs.getString("urlImagen"));
                profesorIndvidual.put("id", rs.getString("id"));
                profesorIndvidual.put("categoria", rs.getString("categoria"));
                profesorIndvidual.put("puntos", rs.getInt("puntos"));
                profesorIndvidual.put("numeroPublicaciones", rs.getInt("numeroPublicaciones"));
                
                profesoresArray.add(profesorIndvidual);
            }
            respuesta.put("profesores", profesoresArray);
            rs.close();
            stmt.close();
            System.out.println("getInfoBasicaProfesoresByFacultad(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getInfoBasicaProfesoresByFacultad(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getInfoBasicaProfesoresByFacultad(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getInfoBasicaProfesoresByNombre (String nombre) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONArray profesoresArray = new JSONArray();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            String[] nombreSplit = nombre.split(" ");
            String likeQuery = "(PE.Nombre like '%"+nombreSplit[0]+"%'";
            
            int tamanoNombreSplit = nombreSplit.length;
            for (int i=1;i<tamanoNombreSplit;i++)
                likeQuery += " or PE.Nombre like '%"+nombreSplit[i]+"%'";
            likeQuery += ")";
            
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            String query = "select PE.Nombre as nombre, PE.URLGoogleScholar as urlImagen, PE.ID as id, PF.Categoria as categoria, " +
                "PU.Puntos as puntos,sub.numeroPublicaciones " +
                "from ( " +
                "	select count(*) as numeroPublicaciones, PP.IdPersona as id " +
                "	from persona_publicacion PP " +
                "	inner join profesor PF on PF.IdPersona = PP.IdPersona " +
                "	inner join persona PE on PE.ID = PF.IdPersona " +
                "	where PP.EstadoPublicacion = 'Verificado' and PP.EstadoSistema = 1 and "+likeQuery+
                "	group by PE.Nombre " +
                ") sub, " +
                "profesor PF " +
                "inner join persona PE on PE.ID = PF.IdPersona " +
                "inner join puntospublicacion PU on PU.IdPersona = PE.ID " +
                "where PE.Registrado = 1 and PE.EstadoSistema = 1 and sub.id=PE.ID and "+likeQuery;
            System.out.println("Query getInfoBasicaProfesoresByNombre: "+query);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                JSONObject profesorIndvidual = new JSONObject();
                profesorIndvidual.put("nombre", rs.getString("nombre"));
                profesorIndvidual.put("urlImagen", rs.getString("urlImagen"));
                profesorIndvidual.put("id", rs.getString("id"));
                profesorIndvidual.put("categoria", rs.getString("categoria"));
                profesorIndvidual.put("puntos", rs.getInt("puntos"));
                profesorIndvidual.put("numeroPublicaciones", rs.getInt("numeroPublicaciones"));
                
                profesoresArray.add(profesorIndvidual);
            }
            respuesta.put("profesores", profesoresArray);
            rs.close();
            stmt.close();
            System.out.println("getInfoBasicaProfesoresByNombre(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getInfoBasicaProfesoresByNombre(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getInfoBasicaProfesoresByNombre(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getNumeroPublicacionesXCategoriaByProfesorId (String idProfesor) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONObject totalPublicaciones = new JSONObject();
        
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            String tipo = "";
            
            totalPublicaciones.put("totalArticulos", 0);
            totalPublicaciones.put("totalCapitulos", 0);
            totalPublicaciones.put("totalConferencias", 0);
            totalPublicaciones.put("totalLibros", 0);
            totalPublicaciones.put("totalSoftwares", 0);
            totalPublicaciones.put("totalTrabajosDirigidos", 0);
            
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            
            String query = "select count(*) total, P.Tipo from persona_publicacion PP inner join publicacion P on P.ID=PP.IdPublicacion " +
                "where PP.EstadoPublicacion = 'Verificado' and PP.EstadoSistema = 1 and PP.IdPersona ="+idProfesor+
                " group by P.Tipo order by P.Tipo";
            System.out.println("Query getNumeroPublicacionesXCategoriaByProfesorId: "+query);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                tipo = rs.getString("Tipo");
                
                switch(tipo) {
                    case "articulo":
                        totalPublicaciones.put("totalArticulos", rs.getInt("total"));
                    break;
                    case "capitulo":
                        totalPublicaciones.put("totalCapitulos", rs.getInt("total"));
                    break;
                    case "conferencia":
                        totalPublicaciones.put("totalConferencias", rs.getInt("total"));
                    break;
                    case "libro":
                        totalPublicaciones.put("totalLibros", rs.getInt("total"));
                    break;
                    case "software":
                        totalPublicaciones.put("totalSoftwares", rs.getInt("total"));
                    break;
                    case "trabajo dirigido":
                        totalPublicaciones.put("totalTrabajosDirigidos", rs.getInt("total"));
                    break;
                }

            }
            respuesta.put("totalPublicaciones", totalPublicaciones);
            rs.close();
            stmt.close();
            System.out.println("getNumeroPublicacionesXCategoriaByProfesorId(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getNumeroPublicacionesXCategoriaByProfesorId(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getNumeroPublicacionesXCategoriaByProfesorId(): "+respuesta.toString());
            Logger.getLogger(parametrosSistemaDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
}
