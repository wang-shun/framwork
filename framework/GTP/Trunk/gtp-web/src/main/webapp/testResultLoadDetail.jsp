<%-- 
    Document   : reportDetail
    Created on : 2015-1-27, 9:30:03
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>TestResultDetail</title>
    <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="/css/page.css">
    <link rel="stylesheet" href="/css/index.css">
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="testResult" class="special-div">
                <div class="half-color-header" id="table_header_div">
                    <div class="form-group">
                        <h2 class="headline">${taskReport.taskName} 详细报告</h2>
                    </div>
                    <!--<div id="task_filter_div" class="half-color-header">-->
                    <!--</div>-->
                </div>
                <div id="table_content_div" class="half-trans-green table-div">
                    <table id="report_List" class="table table-striped  table-hover half-trans-green">
                        <thead>
                        <th>总数</th>
                        <th>成功</th>
                        <th>失败</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>耗时(ms)</th>
                        </thead>
                        <tr>
                            <td><a href="#" class="case-plus dropdown-toggle" title="OrderList" data-name="reportDetail_list"><span class="glyphicon glyphicon-plus"></span></a>${taskReport.total}</td>
                            <td>${taskReport.pass}</td>
                            <td>${taskReport.fail}</td>
                            <td><c:if test="${taskReport.startTime != null }">${taskReport.getStartTimeStr()}</c:if>
                                <c:if test="${taskReport.startTime == null }"></c:if></td>
                            <td><c:if test="${taskReport.endTime != null }">${taskReport.getEndTimeStr()}</c:if>
                                <c:if test="${taskReport.endTime == null }"></c:if></td>
                            <td>${taskReport.duration}</td>
                        </tr>
                    </table>
                    <table id="reportDetail_list" class="table table-striped hidden  table-hover half-trans-green">
                        <thead>
                        <th>场景</th>
                        <th>标签</th>
                        <th>小场景</th>
                        <th>结果版本</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>总数</th>
                        <th>成功</th>
                        <th>失败</th>
                        <th>耗时(ms)</th>
                        </thead>
                        <c:forEach var="detail" items="${taskReport.loadLogDetails}">
                           <tr class="main-row">
                               <td>${detail.sceneName}</td>
                               <td>${detail.labelName}</td>
                               <td>${detail.smallSceneName}</td>
                               <td>${detail.resultVersion}</td>
                               <td><c:if test="${detail.startTime != null }">${detail.getStartTimeStr()}</c:if>
                                   <c:if test="${detail.startTime == null }"></c:if></td>
                               <td><c:if test="${detail.endTime != null }">${detail.getEndTimeStr()}</c:if>
                                   <c:if test="${detail.endTime == null }"></c:if></td>
                               <td>${detail.total}</td>
                               <td>${detail.success}</td>
                               <td>${detail.error}</td>
                               <td>${detail.duration}</td>
                           </tr>
                        </c:forEach>
                        <tr>

                        </tr>
                    </table>
                    <table id="report_mess" class="table table-striped  table-hover half-trans-green">
                        <tr>
                            <td>日志地址</td>
                            <td><a href="javascript:void(0)" onclick="showLog('${taskReport.mojoLogUrl}')" >${taskReport.mojoLogUrl}</a> </td>
                        </tr>
                        <tr>
                            <td>错误信息</td>
                            <td><pre style="height: 150px">${taskReport.errorMessage}</pre></td>
                        </tr>

                    </table>

                </div>
                <div class="half-color-header">
                    <a class="btn btn-primary" href="javascript:window.history.back(-1)">返回列表</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/js/testResultLoadDetail.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
</body>
</html>

