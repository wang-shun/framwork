<%-- 
    Document   : task_edit
    Created on : 2015-1-29, 14:28:46
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>编辑Load任务 ${taskInfo[0]}</title>
    <link href="/css/task.css" rel="stylesheet" type="text/css"/>
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="task_edit" class="special-div">
                <div id="task_edit_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div class="col-xs-6 col-xs-offset-3 col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3">
                            <div class="half-color-header">
                                <h2>编辑Load任务 <span id="task_edit_id_span">${taskInfo[0]}</span></h2>
                            </div>
                            <table id="task_edit_table" class="table half-trans-green">
                                <tbody>
                                <tr>
                                    <th>名称</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <input class="form-control" type="text" id="task_edit_name" name="task_edit_name" value="${taskInfo[1]}"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>运行类型</th>
                                    <td>
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <select id="task_edit_runType_select" class="form-control">
                                                <c:forEach var="type" items="${runTypes}">
                                                    <c:choose>
                                                        <c:when test="${type[0] == taskInfo[11]}">
                                                            <option value="${type[0]}" selected="selected"> ${type[0]} </option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${type[0]}"> ${type[0]} </option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>调度规则</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <select id="task_edit_regularInfo_select" class="form-control">
                                                <c:forEach var="regular" items="${regularInfos}">
                                                    <c:choose>
                                                        <c:when test="${regular == regularName}">
                                                            <option value="${regular}" selected="selected">${regular}</option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${regular}"> ${regular} </option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>开始日期</th>
                                    <td class="">
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <div class="input-group">
                                                <input id="task_edit_sd_input" class="form-control" value="${taskInfo[12]}" type="text" readonly="readonly"/>
                                                <span id="task_edit_startDate" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>环境</th>
                                    <td>
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <select id="task_edit_env_select" class="form-control">
                                                <c:forEach var="env" items="${envs}">
                                                    <c:choose>
                                                        <c:when test="${taskInfo[2] == env[0]}">
                                                            <option value="${env[0]}" selected="selected"> ${env[0]} </option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${env[0]}"> ${env[0]} </option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>SVN</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <select id="task_edit_branch_select" class="form-control">
                                                <c:forEach var="branch" items="${branches}">
                                                    <c:choose>
                                                        <c:when test="${branchName == branch}">
                                                            <option value="${branch}" selected="selected"> ${branch} </option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${branch}">${branch}</option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr >
                                    <th>性能测试配置</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <select id="task_edit_loadconf_select" class="form-control">
                                                <c:forEach var="loadconf" items="${loadConfNames}">
                                                    <c:choose>
                                                        <c:when test="${loadconf == taskInfo[17]}">
                                                            <option value="${loadconf}" selected="selected">${loadconf}</option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${loadconf}">${loadconf}</option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>机器</th>
                                    <td>
                                        <div class="col-xs-5 col-md-5 col-sm-5">
                                            <c:choose>
                                                <c:when test="${'None' != taskInfo[4]}">
                                                    <input type="checkBox" class="form-inline" checked="checked" id="task_edit_isMachine" name="task_edit_isMachine"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="checkBox" class="form-inline" id="task_edit_isMachine" name="task_edit_isMachine"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <span class="form-inline">是否指定机器 ?</span>
                                            <select id="task_edit_machineList" class="form-control">
                                                <c:forEach var="machine" items="${machines}">
                                                    <c:choose>
                                                        <c:when test="${machine[0] == taskInfo[4]}">
                                                            <option value="${machine[0]}" selected="selected"> ${machine[0]} </option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${machine[0]}"> ${machine[0]} </option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>特殊选项</th>
                                    <td>
                                        <div class="col-md-9">
                                            <c:choose>
                                                <c:when test="${ true == taskInfo[14]}">
                                                    <input type="checkBox" class="form-inline" checked="checked" id="task_edit_isMonited" name="task_edit_isMonited"/>
                                                    <span class="form-inline" title="Whether monitor this task ?">是否监控 ? &nbsp;</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="checkBox" class="form-inline" id="task_edit_isMonited" name="task_edit_isMonited"/>
                                                    <span class="form-inline" title="Whether monitor this task ?">是否监控 ? &nbsp;</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${ true == taskInfo[15]}">
                                                    <input type="checkBox" class="form-inline" checked="checked" id="task_edit_isSplit" name="task_edit_isSplit"/>
                                                    <span class="form-inline" title="Whether split this single task to many, which can run parallel ?">是否拆分 ?</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="checkBox" class="form-inline" id="task_edit_isSplit" name="task_edit_isSplit"/>
                                                    <span class="form-inline" title="Whether split this single task to many, which can run parallel ?">是否拆分 ?</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>编辑者</th>
                                    <td>
                                        <div class="col-md-5">
                                            <input type="text" class="form-control" id="task_edit_editor" name="task_edit_editor" value="${sessionScope.userName}" readonly="true"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Email列表</th>
                                    <td>
                                        <div class="col-md-9">
                                            <textarea class="form-control" id="task_edit_email" name="task_edit_email" style="width:100%; height:80px;">${taskInfo[16]}</textarea>
                                            <span style="color:#006dcc">多个Email地址时，请使用 ',' 进行分割</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div>
                                            <a id="task_edit_save_btn" href="javascript:updateTaskInfo(3);" class="btn btn-primary">保存</a>
                                            <a id="task_edit_cancel_btn" class="btn btn-default" onclick="window.history.back(-1)">取消</a>
                                        </div>
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
<div class="modal fade" id="task_edit_error_modal" tabindex="-1" role="dialog"
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
<div class="modal fade" id="task_edit_sending_modal" tabindex="-1" role="dialog"
     aria-labelledby="sendingModalLabel" aria-hidden="false" data-keyboard="false" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sendingModalLabel" >
                </h4>
            </div>
            <div id="sending-body" class="modal-body" style="text-align: center">
                <span>后台处理中......</span>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="task_edit_sent_modal" tabindex="-1" role="dialog"
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
                <span>更新Load任务成功</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
                <button id="task_modal_back_btn" type="button" class="btn btn-primary" onclick="window.location.href='/task'">
                    返回列表
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<script src="/js/task_edit.js" type="text/javascript"></script>
<script src="/js/util.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
</body>
</html>