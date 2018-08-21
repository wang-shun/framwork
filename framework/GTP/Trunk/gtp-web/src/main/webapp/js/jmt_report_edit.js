/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var sceneNamesites=null;

$(document).ready(function () {
    dateShow();
    reportSceneName();
    reportTypeSelect();
    getEnvironment();
    getVersion();
    reportTask();
    var smallsceneName=$("#hiddenValue").attr('smallsceneName');
    var env = $("#hiddenValue").attr('env');
    var resultverison=$("#hiddenValue").attr('resultverison');
    var template=$("#hiddenValue").attr('template');

    if(smallsceneName.length>0)
    {
        $("#task_type_smallSceneName").val(smallsceneName);
    }
    if(env.length>0)
    {
        $('#dimension_environment').val(env);
    }
    if(resultverison.length>0)
    {
        $('#report_timespan_begin_input').val(resultverison);
    }
    if(template.length>0 && template !='T')
    {
        $('#dimension_template').val(template);
    }

    $("#report_type_group").bind('change',function(){
        reportSceneName();
        reportTask();
    });


    $("#task_type_smallSceneName").bind('change', function () {
        getVersion();
        showTpsReport();
    })

    $("#dimension_environment").bind('change', function () {
        getVersion();
    })

    $('#report_generate_btn').bind('click', function () {
        showChart();
    });
    $('#report_begin_div').bind('click', function () {
        $('#report_timespan_begin_input').datetimepicker('show');
    });

    $("#report_history_btn").bind('click',function(){
        showHistroyLog();
    });

    $("#report_remark_btn").bind('click',function(){
        editRemark();
    });

    $('#etp_report_result').addClass('hidden');
    showSceneName_search();


});



function reportTask(){

    $('#task_search').val('');

    var groupid =$.trim($('#report_type_group').find('option:selected').val());
    var template = $.trim($('#dimension_template').find('option:selected').val()) ;
    var sceneName =  $("#hiddenValue").attr('sceneName');
    sceneName = sceneName== undefined?"":sceneName;

    $.ajax({
        type: 'POST',
        url: '/gtp-report/taskName',
        async: false,
        data: {
            'groupid': groupid,
            'sceneName':sceneName,
            'template':template,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                taskListites = data;
                $('#menu-taskName').children(':not(#menu-task-template)').remove();
                for (var i = 0; i < data.length; ++i) {
                    var item = data[i];
                    var setUpMenuDom = $('#menu-task-template').clone(true);
                    setUpMenuDom.removeAttr('id');
                    setUpMenuDom.removeAttr('class');
                    setUpMenuDom.find('a').attr('data-content', item);
                    setUpMenuDom.find('a').text(item);
                    $('#menu-taskName').append(setUpMenuDom);
                }
            }
        }
    });


    ///
}


function showSceneName_search(){

    if (sceneNamesites == null)
        return;

    $('#product_search').typeahead({
        items: 8,
        minChars:0,
        source: function(query, process) {
            return sceneNamesites;
        },
        //匹配
        matcher: function (item) {
            return ~item.toLowerCase().indexOf(this.query.toLowerCase())
        },
        //显示
        highlighter: function(item) {
            return  item ;
        },
        //选中
        updater: function(item) {
            $("#hiddenValue").attr('sceneName', item);
            reportTypeSelect();
            getEnvironment();
            getVersion();
            return item;
        },
        autoSelect:true
    });
}

function updateSceneName_search(data) {
    sceneNamesites=data;
    $('#menu-sceneName').children(':not(#menu-template)').remove();
    for (var i = 0; i < data.length; ++i) {
        var item = data[i];
        var setUpMenuDom = $('#menu-template').clone(true);
        setUpMenuDom.removeAttr('id');
        setUpMenuDom.removeAttr('class');
        setUpMenuDom.find('a').attr('data-content', item);
        setUpMenuDom.find('a').text(item);
        $('#menu-sceneName').append(setUpMenuDom);
    }
}

