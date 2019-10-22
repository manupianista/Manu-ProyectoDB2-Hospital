function getServicio(variable2)
{
   $.ajax({
        type: 'GET',
        url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/getServicios',
        dataType: 'json',
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                if(variable2==data[i].subCatId){
                    var $res = data[i].subCat;
                }
            }
            return $res;
        }
    });
}
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
function checkCobertura(){
        $.ajax({                                                                                                    //Obtener el dpi del usuario
        type: 'GET',
        url:'http://192.168.69.4:8888/proyectoDB2-Hospital1/restP/patient/getPatient',
        dataType: 'json',
        data: {
            pId: query_string('pId') 
        },
        success: function(data) {
            var $variable2 = getServicio(query_string('servicioId'));
            $.ajax({
                type: 'GET',
                url:'http://192.168.69.4:8888/proyectoDB2-Hospital1/GetCliente',
                dataType: 'json',
                data: {
                    dpi: data[0].dpi 
                },
                success: function(data) {
                    var $pData = $('#tipoS');
                    $pData.append(data[0].tipo_poliza+" "+data[0].cobertura);
                },
                error: function(data) {
                    var $pData = $('#tipoS');
                    $pData.empty();
                }
            }); 
            $.ajax({
                type: 'GET',
                url:'http://192.168.69.4:8888/proyectoDB2-Hospital1/verifyCobertura',                      //Verificar si el seguro si cubre el servicio
                dataType: 'json',
                data: {
                    dpi: data[0].dpi ,
                    servicio: $variable2
                },
                success: function(data) {
                    var $pData = $('#verifyAnsw');
                    if(data.client==1){
                        if(data.serv==1){
                            $pData.append("<option value=\"1\">Sí utilizar Seguro</option>");
                        }
                    }
                    else{
                        var $Data = $('#coberturaS');
                        $Data.empty();
                    }
                    $pData.append("<option value=\"0\">No utilizar Seguro</option>");
                },
                error: function(data) {
                    var $pData = $('#tipoS');
                    $pData.empty();
                }
            }); 
        }
    });
}
$(document).ready(
    function(){
        setTimeout(function(){
            checkCobertura()
        }, 2600);
    },
    document.getElementById('serviciosData').onchange = function () { 
        checkCobertura();
    });