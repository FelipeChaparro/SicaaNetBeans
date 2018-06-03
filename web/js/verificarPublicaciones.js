//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-dev/";
var misPublicaciones = [];
var aAutoresEliminados;
var userToken;
var response_dudosas = [];

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
        cargarNavBar(SESSION.roles, "Verificar Publicaciones");
    
        //Cargar Imagen
        cargarImagen(SESSION.datosBasicos);
    
        userToken = SESSION.token;

        var user_login_data = "token=" + userToken + "&"
                            + "action=" + "NO_VERIFICADAS";
    
        $("#loaderModificacion").hide();
        // Get publicaciones no verificadas
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

                    myPublicacion.innerHTML += "<button type='button' class='btn btn-success' onclick='modificarInformacion(" + i + ")' data-target='#myModal' data-toggle='modal' >Verificar y Completar</button>";

                    myPublicacion.innerHTML += "<button type='button' style='margin-left:10px' class='btn btn-danger' onclick='eliminarPublicacion(" + i + ")' id='btn-eliminar-" + i + "'><span class='glyphicon glyphicon-remove'></span> Rechazar</button>";

                    misPublicaciones.push(obj_publicacion);
                    document.getElementById("lista-por-verificar").appendChild(myPublicacion);
                    $("#loaderPublicacion" + i).hide();
                }
                if (document.getElementById("lista-por-verificar").innerHTML == "")
                    document.getElementById("lista-por-verificar").innerHTML = "No tienes publicaciones pendientes.";
            }
           else {
                alert("Fallo Código: " + respuesta.code.toString()+ " - Descripcion: " + respuesta.description);
           }
        });
    
    getServelet(SERVER_URL + 'PublicacionDudosaServlet', null, "token=" + userToken, function(serveletResponse) {
        var response = JSON.parse(serveletResponse);
        if (response.code == 0) {
            var tipo_auxiliar = "";
            var label_auxiliar = "";
            response_dudosas = response;
            var stringAutores;
            for (var i = 0; i < response.publicaciones.length; i++) {
                for (var j = 0; j < response.publicaciones[i].publicacion.length; j++) {
                    stringAutores = "";
                    if (j == 0) {
                        var title_original = document.createElement("h1");
                        title_original.className = "heading";
                        title_original.setAttribute("style", "font-size:20px;margin-top:20px;");
                        title_original.innerHTML = (i + 1) + ". Publicación Original";
                        var breakHTML = document.createElement("hr");    
                        breakHTML.setAttribute("style", "margin-top: 20px;");
                        breakHTML.className = "divider";
                    }
                    
                    // Element Anchor Publicación
                    var anchor_publicacion = document.createElement("a");
                    anchor_publicacion.className = "list-group-item";
                    anchor_publicacion.setAttribute("id", "row-" + i + "-" + j);

                    // Loader Publicacion
                    anchor_publicacion.innerHTML = "<div style='margin-left: 15px; z-index:1000;position:absolute;top:45%;left:45%;display:none;' class='smallLoader' id='loader-" + i + "-" + j + "'></div>";
                    
                    // Titulo Publicacion
                    anchor_publicacion.innerHTML += "<h4 style='margin-top: 10px;' class='list-group-item-heading'>" 
                        + response.publicaciones[i].publicacion[j].Titulo + "</h4>";

                    //Tipo de Publicacion
                    anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Tipo:</span>" + " " + ((response.publicaciones[i].publicacion[j].Tipo == null) ? "No definido" : response.publicaciones[i].publicacion[j].Tipo) + "</p>";

                    tipo_auxiliar = response.publicaciones[i].publicacion[j].Tipo;

                    // Código Publicación
                    if (tipo_auxiliar == "libro" || tipo_auxiliar == "capitulo" || tipo_auxiliar == "software" || tipo_auxiliar == "articulo") {

                        if (tipo_auxiliar == "libro") label_auxiliar = "ISBN:";
                        if (tipo_auxiliar == "capitulo") label_auxiliar = "ISBN:";
                        if (tipo_auxiliar == "software") label_auxiliar = "Registro:";
                        if (tipo_auxiliar == "articulo") label_auxiliar = "ISSN:";

                        anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>" + label_auxiliar + "</span>" + " " + ((response.publicaciones[i].publicacion[j].codigoPublicacion == null) ? "No definido" : response.publicaciones[i].publicacion[j].codigoPublicacion) + "</p>";
                    }

                    // Lugar Publicación
                    anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Lugar:</span>" + " " + ((response.publicaciones[i].publicacion[j].Lugar == null) ? "No definido" : response.publicaciones[i].publicacion[j].Lugar) + "</p>";

                    // Editorial Publicación
                    if (tipo_auxiliar == "libro" || tipo_auxiliar == "capitulo" || tipo_auxiliar == "articulo") {

                        anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Editorial:</span>" + " " + ((response.publicaciones[i].publicacion[j].Editorial == null) ? "No definido" : response.publicaciones[i].publicacion[j].Editorial) + "</p>";
                    }

                    // Fecha de Publicación
                    anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Fecha:</span>" + " " + 
                    ((response.publicaciones[i].publicacion[j].FechaInicio == null) ? "No definido" : response.publicaciones[i].publicacion[j].FechaInicio) + "</p>";

                    if (tipo_auxiliar == "software") {

                        anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Plataforma:</span>" + " " + ((response.publicaciones[i].publicacion[j].Palataforma == null) ? "No definido" : response.publicaciones[i].publicacion[j].Plataforma) + "</p>";
                    }

                    if (tipo_auxiliar == "trabajo dirigido") {

                        anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Duración:</span>" + " " + ((response.publicaciones[i].publicacion[j].Duracion == null) ? "No definido" : response.publicaciones[i].publicacion[j].Duracion) + "</p>";

                        anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Tipo Especifico:</span>" + " " + ((response.publicaciones[i].publicacion[j].tipoEspecifico == null) ? "No definido" : response.publicaciones[i].publicacion[j].tipoEspecifico) + "</p>";
                    }

                    anchor_publicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Extraido:</span>" + " " + response.publicaciones[i].publicacion[j].Extraido + "</p>";

                    // Btn
                    if (j != 0) {
                        anchor_publicacion.innerHTML += "<button type='button' class='btn btn-warning' onclick='reemplazarPublicacion(" + i + ", " + j + ")'>Reemplazar</button>";

                        anchor_publicacion.innerHTML += "<button type='button' style='margin-left:10px' class='btn btn-info' onclick='notDudosa(" + i + ", " + j + ")'>Es diferente</button>";
                    }

                    // Header Original
                    if (j == 0)
                        document.getElementById("lista-ambigua").appendChild(title_original);

                    document.getElementById("lista-ambigua").appendChild(anchor_publicacion);

                    if (j == 0)
                        document.getElementById("lista-ambigua").appendChild(breakHTML);
                }
            }
            if (document.getElementById("lista-ambigua").innerHTML == "")
                    document.getElementById("lista-ambigua").innerHTML = "No hay cargas ambiguas.";
        }
        else {
            alert("Fallo Código: " + response.code + " - Descripción: Error mostrando publicaciones ambigüas.");
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

function reemplazarPublicacion(i, j) {
    var data_to_send = new Object();
    data_to_send = response_dudosas.publicaciones[i].publicacion[j];
    $("#loader-" + i + "-" + j).show();
    postServlet(SERVER_URL + "PublicacionDudosaServlet", JSON.stringify(data_to_send), function(serveletResponse) {
        $("#loader-" + i + "-" + j).hide();
        var respuesta = JSON.parse(serveletResponse);
        if (respuesta.code == 0) {
            alert("La publicación se ha actualizado correctamente!");
        }
        else {
            alert("Error: " + respuesta.code + " - Descripción: " + respuesta.description);
        }
    });
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

function notDudosa(i, j) {
    $("#loader-" + i + "-" + j).show();
    var data_to_send = response_dudosas.publicaciones[i].publicacion[j];
    
    postServlet(SERVER_URL + "PublicacionNoDudosaServlet", JSON.stringify(data_to_send), function(servletReponse) {
        $("#loader-" + i + "-" + j).hide();
        var respuesta = JSON.parse(servletReponse);
        if (respuesta.code == 0) {
            $("#row-" + i + "-" + j).remove();
            alert("La publicación se encuentra pendiente por verificar!");
        }
        else {
            alert("Error: " + respuesta.code + " - Descripción: " + respuesta.description);
        }
    });
}

function verificarPublicacion(i) {
    if (document.getElementById("m_tipoPublicacion").value != "") {
        document.getElementById("btn_modificar").innerHTML = "Actualizando...";
        document.getElementById("btn_modificar").disabled = true;
        $("#loaderModificacion").show();
        var data_to_send = new Object();
        data_to_send.token = userToken;
        data_to_send.idPublicacion = misPublicaciones[i].ID;
        data_to_send.nuevoEstado = "Verificado";

        postServlet(SERVER_URL + "NuevoEstadoPublicacionServlet", JSON.stringify(data_to_send), function(servletResponse) {
            var response = JSON.parse(servletResponse);
            if (response.code == 0) {
                actualizarInformacion(i);
            }
            else {
                $("#loaderModificacion").hide();
                document.getElementById("btn_modificar").innerHTML = "Actualizar Información";
                document.getElementById("btn_modificar").disabled = false;
                alert("Error: " + respuesta.code + " - Descripción: " + respuesta.description);
            }
        });
    }
    else
        alert("El tipo de publicación no puede estar vacío");
}

function modificarInformacion(i){
    aAutoresEliminados = [];
    var tipo = misPublicaciones[i].Tipo;
    var cantidad_autores = misPublicaciones[i].Autores.length
    showTextInputs(tipo, cantidad_autores);
    
    var mBoton = document.getElementById("btn_modificar");
    mBoton.setAttribute("onclick","verificarPublicacion(" + i + ")");
    
    document.getElementById("m_tituloPublicacion").value = misPublicaciones[i].Titulo;
    document.getElementById("m_tipoPublicacion").value = misPublicaciones[i].Tipo;
    
    if (tipo != null) {
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
}

function eliminarPublicacion(i, j) {
    $("#loaderPublicacion" + i).show();
    var mPublicacion = document.getElementById("publicacion" + i);
    var data_to_send = new Object();
    data_to_send.token = userToken;
    data_to_send.idPublicacion = misPublicaciones[i].ID;

    
    postServlet(SERVER_URL + "DesactivarPublicacionServlet", JSON.stringify(data_to_send), function(serveletResponse) {
        var respuesta = JSON.parse(serveletResponse);
        $("#loaderPublicacion" + i).hide();
        if (respuesta.code == 0) {
            document.getElementById("lista-por-verificar").removeChild(mPublicacion);
        }
        else
           alert("Fallo código: " + respuesta.code.toString()+ " - Descripcion: " + respuesta.description); 
    });
    
}

function actualizarInformacion(i) {
    var mPublicacion;
    var newTitulo;
    var newTipo = "";
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

    if (misPublicaciones[i].Tipo != null) {
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

        for (var j = 0; j < div_autores.childNodes.length; j++) {
            if (div_autores.childNodes[j].tagName == "INPUT") {
                if (div_autores.childNodes[j].value != "") {
                    var oAutor = new Object();
                    oAutor.Nombre = div_autores.childNodes[j].value;
                    oAutor.Rol = ((div_autores.childNodes[j + 1].value == "") ? 
                                  null : 
                                  div_autores.childNodes[j + 1].value)
                    oAutor.ID = getAutorID(div_autores.childNodes[j].value);
                    arrayAutores.push(oAutor);
                }
            }
        }
        data_to_send.Autores_Eliminados = aAutoresEliminados;
        data_to_send.Autores = arrayAutores

        data_to_send.codigoPublicacion = newCodigo;
        data_to_send.Lugar = newLugar;
        data_to_send.Editorial = newEditorial;
        data_to_send.FechaInicio = newFecha;
        data_to_send.Plataforma = newPlataforma;
        data_to_send.Duracion = newDuracion;
        data_to_send.tipoEspecifico = newTipoEspecifico;
    }
    else {
        data_to_send.codigoPublicacion = misPublicaciones[i].codigoPublicacion;
        data_to_send.Lugar = misPublicaciones[i].Lugar;
        data_to_send.Editorial = misPublicaciones[i].Editorial;
        data_to_send.FechaInicio = misPublicaciones[i].FechaInicio;
        data_to_send.Plataforma = misPublicaciones[i].Plataforma;
        data_to_send.Duracion = misPublicaciones[i].Duracion;
        data_to_send.tipoEspecifico = misPublicaciones[i].tipoEspecifico;
        data_to_send.Autores = misPublicaciones[i].Autores;
        data_to_send.Autores_Eliminados = [];
    }
    
    let isValid = true;
    if (data_to_send.FechaInicio != null) isValid = dateValidation(data_to_send.FechaInicio);
    if (isValid) {
        data_to_send.ID = misPublicaciones[i].ID;
        data_to_send.Titulo = newTitulo;
        data_to_send.Tipo = newTipo;

        postServlet(SERVER_URL + "ActualizarPublicacionServlet", JSON.stringify(data_to_send), function(serveletResponse) {
            var respuesta = JSON.parse(serveletResponse);
            $("#loaderModificacion").hide();
            document.getElementById("btn_modificar").innerHTML = "Actualizar Información";
            document.getElementById("btn_modificar").disabled = false;
            if (respuesta.code == 0) {
                alert("Actualización Correcta!");
                mPublicacion = document.getElementById("publicacion" + i);
                document.getElementById("lista-por-verificar").removeChild(mPublicacion);
                document.getElementById("btn-close-modal").click();

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
    var newBtn, isEnabled = "";
    
    if (tipo != null) isEnabled = "disabled";
    
    mModal.innerHTML = "";
    newDiv.className = "form-group";
    newDiv.innerHTML = "<label >Titulo:</label>";
    newDiv.innerHTML += "<input type='text' class='form-control' id='m_tituloPublicacion'>";
    mModal.append(newDiv);
    
    newDiv = document.createElement("div");
    newDiv.className = "form-group";
    newDiv.innerHTML = "<label>Tipo:</label>";
    newDiv.innerHTML += "<div class='form-group'><select class='form-control' id='m_tipoPublicacion'" + isEnabled + ">"
                        + "<option>libro</option>"
                        + "<option>articulo</option>"
                        + "<option>capitulo</option>"
                        + "<option>conferencia</option>"
                        + "<option>software</option>"
                        + "<option>trabajo dirigido</option>"
                        + "</select></div>";
    mModal.append(newDiv);

    if (tipo != null) {
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
    var eTipo = element.getAttribute("id").split("-")[1];
    var eID = element.getAttribute("id");
    var content = "data:text/csv;charset=utf-8," + CSVHeader(eTipo);
    
    for (var i = 0; i < misPublicaciones.length; i++) {
        pTipo = misPublicaciones[i].Tipo;
        if (pTipo == eTipo) {
            if (pTipo == "articulo" || pTipo == "libro" || pTipo == "capitulo") {
                content += misPublicaciones[i].Titulo + ","
                        + misPublicaciones[i].codigoPublicacion + ","
                        + misPublicaciones[i].Lugar + ","
                        + misPublicaciones[i].Editorial + ","
                        + misPublicaciones[i].FechaInicio + ","
                        + getAutores(misPublicaciones[i].Autores);
            }
            if (pTipo == "conferencia") {
                content += misPublicaciones[i].Titulo + ","
                        + misPublicaciones[i].Lugar + ","
                        + misPublicaciones[i].FechaInicio + ","
                        + getAutores(misPublicaciones[i].Autores);
            }
            if (pTipo == "trabajo dirigido") {
                content += misPublicaciones[i].Titulo + ","
                        + misPublicaciones[i].Lugar + ","
                        + misPublicaciones[i].FechaInicio + ","
                        + misPublicaciones[i].Duracion + ","
                        + misPublicaciones[i].tipoEspecifico + ","
                        + getAutores(misPublicaciones[i].Autores);
            }
            if (pTipo == "software") {
                content += misPublicaciones[i].Titulo + ","
                        + misPublicaciones[i].codigoPublicacion + ","
                        + misPublicaciones[i].Lugar + ","
                        + misPublicaciones[i].FechaInicio + ","
                        + misPublicaciones[i].Plataforma + ","
                        + getAutores(misPublicaciones[i].Autores);
            }
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
        mAutores += ((i == 0) ? "" : "-") + autores[i].Nombre;
    }
    return mAutores;
}

function CSVHeader(tipo) {
    var mHeader = "";

    if (tipo == "libro" || tipo == "capitulo")
        mHeader = "Titulo,ISBN,Lugar,Editorial,Fecha,Autores";
    
    if (tipo == "articulo")
        mHeader = "Titulo,ISSN,Lugar,Editorial,Fecha,Autores";

    if (tipo == "conferencia")
        mHeader = "Titulo,Lugar,Fecha,Autores";

    if (tipo == "trabajo dirigido")
        mHeader = "Titulo,Lugar,Fecha,Duración,TipoEspecifico,Autores";

    if (tipo == "software")
        mHeader = "Titulo,Registro,Lugar,Fecha,Plataforma,Autores";
    
    return mHeader + "\r\n";
}
