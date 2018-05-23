//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNBGIT/";

var SESSION;
var data_Cvlac;
var data_ResearchGate;
var data_GoogleScholar;

window.onload = function(){
    SESSION = JSON.parse(sessionStorage.getItem("principal"));
    
    if (SESSION == null) {
        window.location.href = SERVER_URL;
    }
    else {
        document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
        document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
        document.getElementById("facultad").innerHTML = SESSION.datosBasicos.nombreFacultad;
        // Cargar Nav-bar
        cargarNavBar(SESSION.roles, "Carga Fuentes Externas");
        // Cargar Imagen
        cargarImagen(SESSION.datosBasicos);
    }
    
}
function cerrarSesion() {
    sessionStorage.clear();
    window.location.href = SERVER_URL;
}

function getCvlac(thisBtn) {
    document.getElementById("cvlac-info").innerHTML = "";
    var params = "urlcvlac=";
    var url = document.getElementById("url-cvlac").value;
    if (url != "") {
        document.getElementById("loader-cvlac").style.display = "block";
        thisBtn.disabled = true;
        thisBtn.innerHTML = "Extrayendo...";
        params += url;
        data_Cvlac = null;
        
        getServelet(SERVER_URL + "extraccionSistemasExternosServlet", null, params, function(serveletResponse) {
            document.getElementById("loader-cvlac").style.display = "none";
            thisBtn.disabled = false;
            thisBtn.innerHTML = "Extraer";
            var response = JSON.parse(serveletResponse);
            data_Cvlac = response;

            titleIntoDiv("Categoría", "cvlac-info");
            pIntoDiv("<span style='color:#000000'>- </span>" + response.datosBasicos.categoria, "cvlac-info");

            titleIntoDiv("Formación Académica", "cvlac-info");
            for (var i = 0; i < response.formacionAcademica.length; i++) {
                pIntoDiv("<span style='color:#000000'>- </span>" + response.formacionAcademica[i].titulo, "cvlac-info");
            }
            titleIntoDiv("Idiomas", "cvlac-info");
            for (var i = 0; i < response.idiomas.length; i++) {
                pIntoDiv("<span style='color:#000000'>- </span>" + response.idiomas[i].idioma, "cvlac-info");
            }
            titleIntoDiv("Áreas de Actuación", "cvlac-info");
            for (var i = 0; i < response.areasActuacion.length; i++) {
                pIntoDiv("<span style='color:#000000'>- </span>" + response.areasActuacion[i].area, "cvlac-info");
            }
            titleIntoDiv("Capítulos (encontrado)", "cvlac-info");
            for (var i = 0; i < response.capitulos.capitulos.length; i++) {
                pIntoDiv("<span style='color:#000000'>Título: </span>" + response.capitulos.capitulos[i].titulo, "cvlac-info");
            }
            titleIntoDiv("Artículos (encontrado)", "cvlac-info");
            for (var i = 0; i < response.publicaciones.articulos.length; i++) {
                pIntoDiv("<span style='color:#000000'>Título: </span>" + response.publicaciones.articulos[i].titulo, "cvlac-info");
            }
            titleIntoDiv("Libros (encontrado)", "cvlac-info");
            for (var i = 0; i < response.Libros.libros.length; i++) {
                pIntoDiv("<span style='color:#000000'>Título: </span>" + response.Libros.libros[i].titulo, "cvlac-info");
            }
            titleIntoDiv("Conferencias (encontrado)", "cvlac-info");
            for (var i = 0; i < response.eventos.eventos.length; i++) {
                pIntoDiv("<span style='color:#000000'>Título: </span>" + response.eventos.eventos[i].titulo, "cvlac-info");
            }
            titleIntoDiv("Trabajos Dirigidos (encontrado)", "cvlac-info");
            for (var i = 0; i < response.trabjosDirigidos.trabjosDirigidos.length; i++) {
                pIntoDiv("<span style='color:#000000'>Título: </span>" + response.trabjosDirigidos.trabjosDirigidos[i].titulo, "cvlac-info");
            }
            titleIntoDiv("Software (encontrado)", "cvlac-info");
            for (var i = 0; i < response.software.software  .length; i++) {
                pIntoDiv("<span style='color:#000000'>Título: </span>" + response.software.software[i].titulo, "cvlac-info");
            }

            btnIntoDiv("Guardar Extracción", "cvlac-info", "saveCvlac(this)");
            addHTMLIntoDiv("<center>"
                           + "<div "
                           + "id='smallLoader-cvlac' "
                           + "class='smallLoader' "
                           + "style='display:none;'>"
                           + "</div></center>", "cvlac-info");
        });
    }
    else
        alert("Escriba una URL");
}

