/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#suggestion_edit_save_btn').bind('click', function() {
       updateSu(); 
    });
});

function updateSu () {
    $('#suggestion_edit_sending_modal').modal('show');
    var id = $('#suggestion_edit_id_span').html();
    var title = $('#suggestion_edit_name').val();
    var type = $('#suggestion_edit_type_select').val();
    var status = $('#suggestion_edit_status_select').val();
    var content = $('#suggestion_edit_content').val();
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/suggestion/update/"+id,
        data: {
            "title": title,
            "type": type,
            "status": status,
            "content": content
        },
        success: function (data) {
            $('#suggestion_edit_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#suggestion_edit_error_modal').modal('show');
            } else {
                $('#suggestion_edit_sent_modal').modal('show');
            }
        }
    });
}