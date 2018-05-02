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
 * @author hariasor
 */
public class estadisticasDao {
    
    public estadisticasDao(){
        super();
    }   
 
            
  public JSONObject getEstadisticasByFiltros (String fechaQueryStatement, String nombreQueryStatement, String rolQueryStatement, String tipoQueryStatement){
        JSONObject estadisticas =new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray counts = new JSONArray();
        Cone conex = new Cone();
        Connection conexion= conex.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        JSONObject respuesta = new JSONObject();
              
        try {
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
            
            String selectStatemt = "select COUNT(*) numero";
            String fromStatement = " from Publicacion PU inner join Persona_Publicacion PP on PP.IdPublicacion = PU.ID";
            String whereStatement = " where PP.EstadoSistema = 1";
            String groupByStatement = "";
            Boolean existeGroupByAnterior = false;
            
            if (nombreQueryStatement != null || rolQueryStatement != null || tipoQueryStatement != null)
                groupByStatement = " group by";
            
            if (fechaQueryStatement != null) {
                whereStatement += " and "+fechaQueryStatement;
            }
            
            if (nombreQueryStatement != null) {
                selectStatemt += ", PE.Nombre as Nombre";
                //selectStatemt = selectStatemt.replace("P", ", P");
                
                fromStatement += " inner join Persona PE on PE.ID = PP.IdPersona";
                
                whereStatement += " and "+nombreQueryStatement;
                
                groupByStatement += " PE.Nombre";
                existeGroupByAnterior = true;
            }
            
            if (rolQueryStatement != null) {
                selectStatemt += ", PP.Rol";
                //selectStatemt = selectStatemt.replace("PP", ", PP");
                
                whereStatement += " and "+rolQueryStatement;
                
                if (existeGroupByAnterior)
                    groupByStatement += ", PP.Rol";
                else {
                    groupByStatement += " PP.Rol";
                    existeGroupByAnterior = true;
                }
            }
            
            if (tipoQueryStatement != null) {
                selectStatemt += ", PU.Tipo as Tipo";
                //selectStatemt = selectStatemt.replace("PU", ", PU");
                
                whereStatement += " and "+tipoQueryStatement;
                
                if (existeGroupByAnterior)
                    groupByStatement += ", PU.Tipo";
                else {
                    groupByStatement += " PU.Tipo";
                    existeGroupByAnterior = true;
                }
            }
                                
            stmt = conexion.createStatement();
            String query = selectStatemt+fromStatement+whereStatement+groupByStatement;
            System.out.println("Query: "+query);
            
            rs = stmt.executeQuery(query);                     
                                   
            while (rs.next()) {
                String label ="";
                int count = 0;
                if (nombreQueryStatement != null)
                    label += rs.getString("Nombre") + " - ";
                if (rolQueryStatement != null) {
                    String valor = rs.getString("Rol");
                    if (valor == null)
                        label += "Rol no definido - ";
                    else
                        label += rs.getString("Rol") + " - ";
                }
                if (tipoQueryStatement != null){
                    String valor = rs.getString("Tipo");
                    if (valor == null)
                        label += "Tipo no definido - ";
                    else
                        label += rs.getString("Tipo")+" - ";
                }
                if (nombreQueryStatement == null && rolQueryStatement == null && tipoQueryStatement == null)
                    label = "Total publicaciones";
                count = rs.getInt("numero");
                labels.add(label);
                counts.add(count);
            }
            
            rs.close();
            stmt.close();
            conex.desconectar();
            estadisticas.put("labels", labels);
            estadisticas.put("data", counts);
            respuesta.put("estadisticas", estadisticas);
         
        }  catch (SQLException ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getParametrosSistema(): "+respuesta.toString());
            Logger.getLogger(filtrosPublicacionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return respuesta;
    }
    
}
