/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;


import Dao.puntosDAO;
import Dao.loginDao;
import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class GuardarInformacion {
    public GuardarInformacion(){
        super();
    }
    public boolean guardarInformacionCVLACDB(String jsonS) throws SQLException{
        JSONParser parser = new JSONParser();
        
        int Bilingue=0,Cientifico=0,Director=0,Doctor=0,Investigador=0,Jefe=0,Administrarivo=0;
        
        String[]meses={"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
         Cone conex = new Cone();
         Connection conexion= conex.getConnection();
        try {
             JSONObject json = (JSONObject) parser.parse(jsonS);
             
             JSONObject datosBasicos=(JSONObject) json.get("datosBasicos");
             JSONArray formacionAcademica=(JSONArray) json.get("formacionAcademica");
             JSONObject publicaciones=(JSONObject) json.get("publicaciones");
             JSONArray areasActuacion=(JSONArray) json.get("areasActuacion");
             JSONArray idiomas=(JSONArray) json.get("idiomas");
             JSONObject libros=(JSONObject) json.get("Libros");
             JSONObject eventos=(JSONObject) json.get("eventos");
             JSONObject capitulos=(JSONObject) json.get("capitulos");
             JSONObject software = (JSONObject) json.get("software");
             JSONObject trabajosDirigidos = (JSONObject) json.get("trabjosDirigidos");
           
             
              Statement stmt1 = conexion.createStatement();
              String sql = "UPDATE Profesor " +
              "SET Categoria='" + datosBasicos.get("categoria") +"', Nacionalidad ='"+datosBasicos.get("nacionalidad") +"', Sexo ='"+datosBasicos.get("sexo") +
              "'where IdPersona='"+ json.get("id")+"'";
              
               stmt1.executeUpdate(sql);
               stmt1.close();  
               
               String categoria=(String) datosBasicos.get("categoria");
               if(categoria.contains("Investigador")){
                   Investigador=1;
               }
               
               
               
               for(int i=0; i<formacionAcademica.size();i++){
                    JSONObject obj=(JSONObject) formacionAcademica.get(i);
                    Statement stmt = null;
                    ResultSet rs;
                    stmt = conexion.createStatement();     
                    rs = stmt.executeQuery("SELECT * FROM  FormacionAcademica where Titulo='"+ obj.get("titulo")+"' AND IdPersona='"+ json.get("id")+"'");                     
                    String cat=(String) obj.get("categoria");
                     if(cat.contains("Doctorado")){
                         Doctor=1;
                    }
                    boolean esta=false;              
                   while (rs.next()) {   
                       esta=true;
                         
                   }
                   
                   if(!esta){
                    int idFormacion=-1;
                    stmt = conexion.createStatement();     
                    rs = stmt.executeQuery("SELECT max(ID) FROM  FormacionAcademica"); 
                    while (rs.next()) {    
                       idFormacion=rs.getInt(1)+1;
                    }
                    Date sqlDate1 = null;
                    Date sqlDate2 = null;
                    
                    String[] fecha= obj.get("fecha").toString().split("-");
               
                    if(fecha.length>=1){
                        String[]mes=fecha[0].split("de");
                        if(mes.length ==2){
                        int contMes=0;
                        for(String mesesitos : meses){
                            if(mesesitos.equalsIgnoreCase(mes[0].trim())){
                               break;
                            }
                            contMes++; 
                        }
                        int ano=Integer.parseInt(mes[1].trim());
                        sqlDate1=new java.sql.Date(ano-1900,contMes,1);
                        sqlDate2 = new java.sql.Date(ano-1900,contMes,1);
                        } 
                    }
                    if(fecha.length>=2){
                        
                        String[]mes=fecha[1].split("de");
                        if(mes.length ==2){
                        int contMes=0;
                        for(String mesesitos : meses){
                            if(mesesitos.equalsIgnoreCase(mes[0].trim())){
                               break;
                            }
                            contMes++; 
                        }
                        int ano=Integer.parseInt(mes[1].trim());
                       
                        sqlDate2 = new java.sql.Date(ano-1900,contMes,1);
                   
                      }
                    } 
                     stmt1 = conexion.createStatement();
                     sql = "INSERT INTO FormacionAcademica " +
                     "(ID,IdPersona,Categoria, Titulo,Universidad,FechaInicio,FechaFin,Descripcion) "+
                     "VALUES ('" + idFormacion + "','"+json.get("id")+"','"+obj.get("categoria") +"','"+obj.get("titulo") +"','"+obj.get("universidad") +"','"+ sqlDate1+"','"+ sqlDate2+"','"+obj.get("descripcion") +"')";
                     System.out.println("Query insert informacion academcia: "+sql);
                     stmt1.executeUpdate(sql);
                     stmt1.close(); 
                    
                   
                      rs.close(); 
                      stmt.close();
                }
      
             }
               
                for(int i=0; i<areasActuacion.size();i++){
                    JSONObject obj=(JSONObject) areasActuacion.get(i);
                    Statement stmt = null;
                    ResultSet rs;
                    stmt = conexion.createStatement();     
                    rs = stmt.executeQuery("SELECT * FROM  AreasActuacion where Nombre='"+ obj.get("area")+"' AND IdPersona='"+ json.get("id")+"'");                     
                    boolean esta=false;              
                   while (rs.next()) {   
                       esta=true;
                         
                   }
                   
                   if(!esta){
                     int idFormacion=-1;
                    stmt = conexion.createStatement();     
                    rs = stmt.executeQuery("SELECT max(ID) FROM  AreasActuacion"); 
                    while (rs.next()) {    
                       idFormacion=rs.getInt(1)+1;
                    }
                     stmt1 = conexion.createStatement();
                     sql = "INSERT INTO AreasActuacion" +
                     "(ID,IdPersona,Nombre) "+
                     "VALUES ('" + idFormacion + "','"+json.get("id")+"','"+obj.get("area") +"')";

                     stmt1.executeUpdate(sql);
                     stmt1.close(); 
                     
                     rs.close(); 
                     stmt.close();
                       
                   }
                }
               
                if(idiomas.size()>=2){
                     Bilingue=1;
                }
                for(int i=0; i< idiomas.size();i++){
                    JSONObject obj=(JSONObject) idiomas.get(i);
                    Statement stmt = null;
                    ResultSet rs;
                    stmt = conexion.createStatement();     
                    rs = stmt.executeQuery("SELECT Idiomas.nombre FROM Idioma_Persona INNER JOIN Idiomas ON Idioma_Persona.IdIdioma=Idiomas.ID "
                    + " where Idioma_Persona.IdPersona='"+ json.get("id")+"'"); 
                    
                    boolean esta=false; 
                     while (rs.next()) {   
                       if(rs.getString(1).equalsIgnoreCase(obj.get("idioma").toString())){
                           esta=true;
                       }
                         
                   }
                     
                     if(!esta){
                         
                         ResultSet rs1;
                         stmt = conexion.createStatement();     
                         rs1 = stmt.executeQuery("SELECT * from Idiomas"
                         + " where nombre='"+ obj.get("idioma")+"'"); 
                         int idIdio=-1;
                          while (rs1.next()) {   
                                idIdio=rs1.getInt(1);
                          }
                          
                          if(idIdio==-1){
                               int idFormacion=-1;
                               stmt = conexion.createStatement();     
                               rs = stmt.executeQuery("SELECT max(ID) FROM  Idiomas"); 
                               while (rs.next()) {    
                                 idFormacion=rs.getInt(1)+1;
                               }
                               stmt1 = conexion.createStatement();
                               sql = "INSERT INTO Idiomas" +
                                "(ID,Nombre) "+
                                "VALUES ('" + idFormacion + "','"+obj.get("idioma")+"')";
                                idIdio=idFormacion;
                                stmt1.executeUpdate(sql);
                                stmt1.close(); 
                          }  
                                stmt1 = conexion.createStatement();
                               sql = "INSERT INTO Idioma_Persona" +
                                "(IdIdioma,IdPersona) "+
                                "VALUES ('" + idIdio + "','"+json.get("id")+"')";

                                stmt1.executeUpdate(sql);
                                stmt1.close(); 
                              
                          
                     }
                }
                 
                  Statement stmt2 = null;
                  ResultSet rs1;
                  stmt2 = conexion.createStatement();     
                  rs1 = stmt2.executeQuery("SELECT * from Medallas where IdPersona='"+ json.get("id")+"'"); 
                  boolean esta= false;
                  while (rs1.next()) {   
                      esta=true;       
                  }
                  puntosDAO dao = new puntosDAO(); 
                  if(!esta){
                     dao.insetarMedallas(Integer.parseInt((String) json.get("id")),Bilingue,Cientifico,Director,Doctor,Investigador,Jefe,Administrarivo);
                  }else{
                      dao.actualizarMedallas(Integer.parseInt((String) json.get("id")),Bilingue,Cientifico,Director,Doctor,Investigador,Jefe,Administrarivo);
                  }
                
                cvlacGuardarPublicaciones(publicaciones,libros, capitulos, eventos, json,software,trabajosDirigidos);
                
                return true;

      }catch (ParseException ex) { 
            Logger.getLogger(GuardarInformacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            conex.desconectar();
        }
    }
    
    private void cvlacGuardarPublicaciones(JSONObject publicaciones,JSONObject libros, JSONObject capitulos, JSONObject eventos, JSONObject json, JSONObject software, JSONObject trabjosDirigidos){
       
        try {

            BeanPublicaciones beanPublicacionesEJB = new BeanPublicaciones();

            JSONObject respuesta_existePublicacion_libros = new JSONObject();
            JSONObject respuesta_existePublicacion_articulos = new JSONObject();
            JSONObject respuesta_existePublicacion_capitulos = new JSONObject();
            JSONObject respuesta_existePublicacion_software = new JSONObject();
            JSONObject respuesta_existePublicacion_trabajosDirigidos = new JSONObject();
            JSONObject respuesta_existePublicacion_eventos = new JSONObject();

            
            JSONArray capitulo=(JSONArray) capitulos.get("capitulos");
            JSONArray libro=(JSONArray) libros.get("libros");
            JSONArray articulos=(JSONArray) publicaciones.get("articulos");
            JSONArray listaSoftware=(JSONArray) software.get("software");
            JSONArray listaTrbajosDirigidos = (JSONArray) trabjosDirigidos.get("trabjosDirigidos");
            JSONArray listaEventos = (JSONArray) eventos.get("eventos");

            
            respuesta_existePublicacion_libros = beanPublicacionesEJB.validarPublicacionesNuevas(libro, json.get("id").toString(),"CvLac");
            
            respuesta_existePublicacion_articulos = beanPublicacionesEJB.validarPublicacionesNuevas(articulos, json.get("id").toString(),"CvLac");

            respuesta_existePublicacion_capitulos = beanPublicacionesEJB.validarPublicacionesNuevas(capitulo, json.get("id").toString(),"CvLac");
            
            respuesta_existePublicacion_software = beanPublicacionesEJB.validarPublicacionesNuevas(listaSoftware, json.get("id").toString(), "CvLac");
            
            respuesta_existePublicacion_trabajosDirigidos = beanPublicacionesEJB.validarPublicacionesNuevas(listaTrbajosDirigidos, json.get("id").toString(), "CvLac");
            
            respuesta_existePublicacion_eventos = beanPublicacionesEJB.validarPublicacionesNuevas(listaEventos, json.get("id").toString(), "CvLac");
            
            System.out.println("FIN cvlacGuardarPublicaciones");
        } catch (Exception ex) {
            Logger.getLogger(GuardarInformacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean guardarInformacionGoogleDB(String jsonS){
         Cone conex = new Cone();
         Connection conexion= conex.getConnection();
         Statement stmt = null;
         ResultSet rs; 
         JSONParser parser = new JSONParser();
        try {
            
            JSONObject respuesta_existePublicacion = new JSONObject();
            JSONObject json = (JSONObject) parser.parse(jsonS);
            JSONArray publicaciones=(JSONArray) json.get("publicaciones");
            
            BeanPublicaciones beanPublicacionesEJB = new BeanPublicaciones();
            loginDao login_dao = new loginDao();
            
            login_dao.guardarImagenGoogle(json.get("id").toString(),json.get("urlImagen").toString());
           
            respuesta_existePublicacion = beanPublicacionesEJB.validarPublicacionesNuevas(publicaciones, json.get("id").toString(),"GoogleScholar");
            
            
            
        } catch (ParseException ex) {
            Logger.getLogger(GuardarInformacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GuardarInformacion.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return true;
    }
    
     public boolean guardarInformacionReserchDB(String jsonS){
         Cone conex = new Cone();
         Connection conexion= conex.getConnection();
         Statement stmt = null;
         ResultSet rs; 
         JSONParser parser = new JSONParser();
         
        try {
            
            JSONObject respuesta_existePublicacion = new JSONObject();
            JSONObject json = (JSONObject) parser.parse(jsonS);
            JSONArray publicaciones=(JSONArray) json.get("publicaciones");
            
            BeanPublicaciones beanPublicacionesEJB = new BeanPublicaciones();
            System.out.println("--PUBLICACIONES QUE LLEGAN DE RESEACHR: "+publicaciones.toJSONString());
            respuesta_existePublicacion = beanPublicacionesEJB.validarPublicacionesNuevas(publicaciones, json.get("id").toString(),"ResearchGate");

            
           
        } catch (ParseException ex) {
            Logger.getLogger(GuardarInformacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GuardarInformacion.class.getName()).log(Level.SEVERE, null, ex);
        }
         return true;
     }
}
