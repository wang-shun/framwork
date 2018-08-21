/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//计数器，checkbox最多选中的数量
var maxCheckBoxCount = 3;
//最多展示的条数
var maxdataCount = 200;

var sceneNamesites = null;
var taskListites = null;

$(document).ready(function () {
    dateShow();
    reportSceneName();
    reportTask();
    getEnvironment();

    $("#report_type_group").bind('change', function () {
        reportSceneName();
        reportTask();
    });
    $("#dimension_template").bind('change', function () {
        reportSceneName();
        reportTask();
    });

    $('#report_generate_btn').bind('click', function () {
        showChart();
    });
    $('#report_begin_div').bind('click', function () {
        $('#report_timespan_begin_input').datetimepicker('show');
    });

    $('#report_end_div').bind('click', function () {
        $('#report_timespan_end_input').datetimepicker('show');
    });
    showSceneName_search();
    showTaskName_search();

    $("#hiddenValue").attr('sceneName', '');
    $("#hiddenValue").attr('smallSceneName', '');
    //计数器；计算选中的checkbox的数量
    $("#hiddenValue").attr('selectCheckBoxCount', 0);
    $("#hiddenValue").attr('selectCheckBoxValue', '');


});

function showSceneName_search() {

    if (sceneNamesites == null)
        return;

    $('#product_search').typeahead({
        items: 8,
        minChars: 0,
        source: function (query, process) {
            return sceneNamesites;
        },
        //匹配
        matcher: function (item) {
            return ~item.toLowerCase().indexOf(this.query.toLowerCase())
        },
        //显示
        highlighter: function (item) {
            return item;
        },
        //选中
        updater: function (item) {
            $("#hiddenValue").attr('sceneName', item);
            reportTypeSelect();
            labelNameSelect();
            getEnvironment();
            return item;
        },
        autoSelect: true
    });
}


function showTaskName_search() {

    if (taskListites == null)
        return;

    $('#task_search').typeahead({
        items: 8,
        minChars: 0,
        source: function (query, process) {
            return taskListites;
        },
        //匹配
        matcher: function (item) {

            return ~item.toLowerCase().indexOf(this.query.toLowerCase())
        },
        //显示
        highlighter: function (item) {
            return item;
        },
        //选中
        updater: function (item) {

            $("#hiddenValue").attr('taskid', item);
            reportSceneName();
            return item;
        },
        autoSelect: true
    });
}


function updateSceneName_search(data) {
    sceneNamesites = data;
    $('#menu-sceneName').children(':not(#menu-template)').remove();
    for (var i = 0; i < data.length; ++i) {
        var item = data[i];
        var setUpMenuDom = $('#menu-template').clone(true);
        setUpMenuDom.removeAttr('id');
        setUpMenuDom.removeAttr('class');
        setUpMenuDom.find('a').attr('data-content', item);
        setUpMenuDom.find('a').text(item);
        $('#menu-sceneName').append(setUpMenuDom);
    }
}

function updatetask_search(data) {
    taskListites = data;
    $('#menu-taskName').children(':not(#menu-task-template)').remove();
    for (var i = 0; i < data.length; ++i) {
        var item = data[i];
        var setUpMenuDom = $('#menu-task-template').clone(true);
        setUpMenuDom.removeAttr('id');
        setUpMenuDom.removeAttr('class');
        setUpMenuDom.find('a').attr('data-content', item);
        setUpMenuDom.find('a').text(item);
        $('#menu-taskName').append(setUpMenuDom);
    }

}


$('#menu-taskName').on('click', 'a', function () {
    var taskValue = $(this).text();

    $("#task_search").val(taskValue);
    $("#hiddenValue").attr('taskid', $(this).attr('data-content'));
    reportSceneName();
});


$('#menu-sceneName').on('click', 'a', function () {
    var sceneName = $(this).text();

    $("#product_search").val(sceneName);
    $("#hiddenValue").attr('sceneName', sceneName);
    reportTypeSelect();
    labelNameSelect();
    getEnvironment();
});




//绑定大场景名称select事件
function reportTypeSelect() {

    $('#task_type_smallSceneName').empty();
    $("#task_type_labelName").empty();
    var sceneName = $("#hiddenValue").attr('sceneName');
    if (sceneName != "") {
        getSmallSceneName(sceneName, true);
    }

}

function reportTask() {

    $('#task_search').val('');
    var reportType = $('#task_type').find('option:selected').val();
    var groupid = $.trim($('#report_type_group').find('option:selected').val());
    var template = $.trim($('#dimension_template').find('option:selected').val());
    var sceneName = '';

    $.ajax({
        type: 'POST',
        url: '/gtp-report/taskName',
        async: false,
        data: {
            'groupid': groupid,
            'sceneName': sceneName,
            'template': template,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                updatetask_search(data);
            }
        }
    });


    ///
}

//绑定大场景
function reportSceneName() {
    $('#task_type_labelName').empty();
    $('#task_type_smallSceneName').empty();
    $("#dimension_environment").empty();

    $("#product_search").val('');
    var taskid = $("#hiddenValue").attr('taskid');
    if (taskid==undefined || $("#task_search").val().length == 0) {
        taskid = '';
    } else {
        var first = taskid.indexOf('(');
        var end = taskid.indexOf(')');
        taskid = taskid.substring(first + 1, end);
    }

    var groupid = $.trim($('#report_type_group').find('option:selected').val());
    var template = $.trim($('#dimension_template').find('option:selected').val());

    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtSceneName',
        async: false,
        data: {
            'groupid': groupid,
            'template': template,
            'taskid': taskid,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                updateSceneName_search(data);
            }
        }
    });


}

