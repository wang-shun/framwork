<%--
    Document   : machine
    Created on : 2015-7-20, 14:54:12
    Author     : lizonglin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>性能测试场景</title>
    <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css" />
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="load" class="special-div">
                <div id="load_filter_div" class="half-color-header filter_div" style="padding: 20px">
                    <h2>性能测试场景</h2>
                    <table class="filter_table" style="width:300px;">
                        <tbody>
                        <tr>
                            <td>
                                <c:choose >
                                    <c:when test="${sessionScope.login}">
                                        <a id="loadscene_create_new" class="btn btn-primary" href="/load/scene/new">新建</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a id="loadscene_create_new" class="btn btn-primary" href='javascript:intervalActiveLogin();'>新建</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><label><span class="glyphicon glyphicon-filter"></span></label></td>
                            <td align="left" id="filter_col4" data-column="4">
                                <div class="input-group form-inline" style="margin-left: 5px;">
                                    <span class="input-group-addon">模板</span>
                                    <select class="form-control input-sm column_filter" id="drop_Template" data-column="3" style="width: 100px;">
                                        <option value="None" selected="selected">全部</option>
                                        <option value="1" >模板</option>
                                        <option value="0" >非模板</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="table-div">
                    <table id="loadscene_list" class="table table-striped table-hover half-trans-green">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>名称</th>
                            <th>OnSampleError</th>
                            <th>ThreadNum</th>
                            <th>InitDelay</th>
                            <th>StartCount</th>
                            <th>StartCountBurst</th>
                            <th>StartPeriod</th>
                            <th>StopCount</th>
                            <th>StopPeriod</th>
                            <th>FlightTime</th>
                            <th>RampUp</th>
                            <th>更新时间</th>
                            <th>模板</th>
                            <th>测试</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="loadscene_sending_modal" tabindex="-1" role="dialog"
         aria-labelledby="sendingModalLabel" aria-hidden="false" data-keyboard="false" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div id="sending-body" class="modal-body" style="text-align: center">
                    <span>后台处理中......</span>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

    <div class="modal fade" id="loadscene_error_modal" tabindex="-1" role="dialog"
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

    <div class="modal fade" id="loadscene_del_detail_modal" tabindex="-1" role="dialog" aria-labelledby="loadscene_del_detail" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="load_del_detail">
                        删除详情
                    </h4>
                </div>
                <div id="loadscene_del_detail_body" class="modal-body" style="text-align: center">
                    <table id="loadscene_del_detail_table" class="table table-bordered">
                        <tbody>
                        <tr>
                            <th>ID</th>
                            <td><span class="loadscene_del_detail_table_span" id="loadscene_del_detail_id"></span></td>
                        </tr>
                        <tr>
                            <th>名称</th>
                            <td><span class="loadscene_del_detail_table_span" id="loadscene_del_detail_name"></span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" id="loadscene_del_modal_btn" data-dismiss="modal" data-load-id="">
                        删除
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</div>
<script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/js/loadScene.js"></script>
</body>
</html>
