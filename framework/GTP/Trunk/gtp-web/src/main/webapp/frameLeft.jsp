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
    <link rel="stylesheet" href="/assets/jqTree-0.20.0/jqtree.css" />
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
</head>
<body style="padding-top: 50px;">
<nav class="navbar navbar-fixed-top navbar-inverse" role="navigation">
    <div class="container-fluid">
        <div id="frame_navbar_collapse" class="collapse navbar-collapse">


        </div>

    </div>
</nav>
<div class="main-div" style="margin-top:-2px; " >
    <div id="helper-tree-data" style="width: 350px;margin-left: 10px;">
    </div>
    </div>


<script type="text/javascript" src="/assets/jquery/jquery.min.js"></script>
<script type="text/javascript" src="/assets/jquery-cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/assets/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/assets/jqTree-0.20.0/tree.jquery.js">
</script>
<script type="text/javascript" src="/js/dirTree.js"></script>
<%--<script type="text/javascript" src="/js/frame.js"></script>--%>
</body>
</html>
