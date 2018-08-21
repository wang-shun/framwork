/**
 * Created by lizonglin on 2015/5/25.
 */
$(document).ready(function () {
    $('#hosts_new_save_btn').bind('click',function () {
        $('#hosts_new_sending_modal').modal('show');
        var env = $('#hosts_new_type_select').val();
        var enable = $('#host_new_enable').is(':checked');
        var hostContent = $('#hosts_new_content').val();
        var editor = $('#hosts_new_editor').val();
        $.ajax({
            type : "POST",
            url : "/hosts/newSave",
            data: {
                "env" : env,
                "enable" : enable,
                "hostContent" : hostContent,
                "editor": editor
            },
            success : function (data) {
                $('#hosts_new_sending_modal').modal('hide');
                if (data['isError']) {
                    $('#error_span').empty();
                    $('#error_span').html(data['message']);
                    $('#hosts_new_error_modal').modal('show');
                } else {
                    $('#hosts_new_sent_modal').modal('show');
                }
            }
        });
    });
});