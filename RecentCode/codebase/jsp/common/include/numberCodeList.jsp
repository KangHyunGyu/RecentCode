<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(list_${linkTableName}_myGridID);
	
	//grid setting
	list_${linkTableName}_createAUIGrid(list_${linkTableName}_columnLayout);
	
	//get grid data
	if("${oid}".length > 0) {
		list_${linkTableName}_getGridData();	
	}
});

//사용자가 검색한 리스트 보관
var list_${linkTableName}_objList = [];

//AUIGrid 생성 후 반환 ID
var list_${linkTableName}_myGridID;

//AUIGrid 칼럼 설정
var list_${linkTableName}_columnLayout = [
	{ dataField : "code",				headerText : "${e3ps:getMessage('코드')}",			width:"20%",	style:"AUIGrid_Left",	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('이름')}",			width:"20%",	style:"AUIGrid_Left",	},
	{ dataField : "description",			headerText : "${e3ps:getMessage('설명')}",			width:"*",	style:"AUIGrid_Left",	},
];

//AUIGrid 를 생성합니다.
function list_${linkTableName}_createAUIGrid(list_${linkTableName}_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		editable : false,
		
		enableSorting : false,
		
		softRemoveRowMode : false,
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성
	list_${linkTableName}_myGridID = AUIGrid.create("#list_${linkTableName}_grid_wrap", list_${linkTableName}_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(list_${linkTableName}_myGridID, "cellClick", list_${linkTableName}_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(list_${linkTableName}_myGridID, gridData);
	
}

function list_${linkTableName}_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.linkTableName = "${linkTableName}";
	
	AUIGrid.showAjaxLoader(list_${linkTableName}_myGridID);
	var url = getURLString("/common/getRelatedCodeList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(list_${linkTableName}_myGridID, gridData);

		AUIGrid.setAllCheckedRows(list_${linkTableName}_myGridID, false);
		AUIGrid.removeAjaxLoader(list_${linkTableName}_myGridID);
		
	});
}

//셀 클릭 핸들러
function list_${linkTableName}_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
}
</script>
<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
	</div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<div class="list" id="list_${linkTableName}_grid_wrap" style="height:${gridHeight}px">
</div>