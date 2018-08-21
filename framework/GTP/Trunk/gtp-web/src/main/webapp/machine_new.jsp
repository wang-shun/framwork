<%-- 
    Document   : machine_edit
    Created on : 2015-2-4, 15:36:04
    Author     : Lin.Shi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>新建机器</title>
        <link rel="stylesheet" href="/css/task.css"/>
        <link href="/assets/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="machine_new" class="special-div">
                        <div id="machine_new_table_div" class="container-fluid">
                            <div class="row-fluid">
                                <div class="col-md-6 col-md-offset-3">
                                    <div class="half-color-header">
                                        <h2>新建机器</h2>
                                    </div>
                                    <table class="table table-bordered half-trans-green">
                                        <tbody>
                                            <tr>
                                                <th>名称</th>
                                                <td>
                                                    <div class="col-md-8">
                                                        <input class="form-control" type="text" id="machine_name" name="machine_name" value=""/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>IP</th>
                                                <td>
                                                    <div class="col-md-8">
                                                        <input class="form-control" type="text" id="machine_ip" name="machine_ip" value=""/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Jenkins标签</th>
                                                <td>
                                                    <div class="col-md-8">
                                                        <input class="form-control" type="text" id="machine_label" name="machine_label" value=""/>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>浏览器</th>
                                                <td>
                                                    <div class="col-md-8" id="av_browser_div">
                                                        <c:forEach var="browser" items="${Browsers}">
                                                            <input name="checkbox_browser" class="form-inline" type="checkbox" value="${browser}"/>
                                                            <span class="form-inline">${browser}</span><br>
                                                        </c:forEach>
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
                                                                    <option value="${env[0]}"> ${env[0]} </option>
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
                                                        <select id="machine_new_type_select" class="form-control">
                                                            <c:forEach var="type" items="${taskTypeList}">
                                                                <option value="${type[0]}"> ${type[0]} </option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>OS</th>
                                                <td>
                                                    <div class="col-sm-5 col-md-5 col-lg-5">
                                                        <select id="machine_new_os_select" class="form-control">
                                                            <c:forEach var="os" items="${OSList}">
                                                                <option value="${os[0]}"> ${os[0]} </option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>描述</th>
                                                <td>
                                                    <div class="col-md-8">
                                                        <input class="form-control" type="text" id="description" name="description" value=""/>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                                <th colspan="2">
                                                    <input type="button" class="btn btn-primary" value="保存" id="machine_new_save_btn"/>
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
                            <button id="machine_modal_back_btn" type="button" class="btn btn-primary" onclick="window.location.href='/machine'">
                                返回列表
                            </button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
        </div>
        <script src="/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="/js/machine_new.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
    </body>
</html>
