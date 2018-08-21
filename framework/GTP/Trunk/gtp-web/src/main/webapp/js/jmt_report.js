/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    dateShow();

    $("#report_type_sceneName").bind('change',function(){ reportTypeSelect();labelNameSelect();getEnvironment();});

    $("#task_type_smallSceneName").bind('change',function(){ getVersion() ;})
    $("#task_type_labelName").bind('change',function(){ getVersion() ;})
    $("#dimension_environment").bind('change',function(){ getVersion() ;})
    //showChart();
    $('#report_generate_btn').bind('click', function() {
        showChart();
    });
    $('#report_begin_div').bind('click', function () {
        $('#report_timespan_begin_input').datetimepicker('show');
    });

    $('#etp_report_result').addClass('hidden');

});

//绑定大场景名称select事件
function reportTypeSelect()
{

    $('#task_type_smallSceneName').empty();
    $("#task_type_labelName").empty();
    var sceneName = $('#report_type_sceneName').find('option:selected').text();
    if(sceneName !="")
    {
        getSmallSceneName(sceneName,true);
    }

}

function getEnvironment()
{
    var sceneName = $('#report_type_sceneName').find('option:selected').text();
    $("#dimension_environment").empty();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtEnvironment',
        async: false,
        data: {
            'sceneName' : sceneName,
            'isEnable': true
        },
        success: function(data) {
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var options = "";
                for (var i = 0; i < data.length; ++i) {
                    var option = data[i];
                    options += '<option value="' + option + '" >' + option+ '</option>';
                }
                $('#dimension_environment').append(options);
            }
        }
    });
}

function getVersion()
{
    var sceneName = $('#report_type_sceneName').find('option:selected').text();
    var smallsceneName=$('#task_type_smallSceneName').find('option:selected').text();
    var labelName=$('#task_type_labelName').find('option:selected').text();
    var environment=$('#dimension_environment').find('option:selected').text();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtgetVersion',
        async: false,
        data: {
            'sceneName' : sceneName,
            'smallSceneName':smallsceneName,
            'labelName':labelName,
            'environment':environment,
            'isEnable': true
        },
        success: function(data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                $('#report_timespan_begin_input').val(data);
            }
        }
    });
}

//得到小场景名称并绑定
function getSmallSceneName(sceneName,isEnable)
{
    $('#task_type_smallSceneName').empty();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtSmallsceneName',
        async: false,
        data: {
            'sceneName' : sceneName,
            'isEnable': isEnable
        },
        success: function(data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var options = "";
                for (var i = 0; i < data.length; ++i) {
                    var option = data[i];
                    options += '<option value="' + option + '" >' + option+ '</option>';
                }

                $('#task_type_smallSceneName').append(options);
            }
        }
    });
}

//绑定小场景名称select事件
function labelNameSelect()
{
    var sceneName = $('#report_type_sceneName').find('option:selected').text();
    if(sceneName !="")
    {

        getLabelName(sceneName,true);
    }
}

function getLabelName(sceneName,isEnable)
{
    setetp_report();
    $('#task_type_labelName').empty();
    $.ajax({
        type: 'POST',
        url: '/gtp-report/jmtLabelName',
        async: false,
        data: {
            'sceneName':sceneName,
            'isEnable': isEnable
        },
        success: function(data) {
            $('#task_type_labelName').empty();
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var options = "";
                for (var i = 0; i < data.length; ++i) {
                    var option = data[i];
                    options += '<option value="' + option + '" >' + option+ '</option>';

                    var report_container = $('#report_container').clone(true);
                    report_container.removeAttr('id');
                    report_container.attr("id", "jmt_report_container_" + option.replace(/\./g,''));
                    $('#etp_report').append(report_container);
                }

                $('#task_type_labelName').append(options);
            }
        }
    });
}



function setetp_report()
{
    $("#etp_report").children().each(function(i,n){
        var obj = $(n);
        var id= obj!=null ? $(obj).attr('id'):'';
        if(id != null && id.indexOf('jmt_report_container_')>-1)
        {
            $('#'+id).remove();
        }
    });

}


function dateShow () {
    $('#report_timespan_begin_input').val('');
    $('#report_timespan_begin_input').datetimepicker({
        timepicker: false,
        format: 'Ymd'
    });
    $('#report_timespan_end_input').datetimepicker({
        timepicker: false,
        format: 'Ymd'
    });
}

