<%--
  Created by IntelliJ IDEA.
  User: lizonglin
  Date: 2015/6/9
  Time: 17:26
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>生成报告</title>
    <link href="/css/task.css" rel="stylesheet" type="text/css"/>
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="gen_report" class="special-div">
                <div id="gen_report_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div class="col-sm-4 col-sm-offset-4 col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-4">
                            <div class="half-color-header">
                                <h2>生成报告</h2>
                            </div>
                            <table id="gen_report_table" class="table half-trans-green">
                                <thead>
                                <tr></tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th class="col-sm-3 col-md-2 col-lg-2">报告类型</th>
                                    <td>
                                        <div class="col-sm-9 col-md-9 col-lg-9">
                                                <select id="task_type_select" class="form-control">
                                                    <option value="-1" selected="selected">All</option>
                                                    <c:forEach var="tt" items="${taskTypes}">
                                                        <option value="${tt[1]}">${tt[0]}</option>
                                                    </c:forEach>
                                                </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>开始日期</th>
                                    <td>
                                        <div class="col-sm-9 col-md-9 col-lg-9">
                                            <div id="start_date_div" class="input-group">
                                                <input id="start_date_input" class="form-inline form-control"/>
                                                <span id="start_date_addon" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>结束日期</th>
                                    <td>
                                        <div class="col-sm-9 col-md-9 col-lg-9">
                                            <div id="end_date_div" class="input-group">
                                                <input id="end_date_input" class="form-inline form-control"/>
                                                <span id="end_date_addon" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="col-sm-12 col-md-12 col-lg-12" style="text-align: center">
                                            <a id="gen_report_btn" class="btn btn-primary">生成</a>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                                <tfoot>
                                <tr></tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="gen_report_sending_modal" tabindex="-1" role="dialog"
     aria-labelledby="sendingModalLabel" aria-hidden="false" data-keyboard="false" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sendingModalLabel" >
                    Generating Report
                </h4>
            </div>
            <div id="sending-body" class="modal-body" style="text-align: center">
                <span>Generating Report......</span>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="gen_report_sent_modal" tabindex="-1" role="dialog"
     aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sentModalLabel" >
                    Generate Success
                </h4>
            </div>
            <div id="sent-body" class="modal-body" style="text-align: center">
                <span>Generate report successfully!</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <a id="branch_modal_back_btn" href="/gtp-report" class="btn btn-primary">
                    Go Report
                </a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<div class="modal fade" id="gen_report_error_modal" tabindex="-1" role="dialog"
     aria-labelledby="errorModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="errorModalLabel">
                    错误
                </h4>
            </div>
            <div id="check-error-body" class="modal-body" style="text-align: center">
                <span id="error_span"></span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<script src="/js/util.js" type="text/javascript"></script>
<script src="/js/gen_report.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
</body>
</html>

