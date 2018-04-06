// Esta es la version de NETBEANS 55
function start(){
	var text = document.getElementById("text");
	getServelet('https://sicaadev.mybluemix.net/EjemploServlet', null, function(serveletResponse) {
	    text.innerHTML = serveletResponse;
	});
}
// https://sicaadev.mybluemix.net/
// https://sicaa.mybluemix.net/Sicaa/
//http://localhost:38161/SicaaNB/
function start1(){

	var respuesta;
	var url=document.getElementById("urlcvlac").value;
	var params = "urlcvlac="+url;
	getServelet('https://sicaadev.mybluemix.net/extraccionSistemasExternosServlet',null, params, function(serveletResponse) {
	   //text.innerHTML = serveletResponse;
	    respuesta=serveletResponse;
	    var json=JSON.parse(respuesta);
	    
            var nombre = document.getElementById("nombre");
	    var categoria = document.getElementById("categoria");
	    var nacionalidad = document.getElementById("nacionalidad");

		nombre.innerHTML =json.datosBasicos.nombre;
		categoria.innerHTML =json.datosBasicos.categoria;
		nacionalidad.innerHTML =json.datosBasicos.nacionalidad;
	    
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
		      bt.setAttribute("onclick", "deleteRow(this)"); 
	         
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
		      bt.setAttribute("onclick", "deleteRow1(this)"); 
	         
	          cell6.appendChild(bt);

	   }
	   
	});
}

function deleteRow(r) {

	var opcion = confirm("Desea eliminar el articulo");
	if ( opcion == true) {
	    var i = r.parentNode.parentNode.rowIndex;
        document.getElementById("tablaArticulos").deleteRow(i);
	} 
    
}

function deleteRow1(r) {

	var opcion = confirm("Desea eliminar el libro");
	if ( opcion == true) {
	    var i = r.parentNode.parentNode.rowIndex;
        document.getElementById("tablaLibros").deleteRow(i);
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