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
$(document).ready(
        function() {
            $.ajax({
                type: 'GET',
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetHistorial',
                dataType: 'json',
                data: { 
                    pId: query_string('pId') 
                },
                success: function(data) {
                    var $pData = $('#historialData');
                    $pData.empty();
                    for (var i = 0; i < data.length; i++) {
                        $pData.append("<tr><td>"+data[i].id+"</td><td>"+data[i].diag+"</td><td>"+data[i].docName+"</td><td>"+data[i].fecha+"</td><td><a href=\"cita_h.jsp?citaId="+data[i].id+"&pId="+data[i].pId+"\">Ver detalles</a></td></tr>");
                    }
                    if(data.length==0){
                        $pData.append("<p>No hay datos disponibles</p>");
                    }

                },
                error : function() {
                    var $pData = $('#historialData');
                    $pData.empty();
                    $pData.append("<p>No hay datos disponibles</p>");
                }
            });
});
