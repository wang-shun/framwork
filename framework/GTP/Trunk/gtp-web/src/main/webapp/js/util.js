/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 按星期更换背景图片
 */
$('body').css("background-image","url(/images/"+calculateImageNum()+".jpg)");
function calculateImageNum() {
    var date = new Date();
    var week = date.getDay();
    return week;
}

//将序列化的时间戳转换为 y-m-d h:m:s
function timeStamp2String(time) {
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
}
//将序列化的时间戳转换为h:m
function timeStamp2SimpleTime(time) {
    var datetime = new Date();
    datetime.setTime(time);
    var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    return hour + ':' + minute;
}

function getTiming (time, startType, weekDay) {
    //alert(startType);
    if ('Daily' === startType) {
        return '每天 - ' + timeStamp2SimpleTime(time);
    } else if ('Weekly' === startType) {
        return '每' + engWeek2ChnWeek(weekDay) + ' - ' + timeStamp2SimpleTime(time);
    } else if ('Once' === startType) {
        return '单次 - ' + timeStamp2String(time);
    } else {
        return '--';
    }
}

function engWeek2ChnWeek(weekDay) {
    var chnWeek = "";
    switch (weekDay) {
        case "SUN":
            chnWeek = "周日";
            break;
        case "MON":
            chnWeek = "周一";
            break;
        case "TUE":
            chnWeek = "周二";
            break;
        case "WED":
            chnWeek = "周三";
            break;
        case "THU":
            chnWeek = "周四";
            break;
        case "FRI":
            chnWeek = "周五";
            break;
        case "SAT":
            chnWeek = "周六";
            break;
        default:
            chnWeek = "未知";
    }
    return chnWeek;
}

function browserTypes(browser) {
    var allTypes = ['IE7', 'IE8', 'IE9', 'IE10', 'IE11','Edge', 'FireFox', 'Chrome', 'Safari', 'Ipad_Safari'];
    var browserTypes = [];
    for (i = 0; i < 10; i++) {
        if (1 === browser % 2) {
            browserTypes.push(allTypes[i]);
        }
        browser = parseInt(browser >> 1);
    }
    if (0 === browserTypes.length) {
        browserTypes.push('--');
    }
    return browserTypes;
}

function calculateBrowserValue(browserArray) {
    var jsonStr = {"IE7":1,"IE8":2,"IE9":4,"IE10":8,"IE11":16,"Edge":32,"FireFox":64,"Chrome":128,"Safari":256,"Ipad_Safari":512};
    var browserValue = 0;
    $.each(browserArray,function(i,value){
        browserValue = browserValue + jsonStr[value];
    });
    return browserValue;
}

//用于从"date time"中截取date
function trimToDate(dateTime) {
    var date = dateTime.split(' ');
    return date[0];
}

//$('document').ready(function() {//测试browserTypes函数
//    var array = browserTypes(60);
//    $.each(array, function (i, value){
//        alert(i +"th item: " + value );
//    });
//});


function getWeekDay(weekDay) {
    var weekdays = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday');
    return weekdays[weekDay];
}

function trimToHm(date) {
    var dateTime = date.split(" ");
    var hmsTime = dateTime[1].split(":");
    var hmTime = hmsTime[0] + ":" + hmsTime[1];
    return hmTime;
}

function timestamp2DateTime (timestamp) {
    var dateTime = newDate(timestamp * 1000);

}

/**
 * 2015-06-10 to 20150610
 */
function trimDate (date) {
    var d = new Date(date);
    var s = d.getUTCFullYear();
    s += ("00"+(d.getUTCMonth()+1)).slice(-2);
    s += ("00"+d.getUTCDate()).slice(-2);
    return s;
}

function getNowDate() {
    var d = new Date();
    var s = d.getUTCFullYear() + '-';
    s += ("00"+(d.getUTCMonth()+1)).slice(-2) + '-';
    s += ("00"+d.getUTCDate()).slice(-2);
    return s;
}

function togglePassword (input, icon) {
    if (input.attr('type') == 'password') {
        input.attr('type','text');
        icon.toggleClass('glyphicon-eye-open');
        icon.toggleClass('glyphicon-eye-close');
    } else {
        input.attr('type','password');
        icon.toggleClass('glyphicon-eye-close');
        icon.toggleClass('glyphicon-eye-open');
    }
}

function isLogin () {
    var login;
    $.ajax({
        type: 'GET',
        url: '/login/isLogin',
        async: false,
        success: function(data) {
            login = data;
        }
    });
    return login;
}

function isAdmin () {
    var admin;
    $.ajax({
        type: 'GET',
        url: '/login/isAdmin',
        async: false,
        success: function(data) {
            admin = data;
        }
    });
    return admin;
}

function array2SplittedStr(strArray){
    var result = "";
    for (var i = 0; i < strArray.length; i++) {
        result = result + strArray[i] + ",";
    }
    if (result.length > 0) {
        return result.substring(0, result.lastIndexOf(","));
    } else {
        return result;
    }
}

function openUrl(url)
{
    if(window.parent!=null)
    {
        window.parent.open(url,'_back');
    }
    else
    {
        window.open(url,'_back');
    }
    return ;
}

//function checkIsNone(id, value, sendingModalId, errorModalId) {
//    if(value.trim() == ""){
//        $('#' + sendingModalId).modal('hide');
//        $('#error_span').empty();
//        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " can not be none，please check and modify！");
//        $('#' + errorModalId).modal('show');
//        return true;
//    } else {
//        return false;
//    }
//}