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
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/restU/usuarios/getUsuarios',
                dataType: 'json',
                data: { 
                    uId: query_string('uId') 
                },
                success: function(data) {
                    var $name = $('#nombreP');
                    $name.append(data[0].firstName);
                    var $lastName = $('#apellidoP');
                    $lastName.append(data[0].lastName);
                    var $uTy = $('#uTy');
                    $uTy.append(data[0].usType);
                    var $uSpe = $('#uSpe');
                    $uSpe.append(data[0].usSpecial);
                    var $tel = $('#tel');
                    $tel.append(data[0].phone);
                    var $dpi = $('#user');
                    $dpi.append(data[0].username);
                },
                error : function() {
                    var $pData = $('#usuariosData');
                    $pData.empty();
                    $pData.append("<tr><td>No hay datos disponibles</td></tr>");
                }
            });
});





