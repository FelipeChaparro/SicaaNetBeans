window.onload = function(){ 
    var json= JSON.parse(sessionStorage.getItem("principal"));
    //alert(sessionStorage.getItem("principal"));
    var nombre = document.getElementById("nombre");
    var categoria = document.getElementById("categoria");
    var nombreUniversidad = document.getElementById("nombreUniversidad");
    var nacionalidad = document.getElementById("nacionalidad");
     nombre.innerHTML =json.datosBasicos.nombre;
     categoria.innerHTML = "Categoria : "+ json.datosBasicos.categoria;
     nombreUniversidad.innerHTML ="Universidad : "+json.datosBasicos.nombreUniversidad;
     nacionalidad.innerHTML ="Nacionalidad : "+json.datosBasicos.nacionalidad;
                            
     for(var i=0; i<json.formacionAcademica.length; i++){                           
     var midiv = document.createElement("div");
        
      midiv.innerHTML = "<h4 class='title'><i class='fa fa-graduation-cap'></i>"+json.formacionAcademica[i].titulo+"</h4>"+
                        "<h6 class='university'>"+json.formacionAcademica[i].universidad +"<span class='year'>("+json.formacionAcademica[i].fechaInicio+"-"+json.formacionAcademica[i].fechaFin+")</span></h6>"
            
     document.getElementById("formacionAca").appendChild(midiv); // Lo pones en "body", si quieres ponerlo dentro de algún id en concreto usas document.getElementById('donde lo quiero poner').appendChild(midiv);
     }
     for(var i=0; i<json.publicacionesRecientes.length; i++){  
       var midiv = document.createElement("div");
        
         midiv.innerHTML ="<h4 class='title'><a href='#' target='_blank'>"+json.publicacionesRecientes[i].titulo+"</a></h4>"+
                          "<p>"+json.publicacionesRecientes[i].fechaInicio+"</p>";               
                           
         document.getElementById("ultimasPublicaciones").appendChild(midiv);      
     }
     
     
     

}

function actualizarInfo(){
    
    window.location.href='../Vista/actualizarinformacion.html';
}