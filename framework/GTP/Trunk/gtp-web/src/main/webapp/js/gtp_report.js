/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    dateShow();
    //showChart();
    $('#report_generate_btn').bind('click', function() {
        showChart();
    });
    $('#report_begin_div').bind('click', function () {
        $('#report_timespan_begin_input').datetimepicker('show');
    });
    $('#report_end_div').bind('click', function () {
        $('#report_timespan_end_input').datetimepicker('show');
    });
    
//控制 Dimension Radio
    $('#dimension_group').bind('click',function () {
        $('#filter_owner').addClass('hidden');
        $('#filter_group').removeClass('hidden');
        $('#dimension_personal').removeClass('data-checked');
        $('#dimension_group').addClass('data-checked');
    });
    $('#dimension_personal').bind('click',function () {
        $('#filter_group').addClass('hidden');
        $('#filter_owner').removeClass('hidden');
        $('#dimension_group').removeClass('data-checked');
        $('#dimension_personal').addClass('data-checked');
    });

    $('#group_filter').on('change', function() {
        getOwnerByGroupId();
    });

});

function dateShow () {
    $('#report_timespan_begin_input').val(currentDateMinusDay(15,new Date().getTime()));
    $('#report_timespan_end_input').val(currentDateMinusDay(0,new Date().getTime()));
    $('#report_timespan_begin_input').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $('#report_timespan_end_input').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
}

function currentDateMinusDay(n, baseDate){
    var s, d, t, t2;
    t = baseDate;
    t2 = n * 1000 * 3600 * 24;
    t-= t2;
    d = new Date(t);
    s = d.getUTCFullYear() + "-";
    s += ("00"+(d.getUTCMonth()+1)).slice(-2) + "-";
    s += ("00"+d.getUTCDate()).slice(-2);
    return s;
} 

function currentDateAddDay(n, baseDate){
    var s, d, t, t2;
    t = baseDate;
    t2 = n * 1000 * 3600 * 24;
    t+= t2;
    d = new Date(t);
    s = d.getUTCFullYear();
    s += ("00"+(d.getUTCMonth()+1)).slice(-2);
    s += ("00"+d.getUTCDate()).slice(-2);
    return s;
}

function genDateArray (startDate, endDate) {
    var start = new Date(startDate).getTime();
    var end = new Date(endDate).getTime();
    var ms = end - start;//未判断start 和 end 的合法性，调用此函数前需要先验证
    var dateArray = new Array();
    var days = ms/(1000*3600*24);
    var interval = 1;
    //if (2 <=  parseInt(days/15)) {
    //    interval = parseInt(days/15);
    //}
    for (var i = 0; i <= parseInt((days)/interval); i++) {
        dateArray[i] = parseInt(currentDateAddDay(i*interval,start));
    }
    return dateArray;
}

//选择Option中的value组装为数组：选All时组装所有非All值，否则只组装所选值
function genOptionArray (id) {
    var optionArray = new Array();
    if ('-1' === $("#"+id).val()) {
        $("#" + id + " option").each(function (i, opt) {
            if ($(this).val() !== '-1') {
                optionArray.push($(this).val());
            }
        });
    } else {
        optionArray.push($("#"+id).val());
    }
    return optionArray;
}

//生成报表副标题
function genSubtitle(reportType,taskTypeValue,groupValue,ownerValue,personal) {
    var taskType = "All";
    var group = "All";
    var owner = "All owners";
    if ("1" !== taskTypeValue) {
        taskType = $("#task_type_filter option:selected").text();
    }
    if ("1" !== groupValue) {
        group = $("#group_filter option:selected").text();
    }
    if ("1" !== ownerValue) {
        owner = $("#owner_filter option:selected").text();
    }
    if (personal) {
        return "About " + reportType + " for " + taskType + " tasks and " + owner + " of " + group +" group(s)";
    } else {
        return "About " + reportType + " for " + taskType + " tasks and " + group +" group(s)";
    }
}

