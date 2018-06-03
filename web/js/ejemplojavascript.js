var jsonCVLAC;
var jsonReserch;
var jsonGoogle;
//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-dev/";

window.onload = function(){
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
    document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
    document.getElementById("facultad").innerHTML = SESSION.datosBasicos.nombreFacultad;
    // Cargar Nav-bar
    cargarNavBar(SESSION.roles, "Carga Fuentes Externas");
}
function start(){
	var text = document.getElementById("text");
	getServelet(SERVER_URL+'EjemploServlet', null, function(serveletResponse) {
	    text.innerHTML = serveletResponse;
	});
}

function start1(){
       
	var respuesta;
	var url=document.getElementById("urlcvlac").value;
	var params = "urlcvlac="+url;
	getServelet(SERVER_URL+'extraccionSistemasExternosServlet',null, params, function(serveletResponse) {
	   //text.innerHTML = serveletResponse;
	    respuesta=serveletResponse;
            
	    var json=JSON.parse(respuesta);
	    jsonCVLAC=respuesta;

                var formacionAcademica=json.formacionAcademica;
                var tableFA = document.getElementById("tablaFormacionAcademica");
                 var tituloFA = document.getElementById("tituloFormacionAcademica");
                 tituloFA.style.display = 'block';

                 for(var i=0; i< formacionAcademica.length; i++){
                         var numRow=tableFA.rows.length;
                         var row = tableFA.insertRow(numRow);
                         var cell1 = row.insertCell(0);
                         cell1.innerHTML = formacionAcademica[i].categoria+', '+ formacionAcademica[i].universidad+', '+ formacionAcademica[i].titulo+', '+ formacionAcademica[i].fecha;

                 }
                 
                var idiomas=json.idiomas;
                var tableId = document.getElementById("idiomas");
               

                 for(var i=0; i< idiomas.length; i++){
                         var numRow=tableId.rows.length;
                         var row = tableId.insertRow(numRow);
                         var cell1 = row.insertCell(0);
                         cell1.innerHTML = idiomas[i].idioma;

                 }
                 
                 var areasActuacion=json.areasActuacion;
                var tableA = document.getElementById("areasActuacion");
               

                 for(var i=0; i< areasActuacion.length; i++){
                         var numRow=tableA.rows.length;
                         var row = tableA.insertRow(numRow);
                         var cell1 = row.insertCell(0);
                         cell1.innerHTML = areasActuacion[i].area;

                 }

            var articulos=json.publicaciones.articulos;
            var table = document.getElementById("tablaArticulos");
            table.style.display = 'block';

            var tituloArticulos = document.getElementById("tituloArticulos");
            tituloArticulos.style.display = 'block';
            for(var i=0; i< articulos.length; i++){
                    var numRow=table.rows.length;
                    var row = table.insertRow(numRow);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                    var cell6 = row.insertCell(5);
                    cell1.innerHTML = articulos[i].titulo;
                var autores=" ";
                    for(var j=0; j<articulos[i].autor.length; j++){
                       autores+=articulos[i].autor[j].nombre;
                       if(j!==articulos[i].autor.length-1){
                            autores+=",";
                       }
                    }

                     cell2.innerHTML = autores;
                     cell3.innerHTML =articulos[i].lugarPublicacion;
                     cell4.innerHTML =articulos[i].ISSN;
                     cell5.innerHTML =articulos[i].editorial;

                      var bt = document.createElement("button");
                      bt.className = "btn btn-danger"
                      bt.innerHTML = "Eliminar";
                      bt.setAttribute("onclick", "deleteRow(this,'tablaArticulos')"); 

                  cell6.appendChild(bt);

           }
    
        var libros=json.Libros.libros;
        var table1 = document.getElementById("tablaLibros");
        table1.style.display = 'block';

        var tituloLibros = document.getElementById("tituloLibros");
        tituloLibros.style.display = 'block';

        for(var i=0; i< libros.length; i++){
                var numRow=table1.rows.length;
                var row = table1.insertRow(numRow);
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                var cell5 = row.insertCell(4);
                var cell6 = row.insertCell(5);
                cell1.innerHTML = libros[i].titulo;
            var autores=" ";
                for(var j=0; j<libros[i].autor.length; j++){
                   autores+=libros[i].autor[j].nombre;
                   if(j!==libros[i].autor.length-1){
                        autores+=",";
                   }
                }

                 cell2.innerHTML = autores;
                 cell3.innerHTML =libros[i].lugarPublicacion;
                 cell4.innerHTML =libros[i].ISBN;
                 cell5.innerHTML =libros[i].editorial;
                  var bt = document.createElement("button");
                  bt.className = "btn btn-danger"
                  bt.innerHTML = "Eliminar";
                  bt.setAttribute("onclick", "deleteRow(this,'tablaLibros')"); 

              cell6.appendChild(bt);
            }	
            
            
            
        var eventos=json.eventos.eventos;
        var table4 = document.getElementById("tablaEventos");
        table4.style.display = 'block';


        for(var i=0; i< eventos.length; i++){
                var numRow=table4.rows.length;
                var row = table4.insertRow(numRow);
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                var cell5 = row.insertCell(4);
                var cell6 = row.insertCell(5);
                var cell7 = row.insertCell(6);
                cell1.innerHTML = eventos[i].lugarPublicacion;
                cell2.innerHTML = eventos[i].tipo;
                cell3.innerHTML = eventos[i].duracion;
                cell4.innerHTML = eventos[i].titulo;
                cell5.innerHTML = eventos[i].tipoEspecifico;
                var autores=" ";
                for(var j=0; j<eventos[i].autor.length; j++){
                   autores+=eventos[i].autor[j].nombre+" rol:"+eventos[i].autor[j].rolEvento;
                   if(j!==eventos[i].autor.length-1){
                        autores+=",";
                   }
                }
                
                cell6.innerHTML =autores;
                  var bt = document.createElement("button");
                  bt.className = "btn btn-danger"
                  bt.innerHTML = "Eliminar";
                  bt.setAttribute("onclick", "deleteRow(this,'tablaEventos')"); 

                 cell7.appendChild(bt);
            }	
            
            var cap=json.capitulos.capitulos;
        var table5 = document.getElementById("tablaCapitulos");
        table5.style.display = 'block';

        for(var i=0; i< cap.length; i++){
                var numRow=table5.rows.length;
                var row = table5.insertRow(numRow);
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                var cell5 = row.insertCell(4);
                var cell6 = row.insertCell(5);
                cell1.innerHTML = cap[i].titulo;
            var autores=" ";
                for(var j=0; j<cap[i].autor.length; j++){
                   autores+=cap[i].autor[j].nombre;
                   if(j!==cap[i].autor.length-1){
                        autores+=",";
                   }
                }

                 cell2.innerHTML = autores;
                 cell3.innerHTML =cap[i].lugarPublicacion;
                 cell4.innerHTML =cap[i].ISBN;
                 cell5.innerHTML =cap[i].editorial;
                  var bt = document.createElement("button");
                  bt.className = "btn btn-danger"
                  bt.innerHTML = "Eliminar";
                  bt.setAttribute("onclick", "deleteRow(this,'tablaCapitulos')"); 

              cell6.appendChild(bt);
            }	
	   
	});
		

}

