/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#report_list').DataTable({
        "bLengthChange": true, //每页显示条数可选 
        "bProcessing": true,
        "bAutoWidth": true,
//        "bServerSide": true,
        "sorting": [1, "desc"],
        ajax: {
            "url": "/testResult/allInfo",
            "type": "POST"
        },
        columns: [
            {data: [0]},
            {data: [1]},
            {data: [2]},
            {data: [3]},
            {data: [4]},
            {data: [5]},
            {data: [6]},
            {data: [7]},
            {data: [8]}
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
                data: [6],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [9],
                data: [0],
                render: function (data, type, full) {
                    return  "<div class='btn-group task-operation-div dropup'>\n\
                                        <button class='btn btn-success dropdown-toggle' data-toggle='dropdown' aira-expanded='false'>\n\
                                            Operations <span class='caret'></span>\n\
                                        </button>\n\
                                        <ul class='dropdown-menu pull-right' role='menu'>\n\
                                            <li><a id='task-report-detail' href='/testResult/detail/" + data + "'>Detail</a></li>\n\
                                            <li><a id='task-report-email' href='/testResult/email/" + data + "'>SendEmail</a></li>\n\
                                        </ul>\n\
                                    </div>";
                }
            }
        ]
    });
});


function filterColumn() {
    $('#reportDetail_list').DataTable().column(6).search('Failed').draw();
//    var count = $('#reportDetail_list').DataTable().rows[0][0];
//    var s = $('#reportDetail_list').DataTable().rows.length;
//    alert(s);
//    alert(count);
}

function filterColumnAll() {
    $('#reportDetail_list').DataTable().column($('#filter_col6').attr('data-column')).search('').draw();
}

$(document).ready(function () {
    $('#fail_checked').bind('click', function () {
        if ($('#fail_checked').prop("checked") === true) {   //点击failed按钮
            $('.Failed').prop("checked", true);
        } else {
            $('.Failed').removeAttr("checked");
        }

    });
    $('#all_checked').bind('click', function () {
        if ($('#all_checked').prop("checked") === true) {   //点击all按钮
            $('.check_each').prop("checked", true);

        } else {
            $('.check_each').removeAttr("checked");
        }
    });

});

function exportTable(reportId) {
    alert(reportId);
    $.ajax({
        type: "POST",
        url: "/testResult/report/" + reportId,
        dataType: "text",
        success: function () {

        }

    });

}