function getEnvironment() {
    var sceneName = $("#hiddenValue").attr('sceneName');

    if (sceneName == null || sceneName == undefined) {
        return false;
    }

    $("#dimension_environment").empty();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtEnvironment',
        async: false,
        data: {
            'sceneName': sceneName,
            'isEnable': true
        },
        success: function (data) {
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var options = "";
                for (var i = 0; i < data.length; ++i) {
                    var option = data[i];
                    options += '<option value="' + option + '" >' + option + '</option>';
                }
                $('#dimension_environment').append(options);
            }
        }
    });
}


//得到小场景名称并绑定
function getSmallSceneName(sceneName, isEnable) {
    $('#task_type_smallSceneName').empty();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtSmallsceneName',
        async: false,
        data: {
            'sceneName': sceneName,
            'isEnable': isEnable
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var options = "";
                for (var i = 0; i < data.length; ++i) {
                    var option = data[i];
                    options += '<option value="' + option + '" >' + option + '</option>';
                }

                $('#task_type_smallSceneName').append(options);
            }
        }
    });
}

//绑定小场景名称select事件
function labelNameSelect() {
    var sceneName = $("#hiddenValue").attr('sceneName');
    if (sceneName != "") {
        getLabelName(sceneName, true);
    }
}

function getLabelName(sceneName, isEnable) {


    $('#task_type_labelName').empty();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtLabelName',
        async: false,
        data: {
            'sceneName': sceneName,
            'isEnable': isEnable
        },
        success: function (data) {
            $('#task_type_labelName').empty();
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var options = "";
                for (var i = 0; i < data.length; ++i) {
                    var option = data[i];
                    options += '<option value="' + option + '" >' + option + '</option>';
                }

                $('#task_type_labelName').append(options);
            }
        }
    });
}


function dateShow() {
    $('#report_timespan_begin_input').val('');
    $('#report_timespan_begin_input').datetimepicker({
        timepicker: false,
        format: 'Ymd'
    });
    $('#report_timespan_end_input').val('');
    $('#report_timespan_end_input').datetimepicker({
        timepicker: false,
        format: 'Ymd'
    });
}


function showError(title, value) {
    $('#report_error_h4').empty();
    $('#report_error_h4').text(title);
    $('#report-error-body').empty();
    $('#report-error-body').text(value);
    $('#report_error_modal').modal('show');
    return false;
}

function showChart() {
    var sceneName = $("#hiddenValue").attr('sceneName');
    if ($("#product_search").val().length == 0) {
        sceneName = "";
    }

    var smallSceneName = $('#task_type_smallSceneName').find('option:selected').text();
    var labelName = $('#task_type_labelName').find('option:selected').text();
    var template = $('#dimension_template').find('option:selected').val();
    var reportType = $('#task_type').find('option:selected').val();
    var resultVersion = $("#report_timespan_begin_input").val();
    var resultEndVersion = $("#report_timespan_end_input").val();
    var environment = $('#dimension_environment').find('option:selected').text();
    var timeArt = $('#task_time').val();

    var taskid = $("#hiddenValue").attr('taskid');
    if ($("#task_search").val().length == 0) {
        taskid = '';
    } else {
        var first = taskid.indexOf('(');
        var end = taskid.indexOf(')');
        taskid = taskid.substring(first + 1, end);
    }


    if (sceneName == "" && reportType == '2') {
        //return showError('场景发生异常', '大场景不能为空!');
    }


    //if (environment == "" && reportType =='2') {
    //    return showError('环境发生异常', '环境不能为空!');
    //}
    //if (template == "" && reportType =='2' ) { //
    //    return showError('模板发生异常', '模板不能为空!');
    //}
    //隐藏展示全选框
    if (reportType == '2') {
        $("#showAllselect").removeClass('hidden');
    } else {
        $("#showAllselect").addClass('hidden');
    }
    $("#hiddenValue").attr('reportType', reportType);
    $("#hiddenValue").attr('labelName', labelName);
    $("#hiddenValue").attr('SmallSceneName', smallSceneName);
    cleanHistoryReport();

    $.ajax({
        url: "/gtp-report/getlabelreport",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "resultEndVersion": resultEndVersion,
            "environment": environment,
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "labelName": labelName,
            "template": template,
            "taskId": taskid,
            "timeART": timeArt

        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];

                bandTable(data, reportType);
            }
        }

    });
}

//定义数组 是否包含的属性
Array.prototype.contains = function (item) {
    item = $.trim(item);
    return ("," + this + ",").indexOf("," + item + ",") > -1;
};

