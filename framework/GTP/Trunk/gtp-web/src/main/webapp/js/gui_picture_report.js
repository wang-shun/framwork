/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    //breakRow();
    filterColumn();
    $('#col6_filter').on('change',function(){
        var value = $('#col6_filter').val();
        if (value === 'All') {
            filterColumnAll();
        } else if (value === 'Fail') {
            filterColumn();
        }
    });
    filterColumnAll();
});

$('body').on('click','.case-plus',function() {
    var id = $(this).attr('data-name');
    $('#'+id).removeClass('hidden');
    $(this).children('span').removeClass('glyphicon-plus').addClass('glyphicon-minus');
    $(this).removeClass('case-plus').addClass('case-minus');
});

function filterColumn() {
    $('td[name=result]').each(function(){
        if ($(this).text() !== 'fail') {
            $(this).parent().addClass('hidden');
        }
    });
}

function filterColumnAll() {
    $(':hidden.main-row').each(function () {
        $(this).removeClass('hidden');
    });
}

$('body').on('click','.case-minus',function() {
    var id = $(this).attr('data-name');
    $('#'+id).addClass('hidden');
    $(this).children('span').removeClass('glyphicon-minus').addClass('glyphicon-plus');
    $(this).removeClass('case-minus').addClass('case-plus');
});

$(function(){
    $("td[name=dateTime]").each(function(){
        $(this).css("white-space","nowrap");
        $(this).text(timeStamp2String($(this).html()));
    });
})

function breakRow() {
    $('td[name=stack-td]').each(function() {
        var value = $(this).text();
        $(this).text(value.replace(/\n|\r|(\r\n)|(\u0085)|(\u2028)|(\u2029)/g, 'nr'));
    });
}