$(document).ready(function () {

    initmap();

    var taskSingleId;
    var taskTable = $('#task_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        "sorting":[7, "desc"],
        ajax: {
            "url": "/task/all",
            "type": "POST"
        },
        columns: [
            { data: [0] },
            { data: [0] },//ID
            { data: [1] },//Name
            { data: [2] },//Env
            { data: [3] },//Group
            { data: [5] },//Machine
            { data: [7] },//Timing
            { data: [9] },//LastRunTime
            { data: [10] },//Type
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
                targets: [0],//Selector
                data: [0],//TaskId
                render: function (data, type, full) {
                    return "<input id='checkBox_" + data + "' type = 'checkBox'/>";
                }
            },
            {
                targets: [5],//machine
                data: [5],
                render: function (data, type, full) {
                    return "<span id='task_machine_span_" + full[0] + "' data-id='" + full[0] + "', class='task-machine-span'>" + data + "</spam>";
                }
             },
            {
                targets: [6],//Timing
                data: [6],//RunTiming
                render: function (data, type, full) {
                    return getTiming(data,full[6],full[8]);
                }
            },
            {
                targets: [7],//LastRunTime
                data: [7],//LastRunTime
                render: function (data, type, full) {
                    return "<span id='task_lastruntime_span_" + full[0] + "' data-id='" + full[0] + "' class='task-lastruntime-span'>" + timeStamp2String(data) + "</span>";
                    //return data;
                }
            },

            {
                targets: [8],
                data: [8],
                render: function (data, type, full) {
                    return "<span id='task_type_span_" + full[0] + "' data-id='" + full[0] + "' class='task-type-span'>" + data + "</span>";
                }
            },

            {
                targets: [9],//Status
                data: [11],
                render: function (data, type, full) {
                    var internalContent = drawTaskStatus(data, full);
                    return "<span id='task_status_span_" + full[0] + "' data-id='" + full[0] + "' class='task-status-span'>" + internalContent + "</span>"
                }
            },
            {
                targets: [10],//Operations
                data: [0],
                render: function (data, type, full) {
                    return genOptContent(data, full);
                }
            }
        ]
    });

    $('select.column_filter').on( 'change', function () {
        filterColumn($(this).parents('td').attr('data-column') );
    } );

    $('#task_modal_confirm_btn').on('click', function() {
        var id = $(this).attr('data-id');
        realDelete(id);
    });

    $('#task_sent_modal button').on('click', function () {
        var params = $("#paramsInput").val();
        var url = "/task";
        if (null != params && params.length >0 ) {
            url = url + "/search/" + params;
        }
        window.location.href = url;

    });

    setInterval(refreshLastRunTimeStatus, 3000);

    $('select.column_filter').each(function() {
        filterColumn($(this).parents('td').attr('data-column') );
    });
    removeTestButton();

    setparams();

    initState();
});



