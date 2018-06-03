var SERVER_URL = "https://sicaadev.mybluemix.net/";
//var SERVER_URL = "http://localhost:8080/SicaaNBGIT/";

var servlet_response = {};
window.onload = function() { 
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    var params = SESSION.token;
    document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
    document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
    
    getServelet(SERVER_URL + 'PublicacionDudosaServlet', null, params, function(serveletResponse) {
        $("#loader-dudosas").hide();
	    var respuesta = JSON.parse(serveletResponse);
        if (respuesta.code == 0) {
            if (respuesta.publicaciones.length == 0) {
                alert("No tienes publicaciones pendientes");
            }
            else {
                servlet_response = respuesta;
                for (var i = 0; i < respuesta.publicaciones.length; i++) {
                    var t = document.createElement("table");
                    t.className = "table table-bordered";
                    t.innerHTML = getTableHead(i);
                    document.getElementById("tablasDudosas").appendChild(t);
                    
                    var btn = document.createElement("button");
                    btn.className = "btn btn-primary"; 
                    btn.type = "button"; 
                    btn.innerHTML = "Actualizar";
                    btn.style.width = "40%";
                    btn.setAttribute("onclick","actualizar_publicacion(" + i + ")");
                    
                    document.getElementById("tablasDudosas").innerHTML += "<div style='display:none; position:relative;' class='smallLoader' id='loader-sm-" + i +"'></div>";
                    document.getElementById("tablasDudosas").appendChild(btn);
                    document.getElementById("tablasDudosas").innerHTML += "<hr class='divider'/>";

                    for (var j = 0; j < respuesta.publicaciones[i].publicacion.length; j++) {
                        var row = document.createElement("tr");
                        var mText = "";
                        if (respuesta.publicaciones[i].publicacion[j].fuente == "dudosas") {
                            mText = respuesta.publicaciones[i].publicacion[j].Titulo;
                            row.innerHTML += "<td><center>"
                                            + "<input type='radio' name='titulo-publicacion-" + i + "' "
                                            + "value='" + mText +"'> "
                                            + mText +
                                            "</center></td>";
                            
                            mText = respuesta.publicaciones[i].publicacion[j].Tipo;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + "<input type='radio' name='tipo-publicacion-" + i + "' "
                                            + "value='" + mText +"'> "
                                            + mText +
                                            "</center></td>";
                            
                            mText = respuesta.publicaciones[i].publicacion[j].codigoPublicacion;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + "<input type='radio' name='codigo-publicacion-" + i + "' "
                                            + "value='" + mText + "'> "
                                            + mText +
                                            "</center></td>";
                            
                            mText = respuesta.publicaciones[i].publicacion[j].Lugar;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + "<input type='radio' name='lugar-publicacion-" + i + "' "
                                            + "value='" + mText + "'> "
                                            + mText +
                                            "</center></td>";

                            mText = respuesta.publicaciones[i].publicacion[j].Editorial;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + "<input type='radio' name='editorial-publicacion-" + i + "' "
                                            + "value='" + mText + "'> "
                                            + mText +
                                            "</center></td>";

                            mText = respuesta.publicaciones[i].publicacion[j].FechaInicio;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + "<input type='radio' name='fecha-publicacion-" + i + "' "
                                            + "value='" + mText +"'> "
                                            + mText +
                                            "</center></td>";

                            row.innerHTML += "<td><center>"
                                            + "<button type'button' onclick='not_dudosa(" + i +","+ j + ", this)' class='btn btn-warning'>No es dudosa</button><div style='display:none;' id='loader-" + i + "-" + j + "' class='smallLoader'></div>"
                                            + "</center></td>";
                        }
                        else {
                            mText = respuesta.publicaciones[i].publicacion[j].Titulo;
                            row.innerHTML += "<td><center>"
                                            + mText +
                                            "</center></td>";
                            
                            mText = respuesta.publicaciones[i].publicacion[j].Tipo;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + mText +
                                            "</center></td>";
                            
                            mText = respuesta.publicaciones[i].publicacion[j].codigoPublicacion;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + mText +
                                            "</center></td>";
                            
                            mText = respuesta.publicaciones[i].publicacion[j].Lugar;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + mText +
                                            "</center></td>";
                            
                            mText = respuesta.publicaciones[i].publicacion[j].Editorial;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + mText +
                                            "</center></td>";
                            
                            mText = respuesta.publicaciones[i].publicacion[j].FechaInicio;
                            if (mText == null) mText = "No definido";
                            row.innerHTML += "<td><center>"
                                            + mText +
                                            "</center></td>";
                            
                            row.innerHTML += "<td><center>"
                                            + "<p class='bg-success'>Registrada</p>"
                                            "</center></td>";
                        }
                        
                        row.setAttribute("id", "row-" + i + "-" + j);
                        document.getElementById("bodyTable" + i).appendChild(row);
                    }
                }
            }
        }
        else {
            alert("Error código: " + respuesta.code + " - Descripción: " + respuesta.description);
        }
     });
}