function deleteRow(r, nombreTabla) {

	var opcion = confirm("Desea eliminar la publicacion");
	if ( opcion == true) {
	    var i = r.parentNode.parentNode.rowIndex;
            document.getElementById(nombreTabla).deleteRow(i);
	} 
    
}


function start2(){
	
	var respuesta;
	var url=document.getElementById("urlresearch").value;
	var params = "urlresearch="+url;
	getServelet(SERVER_URL+'ExtraccionResearchServlet', null,params,function(serveletResponse) {
            respuesta=serveletResponse;
            jsonReserch=respuesta;
            var json=JSON.parse(respuesta);
           // alert(jsonReserch);
           // document.getElementById("jsonR").innerHTML=jsonReserch;
            
            var arti=json.publicaciones;
            var tabla = document.getElementById("tablaResearchArticulos");
            tabla.style.display = 'block';
            for(var i=0; i< arti.length; i++){
                if(arti[i].tipo === "articulo"){
                    var numRow=tabla.rows.length;
                    var row = tabla.insertRow(numRow);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                     

                    cell1.innerHTML = arti[i].titulo;
                    cell2.innerHTML= arti[i].fecha;
                    cell3.innerHTML= arti[i].editorial;
                    cell4.innerHTML= arti[i].codigoPublicacion;
                    for(var j=0; j<arti[i].autor.length; j++){
                        
                        autores+=arti[i].autor[j].nombre;
                        if(j!==arti[i].autor.length-1){
                            autores+=",";
                        }
                    }

                    cell5.innerHTML = autores;
                    //cell3.innerHTML ="Fecha";
                    
                }	  
            }
            
             var tabla1 = document.getElementById("tablaResearchLibros");
            tabla1.style.display = 'block';
            for(var i=0; i< arti.length; i++){
                if(arti[i].tipo === "libro"){
                    var numRow=tabla1.rows.length;
                    var row = tabla1.insertRow(numRow);
                     var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                     

                    cell1.innerHTML = arti[i].titulo;
                    cell2.innerHTML= arti[i].fecha;
                    cell3.innerHTML= arti[i].editorial;
                    cell4.innerHTML= arti[i].codigoPublicacion;
                    var autores=" ";
                    for(var j=0; j<arti[i].autor.length; j++){
                        autores+=arti[i].autor[j].nombre;
                        if(j!==arti[i].autor.length-1){
                            autores+=",";
                        }
                    }

                    cell5.innerHTML = autores;
                   
                }
            }


            var tabla2 = document.getElementById("tablaResearchCapitulos");
            tabla2.style.display = 'block';
            for(var i=0; i< arti.length; i++){
                if(arti[i].tipo === "capitulo"){
                    var numRow=tabla2.rows.length;
                    var row = tabla2.insertRow(numRow);
                     var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                     

                    cell1.innerHTML = arti[i].titulo;
                    cell2.innerHTML= arti[i].fecha;
                    cell3.innerHTML= arti[i].editorial;
                    cell4.innerHTML= arti[i].codigoPublicacion;
                    var autores=" ";
                    for(var j=0; j<arti[i].autor.length; j++){
                        autores+=arti[i].autor[j].nombre;
                        if(j!==arti[i].autor.length-1){
                            autores+=",";
                        }
                    }

                    cell5.innerHTML = autores;
                    

                   
                }
            } 

            var tabla3 = document.getElementById("tablaResearchConferencia");
            tabla3.style.display = 'block';
            for(var i=0; i< arti.length; i++){
                if(arti[i].tipo === "conferencia"){
                    var numRow=tabla3.rows.length;
                    var row = tabla3.insertRow(numRow);
                     var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                     

                    cell1.innerHTML = arti[i].titulo;
                    cell2.innerHTML= arti[i].fecha;
                    cell3.innerHTML= arti[i].editorial;
                    cell4.innerHTML= arti[i].codigoPublicacion;
                    var autores=" ";
                    for(var j=0; j<arti[i].autor.length; j++){
                        autores+=arti[i].autor[j].nombre;
                        if(j!==arti[i].autor.length-1){
                            autores+=",";
                        }
                    }

                    cell5.innerHTML = autores;
                   
                }
            }
	});
	
	//var json={"publicaciones":[{"tipo":"Article","titulo":"A systematic review of serious games in medical education: quality of evidence and pedagogical strategy A systematic review of serious games in medical education: quality of evidence and pedagogical strategy","autor":[{"nombre":"Iouri Gorbanev"},{"nombre":"Sandra M Agudelo-Londoño"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Oscar Mauricio Muñoz"}]},{"tipo":"Article","titulo":"Concept Attribute Labeling and Context-Aware Named Entity Recognition in Electronic Health Records","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Oscar Mauricio Muñoz"},{"nombre":"Cyril Labbé"}]},{"tipo":"Article","titulo":"A Strategy for Prioritizing Electronic Medical Records Using Structured Analysis and Natural Language Processing","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Oscar Mauricio Muñoz"},{"nombre":"Ricardo Bohórquez"}]},{"tipo":"Chapter","titulo":"ICT for Enabling the Quality Evaluation of Health Care Services: A Case Study in a General Hospital","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Alejandro Sierra"},{"nombre":"Ricardo Bohórquez"}]},{"tipo":"Chapter","titulo":"A Review of Existing Applications and Techniques for Narrative Text Analysis in Electronic Medical Records","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Santiago Quintero"},{"nombre":"Dario Londoño"}]},{"tipo":"Conference Paper","titulo":"CSL: A Combined Spanish Lexicon - Resource for Polarity Classification and Sentiment Analysis","autor":[{"nombre":"Luis G. Moreno-Sandoval"},{"nombre":"Paola Beltrán-Herrera"},{"nombre":"Jaime A. Vargas-Cruz"},{"nombre":"Juan C. García-Díaz"}]},{"tipo":"Article","titulo":"Named Entity Recognition Over Electronic Health Records Through a Combined Dictionary-based Approach","autor":[{"nombre":"Oscar Mauricio Muñoz"},{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Alejandro Sierra"},{"nombre":"Angel A. García"}]},{"tipo":"Conference Paper","titulo":"HTL Model: A Model for Extracting and Visualizing Medical Events from Narrative Text in Electronic Health Records","autor":[{"nombre":"Eddie Paul Hernández"},{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Oscar Mauricio Muñoz"}]},{"tipo":"Chapter","titulo":"A Review of Existing Applications and Techniques for Narrative Text Analysis in Electronic Medical Records","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Santiago Quintero"},{"nombre":"Dario Londoño"}]},{"tipo":"Article","titulo":"Generic framework for enriching services: A multiagent approach","autor":[{"nombre":"Angela Carrillo Ramos"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Maria Paula Arias-Baez"},{"nombre":"Hervé Martin"}]},{"tipo":"Book","titulo":"ASHYI: PLATAFORMA BASADA EN AGENTES PARA LA PLANIFICACIÓN DINÁMICA, INTELIGENTE Y ADAPTATIVA DE ACTIVIDADES APLICADA A LA EDUCACIÓN PERSONALIZADA","autor":[{"nombre":"Angela Carrillo Ramos"},{"nombre":"Mery Yolima Uribe Rios"},{"nombre":"Monica Ilanda Brijaldo"},{"nombre":"Julio Ernesto Carreño Vargas"}]},{"tipo":"Conference Paper","titulo":"Dynamic Adaptive Activity Planning in Education: Implementation and Case Study","autor":[{"nombre":"Jaime A. Pavlich-Mariscal"},{"nombre":"Mery Yolima Uribe Rios"},{"nombre":"Luisa Fernanda Barrera León"},{"nombre":"Hervé Martin"}]},{"tipo":"Article","titulo":"A System for the Semi-Automatic Evaluation of Clinical Practice Guideline Indicators","autor":[{"nombre":"Oscar Mauricio Muñoz"},{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"}]},{"tipo":"Conference Paper","titulo":"BigTexts - A Framework for the Analysis of Electronic Health Record Narrative Texts based on Big Data Technologies","autor":[{"nombre":"Wilson Alzate"},{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Oscar Mauricio Muñoz"}]},{"tipo":"Chapter","titulo":"A systemic, participative design of decision support services for clinical research","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Ricardo Bohórquez"},{"nombre":"Dario Londoño"}]},{"tipo":"Chapter","titulo":"A system for the semi-automatic evaluation of clinical practice guideline indicators","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Mar?a Patricia Amortegui"},{"nombre":"Rafael A Gonzalez"},{"nombre":"M.M. Ascanio"}]},{"tipo":"Article","titulo":"An Executable Knowledge Base for Clinical Practice Guideline Rules","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Oscar Mu?oz"},{"nombre":"Dar?o Londo?o"},{"nombre":"Álvaro Andrés Bustamante"}]},{"tipo":"Chapter","titulo":"Improving Decision-Making for Clinical Research and Health Administration","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Ricardo Bohorquez"},{"nombre":"Dario Londoño"}]},{"tipo":"Conference Paper","titulo":"4Med Método para el Desarrollo de Aplicaciones E-Health \u2013 4Med Method for the Development of E-Health Applications","autor":[{"nombre":"Miguel Eduardo Torres Moreno"},{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Patricia Amortegui"}]},{"tipo":"Article","titulo":"A Systemic, Participative Design of Decision Support Services for Clinical Research:","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"},{"nombre":"Ricardo Bohórquez"},{"nombre":"Dario Londoño"}]},{"tipo":"Conference Paper","titulo":"An Executable Knowledge Base for Clinical Practice Guideline Rules","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Oscar Mauricio Muñoz"},{"nombre":"Dario Londoño"},{"nombre":"Álvaro Andrés Bustamante"}]},{"tipo":"Conference Paper","titulo":"MetaExtractor: A System for Metadata Extraction from Structured Data Sources","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Miguel Eduardo Torres Moreno"},{"nombre":"Fabián Roldán"}]},{"tipo":"Chapter","titulo":"Proposal for Interactive Anonymization of Electronic Medical Records","autor":[{"nombre":"Carlos Moque"},{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"}]},{"tipo":"Article","titulo":"AnonymousData.co: A Proposal for Interactive Anonymization of Electronic Medical Records","autor":[{"nombre":"Carlos Moque"},{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"}]},{"tipo":"Conference Paper","titulo":"Plataforma basada en Agentes para Apoyo a la Colaboración de Equipos de Trabajo","autor":[{"nombre":"María Paula Arias Báez"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Angela Carrillo Ramos"},{"nombre":"Enrique González"}]},{"tipo":"Conference Paper","titulo":"Un Sistema Gestor de Recursos para Ambientes Colaborativos.","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Angela Carrillo Ramos"},{"nombre":"Enrique González"}]},{"tipo":"Conference Paper","titulo":"Adapted data dissemination in collaborative models","autor":[{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Alexandra Pomares Quimbaya"}]},{"tipo":"Conference Paper","titulo":"AYPUY: A resource management system for collaborative environments","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Angela Carrillo Ramos"},{"nombre":"Maria Paula"}]},{"tipo":"Conference Paper","titulo":"Spatial processing of events with landmines in Colombia","autor":[{"nombre":"Carlos Andres Garcia Diaz"},{"nombre":"Alexandra Pomares Quimbaya"}]},{"tipo":"Conference Paper","titulo":"Platform based on agents for support to the collaboration of work teams","autor":[{"nombre":"Maria Paula Arias-Baez"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Angela Carrillo Ramos"},{"nombre":"Enrique González"}]},{"tipo":"Conference Paper","titulo":"La investigación científica basada en el diseño como eje de proyectos de investigación en ingeniería","autor":[{"nombre":"Rafael A Gonzalez"},{"nombre":"Alexandra Pomares Quimbaya"}]},{"tipo":"Conference Paper","titulo":"DISEArch: A strategy for searching electronic medical health records","autor":[{"nombre":"Clavijo"},{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Rafael A Gonzalez"}]},{"tipo":"Conference Paper","titulo":"AYLLU: Architecture for Collaboration in a Distributed Environment","autor":[{"nombre":"Angela Carrillo Ramos"},{"nombre":"María Paula Arias Báez"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Enrique González"}]},{"tipo":"Conference Paper","titulo":"Enriching Services using Agents in Mobile Environments","autor":[{"nombre":"Angela Carrillo Ramos"},{"nombre":"María Paula Arias Báez"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Enrique González"}]},{"tipo":"Chapter","titulo":"Knowledge Management Processes Supported by Ontology Technologies","autor":[{"nombre":"Miguel Eduardo Torres Moreno"},{"nombre":"Alexandra Pomares Quimbaya"}]},{"tipo":"Conference Paper","titulo":"Adaptation for enriching services taking into account mobile contextual features","autor":[{"nombre":"Maria Paula Arias-Baez"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Angela Carrillo Ramos"},{"nombre":"Enrique González"}]},{"tipo":"Conference Paper","titulo":"AES: A Generic Framework for Adapting Information in Multidimensional Contexts","autor":[{"nombre":"María Paula Arias Báez"},{"nombre":"Luis Guillermo Torres-Ribero"},{"nombre":"Angela Carrillo Ramos"},{"nombre":"Enrique González"}]},{"tipo":"Article","titulo":"Distributed query execution adaptation","autor":[{"nombre":"Alexandra Pomares Quimbaya"}]},{"tipo":"Article","titulo":"Improving Source Selection in Large Scale Mediation Systems through Combinatorial Optimization Techniques","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Claudia Roncancio"},{"nombre":"Van-Dat Cung"},{"nombre":"María-Del-Pilar Villamil"}]},{"tipo":"Article","titulo":"Source Selection based on Predicate Assignment Optimization: A Novel Approach for Large Scale Mediation Systems","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Claudia Roncancio"},{"nombre":"Van-Dat Cung"},{"nombre":"Maria del Pilar Villamil"}]},{"tipo":"Conference Paper","titulo":"Source Selection in Large Scale Data Contexts: An Optimization Approach","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Claudia Roncancio"},{"nombre":"Van-Dat Cung"},{"nombre":"María-Del-Pilar Villamil"}]},{"tipo":"Article","titulo":"Mediation and Data Source Selection for Large Scale Virtual Organizations","autor":[{"nombre":"Alexandra Pomares Quimbaya"}]},{"tipo":"Conference Paper","titulo":"Knowledge Based Query Processing in Large Scale Virtual Organizations","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Claudia Roncancio"},{"nombre":"José Abásolo"},{"nombre":"María-Del-Pilar Villamil"}]},{"tipo":"Article","titulo":"Virtual Organizations Data Sharing Profile","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Maria del Pilar Villamil"},{"nombre":"José Abásolo"}]},{"tipo":"Conference Paper","titulo":"Dynamic Source Selection in Large Scale Mediation Systems","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Claudia Roncancio"},{"nombre":"José Abásolo"},{"nombre":"María-Del-Pilar Villamil"}]},{"tipo":"Article","titulo":"Virtual objects in large scale health information systems","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Claudia Roncancio"},{"nombre":"José Abásolo"}]},{"tipo":"Conference Paper","titulo":"Ontologies as the Backbone of KM Processes.","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"Miguel Eduardo Torres Moreno"}]},{"tipo":"Conference Paper","titulo":"ONTOLOGY AUTOMATIC EXTRACTION TO IMPROVE KNOWLEDGE MANAGEMENT SYSTEMS","autor":[{"nombre":"Miguel Eduardo Torres Moreno"},{"nombre":"Alexandra Pomares Quimbaya"}]},{"tipo":"Conference Paper","titulo":"Data Warehouses: An Ontology Approach.","autor":[{"nombre":"Alexandra Pomares Quimbaya"},{"nombre":"José Abásolo"}]},{"tipo":"Article","titulo":"PASTEUR: Un Sistema para Indexación y Búsqueda de Componentes de Software PASTEUR: An Indexing and Localization System for Software Components","autor":[{"nombre":"Alexandra Pomares Quimbaya"}]}]};     
}