function currentDateMinusDay(n, baseDate){
    var s, d, t, t2;
    t = baseDate;
    t2 = n * 1000 * 3600 * 24;
    t-= t2;
    d = new Date(t);
    s = d.getUTCFullYear()+"-" ;
    s += ("00"+(d.getUTCMonth()+1)).slice(-2)+"-" ;
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
function genSubtitle(SceneName,smallSceneName) {
    return "About " + SceneName + " for "  + smallSceneName  + " Report";

}

//生成showChart()最后一步的series json 数组
function genSeriesArray (seriesArray) {
    var array = new Array();
    for(var i=0;i<2;i++)
    {
        var val = $(this).text(); //获取option的内容
        var jsonObj = {
            "name": (i==0?'AVG'+getdatatiele(seriesArray[i + 1]) :'TPS'+getdatatiele(seriesArray[i + 1])), // "(" + val + ")",//一条线的标识
            "data":getdata(seriesArray[i + 1]) //一条线上的数据数组
        };
        if (val !== 'All') {
            array.push(jsonObj);
        }
    }
    return array;
}
function getdatatiele(seriesArray)
{
   var list= seriesArray[0].split('_');
    if(list.length>1)
    return "_"+list[1];
    else
    return "";
}

function getdata(seriesArray)
{
    var result=new Array();
    for(var i=0;i<seriesArray.length;i++)
    {
        result[i]=Number(seriesArray[i].split('_')[0]);
    }
    return result;
}

function showError(title,value)
{
    $('#report_error_h4').empty();
    $('#report_error_h4').text(title);
    $('#report-error-body').empty();
    $('#report-error-body').text(value);
    $('#report_error_modal').modal('show');
    return false;
}

function showChart() {
    var sceneName = $('#report_type_sceneName').find('option:selected').text();
    var smallSceneName = $('#task_type_smallSceneName').find('option:selected').text();
    var labelName = $('#task_type_labelName').find('option:selected').text();
    var count=$("#dimension_count").val();
    var resultVersion=$("#report_timespan_begin_input").val();
    var environment=$('#dimension_environment').find('option:selected').text();

    if(sceneName=="")
    {
       return showError('sceneName Error','The SceneName is Not Null!');
    }


    if(count=="")
    {
        return showError('count Error','The count is Not Null!');
    }
    if(resultVersion=="")
    {
        return showError('Date Error','The Date is Not Null!');
    }
    if(environment=="")
    {
        return showError('environment Error','The environment is Not Null!');
    }

    $.ajax({
        url: "/gtp-report/getjmtreport",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "environment": environment,
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "labelName": labelName,
            "count": count

        },//report_container_
        success: function (data) {

            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                $("#filter_table_tbody").html("");
                setResultBody(data);
            }

        }

    });

        $("#task_type_labelName option").each(function (i, opt) { //遍历全部option
            var labelNameVal = $(this).text(); //获取option的内容

            $('#jmt_report_container_'+labelNameVal.replace(/\./g,'')).addClass('hidden');
            if(smallSceneName==''&&(labelName==""||labelName==labelNameVal))
            bindReport(sceneName,labelNameVal,resultVersion,environment);

        });

        $('#report_history').addClass('hidden');
        if(smallSceneName!=""&& count!=""+resultVersion!=""&& sceneName!=""&&labelName!=""&&environment!="")
        {
            bindhistoryReport(sceneName,smallSceneName,labelName,resultVersion,count,environment);
        }

}

function bindhistoryReport(sceneName,smallSceneName,labelNameVal,resultVersion,count,environment)
{
    if(labelNameVal=='') return false;
    $.ajax({
        url: "/gtp-report/gethistoryjmtreport",
        dataType: "json",
        type: "POST",
        data: {
            "sceneName": sceneName,
            "smallSceneName": smallSceneName,
            "labelName": labelNameVal,
            "resultVersion": resultVersion,
            "environment": environment,
            "count":Number(count)


        },//report_container_
        success: function (data) {
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
                var  seriesArray = data;
                if(seriesArray.length==0|| seriesArray[0].length==0)
                {
                    return false;
                }
                $('#report_history').removeClass('hidden');
                $('#report_history').highcharts({
                    title: {
                        text: "JMT Report history "
                    },
                    subtitle: {
                        text: genSubtitle(sceneName,smallSceneName,labelNameVal)
                    },
                    xAxis: {
                        categories: seriesArray[0],
                        labels: {
                            rotation: 60
                        }
                    },
                    yAxis: {
                        title: {
                            text:sceneName+"_" +smallSceneName+"_"+ labelNameVal
                        }
                    },
                    lengend: {
                        enable: false
                    },
                    series: genSeriesArray(seriesArray)
                });

            }

        }

    });

}

