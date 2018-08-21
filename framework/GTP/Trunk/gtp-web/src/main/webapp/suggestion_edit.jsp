<%-- 
    Document   : suggestion_edit
    Created on : 2015-2-10, 17:13:54
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>编辑建议 ${suggestion.advicesId}</title>
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="suggestion_edit">
                        <div id="suggestion_edit_table_div" class="container-fluid">
                            <div class="row-fluid">
                                <div class="col-md-6 col-md-offset-3">
                                    <div class="half-color-header">
                                        <h2>编辑建议&nbsp;<span id="suggestion_edit_id_span">${suggestion.advicesId}</span></h2>
                                    </div>
                            <table id="suggestion_edit_table" class="table table-bordered half-trans-green">
                                <tbody>
                                    <tr>
                                        <th>主题</th>
                                        <td>
                                            <div class="col-md-9">
                                                <input class="form-control" type="text" id="suggestion_edit_name" name="suggestion_edit_name" value="${suggestion.name}"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>类型</th>
                                        <td>
                                            <div class="col-md-5" id="suggestion_edit_type_div">
                                                <select id="suggestion_edit_type_select" class="form-control">
                                                <c:forEach  var="type" items="${suTypes}">
                                                    <c:choose>
                                                        <c:when test="${type[1] == suggestion.advicetype}">
                                                            <option value="${type[1]}" selected="selected"> ${type[0]} </option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="${type[1]}"> ${type[0]} </option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>状态</th>
                                        <td>
                                            <div class="col-md-5">
                                                <c:choose>
                                                    <c:when test="${sessionScope.isAdmin}">
                                                        <select id="suggestion_edit_status_select" class="form-control">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <select id="suggestion_edit_status_select" class="form-control" disabled>
                                                    </c:otherwise>
                                                </c:choose>
                                                    <c:forEach var="status" items="${suStatus}">
                                                        <c:choose>
                                                            <c:when test="${status[1] == suggestion.status}">
                                                                <option value="${status[1]}" selected="selected">${status[0]}</option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${status[1]}">${status[0]}</option>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>内容</th>
                                        <td>
                                            <div class="col-md-9">
                                                <textarea class="form-control" type="text" id="suggestion_edit_content" name="suggestion_edit_name" style="width:100%; height:80px;">${suggestion.advice}</textarea>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <div class="col-md-12" style="text-align: center">
                                                <a id="suggestion_edit_save_btn" class="btn btn-primary">保存</a>
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
        <div class="modal fade" id="suggestion_edit_error_modal" tabindex="-1" role="dialog" 
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
        <div class="modal fade" id="suggestion_edit_sending_modal" tabindex="-1" role="dialog" 
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
        <div class="modal fade" id="suggestion_edit_sent_modal" tabindex="-1" role="dialog" 
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
                        <span>更新建议成功</span>
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
        <script src="/js/suggestion_edit.js" type="text/javascript"></script>
    </body>
</html>
