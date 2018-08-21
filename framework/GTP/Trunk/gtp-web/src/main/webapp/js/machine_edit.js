/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    //checkBrowser($('#browser_value').val());
    $('#machine_edit_save_btn').bind('click', function () {
        $('#machine_edit_sending_modal').modal('show');
        updateMachine();
    });

});

function updateMachine() {
    var name = $('#machine_name').val();
    var ip = $('#machine_ip').val();
    var browser = [];

    $("#av_browser_div input[type='checkbox']").each(function(){
        if($(this).is(':checked')) {
            browser.push($(this).val());
        }
    });

    var id = $('#machine_edit_id_span').html();
    var browserValue = calculateBrowserValue(browser);
    var env = $('#machine_env_select').val();
    var description = $('#description').val();
    var label = $('#machine_label').val();
    var type = $('#machine_edit_type_select').val();
    var os = $('#machine_edit_os_select').val();
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/machine/edit/submit',
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
        success: function (response) {
            $('#machine_edit_sending_modal').modal('hide');
            if(response['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                $('#machine_edit_sent_modal').modal('show');
            }
        }
    });
}

/**
 * 自动选中浏览器复选框
 * @param browser
 */

function checkBrowser(browser) {
    var browserArray =  browserTypes(browser);
    $('#av_browser_div input').each(function(){
       if($.inArray($(this).val(),browserArray) > -1){
           $(this).attr('checked','checked');
       }
    });
}