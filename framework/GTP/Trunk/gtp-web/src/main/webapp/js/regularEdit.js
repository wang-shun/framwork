/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



$(document).ready(function () {
    var name = $('#regularName').val();
    //var type = $('#scheduleType_hide').val();
    var type = $('#scheduleType').val();
    var weekDay = $('#regularWeekDay_hide').val();
    var time = $('#regularTime').val();
    var runRule = $('#runRule_hide').val();

    if (time.split(" ").length == 2) {
        var splitedDate = time.split(" ")[0];
        var splitedTime = time.split(" ")[1];

        var hmsTime = splitedTime.split(":");
        var hmTime = hmsTime[0] + ":" + hmsTime[1];

        $('#regularTime').val(hmTime);
    }

    $('#weekDay_select').val(weekDay);
    $('#regularDate').val(splitedDate);

    if (type == 2) {
        $('#run_rule_date_tr').addClass('hidden');
        $('#run_rule_week_tr').addClass('hidden');

    } else if (type == 3) {
        $('#run_rule_week_tr').removeClass('hidden');
        $('#run_rule_date_tr').addClass('hidden');
    } else {          //Once
        $('#run_rule_date_tr').removeClass('hidden');
        $('#run_rule_week_tr').addClass('hidden');
    }

    if (runRule === '0') {
        $("input[name='RegularRunRule']").get(0).checked = true;
    } else {
        $("input[name='RegularRunRule']").get(1).checked = true;
    }

});

$(document).ready(function () {
    $('#scheduleType').on('change', function () {
        var scheduleType = $("#scheduleType").val();
        if (scheduleType == 2) {             //daily
            $('#run_rule_date_tr').addClass('hidden');
            $('#run_rule_week_tr').addClass('hidden');
//            
        } else if (scheduleType == 3) {               //weekly
            $('#run_rule_week_tr').removeClass('hidden');
            $('#run_rule_date_tr').addClass('hidden');

        } else {                                       //once
            $('#run_rule_date_tr').removeClass('hidden');
            $('#run_rule_week_tr').addClass('hidden');
        }
    });
});


$(document).ready(function () {
    $('#regularSave').bind('click', function () {
        var id = $('#regularId_hide').val();
        var name = $('#regularName').val();
        var type = $('#scheduleType').val();
        var date = $('#regularDate').val();
        var weekDay = $('#weekDay_select').val();
        var time = $('#regularTime').val();
        var runRule = $(':radio:checked').val();

        $.ajax({
            type: "POST",
            url: "/regularRule/save",
            data: {'id': id,
                'name': name,
                'type': type,
                'date': date,
                'weekDay': weekDay,
                'time': time,
                'runRule': runRule},
            dataType: "json", //接收类型
            async: false,
            success: function (data) {
                if (data['isError']) {
                    $('#error_span').empty();
                    $('#error_span').html(data['message']);
                    $('#regular_edit_error_modal').modal('show');
                } else {
                    $('#regular_edit_sent_modal').modal('show');
                }
            }
        });
    });

    $('#task_modal_back_btn').on('click', function () {
        window.location.href = "/regularRule";
    });

});

