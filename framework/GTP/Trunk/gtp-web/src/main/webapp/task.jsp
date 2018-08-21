<%-- 
    Document   : task
    Created on : 2015-1-23, 14:11:08
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>任务</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css"/>
        <link rel="stylesheet" href="/css/task.css"/>
        <link rel="stylesheet" href="/css/page.css"/>
        <link rel="stylesheet" href="/css/tableFilter.css"/>
        <jsp:include page="frame.jsp" flush="true"/>
        
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="task" class="special-div">
                        <div id="task_filter_div" class="half-color-header filter_div" style="padding: 20px;">
                            <h2>任务</h2>
                            <table class="filter_table" id="task_filter_table" >
                                <tbody>
                                    <tr>
                                        <td colspan="6">
                                            <c:choose>
                                                <c:when test="${sessionScope.login}">
                                                    <div class="btn-group" style="margin-left: 0px;padding-left: 0px">
                                                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                            新建<span class="caret"></span>
                                                        </button>
                                                        <ul class="dropdown-menu">
                                                            <li><a href="/task/new/1" title="Create a new API-Type task">API任务</a></li>
                                                            <li role="separator" class="divider"></li>
                                                            <li><a href="/task/new/2" title="Create a new GUI-Type task">GUI任务</a></li>
                                                            <li role="separator" class="divider"></li>
                                                            <li><a href="/task/new/3" title="Create a new Load-Type task">Load任务</a></li>
                                                        </ul>
                                                    </div>
                                                    <%--<a href="/task/new/" id="openNew" class="btn btn-primary">Create New</a>--%>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="btn-group" style="margin-left: 0px;padding-left: 0px">
                                                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                            新建<span class="caret"></span>
                                                        </button>
                                                        <ul class="dropdown-menu">
                                                            <li><a href="javascript:intervalActiveLogin();" title="Create a new API-Type task">API任务</a></li>
                                                            <li role="separator" class="divider"></li>
                                                            <li><a href="javascript:intervalActiveLogin();" title="Create a new GUI-Type task">GUI任务</a></li>
                                                            <li role="separator" class="divider"></li>
                                                            <li><a href="javascript:intervalActiveLogin();" title="Create a new Load-Type task">Load任务</a></li>
                                                        </ul>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>

                                        </td>
                                        <td><label><span class="glyphicon glyphicon-filter"></span></label></td>
                                        <td align="left" id="filter_col3" data-column="3" class="filter-td">
                                            <div class="input-group form-inline" style="padding-left: 0px;margin-left: 0px;">
                                                <span class="input-group-addon">环境</span>
                                                <select class="column_filter form-control form-inline" id="col3_filter" >
                                                    <option value="None" selected="selected">All</option>
                                                    <c:forEach var="env" items="${envTypes}">
                                                        <option value="${env[0]}"> ${env[0]} </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <td align="left" id="filter_col4" data-column="4" class="filter-td">
                                            <div class="input-group form-inline" style="padding-left: 0px;margin-left: 0px;">
                                                <span class="input-group-addon">群组</span>
                                                <select class="column_filter form-control form-inline" id="col4_filter">
                                                    <option value="None" selected="selected">All</option>
                                                    <c:forEach var="group" items="${groups}">
                                                        <option value="${group[0]}"> ${group[0]} </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <td align="left" id="filter_col8" data-column="8" class="filter-td">
                                            <div class="input-group form-inline" style="padding-left: 0px;margin-left: 0px;">
                                                <span class="input-group-addon">类型</span>
                                                <select class="column_filter form-control form-inline" id="col8_filter">
                                                    <option value="None" selected="selected">All</option>
                                                    <c:forEach var="taskType" items="${taskTypes}">
                                                        <option value="${taskType[0]}"> ${taskType[0]} </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <td align="left" id="filter_col9" data-column="9" class="filter-td">
                                            <div class="input-group form-inline" style="padding-left: 0px;margin-left: 0px;">
                                                <span class="input-group-addon">状态</span>
                                                <select class="column_filter form-control form-inline" id="col9_filter" style="width: 350px">
                                                    <option value="None" selected="selected">All</option>
                                                    <c:forEach var="status" items="${taskStatus}">
                                                        <option value="${status[0]}"> ${status[0]} </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="task_table_div" class="half-trans-green table-div">
                            <table id="task_list" class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>选择</th>
                                        <th>ID</th>
                                        <th>名称</th>
                                        <th>环境</th>
                                        <th>群组</th>
                                        <th>机器</th>
                                        <th>调度</th>
                                        <th>上次运行时间</th>
                                        <th>类型</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="task_confirm_modal" tabindex="-1" role="dialog" 
             aria-labelledby="task_confirm" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="task_confirm">
                            删除确认
                        </h4>
                    </div>
                    <div id="task_confirm_body" class="modal-body" style="text-align: center">
                        <table class="table">
                            <tbody>
                            <tr>
                                    <th style="text-align: center">ID</th>
                                    <td><span id="task_modal_taskid"></span></td>
                                </tr>
                                <tr>
                                    <th style="text-align: center">名称</th>
                                    <td><span id="task_modal_taskname"></span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                        <button id="task_modal_confirm_btn" class="btn btn-danger" data-id="">删除</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <div class="modal fade" id="task_details_modal" tabindex="-1" role="dialog" aria-labelledby="task_details" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="task_details">
                            任务详情
                        </h4>
                    </div>
                    <div id="task_details_body" class="modal-body" style="text-align: center">
                        <table id="task_details_table" class="table">
                            <tbody>
                                <tr>
                                    <th>ID</th>
                                    <td><span class="task_details_table_span" id="task_details_taskid"></span></td>
                                </tr>
                                <tr>
                                    <th>名称</th>
                                    <td><span class="task_details_table_span" id="task_details_taskname"></span></td>
                                </tr>
                                <tr>
                                    <th>类型</th>
                                    <td><span class="task_details_table_span" id="task_details_tasktype"></span></td>
                                </tr>
                                <tr>
                                    <th>环境</th>
                                    <td><span class="task_details_table_span" id="task_details_envname"></span></td>
                                </tr>
                                <tr id="task_detail_browser_tr">
                                    <th>浏览器</th>
                                    <td>
                                        <span class="task_details_table_span" id="task_details_browser"></span>
                                    </td>
                                </tr>
                                <tr id="task_detail_loadconfname_tr">
                                    <th>性能测试配置</th>
                                    <td>
                                        <span class="task_details_table_span" id="task_detail_loadconfname"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <th>机器</th>
                                    <td><span class="task_details_table_span" id="task_details_machine"></span></td>
                                </tr>
                                <tr>
                                    <th>最后运行时间</th>
                                    <td><span class="task_details_table_span" id="task_details_lastruntime"></span></td>
                                </tr>
                                <tr>
                                    <th>所有人</th>
                                    <td><span class="task_details_table_span" id="task_details_taskstatus"></span></td>
                                </tr>
                                <tr>
                                    <th>最后编辑者</th>
                                    <td><span class="task_details_table_span" id="task_details_runtype"></span></td>
                                </tr>
                                <tr>
                                    <th>运行类型</th>
                                    <td><span class="task_details_table_span" id="task_details_owner"></span></td>
                                </tr>
                                <tr>
                                    <th>SVN</th>
                                    <td><span class="task_details_table_span" id="task_details_lastmender"></span></td>
                                </tr>
                                <tr>
                                    <th>Email列表</th>
                                    <td><span class="task_details_table_span" id="task_details_emaillist"></span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <div class="modal fade" id="task_error_modal" tabindex="-1" role="dialog" 
             aria-labelledby="task_error" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="task_error">
                            错误
                        </h4>
                    </div>
                    <div id="task_error_body" class="modal-body" style="text-align: center">
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
        <div class="modal fade" id="task_sending_modal" tabindex="-1" role="dialog" 
             aria-labelledby="task_sending" aria-hidden="false" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="text-align: center">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="task_sending" >
                        </h4>
                    </div>
                    <div id="task_sending_body" class="modal-body" style="text-align: center">
                        <span>后台处理中......</span>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <div class="modal fade" id="task_sent_modal" tabindex="-1" role="dialog" 
             aria-labelledby="task_sent" aria-hidden="false" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="text-align: center">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="task_sent" >
                        </h4>
                    </div>
                    <div id="task_sent_body" class="modal-body" style="text-align: center">
                        <span id="sent_span"></span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <input  type="hidden" id="paramsInput" value="${params}" />
        <script type="text/javascript" src="/js/map.js" ></script>
        <script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="/js/task.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
    </body>
</html>
