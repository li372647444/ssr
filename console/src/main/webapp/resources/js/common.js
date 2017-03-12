function submitForm(formId, buttonId, callback, id) {
    var form = $('#' + formId);
    disableButton(buttonId, true);
    $.ajax({
        type	: 'post',
        url		: form.attr("action"),
        data	: form.serialize(),
        fitColumns : true,
        striped : true,
        singleSelect : true,
        success : function(data) {
            disableButton(buttonId, false);
            if(data.success == false){
                if(callback != null && callback != ''){
                    if(typeof(callback) == "function"){
                        callback(data);
                    }
                    else{
                        eval(callback)(data);
                    }
                }
                else{
                	bootbox.alert(data.errorMessage);
                }
                return;
            }
            if(id != null && id != ''){
                if(data.model.id){
                    $('#' + id).val(data.model.id);
                }
            }
            if (data.success == true)
            {
                if(callback != null && callback != ''){
                    if(typeof(callback) == "function"){
                        callback(data);
                    }
                    else{
                        eval(callback)(data);
                    }
                }
                else{
                    if(data.message){
                    	bootbox.alert(data.message);
                    }else{
                    	bootbox.alert("提交成功");
                    }
                }
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            disableButton(buttonId, false);
            bootbox.alert(XMLHttpRequest.responseText);
        }
    });
}

function disableButton(buttonId, disable) {
    if(disable){
        $("#" + buttonId).attr("disabled", true);
    }
    else{
        $("#" + buttonId).attr("disabled", false);
    }
}

function validateForm(form){
	var re = true;
	$("#" + form).find("input").each(function(){
		var r = $(this).attr("required");
		if(r){
			$(this).keyup(function(){
				var tt = $(this).val();
				if(tt !== ""){
					$(this).parent().parent().removeClass("has-error");
				}
				else{
					$(this).parent().parent().addClass("has-error");
				}
			});
			var t = $(this).val();
			if(t == ""){
				$(this).parent().parent().addClass("has-error");
				$(this).focus();
				re = false;
				return re;
			}
		}
	});
	if(!re){
		return re;
	}
	$("#" + form).find("select").each(function(){
		var rc = $(this).attr("required");
		if(rc){
			$(this).change(function(){
				var ttc = $(this).val();
				if(ttc !== ""){
					$(this).parent().parent().removeClass("has-error");
				}
				else{
					$(this).parent().parent().addClass("has-error");
				}
			});
			var tc = $(this).val();
			if(tc == ""){
				$(this).parent().parent().addClass("has-error");
				$(this).focus();
				re = false;
				return re;
			}
		}
	});
	return re;
}

function resetForm(form){
	$("#" + form).find("input").each(function(){
		$(this).val("");
	});
	$("#" + form).find("select").each(function(){
		$(this).prop('selectedIndex', 0);
	});
}

function loadFrom(form, data){
	$("#" + form).find("input").each(function(){
		var id = $(this).attr("id");
		eval("var  t = data." + id);
		if(t){
			$(this).val(t);
		}
	});
	$("#" + form).find("select").each(function(){
		var id = $(this).attr("id");
		eval("var  t = data." + id);
		if(t){
			$(this).val(t);
		}
	});
}

function initMenu(menu){
	if(menu){
		$("#" + menu).addClass("active");
		$("#" + menu).parent().parent().addClass("start active open");
		var t = $("#" + menu).parent().parent().children("a")[0];
		var tt = $(t).children("span");
		$(tt[1]).addClass("open");
		$(tt[2]).addClass("selected");
	}
	else{
		$("#m_home").addClass("start active");
		$("#m_home_s").addClass("start selected");
	}
}

function toDecimal(x) {
	var f = parseFloat(x);
	if (isNaN(f)) {
		return 0;
	}
	f = Math.round(x * 100) / 100;
	return f;
}