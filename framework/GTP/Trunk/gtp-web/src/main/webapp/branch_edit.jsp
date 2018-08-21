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
    <title>编辑SVN ${svnBranch[0]}</title>
    <link href="/css/task.css" rel="stylesheet" type="text/css"/>
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="branch_edit" class="special-div">
                <div id="branch_edit_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div class="col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3 col-lg-6 col-lg-offset-3">
                            <div class="half-color-header">
                                <h2>编辑SVN ${svnBranch[0]}</h2>
                            </div>
                            <table id="branch_edit_table" class="table half-trans-green">
                                <thead>
                                <tr></tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th class="col-sm-2 col-md-2 col-lg-2">ID</th>
                                    <td>
                                        <div class="col-sm-9 col-md-9 col-lg-9">
                                            <span id="branch_edit_id_span">${svnBranch[0]}</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>名称</th>
                                    <td>
                                        <div class="col-sm-9 col-md-9 col-lg-9">
                                            <input class="form-control" type="text" id="branch_edit_name" placeholder="名称不允许重复" value="${svnBranch[1]}" name="branch_edit_name"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>URL</th>
                                    <td>
                                        <div class="col-sm-9 col-md-9 col-lg-9">
                                            <input class="form-control" type="text" id="branch_edit_url" value="${svnBranch[2]}" name="branch_edit_url"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>群组</th>
                                    <td>
                                        <div class="col-sm-5 col-md-5 col-lg-5">
                                            <select id="branch_edit_group_select" class="form-control">
                                                <c:forEach var="group" items="${groupList}">
                                                    <c:choose>
                                                        <c:when test="${svnBranch[4] == group[0]}">
                                                            <option value="${group[0]}" selected="selected"> ${group[0]} </option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${group[0]}"> ${group[0]} </option>
                                                        </c:otherwise>
                                                    </c:choose>
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
                                                <input id="branch_edit_ed_input" class="form-control" type="text" value="${svnBranch[4]}" readonly="readonly"/>
                                                <span id="branch_edit_expireDate" class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>类型</th>
                                    <td>
                                        <div class="col-sm-5 col-md-5 col-lg-5">
                                            <select id="branch_edit_type_select" class="form-control">
                                                <c:forEach var="type" items="${taskTypeList}" varStatus="status">
                                                    <c:choose>
                                                        <c:when test="${isAdmin}">
                                                            <c:choose>
                                                                <c:when test="${svnBranch[5] == type[0]}">
                                                                    <option value="${type[0]}" selected="selected"> ${type[0]} </option>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <option value="${type[0]}"> ${type[0]} </option>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:choose>
                                                                <c:when test="${svnBranch[5] == 'LOAD'}">
                                                                    <c:if test="${status.index == 0}">
                                                                        <option value="LOAD">LOAD</option>
                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:choose>
                                                                        <c:when test="${svnBranch[5] == type[0]}">
                                                                            <option value="${type[0]}" selected="selected"> ${type[0]} </option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:if test="${type[0] != 'LOAD'}">
                                                                                <option value="${type[0]}">${type[0]}</option>
                                                                            </c:if>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <a id="branch_edit_save_btn" class="btn btn-primary">保存</a>
                                        <a id="branch_edit_cancel_btn" class="btn btn-default" onclick="window.history.back(-1)">取消</a>
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
                    更新SVN
                </h4>
            </div>
            <div id="sending-body" class="modal-body" style="text-align: center">
                <span>后台处理中......</span>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div class="modal fade" id="branch_edit_sent_modal" tabindex="-1" role="dialog"
     aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sentModalLabel" >
                    更新SVN
                </h4>
            </div>
            <div id="sent-body" class="modal-body" style="text-align: center">
                <span>处理成功</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="branch_modal_back_btn" type="button" class="btn btn-primary">
                    返回列表
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<div class="modal fade" id="branch_edit_error_modal" tabindex="-1" role="dialog"
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
<script src="/js/util.js" type="text/javascript"></script>
<script src="/js/branch_edit.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
</body>
</html>
