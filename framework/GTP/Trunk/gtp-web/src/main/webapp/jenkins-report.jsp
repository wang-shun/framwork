<%--
    Document   : task_report
    Created on : 2015-2-4, 13:33:30
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/css/task.css" rel="stylesheet" type="text/css"/>
    <link href="/assets/datatables/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
    <title>Jenkins 报告 ${taskId}</title>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="jenkins_report" class="special-div">
                <div class="half-color-header">
                    <h2>Jenkins 报告 <span id="jenkins_report_id_span">${taskId}</span></h2>
                    <span id="jenkinsaddr_span" class="hidden">${jenkinsAddr}</span>
                </div>
                <div id="jenkins_report_table_div" class="half-trans-green table-div">
                    <table id="jenkins_report_list" class="table table-striped table-hover">
                        <thead>
                        <tr>
                            <th>任务ID</th>
                            <th>开始时间</th>
                            <th>任务状态</th>
                            <th>Jenkins日志</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/assets/datatables/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="/js/util.js" type="text/javascript"></script>
<script src="/js/jenkins_report.js" type="text/javascript"></script>
</body>
</html>