function actualizar_publicacion(idPublicacion) {
    var titulos = document.getElementsByName("titulo-publicacion-" + idPublicacion);
    var tipos = document.getElementsByName("tipo-publicacion-" + idPublicacion);
    var codigos = document.getElementsByName("codigo-publicacion-" + idPublicacion);
    var lugares = document.getElementsByName("lugar-publicacion-" + idPublicacion);
    var editoriales = document.getElementsByName("editorial-publicacion-" + idPublicacion);
    var fechas = document.getElementsByName("fecha-publicacion-" + idPublicacion);
    var mTitulo = null;
    var mTipo = null;
    var mCodigo = null;
    var mLugar = null;
    var mEditorial = null;
    var mFecha = null;
    
    for (var i = 0; i < titulos.length; i++)
        if (titulos[i].checked)
            mTitulo = titulos[i].value;
    
    for (var i = 0; i < tipos.length; i++)
        if (tipos[i].checked)
            if (tipos[i].value != "No definido")
                mTipo = tipos[i].value;
    
    for (var i = 0; i < codigos.length; i++)
        if (codigos[i].checked)
            if (codigos[i].value != "No definido")
                    mCodigo = codigos[i].value;
    
    for (var i = 0; i < lugares.length; i++)
        if (lugares[i].checked)
            if (lugares[i].value != "No definido")
                    mLugar = lugares[i].value;
    
    for (var i = 0; i < editoriales.length; i++)
        if (editoriales[i].checked)
            if (editoriales[i].value != "No definido")
                    mEditorial = editoriales[i].value;
    
    for (var i = 0; i < fechas.length; i++)
        if (fechas[i].checked)
            if (fechas[i].value != "No definido")
                    mFecha = fechas[i].value;
    
    if (mTitulo == null) {
        alert("Error: Debe seleccionar un título");
    }
    else {
        $("#loader-sm-" + idPublicacion).show();
        var data_to_send = new Object();
        console.log(servlet_response);
        data_to_send = servlet_response.publicaciones[idPublicacion].publicacion[0];
        data_to_send.Titulo = mTitulo;
        data_to_send.Tipo = mTipo;
        data_to_send.codigoPublicacion = mCodigo;
        data_to_send.Lugar = mLugar;
        data_to_send.Editorial = mEditorial;
        data_to_send.FechaFin = mFecha;
        
        postServlet(SERVER_URL + "PublicacionDudosaServlet", JSON.stringify(data_to_send), function(serveletResponse) {
            $("#loader-sm-" + idPublicacion).hide();
            var respuesta = JSON.parse(serveletResponse);
            if (respuesta.code == 0) {
                alert("La publicación se ha actualizado correctamente!");
            }
            else {
                alert("Error: " + respuesta.code + " - Descripción: " + respuesta.description);
            }
        });
    }
    
}

function not_dudosa(i, j, btn) {
    $(btn).hide();
    $("#loader-" + i + "-" + j).show();
    var data_to_send = servlet_response.publicaciones[i].publicacion[j];
    
    postServlet(SERVER_URL + "PublicacionNoDudosaServlet", JSON.stringify(data_to_send), function(serveletResponse) {
        $("#loader-" + i + "-" + j).hide();
        $(btn).show();
        var respuesta = JSON.parse(serveletResponse);
        if (respuesta.code == 0) {
            $("#row-" + i + "-" + j).remove();
            alert("La publicación se ha aceptado correctamente!");
        }
        else {
            alert("Error: " + respuesta.code + " - Descripción: " + respuesta.description);
        }
    });
}

function getTableHead(i) {
    var mText = "";
    mText += "<thead>";
        mText += "<tr>";
            mText += "<th>Título</th>";
            mText += "<th>Tipo</th>";
            mText += "<th>ISBN o ISSN</th>";
            mText += "<th>Lugar</th>";
            mText += "<th>Editorial</th>";
            mText += "<th>Fecha</th>";
            mText += "<th>Estado</th>"
        mText += "</tr>";
    mText += "</thead>";
    mText += "<tbody id='bodyTable" + i + "'>";
    mText += "</tbody>";
    
    return mText;
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

function goHome(u){
    alert("Nos vamos dice el boton; " + u);
    window.location.href='../Vista/principal.html';
}

function getServelet(url, data, param, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            return callback(xhr.responseText);
        }
        else if(xhr.readyState == 4 && xhr.status != 200) {
            return callback({code:"9997", description:"Imposible obtener respuestan de servidor"});
        }
    };
    xhr.open('GET', url + "?" + param, true);
    xhr.send(data);
}