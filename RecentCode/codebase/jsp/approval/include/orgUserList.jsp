<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style type="text/css">
</style>
<script>
$(document).ready(function(){
	
	//팝업 리사이즈
	popupResize();
	
	//grid reset
	AUIGrid.destroy(tree_myGridID);
	
	//grid reset
	AUIGrid.destroy(list_myGridID);
	
	createDepartmentTreeGrid(tree_columnLayout);
	
	createUserListGrid(list_columnLayout);
	
	getDepartmentTree();
	
	getUserList();
	
	$("#userInfo").keypress(function(e){
		if(e.keyCode==13){
			searchUser();
		}
	});
});

//AUIGrid 생성 후 반환 ID
var tree_myGridID;
var list_myGridID;

//부서 트리 그리드 레이아웃
var tree_columnLayout = [ {
	dataField : "name",
	headerText : "Name",
	width : "100%",
},{
	dataField : "round_sort",
	headerText : "sort",
	visible : false,
}];

//유저 리스트 그리드 레이아웃
var list_columnLayout = [ 
	{
		dataField : "name",
		headerText : "Name",
		style:"AUIGrid_Left",	
		width : "100%",
		labelFunction : function(rowIndex, columnIndex, value, headerText, item) { 
			var retStr = item.name + " / " + item.departmentName;
			
			return retStr;
		}
	},
];

//부서 트리 그리드 생성
function createDepartmentTreeGrid(tree_columnLayout) {
	 tree_auiGridProps = {
		rowIdField : "oid", 
		treeIdField : "oid",				// 트리의 고유 필드명
		treeIdRefField : "parentOid", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
		showSelectionBorder : false,
	  	flat2tree:true,
	  	hScrollPolicy : "off",
		useContextMenu : true,		
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
		enableDrop : false,
	};
	
	tree_myGridID = AUIGrid.create("#grid_tree", tree_columnLayout, tree_auiGridProps);
	
	// 그리드 ready 이벤트 바인딩 폴더 초기화면 설정
	AUIGrid.bind(tree_myGridID, "ready", tree_auiGridCompleteHandler);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(tree_myGridID, "cellClick", tree_auiGridCellClickHandler);
	
	// 선택 변경 이벤트 바인딩
	AUIGrid.bind(tree_myGridID, "selectionChange", tree_auiGridSelectionChangeHandler);
};

//유저 리스트 그리드 생성
function createUserListGrid(list_columnLayout) {
	var list_auiGridProps = {
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : false,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		showHeader : false,
		
		// 드래깅 행 이동 가능 여부 (기본값 : false)
		enableDrag : true,
		// 다수의 행을 한번에 이동 가능 여부(기본값 : true)
		enableMultipleDrag : true,
		// 셀에서 바로  드래깅 해 이동 가능 여부 (기본값 : false) - enableDrag=true 설정이 선행 
		enableDragByCellDrag : true,
		// 드랍 가능 여부 (기본값 : true)
		// false 설정했다는 의미는 이 그리드는 드래깅만 가능하고, 드랍은 절대 받지 않는다는 뜻임.
		enableDrop : true,
		// 드랍을 받아줄 그리드가 다른 그리드에도 있는지 여부 (기본값 : false)
		// 즉, 드리드 간의 행 이동인지 여부
		dropToOthers : true
	};
	
	list_myGridID = AUIGrid.create("#grid_list", list_columnLayout, list_auiGridProps);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(list_myGridID, "cellClick", list_auiGridCellClickHandler);
	
	// 드래그 앤 드랍 종료 이벤트 바인딩
	AUIGrid.bind(list_myGridID, "dropEndBefore", list_dropEndEventHandler);
	
	// 드랍 종료 전 이벤트 바인딩 - 여기서 행 이동, 복사를 결정합니다.
	/* AUIGrid.bind(list_myGridID, "dropEndBefore", function(event) {
		if(event.items.length == 0) return false;
		
		// 이벤트의 isMoveMode 속성을 false 설정하면 행 복사를 합니다.
		event.isMoveMode = false;
		
		// 드랍되는 그리드의 PID
		var pidToDrop = event.pidToDrop;
		var item = event.items[0]; // 드래깅 되고 있는 첫번째 행
		var notHave = AUIGrid.isUniqueValue(pidToDrop, "id", item.id); // 이미 존재하는지 검사
		if(!notHave) {
			if(confirm("지금 드랍되는 행은 이미 이전에 드랍된 행입니다. 또 드랍하시겠습니까?")) {
				return true;
			} else {
				return false; // 기본 행위 안함.
			}
		}
		return true; // 만약 return false 를 하게 되면 드랍 행위를 하지 않습니다.(즉, 기본 행위를 안함)
	}); */
};

