$(document).ready(function(){

    var time=new Date().getTime();
    var str = {
        "appName": "abc",
        "version": "2012",
        "objectTime":time,
        "ip":"10.126.59.1,10.126.59.2",
        "port":"80,81",
        "env":"UAT",
        "level":"1",
        "replaceHost":"1",
        "remark":""

    };
    var obj = JSON.stringify(str);
    $("#text1").val(obj);


    str={
        "objectTime":time
    };
    obj = JSON.stringify(str);
    $("#text2").val(obj);
});


function search()
{
    var jsonStr=$("#text2").val();
    var json =  JSON.parse(jsonStr);
    $.ajax({
        type: "POST",
        url: "/health/detail",
        data: JSON.stringify(json),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function abc()
{
    var jsonStr=$("#text1").val();
    var json =  JSON.parse(jsonStr);

    $.ajax({
        type: "POST",
        url: "/health/do",
        data: JSON.stringify(json),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
          alert(JSON.stringify(data));
        }
    });
}