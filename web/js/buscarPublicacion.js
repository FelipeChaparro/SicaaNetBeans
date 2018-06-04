var SERVER_URL = "https://sicaadev.mybluemix.net/";
//var SERVER_URL = "http://localhost:8080/SicaaNetBeans-dev/";
var last_publicaciones;
window.onload = function(){
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    if (SESSION == null) {
        window.location.href = SERVER_URL;
    }
    else {
        document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
        document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
        document.getElementById("facultad").innerHTML = SESSION.datosBasicos.nombreFacultad;
        // Cargar Nav-bar
        cargarNavBar(SESSION.roles, "Filtrar Publicaciones");

        // Cargar Imagen
        cargarImagen(SESSION.datosBasicos);
    }
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

function buscar() {
    document.getElementById("respuesta-filtro").innerHTML = "";
    var data_to_send = new Object();
    var Rol = [];
    var Tipo = [];
    var fechaValida = true;
    var autorName = document.getElementById("autor-name").value
    var dpName = document.getElementById("dp-name").value
    var roles = document.getElementsByClassName("cb-rol");
    var tipos = document.getElementsByClassName("cb-tipo");
    var fInicio = document.getElementById("fecha-inicio").value;
    var fFin = document.getElementById("fecha-fin").value;
    
    for (var i = 0; i < roles.length; i++) {
        if (roles[i].checked)
            Rol.push(roles[i].getAttribute("name"));
    }
    for (var i = 0; i < tipos.length; i++) {
        if (tipos[i].checked) 
            Tipo.push(tipos[i].getAttribute("name"));
    }
    
    if (dpName == "Sin especificar") dpName = null;
    if (autorName == "") autorName = null;
    if (fInicio == "") fInicio = null;
    if (fFin == "") fFin = null;
    
    if (fInicio == null && fFin != null) fechaValida = false;
    if (fInicio != null && fFin == null) fechaValida = false;
    
    if (fechaValida) {
        document.getElementById("sm-loader-filtro").style.display = "block";
        data_to_send.Nombre = autorName;
        data_to_send.Rol = Rol;
        data_to_send.Tipo = Tipo;
        data_to_send.Departamento = dpName;
        data_to_send.FechaInicio = fInicio;
        data_to_send.FechaFin = fFin;
        last_publicaciones = [];
        postServlet(SERVER_URL + "ConsultarPublicacion", JSON.stringify(data_to_send), function(serveletResponse) {
            var response = JSON.parse(serveletResponse);
            document.getElementById("sm-loader-filtro").style.display = "none";
            if (response.code == 0) {
                if (response.publicaciones.length > 0) {
                    last_publicaciones = response.publicaciones;
                    var tipo = "";
                    for (var i = 0; i < response.publicaciones.length; i++) {
                        tipo = response.publicaciones[i].tipo;
                        if (document.getElementById("respuesta-filtro-" + tipo) == null) {
                            divIntoDiv("respuesta-filtro-" + tipo, "respuesta-filtro");
                            titleIntoDiv(tipo, "respuesta-filtro-" + tipo);
                        }
                        publicacionIntoDiv(response.publicaciones[i], "respuesta-filtro-" + tipo);
                    }
                    
                    insertExportButton("respuesta-filtro");
                }
                else
                    document.getElementById("respuesta-filtro").innerHTML = "No se encontraron publicaciones";
            }
            else
                alert("Error código: " + response.code + " - Descripción: " + response.description);
        });
      }
        else {
            alert("Debes ingresar fecha desde y fecha hasta!");
      }
}

function publicacionIntoDiv(mPublicacion, divID) {
    var tipo = "", auxText = "";
    mDiv = document.getElementById(divID);
    if (mDiv != null) {
        tipo = mPublicacion.tipo;
        var mAnchor = document.createElement("a");
        mAnchor.className = "list-group-item";
        mAnchor.innerHTML = "<h4 class='list-group-item-heading'>"
                            + mPublicacion.titulo
                            + "</h4>";
        
        mAnchor.innerHTML += paragraphHTML("Tipo:", tipo);
        
        if (tipo == "libro" || tipo == "capitulo" || tipo == "software" || tipo == "articulo") {
            if (tipo == "capitulo" || tipo == "libro") auxText = "ISBN:";
            if (tipo == "software") auxText = "Registro:";
            if (tipo == "articulo") auxText = "ISSN:";
            mAnchor.innerHTML += paragraphHTML(auxText, mPublicacion.codigoPublicacion);
        }
        
        mAnchor.innerHTML += paragraphHTML("Lugar:", mPublicacion.lugar);
        
        if (tipo == "libro" || tipo == "capitulo" || tipo == "articulo") {
            mAnchor.innerHTML += paragraphHTML("Editorial:", mPublicacion.editorial);
        }
        
        mAnchor.innerHTML += paragraphHTML("Fecha:", mPublicacion.fecha);
        
        if (tipo == "trabajo dirigido") {
            mAnchor.innerHTML += paragraphHTML("Duración:", mPublicacion.tipoEspecifico);
            mAnchor.innerHTML += paragraphHTML("Tipo Especifico:", mPublicacion.tipoEspecifico);
        }
        
        if (tipo == "software") {
            mAnchor.innerHTML += paragraphHTML("Plataforma:", mPublicacion.plataforma);
        }
        
        mAnchor.innerHTML += paragraphHTML("Autores:", getAutores(mPublicacion.Autores));
        
        mDiv.appendChild(mAnchor);
    }
}

function getAutores(autores) {
    var mAutores = "";
    
    for (var i = 0; i < autores.length; i++) {
        mAutores += ((i == 0) ? "" : "-") + autores[i].Nombre;
    }
    return mAutores;
}

function insertExportButton(id) {
    var mDiv = document.getElementById(id);
    if (mDiv != null) {
        mDiv.innerHTML += "<a onclick='getCSV(this)'><button style='margin-top: 20px;' type='button' class='btn btn-info'>Exportar</button></a>";
    }
}

function getCSV(thisAnchor) {
    csvCreator(thisAnchor, last_publicaciones);
}

function csvCreator(mAnchor, misPublicaciones) {
    var eURI = "";
    var pTipo = "";
    var text = "";
    var content = "data:text/csv;charset=utf-8," + CSVHeader();
    
    for (var i = 0; i < misPublicaciones.length; i++) {
        pTipo = misPublicaciones[i].tipo;
        content += misPublicaciones[i].titulo.replace(/,/g, "") + ",";
        
        if (pTipo != null) 
            content += pTipo + ",";
        else
            content += "No definido,";
        
        if (pTipo == "articulo" || pTipo == "libro" || pTipo == "capitulo" || pTipo == "software") {
            text = misPublicaciones[i].codigoPublicacion;
            if (text != null)
                content += text.replace(/,/g, "") + ",";
            else
                content += "No definido,";
        }
        else {
            content += "N/A,";
        }

        text = misPublicaciones[i].lugar;
        if (text != null)
            content += text.replace(/,/g, "") + ",";
        else
            content += "No definido,";

        if (pTipo == "articulo" || pTipo == "libro" || pTipo == "capitulo") {
            text = misPublicaciones[i].editorial;
            if (text != null)
                content += text.replace(/,/g, "") + ",";
            else
                content += "No definido,";
        }
        else {
            content += "N/A,";
        }

        text = misPublicaciones[i].fechaInicio;
        if (text != null)
            content += text.replace(/,/g, "") + ",";
        else
            content += "No definido,";

        if (pTipo == "trabajo dirigido") {
            text = misPublicaciones[i].duracion;
            if (text != null)
                content += text.replace(/,/g, "") + ",";
            else
                content += "No definido,";

            text = misPublicaciones[i].tipoEspecifico;
            if (text != null)
                content += text.replace(/,/g, "") + ",";
            else
                content += "No definido,";
        }
        else {
            content += "N/A,N/A,";
        }

        if (pTipo == "software") {
            text = misPublicaciones[i].plataforma;
            if (text != null)
                content += text.replace(/,/g, "") + ",";
            else
                content += "No definido,";
        }
        else {
            content += "N/A,";
        }
        
        text = getAutores(misPublicaciones[i].Autores);
        content += text.replace(/,/g, "") + ",";

        content += "\r\n"; 
    }
    console.log(content);
    eURI = encodeURI(content);
    mAnchor.setAttribute("href", eURI);
    mAnchor.setAttribute("download","SICAA_Filtros.csv");
}

function CSVHeader() {
    var mHeader = "\uFEFF";
    mHeader += "Titulo,Tipo,Codigo Publicación,Lugar,Editorial,Fecha,Duración,Tipo Especifico,Plataforma,Autores";
    return mHeader + "\r\n";
}

function paragraphHTML(rowName, value) {
    var mHTML = "";
    mHTML = "<p class='list-group-item-text'>"
            + "<span style='color:#000000'>"
            + rowName
            + "</span> "
            + ((value == null) ? "No definido" : value)
            + "</p>";
    return mHTML;
}

function btnIntoDiv(attributes, divID) {
    var mDiv = document.getElementById(divID);
    if (mDiv != null) {
        var newButton = document.createElement("button");
        newButton.innerHTML = attributes.btnText;
        newButton.type = attributes.btnType;
        newButton.className = attributes.btnClass;
        newButton.setAttribute("style", attributes.btnStyle);
        newButton.setAttribute("onclick", attributes.onclick);
        mDiv.appendChild(newButton);
    }
}

function titleIntoDiv(titleName, divID) {
    var mDiv = document.getElementById(divID); 
    if (mDiv != null) {
        var newTitle = document.createElement("h1");
        newTitle.className = "heading";
        newTitle.style.fontSize = "20px";
        newTitle.style.textTransform = "capitalize";
        newTitle.style.marginTop = "10px";
        newTitle.innerHTML = titleName;
        mDiv.appendChild(newTitle);
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

/**
 * Funcion para consultar un servelet del backend
 * @param url URL del servelet
 * @param data Query con los datos de la consulta
 * @param callback Funcion callback que ejecuta la respuesta
 * @returns
 */
function getServelet(url, data, param, callback) {
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
        	return callback(xhr.responseText);
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
                insertIntoNavbar("dropmenu-publicaciones", obj_roles[i].label, obj_roles[i].referencia, "");
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