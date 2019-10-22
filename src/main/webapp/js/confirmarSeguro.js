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
                //url: 'http://192.168.69.4:8888/proyectoDB2/restC/cliente/getCliente',
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetCliente',
                dataType: 'json',
                data: { 
                    dpi: query_string('dpi') 
                },
                success: function(data) {
                    var $cData = $('#consulta');
                    $cData.empty();
                    //$uData.append(data[0].usuario);
                    //$cData.append("holaa 1");
                    for (var i = 0; i < data.length; i++) {
                        //$cData.append(data[i]._id); Aqui me quede
                        
                        $cData.append("<tr><td>"+data[i].nombre+"</td>"+"<td>"+data[i].apellido+"</td>"+"<td>"+data[i].tipo_poliza+"</td><td>"+data[i].cobertura+"</tr>");
                    }
                    if(data.length===0){
                        $cData.append("<p>No hay datos disponibles</p>");
                    }
                    
                },
                error : function() {
                    var $cData = $('#consulta');
                    $cData.empty();
                    $cData.append("<p>No hay datos disponibles</p>");
                }
            });
});









