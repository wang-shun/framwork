<%-- 
    Document   : changePasswd
    Created on : 2015-1-29, 9:53:00
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
        <title>重置密码</title>
        <link rel="stylesheet" href="/assets/datatables/css/jquery.dataTables.min.css">
        <link rel="stylesheet" href="/css/page.css">
        <jsp:include page="frame.jsp" flush="true"/>

    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="main-div" class="special-div">
                    <div id="account">
                        <div class="col-lg-8 col-md-9">
                            <form id="register_form" name="register_form">
                                <div class="alert-warning">
                                    <h4>重置密码</h4>
                                    <p>Change password use the below form.</p>
                                    <p>Length of password must longer than 6 char(s).</p>
                                </div>
                                <div class="mar_left">
                                    <h3 class="h_css">Account infomation </h3>
                                   
                                    <div class="col-lg-4 col-md-5">
                                        <div>
                                            <label class="control-label">Old Password</label>
                                        </div>
                                        <div>
                                            <input type="text" class="form-control" id="oldPasswd" placeholder="Old Password">
                                        </div>
                                        <div>
                                            <label class="control-label">New Password</label>
                                        </div>
                                        <div>
                                            <input type="text" class="form-control" id="newPasswd" placeholder="New Password">
                                        </div>
                                        <div>
                                            <label class="control-label">Confirm password</label>
                                        </div>
                                        <div>
                                            <input type="text" class="form-control" id="re_passwd" placeholder="Password">
                                        </div>
                                    </div>
                                    
                                    
                                    <div class="col-lg-11">
                                        <br>
                                        <div>
                                            <input type="submit" class="btn btn-success" id="change" name="change" value="Change Password">
                                        </div>
                                    </div>

                                </div>
                        </div>

                        </form>
                    </div>

                </div>
            </div>
        </div>

    </div>
    <script type="text/javascript" src="/assets/datatables/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="/js/util.js"></script>
</body>
</html>

