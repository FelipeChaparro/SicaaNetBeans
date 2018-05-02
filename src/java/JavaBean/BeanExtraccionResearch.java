package JavaBean;


import Entidad.Publicacion;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class BeanExtraccionResearch {
	public BeanExtraccionResearch() {
		super();
	}
	
	public  JSONObject obtenerInformacion(String url) throws IOException {
		
            JSONObject informacionResearch = new JSONObject();
            List<Publicacion> publicaciones=new ArrayList<Publicacion>();
            String urlImagen="";

            Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("cookie","sid=72wkTWX8B9R4bmJRy2ozKEP0Q3E5Nu8u4okXGns0k1yJmNBTa0Y6rIKS8y815vqpRxfXXY3o9d00FBqVM0R5KRJFe01J3EZIzeUxO6fc02OFft1HidA2bvhQnpIc4rLl; did=1a0r0eb1TEPcI0mPlJ19zBzsydISG7cwD1fzCwV0F0wjR0Wcc4Y5tOyMG0L5s0eg; ptc=RG1.86429550282323832.1525066193; _ga=GA1.2.1985139287.1525066183; _gid=GA1.2.1518963699.1525066183; cookieconsent_dismissed=true; __gads=ID=60a4e6d125cea06e:T=1525066197:S=ALNI_Mb-4O3WvRyxrXfQ_R2svgtFb4hD1Q; chseen=1; captui=NDE2YTA4OTg2NTU5NTM4ODBjZjZjNjBhMTBhMTIyNGQwZmQ2NzMyNmY5YTk3ZjA5ZWY0YzlkY2MxYWE4OWM5Nl8xenVYMlRJV3dvTkVnMXFyQ3JmRVBiZzQybGtYb2FCTEMzYzA%3D; _gat=1").timeout(100000).get();

            //Obteiene la foto
            Element e = document.select("meta[property=og:image]").first();
            urlImagen = e.attr("content");
            informacionResearch.put("foto",urlImagen);

            List<String> url54 =new ArrayList<String>();
            Element paginas = document.select("div.pageList").first();
            int con=0;
            if(paginas!=null){
                for (Element elemento1 : paginas.select("a.nova-e-link")){
                    if(con>0)
                        url54.add(elemento1.attr("href").toString());
                    con++;
                }
            }
              
                  

            List<String> urls=new ArrayList<String>();
            
             for(String u : url54){
                        Document document1 = Jsoup.connect(u)
			   .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
			   .header("cookie","sid=72wkTWX8B9R4bmJRy2ozKEP0Q3E5Nu8u4okXGns0k1yJmNBTa0Y6rIKS8y815vqpRxfXXY3o9d00FBqVM0R5KRJFe01J3EZIzeUxO6fc02OFft1HidA2bvhQnpIc4rLl; did=1a0r0eb1TEPcI0mPlJ19zBzsydISG7cwD1fzCwV0F0wjR0Wcc4Y5tOyMG0L5s0eg; ptc=RG1.86429550282323832.1525066193; _ga=GA1.2.1985139287.1525066183; _gid=GA1.2.1518963699.1525066183; cookieconsent_dismissed=true; __gads=ID=60a4e6d125cea06e:T=1525066197:S=ALNI_Mb-4O3WvRyxrXfQ_R2svgtFb4hD1Q; chseen=1; captui=NDE2YTA4OTg2NTU5NTM4ODBjZjZjNjBhMTBhMTIyNGQwZmQ2NzMyNmY5YTk3ZjA5ZWY0YzlkY2MxYWE4OWM5Nl8xenVYMlRJV3dvTkVnMXFyQ3JmRVBiZzQybGtYb2FCTEMzYzA%3D; _gat=1").timeout(100000).get();
		        for(Element el1 : document1.select("meta[itemProp=mainEntityOfPage]")){
                            String url1=el1.attr("content");
                            urls.add("https://www.researchgate.net/"+url1);
                            
                        }
                       
               }
                        
           
            for(Element el : document.select("a.nova-e-link--theme-bare")){
                String url1=el.attr("href");
                if (url1.contains("publication/") && !urls.contains("https://www.researchgate.net/" + url1)) {
                    System.out.println("URL publicacion: https://www.researchgate.net/" + url1.toString());
                    urls.add("https://www.researchgate.net/" + url1);
                    
                }
            }
            
            

            
           /* for(String u : urls){
                Document document1 = Jsoup.connect(u)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .header("cookie","sid=72wkTWX8B9R4bmJRy2ozKEP0Q3E5Nu8u4okXGns0k1yJmNBTa0Y6rIKS8y815vqpRxfXXY3o9d00FBqVM0R5KRJFe01J3EZIzeUxO6fc02OFft1HidA2bvhQnpIc4rLl; did=1a0r0eb1TEPcI0mPlJ19zBzsydISG7cwD1fzCwV0F0wjR0Wcc4Y5tOyMG0L5s0eg; ptc=RG1.86429550282323832.1525066193; _ga=GA1.2.1985139287.1525066183; _gid=GA1.2.1518963699.1525066183; cookieconsent_dismissed=true; __gads=ID=60a4e6d125cea06e:T=1525066197:S=ALNI_Mb-4O3WvRyxrXfQ_R2svgtFb4hD1Q; chseen=1; captui=ZGUxZTIwZWZjMmRjNzExMTkyZTU4MGY2NTI0OTI3YjE3ZGI0ZDhlM2MyYzEwODk5N2YwNWQxOWQwM2ViOTM1Ml9NUTM1MXNNTkdjQVM4aGVzaWVza1QzTXdScXNjZ292T1ZDWjU%3D; _gat=1").timeout(100000).get();
                for(Element el1 : document1.select("meta[itemProp=mainEntityOfPage]")){
                    String url1=el1.attr("content");
                    System.out.println("URLS Publciaicon mainEntityOfPage: https://www.researchgate.net/"+url1);
                    urls.add("https://www.researchgate.net/"+url1);
                }
            }*/
         

            System.out.println("numero urls:::::::::::::::::::::"+urls.size());
            JSONArray articulos = new JSONArray();

            for(String urls2 :urls ){
                //System.out.println("URL: "+urls2.toString());
                JSONObject publicacionIndividual = new JSONObject();
                Document document1 = Jsoup.connect(urls2)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .header("cookie","sid=72wkTWX8B9R4bmJRy2ozKEP0Q3E5Nu8u4okXGns0k1yJmNBTa0Y6rIKS8y815vqpRxfXXY3o9d00FBqVM0R5KRJFe01J3EZIzeUxO6fc02OFft1HidA2bvhQnpIc4rLl; did=1a0r0eb1TEPcI0mPlJ19zBzsydISG7cwD1fzCwV0F0wjR0Wcc4Y5tOyMG0L5s0eg; ptc=RG1.86429550282323832.1525066193; _ga=GA1.2.1985139287.1525066183; _gid=GA1.2.1518963699.1525066183; cookieconsent_dismissed=true; __gads=ID=60a4e6d125cea06e:T=1525066197:S=ALNI_Mb-4O3WvRyxrXfQ_R2svgtFb4hD1Q; chseen=1; captui=NDE2YTA4OTg2NTU5NTM4ODBjZjZjNjBhMTBhMTIyNGQwZmQ2NzMyNmY5YTk3ZjA5ZWY0YzlkY2MxYWE4OWM5Nl8xenVYMlRJV3dvTkVnMXFyQ3JmRVBiZzQybGtYb2FCTEMzYzA%3D; _gat=1")
                    .timeout(100000)
                    .get();
                                                                
                Elements eletitulo = document1.select("h1.nova-e-text");
                String titulo= eletitulo.text().toString().trim();
                System.out.println("Titulo: "+titulo);
                publicacionIndividual.put("titulo", titulo);
                JSONArray autor = new JSONArray();
                for(Element eleAutor : document1.select("meta[property=citation_author]") ){
                    JSONObject autorIndividual = new JSONObject();
                    if(eleAutor.hasAttr("content")){
                        String autores=eleAutor.attr("content");
                        System.out.println("Nombre autor: "+autores);
                        autorIndividual.put("nombre",autores);
                        autor.add(autorIndividual);
                    }  
                }
                publicacionIndividual.put("autor", autor);

                Element elefecha= document1.select("meta[property=citation_publication_date]").first();
                if(elefecha !=null){
                if(elefecha.hasAttr("content")){
                    String fecha=elefecha.attr("content");

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    try { 
                        java.util.Date date = formatter.parse(fecha);
                        String fechaPublicacion =formatter.format(date);
                        System.out.println("fecha: "+fechaPublicacion);
                        publicacionIndividual.put("fecha", fechaPublicacion);
                    } catch (ParseException ex) {
                        Logger.getLogger(BeanExtraccionResearch.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                }else{
                    publicacionIndividual.put("fecha","");
                }

                String tipo="";

                Element eletipo=document1.select("strong.publication-meta-type").first();
                tipo= eletipo.text();

                Element elDoi=document1.select("meta[property=citation_doi]").first();
                if(elDoi != null ){
                    if(elDoi.hasAttr("content")){
                        String DOI=elDoi.attr("content");
                        System.out.println("editorial: "+DOI);
                        publicacionIndividual.put("editorial", DOI);
                    }
                }

                if(tipo.contains("Chapter")||tipo.contains("Book")){
                    if(tipo.contains("Chapter")){
                        System.out.println("TIPO: capitulo");
                        publicacionIndividual.put("tipo", "capitulo");
                    }else{
                        System.out.println("TIPO: libro");
                        publicacionIndividual.put("tipo", "libro");
                    }

                    Element elISBN=document1.select("meta[property=citation_isbn]").first();
                    if(elISBN != null ){
                        if(elISBN.hasAttr("content")){
                            String ISBN=elISBN.attr("content");
                            System.out.println("ISBn: "+ISBN);
                            publicacionIndividual.put("codigoPublicacion", ISBN);
                        }
                    } 
                }else if(tipo.contains("Article")){
                    publicacionIndividual.put("tipo", "articulo");
                    Element elISSN=document1.select("meta[property=citation_issn]").first();
                        if(elISSN != null ){
                            if(elISSN.hasAttr("content")){
                                String ISSN=elISSN.attr("content");
                                System.out.println("ISSN: "+ISSN);
                                publicacionIndividual.put("codigoPublicacion", ISSN);
                            }
                        }
                }else if(tipo.contains("Conference")){
                    System.out.println("Tipo: conferencia");
                    publicacionIndividual.put("tipo", "conferencia");
                }

                articulos.add(publicacionIndividual);
            }

        System.out.println("FIN  EXTRACCION RESEARCHGATE");
        informacionResearch.put("publicaciones", articulos);

        return informacionResearch;
    }
	
	
	
	public static Document getHtmlDocument(String url) {

        Document doc = null;

        try {
            doc = Jsoup.connect(url).userAgent("Java/1.8.0_121").timeout(100000).get();
        	//doc=Jsoup.connect(url).timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepci�n al obtener el HTML de la p�gina" + ex.getMessage());
        }

        return doc;

    }

}
