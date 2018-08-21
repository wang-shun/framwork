<%-- 
    Document   : branch
    Created on : 2015-1-31, 11:46:59
    Author     : Lin.Shi
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SVN</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="/css/machine.css" />
        <link rel="stylesheet" href="/css/tableFilter.css"/>
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="branch" class="special-div">
                        <div id="host_header_div" class="half-color-header filter_div" style="padding: 20px">
                            <h2>SVN地址</h2>
                            <table id="branch_filter_table" class="filter_table" style="width:300px;">
                                <tbody>
                                <tr>
                                    <td>
                                        <div>
                                            <c:choose >
                                                <c:when test="${sessionScope.login}">
                                                    <a id="openNew" class="back_css btn btn-primary" href="/branch/new/">新建</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a class="back_css btn btn-primary" href='javascript:intervalActiveLogin();'>新建</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                    <td><label><span class="glyphicon glyphicon-filter"></span></label></td>
                                    <td align="left" id="filter_col4" data-column="4">
                                        <div class="input-group form-inline">
                                            <span class="input-group-addon">群组</span>
                                            <select class="form-control input-sm column_filter" id="col3_filter" data-column="3" style="width: 100px;">
                                                <option value="None" selected="selected">None</option>
                                                <c:forEach var="ProGroups" items="${ProGroup}">
                                                    <option value="${ProGroups[0]}"> ${ProGroups[0]} </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="branch_table_div" class="half-trans-green table-div">
                            <table id="branch_list" class="table table-striped  table-hover half-trans-green">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>名称</th>
                                        <th>URL</th>
                                        <th>群组</th>
                                        <th>过期时间</th>
                                        <th>类型</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="branch_del_detail_modal" tabindex="-1" role="dialog" aria-labelledby="branch_del_detail" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="branch_del_detail">
                                删除详情
                            </h4>
                        </div>
                        <div id="branch_del_detail_body" class="modal-body" style="text-align: center">
                            <table id="branch_del_detail_table" class="table">
                                <tbody>
                                <tr>
                                    <th>ID</th>
                                    <td><span class="branch_del_detail_table_span" id="branch_del_detail_id"></span></td>
                                </tr>
                                <tr>
                                    <th>名称</th>
                                    <td><span class="branch_del_detail_table_span" id="branch_del_detail_name"></span></td>
                                </tr>
                                <tr>
                                    <th>URL</th>
                                    <td><span class="branch_del_detail_table_span" id="branch_del_detail_url"></span></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" id="branch_del_modal_btn" data-dismiss="modal" data-branch-id="">
                                删除
                            </button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
            <div class="modal fade" id="branch_detail_modal" tabindex="-1" role="dialog" aria-labelledby="branch_del_detail" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="branch_detail">
                                SVN详情
                            </h4>
                        </div>
                        <div id="branch_detail_body" class="modal-body">
                            <table id="branch_detail_table" class="table">
                                <tbody>
                                <tr>
                                    <th>ID</th>
                                    <td><span class="branch_detail_table_span modal-detail-span" id="branch_detail_id"></span></td>
                                </tr>
                                <tr>
                                    <th>名称</th>
                                    <td><span class="branch_detail_table_span modal-detail-span" id="branch_detail_name"></span></td>
                                </tr>
                                <tr>
                                    <th>URL</th>
                                    <td><span style="word-break: break-all;text-align: left" class="branch_detail_table_span" id="branch_detail_url"></span></td>
                                </tr>
                                <tr>
                                    <th>群组</th>
                                    <td><span class="branch_detail_table_span modal-detail-span" id="branch_detail_group"></span></td>
                                </tr>
                                <tr>
                                    <th>过期时间</th>
                                    <td><span class="branch_detail_table_span modal-detail-span" id="branch_detail_ed"></span></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
            <div class="modal fade" id="branch_error_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="branch_sending_modal" tabindex="-1" role="dialog"
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
            <div class="modal fade" id="branch_sent_modal" tabindex="-1" role="dialog"
                 aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header" style="text-align: center">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="sentModalLabel" >
                                处理成功
                            </h4>
                        </div>
                        <div id="sent-body" class="modal-body" style="text-align: center">
                            <span>Create New branch Successfully!</span>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal">关闭
                            </button>
                            <button id="branch_new_modal_back_btn" type="button" class="btn btn-primary" onclick="window.location.href='/branch'">
                                返回列表
                            </button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
        </div>
        <input  type="hidden" id="paramsInput" value="${params}" />
        <script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="/js/branch.js"></script>
    </body>
</html>
