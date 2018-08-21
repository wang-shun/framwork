<%-- 
    Document   : home
    Created on : 2015-2-12, 15:07:22
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>GTP 首页</title>
    <link href="/assets/jquery-datepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <link href="/css/home.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="frame.jsp" flush="true"/>

</head>
<body>
<div class="container">
    <%--<div class="row test-filter">--%>
        <div class="main-div" style="margin-bottom: 0px;padding-bottom: 0px;">
            <div id="home_div">
                <div id="jum-div" class="container">
                    <div class="jumbotron">
                        <h1>Gome Test Platform</h1>

                        <h3>API/GUI/LOAD...... all will be here !</h3>
                    </div>
                </div>
                <div class="container">
                    <div class="row sub-row">
                        <div class="col-sm-4 col-md-4 col-lg-4">
                            <div class="thumbnail">
                                <div class="caption" onclick="window.location.href='/task'">
                                            <h3>任务</h3>
                                        <p>&nbsp;&nbsp;每个测试工程需要配置成一个GTP任务</p>

                                        <p>&nbsp;&nbsp;SVN、调度规则、机器、Hosts等信息需要提前配置</p><br>

                                    <p><a class="btn btn-primary" href="/task">任务</a></p>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-md-4 col-lg-4">
                            <div class="thumbnail">
                                <div class="caption">
                                    <h3>SVN</h3>

                                    <p>&nbsp;&nbsp;每一条SVN地址需要配置为某测试工程的根目录</p>

                                    <p>&nbsp;&nbsp;测试工程的最新代码需要提前提交到SVN服务器</p><br>

                                    <p><a class="btn btn-primary" href="/branch">SVN</a></p>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-md-4 col-lg-4">
                            <div class="thumbnail">
                                <div class="caption">
                                    <h3>调度规则</h3>

                                    <p>&nbsp;&nbsp;测试任务应该按某种调度规则运行</p>

                                    <p>&nbsp;&nbsp;包括每天、每周、仅一次三种主要的调度规则</p><br>

                                    <p><a class="btn btn-primary" href="/regularRule">调度规则</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row sub-row">
                        <div class="col-sm-4 col-md-4 col-lg-4">
                            <div class="thumbnail">
                                <div class="caption">
                                    <h3>机器</h3>

                                    <p>&nbsp;&nbsp;每一个测试任务都必须分配到某个确定的机器上执行</p>

                                    <p>&nbsp;&nbsp;分配到的机器运行环境必须和该任务匹配</p><br>

                                    <p><a class="btn btn-primary" href="/machine">Machine</a></p>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-md-4 col-lg-4">
                            <div class="thumbnail">
                                <div class="caption">
                                    <h3>Hosts</h3>

                                    <p>&nbsp;&nbsp;每次执行测试任务之前机器的Hosts都会按要求重置</p>

                                    <p>&nbsp;&nbsp;每一个测试任务需要制定Hosts内容</p><br>

                                    <p><a class="btn btn-primary" href="/hosts">Hosts</a></p>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-md-4 col-lg-4">
                            <div class="thumbnail">
                                <div class="caption">
                                    <h3>GTP报告</h3>

                                    <p>&nbsp;&nbsp;测试结果绘制而成的图表</p>

                                    <p>&nbsp;&nbsp;用例数和通过率是两个主要统计类型</p><br>

                                    <p><a class="btn btn-primary" href="/gtp-report">GTP报告</a></p>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4 col-md-4 col-lg-4">
                            <div class="thumbnail">
                                <div class="caption">
                                    <h3>JMT报告</h3>

                                    <p>&nbsp;&nbsp;性能测试的详细统计图表</p><br><br>

                                    <p><a class="btn btn-primary" href="/gtp-report/jmt">JMT报告</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="border-bottom: 2px solid #000;margin-top: 10px;margin-bottom: 10px;"></div>
                <div class="container">
                    <div class="row">
                        <div style="text-align: center">建议您使用FireFox、Chrome，分辨率1280*800及以上设备浏览本网站，获得更好用户体验。</div><br>
                        <div style="text-align: center">&copy;2015-2016&nbsp; Gome-Test</div><br><br><br>
                    </div>
                </div>
            </div>

        <%--</div>--%>
    </div>
</div>
<script type="text/javascript" src="/js/util.js"></script>
</body>
</html>
