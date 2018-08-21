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
    <title>性能测试配置</title>
    <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css"/>
    <link rel="stylesheet" href="/css/task.css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="loadconf" class="special-div">
                <div id="loadconf_filter_div" class="half-color-header filter_div" style="padding: 20px">
                    <h2>性能测试配置</h2>
                    <table>
                        <tbody>
                        <tr>
                            <td>
                                <c:choose >
                                    <c:when test="${sessionScope.login}">
                                        <a id="loadconf_create_new" class="btn btn-primary" href="/load/conf/new">新建</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a id="loadconf_create_new" class="btn btn-primary" href='javascript:intervalActiveLogin();'>新建</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="table-div">
                    <table id="loadconf_list" class="table table-striped table-hover half-trans-green">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>名称</th>
                            <th>脚本Svn</th>
                            <th>Jmx内容</th>
                            <th>场景名称</th>
                            <th>依赖源码Svn</th>
                            <th>环境</th>
                            <th>最后更新时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="loadconf_del_detail_modal" tabindex="-1" role="dialog"
         aria-labelledby="loadconf_del_detail" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="loadconf_del_detail">
                        删除详情
                    </h4>
                </div>
                <div id="loadconf_del_detail_body" class="modal-body" style="text-align: center">
                    <table id="loadconf_del_detail_table" class="table table-bordered">
                        <tbody>
                        <tr>
                            <th>ID</th>
                            <td><span class="task_details_table_span" id="loadconf_del_detail_id"></span></td>
                        </tr>
                        <tr>
                            <th>名称</th>
                            <td>
                                <span class="task_details_table_span" id="loadconf_del_detail_name"></span>
                            </td>
                        </tr>
                        <tr>
                            <th>场景名称</th>
                            <td>
                                <span class="task_details_table_span" id="loadconf_del_detail_scenename"></span>
                            </td>
                        </tr>
                        <tr>
                            <th>环境</th>
                            <td>
                                <span class="task_details_table_span" id="loadconf_del_detail_env"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" onclick="delLoadConf(this)" id="loadconf_del_modal_btn"
                            data-dismiss="modal"
                            data-loadconf-id=""
                            data-loadconf-name="">
                        删除
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal -->
    </div>

    <div class="modal fade" id="loadconf_detail_modal" tabindex="-1" role="dialog"
         aria-labelledby="loadconf_del_detail" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="loadconf_detail">
                        性能测试配置详情
                    </h4>
                </div>
                <div id="loadconf_detail_body" class="modal-body" style="text-align: center">
                    <table id="loadconf_detail_table" class="table">
                        <tbody>
                        <tr>
                            <th>ID</th>
                            <td><span class="task_details_table_span" id="loadconf_detail_id"></span></td>
                        </tr>
                        <tr>
                            <th>名称</th>
                            <td>
                                <span class="task_details_table_span" id="loadconf_detail_name"></span>
                            </td>
                        </tr>
                        <tr>
                            <th>脚本Svn</th>
                            <td>
                                <textarea readonly='readonly' class="textarea-4line"
                                          id="loadconf_detail_scriptsvn"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <th>Jmx内容</th>
                            <td>
                                <textarea readonly="readonly" class="loadconf_del_detail_table_span textarea-4line"
                                          id="loadconf_detail_jmxcontent"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <th>场景名称</th>
                            <td>
                                <span class="task_details_table_span" id="loadconf_detail_scenename"></span>
                            </td>
                        </tr>
                        <tr>
                            <th>依赖源码Svn</th>
                            <td>
                                <textarea readonly='readonly' class="textarea-4line"
                                          id="loadconf_detail_sourcesvn"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <th>环境</th>
                            <td>
                                <span class="task_details_table_span" id="loadconf_detail_env"></span>
                            </td>
                        </tr>
                        <tr>
                            <th>最后更新时间</th>
                            <td>
                                <span class="task_details_table_span" id="loadconf_detail_lastupdatetime"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal -->
    </div>

    <div class="modal fade" id="loadconf_error_modal" tabindex="-1" role="dialog"
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
    <div class="modal fade" id="loadconf_sending_modal" tabindex="-1" role="dialog"
         aria-labelledby="sendingModalLabel" aria-hidden="false" data-keyboard="false" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div id="sending-body" class="modal-body" style="text-align: center">
                    <span>后台处理中......</span>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal -->
    </div>
    <div class="modal fade" id="loadconf_sent_modal" tabindex="-1" role="dialog"
         aria-labelledby="sentModalLabel" aria-hidden="false" data-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="text-align: center">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="sentModalLabel">
                        新建性能测试配置成功
                    </h4>
                </div>
                <div id="sent-body" class="modal-body" style="text-align: center">
                    <span>Created Load successfully!</span>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal">关闭
                    </button>
                    <button id="load_new_modal_back_btn" type="button" class="btn btn-primary"
                            onclick="window.location.href='/load'">
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
<script type="text/javascript" src="/js/loadConf.js"></script>
</body>
</html>
