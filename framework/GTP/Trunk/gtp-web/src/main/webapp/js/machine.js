/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    var admin = isAdmin();
    $('#machine_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        ajax: {
            "url": "/machine/all",
            "type": "GET"
        },
        columns: [
            {data: [0]},
            {data: [1]},
            {data: [2]},
            {data: [6]},
            {data: [3]},
            {data: [4]},
            {data: [7]},
            {data: [8]},
            {data: [5]}
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
                targets: [4],
                data: [3],
                render: function (data, type, full) {
                    if (full[7] == 'GUI') {
                        return browserTypes(data).join(',');
                    } else {
                        return "--";
                    }
                }
            },
            {
                targets: [5],
                data: [6],
                render: function (data, type, full) {
                    if(full[6]) {
                        return full[4];
                    } else {
                        return "None";
                    }
                }
            },
            {
                targets: [9],
                data: [0],
                render: function (data, type, full) {
                    if(admin) {
                        return "<a href='/machine/edit/" + data + "'style='cursor:pointer'>编辑</a> | <a href='javascript:confirmDel("+data+");' id='delete_" + data + "'>删除</a>";
                    } else {
                        return "<span class='glyphicon glyphicon-ban-circle' title='管理员权限'></span>";
                    }
                }
            }
        ]
    });

    $('#machine_list_wapper').addClass('.half-trans-green');

    $('select.column_filter').on('change', function () {
        filterColumn($(this).attr('data-column'));
    });

    $('#machine_del_modal_btn').bind('click', function(){
        $('#machine_new_sending_modal').modal('show');
        var id = $(this).attr('data-machine-id');
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/machine/delete/"+id,
            success: function (data) {
                $('#machine_new_sending_modal').modal('hide');
                if (data['isError']) {
                    $('#error_span').empty();
                    $('#error_span').html(data['message']);
                    $('#machine_new_error_modal').modal('show');
                } else {
                    $('#machine_new_sent_modal').modal('show');
                }
            }
        });
    });
});

function filterColumn(i) {
    if ('None' === $('#col' + i + '_filter').val()) {
        $('#machine_list').DataTable().column(i).search('').draw();
    } else {
        $('#machine_list').DataTable().column(i).search($('#col' + i + '_filter').val()).draw();
    }
}

function confirmDel(id) {
    var name = $("#"+"delete_"+id).closest('tr').find('td:eq(1)').html();
    var ip = $("#"+"delete_"+id).closest('tr').find('td:eq(2)').html();

    $('#machine_del_detail_id').html(id);
    $('#machine_del_detail_name').html(name);
    $('#machine_del_detail_ip').html(ip);

    $('#machine_del_modal_btn').attr('data-machine-id',id);

    $('#machine_del_detail_modal').modal('show');
}