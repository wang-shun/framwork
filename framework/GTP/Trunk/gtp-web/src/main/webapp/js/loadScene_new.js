/**
 * Created by lizonglin on 2016/1/14/0014.
 */

$(document).ready(function() {
    $('#loadscene_new_save_btn').bind('click',function() {
        //alert($('#loadscene_new_onerror').closest('tr').find('th:eq(0)').html());
        saveNewScene();
    });
});

function saveNewScene() {
    if (checkIsNone("loadscene_new_name")) {
        var name = $('#loadscene_new_name').val();
    } else {
        return;
    }
    var onError = $('#loadscene_new_onerror').val();
    if (checkIsNoneNum("loadscene_new_threadnum")) {
        var threadNum = $('#loadscene_new_threadnum').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_new_initdelay")) {
        var initDelay = $('#loadscene_new_initdelay').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_new_startcount")) {
        var startCount = $('#loadscene_new_startcount').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_new_startcountburst")) {
        var startCountBurst = $('#loadscene_new_startcountburst').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_new_startperiod")) {
        var startPeriod = $('#loadscene_new_startperiod').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_new_stopcount")) {
        var stopCount = $('#loadscene_new_stopcount').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_new_stopperiod")) {
        var stopPeriod = $('#loadscene_new_stopperiod').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_new_flighttime")) {
        var flightTime = $('#loadscene_new_flighttime').val();
    } else {
        return;
    }
    if (checkIsNoneNum("loadscene_new_rampup")) {
        var rampUp = $('#loadscene_new_rampup').val();
    } else {
        return;
    }
    var isTemplate = $('#loadscene_new_istemplate').is(':checked');
    var isTest = $('#loadscene_new_istest').is(':checked');

    $('#loadscene_new_sending_modal').modal('show');

    $.ajax({
        "url":"/load/scene/newSave",
        "async":false,
        "data":{
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
            $('#loadscene_new_sending_modal').modal('hide');
            if(data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#loadscene_new_error_modal').modal('show');
            } else {
                $('#loadscene_new_sent_modal').modal('show');
            }
        }
    });


}

function checkIsNoneNum(id) {
    var value = $('#' + id).val();
    if(value.trim() == ""){
        $('#error_span').empty();
        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " 不能为空，请填写！");
        $('#loadscene_new_error_modal').modal('show');
        return false;
    } else if(isNaN(value)) {
        $('#error_span').empty();
        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " 必须为数字，请修改！");
        $('#loadscene_new_error_modal').modal('show');
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
        $('#loadscene_new_error_modal').modal('show');
        return false;
    } else {
        return true;
    }
}