function saveCvlac(thisBtn) {
    $("#smallLoader-cvlac").show();
    if (data_Cvlac != null) {
        var data_to_send = new Object();
        thisBtn.innerHTML = "Guardando...";
        thisBtn.disabled = true;
        data_to_send = data_Cvlac;
        data_to_send.id = SESSION.datosBasicos.id;
       try { 
           postServlet(SERVER_URL + "GuardarInformacionServlet", JSON.stringify(data_to_send), function(serveletResponse) {
               thisBtn.innerHTML = "Guardar Extracción";
               $("#smallLoader-cvlac").hide();
               thisBtn.disabled = false;
               alert("Success!");
           });
       }
        catch(err) {
            alert(err.message);
        }
    }
}

function getResearchGate(thisBtn) {
    document.getElementById("researchgate-info").innerHTML = "";
    var params = "urlresearch=";
    var tipo = "";
    
    var url = document.getElementById("url-researchgate").value;
    if (url != "") {
        document.getElementById("loader-researchgate").style.display = "block";
        thisBtn.disabled = true;
        thisBtn.innerHTML = "Extrayendo...";
        params += url;
        data_ResearchGate = null;
        
        getServelet(SERVER_URL + "ExtraccionResearchServlet", null, params, function(serveletResponse) {
            document.getElementById("loader-researchgate").style.display = "none";
            thisBtn.disabled = false;
            thisBtn.innerHTML = "Extraer";
            var response = JSON.parse(serveletResponse);
            data_ResearchGate = response;
            
            for (var i = 0; i < response.publicaciones.length; i++) {
                tipo = response.publicaciones[i].tipo;
                if (document.getElementById("researchgate-info-" + tipo) == null) {
                    divIntoDiv("researchgate-info-" + tipo, "researchgate-info");
                    titleIntoDiv(tipo + "s (Encontrados)", "researchgate-info-" + tipo);
                }
                pIntoDiv("<span style='color:#000000'>Titulo: </span>" + response.publicaciones[i].titulo, "researchgate-info-" + tipo);
            }
            
            btnIntoDiv("Guardar Extracción", "researchgate-info", "saveResearchgate(this)");
            addHTMLIntoDiv("<center>"
                           + "<div "
                           + "id='smallLoader-researchgate' "
                           + "class='smallLoader' "
                           + "style='display:none;'>"
                           + "</div></center>", "researchgate-info");
        });
    }
    else
        alert("Escriba una URL");
}

function saveResearchgate(thisBtn) {
    $("#smallLoader-researchgate").show();
    if (data_ResearchGate != null) {
        var data_to_send = new Object();
        thisBtn.innerHTML = "Guardando...";
        thisBtn.disabled = true;
        data_to_send = data_ResearchGate;
       try { 
           data_to_send.id = SESSION.datosBasicos.id;
           postServlet(SERVER_URL + "GuardarInformacionReserch", JSON.stringify(data_to_send), function(serveletResponse) {
               thisBtn.innerHTML = "Guardar Extracción";
               $("#smallLoader-researchgate").hide();
               thisBtn.disabled = false;
               alert("Success!");
           });
       }
        catch(err) {
            thisBtn.innerHTML = "Guardar Extracción";
            $("#smallLoader-researchgate").hide();
            thisBtn.disabled = false;
            alert(err.message);
        }
    }
}

