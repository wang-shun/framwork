<%-- 
    Document   : account
    Created on : 2015-1-26, 15:22:00
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
         <link rel="stylesheet" href="/css/page.css">
         <link rel="stylesheet" href="/css/index.css">
        <jsp:include page="frame.jsp" flush="true"/>
        
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="account" class="special-div">
                        <div class="half-color-header filter_div">
                            <h2>用户管理</h2>
                            <table class="filter_table">
                                <tr>
                                    <td>
                                        <div>
                                            <a id="userNew" class="back_css btn btn-primary" href="/signup">添加用户</a>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <table id="account_list" class="table table-striped  table-hover half-trans-green">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>用户名</th>
                                    <th>Email</th>
                                    <th>管理员</th>
                                    <th>群组</th>
                                    <th>最后登录时间</th>
                                    <th>更改密码时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
            <!-- 模态框（Modal） -->
            <div class="modal fade" id="account_error_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="account_sending_modal" tabindex="-1" role="dialog"
                 aria-labelledby="sendingModalLabel" aria-hidden="false" data-keyboard="false" data-backdrop="static">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" style="text-align: center">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="sendingModalLabel" >
                                Processing
                            </h4>
                        </div>
                        <div id="sending-body" class="modal-body" style="text-align: center">
                            <span>后台处理中......</span>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
            <div class="modal fade" id="account_sent_modal" tabindex="-1" role="dialog"
                 aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" style="text-align: center">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="sentModalLabel" >
                                处理完成
                            </h4>
                        </div>
                        <div id="sent-body" class="modal-body" style="text-align: center">
                            <span id="sent_span"></span>
                        </div>
                        <div class="modal-footer">
                            <button id="account_modal_back_btn" type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.location.reload()">
                                返回列表
                            </button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
            <div class="modal fade" id="account_reset_modal" tabindex="-1" role="dialog"
                 aria-labelledby="resetModalLabel" aria-hidden="false" data-keyboard="false">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" style="text-align: center">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="resetModalLabel" >
                                重置密码
                            </h4>
                        </div>
                        <div id="reset-body" class="modal-body" style="text-align: center">
                            <div class="input-group col-lg-8 col-lg-offset-2">
                                <input class="form-control form-inline" type="password" id="reset_password" data-id="" placeholder="新密码不能为空"/>
                                <a href="#" class="input-group-addon"id="show_reset_password" title="Show Password"><span ><b id="reset_password_icon" class="glyphicon glyphicon-eye-open"></b></span></a>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button id="reset_modal_btn" type="button" data-dismiss="modal" class="btn btn-primary">
                                重置
                            </button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
        </div>
        <script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
        <script type="text/javascript" src="/js/account.js"></script>
    </body>
</html>

