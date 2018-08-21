<%--
  Created by IntelliJ IDEA.
  User: lizonglin
  Date: 2015/6/4
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>设置用户信息</title>
    <link rel="stylesheet" href="/css/task.css"/>
    <link href="/assets/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="set_userinfo" class="special-div">
                <div id="set_userinfo_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div class="col-lg-6 col-lg-offset-3">
                            <div class="half-color-header">
                                <h2>设置用户信息</h2>
                            </div>
                            <table class="table table-bordered half-trans-green">
                                <tbody>
                                <tr>
                                    <th>用户名</th>
                                    <td>
                                        <div class="col-lg-8">
                                            <input class="form-control" type="text" id="username" value="${sessionScope.userName}" readonly="true"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>更改用户密码</th>
                                    <td>
                                        <span class="">(是否更改密码?)</span>
                                        <div class="col-lg-1">
                                            <input type="checkbox" id="change_password"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr id="password_tr" class="hidden">
                                    <th>新密码</th>
                                    <td>
                                        <div class="col-lg-8">
                                            <div class="input-group">
                                                <input id="password" class="form-inline form-control" type="password" id="password" placeholder="密码不能为空"/>
                                                <a href="#" class="input-group-addon"id="show_password" title="显示密码"><span ><b id="password_icon" class="glyphicon glyphicon-eye-open"></b></span></a>
                                            </div>
                                            <span class="hidden error-span" id="password_empty">密码不能为空</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr id="confirm_password_tr" class="hidden">
                                    <th>确认密码</th>
                                    <td>
                                        <div class="col-lg-8">
                                            <div class="input-group">
                                                <input id="confirm_password" class="form-inline form-control" type="password" id="confirm_password" placeholder="确认密码"/>
                                                <a href="#" class="input-group-addon"id="show_confirm_password" title="Show Password"><span ><b id="confirm_password_icon" class="glyphicon glyphicon-eye-open"></b></span></a>
                                            </div>
                                            <span class="hidden error-span" id="confirm_password_differ">两次输入密码不一致</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>群组</th>
                                    <td>
                                        <div class="col-lg-8">
                                            <select id="group_select"class="form-control">
                                                <option value="None" selected="selected">None</option>
                                                <c:forEach var="group" items="${groups}">
                                                    <c:if test="${'None' != group}">
                                                        <c:choose>
                                                            <c:when test="${group == sessionScope.group}">
                                                                <option value="${group}" selected="selected"> ${group} </option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${group}"> ${group} </option>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Email</th>
                                    <td>
                                        <div class="col-lg-8">
                                            <input class="form-control" type="text" id="email" value="${sessionScope.email}"/>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                                <tfoot>
                                <tr>
                                    <th colspan="2">
                                        <input type="button" class="btn btn-primary" value="保存" id="set_userinfo_save_btn"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="btn btn-default" onclick="window.history.back(-1)" value="取消" />
                                    </th>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="set_userinfo_error_modal" tabindex="-1" role="dialog"
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
    <div class="modal fade" id="set_userinfo_sending_modal" tabindex="-1" role="dialog"
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
    <div class="modal fade" id="set_userinfo_sent_modal" tabindex="-1" role="dialog"
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
                    <span>更新用户信息成功</span>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal">关闭
                    </button>
                    <button id="machine_modal_back_btn" type="button" class="btn btn-primary" onclick="window.location.href='/machine'">
                        返回列表
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</div>
<script src="/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/set_userinfo.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
</body>
</html>

