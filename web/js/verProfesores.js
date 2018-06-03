//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-dev/";
var radio_selection = null;
var last_words = [];
var mDepartamentos = {};
var mFacultades = {};

window.onload = function(){
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    if (SESSION != null) {
        document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
        document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
        document.getElementById("facultad").innerHTML = SESSION.datosBasicos.nombreFacultad;
        // Cargar Nav-bar
        cargarNavBar(SESSION.roles, "Buscar Profesores");

        // Cargar Imagen
        cargarImagen(SESSION.datosBasicos);
    
        //Insertar lista de departamentos
        insertList("dp-name", SESSION.departamentos);
        
        // Insertar lista de facultades
        insertList("f-name", SESSION.facultades);
        
        for (var i = 0; i < SESSION.departamentos.length; i++)
            mDepartamentos[SESSION.departamentos[i].Nombre] = SESSION.departamentos[i].ID;
        
        for (var i = 0; i < SESSION.facultades.length; i++)
            mFacultades[SESSION.facultades[i].Nombre] = SESSION.facultades[i].ID;
        
    }  
    else
        window.location.href = SERVER_URL;
}

function insertList(selectID, mList) {
    var mSelect = document.getElementById(selectID);
    if (mSelect != null) {
        mSelect.innerHTML = "";
        for (var i = 0; i < mList.length; i++) {
            mSelect.innerHTML += "<option>" + mList[i].Nombre + "</option>";
        }
    }
}

function buscarProfesor() {
    var param = "action=";
    if (radio_selection != null) {
        document.getElementById("accordion").innerHTML = "";
        document.getElementById("main-loader").style.display = "block";
        param += radio_selection;
        
        if (radio_selection == "INFO_BASICA_NAME")
            param += "&nombre=" + 
                document.getElementById("autor-name").value;
        
        if (radio_selection == "INFO_BASICA_DPTO")
            param += "&idDepartamento=" + 
                mDepartamentos[document.getElementById("dp-name").value];
        
        if (radio_selection == "INFO_BASICA_FACU")
            param += "&idFacultad=" + 
                mFacultades[document.getElementById("f-name").value];
        
        getServlet(SERVER_URL + "ProfesoresServlet", null, param, function(servletResponse) {
            var jsonResponse = JSON.parse(servletResponse);
            document.getElementById("main-loader").style.display = "none";
            if (jsonResponse.code === 0) {
                if (jsonResponse.profesores.length > 0) {
                    for (var i = 0; i < jsonResponse.profesores.length; i++) {
                        document.getElementById("accordion").innerHTML += getPanelCollapse(i, jsonResponse.profesores[i]);
                    }
                }
                else
                    document.getElementById("accordion").innerHTML = "No se encontraron profesores";
            }
            else
                alert("Error código: " + jsonResponse.code + " - Descripción: " + jsonResponse.description);
        });
    }
    else
        alert("Seleccione algún filtro");
}

function getPanelCollapse(number, infoProfesor) {
    var mHTML = "";
    var urlImagen = "../imagenes/default_avatar_img.png";
    if (infoProfesor.urlImagen != null) urlImagen = infoProfesor.urlImagen;
    
    mHTML = "<div class='panel panel-default'>";
    mHTML += "<div class='panel-heading'>";
    mHTML += "<h4 class='panel-title'>";
    mHTML += "<a style='text-decoration:none;' data-toggle='collapse' data-parent='#accordion' href='#collapse" + number + "'>" + infoProfesor.nombre + "</a>";
    mHTML += "</h4></div>";
    mHTML += "<div id='collapse" + number +"' class='panel-collapse collapse'>";
    mHTML += "<div class='panel-body'>"
            + "<img height='150px' width='150px' style='border-radius: 50%; margin-bottom:20px;' src='" + urlImagen + "'>"
            + "<p><span style='color:#000000'>Categoría:</span> " + infoProfesor.categoria + "</p>"
            + "<p><span style='color:#000000'>Número de Publicaciones:</span> " + infoProfesor.numeroPublicaciones + "</p>"
            + "<p><span style='color:#000000'>Javepoints:</span> " + infoProfesor.puntos + "</p>"
            + "<a onclick='showMoreInformation(" + infoProfesor.id + ")' data-toggle='modal' data-target='#myInfo' style='cursor: pointer;text-decoration:none;'> Ver más</a>"
            + "</div>";
    mHTML += "</div></div>";
    return mHTML;
}

function showMoreInformation(idProfesor) {
    
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

function handlerInputs(mNode) {
    radio_selection = "";
    if (mNode.value == "autor") {
        radio_selection += "INFO_BASICA_NAME";
        if (document.getElementById("autor-name").disabled)
            document.getElementById("autor-name").disabled = false;
        if (!document.getElementById("dp-name").disabled)
            document.getElementById("dp-name").disabled = true;
        if (!document.getElementById("f-name").disabled)
            document.getElementById("f-name").disabled = true;
    }
    if (mNode.value == "departamento") {
        radio_selection += "INFO_BASICA_DPTO";
        if (document.getElementById("dp-name").disabled)
            document.getElementById("dp-name").disabled = false;
        if (!document.getElementById("autor-name").disabled)
            document.getElementById("autor-name").disabled = true;
        if (!document.getElementById("f-name").disabled)
            document.getElementById("f-name").disabled = true;
    }
    if (mNode.value == "facultad") {
        radio_selection += "INFO_BASICA_FACU";
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