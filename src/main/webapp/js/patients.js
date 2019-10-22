$(document).ready(
        function() {
            $.ajax({
                type: 'GET',
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetPatient',
                dataType: 'json',
                success: function(data) {
                    var $pData = $('#patientData');
                    $pData.empty();
                    for (var i = 0; i < data.length; i++) {
                        //$pData.append("<tr><td>"+data[i].id+"</td>"+"<td>"+data[i].nombre+"</td>"+"<td>"+data[i].apellido+"</td>"+"<td>"+data[i].tel+"</td>"+"<td>"+data[i].dpi+"<td>"+data[i].segNum+"<td>"+data[i].asegTypeName+"</td></td><td><a href=\"historial_h.jsp?pId="+data[i].id+"\" class=\"button expanded pLink\">Ver historial</a><a href=\"modificarP_h.jsp?pId="+data[i].id+"\" class=\"button pLink expanded\">Modificar</a><a href=\"http://192.168.69.4:8888/proyectoDB2-Hospital1/deleteP_h.jsp?pId="+data[i].id+"\" class=\"pLink button expanded \">Eliminar</a></td></tr>");
                        $pData.append("<tr><td>"+data[i].id+"</td>"+"<td>"+data[i].nombre+"</td>"+"<td>"+data[i].apellido+"</td>"+"<td>"+data[i].tel+"</td>"+"<td>"+data[i].dpi+"<td>"+data[i].segNum+"</td><td><a href=\"historial_h.jsp?pId="+data[i].id+"\" class=\"button expanded pLink\">Ver historial</a><a href=\"modificarP_h.jsp?pId="+data[i].id+"\" class=\"button pLink expanded\">Modificar</a><a href=\"http://192.168.69.4:8888/proyectoDB2-Hospital1/deleteP_h.jsp?pId="+data[i].id+"\" class=\"pLink button expanded \">Eliminar</a></td></tr>");
                    }
                    if(data.length==0){
                        $pData.append("<p>No hay datos disponibles</p>");
                    }
                },
                error : function() {
                    var $pData = $('#patientData');
                    $pData.empty();
                    $pData.append("<p>No hay datos disponibles</p>");
                }
            });
});







