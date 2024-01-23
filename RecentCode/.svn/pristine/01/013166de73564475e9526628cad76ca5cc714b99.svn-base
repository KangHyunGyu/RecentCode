<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
document.addEventListener("DOMContentLoaded", function(){
	
	app_rec_line_view_createAUIGrid(app_rec_line_view_columnLayout);
	app_rec_line_view_getGridData();
	
});

var app_rec_line_view_myGridID;

var app_rec_line_view_columnLayout = [
	{ dataField : "roleName",	headerText : "${e3ps:getMessage('구분')}",	width:"5%"},
	{ dataField : "name",			headerText : "${e3ps:getMessage('이름')}",	width:"10%"},
	{ dataField : "departmentName",	headerText : "${e3ps:getMessage('부서')}",	width:"5%"},
	{ dataField : "duty",			headerText : "${e3ps:getMessage('직위')}",	width:"5%"},
	{ dataField : "userId",			headerText : "${e3ps:getMessage('아이디')}",	width:"7%"},
	{ dataField : "approveDate",	headerText : "${e3ps:getMessage('수신일')}",	width:"8%"},
	{ dataField : "stateName",		headerText : "${e3ps:getMessage('수신 상태')}",	width:"8%"},
	{ dataField : "description",	headerText : "${e3ps:getMessage('결재 의견')}",	width:"*"}
];

function app_rec_line_view_createAUIGrid(app_rec_line_view_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : false,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		editable : false,
		
		enableSorting : false,
		
	};

	app_rec_line_view_myGridID = AUIGrid.create("#app_rec_line_view_grid_wrap", app_rec_line_view_columnLayout, gridPros);
	
	resizeGrid();
	
	var gridData = new Array();
	AUIGrid.setGridData(app_rec_line_view_myGridID, gridData);
}

function app_rec_line_view_getGridData(){

	var param = new Object();
	
	param["oid"] = "${oid}";
	param["searchRoleType"] = "RECEIVE";
	param["type"] = "view";
	
	var url = getURLString("/approval/getApprovalLine");
	AUIGrid.showAjaxLoader(app_rec_line_view_myGridID);
	ajaxCallServer(url, param, function(data){

		var gridData = data.list;
		
		AUIGrid.setGridData(app_rec_line_view_myGridID, gridData);
		AUIGrid.removeAjaxLoader(app_rec_line_view_myGridID);
		
	});
}

function appendReceiveLinePopup() {

	var url = getURLString("/approval/appendReceiveLinePopup") + "?oid=${oid}";
	openPopup(url, "appendReceiveLinePopup", "1300", "750");
}			
</script>
<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<h4>${e3ps:getMessage('수신 리스트')}</h4>
	</div>
	<div class="rightbt">
		<%-- <button type="button" class="s_bt03" onclick="appendReceiveLinePopup(this);" id="appendReceiveLine">${e3ps:getMessage('수신인 추가')}</button> --%>
	</div>
</div>
<!-- //button -->
<div class="list" id="app_rec_line_view_grid_wrap">
</div>