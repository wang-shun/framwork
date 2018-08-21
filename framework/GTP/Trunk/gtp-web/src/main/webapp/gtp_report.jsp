<%-- 
    Document   : etpReport
    Created on : 2015-1-28, 18:01:13
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>GTP报告</title>
        <link rel="stylesheet" href="css/gtp_report.css"/>
        <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
        <jsp:include page="frame.jsp" flush="true"/>

    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="etp_report" class="special-div">
                        <div id="etp_report_filter" class="half-color-header filter_div" style="padding: 20px">
                            <h2>GTP报告</h2>
                            <table class="filter_table" id="etp_report_filter_table" style="width:1240px">
                                <tbody>
                                    <tr>
                                        <td rowspan="2" style="padding: 0">
                                            <label><span class="glyphicon glyphicon-filter"></span></label>
                                        </td>
                                        <td align="left" id="filter_reportType" style="width:300px">
                                            <div class="input-group">
                                                <span class="input-group-addon">统计类型</span>
                                                <select class="report_filter form-control form-inline" id="report_type_filter">
                                                    <c:forEach var="rt" items="${reportTypes}">
                                                        <option value="${rt[1]}">${rt[0]}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <td align="left" id="filter_col4" data-column="4"style="width:300px">
                                            <div class="input-group">
                                                <span class="input-group-addon">任务类型</span>
                                                <select class="report_filter form-control form-inline" id="task_type_filter">
                                                    <option value="-1" selected="selected">All</option>
                                                    <c:forEach var="tt" items="${taskTypes}">
                                                        <option value="${tt[1]}">${tt[0]}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <%--<td align="left" id="filter_dimension" data-column="4" style="width:300px">--%>
                                            <%--<div class="input-group">--%>
                                                <%--&lt;%&ndash;<span class="input-group-addon ">范围</span>&ndash;%&gt;--%>
                                                <%--<span class="input-group-addon">群组--%>
                                                    <%--<input type="radio" class="report_filter data-checked" name="dimension_filter" value="Group" id="dimension_group" checked/>--%>
                                                <%--</span>--%>
                                                <%--<span class="input-group-addon">个人--%>
                                                    <%--<input type="radio" class="report_filter" name="dimension_filter" value="Personal" id="dimension_personal"/>--%>
                                                <%--</span>--%>
                                            <%--</div>--%>
                                        <%--</td>--%>
                                        <td align="left" id="filter_group" data-column="8">
                                            <div class="input-group">
                                                <span class="input-group-addon">群组</span>
                                                <select class="report_filter form-control form-inline" id="group_filter">
                                                    <option value="-1" selected="selected">All</option>
                                                    <c:forEach var="group" items="${groups}">
                                                        <option value="${group[1]}">${group[0]}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <td align="left" id="filter_owner" data-column="8">
                                            <div class="input-group">
                                                <span class="input-group-addon">所有人</span>
                                                <select class="report_filter form-control form-inline" id="owner_filter">
                                                    <option id="owner_all_option" value="-1" selected="selected">All</option>
                                                    <c:forEach var="owner" items="${owners}">
                                                        <option value="${owner}">${owner}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td  align="left" id="report_filter_begin_td">
                                            <div id="report_begin_div" class="input-group ">
                                                <span id="report_startDate" class="input-group-addon">开始日期</span>
                                                <input id="report_timespan_begin_input" class="form-inline form-control"/>
                                                <span id="report_startDate_addon" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td align="left" id="report_filter_end_td">
                                            <div id="report_end_div" class="input-group ">
                                                <span id="report_endDate" class="input-group-addon">截止日期</span>
                                                <input id="report_timespan_end_input" class="form-inline form-control"/>
                                                <span id="report_endDate_addon" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td align="left">
                                            <a id="report_generate_btn" class="btn btn-primary">生成</a>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <span id="test_span_domain"></span>
                        </div>
                        <div id="report_container" style="width: 100%; height:600px;">

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="loading_modal" tabindex="-1" role="dialog"
             aria-labelledby="task_sending" aria-hidden="false" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="text-align: center">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="task_sending" >
                        </h4>
                    </div>
                    <div id="task_sending_body" class="modal-body" style="text-align: center">
                        <span>后台处理中......</span>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
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
    <script type="text/javascript" src="/js/gtp_report.js"></script>
</html>
