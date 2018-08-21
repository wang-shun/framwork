<%-- 
    Document   : machine_edit
    Created on : 2015-2-4, 15:36:04
    Author     : Lin.Shi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>编辑机器</title>
        <link rel="stylesheet" href="/css/task.css"/>
        <link href="/assets/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="machine_edit" class="special-div">
                        <div id="machine_edit_table_div" class="container-fluid">
                            <div class="row-fluid">
                                <div class="col-md-6 col-md-offset-3">
                                    <div class="half-color-header">
                                        <h2>编辑机器 <span id="machine_edit_id_span">${machine[0]}</span></h2>
                                    </div>
                                    <table class="table table-bordered half-trans-green">
                                        <tbody>
                                            <tr>
                                                <th>名称</th>
                                                <td>
                                                    <div class="col-md-8">
                                                        <input class="form-control" type="text" id="machine_name" name="machine_name" value="${machine[1]}"/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>IP</th>
                                                <td>
                                                    <div class="col-md-8">
                                                        <input class="form-control" type="text" id="machine_ip" name="machine_ip" value="${machine[2]}"/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Jenkins标签</th>
                                                <td>
                                                    <div class="col-md-8">
                                                        <input class="form-control" type="text" id="machine_label" name="machine_label" value="${machine[6]}"/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>浏览器</th>
                                                <td>
                                                    <div class="col-md-8" id="av_browser_div">
                                                        <input id="browser_value" value="${machine[3]}" class="hidden">
                                                        <c:forEach var="browser" items="${Browsers}" varStatus="status">
                                                            <c:choose>
                                                                <c:when test="${fn:contains(selectedBrowsers, browser)}">
                                                                    <input name="checkbox_browser" class="form-inline" type="checkbox" value="${browser}" checked="checked"/>
                                                                    <span class="form-inline">${browser}</span><br>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input name="checkbox_browser" class="form-inline" type="checkbox" value="${browser}"/>
                                                                    <span class="form-inline">${browser}</span><br>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                        <%--<c:forEach  var="br" items="${Browsers}">--%>
                                                            <%--<c:if test="${'None' != br[0]}">--%>
                                                                <%--<input type="checkbox" class="browser-checkbox" name="checkbox_browser" id="checkbox_browser_${br[0]}" value="${br[0]}" /><label>${br[0]}</label><br />--%>
                                                            <%--</c:if>--%>
                                                        <%--</c:forEach>--%>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>
                                                    <label>环境</label>
                                                </th>
                                                <td>
                                                    <div class="col-sm-5 col-md-5 col-lg-5">
                                                        <select id="machine_env_select" name="machine_env_select" class="form-control">
                                                            <option value="None" selected="selected">None</option>
                                                            <c:forEach var="env" items="${Environment}">
                                                                <c:if test="${'None' != env[0]}">
                                                                    <c:choose>
                                                                        <c:when test="${env[0] == machine[4]}">
                                                                            <option value="${env[0]}" selected="selected"> ${env[0]} </option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${env[0]}"> ${env[0]} </option>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:if>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>任务类型</th>
                                                <td>
                                                    <div class="col-sm-5 col-md-5 col-lg-5">
                                                        <select id="machine_edit_type_select" class="form-control">
                                                            <c:forEach var="type" items="${taskTypeList}">
                                                                <c:choose>
                                                                    <c:when test="${machine[7] == type[0]}">
                                                                        <option value="${type[0]}" selected="selected"> ${type[0]} </option>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <option value="${type[0]}"> ${type[0]} </option>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>OS</th>
                                                <td>
                                                    <div class="col-sm-5 col-md-5 col-lg-5">
                                                        <select id="machine_edit_os_select" class="form-control">
                                                            <c:forEach var="os" items="${OSList}">
                                                                <c:choose>
                                                                    <c:when test="${machine[8] == os[0]}">
                                                                        <option value="${os[0]}" selected="selected"> ${os[0]} </option>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <option value="${os[0]}"> ${os[0]} </option>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>描述</th>
                                                <td>
                                                    <div class="col-md-8">
                                                        <input class="form-control" type="text" id="description" name="description" value="${machine[5]}"/>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                                <th colspan="2">
                                                    <input type="button" class="btn btn-primary" value="保存" id="machine_edit_save_btn"/>
                                                    <input type="button" class="btn btn-default" onclick="window.location.href = '/machine'" value="取消" />
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
            <div class="modal fade" id="machine_edit_error_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="machine_edit_sending_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="machine_edit_sent_modal" tabindex="-1" role="dialog"
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
                            <span>更新机器成功</span>
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
        <script type="text/javascript" src="/js/machine_edit.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
    </body>
</html>
