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
        <title>${tasktype} 任务报告 ${taskId}</title>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="task_report" class="special-div">
                        <div class="half-color-header">
                            <h2><span  id="task_report_type_span">${tasktype}</span> 任务报告 <span id="task_report_id_span">${taskId}</span></h2>
                        </div>
                        <div id="task_report_table_div" class="half-trans-green table-div">
                            <table id="task_report_list" class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>任务名称</th>
                                        <th>用例总数</th>
                                        <th>成功</th>
                                        <th>失败</th>
                                        <th>中断</th>
                                        <th>开始时间</th>
                                        <th>结束时间</th>
                                        <th>耗时(ms)</th>
                                        <th>入库时间</th>
                                        <th>操作</th>
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
        <script src="/js/task_report.js" type="text/javascript"></script>
    </body>
</html>
