/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the newor.
 */

$(document).ready(function () {
    $('#suggestion_new_save_btn').bind('click', function() {
       updateSu(); 
    });
});

function updateSu () {
    $('#suggestion_new_sending_modal').modal('show');
    var title = $('#suggestion_new_name').val();
    var type = $('#suggestion_new_type_select').val();
    var status = $('#suggestion_new_status_select').val();
    var content = $('#suggestion_new_content').val();
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/suggestion/create",
        data: {
            "title": title,
            "type": type,
            "status": status,
            "content": content
        },
        success: function (data) {
            $('#suggestion_new_sending_modal').modal('hide');
            if (data['isError']) {
                $('#check-error-body').empty();
                $('#check-error-body').val(data['message']);
                $('#suggestion_new_error_modal').modal('show');
            } else {
                $('#suggestion_new_sent_modal').modal('show');
            }
        }
    });
}