function bandTable(data, reportType) {

    $('#report_label_div').removeClass('hidden');
    $("#report_aggreport_div").addClass('hidden');
    //计数器；计算选中的checkbox的数量
    $("#hiddenValue").attr('selectCheckBoxCount', 0);
    $("#hiddenValue").attr('selectCheckBoxValue', '');

    var index = 0;
    var grpThreadsList = new Array();


    $('#report_label_list').DataTable({
            "bLengthChange": true,
            "processing": false,
            "bDestroy": true,
            data: data,
            columns: [
                {data: "sceneName", orderable: false},
                {data: "resultVersion"},
                {data: "sceneName"},
                {data: "environment"},
                {data: "template"},
                {data: "grpThreadDetails", orderable: false, bVisible: (reportType == '2' ? true : false)}
            ],
            columnDefs: [
                {
                    targets: [0],
                    data: [0],
                    render: function (data, type, full) {
                        if (data) {
                            var grp = full["grpThreadDetails"];
                            var sceneName = full["sceneName"];
                            var resultVersion = full["resultVersion"];
                            var environment = full["environment"];
                            var template = full["template"];
                            var value = sceneName + '_' + resultVersion + '_' + environment + '_' + template;
                            index++;
                            return "<input type='checkbox' onclick='selectCheck(this," + reportType + ",null)' scenename='" + sceneName + "' resultversion='" + resultVersion + "' " +
                                " environment='" + environment + "' id='selectLabelInput" + index + "' template='" + template + "'  name='selectLabelInput' grp='" + grp + "' /> ";
                        } else {
                            return "";
                        }
                    }
                },
                {
                    targets: [5],
                    data: [5],
                    render: function (data, type, full) {
                        if (data) {
                            var list = data.split(',');
                            var result = '';
                            for (var i = 0; i < list.length; i++) {
                                if (!grpThreadsList.contains(list[i])) {
                                    grpThreadsList.push(list[i]);
                                }
                                if (i % 10 == 0) {
                                    result += '<br />'
                                }
                                result += (i == 0 ? '' : ',') + list[i];
                            }

                            return result;
                        } else {

                            return "";
                        }
                    }
                }
            ],
            language: {
                "emptyTable": "没有可用数据",
                "info": "总共_TOTAL_, 当前页为 _START_ 到 _END_ ",
                "infoEmpty": "总共 0",
                "infoFiltered": "(从 _MAX_ 项中过滤)",
                "infoPostFix": "",
                "thousands": ",",
                "lengthMenu": '每页显示 <select >' + '<option value="10" selected="selected">10</option>' + '<option value="20">20</option>'
                + '<option value="30">30</option>' + '<option value="40">40</option>' + '<option value="50">50</option>' + '</select> 行 '
                + ' '
                + ' <a onclick="ShowChars()" id="btnShowLabel" class="btn btn-primary">显示图表</a>' + ' <a id="btnExport" onclick="exportChart()" class="btn btn-primary">生成报表</a>'
                + '<ul id="load-report" class="dropdown-menu1" ><li id="menu-load-report" class="hide"> <a rel="popover" data-content="template menu description" href="javascript:void(0);">menu</a> </li></ul>',
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
            }

        }
    );

    $("#showAllselect").prop("checked", false);
    $("#showGrpThreads").empty();

    var tdShowHistory = $("#tdShowHistory");
    var btnShowLabel = $("#btnShowLabel");
    var btnExport = $("#btnExport");
    if (reportType == '2') {
        btnExport.removeClass('hidden');
        tdShowHistory.removeClass('hidden');
        btnShowLabel.addClass("hidden");
    } else {
        tdShowHistory.addClass('hidden');
        btnShowLabel.removeClass("hidden");
        btnExport.addClass('hidden');
    }


}

//导出图表
function exportChart() {


    var selectCheckBoxValue = $.trim($("#hiddenValue").attr('selectCheckBoxValue'));
    if (selectCheckBoxValue.length == 0) {
        return showError('报告发生异常', '选择的数据为空!');
        return false;
    }


    var list = selectCheckBoxValue.split(',');
    var ValueList = new Array();
    //处理去除空内容
    for (var i = 0; i < list.length; i++) {
        var id = $.trim(list[i]);
        if (id.length > 0) {
            ValueList.push(id);
        }
    }


    var resultVersion = '';
    var environment = '';
    var scenename = '';
    var template = '';

    for (var i = 0; i < ValueList.length; i++) {
        var id = $.trim(ValueList[i]);

        var list = $("#hiddenValue").attr(id).split(',');

        if (list.length > 0) {
            //obj.attr('resultversion')+"_"+obj.attr('scenename')+"_"+obj.attr('environment')+"_"+obj.attr('template')
            resultVersion += list[0] + ',';
            environment += list[2] + ',';
            scenename += list[1] + ',';
            if (list[3].length == 0) {
                list[3] = 'NONE';
            }
            template += list[3] + ',';
        }

    }

    resultVersion = resultVersion.length > 0 ? resultVersion.substring(0, resultVersion.length - 1) : resultVersion;
    environment = environment.length > 0 ? environment.substring(0, environment.length - 1) : environment;
    scenename = scenename.length > 0 ? scenename.substring(0, scenename.length - 1) : scenename;
    template = template.length > 0 ? template.substring(0, template.length - 1) : template;

    bindexpint(resultVersion, environment, scenename, template);
}

function bindexpint(resultVersion, environment, scenename, template) {
    $.ajax({
        url: "/gtp-report/exportReport",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "environment": environment,
            "sceneName": scenename,
            "template": template
        },

        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['message'];
                var item = data;
                var setUpMenuDom = $('#menu-load-report').clone(true);
                setUpMenuDom.removeAttr('id');
                setUpMenuDom.removeAttr('class');
                setUpMenuDom.find('a').attr('data', item);
                setUpMenuDom.find('a').attr('href',"javascript:downloadfile('"+item+"')");
                setUpMenuDom.find('a').text(item);
                $('#load-report').append(setUpMenuDom);


                //load-report
            }


        }

    });
}


function downloadfile(file)
{
    if(confirm('请确认，是否下载该报表？'))
    {
        window.open('/gtp-report/download/'+file);
    }

}

