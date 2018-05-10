/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Dao.filtrosPublicacionDao;
import Dao.publicacionesDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author hariasor
 */
public class BeanNubePalabras {
   
    public BeanNubePalabras() {
	super();
    }
    
    public JSONObject getAllPalabarsByFiltrosBean(String departamentoFiltro, String facultadFiltro, String nombreAutorFiltro, boolean usarDptoFiltro, int numeroMaximoPalabras) {
        
        JSONObject respuesta = new JSONObject();
        JSONObject respuesta_publicaciones_dao = new JSONObject();
        JSONArray listaTitulos = new JSONArray();
        JSONArray palabrasAcontar = new JSONArray();
        JSONArray palabrasContadas = new JSONArray();
        try {
            
            publicacionesDAO publicaciones_dao = new publicacionesDAO();
            int tipoConsulta = 0;
            
            respuesta.put("code", 0);
            respuesta.put("description", "Operacion exitosa");
            
            System.out.println("Parametros: usarDptoFiltro"+usarDptoFiltro+" - departamentoFiltro: "+departamentoFiltro+"- facultadFiltro: "+facultadFiltro+"- nombreAutorFiltro: "+nombreAutorFiltro);
            if (usarDptoFiltro && departamentoFiltro!= null && nombreAutorFiltro == null) {
                tipoConsulta = 1;
                System.out.println("Entro a 1: ");
            }
            else if (!usarDptoFiltro && facultadFiltro!=null && nombreAutorFiltro == null){
                tipoConsulta = 2;
                System.out.println("Entro a 1: ");
            }
            else if (facultadFiltro == null && departamentoFiltro == null){
                tipoConsulta = 3;
                System.out.println("Entro a 1: ");
            }
            else if (facultadFiltro == null && nombreAutorFiltro!= null && departamentoFiltro!= null){
                tipoConsulta = 4;
                System.out.println("Entro a 1: ");
            }
            else if (departamentoFiltro == null && nombreAutorFiltro!= null && facultadFiltro!= null){
                tipoConsulta = 5;
                System.out.println("Entro a 1: ");
            }
            respuesta_publicaciones_dao = publicaciones_dao.getAllTitulosByFiltros(departamentoFiltro, facultadFiltro, nombreAutorFiltro, tipoConsulta);
                        
            if ((int)respuesta_publicaciones_dao.get("code")==0){
                
                listaTitulos = (JSONArray) respuesta_publicaciones_dao.get("titulos");
                int tamanoListaTitulos = listaTitulos.size();
                String tituloTotal = "";

                for (int i=0;i<tamanoListaTitulos;i++)
                    tituloTotal += listaTitulos.get(i).toString()+" ";
                
                palabrasAcontar = removePalabrasCortas(tituloTotal, 3);
                palabrasContadas = countPalabras(palabrasAcontar,numeroMaximoPalabras);
                
                respuesta.put("nube", palabrasContadas);
            }
            else
                respuesta = respuesta_publicaciones_dao;
            
            
        } catch (Exception ex) {
            respuesta.put("code", 9997);
            respuesta.put("description", "Error de sistema");
            System.out.println("getAllPalabarsByFiltrosBean(): "+respuesta.toString());
            Logger.getLogger(filtrosPublicacionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return respuesta;        
    }
    
    private JSONArray removePalabrasCortas (String palabraALimpiar, int tamanoPalabrasMinimo) {
        System.out.println("removePalabrasCortas() - Palabra original: "+palabraALimpiar);
        
        JSONArray palabrasXRetornar = new JSONArray();
        String[] palbraALimpiarSplit = StringUtils.stripAccents(palabraALimpiar)
                .toUpperCase().trim().replace(", "," ").replace(","," ").replace("."," ").replace("/"," ")
                .replace("-"," ").replace("\"","").split(" ");
        
        int tamanoPalbraALimpiarSplit = palbraALimpiarSplit.length;
        
        for (int i=0;i<tamanoPalbraALimpiarSplit;i++) {
            //System.out.println(i+"-Palabra:"+palbraALimpiarSplit[i]);
            if (palbraALimpiarSplit[i].length()>tamanoPalabrasMinimo)
                if (!palbraALimpiarSplit[i].equalsIgnoreCase("PARA"))
                    palabrasXRetornar.add(palbraALimpiarSplit[i]);
        }
        return palabrasXRetornar;        
    }
    
    public JSONArray countPalabras (JSONArray listaPalabras,int numeroMaximoPalabras) {
        JSONArray palabrasOrdenadas = new JSONArray();
        HashMap<String,Integer> mapaPalabras = new HashMap<String,Integer>();  
        
        int tamanoListaPalabras = listaPalabras.size();
        String palabraIndividual = "";
        
        for (int i=0;i<tamanoListaPalabras;i++) {
            palabraIndividual = listaPalabras.get(i).toString();
            if (mapaPalabras.containsKey(palabraIndividual)) {
                mapaPalabras.put(palabraIndividual,mapaPalabras.get(palabraIndividual)+1);
            }
            else
                mapaPalabras.put(palabraIndividual,1);
        }
        
        LinkedHashMap<String, Integer> sortedMap = sortHashMapByValues(mapaPalabras);
        
        Object[] palabrasContadas = sortedMap.keySet().toArray();
        int tamanopalabrasXRetornar = palabrasContadas.length;        
        int numeroPalabrasIngresadas = 0;
        for (int i=tamanopalabrasXRetornar-1;i>=0 && numeroPalabrasIngresadas<numeroMaximoPalabras;i--) {
            JSONObject palabraNube = new JSONObject();
            palabraNube.put("text", palabrasContadas[i]);
            palabraNube.put("weight", mapaPalabras.get(palabrasContadas[i]));
            palabrasOrdenadas.add(palabraNube);
            numeroPalabrasIngresadas++;
            //System.out.println(i+"-Palabra:"+palabrasContadas[i]+" - "+mapaPalabras.get(palabrasContadas[i]));
        }

        return palabrasOrdenadas;        
    }
        
    public LinkedHashMap<String, Integer> sortHashMapByValues(HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
    
}
