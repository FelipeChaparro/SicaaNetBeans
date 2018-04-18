window.onload = function(){ 
     
    var json= JSON.parse(sessionStorage.getItem("principal"));
    var nombre = document.getElementById("nombre");
    nombre.innerHTML =json.datosBasicos.nombre;
}

function modificarInfo(){
    var dib = document.getElementById("publicacion_1");
    dib.innerHTML = "<input type='text' class='form-control' id='nombrePublicacion' value='ASHYI Plataforma basada en agentes para la planificación dinámica, inteligente y adaptativa de actividades aplicada a la educación personalizada'>";
}

function goHome(){
    window.location.href='../Vista/principal.html';
}