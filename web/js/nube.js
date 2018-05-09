//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-master/";
var radio_selection = null;
var last_words = [];

window.onload = function(){
    /*var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    document.getElementById("nombre").innerHTML = SESSION.datosBasicos.nombre;
    document.getElementById("departamento").innerHTML = SESSION.datosBasicos.nombreDepartamento;
    document.getElementById("facultad").innerHTML = SESSION.datosBasicos.nombreFacultad;
    // Cargar Nav-bar
    cargarNavBar(SESSION.roles, "Nube de palabras");*/
        
}

function insertTargetDiv(id) {
    document.getElementById(id).innerHTML = 
        "<div style='width: 100%; height: 70%;' id='my-cloud'></div>"
        + "<hr class='divider'/>"
        + "<a onclick='exportarPalabras(this)'><button style='margin-top: 10px;' type='button' class='btn btn-info'>Exportar</button></a>";
}

function refrescarNube() {
    
    if (radio_selection != null) {
        var params = document.getElementById(radio_selection).value;
        insertTargetDiv("parent-cloud");
        
        params = "Nombre=" + document.getElementById(radio_selection).value;
        console.log(params);
        
        $('#my-cloud').jQCloud('destroy');
        
        /*getServlet(SERVER_URL + "aaa", null, params, function(servletResponse){
            var response = JSON.parse(servletResponse);
            last_words = response;
            
            
            $('#my-cloud').jQCloud(response, 
                        { 
                            autoResize: true, 
                            delay: 100,
                            shape: 'elliptic'
                        });
        });*/
        
        var words = [];
                    words = [
{text:'CLÓUD',weight:100},
{text:'Retrieved',weight:90},
{text:'FORM',weight:87},
{text:'species',weight:73},
{text:'meteorology',weight:65},
{text:'type',weight:60},
{text:'ed.',weight:55},
{text:'cumulus',weight:53},
{text:'into',weight:50},
{text:'genus',weight:49},
{text:'atmosphere',weight:48},
{text:'air',weight:47},
{text:'level',weight:46},
{text:'World',weight:45},
{text:'Organization',weight:43},
{text:'edit',weight:43},
{text:'high',weight:43},
{text:'International',weight:41},
{text:'convection',weight:41},
{text:'altitude',weight:40},
{text:'vertical',weight:40},
{text:'troposphere',weight:39},
{text:'Atlas',weight:38},
{text:'layer',weight:36},
{text:'stratocumulus',weight:36},
{text:'climate',weight:34},
{text:'more',weight:34},
{text:'variety',weight:34},
{text:'appear',weight:33},
{text:'weather',weight:33},
{text:'cumulonimbus',weight:32},
{text:'feature',weight:32},
{text:'cirrus',weight:31},
{text:'formation',weight:31},
{text:'low',weight:31},
{text:'water',weight:31},
{text:'altocumulus',weight:30},
{text:'cumuliform',weight:30},
{text:'precipitation',weight:27},
{text:'general',weight:26},
{text:'produce',weight:26},
{text:'stratiform',weight:26},
{text:'ISBN',weight:25},
{text:'also',weight:25},
{text:'base',weight:25},
{text:'genera',weight:25},
{text:'surface',weight:24},
{text:'April',weight:23},
{text:'see',weight:23},
{text:'very',weight:23},
{text:'doi',weight:22},
{text:'name',weight:22},
{text:'than',weight:22},
{text:'altostratus',weight:21},
{text:'other',weight:21},
{text:'p.',weight:21},
{text:'polar',weight:21},
{text:'result',weight:21},
{text:'stratiformis',weight:21},
{text:'usual',weight:21},
{text:'color',weight:20},
{text:'cool',weight:20},
{text:'origin',weight:20},
{text:'stratocumuliform',weight:20},
{text:'top',weight:20},
{text:'National',weight:19},
{text:'cirrocumulus',weight:19},
{text:'lift',weight:19},
{text:'physics',weight:19},
{text:'seen',weight:19},
{text:'stratus',weight:19},
{text:'structure',weight:19},
{text:'temperature',weight:19},
{text:'tower',weight:19},
{text:'Universe',weight:18},
{text:'cirriform',weight:18},
{text:'main',weight:18},
{text:'over',weight:18},
{text:'supplementary',weight:18},
{text:'warm',weight:18},
{text:'Bibcode',weight:17},
{text:'Earth',weight:17},
{text:'group',weight:17},
{text:'light',weight:17},
{text:'mid-level',weight:17},
{text:'one',weight:17},
{text:'tend',weight:17},
{text:'associated',weight:16},
{text:'global',weight:16},
{text:'mostly',weight:16},
{text:'nasa',weight:16},
{text:'new',weight:16},
{text:'wind',weight:16},
{text:'January',weight:15},
{text:'Latín',weight:15},
{text:'any',weight:15},
{text:'cause',weight:15},
{text:'change',weight:15},
{text:'cirrostratus',weight:15},
{text:'during',weight:15}
                ];
last_words = words;
        $('#my-cloud').jQCloud(words, 
                    { 
                        autoResize: true, 
                        delay: 50,
                    });
    }
}

function handlerInputs(mNode) {
    if (mNode.value == "autor") {
        radio_selection = "autor-name";
        if (document.getElementById("autor-name").disabled)
            document.getElementById("autor-name").disabled = false;
        if (!document.getElementById("dp-name").disabled)
            document.getElementById("dp-name").disabled = true;
        if (!document.getElementById("f-name").disabled)
            document.getElementById("f-name").disabled = true;
    }
    if (mNode.value == "departamento") {
        radio_selection = "dp-name";
        if (document.getElementById("dp-name").disabled)
            document.getElementById("dp-name").disabled = false;
        if (!document.getElementById("autor-name").disabled)
            document.getElementById("autor-name").disabled = true;
        if (!document.getElementById("f-name").disabled)
            document.getElementById("f-name").disabled = true;
    }
    if (mNode.value == "facultad") {
        radio_selection = "f-name";
        if (document.getElementById("f-name").disabled)
            document.getElementById("f-name").disabled = false;
        if (!document.getElementById("dp-name").disabled)
            document.getElementById("dp-name").disabled = true;
        if (!document.getElementById("autor-name").disabled)
            document.getElementById("autor-name").disabled = true;
    }
    
}

function exportarPalabras(anchor) {
    CSVCreator(anchor, last_words);
}

function CSVCreator(anchor, elements) {
    var eURI = "";
    var filename = "SICCA_cloud.csv";
    var content = "data:text/csv;charset=utf-8," + CSVHeader();
    
    for (var i = 0; i < elements.length; i++) {
        content += elements[i].text + "," 
                + elements[i].weight;
        content += "\r\n";
    }

    eURI = encodeURI(content);
    anchor.setAttribute("href", eURI);
    anchor.setAttribute("download", filename);
}
    
function CSVHeader() {
    var mHeader = "\uFEFF";
    mHeader += "Palabra,Frecuencia";
    return mHeader + "\r\n";
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
                insertIntoNavbar("dropmenu-publicaciones", obj_roles[i].label, obj_roles[i].referencia, "active");
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

function getServlet(url, data, param, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            return callback(xhr.responseText);
        }
        else if(xhr.readyState == 4 && xhr.status != 200) {
            return callback({code:"9997",description:"Imposible obtener respuestan de servidor"});
        }
    }
    xhr.open('GET', url + "?" + param, true);
    xhr.send(data);
}