/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the newor.
 */

$(document).ready(function () {

    bandHistory();


    $('#task_modal_confirm_btn').on('click', function() {
        var id = $(this).attr('data-id');
        realDelete(id);
    });
    $("#task_new_save_btn").attr('isCreating','0');

});


function bandHistory()
{
    $('#task_list').DataTable({
        "bLengthChange": true, //每页显示条数可选
        "processing": false,
        "bDestroy":true,
        "sorting":[0, "desc"],
        ajax: {
            "url": "/gui-pageTest/getHistory",
            "type": "POST"
        },
            columns: [
            { data: [0] },//ID
            { data: [1] },//Name
            { data: [13] },
            { data: [9] },//LastRunTime
            { data: [11] }//Status
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
                targets: [2],//Timing
                data: [13],//RunTiming
                render: function (data, type, full) {
                    return "<a id='url"+full[0]+"' href='"+data+"' title='"+array2SplittedStr(browserTypes(full[4]))+"' target='_blank'>"+data+"</a>";
                }
            },
            {
                targets: [3],//Timing
                data: [9],//RunTiming
                render: function (data, type, full) {
                    return getTiming(data,full[6],full[8]);
                }
            },

            {
                targets: [4],//Status
                render: function (data, type, full) {
                    var internalContent = drawTaskStatus(data, full);
                    return "<span id='task_status_span_" + full[0] + "' data-id='" + full[0] + "' class='task-status-span'>" + internalContent + "</span>"
                }
            },
            {
                targets: [5],//Timing
                data: [0],//RunTiming
                render: function (data, type, full) {
                    return showEditContent(data, full);
                    //return "<a href='/task/report/"+full[0]+"/GUI' target='_blank'>日志</a>";
                }
            }
        ]
    });
}

function showEditContent(data, full) {
    var content = " <div  data-id='"+data+"' role='menu'>\n";

    if (alreadyLogin) {
        if ('Running' === full[11]){//full 为包含当前行所有列的json串,inArray函数
            content += "<span> <a class='task_stop_opt' href='javascript:stopTask("+data+");'>停止</a> |</span>\n";
        } else if ('Assigned' == full[11] || 'Sent' == full[11] || 'Splitted' == full[11] || 'Waiting' == full[11]) {
            content += "<span> <a class='task_start_opt' href='javascript:void(0);' disabled>启动</a> |</span>\n";
        } else {
            content += "<span> <a class='task_start_opt' href='javascript:startTask("+data+");' disabled>启动</a> |</span>\n";
        }
    } else {
        if ('Running' === full[11]){//full 为包含当前行所有列的json串,inArray函数
            content += "<span> <a class='task_stop_opt' href='javascript:intervalActiveLogin();'>停止</a> |</span>\n";
        } else {
            content += "<span> <a class='task_start_opt' href='javascript:intervalActiveLogin();'>启动</a> |</span>\n";
        }
    }
    content += "<span><a id='delete_"+data+"' class='task_delete_opt' href='javascript:confirmDel("+data+");'>删除</a> |</span>\n\
                    <span> <a class='task_report_opt' href='/task/report/"+data+"/"+full[10]+"'>报告</a> |</span>\n\
                    <span> <a class='task_report_opt' href='/task/report/jenkins_report/" + data + "' target='_blank'>Jenkins</a></span>\n\
                </div>\n\
           ";
    return content;
}






