/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#btn-login-auth').bind('click', function () {
        loginAuth();
    });
    $('#btn-login-submit').bind('click', function () {
        realLogin();
    });

    $('#login-group-select').on("change", function () {
        if ("None" !== $('#login-group-select').val()) {
            $('#group-a').addClass('hidden');
            $('#btn-login-submit').attr("disabled", false);
        } else {
            $('#group-a').removeClass('hidden');
            $('#btn-login-submit').attr("disabled", true);
        }
    });
});

function loginAuth() {
    var domainName = $('#login-apply-username').val();
    var password = $('#login-apply-password').val();
    if ('' === domainName && '' === password) {
        $('#login-error-a').html("用户名和密码为空");
    } else if ('' === domainName){
        $('#login-error-a').html("用户名为空");
    } else if ('' === password){
        $('#login-error-a').html("密码为空");
    } else {
        $('#login_sending_modal').modal('show');
        getUicTicket(domainName,password);
    }
}

function getUicTicket(username, password) {
    $.ajax({
        url: "http://192.168.14.125:8080/ucenter/api/User/Auth",
        dataType: "json",
        type: "POST",
        data: {
            'user': username,
            'password': password,
            'subsystem': "deploy"
        },
        success: function (data) {
            if (200 === data['code']) {
                var ticket = data['data']['ticket'];
                decodeUicTicket(ticket);
            } else {
                $('#login_sending_modal').modal('hide');
                $('#login-error-a').html("用户名或密码错误");
            }
        }
    });
}

function decodeUicTicket(ticket) {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: "http://192.168.14.125:8080/ucenter/api/User/decodeTicket",
        data: {
            'ticket': ticket,
            'subsystem': "deploy"
        },
        success: function (data) {
            $('#login_sending_modal').modal('hide');
            $('#login-error-a').html("");
            if (200 === data['code']) {
                var username = data['data']['username'];
                var realname = data['data']['realname'];
                var email = data['data']['email'];
                $('#login-username').val(username);
                $('#login-realname').val(realname);
                $('#login-email').val(email);
                authUser(username);
//                loadLoginInfo(username, realname, email, ext);
            } else {
                $('#login-error-a').html("用户名或密码错误");
            }
        }
    });
}

function authUser(domainName) {
    $.ajax({
            type: 'POST',
            url: '/login/auth',
            data: {
                'userName':domainName
            },
            success: function (response) {
                $('#group-a').addClass('hidden');
                $('#login-group-select').attr("disabled",true);
                $('#btn-login-submit').attr("disabled",true);
                if ("null" !== response) {
                    var lastGroup = response[1];
                    var isAdmin = response[3];
                    var userId = response[4];
                    $('#login-isadmin').val(isAdmin);
                    $('#login-userid').val(userId);
                    $('#login-group').val(lastGroup);
                    $('#login-group-select option').each(function () {
                        if (lastGroup === $(this).val()) {
                            $(this).attr("selected",true);
                        }
                    });
                    if (true === isAdmin) {
                        $('#btn-login-submit').attr("value","Admin Login");
                    } else {
                        $('#btn-login-submit').attr("value","User Login");
                    }
                    $('#login-group-select').attr("disabled",false);
                    $('#btn-login-submit').attr("disabled",false);
                } else {
                    $('#login-isadmin').val(false);
                    $('#login-userid').val(-1);
                    $('#btn-login-submit').attr("value","User Login");
                    if ("None" === $('#login-group-select').val()) {
                        $('#group-a').removeClass('hidden');
                    } else {
                        $('#btn-login-submit').attr("disabled", false);
                    }
                    $('#login-group-select').attr("disabled",false);
                    
//                    $('#login-group-select').attr("disabled",false);
//                    $('#btn-login-submit').attr("disabled",false);
                }
            }
        });
}

function realLogin() {
//    var userName = $('#login-username').val();
//    var realName = $('#login-realname').val();
//    var email = $('#login-email').val();
    $('#login-group').val($('#login-group-select').val());
    $('#login-form').submit();
    
}