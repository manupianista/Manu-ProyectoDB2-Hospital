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
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetCita',
                dataType: 'json',
                data: { 
                    citaId: query_string('citaId') 
                },
                success: function(data) {
                    var $pData = $('#historialData');
                    $pData.empty();
                    $pData.append("<h3>Detalles:</h3><article>");
                    $pData.append("<p><strong>Id Cita: </strong> "+data[0].id+"</p><p><strong>Fecha de la cita: </strong>"+data[0].fecha+"</p><p><strong>Area: </strong>"+data[0].cat+"</p><p><strong>Servicio: </strong>"+data[0].subCat+"</p><p><strong>Doctor o Encargado: </strong>"+data[0].docName+" "+data[0].docLastName+"</p><p><strong>Diagnostico: </strong>"+data[0].diag+"</p><p><strong>Resultados: </strong>"+data[0].res+"</p><p><strong>Medicamentos: </strong>"+data[0].meds+"</p><p><strong>Pasos a seguir: </strong>"+data[0].pasos+"</p><p><strong>Observaciones: </strong>"+data[0].observ+"</p>");
                    $pData.append("</article>");
                    

                },
                error : function() {
                    var $pData = $('#patientData');
                    $pData.empty();
                    $pData.append("<p>No hay datos disponibles</p>");
                }
            });
});
