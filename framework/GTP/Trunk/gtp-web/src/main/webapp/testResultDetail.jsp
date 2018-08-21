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
    <title>测试结果详情</title>
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
                        <h2 class="headline">${taskReport.taskId} 详细报告</h2>
                    </div>
                    <!--<div id="task_filter_div" class="half-color-header">-->
                    <table id="detail_filter_table">
                        <tbody>
                        <tr>
                            <td><label><span class="glyphicon glyphicon-filter">&nbsp;</span></label></td>
                            <td align="left" id="filter_col6">
                                <div class="">
                                    <select class="column_filter form-control" id="col6_filter">
                                        <option value="All">All</option>
                                        <option value="Fail" selected="selected">Fail</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <!--</div>-->
                </div>
                <div id="table_content_div" class="half-trans-green table-div">
                    <table id="reportDetail_list" class="table table-striped  table-hover half-trans-green">
                        <thead>
                        <th>用例名称</th>
                        <th>结果</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>耗时(ms)</th>
                        <th>重试次数</th>
                        <th>机器</th>
                        <th>所有人</th>
                        <th>用例描述</th>
                        </thead>
                        <c:forEach var="detail" items="${taskReport.details}">
                            <tr class="main-row">
                                <td><c:if test="${fn:length(detail.children) >0}"><a href="#" class="case-plus dropdown-toggle" title="OrderList" data-name="${detail.testCaseName}"><span class="glyphicon glyphicon-plus"></span></a></c:if>${detail.testCaseName}</td>
                                <td name="result">${detail.testResult}</td>
                                <td name="dateTime">${detail.startTime}</td>
                                <td name="dateTime">${detail.endTime}</td>
                                <td>${detail.duration}</td>
                                <td>${detail.rerun}</td>
                                <td>${detail.computerName}</td>
                                <td>${detail.owner}</td>
                                <td>${detail.caseDesc}</td>
                            </tr>
                            <c:if test="${fn:length(detail.children) >0}">
                                <tr class="hidden show-tr" id="${detail.testCaseName}">
                                    <td colspan="10">
                                        <table class="table table-bordered">
                                            <thead>
                                            <th>用例名称</th>
                                            <th>结果</th>
                                            <th>开始时间</th>
                                            <th>结束时间</th>
                                            <th>耗时(ms)</th>
                                            <th>重试次数</th>
                                            <th>机器</th>
                                            <th>所有人</th>
                                            <th>用例描述</th>
                                            </thead>
                                            <c:forEach var="d" items="${detail.children}">
                                                <tr>
                                                    <td>${d.testCaseName}</td>
                                                    <td>${d.testResult}</td>
                                                    <td name="dateTime">${d.startTime}</td>
                                                    <td name="dateTime">${d.endTime}</td>
                                                    <td>${d.duration}</td>
                                                    <td>${d.rerun}</td>
                                                    <td>${d.rerunCount}</td>
                                                    <td>${d.owner}</td>
                                                    <td>${d.caseDesc}</td>
                                                </tr>
                                                <c:if test="${fn:length(d.errorMessage) > 0 && fn:length(d.stackTrace) > 0}">
                                                    <tr>
                                                        <th>错误信息</th>
                                                        <td style="word-wrap: break-word" colspan="8">${d.errorMessage}</td>
                                                    </tr>
                                                    <tr>
                                                        <th>异常堆栈</th>
                                                        <td colspan="8"><pre style="height: 150px">${d.stackTrace}</pre></td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                        </table>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${fn:length(detail.errorMessage) >0 || fn:length(detail.stackTrace) >0}">
                                <tr>
                                    <th>错误信息</th>
                                    <td style="word-wrap: break-word" colspan="8">${detail.errorMessage}</td>
                                </tr>
                                <tr>
                                    <th>异常堆栈</th>
                                    <td name="stack-td" colspan="9"><pre style="height: 200px;">${detail.stackTrace}</pre></td>
                                </tr>
                            </c:if>
                        </c:forEach>
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
<script type="text/javascript" src="/js/testResultDetail.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
</body>
</html>

