<%-- 
    Document   : frame
    Created on : 2015-1-23, 13:47:59
    Author     : Zonglin.Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="/css/index.css"/>
        <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    </head>
    <body style="padding-top: 50px;">
        <nav class="navbar navbar-fixed-top navbar-inverse" role="navigation">
            <div class="container-fluid">
                    <div id="frame_navbar_collapse" class="collapse navbar-collapse">
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-home-label"><a class="navbar-brand navbar-left" href="/">GTP</a></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-left">
                            <li><a><span>&mid;</span></a></li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-home"><a id="homeA" href="#"><span class="glyphicon glyphicon-home"></span></a></li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-task"><a href="/task">任务</a></li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-branch"><a class="left_bar" href="/branch" >SVN</a></li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-regular"><a class="left_bar" href="/regularRule" >调度规则</a></li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-machine"><a href="/machine">机器</a></li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-host"><a class="left_bar" href="/hosts" >Hosts</a></li>
                        </ul>
                        <ul class="nav navbar-nav">
                                <li id="nav_load_li" class="dropdown nav-tab">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">性能测试<b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li id="navbar_tab_load"><a href="/load/conf" >配置</a></li>
                                        <li class="divider"></li>
                                        <li id="navbar_tab_scene"><a href="/load/scene" >场景</a></li>
                                    </ul>
                                </li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li id="nav_report_li" class="dropdown nav-tab">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">GTP报告<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li id="navbar-tab-report"><a href="/gtp-report" >API/GUI报告</a></li>
                                    <li class="divider"></li>
                                    <li id="navbar-tab-report-jmt"><a href="/gtp-report/jmt" >LOAD报告</a></li>
                                    <li class="divider"></li>
                                    <li id="navbar-tab-report-history-jmt"><a href="/gtp-report/jmthistory" >LOAD历史报告</a></li>
                                </ul>
                            </li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-openUrl"><a class="left_bar" href="/gui-pageTest" >Gui页面测试</a></li>
                        </ul>
                        <ul class="nav navbar-nav">
                            <li role="presentation" class="nav-tab" id="navbar-tab-mq"><a class="left_bar" href="javascript:openUrl('http://10.126.59.2:8077')" >GTP-Mock-MQ</a></li>
                        </ul>
                        <c:if test="${sessionScope.isAdmin}">
                            <ul class="nav navbar-nav">
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" title="Admin Operations">
                                        Admin<b class="caret"></b>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li><a href="/account">用户管理</a></li>
                                        <li class="divider"></li>
                                        <li><a href="/gtp-report/gen_report">生成报告</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </c:if>
                        <ul class="nav navbar-nav nav-divider">
                            <li><a><span>&mid;</span></a></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true"><span class="glyphicon glyphicon-list"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="/suggestion">建议</a></li>
                                    <li class="divider"></li>
                                    <li><a href="http://wiki.ds.gome.com.cn/pages/viewpage.action?pageId=3671695" target="_blank">帮助</a></li>
                                </ul>
                            </li>
                        </ul>
                        <c:if test="${!sessionScope.login}">
                            <%--<ul class="nav navbar-nav navbar-right">--%>
                                <%--<li class="dropdown">--%>
                                    <%--<a href="/signup">注册</a>--%>
                                <%--</li>--%>
                            <%--</ul>--%>
                            <ul class="nav navbar-nav navbar-right">
                                <li id="nav_login_li" class="dropdown">
                                    <a id="nav_login_a" href="javascript:cookieLogin();">登录</a>
                                </li>
                            </ul>
                        </c:if>
                        <c:if test="${sessionScope.login}">
                            <ul class="nav navbar-nav navbar-right">
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    <span class="glyphicon glyphicon-user"></span>&nbsp;${sessionScope.userName}<b class="caret"></b>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li><a href="/set_userinfo">${sessionScope.group}&nbsp;/&nbsp;<c:choose><c:when test="${sessionScope.isAdmin}"> Admin</c:when><c:otherwise> User</c:otherwise></c:choose></a></li>
                                        <li class="divider"></li>
                                        <li><a href="/set_userinfo">账号设置</a></li>
                                        <li class="divider"></li>
                                        <li><a href="/logout">退出</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </c:if>
                    </div>

            </div>
        </nav>
        <script type="text/javascript" src="/assets/jquery/jquery.min.js"></script>
        <script type="text/javascript" src="/assets/jquery-cookie/jquery.cookie.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
        <script type="text/javascript" src="/assets/bootstrap/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/js/frame.js"></script>
    </body>
</html>
