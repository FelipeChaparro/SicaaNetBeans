//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:38161/SicaaNetBeans-siccaV2/";

/**
 * Metodo para hacer login de usuario y traer los roles a los que tiene acceso
 * @returns {undefined}
 */
function login(){
    var user_name = document.getElementById("user_name").value;
    var user_psw = document.getElementById("user_pss").value;
    var respuesta;
    var user_login_data = {
            "user_name":user_name,
            "user_pass":user_psw
    };
    
    /*postServelet(SERVER_URL+"login_servelet",JSON.stringify(user_login_data),function(serveletResponse) {
        var respuesta = JSON.parse(serveletResponse);
        if (respuesta.code === 0) {
            sessionStorage.setItem("principal", JSON.stringify(respuesta));
            alert(sessionStorage.getItem("principal"));
            window.location.href='Vista/principal.html';
            //alert("Bien codigo: "+respuesta.code.toString()+" - Descripcion: "+respuesta.description + " - Token: "+respuesta.token);
        }
        else {
            alert("Fallo codigo: "+respuesta.code.toString()+" - Descripcion: "+respuesta.description);
        }
    })*/
     var respuesta = {
	"publicacionesRecientes": [{
		"fechaInicio": "1969-12-31",
		"titulo": "ASHYI Plataforma basada en agentes para la planificación dinámica, inteligente y adaptativa de actividades aplicada a la educación personalizada",
		"fechaFin": "1969-12-31"
	},
	{
		"fechaInicio": "1969-12-31",
		"titulo": "Mediación Y Selección De Fuentes De Datos En Organizaciones Virtuales A Gran Escala",
		"fechaFin": "1969-12-31"
	},
	{
		"fechaInicio": "2000-01-01",
		"titulo": "El trabajo cooperativo asistido por computador",
		"fechaFin": "2000-01-01"
	},
	{
		"fechaInicio": "2009-01-01",
		"titulo": "Selección de Fuentes de Datos en Organizaciones Virtuales",
		"fechaFin": "2009-01-01"
	},
	{
		"fechaInicio": "2010-01-01",
		"titulo": "Source selection in large scale data contexts: An optimization approach",
		"fechaFin": "2010-01-01"
	}],
	"podioPublicacionUniversidad": [{
		"puntos": "1000",
		"nombre": "\tAlexandra Pomares Quimbaya",
		"facultad": "Ingenieria"
	},
	{
		"puntos": "480",
		"nombre": "Juan Chaparro",
		"facultad": "Historia y geografia"
	},
	{
		"puntos": "250",
		"nombre": "Henry Arias",
		"facultad": "Derecho"
	}],
	"formacionAcademica": [{
		"descripcion": "Mediación y Selección de Fuentes de Datos en Organizaciones Virtuales de Gran Escala",
		"fechaInicio": "2007-01-01",
		"titulo": "Doctorado en Ingeniería",
		"universidad": "UNIVERSIDAD DE LOS ANDES UNIANDES",
		"fechaFin": "2010-08-01"
	},
	{
		"descripcion": "ENFOQUE BASADO EN ONTOLOGÍAS PARA EL DISEÑO DE BODEGAS DE DATOS",
		"fechaInicio": "2003-01-01",
		"titulo": "Maestría En Ingeniería de Sistemas y Computación",
		"universidad": "UNIVERSIDAD DE LOS ANDES UNIANDES",
		"fechaFin": "2007-01-01"
	},
	{
		"descripcion": "XUE- Comunidad Virtual",
		"fechaInicio": "1995-01-01",
		"titulo": "Ingeniería de Sistemas",
		"universidad": "PONTIFICIA UNIVERSIDAD JAVERIANA PUJ SEDE BOGOTA",
		"fechaFin": "2001-01-01"
	}],
	"code": 0,
	"datosBasicos": {
		"nombreDepartamento": "Ingenieria de Sistemas",
		"nombreFacultad": "Ingenieria",
		"categoria": "Investigador Senior (IS) (con vigencia hasta 2019-12-05 00:00:00.0) - Convocatoria 781 de 2017",
		"id": "1",
		"nombre": "\tAlexandra Pomares Quimbaya",
		"nacionalidad": "Colombiana"
	},
	"medallas": [{
		"nombreMedalla": "Bilingue",
		"acomplished": "1"
	},
	{
		"nombreMedalla": "Cientifico",
		"acomplished": "1"
	},
	{
		"nombreMedalla": "Director",
		"acomplished": "1"
	},
	{
		"nombreMedalla": "Doctor",
		"acomplished": "0"
	},
	{
		"nombreMedalla": "Investigador",
		"acomplished": "1"
	},
	{
		"nombreMedalla": "Administrativo",
		"acomplished": "1"
	},
	{
		"nombreMedalla": "Jefe",
		"acomplished": "0"
	}],
	"podioPublicacionPrograma": [{
		"programa": "Ingenieria de Sistemas",
		"puntos": "1000",
		"nombre": "\tAlexandra Pomares Quimbaya"
	}],
	"podioPublicacionFacultad": [{
		"puntos": "1000",
		"nombre": "\tAlexandra Pomares Quimbaya",
		"facultad": "Ingenieria"
	}],
	"roles": [{
		"id": "1",
		"label": "Publicaciones",
		"referencia": "http:\/\/localhost:8080\/SicaaNB\/Vista\/principal.html"
	},
	{
		"id": "2",
		"label": "Actualizar informacion",
		"referencia": "http:\/\/localhost:8080\/SicaaNB\/Vista\/actualizarinformacion.html"
	}],
	"description": "Operacion exitosa",
	"areasActuacion": [{
		"nombre": "Ciencias Naturales"
	},
	{
		"nombre": "Computación y Ciencias de la Información"
	},
	{
		"nombre": "Ciencias de la Computación"
	}],
	"token": "1121141171019897115504849"
}
        if(respuesta.code === 0){
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