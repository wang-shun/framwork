/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var dateTime = $('#scheduleTime').val();
    var hm = trimToHm(dateTime);
    $('#scheduleTime').val(hm);
    
    $('#scheduleTime').datetimepicker({
        datepicker: false,
        format: 'H:i',
        step: 5
    });
    $('#schedule_edit_time').on('click', function () {
        $('#scheduleTime').datetimepicker('show');
    });

});