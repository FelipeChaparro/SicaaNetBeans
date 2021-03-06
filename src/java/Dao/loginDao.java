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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


/**
 *
 * @author hariasor
 */
public class loginDao {
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public loginDao(){
        super();
    }
    
    public JSONObject login_user (String user_name, String user_pass) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 100);
        respuesta.put("description", "Usuario o contraseña incorrecta");
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from datos_usuarios where user_correo='"+user_name+"'");
            rs = stmt.executeQuery("SELECT * FROM datos_usuarios WHERE user_correo='"+user_name+"'");
            while (rs.next()) {
                String pass = rs.getString("user_password");
                System.out.println("DB Pass: "+pass);
                if (pass.equals(user_pass)) {
                    respuesta.put("code", 0);
                    respuesta.put("id", rs.getString("id"));
                    respuesta.put("description", "Operacion exitosa");
                }
            }
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("Retorno: "+respuesta.toString());
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Retorno: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getRolesByUserMail (String user_id) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        JSONArray arregloRoles = new JSONArray();
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from roles_usuarios where user_id = '"+user_id+"'");
            rs = stmt.executeQuery("select * from roles_usuarios where user_id = '"+user_id+"'");
            while (rs.next()) {
                JSONObject rol = new JSONObject();
                rol.put("id", rs.getString("rol_id").toString());
                rol.put("label", "");
                rol.put("referencia", "");
                arregloRoles.add(rol);
            }
            respuesta.put("roles", arregloRoles);
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
    
    public JSONObject getRoleDescriptionByID (JSONObject user_data, String SERVER_URL) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            String rolesABuscar = "";
            JSONObject rolIndividual = new JSONObject();
            JSONArray arregloRoles = new JSONArray();
            
            JSONArray roles = (JSONArray) user_data.get("roles");
            roles.toArray();
            for (int i=0;i<roles.size();i++) {
               rolIndividual = (JSONObject) roles.get(i);
               rolesABuscar += rolIndividual.get("id");
               if (i != roles.size()-1)
                   rolesABuscar += ", ";
            }
            System.out.println("ID roles: "+rolesABuscar);
            
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from roles where rol_id in ("+rolesABuscar+")");
            rs = stmt.executeQuery("select * from roles where rol_id in ("+rolesABuscar+")");
            while (rs.next()) {
                JSONObject rolEncontrado = new JSONObject();
                rolEncontrado.put("id", rs.getString("rol_id"));
                rolEncontrado.put("label", rs.getString("label"));
                rolEncontrado.put("referencia", rs.getString("referencia").toString().replace("#URL",SERVER_URL));
                System.out.println("Role de BD: "+rolEncontrado.toJSONString());
                arregloRoles.add(rolEncontrado);
            }
            respuesta.put("roles", arregloRoles);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getRoleDescriptionByID: "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getRoleDescriptionByID: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject save_token (String user_name, String token) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            stmt = conexion.createStatement();
            String query = "INSERT INTO sesiones_usuarios (user_correo, user_token, fecha_create, fecha_last_acces) VALUES('"+user_name+"', '"+token+"', '"+getFecha()+"', '"+getFecha()+"') ON DUPLICATE KEY UPDATE user_token='"+token+"', fecha_last_acces='"+getFecha()+"'";
            System.out.println("Query: "+query);
            stmt.executeUpdate(query);
            stmt.close();
            //conex.desconectar();
            return respuesta;
        } catch (SQLException e) {
            System.out.println("Errror DB: "+e.toString());
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("Retorno: "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    private static String getFecha() {
        java.util.Date fecha = new java.util.Date();
        return dateFormat.format(fecha.getTime());
    }

    public JSONObject getDatosBasicos (String user_id) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONObject datosBasicos = new JSONObject();
        int idDepartamento = 0;
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select P.IdPersona as IdPersona,PE.Nombre as Nombre, P.Nacionalidad as Nacionalidad, P.Categoria as Categoria, P.IdDepartamento as IdDepartamento,PE.URLGoogleScholar AS Imagen from Profesor P\n" +
                "inner join Persona PE on PE.ID=P.IdPersona\n" +
                "where IdPersona="+user_id);
            rs = stmt.executeQuery("select P.IdPersona as IdPersona,PE.Nombre as Nombre, P.Nacionalidad as Nacionalidad, P.Categoria as Categoria, P.IdDepartamento as IdDepartamento,PE.URLGoogleScholar AS Imagen from Profesor P\n" +
                "inner join Persona PE on PE.ID=P.IdPersona\n" +
                "where IdPersona="+user_id);
            while (rs.next()) {
                datosBasicos.put("id", rs.getString("IdPersona"));
                datosBasicos.put("nombre", rs.getString("Nombre"));
                datosBasicos.put("nacionalidad", rs.getString("Nacionalidad"));
                datosBasicos.put("categoria", rs.getString("Categoria")==null?"":rs.getString("Categoria"));
                datosBasicos.put("urlImagen", rs.getString("Imagen")==null?null:rs.getString("Imagen"));
                idDepartamento = Integer.parseInt(rs.getString("IdDepartamento"));
            }
            rs.close();
            stmt.close();
            //conex.desconectar();
            
            Cone conex2 = new Cone();
            Connection conexion2= conex2.getConnection();
            Statement stmt2 = null;
            ResultSet rs2;
            stmt2 = conexion2.createStatement();
            System.out.println("select D.id as IDDepratmento, D.Nombre as NombreDespartamento, F.ID as IDFacultad, F.Nombre as NombreFacultad from Departamento D inner join Facultad F on F.id = D.IDfACULTAD where D.ID ="+idDepartamento);
            rs2 = stmt2.executeQuery("select D.Nombre as NombreDespartamento, F.Nombre as NombreFacultad from Departamento D inner join Facultad F on F.id = D.IDfACULTAD where D.ID ="+idDepartamento);
            while (rs2.next()) {
                datosBasicos.put("nombreDepartamento", rs2.getString("NombreDespartamento"));
                datosBasicos.put("nombreFacultad", rs2.getString("NombreFacultad"));
            }
            rs2.close();
            stmt2.close();
            //conex2.desconectar();
            
            respuesta.put("IdDepartamento", idDepartamento);
            respuesta.put("datosBasicos", datosBasicos);
            
            System.out.println("getDatosBasicos(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getDatosBasicos(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getDatosBasicos(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getMedallas (String user_id) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONArray medallas = new JSONArray();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        
        int numeroFilasPorUsuario = 0;
        
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            
            String query = "select count(*) as cuenta from Medallas where IdPersona ="+user_id;
            rs = stmt.executeQuery(query);
            
            while (rs.next())
                numeroFilasPorUsuario = rs.getInt("cuenta");                
            
            if (numeroFilasPorUsuario == 1) {
                System.out.println("select * from Medallas where IdPersona ="+user_id);
                rs = stmt.executeQuery("select * from Medallas where IdPersona ="+user_id);
                while (rs.next()) {
                    JSONObject medalla1 = new JSONObject();
                    medalla1.put("nombreMedalla", "Bilingue");
                    medalla1.put("acomplished", rs.getString("Bilingue").isEmpty()?null:rs.getString("Bilingue"));

                    JSONObject medalla2 = new JSONObject();
                    medalla2.put("nombreMedalla", "Cientifico");
                    medalla2.put("acomplished", rs.getString("Cientifico").isEmpty()?null:rs.getString("Cientifico"));

                    JSONObject medalla3 = new JSONObject();
                    medalla3.put("nombreMedalla", "Director");
                    medalla3.put("acomplished",rs.getString("Director").isEmpty()?null:rs.getString("Director"));

                    JSONObject medalla4 = new JSONObject();
                    medalla4.put("nombreMedalla", "Doctor");
                    medalla4.put("acomplished", rs.getString("Doctor").isEmpty()?null:rs.getString("Doctor"));

                    JSONObject medalla5 = new JSONObject();
                    medalla5.put("nombreMedalla", "Investigador");
                    medalla5.put("acomplished", rs.getString("Investigador").isEmpty()?null:rs.getString("Investigador"));

                    JSONObject medalla6 = new JSONObject();
                    medalla6.put("nombreMedalla", "Administrativo");
                    medalla6.put("acomplished", rs.getString("Administrativo").isEmpty()?null:rs.getString("Administrativo"));

                    JSONObject medalla7 = new JSONObject();
                    medalla7.put("nombreMedalla", "Jefe");
                    medalla7.put("acomplished", rs.getString("Jefe").isEmpty()?null:rs.getString("Jefe"));

                    medallas.add(medalla1);
                    medallas.add(medalla2);
                    medallas.add(medalla3);
                    medallas.add(medalla4);
                    medallas.add(medalla5);
                    medallas.add(medalla6);
                    medallas.add(medalla7);
                }
            }
            else {
                JSONObject medalla1 = new JSONObject();
                medalla1.put("nombreMedalla", "Bilingue");
                medalla1.put("acomplished", null);

                JSONObject medalla2 = new JSONObject();
                medalla2.put("nombreMedalla", "Cientifico");
                medalla2.put("acomplished", null);

                JSONObject medalla3 = new JSONObject();
                medalla3.put("nombreMedalla", "Director");
                medalla3.put("acomplished",null);

                JSONObject medalla4 = new JSONObject();
                medalla4.put("nombreMedalla", "Doctor");
                medalla4.put("acomplished", null);

                JSONObject medalla5 = new JSONObject();
                medalla5.put("nombreMedalla", "Investigador");
                medalla5.put("acomplished", null);

                JSONObject medalla6 = new JSONObject();
                medalla6.put("nombreMedalla", "Administrativo");
                medalla6.put("acomplished", null);

                JSONObject medalla7 = new JSONObject();
                medalla7.put("nombreMedalla", "Jefe");
                medalla7.put("acomplished", null);

                medallas.add(medalla1);
                medallas.add(medalla2);
                medallas.add(medalla3);
                medallas.add(medalla4);
                medallas.add(medalla5);
                medallas.add(medalla6);
                medallas.add(medalla7);
            }
                    
            respuesta.put("medallas", medallas);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getMedallas(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getMedallas(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getMedallas(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getFormacionAcademica (String user_id) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONArray formacionAcademica = new JSONArray();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from FormacionAcademica where IdPersona="+user_id);
            rs = stmt.executeQuery("select * from FormacionAcademica where IdPersona="+user_id);
            while (rs.next()) {
                JSONObject elementoFormacion = new JSONObject();
                elementoFormacion.put("titulo", rs.getString("Titulo"));
                elementoFormacion.put("universidad", rs.getString("Universidad"));
                elementoFormacion.put("fechaInicio", rs.getString("FechaInicio"));
                elementoFormacion.put("fechaFin", rs.getString("FechaFin"));
                elementoFormacion.put("descripcion", rs.getString("Descripcion"));
                
                formacionAcademica.add(elementoFormacion);
            }
            respuesta.put("formacionAcademica", formacionAcademica);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getFormacionAcademica(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getFormacionAcademica(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getFormacionAcademica(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getPublicacionesRecientes (String user_id,int numero_publicaciones) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONArray publicacionesRecientes = new JSONArray();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            String publicacionesABuscar = "";
            int numPublicaciones = 0;
            int currentNumPublicaciones = 0;
            
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            
            /**
             * Obtengo numero de publicaciones
             */
            stmt = conexion.createStatement();
            System.out.println("SELECT count(*) as numPublicaciones from Persona_Publicacion where IdPersona="+user_id);
            rs = stmt.executeQuery("SELECT count(*) as numPublicaciones from Persona_Publicacion where IdPersona ="+user_id);
            while (rs.next()) {
                numPublicaciones = Integer.parseInt(rs.getString("numPublicaciones").toString());
            }
            rs.close();
            stmt.close();
            //conex.desconectar();
            
            /**
             * Obtengo las publicaciones de un profesor
             */
            Cone conex2 = new Cone();
            Connection conexion2= conex2.getConnection();
            Statement stmt2 = null;
            ResultSet rs2;
            stmt2 = conexion2.createStatement();
            String query = "select * from Persona_Publicacion where IdPersona="+user_id+" and EstadoSistema = 1 and EstadoPublicacion = 'Verificado'";
            System.out.println(query);
            rs2 = stmt2.executeQuery(query);
            for (int i=0;i<numPublicaciones && rs2.next();i++) {
                
                if (i > 0)
                    publicacionesABuscar += ", ";
                publicacionesABuscar += rs2.getString("IdPublicacion");
                
                         
            }
            rs2.close();
            stmt2.close();
            //conex2.desconectar();
            
            /**
             * Obtengo el detalle de las publicaciones
             */
            if (!publicacionesABuscar.equalsIgnoreCase("")) {
                Cone conex3 = new Cone();
                Connection conexion3= conex3.getConnection();
                Statement stmt3 = null;
                ResultSet rs3;
                stmt3 = conexion3.createStatement();
                System.out.println("select * from Publicacion where ID in ("+publicacionesABuscar+") order by FechaInicio");
                rs3 = stmt3.executeQuery("select * from Publicacion where ID in ("+publicacionesABuscar+") order by FechaInicio");
                while (rs3.next() && currentNumPublicaciones <= numero_publicaciones-1) {
                    JSONObject elementoPublicacion = new JSONObject();
                    elementoPublicacion.put("titulo", rs3.getString("Titulo"));
                    elementoPublicacion.put("fechaInicio", rs3.getString("FechaInicio"));
                    currentNumPublicaciones++;

                    publicacionesRecientes.add(elementoPublicacion);
                }
                rs3.close();
                stmt3.close();
                //conex3.desconectar();
            }
            respuesta.put("publicacionesRecientes", publicacionesRecientes);
            
            
            System.out.println("getPublicacionesRecientes(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getPublicacionesRecientes(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getPublicacionesRecientes(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getPodioPublicaciones (int maximoPodioPublicaciones, String user_id_departamento) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONArray podioPrograma = new JSONArray();
        JSONArray podioFacultad = new JSONArray();
        JSONArray podioUniversidad = new JSONArray();
        
        String user_id_facultad = "-1";
        
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();            
            System.out.println("select F.ID as IDFacultad from Departamento D inner join Facultad F on F.id = D.IDfACULTAD where D.ID ="+user_id_departamento);
            rs = stmt.executeQuery("select F.ID as IDFacultad from Departamento D inner join Facultad F on F.id = D.IDfACULTAD where D.ID ="+user_id_departamento);

            while (rs.next()) {
                user_id_facultad = rs.getString("IDFacultad").toString();
            }
            rs.close();
            stmt.close();  
            //conex.desconectar();
            
            /**
             * PODIO PROGRAMA
             */
            Cone conex2 = new Cone();
            Connection conexion2= conex2.getConnection();
            Statement stmt2 = null;
            ResultSet rs2;
            stmt2 = conexion2.createStatement();
            System.out.println("select PP.Puntos,PE.Nombre as NombreProfesor,D.Nombre as NombreDepartamento from PuntosPublicacion PP \n" +
                " inner join Profesor P on PP.IdPersona=P.IdPersona\n" +
                " inner join Persona PE on PE.ID=P.IdPersona\n"+
                " inner join Departamento D on D.ID=P.IdDepartamento\n" +
                " inner join Facultad F on F.ID=D.IDfACULTAD\n" +
                " where D.id = "+user_id_departamento+"\n"+
                " order by PP.Puntos desc\n" +
                " LIMIT "+maximoPodioPublicaciones);
            rs2 = stmt2.executeQuery("select PP.Puntos,PE.Nombre as NombreProfesor,D.Nombre as NombreDepartamento from PuntosPublicacion PP \n" +
                " inner join Profesor P on PP.IdPersona=P.IdPersona\n" +
                " inner join Persona PE on PE.ID=P.IdPersona\n"+
                " inner join Departamento D on D.ID=P.IdDepartamento\n" +
                " inner join Facultad F on F.ID=D.IDfACULTAD\n" +
                " where D.id = "+user_id_departamento+"\n"+
                " order by PP.Puntos desc\n" +
                " LIMIT "+maximoPodioPublicaciones);
            while (rs2.next()) {
                JSONObject puesto = new JSONObject();
                puesto.put("nombre", rs2.getString("NombreProfesor"));
                puesto.put("puntos", rs2.getString("Puntos"));
                puesto.put("programa", rs2.getString("NombreDepartamento"));
                podioPrograma.add(puesto);
            }
            rs2.close();
            stmt2.close(); 
            //conex2.desconectar();
            
            /**
             * PODIO FACULTAD
             */
            Cone conex3 = new Cone();
            Connection conexion3= conex3.getConnection();
            Statement stmt3 = null;
            ResultSet rs3;
            stmt3 = conexion3.createStatement();
            System.out.println("select PP.Puntos,PE.Nombre as NombreProfesor,F.nombre as NombreFacultad from PuntosPublicacion PP \n" +
                " inner join Profesor P on PP.IdPersona=P.IdPersona\n" +
                " inner join Persona PE on PE.ID=P.IdPersona\n"+
                " inner join Departamento D on D.ID=P.IdDepartamento\n" +
                " inner join Facultad F on F.ID=D.IDfACULTAD "+
                " where F.id = "+user_id_facultad+" order by PP.Puntos desc "+" LIMIT "+maximoPodioPublicaciones);
            rs3 = stmt3.executeQuery("select PP.Puntos,PE.Nombre as NombreProfesor,F.nombre as NombreFacultad from PuntosPublicacion PP \n" +
                " inner join Profesor P on PP.IdPersona=P.IdPersona\n" +
                " inner join Persona PE on PE.ID=P.IdPersona\n"+
                " inner join Departamento D on D.ID=P.IdDepartamento\n" +
                " inner join Facultad F on F.ID=D.IDfACULTAD "+
                " where F.id = "+user_id_facultad+" order by PP.Puntos desc "+" LIMIT "+maximoPodioPublicaciones);           
            while (rs3.next()) {
                JSONObject puesto = new JSONObject();
                puesto.put("nombre", rs3.getString("NombreProfesor"));
                puesto.put("puntos", rs3.getString("Puntos"));
                puesto.put("facultad", rs3.getString("NombreFacultad"));
                podioFacultad.add(puesto);
            }
            rs3.close();
            stmt3.close();
            //conex3.desconectar();
            
            /**
             * PODIO UNIVERSIDAD
             */
            Cone conex4 = new Cone();
            Connection conexion4= conex4.getConnection();
            Statement stmt4 = null;
            ResultSet rs4;
            stmt4 = conexion4.createStatement();
            System.out.println("select PP.Puntos,PE.Nombre as NombreProfesor,F.nombre as NombreFacultad from PuntosPublicacion PP \n" +
                " inner join Profesor P on PP.IdPersona=P.IdPersona\n" +
                " inner join Persona PE on PE.ID=P.IdPersona\n"+
                " inner join Departamento D on D.ID=P.IdDepartamento\n" +
                " inner join Facultad F on F.ID=D.IDfACULTAD\n" +
                " order by PP.Puntos desc\n" +
                " limit "+maximoPodioPublicaciones);
            rs4 = stmt4.executeQuery("select PP.Puntos,PE.Nombre as NombreProfesor,F.nombre as NombreFacultad from PuntosPublicacion PP \n" +
                " inner join Profesor P on PP.IdPersona=P.IdPersona\n" +
                " inner join Persona PE on PE.ID=P.IdPersona\n"+
                " inner join Departamento D on D.ID=P.IdDepartamento\n" +
                " inner join Facultad F on F.ID=D.IDfACULTAD\n" +
                " order by PP.Puntos desc\n" +
                " limit "+maximoPodioPublicaciones);
            while (rs4.next()) {
                JSONObject puesto = new JSONObject();
                puesto.put("nombre", rs4.getString("NombreProfesor"));
                puesto.put("puntos", rs4.getString("Puntos"));
                puesto.put("facultad", rs4.getString("NombreFacultad"));
                podioUniversidad.add(puesto);
            }
            rs4.close();
            stmt4.close(); 
            //conex4.desconectar();
            
            
            respuesta.put("podioPublicacionPrograma", podioPrograma);
            respuesta.put("podioPublicacionFacultad", podioFacultad);
            respuesta.put("podioPublicacionUniversidad", podioUniversidad);
         
            System.out.println("getPodioPublicaciones(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getPodioPublicaciones(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getPodioPublicaciones(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getAreasActuacion (String user_id) throws SQLException{
        JSONObject respuesta = new JSONObject();
        JSONArray areasActuacion = new JSONArray();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select * from AreasActuacion where IdPersona="+user_id);
            rs = stmt.executeQuery("select * from AreasActuacion where IdPersona="+user_id);
            while (rs.next()) {
                JSONObject area = new JSONObject();
                area.put("nombre", rs.getString("Nombre"));
                areasActuacion.add(area);
            }
            
            respuesta.put("areasActuacion", areasActuacion);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAreasActuacion(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAreasActuacion(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getAreasActuacion(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    /**
     * METODO INCOMPLETO -> DEBE REVISAR EN LA DB QUE EL TOKEN NO HAYA EXPIRADO
     * @param token
     * @return
     * @throws SQLException 
     */
    public JSONObject validar_user_tokenDAO (String token) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        try {
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
            
            System.out.println("validar_user_tokenDAO(): "+respuesta.toString());
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("validar_user_tokenDAO(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    } 
    
    public JSONObject getUserIDByToken (String token) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 102);
        respuesta.put("description", "No se encontro el id del usuario");
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            System.out.println("select DU.id as idUsuario,DU.user_correo as correroUsuario \n" +
                "  from sesiones_usuarios SU inner join datos_usuarios DU on SU.user_correo = DU.user_correo \n" +
                "  where SU.user_token ='"+token+"'");
            rs = stmt.executeQuery("select DU.id as idUsuario,DU.user_correo as correroUsuario \n" +
                "  from sesiones_usuarios SU inner join datos_usuarios DU on SU.user_correo = DU.user_correo \n" +
                "  where SU.user_token ='"+token+"'");
            while (rs.next()) {
                respuesta.put("code", 0);
                respuesta.put("description", "Operacion exitosa");
                respuesta.put("id", rs.getString("idUsuario"));
            }
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("Retorno: "+respuesta.toString());
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("validar_user_tokenDAO(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    } 
    
    public JSONObject guardarImagenGoogle(String user_id, String urlImagen) throws SQLException {
        JSONObject respuesta_db = new JSONObject();
        Statement stmt = null;
        Cone cone = new Cone();
        String query;
        try {
            Connection mConexion = cone.getConnection();
            stmt = mConexion.createStatement(); 
            query = "UPDATE Persona SET URLGoogleScholar='"+ urlImagen +"' WHERE ID='" + user_id + "'";
            System.out.println("Query guardarImagenGoogle: " + query);
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
    
    public JSONObject getPuntajeProfesor (String user_id) throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        int puntaje = 0;
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            String query = "SELECT Puntos FROM PuntosPublicacion WHERE IdPersona ="+user_id;
            System.out.println("Query getPuntajeProfesor: "+query);
            rs = stmt.executeQuery(query);
            
            while (rs.next())
                puntaje = rs.getInt("Puntos");
            
            respuesta.put("puntos", puntaje);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAreasActuacion(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("puntos", puntaje);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getPuntajeProfesor(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("puntos", puntaje);
            respuesta.put("description", "Error de sistema");
            System.out.println("getPuntajeProfesor(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getAllFacultades () throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        
        JSONArray listaFacultades = new JSONArray();
        
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            String query = "SELECT ID, Nombre FROM facultad where publicaciones = 1 order by Nombre";
            System.out.println("Query getAllFacultades: "+query);
            rs = stmt.executeQuery(query);
 
            while (rs.next()) {
                JSONObject facultadIndividual = new JSONObject();
                facultadIndividual.put("ID",rs.getInt("ID"));
                facultadIndividual.put("Nombre", rs.getString("Nombre"));
                listaFacultades.add(facultadIndividual);
            }
            
            respuesta.put("facultades", listaFacultades);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAllFacultades(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAllFacultades(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getAllFacultades(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
    
    public JSONObject getAllDepartamentos () throws SQLException{
        JSONObject respuesta = new JSONObject();
        respuesta.put("code", 0);
        respuesta.put("description", "Operacion exitosa");
        
        JSONArray listaDepartamentos = new JSONArray();
        
        try {
            Cone conex = new Cone();
            Connection conexion= conex.getConnection();
            Statement stmt = null;
            ResultSet rs;
            stmt = conexion.createStatement();
            String query = "SELECT D.ID, D.Nombre " +
                "FROM departamento D inner join facultad F on F.ID = D.IdFacultad " +
                "where F.publicaciones = 1 and D.publicaciones = 1 " +
                "order by F.Nombre,D.Nombre";
            System.out.println("Query getAllDepartamentos: "+query);
            rs = stmt.executeQuery(query);
 
            while (rs.next()) {
                JSONObject departamentoIndividual = new JSONObject();
                departamentoIndividual.put("ID",rs.getInt("ID"));
                departamentoIndividual.put("Nombre", rs.getString("Nombre"));
                listaDepartamentos.add(departamentoIndividual);
            }
            
            respuesta.put("departamentos", listaDepartamentos);
            rs.close();
            stmt.close();
            //conex.desconectar();
            System.out.println("getAllDepartamentos(): "+respuesta.toString());
            return respuesta;
        } catch (SQLException e) {
            respuesta.put("code", 9999);
            respuesta.put("description", "Error en base de datos");
            System.out.println("getAllDepartamentos(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        } catch (Exception e) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getAllDepartamentos(): "+respuesta.toString());
            Logger.getLogger(loginDao.class.getName()).log(Level.SEVERE, null, e);
            return respuesta;
        }
    }
}
