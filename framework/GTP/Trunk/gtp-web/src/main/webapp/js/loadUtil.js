/**
 * Created by lizonglin on 2016/1/19/0019.
 */

function delDiv(a) {
    $(a).closest('div').remove();
}

function addAfter(tplId, addAfterId) {
    var newScriptSvn = $('#' + tplId).clone();
    newScriptSvn.removeAttr("id");
    newScriptSvn.removeClass("hidden");
    $('#' + addAfterId).after(newScriptSvn);
}

function inputsVal(className) {
    var values = "";
    $('input.' + className).each(function(i) {
        if (i > 0 && $(this).val().trim() != "") {
            values = values + $(this).val().trim() + ",";
        }
    });
    if (values.trim() != ""){
        return values.substring(0, values.lastIndexOf(","));
    } else {
        return values;
    }
}

function checkIsNone(id, value, sendingModalId, errorModalId) {
    if(value.trim() == ""){
        $('#' + sendingModalId).modal('hide');
        $('#error_span').empty();
        $('#error_span').html($('#' + id).closest("tr").find('th:eq(0)').html() + " 不能为空，请检查并修改");
        $('#' + errorModalId).modal('show');
        return true;
    } else {
        return false;
    }
}
