var SERVER_URL = "https://sicaadev.mybluemix.net/";
//var SERVER_URL = "http://localhost:8080/SicaaNetBeans-dev/";

window.onload = function(){
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    if (SESSION != null) {
        window.location.href = "Vista/principal.html";
    }
}

/**
 * Metodo para hacer login de usuario y traer los roles a los que tiene acceso
 * @returns {undefined}
 */
function login(){
    var user_name = document.getElementById("user_name").value;
    var user_psw = document.getElementById("user_pss").value;
    var respuesta;
    var user_login_data = {
            "user_name":user_name,
            "user_pass":user_psw,
            "server_url":SERVER_URL,
            "action":"LOGIN"
    };
    document.getElementById("loaderLogin").style.display = "block";
    document.getElementById("login-image").style.display = "none";
    document.getElementById("login-button").disabled = true;
    postServelet(SERVER_URL + "login_servelet",JSON.stringify(user_login_data),function(serveletResponse) {
        var respuesta = JSON.parse(serveletResponse);
        document.getElementById("loaderLogin").style.display = "none";
        document.getElementById("login-button").disabled = false;
        document.getElementById("login-image").style.display = "block";
        if (respuesta.code === 0) {
            respuesta.firstTime = true;
            sessionStorage.setItem("principal", JSON.stringify(respuesta));
            window.location.href = respuesta.roles[0].referencia;
        }
        else {
            alert("Error c\xf3digo: " + respuesta.code.toString() + " - Descripci\xf3n: " + respuesta.description);
            document.getElementById("user_name").value = "";
            document.getElementById("user_pss").value = "";
        }
    });
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
        else if(xhr.readyState == 4 && xhr.status != 200) {
            return callback({code:"9997",description:"Imposible obtener respuestan de servidor"});
        }
    }
    xhr.open('GET', url+"?"+param, true);
    xhr.send(data);
}

function postServelet(url,data,callback) {
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