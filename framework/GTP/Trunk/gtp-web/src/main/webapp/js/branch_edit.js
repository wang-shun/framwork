/**
 * Created by lizonglin on 2015/5/4.
 */
$(document).ready(function() {
    dateShow();
    $('#branch_edit_expireDate').bind('click', function () {
        $('#branch_edit_ed_input').datetimepicker('show');
    });

    $('#branch_edit_ed_input').val(trimToDate($('#branch_edit_ed_input').val()));

    $('#branch_edit_save_btn').bind('click', function () {
       $('#branch_edit_sending_modal').modal('show');
        saveEditBranch();
    });

    $('#branch_modal_back_btn').bind('click',function(){
        window.location.href="/branch";
    });
});

function dateShow () {
    return $('#branch_edit_ed_input').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
}

function saveEditBranch () {
    var branchId = $('#branch_edit_id_span').html();
    var branchName = $('#branch_edit_name').val();
    var branchUrl = $('#branch_edit_url').val();
    var group = $('#branch_edit_group_select').val();
    var expireDate = $('#branch_edit_ed_input').val();
    var type = $('#branch_edit_type_select').val();
    $.ajax({
        type: "POST",
        url: "/branch/editSave",
        data: {
            'branchId': branchId,
            'branchName': branchName,
            'branchUrl': branchUrl,
            'group': group,
            'expireDate': expireDate,
            'type': type
        },
        success: function (data) {
            $('#branch_edit_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#branch_edit_error_modal').modal('show');
            } else {
                $('#branch_edit_sent_modal').modal('show');
            }
        }
    });
}