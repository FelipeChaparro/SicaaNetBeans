package JavaBean;

import Entidad.Publicacion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BeanExtraccionResearch {
	public BeanExtraccionResearch() {
		super();
	}
	
	public  JSONObject obtenerInformacion(String url) {
		
		JSONObject obj = new JSONObject();
		 List<Publicacion> publicaciones=new ArrayList<Publicacion>();
		   Document doc = null;
		   
		   JSONArray articulos = new JSONArray();
		   
		   Document document = getHtmlDocument(url);
		   Elements entradas = document.select("div.nova-v-publication-item");
		   int con=0;
		  
		   //Nombre de la publicacion
		   for(Element el: entradas.select("a[itemProp=mainEntityOfPage]")){
			   Publicacion publicacion=new Publicacion();
			  // System.out.println(el.text());
			   publicacion.setTitulo(el.text().trim());
			   publicaciones.add(publicacion);
			   
			   
		   }
		   
		   //Tipo de publicacion 
		   int contador=0;
		   for(Element el: entradas.select("div.nova-v-publication-item__badges-item")){
			   if(!el.text().trim().equals("Full-text available") && !el.text().trim().equals("")) {
				   publicaciones.get(contador).setTipo(el.text());
				   contador++;
			   }
			   //System.out.println(el.text());
			   //System.out.println("------------------------------------");
		   }
		   
		   //class="nova-v-publication-item__stack-item"
		   int contadorAutores=0;		   
		   for(Element el: entradas.select("div.nova-v-publication-item__stack-item")){
			  List<String> autores=new ArrayList<String>();
			  for(Element e: el.select("span.nova-v-person-inline-item__fullname")) {
				 // System.out.println(e.text());
				  autores.add(e.text().trim());
			  }
			  if(autores.size()>0) {
				  publicaciones.get(contadorAutores).setAutores(autores);
				  contadorAutores++;
				  
			  }
			   //System.out.println("-----");
			   
			   
		   }
		   
		  
		   
		   for(Publicacion p: publicaciones) {
			   
			   JSONObject arti = new JSONObject();
				  arti.put("titulo", p.getTitulo());
				  arti.put("tipo",p.getTipo());
			
			      //System.out.println(p.getTipo());
			      //System.out.println(p.getTitulo());
				   JSONArray autor = new JSONArray();
				   for(String pu : p.getAutores()) {
					   JSONObject auto = new JSONObject();
						auto.put("nombre", pu);
						autor.add(auto);
				   }
			   
				   arti.put("autor",autor);
				   articulos.add(arti);
		   }
		  obj.put("publicaciones", articulos);
		  
		  System.out.println(obj);
		return obj;
	}
	
	
	
	public static Document getHtmlDocument(String url) {

        Document doc = null;

        try {
            doc = Jsoup.connect(url).userAgent("Java/1.8.0_131").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
        }

        return doc;

    }
}
