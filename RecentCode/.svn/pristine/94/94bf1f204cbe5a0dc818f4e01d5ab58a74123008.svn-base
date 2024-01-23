<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree" style="height:100%">	
	<input type="hidden" id="parentCode" name="parentCode"/>
	<input type="hidden" id="parentOid" name="parentOid"/>
	<div class="grid_tree" id="grid_tree" style="height:${gridHeight}px">
	</div>
</div>
<style type="text/css">
/* .aui-grid-tree-leaf-icon {
    background: url(/Windchill/jsp/component/AUIGrid/images/b_folder2.png) 50% 50% no-repeat;
} */
.grid_tree .aui-grid {
    border: none;
}
</style>
<script>
$(document).ready(function(){
	
	createCodeTypeTreeGrid(tree_columnLayout);
	
});

//AUIGrid 생성 후 반환 ID
var tree_myGridID;

//코드 타입 트리 그리드 레이아웃
var tree_columnLayout = [ {
	dataField : "name",
	headerText : "Name",
	width : "100%",
	style : "pointer",
},{
	dataField : "round_sort",
	headerText : "sort",
	visible : false,
}];

//코드 타입 트리 그리드 생성
function createCodeTypeTreeGrid(tree_columnLayout) {
	 tree_auiGridProps = {
		rowIdField : "oid", 
		treeIdField : "oid",				// 트리의 고유 필드명
		treeIdRefField : "parentOid", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
		showSelectionBorder : false,
	  	flat2tree:true,
	  	hScrollPolicy : "off",
		useContextMenu : false,		
		enableFilter : true,
		enableRightDownFocus : true, 	// 컨텍스트 왼쪽 클릭과 같이 셀 선택
		editable : false,
		editingOnKeyDown : true,
		onlyEnterKeyEditEnd : true,
		showRowNumColumn : false,
		enableSorting : true,
		showStateColumn : false,
		selectionMode : "singleRow",
		//editBeginMode : "doubleClick",
		showAutoNoDataMessage : false,
		showHeader : false,
	    autoGridHeight : ${autoGridHeight},
	    height : ${gridHeight},
	    width : 200,
	};
	
	tree_myGridID = AUIGrid.create("#grid_tree", tree_columnLayout, tree_auiGridProps);
	
	// 그리드 ready 이벤트 바인딩 폴더 초기화면 설정
	AUIGrid.bind(tree_myGridID, "ready", tree_auiGridCompleteHandler);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(tree_myGridID, "cellClick", tree_auiGridCellClickHandler);
	
	// 선택 변경 이벤트 바인딩
	//AUIGrid.bind(tree_myGridID, "selectionChange", tree_auiGridSelectionChangeHandler);
};

//코드 타입 트리 불러오기
function getCodeTypeTree(){
	
	var param = new Object();
	
	var codeType = $("#codeType option:selected").val();
	param["codeType"] = codeType;
	
	var endLevel = 0;
	
	if(false) {
		endLevel = 2;
	} else if(codeType == "GATE") {
		endLevel = 3;
	}
	 
	param["endLevel"] = endLevel;
	var url = getURLString("/admin/getCodeTypeTree");
	ajaxCallServer(url, param, function(data){
		var tree_gridData = data.list;
		AUIGrid.setGridData(tree_myGridID, tree_gridData);
	});
	
   	AUIGrid.resize(tree_myGridID);
}

//셀 클릭 핸들러
function tree_auiGridCellClickHandler(event) {
	
	var code = event.item.code;
	var oid = event.item.oid;
	
	$("#parentCode").val(code);
	$("#parentOid").val(oid);
	
	if(typeof getGridData == 'function') {
		getGridData();
	}
}

//선택 변경 핸들러
function tree_auiGridSelectionChangeHandler(event) {
	
	var code = event.item.code;
	var oid = event.item.oid;
	
	$("#parentCode").val(code);
	$("#parentOid").val(oid);
	
	if(typeof getGridData == 'function') {
		getGridData();
	}
}

//트리 그리드 초기화면 설정
function tree_auiGridCompleteHandler(event) {
	var item = AUIGrid.getItemByRowIndex(tree_myGridID, 0);

	var rowId = item.oid;
	
	AUIGrid.expandItemByRowId(tree_myGridID, rowId, true);	// 브랜치(branch)일 때 열기/닫기
	AUIGrid.selectRowsByRowId(tree_myGridID, rowId);
	
}
</script>
<div class="tree" style="height:100%">	
	<input type="hidden" id="parentCode" name="parentCode"/>
	<input type="hidden" id="parentOid" name="parentOid"/>
	<div class="grid_tree" id="grid_tree">
	</div>
</div>