<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
var edit = false;
var checked = false;
var eContentOid = "${oid}";
$(document).ready(function(){
	
	AUIGrid.destroy(ecr_content_gridID);
	
	if("${type}" == "view") {
		create_ecr_content_AUIGrid(ecr_content_columnLayout);
		ecrContentGetGridData(eContentOid);	
	} else if("${type}" == "modify") {
		edit = true;
		checked = true;
		create_ecr_content_AUIGrid(ecr_content_columnLayout);
		ecrContentGetGridData(eContentOid);
	} else {
		edit = true;
		checked = true;
		create_ecr_content_AUIGrid(ecr_content_columnLayout);
	}
	resizeGrid();
	
	
});

//AUIGrid 생성 후 반환 ID
var ecr_content_gridID;

//AUIGrid 칼럼 설정
var ecr_content_columnLayout = [
	{ dataField : "name", headerText : "${e3ps:getMessage('변경 전')}", width:"50%", style:"AUIGrid_Left"},
	{ dataField : "contents", headerText : "${e3ps:getMessage('변경 후')}", width:"50%",	style:"AUIGrid_Left"}
];

//AUIGrid 를 생성합니다.
function create_ecr_content_AUIGrid(columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		rowIdField : "_$uid",
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : checked,
		
		editable : edit,
		
		enableFilter : false,
		
		enableMovingColumn : false,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight,
		
		//툴팁 출력 지정
		showTooltip : true,
		
		//툴팁 마우스 대면 바로 나오도록 
		tooltipSensitivity: 500,
		
		softRemoveRowMode:false
		
	};

	// 실제로 #grid_wrap 에 그리드 생성
	ecr_content_gridID = AUIGrid.create("#ecr_content_grid", columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(ecr_content_gridID, "cellClick", auiGridCellClickHandler);
	
	var gridData = new Array();
	
	AUIGrid.setGridData(ecr_content_gridID, gridData);
	
	
}

//셀 클릭 핸들러
function auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

function ecrContentGetGridData(ecrOid) {
	var param = new Object();
	
	
	param.oid = ecrOid;
/* 	if(ecrOid.length > 0) {
		console.log("짜잔");
		param.oid = ecrOid;
	} else {
		console.log("힝구");
		param.oid = "${oid}";
	} */
	
	
	AUIGrid.showAjaxLoader(ecr_content_gridID);
	var url = getURLString("/change/getEchangeContent");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(ecr_content_gridID, gridData);

		AUIGrid.setAllCheckedRows(ecr_content_gridID, false);
		AUIGrid.removeAjaxLoader(ecr_content_gridID);
		
	}); 
}

function addRow() {
	var item = new Object();
	AUIGrid.addRow(ecr_content_gridID, item, "last");
}

function removeRow() {
	var checkItemList = AUIGrid.getCheckedRowItems(ecr_content_gridID);
	checkItemList.forEach((row)=>{
		AUIGrid.removeRowByRowId(ecr_content_gridID, row.item._$uid);
	})
	
}
</script>

<div class="seach_arm2 pt10 pb5">
	<div class="leftbt">
	</div>
	<div class="rightbt">
		<span id="rootBtn">
		<c:if test="${type eq 'create' || type eq 'modify'}">
			<button type="button" class="s_bt03" onclick="addRow()">${e3ps:getMessage('추가')}</button>
			<button type="button" class="s_bt05" onclick="removeRow()">${e3ps:getMessage('삭제')}</button>
		</c:if>
		</span>
	</div>
</div>
<div class="pb10">
	<div class="list" id="ecr_content_grid"></div>
</div>
