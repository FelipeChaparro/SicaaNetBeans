window.onload = function(){ 
    
    document.getElementById("nombre").innerHTML = "Alexandra Pomares Quimbaya";
    document.getElementById("departamento").innerHTML = "Ingeniería de Sistemas";
    
    var json= JSON.parse(sessionStorage.getItem("principal"));
    //alert(sessionStorage.getItem("principal"));
    var nombre = document.getElementById("nombre");
    var categoria = document.getElementById("categoria");
    var nombreUniversidad = document.getElementById("nombreUniversidad");
    var departamento = document.getElementById("departamento");
    departamento.innerHTML = json.datosBasicos.nombreDepartamento;
    var nacionalidad = document.getElementById("nacionalidad");
     nombre.innerHTML =json.datosBasicos.nombre;
     categoria.innerHTML = "Categoria : "+ json.datosBasicos.categoria;
     nombreUniversidad.innerHTML ="Universidad : "+json.datosBasicos.nombreUniversidad;
     nacionalidad.innerHTML ="Nacionalidad : "+json.datosBasicos.nacionalidad;
                            
     for(var i=0; i<json.formacionAcademica.length; i++){                           
        var midiv = document.createElement("div");
        midiv.className = "item";
        midiv.innerHTML = "<h4 class='title'><i class='fa fa-graduation-cap'></i>"+json.formacionAcademica[i].titulo+"</h4>"+
                        "<h6 class='university'>"+json.formacionAcademica[i].universidad +"<span class='year'> ("+json.formacionAcademica[i].fechaInicio+"-"+json.formacionAcademica[i].fechaFin+")</span></h6>"
            
        document.getElementById("formacionAca").appendChild(midiv); // Lo pones en "body", si quieres ponerlo dentro de algún id en concreto usas document.getElementById('donde lo quiero poner').appendChild(midiv);
     }
     for(var i=0; i<json.publicacionesRecientes.length; i++){  
       var midiv = document.createElement("div");
         midiv.className = "item row";    
         midiv.innerHTML ="<h3 class='title'><a href='#' target='_blank'>"+json.publicacionesRecientes[i].titulo+"</a></h3>"+
                          "<p>"+json.publicacionesRecientes[i].fechaInicio+"</p>";
         document.getElementById("ultimasPublicaciones").appendChild(midiv);      
     }
     
     for(var i = 0; i < json.podioPublicacionUniversidad.length; i++) {
         var miRow = document.createElement("tr");
         miRow.innerHTML = "<td>"+ (i+1) +"</td> <td> <a href='#'>"+json.podioPublicacionUniversidad[i].nombre+"</a> <td>"+json.podioPublicacionUniversidad[i].puntos+"</td> <td>"+json.podioPublicacionUniversidad[i].facultad+"</td>";
         document.getElementById("podioUniversidad").appendChild(miRow);  
     }
     
     for(var i = 0; i < json.podioPublicacionFacultad.length; i++) {
         var miRow = document.createElement("tr");
         miRow.innerHTML = "<td>"+ (i+1) +"</td> <td> <a href='#'>"+json.podioPublicacionFacultad[i].nombre+"</a> <td>"+json.podioPublicacionFacultad[i].puntos+"</td> <td>"+json.podioPublicacionFacultad[i].facultad+"</td>";
         document.getElementById("podioFacultad").appendChild(miRow);  
     }
     
     for(var i = 0; i < json.podioPublicacionPrograma.length; i++) {
         var miRow = document.createElement("tr");
         miRow.innerHTML = "<td>"+ (i+1) +"</td> <td> <a href='#'>"+json.podioPublicacionPrograma[i].nombre+"</a> <td>"+json.podioPublicacionPrograma[i].puntos+"</td> <td>"+json.podioPublicacionPrograma[i].programa+"</td>";
         document.getElementById("podioPrograma").appendChild(miRow);  
     }
     
     for(var i = 0; i < json.medallas.length; i++) {
         var myMedal = document.createElement("div");
         myMedal.className = "item";
        if (json.medallas[i].acomplished == "1")
            myMedal.innerHTML = "<img class='medalla' src='../assets/images/medallas/m"+json.medallas[i].nombreMedalla +".png'></>" + "<p>" + json.medallas[i].nombreMedalla + "</p>";
        else
            myMedal.innerHTML = "<img class='medalla' src='../assets/images/medallas/noMedalla.png'></>" + "<p>" + json.medallas[i].nombreMedalla + "</p>";     
        document.getElementById("medallasProfesor").appendChild(myMedal); 
     }
}
