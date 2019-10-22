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
function query_string(variable)
{
   var query = window.location.search.substring(1);
   var vars = query.split("&");
   for (var i=0;i<vars.length;i++) {
           var pair = vars[i].split("=");
           if(pair[0] === variable){return pair[1];}
   }
   return(false);
}
$(document).ready(
        function () {
            $.ajax({
                type: 'GET',
                //url: 'http://192.168.69.4:8888/proyectoDB2-seguro/restHist/hist/getHist?hospital=2',
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetHistSeg',
                dataType: 'json',
                data: { 
                    citaId: query_string('citaId') 
                },
                success: function (data) {
                    var $id = parseInt(query_string('citaId'));
                    for (var i = 0; i < data.length; i++) {
                        var cit = data[i].idCita;
                        if($id === cit){
                            var $name = $('#nombreP');
                            $name.append(data[i].nombre);
                            var $lastName = $('#apellidoP');
                            $lastName.append(data[i].apellido);
                            var $aseguradora = $('#aseguradora');
                            $aseguradora.append("Seguros Gio");
                            //var $fNac = $('#fNac');
                            //$fNac.append(data[0].fNacimiento);
                            //var $dir = $('#dir');
                            //$dir.append(data[0].dir);
                            //var $tel = $('#tel');
                            //$tel.append(data[0].tel);
                            var $dpi = $('#dpi');
                            $dpi.append(data[i].dpi);
                            var $segNum = $('#segNum');
                            $segNum.append(data[i].dpi);
                            var $pData = $('#historialData');
                            $pData.empty();
                            $pData.append("<h3>Detalles:</h3><article>");
                            $pData.append("<p><strong>Id Cita: </strong> "+data[i].idCita+"</p><p><strong>Fecha de la cita: </strong>"+data[i].fecha+"</p><p><strong>Area: </strong>"+data[i].categoria+"</p><p><strong>Servicio: </strong>"+data[i].subcat+"</p><p><strong>Doctor o Encargado: </strong>"+data[i].doctor+"</p><p><strong>Diagnostico: </strong>"+data[i].diagnostico+"</p><p><strong>Resultados: </strong>"+data[i].resultados+"</p><p><strong>Medicamentos: </strong>"+data[i].medicinas+"</p><p><strong>Pasos a seguir: </strong>"+data[i].pasos+"</p><p><strong>Observaciones: </strong>"+data[i].observaciones+"</p>");
                            $pData.append("</article>");
                            
                        }
                    
                        
                    }
                    
                },
                error: function () {
                    var $pData = $('#historialData2');
                    $pData.empty();
                    $pData.append("<p>No hay datos disponibles sobre este paciente en la aseguradora</p>");
                }
            });
            
});








