$('#menu-sceneName').on('click', 'a', function () {
    var sceneName = $(this).text();
    $("#product_search").val(sceneName);
    $("#hiddenValue").attr('sceneName', sceneName);
    reportTypeSelect();
    getEnvironment();
    getVersion();

});


$('#menu-taskName').on('click', 'a', function () {
    var taskValue = $(this).text();

    $("#task_search").val(taskValue);
    $("#hiddenValue").attr('taskid', $(this).attr('data-content'));
    reportSceneName();
});



//绑定大场景
function reportSceneName() {
    $('#task_type_labelName').empty();
    $('#task_type_smallSceneName').empty();
    $("#dimension_environment").empty();
    $("#report_timespan_begin_input").val('');

    $("#hiddenValue").attr('sceneName','');
    $("#product_search").val('');
    var taskid =  $("#hiddenValue").attr('taskid');
    if($("#task_search").val().length==0)
    {
        taskid='';
    }else
    {
        var first= taskid.indexOf('(');
        var end= taskid.indexOf(')');
        taskid = taskid.substring(first+1,end);
    }

    var groupid = $('#report_type_group').find('option:selected').val();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtSceneName',
        async: false,
        data: {
            'groupid': groupid,
            'template':'',
            'taskid':taskid,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                updateSceneName_search(data);
            }
        }
    });


}

function showTpsReport() {
    var smallsceneName = $.trim($('#task_type_smallSceneName').find('option:selected').text());

    var template = $.trim($("#dimension_template").find('option:selected').text());

    if (smallsceneName.length > 0 || template.length > 0) {
        $("#filter_table_tbody2").removeClass('hidden');
    } else {
        $("#filter_table_tbody2").addClass('hidden');
    }

}

//绑定大场景名称select事件
function reportTypeSelect() {

    $('#task_type_smallSceneName').empty();
    var sceneName = $("#hiddenValue").attr('sceneName');
    if (sceneName != "") {
        getSmallSceneName(sceneName, true);
    }

}

function getEnvironment() {
    var sceneName = $("#hiddenValue").attr('sceneName');
    $("#dimension_environment").empty();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtEnvironment',
        async: false,
        data: {
            'sceneName': sceneName,
            'isEnable': true
        },
        success: function (data) {
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var options = "";
                for (var i = 0; i < data.length; ++i) {
                    var option = data[i];
                    options += '<option value="' + option + '" >' + option + '</option>';
                }
                $('#dimension_environment').append(options);
            }
        }
    });
    getVersion();
}

//根据版本日期和其他条件查询，该日期下的所有时间选项
function getVersionTime()
{

    var lastdate= $('#report_timespan_begin_input').attr('last');
    var resultdate=$('#report_timespan_begin_input').val();

    if(lastdate==resultdate)
    {
        return false;
    }
    $('#report_timespan_begin_input').attr('last',resultdate);
    $('#result_time').empty();

    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallsceneName = $('#task_type_smallSceneName').find('option:selected').text();
    var labelName = $('#task_type_labelName').find('option:selected').text();
    var environment = $('#dimension_environment').find('option:selected').text();

    if(sceneName.length==0 )
    {
        return false;
    }
    $.ajax({
        type: 'POST',
        url: '/gtp-report/getVersionTime',
        async: false,
        data: {
            'sceneName': sceneName,
            'smallSceneName': smallsceneName,
            'labelName': labelName,
            'environment': environment,
            'resultdate':resultdate,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                if(data!=null) {
                    var options = "";
                    for (var i = 0; i < data.length; ++i) {
                        var option = data[i];
                        options += '<option value="' + option + '" >' + option + '</option>';
                    }
                    $('#result_time').append(options);
                }
            }
        }
    });
}

//根据相关条件查询版本号，取最新的绑定版本日期和时间
function getVersion() {
    $('#result_time').empty();
    var sceneName =$("#hiddenValue").attr('sceneName');
    var smallsceneName = $('#task_type_smallSceneName').find('option:selected').text();
    var environment = $('#dimension_environment').find('option:selected').text();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtgetVersion',
        async: false,
        data: {
            'sceneName': sceneName,
            'smallSceneName': smallsceneName,
            'labelName': '',
            'environment': environment,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                if(data['message']!=null)
                {
                    $('#report_timespan_begin_input').val(data['message']);
                }
                data = data['data'];

                if(data!=null)
                {
                    var options = "";
                    for (var i = 0; i < data.length; ++i) {
                        var option = data[i];

                        options += '<option value="' + option + '" >' + option + '</option>';
                    }

                    $('#result_time').append(options);
                }
            }
        }
    });
}

