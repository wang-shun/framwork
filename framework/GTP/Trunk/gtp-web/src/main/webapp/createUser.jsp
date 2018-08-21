<%-- 
    Document   : createUser
    Created on : 2015-1-26, 16:35:47
    Author     : Dangdang.Cao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New Account</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="/css/page.css">
        <jsp:include page="frame.jsp" flush="false"/>

    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div">
                        <div class="col-lg-8 col-md-9">
                            <form id="register_form" name="register_form" action="/account/registerSuccess" method="POST">
                                <div class="alert-warning">
                                    <h4>Create New Account</h4>
                                    <p>Create account use the form.</p>
                                    <p>Length of password must longer than 6 char(s).</p>
                                </div>
                                <div class="mar_left">
                                    <h3 class="h_css">Account infomation </h3>         
                                    <div class="col-lg-4 col-md-5">
                                        <div>
                                            <label class="control-label">User name</label>
                                        </div>
                                        <div>
                                            <input type="text" class="form-control" id="userName" placeholder="UserName">
                                        </div>
                                        <div>
                                            <label class="control-label">Email address</label>
                                        </div>
                                        <div>
                                            <input type="text" class="form-control" id="email" placeholder="Email address">
                                        </div>
                                        <div>
                                            <label class="control-label">Password</label>
                                        </div>
                                        <div>
                                            <input type="text" class="form-control" id="passwd" placeholder="Password">
                                        </div>
                                        <div>
                                            <label class="control-label">Confirm password</label>
                                        </div>
                                        <div>
                                            <input type="text" class="form-control" id="re_passwd" placeholder="Password">
                                        </div>
                                    </div>
                                    <div class="col-lg-12">
                                        <label>User Role:</label>
                                        <div class="">
                                           <select class="select_css" name="userRole" id="userRole">           <!--   角色从数据库里取值-->
                                                <option value="admin">admin</option>                    
                                                <option value="Tester">Tester</option>
                                            </select>

                                            <a class="btn btn-default btn-sm" id="createRole" name="createRole">New</a>

                                            <div id="role" name="role">
                                                <input class="hidden" type="text" id="new_role" name="new_role" size="15px" placeholder="New Role">
                                                <a class="btn btn-default btn-sm hidden" id="addRole" name="addRole" style="margin-left: 9px">Add</a>
                                                <span class="check_css hidden" id="error_tag">* Role name must not be empty!</span>
                                            </div>
                                        </div>

                                        <label>ProjectGroup:</label>

                                        <select class="select_css" name="userRole" id="userRole">
                                            <c:forEach var="group" items="${groups}">
                                                <option value="${group[0]}"><c:out value="${group[0]}" /></option>
                                            </c:forEach>
                                        </select>
                                        <br>
                                        <div>
                                            <input type="submit" class="btn btn-success" id="newUser" name="newUser" value="Register">
                                        </div>
                                    </div>
                                </div>
                        </form>
                    </div>
        </div>
            </div>
    </div>
    <script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/js/account.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
</body>
</html>