function genOptContent(data, full) {
    var content = "<div class='btn-group task-operation-div dropup'>\n\
                        <button class='btn btn-default dropdown-toggle' data-toggle='dropdown' aira-expanded='false'>\n\
                                                操作 <span class='caret'></span>\n\
                        </button>\n\
                        <ul class='dropdown-menu pull-right task-operation-ul' data-id='"+data+"' role='menu'>\n";

    if (alreadyLogin) {
        if ('Running' === full[11]){//full 为包含当前行所有列的json串,inArray函数
            content += "<li><a class='task_stop_opt' href='javascript:stopTask("+data+");'>停止</a></li>\n";
            if ('LOAD' == full[10]) {
                content += "<li><a class='task_test_opt' href='javascript:void(0);'>测试</a></li>\n";
            }
        } else if ('Assigned' == full[11] || 'Sent' == full[11] || 'Splitted' == full[11] || 'Waiting' == full[11]) {
            content += "<li><a class='task_start_opt' href='javascript:canNotStart();' disabled>启动</a></li>\n";
            if ('LOAD' == full[10]) {
                content += "<li><a class='task_test_opt' href='javascript:canNotStart();'>测试</a></li>\n";
            }
        } else {
            content += "<li><a class='task_start_opt' href='javascript:startTask("+data+");' disabled>启动</a></li>\n";
            if ('LOAD' == full[10]) {
                content += "<li><a class='task_test_opt' href='javascript:testTask("+data+");'>测试</a></li>\n";
            }
        }
    } else {
        if ('Running' === full[11]){//full 为包含当前行所有列的json串,inArray函数
            content += "<li><a class='task_stop_opt' href='javascript:intervalActiveLogin();'>停止</a></li>\n";
            if ('LOAD' == full[10]) {
                content += "<li><a class='task_test_opt' href='javascript:intervalActiveLogin();'>测试</a></li>\n";
            }
        } else {
            content += "<li><a class='task_start_opt' href='javascript:intervalActiveLogin();'>启动</a></li>\n";
            if ('LOAD' == full[10]) {
                content += "<li><a class='task_test_opt' href='javascript:intervalActiveLogin();'>测试</a></li>\n";
            }
        }
    }
    content += "<li><a class='task_edit_opt' href='/task/edit/"+data+"'>编辑</a></li>\n\
                    <li><a class='task_copy_opt' href='/task/copy/"+data+"'>复制</a></li>\n\
                    <li><a id='delete_"+data+"' class='task_delete_opt' href='javascript:confirmDel("+data+");'>删除</a></li>\n\
                    <li><a id='details_"+data+"'class='task_details_opt' href='javascript:showDetails("+data+");'>详情</a></li>\n\
                    <li><a class='task_report_opt' href='/task/report/"+data+"/"+full[10]+"'>报告</a></li>\n\
                    <li><a class='task_report_opt' href='/task/report/jenkins_report/" + data + "' target='_blank'>Jenkins</a></li>\n\
                </ul>\n\
            </div>";
    return content;
}

function setparams(){
    var params=$("#paramsInput").val();
    if( null== params || params=='' )
        return false;
    //$("#openNew").attr("href",$("#openNew").attr("href")+params)
    var paramsList=params.split('_');
    switch (paramsList.length)
    {
        case 2:
            $("#col4_filter").find("option[value='"+paramsList[1]+"']").attr("selected",true);
            filterColumn($("#col4_filter").parents('td').attr('data-column') );
            break;
        case 3:
            $("#col4_filter").find("option[value='"+paramsList[1]+"']").attr("selected",true);
            filterColumn($("#col4_filter").parents('td').attr('data-column') );
            $("#col8_filter").find("option[value='"+paramsList[2]+"']").attr("selected",true);
            filterColumn($("#col8_filter").parents('td').attr('data-column') );
            break;
        case 4:
            $("#col4_filter").find("option[value='"+paramsList[1]+"']").attr("selected",true);
            filterColumn($("#col4_filter").parents('td').attr('data-column') );
            $("#col8_filter").find("option[value='"+paramsList[2]+"']").attr("selected",true);
            filterColumn($("#col8_filter").parents('td').attr('data-column') );
            $("#col3_filter").find("option[value='"+paramsList[3]+"']").attr("selected",true);
            filterColumn($("#col3_filter").parents('td').attr('data-column') );
            break;
        case 5:
            $("#col4_filter").find("option[value='"+paramsList[1]+"']").attr("selected",true);
            $("#col8_filter").find("option[value='"+paramsList[2]+"']").attr("selected",true);
            $("#col3_filter").find("option[value='"+paramsList[3]+"']").attr("selected",true);
            searchColumnbyTaskInfoID(paramsList[4]);
            break;

    }
    return false;
}

//=================================删除任务=================================

function confirmDel (id) {
    var name = $("#"+"delete_"+id).closest('tr').find('td:eq(2)').html();
    $('#task_modal_taskid').html(id);
    $('#task_modal_taskname').html(name);
    $('#task_modal_confirm_btn').attr('data-id',id);
//    $('#task_modal_confirm_btn').attr('href','/task/delete/'+id).val();
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
            }
        }
    });
}


//=================================任务详情=================================
function showDetails (id) {
    $('#task_sending_modal').modal('show');
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "/task/details/"+id,
        async: false,
        success: function (data) {
            $('#task_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#task_error_modal').modal('show');
            } else {
                insertDetails(data['data']);
                $('#task_details_modal').modal('show');
            }
        }
    });
}

