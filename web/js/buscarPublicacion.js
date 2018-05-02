//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-master/";

function buscar(){
       //var text = '{"fechaInicial":"","fechaFinal":"","nombre":"Alexandra Pomares Quimbaya","departamento":"Ingenieria de sistemas","titulo":"Mis putas tristes","tipo":"libro"}';
        var nombre = document.getElementById("nombre").value;
        var fechaInicial = document.getElementById("fechaInicial").value;
        var fechaFinal = document.getElementById("fechaFinal").value;
        var titulo = document.getElementById("titulo").value;
        var tipo = document.getElementById("tipo").value;
        var text = '{"fechaInicial":"'+fechaInicial+ '","fechaFinal":"'+fechaFinal+ '","nombre":"'+nombre+'","departamento":"Ingenieria de sistemas","titulo":"'+ titulo+'","tipo":"'+tipo+'"}';
        var obj = JSON.parse(text);
       var myJSON = JSON.stringify(obj);
	postServelet(SERVER_URL+"ConsultarPublicacion",myJSON,function(serveletResponse) {
        var obj1 = JSON.parse(serveletResponse);
         var publicaciones=obj1.publicaciones;
                
         var table = document.getElementById("tabla");
         for(var i=0; i< publicaciones.length; i++){
                    var numRow=table.rows.length;
                    var row = table.insertRow(numRow);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                    var cell6 = row.insertCell(5);
                    var cell7 = row.insertCell(6);
                    cell1.innerHTML = publicaciones[i].titulo;
                    cell2.innerHTML = publicaciones[i].lugar;
                    cell3.innerHTML = publicaciones[i].ISSN;
                    cell4.innerHTML = publicaciones[i].ISBN;
                    cell5.innerHTML = publicaciones[i].fecha;
                    cell6.innerHTML = publicaciones[i].editorial;
                    cell7.innerHTML = publicaciones[i].tipo;
         }
        //alert(serveletResponse);
     //respuesta=serveletResponse;
        console.log("respondio");
        });
		

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