/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#hosts_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        ajax: {
            "url": "/hosts/all",
            "type": "POST"
        },
        columns: [
            {data: [0]},
            {data: [1]},
            {data: [2]},
            {data: [3]},
            {data: [4]},
            {data: [0]}
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
                data: [4],
                render: function (data, type, full) {
                    if (data) {
                        return "<span class='glyphicon glyphicon-ok success' title='True'></span>";
                    } else {
                        return "<span class='glyphicon glyphicon-remove' title='False'></span>";
                    }
                }
            },
            {
                targets: [5],
                data: [0],
                render: function (data, type, full) {
                    if(alreadyLogin) {
                        return "<a href='/hosts/edit/"+data+"'>编辑</a> | <a href='javascript:hostDetail("+data+");'>详情</a> | <a href='javascript:hostDel("+data+");'>删除</a>";
                    } else {
                        return "<a href='javascript:intervalActiveLogin();'>编辑</a> | <a href='javascript:hostDetail("+data+");'>详情</a> | <a href='javascript:intervalActiveLogin();'>删除</a>";
                    }
                }
            }
        ]
    });
    $('#modal_delete_btn').bind('click', function() {
        realHostDel();
    });
});

function hostEdit (id) {
    
}

function hostDetail (id) {
    $('#hosts_sending_modal').modal('show');
    $.ajax({
        'type': 'GET',
        'url': '/hosts/detail/' + id,
        success: function(data) {
            $('#hosts_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#hosts_error_modal').modal('show');
            } else {
                $('#hosts_detail_id').html(data[0]);
                $('#hosts_detail_name').html(data[1]);
                $('#hosts_detail_content').html(data[2].replace(/\n/g,"<br/>"));
                $('#hosts_detail_updateUser').html(data[3]);
                $('#hosts_detail_updateTime').html(data[4]);
                $('#hosts_detail_enable').html(data[5]);
                $('#hosts_detail_modal').modal('show');
            }
        }
    });
}

function hostDel (id) {
    $('#modal_delete_btn').attr('data-id',id);
    $.ajax({
        'type': 'GET',
        'url': '/hosts/detail/' + id,
        success: function(data) {
            $('#hosts_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#hosts_error_modal').modal('show');
            } else {
                $('#hosts_delete_id').html(data[0]);
                $('#hosts_delete_name').html(data[1]);
                $('#hosts_delete_content').html(data[2].replace(/\n/g,"<br/>"));
                $('#hosts_delete_modal').modal('show');
            }
        }
    });
}

function realHostDel () {
    var id = $('#modal_delete_btn').attr('data-id');
    $('#hosts_sending_modal').modal('show');
    $.ajax({
        'type': 'GET',
        'url': '/hosts/delete/' + id,
        success: function(data) {
            $('#hosts_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#hosts_error_modal').modal('show');
            } else {
                window.location.href="/hosts";
            }
        }
    });
}