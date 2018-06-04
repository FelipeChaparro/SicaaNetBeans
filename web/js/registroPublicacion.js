var SERVER_URL = "https://sicaadev.mybluemix.net/";
//var SERVER_URL = "http://localhost:8080/SicaaNetBeans-dev/";

var contAutor = 1;
var global_autores = {};
var SESSION;

window.onload = function(){
    SESSION = JSON.parse(sessionStorage.getItem("principal"));
    if (SESSION != null) {
        document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
        document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
        document.getElementById("facultad").innerHTML = SESSION.datosBasicos.nombreFacultad;
        
        // Cargar Nav-bar
        cargarNavBar(SESSION.roles, "Registro Manual");
        
        // Cargar Imagen
        cargarImagen(SESSION.datosBasicos);
        
        document.getElementsByClassName("infoPublicacion")[1].value = null;
    }
    else 
        window.location.href = SERVER_URL;
}

function cerrarSesion() {
    sessionStorage.clear();
    window.location.href = SERVER_URL;
}

function cargarImagen(oDatosBasicos) {
    var iSource = "../imagenes/default_avatar_img.png";
    mImage = document.getElementById("my-profile-img");
    mImage.setAttribute("title", oDatosBasicos.nombre);
    mImage.setAttribute("alt", oDatosBasicos.nombre);
    
    if (oDatosBasicos.urlImagen != null) {
        iSource = oDatosBasicos.urlImagen;
    }

    mImage.setAttribute("src", iSource);
}

function showInputs(mSelect) {
    var eTipo = mSelect.value;
    var target_div = mSelect.parentNode.parentNode.children[2];
    
    if (target_div != null) {
        target_div.innerHTML = "";
        
        if (eTipo == "libro" || eTipo == "capitulo")
            target_div.appendChild(
                getNewDiv(
                    "form-group", 
                    getHTMLInputBox("ISBN de la publicación", "form-control infoPublicacion", "")
                )
            );
            
        if (eTipo == "articulo") 
            target_div.appendChild(
                getNewDiv(
                    "form-group", 
                    getHTMLInputBox("ISSN de la publicación", "form-control infoPublicacion", "")
                )
            );
        
        if (eTipo == "software") 
            target_div.appendChild(
                getNewDiv(
                    "form-group", 
                    getHTMLInputBox("Registro de la publicación", "form-control infoPublicacion", "")
                )
            );

        target_div.appendChild(
            getNewDiv(
                "form-group", 
                getHTMLInputBox("Lugar de la publicación", "form-control infoPublicacion", "")
            )
        );
      
        if (eTipo == "libro" || eTipo == "capitulo" || eTipo == "articulo") 
            target_div.appendChild(
                getNewDiv(
                    "form-group", 
                    getHTMLInputBox("Editorial de la publicación", "form-control infoPublicacion", "")
                )
            );
        
        target_div.appendChild(
            getNewDiv(
                "form-group", 
                getHTMLInputBox("Fecha de la publicación", "form-control infoPublicacion", "YYYY-MM-DD")
            )
        );
        
        if (eTipo == "software") 
            target_div.appendChild(
                getNewDiv(
                    "form-group", 
                    getHTMLInputBox("Plataforma", "form-control infoPublicacion", "")
                )
            );
        
        if (eTipo == "trabajo dirigido") {
            target_div.appendChild(
                getNewDiv(
                    "form-group", 
                    getHTMLInputBox("Duración", "form-control infoPublicacion", "")
                )
            );
            target_div.appendChild(
                getNewDiv(
                    "form-group", 
                    getHTMLInputBox("Tipo Específico", "form-control infoPublicacion", "")
                )
            );
        }
    }
    else
        console.log("Error en 'target_div'");
}

function getNewDiv(cName, inHTML) {
    var newDiv = document.createElement("div");
    newDiv.className = cName;
    newDiv.innerHTML = inHTML;
    return newDiv;
}

function getHTMLInputBox(label, className, placeholder) {
    var mHTML = "<label>" + label + "</label>";
    mHTML += "<input "
            + "type='text' "
            + "placeholder='" + placeholder + "' "
            + "class='" + className + "'>";
    return mHTML;
}

function dateValidation(newDate) {
    let validator = /^\d\d\d\d-\d\d-\d\d$/;
    if (validator.test(newDate)) {
        let mArray = newDate.split("-");
        let isValid = true;
        if (mArray[0] < 1900) isValid = false;
        if (mArray[1] < 1 || mArray[1] > 12) isValid = false; 
        if (mArray[2] < 1 || mArray[2] > 31) isValid = false; 
        return isValid;
    }
    return false
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
    
    if (infoPublicacion.Titulo != null && infoPublicacion.Tipo != null) {
        
        if (infoPublicacion.Tipo == "libro" || 
            infoPublicacion.Tipo == "articulo" || 
            infoPublicacion.Tipo == "capitulo" || 
            infoPublicacion.Tipo == "software") {
            infoPublicacion.codigoPublicacion = (info[2].value == "") ? null : info[2].value;
            infoPublicacion.Lugar = (info[3].value == "") ? null : info[3].value;
        }
        else {
            infoPublicacion.codigoPublicacion = null;
            infoPublicacion.Lugar = (info[2].value == "") ? null : info[2].value;
        }
        
        if (infoPublicacion.Tipo == "libro" || 
            infoPublicacion.Tipo == "articulo" || 
            infoPublicacion.Tipo == "capitulo") {
            infoPublicacion.Editorial = (info[4].value == "") ? null : info[4].value;
            infoPublicacion.FechaInicio = (info[5].value == "") ? null : info[5].value;
        }
        else {
            infoPublicacion.Editorial = null;
        }
        
        if (infoPublicacion.Tipo == "trabajo dirigido") {
            infoPublicacion.FechaInicio = (info[3].value == "") ? null : info[3].value;
            infoPublicacion.Duracion = (info[4].value == "") ? null : info[4].value;
            infoPublicacion.tipoEspecifico = (info[5].value == "") ? null : info[5].value;
        }
        
        if (infoPublicacion.Tipo == "software") {
            infoPublicacion.FechaInicio = (info[4].value == "") ? null : info[4].value;
            infoPublicacion.Plataforma = (info[5].value == "") ? null : info[5].value;
        }
        let isValid = true;
        if (infoPublicacion.FechaInicio != null) isValid = dateValidation(infoPublicacion.FechaInicio);
        
        if (isValid) {
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
                    alert("Publicación registrada correctamente");
                    location.reload(true);
                }
                else {
                    alert("Error en el registro: " + respuesta.code.toString() + " - Descripcion: " + respuesta.description);
                }
            });
        }
        else {
            alert("Error en fecha");
            $("#main-loader").hide();
            document.getElementById("save-btn").disabled = false;
        }
    }
    else {
        $("#main-loader").hide();
        document.getElementById("save-btn").disabled = false;
        alert("No puedes dejar el titulo ni el tipo de la publicación vacío!");
    }
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