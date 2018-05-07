window.onload = function(){ 
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));

    // Cargar Nav-bar
    cargarNavBar(SESSION.roles);
    
    // User info
    document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
    document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
    document.getElementById("facultad").innerHTML += SESSION.datosBasicos.nombreFacultad;
    
    //Basic information
    document.getElementById("categoria").innerHTML += SESSION.datosBasicos.categoria;
    
    // Areas de Actuación
    for (var i = 0; i < SESSION.areasActuacion.length; i++) {
        var mItem = document.createElement("li");
        mItem.innerHTML = "<h4 style='font-weight:initial'>" + SESSION.areasActuacion[i].nombre + "</h4>"
        document.getElementById("list-areas").appendChild(mItem);
    }

    //Formacion Académica
    for (var i = 0; i < SESSION.formacionAcademica.length; i++) {                           
        var midiv = document.createElement("div");
        midiv.className = "item";
        midiv.innerHTML = "<h4 class='title'><i class='fa fa-graduation-cap'></i> " + SESSION.formacionAcademica[i].titulo + "</h4>" + "<h6 style='font-weight:initial;' class='university'>" + SESSION.formacionAcademica[i].universidad + "<span class='year'> ( " + SESSION.formacionAcademica[i].fechaInicio + " - " + SESSION.formacionAcademica[i].fechaFin+ " ) </span></h6>";     
        document.getElementById("formacionAca").appendChild(midiv); 
     }
    
    //Publicaciones Recientes
    for(var i = 0; i < SESSION.publicacionesRecientes.length; i++){  
        var midiv = document.createElement("div");
        midiv.className = "item row";    
        midiv.innerHTML ="<h3 style='font-weight:initial;margin-left: 15px;' class='title'>" + SESSION.publicacionesRecientes[i].titulo + "</h3>" + ((SESSION.publicacionesRecientes[i].fechaInicio == null) ? "" : "<p>" + SESSION.publicacionesRecientes[i].fechaInicio + "</p>");
        document.getElementById("ultimasPublicaciones").appendChild(midiv);      
    }
    
    // Podio - Publicaciones universidad
    for(var i = 0; i < SESSION.podioPublicacionUniversidad.length; i++) {
        var miRow = document.createElement("tr");
        miRow.innerHTML = "<td>" + (i+1) + "</td> <td> <a>" + SESSION.podioPublicacionUniversidad[i].nombre + "</a> <td>"+SESSION.podioPublicacionUniversidad[i].puntos+"</td> <td>" + SESSION.podioPublicacionUniversidad[i].facultad + "</td>";
        document.getElementById("podioUniversidad").appendChild(miRow);  
    }

    // Podio - Publicaciones universidad
     for(var i = 0; i < SESSION.podioPublicacionFacultad.length; i++) {
         var miRow = document.createElement("tr");
         miRow.innerHTML = "<td>"+ (i+1) +"</td> <td> <a>"+SESSION.podioPublicacionFacultad[i].nombre+"</a> <td>"+SESSION.podioPublicacionFacultad[i].puntos+"</td> <td>"+SESSION.podioPublicacionFacultad[i].facultad+"</td>";
         document.getElementById("podioFacultad").appendChild(miRow);  
     }

    // Podio - Publicaciones universidad
    for(var i = 0; i < SESSION.podioPublicacionPrograma.length; i++) {
        var miRow = document.createElement("tr");
        miRow.innerHTML = "<td>"+ (i+1) +"</td> <td> <a>"+SESSION.podioPublicacionPrograma[i].nombre+"</a> <td>"+SESSION.podioPublicacionPrograma[i].puntos+"</td> <td>"+SESSION.podioPublicacionPrograma[i].programa+"</td>";
        document.getElementById("podioPrograma").appendChild(miRow);  
    }
    
    // Información de medallas
    for(var i = 0; i < SESSION.medallas.length; i++) {
        var myMedal = document.createElement("div");
        myMedal.className = "item";
        if (SESSION.medallas[i].acomplished == "1")
            myMedal.innerHTML = "<img class='medalla' src='../assets/images/medallas/m" + SESSION.medallas[i].nombreMedalla +".png'></>" + "<p style='margin-left: 10px;'>" + SESSION.medallas[i].nombreMedalla + "</p>";
        else
            myMedal.innerHTML = "<img class='medalla' src='../assets/images/medallas/noMedalla.png'></>" + "<p style='margin-left: 10px;'>" + SESSION.medallas[i].nombreMedalla + "</p>";   
        document.getElementById("medallasProfesor").appendChild(myMedal); 
    }
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
