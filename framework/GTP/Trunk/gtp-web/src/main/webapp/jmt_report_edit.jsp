<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>GTP Report</title>
    <link rel="stylesheet" href="/css/gtp_report.css"/>
    <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css" />
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>


</head>
<body>
<input type="hidden" id="hiddenValue" sceneName="${sceneName}" smallsceneName="${smallsceneName}" resultverison="${resultverison}" env="${env}" template="${template}" />
<div class="container-fluid"><h1></h1>
    <div class="row">
        <div class="main-div">
            <div id="etp_report" class="special-div">

                <div id="etp_report_filter" class="half-color-header filter_div" style="padding: 20px">
                    <h2>JMT Report</h2>

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


                        </tr>
                        <tr>
                            <td align="left" colspan="3">

                            <%--</td>--%>
                            <%--<td align="left" colspan="2" >--%>

                                <div class="input-group">
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
                                    <span class="input-group-addon " style="text-align: left;">环 境</span>
                                    <select class="report_filter form-control form-inline" id="dimension_environment">
                                    </select>
                                </div>
                            </td>
                            <td align="left" id="filter_dimension" data-column="4" style="width:250px">
                                <div class="input-group">
                                    <span class="input-group-addon " style="text-align: left;">模 板</span>
                                    <select class="report_filter form-control form-inline" id="dimension_template">
                                        <option value=""></option>
                                        <c:forEach var="scene" items="${loadScene}">
                                            <option value="${scene.name}">${scene.name}</option>
                                        </c:forEach>
                                    </select>


                                    <span style=""></span>
                                </div>
                            </td>
                            <td  align="left" id="report_filter_begin_td">

                                <div class="input-group">
                                </div>
                            </td>

                        </tr>
                        <tr>
                            <td align="left"  style="width:400px" >
                                <div id="report_begin_div" class="input-group ">
                                    <span id="report_startDate" class="input-group-addon">版 本</span>
                                    <input id="report_timespan_begin_input" class="form-inline form-control"/>
                                    <span id="report_startDate_addon" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td align="left"  data-column="4" >
                                <div class="input-group">
                                    <span class="input-group-addon " style="text-align: left;">时间版本</span>
                                    <select class="report_filter form-control form-inline" id="result_time">
                                        <option value="" selected="selected" ></option>

                                    </select>
                                    <span style=""></span>
                                </div>

                            </td>
                            <td align="left" style="width:400px">

                                <div class="input-group">
                                    <a id="report_generate_btn" class="btn btn-primary">查 询</a>
                                    <a id="report_history_btn" class="btn btn-primary" style=" margin-left: 20px;">历史</a>

                                </div>
                            </td>

                            <td align="left"   data-column="4" style="width:200px">

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
                <table id="report_list" class="table table-striped table-bordered table-hover half-trans-green ">

                    <thead>
                    <tr>

                        <th>版本</th>
                        <th>场景</th>
                        <th>小场景</th>
                        <th>创建时间</th>
                        <th>环境</th>
                        <th>版本</th>
                        <th>修改</th>
                    </tr>
                    </thead>
                </table>



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

    <div class="modal fade" id="report_edit_modal" tabindex="-1" role="dialog"
         aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="">
                        修改场景
                    </h4>
                </div>
                <div id="report-edit-body" class="modal-body" style="text-align: center">
                    <table class="table table-bordered half-trans-green">
                        <tbody>
                        <tr>
                            <th>创建时间</th>
                            <td>
                                <div class="col-md-8">
                                    <input class="form-control" type="text" id="jmt_createTime" readonly="true"  value="" style="width:400px;" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>场 景</th>
                            <td>
                                <div class="col-md-8">
                                    <input class="form-control" type="text" id="jmt_sceneName"  value="" style="width:400px;" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>小场景</th>
                            <td>
                                <div class="col-md-8">
                                    <input class="form-control" type="text" id="jmt_smallsceneName" readonly="true" value="" style="width:400px;" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>版 本</th>
                            <td>
                                <div class="col-md-8">
                                    <input class="form-control" type="text" id="jmt_resultversion" readonly="true" style="width:400px;" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>环 境</th>
                            <td>
                                <div class="col-md-8">
                                    <input class="form-control" type="text" id="jmt_env" readonly="true" style="width:400px;" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>模 板</th>
                            <td>
                                <div class="col-md-8">
                                    <input class="form-control" type="text" id="jmt_template" readonly="true" style="width:400px;" />
                                </div>
                            </td>
                        </tr>


                        </tbody>
                        <tfoot>
                        <tr>
                            <th colspan="2">
                                <input type="button" class="btn btn-primary" value="保存" id="jmt_edit_save_btn" onclick="jmtedit()"/>
                                <span id="jmt_error_span" style="color:red;"></span>
                            </th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

    <div class="modal fade" id="report_editRemark_modal" tabindex="-1" role="dialog"
         aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog"  style="width: 800px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" >
                        修改备注
                    </h4>
                </div>
                <div id="report-editRemark-body" class="modal-body" style="text-align: center">
                    <table class="table table-bordered half-trans-green" style="text-align: left;">
                        <tbody>
                        <tr>
                            <th>创建时间</th>
                            <td>
                                <div class="col-md-8">
                                    <span id="sp_jmt_createTime"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>场 景</th>
                            <td>
                                <div class="col-md-8">
                                    <span id="sp_jmt_sceneName"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>小场景</th>
                            <td>
                                <div class="col-md-8">
                                    <span id="sp_jmt_smallsceneName"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>版 本</th>
                            <td>
                                <div class="col-md-8">
                                    <span id="sp_jmt_resultversion"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>环 境</th>
                            <td>
                                <div class="col-md-8">
                                    <span id="sp_jmt_env"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>模 板</th>
                            <td>
                                <div class="col-md-8">
                                    <span id="sp_jmt_template"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>备 注</th>
                            <td>
                                <div class="col-md-8">
                                   <textarea id="jmt_remark"  class="form-control" placeholder="" style="width: 600px; height: 400px;" ></textarea>
                                </div>
                            </td>
                        </tr>

                        </tbody>
                        <tfoot>
                        <tr>
                            <th colspan="2">
                                <input type="button" class="btn btn-primary" value="保存" id="jmt_editRemark_save_btn" onclick="jmteditremark()"/>
                                <span id="jmt_errorRemark_span" style="color:red;"></span>
                            </th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>


    <div class="modal fade" id="report_historyLog_modal" tabindex="-1" role="dialog"
         aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="width: 1200px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" align="center" >
                        历 史
                    </h4>
                </div>
                <div id="report-historylog-body" class="modal-body" style="text-align: center">
                    <table id="report_log_list" class="table table-striped table-bordered table-hover half-trans-green ">
                        <thead>
                        <tr>
                            <th style="width: 10%; ">修改人(修改人_IP)</th>
                            <th style="width: 30%;">场景 <br /> (修改前场景)</th>
                            <th style="width: 10%;">小场景</th>
                            <th style="width: 10%;">环境</th>
                            <th style="width: 10%;">模板</th>
                            <th style="width: 10%;">版本</th>
                            <th style="width: 10%;">创建时间</th>
                            <th style="width: 10%;">修改时间</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</body>

<script src="/assets/jquery-highcharts/highcharts.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="/assets/jquery-autocomplete/typeahead.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/jmt_report_edit.js"></script>
<script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>

</html>
