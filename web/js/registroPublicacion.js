window.onload = function(){ 
     
    var json= JSON.parse(sessionStorage.getItem("principal"));
    var nombre = document.getElementById("nombre");
    nombre.innerHTML =json.datosBasicos.nombre;
}

function guardarInfo(){
    
}