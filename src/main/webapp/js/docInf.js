$(document).ready(
        function() {
            
            $.ajax({
                type: 'GET',
                url: 'http://192.168.69.4:8888/proyectoDB2-Hospital1/DocInfo',
                dataType: 'json',
                success: function(data) {
                    var $pData = $('#doctoresData');
                    $pData.empty();
                    for (var i = 0; i < data.length; i++) {
                        $pData.append("<option value="+data[i].id+">"+data[i].name+"</option>");
                    }
                    if(data.length==0){
                        $pData.append("<p>No hay datos disponibles</p>");
                    }
                },
                error : function() {
                    var $pData = $('#doctoresData');
                    $pData.empty();
                    $pData.append("<p>No hay datos disponibles</p>");
                }
            });
});


