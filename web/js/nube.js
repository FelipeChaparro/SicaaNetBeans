//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNBGIT/";

var radio_selection = null;
var last_words = [];

window.onload = function(){
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    if (SESSION != null) {
        document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
        document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
        document.getElementById("facultad").innerHTML = SESSION.datosBasicos.nombreFacultad;
        // Cargar Nav-bar
        cargarNavBar(SESSION.roles, "Nube de palabras");

        // Cargar Imagen
        cargarImagen(SESSION.datosBasicos);
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

function insertTargetDiv(id) {
    document.getElementById(id).innerHTML = 
        "<div style='width: 100%; height: 70%;' id='my-cloud'></div>"
        + "<hr class='divider'/>"
        + "<a onclick='exportarPalabras(this)'><button style='margin-top: 10px;' type='button' class='btn btn-info'>Exportar</button></a>";
}

function refrescarNube() {
    
    if (radio_selection != null) {
        var params;
        insertTargetDiv("parent-cloud");
        
        if (radio_selection == "autor-name") {
            params = "Nombre=" + document.getElementById(radio_selection).value
                    + "&Facultad=" + ""
                    + "&Departamento=" + ""
                    + "&UsarDpto=";
        }

        if (radio_selection == "f-name") {
            params = "Nombre=" + ""
                    + "&Facultad=" + document.getElementById(radio_selection).value
                    + "&Departamento=" + ""
                    + "&UsarDpto=";
        }
   
        if (radio_selection == "dp-name") {
            params = "Nombre=" + ""
                    + "&Facultad=" + ""
                    + "&Departamento=" + document.getElementById(radio_selection).value
                    + "&UsarDpto=true";
        }
        
        console.log(params);
        
        $('#my-cloud').jQCloud('destroy');
        
        getServlet(SERVER_URL + "NubePalabrasServlet", null, params, function(servletResponse){
            var response = JSON.parse(servletResponse);
            last_words = response.nube;
            
            if (response.code == 0) {
                
                $('#my-cloud').jQCloud(response.nube, 
                            { 
                                autoResize: true, 
                                delay: 100,
                                shape: 'elliptic'
                            });
            }
        });
    }
}

function handlerInputs(mNode) {
    if (mNode.value == "autor") {
        radio_selection = "autor-name";
        if (document.getElementById("autor-name").disabled)
            document.getElementById("autor-name").disabled = false;
        if (!document.getElementById("dp-name").disabled)
            document.getElementById("dp-name").disabled = true;
        if (!document.getElementById("f-name").disabled)
            document.getElementById("f-name").disabled = true;
    }
    if (mNode.value == "departamento") {
        radio_selection = "dp-name";
        if (document.getElementById("dp-name").disabled)
            document.getElementById("dp-name").disabled = false;
        if (!document.getElementById("autor-name").disabled)
            document.getElementById("autor-name").disabled = true;
        if (!document.getElementById("f-name").disabled)
            document.getElementById("f-name").disabled = true;
    }
    if (mNode.value == "facultad") {
        radio_selection = "f-name";
        if (document.getElementById("f-name").disabled)
            document.getElementById("f-name").disabled = false;
        if (!document.getElementById("dp-name").disabled)
            document.getElementById("dp-name").disabled = true;
        if (!document.getElementById("autor-name").disabled)
            document.getElementById("autor-name").disabled = true;
    }
    
}

function exportarPalabras(anchor) {
    CSVCreator(anchor, last_words);
}

function CSVCreator(anchor, elements) {
    var eURI = "";
    var filename = "SICCA_cloud.csv";
    var content = "data:text/csv;charset=utf-8," + CSVHeader();
    
    for (var i = 0; i < elements.length; i++) {
        content += elements[i].text + "," 
                + elements[i].weight;
        content += "\r\n";
    }

    eURI = encodeURI(content);
    anchor.setAttribute("href", eURI);
    anchor.setAttribute("download", filename);
}
    
function CSVHeader() {
    var mHeader = "\uFEFF";
    mHeader += "Palabra,Frecuencia";
    return mHeader + "\r\n";
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
                mNavbar.appendChild(createDrowndown("Producción Intelectual", "dropmenu-publicaciones", ""));
                mDropDownPublicaciones = true;
            }
            if (obj_roles[i].label == pageName)
                insertIntoNavbar("dropmenu-publicaciones", obj_roles[i].label, obj_roles[i].referencia, "active");
            else
                insertIntoNavbar("dropmenu-publicaciones", obj_roles[i].label, obj_roles[i].referencia, "");
        }
        if (obj_roles[i].id >= 6) {
            if (mDropDownConsultas == false) {
                mNavbar.appendChild(createDrowndown("Consultas", "dropmenu-consultas", "active"));
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