<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>GTP Report</title>
    <link rel="stylesheet" href="/css/gtp_report.css"/>
    <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css" />
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
    <jsp:include page="frame.jsp" flush="true"/>


</head>
<body>
<input type="hidden" id="hiddenValue" />
<div class="container-fluid"><h1></h1>
    <div class="row">
        <div class="main-div">
            <div id="etp_report" class="special-div">

                <div id="etp_report_filter" class="half-color-header filter_div" style="padding: 20px">
                    <h2>LOAD报告</h2>

                    <table class="filter_table" id="etp_report_filter_table" style="width:1240px">
                        <tbody>
                        <tr>
                            <td align="left" >
                                <div class="input-group">
                                <span class="input-group-addon">所属组</span>
                                <select class="column_filter form-control form-inline" id="report_type_group">
                                    <option value="None" selected="selected">All</option>
                                    <c:forEach var="group" items="${groups}">
                                        <option value="${group[1]}"> ${group[0]} </option>
                                    </c:forEach>
                                    <option value="Null" >NULL</option>
                                </select>
                                </div>
                            </td>

                            <td align="left" colspan="2" >
                                <div class="input-group" style="white-space: nowrap;" colspan="2">
                                    <span class="input-group-addon">任务名称  &nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    <input id="task_search" type="text" data-provide="typeahead" style="width:500px;" />
                                    <a class="dropdown-toggle" data-toggle="dropdown" style="margin-left: 5px;" href="javascript:void(0);"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span> </a>
                                    <ul class="dropdown-menu" id="menu-taskName" style="left:560px;top:-1px">
                                        <li class="hide" id="menu-task-template">
                                            <a href="javascript:void(0);"
                                               data-content="template menu description"
                                               rel="popover" data-original-title="Description"
                                               data-placement="left" data-container="body">menu</a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                            <td></td>

                        </tr>



                        <tr>
                            <td align="left" >
                                <div class="input-group" style="white-space: nowrap;" >
                                    <span class="input-group-addon">大场景  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    <input id="product_search" type="text" data-provide="typeahead" style="width:400px;" />
                                    <a class="dropdown-toggle" data-toggle="dropdown" style="margin-left: 5px;" href="javascript:void(0);"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span> </a>
                                    <ul class="dropdown-menu" id="menu-sceneName" style="left:310px;top:-1px">
                                        <li class="hide" id="menu-template">
                                            <a href="javascript:void(0);"
                                               data-content="template menu description"
                                               rel="popover" data-original-title="Description"
                                               data-placement="left" data-container="body">menu</a>
                                        </li>
                                    </ul>
                                </div>
                            </td>

                            <td align="left" colspan="2" >
                                <div class="input-group">
                                    <span class="input-group-addon">耗时</span>
                                    <input id="task_time" type="text" data-provide="typeahead" style="width:200px;" />
                                </div>
                            </td>


                        </tr>



                        <tr>

                            <td align="left" id="filter_reportType" style="width:400px">
                                <div class="input-group">
                                    <span class="input-group-addon">小场景</span>
                                    <select class="report_filter form-control form-inline" id="task_type_smallSceneName">

                                    </select>
                                </div>
                            </td>
                            <td align="left" id="filter_col4" data-column="4"style="width:400px">
                                <div class="input-group">
                                    <span class="input-group-addon">接 口</span>
                                    <select class="report_filter form-control form-inline" id="task_type_labelName">

                                    </select>
                                </div>
                            </td>
                            <td align="left" id="filter_dimension" data-column="4" style="width:250px">
                                <div class="input-group">
                                    <span class="input-group-addon " style="text-align: left;">环 境</span>
                                    <select class="report_filter form-control form-inline" id="dimension_environment">

                                    </select>


                                    <span style=""></span>
                                </div>
                            </td>
                            <td  align="left" id="report_filter_begin_td">

                            </td>

                        </tr>
                        <tr>
                            <td align="left"  style="width:400px" >

                                <div id="report_begin_div" class="input-group ">
                                    <span id="report_startDate" class="input-group-addon">日期版本</span>
                                    <input id="report_timespan_begin_input"  class="form-inline form-control"/>
                                    <span id="report_startDate_addon" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>


                            </td>
                            <td  align="left" >
                                <div class="input-group">
                                    <span class="input-group-addon " style="text-align: left;">时间版本</span>
                                <select class="report_filter form-control form-inline" id="result_time">
                                    <option value="" selected="selected" ></option>

                                </select>
                                    <span style=""></span>
                                </div>
                            </td>
                            <td align="left"  data-column="4"style="width:400px" >
                                <div class="input-group">
                                    <span class="input-group-addon " style="text-align: left;">模 板</span>
                                    <select class="report_filter form-control form-inline" id="dimension_template">
                                        <option value="" selected="selected" ></option>
                                        <c:forEach var="scene" items="${loadScene}">
                                                <option value="${scene.name}">${scene.name}</option>
                                        </c:forEach>
                                    </select>


                                    <span style=""></span>
                                </div>
                            </td>


                            <td align="left"   data-column="4" style="width:200px">
                                <div class="input-group" style="white-space: nowrap;">
                                    <a id="report_generate_btn" class="btn btn-primary">查 询</a>
                                    <a id="report_edit_btn" class="btn btn-primary" style="margin-left: 20px;">修 改</a>
                                </div>
                                <div id="report_end_div" class="input-group hidden">
                                    <span id="report_endDate" class="input-group-addon">End</span>
                                    <input id="report_timespan_end_input" class="form-inline form-control"/>
                                    <span id="report_endDate_addon" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>

                            </td>


                        </tr>
                        </tbody>
                    </table>
                    <span id="test_span_domain"></span>
                </div>
                <div class="half-color-header filter_div" style="padding: 20px">
                    <h3 id="report_title"></h3>
                <table id="report_list" class="table table-striped table-bordered table-hover half-trans-green hidden">

                    <thead>
                    <tr>
                        <th>版本</th>
                        <th>接口</th>
                        <th>开始</th>
                        <th>结束</th>
                        <th>小场景</th>
                        <th>线程</th>
                        <th>ART</th>
                        <th>MinElapsed</th>
                        <th>MaxElapsed</th>
                        <th>50%</th>
                        <th>90%</th>
                        <th>95%</th>
                        <th>99%</th>
                        <th>Error</th>
                        <th>TPS(Avg)</th>

                    </tr>
                    </thead>
                </table>
                    <pre id="remark" style=" width:100%; height: 200px;" class="hidden">

                    </pre>
                </div>

                <button id="btn_loading" style="margin-left: 20px;" class=" hidden "  data-loading-text="Loading..." data-style="expand-right">

                </button>

                <div id="etp_report_result" class="half-color-header filter_div" style="padding: 20px">
                    <table class="filter_table" id="etp_report_table" style="width:1240px; border: 1px; ">
                        <tbody id="filter_table_tbody">


                        </tbody>
                    </table>
                    <span id="test_span_domain2"></span>
                </div>


                    <table class="report_container_label" class="hidden" id="etp_report_table2" style="width:1240px; border: 1px; margin-left: 40px; ">
                        <tbody id="filter_table_tbody2" class="hidden">


                        </tbody>
                    </table>

                <div id="report_tps" style="width: 100%; height:600px;" class="hidden" ></div>
            </div>

        </div>
    </div>
    <div id="report_container" style="width: 100%; height:600px;" class="hidden">

    </div>
    <div id="report_container234" style="width: 100%; height:600px;" class="hidden">

    </div>
    <table class="hidden" >
    <tr id="labelTR" >

    </tr>
    </table>
    <div class="modal fade" id="report_error_modal" tabindex="-1" role="dialog"
         aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="report_error_h4">

                    </h4>
                </div>
                <div id="report-error-body" class="modal-body" style="text-align: center">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal">关闭
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</body>


<script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
<script src="/assets/jquery-highcharts/highcharts.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="/assets/jquery-autocomplete/typeahead.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/jmt_report_new.js"></script>


</html>
