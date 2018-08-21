/**
 * Created by lizonglin on 2015/7/20/0020.
 */
$(document).ready(function () {
    $('#loadconf_edit_save_btn').bind('click', function () {
        saveEditConf();
    });
});

function saveEditConf() {
    var sendingModalId = "loadconf_edit_sending_modal";
    var errorModalId = "loadconf_edit_error_modal";
    $('#loadconf_edit_sending_modal').modal('show');
    var id = $('#loadconf_edit_id').html();
    var name = $('#loadconf_edit_name').val().trim();
    if (checkIsNone("loadconf_edit_name", name, sendingModalId, errorModalId)) {
        return;
    }
    var scriptSvn = inputsVal("script-svn-input");
    if(checkIsNone("loadconf_scriptsvn_div_tpl", scriptSvn, sendingModalId, errorModalId)) {
        return;
    }
    var jmxContent = $('#loadconf_edit_jmxcontent').val().trim();
    if(checkIsNone("loadconf_edit_jmxcontent", jmxContent, sendingModalId, errorModalId)) {
        return;
    }
    var scene = $('#loadconf_edit_scene').val();
    var sourceSvn = inputsVal("source-svn-input");
    if(checkIsNone("loadconf_sourcesvn_add", sourceSvn, sendingModalId, errorModalId)) {
        return;
    }
    var env = $('#loadconf_edit_env').val();

    $.ajax({
        url:"/load/conf/editSave",
        type:"POST",
        data:{
            "id":id,
            "name":name,
            "scriptSvn":scriptSvn,
            "jmxContent":jmxContent,
            "scene":scene,
            "sourceSvn":sourceSvn,
            "env":env
        },
        success:function(data) {
            $('#loadconf_edit_sending_modal').modal('hide');
            if(data['isError']) {
                $('#error_span').empty();
                $('#error_span').html(data['message']);
                $('#loadconf_edit_error_modal').modal('show');
            } else {
                $('#loadconf_edit_sent_modal').modal('show');
            }
        }
    });
}

//function delDiv(a) {
//    $(a).closest('div').remove();
//}
//
//function addScriptSvn() {
//    var newScriptSvn = $('#loadconf_edit_scriptsvn_div_tpl').clone();
//    newScriptSvn.removeAttr("id");
//    newScriptSvn.removeClass("hidden");
//    $('#loadconf_scriptsvn_add').after(newScriptSvn);
//}
//
//function addSourceSvn() {
//    var newSourceSvn = $('#loadconf_edit_sourcesvn_div_tpl').clone();
//    newSourceSvn.removeAttr("id");
//    newSourceSvn.removeClass("hidden");
//    $('#loadconf_sourcesvn_add').after(newSourceSvn);
//}