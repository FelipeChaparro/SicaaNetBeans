//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNB/";
var misPublicaciones = [];
window.onload = function(){
     
    //var json = JSON.parse(sessionStorage.getItem("principal"));
    var nombre = document.getElementById("nombre");
    var departamento = document.getElementById("departamento");
    //departamento.innerHTML = json.datosBasicos.nombreDepartamento;
    //nombre.innerHTML =json.datosBasicos.nombre;
    //var user_login_data = "token=" + json.token;
    var user_login_data = "token=1234";
    $("#loaderModificacion").hide();
	getServelet(SERVER_URL+"PublicacionesServelet", null, user_login_data, function(serveletResponse) {
        var respuesta = JSON.parse(serveletResponse);
        document.getElementById("loaderPublicaciones").style.display = "none";
        if (respuesta.code == "0") {
    
    /*var respuesta = {
        publicaciones: [
            {
                "ID":"1",
                "Titulo":"ASHYI Plataforma basada en agentes para la planificación dinámica, inteligente y adaptativa de actividades aplicada a la educación personalizada",
                "Tipo":"libro",
                "codigoPublicacion":"978-958-716-827-3",
                "Lugar":"Colombia 2015.",
                "Editorial":"Editorial Pontificia Universidad Javeriana",
                "FechaInicio":"1969-12-31",
                "Extraido":"CvLac",
                "EstadoPublicacion":"Verificado"
            },
            {
                "ID":"2",
                "Titulo":"El trabajo cooperativo asistido por computador",
                "Tipo":"articulo",
                "codigoPublicacion":null,
                "Lugar":"Colombia Ingenieria Y Universidad",
                "Editorial":"Ediciones Uniandes",
                "FechaInicio":"1969-12-12",
                "Extraido":"ResearchGate",
                "EstadoPublicacion":"Pendiente de verificacion"
            }
        ]
    }*/
            console.log(respuesta);
            for (var i = 0; i < respuesta.publicaciones.length; i++) {
                var myPublicacion = document.createElement("a");
                myPublicacion.className = "list-group-item";
                myPublicacion.setAttribute("id","publicacion" + i);
                obj_publicacion = new Object();

                obj_publicacion.ID = respuesta.publicaciones[i].ID;
                myPublicacion.innerHTML = "";
                obj_publicacion.Titulo = respuesta.publicaciones[i].Titulo;
                
                if (respuesta.publicaciones[i].EstadoPublicacion == "Verificado") 
                    myPublicacion.innerHTML += "<button type='button' class='btn btn-success' id='btnPublicacion" + i + "' style='border-radius: 15px;' onclick='showMeDescription(\"Verificado\"," + i +")'><i class='glyphicon glyphicon-ok-sign' ></i></button>";
                if (respuesta.publicaciones[i].EstadoPublicacion == "Pendiente de verificacion") 
                    myPublicacion.innerHTML += "<button type='button' class='btn btn-warning' id='btnPublicacion" + i + "' style='border-radius: 15px;' onclick='showMeDescription(\"Pendiente de verificacion\"," + i +")'><i class='glyphicon glyphicon-question-sign' ></i></button>";
                
                myPublicacion.innerHTML += "<div style='margin-left: 15px; z-index:1000;position:absolute;top:45%;left:45%;' class='smallLoader' id='loaderPublicacion" + i + "'></div>";
                
                myPublicacion.innerHTML += "<h4 class='list-group-item-heading'>" + respuesta.publicaciones[i].Titulo + "</h4>";

                obj_publicacion.Tipo = respuesta.publicaciones[i].Tipo;
                myPublicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Tipo:</span>" + " " + ((respuesta.publicaciones[i].Tipo == null) ? "No definido" : respuesta.publicaciones[i].Tipo) + "</p>";

                obj_publicacion.codigoPublicacion = respuesta.publicaciones[i].codigoPublicacion;
                myPublicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>ISBN o ISSN:</span>" + " " + ((respuesta.publicaciones[i].codigoPublicacion == null) ? "No definido" : respuesta.publicaciones[i].codigoPublicacion) + "</p>";

                obj_publicacion.Lugar = respuesta.publicaciones[i].Lugar;
                myPublicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Lugar:</span>" + " " + ((respuesta.publicaciones[i].Lugar == null) ? "No definido" : respuesta.publicaciones[i].Lugar) + "</p>";

                obj_publicacion.Editorial = respuesta.publicaciones[i].Editorial;
                myPublicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Editorial:</span>" + " " + ((respuesta.publicaciones[i].Editorial == null) ? "No definido" : respuesta.publicaciones[i].Editorial) + "</p>";

                obj_publicacion.FechaInicio = respuesta.publicaciones[i].FechaInicio;
                myPublicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Fecha:</span>" + " " + 
                ((respuesta.publicaciones[i].FechaInicio == null) ? "No definido" : respuesta.publicaciones[i].FechaInicio) + "</p>";

                myPublicacion.innerHTML += "<p class='list-group-item-text'><span style='color:#000000'>Extraido:</span>" + " " + respuesta.publicaciones[i].Extraido + "</p>";
                
                if (respuesta.publicaciones[i].EstadoPublicacion == "Verificado") {
                    myPublicacion.innerHTML += "<button type='button' class='btn btn-info' data-toggle='modal' data-target='#myModal' onclick='modificarInformacion(" + i + ")'>Modificar</button>";
                    myPublicacion.innerHTML += "<button type='button' style='margin-left:10px' class='btn btn-danger' onclick='eliminarPublicacion("+ i +")'><span class='glyphicon glyphicon-remove'></span> Eliminar</button>";
                }
                if (respuesta.publicaciones[i].EstadoPublicacion == "Pendiente de verificacion") {
                    myPublicacion.innerHTML += "<button type='button' class='btn btn-success' onclick='verificarPublicacion(" + i + ")' id='btnVerificar" + i + "'><span class='glyphicon glyphicon-ok'></span> Verificar</button>";
                    myPublicacion.innerHTML += "<button type='button' style='margin-left:10px' class='btn btn-danger' onclick='eliminarPublicacion(" + i + ")' id='btnRechazar" + i + "'><span class='glyphicon glyphicon-remove'></span> Rechazar</button>";
                }

                misPublicaciones.push(obj_publicacion);
                document.getElementById("listaPublicaciones").appendChild(myPublicacion);
                $("#loaderPublicacion" + i).hide();
            }
        }
        else {
            alert("Fallo codigo: " + respuesta.code.toString()+ " - Descripcion: " + respuesta.description);
       }
    });
}