//显示第二张图片
function ShowHistoryChars() {
    var labelNameAttr = $.trim($("#hiddenValue").attr('labelName'));
    var smallSceneNameAttr = $.trim($("#hiddenValue").attr('SmallSceneName'));

    var oppValue = labelNameAttr.length == 0 ? "" : ( labelNameAttr + (smallSceneNameAttr.length == 0 ? "" : "(" + smallSceneNameAttr + ")"));
    var reportType = $("#hiddenValue").attr('reportType');
    var showGrpThreads = $("#showGrpThreads");
    if (showGrpThreads.val().length == 0 && reportType == '2') {
        return showError('报告发生异常', '线程数不能为空!');
        return false;
    }

    var selectCheckBoxValue = $.trim($("#hiddenValue").attr('selectCheckBoxValue'));
    if (selectCheckBoxValue.length == 0) {
        return showError('报告发生异常', '选择的数据为空!');
        return false;
    }


    $("#btn_loading").removeClass('hidden');
    var list = selectCheckBoxValue.split(',');
    var ValueList = new Array();
    //处理去除空内容
    for (var i = 0; i < list.length; i++) {
        var id = $.trim(list[i]);
        if (id.length > 0) {
            ValueList.push(id);
        }
    }

    $("#filter_table_tbody2").empty();
    $("#filter_table_tbody2").addClass("hidden");
    $("#report_tps").addClass('hidden');
    loagShow(true);


    var grpThreads = showGrpThreads.val();


    var resultVersion = '';
    var environment = '';
    var scenename = '';
    var template = '';

    for (var i = 0; i < ValueList.length; i++) {
        var id = $.trim(ValueList[i]);

        var list = $("#hiddenValue").attr(id).split(',');

        if (list.length > 0) {
            //obj.attr('resultversion')+"_"+obj.attr('scenename')+"_"+obj.attr('environment')+"_"+obj.attr('template')
            resultVersion += list[0] + ',';
            environment += list[2] + ',';
            scenename += list[1] + ',';
            if (list[3].length == 0) {
                list[3] = 'NONE';
            }
            template += list[3] + ',';
        }

    }

    resultVersion = resultVersion.length > 0 ? resultVersion.substring(0, resultVersion.length - 1) : resultVersion;
    environment = environment.length > 0 ? environment.substring(0, environment.length - 1) : environment;
    scenename = scenename.length > 0 ? scenename.substring(0, scenename.length - 1) : scenename;
    template = template.length > 0 ? template.substring(0, template.length - 1) : template;

    bandhistoryChart(resultVersion, environment, scenename, template, grpThreads, oppValue);

}

function bandhistoryChart(resultVersion, environment, sceneName, template, grpThreads, oppValue) {
    var checkLabelCount = 5;
    var tdCount = 0;
    var trCount = 0;

    var id = '';
    $.ajax({
        url: "/gtp-report/getHistoryGrpJMTReport",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "environment": environment,
            "sceneName": sceneName,
            "template": template,
            "grpThreads": grpThreads
        },

        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {

                data = data['data'];
                for (var i = 0; i < data.length; i++) {
                    var option = data[i][0];
                    var xdata = data[i][5];
                    var tps = data[i][2];
                    var avg = data[i][3];
                    var elapsed90 = data[i][4];
                    var checked = (oppValue.length == 0 && i == 0) || (oppValue.length > 0 && option.indexOf(oppValue) == 0 ) ? "checked='checked'" : "";
                    var timeStamp = data[i][1];
                    var env = data[i][7];
                    var tem = data[i][8];
                    var name = option + "_" + env + "_" + tem;
                    name = name.replace("(", "").replace(")", "").replace("/", "");

                    if (i == 0) {
                        //$('#filter_table_tbody2').append("<tr><th  align='center' colspan='"+checkLabelCount+"'>"+name+"</th></tr>");
                        var minversion = data[i][6];

                        $("#hiddenValue").attr("minversion", minversion);

                    }
                    if (i == 0 || i % checkLabelCount == 0) {
                        trCount++;
                        id = "jmt_report_label_" + name + "_" + trCount;
                        id = id.replace(".", "");
                        var labelTr = $("#labelTR").clone(true);
                        labelTr.removeAttr('id');
                        labelTr.attr("id", id);
                        $('#filter_table_tbody2').append(labelTr);
                        tdCount = 0;

                    }
                    var html = "<td><input type='checkbox' onclick='checkFun(" + grpThreads + ")' xdata='" + xdata + "' timestamp='" + timeStamp + "' grpThreads='" + grpThreads + "' " +
                        "tps='" + tps + "' avg='" + avg + "'  elapsed90='" + elapsed90 + "' name='chklabel' value='" + option + "' " + checked + " ></input> " + option + "_" + env + "_" + tem + " </td>";
                    $("#" + id).append(html);
                    tdCount++;

                    if (checked.length > 0) {
                        checkFun(grpThreads);
                    }

                }
                while (tdCount < checkLabelCount) {
                    $("#" + id).append("<td></td>");
                    tdCount++;
                }

                loagShow(false);
                $("#report_tps").removeClass('hidden');
                $("#filter_table_tbody2").removeClass("hidden");

            }
        }

    });
}


