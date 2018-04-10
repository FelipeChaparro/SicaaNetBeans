function start(){
	var text = document.getElementById("text");
	postServelet("http://localhost:8080/SicaaNB//GuardarInformacionServlet",json,function(serveletResponse) {
	    //respuesta=serveletResponse;
		console.log("respondio")
	}); 
	getServelet('http://localhost:8080/SicaaNB/EjemploServlet', null, function(serveletResponse) {
	    text.innerHTML = serveletResponse;
	});
}

function login(){
		var user_name = document.getElementById("user_name").value;
		var user_psw = document.getElementById("user_pss").value;
		alert("Nombre: "+user_name+" - Pass: "+user_psw);
		var user_login_data = {
			"user_name":user_name,
			"user_pass":user_psw
		};
	    postServelet("http://localhost:8080/Sicaa/login_servelet",user_login_data,function(serveletResponse) {
	    	var respuesta=serveletResponse;
	        console.log("respondio: ",respuesta);
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