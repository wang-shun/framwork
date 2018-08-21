<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Env Job</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="/css/task.css"/>
        <link rel="stylesheet" href="/css/page.css"/>
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="testResult">
                        <div class="half-color-header" id="table_header_div">
                            <h2 class="headline">ETP:Hosts</h2>
                        </div>
                        <div id="table_content_div" class="half-trans-green">
                        <table id="envJobs_list" class="table table-striped  table-hover half-trans-green">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>JobName</th>
                                    <th>Week</th>
                                    <th>Time</th>
                                    <th>LastUpdateUser</th>
                                    <th>LastUpdatTime</th>
                                    <th>Enable</th>
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
        <script type="text/javascript" src="/js/envJobs.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
        
    </body>
</html>