function insertDetails(data) {
    $('.task_details_table_span').each(function (i) {
        if (7 == i) {
            $(this).html(timeStamp2String(data[i]));
        } else if (4 == i) {
            $(this).html(array2SplittedStr(browserTypes(data[i])));
        } else {
            $(this).html(data[i]);
        }
    });
    $('#task_detail_browser_tr').removeClass('hidden');
    $('#task_detail_loadconfname_tr').removeClass('hidden');

    if(data[2] == "API") {
        $('#task_detail_browser_tr').addClass('hidden');
        $('#task_detail_loadconfname_tr').addClass('hidden');
    } else if(data[2] == "GUI") {
        $('#task_detail_loadconfname_tr').addClass('hidden');
    } else if(data[2] == "LOAD") {
        $('#task_detail_browser_tr').addClass('hidden');
    }
}


//=================================过滤器=================================
function filterColumn ( i ) {
    if ('None' === $('#col'+i+'_filter').val()) {
        $('#task_list').DataTable().column(i).search(
            '').draw();
    } else {
        $('#task_list').DataTable().column(i).search(
            $('#col'+i+'_filter').val()
        ).draw();
    }
}

function searchColumnbyTaskInfoID(id){
    $('#task_list').DataTable().column(1).search(
        id
    ).draw();
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
}

