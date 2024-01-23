<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	$("#folderName").keypress(function(e){
		if(e.keyCode==13){
			tree_searchFolderTree();
		}
	});
	
	createDepartmentTreeGrid(tree_columnLayout);
	
	getDepartmentTree();
});

//AUIGrid 생성 후 반환 ID
var tree_myGridID;

//부서 트리 그리드 레이아웃
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

//부서 트리 그리드 생성
function createDepartmentTreeGrid(tree_columnLayout) {
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
		showHeader : false,
		showAutoNoDataMessage : false,
	    autoGridHeight : false,
	    
	    height : 400,
	};
	
	tree_myGridID = AUIGrid.create("#grid_tree", tree_columnLayout, tree_auiGridProps);
	
	// 그리드 ready 이벤트 바인딩 폴더 초기화면 설정
	AUIGrid.bind(tree_myGridID, "ready", tree_auiGridCompleteHandler);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(tree_myGridID, "cellClick", tree_auiGridCellClickHandler);
	
	// 선택 변경 이벤트 바인딩
	AUIGrid.bind(tree_myGridID, "selectionChange", tree_auiGridSelectionChangeHandler);
};

//부서 트리 불러오기
function getDepartmentTree(){
	
	var param = new Object();
	param.rootLocation = "${rootLocation}";
	var url = getURLString("/department/getDepartmentTree");
	ajaxCallServer(url, param, function(data){
		var tree_gridData = data.list;
		AUIGrid.setGridData(tree_myGridID, tree_gridData);
		AUIGrid.removeAjaxLoader(tree_myGridID);
	});
}

//셀 클릭 핸들러
function tree_auiGridCellClickHandler(event) {
	
}

//선택 변경 핸들러
function tree_auiGridSelectionChangeHandler(event) {
	
}

//트리 그리드 초기화면 설정
function tree_auiGridCompleteHandler(event) {
	var item = AUIGrid.getItemByRowIndex(tree_myGridID, 0);

	if(item != null) {
		var rowId = item.oid;
		
		//AUIGrid.expandItemByRowId(tree_myGridID, rowId, true);	// 브랜치(branch)일 때 열기/닫기
		AUIGrid.expandAll(tree_myGridID);
		
		$("#soid").val(item.oid);
	}
}

//폴더 검색
function tree_searchFolderTree() {
	var folderName = $("#folderName").val();

	if(folderName.trim() == "") {
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
	AUIGrid.search(tree_myGridID, "name", folderName, options);
}

function select(){
	
	var selectedItems = AUIGrid.getSelectedItems(tree_myGridID);
	var item;
	if(selectedItems.length > 0) {
		item = selectedItems[0].item;
	} else {
		return;
	}
	
	if(opener.window.setAssignTeam) {
		opener.window.setAssignTeam(item);
	}
	if(opener.window.setTechnicalCommitee_setReviewTeam) {
		if("${pageName}" == "setTechnicalCommitee") {
			opener.window.setTechnicalCommitee_setReviewTeam(item);
		}
	}
	if(opener.window.setReviewTeam) {
		opener.window.setReviewTeam(item);
	}
	if(opener.window.setECCBExpTeam_setExpTeam) {
		if("${pageName}" == "setECCBExpTeam") {
			opener.window.setECCBExpTeam_setExpTeam(item);
		}
	}
	if(opener.window.setDepartment){
		if("${id}" != ""){
			opener.window.setDepartment("${id}", item);
		}
	}
	
	window.close();
}
</script>
<!-- pop -->
<div class="pop" style="min-width:0">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('부서 선택')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<!-- button -->
	<div class="seach_arm2 ml10 mr10 pt5 pb5">
		<div class="leftbt" style="width:70%">
			<input class="w60" type="text" id="folderName" name="folderName">
			<button type="button" class="s_bt" onclick="javascript:tree_searchFolderTree()" style="padding: 1px 10px;">${e3ps:getMessage('검색')}</button>			
		</div>
		<div class="rightbt" style="width:30%">
			<button type="button" class="s_bt03" onclick="javascript:select();">${e3ps:getMessage('선택')}</button>
		</div>
	</div>
	<!-- //button -->
	
	<!-- table list-->
	<div class="table_list ml10 mr10">
		<div class="list" id="grid_tree" style="height:400px"></div>
	</div>
	<!-- //table list-->
</div>		
<!-- //pop-->