function start3(){
	
    var respuesta;
    var url=document.getElementById("urlschoolar").value;
    var params = "urlschoolar="+url;
    getServelet(SERVER_URL+'ExtraccionGoogleSchoolar', null,params, function(serveletResponse) {
        respuesta=serveletResponse;
        var json=JSON.parse(respuesta);
        jsonGoogle= json;
        var arti=json.publicaciones;
        var tabla = document.getElementById("tablaGoogleArticulos");
        tabla.style.display = 'block';
        for(var i=0; i< arti.length; i++){
            var numRow=tabla.rows.length;
            var row = tabla.insertRow(numRow);
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell4 = row.insertCell(2);
            var cell3 = row.insertCell(3);

            cell1.innerHTML = arti[i].titulo;
            var autores=" ";
            for(var j=0; j<arti[i].autor.length; j++){
                autores+=arti[i].autor[j].nombre;
                if(j!==arti[i].autor.length-1){
                    autores+=",";
                }
            }

            cell2.innerHTML = autores;
            cell3.innerHTML = arti[i].tipo;
            var bt = document.createElement("button");
            bt.className = "btn btn-danger"
            bt.innerHTML = "Eliminar";
            bt.setAttribute("onclick", "deleteRow(this,'tablaGoogleArticulos')"); 

            cell4.appendChild(bt);
        }
    });	
}