//得到小场景名称并绑定
function getSmallSceneName(sceneName, isEnable) {
    $('#task_type_smallSceneName').empty();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtSmallsceneName',
        async: false,
        data: {
            'sceneName': sceneName,
            'isEnable': isEnable
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var options = "";
                for (var i = 0; i < data.length; ++i) {
                    var option = data[i];

                    options += '<option value="' + option + '" >' + option + '</option>';
                }

                $('#task_type_smallSceneName').append(options);
            }
        }
    });
}



function dateShow() {
    $('#report_timespan_begin_input').val('');
    $('#report_timespan_begin_input').datetimepicker({
        timepicker: false,
        format: 'Ymd',
        onChangeDateTime:function(dp,$input){
            getVersionTime($input);
        }
    });
}


function showError(title, value) {
    $('#report_error_h4').empty();
    $('#report_error_h4').text(title);
    $('#report-error-body').empty();
    $('#report-error-body').text(value);
    $('#report_error_modal').modal('show');
    return false;
}


function bandTable(data,sceneName){

    $("#report_list").removeClass('hidden');
    var i=0;
    $('#report_title').html(sceneName);
    $('#report_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        "bDestroy":true,
        data: data,
        columns: [
            {data: 2},
            {data: 0},
            {data: 1},
            {data: 5},
            {data: 3},
            {data: 4},
            {data :5}
        ],
        language:{
            "emptyTable":     "没有可用数据",
            "info":           "总共_TOTAL_, 当前页为 _START_ 到 _END_ ",
            "infoEmpty":      "总共 0",
            "infoFiltered":   "(从 _MAX_ 项中过滤)",
            "infoPostFix":    "",
            "thousands":      ",",
            "lengthMenu":     "每页显示 _MENU_ 行",
            "loadingRecords": "正在加载...",
            "processing":     "后台处理中...",
            "search":         "搜索",
            "zeroRecords":    "没有找到匹配结果",
            "paginate": {
                "first":      "首页",
                "last":       "尾页",
                "next":       "下一页",
                "previous":   "上一页"
            },
            "aria": {
                "sortAscending":  ": 按列升序排列",
                "sortDescending": ": 按列降序排列"
            }
        },
        columnDefs: [
            {
                targets: [6],
                render: function (data, type, full) {

                    return '<div class="input-group" style="white-space: nowrap;"><a  class="btn btn-primary" id="btneditscene'+i+'" data='+data.replace(' ','T')+' onclick="showEdit(this.id)">改场景</a>'+
                            '<a  class="btn btn-primary" id="btneditenable'+i+'" data='+data.replace(' ','T')+' style="margin-left:10px;" onclick="editEnable(this.id)">设为无效</a>'+
                            '<a  class="btn btn-primary" id="btneditremark'+i+'" data='+data.replace(' ','T')+' style="margin-left:10px;" onclick="showRemark(this.id)">改备注</a>'+
                            '</div>';
                }

            }
        ]

    });

}

