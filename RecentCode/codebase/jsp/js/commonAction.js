/**
 *  기본 form 전송, ajax 전송 관련 Action 
 */
//폼 submit 처리
window.formSubmit = function(formId, param, confirmMsg, cbMethod, isProgress) {
	

	var arrayParam = $("#" + formId).serializeArray();
	var paramCheck = new Array();
	
	if(param == null) {
		param = new Object();
	}

	//필수값 체크
	var isRequired = false;
	$("input, select, textarea").each(function() {
		if ($(this).data("required") && $(this).val().trim() == "") {
			isRequired = $(this).data("message");
			return false;
		}
	});

	if (isRequired) {
		openNotice(isRequired);
		return;
	}

	//주 첨부파일 체크 : 드래그로 바꿔서 주석처리함
	/*
	if($(".primary").length == 1){
		if($("[id=PRIMARY]").length == 0){
			openNotice($(".primary").data("message"));
			return;
		}
	}
	*/
	
	$.each(arrayParam, function(idx, obj) {

		var thisName = obj.name;
		var thisVal = obj.value;
		var isArray = false;
		
		//배열 파라미터를 하나로 합침
		$.each(param, function(key, value) {
			if (thisName == key) {
				var array = new Array();
				
				if(value instanceof Array) {
					value.push(obj.value);
				} else {
					array.push(value);
					array.push(obj.value);
					
					param[obj.name] = array;
				}
				isArray = true;
			}
		});

		if (!isArray) {
			param[obj.name] = obj.value;
		}
	});

	var actionUrl = $("#" + formId).attr("action");
	
	if(confirmMsg != null){
		openConfirm(confirmMsg, function(){
			return ajaxCallServer(actionUrl, param, cbMethod, isProgress);
		});
		
	}else{
		return ajaxCallServer(actionUrl, param, cbMethod, isProgress);
	}
}

//ajax 호출
window.ajaxCallServer = function(searchURL, param, cbMethod, isProgress) {
	
	if(param == null){
		param = new Object();
	}
	
	param = JSON.stringify(param);
	
	var retVal = [];
	var isSync = false;
	if (cbMethod != null){
		isSync = true;
	}

	$.ajax({
		type : "POST",
		url : searchURL,
		dataType : "JSON",
		crossDomain: true,
		data : param,
		async : isSync, //동기처리를 해야만 펑션리턴이 가능함
		//accepts : "application/json",
		contentType : "application/json; charset=UTF-8",

		beforeSend : function(xhr) {
			xhr.setRequestHeader("AJAX","true");
			if(isProgress) {
				startProgress();
			}
		},
		complete : function(data, flag) { //flag : success, error
			endProgress();
			/*if (opener) {
				
				if (opener.window.refreshGridList) {
					opener.window.refreshGridList();
				}
				
				if(window.myGridID){
					AUIGrid.removeAjaxLoader(window.myGridID);
				}
			}*/
		},
		success : function(data) {
			if (data.result || data.result == "ok") {
				if (cbMethod != null) {
					cbMethod(data);
				}
			}

			retVal = data;
			
			openNotice(data.msg, data.redirectUrl, data.openView);

		},
		error : function(xhr, textStatus, error) {
			openNotice("오류가 발생했습니다.<br>관리자에게 문의하시기 바랍니다. <br> ERROR = "
					+ xhr.status + " : " + xhr.statusText);
			endProgress();
			for(var i = 0; i < intervalList.length; i++){
				clearInterval(intervalList[i]);
			}
		}
	});

	return retVal;
}

window.callFormAjax = function(url, formData, cbMethod, isProgress) {
	
	var retVal = null;
	var progress = false;
	var isSync = false;
	
	if(isProgress != null){
		progress = isProgress;
	}
	
	if(cbMethod != null){
		isSync = true;
	}
	
	$.ajax({
		type:"POST",
		processData: false,
		contentType: false,
		url: url,
		data:formData,
		dataType:"json",
		async: isSync,
		cache: false,
		success:function(data){
			if (data.result || data.result == "ok") {
				if (cbMethod != null) {
					cbMethod(data);
				}
			}

			retVal = data;
			openNotice(data.msg, data.redirectUrl, data.openView);
		},
		
		error : function(xhr, textStatus, error) {
			openNotice("오류가 발생했습니다.<br>관리자에게 문의하시기 바랍니다. <br> ERROR = "
					+ xhr.status + " : " + xhr.statusText);
			endProgress();
			for(var i = 0; i < intervalList.length; i++){
				clearInterval(intervalList[i]);
			}
		},

		beforeSend : function(ready, data) {
			if(progress) {
				startProgress();
			}
		},
        
		complete : function(data, flag) { //flag : success, error
			endProgress();
			/*if (opener) {
				
				if (opener.window.refreshGridList) {
					opener.window.refreshGridList();
				}
				
				if(window.myGridID){
					AUIGrid.removeAjaxLoader(window.myGridID);
				}
			}*/
		}
	}); 
	
	return retVal;
}