//显示第一张图片的 图表
function ShowChars() {
    var labelNameAttr = $.trim($("#hiddenValue").attr('labelName'));
    var smallSceneNameAttr = $.trim($("#hiddenValue").attr('SmallSceneName'));

    var oppValue = labelNameAttr.length == 0 ? "" : ( labelNameAttr + (smallSceneNameAttr.length == 0 ? "" : "(" + smallSceneNameAttr + ")"));

    var selectCheckBoxValue = $.trim($("#hiddenValue").attr('selectCheckBoxValue'));
    if (selectCheckBoxValue.length == 0) {
        return showError('报告发生异常', '选择的数据为空!');
        return false;
    }

    $("#btn_loading").removeClass('hidden');
    var list = selectCheckBoxValue.split(',');
    var ValueList = new Array();
    for (var i = 0; i < list.length; i++) {
        var id = $.trim(list[i]);
        if (id.length > 0) {
            id= id.replace('selectLabelInput','');

            ValueList.push(Number(id));
        }
    }

    ValueList.sort(function(a,b){
        return a-b});

    $("#filter_table_tbody2").empty();
    $("#filter_table_tbody2").addClass("hidden");
    $("#report_tps").addClass('hidden');
    loagShow(true);

    var aggVersion = '';
    var aggenv = '';
    var aggscene = '';
    var aggtem = '';

    for (var i = 0; i < ValueList.length; i++) {
        var id = $.trim(ValueList[i]);
        id='selectLabelInput'+id;
        var list = $("#hiddenValue").attr(id).split(',');


        if (list.length > 0) {

            var resultVersion = list[0];
            var environment = list[2];
            var scenename = list[1];
            var template = list[3];

            aggVersion += resultVersion + ",";
            aggenv += environment + ",";
            aggscene += scenename + ",";
            aggtem += template + ",";

            bandTpsChart(resultVersion, environment, scenename, template, i, ValueList.length - 1, oppValue);

        }
    }
    if (aggVersion.length > 0) {
        bandAggChart(aggVersion, aggenv, aggscene, aggtem);
    }

    loagShow(false);
}

function loagShow(isHidden) {
    var btn = $("#btn_loading");
    btn.button('loading');
    if (isHidden) {
        btn.removeClass('hidden');
    } else {
        btn.addClass('hidden');
    }
}

function loagShow2(isHidden, id) {
    var btn = $("#" + id);
    btn.button('loading');
    if (isHidden) {
        btn.removeClass('hidden');
    } else {
        btn.addClass('hidden');
    }
}

//selectLabelInput
function selectCheck(input, max, ischecked) {
    var obj = $(input);
    var isCheckCount = max != 2;
    var checked = ischecked == null ? obj.is(':checked') : ischecked;

    var selectCheckBoxCount = Number($("#hiddenValue").attr('selectCheckBoxCount'));
    var selectCheckBoxValue = $("#hiddenValue").attr('selectCheckBoxValue');
    if (checked && selectCheckBoxCount == maxCheckBoxCount && isCheckCount) {
        obj.prop("checked", false);
        return showError('选择对比数据过多', '对比报告最多选择数据条数为：' + maxCheckBoxCount + ' !');

    }

    else if (checked) {
        selectCheckBoxValue = selectCheckBoxValue + "," + obj.attr('id') + ",";
        $("#hiddenValue").attr('selectCheckBoxCount', selectCheckBoxCount + 1);
        $("#hiddenValue").attr('selectCheckBoxValue', selectCheckBoxValue);

        $("#hiddenValue").attr(obj.attr('id'), obj.attr('resultversion') + "," + obj.attr('scenename') + "," + obj.attr('environment') + "," + obj.attr('template'));
    } else {
        selectCheckBoxValue = selectCheckBoxValue.replace("," + obj.attr('id') + ",", "");

        $("#hiddenValue").attr('selectCheckBoxCount', selectCheckBoxCount - 1);
        $("#hiddenValue").attr('selectCheckBoxValue', selectCheckBoxValue);

        $("#hiddenValue").attr(obj.attr('id'), '');
    }

    var grpOption = $("#showGrpThreads");
    if (!isCheckCount && grpOption) {
        var grp = new Array();
        var value = grpOption.find("option:selected").val();
        $("input[name='selectLabelInput']").each(function (i, n) {
            var obj2 = $(n);
            if (obj2.prop('checked') || obj2.attr('id') == obj.attr('id') && ischecked) {
                var list = obj2.attr('grp').split(',');
                for (var t = 0; t < list.length; t++) {

                    if (!grp.contains(list[t])) {
                        grp.push(list[t]);
                    }
                }
            }

        });

        grp.sort(function (a, b) {
            return Number(a) > Number(b) ? 1 : -1
        });
        grpOption.empty();
        grpOption.append("<option value=''></option>");

        for (var i = 0; i < grp.length; i++) {

            grpOption.append("<option value='" + grp[i] + "'>" + grp[i] + "</option>");
            if (value == grp[i]) {
                grpOption.find("option[value='" + grp[i] + "']").prop("selected", true);
            }
        }
    }


}


