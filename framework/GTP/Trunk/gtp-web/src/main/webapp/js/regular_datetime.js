/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    $('#regularDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $('#regular_edit_date').on('click', function () {
        $('#regularDate').datetimepicker('show');
    });

    $('#regularTime').datetimepicker({
        datepicker: false,
        format: 'H:i',
        step: 5
    });
    $('#regular_edit_time').on('click', function () {
        $('#regularTime').datetimepicker('show');
    });

});