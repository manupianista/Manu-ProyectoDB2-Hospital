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
                    var $fullFecha = data[0].fecha;
                    //dividir la fecha en dia y hora
                    var $fecha = $fullFecha.split(" ")[0];
                    var $hora = $fullFecha.split(" ")[1];
                    var $fechaC = 'fecha';
                    document.getElementById($fechaC).value= $fecha;
                    var $horariosData = 'horariosData';
                    document.getElementById($horariosData).value= $hora;
                    var $pInfo = 'patients';
                    document.getElementById($pInfo).value= data[0].pName+" "+data[0].pLastName;
                    var $sData = 'serviciosData';
                    document.getElementById($sData).value= data[0].idSubCat;
                    var $dData = 'doctoresData';
                    document.getElementById($dData).value= data[0].docId;
                    var $diag = 'diag';
                    document.getElementById($diag).value= data[0].diag;
                    var $res = 'res';
                    document.getElementById($res).value= data[0].res;
                    var $meds = 'meds';
                    document.getElementById($meds).value= data[0].meds;
                    var $pasos = 'pasosASeguir';
                    document.getElementById($pasos).value= data[0].pasos;
                    var $observ = 'observ';
                    document.getElementById($observ).value= data[0].observ;
                },
                error : function() {
                    var $pData = $('#patientData');
                    $pData.empty();
                    $pData.append("<tr><td>No hay datos disponibles</td></tr>");
                }
            });
});
