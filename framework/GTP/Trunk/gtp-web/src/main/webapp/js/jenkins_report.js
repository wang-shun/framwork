$(document).ready(function() {
    var jenkinsAddr = $('#jenkinsaddr_span').html();
    var taskId = $('#jenkins_report_id_span').html();
    $('#jenkins_report_list').DataTable({
        "bLengthChange": true,//每页显示条数可选
        "processing": false,
        ajax: {
            "url": "/task/report/jenkins_report/list/"+taskId,
            "type": "POST"
        },
        columns: [
            { data: ["taskID"] },
            { data: ["startTime"] },
            { data: ["taskState"]},
            { data: ["jobName"] }
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
                targets: [1],
                data: ["startTime"],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [2],
                data: ["taskState"],
                render: function (data, type, full) {
                    return genTaskState(data);
                }
            },
            {
                targets: [3],
                data: ["jobName"],
                render: function (data, type, full) {
                    return "<a class='btn btn-block' href='javascript:void(0);' onclick=\"goJenkinsLog('"+ jenkinsAddr + "/job/" + data + "');\">Jenkins日志</a>"
                }
            }
        ],
        "aaSorting":[1,'desc']

    });
});

function genTaskState(data) {
    var state = "";
    switch (data) {
        case 50:
            state = "<strong><span title='任务已发送到执行机，即将运行(Sent)' class='text-primary'>已发送</span><span class='span-bg'>Sent</span></strong>";
            break;
        case 60:
            state = "<strong><span title='任务正在运行(Running)' class='text-primary'>运行中</span><span class='span-bg'>Running</span></strong>";
            break;
        case 70:
            state = "<strong><span title='任务已成功执行完成(Completed)' class='text-success'>已完成</span><span class='span-bg'>Completed</span></strong>";
            break;
        case 80:
            state = "<strong><span title='任务因超时等原因被中断(Aborted)' class='text-danger'>中断</span><span class='span-bg'>Aborted</span></strong>";
            break;
        case 90:
            state = "<strong><span title='任务执行过程中出错(Error)' class='text-danger'>错误</span><span class='span-bg'>Error</span></strong>";
            break;
        case 110:
            state = "<strong><span title='任务被手动停止(Stopped)' class='text-warning'>已停止</span><span class='span-bg'>Stopped</span></strong>";
            break;
        default :
            state = "<strong><span title='任务处于未知状态(Error)' class='text-danger'>异常</span><span class='span-bg'>Error</span></strong>";
    }

    return state;
}

function goJenkinsLog(jenkinsUrl) {
    if (jenkinsUrl.length > 0) {
        window.open(jenkinsUrl);
    }
}