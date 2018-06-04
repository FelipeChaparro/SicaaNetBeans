var SERVER_URL = "https://sicaadev.mybluemix.net/";
//var SERVER_URL = "http://localhost:8080/SicaaNetBeans-dev/";

window.onload = function(){
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    console.log(SESSION);
    if (SESSION == null) {
        window.location.href = SERVER_URL;
    } 
    else {
        
        if (SESSION.firstTime) {
            SESSION.firstTime = false;
            sessionStorage.setItem("principal", JSON.stringify(SESSION));
            showMeAllInformation(SESSION);
            $('[data-toggle="popover"]').popover(); 
        }
        else {
            var user_login_data = {
                "token":SESSION.token,
                "server_url":SERVER_URL,
                "action":"LOAD_INFORMACION_BASICA"
            };
            $("#loader-home").show();
            postServelet(SERVER_URL + "login_servelet", JSON.stringify(user_login_data), function(serveletResponse) {
                $("#loader-home").hide();
                var respuesta = JSON.parse(serveletResponse);
                
                if (respuesta.code === 0) {
                    respuesta.firstTime = false;
                    sessionStorage.setItem("principal", JSON.stringify(respuesta));
                    showMeAllInformation(respuesta);
                    
                    $('[data-toggle="popover"]').popover(); 
                    
                }
                else {
                    alert("Error c\xf3digo: " + respuesta.code.toString() + " - Descripci\xf3n: " + respuesta.description);
                }
            });
            //showMeAllInformation(SESSION);
        }
    }
}

function showMeAllInformation(SESSION) {
    // Cargar Nav-bar
    cargarNavBar(SESSION.roles);

    // User info
    document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
    document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
    document.getElementById("facultad").innerHTML += SESSION.datosBasicos.nombreFacultad;

    //Basic information
    document.getElementById("categoria").innerHTML += SESSION.datosBasicos.categoria;
    document.getElementById("puntaje-profesor").innerHTML = ((SESSION.puntosTotales == null) ?
                                                             0 : 
                                                             SESSION.puntosTotales);

    // Cargar Imagen
    cargarImagen(SESSION.datosBasicos);

    // Areas de Actuación
    for (var i = 0; i < SESSION.areasActuacion.length; i++) {
        var mItem = document.createElement("li");
        mItem.innerHTML = "<h4 style='font-weight:initial;font-size: 15px;'>" + SESSION.areasActuacion[i].nombre + "</h4>"
        document.getElementById("list-areas").appendChild(mItem);
    }

    //Formacion Académica
    for (var i = 0; i < SESSION.formacionAcademica.length; i++) {                           
        var midiv = document.createElement("div");
        midiv.className = "item";
        midiv.innerHTML = "<h4 class='title'><i class='fa fa-graduation-cap'></i> " + SESSION.formacionAcademica[i].titulo + "</h4>" + "<h6 style='font-weight:initial;' class='university'>" + SESSION.formacionAcademica[i].universidad + "<span class='year'> (" + SESSION.formacionAcademica[i].fechaInicio + " - " + SESSION.formacionAcademica[i].fechaFin+ ")</span></h6>";     
        document.getElementById("formacionAca").appendChild(midiv); 
     }

    //Publicaciones Recientes
    for(var i = 0; i < SESSION.publicacionesRecientes.length; i++){  
        var midiv = document.createElement("div");
        midiv.className = "item row";    
        midiv.innerHTML ="<h3 style='font-weight:initial;margin-left: 15px;font-size: 15px;' class='title'><span style='color:#000000'>Título: </span>" + SESSION.publicacionesRecientes[i].titulo + "</h3>";
        document.getElementById("ultimasPublicaciones").appendChild(midiv);      
    }

    // Información de medallas
    for(var i = 0; i < SESSION.medallas.length; i++) {
        var myMedal = document.createElement("div");
        var iSource = "";
        myMedal.className = "item";
        myMedal.style.margin = "10px";
        myMedal.style.display = "inline-block";
        if (SESSION.medallas[i].acomplished == "1")
            iSource = "../assets/images/medallas/m" + SESSION.medallas[i].nombreMedalla + ".png";
        else
            iSource = "../assets/images/medallas/noMedalla.png";
        
        myMedal.innerHTML = "<a data-toggle='popover' data-placement='top' " 
                            + "title='" + SESSION.medallas[i].nombreMedalla + "' "
                            + "data-content='" + getInfoMedalla(SESSION.medallas[i].nombreMedalla) + "' "
                            + "style='cursor:pointer;'>"
                                + "<img width='150px'"
                                + "src='" + iSource + "'>"
                            +"</a>";
        
        document.getElementById("medallasProfesor").appendChild(myMedal); 
    }
    
    // Charts
    createChart("myChart-universidad", 
                getNombres(SESSION.podioPublicacionUniversidad, false), 
                getPuntos(SESSION.podioPublicacionUniversidad), 
                "rgba(247, 220, 27, 0.8)");

    createChart("myChart-facultad",
                getNombres(SESSION.podioPublicacionFacultad, false), 
                getPuntos(SESSION.podioPublicacionFacultad),
                "rgba(54, 162, 235, 0.8)");
    
    
    createChart("myChart-departamento",
                getNombres(SESSION.podioPublicacionPrograma, true), 
                getPuntos(SESSION.podioPublicacionPrograma),
                "rgba(231, 76, 60, 0.8)");
    
}

