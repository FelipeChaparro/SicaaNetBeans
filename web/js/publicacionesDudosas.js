//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNBGIT/";

window.onload = function(){ 
     var json = JSON.parse(sessionStorage.getItem("principal"));
    //var nombre = document.getElementById("nombre");
    //var departamento = document.getElementById("departamento");
    //departamento.innerHTML = json.datosBasicos.nombreDepartamento;
    //nombre.innerHTML =json.datosBasicos.nombre;
    //var token = json.token;
    //var user_login_data = "token=" + token;
    var params = json.token;
    document.getElementById("nombre").innerHTML = "Alexandra Pomares Quimbaya";
    document.getElementById("departamento").innerHTML = "Ingeniería de Sistemas";
    getServelet(SERVER_URL+'PublicacionDudosaServlet',null, params, function(serveletResponse) {
	   //text.innerHTML = serveletResponse;
	    respuesta=JSON.parse(serveletResponse);
           // alert(respuesta);
             if (respuesta.publicaciones.length == 0) {
                alert("No tienes publicaciones pendientes");
            }
            else {
                for (var i = 0; i < respuesta.publicaciones.length; i++) {
                    var t = document.createElement("table");
                    t.className = "table table-bordered";
                    t.innerHTML = getTableHead(i);
                    document.getElementById("tablasDudosas").appendChild(t);
                    var btn = document.createElement("button");
                    btn.className = "btn btn-primary btn-block"; btn.type = "button"; btn.innerHTML = "Guardar";
                    document.getElementById("tablasDudosas").appendChild(btn);
                    btn = document.createElement("button");
                    btn.className = "btn btn-danger btn-block"; btn.type = "button"; btn.innerHTML = "Cancelar";
                    btn.setAttribute("onclick","goHome(" + i + ")");
                    document.getElementById("tablasDudosas").appendChild(btn);

                    for (var j = 0; j < respuesta.publicaciones[i].publicacion.length; j++) {
                        var row = document.createElement("tr");
                        row.innerHTML = "<td>\
                                            <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].Titulo + "</label>\
                                            <input type='radio' name='titulo" + i + "' id='titulo_" + i + "_" + j + "'>\
                                        </td>\
                                        <td>\
                                            <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].Tipo + "</label>\
                                            <input type='radio' name='tipo" + i + "' id='tipo_" + i + "_" + j + "'>\
                                        </td>\
                                        <td>\
                                            <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].ISBN + "</label>\
                                            <input type='radio' name='isbn" + i + "' id='isbn_" + i + "_" + j + "'>\
                                        </td>\
                                        <td>\
                                            <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].ISSN + "</label>\
                                            <input type='radio' name='issn" + i + "' id='issn_" + i + "_" + j + "'>\
                                        </td>\
                                        <td>\
                                            <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].Lugar + "</label>\
                                            <input type='radio' name='lugar" + i + "' id='lugar_" + i + "_" + j + "'>\
                                        </td>\
                                        <td>\
                                            <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].Editorial + "</label>\
                                            <input type='radio' name='editorial" + i + "' id='editorial_" + i + "_" + j + "'>\
                                        </td>\
                                        <td>\
                                            <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].FechaInicio + "</label>\
                                            <input type='radio' name='fecha" + i + "' id='fecha_" + i + "_" + j + "'>\
                                        </td>";
                        document.getElementById("bodyTable"+i).appendChild(row);
                    }
                }
              }
     });
    /*var respuesta = {
        "code":0,
        "description":"Operacion exitosa",
        "token":"23456789",
        "publicaciones":[
            {
                "estadoPublicacion":"ESPERAOTROS",
                "publicacion":[
                    {
                        "Titulo":"Bases de datos V1",
                        "Tipo":"Articulo",
                        "ISBN":"1234",
                        "ISSN":"1234",
                        "Lugar":"Colombia",
                        "Editorial":"Editorial javeriana",
                        "FechaInicio":"05-2018",
                        "Extraido":"Cvlac",
                        "userId":"12",
                        "publicacionID":"6",
                        "publicacionDudosaID":"25"
                    },
                    {
                        "Titulo":"Base de datos volumen 1",
                        "Tipo":"Articulo",
                        "ISBN":"1234",
                        "ISSN":"1234",
                        "Lugar":"Colombia",
                        "Editorial":"Editorial javeriana",
                        "FechaInicio":"05-2018",
                        "Extraido":"Cvlac",
                        "userId":"16",
                        "publicacionID":"6",
                        "publicacionDudosaID":"56"
                    },
                    {
                        "Titulo":"Base de datos Vol. 1",
                        "Tipo":"Articulo",
                        "ISBN":"",
                        "ISSN":"",
                        "Lugar":"Colo",
                        "Editorial":"Editorial javeriana",
                        "FechaInicio":"Mayo 2018",
                        "Extraido":"Research",
                        "userId":"17",
                        "publicacionID":"6",
                        "publicacionDudosaID":"78"
                    }
                ]
            },
            {
                "estadoPublicacion":"ESPERAOTROS",
                "publicacion":[
                    {
                        "Titulo":"Bases de datos V2",
                        "Tipo":"Articulo",
                        "ISBN":"1234",
                        "ISSN":"1234",
                        "Lugar":"Colombia",
                        "Editorial":"Editorial javeriana",
                        "FechaInicio":"05-2018",
                        "Extraido":"Cvlac",
                        "userId":"12",
                        "publicacionID":"7",
                        "publicacionDudosaID":"26"
                    },
                    {
                        "Titulo":"Base de datos volumen 2",
                        "Tipo":"Articulo",
                        "ISBN":"1234",
                        "ISSN":"1234",
                        "Lugar":"Colombia",
                        "Editorial":"Editorial javeriana",
                        "FechaInicio":"05-2018",
                        "Extraido":"Cvlac",
                        "userId":"16",
                        "publicacionID":"7",
                        "publicacionDudosaID":"57"
                    },
                    {
                        "Titulo":"Base de datos Vol. 2",
                        "Tipo":"Articulo",
                        "ISBN":"1234",
                        "ISSN":"1234",
                        "Lugar":"Colo",
                        "Editorial":"",
                        "FechaInicio":"Mayo 2018",
                        "Extraido":"Research",
                        "userId":"17",
                        "publicacionID":"7",
                        "publicacionDudosaID":"79"
                    }
                ]
            },
            {
                "estadoPublicacion":"ESPERAOTROS",
                "publicacion":[
                    {
                        "Titulo":"Bases de datos V3",
                        "Tipo":"Articulo",
                        "ISBN":"1234",
                        "ISSN":"1234",
                        "Lugar":"Colombia",
                        "Editorial":"Editorial javeriana",
                        "FechaInicio":"05-2018",
                        "Extraido":"Cvlac",
                        "userId":"12",
                        "publicacionID":"8",
                        "publicacionDudosaID":"30"
                    },
                    {
                        "Titulo":"Base de datos volumen 3",
                        "Tipo":"Articulo",
                        "ISBN":"1234",
                        "ISSN":"1234",
                        "Lugar":"",
                        "Editorial":"Editorial javeriana",
                        "FechaInicio":"05-2018",
                        "Extraido":"Cvlac",
                        "userId":"45",
                        "publicacionID":"8",
                        "publicacionDudosaID":"108"
                    },
                    {
                        "Titulo":"Base de datos volunen tres",
                        "Tipo":"Conferencia",
                        "ISBN":"1234",
                        "ISSN":"1234",
                        "Lugar":"Colomby",
                        "Editorial":"Editorial javerianca",
                        "FechaInicio":"05-2018",
                        "Extraido":"Cvlacc",
                        "userId":"45",
                        "publicacionID":"8",
                        "publicacionDudosaID":"108"
                    }
                ]
            }
        ]
    }

	
    if (respuesta.publicaciones.length == 0) {
        alert("No tienes publicaciones pendientes");
    }
    else {
        for (var i = 0; i < respuesta.publicaciones.length; i++) {
            var t = document.createElement("table");
            t.className = "table table-bordered";
            t.innerHTML = getTableHead(i);
            document.getElementById("tablasDudosas").appendChild(t);
            var btn = document.createElement("button");
            btn.className = "btn btn-primary btn-block"; btn.type = "button"; btn.innerHTML = "Guardar";
            document.getElementById("tablasDudosas").appendChild(btn);
            btn = document.createElement("button");
            btn.className = "btn btn-danger btn-block"; btn.type = "button"; btn.innerHTML = "Cancelar";
            btn.setAttribute("onclick","goHome(" + i + ")");
            document.getElementById("tablasDudosas").appendChild(btn);

            for (var j = 0; j < respuesta.publicaciones[i].publicacion.length; j++) {
                var row = document.createElement("tr");
                row.innerHTML = "<td>\
                                    <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].Titulo + "</label>\
                                    <input type='radio' name='titulo" + i + "' id='titulo_" + i + "_" + j + "'>\
                                </td>\
                                <td>\
                                    <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].Tipo + "</label>\
                                    <input type='radio' name='tipo" + i + "' id='tipo_" + i + "_" + j + "'>\
                                </td>\
                                <td>\
                                    <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].ISBN + "</label>\
                                    <input type='radio' name='isbn" + i + "' id='isbn_" + i + "_" + j + "'>\
                                </td>\
                                <td>\
                                    <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].ISSN + "</label>\
                                    <input type='radio' name='issn" + i + "' id='issn_" + i + "_" + j + "'>\
                                </td>\
                                <td>\
                                    <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].Lugar + "</label>\
                                    <input type='radio' name='lugar" + i + "' id='lugar_" + i + "_" + j + "'>\
                                </td>\
                                <td>\
                                    <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].Editorial + "</label>\
                                    <input type='radio' name='editorial" + i + "' id='editorial_" + i + "_" + j + "'>\
                                </td>\
                                <td>\
                                    <label class='nextRB'>" + respuesta.publicaciones[i].publicacion[j].FechaInicio + "</label>\
                                    <input type='radio' name='fecha" + i + "' id='fecha_" + i + "_" + j + "'>\
                                </td>";
                document.getElementById("bodyTable"+i).appendChild(row);
            }
        }
    }*/
}

function getTableHead(i) {
    return "<thead>\
                  <tr>\
                    <th>Titulo</th>\
                    <th>Tipo</th>\
                    <th>ISBN</th>\
                    <th>ISSN</th>\
                    <th>Lugar</th>\
                    <th>Editorial</th>\
                    <th>Fecha Inicio</th>\
                  </tr>\
                </thead>\
                <tbody id='bodyTable" + i + "'>\
                </tbody>";
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
            return callback({code:"9997",description:"Imposible obtener respuestan de servidor"});
        }
    };
    xhr.open('GET', url+"?"+param, true);
    xhr.send(data);
}