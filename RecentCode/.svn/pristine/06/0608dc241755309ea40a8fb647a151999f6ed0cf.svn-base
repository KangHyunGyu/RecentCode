<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//팝업 리사이즈
	//popupResize();
	
	//grid setting
	createAUIGrid(columnLayout);
	
	$("#folderOid").val("${fOid}");
	//folderTree setting
	createFolderTreeGrid(tree_columnLayout);
	getFolderTree();
	getGridData();
	
	AUIGrid.resize(tree_myGridID);
});

//AUIGrid 생성 후 반환 ID
var myGridID;

//AUIGrid 칼럼 설정
var columnLayout = [
	{ dataField : "name",		headerText : "${e3ps:getMessage('이름')}",		width:"*",			style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
/* 	{ dataField : "path",		headerText : "${e3ps:getMessage('폴더')}",		width:"60%",	
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
 */];

//AUIGrid 를 생성합니다.
function createAUIGrid(columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		editable : true,
			
		selectionMode : "multipleCells",
		
		rowIdField : "_$uid",

		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showRowNumColumn : true,
		
		wrapSelectionMove : true,
		
		//showRowCheckColumn : true,
		
		enableMovingColumn : true,
		
		showEditedCellMarker : true,
		
		showStateColumn : true,
		
		showRowCheckColumn : true,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight
	};

	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

function getGridData() {
	
	var param = new Object();
	param["folderOid"] = $("#folderOid").val();
	
	AUIGrid.showAjaxLoader(myGridID);
	var url = getURLString("/common/getFolderChildrenList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(myGridID, gridData);

		AUIGrid.removeAjaxLoader(myGridID);
		
	});
}

//AUIGrid 생성 후 반환 ID
var tree_myGridID;

//폴더 타입 트리 그리드 레이아웃
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
//폴더 타입 트리 그리드 생성
function createFolderTreeGrid(tree_columnLayout) {
	tree_auiGridProps = {
		rowIdField : "oid", 
		treeIdField : "oid",				// 트리의 고유 필드명
		treeIdRefField : "parent", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
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
		showHeader : false,
		height : 430,
	};
	
	tree_myGridID = AUIGrid.create("#grid_tree", tree_columnLayout, tree_auiGridProps);
	
	// 그리드 ready 이벤트 바인딩 폴더 초기화면 설정
	AUIGrid.bind(tree_myGridID, "ready", tree_auiGridCompleteHandler);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(tree_myGridID, "cellClick", tree_auiGridCellClickHandler);
	
	// 선택 변경 이벤트 바인딩
	//AUIGrid.bind(tree_myGridID, "selectionChange", tree_auiGridSelectionChangeHandler);
};

//폴더 트리 불러오기
function getFolderTree(){
	
	var param = new Object();
	param["fOid"] = "${fOid}";
	param["rootLocation"] = "${rootLocation}";
	param["container"] = "${container}";
	
	var url = getURLString("/common/getFolderTree");
	ajaxCallServer(url, param, function(data){
		var tree_gridData = data.list;
		var parent = data.list[0];
		parent.parent=null;
		
		AUIGrid.setGridData(tree_myGridID, tree_gridData);	
		
		AUIGrid.removeAjaxLoader(tree_myGridID);
	});
}
//셀 클릭 핸들러
function tree_auiGridCellClickHandler(event) {
	var oid = event.item.oid;
	var path = event.item.path;
	var isBranch = event.item._$isBranch;
	$("#folderOid").val(oid);
	getGridData();
}

//선택 변경 핸들러
function tree_auiGridSelectionChangeHandler(event) {
	var oid = event.item.oid;
	var path = event.item.path;
	var isBranch = event.item._$isBranch;
	$("#folderOid").val(oid);
	getGridData();
}

//트리 그리드 초기화면 설정
function tree_auiGridCompleteHandler(event) {
	var item = AUIGrid.getItemByRowIndex(tree_myGridID, 0);
	var rowId = item.oid;
	
	AUIGrid.expandItemByRowId(tree_myGridID, rowId, true);	// 브랜치(branch)일 때 열기/닫기
	AUIGrid.selectRowsByRowId(tree_myGridID, rowId);
}
//행 추가
function addRow(){
	var item = new Object();
	AUIGrid.addRow(myGridID, item, "last");
}
//삭제 버튼
function removeRow() {
	var checkItemList = AUIGrid.getCheckedRowItems(myGridID);
	
	for(var i = 0; i < checkItemList.length; i++){
		AUIGrid.removeRowByRowId(myGridID, checkItemList[i].item._$uid);
	}
	
	AUIGrid.setAllCheckedRows(myGridID, false);
}
//저장 버튼
function saveFolder(){
	//추가된 아이템들
	var addedItemList = AUIGrid.getAddedRowItems(myGridID);
	
	//수정된 아이템들
	var editedItemList = AUIGrid.getEditedRowItems(myGridID); 
	
	//삭제된 아이템들
	var removedItemList = AUIGrid.getRemovedItems(myGridID);
	
	var param = new Object();
	param["folderOid"] = $("#folderOid").val();
	
	param["addedItemList"] = addedItemList;
	param["editedItemList"] = editedItemList;
	param["removedItemList"] = removedItemList;
	
	var url = getURLString("/common/saveFolderAction");
	ajaxCallServer(url, param, function(data){
		getFolderTree();	
		getGridData();
		if(opener.${renderTo}_tree_getFolderTree) {
			opener.${renderTo}_tree_getFolderTree();
		}
	});
}
</script>
<!-- pop -->
<div class="pop" style="min-width:840px;">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('폴더 생성')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="semi_content">
		<div class="con semi_table1 ml10 mt10">
			<div class="tree" style="height:100%">	
				<input type="hidden" id="folderOid" name="folderOid"/>
				<div class="grid_tree" id="grid_tree">
				</div>
			</div>
		</div>
		<div class="semi_content2" style="min-width: 500px;">
			<div class="seach_arm pt5 pb5">
				<div class="leftbt">
				</div>
				<div class="rightbt">
					<button type="button" class="s_bt03" onclick="javascript:addRow()">${e3ps:getMessage('추가')}</button>
					<%-- <button type="button" class="s_bt03" onclick="javascript:removeRow()">${e3ps:getMessage('삭제')}</button> --%>
					<button type="button" class="s_bt03" onclick="javascript:saveFolder()">${e3ps:getMessage('저장')}</button>
					<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
				</div>
			</div>
			<!-- //button -->
			<!-- table list-->
			<div class="table_list">
				<div class="list" id="grid_wrap" style="height:400px"></div>
			</div>
			<!-- //table list-->
		</div>
	</div>
</div>		
<!-- //pop-->