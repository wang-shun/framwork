<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>GTP Report</title>
    <link rel="stylesheet" href="/css/gtp_report.css"/>
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>

</head>
<body>
<input type="hidden" id="hiddenValue" />
<div class="container-fluid"><h1></h1>
    <div class="row">
        <div class="main-div">
            <div id="etp_report" class="special-div">

                <div id="etp_report_filter" class="half-color-header filter_div" style="padding: 20px">
                    <h2>JMT Report</h2>

                    <table class="filter_table" id="etp_report_filter_table" style="width:1240px">
                        <tbody>
                        <tr>

                            <td align="left" colspan="4" >
                                <div class="input-group">
                                    <span class="input-group-addon">SceneName  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    <select class="report_filter form-control form-inline" id="report_type_sceneName" style="width: 910px;">
                                        <c:forEach var="rt" items="${reportTypes}">
                                            <option value="${rt}">${rt}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <tr>

                            <td align="left" id="filter_reportType" style="width:400px">
                                <div class="input-group">
                                    <span class="input-group-addon">SmallSceneName</span>
                                    <select class="report_filter form-control form-inline" id="task_type_smallSceneName">

                                    </select>
                                </div>
                            </td>
                            <td align="left" id="filter_col4" data-column="4"style="width:400px">
                                <div class="input-group">
                                    <span class="input-group-addon">labelName</span>
                                    <select class="report_filter form-control form-inline" id="task_type_labelName">

                                    </select>
                                </div>
                            </td>
                            <td align="left" id="filter_dimension" data-column="4" style="width:200px">
                                <div class="input-group">
                                    <span class="input-group-addon ">count</span>
                                                <span class="input-group-addon">
                                                    <input type="text" class="report_filter" name="dimension_filter" value="10" id="dimension_count"/>
                                                </span>
                                </div>
                            </td>
                            <td  align="left" id="report_filter_begin_td">

                            </td>

                        </tr>
                        <tr>
                            <td align="left"  style="width:400px" >
                                <div id="report_begin_div" class="input-group ">
                                    <span id="report_startDate" class="input-group-addon">Begin</span>
                                    <input id="report_timespan_begin_input" class="form-inline form-control"/>
                                    <span id="report_startDate_addon" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td align="left"  data-column="4"style="width:400px" >
                                <div class="input-group">
                                    <span class="input-group-addon " style="text-align: left;">environment</span>
                                    <select class="report_filter form-control form-inline" id="dimension_environment">

                                    </select>


                                    <span style=""></span>
                                </div>
                            </td>

                            <td align="left"   data-column="4" style="width:200px">
                                <div class="input-group">
                                    <a id="report_generate_btn" class="btn btn-primary">Generate</a>
                                </div>

                            </td>
                            <td align="left">

                            </td>

                        </tr>
                        </tbody>
                    </table>
                    <span id="test_span_domain"></span>
                </div>

                <div id="etp_report_result" class="half-color-header filter_div" style="padding: 20px">
                    <table class="filter_table" id="etp_report_table" style="width:1240px; border: 1px; ">
                        <tbody id="filter_table_tbody">


                        </tbody>
                    </table>
                    <span id="test_span_domain2"></span>
                </div>



            </div>
            <div id="report_history" style="width: 100%; height:600px;" ></div>
            <div id="report_history2" style="width: 100%; height:50px;" ></div>
    </div>
</div>
<div id="report_container" style="width: 100%; height:600px;" class="hidden">

</div>
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
<script src="/assets/jquery-highcharts/highcharts.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/jmt_report.js"></script>
</html>
