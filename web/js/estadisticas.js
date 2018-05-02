//var SERVER_URL = "https://sicaadev.mybluemix.net/";
var SERVER_URL = "http://localhost:8080/SicaaNetBeans-master/";
var myChart;

window.onload = function(){
    var SESSION = JSON.parse(sessionStorage.getItem("principal"));
    //var nombre = document.getElementById("nombre");
    //nombre.innerHTML = SESSION.datosBasicos.nombre;
    document.getElementById("nombre").innerHTML = "Diana Usgame";
    document.getElementById("departamento").innerHTML = "Vicerrectoria Académica";
    $("#loaderGraph").hide();
    var ctx = document.getElementById("bar-chart").getContext('2d');
    myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [
            {
                label: "",
                backgroundColor: [],
                data: []
            }
          ]
        },
        options: {
            legend: { display: false }
        }
    });
}

function exportarDiagrama() {
    var mFileName = "default-name.png";
    var mAnchor;
    var mImageURL;
    if (document.getElementById("fileName").value != "") {
        mFileName = document.getElementById("fileName").value
    }
    mAnchor = document.getElementById("link");
    mImageURL = document.getElementById("bar-chart").toDataURL();
    
    mAnchor.href = mImageURL;
    mAnchor.setAttribute("download", mFileName);
    document.getElementById("fileName").value = "";
}

function refrescarDiagrama() {
    $("#loaderGraph").show();
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
        
        postServlet(SERVER_URL + "EstadisticasServelet", JSON.stringify(data_to_send), function(serveletResponse) {
            var response = JSON.parse(serveletResponse);
            $("#loaderGraph").hide();
            if (response.code == 0) {
                myChart.destroy();

                var mInfoDiagram = new Object();
                var mData = new Object();
                var mOptions = new Object();
                var mBar = new Object();
                var mDatasets = [];
                var mColors = [];
        
                for (var i = 0; i < response.estadisticas.labels.length; i++)
                    mColors.push(getColor());
                
                mBar.label = "Número de Publicaciones";
                mBar.backgroundColor = mColors;
                mBar.data = response.estadisticas.data;
                mDatasets.push(mBar);
        
                mData.labels = response.estadisticas.labels;
                mData.datasets = mDatasets;
        
                mOptions.legend = {display: false};

                mInfoDiagram.type = "bar";
                mInfoDiagram.data = mData;
                mInfoDiagram.options = mOptions;

                createDiagram(document.getElementById("bar-chart").getContext('2d'), mInfoDiagram);  
            }
            else {
                alert("Error: " + response.code.toString() + " - Descripcion: " + response.description);
            }
        });
    }
    else {
        alert("Debes ingresar fecha desde y fecha hasta!");
    }
}

function getColor() {
    var red =  Math.round(Math.random() * 255);
    var green = Math.round(Math.random() * 255);
    var blue = Math.round(Math.random() * 255);
    return "rgba(" + red + "," + green + "," + blue + "," + 0.8 + ")";
}

function createDiagram(ctx, info) {
    myChart = new Chart(ctx, info); 
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