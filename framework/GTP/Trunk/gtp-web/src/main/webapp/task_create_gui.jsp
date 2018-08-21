<%-- 
    Document   : task_create
    Created on : 2015-3-2, 14:26:22
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>新建GUI任务</title>
    <link href="/css/task.css" rel="stylesheet" type="text/css"/>
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="task_new" class="special-div">
                <div id="task_new_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div class="col-xs-6 col-xs-offset-3 col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3">
                            <div class="half-color-header">
                                <h2>新建GUI任务</h2>
                            </div>
                            <table id="task_new_table" class="table table-bordered half-trans-green">
                                <thead>
                                <tr></tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th>名称</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <input class="form-control" type="text" id="task_new_name"
                                                   name="task_new_name"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>运行类型</th>
                                    <td>
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <select id="task_new_runType_select" class="form-control">
                                                <c:forEach var="type" items="${runTypes}">
                                                    <option value="${type[0]}"> ${type[0]} </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>调度规则</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <select id="task_new_regularInfo_select" class="form-control">
                                                <c:forEach var="regular" items="${regularInfos}">
                                                    <option value="${regular}">${regular}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>

                                <tr>
                                    <th>环境</th>
                                    <td>
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <select id="task_new_env_select" class="form-control">
                                                <c:forEach var="env" items="${envs}">
                                                    <option value="${env[0]}"> ${env[0]} </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>SVN</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <select id="task_new_branch_select" class="form-control">
                                                <c:forEach var="branch" items="${branches}">
                                                    <option value="${branch}"> ${branch} </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="api-type gui-type">
                                    <th>用例筛选</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <input type="text" class="form-control" id="task_new_cq"
                                                   placeholder="筛选条件表达式" name="task_new_cq"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>浏览器</th>
                                    <td>
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <c:forEach var="browser" items="${browsers}">
                                                <input name="task_new_browser" class="form-inline" type="checkbox" value="${browser}"/>
                                                <span class="form-inline">${browser}</span><br>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>机器</th>
                                    <td>
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <input type="checkBox" class="form-inline" id="task_new_isMachine"
                                                   name="task_new_isMachine"/>
                                            <span class="form-inline">是否指定机器 ?</span>
                                            <select id="task_new_machineList" class="form-control">
                                                <%--<option value="None" selected="selected">None</option>--%>
                                                <c:forEach var="machine" items="${machines}">
                                                    <option value="${machine[0]}" title="${machine[1]}"> ${machine[0]} </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>特殊选项</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <input type="checkBox" class="form-inline" checked="checked"
                                                   id="task_new_isMonited" name="task_new_isMonited"/>
                                            <span class="form-inline">是否监控 ?&nbsp;&nbsp;</span>
                                            <input type="checkBox" class="form-inline" id="task_new_isSplit"
                                                   name="task_new_isSplit"/>
                                            <span class="form-inline">是否拆分 ?</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>创建者</th>
                                    <td>
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <input type="text" class="form-control" id="task_new_editor"
                                                   name="task_new_editor" value="${sessionScope.userName}"
                                                   readonly="true"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Email列表</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <textarea class="form-control" id="task_new_email" name="task_new_email"
                                                      style="width:100%; height:80px;"
                                                      placeholder="请填写收件人列表，以英文逗号分隔">${sessionScope.email}</textarea>
                                            <span style="color:#006dcc">多个Email地址时，请使用 ',' 进行分割</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <a id="task_new_save_btn" href="javascript:saveNewTask(2);"
                                           class="btn btn-primary">保存</a>
                                        <a id="task_new_cancel_btn" class="btn btn-default"
                                           onclick="window.history.back(-1)">取消</a>
                                    </td>
                                </tr>
                                </tbody>
                                <tfoot>
                                <tr></tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="task_new_sent_modal" tabindex="-1" role="dialog"
     aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sentModalLabel">

                </h4>
            </div>
            <div id="sent-body" class="modal-body" style="text-align: center">
                <span>新建GUI任务成功</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>

<div class="modal fade" id="task_new_error_modal" tabindex="-1" role="dialog"
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
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<input type="hidden" id="paramsInput" value="${params}"/>
<script src="/js/util.js" type="text/javascript"></script>
<script src="/js/task_new.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
</body>
</html>