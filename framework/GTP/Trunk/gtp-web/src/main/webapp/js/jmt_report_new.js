/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//最多展示的条数
var maxdataCount=200;
var alreadyLogin = isLogin();

var sceneNamesites=null;
var taskListites=null;

$(document).ready(function () {
    dateShow();
    reportSceneName();
    reportTask();
    $("#report_edit_btn").bind('click',function(){
        if(alreadyLogin)
        {
            goEdit();
        }else
        {
            intervalActiveLogin();
        }

    });

    $("#report_type_group").bind('change',function(){
        reportSceneName();
        reportTask();
    });


    $("#task_type_smallSceneName").bind('change', function () {
        getVersion();
        showTpsReport();
    })
    $("#task_type_labelName").bind('change', function () {
        getVersion();
    })
    $("#dimension_environment").bind('change', function () {
        getVersion();
    })

    $('#report_generate_btn').bind('click', function () {
        showChart();
    });
    $('#report_begin_div').bind('click', function () {
        $('#report_timespan_begin_input').datetimepicker('show');
    });


    showSceneName_search();
    showTaskName_search();

    $('#etp_report_result').addClass('hidden');


    $("#hiddenValue").attr('smallSceneName', '');

});

function showSceneName_search(){

    if (sceneNamesites == null)
        return;

    $('#product_search').typeahead({
        items: 8,
        minChars:0,
        source: function(query, process) {
            return sceneNamesites;
        },
        //匹配
        matcher: function (item) {
            return ~item.toLowerCase().indexOf(this.query.toLowerCase())
        },
        //显示
        highlighter: function(item) {
            return  item ;
        },
        //选中
        updater: function(item) {
            $("#hiddenValue").attr('sceneName', item);
            reportTypeSelect();
            labelNameSelect();
            getEnvironment();
            getVersion();
            return item;
        },
        autoSelect:true
    });
}


function showTaskName_search(){

    if (taskListites == null)
        return;

    $('#task_search').typeahead({
        items: 8,
        minChars:0,
        source: function(query, process) {
            return taskListites;
        },
        //匹配
        matcher: function (item) {

            return ~item.toLowerCase().indexOf(this.query.toLowerCase())
        },
        //显示
        highlighter: function(item) {
            return  item ;
        },
        //选中
        updater: function(item) {

            $("#hiddenValue").attr('taskid', item);
            reportSceneName();
            return item;
        },
        autoSelect:true
    });
}

$('#menu-taskName').on('click', 'a', function () {
    var taskValue = $(this).text();

    $("#task_search").val(taskValue);
    $("#hiddenValue").attr('taskid', $(this).attr('data-content'));
    reportSceneName();
});



