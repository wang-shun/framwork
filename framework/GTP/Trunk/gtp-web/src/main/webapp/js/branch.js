/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var admin = isAdmin();
    $('#branch_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        ajax: {
            "url": "/branch/all",
            "type": "POST"
        },
        columns: [
            {data: [0]},
            {data: [1]},
            {data: [2]},
            {data: [3]},
            {data: [4]},
            {data: [5]},
            {data: [0]}
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
                targets: [6],
                data: [0],
                render: function (data, type, full) {
                    if (alreadyLogin) {
                        if (full[5] == 'LOAD') {
                            if (admin) {
                                if (full[6] == 1) {
                                    return "<a href='/branch/edit/" + data + "'>编辑</a> | <a href='#' onclick='javascript:showBranchDetail(" + data + ")'>详情</a>";
                                } else {
                                    return "<a href='/branch/edit/" + data + "'>编辑</a> | <a href='#' onclick='javascript:showBranchDetail(" + data + ")'>详情</a> | <a href='#' id='del_branch_" + data + "' onclick='javascript:delConfirm(" + data + ");'>删除</a>";
                                }
                            } else {
                                return "<a href='#' onclick='javascript:showBranchDetail(" + data + ")'>详情</a>";
                            }
                        } else {
                            if (full[6] == 1) {
                                return "<a href='/branch/edit/" + data + "'>编辑</a> | <a href='#' onclick='javascript:showBranchDetail(" + data + ")'>详情</a>";
                            } else {
                                return "<a href='/branch/edit/" + data + "'>编辑</a> | <a href='#' onclick='javascript:showBranchDetail(" + data + ")'>详情</a> | <a href='#' id='del_branch_" + data + "' onclick='javascript:delConfirm(" + data + ");'>删除</a>";
                            }
                        }
                    } else {
                        return "<a href='javascript:intervalActiveLogin();'>编辑</a> | <a href='#' onclick='javascript:showBranchDetail(" + data + ")'>详情</a> | <a href='javascript:intervalActiveLogin();' id='del_branch_" + data + "'>删除</a>";
                    }
                }
            },
            {
                targets: [4],
                data: [4],
                render: function (data, type, full) {
                    return trimToDate(timeStamp2String(data));
                }
            }
        ]
    });

    $('#branch_list_wapper').addClass('.half-trans-green');

    $('select.column_filter').on('change', function () {
        filterColumn($(this).attr('data-column'));
    });

    $('#branch_del_modal_btn').bind('click', function () {
        var id = $(this).attr('data-branch-id');
        $('#branch_sending_modal').modal('show');
        realDelBranch(id);
    });

    filterColumn($('#col3_filter').attr('data-column'));

    setparams();
});


function setparams() {
    var params = $("#paramsInput").val();
    if (null == params || params == '' || params.split('_').length == 1)
        return false;
    $("#openNew").attr("href", $("#openNew").attr("href") + params)
    $("#col3_filter").find("option[value='" + params.split('_')[1] + "']").attr("selected", true);
    filterColumn($("#col3_filter").attr('data-column'));
    return false;
}


function filterColumn(i) {
    if ('None' === $('#col' + i + '_filter').val()) {
        $('#branch_list').DataTable().column(i).search('').draw();
    } else {
        $('#branch_list').DataTable().column(i).search($('#col' + i + '_filter').val()).draw();
    }
}

function showBranchDetail(id) {
    $('#branch_sending_modal').modal('show');
    $.ajax({
        'type': "GET",
        'url': "/branch/detail/" + id,
        success: function (data) {
            $('#branch_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#branch_error_modal').modal('show');
            } else {
                insertBranchDetail(data['data']);
                $('#branch_detail_modal').modal('show');
            }
        }
    });
}

function insertBranchDetail(data) {
    $('.branch_detail_table_span').each(function (i) {
        if (4 === i) {
            $(this).html(timeStamp2String(data[i]));
        } else {
            $(this).html(data[i]);
        }
    });
}

function delConfirm(id) {

    var name = $("#" + "del_branch_" + id).closest('tr').find('td:eq(1)').html();
    var url = $("#" + "del_branch_" + id).closest('tr').find('td:eq(2)').html();
    $('#branch_del_detail_id').html(id);
    $('#branch_del_detail_name').html(name);
    $('#branch_del_detail_url').html(url);
    $('#branch_del_modal_btn').attr('data-branch-id', id);
    $('#branch_del_detail_modal').modal('show');
}

function realDelBranch(id) {
    $.ajax({
        'type': 'POST',
        'url': '/branch/delete/' + id,
        success: function (data) {
            $('#branch_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#branch_error_modal').modal('show');
            } else {
                window.location.href = '/branch';
            }
        }
    });
}