function verificarPublicacion(i) {
    $("#loaderPublicacion" + i).show();
    document.getElementById("btnVerificar" + i).innerHTML = "Verificando...";
    var mPublicacion = document.getElementById("publicacion" + i);
    var data_to_send = new Object();
    var mBtnPublicacion, mBtnVerificar, mBtnRechazar;
    data_to_send.token = "1234";
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
}

function eliminarPublicacion(i) {
    $("#loaderPublicacion" + i).show();
    var mPublicacion = document.getElementById("publicacion" + i);
    var data_to_send = new Object();
    data_to_send.token = "1234";
    data_to_send.idPublicacion = misPublicaciones[i].ID;

    if (confirm("¿Desea eliminar esta publicación?")) {
        postServlet(SERVER_URL + "DesactivarPublicacionServlet", JSON.stringify(data_to_send), function(serveletResponse) {
            var respuesta = JSON.parse(serveletResponse);
            $("#loaderPublicacion" + i).hide();
            if (respuesta.code == 0) {
                document.getElementById("listaPublicaciones").removeChild(mPublicacion);
            }
            else
               alert("Fallo código: " + respuesta.code.toString()+ " - Descripcion: " + respuesta.description); 
        });
    }
}

function showMeDescription(description,i) {
    var mBtnPublicacion;
    if (description == "Verificado") {
        mBtnPublicacion = document.getElementById("btnPublicacion" + i);
        mBtnPublicacion.innerHTML = "Verificado";
        mBtnPublicacion.setAttribute("onclick","showMeIcon(\"Verificado\"," + i + ")");
    }
    if (description == "Pendiente de verificacion") {
        mBtnPublicacion = document.getElementById("btnPublicacion" + i);
        mBtnPublicacion.innerHTML = "Por Verificar";
        mBtnPublicacion.setAttribute("onclick","showMeIcon(\"Por Verificar\"," + i + ")");
    }
}