function genOptContent(data, full) {
    var content = "<div class='btn-group task-operation-div dropup'>\n\
                        <button class='btn btn-default dropdown-toggle' data-toggle='dropdown' aira-expanded='false'>\n\
                                                操作 <span class='caret'></span>\n\
                        </button>\n\
                        <ul class='dropdown-menu pull-right task-operation-ul' data-id='"+data+"' role='menu'>\n";

    if (alreadyLogin) {
        if ('Running' === full[11]){//full 为包含当前行所有列的json串,inArray函数
            content += "<li><a class='task_stop_opt' href='javascript:stopTask("+data+");'>停止</a></li>\n";
        } else if ('Assigned' == full[11] || 'Sent' == full[11] || 'Splitted' == full[11] || 'Waiting' == full[11]) {
            content += "<li><a class='task_start_opt' href='javascript:void(0);' disabled>启动</a></li>\n";
        } else {
            content += "<li><a class='task_start_opt' href='javascript:startTask("+data+");' disabled>启动</a></li>\n";
        }
    } else {
        if ('Running' === full[11]){//full 为包含当前行所有列的json串,inArray函数
            content += "<li><a class='task_stop_opt' href='javascript:intervalActiveLogin();'>停止</a></li>\n";
        } else {
            content += "<li><a class='task_start_opt' href='javascript:intervalActiveLogin();'>启动</a></li>\n";
        }
    }
    content += "<li><a id='delete_"+data+"' class='task_delete_opt' href='javascript:confirmDel("+data+");'>删除</a></li>\n\
                    <li><a class='task_report_opt' href='/task/report/"+data+"/"+full[10]+"'>报告</a></li>\n\
                    <li><a class='task_report_opt' href='/task/report/jenkins_report/" + data + "' target='_blank'>Jenkins</a></li>\n\
                </ul>\n\
            </div>";
    return content;
}







//=================================刷新任务状态=================================
function drawTaskStatus (data, full) {
    switch (data) {
        case "Created":
            return "<strong><span title='新添加的任务(Created)' class='text-info'>新添加</span><span class='span-bg'>Created</span></strong>";
            break;
        case "Waiting":
            return "<strong><span title='任务已进入队列，正在等待资源(Waiting)' class='text-warning'>排队中</span><span class='span-bg'>Waiting</span></strong>";
            break;
        case "Splitted":
            return "<strong><span title='任务已执行完拆分，正在等待资源(Splitted)' class='text-info'>排队中</span><span class='span-bg'>Splitted</span></strong>";
            break;
        case "Sent":
            return "<strong><span title='任务已发送到执行机，即将运行(Sent)' class='text-primary'>已发送</span><span class='span-bg'>Sent</span></strong>";
            break;
        case "Running":
            return "<strong><span title='任务正在运行(Running)' class='text-primary'>运行中</span><span class='span-bg'>Running</span></strong>";
            break;
        case "Completed":
            return "<strong><span title='任务已成功执行完成(Completed)' class='text-success'>已完成</span><span class='span-bg'>Completed</span></strong>";
            break;
        case "Aborted":
            return "<strong><span title='任务因超时等原因被中断(Aborted)' class='text-danger'>中断</span><span class='span-bg'>Aborted</span></strong>";
            break;
        case "Error":
            return "<strong><span title='任务执行过程中出错(Error)' class='text-danger'>错误</span><span class='span-bg'>Error</span></strong>";
            break;
        case "Stopped":
            return "<strong><span title='任务被手动停止(Stopped)' class='text-warning'>已停止</span><span class='span-bg'>Stopped</span></strong>";
            break;
        default :
            return "<strong><span title='任务处于未知状态(Error)' class='text-danger'>异常</span><span class='span-bg'>Error</span></strong>";
    }
}


//=================================启停任务=================================
function startTask (id) {
    $('#task_sending_modal').modal('show');
    $.ajax({
        "type": 'POST',
        "url": '/task/start/'+id,
        success: function(data) {
            $('#task_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#task_error_modal').modal('show');
            } else {
                $('#sent_span').empty();
                $('#sent_span').html("任务已进入队列等待执行");
                $('#task_sent_modal').modal('show');
            }
        }
    });

    bandHistory();
}

function stopTask (id) {
    $('#task_sending_modal').modal('show');
    $.ajax({
        "type": 'POST',
        "url": '/task/stop/'+id,
        success: function(data) {
            $('#task_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#task_error_modal').modal('show');
            } else {
                $('#sent_span').empty();
                $('#sent_span').html("任务已成功停止");
                $('#task_sent_modal').modal('show');
            }
        }
    });
}


