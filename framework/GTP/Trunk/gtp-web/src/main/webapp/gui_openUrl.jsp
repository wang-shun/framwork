<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>GUI页面测试</title>
    <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css"/>
    <link href="/css/task.css" rel="stylesheet" type="text/css"/>
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="task_new" class="special-div">
                <div id="task_new_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div style="width: 80%; text-align: left; margin-left: 200px;">
                            <div class="half-color-header">
                                <h2>页面测试</h2>
                            </div>
                            <table id="open_url_table" class="table table-bordered half-trans-green"  >
                                <tbody>
                                <tr>
                                    <th>url</th>
                                    <td>
                                        <div class="col-xs-9 col-md-9 col-sm-9">
                                            <input class="form-control" type="text" style="width: 800px;" id="open_url_address" name="open_url_address"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>浏览器</th>
                                    <td>
                                        <div style="width: 100%;" class="col-xs-5 col-md-5 col-sm-5">

                                            <c:forEach var="browser" items="${browsers}">
                                                <c:if test="${'None' != browser[0]}">
                                               <span style=" margin-right: 5px;"><input
                                                       <c:if test="${'Edge' == browser[0] || 'IE10' == browser[0] ||'IE11' == browser[0] ||'IE6' == browser[0] ||'IE7' == browser[0] ||'IE8' == browser[0] ||'IE9' == browser[0]  ||'Ipad_Safari' == browser[0]}"> disabled="false"

                                               </c:if>
                                                       style=" margin-right: 3px;" id="chk${browser[0]}" type="checkbox" name="browser" value="${browser[0]}"><label for="chk${browser[0]}">${browser[0]}</label> </input></span>
                                                </c:if>
                                            </c:forEach>
                                    </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>操作人</th>
                                    <td>
                                        <input type="text"  class="form-control" id="editor" name="editor" value="${sessionScope.userName} (Email: ${sessionScope.email})" readonly="true"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <a id="task_new_save_btn" style="text-align: center;" href="javascript:saveOpenUrl();"  class="btn btn-primary">保存</a>
                                        <a id="task_new_cancel_btn" style="text-align: center;" href="javascript:cancel();" class="btn btn-default">取消</a>
                                    </td>
                                </tr>
                                </tbody>
                                <tfoot>
                                <tr></tr>
                                </tfoot>
                            </table>



                        </div>

                        <div class="half-color-header">
                            <h2>历史</h2>
                        </div>
                        <div id="task_table_div" class="half-trans-green table-div">
                            <table id="task_list" class="table table-striped table-hover">
                                <thead>
                                <tr>
                                    <th style="width: 10%;">ID</th>
                                    <th style="width: 10%;">名称</th>
                                    <th style="">URL</th>
                                    <th style="width: 15%;">上次运行时间</th>
                                    <th style="width: 10%;">状态</th>
                                    <th style="width: 15%;">操作</th>
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
    </div>
</div>

<div class="modal fade" id="task_new_sent_modal" tabindex="-1" role="dialog"
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
                <span>新建GUI任务成功</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<div class="modal fade" id="task_new_error_modal" tabindex="-1" role="dialog"
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
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <th style="text-align: center">ID</th>
                        <td><span id="task_modal_taskid"></span></td>
                    </tr>
                    <tr>
                        <th style="text-align: center">url</th>
                        <td><span id="task_modal_taskname"></span></td>
                    </tr>
                    <tr>
                        <th style="text-align: center">浏览器</th>
                        <td><span id="task_modal_brewname"></span></td>
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
                <table id="task_details_table" class="table table-bordered">
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
<script src="/js/util.js" type="text/javascript"></script>
<script src="/js/gui_openUrl.js" type="text/javascript"></script>
<script src="/assets/jquery-datepicker/jquery.datetimepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/map.js" ></script>
<script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
</body>
</html>