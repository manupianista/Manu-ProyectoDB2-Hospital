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
            var $idNum = $('#cob');
            $.ajax({
                type: 'GET',
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/getCobertura',
                dataType: 'json',
                data: { 
                    citaId: query_string('citaId') 
                    
                },
                success: function(data) {
                    var $pData = $('#cob');
                    $pData.empty();
                    //var totSeg = data[0].costo. * data[0].pCobertura
                    let x = data[0].pCobertura;
                    let y = data[0].costo;
                    let z = x*y;
                    let w = y-z; 
                    let m = x * 100;
                    $pData.append("<p> tratamiento cubierto por el seguro al "+m+"% El costo total es de: Q"+data[0].costo+" el seguro cubrira Q"+z+" y el paciente pagara Q"+w+" por el uso de una poliza <strong>"+data[0].tipoSeguro+"</strong> </p>");
                    

                },
                error : function() {
                    var $pData = $('#cob');
                    $pData.empty();
                    $pData.append("<p>No hay datos disponibles</p>");
                }
            });
});