function confirmDel (id) {
    var name = $("#"+"delete_"+id).closest('tr').find('td:eq(2)').html();
    var brew = $("#"+"url"+id).attr("title");
    $('#task_modal_taskid').html(id);
    $('#task_modal_taskname').html(name);

    $('#task_modal_brewname').html(brew);
    $('#task_modal_confirm_btn').attr('data-id',id);
    $('#task_confirm_modal').modal('show');
}



function realDelete (id) {
    $('#task_confirm_modal').modal('hide');
    $('#task_sending_modal').modal('show');
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/task/delete/"+id,
        success: function (data) {
            $('#task_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#task_error_modal').modal('show');
            } else {
                $('#sent_span').empty();
                $('#sent_span').html("删除任务成功");
                $('#task_sent_modal').modal('show');
                cleanPage();
            }
        }
    });
}



function saveOpenUrl()
{
   var isCreating = $("#task_new_save_btn").attr('isCreating');

    if(isCreating!=null && isCreating =='1')
    {
        return ;
    }

    var url= $.trim($("#open_url_address").val());
    var browser="";
    var index =0;
    $("input[name='browser']").each(function(n,i){
        if(this.checked)
        {

            browser += $.trim(this.value) +";";
            index = index +1 ;
        }
    });

    if(url.length==0)
    {
        $('#error_span').empty();
        $('#error_span').html(" url不能为空，请检查并修改");
        $('#task_new_error_modal').modal('show');
        return ;
    }else if(!isURL(url))
    {
        $('#error_span').empty();
        $('#error_span').html(" url格式验证不通过，请检查并修改");
        $('#task_new_error_modal').modal('show');
        return ;
    }
    browser= $.trim(browser);

    if(browser.length==0)
    {
        $('#error_span').empty();
        $('#error_span').html(" 浏览器不能为空，请检查并修改");
        $('#task_new_error_modal').modal('show');
        return ;
    }

    var editor = $("#editor").val() ;
    if(editor.length ==0 )
    {
        $('#error_span').empty();
        $('#error_span').html(" 请先登录 ");
        $('#task_new_error_modal').modal('show');
        return ;
    }
    if(confirm("请确认是否加入队列进行测试！"))
    {
        $("#task_new_save_btn").attr('isCreating','1');
        createTaskInfo(url,browser,editor);
    }

}

function isURL(url) {
    var strRegex = "^(https|http|ftp|rtsp|mms)://[a-z0-9A-Z]{3}\.[a-z0-9A-Z][a-z0-9A-Z]{0,61}?[a-z0-9A-Z]\.com|net|cn|cc (:s[0-9]{1-4})?/$";
    var re = new RegExp(strRegex);
    if (re.test(url)){
        return true;
    } else {
        return false;
    }
}


function cancel()
{
    $("#open_url_address").val('');
    $("input[name='browser']").each(function(n,i){
        this.checked =false ;
    });
}


function createTaskInfo(url,browser,editor)
{
    $.ajax({
        type: 'POST',
        url: "/gui-pageTest/save",
        data: {
            'url': url,
            'browser': browser,
            'editor': editor
        },
        success:function(data){
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#task_new_error_modal').modal('show');
            } else {
                $('#task_new_sent_modal').modal('show');
                cleanPage();
            }
            $("#task_new_save_btn").attr('isCreating','0');
        }
    });



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
    var browser = "None";
    var loadConfName = "";

    switch (taskType) {
        case 1:
            caseQuery = $('#task_new_cq').val();
            break;
        case 2:
            caseQuery = $('#task_new_cq').val();
            browser = $('#task_new_browser_select').val();
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
        $('#error_span').html(" url不能为空，请检查并修改");
        $('#task_new_error_modal').modal('show');
        return true;
    } else {
        return false;
    }
}


function cleanPage()
{
    cancel();

    bandHistory();
}