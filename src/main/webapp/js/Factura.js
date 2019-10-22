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
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/restFac/factura/getFactura',
                dataType: 'json',
                data: { 
                    cId: query_string('cId') 
                },
                success: function(data) {
                    try{
                        var $factura = $('#noFac');
                        $factura.append(data[0].id);
                        var $noCita = $('#cita');
                        $noCita.append(data[0].cita);
                        var $autorizacion = data[0].auth;
                        var $auth = $('#noAuth');
                        var $seguro = $('#seg');
                        if ($autorizacion === 0){
                            $auth.append("N/A");
                            $seguro.append("N/A");
                        } else{
                            $auth.append(data[0].auth);
                            $seguro.append("Seguros Gio");
                        }
                        var $fechaHora =data[0].fecha;
                        var $fecha = $('#fecha');
                        $fecha.append($fechaHora.substring(0, $fechaHora.indexOf(' ')));
                        var $nombreC = data[0].nombre +" "+ data[0].apellido; 
                        var $nombre = $('#nombre');
                        $nombre.append($nombreC);
                        var $servicio = data[0].cat+"----->"+data[0].subcat;
                        var $serv = $('#serv');
                        $serv.append($servicio);
                        var $costoT = data[0].costo;
                        var $pagoP = data[0].cobroCliente;
                        var $costo = $('#costo');
                        $costo.append("Q"+$costoT);
                        var $pagoPaciente = $('#pago');
                        $pagoPaciente.append("Q"+$pagoP);
                        var $pagoSeguro = $('#cob');
                        var $pagoSeguroC = $costoT-$pagoP;
                        $pagoSeguro.append("Q"+$pagoSeguroC);
                    }catch(err){
                        var $error = $('#facuraInex');
                        //$error ="NO EXISTE LA FACTURA BUSCADA";
                        $error.empty();
                        $error.append("NO EXISTE LA FACTURA BUSCADA");
                    }
                },
                error : function() {
                    var $facturaInex = $('#facuraInex');
                    $facturaInex.empty();
                    $facturaInex.append("Error en el sistema. Por favor informe a el encargado de TI");
                }
            });
});


