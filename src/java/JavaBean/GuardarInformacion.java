/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;


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
        try {
             JSONObject json = (JSONObject) parser.parse(jsonS);
             //System.out.println(json);
       
             JSONObject datosBasicos=(JSONObject) json.get("datosBasicos");
             JSONArray formacionAcademica=(JSONArray) json.get("formacionAcademica");
             JSONObject publicaciones=(JSONObject) json.get("publicaciones");
             
             int identificador=-1;
              Cone conex = new Cone();
              Connection conexion= conex.getConnection();
              Statement stmt = null;
              ResultSet rs;
                stmt = conexion.createStatement();     
                rs = stmt.executeQuery("SELECT * FROM  Profesores where Nombre='"+datosBasicos.get("nombre")+"'");                     
                 
                boolean esta=false;               
                while (rs.next()) {               
                  esta=true;           
                }
                
                rs.close(); 
                stmt.close();
                
                if(!esta){
                stmt = conexion.createStatement();     
                rs = stmt.executeQuery("SELECT max(ID) FROM  Profesores"); 
                 while (rs.next()) {               
    
                     identificador=rs.getInt(1)+1;
                     Statement stmt1 = conexion.createStatement();
                     String sql = "INSERT INTO Profesores " +
                     "(ID, Nombre,Categoria,Nacionalidad,Sexo) VALUES ('" + identificador + "', '" + datosBasicos.get("nombre") + "','" + datosBasicos.get("categoria") + "','" + datosBasicos.get("nacionalidad") + "','" + datosBasicos.get("sexo") + "')";

                     stmt1.executeUpdate(sql);
                     stmt1.close();  
                     
                }
                rs.close(); 
                stmt.close();
                
                for(int i=0; i<formacionAcademica.size();i++){
                    JSONObject obj=(JSONObject) formacionAcademica.get(i);
                    int idFormacion=-1;
                    stmt = conexion.createStatement();     
                    rs = stmt.executeQuery("SELECT max(ID) FROM  FormacionAcademica"); 
                    while (rs.next()) {    
                       idFormacion=rs.getInt(1)+1;
                    }
                    Date sqlDate1 = new java.sql.Date(0);
                    Date sqlDate2 = new java.sql.Date(0);
                    
                    Statement stmt1 = conexion.createStatement();
                     String sql = "INSERT INTO FormacionAcademica " +
                     "(ID,IdProfesor,Categoria, Titulo,Universidad,FechaInicio,FechaFin,Descripcion) "+
                     "VALUES ('" + idFormacion + "','"+identificador+"','"+obj.get("categoria") +"','"+obj.get("titulo") +"','"+obj.get("universidad") +"','"+ sqlDate1+"','"+ sqlDate1+"','"+obj.get("descripcion") +"')";

                     stmt1.executeUpdate(sql);
                     stmt1.close(); 
                    
                }
                
             JSONArray articulos=(JSONArray) publicaciones.get("articulos");
              for(int i=0; i<articulos.size();i++){
                  JSONObject obj=(JSONObject)articulos.get(i);
                  int idPublicacion=-1;
                    stmt = conexion.createStatement();     
                    rs = stmt.executeQuery("SELECT max(ID) FROM  Publicacion"); 
                    while (rs.next()) {    
                       idPublicacion=rs.getInt(1)+1;
                    }
                     Date sqlDate1 = new java.sql.Date(0);
                    Statement stmt1 = conexion.createStatement();
                     String sql = "INSERT INTO Publicacion " +
                     "(ID,Titulo,ISBN,Lugar,Editorial,FechaInicio,FechaFin,Tipo) "+
                     "VALUES ('" + idPublicacion + "','"+obj.get("titulo") +"','"+obj.get("ISSN") +
                     "','"+obj.get("lugarPublicacion") +"','"+obj.get("editorial") +"','"+ sqlDate1+
                     "','"+ sqlDate1+"','"+obj.get("tipo") +"')";

                     stmt1.executeUpdate(sql);
                     stmt1.close(); 

              }
   
           }else{
                    Statement stmt1 = conexion.createStatement();
                     String sql = "UPDATE Profesores " +
                     "SET Categoria='" + datosBasicos.get("categoria") +"'where Nombre='" +datosBasicos.get("nombre") +"'";

                     stmt1.executeUpdate(sql);
                     stmt1.close();  
            
            }
      
    }   catch (ParseException ex) {
            Logger.getLogger(GuardarInformacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    } 
}