function updateSceneName_search(data) {
    sceneNamesites=data;
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

$('#menu-sceneName').on('click', 'a', function () {
    var sceneName = $(this).text();
    $("#product_search").val(sceneName);
    $("#hiddenValue").attr('sceneName', sceneName);
    reportTypeSelect();
    labelNameSelect();
    getEnvironment();
    getVersion();

});




function goEdit()
{
    window.location.href='/gtp-report/jmtedit/';
}

function loag(isHidden) {
    var btn = $("#btn_loading");
    btn.button('loading');
    if(isHidden){
        btn.removeClass('hidden');
    }else{
        btn.addClass('hidden');
    }
}

function showTpsReport() {
    var smallsceneName = $.trim($('#task_type_smallSceneName').find('option:selected').text());

    var template = $.trim($("#dimension_template").find('option:selected').text());

    if (smallsceneName.length > 0 || template.length > 0) {
        $("#filter_table_tbody2").removeClass('hidden');
    } else {
        $("#filter_table_tbody2").addClass('hidden');
    }

}

function reportTask(){

    $('#task_search').val('');

    var groupid =$.trim($('#report_type_group').find('option:selected').val());
    var template = $.trim($('#dimension_template').find('option:selected').val()) ;
    var sceneName =  $("#hiddenValue").attr('sceneName');
    sceneName = sceneName== undefined?"":sceneName;

    $.ajax({
        type: 'POST',
        url: '/gtp-report/taskName',
        async: false,
        data: {
            'groupid': groupid,
            'sceneName':sceneName,
            'template':template,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
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
        }
    });


    ///
}



//绑定大场景
function reportSceneName() {
    $("#hiddenValue").attr('sceneName','');
    $("#product_search").val('');

    $('#task_type_labelName').empty();
    $('#task_type_smallSceneName').empty();
    $("#dimension_environment").empty();
    $("#report_timespan_begin_input").val('');

    var taskid =  $("#hiddenValue").attr('taskid');
    if($("#task_search").val().length==0)
    {
        taskid='';
    }else
    {
        var first= taskid.indexOf('(');
        var end= taskid.indexOf(')');
        taskid = taskid.substring(first+1,end);
    }

    var groupid = $('#report_type_group').find('option:selected').val();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtSceneName',
        async: false,
        data: {
            'groupid': groupid,
            'template':'',
            'taskid':taskid,
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
//绑定大场景名称select事件
function  reportTypeSelect(){
    $('#task_type_smallSceneName').empty();
    $("#task_type_labelName").empty();
    var sceneName = $("#hiddenValue").attr('sceneName');
    if (sceneName != "") {
        getSmallSceneName(sceneName, true);
    }
}


function getEnvironment() {
    var sceneName = $("#hiddenValue").attr('sceneName');
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

    getVersion();
}
//根据版本日期和其他条件查询，该日期下的所有时间选项
function getVersionTime()
{
    var lastdate= $('#report_timespan_begin_input').attr('last');
    var resultdate=$('#report_timespan_begin_input').val();

    if(lastdate==resultdate)
    {
        return false;
    }
    $('#report_timespan_begin_input').attr('last',resultdate);
    $('#result_time').empty();

    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallsceneName = $('#task_type_smallSceneName').find('option:selected').text();
    var labelName = $('#task_type_labelName').find('option:selected').text();
    var environment = $('#dimension_environment').find('option:selected').text();

    if(sceneName.length==0 )
    {
        return false;
    }
    $.ajax({
        type: 'POST',
        url: '/gtp-report/getVersionTime',
        async: false,
        data: {
            'sceneName': sceneName,
            'smallSceneName': smallsceneName,
            'labelName': labelName,
            'environment': environment,
            'resultdate':resultdate,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                if(data!=null) {
                    var options = "";
                    for (var i = 0; i < data.length; ++i) {
                        var option = data[i];
                        options += '<option value="' + option + '" >' + option + '</option>';
                    }
                    $('#result_time').append(options);
                }
            }
        }
    });
}


//根据相关条件查询版本号，取最新的绑定版本日期和时间
function getVersion() {
    $('#result_time').empty();
    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallsceneName = $('#task_type_smallSceneName').find('option:selected').text();
    var labelName = $('#task_type_labelName').find('option:selected').text();
    var environment = $('#dimension_environment').find('option:selected').text();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtgetVersion',
        async: false,
        data: {
            'sceneName': sceneName,
            'smallSceneName': smallsceneName,
            'labelName': labelName,
            'environment': environment,
            'isEnable': true
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                if(data['message']!=null)
                {
                    $('#report_timespan_begin_input').val(data['message']);
                }
                data = data['data'];

                if(data!=null)
                {
                    var options = "";
                    for (var i = 0; i < data.length; ++i) {
                        var option = data[i];

                        options += '<option value="' + option + '" >' + option + '</option>';
                    }

                    $('#result_time').append(options);
                }


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
        format: 'Ymd',
        onChangeDateTime:function(dp,$input){
            getVersionTime($input);
        }
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


function bandTable(data,sceneName){

    $("#report_list").removeClass('hidden');

    $('#report_title').html(sceneName);
    $('#report_list').DataTable({
        "bLengthChange": true,
        "processing": false,
        "bDestroy":true,
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
            {data: "tps"}
        ], language:{
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
            targets: [13],
            render: function (data, type, full) {

                if (data) {
                    var error=  parseFloat(data) * 100;

                    return   (error==0 ? "" :error.toFixed(2) +"%" );
                } else {
                    return data;
                }
            }
        }
    ]

    });

}

function showChart() {

    $("#remark").addClass('hidden');
    $("#remark").html('');
    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallSceneName = $('#task_type_smallSceneName').find('option:selected').text();
    var labelName = $('#task_type_labelName').find('option:selected').text();
    var template = $('#dimension_template').find('option:selected').val();
    var resultVersion = $("#report_timespan_begin_input").val();
    var timeVersion=$("#result_time").val();

    var environment = $('#dimension_environment').find('option:selected').text();
    var oppValue=  labelName + (smallSceneName.length==0?"":"("+smallSceneName+")") ;


    var timeavg=$('#task_time').val();

    var taskid = $("#hiddenValue").attr('taskid');
    if($("#task_search").val().length==0)
    {
        taskid='';
    }else
    {
        var first= taskid.indexOf('(');
        var end= taskid.indexOf(')');
        taskid = taskid.substring(first+1,end);
    }

    if (sceneName == "") {
        return showError('场景发生异常！', '场景不能为空!');
    }


    if (resultVersion == "") {
        return showError('版本发生异常', '版本不能为空!');
    }
    if(timeVersion =="")
    {
        return showError('版本发生异常', '时间版本不能为空!');
    }
    if (environment == "") {
        return showError('环境发生异常', '请选择环境!');
    }

    $('#report_label_div').addClass('hidden');

    $("#hiddenValue").attr('sceneName', sceneName);
    $("#hiddenValue").attr('smallSceneName', smallSceneName);

    var createTime='';
    var reportdata ;
    $.ajax({
        url: "/gtp-report/getjmtreport",
        dataType: "json",
        type: "POST",
        async: false,
        data: {
            "resultVersion": resultVersion,
            "timeVersion":timeVersion,
            "environment": environment,
            "sceneName": sceneName,
            "taskId":taskid,
            "timeavg":timeavg,
            "smallSceneName": smallSceneName,
            "labelName": labelName,
            "template": template

        },//report_container_
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $("#hiddenValue").attr('sceneName', '');
                $("#hiddenValue").attr('smallSceneName', '');
            } else {

                data = data['data'];
                bandTable(data,sceneName);
                if(data.length > 0 )
                {
                    createTime = data[data.length-1].createTime;
                    reportdata=data;
                }
                //注释掉js绑定
                $("#filter_table_tbody2").removeClass("hidden");


            }
        }

    });
    $("#filter_table_tbody2").empty();
    $("#report_tps").addClass('hidden');
    //绑定第二张图
    if ((smallSceneName.length > 0 || template.length > 0) ) {
        //绑定各个接口事件的checkbox
            loag(true);
            bandTpsChart(resultVersion,  environment, sceneName, smallSceneName, template, oppValue,taskid,timeavg);
    }

    //显示备注
    if(reportdata.length >0 )
    {
        showRemark(sceneName,smallSceneName,environment,template,resultVersion,timeVersion,createTime);
    }


}

