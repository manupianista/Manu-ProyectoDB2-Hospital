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
                data: { 
                    pId: query_string('pId') 
                },
                success: function(data) {
                    var $name = $('#nombreP');
                    $name.append(data[0].nombre);
                    var $lastName = $('#apellidoP');
                    $lastName.append(data[0].apellido);
                    var $fNac = $('#fNac');
                    $fNac.append(data[0].fNacimiento);
                    var $dir = $('#dir');
                    $dir.append(data[0].dir);
                    var $tel = $('#tel');
                    $tel.append(data[0].tel);
                    var $dpi = $('#dpi');
                    $dpi.append(data[0].dpi);
                    var $segNum = $('#segNum');
                    $segNum.append(data[0].segNum);
                    var $aseguradora = $('#aseguradora');
                    $aseguradora.append(data[0].asegName);
                },
                error : function() {
                    var $pData = $('#patientData');
                    $pData.empty();
                    $pData.append("<tr><td>No hay datos disponibles</td></tr>");
                }
            });
});


