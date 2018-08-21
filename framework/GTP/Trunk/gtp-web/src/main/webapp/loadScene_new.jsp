<%--
    Document   : machine
    Created on : 2015-7-20, 14:54:12
    Author     : lizonglin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>新建性能测试场景</title>
    <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="loadscene_new" class="special-div">
                <div id="loadscene_new_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div class="col-md-8 col-md-offset-2">
                            <div class="half-color-header">
                                <h2>新建性能测试场景</h2>
                            </div>
                            <table id="loadscene_new_table" class="table table-bordered half-trans-green">
                                <tbody>
                                <tr>
                                    <th>场景名称</th>
                                    <td>
                                        <div class="col-md-9" id="loadscene_new_name_div">
                                            <input type="text" class="form-control" id="loadscene_new_name" placeholder="场景名称"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>OnSampleError</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_onerror_div">
                                            <select class="form-control" id="loadscene_new_onerror">
                                                <c:forEach var="error" items="${onErrorList}">
                                                    <option value="${error[0]}">${error[0]}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>ThreadNum</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_threadnum_div">
                                            <input type="text" class="form-control" id="loadscene_new_threadnum" placeholder="Thread Number"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>InitDelay</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_initdelay_div">
                                            <input type="text" class="form-control" id="loadscene_new_initdelay" placeholder="Initial Delay"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>StartCount</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_startcount_div">
                                            <input type="text" class="form-control" id="loadscene_new_startcount" placeholder="Start Count"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>StartCountBurst</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_startcountburst_div">
                                            <input type="text" class="form-control" id="loadscene_new_startcountburst" placeholder="Start Count Burst"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>StartPeriod</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_startperiod_div">
                                            <input type="text" class="form-control" id="loadscene_new_startperiod" placeholder="Start Period"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>StopCount</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_stopcount_div">
                                            <input type="text" class="form-control" id="loadscene_new_stopcount" placeholder="Stop Count"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>StopPeriod</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_stopperiod_div">
                                            <input type="text" class="form-control" id="loadscene_new_stopperiod" placeholder="Stop Period"/>
                                        </div>
                                    </td>
                                </tr><tr>
                                    <th>FlightTime</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_flighttime_div">
                                            <input type="text" class="form-control" id="loadscene_new_flighttime" placeholder="Flight Time"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>RampUp</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_rampup_div">
                                            <input type="text" class="form-control" id="loadscene_new_rampup" placeholder="Ramp Up"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>模板</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_istemplate_div">
                                            <c:choose>
                                                <c:when test="${isAdmin}">
                                                    <input type="checkbox" class="form-inline" id="loadscene_new_istemplate"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="checkbox" class="form-inline" id="loadscene_new_istemplate" disabled="disabled"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <span class="form-inline">是否为模板?</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>测试</th>
                                    <td>
                                        <div class="col-md-6" id="loadscene_new_istest_div">
                                            <c:choose>
                                                <c:when test="${isAdmin}">
                                                    <input type="checkbox" class="form-inline" id="loadscene_new_istest"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="checkbox" class="form-inline" id="loadscene_new_istest" disabled="disabled"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <span class="form-inline">是否为测试?</span>
                                        </div>
                                    </td>
                                </tr>

                                <tr>
                                    <td colspan="2">
                                        <a id="loadscene_new_save_btn" class="btn btn-primary">保存</a>
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
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="loadscene_new_error_modal" tabindex="-1" role="dialog"
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
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal -->
    </div>
    <div class="modal fade" id="loadscene_new_sending_modal" tabindex="-1" role="dialog"
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
                    <span>后台处理中......</span>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal -->
    </div>
    <div class="modal fade" id="loadscene_new_sent_modal" tabindex="-1" role="dialog"
         aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="text-align: center">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="sentModalLabel">
                    </h4>
                </div>
                <div id="sent-body" class="modal-body" style="text-align: center">
                    <span>新建性能测试场景成功</span>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal">关闭
                    </button>
                    <button id="loadscene_new_modal_back_btn" type="button" class="btn btn-primary"
                            onclick="window.location.href='/load/scene'">
                        返回列表
                    </button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal -->
    </div>
</div>
<script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/js/loadScene_new.js"></script>
</body>
</html>
