/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    getTaskReportList();
});

function getTaskReportList () {
    var taskId = $('#task_report_id_span').html();
    var taskType=$('#task_report_type_span').html();
    if(taskType.toLowerCase()=="load")
    {
        getReportLoadList(taskId);
    }else
    {
        getReportList(taskId,taskType);
    }

}

function getReportList(taskId,taskType)
{
    $('#task_report_list').DataTable({
        "bLengthChange": true,//每页显示条数可选
        "processing": false,
        ajax: {
            "url": "/task/report/list/"+taskId,
            "type": "POST"
        },
        columns: [
            { data: ["taskName"] },
            { data: ["totalCases"] },
            { data: ["pass"] },
            { data: ["fail"] },
            { data: ["aborted"] },
            { data: ["startTime"] },
            { data: ["endTime"] },
            { data: ["duration"] },
            {data:null},
            {data:["_id"]}
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
                targets: [5],
                data: ["startTime"],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [6],
                data: ["endTime"],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [8],
                "visible": false
            },
            {
                targets: [9],
                data: ["_id"],
                render: function (data, type, full) {
                    var retStr="<a class='btn-block' href='/testResult/detail/"+data+"' >详情</a>";
                    if(taskType.toLowerCase()=="gui")
                    {
                        retStr = "<a class='btn-block' href='/gui-pageTest/guiPictureReport/"+data+"' >图文报告</a>" + retStr ;
                    }
                    return "<span style= 'white-space: nowrap '>"+retStr+"</span>" ;
                }
            }
        ],
        "aaSorting":[5,'desc']

    });
}

function getReportLoadList(taskId)
{
    $('#task_report_list').DataTable({
        "bLengthChange": true,//每页显示条数可选
        "processing": false,
        "aaSorting":[8,'desc'],
        ajax: {
            "url": "/task/report/loadlist/"+taskId,
            "type": "POST"
        },
        columns: [
            { data: ["taskName"] },
            { data: ["total"] },
            { data: ["pass"] },
            { data: ["fail"] },
            { data: ["taskId"] },
            { data: ["startTime"] },
            { data: ["endTime"] },
            { data: ["createTime"] },
            { data: ["duration"] }
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
                targets: [0],
                render: function (data, type, full) {
                    return "<a href='/testResult/loaddetail/"+full["_id"]+"'>"+full["taskName"]+(full["isTest"]?"(test)":"")+"</a>"
                }
            },
            {
                targets: [4],
                "visible": false
            },
            {
                targets: [5],
                data: ["startTime"],
                render: function (data, type, full) {
                    if(data==null)
                    {
                        return "";
                    }else
                        return timeStamp2String(data);
                }
            },
            {
                targets: [6],
                data: ["endTime"],
                render: function (data, type, full) {
                    if(data== null)
                    {
                        return "";
                    }else
                    return timeStamp2String(data);
                }
            },
            {
                targets: [8],
                render: function (data, type, full) {
                    if(data== null)
                    {
                        return "";
                    }else
                        return   timeStamp2String(full["createTime"]);
                }
            },
            {
                targets: [9],
                data: ["_id"],
                render: function (data, type, full) {
                    return "<a class='btn btn-block' href='/testResult/loaddetail/"+data+"'>Detail</a>"
                }
            }

        ]
        //,"aaSorting":[5,'desc']

    });
}
