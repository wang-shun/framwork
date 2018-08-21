/**
 * Created by lizonglin on 2015/6/3.
 */
$(document).ready(function() {
    $('#show_password').bind('click', function(){
        togglePassword($('#password'), $('#password_icon'));
    });

    $('#show_confirm_password').bind('click', function(){
        togglePassword($('#confirm_password'), $('#confirm_password_icon'));
    });
    $('#signup_btn').bind('click', function() {
        submitSignup();
    });
});

function submitSignup () {
    var username = $('#username').val().trim();
    var password = $('#password').val().trim();
    var confirm_password = $('#confirm_password').val().trim();
    var group = $('#group_select').val().trim();
    var email = $('#email').val().trim();
    $('.error-span').each(function() {
        $(this).addClass('hidden');
    });
    if (password == '' && username == '') {
        $('#username_empty').removeClass('hidden');
        $('#password_empty').removeClass('hidden');
        return;
    }
    if (username == '') {
        $('#username_empty').removeClass('hidden');
        return;
    }
    if (password == '') {
        $('#password_empty').removeClass('hidden');
        return;
    }
    if (!uniqueUsername(username) && (password !== confirm_password)) {
        $('#confirm_password_differ').removeClass('hidden');
        $('#username_unique').removeClass('hidden');
        return;
    }
    if (password !== confirm_password) {
        $('#confirm_password_differ').removeClass('hidden');
        return;
    }
    if (!uniqueUsername(username)) {
        $('#username_unique').removeClass('hidden');
        return;
    }
    if (group == 'None') {
        $('#group_empty').removeClass('hidden');
        return;
    }
    if (!email.contains('@yolo24.com')) {
        $('#email_empty').removeClass('hidden');
        return;
    }
    $('#signup_sending_modal').modal('show');
    $.ajax({
        "type": 'POST',
        "url": '/signup/submit',
        "data": {
            "username": username,
            "password": password,
            "group": group,
            "email": email
        },
        success: function (data) {
            $('#signup_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#signup_error_modal').modal('show');
            } else {
                $('#sent_span').empty();
                $('#sent_span').html("注册成功");
                $('#signup_sent_modal').modal('show');
            }
        }
    });
}

function uniqueUsername (username) {
    var unique = false;
    $.ajax({
        "type": 'GET',
        "url": '/signup/unique/'+username,
        "async": false,
        success: function(data) {
            if (data) {
                unique = true;
            }
        }
    });
    return unique;
}