//生成showChart()最后一步的series json 数组
function genSeriesArray (personal, reportType,seriesArray) {
    var array = new Array();
    var selectId = "group_filter";
    if (personal) {//按个人统计
        selectId = "owner_filter";
    }
    if ($("#" + selectId).val() === '-1') {//取所有非All的text，装入json
        $("#group_filter option").each(function (i, opt) { //遍历全部option
            var val = $(this).text(); //获取option的内容
            var jsonObj = {
                "name": reportType + "(" + val + ")",//一条线的标识
                "data": seriesArray[i - 1]//一条线上的数据数组
            };
            if (val !== 'All') {
                array.push(jsonObj);
            }
        });
    } else {//取所选text装入json
        var val = $("#" + selectId + " option:selected").text();
        var jsonObj = {
            "name": reportType + "(" + val + ")",//一条线的标识
            "data": seriesArray[0]//一条线上的数据数组
        };
        array.push(jsonObj);
    }
    return array;
}

function showChart() {
    var reportType = $('#report_type_filter').find('option:selected').text();
    var reportTypeValue = $('#report_type_filter').val();

    var taskTypeValue = $('#task_type_filter').val();
    var taskType = genOptionArray("task_type_filter");

    var personal = false;
    if($('#owner_filter').val() != "-1") {
        personal = true;
    }

    var businessGroupValue = $('#group_filter').val();
    var businessGroup=genOptionArray("group_filter");

    var owner = genOptionArray("owner_filter");
    var ownerValue = $('#owner_filter').val();
    var startDate = new Date($('#report_timespan_begin_input').val()).getTime();
    var endDate = new Date($('#report_timespan_end_input').val()).getTime();

    if (startDate > endDate) {
        $('#report_error_h4').empty();
        $('#report_error_h4').text("日期范围非法");
        $('#report-error-body').empty();
        $('#report-error-body').text("开始日期不能晚于截止日期!");
        $('#report_error_modal').modal('show');
    } else {
        var dateArray = genDateArray(startDate, endDate);
        $.ajax({
            url: "/gtp-report/report",
            dataType: "json",
            type: "POST",
            data: {
                "dates": dateArray,
                "group": businessGroup,
                "taskType": taskType,
                "owner": owner,
                "personal": personal,
                "reportType": reportTypeValue

            },
            success: function (data) {
                seriesArray = data;
                if(reportType.trim() == "CaseNumber") {
                    $('#report_container').highcharts({
                        title: {
                            text: "GTP Report"
                        },
                        subtitle: {
                            //text: "about: " + reportType + "/ for: " + taskType + " TaskType(s) and " + businessGroup + " Group(s)"
                            text: genSubtitle(reportType, taskTypeValue, businessGroupValue, ownerValue, personal)
                        },
                        xAxis: {
                            categories: dateArray,
                            labels: {
                                rotation: 60
                            }
                        },
                        yAxis: {
                            title: {
                                text: reportType
                            },
                            min: 0
                        },
                        lengend: {
                            enable: false
                        },
                        series: genSeriesArray(personal, reportType, seriesArray)
                    });
                } else if (reportType.trim() == "PassRate") {
                    $('#report_container').highcharts({
                        title: {
                            text: "GTP Report"
                        },
                        subtitle: {
                            //text: "about: " + reportType + "/ for: " + taskType + " TaskType(s) and " + businessGroup + " Group(s)"
                            text: genSubtitle(reportType, taskTypeValue, businessGroupValue, ownerValue, personal)
                        },
                        xAxis: {
                            categories: dateArray,
                            labels: {
                                rotation: 60
                            }
                        },
                        yAxis: {
                            title: {
                                text: reportType
                            },
                            min: 0,
                            max: 100
                        },
                        lengend: {
                            enable: false
                        },
                        series: genSeriesArray(personal, reportType, seriesArray)
                    });
                }
            }
            //}
        });
    }
}


function getOwnerByGroupId() {
    var groupId = $('#group_filter').val();
    $('#loading_modal').modal('show');
    $.ajax({
        'url':'/gtp-report/owner/' + groupId,
        'type':'GET',
        'async':false,
        success:function(data) {
            $('#owner_filter').find('option[value!="-1"]').remove();
            for (var i = 0; i < data.length; i++) {
                $('#owner_all_option').after('<option value="'+ data[i] + '">'+  data[i] +'</option>')
            }
            $('#loading_modal').modal('hide');
        }
    });
}