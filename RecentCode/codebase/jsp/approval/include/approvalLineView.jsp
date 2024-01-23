<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
document.addEventListener("DOMContentLoaded", function(){
	
	app_line_view_createAUIGrid(app_line_view_columnLayout);
	app_line_view_getGridData();
	
});

var app_line_view_myGridID;

var app_line_view_columnLayout = [
	{ dataField : "approvalGubun",	headerText : "${e3ps:getMessage('구분')}",	width:"5%"},
	{ dataField : "name",			headerText : "${e3ps:getMessage('이름')}",	width:"10%"},
	{ dataField : "departmentName",	headerText : "${e3ps:getMessage('부서')}",	width:"5%"},
	{ dataField : "duty",			headerText : "${e3ps:getMessage('직위')}",	width:"5%"},
	{ dataField : "userId",			headerText : "${e3ps:getMessage('아이디')}",	width:"7%"},
	{ dataField : "approveDate",	headerText : "${e3ps:getMessage('결재일')}",	width:"8%"},
	{ dataField : "stateName",		headerText : "${e3ps:getMessage('결재')}",	width:"8%"},
	{ dataField : "description",	headerText : "${e3ps:getMessage('결재 의견')}",	width:"*"}
];

function app_line_view_createAUIGrid(app_line_view_columnLayout) {
	
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

	app_line_view_myGridID = AUIGrid.create("#app_line_view_grid_wrap", app_line_view_columnLayout, gridPros);
	
	resizeGrid();
	
	var gridData = new Array();
	AUIGrid.setGridData(app_line_view_myGridID, gridData);
}

function app_line_view_getGridData(){

	var param = new Object();
	
	param["oid"] = "${oid}";
	param["type"] = "view";
	param["searchRoleType"] = "${searchRoleType}";
	
	var url = getURLString("/approval/getApprovalLine");
	AUIGrid.showAjaxLoader(app_line_view_myGridID);
	ajaxCallServer(url, param, function(data){
		
		var gridData = data.list;
		
		AUIGrid.setGridData(app_line_view_myGridID, gridData);
		AUIGrid.removeAjaxLoader(app_line_view_myGridID);
	});
}

</script>
<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<h4>${e3ps:getMessage('결재 리스트')}</h4>
	</div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<div class="list" id="app_line_view_grid_wrap">
</div>