function bindReport(sceneName,labelNameVal,resultVersion,environment)
{
   if(labelNameVal=='') return false;
var labelId=labelNameVal.replace(/\./g,'');
    $.ajax({
        url: "/gtp-report/getlinejmtreport",
        dataType: "json",
        type: "POST",
        data: {
            "resultVersion": resultVersion,
            "environment": environment,
            "sceneName": sceneName,
            "labelName": labelNameVal

        },//report_container_
        success: function (data) {
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
            } else {
                data = data['data'];
               var  seriesArray = data;
                if(seriesArray.length==0|| seriesArray[0].length==0)
                {
                    return false;
                }
                $("#jmt_report_container_" + labelId).removeClass('hidden');
                $('#jmt_report_container_'+labelId).highcharts({
                    title: {
                        text: "JMT Report ("+labelNameVal+")"
                    },
                    subtitle: {
                        text: genSubtitle(sceneName,labelNameVal)
                    },
                    xAxis: {
                        categories: seriesArray[0],
                        labels: {
                            rotation: 60
                        }
                    },
                    yAxis: {
                        title: {
                            text: labelNameVal
                        }
                    },
                    lengend: {
                        enable: false
                    },
                    series: genSeriesArray(seriesArray)
                });

            }

        }

    });
}

function setResultBody(data)
{
    if(data.length>0)
        setResultTitle();
    for (var t = 0; t < data.length; t++) {
        var item = data[t];
        var sceneName=item["sceneName"];
        var resultVersion=item["resultVersion"];
        var smallsSceneName=item["smallSceneName"];
        var labelName=item["labelName"];
        var avg=item["avg"];
        var min=item["minElapsed"];
        var max=item["maxElapsed"];
        var elapsed50=item["elapsed50"];
        var elapsed90=item["elapsed90"];
        var elapsed95=item["elapsed95"];
        var elapsed99=item["elapsed99"];
        var fail=item["error"];
        var Throughput=item["throughput"];

            var tr = document.createElement('tr');
            var td = document.createElement('td');
            var td1_txt = document.createTextNode(sceneName);
            td.appendChild(td1_txt);
            tr.appendChild(td);
            var td_resultVersion = document.createElement('td');
            td1_txt = document.createTextNode(resultVersion);
            td_resultVersion.appendChild(td1_txt);
            tr.appendChild(td_resultVersion);

            var td_smallsSceneName = document.createElement('td');
            td1_txt = document.createTextNode(smallsSceneName);
            td_smallsSceneName.appendChild(td1_txt);
            tr.appendChild(td_smallsSceneName);

            var td_labelName = document.createElement('td');
            td1_txt = document.createTextNode(labelName);
            td_labelName.appendChild(td1_txt);
            tr.appendChild(td_labelName);

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
            td1_txt = document.createTextNode(fail);
            td_fail.appendChild(td1_txt);
            tr.appendChild(td_fail);

            var td_Throughput = document.createElement('td');
            td1_txt = document.createTextNode(Throughput);
            td_Throughput.appendChild(td1_txt);
            tr.appendChild(td_Throughput);


            document.getElementById('filter_table_tbody').appendChild(tr);
        }

}

function setResultTitle()
{


    $('#etp_report_result').removeClass('hidden');
    var tdtitle='sceneName,resultVersion,smallSceneName,labelName,avg,min,max,50%,90%,95%,99%,Error,Throughput';
    var titleList=tdtitle.split(',');
    var tr=document.createElement('tr');
    tr.setAttribute("id","report_title");
    for(var i=0;i< titleList.length;i++)
    {

        var td=document.createElement('td');
        var td1_txt = document.createTextNode(titleList[i]);
        td.appendChild(td1_txt);

        tr.appendChild(td);
    }
document.getElementById('filter_table_tbody').appendChild(tr);

}