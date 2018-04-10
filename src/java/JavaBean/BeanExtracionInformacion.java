package JavaBean;

import com.mysql.jdbc.Connection;
import conexionBD.Cone;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BeanExtracionInformacion {
	public BeanExtracionInformacion() {
		super();
	}
	
	public JSONObject obtenerInformacion(String url) {
		Document doc = null;
		JSONObject obj = new JSONObject();
		JSONObject combined = new JSONObject();
		
		try {
		   doc = Jsoup.connect(url).get(); // URL shortened!
		   Elements ele=doc.select("div.container table");
		   Element elementoDatos=null;
		   System.out.println(ele.size());
		   for(Element e : ele.select("td")) {
			   for(Element elemento : e.select("a[name=datos_generales]")) {
				   elementoDatos=e;
				  // System.out.println(elemento);
					  
			 }
		   }
		   Elements el= elementoDatos.select("tbody tr td");
		   String categoria="";
		   String nombre="";
		   String nacionalidad="";
		   String citas="";
		   String sexo="";
		   
		   for(int i=0; i < el.size()-1; i++) {
			   if(el.get(i).text().equals("Categoría")) {
				   categoria=el.get(i+1).text();
			   }else if(el.get(i).text().equals("Nombre")) {
				   nombre=el.get(i+1).text();
			   }else if(el.get(i).text().equals("Nombre en citaciones")) {
				   citas=el.get(i+1).text();
			   }else if(el.get(i).text().equals("Nacionalidad")) {
				   nacionalidad=el.get(i+1).text();
			   }else if(el.get(i).text().equals("Sexo")) {
				   sexo=el.get(i+1).text();
			   }
			   
		   }
		   
		   System.out.println(el);
		   
		   System.out.println(nombre);
		   
		    
	        obj.put("nombre",nombre);
	        obj.put("categoria", categoria);
	        obj.put("nacionalidad", nacionalidad);
	        obj.put("citas", citas);
	        obj.put("sexo", sexo);
                
                
		    //System.out.println(obj);
	        
	        for(Element e : ele.select("td")) {
				   for(Element elemento : e.select("a[name=formacion_acad]")) {
					   elementoDatos=e;
					  // System.out.println(elemento);
						  
				 }
			}
	        
	        JSONArray formacionAcademica = new JSONArray();
	        
	         el= elementoDatos.select("td");
	         System.out.println("Formacion Academica");
	         
	         for(int i=1; i < el.size(); i++) {
	        	 if(!el.get(i).text().equals("")) {
	        		 JSONObject objAca = new JSONObject();
	        		//Titulo obtenido 
	        		Elements elemnt= el.get(i).select("strong");
	        		if(!elemnt.text().equals("")) {
	        		objAca.put("categoria", elemnt.text().trim());
	        		//System.out.println(elemnt.text());
	        		
	        		 String[] textSplitResult =el.get(i).html().split("<br>");
	        		    if (null != textSplitResult) {
	        		    	int con=0;
	        		         for (String t : textSplitResult) {
	        		        	 if(con==0) {
	        		        		 String[] textSplitResult1=t.split("</strong>");
	        		        	     int contador=0;			 
	        		        		for(String a : textSplitResult1) {
	        		        			if(contador==1) {
	        		        			System.out.println(a);
	        		        			//Universidad 
	        		        			objAca.put("universidad",a.trim());
	        		        			}
	        		        			contador++;
	        		        		}	
	        		        	 }else {
	        		        		 if(con==1) {
	        		        			 
	        		        			 objAca.put("titulo",t.trim());
	        		        			
	        		        		 }else if(con==2) {
	        		        			 
	        		        			 objAca.put("fecha",t.trim());
	        		        			 
	        		        		 }else if(con==3) {
	        		        			 objAca.put("descripcion",t.trim());
	        		        		 }
	        		        		 
	        		        		 
	        		        	 }
	        		        	 con++;
	        		            
	        		         }
	        		    }
	        		    
	        		    formacionAcademica.add(objAca);
	        		} 
	        	 }
	        	
	         }
		
	         combined.put("formacionAcademica",formacionAcademica);
                 JSONArray areasActuacion = new JSONArray(); 
                        ele=doc.select("div.container table");
			elementoDatos=null;
			   
			   for(Element e : ele.select("td")) {
				   //System.out.println(e.select("a"));
				   for(Element elemento : e.select("a[name=otra_info_personal]")) {
					   elementoDatos=e;
					 
						  
				 }
			   }
			   int conAreas=0;
			   String[] areas = null;
			   for(Element elemento1 : elementoDatos.select("table td") ) {
				   
				   if(conAreas==1) {
					   areas=elemento1.text().split("--");
				   }
				   conAreas++;
			   }
			   for(String areasA: areas) {
                                JSONObject area2= new JSONObject(); 
                                area2.put("area",areasA.trim());
                                areasActuacion.add(area2);
			
			   }
                         combined.put("areasActuacion",areasActuacion);
			   
			   
			   
			  ele=doc.select("div.container table");
			  elementoDatos=null;
			  
			   int con=0;
			   
			   for(Element e : ele.select("table")) {
				   if(!e.text().equals("")) {
					   for(Element elemento2: e.select("td[colspan=5]") ) {
						   if(con==1  ) {
							   elementoDatos=e;
						   }else {
							   con++;
						   }
					   }
				   }
				 
				
				
			   }
			  JSONArray idiomas = new JSONArray(); 
			  for(Element e : elementoDatos.select("li")) {
				JSONObject idioma= new JSONObject(); 
                                idioma.put("idioma",e.text().trim());
                                idiomas.add(idioma);
                                  
			  }
                         combined.put("idiomas",idiomas);
			  
	        
		
		} catch (IOException ioe) {
		   ioe.printStackTrace();
		}  
		
        JSONObject mainObj = obtenerArticulos(url);
        JSONObject mainObj1 = obtenerLibros(url); 
        JSONObject mainObj2 = obtenerEventos(url); 
        JSONObject mainObj3 = obtenerCapitulos(url);
        combined.put("datosBasicos", obj);
        combined.put("publicaciones", mainObj);
        combined.put("Libros", mainObj1);
        combined.put("eventos", mainObj2);
        combined.put("capitulos", mainObj3);
        
       System.out.println(combined.toString());
		return combined;
	}
	
	
   public JSONObject obtenerArticulos(String url) {
	   
	   Document doc = null;
	   JSONArray articulos = new JSONArray();
		try {
		   doc = Jsoup.connect(url).get(); // URL shortened!
		   Elements ele=doc.select("div.container table");
		   Element elementoDatos=null;
		   System.out.println(ele.size());
		   for(Element e : ele.select("td")) {
			 
			   for(Element elemento : e.select("a[name=articulos]")) {
				   elementoDatos=e;
				 
					  
			 }
		   }
		   
		   for(Element el: elementoDatos.select("blockquote")) {
			  String articulo=el.text();
			  String[] parts = articulo.split("\"");
			  
			  String[] parts1 = parts[0].split(",");
			  //Nombre de los autores
			  JSONArray autor = new JSONArray();
			  List<String> autores = new ArrayList<String>();
			  for (int i=0; i < parts1.length-1; i++ ) {
				  autores.add(parts1[i].trim());
				  
				  
				  JSONObject auto = new JSONObject();
				  auto.put("nombre", parts1[i].trim());
				  
				  autor.add(auto);
		      }
			  //Titulo del articulo
			  String titulo=parts[1].trim();
			
			  
			  parts = articulo.split("En:");
			  
			  parts1=parts[1].split("ISSN:");
			  
			  // Lugar de publicacion
			  String en=parts1[0].trim();
			  
			  String[] parts2=parts1[1].split("ed:");
			  // Codigo ISSN
			  String issn=parts2[0].trim();
			  //System.out.println(ISSN);
			  
			  String editorial=parts2[1].trim();	  
			  
			  JSONObject arti = new JSONObject();
			  arti.put("titulo", titulo);
			  arti.put("lugarPublicacion", en);
			  arti.put("ISSN",issn);
			  arti.put("tipo","articulo");
			  arti.put("autor",autor);
			  arti.put("editorial", editorial);
				
				
			  articulos.add(arti);
		   }
		 
		  
		
		} catch (IOException ioe) {
		   ioe.printStackTrace();
		}
		

		

		JSONObject mainObj = new JSONObject();
		mainObj.put("articulos", articulos);
		
		System.out.println(mainObj.toString());

	   return mainObj;
   }
   
   public JSONObject obtenerLibros(String url) {
	   Document doc = null;
	   JSONArray libros = new JSONArray();
		try {
			
		  
		   doc = Jsoup.connect(url).get(); // URL shortened!
		   Elements ele=doc.select("div.container table");
		   Element elementoDatos=null;
		   
		   for(Element e : ele.select("td")) {
			  
			   for(Element elemento : e.select("a[name=libros]")) {
				   elementoDatos=e;
				  
					  
			 }
		   }
		  for(Element e : elementoDatos.select("blockquote")) {
			  System.out.println(e.text());
			  String libro=e.text();
			  String[] parts = libro.split("\"");
			  String[] parts1 = parts[0].split(",");
			  //Nombre de los autores
			  JSONArray autor = new JSONArray();
			  List<String> autores = new ArrayList<String>();
			  for (int i=0; i < parts1.length-1; i++ ) {
				  autores.add(parts1[i].trim());
				  System.out.println(parts1[i].trim());
				  
				   JSONObject auto = new JSONObject();
				   auto.put("nombre", parts1[i].trim());
				  
				   autor.add(auto);
		      }
			  
			  String titulo=parts[1].trim();
			  System.out.println(titulo);
			  
			  parts = libro.split("En:");
			  //System.out.println(parts[1]);
			  parts1=parts[1].split("ed:");
			  
			  // Lugar de publicacion
			  String en=parts1[0];
			  System.out.println(parts1[0]);
			  String[] parts2=parts1[1].split("ISBN:");
			  // Codigo ISSN
			  String editorial=parts2[0];
			  System.out.println(editorial);
			  
			  String [] parts4=parts2[1].split("v.");
			  String ISBN=parts4[0].trim();
			  
			  System.out.println(ISBN);
			  
			  JSONObject lib = new JSONObject();
			  lib.put("titulo", titulo);
			  lib.put("lugarPublicacion", en);
			  lib.put("ISBN",ISBN);
			  lib.put("tipo","libro");
			  lib.put("autor",autor);
			  lib.put("editorial", editorial);
				
				
			  libros.add(lib);
		  }
		
		  
		
		} catch (IOException ioe) {
		   ioe.printStackTrace();
		}
		
		JSONObject mainObj = new JSONObject();
		mainObj.put("libros", libros);
		
		System.out.println(mainObj.toString());

	   return mainObj;

   }
   
   
   public JSONObject obtenerEventos(String url) {
	   Document doc = null; 
	   JSONArray eventos = new JSONArray();
		try {
		   doc = Jsoup.connect(url).get(); // URL shortened!
		   Elements ele=doc.select("div.container table");
		   Element elementoDatos=null;
		   System.out.println(ele.size());
		   for(Element e : ele.select("td")) {
			   //System.out.println(e.select("a"));
			   for(Element elemento : e.select("a[name=evento]")) {
				   elementoDatos=e;
				   //System.out.println(e);
					  
			 }
		   }
		   
		   for(Element el: elementoDatos.select("table table")) {
			   JSONObject eve = new JSONObject();
			   String evento=el.text();
			   String[] parts=evento.split("Nombre del evento:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Tipo de evento:");
				   if(parts1.length>=2) {
					   //nombre del evento
					   eve.put("nombreEvento",parts1[0].trim());
					  // System.out.println(parts1[0].trim());
				   }
			   }
			   parts=evento.split("Tipo de evento:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Ámbito:");
				   if(parts1.length>=2) {
					   //Tipo de evento
					  // System.out.println(parts1[0].trim());
					   eve.put("tipoEvento",parts1[0].trim());
				   }
			   }
			   
			   parts=evento.split("Ámbito:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Productos asociados Nombre del producto:");
				   if(parts1.length>=2) {
					   //Ambito
					   //System.out.println(parts1[0].trim());
					   eve.put("ambito",parts1[0].trim());
				   }
			   }
			   
			   
			   parts=evento.split("Productos asociados Nombre del producto:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Tipo de producto:");
				   if(parts1.length>=2) {
					   //Tipo de producto
					   //System.out.println(parts1[0].trim());
					   eve.put("nombreProducto",parts1[0].trim());
				   }
			   }
			   
			   parts=evento.split("Tipo de producto:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Instituciones asociadas");
				   if(parts1.length>=2) {
					   //Tipo de producto
					   //System.out.println(parts1[0].trim());
					   eve.put("tipoProducto",parts1[0].trim());
				   }
			   }
			   JSONArray participantes = new JSONArray();
			   parts=evento.split("Participantes");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Nombre:");
				   for(String participante :  parts1 ) {
					   String[] parts2=participante.split("Rol en el evento:");
					   if(parts2.length==2) {
						   JSONObject partici = new JSONObject();
						   //Nombre
						  // System.out.println(parts2[0]);
						   partici.put("nombre", parts2[0].trim());
						   //rol evento
						   partici.put("rolEvento", parts2[1].trim());
						   
						   participantes.add(partici);
					   }
				   }
			   }
			   eve.put("participantes",participantes);
			   
			   eventos.add(eve);
		   }
		   
		   
		  
		
		} catch (IOException ioe) {
		   ioe.printStackTrace();
		}
		
		JSONObject mainObj = new JSONObject();
		mainObj.put("eventos", eventos);
		
		return mainObj;
		
   }
   public JSONObject obtenerCapitulos(String url) {
                Document doc = null; 
                JSONArray capitulos = new JSONArray();
			try {
			   doc = Jsoup.connect(url).get(); // URL shortened!
			   Elements ele=doc.select("div.container table");
			   Element elementoDatos=null;
			   System.out.println(ele.size());
			   for(Element e : ele.select("td")) {
				   //System.out.println(e.select("a"));
				   for(Element elemento : e.select("a[name=capitulos]")) {
					   elementoDatos=e;
					   //System.out.println(e);
						  
				 }
			   }
			   
			   for(Element el: elementoDatos.select("blockquote")) {
				  // System.out.println(el.text());
                                   JSONObject capituloLibros = new JSONObject();
				   String [] parts=el.text().split(", \"");
				   if(parts.length>=2) {
					   String [] parts1=parts[0].split("Tipo: Capítulo de libro");
                                       JSONArray autores = new JSONArray();    
				       for(String autor: parts1) {
				    	   if(!autor.equals("")){
				    	   System.out.println(autor.trim());
                                           JSONObject autor2 = new JSONObject();
                                           autor2.put("nombre", autor.trim());
                                           autores.add(autor2);
                                          }
				       }
                                       capituloLibros.put("autor", autores);
				       String [] parts2=parts[1].split("En:");
				      
				       if(parts2.length>=2) {
				    	   //Titulo
				         // System.out.println(parts2[0].trim());
				          capituloLibros.put("titulo", parts2[0].trim());
				          String [] parts3=parts2[1].split("ISBN:");
				          if(parts3.length>=2) {
				        	//En
				        	//System.out.println(parts3[0]);
				        	capituloLibros.put("lugarPublicacion", parts3[0].trim());
				        	String [] parts4=parts3[1].split("ed:");
				        	//ISBN
				        	//System.out.println(parts4[0]);
				        	capituloLibros.put("ISBN", parts4[0].trim());
				        	String [] parts5=parts4[1].split("Palabras:");
                                                //ed
                                                String ed;
				        	if(parts5.length>=2) {
				        		//System.out.println(parts5[0]);
                                                        ed=parts5[0].trim();
				        	}else {
				        		 parts5=parts4[1].split("Areas:");
                                                         ed=parts5[0].trim();
				        		// System.out.println(parts5[0]);
				        	}
                                                
                                                capituloLibros.put("editorial",ed);
				        	
				        	
				        	
				          }
				       
				       }
                                       
                                       capitulos.add(capituloLibros);
				   }
			   }
			   
			   
			  
			
			} catch (IOException ioe) {
			   ioe.printStackTrace();
			}
                        
                        JSONObject mainObj = new JSONObject();
		        mainObj.put("capitulos", capitulos);
                return mainObj;
   }
}
