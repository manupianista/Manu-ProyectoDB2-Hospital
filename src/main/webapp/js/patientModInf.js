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
                    var $fullFecha = data[0].fNacimiento;
                    var $fecha = $fullFecha.split(" ")[0];                              //obtener el dia
                    var $name = 'nombreP';
                    document.getElementById($name).value= data[0].nombre;
                    var $lastName = 'apellidoP';
                    document.getElementById($lastName).value= data[0].apellido;
                    var $fNac = 'fNac';
                    document.getElementById($fNac).value= $fecha;                       //Enviar el dia
                    var $dir = 'dir';
                    document.getElementById($dir).value= data[0].dir;
                    var $tel = 'tel';
                    document.getElementById($tel).value= data[0].tel;
                    var $dpi = 'dpi';
                    document.getElementById($dpi).value= data[0].dpi;
                    var $segNum = 'segNum';
                    document.getElementById($segNum).value= data[0].segNum;
                    var $aseguradora = 'asegData';
                    document.getElementById($aseguradora).value= data[0].asegNum;
                    var $docId = 'doctoresData';
                    document.getElementById($docId).value= data[0].docId;
                    var $asegType = 'typeAseg';
                    document.getElementById($asegType).value= data[0].asegType;
                },
                error : function() {
                    var $pData = $('#patientData');
                    $pData.empty();
                    $pData.append("<tr><td>No hay datos disponibles</td></tr>");
                }
            });
});


