<%-- 
    Document   : host_edit
    Created on : 2015-2-11, 14:08:15
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="frame.jsp" flush="true"/>
        <title>新建 Hosts {host.id}</title>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="hosts_new" class="special-div">
                        <div id="hosts_new_table_div" class="container-fluid">
                            <div class="row-fluid">
                                <div class="col-md-8 col-md-offset-2">
                                    <div class="half-color-header">
                                        <h2>新建 Hosts</h2>
                                    </div>
                            <table id="hosts_new_table" class="table half-trans-green">
                                <tbody>
                                    <tr>
                                        <th>环境</th>
                                        <td>
                                            <div class="col-md-3" id="hosts_new_type_div">
                                                <select id="hosts_new_type_select" class="form-control">
                                                <c:forEach  var="env" items="${envs}">
                                                    <option value="${env[0]}"> ${env[0]} </option>
                                                </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>是否有效</th>
                                        <td>
                                            <div class="col-md-5">
                                                <input type="checkBox" class="form-inline" id="host_new_enable" name="host_new_enable"/>
                                                <span class="form-inline"><strong>有效?</strong></span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>内容</th>
                                        <td>
                                            <div class="col-md-9">
                                                <textarea class="form-control" type="text" id="hosts_new_content" name="hosts_new_name" rows="" ></textarea>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>编辑者</th>
                                        <td>
                                            <div class="col-md-3">
                                                <input type="text" class="form-control" id="hosts_new_editor" readonly="true" value="${sessionScope.userName}"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <a id="hosts_new_save_btn" class="btn btn-primary">保存</a>
                                            <a class="btn btn-default" onclick="window.history.back(-1)">取消</a>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </div>
        </div>
        <div class="modal fade" id="hosts_new_error_modal" tabindex="-1" role="dialog"
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
        <div class="modal fade" id="hosts_new_sending_modal" tabindex="-1" role="dialog"
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
        <div class="modal fade" id="hosts_new_sent_modal" tabindex="-1" role="dialog"
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
                        <span>新建Hosts成功!</span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                        <button id="hosts_modal_back_btn" type="button" class="btn btn-primary" onclick="window.location.href='/hosts'">
                                返回列表
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <script type="text/javascript" src="/js/util.js"></script>
        <script type="text/javascript" src="/js/host_new.js"></script>
    </body>
</html>