function showAggReport(version,env,scene,tem,timeavg,index)
{
    $.ajax({
        url: "/gtp-report/getjmtaggreport",
        dataType: "json",
        type: "POST",
        async: false,
        data: {
            "aggVersion": version,
            "aggenv": env,
            "aggscene": scene,
            "aggtem": tem,
            "timeavg": timeavg

        },//report_container_
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $("#hiddenValue").attr('sceneName', '');
                $("#hiddenValue").attr('smallSceneName', '');
            } else {

                data = data['data'];

                var report_Table = $("#report_list").clone(true);
                report_Table.removeAttr('id');
                report_Table.removeClass('hidden');
                $("#report_list").addClass('hidden');
                report_Table.attr("id", "report_list"+index);
                $('#report_aggreport_div').append(report_Table);

                //$("#report_list")
                $("#report_list"+index+" tr:first th:first").html("场景_"+ scene+"   环境_"+ env + (tem.length==0?"":"  模板_") + tem );

                $("#report_aggreport_div").removeClass('hidden');

                $('#report_list'+index).DataTable({
                    "bLengthChange": true,
                    "processing": false,
                    "bDestroy": true,
                    data: data,
                    columns: [
                        {data: "resultVersion"},
                        {data: "labelName"},
                        {data: "begin"},
                        {data: "end"},
                        {data: "smallSceneName"},
                        {data: "grpThreads"},
                        {data: "avg"},
                        {data: "minElapsed"},
                        {data: "maxElapsed"},
                        {data: "elapsed50"},
                        {data: "elapsed90"},
                        {data: "elapsed95"},
                        {data: "elapsed99"},
                        {data: "fail"},
                        {data: "tpsAvg"}
                    ], language: {
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
                    "createdRow": function ( row, data, index ) {
                        if ( data['newData'] == true ) {//操作次数大于1000的变红显示
                            $('td', row).css('font-weight',"bold").css("background","gray");
                        }
                    },
                    columnDefs: [
                        {
                            targets: [13],
                            render: function (data, type, full) {

                                if (data) {
                                    var error = parseFloat(data) * 100;

                                    return (error == 0 ? "" : error.toFixed(2) + "%" );
                                } else {
                                    return data;
                                }
                            }
                        }
                    ]

                });

            }
        }

    });
}

function bandAggChart(aggVersion, aggenv, aggscene, aggtem) {

    $('#report_aggreport_div').children(':not(#report_list)').remove();




    var timeavg = $("#task_time").val();
    var aggVersionList=aggVersion.split(',');
    var aggenvList= aggenv.split(',');
    var aggsceneList= aggscene.split(',');
    var aggtemList= aggtem.split(',');

    for(var i=0;i< aggVersion.length;i++)
    {
        var version = aggVersionList[i];
        var env = aggenvList[i];
        var scene= aggsceneList[i];
        var tem = aggtemList[i];

        if(version!= undefined && version.length>0 && env.length>0 && scene.length>0)
        {
            showAggReport(version,env,scene,tem,timeavg,i);
        }

    }


}


function bandTpsChart(resultVersion, environment, sceneName, template, thisIndex, maxIndex, oppValue) {

    var checkLabelCount = 5;
    var tdCount = 0;
    var trCount = 0;
    var name = resultVersion + "_" + sceneName + "_" + environment + ( template.length > 0 ? "_" : "" ) + template;
    name= name.replace("*","X").replace(" ","");
    var version = "";
    var timeVersion = "";
    if (resultVersion.indexOf("_") > 0) {
        var versionList = resultVersion.split("_");
        version = versionList[0];
        timeVersion = versionList[1];
    } else {
        version = resultVersion;
    }

    var id = '';
    $.ajax({
        url: "/gtp-report/getHistoryTpsJMTReport",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": version,
            "timeVersion": timeVersion,
            "environment": environment,
            "sceneName": sceneName,
            "template": template,
            "maxdataCount": maxdataCount
        },

        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {

                data = data['data'];
                for (var i = 0; i < data.length; i++) {
                    var optionList = data[i][0].split('_');
                    var xdata = data[i][7];
                    var grpThreads = data[i][2];
                    var elapsedCount = data[i][3];
                    var success = data[i][4];
                    var errorCount = data[i][5];
                    var avg = data[i][6];
                    var option = optionList[0];
                    var checked = (oppValue.length == 0 && i == 0) || (oppValue.length > 0 && option.indexOf(oppValue) == 0 ) ? "checked='checked'" : "";
                    var timeStamp = data[i][1];

                    var datacount = Number(optionList[1]);
                    if (i == 0) {
                        $('#filter_table_tbody2').append("<tr><th  align='center' colspan='" + checkLabelCount + "'>" + name + "</th></tr>");

                    }
                    if (i == 0 || i % checkLabelCount == 0) {

                        id = "jmt_report_label_" + name + "_" + trCount;
                        id = id.replace(".", "");
                        var labelTr = $("#labelTR").clone(true);
                        labelTr.removeAttr('id');
                        labelTr.attr("id", id);
                        $('#filter_table_tbody2').append(labelTr);
                        tdCount = 0;
                        trCount++;

                    }

                    var html = "<td><input type='checkbox' onclick='checkFun(0)' version='" + resultVersion + "' xdata='" + xdata + "' timestamp='" + timeStamp + "' grpThreads='" + grpThreads + "' elapsedCount='" + elapsedCount + "' " +
                        "success='" + success + "' errorCount='" + errorCount + "' avg='" + avg + "' name='chklabel' value='" + option + "' " + checked + " ></input> " + option + ( datacount > 1 ? "_Merge " + datacount : "") + " </td>";
                    $("#" + id).append(html);
                    tdCount++;

                }
                while (tdCount < checkLabelCount) {
                    $("#" + id).append("<td></td>");
                    tdCount++;
                }
                if (thisIndex == maxIndex) {
                    loagShow(false);
                    $("#report_tps").removeClass('hidden');
                    $("#filter_table_tbody2").removeClass("hidden");
                }

                checkFun(0);
            }


        }

    });


}


