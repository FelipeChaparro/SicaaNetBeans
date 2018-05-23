/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import org.apache.commons.text.similarity.*;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author hariasor
 */
public class BeanConcordancia {
    
    public BeanConcordancia() {
	super();
    }
    
    public Double getSimilarity(String palabraUno, String palabraDos, String tipo) {

        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
        Double porcentageConfianza = 0.0;
        
        if (tipo.equals("NOMBRE")){
            String nombreUno = StringUtils.stripAccents(palabraUno).trim().toUpperCase().replace(".","").replace("-", " ").replace(":"," ");
            String nombreDos = StringUtils.stripAccents(palabraDos).trim().toUpperCase().replace(".","").replace("-", " ").replace(":"," ");
                        
            String[] nombreUnoTokenizado = nombreUno.split(",");
            String[] nombreDosTokenizado = nombreDos.split(",");
            
            if (nombreUnoTokenizado.length == 2)
                nombreUno = nombreUnoTokenizado[1].trim()+" "+nombreUnoTokenizado[0].trim();
            if (nombreDosTokenizado.length == 2)
                nombreDos = nombreDosTokenizado[1].trim()+" "+nombreDosTokenizado[0].trim();
            
            porcentageConfianza = jaroWinklerDistance.apply(nombreUno, nombreDos);
            //System.out.println(nombreUno + " - " +nombreDos + ": "+porcentageConfianza);          
                
        }
        else if (tipo.equals("NOMBRE_PUBLICACION")) {
            String tituloUno = StringUtils.stripAccents(palabraUno).toUpperCase().replace(".","").replace("-", " ").replace(":"," ");
            String tituloDos = StringUtils.stripAccents(palabraDos).toUpperCase().replace(".","").replace("-", " ").replace(":"," ");
            
            porcentageConfianza = jaroWinklerDistance.apply(tituloUno, tituloDos);
            //System.out.println(tituloUno + " - " +tituloDos + ": "+porcentageConfianza);
        }
        
        return porcentageConfianza;
    }
    
}
