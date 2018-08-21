<%-- 
    Document   : suggestion_new
    Created on : 2015-2-10, 17:13:54
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>新建建议</title>
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="suggestion_new">
                        <div id="suggestion_new_table_div" class="container-fluid">
                            <div class="row-fluid">
                                <div class="col-md-6 col-md-offset-3">
                                    <div class="half-color-header">
                                        <h2>新建建议</h2>
                                    </div>
                            <table id="suggestion_new_table" class="table table-bordered half-trans-green">
                                <thead>
                                    <tr>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <th>主题</th>
                                        <td>
                                            <div class="col-md-9">
                                                <input class="form-control" type="text" id="suggestion_new_name" name="suggestion_new_name" value=""/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>类型</th>
                                        <td>
                                            <div class="col-md-5" id="suggestion_new_type_div">
                                                <select id="suggestion_new_type_select" class="form-control">
                                                <c:forEach  var="type" items="${suTypes}">
                                                    <option value="${type[1]}">${type[0]}</option>
                                                </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>状态</th>
                                        <td>
                                            <div class="col-md-5">
                                                <select id="suggestion_new_status_select" class="form-control" disabled>
                                                    <c:forEach var="status" items="${suStatus}">
                                                        <option value="${status[1]}">${status[0]}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>内容</th>
                                        <td>
                                            <div class="col-md-9">
                                                <textarea class="form-control" type="text" id="suggestion_new_content" name="suggestion_new_name" style="width:100%; height:80px;"></textarea>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <div class="col-md-12" style="text-align: center">
                                                <a id="suggestion_new_save_btn" class="btn btn-primary" title="Sent to tech-test-arch@yolo24.com">保存</a>
                                            </div>
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
        <div class="modal fade" id="suggestion_new_error_modal" tabindex="-1" role="dialog" 
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
        <div class="modal fade" id="suggestion_new_sending_modal" tabindex="-1" role="dialog" 
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
        <div class="modal fade" id="suggestion_new_sent_modal" tabindex="-1" role="dialog" 
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
                        <span>新建建议成功</span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                        <button id="suggestion_modal_back_btn" type="button" class="btn btn-primary" onclick="javascript:window.location.href='/suggestion';">
                                返回列表
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <script src="/js/suggestion_new.js" type="text/javascript"></script>
    </body>
</html>
