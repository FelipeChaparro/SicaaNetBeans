window.onload = function(){ 
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    // User info
    document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
    document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
    
    //Basic information
    document.getElementById("facultad").innerHTML += SESSION.datosBasicos.nombreFacultad;
    document.getElementById("categoria").innerHTML += SESSION.datosBasicos.categoria;
    document.getElementById("nacionalidad").innerHTML += SESSION.datosBasicos.nacionalidad;
    
    // Areas de Actuación
    for (var i = 0; i < SESSION.areasActuacion.length; i++) {
        var mItem = document.createElement("li");
        mItem.innerHTML = "<h6>" + SESSION.areasActuacion[i].nombre + "</h6>"
        document.getElementById("list-areas").appendChild(mItem);
    }

    //Formacion Académica
    for (var i = 0; i < SESSION.formacionAcademica.length; i++) {                           
        var midiv = document.createElement("div");
        midiv.className = "item";
        midiv.innerHTML = "<h4 class='title'><i class='fa fa-graduation-cap'></i> " + SESSION.formacionAcademica[i].titulo + "</h4>" + "<h6 class='university'>" + SESSION.formacionAcademica[i].universidad + "<span class='year'> ( " + SESSION.formacionAcademica[i].fechaInicio + " - " + SESSION.formacionAcademica[i].fechaFin+ " ) </span></h6>";     
        document.getElementById("formacionAca").appendChild(midiv); 
     }
    
    //Publicaciones Recientes
    for(var i = 0; i < SESSION.publicacionesRecientes.length; i++){  
        var midiv = document.createElement("div");
        midiv.className = "item row";    
        midiv.innerHTML ="<h3 class='title'>" + SESSION.publicacionesRecientes[i].titulo + "</h3>" + ((SESSION.publicacionesRecientes[i].fechaInicio == null) ? "" : "<p>" + SESSION.publicacionesRecientes[i].fechaInicio + "</p>");
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
            myMedal.innerHTML = "<img class='medalla' src='../assets/images/medallas/m" + SESSION.medallas[i].nombreMedalla +".png'></>" + "<p>" + SESSION.medallas[i].nombreMedalla + "</p>";
        else
            myMedal.innerHTML = "<img class='medalla' src='../assets/images/medallas/noMedalla.png'></>" + "<p>" + SESSION.medallas[i].nombreMedalla + "</p>";   
        document.getElementById("medallasProfesor").appendChild(myMedal); 
    }
}