function checkFun(grpthreads) {
    $("#report_tps").addClass('hidden');
    var data = new Array();
    var threads = Number(grpthreads);
    $("input[name='chklabel']").each(function (i, n) {
        var obj = $(n);
        if (obj.prop("checked")) {
            var dataDetails = threads == 0 ? checkFun2(obj) : checkFun3(obj);
            for (var i = 0; i < dataDetails.length; i++) {
                data.push(dataDetails[i]);
            }

        }

    });

    if (data.length == 0) {
        return;
    }

    var count = 0;
    var max = 0;
    var min = 0;
    for (var i = 0; i < data.length; i++) {
        var thisnumber = Number(data[i].data[data[i].data.length - 1][0]);
        var thsminnumber = Number(data[i].data[0][0]);
        max = (i == 0 || max < thisnumber ) ? thisnumber : max;
        min = (i == 0 || min > thsminnumber) ? thsminnumber : min;
    }

    var len = parseInt((max - min) / 4);
    if (len < 1) {
        count = 1;
    } else {
        count = len;
    }


    $("#report_tps").removeClass('hidden');
    Highcharts.setOptions({global: {useUTC: false}});

    if (threads > 0) {
        $('#report_tps').highcharts({
            chart: {
                type: 'spline'
            },
            title: {
                text: "图表报告 "
            },
            subtitle: {
                text: grpthreads == 0 ? "" : "GrpThreads " + threads
            },
            xAxis: {
                labels: {
                    formatter: function () {
                        var date = getDateX(this.value);
                        return date;
                    }
                }
                ,
                tickInterval: count * 1 //后台处理过内容，所以一天一个刻度
            },
            yAxis: [{
                labels: {
                    formatter: function () {
                        return this.value;
                    },
                    style: {
                        color: '#AA4643'
                    }
                },
                title: {
                    text: ' ResponseTime ',
                    style: {
                        color: '#AA4643'
                    }
                }
            },
                {
                    labels: {
                        formatter: function () {
                            return this.value;
                        },
                        style: {
                            color: '#4572A7'
                        }
                    },
                    title: {
                        text: ' TPS ',
                        style: {
                            color: '#4572A7'
                        }
                    },
                    opposite: true
                }
            ],
            tooltip: {
                formatter: function () {

                    var date = getTimeStp(this.series.name, this.x);
                    return '<b>' + this.series.name + '</b><br/> ' + date + '  Value: (' + this.y + ' )';

                    //return '<b>' + this.series.name + '</b><br/> ' + (  Highcharts.dateFormat((grpthreads ==0 ?'%Y-%m-%d %H:%M:%S':'%Y-%m-%d'), date) ) + '  Value: (' + this.y + ' )';

                }

            },
            series: data,
            global: {
                useUTC: false
            },
            credits: {
                enabled: false //关闭右下角Logo
            }
        });
    } else {
        $('#report_tps').highcharts({
            chart: {
                type: 'spline'
            },
            title: {
                text: "图表报告 "
            },
            subtitle: {
                text: grpthreads == 0 ? "" : "GrpThreads " + threads
            },
            yAxis: [{
                labels: {
                    formatter: function () {
                        return this.value;
                    },
                    style: {
                        color: '#AA4643'
                    }
                },
                title: {
                    text: ' ResponseTime ',
                    style: {
                        color: '#AA4643'
                    }
                }
            },
                {
                    labels: {
                        formatter: function () {
                            return this.value;
                        },
                        style: {
                            color: '#89A54E'
                        }
                    },
                    title: {
                        text: ' Threads ',
                        style: {
                            color: '#89A54E'
                        }
                    },
                    opposite: true,
                    visible: false

                },
                {
                    labels: {
                        formatter: function () {
                            return this.value;
                        },
                        style: {
                            color: '#4572A7'
                        }
                    },
                    title: {
                        text: ' TPS ',
                        style: {
                            color: '#4572A7'
                        }
                    },
                    opposite: true
                }
            ],
            tooltip: {
                formatter: function () {

                    var date = getTimeStp(this.series.name, this.x);

                    return '<b>' + this.series.name + '</b><br/> ' + (  Highcharts.dateFormat((grpthreads == 0 ? '%Y-%m-%d %H:%M:%S' : '%Y-%m-%d'), date) ) + '  Value: (' + this.y + ' )';

                }

            },
            series: data,
            global: {
                useUTC: false
            },
            credits: {
                enabled: false //关闭右下角Logo
            }
        });
    }


}

//计算当前X轴显示的日期
function getDateX(x) {
    var minversion = $("#hiddenValue").attr("minversion");
    var date = new Date(minversion);
    date = date.valueOf();
    date = date + Number(x) * 24 * 60 * 60 * 1000;
    date = new Date(date);

    return date.getFullYear() + '-' + (date.getMonth() + 1) + "-" + date.getDate();
}

//计算图标显示内容 日期
function getTimeStp(vname, x) {
    var date = '';
    $("input[chartValue]").each(function (i, n) {
        if (date == '') {
            var obj = $(n);
            if (obj.attr('chartValue')) {

                if (obj.attr('chartValue').indexOf("&" + vname + "&") >= 0) {
                    var timestapList = obj.attr("timestamp").split(',');
                    var xdata = obj.attr('xdata').split(',');


                    for (var i = 0; i < xdata.length; i++) {
                        if (xdata[i] == x) {
                            date = timestapList[i];
                            break;
                        }
                    }

                }
            }
        }

    });

    return date;
}

