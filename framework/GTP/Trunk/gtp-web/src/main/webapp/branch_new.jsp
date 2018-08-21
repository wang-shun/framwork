<%--
  Created by IntelliJ IDEA.
  User: lizonglin
  Date: 2015/5/4
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>新建SVN</title>
    <link href="/css/task.css" rel="stylesheet" type="text/css"/>
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="branch_new" class="special-div">
                <div id="branch_new_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div class="col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3 col-lg-6 col-lg-offset-3">
                            <div class="half-color-header">
                                <h2>新建SVN</h2>
                            </div>
                            <table id="branch_new_table" class="table half-trans-green">
                                <thead>
                                <tr></tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th class="col-sm-2 col-md-2 col-lg-2">名称</th>
                                    <td>
                                        <div class="col-sm-9 col-md-9 col-lg-9">
                                            <input class="form-control" type="text" id="branch_new_name" placeholder="名称不允许重复" name="branch_new_name"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>URL</th>
                                    <td>
                                        <div class="col-sm-9 col-md-9 col-lg-9">
                                            <input class="form-control" type="text" id="branch_new_url" name="branch_new_url"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>群组</th>
                                    <td>
                                        <div class="col-sm-5 col-md-5 col-lg-5">
                                            <select id="branch_new_group_select"  class="form-control">
                                                <c:forEach var="group" items="${groups}">
                                                    <option value="${group[0]}"> ${group[0]} </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>过期时间</th>
                                    <td>
                                        <div class="col-sm-5 col-md-5 col-lg-5">
                                            <div class="input-group">
                                                <input id="branch_new_ed_input" class="form-control" type="text" readonly="readonly"/>
                                                <span id="branch_new_expireDate" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>类型</th>
                                    <td>
                                        <div class="col-sm-5 col-md-5 col-lg-5">
                                            <select id="branch_new_type_select" class="form-control">
                                                <c:forEach var="type" items="${taskTypeList}">
                                                    <c:choose>
                                                        <c:when test="${isAdmin}">
                                                            <option value="${type[0]}"> ${type[0]} </option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:if test="${type[0] != 'LOAD'}">
                                                                <option value="${type[0]}"> ${type[0]} </option>
                                                            </c:if>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <a id="branch_new_save_btn" class="btn btn-primary">保存</a>
                                        <a id="branch_new_cancel_btn" class="btn btn-default" onclick="window.history.back(-1)">取消</a>
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
<div class="modal fade" id="branch_edit_sending_modal" tabindex="-1" role="dialog"
     aria-labelledby="sendingModalLabel" aria-hidden="false" data-keyboard="false" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sendingModalLabel" >
                    新建SVN
                </h4>
            </div>
            <div id="sending-body" class="modal-body" style="text-align: center">
                <span>后台处理中......</span>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="branch_new_sent_modal" tabindex="-1" role="dialog"
     aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sentModalLabel" >
                    新建SVN
                </h4>
            </div>
            <div id="sent-body" class="modal-body" style="text-align: center">
                <span>处理成功</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<div class="modal fade" id="branch_new_error_modal" tabindex="-1" role="dialog"
     aria-labelledby="errorModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
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
<input  type="hidden" id="paramsInput" value="${params}" />
<script src="/js/branch_new.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/util.js"></script>
</body>
</html>