function cerrarSesion() {
    sessionStorage.clear();
    window.location.href = SERVER_URL   ;
}

function getNombres(names, paramDpto) {
    var newArray = [];
    var allName = "";
    
    if (names.length == 1) {
        allName = getNewName(names[0].nombre);
        if (paramDpto)
            allName += "(" + names[0].programa + ")";
        else 
            allName += "(" + names[0].facultad + ")";
        newArray.push(allName);
    }
    
    if (names.length == 2) {
        allName = getNewName(names[1].nombre);
        if (paramDpto)
            allName += "(" + names[1].programa + ")";
        else 
            allName += "(" + names[1].facultad + ")";
        newArray.push(allName);
        
        allName = getNewName(names[0].nombre);
        if (paramDpto)
            allName += "(" + names[0].programa + ")";
        else 
            allName += "(" + names[0].facultad + ")";
        newArray.push(allName);
    }
    
    if (names.length == 3) {
        allName = getNewName(names[1].nombre);
        if (paramDpto)
            allName += "(" + names[1].programa + ")";
        else 
            allName += "(" + names[1].facultad + ")";
        newArray.push(allName);
        
        allName = getNewName(names[0].nombre);
        if (paramDpto)
            allName += "(" + names[0].programa + ")";
        else 
            allName += "(" + names[0].facultad + ")";
        newArray.push(allName);
        
        allName = getNewName(names[2].nombre);
        if (paramDpto)
            allName += "(" + names[2].programa + ")";
        else 
            allName += "(" + names[2].facultad + ")";
        newArray.push(allName);
    }
    
    return newArray;
}

function getNewName(name) {
    var newName = "";
    var auxName = name.trim().split(" ");
    
    if (auxName.length == 4) newName = auxName[0] + auxName[2];
    if (auxName.length == 3) newName = auxName[0] + auxName[1];
    if (auxName.length <= 2) newName = auxName.join(" ");
    
    return newName;
}

function getPuntos(puntos) {
    var newArray = [];
    
    if (puntos.length == 1) {
        newArray.push(puntos[0].puntos);
    }
    
    if (puntos.length == 2) {
        newArray.push(puntos[1].puntos);
        newArray.push(puntos[0].puntos);
    }
    
    if (puntos.length == 3) {
        newArray.push(puntos[1].puntos);
        newArray.push(puntos[0].puntos);
        newArray.push(puntos[2].puntos);
    }
    
    return newArray;
}

function createChart(idCanvas, names, points, mBarColor) {
    var ctx = document.getElementById(idCanvas).getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: names,
            datasets: [{
                data: points,
                label:"Javepoints",
                backgroundColor: mBarColor
            }],
        },
        options: {
            legend: {display:false},
            scales: {
                yAxes: [{
                    display: false,
                    ticks: {
                        beginAtZero:true
                    }
                }],
                xAxes: [{
                    display: false,
                    barPercentage: 0.95,
				    categoryPercentage:1,
                }]
            }
        }
    });
}

function getInfoMedalla(name) {
    var info = "";
    if (name == "Bilingue") info = "Se otorga por dominar 2 o más idiomas.";
    if (name == "Cientifico") info = "Se otorga por avanzar en la actividad científica, cultural o académica.";
    if (name == "Director") info = "Se otorga por ser director de un trabajo de grado reconocido.";
    if (name == "Doctor") info = "Se otorga por tener doctorado.";
    if (name == "Investigador") info = "Se otorga por ser investigador.";
    if (name == "Administrativo") info = "Se otorga por participar en labores administrativas del programa.";
    if (name == "Jefe") info = "Se otorga por ser jefe de sección.";
    return info;
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

function cargarNavBar(obj_roles) {
    var mNavbar = document.getElementById("navbar-sicaa");
    var mDropDownPublicaciones = false;
    var mDropDownConsultas = false;
    
    for (var i = 0; i < obj_roles.length; i++) {
        if (obj_roles[i].id == 1) {
            insertIntoNavbar("navbar-sicaa", obj_roles[i].label, obj_roles[i].referencia, "active");
        }
        if (obj_roles[i].id >= 2 && obj_roles[i].id <= 5) {
            if (mDropDownPublicaciones == false) {
                mNavbar.appendChild(createDrowndown("Producción Intelectual", "dropmenu-publicaciones", ""));
                mDropDownPublicaciones = true;
            }
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

function postServelet(url, data, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('post', url, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            return callback(xhr.responseText);
        }
    }
    xhr.send(data);
}
