package JavaBean;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;
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
			  
	        
		
		} 
                catch (IOException ioe) {
		   ioe.printStackTrace();
                }
                
		
        JSONObject mainObj = obtenerArticulos(url);
        JSONObject mainObj1 = obtenerLibros(url); 
        JSONObject mainObj3 = obtenerCapitulos(url);
        JSONObject mainObj2 = obtenerEventos(url); 
        JSONObject software = obtenerSoftware(url);
        
        JSONObject trabajosDeGrado = obtenerTrabajosDeGrado(url);       
        
        combined.put("datosBasicos", obj);
        combined.put("publicaciones", mainObj);
        combined.put("Libros", mainObj1);
        combined.put("eventos", mainObj2);
        combined.put("capitulos", mainObj3);     
        combined.put("software", software);
        combined.put("trabjosDirigidos",trabajosDeGrado);
       //System.out.println(combined.toString());
       System.out.println("FIN obtenerInformacion()");
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
                          Date sqlDate1 = new java.sql.Date(0);
                                 String[] edi=editorial.split("DOI");
                                 if(edi.length >=1){
                                     String [] fecha=edi[0].split(",");
                               
                                     String fechaP=fecha[fecha.length-2].trim();
                                 
                                 if(!fechaP.equals("")){
                                 int fechaArt=Integer.parseInt(fechaP);
                                 sqlDate1=new java.sql.Date(fechaArt-1900,0,1);
                             }   
                            }
                           String[] editorial1=editorial.split("Palabras:");
			  
			  JSONObject arti = new JSONObject();
			  arti.put("titulo", titulo);
			  arti.put("lugarPublicacion", en);
			  arti.put("codigoPublicacion",issn);
			  arti.put("tipo","articulo");
			  arti.put("autor",autor);
                          arti.put("fecha",sqlDate1.toString());
			  arti.put("editorial", editorial1[0]);
				
				
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
			 
			  parts = libro.split("En:");
			  //System.out.println(parts[1]);
			  parts1=parts[1].split("ed:");
			  
			  // Lugar de publicacion
			  String en=parts1[0];
                          String fecha="";
                           
                            Date sqlDate1 = new java.sql.Date(0);
                             
                                 String[] lug=en.split(" ");
                                 if(lug.length>=3){
                                     
                                     String fe=lug[2].substring(0,lug[2].length()-1);
                                           
                                       
                                       int fechaArt=Integer.parseInt(fe.trim());
                                       sqlDate1=new java.sql.Date(fechaArt-1900,0,1); 
                                       fecha=sqlDate1.toString();
                                     
                                 }
			  
			  String[] parts2=parts1[1].split("ISBN:");
			  // Codigo ISSN
			  String editorial=parts2[0];
		
			  
			  String [] parts4=parts2[1].split("v.");
			  String ISBN=parts4[0].trim();
			  
                          
                          
			  
			  JSONObject lib = new JSONObject();
			  lib.put("titulo", titulo);
			  lib.put("lugarPublicacion", en);
			  lib.put("codigoPublicacion",ISBN);
			  lib.put("tipo","libro");
			  lib.put("autor",autor);
                          lib.put("fecha",fecha);
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
                            eve.put("tipo","conferencia");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Tipo de evento:");
				   if(parts1.length>=2) {
					   //nombre del evento
					   eve.put("lugarPublicacion",parts1[0].trim());
					  // System.out.println(parts1[0].trim());
				   }
			   }
			   parts=evento.split("Tipo de evento:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Ámbito:");
				   if(parts1.length>=2) {
					   //Tipo de evento
					  // System.out.println(parts1[0].trim());
					   eve.put("tipoEspecifico",parts1[0].trim());
				   }
			   }
			   
			   parts=evento.split("Ámbito:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Productos asociados Nombre del producto:");
				   if(parts1.length>=2) {
					   //Ambito
					   //System.out.println(parts1[0].trim());
					   eve.put("duracion",parts1[0].trim());
				   }
			   }
			   
			   
			   parts=evento.split("Productos asociados Nombre del producto:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Tipo de producto:");
				   if(parts1.length>=2) {
					   //Tipo de producto
					   //System.out.println(parts1[0].trim());
					   eve.put("titulo",parts1[0].trim());
				   }
			   }
			   
			   parts=evento.split("Tipo de producto:");
			   if(parts.length>=2) {
				   String[] parts1=parts[1].split("Instituciones asociadas");
				   if(parts1.length>=2) {
					   //Tipo de producto
					   //System.out.println(parts1[0].trim());
					   eve.put("editorial",parts1[0].trim());
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
						   partici.put("rol", parts2[1].trim());
						   
						   participantes.add(partici);
					   }
				   }
			   }
			   eve.put("autor",participantes);
			   
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
				        	capituloLibros.put("codigoPublicacion", parts4[0].trim());
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
				        	String[] obtfecha=ed.split(",");
                                                int fechaArt=Integer.parseInt(obtfecha[obtfecha.length-1]);
                                                Date sqlDate1=new java.sql.Date(fechaArt-1900,0,1);
                                                capituloLibros.put("fecha", sqlDate1.toString());
				        	  
				        	
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
       
    public JSONObject obtenerSoftware (String url) {
        Document doc = null; 
        JSONArray listaSoftware = new JSONArray();
        JSONObject respuesta = new JSONObject();
        
        try {
            doc = Jsoup.connect(url).get(); // URL shortened!
            Elements ele=doc.select("div.container table");
            Element elementoDatos=null;
            System.out.println(ele.size());
            for(Element e : ele.select("td"))
                for(Element elemento : e.select("a[name=software]"))
                    elementoDatos=e;

            for(Element el: elementoDatos.select("blockquote")) {
             
                JSONObject productoSoftware = new JSONObject();
                String [] parts=el.text().split(", ");
                System.out.println("---------------------------------");
                
                JSONArray autores = new JSONArray();
                String nombre = "";
                String lugar = "";
                String fecha = "";
                String plataforma = "";
                String plataformaIngresar = "";
                String segunda_plataforma = "";
                String registro = "";
                int indiceNombreComercial = 0;
                
                for (int i=0;i<parts.length;i++){
                    if (parts[i].toString().contains("Nombre comercial")) {
                        indiceNombreComercial = i;
                        nombre = parts[i-1].toString();
                    }
                    if (parts[i].toString().contains(". En:")) {
                        String [] lugarSplit = parts[i].split(". En:");
                        lugar = lugarSplit[1];
                        
                        String [] fechaSplit = parts[i+1].split(",");
                        fecha = fechaSplit[1];
                    }
                    if (parts[i].toString().contains(".plataforma:")) {
                        String [] plataformaSplit = parts[i].split(".plataforma:");
                        plataforma = plataformaSplit[1].toString();
                    }
                    if (parts[i].toString().contains(".ambiente:")) {
                        String [] segunda_plataformaSplit = parts[i].split(".ambiente:");
                        segunda_plataforma = segunda_plataformaSplit[1].toString();
                    }
                    if (parts[i].toString().contains("contrato/registro:")) {
                        String [] registroSplit = parts[i].split("contrato/registro:");
                        registro = registroSplit[1].toString();
                    }
                }
                
                //Co autores
                if (indiceNombreComercial > 0) {
                    for (int i=0; i<indiceNombreComercial-1;i++) {
                        JSONObject autor = new JSONObject();
                        autor.put("nombre", parts[i].toString().trim());
                        autores.add(autor);
                    }
                }
                System.out.println("Publicaicon: "+nombre+" -- "+lugar+" -- "+fecha+" -- "+plataforma+" -- "+segunda_plataforma +" -- "+registro);
                System.out.println("Autores: "+autores.toJSONString());
                                
                productoSoftware.put("titulo", nombre.trim());
                productoSoftware.put("tipo", "software");
                productoSoftware.put("codigoPublicacion", registro.equals(" ")?null:registro.trim());
                productoSoftware.put("lugarPublicacion", lugar.equals("")?null:lugar.trim());
                productoSoftware.put("editorial", null);
                productoSoftware.put("fecha", fecha.equals("")?null:fecha.trim()+"-01-01");
                productoSoftware.put("extraido", "CvLac");
                productoSoftware.put("duracion", null);
                productoSoftware.put("tipoEspecifico", " Computacional");
                productoSoftware.put("plataforma", plataforma.equalsIgnoreCase("")?(segunda_plataforma.equals("")?null:segunda_plataforma.trim()):(segunda_plataforma.equalsIgnoreCase("")?plataforma:plataforma+" - "+segunda_plataforma));
                productoSoftware.put("autor", autores);    
                
                listaSoftware.add(productoSoftware);
            }
        }
        catch (IOException ioe) {
           ioe.printStackTrace();
        }
        respuesta.put("software", listaSoftware);
        return respuesta;
    }
    
     public JSONObject obtenerTrabajosDeGrado (String url) {
        Document doc = null; 
        JSONArray listaTrabajosDirigidos = new JSONArray();
        JSONObject respuesta = new JSONObject();
        
        try {
            doc = Jsoup.connect(url).get(); // URL shortened!
            Elements ele=doc.select("div.container table");
            Element elementoDatos=null;
            JSONArray listaTiposEpecificos = new JSONArray();
            JSONArray listaUniversidadesEliminar = new JSONArray();
            int indiceElementosDatos = 0;
            
            for(Element e : ele.select("td"))
                for(Element elemento : e.select("a[name=trabajos_dirigi]"))
                    elementoDatos=e;
            
            
            for(Element el: elementoDatos.select("strong")) {
                String [] tiposEspecificos_split=el.html().split("&nbsp");
                for (int i=0;i<tiposEspecificos_split.length;i++){
                    if (tiposEspecificos_split[i].contains("Trabajos dirigidos/Tutorías - ")) {
                        listaTiposEpecificos.add(tiposEspecificos_split[0].split("Trabajos dirigidos/Tutorías - ")[1]);            
                    }
                }
            }
            
            for(Element el: elementoDatos.select("blockquote")) {
                String [] tiposEspecificos_split=el.html().split("&nbsp");
                for (int i=0;i<tiposEspecificos_split.length;i++){
                    //System.out.println("Elemtno: "+tiposEspecificos_split[i]);
                    if (i == 1)
                        listaUniversidadesEliminar.add(tiposEspecificos_split[1].split(";")[1].replace("  ","").trim());   
                }
            }
            
            for(Element el: elementoDatos.select("blockquote")) {
             
                JSONObject productoTrabajoDirigido = new JSONObject();
                String [] parts=el.text().split(", ");
                                              
                JSONArray autores = new JSONArray();
                String titulo = "";
                String fecha = "";
                String duracion = "";
                String tipoEspecifico = listaTiposEpecificos.get(indiceElementosDatos).toString();
                
                if (parts[2].contains("Persona orientada")) {
                    String [] titulo_Split = parts[1].split("Estado:");
                    titulo = titulo_Split[0];
                    titulo_Split = titulo.split(listaUniversidadesEliminar.get(indiceElementosDatos).toString());
                    titulo = titulo_Split[0];
                    
                    String [] fecha_split = parts[1].split("Estado:");
                    String [] fecah_Split_sin_comas = fecha_split[1].split(" ,");
                    fecha = fecah_Split_sin_comas[1]; 
                }
                else {
                    int indiceComienzoTitulo = 1;
                    int indicePersonasOrientadas = 2;
                    for (int i=0;i<parts.length;i++) {
                        if (parts[i].contains("Persona orientada:")) {
                            indicePersonasOrientadas = i;
                            //System.out.println("indicePersonasOrientadas: "+indicePersonasOrientadas);
                        }
                    }
                    
                    for (int i=1;i<indicePersonasOrientadas;i++) {
                        if (parts[indicePersonasOrientadas].toUpperCase().trim().contains(parts[i].toUpperCase().trim()))
                            indiceComienzoTitulo++;
                    }
                    
                    for (int i=indiceComienzoTitulo;i<indicePersonasOrientadas-1;i++)
                        titulo += parts[i].trim();
                    
                    String [] ultimaParteTitulo_split = parts[indicePersonasOrientadas-1].split(" Estado:");
                    titulo += ultimaParteTitulo_split[0]+" ";
                }
                               
                for (int i=0;i<parts.length;i++){ 
                    if (parts[i].toString().contains("meses")) {
                        if (parts[i].toString().contains("Areas")) {
                            String [] duracion_Split = parts[i].split("Areas");
                            duracion = duracion_Split[0].trim();
                        }
                        else
                            duracion = parts[i].toString();
                    }
                    
                    if (parts[i].toString().contains("Persona orientada")) {
                        String [] autores_Split_personas = parts[i].split("Persona orientada:");
                        String [] autores_split = {};
                        //SEPARAR POR: Y
                        if (autores_Split_personas[1].contains(" y ")) {
                            autores_split = autores_Split_personas[1].split(" y ");
                        }
                        if (autores_Split_personas[1].contains(" Y ")) {
                            autores_split = autores_Split_personas[1].split(" Y ");
                        }
                        //SEPARAR POR: .
                        if (autores_Split_personas[1].contains(". ")) {
                            autores_split = autores_Split_personas[1].split("[.] ");
                        }
                        //SEPARARA POR: ,
                        if (autores_Split_personas[1].contains(", ")) {
                            autores_split = autores_Split_personas[1].split(", ");
                        }
                        //SEPARAR POR: - 
                        if (autores_Split_personas[1].contains(" - ")) {
                            autores_split = autores_Split_personas[1].split(" - ");
                        }
                            
                        if (autores_split.length== 0){
                            JSONObject autor = new JSONObject();
                            autor.put("nombre", autores_Split_personas[1].toString().trim());
                            autores.add(autor);
                        }
                        else {
                            int tamanoAutores = autores_split.length;
                            for (int j=0; j<tamanoAutores;j++) {
                                JSONObject autor = new JSONObject();
                                autor.put("nombre", autores_split[j].toString().trim());
                                autores.add(autor);
                            }
                        }
                        
                    }
                }
                
                System.out.println("Trabajo de grado: "+titulo+" -- "+fecha+" -- "+duracion+" -- "+tipoEspecifico);
                System.out.println("Autores: "+autores.toJSONString());

                productoTrabajoDirigido.put("titulo", titulo.trim());
                productoTrabajoDirigido.put("tipo", "trabajo dirigido");
                productoTrabajoDirigido.put("codigoPublicacion", null);
                productoTrabajoDirigido.put("lugarPublicacion", null);
                productoTrabajoDirigido.put("editorial", null);
                productoTrabajoDirigido.put("fecha", fecha.equals("")?null:fecha.trim()+"-01-01");
                productoTrabajoDirigido.put("extraido", "CvLac");
                productoTrabajoDirigido.put("duracion", duracion);
                productoTrabajoDirigido.put("tipoEspecifico", tipoEspecifico);
                productoTrabajoDirigido.put("plataforma", null);
                productoTrabajoDirigido.put("autor", autores);    

                listaTrabajosDirigidos.add(productoTrabajoDirigido);
                indiceElementosDatos++;
            }
        }
        catch (IOException ioe) {
           ioe.printStackTrace();
        }
        respuesta.put("trabjosDirigidos", listaTrabajosDirigidos);
        return respuesta;
    }
}

