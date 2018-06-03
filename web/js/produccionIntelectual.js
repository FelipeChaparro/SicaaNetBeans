//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-dev/";
var misPublicaciones = [];
var aAutoresEliminados;
var userToken;

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
        cargarNavBar(SESSION.roles, "Ver Publicaciones");
    
        // Cargar Imagen
        cargarImagen(SESSION.datosBasicos);

        userToken = SESSION.token;

        var user_login_data = "token=" + userToken + "&"
                            + "action=" + "VERIFICADAS";
    
        $("#loaderModificacion").hide();
        getServelet(SERVER_URL+"PublicacionesServelet", null, user_login_data, function(serveletResponse) {
            var respuesta = JSON.parse(serveletResponse);
            document.getElementById("loaderPublicaciones").style.display = "none";

            if (respuesta.code == "0") {
                //console.log(respuesta);
                var stringAutores;
                var label_auxiliar;
                for (var i = 0; i < respuesta.publicaciones.length; i++) {
                    stringAutores = "";
                    label_auxiliar = "";
                    var myPublicacion = document.createElement("a");
                    myPublicacion.className = "list-group-item";
                    myPublicacion.setAttribute("id","publicacion" + i);
                    obj_publicacion = new Object();

                    obj_publicacion.ID = respuesta.publicaciones[i].ID;
                    myPublicacion.innerHTML = "";
                    obj_publicacion.Titulo = respuesta.publicaciones[i].Titulo;

                    myPublicacion.innerHTML += "<div style='margin-left: 15px; z-index:1000;position:absolute;top:45%;left:45%;' class='smallLoader' id='loaderPublicacion" + i + "'></div>";

                    myPublicacion.innerHTML += "<h4 style='margin-top: 10px;' class='list-group-item-heading' id='title-" + i + "'>" + respuesta.publicaciones[i].Titulo + "</h4>";

                    obj_publicacion.Tipo = respuesta.publicaciones[i].Tipo;
                    myPublicacion.innerHTML += "<p id='type-" + i + "' class='list-group-item-text'><span style='color:#000000'>Tipo:</span>" + " " + ((respuesta.publicaciones[i].Tipo == null) ? "No definido" : respuesta.publicaciones[i].Tipo) + "</p>";

                    if (obj_publicacion.Tipo == "libro" || obj_publicacion.Tipo == "capitulo" || obj_publicacion.Tipo == "software" || obj_publicacion.Tipo == "articulo") {

                        if (obj_publicacion.Tipo == "libro") label_auxiliar = "ISBN:";
                        if (obj_publicacion.Tipo == "capitulo") label_auxiliar = "ISBN:";
                        if (obj_publicacion.Tipo == "software") label_auxiliar = "Registro:";
                        if (obj_publicacion.Tipo == "articulo") label_auxiliar = "ISSN:";

                        obj_publicacion.codigoPublicacion = respuesta.publicaciones[i].codigoPublicacion;
                        myPublicacion.innerHTML += "<p id='code-" + i + "' class='list-group-item-text'><span style='color:#000000'>" + label_auxiliar + "</span>" + " " + ((respuesta.publicaciones[i].codigoPublicacion == null) ? "No definido" : respuesta.publicaciones[i].codigoPublicacion) + "</p>";
                    }

                    obj_publicacion.Lugar = respuesta.publicaciones[i].Lugar;
                    myPublicacion.innerHTML += "<p id='place-" + i + "' class='list-group-item-text'><span style='color:#000000'>Lugar:</span>" + " " + ((respuesta.publicaciones[i].Lugar == null) ? "No definido" : respuesta.publicaciones[i].Lugar) + "</p>";

                    if (obj_publicacion.Tipo == "libro" || obj_publicacion.Tipo == "capitulo" || obj_publicacion.Tipo == "articulo") {

                        obj_publicacion.Editorial = respuesta.publicaciones[i].Editorial;
                        myPublicacion.innerHTML += "<p id='editorial-" + i + "' class='list-group-item-text'><span style='color:#000000'>Editorial:</span>" + " " + ((respuesta.publicaciones[i].Editorial == null) ? "No definido" : respuesta.publicaciones[i].Editorial) + "</p>";
                    }

                    obj_publicacion.FechaInicio = respuesta.publicaciones[i].FechaInicio;
                    myPublicacion.innerHTML += "<p id='fecha-" + i + "' class='list-group-item-text'><span style='color:#000000'>Fecha:</span>" + " " + 
                    ((respuesta.publicaciones[i].FechaInicio == null) ? "No definido" : respuesta.publicaciones[i].FechaInicio) + "</p>";

                    if (obj_publicacion.Tipo == "software") {
                        obj_publicacion.Plataforma = respuesta.publicaciones[i].Plataforma;
                        myPublicacion.innerHTML += "<p id='plataform-" + i + "' class='list-group-item-text'><span style='color:#000000'>Plataforma:</span>" + " " + ((respuesta.publicaciones[i].Plataforma == null) ? "No definido" : respuesta.publicaciones[i].Plataforma) + "</p>";
                    }

                    if (obj_publicacion.Tipo == "trabajo dirigido") {
                        obj_publicacion.Duracion = respuesta.publicaciones[i].Duracion;
                        myPublicacion.innerHTML += "<p id='duration-" + i + "' class='list-group-item-text'><span style='color:#000000'>Duración:</span>" + " " + ((respuesta.publicaciones[i].Duracion == null) ? "No definido" : respuesta.publicaciones[i].Duracion) + "</p>";

                        obj_publicacion.tipoEspecifico = respuesta.publicaciones[i].tipoEspecifico;
                        myPublicacion.innerHTML += "<p id='especifico-" + i + "' class='list-group-item-text'><span style='color:#000000'>Tipo Especifico:</span>" + " " + ((respuesta.publicaciones[i].tipoEspecifico == null) ? "No definido" : respuesta.publicaciones[i].tipoEspecifico) + "</p>";
                    }

                    obj_publicacion.Autores = respuesta.publicaciones[i].Autores;

                    for (var j = 0; j < respuesta.publicaciones[i].Autores.length; j++) {
                        stringAutores += ((j == 0) ? "" : ", ") + respuesta.publicaciones[i].Autores[j].Nombre;
                    }

                    myPublicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Autores:</span>" + " " + stringAutores + "</p>";

                    myPublicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Extraido:</span>" + " " + respuesta.publicaciones[i].Extraido + "</p>";

                    myPublicacion.innerHTML += "<button type='button' class='btn btn-info' data-toggle='modal' data-target='#myModal' onclick='modificarInformacion(" + i + ")'>Modificar</button>";
                    myPublicacion.innerHTML += "<button type='button' style='margin-left:10px' class='btn btn-danger' onclick='eliminarPublicacion("+ i +")'><span class='glyphicon glyphicon-remove'></span> Eliminar</button>";

                    misPublicaciones.push(obj_publicacion);
                    document.getElementById("lista-" + obj_publicacion.Tipo).appendChild(myPublicacion);
                    $("#loaderPublicacion" + i).hide();
                }
                if (document.getElementById("lista-articulo").innerHTML == "")
                    document.getElementById("lista-articulo").innerHTML = "No tienes artículos verificados.";

                if (document.getElementById("lista-libro").innerHTML == "")
                    document.getElementById("lista-libro").innerHTML = "No tienes libros verificados.";

                if (document.getElementById("lista-conferencia").innerHTML == "")
                    document.getElementById("lista-conferencia").innerHTML = "No tienes conferencias verificadaes.";

                if (document.getElementById("lista-capitulo").innerHTML == "")
                    document.getElementById("lista-capitulo").innerHTML = "No tienes capítulos verificados.";

                if (document.getElementById("lista-trabajo dirigido").innerHTML == "")
                    document.getElementById("lista-trabajo dirigido").innerHTML = "No tienes trabajos dirigidos verificados.";

                if (document.getElementById("lista-software").innerHTML == "")
                    document.getElementById("lista-software").innerHTML = "No tienes software verificados.";
            }
           else {
                alert("Fallo codigo: " + respuesta.code.toString()+ " - Descripcion: " + respuesta.description);
           }
        });
    }
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

