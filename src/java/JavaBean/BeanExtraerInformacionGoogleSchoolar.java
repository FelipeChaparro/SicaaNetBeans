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
		boolean pedriMasPublicaciones = false;
		JSONObject obj = new JSONObject();
		Document doc = null;
		JSONArray articulos = new JSONArray();
		System.out.println("URL: "+url+"&cstart=0&pagesize=100");
		Document document = Jsoup.connect(url+"&cstart=0&pagesize=100")
			   .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
			   .header("cookie", "CONSENT=YES+ES.es+V10; SID=zwW1AuvYjlgnf-KfymCDokwhhbO-yPqdjeOoj2i1Fu0gK8pnwyLzSk_uR8AmgDZJN2IyuQ.; HSID=AgBAM2R08NPs_Q0Gb; SSID=Ao5mLQzgAUn8C1c5w; APISID=3_c5DASu2HslOlAM/Atjp92VTf1bPycsvl; SAPISID=7DXMMPQMeXWJl4nb/AsUwow0N6kp5miOAq; GSP=LM=1523144400:S=JbcfPCDuxxrEndYA; 1P_JAR=2018-4-9-0; NID=127=X2FXhQyNua9wc6DXkLt7Pr45i6iGSbondbIubdukxfwrdRGmOjxpseE_km_kcHp-sKzMK-E3_8_l5wDV1uEY9nf9omhrKeVm4eFoKTkU9xMgvGz_JVJeeo8ic5kU8yJPJpc0a3ABwTYf_zsPtqMpG1o3HeEnBcdgIljgyuWRIPKj4mcaTKNJxjCEKwhTKI_WiYAi7NWwax5C0HCFjoYKo6N_S-1ZHcGkEgaWGxHOTMxotn_UvNMPdJ9QtcuwiQiHZrmhhsanGesuJEJm; GOOGLE_ABUSE_EXEMPTION=ID=5a1e6d8493dbeb64:TM=1523241333:C=r:IP=181.56.40.114-:S=APGng0vNyKKSjrBQKasE3qSv0CUOfxZugw; SIDCC=AEfoLeYaxQBscVgBUlLZYBgML62vHIUmxynIr0Owt19d9cMfcsAn_WVGaffEvIpVyUrUgfmhzg")
	       	   .timeout(50000)
	           .get();
		//System.out.println("Documento: "+document.toString());
		Elements entradas = document.select("td.gsc_a_t");
		String urlGoo = "https://scholar.google.com";
				
		if (document.select("div#gsc_lwp").text().contains("100"))
			pedriMasPublicaciones = true;
		
		for(Element el: entradas.select("a[href]")){
			String urlPub = el.attr("data-href");
			//System.out.println("URL bien: "+urlGoo+urlPub.replace("amp;", ""));
			Document publicacion = Jsoup.connect(urlGoo+urlPub.replace("amp;", ""))
					.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
					   .header("cookie", "CONSENT=YES+ES.es+V10; SID=zwW1AuvYjlgnf-KfymCDokwhhbO-yPqdjeOoj2i1Fu0gK8pnwyLzSk_uR8AmgDZJN2IyuQ.; HSID=AgBAM2R08NPs_Q0Gb; SSID=Ao5mLQzgAUn8C1c5w; APISID=3_c5DASu2HslOlAM/Atjp92VTf1bPycsvl; SAPISID=7DXMMPQMeXWJl4nb/AsUwow0N6kp5miOAq; GSP=LM=1523144400:S=JbcfPCDuxxrEndYA; 1P_JAR=2018-4-9-0; NID=127=X2FXhQyNua9wc6DXkLt7Pr45i6iGSbondbIubdukxfwrdRGmOjxpseE_km_kcHp-sKzMK-E3_8_l5wDV1uEY9nf9omhrKeVm4eFoKTkU9xMgvGz_JVJeeo8ic5kU8yJPJpc0a3ABwTYf_zsPtqMpG1o3HeEnBcdgIljgyuWRIPKj4mcaTKNJxjCEKwhTKI_WiYAi7NWwax5C0HCFjoYKo6N_S-1ZHcGkEgaWGxHOTMxotn_UvNMPdJ9QtcuwiQiHZrmhhsanGesuJEJm; GOOGLE_ABUSE_EXEMPTION=ID=5a1e6d8493dbeb64:TM=1523241333:C=r:IP=181.56.40.114-:S=APGng0vNyKKSjrBQKasE3qSv0CUOfxZugw; SIDCC=AEfoLeYaxQBscVgBUlLZYBgML62vHIUmxynIr0Owt19d9cMfcsAn_WVGaffEvIpVyUrUgfmhzg")
			       	.timeout(50000)
					.get();
			Elements titulos = publicacion.select("a.gsc_vcd_title_link");
		   
			JSONObject articulo = new JSONObject();
			JSONArray autor = new JSONArray();
			String tipoPublicacion = "";
			
			articulo.put("titulo", titulos.text());			  
			//System.out.println("-- Nombre publicacion: "+titulos.text());
			Elements autores = publicacion.select("div.gsc_vcd_value");
			String[] listaAutores = autores.first().text().split(",");
			for (String a: listaAutores) {
				JSONObject autorSimple = new JSONObject();
				autorSimple.put("nombre", a);
				autor.add(autorSimple);
			}
			articulo.put("autor", autor);
			//System.out.println("-- autores: "+autores.first().text());
			Boolean tieneFecha = publicacion.select("div.gsc_vcd_field").get(1).toString().contains("Fecha");
			if (tieneFecha) {
				Elements fechaPublicacion = publicacion.select("div.gsc_vcd_value");
				articulo.put("fecha",fechaPublicacion.get(1).text());
			}
			
			//System.out.println("-- Fecha: "+fechaPublicacion.get(1).text());
			Elements tipo = publicacion.select("div.gsc_vcd_field");
			if (tipo.size() >= 3) {
				if (tipo.get(2).text().equals("Descripción") || tipo.get(2).text().equals("Origen")) {
					//System.out.println("-- tipo: Otro");
					tipoPublicacion = "Otro";
				} 
				else {
					//System.out.println("-- tipo: "+tipo.get(2).text());
					tipoPublicacion = tipo.get(2).text();
				}
			}
			else {
				//System.out.println("-- tipo: N/A");
				tipoPublicacion = "N/A";
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
				   .header("cookie", "CONSENT=YES+ES.es+V10; SID=zwW1AuvYjlgnf-KfymCDokwhhbO-yPqdjeOoj2i1Fu0gK8pnwyLzSk_uR8AmgDZJN2IyuQ.; HSID=AgBAM2R08NPs_Q0Gb; SSID=Ao5mLQzgAUn8C1c5w; APISID=3_c5DASu2HslOlAM/Atjp92VTf1bPycsvl; SAPISID=7DXMMPQMeXWJl4nb/AsUwow0N6kp5miOAq; GSP=LM=1523144400:S=JbcfPCDuxxrEndYA; 1P_JAR=2018-4-9-0; NID=127=X2FXhQyNua9wc6DXkLt7Pr45i6iGSbondbIubdukxfwrdRGmOjxpseE_km_kcHp-sKzMK-E3_8_l5wDV1uEY9nf9omhrKeVm4eFoKTkU9xMgvGz_JVJeeo8ic5kU8yJPJpc0a3ABwTYf_zsPtqMpG1o3HeEnBcdgIljgyuWRIPKj4mcaTKNJxjCEKwhTKI_WiYAi7NWwax5C0HCFjoYKo6N_S-1ZHcGkEgaWGxHOTMxotn_UvNMPdJ9QtcuwiQiHZrmhhsanGesuJEJm; GOOGLE_ABUSE_EXEMPTION=ID=5a1e6d8493dbeb64:TM=1523241333:C=r:IP=181.56.40.114-:S=APGng0vNyKKSjrBQKasE3qSv0CUOfxZugw; SIDCC=AEfoLeYaxQBscVgBUlLZYBgML62vHIUmxynIr0Owt19d9cMfcsAn_WVGaffEvIpVyUrUgfmhzg")
		       	   .timeout(50000)
		           .get();
			//System.out.println("Documento: "+document.toString());
			entradas = document.select("td.gsc_a_t");
			urlGoo = "https://scholar.google.com";
			
			for(Element el: entradas.select("a[href]")){
				String urlPub = el.attr("data-href");
				//System.out.println("URL bien: "+urlGoo+urlPub.replace("amp;", ""));
				Document publicacion = Jsoup.connect(urlGoo+urlPub.replace("amp;", ""))
						.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
						   .header("cookie", "CONSENT=YES+ES.es+V10; SID=zwW1AuvYjlgnf-KfymCDokwhhbO-yPqdjeOoj2i1Fu0gK8pnwyLzSk_uR8AmgDZJN2IyuQ.; HSID=AgBAM2R08NPs_Q0Gb; SSID=Ao5mLQzgAUn8C1c5w; APISID=3_c5DASu2HslOlAM/Atjp92VTf1bPycsvl; SAPISID=7DXMMPQMeXWJl4nb/AsUwow0N6kp5miOAq; GSP=LM=1523144400:S=JbcfPCDuxxrEndYA; 1P_JAR=2018-4-9-0; NID=127=X2FXhQyNua9wc6DXkLt7Pr45i6iGSbondbIubdukxfwrdRGmOjxpseE_km_kcHp-sKzMK-E3_8_l5wDV1uEY9nf9omhrKeVm4eFoKTkU9xMgvGz_JVJeeo8ic5kU8yJPJpc0a3ABwTYf_zsPtqMpG1o3HeEnBcdgIljgyuWRIPKj4mcaTKNJxjCEKwhTKI_WiYAi7NWwax5C0HCFjoYKo6N_S-1ZHcGkEgaWGxHOTMxotn_UvNMPdJ9QtcuwiQiHZrmhhsanGesuJEJm; GOOGLE_ABUSE_EXEMPTION=ID=5a1e6d8493dbeb64:TM=1523241333:C=r:IP=181.56.40.114-:S=APGng0vNyKKSjrBQKasE3qSv0CUOfxZugw; SIDCC=AEfoLeYaxQBscVgBUlLZYBgML62vHIUmxynIr0Owt19d9cMfcsAn_WVGaffEvIpVyUrUgfmhzg")
				       	.timeout(50000)
						.get();
				Elements titulos = publicacion.select("a.gsc_vcd_title_link");
			   
				JSONObject articulo = new JSONObject();
				JSONArray autor = new JSONArray();
				String tipoPublicacion = "";
				
				articulo.put("titulo", titulos.text());			  
				//System.out.println("-- Nombre publicacion: "+titulos.text());
				Elements autores = publicacion.select("div.gsc_vcd_value");
				String[] listaAutores = autores.first().text().split(",");
				for (String a: listaAutores) {
					JSONObject autorSimple = new JSONObject();
					autorSimple.put("nombre", a);
					autor.add(autorSimple);
				}
				articulo.put("autor", autor);
				//System.out.println("-- autores: "+autores.first().text());
				Boolean tieneFecha = publicacion.select("div.gsc_vcd_field").get(1).toString().contains("Fecha");
				if (tieneFecha) {
					Elements fechaPublicacion = publicacion.select("div.gsc_vcd_value");
					articulo.put("fecha",fechaPublicacion.get(1).text());
				}
				
				//System.out.println("-- Fecha: "+fechaPublicacion.get(1).text());
				Elements tipo = publicacion.select("div.gsc_vcd_field");
				if (tipo.size() >= 3) {
					if (tipo.get(2).text().equals("Descripción") || tipo.get(2).text().equals("Origen") || tipo.get(2).text().contains("Google")) {
						//System.out.println("-- tipo: Otro");
						tipoPublicacion = "Otro";
					} 
					else {
						//System.out.println("-- tipo: "+tipo.get(2).text());
						tipoPublicacion = tipo.get(2).text();
					}
				}
				else {
					//System.out.println("-- tipo: N/A");
					tipoPublicacion = "N/A";
				}
				articulo.put("tipo",tipoPublicacion);
			   	articulos.add(articulo);		   
			}
			
		}
		
		System.out.println("Fin");
		obj.put("publicaciones", articulos);
		return obj; 		   
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
