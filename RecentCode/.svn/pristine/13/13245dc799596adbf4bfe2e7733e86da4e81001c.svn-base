<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>

$(document).ready(function(){
	create_pdr_content_AUIGrid(pdr_content_columnLayout);
	pdr_dataInit();
});

//AUIGrid 생성 후 반환 ID
var pdr_content_gridID;

//AUIGrid 칼럼 설정
var pdr_content_columnLayout = [
	{ dataField : "name",		headerText : "${e3ps:getMessage('내용')}",	width:"30%", style:"aui-grid-pdr-contents"},
	{ dataField : "contents", headerText : "${e3ps:getMessage('검토 내용')}", width:"70%",	style:"AUIGrid_Left"}
];

//AUIGrid 를 생성합니다.
function create_pdr_content_AUIGrid(columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		showSelectionBorder : true,
		noDataMessage : gridNoDataMessage,
		rowIdField : "_$uid",
		showRowNumColumn : true,
		showEditedCellMarker : false,
		wrapSelectionMove : true,
		showRowCheckColumn : true,
		editable : true,
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
	pdr_content_gridID = AUIGrid.create("#pdr_content_grid", columnLayout, gridPros);
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(pdr_content_gridID, "cellClick", auiGridCellClickHandler);
	var gridData = new Array();
	AUIGrid.setGridData(pdr_content_gridID, gridData);
}

//셀 클릭 핸들러
function auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

function pdr_dataInit(){
	let nameArr = ['공정명','가공내역','가공SIZE','가공시간(분)','TOOL','TOOL 가격','TOOL사용량에 따른 가격','JIG','JIG 가격','JIG사용량에 따른 가격','단가','비고'];
	let itemArr = [];
	
	nameArr.forEach((name)=>{
		itemArr.push({'name':name, 'contents':''});
	})
	
	AUIGrid.setGridData(pdr_content_gridID, itemArr);
}

function pdr_addRow() {
	console.log("========");
	var item = new Object();
	AUIGrid.addRow(pdr_content_gridID, item, "last");
}

function pdr_removeRow() {
	var checkItemList = AUIGrid.getCheckedRowItems(pdr_content_gridID);
	checkItemList.forEach((row)=>{
		AUIGrid.removeRowByRowId(pdr_content_gridID, row.item._$uid);
	})
	
}
</script>

<div class="seach_arm2 pt10 pb5">
	<div class="leftbt">
	</div>
	<div class="rightbt">
		<span id="rootBtn">
			<button type="button" class="s_bt03" onclick="pdr_addRow()">${e3ps:getMessage('추가')}</button>
			<button type="button" class="s_bt05" onclick="pdr_removeRow()">${e3ps:getMessage('삭제')}</button>
		</span>
	</div>
</div>
<div class="pb10">
	<div class="list" id="pdr_content_grid"></div>
</div>