//부서 트리 불러오기
function getDepartmentTree(){
	
	var param = new Object();
	
	param.rootLocation = "${rootLocation}";
	
	var url = getURLString("/department/getDepartmentTree");
	ajaxCallServer(url, param, function(data){
		var tree_gridData = data.list;
		AUIGrid.setGridData(tree_myGridID, tree_gridData);
		AUIGrid.showItemsOnDepth(tree_myGridID, 3);
	});
}

//유저 리스트 불러오기
function getUserList(oid){
	
	var param = new Object();
	
	param["departmentOid"] = oid;
	param["excludeSessionUser"] = true;
	
	AUIGrid.showAjaxLoader(list_myGridID);
	var url = getURLString("/common/searchUserAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		$(".approvalLineGrid").each(function(){
			var appLineList = AUIGrid.getGridData("#" + $(this).attr("id"));

			for(var i=0; i < appLineList.length; i++) {
				gridData = gridData.filter(function(item, index, arr){
				    return item.oid != appLineList[i].oid;
				});
			}
		});
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(list_myGridID, gridData);

		AUIGrid.setAllCheckedRows(list_myGridID, false);
		AUIGrid.removeAjaxLoader(list_myGridID);
	});
}

//셀 클릭 핸들러
function tree_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
	getUserList(oid);
}

//셀 클릭 핸들러
function list_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
}

//선택 변경 핸들러
function tree_auiGridSelectionChangeHandler(event) {
	
	var selectedItems = event.selectedItems;
	var item = selectedItems[0].item;

}

//트리 그리드 초기화면 설정
function tree_auiGridCompleteHandler(event) {
	var item = AUIGrid.getItemByRowIndex(tree_myGridID, 0);

	var rowId = item.oid;
	
	AUIGrid.expandItemByRowId(tree_myGridID, rowId, true);	// 브랜치(branch)일 때 열기/닫기
	AUIGrid.selectRowsByRowId(tree_myGridID, rowId);
	
}

//드래드 앤 드랍 핸들러
function list_dropEndEventHandler(event) {
	var item = event.items[0];
	
	var toRowIndex = event.toRowIndex;
	var type = item.type;
	
	if(event.pid == event.pidToDrop){
		return false;
	}
}

//유저 검색
function searchUser() {
	var userInfo = $("#userInfo").val();

	if(userInfo.trim() == "") {
		alert("${e3ps:getMessage('검색할 단어를 입력하십시오.')}");
		return;
	}
	
	var options = {
		direction : true, // 검색 방향  (true : 다음, false : 이전 검색)
		caseSensitive : false, // 대소문자 구분 여부 (true : 대소문자 구별, false :  무시)
		wholeWord : false, // 온전한 단어 여부
		wrapSearch : true, // 끝에서 되돌리기 여부
	};

	// 검색 실시
	//options 를 지정하지 않으면 기본값이 적용됨(기본값은 direction : true, wrapSearch : true)
	AUIGrid.searchAll(list_myGridID, userInfo, options);
}
</script>
<div class="pop_left ">
	<div class="left"><h2>${e3ps:getMessage('조직')}</h2></div>
	<div class="search">
		<input type="text" id="userInfo" class="w62" placeholder="${e3ps:getMessage('이름')}">
		<button type="button" class="s_bt" onclick="searchUser();">${e3ps:getMessage('검색')}</button>
		<div style="font-size: 12px;"> * ${e3ps:getMessage('담당자 선택하여 결재선으로 드래그')}</div>			
	</div>
</div>
<div class="pop_left_2 mt10">
	<div class="left pr10">
		<div class="grid_tree" id="grid_tree" style="height:500px"></div>
	</div>
	<div class="right pl10">
		<div id="grid_list" style="height:500px"></div>
	</div>
</div>