function cerrarSesion() {
    sessionStorage.clear();
    window.location.href = SERVER_URL;
}

function verificarPublicacion(i) {
    $("#loaderPublicacion" + i).show();
    document.getElementById("btnVerificar" + i).innerHTML = "Verificando...";
    var mPublicacion = document.getElementById("publicacion" + i);
    var data_to_send = new Object();
    var mBtnPublicacion, mBtnVerificar, mBtnRechazar;
    data_to_send.token = userToken;
    data_to_send.idPublicacion = misPublicaciones[i].ID;
    data_to_send.nuevoEstado = "Verificado";

    postServlet(SERVER_URL + "NuevoEstadoPublicacionServlet", JSON.stringify(data_to_send), function(servletResponse) {
        var response = JSON.parse(servletResponse);
        $("#loaderPublicacion" + i).hide();
        if (response.code == 0) {
            mBtnPublicacion = document.getElementById("btnPublicacion" + i);
            mBtnPublicacion.className = "btn btn-success";
            mBtnPublicacion.innerHTML = "<i class='glyphicon glyphicon-ok-sign'></i>";
            mBtnPublicacion.setAttribute("onclick","showMeDescription(\"Verificado\"," + i + ")");

            var newBtn = document.createElement("button");
            newBtn.type = "button";
            newBtn.innerHTML = "Modificar";
            newBtn.className = "btn btn-info";
            newBtn.setAttribute("data-target", "#myModal");
            newBtn.setAttribute("data-toggle", "modal");
            newBtn.setAttribute("onclick", "modificarInformacion(" + i + ")");
            $("#btnVerificar" + i).replaceWith(newBtn);

            document.getElementById("btnRechazar" + i).innerHTML = "<span class='glyphicon glyphicon-remove'></span> Eliminar";
        }
        else
            alert("Fallo código: " + response.code.toString() + " - Descripción: " + response.description);
    });
    
    //Tooltip
    $('[data-toggle="tooltip"]').tooltip(); 
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

function eliminarPublicacion(i) {
    var mPublicacion = document.getElementById("publicacion" + i);
    var data_to_send = new Object();
    data_to_send.token = userToken;
    data_to_send.idPublicacion = misPublicaciones[i].ID;

    if (confirm("¿Desea eliminar esta publicación?")) {
        $("#loaderPublicacion" + i).show();
        postServlet(SERVER_URL + "DesactivarPublicacionServlet", JSON.stringify(data_to_send), function(serveletResponse) {
            var respuesta = JSON.parse(serveletResponse);
            $("#loaderPublicacion" + i).hide();
            if (respuesta.code == 0) {
                document.getElementById("lista-" + misPublicaciones[i].Tipo).removeChild(mPublicacion);
            }
            else
               alert("Fallo código: " + respuesta.code.toString()+ " - Descripcion: " + respuesta.description); 
        });
    }
}

function modificarInformacion(i){
    aAutoresEliminados = [];
    var tipo = misPublicaciones[i].Tipo;
    var cantidad_autores = misPublicaciones[i].Autores.length
    showTextInputs(tipo, cantidad_autores);
    var mBoton = document.getElementById("btn_modificar");
    mBoton.setAttribute("onclick","actualizarInformacion(" + i + ")");
    
    document.getElementById("m_tituloPublicacion").value = misPublicaciones[i].Titulo;
    document.getElementById("m_tipoPublicacion").value = misPublicaciones[i].Tipo;

    if (tipo == "libro" || tipo == "capitulo" || tipo == "software" || tipo == "articulo") {
        document.getElementById("m_codigoPublicacion").value = misPublicaciones[i].codigoPublicacion;
    } 
    document.getElementById("m_lugarPublicacion").value = misPublicaciones[i].Lugar;
    
    if (tipo == "libro" || tipo == "capitulo" || tipo == "articulo") {
        document.getElementById("m_editorialPublicacion").value = misPublicaciones[i].Editorial;
    }
    
    document.getElementById("m_fechaPublicacion").value = misPublicaciones[i].FechaInicio;
    
    if (tipo == "trabajo dirigido") {
        document.getElementById("m_duracionPublicacion").value = misPublicaciones[i].Duracion;
        document.getElementById("m_tipoEspecifico").value = misPublicaciones[i].tipoEspecifico;
    }
    
    if (tipo == "software") {
        document.getElementById("m_plataformaPublicacion").value = misPublicaciones[i].Plataforma;
    }
    
    for (var j = 0; j < cantidad_autores; j++) {
        document.getElementById("m_autor_name_" + j).value = misPublicaciones[i].Autores[j].Nombre;
        document.getElementById("m_autor_rol_" + j).value = misPublicaciones[i].Autores[j].Rol;
    }
}

function actualizarInformacion(i) {
    $("#loaderModificacion").show();
    document.getElementById("btn_modificar").innerHTML = "Actualizando...";
    document.getElementById("btn_modificar").disabled = true;
    var newTitulo;
    var newTipo;
    var newCodigo = "";
    var newLugar = "";
    var newEditorial = "";
    var newFecha = "";
    var newPlataforma = "";
    var newDuracion = "";
    var newTipoEspecifico = "";
    var arrayAutores = [];
    var div_autores = document.getElementById("autores-publicacion");
    var data_to_send = new Object();
    
    newTitulo = (document.getElementById("m_tituloPublicacion").value).trim();
    newTipo = document.getElementById("m_tipoPublicacion").value;
    
    if (newTipo == "libro" || newTipo == "capitulo" || newTipo == "software" || newTipo == "articulo")
        newCodigo = (document.getElementById("m_codigoPublicacion").value).trim();
    
    newLugar = (document.getElementById("m_lugarPublicacion").value).trim();
    
    if (newTipo == "libro" || newTipo == "capitulo" || newTipo == "articulo")
        newEditorial = (document.getElementById("m_editorialPublicacion").value).trim();
    
    newFecha = (document.getElementById("m_fechaPublicacion").value).trim();
    
    if (newTipo == "trabajo dirigido") {
        newDuracion = (document.getElementById("m_duracionPublicacion").value).trim();
        newTipoEspecifico = (document.getElementById("m_tipoEspecifico").value).trim();
    }
    
    if (newTipo == "software")
        newPlataforma = (document.getElementById("m_plataformaPublicacion").value).trim();

    if (newCodigo == "") newCodigo = null;
    if (newLugar == "") newLugar = null;
    if (newEditorial == "") newEditorial = null;
    if (newFecha == "") newFecha = null;
    if (newPlataforma == "") newPlataforma = null;
    if (newDuracion == "") newDuracion = null;
    if (newTipoEspecifico == "") newTipoEspecifico = null;
    
    data_to_send.ID = misPublicaciones[i].ID;
    data_to_send.Titulo = newTitulo;
    data_to_send.Tipo = newTipo;
    data_to_send.codigoPublicacion = newCodigo;
    data_to_send.Lugar = newLugar;
    data_to_send.Editorial = newEditorial;
    data_to_send.FechaInicio = newFecha;
    data_to_send.Plataforma = newPlataforma;
    data_to_send.Duracion = newDuracion;
    data_to_send.tipoEspecifico = newTipoEspecifico;

    for (var i = 0; i < div_autores.childNodes.length; i++) {
        if (div_autores.childNodes[i].tagName == "INPUT") {
            if (div_autores.childNodes[i].value != "") {
                var oAutor = new Object();
                oAutor.Nombre = div_autores.childNodes[i].value;
                oAutor.Rol = ((div_autores.childNodes[i + 1].value == "") ? null : div_autores.childNodes[i + 1].value)
                oAutor.ID = getAutorID(div_autores.childNodes[i].value);
                arrayAutores.push(oAutor);
            }
        }
    }
    data_to_send.Autores_Eliminados = aAutoresEliminados;
    data_to_send.Autores = arrayAutores

    let isValid = true;
    if (data_to_send.FechaInicio != null) isValid = data_to_send.FechaInicio();
    
    if (isValid) {
        postServlet(SERVER_URL + "ActualizarPublicacionServlet", JSON.stringify(data_to_send), function(serveletResponse) {
            var respuesta = JSON.parse(serveletResponse);
            $("#loaderModificacion").hide();
            document.getElementById("btn_modificar").innerHTML = "Actualizar Información";
            document.getElementById("btn_modificar").disabled = false;

            if (respuesta.code == 0) {
                alert("Actualización Correcta!");
                location.reload(true);
            }
            else {
                alert("Error: " + respuesta.code + " - Descripción: " + respuesta.description);
            }
        });
    }
    else {
        alert("Error en fecha");
        $("#loaderModificacion").hide();
        document.getElementById("btn_modificar").innerHTML = "Actualizar Información";
        document.getElementById("btn_modificar").disabled = false;
    }
}

function goHome(){
    window.location.href='../Vista/principal.html';
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

// Post para actualizar una publicación
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

function showTextInputs(tipo, cantidad_autores) {
    var mModal = document.getElementById("inputs-modal");
    var newDiv = document.createElement("div");
    var newBtn;
    
    mModal.innerHTML = "";
    newDiv.className = "form-group";
    newDiv.innerHTML = "<label >Titulo:</label>";
    newDiv.innerHTML += "<input type='text' class='form-control' id='m_tituloPublicacion'>";
    mModal.append(newDiv);
    
    newDiv = document.createElement("div");
    newDiv.className = "form-group";
    newDiv.innerHTML = "<label >Tipo:</label>";
    newDiv.innerHTML += "<div class='form-group'><select class='form-control' id='m_tipoPublicacion' disabled>"
                        + "<option>libro</option>"
                        + "<option>articulo</option>"
                        + "<option>capitulo</option>"
                        + "<option>conferencia</option>"
                        + "<option>software</option>"
                        + "<option>trabajo dirigido</option>"
                        + "</select></div>";
    mModal.append(newDiv);

    if (tipo == "libro" || tipo == "capitulo" || tipo == "software" || tipo == "articulo") {
        newDiv = document.createElement("div");
        newDiv.className = "form-group";

        if (tipo == "libro") newDiv.innerHTML = "<label>ISBN:</label>";
        if (tipo == "capitulo") newDiv.innerHTML = "<label>ISBN:</label>";
        if (tipo == "software") newDiv.innerHTML = "<label>Registro:</label>";
        if (tipo == "articulo") newDiv.innerHTML = "<label>ISSN:</label>";

        newDiv.innerHTML += "<input type='text' class='form-control' id='m_codigoPublicacion'>";
        mModal.append(newDiv);
    }
    
    newDiv = document.createElement("div");
    newDiv.className = "form-group";
    newDiv.innerHTML = "<label >Lugar:</label>";
    newDiv.innerHTML += "<input type='text' class='form-control' id='m_lugarPublicacion'>";
    mModal.append(newDiv);
    
    if (tipo == "libro" || tipo == "capitulo" || tipo == "articulo") {
        newDiv = document.createElement("div");
        newDiv.className = "form-group";
        newDiv.innerHTML = "<label>Editorial:</label>";
        newDiv.innerHTML += "<input type='text' class='form-control' id='m_editorialPublicacion'>";
        mModal.append(newDiv);
    }

    newDiv = document.createElement("div");
    newDiv.className = "form-group";
    newDiv.innerHTML = "<label >Fecha:</label>";
    newDiv.innerHTML += "<input type='text' placeholder='YYYY-MM-DD' class='form-control' id='m_fechaPublicacion'>";
    mModal.append(newDiv);
    
    if (tipo == "trabajo dirigido") {
        newDiv = document.createElement("div");
        newDiv.className = "form-group";
        newDiv.innerHTML = "<label>Duracion:</label>";
        newDiv.innerHTML += "<input type='text' class='form-control' id='m_duracionPublicacion'>";
        mModal.append(newDiv);
        
        newDiv = document.createElement("div");
        newDiv.className = "form-group";
        newDiv.innerHTML = "<label>Tipo Especifico:</label>";
        newDiv.innerHTML += "<input type='text' class='form-control' id='m_tipoEspecifico'>"; 
        mModal.append(newDiv);
    }
    
    if (tipo == "software") {
        newDiv = document.createElement("div");
        newDiv.className = "form-group";
        newDiv.innerHTML = "<label>Plataforma:</label>";
        newDiv.innerHTML += "<input type='text' class='form-control' id='m_plataformaPublicacion'>";
        mModal.append(newDiv);
    }
    
    newDiv = document.createElement("div");
    newDiv.className = "form-group";
    newDiv.setAttribute("id", "autores-publicacion");
    newDiv.innerHTML = "<label style='display:block;'>Coautores:</label>";
    for (var i = 0; i < cantidad_autores; i++) {
        newDiv.innerHTML += HTMLAutorInfo(i, "disabled");
    }
    mModal.append(newDiv);
    
    newBtn = document.createElement("button");
    newBtn.className = "btn btn-success";
    newBtn.setAttribute("type", "button");
    newBtn.setAttribute("onclick", "addAutor()");
    newBtn.innerHTML = "<span class='glyphicon glyphicon-plus'></span>";
    mModal.append(newBtn);
}

function addAutor() {
    var div_autores = document.getElementById("autores-publicacion");   
    var newInputBox = document.createElement("input");
    var newSelect = document.createElement("select");
    var newButton = document.createElement("button");
    
    newInputBox.setAttribute("type", "text");
    newInputBox.setAttribute("class", "form-control");
    newInputBox.setAttribute("style", "display:inline;width:40%;margin-bottom:10px;");
    
    newSelect.setAttribute("class", "form-control");
    newSelect.setAttribute("style", "display:inline;margin-left:20px; width:40%;");
    newSelect.innerHTML = "<option> Profesor </option>"
                            + "<option> Estudiante </option>"
                            + "<option> Externo </option>";
    newSelect.value = null;
    
    newButton.setAttribute("type", "button");
    newButton.setAttribute("class", "btn btn-danger");
    newButton.setAttribute("style", "margin-left:10px;height:34px;");
    newButton.innerHTML = "<span class='glyphicon glyphicon-remove'></span>";
    newButton.setAttribute("onclick", "removeLastAutor()");
    
    div_autores.appendChild(newInputBox);
    div_autores.appendChild(newSelect);
    div_autores.appendChild(newButton);
}

function removeAutor(i) {
    var div_autores = document.getElementById("autores-publicacion");
    var mAutorName = document.getElementById("m_autor_name_" + i);
    var mRol = document.getElementById("m_autor_rol_" + i);
    var mBtn = document.getElementById("button_autor_" + i);
    aAutoresEliminados.push(getAutorID(mAutorName.value));
    div_autores.removeChild(mAutorName);
    div_autores.removeChild(mRol);
    div_autores.removeChild(mBtn);
}

function getAutorID(autorName) {
    for (var i = 0; i < misPublicaciones.length; i++) {
        for (var j = 0; j < misPublicaciones[i].Autores.length; j++) {
            if (misPublicaciones[i].Autores[j].Nombre == autorName) {
                return misPublicaciones[i].Autores[j].ID;
            }
        }
    }
    return "-1";
}

function removeLastAutor() {
    var div_autores = document.getElementById("autores-publicacion");
    div_autores.removeChild(div_autores.lastChild);
    div_autores.removeChild(div_autores.lastChild);
    div_autores.removeChild(div_autores.lastChild);
}

function HTMLAutorInfo(j, status) {
    var mHTML = "";
    mHTML = "<input id='m_autor_name_" + j + "' style='display:inline;width:40%;margin-bottom:10px;' type='text' class='form-control'" + status + ">";
    mHTML += "<select id='m_autor_rol_" + j + "' class='form-control' style='display:inline;margin-left:20px; width:40%;'>"
            + "<option>" + "Profesor" + "</option>"
            + "<option>" + "Estudiante" + "</option>"
            + "<option>" + "Externo" + "</option>"
            + "</select>";
    mHTML += "<button id='button_autor_" + j + "' type='button' class='btn btn-danger' style='margin-left:10px;height:34px;' onclick='removeAutor(" + j + ")'><span class='glyphicon glyphicon-remove'></span></button>";
    return mHTML;
}

function CSVCreator(element) {
    var eURI = "";
    var pTipo = "";
    var text = "";
    var eTipo = element.getAttribute("id").split("-")[1];
    var eID = element.getAttribute("id");
    var content = "data:text/csv;charset=utf-8," + CSVHeader(eTipo);
    
    for (var i = 0; i < misPublicaciones.length; i++) {
        pTipo = misPublicaciones[i].Tipo;
        if (pTipo == eTipo) {
            content += misPublicaciones[i].Titulo.replace(/,/g, "") + ",";
            
            if (pTipo == "articulo" || pTipo == "libro" || pTipo == "capitulo" || pTipo == "software") {
                text = misPublicaciones[i].codigoPublicacion;
                if (text != null)
                    content += text.replace(/,/g, "") + ",";
                else
                    content += "No definido,";
            }
            
            text = misPublicaciones[i].Lugar;
            if (text != null)
                content += text.replace(/,/g, "") + ",";
            else
                content += "No definido,";
                
            if (pTipo == "articulo" || pTipo == "libro" || pTipo == "capitulo") {
                text = misPublicaciones[i].Editorial;
                if (text != null)
                    content += text.replace(/,/g, "") + ",";
                else
                    content += "No definido,";
            }
            
            text = misPublicaciones[i].FechaInicio;
            if (text != null)
                content += text.replace(/,/g, "") + ",";
            else
                content += "No definido,";
            
            if (pTipo == "trabajo dirigido") {
                text = misPublicaciones[i].Duracion;
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
            
            if (pTipo == "software") {
                text = misPublicaciones[i].Plataforma;
                if (text != null)
                    content += text.replace(/,/g, "") + ",";
                else
                    content += "No definido,";
            }
            
            content += getAutores(misPublicaciones[i].Autores);
            content += "\r\n";
        }
    }
    //console.log(content);
    eURI = encodeURI(content);
    element.setAttribute("href", eURI);
    element.setAttribute("download", eID + ".csv");
}

function getAutores(autores) {
    var mAutores = "";
    
    for (var i = 0; i < autores.length; i++) {
        mAutores += ((i == 0) ? "" : "-") + autores[i].Nombre.replace(/,/g, "");
    }
    return mAutores;
}

function CSVHeader(tipo) {
    var mHeader = "\uFEFF";

    if (tipo == "libro" || tipo == "capitulo")
        mHeader += "Titulo,ISBN,Lugar,Editorial,Fecha,Autores";
    
    if (tipo == "articulo")
        mHeader += "Titulo,ISSN,Lugar,Editorial,Fecha,Autores";

    if (tipo == "conferencia")
        mHeader += "Titulo,Lugar,Fecha,Autores";

    if (tipo == "trabajo dirigido")
        mHeader += "Titulo,Lugar,Fecha,Duración,TipoEspecifico,Autores";

    if (tipo == "software")
        mHeader += "Titulo,Registro,Lugar,Fecha,Plataforma,Autores";
    
    return mHeader + "\r\n";
}
