//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNB/";

/**
 * Metodo para hacer login de usuario y traer los roles a los que tiene acceso
 * @returns {undefined}
 */
function login(){
    var user_name = document.getElementById("user_name").value;
    var user_psw = document.getElementById("user_pss").value;
   /* var respuesta;
    var user_login_data = {
            "user_name":user_name,
            "user_pass":user_psw
    };
    postServelet(SERVER_URL+"login_servelet",JSON.stringify(user_login_data),function(serveletResponse) {
        var respuesta = JSON.parse(serveletResponse);
        if (respuesta.code == "0") {
            alert("Bien codigo: "+respuesta.code.toString()+" - Descripcion: "+respuesta.description);
        }
        else {
            alert("Fallo codigo: "+respuesta.code.toString()+" - Descripcion: "+respuesta.description);
        }
    });*/
    var respuesta={
	"code": 0,
	"description": "Operacion exitosa",
	"roles": [
		{
			"id": 100,
			"label": "Publicaciones",
			"referencia": "http://localhost:8080/SicaaNB/Vista/principal.html"
		},
		{
			"id": 102,
			"label": "Actualizar informacion",
			"referencia": "http://localhost:8080/SicaaNB/Vista/actualizarinformacion.html"
		},
		{
			"id": 104,
			"label": "Dashboard",
			"referencia": "http://localhost:8080/SicaaNB/Vista/dashboard.html"
		}
	],
	"datosBasicos": {
		"id": 1,
		"nombre": "Alexandra Pomares Quimbaya",
		"categoria": "categoria",
		"nombreDepartamento": "nombreDepartamento",
		"nombreUniversidad": "nombreUniversidad",
		"nacionalidad": "nacionalidad",
		"urlImagen": "urlImagen"
	},
	"formacionAcademica": [
		{
			"titulo": "titulo",
			"universidad": "universida",
			"fechaInicio": "fechaInicio",
			"fechaFin": "fechaFin",
			"descripcion": "descripcion"
		},
		{
			"titulo": "titulo",
			"universidad": "universida",
			"fechaInicio": "fechaInicio",
			"fechaFin": "fechaFin",
			"descripcion": "descripcion"
		}
	],
	"areasActuacion": [
		{
			"nombre": "nombre"
		},
		{
			"nombre": "nombre"
		}
	],
	"medallas": [
		{
			"nombreMedalla": "nombreMedalla",
			"imagen": "rutaImagen"
		},
		{
			"nombreMedalla": "nombreMedalla",
			"imagen": "rutaImagen"
		}
	],
	"podioArticulos": [
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		},
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		},
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		}
	],
	"podioLibros": [
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		},
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		},
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		}
	],
	"podioCapitulos": [
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		},
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		},
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		}
	],
	"podioEventos": [
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		},
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		},
		{
			"nombre": "nombreProfesor",
			"puntos": "puntos"
		}
	],
	"publicacionesRecientes": [
		{
			"titulo": "titulo",
			"fechaInicio": "fechaInicio",
			"fechaFin": "fechaFin"
		},
		{
			"titulo": "titulo",
			"fechaInicio": "fechaInicio",
			"fechaFin": "fechaFin"
		},
		{
			"titulo": "titulo",
			"fechaInicio": "fechaInicio",
			"fechaFin": "fechaFin"
		},
		{
			"titulo": "titulo",
			"fechaInicio": "fechaInicio",
			"fechaFin": "fechaFin"
		},
		{
			"titulo": "titulo",
			"fechaInicio": "fechaInicio",
			"fechaFin": "fechaFin"
		}
	]
}
               if(respuesta.code===0){
                     sessionStorage.setItem("principal", JSON.stringify(respuesta));
                     alert(sessionStorage.getItem("principal"));
                     window.location.href='Vista/principal.html';
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
        else if(xhr.readyState == 4 && xhr.status != 200) {
            return callback({code:"9997",description:"Imposible obtener respuestan de servidor"});
        }
    }
    xhr.open('GET', url+"?"+param, true);
    xhr.send(data);
}

function postServelet(url,data,callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('post', url, false);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            return callback(xhr.responseText);
        }
    }
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