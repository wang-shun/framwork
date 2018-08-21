/**
 * Created by lizonglin on 2015/6/9.
 */
$(document).ready(function () {
    $('#start_date_input').val(getNowDate());
    $('#end_date_input').val(getNowDate());

    $('#start_date_input').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $('#start_date_div').bind('click', function() {
        $('#start_date_input').datetimepicker('show');
    });

    $('#end_date_input').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $('#end_date_div').bind('click', function() {
        $('#end_date_input').datetimepicker('show');
    });
    $('#gen_report_btn').bind('click', function() {
        genReport();
    });
});

function genReport () {
    $('#gen_report_sending_modal').modal('show');
    var taskType = $('#task_type_select').val();
    var startDate = trimDate($('#start_date_input').val());
    var endDate = trimDate($('#end_date_input').val());
    $.ajax({
        type: 'POST',
        url: '/gtp-report/generate',
        async: false,
        data: {
            'taskType' : taskType,
            'startDate': startDate,
            'endDate': endDate
        },
        success: function(data) {
            $('#gen_report_sending_modal').modal('hide');
            if (data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#gen_report_error_modal').modal('show');
            } else {
                $('#gen_report_sent_modal').modal('show');
            }
        }
    });
}