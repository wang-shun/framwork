/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $("input[name='regularRunRule']").get(0).checked = true;
    $('#newTypes').val("1");
    $('#new_week_tr').addClass('hidden');

    $('#newTypes').on('change', function () {
        changeTypes();
    });

    $('#newSave').on('click', function () {
        newSave();
    });
    
    $('#regular_new_sent_modal button').on('click',function(){
        window.location.href="/regularRule";
    });

    loadRegularDate();
});

function newSave () {
    var regularName = $('#newName').val();
    var scheduleType = $('#newTypes').val();
    var regularDate = $('#regularDate').val();
    var weekDay = $('#newWeekDay ').val();
    var regularTime = $('#regularTime').val();
    var runRule = $(':radio:checked').val();
    $.ajax({
        type: "POST",
        url: "/regularRule/createSave",
        async: false,
        dataType: "json",
        data: {
            'regularName': regularName,
            'scheduleType': scheduleType,
            'regularDate': regularDate,
            'weekDay': weekDay,
            'regularTime': regularTime,
            'runRule': runRule},
        success: function (data) {
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#regular_new_error_modal').modal('show');
            } else {
                $('#regular_new_sent_modal').modal('show');
            }
        }
    });
}

function changeTypes () {
    var type = $('#newTypes').val();
    if (type == 2) {             //daily
        $('#new_date_tr').addClass('hidden');
        $('#new_week_tr').addClass('hidden');
//
    } else if (type == 3) {               //weekly
        $('#new_week_tr').removeClass('hidden');
        $('#new_date_tr').addClass('hidden');

    } else {                                       //once
        $('#new_date_tr').removeClass('hidden');
        $('#new_week_tr').addClass('hidden');
    }
}

function loadRegularDate() {
    var date = new Date();
    if (date < 10){
        date = "0" + date;
    }
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if (month < 10){
        month = "0" + month;
    }
    var day = date.getDate();
    if (day < 10){
        day = "0" + day;
    }
    var hour = date.getHours();
    if (hour < 10) {
        hour = "0" + hour;
    }
    var minute = date.getMinutes();
    if (minute < 10) {
        minute = "0" + minute;
    }


    $('#regularDate').val(year + "-" + month + "-" + day);
    $('#regularTime').val(hour + ":" + minute);
}