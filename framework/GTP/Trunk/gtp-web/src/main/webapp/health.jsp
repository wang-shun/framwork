<%--
  Created by IntelliJ IDEA.
  User: zhangjiadi
  Date: 16/7/14
  Time: 下午4:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/css/index.css"/>
</head>
<body>

<div>

    <table >
        <tr>
            <th>发送</th>
            <th>查询</th>
        </tr>
        <tr>
            <td>
                <div>

                    <textarea class="form-control" type="text" style="width: 600px;" id="text1"  rows="20" ></textarea>
                </div>
                <div>
                    <input type="button" value="执行" onclick="abc()" />
                </div>
            </td>
            <td>
                <div>

                    <textarea class="form-control" type="text" style="width: 600px;" id="text2"  rows="20" ></textarea>
                </div>
                <div>
                    <input type="button" value="查询" onclick="search()" />
                </div>
            </td>
        </tr>
    </table>

</div>





    <script type="text/javascript" src="/assets/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/js/health.js"></script>
</body>
</html>