function canNotStart() {
    $('#error_span').empty();
    $('#error_span').html("任务已处于执行状态，不允许重复执行");
    $('#task_error_modal').modal('show');
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

function testTask(id) {

    $('#task_sending_modal').modal('show');
    $.ajax({
        "type": 'POST',
        "url": '/task/startTest/'+id,
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

}

var startmap=new Map();

function initmap()
{
    startmap.put('None','全部');
    startmap.put('Created','新添加的任务(新添加)');
    startmap.put('Waiting','任务已进入队列，正在等待资源(排队中)');
    startmap.put('Splitted','任务已执行完拆分，正在等待资源(排队中)');
    startmap.put('Sent','任务已发送到执行机，即将运行(已发送)');
    startmap.put('Running','任务正在运行(运行中)');
    startmap.put('Completed','任务已成功执行完成(已完成)');
    startmap.put('Aborted','任务因超时等原因被中断(中断)');
    startmap.put('Error','任务执行过程中出错(错误)');
    startmap.put('Stopped','任务被手动停止(已停止)');
    startmap.put('Aborted','任务因超时等原因被中断(中断)');
    startmap.put('Deleted','已删除');
    startmap.put('Assigned','任务已分配未下发');
}

function initState()
{
    $("#col9_filter option").each(function(){
        var values= startmap.get($(this).val());
        $(this).text(values);
    });
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

function getIdStatusLastRuntimeMap() {
    var idStatusMap;
    $.ajax({
        url:'/task/status',
        type:'GET',
        async:false,
        dataType:'json',
        success:function(data) {
            idStatusMap = data;
        }
    });
    return idStatusMap;
}

function refreshLastRunTimeStatus() {
    var idStatusLastRunTimeJson = getIdStatusLastRuntimeMap();
    refreshStatus(idStatusLastRunTimeJson);
    refreshLastRunTime(idStatusLastRunTimeJson);
    refreshAgent(idStatusLastRunTimeJson);
}

function refreshStatus(idStatusLastRunTimeJson) {
    $('.task-status-span').each(function() {
        var id = $(this).attr('data-id');
        var status = $(this).find('span:first-child').attr('title');
        var newStatusLastTime = idStatusLastRunTimeJson[id];
        var newStatus = newStatusLastTime[1];

        if (newStatus !== status) {
            var type = $('#task_type_span_' + id).html()
            $(this).empty();
            $(this).append(drawTaskStatus(newStatus));
            if (alreadyLogin) {
                if (newStatus == "Running") {
                    var thisA = $(".task-operation-ul[data-id='"+id+"']").find('li:first-child').find('a:first-child');
                    thisA.html("停止");
                    thisA.attr('href',"javascript:stopTask("+id+");");
                    thisA.removeClass('task_start_opt').addClass('task_stop_opt');
                    if ('LOAD' == type) {
                        var this2A = $(".task-operation-ul[data-id='"+id+"']").find('li:eq(1)').find('a:first-child');
                        this2A.attr('href',"javascript:void(0);");
                    }

                } else if ('Assigned' == newStatus || 'Sent' == newStatus || 'Splitted' == newStatus || 'Waiting' == newStatus){
                    var thisA = $(".task-operation-ul[data-id='"+id+"']").find('li:first-child').find('a:first-child');
                    thisA.html("启动");
                    thisA.attr('href',"javascript:canNotStart();");
                    thisA.removeClass('task_stop_opt').addClass('task_start_opt');

                    if ('LOAD' == type) {
                        var this2A = $(".task-operation-ul[data-id='" + id + "']").find('li:eq(1)').find('a:first-child');
                        this2A.attr('href', "javascript:void(0);");
                    }
                } else {//end状态的任务
                    var thisA = $(".task-operation-ul[data-id='"+id+"']").find('li:first-child').find('a:first-child');
                    thisA.html("启动");
                    thisA.attr('href',"javascript:startTask("+id+");");
                    thisA.removeClass('task_stop_opt').addClass('task_start_opt');

                    if ('LOAD' == type) {
                        var this2A = $(".task-operation-ul[data-id='" + id + "']").find('li:eq(1)').find('a:first-child');
                        this2A.attr('href', "javascript:testTask(" + id + ");");
                    }
                }
            } else {
                if (newStatus === "Running") {
                    var thisA = $(".task-operation-ul[data-id='"+id+"']").find('li:first-child').find('a:first-child');
                    thisA.html("停止");
                    thisA.attr('href',"javascript:intervalActiveLogin();");
                    thisA.removeClass('task_start_opt').addClass('task_stop_opt');

                    if ('LOAD' == type) {
                        var this2A = $(".task-operation-ul[data-id='" + id + "']").find('li:eq(1)').find('a:first-child');
                        this2A.attr('href', "javascript:intervalActiveLogin();");
                    }
                } else {
                    var thisA = $(".task-operation-ul[data-id='" + id + "']").find('li:first-child').find('a:first-child');
                    thisA.html("启动");
                    thisA.attr('href', "javascript:intervalActiveLogin();");
                    thisA.removeClass('task_stop_opt').addClass('task_start_opt');

                    if ('LOAD' == type) {
                        var this2A = $(".task-operation-ul[data-id='" + id + "']").find('li:eq(1)').find('a:first-child');
                        this2A.attr('href', "javascript:intervalActiveLogin();");
                    }
                }
            }
        }
    });
}

function refreshLastRunTime(idStatusLastRunTimeJson) {
    $('.task-lastruntime-span').each(function() {
        var id = $(this).attr('data-id');
        var lastRunTime = $(this).html();
        var newStatusLastTime = idStatusLastRunTimeJson[id];
        if (newStatusLastTime[2] !== lastRunTime) {
            $(this).empty();
            $(this).html(timeStamp2String(newStatusLastTime[2]));
        }
    });
}

function refreshAgent(idStatusLastRunTimeJson) {
    $('.task-machine-span').each(function() {
        var id = $(this).attr('data-id');
        var newStatusLastTime = idStatusLastRunTimeJson[id];
        if (newStatusLastTime[3] == 20) {
            $(this).addClass('text-danger');
            $(this).attr('title', "机器已被 " + newStatusLastTime[4] + "# 任务占用");
        } else {
            $(this).removeClass('text-danger');
            $(this).attr('title', "");
        }
    });
}


function removeTestButton() {
    $('.task-type-span').each(function() {
        var id = $(this).attr('data-id');
        var type = $(this).html();
        if ('LOAD' != type) {
            var thisLi = $(".task-operation-ul[data-id='"+id+"']").find('li:eq(1)');
            thisLi.addClass('hidden');
        }
    });
}

function isEndStatus(newStatus) {
    var isEnd;
    if (newStatus == 'Created' || newStatus == 'Completed' || newStatus == 'Aborted' || newStatus == 'Error' || newStatus == 'Deleted' || newStatus == 'Stopped') {
        isEnd = true;
    } else {
        isEnd = false;
    }
    return isEnd;
}