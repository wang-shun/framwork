<%-- 
    Document   : login
    Created on : 2015-2-12, 15:40:43
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/css/login.css" rel="stylesheet" type="text/css"/>
    <link href="/css/index.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
    <title>登录GTP</title>
</head>
<body>
<div id="login_main_div" class="container-fluid" style="height: 100%">
    <div class="row">
        <div class="main-div">
            <div class="alert half-color-black login-box">
                <h3 style="color:#FFFFFF">
                    <center>登录GTP</center>
                </h3>
                <div class="hd" style="margin-bottom: 15px"></div>
                <div id="login-input">
                    <div id="login-auth-div" class="row">
                        <table class="col-lg-12">
                            <tr>
                                <td>
                                    <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1">
                                        <span class="label-span">用户名</span>
                                        <input id="login-apply-username" class="form-control" type="text"
                                               name="login-apply-username" id="login-apply-username" value=""/>
                                        <span id="username-error"
                                              class="error-span hidden">该用户名不存在</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1">
                                        <span class="label-span">密码</span>

                                        <div class="input-group">
                                            <input id="login-apply-password" class="form-inline form-control"
                                                   type="password" name="login-apply-password"
                                                   id="login-apply-password" value=""/>
                                            <a href="#" class="input-group-addon" id="show_password"
                                               title="Show Password"><span><b id="password_icon"
                                                                              class="glyphicon glyphicon-eye-open"></b></span></a>
                                        </div>
                                        <span id="empty-error" class="error-span hidden">用户名或密码为空</span>
                                        <span id="password-error" class="error-span hidden">密码错误</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1">
                                        <input type="checkbox" id="login_apply_auto" checked/>
                                        <span class="label-span">记住用户</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1">
                                        <br><a id="btn-login-submit" class="btn btn-primary" type="button">登录</a>
                                        <a style="float: right" id="btn-signup-submit" class="btn btn-success"
                                           href="/signup" type="button" value="Signup">注册</a>
                                        <span id="login-error" class="error-span hidden">登录失败</span>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="login_error_modal" tabindex="-1" role="dialog"
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
<div class="modal fade" id="login_sending_modal" tabindex="-1" role="dialog"
     aria-labelledby="sendingModalLabel" aria-hidden="false" data-keyboard="false" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sendingModalLabel">

                </h4>
            </div>
            <div id="sending-body" class="modal-body" style="text-align: center">
                <span>身份认证中......</span>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<div class="modal fade" id="login_sent_modal" tabindex="-1" role="dialog"
     aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="text-align: center">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="sentModalLabel">
                    Update Success
                </h4>
            </div>
            <div id="sent-body" class="modal-body" style="text-align: center">
                <span>Updated Task Information Successfully!</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
                <button id="task_modal_back_btn" type="button" class="btn btn-primary"
                        onclick="window.location.href='/home'">
                    Home
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
</div>
<script type="text/javascript" src="/js/util.js"></script>
<script src="/js/login.js" type="text/javascript"></script>
</body>
</html>
