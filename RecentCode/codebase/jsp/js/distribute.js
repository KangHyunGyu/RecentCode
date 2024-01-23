/**
 * 배포관리(distribute) 공통 JS 파일
 */
$(document).ready(function(){
	// 업체 autocomplete
	$(".searchSupplier").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/distribute/searchSupplierAuto");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["code"] = params.term;
					
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	item.id = item.supplierCode;
			            	item.name = item.supplierName;
			            	item.text = item.supplierCode + " / " + item.supplierName;
			              	return item;
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Object Info (Number or NAME)",
			minimumInputLength: 2,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			},
			insertTag: function (data, tag) {
				//console.log(data);
				//console.log(tag);
			    //data.push(tag);
			  }
		});
	});
	
	$("[name=distributeType], [name=submit_type]").click(function(){
		var roleCode = $(this).data("rolecode");
		$("#classification").empty();
		$("#classification").data("rolecode", roleCode);
		$("#classificationPop").data("rolecode", roleCode);
	});
});
/**************************************************************
*                       검색 Popup 창 열기
****************************************************************/
function openSupplierPopup(){
	var url = getURLString("/distribute/searchSupplierPopup");
	
	openPopup(url, "searchSupplierPopup");
}
/**************************************************************
*                       popup창 업체 선택
****************************************************************/
function add_supplier(list) {
	var supplierCode = list[0].supplierCode;
	var supplierName = list[0].supplierName;
	var html = "<option value='"+supplierCode+"' selected>"+supplierCode + " / " + supplierName+"</option>"
	
	$("[id=supplierId]").append(html);
	$("#supplierName").val(supplierName); 
};
/**************************************************************
*                       popup창 분류 선택
****************************************************************/
function openCodePopup(id, codeType, obj) {
	var code = $("#" + id).val();
	var roleCode = $(obj).data("rolecode");
		
	var url = getURLString("/common/openCodePopup") + "?codeType=" + codeType + "&id=" + id + "&code=" + code + "&roleCode=" + roleCode;
	
	openPopup(url,"openCode", 1000, 600);
}

/**************************************************************
*                      상태 가져오기
****************************************************************/
function getReceiptStateList() {
	var url	= getURLString("/distribute/getReceiptStateList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	$("#state").find("option").remove();
	
	$("#state").append("<option value=''></option>");
	for(var i=0; i<list.length; i++) {
		$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}
/**************************************************************
*                      그리드 개수 체크
****************************************************************/
function gridLengthCheck(gridId){
	
	var length = 0;
	//var length = AUIGrid.getRowCount(gridId);
	if("part" == gridId){
		length = $$("add_relatedPart_grid_wrap").count();
	}else if("epm" == gridId){
		length = $$("add_relatedEpm_grid_wrap").count();
	}else if("doc" == gridId){
		length = $$("add_relatedDoc_grid_wrap").count();
	}else if("dwg" == gridId){
		length = $$("check_drawing_grid_wrap").count();
	}
	console.log(length);
	if(length <= 0){
		return false;
	}
	return true;
}
/**************************************************************
*                      supplierName hidden Data 넣기  
****************************************************************/
function setSupplierData(select){
	var id = $(select).attr("id");
	
	var item = $("#" + id).select2("data")[0];
	
	if(item == null || item.name.length == 0) {
		return;
	} 
	$("#supplierName").val(item.name);
}
/**************************************************************
*                      배포 상태 가져오기
****************************************************************/
function getLinkStateList() {
	var url	= getURLString("/distribute/getLinkStateList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	$("#linkState").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#linkState").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}
/**************************************************************
*                      도면 검증 (Part)
****************************************************************/
function checkDrawing(){
	//관련 부품
	var relatedPartList = $$("add_relatedPart_grid_wrap").data.serialize();//AUIGrid.getGridData(add_relatedPart_myGridID);
	var param = new Object();
	param["relatedPartList"] = relatedPartList;
	param["cadType"] = $("#cadType").prop("checked");

	var url = getURLString("/distribute/checkDrawingAction");
	ajaxCallServer(url, param, function(data){
		isCheckDWG = true;
		var gridData = data.list;
		$$("check_drawing_grid_wrap").parse(gridData);
	}, false);

	if(!gridLengthCheck("part")){
		openNotice("부품을 선택하세요.");
		return;
	}

	var partValid = true;
	$$("add_relatedPart_grid_wrap").data.each(function(obj){
		if(!obj.number || obj.number.length === 0){
			partValid = false;
		}
	});
	
	if(!partValid) {
		$("#add_relatedPart_grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}
	// var partValid = AUIGrid.validateGridData(add_relatedPart_myGridID, ["number"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	// if(!partValid) {
	// 	$(add_relatedPart_myGridID)[0].scrollIntoView();
	// 	openNotice("추가된 관련 부품에 값을 입력하세요.");
	// 	return;
	// }
	
	// AUIGrid.showAjaxLoader(check_drawing_myGridID);
 	// var url = getURLString("/distribute/checkDrawingAction");
	// ajaxCallServer(url, param, function(data){
	// 	isCheckDWG = true;
	// 	var gridData = data.list;
	// 	AUIGrid.setGridData(check_drawing_myGridID, gridData);	
		
	// 	AUIGrid.removeAjaxLoader(check_drawing_myGridID);
	// }, false);
}
/**************************************************************
 *                      도면 검증 (epm)
 ****************************************************************/
function checkDrawingEpm(){
	//관련 도면
	var relatedEpmList = $$("add_relatedEpm_grid_wrap").data.serialize();//AUIGrid.getGridData(add_relatedEpm_myGridID);
	var param = new Object();
	param["relatedEpmList"] = relatedEpmList;

	var url = getURLString("/distribute/checkDrawingEpmAction");
	ajaxCallServer(url, param, function(data){
		isCheckDWGEpm = true;
		var gridData = data.list;
		console.log(gridData);
		// $$("check_drawing_grid_wrap").parse(gridData);
	}, false);
	if(!gridLengthCheck("epm")){
		openNotice("도면을 선택하세요.");
		return;
	}

	var epmValid = true;
	$$("add_relatedEpm_grid_wrap").data.each(function(obj){
		if(!obj.number || obj.number.length === 0){
			epmValid = false;
		}
	});
	
	if(!epmValid) {
		$("#add_relatedEpm_grid_wrap")[0].scrollIntoView();
		openNotice("${e3ps:getMessage('추가된 관련 부품에 값을 입력하세요.')}");
		return false;
	}
	// var epmValid = AUIGrid.validateGridData(add_relatedEpm_myGridID, ["number"], "${e3ps:getMessage('필수 필드는 반드시 값을 입력해야 합니다.')}");
	// if(!epmValid) {
	// 	$(add_relatedEpm_myGridID)[0].scrollIntoView();
	// 	openNotice("추가된 관련 도면에 값을 입력하세요.");
	// 	return;
	// }
	
	// AUIGrid.showAjaxLoader(add_relatedEpm_myGridID);
	// var url = getURLString("/distribute/checkDrawingEpmAction");
	// ajaxCallServer(url, param, function(data){
	// 	isCheckDWGEpm = true;
	// 	var gridData = data.list;
	// 	for(var i=0; i<gridData.length; i++){
	// 		var rows = AUIGrid.getRowIndexesByValue(add_relatedEpm_myGridID, "oid", gridData[i].oid);
	// 		AUIGrid.updateRow(add_relatedEpm_myGridID, gridData[i], rows);
	// 	}
	// 	AUIGrid.removeAjaxLoader(add_relatedEpm_myGridID);
	// }, false);
}