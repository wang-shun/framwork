/**
 * Created by lizonglin on 2016/1/15/0020.
 */


$(document).ready(function () {
    $('#loadconf_new_save_btn').bind('click',function() {
        saveLoadScene();
    });
});

function addScriptSvn() {
    var newScriptSvn = $('#loadconf_scriptsvn_div_tpl').clone();
    newScriptSvn.removeAttr("id");
    newScriptSvn.removeClass("hidden");
    $('#loadconf_scriptsvn_add').after(newScriptSvn);
}

function addSourceSvn() {
    var newSourceSvn = $('#loadconf_sourcesvn_div_tpl').clone();
    newSourceSvn.removeAttr("id");
    newSourceSvn.removeClass("hidden");
    $('#loadconf_sourcesvn_add').after(newSourceSvn);
}

function delDiv(a) {
    $(a).closest('div').remove();
}

function saveLoadScene() {
    $('#loadconf_new_sending_modal').modal('show');
    var name = $('#loadconf_new_name').val().trim();
    if (checkIsNone("loadconf_new_name", name)) {
        return;
    }
    var scriptSvn = inputsVal("script-svn-input");
    if(checkIsNone("loadconf_scriptsvn_div_tpl", scriptSvn)) {
        return;
    }
    var jmxContent = $('#loadconf_new_jmxcontent').val().trim();
    if(checkIsNone("loadconf_new_jmxcontent", jmxContent)) {
        return;
    }
    var scene = $('#loadconf_new_scene').val();
    var sourceSvn = inputsVal("source-svn-input");
    if(checkIsNone("loadconf_sourcesvn_add", sourceSvn)) {
        return;
    }
    var env = $('#loadconf_new_env').val();

    $.ajax({
        url:"/load/conf/newSave",
        type:"POST",
        data:{
            "name":name,
            "scriptSvn":scriptSvn,
            "jmxContent":jmxContent,
            "scene":scene,
            "sourceSvn":sourceSvn,
            "env":env
        },
        success:function(data) {
            $('#loadconf_new_sending_modal').modal('hide');
            if(data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#loadconf_new_error_modal').modal('show');
            } else {
                $('#loadconf_new_sent_modal').modal('show');
            }
        }
    });
}

function inputsVal(className) {
    var values = "";
    $('input.' + className).each(function(i) {
        if (i > 0 && $(this).val().trim() != "") {
            values = values + $(this).val().trim() + ",";
        }
    });
    if (values.length > 0){
        return values.substring(0, values.lastIndexOf(","));
    } else {
        return values;
    }
}

function checkIsNone(id, value) {
    if(value.trim() == ""){
        $('#loadconf_new_sending_modal').modal('hide');
        $('#error_span').empty();
        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " 不能为空，请填写！");
        $('#loadconf_new_error_modal').modal('show');
        return true;
    } else {
        return false;
    }
}