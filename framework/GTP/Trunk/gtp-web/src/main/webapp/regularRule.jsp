<%-- 
    Document   : RegularRole
    Created on : 2015-2-3, 15:57:29
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>调度规则</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="/css/task.css"/>
        <link rel="stylesheet" href="/css/page.css"/>
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="regular_rule" class="special-div">
                        <div id="table_header_div" class="half-color-header filter_div" style="padding: 20px">
                            <h2>调度规则</h2>
                            <table>
                                <tbody>
                                <tr>
                                    <td>
                                        <c:choose >
                                            <c:when test="${sessionScope.login}">
                                                <a class="btn btn-primary" href="/regularRule/new">新建</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="btn btn-primary" href='javascript:intervalActiveLogin();'>新建</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="table_content_div" class="table-div half-trans-green">
                            <table id="regular_rule_list" class="table table-striped table-hover half-trans-green">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>名称</th>
                                        <th>调度类型</th>
                                        <th>星期</th>
                                        <th>时间</th>
                                        <th>可选</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="modal fade" id="regular_confirm_modal" tabindex="-1" role="dialog" 
             aria-labelledby="task_confirm" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="task_confirm">
                            Delete RegularRule Confirm
                        </h4>
                    </div>
                    <div id="regular_confirm_body" class="modal-body" style="text-align: center">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th style="text-align: center">RegularID</th>
                                    <th style="text-align: center">RegularName</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><span id="regular_modal_id"></span></td> 
                                    <td><span id="regular_modal_name"></span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                        <button id="regular_modal_confirm_btn" class="btn btn-primary" data-id="">删除</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>

        <div class="modal fade" id="regular_del_modal" tabindex="-1" role="dialog" 
             aria-labelledby="task_sent" aria-hidden="false" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="text-align: center">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="task_sent" >
                            删除成功
                        </h4>
                    </div>
                    <div id="regular_sent_body" class="modal-body" style="text-align: center">
                        <span>删除成功</span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>

        <div class="modal fade" id="regular_error_modal" tabindex="-1" role="dialog" 
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
                    <div id="regular-error-body" class="modal-body" style="text-align: center">
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
        <script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="/js/regularRule.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
    </body>
</html>
