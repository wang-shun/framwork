/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#a_sidebar a').click(function (e) {
          e.preventDefault();//阻止a链接的跳转行为 
          $(this).tab('show');//显示当前选中的链接及关联的content 
        }) ;
    $('#account_list').DataTable({
        "bLengthChange": true, //每页显示条数可选
        "processing": false,
        ajax: {
            "url": "/account/all",
            "type": "POST"
        },
        columns: [
            {data: ['userID']},
            {data: ['userName']},
            {data: ['email']},
            {data: ['isAdmin']},
            {data: ['lastGroup']},
            {data: ['lastLogin']},
            {data: ['lastChangeGroup']}
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
                data: ['lastLogin'],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [6],
                data: [6],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [7],
                data: ['userID'],
                render: function (data, type, full) {
                    return "<a href='javascript:resetPassword("+data+");'>重置密码</a>&nbsp;|&nbsp;<a href='javascript:setAdmin("+data+",true);'>管理员</a>&nbsp;|&nbsp;<a href='javascript:setAdmin("+data+",false)'>用户</a>&nbsp;|&nbsp;<a href='javascript:deleteUser("+data+");'>删除</a>";
                }
            }
        ]
    });

    $('#show_reset_password').bind('click', function(){
        togglePassword($('#reset_password'), $('#reset_password_icon'));
    });

    $('#reset_modal_btn').bind('click', function() {
        sendReset();
    });
});

function resetPassword(id) {
    $('#reset_password').attr('data-id',id);
    $('#account_reset_modal').modal('show');
}

function sendReset() {
    var id = $('#reset_password').attr('data-id');
    var password = $('#reset_password').val();
    $('#account_reset_modal').modal('hide');
    $('#account_sending_modal').modal('show');
    $.ajax({
        type: 'POST',
        url: '/account/reset',
        data: {
            'id': id,
            'password': password
        },
        success: function(data) {
            $('#account_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#account_error_modal').modal('show');
            } else {
                $('#sent_span').empty();
                $('#sent_span').html(data['message']);
                $('#account_sent_modal').modal('show');
            }
        }
    });
}

function setAdmin(id, admin) {
    $('#account_sending_modal').modal('show');
    $.ajax({
        type: 'GET',
        url: '/account/setAdmin/'+id+'/'+admin,
        success: function(data) {
            $('#account_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#account_error_modal').modal('show');
            } else {
                $('#sent_span').empty();
                $('#sent_span').html(data['message']);
                $('#account_sent_modal').modal('show');
            }
        }
    });
}

function deleteUser(id) {
    $('#account_sending_modal').modal('show');
    $.ajax({
        type: 'GET',
        url: '/account/delete/'+id,
        success: function(data) {
            $('#account_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#account_error_modal').modal('show');
            } else {
                $('#sent_span').empty();
                $('#sent_span').html(data['message']);
                $('#account_sent_modal').modal('show');
            }
        }
    });
}