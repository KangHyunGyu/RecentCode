<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style type="text/css">
/* .aui-grid-tree-leaf-icon {
    background: url(/Windchill/jsp/component/AUIGrid/images/b_folder2.png) 50% 50% no-repeat;
} */
.grid_tree .aui-grid {
    border: none;
}
.isDisable{
    background:#fff5f5;
	font-weight:bold;
	color:#ff0000;
}
</style>
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
	styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
		if(item.disable) {
			return "isDisable";
		}
		return null;
	}
},{
	dataField : "round_sort",
	headerText : "sort",
	visible : false,
}];

//부서 트리 그리드 생성
function createDepartmentTreeGrid(tree_columnLayout) {
	 tree_auiGridProps = {
		rowIdField : "code", 
		treeIdField : "code",				// 트리의 고유 필드명
		treeIdRefField : "parentCode", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
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
		showAutoNoDataMessage : false,
		
		contextMenuItems: [{
		    label: "${e3ps:getMessage('부서 정보')}", 
		  	callback :  function(event){
		  		var oid = event.item.oid;
		  		
		  		openView(oid);
		  	}
	    },{
		    label: "${e3ps:getMessage('부서 수정')}", 
		  	callback :  function(event){
		  		var oid = event.item.oid;
		  		
		  		var url = getURLString("/department/modifyDepartment") + "?oid=" + oid;
		  		
		  		openPopup(url, oid, 700, 300);
		  	}
	    },{
		    label: "${e3ps:getMessage('부서 삭제')}",
		    callback :  function(event){
		    	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		    		
		    		var param = new Object();
		    		
		    		var oid = event.item.oid;
		    		
		    		param["oid"] = oid;
		    		param["isPopup"] = false;
		    		
		    		var url = getURLString("/department/deleteDepartmentAction");
					ajaxCallServer(url, param, function(data){
						getDepartmentTree();
					});
		    	});
		    	
		  	}
	    },{
	    	label: "${e3ps:getMessage('부서 추가')}",
		    callback :  function(event){
				var oid = event.item.oid;
		  		
		  		var url = getURLString("/department/createDepartment") + "?parentOid=" + oid;
		  		
		  		openPopup(url, oid, 700, 300);
		  	}
	    }], 
	    
	    autoGridHeight : ${autoGridHeight},
	    
	    height : ${gridHeight},
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
	
	param.rootLocation = "worldexint";
	param.isAdmin = ${isAdmin};
	
	var url = getURLString("/department/getDepartmentTree");
	ajaxCallServer(url, param, function(data){
		console.log("tree data:",data.list)
		var tree_gridData = data.list;
		AUIGrid.setGridData(tree_myGridID, tree_gridData);
	});
}

//셀 클릭 핸들러
function tree_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	var name = event.item.name;
	var code = event.item.code;
	var sort = event.item.sort;
	var pCode = event.item.parentCode;
	
	$("#departmentOid").val(oid);
	$("#soid").val(oid);
	$("#sname").val(name);
	$("#scode").val(code);
	$("#ssort").val(sort);
	$("#sdept").val(pCode);
	
	$("#userName").val("");
	
	preRequestData();
}

//선택 변경 핸들러
function tree_auiGridSelectionChangeHandler(event) {
	
	var selectedItems = event.selectedItems;
	var item = selectedItems[0].item;

	$("#departmentOid").val(item.oid);
	$("#soid").val(item.oid);
	$("#sname").val(item.name);
	$("#scode").val(item.code);
	$("#ssort").val(item.sort);
	$("#sdept").val(item.pCode);
}

//트리 그리드 초기화면 설정
function tree_auiGridCompleteHandler(event) {
	var item = AUIGrid.getItemByRowIndex(tree_myGridID, 0);

	if(item != null) {
		var rowId = item.oid;
		
		//AUIGrid.expandItemByRowId(tree_myGridID, rowId, true);	// 브랜치(branch)일 때 열기/닫기
		AUIGrid.selectRowsByRowId(tree_myGridID, rowId);
		AUIGrid.showItemsOnDepth(tree_myGridID, 3);
		
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
</script>
<div class="tree">	
	<div class="search">
		<input class="w70" type="text" id="folderName" name="folderName">
		<button type="button" class="s_bt" onclick="javascript:tree_searchFolderTree()" style="padding: 1px 10px;">${e3ps:getMessage('검색')}</button>			
	</div>
	<div class="grid_tree" id="grid_tree" style="height:${gridHeight}px">
</div>
</div>
