function findCookie(variable)
{
    var query = document.cookie;
    var vars = query.split("; ");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === variable) {
            return pair[1];
        }
    }
    return(false);
}
$(document).ready(
        function () {
            $.ajax({
                type: 'GET',
                //url: 'http://192.168.69.4:8888/proyectoDB2-seguro/restHist/hist/getHist?hospital=2',
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetHistSeg',
                dataType: 'json',
                success: function (data) {
                    var $pData = $('#historialData2');
                    var $hos =3;
                    $pData.empty();
                    for (var i = 0; i < data.length; i++) {
                        var temp = data[i].hospital;
                        if($hos == 3){
                          $pData.append("<tr><td>" + data[i].idCita + "</td><td>" + data[i].categoria + "</td><td>" + data[i].doctor + "</td><td>" + data[i].nombre+ " " + data[i].apellido + "</td><td>" + data[i].fecha + "</td><td><a class=\"button\" href=\"cita_hs.jsp?citaId=" + data[i].idCita + "&pId=" + /*data[i].pId*/ "patito" + "\">Ver detalles</a> </td></tr>");
                        }
                        
                    }
                    if (data.length == 0) {
                        $pData.append("<p>No hay datos disponibles</p>");
                    }
                },
                error: function () {
                    var $pData = $('#historialData2');
                    $pData.empty();
                    $pData.append("<p>No hay datos disponibles sobre este paciente en la aseguradora</p>");
                }
            });
            
});


































