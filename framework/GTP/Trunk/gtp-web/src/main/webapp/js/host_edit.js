/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#hosts_edit_save_btn').bind('click',function () {
        $('#hosts_edit_sending_modal').modal('show');
        var id = $('#hosts_edit_id_span').text();
        var env = $('#hosts_edit_type_select').val();
        var enable = $('#host_edit_enable').is(':checked');
        var hostContent = $('#hosts_edit_content').val();
        $.ajax({
            type : "POST",
            url : "/hosts/update",
            data: {
                "id" : id,
                "env" : env,
                "enable" : enable,
                "hostContent" : hostContent
            },
            success : function (data) {
                $('#hosts_edit_sending_modal').modal('hide');
                if (data['isError']) {
                    $('#error_span').empty();
                    $('#error_span').html(data['message']);
                    $('#hosts_edit_error_modal').modal('show');
                } else {
                    $('#hosts_edit_sent_modal').modal('show');
                }
            }
        });
    });
});

