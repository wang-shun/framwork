<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>N新建性能测试配置</title>
    <link href="/assets/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="main-div">
            <div id="loadconf_new" class="special-div">
                <div id="loadconf_new_table_div" class="container-fluid">
                    <div class="row-fluid">
                        <div class="col-md-8 col-md-offset-2">
                            <div class="half-color-header">
                                <h2>新建性能测试配置</h2>
                            </div>
                            <table class="table table-bordered half-trans-green">
                                <tbody>
                                <tr>
                                    <th>名称</th>
                                    <td>
                                        <div class="col-lg-10 col-md-10 col-sm-10">
                                            <input class="form-control" type="text" id="loadconf_new_name" value="" placeholder="性能测试配置名称"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>脚本Svn</th>
                                    <td>
                                        <div class="col-lg-10 col-md-10 col-sm-10">
                                            <div id="loadconf_scriptsvn_div_tpl" class="form-inline hidden">
                                                <input style="width:90%;" class="form-inline form-control script-svn-input" type="text" placeholder="脚本Svn"/>
                                                <a class="form-inline glyphicon glyphicon-remove remove_scriptsvn" href="javascript:void(0);" onclick="delDiv(this);"></a>
                                            </div>
                                            <a id="loadconf_scriptsvn_add" href='javascript:addScriptSvn();' title="添加脚本SVN">添加</a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Jmx内容</th>
                                    <td>
                                        <div class="col-lg-10 col-md-10 col-sm-10" id="loadconf_new_jmx_div">
                                            <textarea class="form-control" id="loadconf_new_jmxcontent"></textarea>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>场景名称</th>
                                    <td>
                                        <div class="col-lg-10 col-md-10 col-sm-10" id="loadconf_new_scene_div">
                                            <select class="form-control" id="loadconf_new_scene">
                                                <c:forEach var="scene" items="${sceneList}">
                                                    <option value="${scene}">${scene}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>依赖源码Svn</th>
                                    <td>
                                        <div class="col-lg-10 col-md-10 col-sm-10">
                                            <div id="loadconf_sourcesvn_div_tpl" class="form-inline hidden">
                                                <input style="width:90%;" class="form-inline form-control source-svn-input" type="text" placeholder="依赖源码Svn"/>
                                                <a class="form-inline glyphicon glyphicon-remove remove_scriptsvn" href="javascript:void(0);" onclick="delDiv(this);"></a>
                                            </div>
                                            <a id="loadconf_sourcesvn_add" href='javascript:addSourceSvn();' title="添加源码SVN">添加</a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>环境</th>
                                    <td>
                                        <div class="col-lg-4 col-md-4 col-sm-4">
                                            <select id="loadconf_new_env" class="form-control">
                                                <c:forEach var="env" items="${envList}">
                                                    <option value="${env}"> ${env} </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                                <tfoot>
                                <tr>
                                    <th colspan="2">
                                        <input type="button" class="btn btn-primary" value="保存" id="loadconf_new_save_btn"/>
                                        <input type="button" class="btn btn-default" onclick="window.location.href='/load/conf'" value="取消" />
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
    <div class="modal fade" id="loadconf_new_error_modal" tabindex="-1" role="dialog"
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
    <div class="modal fade" id="loadconf_new_sending_modal" tabindex="-1" role="dialog"
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
    <div class="modal fade" id="loadconf_new_sent_modal" tabindex="-1" role="dialog"
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
                    <span>保存性能测试配置成功</span>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal">关闭
                    </button>
                    <button id="loadconf_modal_back_btn" type="button" class="btn btn-primary" onclick="window.location.href='/load/conf'">
                        返回列表
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</div>
<script src="/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/loadConf_new.js"></script>
</body>
</html>