//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-master/";

var contAutor = 1;
var global_autores = {};
var SESSION;

window.onload = function(){
    SESSION = JSON.parse(sessionStorage.getItem("principal"));
    if (SESSION != null) {
        document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
        document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
    }
}

function guardarInfo(){
    document.getElementById("save-btn").disabled = true;
    $("#main-loader").show();
    var data_publicacion = new Object();
    var infoPublicacion = new Object();
    var listaAutores = [];
    var info = document.getElementsByClassName("infoPublicacion");
    var infoAutores = document.getElementsByClassName("infoAutores");
    var rolAutores = document.getElementsByClassName("rolAutores");
    
    infoPublicacion.Titulo = (info[0].value == "") ? null : info[0].value;
    infoPublicacion.Tipo = (info[1].value == "") ? null : info[1].value;
    infoPublicacion.CodigoPublicacion = (info[2].value == "") ? null : info[2].value;
    infoPublicacion.Lugar = (info[3].value == "") ? null : info[3].value;
    infoPublicacion.Editorial = (info[4].value == "") ? null : info[4].value;
    infoPublicacion.FechaInicio = (info[5].value == "") ? null : info[5].value;
    infoPublicacion.Extraido = "Manual";
    
    for (var i = 0; i < infoAutores.length; i++) {  
        var objeto_autor = new Object();
        if (infoAutores[i].value != "Crear un nuevo usuario") {
            objeto_autor.ID = global_autores[infoAutores[i].value];
        }
        else {
            objeto_autor.ID = -1;
            objeto_autor.Nombre = document.getElementsByClassName("autorPublicacion").item(i).value;
        }
        objeto_autor.Rol = rolAutores[i].value;
        listaAutores.push(objeto_autor);
    }
    
    data_publicacion.token = SESSION.token; 
    if (infoAutores.length > 0) infoPublicacion.autores = listaAutores;
    data_publicacion.publicacion = infoPublicacion;
   
    postServlet(SERVER_URL + "GuardarPublicacion", JSON.stringify(data_publicacion), function(serveletResponse) {
        $("#main-loader").hide();
        document.getElementById("save-btn").disabled = false;
        var respuesta = JSON.parse(serveletResponse);
        if (respuesta.code == 0) {
            alert("Registro Correcto");
            window.location.href='../Vista/principal.html';
        }
        else {
            alert("Error en el registro: " + respuesta.code.toString() + " - Descripcion: " + respuesta.description);
        }
    });
}

function addAutor() {
    contAutor = contAutor + 1;
    var myAutores = document.getElementById("autores");
    var autor = document.createElement("div");
    autor.className = "form-group";
    autor.innerHTML = "<div style='display:none;' id='loader-search-autor" + contAutor + "' class='smallLoader'></div>";
    autor.innerHTML += "<label style='display:block;'>Nuevo Coautor</label>" +
        "<input style='width:40%; display:inline;' type='text' placeholder='Escriba el nombre de la persona' class='form-control autorPublicacion'>" +
        "<button style='margin-left: 10px;margin-right: 10px; width:10%;display:inline;' type='button' class='btn btn-info btn-sm' onclick='buscarAutor(" + contAutor + ")'> <span class='glyphicon glyphicon-search'></span></button>"
    + "<select style='width:40%;display:inline;' class='form-control infoAutores' id='a" + contAutor + "'></select>";
    autor.innerHTML += "<label style='display:block; margin-top:10px;'>Selecciona el Rol</label>";
    autor.innerHTML += "<select style='width:50%;display:inline;' class='form-control rolAutores'>"
        + "<option>" + "Profesor" + "</option>"
        + "<option>" + "Estudiante" + "</option>"
        + "<option>" + "Externo" + "</option>"
        + "</select>";
    autor.innerHTML += "<hr class='divider'/>";
    myAutores.appendChild(autor);
}

function buscarAutor(idAutor) {
    $("#loader-search-autor" + idAutor).show();
    var autores = document.getElementsByClassName("autorPublicacion");
    var autorName = autores[idAutor-2].value;
    var getSelect = document.getElementById("a" + idAutor);

    var object_to_send = "autor=" + autorName;
    
	getServlet(SERVER_URL + "GetAutoresSimilares", null, object_to_send, function(serveletResponse) {
        $("#loader-search-autor" + idAutor).hide();
        var respuesta = JSON.parse(serveletResponse);
            if (respuesta.code == "0") {
                getSelect.innerHTML = "";
                console.log(respuesta);
                for (var i = 0; i < respuesta.autores.length ; i++) {
                    var global_object = new Object();
                    global_autores[respuesta.autores[i].Nombre] = respuesta.autores[i].ID;
                    getSelect.innerHTML += "<option>" + respuesta.autores[i].Nombre + "</option>";
                }
                getSelect.innerHTML += "<option>Crear un nuevo usuario</option>";
            }
            else {
                alert("Fallo codigo: "+ respuesta.code.toString()+ " - Descripcion: " + respuesta.description);
            }
    });
    console.log(global_autores);
}

function removeAutor() {
    var myAutores = document.getElementById("autores");
    if (myAutores.lastChild != null) {
        contAutor = contAutor - 1;
        myAutores.removeChild(myAutores.lastChild);
    }
}

function goHome(){
    window.location.href='../Vista/principal.html';
}

function postServlet(url, data, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('post', url, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            return callback(xhr.responseText);
        }
    }
    xhr.send(data);
}

function getServlet(url, data, param, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            return callback(xhr.responseText);
        }
        else if(xhr.readyState == 4 && xhr.status != 200) {
            return callback({code:"9997",description:"Imposible obtener respuestan de servidor"});
        }
    }
    xhr.open('GET', url + "?" + param, true);
    xhr.send(data);
}