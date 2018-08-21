/**
 * Created by lizonglin on 2015/7/20/0020.
 */
$(document).ready(function () {
    var admin = isAdmin();
    $('#loadconf_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        ajax: {
            "url": "/load/conf/all",
            "type": "GET"
        },
        columns: [
            {data: [0]},//id
            {data: [1]},//name
            {data: [2]},//scriptSvn
            {data: [3]},//jmxContent
            {data: [4]},//sceneName
            {data: [5]},//sourceSvn
            {data: [6]},//env
            {data: [7]}//lastUpdateTime
            //{data: [8]}//isUsed
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
                targets: [2],
                data: [2],
                render: function (data, type, full) {
                    return "<textarea readonly>" + splitSSvnUrl(data, 1) + "</textarea>";
                }
            },
            {
                targets: [3],
                data: [3],
                render: function (data, type, full) {
                    return "<textarea readonly>" + data + "</textarea>";
                }
            },
            {
                targets: [5],
                data: [2],
                render: function (data, type, full) {
                    return "<textarea readonly>" + splitSSvnUrl(data, 2) + "</textarea>";
                }
            },
            {
                targets: [7],
                data: [7],
                render: function (data, type, full) {
                    return timeStamp2String(data);
                }
            },
            {
                targets: [8],
                data: [0],
                render: function (data, type, full) {
                    if (alreadyLogin) {
                        if (full[8] == 1) {
                            return "<a href='/load/conf/edit/" + data + "'>编辑</a> | <a href='javascript:getDetailById(" + data + ")'>详情</a>";
                        } else {
                            return "<a href='/load/conf/edit/" + data + "'>编辑</a> | <a href='javascript:getDetailById(" + data + ")'>详情</a> | <a href='javascript:confirmDel(" + data + ", \"" + full[1] + "\");' id='delete_" + data + "'>删除</a>";
                        }
                    } else {
                        return "<a href='javascript:intervalActiveLogin();'>编辑</a> | <a href='javascript:getDetailById(" + data + ")'>详情</a> | <a href='javascript:intervalActiveLogin();' id='delete_" + data + "'>删除</a>";
                    }
                }
            }
        ]
    });
});

//截取每个SVNUrl（，分隔）最后num个字段，换行返回
function splitSSvnUrl(data, num) {
    var resultStr = "";
    var urls = data.split(",");
    urls.forEach(function (url) {
        var str = "";
        var i = 0;
        while (i < num) {
            var last = url.lastIndexOf("/");
            str = url.substring(last) + str;
            url = url.substring(0, last);
            i++;
        }
        resultStr = resultStr + str + "\r\n";
    });
    return resultStr;
}

function getDetailById(id) {
    $('#loadconf_sending_modal').modal('show');
    $.ajax({
        url: "/load/conf/detail/" + id,
        type: "GET",
        success: function (data) {
            $('#loadconf_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#loadconf_error_modal').modal('show');
            } else {
                refreshSpan("loadconf_detail_id", data['data']['id']);
                refreshSpan("loadconf_detail_name", data['data']['name']);
                $('#loadconf_detail_scriptsvn').val(replaceAll(data['data']['scriptSvn'], ',', '\r\n'));
                $('#loadconf_detail_jmxcontent').val(data['data']['jmxContent']);
                refreshSpan("loadconf_detail_schenename", data['data']['sceneName']);
                $('#loadconf_detail_sourcesvn').val(replaceAll(data['data']['sourceSvn'], ',', '\r\n'));
                refreshSpan("loadconf_detail_env", data['data']['env']);
                refreshSpan("loadconf_detail_lastupdatetime", timeStamp2String(data['data']['lastUpdateTime']));

                $('#loadconf_detail_modal').modal('show');
            }
        }
    });
}

function confirmDel(id, name) {
    var tr = $('#delete_' + id).closest('tr');
    $('#loadconf_del_detail_id').empty();
    $('#loadconf_del_detail_id').html(id);
    $('#loadconf_del_detail_name').empty();
    $('#loadconf_del_detail_name').html($(tr).find('td:eq(1)').html());
    $('#loadconf_del_detail_scenename').empty();
    $('#loadconf_del_detail_scenename').html($(tr).find('td:eq(4)').html());
    $('#loadconf_del_detail_env').empty();
    $('#loadconf_del_detail_env').html($(tr).find('td:eq(6)').html());
    $('#loadconf_del_modal_btn').attr('data-loadconf-id', id);
    $('#loadconf_del_modal_btn').attr('data-loadconf-name', name);
    $('#loadconf_del_detail_modal').modal('show');
}

function delLoadConf(btn) {
    $('#loadconf_del_detail_modal').modal('hide');
    $('#loadconf_sending_modal').modal('show');
    var id = $(btn).attr('data-loadconf-id');
    var name = $(btn).attr('data-loadconf-name');
    $.ajax({
        url: "/load/conf/del/" + id + "/" + name,
        type: "GET",
        async: false,
        success: function (data) {
            $('#loadconf_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#loadconf_error_modal').modal('show');
            } else {
                window.location.href = '/load/conf';
            }
        }
    });
}

function refreshSpan(id, newVal) {
    $('#' + id).empty();
    $('#' + id).html(newVal);
}

function replaceAll(str, oStr, rStr) {
    re = new RegExp(oStr, "g");
    return str.replace(re, rStr);
}