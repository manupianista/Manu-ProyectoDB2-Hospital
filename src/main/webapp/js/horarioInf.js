function getValue(variable)
{
    var value = document.getElementById(variable).value;
    if (value !== null || value !== "") {
        return value;
    } else {
        return("");
    }
}
function query_string(variable)
{
   var query = window.location.search.substring(1);
   var vars = query.split("&");
   for (var i=0;i<vars.length;i++) {
           var pair = vars[i].split("=");
           if(pair[0] == variable){return pair[1];}
   }
   return(false);
}
function getHorario() {
    $.ajax({
        type: 'GET',
        url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/restC/cita/getDisp',
        dataType: 'json',
        data: {
            fecha: getValue('fecha'),
            docId: getValue('doctoresData')

        },
        success: function (data) {
            var $pData = $('#horariosData');
            $pData.empty();
            for (var i = 0; i < data.length; i++) {
                $pData.append("<option value=" + data[i].hora + ">" + data[i].hora + "</option>");
            }
            if (data.length == 0) {
                $pData.append("<optgroup>No hay horarios disponibles</optgroup>");
            }
        },
        error: function () {
            var $pData = $('#horariosData');
            $pData.empty();
            $pData.append("<p>No hay datos disponibles</p>");
        }
    });
}
$(document).ready(
        function () {
            setTimeout(function() {
                getHorario();
                getHora();
            }, 0600);
        },
        document.getElementById('doctoresData').onchange = function () {
            getHorario();
            getHora();
        },
        document.getElementById('fecha').onchange = function () {
            getHorario();
            getHora();
        });
        

function getHora() {
    $.ajax({
        type: 'GET',
        url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetCita',
        dataType: 'json',
        data: { 
            citaId: query_string('citaId') 

        },
        success: function(data) {
            var $fullFecha = data[0].fecha;
            //dividir la fecha en dia y hora
            var $fecha = $fullFecha.split(" ")[0];
            var $hora = $fullFecha.split(" ")[1];
            if((getValue('doctoresData')==data[0].docId)&&(getValue('fecha')==$fecha)){
                //agregar la hora de la cita
                var $pData = $('#horariosData');
                $pData.append("<option value=" + $hora + ">" + $hora + "</option>");
                var $horariosData = 'horariosData';
                document.getElementById($horariosData).value= $hora;
            }
        },
        error : function() {
            var $pData = $('#patientData');
            $pData.empty();
            $pData.append("<tr><td>No hay datos disponibles</td></tr>");
        }
    });
}