function showRemark(sceneName,smallSceneName,environment,template,resultVersion,timeVersion,createtime)
{
    $.ajax({
        url: "/gtp-report/getjmtRemark",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "environment": environment,
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "template": template,
            "timeVersion":timeVersion,
            "createtime":createtime
        },
        success: function (data) {
            if (!data['isError'] && data['data'].remark != null ) {
                $("#remark").html(data['data'].remark);
                if(data['data'].remark.length>0)
                {
                    $("#remark").removeClass('hidden');
                }
            }
        }

    });
}

function bandTpsChart(resultVersion, environment, sceneName, smallSceneName, template, oppValue,taskid,timeavg) {

    $("#filter_table_tbody2").empty();
    var checkLabelCount = 5;
    var tdCount = 0;
    var trCount = 0;

    $.ajax({
        url: "/gtp-report/getTpsJMTReport",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "environment": environment,
            "sceneName": sceneName,
            "taskid":taskid,
            "timeavg":timeavg,
            "smallSceneName": smallSceneName,
            "labelName": '',
            "template": template,
            "maxdataCount":maxdataCount
        },
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                for (var i = 0; i < data.length; i++) {
                    var optionList = data[i][0].split('_');
                    var timeStamp= data[i][1];
                    var grpThreads= data[i][2];
                    var elapsedCount=data[i][3];
                    var success= data[i][4];
                    var errorCount=data[i][5];
                    var avg=data[i][6];
                    var option =optionList[0];
                    var datacount= Number(optionList[1]);
                    var checked = option.indexOf(oppValue)>-1 ?"checked='checked'":"";
                    if (i == 0 || i % checkLabelCount == 0) {
                        trCount++;
                        var labelTr = $("#labelTR").clone(true);
                        labelTr.removeAttr('id');
                        labelTr.attr("id", "jmt_report_label_" + trCount);
                        $('#filter_table_tbody2').append(labelTr);
                        tdCount = 0;
                    }
                    var html = "<td><input type='checkbox' onclick='checkFun()' datacount='"+datacount+"' timeStamp='"+timeStamp+"' grpThreads='"+grpThreads+"' elapsedCount='"+elapsedCount+"' " +
                               "success='"+success+"' errorCount='"+errorCount+"' avg='"+avg+"' name='chklabel' value='" + option + "' "+checked+" ></input> " + option+( datacount>1 ? "_Merge "+datacount : "") + " </td>";
                    $("#jmt_report_label_" + trCount).append(html);
                    tdCount++;

                    //var obj = $("input[value='" + data[i][0] + "']");

                    if ( option.indexOf(oppValue)>-1) {

                            checkFun();
                    }


                }
                while (tdCount < checkLabelCount) {
                    $("#jmt_report_label_" + trCount).append("<td></td>");
                    tdCount++;
                }

                loag(false);

            }
        }

    });

}

