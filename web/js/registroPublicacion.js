//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNB/";

var contAutor = 1;
var global_autores = {};
window.onload = function(){
    var json = JSON.parse(sessionStorage.getItem("principal"));
    //var nombre = document.getElementById("nombre");
    //nombre.innerHTML =json.datosBasicos.nombre;
    document.getElementById("nombre").innerHTML = "Alexandra Pomares Quimbaya";
    document.getElementById("departamento").innerHTML = "Ingeniería de Sistemas";
}

function guardarInfo(){
    /*var data_publicacion = {
        "token":"1234",
        "publicacion": {
            "Titulo":"A",
            "Tipo":"libro",
            "Editorial":"Colombie IEEE Xplore Library",
            "Extraido":"Manual",
            "autores": [
                {
                    "ID":"2",
                    "Rol":"Profesor"
                },
                {
                    "ID":"3",
                    "Rol":"Profesor"
                }
            ]
        }
    }*/
    var data_publicacion = new Object();
    var infoPublicacion = new Object();
    var listaAutores = [];
    var info = document.getElementsByClassName("infoPublicacion");
    var infoAutores = document.getElementsByClassName("infoAutores");
    
    infoPublicacion.Titulo = (info[0].value == "") ? null : info[0].value;
    infoPublicacion.Tipo = (info[1].value == "") ? null : info[1].value;
    infoPublicacion.CodigoPublicacion = (info[2].value == "") ? null : info[2].value;
    infoPublicacion.Lugar = (info[3].value == "") ? null : info[3].value;
    infoPublicacion.Editorial = (info[4].value == "") ? null : info[4].value;
    infoPublicacion.FechaInicio = (info[5].value == "") ? null : info[5].value;
    infoPublicacion.Extraido = "Manual";
    
    for (var i = 0; i < infoAutores.length; i++) {  
        var objeto_autor = new Object();
        objeto_autor.Rol = "Profesor";
        if (infoAutores[i].value != "Crear un nuevo usuario") {
            objeto_autor.ID = global_autores[infoAutores[i].value];
        }
        else {
            objeto_autor.ID = -1;
            objeto_autor.Nombre = document.getElementsByClassName("autorPublicacion").item(i).value;
        }
        listaAutores.push(objeto_autor);
    }
    
    data_publicacion.token = json.token; 
    //data_publicacion.token = "1234";
    if (infoAutores.length > 0) infoPublicacion.autores = listaAutores;
    data_publicacion.publicacion = infoPublicacion;
    
    postServlet(SERVER_URL + "GuardarPublicacion", JSON.stringify(data_publicacion), function(serveletResponse) {
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
    autor.innerHTML = "<label>Nuevo Coautor</label>" +
        "<input type='text' placeholder='Escriba el nombre de la persona' class='form-control autorPublicacion'>" +
        "<button type='button' class='btn btn-info btn-sm' onclick='buscarAutor(" + contAutor + ")'> <span class='glyphicon glyphicon-search'></span> Buscar Autor</button>"
    + "<select class='form-control infoAutores' id='a" + contAutor + "'></select>";
    myAutores.appendChild(autor);
}

function buscarAutor(idAutor) {
    var autores = document.getElementsByClassName("autorPublicacion");
    var autorName = autores[idAutor-2].value;
    var getSelect = document.getElementById("a" + idAutor);

    var object_to_send = "autor=" + autorName;
    
	getServlet(SERVER_URL + "GetAutoresSimilares", null, object_to_send, function(serveletResponse) {
        var respuesta = JSON.parse(serveletResponse);
            if (respuesta.code == "0") {
            //respuesta = {autores: [{"Nombre":"Germancho","ID":4},{"Nombre":"Lonchitas","ID":9}]}
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