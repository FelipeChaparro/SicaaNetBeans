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

import Entidad.Publicacion;

public class BeanExtraerInformacionGoogleSchoolar {
	
	public BeanExtraerInformacionGoogleSchoolar() {
		super();
	}
	
	public JSONObject obtenerInformacion(String url) throws IOException {
            
            String cookieGoogle = "CONSENT=YES+ES.es+V10; GSP=LM=1523144400:S=JbcfPCDuxxrEndYA; SID=DAa1AkkfAIYUeXjnFerwwYm2F732HvYsj7LByxphpjJBLY3eUbBaqvUZa1Ls6na-FF-6tQ.; HSID=ANp_ODUlPvY0tD6Xj; SSID=ATdaTt9Su0F63Frj8; APISID=Z9Bf4RDJNO65kAkq/AisIulpRRWeyfoTFr; SAPISID=Zeq2q8uBLcz_ouJl/ARhyu-YVusSFB02_M; 1P_JAR=2018-5-1-0; NID=129=Em9Mve0yMVf7v0_2rWa0DLCdRpIqnkXNg3dex_avogQXxoPZ65SbIhSQ83bxK3hApn3oPp19b9iBmqgeRMhK7fXCSFYv6_5_OAQ6V7ljNOgi8zIzrif9IgHl4jAkh9LNRhWuxThk9mUzXgpxlqqU1Os0wSBZ5qIAImz9i9uIY60X94CpJkI2fVBsD5lLvO11N6ru5EyqiEw7TSRyVTziiCnowagdudnfu0nfvkUG6msuWdYfZAUFR06nvw; GOOGLE_ABUSE_EXEMPTION=ID=055535b100256100:TM=1525150542:C=r:IP=177.252.254.15-:S=APGng0v7hRYoh8VerFrvzmHRKgwq2lrCSg; SIDCC=AEfoLeYhnYNUdSRnOy-pKXasgiSp0xwV0AAxsFD78AB13S6SUFeTrA3DOKhwu5pbIXYTYo8JXw";
            
            boolean pedriMasPublicaciones = false;
            JSONObject respuesta = new JSONObject();
            JSONArray articulos = new JSONArray();
            System.out.println("URL: "+url+"&cstart=0&pagesize=100");
            Document document = Jsoup.connect(url+"&cstart=0&pagesize=100")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header("cookie", cookieGoogle)
                .timeout(50000)
                .get();
            //System.out.println("Documento: "+document.toString());
            Elements entradas = document.select("td.gsc_a_t");
            String urlGoo = "https://scholar.google.com";

            if (document.select("div#gsc_lwp").text().contains("100"))
                pedriMasPublicaciones = true;

            List<String> listaUrlsPublicacionesTodas = new ArrayList<>();
            for (Element el: entradas.select("a[href]")) {
                System.out.println("URL publicacion: "+urlGoo+el.attr("data-href").replace("amp;", ""));
                listaUrlsPublicacionesTodas.add(urlGoo+el.attr("data-href").replace("amp;", ""));
            }

            for(String urlPublicacion: listaUrlsPublicacionesTodas){
                    //System.out.println("URL bien: "+urlGoo+urlPub.replace("amp;", ""));
                    Document publicacion = Jsoup.connect(urlPublicacion)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .header("cookie", cookieGoogle)
                        .timeout(50000)
                        .get();
                    Elements titulos = publicacion.select("a.gsc_vcd_title_link");

                    JSONObject articulo = new JSONObject();
                    JSONArray autor = new JSONArray();
                    String tipoPublicacion = "";

                    articulo.put("titulo", titulos.text());			  
                    System.out.println("-- Nombre publicacion: "+titulos.text());
                    Elements autores = publicacion.select("div.gsc_vcd_value");
                    String[] listaAutores = autores.first().text().trim().split(", ");
                    for (String a: listaAutores) {
                        JSONObject autorSimple = new JSONObject();
                        autorSimple.put("nombre", a);
                        autor.add(autorSimple);
                    }
                    System.out.println("Autores: "+autor.toJSONString());
                    articulo.put("autor", autor);
                    //System.out.println("-- autores: "+autores.first().text());
                    Boolean tieneFecha = publicacion.select("div.gsc_vcd_field").get(1).toString().contains("Fecha");
                    if (tieneFecha) {
                        String fechaPublicacionCompleta = publicacion.select("div.gsc_vcd_value").get(1).text().toString().trim();
                        String[] fechaSplit = fechaPublicacionCompleta.split("/");
                        if (fechaSplit.length == 1) {
                            System.out.println("Fecha si ano: "+fechaSplit[0]+"/01/01");
                            articulo.put("fecha",fechaSplit[0]+"/01/01");
                        }
                        else {
                            System.out.println("Fecha si: "+fechaPublicacionCompleta);
                            articulo.put("fecha",fechaPublicacionCompleta);
                        }
                    }
                    else {
                        System.out.println("Fecha no: "+null); 
                        articulo.put("fecha",null);
                    }

                    Elements tipo = publicacion.select("div.gsc_vcd_field");
                    if (tipo.size() >= 3) {
                        if (tipo.get(2).text().equals("Libro")) {
                            System.out.println("tipo: libro");
                            tipoPublicacion = "libro";
                        }
                        else if (tipo.get(2).text().equals("Revista")) {
                            System.out.println("tipo: articulo");
                            tipoPublicacion = "articulo";
                        } 
                        else if (tipo.get(2).text().equals("Conferencia")) {
                            System.out.println("tipo: conferencia");
                            tipoPublicacion = "conferencia";
                        } 
                        else {
                            System.out.println("tipo: "+null);
                            tipoPublicacion = null;
                        }    
                    }
                    else {
                        System.out.println("tipo: "+null);
                        tipoPublicacion = null;
                    }
                    articulo.put("tipo",tipoPublicacion);
                    articulos.add(articulo);		   
            }

            /**
             * Segunda iteracion por si tiene mas de 100 publicaciones
             */
            if (pedriMasPublicaciones) {
                document = Jsoup.connect(url+"&cstart=100&pagesize=100")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .header("cookie", cookieGoogle)
                    .timeout(50000)
                    .get();
                //System.out.println("Documento: "+document.toString());
                entradas = document.select("td.gsc_a_t");

                List<String> listaUrlsPublicacionesTodas2Iteracion = new ArrayList<>();
                for (Element el2: entradas.select("a[href]")) {
                    System.out.println("URL publicacion: "+urlGoo+el2.attr("data-href").replace("amp;", ""));
                    listaUrlsPublicacionesTodas2Iteracion.add(urlGoo+el2.attr("data-href").replace("amp;", ""));
                }

                for(String urlIndividual2Iteracion: listaUrlsPublicacionesTodas2Iteracion){
                    Document publicacion = Jsoup.connect(urlIndividual2Iteracion)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .header("cookie", cookieGoogle)
                        .timeout(50000)
                        .get();
                    Elements titulos = publicacion.select("a.gsc_vcd_title_link");

                    JSONObject articulo = new JSONObject();
                    JSONArray autor = new JSONArray();
                    String tipoPublicacion = "";

                    articulo.put("titulo", titulos.text());			  
                    System.out.println("-- Nombre publicacion: "+titulos.text());
                    Elements autores = publicacion.select("div.gsc_vcd_value");
                    String[] listaAutores = autores.first().text().trim().split(",");
                    for (String a: listaAutores) {
                            JSONObject autorSimple = new JSONObject();
                            autorSimple.put("nombre", a);
                            autor.add(autorSimple);
                    }
                    System.out.println("Autores: "+autor.toJSONString());
                    articulo.put("autor", autor);

                    Boolean tieneFecha = publicacion.select("div.gsc_vcd_field").get(1).toString().contains("Fecha");
                    if (tieneFecha) {
                        String fechaPublicacionCompleta = publicacion.select("div.gsc_vcd_value").get(1).text().toString().trim();
                        String[] fechaSplit = fechaPublicacionCompleta.split("/");
                        if (fechaSplit.length == 1) {
                            System.out.println("Fecha si ano: "+fechaSplit[0]+"/01/01");
                            articulo.put("fecha",fechaSplit[0]+"/01/01");
                        }
                        else {
                            System.out.println("Fecha si: "+fechaPublicacionCompleta);
                            articulo.put("fecha",fechaPublicacionCompleta);
                        }
                    }
                    else {
                        System.out.println("Fecha no: "+null); 
                        articulo.put("fecha",null);
                    }

                    Elements tipo = publicacion.select("div.gsc_vcd_field");
                    if (tipo.size() >= 3) {
                        if (tipo.get(2).text().equals("Libro")) {
                            System.out.println("tipo: libro");
                            tipoPublicacion = "libro";
                        }
                        else if (tipo.get(2).text().equals("Revista")) {
                            System.out.println("tipo: articulo");
                            tipoPublicacion = "articulo";
                        } 
                        else if (tipo.get(2).text().equals("Conferencia")) {
                            System.out.println("tipo: conferencia");
                            tipoPublicacion = "conferencia";
                        } 
                        else {
                            System.out.println("tipo: "+null);
                            tipoPublicacion = null;
                        }    
                    }
                    else {
                        System.out.println("tipo: "+null);
                        tipoPublicacion = null;
                    }
                    articulo.put("tipo",tipoPublicacion);
                    articulos.add(articulo);		   
                }
            }

            System.out.println("Fin publicaciones: "+articulos);
            respuesta.put("publicaciones", articulos);
            return respuesta; 		   
	}
	
	
	
	public static Document getHtmlDocument(String url) {

        Document doc = null;

        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Excepci�n al obtener el HTML de la p�gina" + ex.getMessage());
        }

        return doc;

    }
	

}