function showMeIcon(description, i) {
    var mBtnPublicacion;
    if (description == "Verificado") {
        mBtnPublicacion = document.getElementById("btnPublicacion" + i);
        mBtnPublicacion.innerHTML = "<i class='glyphicon glyphicon-ok-sign'></i>";
        mBtnPublicacion.setAttribute("onclick","showMeDescription(\"Verificado\"," + i + ")");
    }
    if (description == "Por Verificar") {
        mBtnPublicacion = document.getElementById("btnPublicacion" + i);
        mBtnPublicacion.innerHTML = "<i class='glyphicon glyphicon-question-sign'></i>";
        mBtnPublicacion.setAttribute("onclick","showMeDescription(\"Pendiente de verificacion\"," + i + ")");
    }
}

function modificarInformacion(i){
    document.getElementById("infoModificacion").innerHTML = "";
    var mBoton = document.getElementById("btn_modificar");
    document.getElementById("m_tituloPublicacion").value = misPublicaciones[i].Titulo;
    document.getElementById("m_tipoPublicacion").value = misPublicaciones[i].Tipo;
    document.getElementById("m_codigoPublicacion").value = misPublicaciones[i].codigoPublicacion;
    document.getElementById("m_lugarPublicacion").value = misPublicaciones[i].Lugar;
    document.getElementById("m_editorialPublicacion").value = misPublicaciones[i].Editorial;
    document.getElementById("m_fechaPublicacion").value = misPublicaciones[i].FechaInicio;
    mBoton.setAttribute("onclick","actualizarInformacion(" + i + ")");
}

function actualizarInformacion(i) {
    $("#loaderModificacion").show();
    document.getElementById("btn_modificar").innerHTML = "Actualizando...";
    var mNull = false;
    var idPublicacion = misPublicaciones[i].ID;
    var newTitulo = (document.getElementById("m_tituloPublicacion").value).trim();
    var newTipo = document.getElementById("m_tipoPublicacion").value;
    var newCodigo = (document.getElementById("m_codigoPublicacion").value).trim();
    var newLugar = (document.getElementById("m_lugarPublicacion").value).trim();
    var newEditorial = (document.getElementById("m_editorialPublicacion").value).trim();
    var newFecha = (document.getElementById("m_fechaPublicacion").value).trim();

    if (newTitulo == "") newTitulo = null;
    if (newCodigo == "") newCodigo = null;
    if (newLugar == "") newLugar = null;
    if (newEditorial == "") newEditorial = null;
    if (newFecha == "") newFecha = null;
    
    if (newTitulo == null && newTitulo != misPublicaciones[i].Titulo) mNull = true;
    if (newCodigo == null && newCodigo != misPublicaciones[i].codigoPublicacion) mNull = true;
    if (newLugar == null && newLugar != misPublicaciones[i].Lugar) mNull = true;
    if (newEditorial == null && newEditorial != misPublicaciones[i].Editorial) mNull = true;
    if (newFecha == null && newFecha != misPublicaciones[i].FechaInicio) mNull = true;

    if (!mNull) {
        var data_publicacion = new Object();
        data_publicacion.ID = idPublicacion;
        data_publicacion.Titulo = newTitulo;
        data_publicacion.Tipo = newTipo;
        data_publicacion.codigoPublicacion = newCodigo;
        data_publicacion.Lugar = newLugar;
        data_publicacion.Editorial = newEditorial;
        data_publicacion.FechaInicio = newFecha;

        postServlet(SERVER_URL + "ActualizarPublicacionServlet", JSON.stringify(data_publicacion), function(serveletResponse) {
            $("#loaderModificacion").hide();
            document.getElementById("btn_modificar").innerHTML = "Actualizar Información";
            var respuesta = JSON.parse(serveletResponse);
            if (respuesta.code == 0) {
                document.getElementById("infoModificacion").innerHTML = "La publicación se ha actualizado correctamente!";
            }
            else {
                document.getElementById("infoModificacion").innerHTML = "Error: " + respuesta.code + " - Descripción: " + respuesta.description;
            }
        });
    }
    else {
        document.getElementById("infoModificacion").innerHTML = "Error: No puedes dejar campos vacios!";
        $("#loaderModificacion").hide();
        document.getElementById("btn_modificar").innerHTML = "Actualizar Información";
    }
}

function goHome(){
    window.location.href='../Vista/principal.html';
}

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