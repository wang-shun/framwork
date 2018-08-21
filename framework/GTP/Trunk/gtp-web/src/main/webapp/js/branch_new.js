/**
 * Created by lizonglin on 2015/5/4.
 */
$(document).ready(function() {
    dateShow();
    $('#branch_new_expireDate').bind('click', function () {
        $('#branch_new_ed_input').datetimepicker('show');
    });

    $('#branch_new_save_btn').bind('click', function () {
       $('#branch_edit_sending_modal').modal('show');
        saveNewBranch();
    });

    $('#branch_new_sent_modal button').bind('click',function(){

        gotoBranch();
    })
    setparams();
});

function gotoBranch()
{
    var params=$("#paramsInput").val();
    var url="/branch";
    if( null!= params && params.length  > 0 )
    {
        url=url+"/search/"+params;
    }

    window.location.href=url;
}

function setparams(){
    var params=$("#paramsInput").val();
    if( null== params || params=='' )
        return false;

    var paramsList=params.split('_');
    if(paramsList.length>1)
    {
        $("#branch_new_group_select").find("option[value='"+paramsList[1]+"']").attr("selected",true);
        $("#branch_new_group_select").attr("disabled",true);
    }

    return false;
}

function dateShow () {
    return $('#branch_new_ed_input').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
}

function saveNewBranch () {
    var branchName = $('#branch_new_name').val();
    var branchUrl = $('#branch_new_url').val();
    var group = $('#branch_new_group_select').val();
    var expireDate = $('#branch_new_ed_input').val();
    var type = $('#branch_new_type_select').val();
    $.ajax({
        type: "POST",
        url: "/branch/newSave",
        data: {
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
                $('#branch_new_error_modal').modal('show');
            } else {
                $('#branch_new_sent_modal').modal('show');
            }
        }
    });
}