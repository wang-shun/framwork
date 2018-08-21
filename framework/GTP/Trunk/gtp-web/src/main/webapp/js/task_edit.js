/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    dateShow();
    $('#task_edit_startDate').bind('click', function () {
        $('#task_edit_sd_input').datetimepicker('show');
    });
    
    trimDate();
    
    isMachine();
    
    $('#task_edit_isMachine').bind('click', function () {
        $('#task_edit_machineList').toggleClass('hidden');
    });
    
    //$('#task_edit_save_btn').bind('click', function () {
    //    $('#task_edit_sending_modal').modal('show');
    //    updateTaskInfo();
    //});

});

function dateShow () {
    return $('#task_edit_sd_input').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
}

function trimDate () {
    $('#task_edit_sd_input').val(trimToDate($('#task_edit_sd_input').val()));
}

function isMachine () {
    if ($('#task_edit_isMachine').is(':checked')) {
        $('#task_edit_machineList').removeClass('hidden');
    } else {
        $('#task_edit_machineList').addClass('hidden');
    }
}

function getBrowsers() {
    var browsers = "";
    $("input[name='task_edit_browser']:checked").each(function() {
        browsers = browsers + $(this).val() + ",";
    });
    if (browsers.length > 0) {
        return browsers.substring(0, browsers.lastIndexOf(","));
    } else {
        return browsers;
    }
}

function toggleMachine () {
    
}

function updateTaskInfo (taskType) {

    //各类型任务公共参数
    var taskId = $('#task_edit_id_span').html();
    var taskName = $('#task_edit_name').val();
    var runType = $('#task_edit_runType_select').val();
    var regularInfo = $('#task_edit_regularInfo_select').val();
    var startDate = $('#task_edit_sd_input').val();
    var env = $('#task_edit_env_select').val();
    var branch = $('#task_edit_branch_select').val();

    var isMachine = $('#task_edit_isMachine').is(':checked');
    var machine = $('#task_edit_machineList').val();
    if (isMachine == false) {
        machine = "None";
    }
    var isMonited = $('#task_edit_isMonited').is(':checked');
    var isSplit = $('#task_edit_isSplit').is(':checked');
    var editor = $('#task_edit_editor').val();
    var emailList = $('#task_edit_email').val();

    //不同类型的特殊参数
    var caseQuery = "";
    var browsers = "";

    //if (taskType == 2) {
    //    browsers = getBrowsers();
    //    if (checkBrowser(browsers)) {
    //        return;
    //    }
    //}

    var loadConfName = "";
    switch (taskType) {
        case 1:
            caseQuery = $('#task_edit_cq').val();
            break;
        case 2:
            caseQuery = $('#task_edit_cq').val();
            browsers = getBrowsers();
            if (checkBrowser(browsers)) {
                return;
            }
            break;
        case 3:
            loadConfName = $('#task_edit_loadconf_select').val();
    }
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/task/update/"+taskId,
        data: {
            'taskId': taskId,
            'taskName': taskName,
            'taskType': taskType,//
            'runType': runType,//
            'regularInfo': regularInfo,//
            'startDate': startDate,
            'env': env,//
            'branch': branch,//
            'caseQuery': caseQuery,
            'browser': browsers,//
            'isMachine': isMachine,
            'machine': machine,//
            'isMonited': isMonited,
            'isSplit': isSplit,
            'editor': editor,
            'emailList': emailList,
            'loadConfName': loadConfName
        },
        success: function (data) {
            $('#task_edit_sending_modal').modal('hide');
            if (data['isError']){
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#task_edit_error_modal').modal('show');
            } else {
                $('#task_edit_sent_modal').modal('show');
            }
        }
    });
}

function checkBrowser(browsers) {
    if (browsers.trim() == "") {
        $('#error_span').empty();
        $('#error_span').html("至少需要选择一个浏览器");
        $('#task_edit_error_modal').modal('show');
        return true;
    } else if ($('#task_edit_isMachine').is(':checked')) {
        var machine = $('#task_edit_machineList').val();
        var hasAllBrowser = false;
        $.ajax({
            url:'/task/save/checkbrowser',
            type:'GET',
            async:false,
            data:{
                'machine':machine,
                'browsers':browsers
            },
            success:function(data) {
                if(data['data'] == true) {
                    hasAllBrowser = true;
                }
            }
        });
        if (!hasAllBrowser) {
            $('#error_span').empty();
            $('#error_span').html("指定的机器不支持所有已选浏览器");
            $('#task_edit_error_modal').modal('show');
            return true;
        }
    } else {
        return false;
    }
}


function loadMachine() {

}