/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#machine_new_save_btn').bind('click', function () {
        $('#machine_new_sending_modal').modal('show');
        saveMachine();
    });
});

function saveMachine() {
    var id = $('#machine_id').val();
    var name = $('#machine_name').val();
    var ip = $('#machine_ip').val();
    var browser = new Array();

    $("#av_browser_div input[type='checkbox']").each(function(){
        if($(this).is(':checked')) {
            browser.push($(this).val());
        }
    });

    var browserValue = calculateBrowserValue(browser);
    var env = $('#machine_env_select').val();
    var description = $('#description').val();
    var label = $('#machine_label').val();
    var type = $('#machine_new_type_select').val();
    var os = $('#machine_new_os_select').val();
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/machine/new/submit',
        traditional: true,
        data: {
            'id': id,
            'name': name,
            'ip': ip,
            'browser': browserValue,
            'env': env,
            'description': description,
            'label': label,
            'type': type,
            'os': os
        },
        success: function (data) {
            $('#machine_new_sending_modal').modal('hide');
            if(data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                $('#machine_new_sent_modal').modal('show');
            }
        }
    });
}