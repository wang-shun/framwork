<%-- 
    Document   : envJobs_edit
    Created on : 2015-2-2, 16:53:07
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EnvJob Edit</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
        <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="/css/page.css">
        <jsp:include page="frame.jsp" flush="true"/>

    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="envJobs_edit" class="col-lg-8 col-lg-offset-2">
                        <form class="container-fluid" role="form" action="/envJobs/save/${job.id}" method="POST">
                            <div class="half-color-header" id="table_header_div">
                                <h2 class="headline">Edit &nbsp;&nbsp;#<span id="regular_edit_id">${job.id}</span></h2>
                                <input type="hidden" id="id" name="id" value="${job.id}">
                                <div class="form-inline">
                                    <div class="form-group">
                                        <label class="headline">JobName</label>
                                        <input type="text" class="form-control" id="jobname" name="jobname" value="${job.jobname}">
                                    </div>
                                    <div class="form-group">
                                        <label class="headline">scheduleWeek </label>
                                        <select class="select_css form-control" name="scheduleWeek" id="scheduleWeek">
                                            <option value="0" selected="selected">Sunday</option>                    
                                            <option value="1">Monday</option>
                                            <option value="2">Tuesday</option>                    
                                            <option value="3">Wednesday</option>
                                            <option value="4">Thursday</option>                    
                                            <option value="5">Friday</option>
                                            <option value="6">Saturday</option>                    
                                        </select>
                                    </div>
                                    <div class="form-group">
                                            <div class="form-group">
                                                <label class="headline">scheduleTime </label>
                                                <div class="input-group">
                                                    <input type="text" class="form-control" id="scheduleTime" name="scheduleTime" value="${job.scheduleTime}">
                                                    <span id="schedule_edit_time" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                </div>
                                            </div>
                                    </div>
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" id="enable" name="enable" value="true" checked="checked"><b class="headline">enable</b>
                                        <input type="hidden" value="false" name="enable">
                                    </label>
                                </div>
                            </div>

                            <div class="form-group">
                                <textarea class="col-lg-12 col-md-12" id="CMD" name="CMD" style="height: 600px">${job.CMD}</textarea>
                            </div>

                            <div>
                                <input type="submit" class="btn btn-success" id="jobSave" name="jobSave" value="Save">
                            </div>
                            <h4><a class="alert-link back_css" href="/envJobs">Back To List</a><h4>
                                    </form>

                                    </div>
                                    </div>
                                    </div>

                                    </div>
                            
                                    <script type="text/javascript" src="/assets/jquery-datepicker/jquery.datetimepicker.js" ></script>
                                    <script type="text/javascript" src="/js/envJobs_datetime.js"></script>
                                    <script type="text/javascript" src="/js/envJobs.js"></script>
                                    <script type="text/javascript" src="/js/util.js"></script>
                                    </body>
                                    </html>