function getGoogleScholar(thisBtn) {
    document.getElementById("google-info").innerHTML = "";
    var params = "urlschoolar=";
    var tipo = "";
    
    var url = document.getElementById("url-google").value;
    if (url != "") {
        document.getElementById("loader-google").style.display = "block";
        thisBtn.disabled = true;
        thisBtn.innerHTML = "Extrayendo...";
        params += url;
        data_GoogleScholar = null;
        
        getServelet(SERVER_URL + "ExtraccionGoogleSchoolar", null, params, function(serveletResponse) {
            document.getElementById("loader-google").style.display = "none";
            thisBtn.disabled = false;
            thisBtn.innerHTML = "Extraer";
            var response = JSON.parse(serveletResponse);
            data_GoogleScholar = response;
            
            titleIntoDiv("Publicaciones (Encontradas)", "google-info");
            for (var i = 0; i < response.publicaciones.length; i++) {
                pIntoDiv("<span style='color:#000000'>Titulo: </span>" + response.publicaciones[i].titulo, "google-info");
            }
            
            btnIntoDiv("Guardar Extracción", "google-info", "saveGoogleScholar(this)");
            addHTMLIntoDiv("<center>"
                           + "<div "
                           + "id='smallLoader-google' "
                           + "class='smallLoader' "
                           + "style='display:none;'>"
                           + "</div></center>", "google-info");
        });
    }
    else
        alert("Escriba una URL"); 
}

function saveGoogleScholar(thisBtn) {
    $("#smallLoader-google").show();
    if (data_GoogleScholar != null) {
        var data_to_send = new Object();
        thisBtn.innerHTML = "Guardando...";
        thisBtn.disabled = true;
        data_to_send = data_GoogleScholar;
        data_to_send.id = SESSION.datosBasicos.id;
        try { 
            postServlet(SERVER_URL + "GuardarInformacionGoogle", JSON.stringify(data_to_send), function(serveletResponse) {
               thisBtn.innerHTML = "Guardar Extracción";
               $("#smallLoader-google").hide();
               thisBtn.disabled = false;
               alert("Success!");
            });
        }
        catch(err) {
            alert(err.message);
        }
    }
}

function divIntoDiv(newID, targetID) {
    var mDiv = document.getElementById(targetID); 
    if (mDiv != null) {
        var newDiv = document.createElement("div");
        newDiv.setAttribute("id", newID);
        mDiv.appendChild(newDiv);
    }
}

function titleIntoDiv(titleName, divID) {
    var mDiv = document.getElementById(divID); 
    if (mDiv != null) {
        var newTitle = document.createElement("h1");
        newTitle.className = "heading";
        newTitle.style.fontSize = "18px";
        newTitle.style.textTransform = "capitalize";
        newTitle.innerHTML = titleName;
        mDiv.appendChild(newTitle);
    }
}

function pIntoDiv(paragraph, divID) {
    var mDiv = document.getElementById(divID); 
    if (mDiv != null) {
        var newP = document.createElement("p");
        newP.innerHTML = paragraph;
        newP.style.fontSize = "14px";
        newP.style.marginLeft = "20px";
        newP.setAttribute("align", "justify");
        mDiv.appendChild(newP);
    }
}

function addHTMLIntoDiv(htmlLine, divID){
    var mDiv = document.getElementById(divID); 
    if (mDiv != null) {
        mDiv.innerHTML += htmlLine;
    }
} 

function btnIntoDiv(btnTxt, divID, oFunction) {
    var mDiv = document.getElementById(divID); 
    if (mDiv != null) {
        var newBtn = document.createElement("button");
        newBtn.type = "button";
        newBtn.className = "btn btn-success";
        newBtn.innerHTML = btnTxt;
        newBtn.setAttribute("onclick", oFunction);
        mDiv.appendChild(newBtn);
    }
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
            if (obj_roles[i].label == pageName)
                insertIntoNavbar("dropmenu-consultas", obj_roles[i].label, obj_roles[i].referencia, "active");
            else
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


function getServelet(url, data, param, callback) {
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