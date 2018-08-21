<%-- 
    Document   : hosts
    Created on : 2015-1-30, 11:58:17
    Author     : Lin.Shi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hosts</title>
        <link rel="stylesheet" href="assets/datatables/css/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="css/hosts.css" />
        <jsp:include page="frame.jsp" flush="true"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="hosts" class="special-div">
                        <div id="host_header_div" class="half-color-header filter_div" style="padding: 20px">
                            <h2>Hosts</h2>
                            <table>
                                <tbody>
                                <tr>
                                    <td>
                                        <c:choose >
                                            <c:when test="${sessionScope.login}">
                                                <a id="host_create_new" class="btn btn-primary" href="/hosts/new">新建</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a id="host_create_new" class="btn btn-primary" href='javascript:intervalActiveLogin();'>新建</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="host_table_div" class="half-trans-green table-div" >
                            <table id="hosts_list" class="table table-striped table-hover half-trans-green">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>环境</th>
                                        <th>最后编辑者</th>
                                        <th>最后更新时间</th>
                                        <th>是否有效</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="hosts_detail_modal" tabindex="-1" role="dialog" aria-labelledby="hosts_detail" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="hosts_detail">
                            Hosts 详情
                        </h4>
                    </div>
                    <div id="hosts_detail_body" class="modal-body" style="text-align: left">
                        <table id="hosts_detail_table" class="table ">
                            <tbody>
                            <tr>
                                <th>ID</th>
                                <td><span class="hosts_detail_table_span" id="hosts_detail_id"></span></td>
                            </tr>
                            <tr>
                                <th>环境</th>
                                <td><span class="hosts_detail_table_span" id="hosts_detail_name"></span></td>
                            </tr>
                            <tr>
                                <th>Hosts内容</th>
                                <td><span class="hosts_detail_table_span" id="hosts_detail_content"></span></td>
                            </tr>
                            <tr>
                                <th>最后编辑者</th>
                                <td><span class="hosts_detail_table_span" id="hosts_detail_updateUser"></span></td>
                            </tr>
                            <tr>
                                <th>最后更新时间</th>
                                <td><span class="hosts_detail_table_span" id="hosts_detail_updateTime"></span></td>
                            </tr>
                            <tr>
                                <th>是否有效</th>
                                <td><span class="hosts_detail_table_span" id="hosts_detail_enable"></span></td>
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
        <div class="modal fade" id="hosts_delete_modal" tabindex="-1" role="dialog" aria-labelledby="hosts_delete" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="hosts_delete">
                            删除Hosts
                        </h4>
                    </div>
                    <div id="hosts_delete_body" class="modal-body" style="text-align: center">
                        <table id="hosts_delete_table" class="table">
                            <tbody>
                            <tr>
                                <th>ID</th>
                                <td><span class="hosts_delete_table_span" id="hosts_delete_id"></span></td>
                            </tr>
                            <tr>
                                <th>环境</th>
                                <td><span class="hosts_delete_table_span" id="hosts_delete_name"></span></td>
                            </tr>
                            <tr>
                                <th>Hosts内容</th>
                                <td><span class="hosts_delete_table_span" id="hosts_delete_content"></span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <a id="modal_delete_btn" type="button" class="btn btn-danger" data-id="" data-dismiss="modal">删除</a>
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <div class="modal fade" id="hosts_sending_modal" tabindex="-1" role="dialog"
             aria-labelledby="hosts_sending" aria-hidden="false" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="text-align: center">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="hosts_sending" >

                        </h4>
                    </div>
                    <div id="hosts_sending_body" class="modal-body" style="text-align: center">
                        <span>后台处理中......</span>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <div class="modal fade" id="hosts_error_modal" tabindex="-1" role="dialog"
             aria-labelledby="hosts_error" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="hosts_error">
                            错误
                        </h4>
                    </div>
                    <div id="hosts_error_body" class="modal-body" style="text-align: center">
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
        <script type="text/javascript" src="assets/datatables/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="js/hosts.js"></script>
        <script type="text/javascript" src="js/util.js"></script>
    </body>
</html>
