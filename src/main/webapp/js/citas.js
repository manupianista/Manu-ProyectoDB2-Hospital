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
            if (findCookie("rol") == 4) {
                    $.ajax({
                        type: 'GET',
                        url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetCita',
                        dataType: 'json',
                        data:{
                          docId:  findCookie("UId")
                        },
                        success: function (data) {
                            var $pData = $('#historialData');
                            $pData.empty();
                            for (var i = 0; i < data.length; i++) {
                                $pData.append("<tr><td>" + data[i].id + "</td><td>" + data[i].cat + "</td><td>" + data[i].docName + " " + data[i].docLastName + "</td><td>" + data[i].pName + " " + data[i].pLastName + "</td><td>" + data[i].fecha + "</td><td><a class=\"button\" href=\"cita_h.jsp?citaId=" + data[i].id + "&pId=" + data[i].pId + "\">Ver detalles</a> <a class=\"button\" href=\"deleteC_h.jsp?citaId=" + data[i].id + "\">Eliminar Cita</a></td></tr>");
                            }
                            if (data.length == 0) {
                                $pData.append("<p>No hay datos disponibles</p>");
                            }
                        },
                        error: function () {
                            var $pData = $('#historialData');
                            $pData.empty();
                            $pData.append("<p>No hay datos disponibles</p>");
                        }
                    });
            }else{
                    $.ajax({
                        type: 'GET',
                        url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetCita',
                        dataType: 'json',
                        success: function (data) {
                            var $pData = $('#historialData');
                            $pData.empty();
                            for (var i = 0; i < data.length; i++) {
                                $pData.append("<tr><td>" + data[i].id + "</td><td>" + data[i].cat + "</td><td>" + data[i].docName + " " + data[i].docLastName + "</td><td>" + data[i].pName + " " + data[i].pLastName + "</td><td>" + data[i].fecha + "</td><td><a class=\"button\" href=\"cita_h.jsp?citaId=" + data[i].id + "&pId=" + data[i].pId + "\">Ver detalles</a> <a class=\"button\" href=\"deleteC_h.jsp?citaId=" + data[i].id + "\">Eliminar Cita</a></td></tr>");
                            }
                            if (data.length == 0) {
                                $pData.append("<p>No hay datos disponibles</p>");
                            }
                        },
                        error: function () {
                            var $pData = $('#historialData');
                            $pData.empty();
                            $pData.append("<p>No hay datos disponibles</p>");
                        }
                    });
            }
});
