/**
 * Created by lizonglin on 2015/6/4.
 */
$(document).ready(function() {
    $('#show_password').bind('click', function(){
        togglePassword($('#password'), $('#password_icon'));
    });

    $('#show_confirm_password').bind('click', function(){
        togglePassword($('#confirm_password'), $('#confirm_password_icon'));
    });

    $('#set_userinfo_save_btn').bind('click', function(){
        clearError();
        updateUserInfo();
    });

    $('#change_password').bind('click',function() {
        clearError();
        $('#password_tr').toggleClass('hidden');
        $('#confirm_password_tr').toggleClass('hidden');
    });
});

function updateUserInfo() {
    var username = $('#username').val();
    var password = $('#password').val();
    var confirm_password = $('#confirm_password').val();
    var group = $('#group_select').val();
    var email = $('#email').val();
    var change_password = $('#change_password').is(':checked');
    $('.error-span').each(function() {
        $(this).addClass('hidden');
    });
    if ($('#change_password').is(':checked')) {
        if (password == '') {
            $('#password_empty').removeClass('hidden');
            return;
        }
        if (password !== confirm_password) {
            $('#confirm_password_differ').removeClass('hidden');
            return;
        }
    }

    $('#set_userinfo_sending_modal').modal('show');
    $.ajax({
        type: 'POST',
        url: '/set_userinfo/update',
        async: false,
        data: {
            'username': username,
            'password': password,
            'group': group,
            'email': email,
            'change_password': change_password
        },
        success: function(data) {
            $('#set_userinfo_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#set_userinfo_error_modal').modal('show');
            } else {
                //alert(document.referrer);
                window.location.href=document.referrer;
            }
        }
    });
}

function clearError () {
    $('.error-span').each(function() {
        $(this).addClass('hidden');
    });
}