function checkFun2(obj) {
    var resultData = new Array();
    var version = obj.attr('version');
    var timestapList = obj.attr("timestamp").split(',');
    var grpthreadsList = obj.attr('grpthreads').split(',');
    var elapsedCountList = obj.attr('elapsedcount').split(',');
    var successList = obj.attr('success').split(',');
    var errorcountList = obj.attr('errorcount').split(',');
    var avgList = obj.attr('avg').split(',');
    var xdata = obj.attr('xdata').split(',');
    var value = obj.attr("value");


    //GrpThreads
    var resultGrpThreads = {};
    resultGrpThreads["name"] = "Threads( " + value + " _" + version + ")";
    resultGrpThreads["yAxis"] = 1;
    //Total
    var total = {};
    total["name"] = "Total( " + value + " _" + version + ")";
    total["yAxis"] = 2;
    //SuccessCount
    var successCount = {};
    successCount["name"] = "Success(" + value + " _" + version + ")";
    successCount["visible"] = false;//绑定后就隐藏
    successCount["yAxis"] = 2;
    //Error
    var errorCount = {};
    errorCount["name"] = "Error(" + value + " _" + version + ")";
    errorCount["visible"] = false;//绑定后就隐藏
    errorCount["yAxis"] = 2;
    //avg
    var avg = {};
    avg["name"] = "ART(" + value + " _" + version + ")";
    avg["yAxis"] = 0;

    obj.attr("chartValue", "&" + resultGrpThreads["name"] + "&" + total["name"] + "&" + successCount["name"] + "&" + errorCount["name"] + "&" + avg["name"] + "&");

    var grpdata = new Array();
    var totaldata = new Array();
    var successdata = new Array();
    var errordata = new Array();
    var avgdata = new Array();
    for (var i = 0; i < timestapList.length; i++) {
        if (timestapList[i].length > 0) {
            var dataX = Number(xdata[i]);

            var dataDetail = new Array();
            dataDetail.push(dataX);
            dataDetail.push(parseFloat(grpthreadsList[i]));
            grpdata.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(dataX);
            dataDetail.push(parseFloat(elapsedCountList[i]));
            totaldata.push(dataDetail);

            dataDetail = new Array();

            dataDetail.push(dataX);
            dataDetail.push(parseFloat(successList[i]));
            successdata.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(dataX);
            dataDetail.push(parseFloat(errorcountList[i]));
            errordata.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(dataX);
            dataDetail.push(parseFloat(avgList[i]));
            avgdata.push(dataDetail);


        }
    }

    resultGrpThreads["data"] = grpdata;
    total["data"] = totaldata;
    successCount["data"] = successdata;
    errorCount["data"] = errordata;
    avg["data"] = avgdata;

    resultData.push(resultGrpThreads);
    resultData.push(total);
    resultData.push(successCount);
    resultData.push(errorCount);
    resultData.push(avg);
    return resultData;
}

function checkFun3(obj) {
    var resultData = new Array();
    var timestapList = obj.attr("timestamp").split(',');
    var xdataList = obj.attr("xdata").split(',');
    var elapsed90List = obj.attr('elapsed90').split(',');
    var tpsList = obj.attr('tps').split(',');
    var avgList = obj.attr('avg').split(',');
    var value = obj.attr("value");


    //tps
    var tps = {};
    tps["name"] = "TPS_" + value;
    tps["yAxis"] = 1;
    //elapsed90
    var elapsed90 = {};
    elapsed90["name"] = "90%_" + value;
    elapsed90["yAxis"] = 0;

    //avg
    var avg = {};
    avg["name"] = "ART_" + value;
    avg["yAxis"] = 0;

    obj.attr("chartValue", "&" + tps["name"] + "&" + elapsed90["name"] + "&" + avg["name"] + "&");

    var tpsdata = new Array();
    var elapsed90data = new Array();
    var avgdata = new Array();
    for (var i = 0; i < timestapList.length; i++) {
        if (timestapList[i].length > 0) {

            var dataX = Number(xdataList[i]);
            var dataDetail = new Array();
            dataDetail.push(dataX);
            dataDetail.push(parseFloat(tpsList[i]));
            tpsdata.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(dataX);
            dataDetail.push(parseFloat(elapsed90List[i]));
            elapsed90data.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(dataX);
            dataDetail.push(parseFloat(avgList[i]));
            avgdata.push(dataDetail);


        }
    }


    tps["data"] = tpsdata;
    elapsed90["data"] = elapsed90data;
    avg["data"] = avgdata;


    resultData.push(tps);
    resultData.push(elapsed90);
    resultData.push(avg);
    return resultData;
}

//隐藏历史对比图
function cleanHistoryReport() {
    $("#filter_table_tbody2").empty();
    $("#filter_table_tbody2").addClass("hidden");

    $("#report_tps").addClass('hidden');
    $('#report_label_div').addClass('hidden');
    $("#hiddenValue").attr('selectCheckBoxValue', '');
    $("#tdShowHistory").addClass("hidden");
}

function selectAll(n) {
    var obj = $(n);

    var checked = obj.is(':checked');

    if (!checked) {
        $("#hiddenValue").attr('selectCheckBoxValue', '');
        $("#hiddenValue").attr('selectCheckBoxCount', 0);
        $("#showGrpThreads").empty();
    }
    $("input[name='selectLabelInput']").each(function (i, t) {
        var tobj = $(t);

        if (!tobj.is(":checked") && checked) {

            selectCheck(t, 2, checked);
        }
        tobj.prop("checked", checked);

    });

}