package JavaBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		   
		   for(int i=1; i < el.size()-1; i++) {
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
		   
		  // System.out.println(el);
		   
		   System.out.println(nombre);
		   
		    
	        obj.put("nombre",nombre);
	        obj.put("categoria", categoria);
	        obj.put("nacionalidad", nacionalidad);
	        obj.put("citas", citas);
	        obj.put("sexo", sexo);
		    //System.out.println(obj);
		   
		  
		
		} catch (IOException ioe) {
		   ioe.printStackTrace();
		}
        JSONObject mainObj = obtenerArticulos(url);
        JSONObject mainObj1 = obtenerLibros(url);
        JSONObject combined = new JSONObject();
        combined.put("datosBasicos", obj);
        combined.put("publicaciones", mainObj);
        combined.put("Libros", mainObj1);
        
       
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
			   //System.out.println(e.select("a"));
			   for(Element elemento : e.select("a[name=articulos]")) {
				   elementoDatos=e;
				   //System.out.println(e);
					  
			 }
		   }
		   
		   for(Element el: elementoDatos.select("blockquote")) {
			  String articulo=el.text();
			  String[] parts = articulo.split("\"");
			  //System.out.println(parts[0]);
			  String[] parts1 = parts[0].split(",");
			  //Nombre de los autores
			  JSONArray autor = new JSONArray();
			  List<String> autores = new ArrayList<String>();
			  for (int i=0; i < parts1.length-1; i++ ) {
				  autores.add(parts1[i].trim());
				  //System.out.println(parts1[i].trim());
				  
				  JSONObject auto = new JSONObject();
				  auto.put("nombre", parts1[i].trim());
				  
				  autor.add(auto);
		      }
			  //Titulo del articulo
			  String titulo=parts[1].trim();
			  //System.out.println(titulo);
			  
			  parts = articulo.split("En:");
			  //System.out.println(parts[1]);
			  parts1=parts[1].split("ISSN:");
			  
			  // Lugar de publicacion
			  String en=parts1[0].trim();
			  //System.out.println(parts1[0]);
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

}