function GuardarInformacion(){
    
    var jsonPrincipal= JSON.parse(sessionStorage.getItem("principal"));
    if(jsonCVLAC){ 
    var jsoncvla=JSON.parse(jsonCVLAC);
     jsoncvla.id=jsonPrincipal.datosBasicos.id;
      var json=JSON.stringify(jsoncvla);
    
    if(json){
    postServelet(SERVER_URL+"GuardarInformacionServlet",json,function(serveletResponse) {
     //respuesta=serveletResponse;
        console.log("respondio");
    });
    }
    }
    if(jsonGoogle){
    var jsonrs=jsonGoogle;
    jsonrs.id=jsonPrincipal.datosBasicos.id;
    var json1=JSON.stringify(jsonrs);
  
    if(json1){
    postServelet(SERVER_URL+"GuardarInformacionGoogle",json1,function(serveletResponse) {
     //respuesta=serveletResponse;
        console.log("respondio");
    });
    }
    }
     if(jsonReserch){
    var jsonrs=JSON.parse(jsonReserch);
    jsonrs.id=jsonPrincipal.datosBasicos.id;
    var json1=JSON.stringify(jsonrs);
  
    if(json1){
    postServelet(SERVER_URL+"GuardarInformacionReserch",json1,function(serveletResponse) {
     //respuesta=serveletResponse;
        console.log("respondio");
    });
    }
    }
     
}



