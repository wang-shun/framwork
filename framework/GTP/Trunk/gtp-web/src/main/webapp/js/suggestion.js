/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#suggestion_table').DataTable({
        "bLengthChange": true,//每页显示条数可选
        "processing": false,
        ajax: {
            "url": "/suggestion/all",
            "type": "POST"
            },
        columns: [
            { data: [0] },
            { data: [1] },
            { data: [2] },
            { data: [3] },
            { data: [4] },
            { data: [5] },
            { data: [6] },
            { data: [7] }
//            { data: [8] }
        ],
        columnDefs: [
            {
                targets: [5],
                data: [5],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [8],
                data: [0],
                render: function (data, type, full) {
                    if (alreadyLogin) {
                        return "<a id='su_edit_" + data + "' class='su_delete_opt' href='/suggestion/edit/" + data + "'>Edit</a>";
                    } else {
                        return "<a class='btn btn-default' href='/login'>Login to operate</a>";
                    }
                }
            }
        ]
    });
});

function suEdit (id) {
    
}
function suDetails (id) {
    $('#suggestion_sending_modal').modal('show');
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "/suggestion/details/"+id,
        success: function (data) {
            $('#suggestion_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#suggestion_error_modal').modal('show');
            } else {
                    insertDetails(data['data'][0]);
                    $('#suggestion_details_modal').modal('show');
            }
        }
    });
}

function insertDetails(data) {
    $('.suggestion_details_table_span').each(function (i) {
        
        if (5 === i) {
            $(this).html(timeStamp2String(data[i]));
        } else {
            $(this).html(data[i]);
        }
    });
}