function showRemark(id)
{
    var obj=$("#"+id);
    var createtime=obj.attr('data').replace('T',' ');
    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallSceneName = $("#hiddenValue").attr('smallsceneName');
    var template = $("#hiddenValue").attr('template');
    var resultVersion = $("#hiddenValue").attr('resultverison');
    var environment = $("#hiddenValue").attr('env');
    var timeVersion= $("#hiddenValue").attr('timeVersion');


    $("#jmt_errorRemark_span").html('');

    $("#sp_jmt_sceneName").html(sceneName);
    $("#sp_jmt_smallsceneName").html(smallSceneName);
    $("#sp_jmt_resultversion").html(resultVersion+"_"+timeVersion);
    $("#sp_jmt_env").html(environment);
    $("#sp_jmt_template").html(template);
    $("#sp_jmt_createTime").html(createtime);
    $("#jmt_createTime").val(createtime);
    $("#jmt_remark").val('');

    $.ajax({
        url: "/gtp-report/getjmtRemark",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "timeVersion":timeVersion.replace('NONE',''),
            "environment": environment,
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "template": template,
            "createtime":createtime
        },
        success: function (data) {
            if (data['isError']) {
                $("#jmt_errorRemark_span").html( data['message']);
            } else {
                $("#jmt_editRemark_save_btn").attr('remarkid',data['data'].id);
                $("#jmt_remark").val(data['data'].remark);
            }
        }

    });

    $("#report_editRemark_modal").modal('show');

}




function editEnable(id)
{
    var obj=$("#"+id);
    var createtime=obj.attr('data').replace('T',' ');
    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallSceneName = $("#hiddenValue").attr('smallsceneName');
    var template = $("#hiddenValue").attr('template');
    var resultVersion = $("#hiddenValue").attr('resultverison');
    var environment = $("#hiddenValue").attr('env');
    var timeVersion= $("#hiddenValue").attr('timeVersion');

    if(!confirm("请确认是否将符合条件的数据置为无效 !"))
    {
     return false;
    }

    $.ajax({
        url: "/gtp-report/editJmtEnable",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "timeVersion":timeVersion,
            "environment": environment,
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "template": template,
            "createTime":createtime

        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);

            } else {
                $('#report_edit_modal').modal('hide');
                window.location.href=window.location.href;
            }
        }

    });

}


function jmteditremark()
{
    var id=$("#jmt_editRemark_save_btn").attr('remarkid');
    if(id==null|| id=='' )
    {
        $("#jmt_errorRemark_span").html('Remark is error!');
        return false;
    }

    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallSceneName = $("#hiddenValue").attr('smallsceneName');
    var template = $("#hiddenValue").attr('template');
    var resultVersion = $("#hiddenValue").attr('resultverison');
    var environment = $("#hiddenValue").attr('env');
    var remark=$("#jmt_remark").val();
    var createtime=$("#jmt_createTime").val();
    var timeVersion=  $("#hiddenValue").attr('timeVersion');
    if(remark.length==0)
    {
        $("#jmt_errorRemark_span").html('remark is error!');
        return false;
    }


    $.ajax({
        url: "/gtp-report/editjmtRemark",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "timeVersion":timeVersion,
            "environment": environment,
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "template": template,
            "id":id,
            "remark":remark,
            "createtime":createtime

        },
        success: function (data) {

            if (data['isError']) {
                $("#jmt_errorRemark_span").html( data['message']);
            } else {
                $('#report_editRemark_modal').modal('hide');
            }
        }

    });

}

function jmtedit()
{
    var sceneName =  $("#jmt_sceneName").val();
    var old_sceneName =  $("#jmt_sceneName").attr('old');
    var smallSceneName =  $("#jmt_smallsceneName").val();
    var template = $("#jmt_template").val();
    var resultVersion =  $("#hiddenValue").attr('resultverison');
    var environment = $("#jmt_env").val();
    var createtime=$("#jmt_createTime").val();
    var timeVersion= $("#hiddenValue").attr('timeVersion');

    $("#jmt_error_span").html('');

    if(sceneName == old_sceneName )
    {
       $("#jmt_error_span").html('场景没有变化!');
        return false;
    }
    $.ajax({
        url: "/gtp-report/editJmtSceneName",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "timeVersion":timeVersion,
            "environment": environment,
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "template": template,
            "oldsceneName":old_sceneName,
            "createTime":createtime

        },
        success: function (data) {

            if (data['isError']) {
                $("#jmt_error_span").html( data['message']);
            } else {
                $('#report_edit_modal').modal('hide');
                window.location.href=window.location.href;
            }
        }

    });

}