/**
 * Funcion para consultar un servelet del backend
 * @param url URL del servelet
 * @param data Query con los datos de la consulta
 * @param callback Funcion callback que ejecuta la respuesta
 * @returns
 */
function getServelet(url,data,param,callback) {
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
        	return callback(xhr.responseText);
        }
    }
    xhr.open('GET', url+"?"+param, true);
    xhr.send(data);
}

function postServelet(url,data,callback) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
        	return callback(xhr.responseText);
        }
    }
    xhr.open('POST', url, true);
    xhr.send(data);
}

function getServelet1(url,data,callback) {
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
        	return callback(xhr.responseText);
        }
    }
    xhr.open('GET', url, true);
    xhr.send(data);
}

function cargarNavBar(obj_roles, pageName) {
    var mNavbar = document.getElementById("navbar-sicaa");
    var mDropDownPublicaciones = false;
    var mDropDownConsultas = false;
    
    for (var i = 0; i < obj_roles.length; i++) {
        if (obj_roles[i].id == 1) {
            insertIntoNavbar("navbar-sicaa", obj_roles[i].label, obj_roles[i].referencia, "");
        }
        if (obj_roles[i].id >= 2 && obj_roles[i].id <= 5) {
            if (mDropDownPublicaciones == false) {
                mNavbar.appendChild(createDrowndown("Producción Intelectual", "dropmenu-publicaciones", "active"));
                mDropDownPublicaciones = true;
            }
            if (obj_roles[i].label == pageName)
                insertIntoNavbar("dropmenu-publicaciones", obj_roles[i].label, obj_roles[i].referencia, "active");
            else
                insertIntoNavbar("dropmenu-publicaciones", obj_roles[i].label, obj_roles[i].referencia, "");
        }
        if (obj_roles[i].id >= 6) {
            if (mDropDownConsultas == false) {
                mNavbar.appendChild(createDrowndown("Consultas", "dropmenu-consultas", ""));
                mDropDownConsultas = true;
            }
            insertIntoNavbar("dropmenu-consultas", obj_roles[i].label, obj_roles[i].referencia, "");
        }
    }
}

function createDrowndown(dropdownTitle, dropdownID, isActive) {
    var newDropdown = document.createElement("li");
    newDropdown.className = "dropdown " + isActive;
    newDropdown.innerHTML = "<a class='dropdown-toggle' data-toggle='dropdown' href='#'>" + dropdownTitle + "<span class='caret'></span></a>";
    newDropdown.innerHTML += "<ul class='dropdown-menu' id='" + dropdownID + "'></ul>";
    return newDropdown;
}

function insertIntoNavbar(elementID, label, link, isActive) {
    var newListItem = document.createElement("li");
    newListItem.className = isActive;
    newListItem.innerHTML = "<a href='" + link + "'>" + label + "</a>";
    document.getElementById(elementID).append(newListItem);
}