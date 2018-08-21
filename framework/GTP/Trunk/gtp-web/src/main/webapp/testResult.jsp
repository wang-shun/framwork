<%-- 
    Document   : testResult
    Created on : 2015-1-26, 12:14:48
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>测试报告</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="/css/page.css"/>
        <jsp:include page="frame.jsp" flush="true"/>
        
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="testResult" class="special-div">
                        <div class="half-color-header">
                            <h2>测试报告 </h2>
                        </div>
                        <div id="table_content_div" class="half-trans-green">
                        <table id="report_list" class="table table-striped  table-hover half-trans-green">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>任务</th>
                                    <th>Total</th>
                                    <th>Passed</th>
                                    <th>Failed</th>
                                    <th>Aborted</th>
                                    <th>StartTime</th>
                                    <th>Duration (ms)</th>
                                    <th>ErrorMessage</th>
                                    <th>Operation</th>
                                </tr>
                            </thead>
                        </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="/js/testResult.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
    </body>
</html>
