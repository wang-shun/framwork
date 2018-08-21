<%-- 
    Document   : regularRule_edit
    Created on : 2015-2-4, 9:49:56
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>编辑调度规则</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
        <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="/css/page.css" rel="stylesheet" >
        <link href="/css/task.css" rel="stylesheet" type="text/css"/>


        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="regular_edit" class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1 special-div">
                        <!--<form class="container-fluid" role="form" action="/regularRule/save" method="POST">-->
                        <div>
                            <div class="half-color-header">
                                <h2>编辑调度规则&nbsp;<span id="regular_edit_id">${regular[0]}</span></h2>
                                <input type="text" class="form-control hidden" id="regularId_hide" name="regularId_hide" value="${regular[0]}">
                            </div>
                            <table class="table half-trans-green table-bordered">
                                <thead>
                                    <tr class=""></tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <th class="col-md-1">名称</th>
                                        <td class="col-md-2">
                                            <div>
                                                <input type="text" class="form-control" id="regularName" placeholder="名称不允许重复" name="regularName" value="${regular[1]}">
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="col-md-1">调度类型</th>
                                        <td>
                                            <div class="form-inline">
                                                <select class="form-control" name="scheduleType" id="scheduleType">
                                                    <c:forEach var="type" items="${regularTypes}">

                                                        <c:choose>
                                                            <c:when test="${regular[2] == type[0]}">
                                                                <option value="${type[1]}" selected="selected">${type[0]}</option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${type[1]}"> ${type[0]} </option>
                                                            </c:otherwise>
                                                        </c:choose>

                                                    </c:forEach>                    
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr id="run_rule_date_tr">
                                        <th>日期</th>
                                        <td>
                                            <div class="form-inline">
                                                <div id="date_hide" class="form-group">
                                                    <div id="regular_edit_date" class="input-group">
                                                        <input type="text" class="form-control" id="regularDate" name="regularDate" value="${regular[4]}"/>
                                                        <span  class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr id="run_rule_week_tr">
                                        <th>星期</th>
                                        <td>
                                            <div class="form-inline">
                                                <div class="form-group">
                                                    <select id="weekDay_select" name="weekDay_select" class="form-control">
                                                        <option value="SUN">SUN</option>
                                                        <option value="MON">MON</option>
                                                        <option value="TUE">TUE</option>
                                                        <option value="WED">WED</option>
                                                        <option value="THU">THU</option>
                                                        <option value="FRI">FRI</option>
                                                        <option value="SAT">SAT</option>
                                                    </select>
                                                    <input type="text" class="hidden" id="regularWeekDay_hide" name="regularWeekDay_hide" value="${regular[3]}">
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>时间</th>
                                        <td class="col-md-4">
                                            <div class="form-inline">
                                                <div class="form-group">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" id="regularTime" name="regularTime" value="${regular[4]}">
                                                        <span id="regular_edit_time" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </div>
                                                <span>* 0-23(例如 15:10)</span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>运行规则</th>
                                        <td>
                                            <input type="radio" name="RegularRunRule" value="0">可选
                                            <input type="radio" name="RegularRunRule" value="1">强制
                                        </td>
                                        <td class="hidden">
                                            <input class="hidden" id="runRule_hide" name="runRule_hide" value="${regular[5]}">
                                            <input class="hidden" id="scheduleType_hide" name="scheduleType_hide" value="${regular[2]}">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <!--</form>-->
                        <input type="button" class="btn btn-primary" id="regularSave" name="regularSave" value="保存"/>
                        <input type="button" class="btn btn-default" onclick="window.history.back(-1)" value="取消">
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="regular_edit_sent_modal" tabindex="-1" role="dialog" 
             aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="text-align: center">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="sentModalLabel" >
                        </h4>
                    </div>
                    <div id="sent-body" class="modal-body" style="text-align: center">
                        <span>更新调度规则成功</span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                        <button id="task_modal_back_btn" type="button" class="btn btn-primary">
                                返回列表
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
                                        
        <div class="modal fade" id="regular_edit_error_modal" tabindex="-1" role="dialog" 
             aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
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
        <script type="text/javascript" src="/assets/jquery-datepicker/jquery.datetimepicker.js" ></script>
        <script type="text/javascript" src="/js/regular_datetime.js"></script> 
        <script type="text/javascript" src="/js/regularEdit.js"></script> 
        <script type="text/javascript" src="/js/util.js"></script>
    </body>
</html>

