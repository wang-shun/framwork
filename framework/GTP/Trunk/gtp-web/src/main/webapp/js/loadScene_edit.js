/**
 * Created by lizonglin on 2016/1/14/0014.
 */

$(document).ready(function() {
    $('#loadscene_edit_save_btn').bind('click',function() {
        //alert($('#loadscene_edit_onerror').closest('tr').find('th:eq(0)').html());
        saveEditScene();
    });
});

function saveEditScene() {
    var id = $('#loadScene_edit_id').html();
    if (checkIsNone("loadscene_edit_name")) {
        var name = $('#loadscene_edit_name').val();
    } else {
        return;
    }
    var onError = $('#loadscene_edit_onerror').val();
    if (checkIsNoneNum("loadscene_edit_threadnum")) {
        var threadNum = $('#loadscene_edit_threadnum').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_edit_initdelay")) {
        var initDelay = $('#loadscene_edit_initdelay').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_edit_startcount")) {
        var startCount = $('#loadscene_edit_startcount').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_edit_startcountburst")) {
        var startCountBurst = $('#loadscene_edit_startcountburst').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_edit_startperiod")) {
        var startPeriod = $('#loadscene_edit_startperiod').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_edit_stopcount")) {
        var stopCount = $('#loadscene_edit_stopcount').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_edit_stopperiod")) {
        var stopPeriod = $('#loadscene_edit_stopperiod').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_edit_flighttime")) {
        var flightTime = $('#loadscene_edit_flighttime').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_edit_rampup")) {
        var rampUp = $('#loadscene_edit_rampup').val();
    } else {
        return;
    }
    var isTemplate = $('#loadscene_edit_istemplate').is(':checked');
    var isTest = $('#loadscene_edit_istest').is(':checked');

    $('#loadscene_edit_sending_modal').modal('show');

    $.ajax({
        "url":"/load/scene/editSave",
        "async":false,
        "data":{
            "id":id,
            "name":name,
            "onError":onError,
            "threadNum":threadNum,
            "initDelay":initDelay,
            "startCount":startCount,
            "startCountBurst":startCountBurst,
            "startPeriod":startPeriod,
            "stopCount":stopCount,
            "stopPeriod":stopPeriod,
            "flightTime":flightTime,
            "rampUp":rampUp,
            "isTemplate":isTemplate,
            "isTest":isTest
        },
        success:function(data) {
            $('#loadscene_edit_sending_modal').modal('hide');
            if(data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#loadscene_edit_error_modal').modal('show');
            } else {
                $('#loadscene_edit_sent_modal').modal('show');
            }
        }
    });


}

function checkIsNoneNum(id) {
    var value = $('#' + id).val();
    if(value.trim() == ""){
        $('#error_span').empty();
        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " 不能为空，请填写！");
        $('#loadscene_edit_error_modal').modal('show');
        return false;
    } else if(isNaN(value)) {
        $('#error_span').empty();
        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " 必须为数字，请修改！");
        $('#loadscene_edit_error_modal').modal('show');
        return false;
    } else {
        return true;
    }
}

function checkIsNone(id) {
    var value = $('#' + id).val();
    if(value.trim() == ""){
        $('#error_span').empty();
        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " 不能为空，请填写！");
        $('#loadscene_edit_error_modal').modal('show');
        return false;
    } else {
        return true;
    }
}