function showEdit(id)
{
    $("#jmt_error_span").html('');
    var obj=$("#"+id);
    var createTime=obj.attr('data');

    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallSceneName = $("#hiddenValue").attr('smallsceneName');
    var template = $("#hiddenValue").attr('template');
    var resultVersion = $("#hiddenValue").attr('resultverison');
    var environment = $("#hiddenValue").attr('env');
    var timeVersion =$("#hiddenValue").attr('timeversion');

    $("#report_edit_modal").modal('show');
    $("#jmt_sceneName").val(sceneName);
    $("#jmt_sceneName").attr("old",sceneName);
    $("#jmt_smallsceneName").val(smallSceneName);
    $("#jmt_resultversion").val(resultVersion+"_"+timeVersion);
    $("#jmt_env").val(environment);
    $("#jmt_template").val(template);
    $("#jmt_createTime").val(createTime.replace('T',' '));
}

function showChart() {
    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallSceneName = $('#task_type_smallSceneName').find('option:selected').text();
    var template = $('#dimension_template').find('option:selected').val();
    var resultVersion = $("#report_timespan_begin_input").val();
    var environment = $('#dimension_environment').find('option:selected').text();
    var timeVersion=$("#result_time").val();
    if (sceneName == "") {
        return showError('场景发生异常', '请选择场景!');
    }


    if (resultVersion == "") {
        return showError('版本不能为空', '请选择日期版本!');
    }
    if(timeVersion =="")
    {
        return showError('版本发生异常', '时间版本不能为空!');
    }
    if (environment == "") {
        return showError('环境不能为空', '请选择环境!');
    }

    var taskid =  $("#hiddenValue").attr('taskid');
    if($("#task_search").val().length==0)
    {
        taskid='';
    }else
    {
        var first= taskid.indexOf('(');
        var end= taskid.indexOf(')');
        taskid = taskid.substring(first+1,end);
    }

    $('#report_label_div').addClass('hidden');

    $("#hiddenValue").attr('sceneName', sceneName);
    $("#hiddenValue").attr('smallsceneName', smallSceneName);
    $("#hiddenValue").attr('resultverison', resultVersion);
    $("#hiddenValue").attr('env', environment);
    $("#hiddenValue").attr('template', template);
    $("#hiddenValue").attr('timeversion',timeVersion);

    $.ajax({
        url: "/gtp-report/getjmthistory",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "timeVersion":timeVersion,
            "environment": environment,
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "taskid":taskid,
            "template": template

        },//report_container_
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $("#hiddenValue").attr('sceneName', '');
                $("#hiddenValue").attr('smallSceneName', '');
                return false;
            } else {

                data = data['data'];
                bandTable(data,sceneName);

                return true ;
            }
        }

    });

}


function showHistroyLog()
{
    showLog();
    $("#report_historyLog_modal").modal('show');
}

function showLog()
{
    $('#report_log_list').dataTable().fnDestroy();
    $('#report_log_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        "bDestroy":true,
        "aaSorting":[7,'desc'],
        ajax: {
            "url": "/gtp-report/getjmtedithistory",
            "type": "POST"
        },
        columns: [
            {data: ["user_Name"]},
            {data: ["sceneName"]},
            {data: ["smallSceneName"]},
            {data: ["environment"]},
            {data: ["template"]},
            {data: ["version"]},
            {data: ["dataCreateTime"]},
            {data: ["editTime"]}
        ],
        columnDefs: [
            {
                targets: [0],
                render: function (data, type, full) {
                    return "<div >"+full["user_Name"]+"</div><div>("+full['user_IP']+")</div><div style='color: red;'>"+full["editTypeValue"]+"</div>"
                }
            },
            {
                targets: [1],
                render: function (data, type, full) {
                    return "<div  >"+full["sceneName"]+"</div ><div style='color: red;'>("+full['sceneName_Old']+")</div>"
                }
            },
            {
                targets: [5],
                render: function (data, type, full) {
                    return "<div  >"+full["version"]+"_"+(full["timeVersion"]==null||full["timeVersion"]==""?"NONE":full["timeVersion"])+"</div>"
                }
            },
            {
                targets: [7],
                data: ['editTime'],
                render: function (data, type, full) {
                    return   timeStamp2String(data);
                }
            }
        ]
    });

}









