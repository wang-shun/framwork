/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the newor.
 */

$(document).ready(function () {
    $('#task_new_isMachine').bind('click', function () {
        isMachine();
    });
    isMachine();
    setparams();
});

function setparams(){
    var params=$("#paramsInput").val();
    if( null== params || params=='' )
        return false;

    var paramsList=params.split('_');
    switch (paramsList.length)
    {
        case 2:
            var groupStr=params[1];
            break;
        case 3:
            $("[name='radio-task-type']").each(function(i,n){
               if(n.value==paramsList[2])
               {
                 $("#"+ n.id).attr("checked",true);
               }
            });
            break;
        case 4:
        case 5:
            $("[name='radio-task-type']").each(function(i,n){
                if(n.value==paramsList[2])
                {
                    $("#"+ n.id).attr("checked",true);
                }
            });
            $("#task_new_env_select").find("option[value='"+paramsList[3]+"']").attr("selected",true);
            break;

    }
    return false;
}


function isMachine () {
    if ($('#task_new_isMachine').is(':checked')) {
        $('#task_new_machineList').removeClass('hidden');
    } else {
        $('#task_new_machineList').addClass('hidden');
    }
}

function getBrowsers() {
    var browsers = "";
    $("input[name='task_new_browser']:checked").each(function() {
        browsers = browsers + $(this).val() + ",";
    });
    if (browsers.length > 0) {
        return browsers.substring(0, browsers.lastIndexOf(","));
    } else {
        return browsers;
    }
}

function saveNewTask(taskType) {

    //各类型任务的公共参数
    var taskName = $('#task_new_name').val();
    if(checkIsNone("task_new_name")){
        return;
    }
    var runType = $('#task_new_runType_select').val();
    var regularInfo = $('#task_new_regularInfo_select').val();
    var env = $('#task_new_env_select').val();
    var branch = $('#task_new_branch_select').val();
    var isMachine = $('#task_new_isMachine').is(':checked');
    var machine = $('#task_new_machineList').val();
    if (isMachine == false) {
        machine = "None";
    }
    var isMonited = $('#task_new_isMonited').is(':checked');
    var isSplit = $('#task_new_isSplit').is(':checked');
    var editor = $('#task_new_editor').val();
    var emailList = $('#task_new_email').val();

    //各类型任务的特殊参数
    var caseQuery = "";
    var browser = "";
    var loadConfName = "";

    switch (taskType) {
        case 1:
            caseQuery = $('#task_new_cq').val();
            break;
        case 2:
            caseQuery = $('#task_new_cq').val();
            browser = getBrowsers();
            if (checkBrowser(browser)) {
                return;
            }
            break;
        case 3:
            loadConfName = $('#task_new_loadconf_select').val();
    }

    $.ajax({
        type: 'POST',
        url: "/task/newSave",
        data: {
            'taskName': taskName,
            'taskType': taskType, //
            'runType': runType, //
            'regularInfo': regularInfo, //
            'env': env, //
            'branch': branch, //
            'caseQuery': caseQuery,
            'browser': browser, //
            'isMachine': isMachine,
            'machine': machine, //
            'isMonited': isMonited,
            'isSplit': isSplit,
            'editor': editor,
            'emailList': emailList,
            'loadConfName': loadConfName
        },
        success:function(data){
            if (data['isError']) {
                        $('#error_span').empty();
                        $('#error_span').html(data['message']);
                        $('#task_new_error_modal').modal('show');
                    } else {
                        $('#task_new_sent_modal').modal('show');
                    }
        }
    });
    
    $('#task_new_sent_modal button').on('click',function(){
        var params=$("#paramsInput").val();
        var url="/task";
        if( null!= params && params.length>0 )
        {
            url=url+"/search/"+params;
        }

        window.location.href=url;
    });

    function displayByType(type) {
        $('#task_new_table').find('tr').each(function(){
            $(this).addClass('hidden');
        });
        switch(type) {
            case "API":
                break;
            case "GUI":
                break;
            case "LOAD":
                break;
            default :

        }
    }
}


function checkIsNone(id) {
    var value = $('#' + id).val();
    if(value.trim() == ""){
        $('#error_span').empty();
        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " 不能为空，请检查并修改");
        $('#task_new_error_modal').modal('show');
        return true;
    } else {
        return false;
    }
}

function checkBrowser(browsers) {
    if (browsers.trim() == "") {
        $('#error_span').empty();
        $('#error_span').html("至少需要选择一个浏览器");
        $('#task_new_error_modal').modal('show');
        return true;
    } else if ($('#task_new_isMachine').is(':checked')) {
        var machine = $('#task_new_machineList').val();
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
            $('#task_new_error_modal').modal('show');
            return true;
        }
    } else {
        return false;
    }
}