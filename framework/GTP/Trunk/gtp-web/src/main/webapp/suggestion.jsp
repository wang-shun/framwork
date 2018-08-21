<%-- 
    Document   : suggestion
    Created on : 2015-2-10, 10:29:05
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>GTP 建议</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css"/>
        <link href="/css/suggestion.css" rel="stylesheet" type="text/css"/>
        <jsp:include page="frame.jsp" flush="true"/>
        
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                    <div id="suggestion">
                        <div id="suggestion_header" class="half-color-header" style="padding: 20px">
                            <h2>GTP 建议</h2>
                            <div>
                                <a class="btn btn-primary" href="/suggestion/new">新建</a>
                            </div>
                        </div>
                        <div id="suggestion_table_div" class="half-trans-green table-div">
                            <table id="suggestion_table" class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>主题</th>
                                        <th>类型</th>
                                        <th>状态</th>
                                        <th>目标</th>
                                        <th>创建时间</th>
                                        <th>所有人</th>
                                        <th>建议</th>
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
        <div class="modal fade" id="suggestion_details_modal" tabindex="-1" role="dialog" 
             aria-labelledby="suggestion_details" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="suggestion_details">
                            建议详情
                        </h4>
                    </div>
                    <div id="suggestion_details_body" class="modal-body" style="text-align: center">
                        <table id="suggestion_details_table" class="table table-bordered">
                            <tbody>
                                <tr>
                                    <th>主题</th>
                                    <td><span class="suggestion_details_table_span" id="suggestion_details_title"></span></td>
                                </tr>
                                <tr>
                                    <th>状态</th>
                                    <td><span class="suggestion_details_table_span" id="suggestion_details_status"></span></td>
                                </tr>
                                <tr>
                                    <th>类型</th>
                                    <td><span class="suggestion_details_table_span" id="suggestion_details_type"></span></td>
                                </tr>
                                <tr>
                                    <th>内容</th>
                                    <td><span class="suggestion_details_table_span" id="suggestion_details_content"></span></td>
                                </tr>
                                <tr>
                                    <th>目标</th>
                                    <td><span class="suggestion_details_table_span" id="suggestion_details_assignTo"></span></td>
                                </tr>
                                <tr>
                                    <th>创建时间</th>
                                    <td><span class="suggestion_details_table_span" id="suggestion_details_createTime"></span></td>
                                </tr>
                                <tr>
                                    <th>所有人</th>
                                    <td><span class="suggestion_details_table_span" id="suggestion_details_owner"></span></td>
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
        <div class="modal fade" id="suggestion_error_modal" tabindex="-1" role="dialog" 
             aria-labelledby="suggestion_error" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" >
                            错误
                        </h4>
                    </div>
                    <div id="suggestion_error_body" class="modal-body" style="text-align: center">
                        
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" 
                                data-dismiss="modal">关闭
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <div class="modal fade" id="suggestion_sending_modal" tabindex="-1" role="dialog" 
             aria-labelledby="suggestion_sending" aria-hidden="false" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" style="text-align: center">
                        <button type="button" class="close" 
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="suggestion_sending" >
                        </h4>
                    </div>
                    <div id="suggestion_sending_body" class="modal-body" style="text-align: center">
                        <span>后台处理中......</span>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
        <script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
        <script src="/js/util.js" type="text/javascript"></script>
        <script type="text/javascript" src="/js/suggestion.js"></script>
    </body>
</html>