function checkFun2(obj) {
    var resultData = new Array();

    var timestapList = obj.attr("timestamp").split(',');
    var grpthreadsList = obj.attr('grpthreads').split(',');
    var elapsedCountList = obj.attr('elapsedcount').split(',');
    var successList = obj.attr('success').split(',');
    var errorcountList = obj.attr('errorcount').split(',');
    var avgList = obj.attr('avg').split(',');

    var value = obj.attr("value");

    //GrpThreads
    var resultGrpThreads = {};
    resultGrpThreads["name"] = "Threads( " + value + " )";
    resultGrpThreads["yAxis"] = 1;
    //Total
    var total = {};
    total["name"] = "Total( " + value + " )";
    total["yAxis"] = 2;
    //SuccessCount
    var successCount = {};
    successCount["name"] = "Success(" + value + ")";
    successCount["visible"] = false;//绑定后就隐藏
    successCount["yAxis"] = 2;
    //Error
    var errorCount = {};
    errorCount["name"] = "Error(" + value + ")";
    errorCount["visible"] = false;//绑定后就隐藏
    errorCount["yAxis"] = 2;
    //avg
    var avg = {};
    avg["name"] = "ART(" + value + ")";
    avg["yAxis"] = 0;

    var grpdata = new Array();
    var totaldata = new Array();
    var successdata = new Array();
    var errordata = new Array();
    var avgdata = new Array();
    for (var i = 0; i < timestapList.length; i++) {
        if (timestapList[i].length > 0) {
            var timestap = timestapList[i].split(":");//new Date().getTime();//
            var times = Number(timestapList[i]);  //Date.UTC(timestap[0],timestap[1],timestap[2],timestap[3],timestap[4],timestap[5]);


            var dataDetail = new Array();
            dataDetail.push(times);
            dataDetail.push(parseFloat(grpthreadsList[i]));
            grpdata.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(times);
            dataDetail.push(parseFloat(elapsedCountList[i]));
            totaldata.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(times);
            dataDetail.push(parseFloat(successList[i]));
            successdata.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(times);
            dataDetail.push(parseFloat(errorcountList[i]));
            errordata.push(dataDetail);

            dataDetail = new Array();
            dataDetail.push(times);
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


function checkFun() {
    var sceneName = $("#hiddenValue").attr('sceneName');
    var smallSceneName = $("#hiddenValue").attr('smallSceneName');


    var data = new Array();

    $("input[name='chklabel']").each(function (i, n) {
        var obj = $(n);
        if (obj.prop("checked")) {
            var dataDetails = checkFun2(obj);
            for (var i = 0; i < dataDetails.length; i++) {
                data.push(dataDetails[i]);
            }

        }

    });


    var count = 20 ;
    if(data.length> 0 )
    {
        if (data[0].data.length >= 100 && data[0].data.length <500)
        {
            count = 30 ;
        }else if(data[0].data.length >= 500 && data[0].data.length <800)
        {
            count = 2 * 60 ;
        }else if(data[0].data.length >= 800 && data[0].data.length <1000)
        {
            count = 5 * 60 ;
        }else if (data[0].data.length >= 1000 && data[0].data.length <5000)
        {
            count = 10 * 60 ;
        }else if(data[0].data.length >= 5000 && data[0].data.length <10000)
        {
            count = 15 * 60 ;
        }else
        {
            count = 20 * 60 ;
        }

    }



    $("#report_tps").removeClass('hidden');

    Highcharts.setOptions({global: {useUTC: false}});

    $('#report_tps').highcharts({
        chart: {
            type: 'spline'
        },
        title: {
            text: "图表报告 ( " + sceneName + " )"
        },
        subtitle: {
            text: ""//genSubtitle(sceneName,labelNameVal)
        },
        xAxis: {
            type: 'datetime',

            labels: {
                step: 1,//相隔一个标尺显示
                formatter: function () {
                    return Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.value);
                }
            }
            //,
            //maxPadding : 0.05,
            //minPadding : 0,
            //showFirstLabel:true,
            //tickPixelInterval: 50  //间隔像素
            //tickInterval: count  * 1000   //50秒画一个x刻度
            //或者150px画一个x刻度，如果跟上面那个一起设置了，则以最大的间隔为准
            //tickPixelInterval : 150,
            //tickWidth:5,//刻度的宽度
            //lineWidth :3//自定义x轴宽度
            //tooltip:{
            //    formatter:function(){
            //        return Highcharts.dateFormat('%Y-%m-%d %H:%M:%S',this.value);
            //    }
            //}

        },
        yAxis: [{
            labels: {
                formatter: function() {
                    return this.value ;
                },
                style: {
                    color: '#AA4643'
                }
            },
            title: {
                text: ' ResponseTime ' ,
                style: {
                    color: '#AA4643'
                }
            }
        },
            {
                labels: {
                    formatter: function() {
                        return this.value ;
                    },
                    style: {
                        color: '#89A54E'
                    }
                },
                title: {
                    text: ' Threads ' ,
                    style: {
                        color: '#89A54E'
                    }
                },
                opposite: true

            },
            {
                labels: {
                    formatter: function() {
                        return this.value  ;
                    },
                    style: {
                        color: '#4572A7'
                    }
                },
                title: {
                    text: ' TPS ' ,
                    style: {
                        color: '#4572A7'
                    }
                },
                opposite: true
            }
        ],
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br/> ' + Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '  Value: (' + this.y + ' )';
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


function setResultBody(sceneName, data) {
    if (data.length > 0)
        setResultTitle(sceneName);

    for (var t = 0; t < data.length; t++) {
        var item = data[t];
        var sceneName = item["sceneName"];
        var resultVersion = item["resultVersion"];
        var smallsSceneName = item["smallSceneName"];
        var labelName = item["labelName"];
        var grpThreads = item["grpThreads"];
        var avg = item["avg"];
        var min = item["minElapsed"];
        var max = item["maxElapsed"];
        var elapsed50 = item["elapsed50"];
        var elapsed90 = item["elapsed90"];
        var elapsed95 = item["elapsed95"];
        var elapsed99 = item["elapsed99"];
        var error = item["fail"];
        var tpsAvg = item["tpsAvg"];
        var begin = item["begin"];
        var end = item["end"];

        var tr = document.createElement('tr');


        var td_resultVersion = document.createElement('td');
        var td1_txt = document.createTextNode(resultVersion);
        td_resultVersion.appendChild(td1_txt);
        tr.appendChild(td_resultVersion);

        var td_begin = document.createElement('td');
        td1_txt = document.createTextNode(begin);
        td_begin.appendChild(td1_txt);
        tr.appendChild(td_begin);

        var td_end = document.createElement('td');
        td1_txt = document.createTextNode(end);
        td_end.appendChild(td1_txt);
        tr.appendChild(td_end);

        var td_smallsSceneName = document.createElement('td');
        td1_txt = document.createTextNode(smallsSceneName);
        td_smallsSceneName.appendChild(td1_txt);
        tr.appendChild(td_smallsSceneName);

        var td_labelName = document.createElement('td');
        td1_txt = document.createTextNode(labelName);
        td_labelName.appendChild(td1_txt);
        tr.appendChild(td_labelName);

        var td_grpThreadsName = document.createElement('td');
        td1_txt = document.createTextNode(grpThreads);
        td_grpThreadsName.appendChild(td1_txt);
        tr.appendChild(td_grpThreadsName);


        var td_avg = document.createElement('td');
        td1_txt = document.createTextNode(avg);
        td_avg.appendChild(td1_txt);
        tr.appendChild(td_avg);

        var td_min = document.createElement('td');
        td1_txt = document.createTextNode(min);
        td_min.appendChild(td1_txt);
        tr.appendChild(td_min);

        var td_max = document.createElement('td');
        td1_txt = document.createTextNode(max);
        td_max.appendChild(td1_txt);
        tr.appendChild(td_max);

        var td_elapsed50 = document.createElement('td');
        td1_txt = document.createTextNode(elapsed50);
        td_elapsed50.appendChild(td1_txt);
        tr.appendChild(td_elapsed50);

        var td_elapsed90 = document.createElement('td');
        td1_txt = document.createTextNode(elapsed90);
        td_elapsed90.appendChild(td1_txt);
        tr.appendChild(td_elapsed90);

        var td_elapsed95 = document.createElement('td');
        td1_txt = document.createTextNode(elapsed95);
        td_elapsed95.appendChild(td1_txt);
        tr.appendChild(td_elapsed95);

        var td_elapsed99 = document.createElement('td');
        td1_txt = document.createTextNode(elapsed99);
        td_elapsed99.appendChild(td1_txt);
        tr.appendChild(td_elapsed99);

        var td_fail = document.createElement('td');
        td1_txt = document.createTextNode(error);
        td_fail.appendChild(td1_txt);
        tr.appendChild(td_fail);

        var td_tpsAvg = document.createElement('td');
        td1_txt = document.createTextNode(tpsAvg);
        td_tpsAvg.appendChild(td1_txt);
        tr.appendChild(td_tpsAvg);


        document.getElementById('filter_table_tbody').appendChild(tr);
    }

}

function setResultTitle(sceneName) {
    $('#etp_report_result').removeClass('hidden');
    var tdtitle = 'resultVersion,begin,end,smallSceneName,labelName,grpThreads,avg,min,max,50%,90%,95%,99%,Error,TPS(Avg)';
    var titleList = tdtitle.split(',');
    var trhead = document.createElement('tr');
    trhead.setAttribute("id", "report_title_head");
    var th = document.createElement('th');
    th.setAttribute("colspan", titleList.length);
    th.setAttribute("style", "text-align: center;");
    var td_txt = document.createTextNode("sceneName : " + sceneName);
    th.appendChild(td_txt);

    trhead.appendChild(th);
    document.getElementById('filter_table_tbody').appendChild(trhead);


    var tr = document.createElement('tr');
    tr.setAttribute("id", "report_title");
    for (var i = 0; i < titleList.length; i++) {
        var td = document.createElement('td');
        var td1_txt = document.createTextNode(titleList[i]);
        td.appendChild(td1_txt);

        tr.appendChild(td);
    }
    document.getElementById('filter_table_tbody').appendChild(tr);

}

