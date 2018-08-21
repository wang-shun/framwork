/**
 * Created by lizonglin on 2015/6/3.
 */
$(document).ready(function() {
    $('#btn-login-submit').bind('click', function(){
        checkUser();
    });

    $('#show_password').bind('click', function(){
        togglePassword($('#login-apply-password'), $('#password_icon'));
    });

    loadUserInfo();
});

$(document).keypress(function(e) {
    if(e.which == 13) {
        checkUser();
    }
});

function checkUser() {
    var username = $('#login-apply-username').val();
    var password = $('#login-apply-password').val();
    var rem = $('#login_apply_auto').is(':checked');
    $(".error-span").each(function(){
        $(this).addClass('hidden');
    });
    if (username == '' || password == '') {
        $('#empty-error').removeClass('hidden');
    } else {
        if(existUsername(username)) {
            if(validPassword(username, password)) {
                var lastPath = document.referrer;
                lastPath = lastPath.split("/").pop();
                if(rem == false) {
                    clearCookie();
                }
                var lasthref= window.parent.frames.length==0 ? '':  window.parent.frames["main"].location.href;
                lasthref=lasthref.split("/").pop();
                var url="";
                if (lastPath == "signup" || lastPath == "logout"||lastPath == ""||lasthref == "" ||lasthref == "signup" || lasthref == "logout"|| lasthref == "logout") {
                   url="/"

                }else if(lasthref=='login')
                {
                    url=document.referrer;
                }
                else {
                    url=window.parent.frames["main"].location.href;
                }

                if(window.parent.frames.length==0)
                {
                    window.location.href=url;
                }else
                {
                   window.parent.frames["main"].location.href=url;
                }
            } else {
                $('#password-error').removeClass('hidden');
            }
        } else {
            $('#username-error').removeClass('hidden');
        }
    }
}

function existUsername(username) {
    var exist = true;
    $.ajax({
        "type": 'GET',
        "url": '/signup/unique/'+username,
        "async": false,
        success: function(data) {
            if (data) {
                exist = false;
            }
        }
    });
    return exist;
}

function validPassword(username, password) {
    var valid = false;
    var cookietime = new Date();
    $.ajax({
        "type": 'POST',
        "url": '/login/valid',
        "async": false,
        "data": {
            "username": username,
            "password": password
        },
        success: function(data) {
            if (data) {
                valid = true;
                cookietime.setTime(cookietime.getTime() + (90 * 24 * 60 * 60 * 1000));//coockie保存90天
                $.cookie("gtp-login-username",username,{expires:cookietime});
                $.cookie("gtp-login-password",password,{expires:cookietime});
            }
        }
    });
    return valid;
}

function loadUserInfo () {
    var username = $.cookie("gtp-login-username");
    var password = $.cookie("gtp-login-password");

    if (username !== 'null' && password !== 'null' && username != null && password !== null) {
        $('#login-apply-username').val(username);
        $('#login-apply-password').val(password);
    } else {
        $('#login-apply-password').empty();
        $('#login-apply-username').empty().focus();
    }
}

function clearCookie () {
    $.cookie("gtp-login-username",'',{expires:-1});
    $.cookie("gtp-login-password",'',{expires:-1});
}