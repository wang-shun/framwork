/**
 * Created by lizonglin on 2015/7/20/0020.
 */
$(document).ready(function () {
    var admin = isAdmin();
    $('#loadscene_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        ajax: {
            "url": "/load/scene/all",
            "type": "GET"
        },
        columns: [
            //id
            {data: [0]},
            //name
            {data: [1]},
            //onError
            {data: [2]},
            //threadNum
            {data: [3]},
            //initDelay
            {data: [4]},
            //startCount
            {data: [5]},
            //startCountBurst
            {data: [6]},
            //startPeriod
            {data: [7]},
            //stopCount
            {data: [8]},
            //stopPeriod
            {data: [9]},
            //flightTime
            {data: [10]},
            //rampUp
            {data: [11]},
            //lastUpdateTime
            {data: [12]},
            //template
            {data: [13]},
            //test
            {data: [14]}
            //isUsed
            //{data: [15]}
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
                targets: [12],
                data: [12],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [13],
                data: [13],
                render: function (data, type, full) {
                    return markColor(data);
                }
            },
            {
                targets: [14],
                data: [14],
                render: function (data, type, full) {
                    return markColor(data);
                }
            },
            {
                targets: [15],
                data: [0],
                render: function (data, type, full) {
                    if (alreadyLogin) {
                        if(!admin && (full[13] || full[14])) {
                            return ""
                        } else {
                            if (full[15] == 1) {
                                return "<a href='/load/scene/edit/" + data + "'>编辑</a>";
                            } else {
                                return "<a href='/load/scene/edit/" + data + "'>编辑</a> | <a href='javascript:confirmDel(" + data + ");' id='delete_" + data + "'>删除</a>";
                            }
                        }
                    } else {
                        return "<a href='javascript:intervalActiveLogin();'>编辑</a> | <a href='javascript:intervalActiveLogin();' id='delete_" + data + "'>删除</a>";

                    }
                }
            }
            ]
    });

    $('#loadscene_del_modal_btn').bind('click', function () {
        deleteScene();
    });

    $('#drop_Template').bind('change', function () {
        filterColumn($(this).find("option:selected").val());
    });
});

function filterColumn(i) {
    if ('None' === i) {
        $('#loadscene_list').DataTable().column(13).search('').draw();
    } else {
        $('#loadscene_list').DataTable().column(13).search(i==1?'是':'否').draw();
    }
}

function markColor(data) {
    if (false == data) {
        return "<span style='color: RED'>否</span>";
    } else if (true == data) {
        return "<span style='color: GREEN'>是</span>";
    } else {
        return data;
    }
}

function confirmDel(id) {
    $('#loadscene_del_detail_id').empty();
    $('#loadscene_del_detail_id').html(id);
    $('#loadscene_del_detail_name').empty();
    var name = $('#delete_' + id).closest('tr').find('td:eq(1)').html();
    $('#loadscene_del_detail_name').html(name);
    $('#loadscene_del_detail_modal').modal('show');
}

function deleteScene() {
    $('#loadscene_del_detail_modal').modal('hide');
    $('#loadscene_sending_modal').modal('show');
    var id = $('#loadscene_del_detail_id').html();
    $.ajax({
        url:"/load/scene/delete/" + id,
        type:"GET",
        async:false,
        success:function(data) {
            $('#loadscene_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#loadscene_error_modal').modal('show');
            } else {
                window.location.href = "/load/scene";
            }
        }
    });
}


