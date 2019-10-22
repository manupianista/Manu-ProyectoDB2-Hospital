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
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetPatient',
                dataType: 'json',
                success: function(data) {
                    var $pData = $('#patients');
                    $pData.empty();
                    for (var i = 0; i < data.length; i++) {                        
                        if(query_string('pId')!=false){
                        //if(query_string('pId')==data[i].id){
                            if(query_string('pId')==data[i].id){
                                $pData.append("<option value="+data[i].id+">"+data[i].nombre+" "+data[i].apellido+"</option>");
                            }
                        } else{
                            $pData.append("<option value="+data[i].id+">"+data[i].nombre+" "+data[i].apellido+"</option>");
                        }
                        
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
