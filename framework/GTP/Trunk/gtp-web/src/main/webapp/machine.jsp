<%-- 
    Document   : machine
    Created on : 2015-1-26, 18:54:12
    Author     : Lin.Shi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>机器</title>
        <link rel="stylesheet" href="assets/datatables/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="css/machine.css" />
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="machine" class="special-div">
                        <div id="machine_filter_div" class="half-color-header filter_div" style="padding: 20px">
                            <h2>机器</h2>
                            <table>
                                <tbody>
                                    <tr>
                                        <td>
                                            <c:if test="${sessionScope.isAdmin}">
                                                <a id="machine_create_new" class="btn btn-primary" href="/machine/new">新建</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="table-div">
                            <table id="machine_list" class="table table-striped table-hover half-trans-green">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>名称</th>
                                        <th>IP</th>
                                        <th>Jenkins标签</th>
                                        <th>浏览器</th>
                                        <th>环境</th>
                                        <th>任务类型</th>
                                        <th>OS</th>
                                        <th>描述</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 模态框（Modal） -->
            <div class="modal fade" id="machine_del_detail_modal" tabindex="-1" role="dialog" aria-labelledby="machine_del_detail" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="machine_del_detail">
                                机器删除详情
                            </h4>
                        </div>
                        <div id="machine_del_detail_body" class="modal-body" style="text-align: center">
                            <table id="machine_del_detail_table" class="table table-bordered">
                                <tbody>
                                    <tr>
                                        <th>ID</th>
                                        <td><span class="machine_del_detail_table_span" id="machine_del_detail_id"></span></td>
                                    </tr>
                                    <tr>
                                        <th>名称</th>
                                        <td><span class="machine_del_detail_table_span" id="machine_del_detail_name"></span></td>
                                    </tr>
                                    <tr>
                                        <th>IP</th>
                                        <td><span class="machine_del_detail_table_span" id="machine_del_detail_ip"></span></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" id="machine_del_modal_btn" data-dismiss="modal" data-machine-id="">
                                删除
                            </button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
            <div class="modal fade" id="machine_new_error_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="machine_new_sending_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="machine_new_sent_modal" tabindex="-1" role="dialog"
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
                            <span>新建机器成功</span>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal">关闭
                            </button>
                            <button id="machine_new_modal_back_btn" type="button" class="btn btn-primary" onclick="window.location.href='/machine'">
                                返回列表
                            </button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
        </div>
        <script type="text/javascript" src="assets/datatables/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="js/machine.js"></script>
    </body>
</html>
