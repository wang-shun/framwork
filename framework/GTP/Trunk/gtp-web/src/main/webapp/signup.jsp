<%-- 
    Document   : signup
    Created on : 2015-2-4, 15:36:04
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>注册</title>
        <link rel="stylesheet" href="/css/task.css"/>
        <link href="/assets/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="signup" class="special-div">
                        <div id="signup_table_div" class="container-fluid">
                            <div class="row-fluid">
                                <div class="col-lg-4 col-lg-offset-4 col-md-4 col-md-offset-4 col-sm-4 col-sm-offset-4">
                                    <div class="half-color-header">
                                        <h2>注册</h2>
                                    </div>
                                    <table class="table half-trans-green">
                                        <tbody>
                                            <tr>
                                                <th>用户名</th>
                                                <td>
                                                    <div class="col-lg-8 col-md-8 col-sm-8">
                                                        <input class="form-control" type="text" id="username" name="username" value="" placeholder="用户名不能为空"/>
                                                        <span class="hidden error-span" id="username_unique">该用户名已被占用</span>
                                                        <span class="hidden error-span" id="username_empty">用户名不能为空</span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>密码</th>
                                                <td>
                                                    <div class="col-lg-8 col-md-8 col-sm-8">
                                                        <div class="input-group">
                                                            <input class="form-control form-inline" type="password" id="password" name="password" value="" placeholder="密码不能为空"/>
                                                            <a href="#" class="input-group-addon"id="show_password" title="显示密码"><span ><b id="password_icon" class="glyphicon glyphicon-eye-open"></b></span></a>
                                                        </div>
                                                        <span class="hidden error-span" id="password_empty">密码不能为空</span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>确认密码</th>
                                                <td>
                                                    <div class="col-lg-8 col-md-8 col-sm-8">
                                                        <div class="input-group">
                                                            <input class="form-control" type="password" id="confirm_password" name="confirm_password" value="" placeholder="再次输入密码"/>
                                                            <a href="#" class="input-group-addon"id="show_confirm_password" title="Show Confirm Password"><span ><b id="confirm_password_icon" class="glyphicon glyphicon-eye-open"></b></span></a>
                                                        </div>
                                                        <span class="hidden error-span" id="confirm_password_differ">两次输入密码不一致</span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>群组</th>
                                                <td>
                                                    <div class="col-lg-8 col-md-8 col-sm-8" id="group_div">
                                                            <select id="group_select" name="group_select" class="form-control">
                                                                <option value="None" selected="selected">请选择</option>
                                                                <c:forEach var="group" items="${groups}">
                                                                    <option value="${group}">${group}</option>
                                                                </c:forEach>
                                                            </select>
                                                        <span class="hidden error-span" id="group_empty">请选择所属群组</span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Email</th>
                                                <td>
                                                    <div class="col-lg-8 col-md-8 col-sm-8">
                                                        <input class="form-control" type="text" id="email" name="email" placeholder="必须为 @yolo24.com 邮箱" value=""/>
                                                        <span class="hidden error-span" id="email_empty">请正确填写 @yolo24.com 邮箱地址</span>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                                <th colspan="2">
                                                    <input type="button" class="btn btn-primary" value="注册" id="signup_btn"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="btn btn-default" onclick="window.location.href = '/login'" value="取消" />
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
            <div class="modal fade" id="signup_error_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="signup_sending_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="signup_sent_modal" tabindex="-1" role="dialog"
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
                            <span id="sent_span">注册成功</span>
                        </div>
                        <div class="modal-footer">
                            <button id="machine_modal_back_btn" type="button" class="btn btn-primary" onclick="window.location.href='/login'">
                                登录
                            </button>
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal">关闭
                            </button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
        </div>
        <script src="/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="/js/signup.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
    </body>
</html>
