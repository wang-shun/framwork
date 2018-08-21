<%-- 
    Document   : regularNew
    Created on : 2015-2-6, 13:23:53
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>新建调度规则</title>
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
                    <div id="regular_new" class="special-div">
                        <div class="col-md-8 col-md-offset-2">
                            <div class="half-color-header">
                                <h2>新建调度规则</h2>
                                <div id="error_div">
                                    <p class="check_css hidden">请更正错误后重试</p>
                                    <p class="check_css hidden">所有项都为必填</p>
                                </div>
                            </div>
                            <div>
                                <table class="table half-trans-green table-bordered">
                                    <thead>
                                        <tr class=""></tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <th class="col-md-1">名称</th>
                                            <td class="col-md-2" colspan="3">
                                                <div>
                                                    <input type="text" class="form-control" id="newName" placeholder="名称不允许重复" name="newName" />
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="col-md-1">调度类型</th>
                                            <td>
                                                <div class="form-inline">
                                                    <select class="form-control" name="newTypes" id="newTypes">
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
                                        <tr id="new_date_tr">
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
                                        <tr id="new_week_tr">
                                            <th>星期</th>
                                            <td>
                                                <div class="form-inline">
                                                    <div class="form-group">
                                                        <select id="newWeekDay" name="weekDay_select" class="form-control">
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
                                                <input type="radio" name="regularRunRule" value="0">可选
                                                <input type="radio" name="regularRunRule" value="1">强制
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                <input type="button" class="btn btn-primary" id="newSave" name="newSave" value="保存"/>
                                <input type="button" class="btn btn-default" onclick="window.history.back(-1)" value="取消">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="regular_new_sent_modal" tabindex="-1" role="dialog" 
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
                        <span>新建调度规则成功</span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>

        <div class="modal fade" id="regular_new_error_modal" tabindex="-1" role="dialog" 
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
        <script type="text/javascript" src="/js/util.js"></script>
        <script type="text/javascript" src="/js/regularNew.js"></script>
    </body>
</html>

