/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.autoresDao;
import Entidad.Autor;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrador
 */
public class BeanConcordanciaAutores {
 public BeanConcordanciaAutores(){
   super();
 }

 public JSONObject buscarAutores(JSONObject json){
     
     
     JSONArray lista=(JSONArray) json.get("autor");
      autoresDao buscar= new autoresDao();
      
     JSONObject respuesta= new JSONObject();
     JSONArray autoresConcor=new JSONArray(); 
     for(int i=0; i<lista.size();i++){
         JSONObject aux=(JSONObject)lista.get(i);
        List<Autor> autores=buscar.busquedaAutorBD((String) aux.get("Nombre"));
        String nombreAutor="";
        int id=-1;
        int existeAutor=0;
        if(autores.size()==1){
          nombreAutor= autores.get(0).getNombre();
          id=autores.get(0).getId();
          existeAutor=1;
        }
         JSONObject aux1= new JSONObject();
         aux1.put("Nombre",(String) aux.get("Nombre") );
         aux1.put("ID", id);
         aux1.put("existeAutor",existeAutor );
         
         autoresConcor.add(aux1);
       
         
     }
  
    respuesta.put("autor",autoresConcor);
    return respuesta;
 }
 
  public int compararAutoresPublicaciones(JSONObject json){
      JSONArray publicacion1= (JSONArray) json.get("autoresPublicacionSistema");
      JSONArray publicacion2= (JSONArray) json.get("autoresPublicacionNueva");
      BeanConcordancia bean= new BeanConcordancia();
      List<Integer> respuesta= new ArrayList<Integer>();
      int retorno=1;
      for(int i=0; i<publicacion2.size();i++){
          respuesta.add(0);
      }
      if(publicacion2.size() !=publicacion1.size()){
          retorno=2;
      }else{
      for(int i=0; i<publicacion2.size();i++){
          JSONObject pub2=(JSONObject) publicacion2.get(i);
        
         for(int j=0; j<publicacion1.size();j++){
          
                     
           JSONObject pub1=(JSONObject) publicacion1.get(j);
           
           double porcentaje=bean.getSimilarity((String)pub2.get("nombre"),(String)pub1.get("Nombre"),"NOMBRE");
             System.out.println(porcentaje);
             double valor=0.90;
               double valorMax=1.0;
               if(valorMax==porcentaje){
                  respuesta.set(i, 1);
                  break;
               }else if(porcentaje >=  valor){
                   if(respuesta.get(i)==1||respuesta.get(i)==2){
                       respuesta.set(i, 2);  
                   }else{
                       respuesta.set(i, 1);  
                   }
                  
               }
         } 
      }
      boolean existe0=true;
      for(Integer inte : respuesta){
          if(inte==2 || inte==1)
           existe0=false;
        
      } 
      if(existe0){
         retorno=0; 
      }else{
          boolean existe2=false;
         for(Integer inte : respuesta){
          if(inte==2||inte==0)
           existe2=true;
        
         }
         
         if(existe2){
             retorno=2;
         }
          
      }
      
     
  }
      
     return retorno;
  }    

  public JSONArray buscarAutoresPorNombre(JSONObject json){
     
     
     JSONArray lista=(JSONArray) json.get("autor");
      autoresDao buscar= new autoresDao();
      
     JSONObject respuesta= new JSONObject();
     JSONArray autoresConcor=new JSONArray(); 
     for(int i=0; i<lista.size();i++){
         JSONObject aux=(JSONObject)lista.get(i);
        List<Autor> autores=buscar.busquedaAutorBD((String) aux.get("Nombre"));
        
        for(int k=0; k <autores.size();k++ ){
          JSONObject aux1= new JSONObject();
          aux1.put("Nombre",autores.get(k).getNombre());
          aux1.put("ID",autores.get(k).getId());
          autoresConcor.add(aux1);
        }
         
   
         
     }
  
   // respuesta.put("autor",autoresConcor);
    return autoresConcor;
 }

}
