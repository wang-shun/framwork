/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var admin = isAdmin();
    $('#regular_rule_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        ajax: {
            "url": "/regularRule/all",
            "type": "POST"
        },
        columns: [
            {data: [0]},
            {data: [1]},
            {data: [2]},
            {data: [3]},
            {data: [4]},
            {data: [5]},
            {data: [6]}
        ],
        language: {
            "emptyTable": "没有可用数据",
            "info": "总共_TOTAL_, 当前页为 _START_ 到 _END_ ",
            "infoEmpty": "总共 0",
            "infoFiltered": "(从 _MAX_ 项中过滤)",
            "infoPostFix": "",
            "thousands": ",",
            "lengthMenu": "每页显示 _MENU_ 行",
            "loadingRecords": "正在加载...",
            "processing": "后台处理中...",
            "search": "搜索",
            "zeroRecords": "没有找到匹配结果",
            "paginate": {
                "first": "首页",
                "last": "尾页",
                "next": "下一页",
                "previous": "上一页"
            },
            "aria": {
                "sortAscending": ": 按列升序排列",
                "sortDescending": ": 按列降序排列"
            }
        },
        columnDefs: [
            {
                targets: [4],
                data: [4],
                render: function (data, type, full) {
                    if (full[2].trim() == 'Once') {
                        return data;
                    } else {
                        return data.split(' ')[1];
                    }
                }
            },
            {
                targets: [6],
                data: [0],
                render: function (data, type, full) {
                    if (alreadyLogin) {
                        if (full[6] == 1) {
                            return "<a id='regular-edit" + full[0] + "' href='/regularRule/edit/" + full[0] + "' onclick='regularEdit(" + full[0] + ")' >编辑</a>";
                        } else {
                            return "<a id='regular-edit" + full[0] + "' href='/regularRule/edit/" + full[0] + "' onclick='regularEdit(" + full[0] + ")' >编辑</a>&nbsp;|&nbsp;<a id='regular-delete" + full[0] + "' href='javascript:regularConfirm(" + full[0] + ");'>删除</a>";
                        }
                    } else {
                        return "<a id='regular-edit" + full[0] + "' href='javascript:intervalActiveLogin();' >编辑</a>&nbsp;|&nbsp;<a id='regular-delete" + full[0] + "' href='javascript:intervalActiveLogin();'>删除</a>";
                    }
                }
            }
        ]
    });

    $('#regular_modal_confirm_btn').on('click', function () {
        var id = $(this).attr('data-id');
        regularDel(id);
    });

    $('#regular_del_modal button').on('click', function () {
        window.location.reload();
    });

    $('#regular_error_modal button').on('click', function () {
        window.location.reload();
    });


});

function regularConfirm(id) {
    var name = $("#" + "regular-delete" + id).closest('tr').find('td:eq(1)').html();
    $('#regular_modal_id').html(id);
    $('#regular_modal_name').html(name);
    $('#regular_modal_confirm_btn').attr('data-id', id);
    $('#regular_confirm_modal').modal('show');
}

function regularDel(id) {
    $('#regular_confirm_modal').modal('hide');
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/regularRule/delete/" + id,
        //data: {"id": id},
        success: function (data) {
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#regular_error_modal').modal('show');
            } else {
                $('#regular_del_modal').modal('show');
            }
        }

    });
}




