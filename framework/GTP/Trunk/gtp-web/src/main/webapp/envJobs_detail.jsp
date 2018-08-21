<%-- 
    Document   : envJobs_detail
    Created on : 2015-2-3, 14:45:38
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EnvJob Detail</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="/css/page.css">
        <jsp:include page="frame.jsp" flush="true"/>

    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="envJobs_edit" class="col-lg-10 col-md-12">
                        <div class="half-color-header" id="table_header_div">
                            <h4 class="headline">
                                <label>ID:</label><span>${job.id}&nbsp;</span>
                                <label>JobName:</label><span>${job.jobname}&nbsp;</span>
                                <label>scheduleWeek:</label><span>${job.scheduleWeek}&nbsp;</span>
                                <label>scheduleTime:</label><span>${job.scheduleTime}&nbsp;</span>
                                <label>Enable:</label><span>${job.enable}&nbsp;</span>
                            </h4>
                        </div>

                        <div class="form-group">
                            <textarea class="col-lg-12 col-md-12" id="CMD" name="CMD" style="height: 600px">${job.CMD}</textarea>
                        </div>

                        <div>
                            <h4><a class="alert-link back_css" href="/envJobs/edit/${job.id}">Edit</a>
                                |
                                <a class="alert-link back_css" href="/envJobs">Back To List</a>
                                <h4>
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


