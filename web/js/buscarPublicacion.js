//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-master/";

window.onload = function(){
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
    document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
    document.getElementById("facultad").innerHTML = SESSION.datosBasicos.nombreFacultad;
    // Cargar Nav-bar
    cargarNavBar(SESSION.roles, "Filtrar Publicaciones");
}

function buscar(){
    
    var data_to_send = new Object();
    var Rol = [];
    var Tipo = [];
    var fechaValida = true;
    var autorName = document.getElementById("autor-name").value
    var dpName = document.getElementById("dp-name").value
    var roles = document.getElementsByClassName("cb-rol");
    var tipos = document.getElementsByClassName("cb-tipo");
    var fInicio = document.getElementById("fecha-inicio").value;
    var fFin = document.getElementById("fecha-fin").value;
    
    for (var i = 0; i < roles.length; i++) {
        if (roles[i].checked)
            Rol.push(roles[i].getAttribute("name"));
    }
    for (var i = 0; i < tipos.length; i++) {
        if (tipos[i].checked) 
            Tipo.push(tipos[i].getAttribute("name"));
    }
    
    if (dpName == "Sin especificar") dpName = null;
    if (autorName == "") autorName = null;
    if (fInicio == "") fInicio = null;
    if (fFin == "") fFin = null;
    
    if (fInicio == null && fFin != null) fechaValida = false;
    if (fInicio != null && fFin == null) fechaValida = false;
    
    if (fechaValida) {
        data_to_send.Nombre = autorName;
        data_to_send.Rol = Rol;
        data_to_send.Tipo = Tipo;
        data_to_send.Departamento = dpName;
        data_to_send.FechaInicio = fInicio;
        data_to_send.FechaFin = fFin;
        
        postServlet(SERVER_URL + "ConsultarPublicacion", JSON.stringify(data_to_send), function(serveletResponse) {
            var obj1 = JSON.parse(serveletResponse);
           
         var publicaciones=obj1.publicaciones;
               
         var table = document.getElementById("tabla");
         table.innerHTML = "";
         
            var header = table.createTHead();
            var row = header.insertRow(0);
            var cell = row.insertCell(0);
            cell.innerHTML = "<b> Titulo</b>";
            cell = row.insertCell(1);
            cell.innerHTML = "<b> Editorial</b>";
            cell = row.insertCell(2);
            cell.innerHTML = "<b> Fecha </b>";
            cell = row.insertCell(3);
            cell.innerHTML = "<b> Lugar de publicación </b>";
            cell = row.insertCell(4);
            cell.innerHTML = "<b> Código publicación </b>";
            cell = row.insertCell(5);
            cell.innerHTML = "<b> Tipo </b>";
         for(var i=0; i< publicaciones.length; i++){
                    var numRow=table.rows.length;
                    var row = table.insertRow(numRow);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                    var cell6 = row.insertCell(5);
                  
                    cell1.innerHTML = publicaciones[i].titulo;
                    cell2.innerHTML = publicaciones[i].editorial;
                    cell3.innerHTML = publicaciones[i].fecha;
                    cell4.innerHTML = publicaciones[i].lugar;
                    cell5.innerHTML = publicaciones[i].codigoPublicacion;
                    cell6.innerHTML = publicaciones[i].tipo;
                   
         }
           
        });
      }
        else {
        alert("Debes ingresar fecha desde y fecha hasta!");
      }
    
	/*postServelet(SERVER_URL+"ConsultarPublicacion",myJSON,function(serveletResponse) {
        var obj1 = JSON.parse(serveletResponse);
         var publicaciones=obj1.publicaciones;
                
         var table = document.getElementById("tabla");
         for(var i=0; i< publicaciones.length; i++){
                    var numRow=table.rows.length;
                    var row = table.insertRow(numRow);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                    var cell6 = row.insertCell(5);
                    var cell7 = row.insertCell(6);
                    cell1.innerHTML = publicaciones[i].titulo;
                    cell2.innerHTML = publicaciones[i].lugar;
                    cell3.innerHTML = publicaciones[i].ISSN;
                    cell4.innerHTML = publicaciones[i].ISBN;
                    cell5.innerHTML = publicaciones[i].fecha;
                    cell6.innerHTML = publicaciones[i].editorial;
                    cell7.innerHTML = publicaciones[i].tipo;
         }
        //alert(serveletResponse);
     //respuesta=serveletResponse;
        console.log("respondio");
        });*/
		

}
/**
 * Funcion para consultar un servelet del backend
 * @param url URL del servelet
 * @param data Query con los datos de la consulta
 * @param callback Funcion callback que ejecuta la respuesta
 * @returns
 */
function getServelet(url,data,param,callback) {
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
        	return callback(xhr.responseText);
        }
    }
    xhr.open('GET', url+"?"+param, true);
    xhr.send(data);
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

function getServelet1(url,data,callback) {
	var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
        	return callback(xhr.responseText);
        }
    }
    xhr.open('GET', url, true);
    xhr.send(data);
}

function cargarNavBar(obj_roles, pageName) {
    var mNavbar = document.getElementById("navbar-sicaa");
    var mDropDownPublicaciones = false;
    var mDropDownConsultas = false;
    
    for (var i = 0; i < obj_roles.length; i++) {
        if (obj_roles[i].id == 1) {
            insertIntoNavbar("navbar-sicaa", obj_roles[i].label, obj_roles[i].referencia, "");
        }
        if (obj_roles[i].id >= 2 && obj_roles[i].id <= 5) {
            if (mDropDownPublicaciones == false) {
                mNavbar.appendChild(createDrowndown("Producción Intelectual", "dropmenu-publicaciones", ""));
                mDropDownPublicaciones = true;
            }
            if (obj_roles[i].label == pageName)
                insertIntoNavbar("dropmenu-publicaciones", obj_roles[i].label, obj_roles[i].referencia, "");
            else
                insertIntoNavbar("dropmenu-publicaciones", obj_roles[i].label, obj_roles[i].referencia, "");
        }
        if (obj_roles[i].id >= 6) {
            if (mDropDownConsultas == false) {
                mNavbar.appendChild(createDrowndown("Consultas", "dropmenu-consultas", "active"));
                mDropDownConsultas = true;
            }
            if (obj_roles[i].label == pageName)
                insertIntoNavbar("dropmenu-consultas", obj_roles[i].label, obj_roles[i].referencia, "active");
            else
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