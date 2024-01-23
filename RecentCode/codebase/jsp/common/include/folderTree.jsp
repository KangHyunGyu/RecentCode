<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style type="text/css">
/* .aui-grid-tree-leaf-icon {
    background: url(/Windchill/jsp/component/AUIGrid/images/b_folder2.png) 50% 50% no-repeat;
} */
.${renderTo}_grid_tree .aui-grid {
    border: none;
}
</style>
<script>
$(document).ready(function(){
		
	$("#${renderTo}_folderName").keypress(function(e){
		if(e.keyCode==13){
			${renderTo}_tree_searchFolderTree();
		}
	});
	
	${renderTo}_tree_createFolderTreeGrid(${renderTo}_tree_columnLayout);
	
	${renderTo}_tree_getFolderTree();
	
});

//AUIGrid 생성 후 반환 ID
var ${renderTo}_tree_myGridID;

//폴더 트리 그리드 레이아웃
var ${renderTo}_tree_columnLayout = [ {
	dataField : "name",
	headerText : "Name",
	width : "100%",
	style : "pointer",
},{
	dataField : "round_sort",
	headerText : "sort",
	visible : false,
}];

//폴더 트리 그리드 생성
function ${renderTo}_tree_createFolderTreeGrid(${renderTo}_tree_columnLayout) {
	 ${renderTo}_tree_auiGridProps = {
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
		contextMenuItems: [{
		    label: "${e3ps:getMessage('폴더 생성')}", 
		  	callback :  function(){
		  	}
		   },{
		    label: "${e3ps:getMessage('폴더 삭제')}",
	    }],
	    width : ${gridWidth},
	    autoGridHeight : ${autoGridHeight},
	    height : ${gridHeight},
	};
	 
	${renderTo}_tree_myGridID = AUIGrid.create("#${renderTo}_grid_tree", ${renderTo}_tree_columnLayout, ${renderTo}_tree_auiGridProps);
	
	// 그리드 ready 이벤트 바인딩 폴더 초기화면 설정
	AUIGrid.bind(${renderTo}_tree_myGridID, "ready", ${renderTo}_tree_auiGridCompleteHandler);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(${renderTo}_tree_myGridID, "cellClick", ${renderTo}_tree_auiGridCellClickHandler);
	
	// 셀 더블클릭 이벤트 바인딧
	AUIGrid.bind(${renderTo}_tree_myGridID, "cellDoubleClick", ${renderTo}_tree_auiGridCellDoubleClickHandler);
	
	// 선택 변경 이벤트 바인딩
	AUIGrid.bind(${renderTo}_tree_myGridID, "selectionChange", ${renderTo}_tree_auiGridSelectionChangeHandler);
};

//폴더 트리 불러오기
function ${renderTo}_tree_getFolderTree(){
	
	var param = new Object();
	var container = $("#container option:selected").val();
	
	/*
	var container = "${container}";
	if("${renderTo}" == "epmFolder") {
		container = $("#container").val();	
		if(container == null || container ==""){
			return;
		}
	}
	*/
	param["container"] = container;
	param["rootLocation"] = "${rootLocation}";
	
	var url = getURLString("/common/getFolderTree");
	
	AUIGrid.showAjaxLoader(${renderTo}_tree_myGridID);
	ajaxCallServer(url, param, function(data){
		var ${renderTo}_tree_gridData = data.list;
		
		if(container == "COMMON" || container == "LIBRARY") {
			var list = new Array();
			
			var root = new Object();
			
			root["oid"] = container;
			root["name"] = container;
			root["path"] = "/Default";
			
			list.push(root);
			
			for(var i=0; i < ${renderTo}_tree_gridData.length; i++) {
				if(${renderTo}_tree_gridData[i].parent == null) {
					${renderTo}_tree_gridData[i].parent = container;
				}
			}
			
			list = list.concat(${renderTo}_tree_gridData);
			
			AUIGrid.setGridData(${renderTo}_tree_myGridID, list);
			
		} else {
			AUIGrid.setGridData(${renderTo}_tree_myGridID, ${renderTo}_tree_gridData);	
		}
		
		${renderTo}_tree_setInitFolder();
		
		AUIGrid.removeAjaxLoader(${renderTo}_tree_myGridID);
		
		if("${renderTo}" != "partFolder" && "${renderTo}" != "docFolder") {
			if(typeof search == 'function') {
				search();
			}
		}
	});
}

//셀 클릭 핸들러
function ${renderTo}_tree_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
	var location = event.item.path;
	var locationDisplay = "";
	locationDisplay = location.substring("/Default".length);
	
	document.querySelectorAll('form').forEach((formItem) => {
		let formItemArray = Array.from(formItem.childNodes);
		let location_Input = formItemArray.find((x)=> x.id == "${locationId}");
		if(location_Input){
			location_Input.value = location;
			document.getElementById(`${locationId}Display`).value = locationDisplay;
		}
	})

	if(typeof search == 'function') {
		search();
	}
	// 문서 folder Tree 클릭 시 실행
	if(locationDisplay.indexOf("document") != 0){
		var rowId = event.rowIdValue;
		var path = event.item.path;
		
		var depth = AUIGrid.getDepthByRowId("#${renderTo}_grid_tree", rowId);
		
		if(depth > 2){	// 레벨2일경우(/Document 하위 레벨)
			var ascendants = AUIGrid.getAscendantsByRowId("#${renderTo}_grid_tree", rowId);
			rowId = ascendants[ascendants.length-2].oid;
		}
		
		docFolderClick(rowId);
		$("#docCode").val(rowId);
	}
	
	if("${renderTo}" == "epmFolder") {
		/* var checked = AUIGrid.getCheckedRowItems(myGridID);
		for( var i =0;i< checked.length;i++){
			var dataField = AUIGrid.getDataFieldByColumnIndex(myGridID, 0); // 칼럼 인덱스로 dataField 얻기
			
			
			AUIGrid.setCellValue(myGridID, checked[i].rowIndex, 0, locationDisplay);
		} */
	}else if("${renderTo}" == "adminFolder"){
		//그리드 선택 없이 폴더 클릭 시 Guide
		
		//folderValueToDocTypeGrid(event);
	}
}

function ${renderTo}_tree_auiGridCellDoubleClickHandler(event){
	
	if("${renderTo}" == "adminFolder"){
		//클릭 시 선택 행의 폴더 값을 수정함
		folderValueToDocTypeGrid(event);
	}else if(typeof(folderValueToDocDirectory) == 'function'){
		//문서 관리(생성)
		folderValueToDocDirectory(event);
	}else if(typeof(setPartFolderMenual) == 'function'){
		//품목 폴더 지정
		setPartFolderMenual(event);
	}else if("${renderTo}" == "epmFolder") {
		document.getElementById('toggleBtn').click();
	}
	
}

//선택 변경 핸들러
function ${renderTo}_tree_auiGridSelectionChangeHandler(event) {
	
	var selectedItems = event.selectedItems;
	
	var item = selectedItems[0].item;
	
	var location = item.path;
	
	var locationDisplay = "";
	if("${renderTo}" == "epmFolder") {
		container = $("#container").val();
		
		if(container == "COMMON" || container == "LIBRARY") {
			locationDisplay = "/" + container + location.substring("/Default".length)		
		} else {
			locationDisplay = location.substring("/Default".length);	
		}
	} else {
		locationDisplay = location.substring("/Default".length);	
	}
	
	var form = "${formId}";
	
	if(form == "form") {
		if($("form").length > 0) {
			$("form:first").find("#${locationId}Display").val(locationDisplay);
			$("form:first").find("#${locationId}").val(location);
		}		
	} else {
		if($("#${formId}").length > 0) {
			$("#${formId}").find("#${locationId}Display").val(locationDisplay);
			$("#${formId}").find("#${locationId}").val(location);
		}		
	}
	
	var folderName = $("#${renderTo}_folderName").val();
	if(folderName.length > 0){
		if(typeof search == 'function') {
			search();
		}	
		console.log(typeof search2)
		if(typeof search2 == 'function') {
			search2();
		}	
	}
}

//트리 그리드 초기화면 설정
function ${renderTo}_tree_auiGridCompleteHandler(event) {
	
	var item = AUIGrid.getItemByRowIndex(${renderTo}_tree_myGridID, 0);

	var rowId = item.oid;
	
	AUIGrid.expandItemByRowId(${renderTo}_tree_myGridID, rowId, true);	// 브랜치(branch)일 때 열기/닫기
	AUIGrid.selectRowsByRowId(${renderTo}_tree_myGridID, rowId);
	
	var location = item.path;
	
	var locationDisplay = "";
	if("${renderTo}" == "epmFolder") {
		container = $("#container").val();
		
		if(container == "COMMON" || container == "LIBRARY") {
			locationDisplay = "/" + container + location.substring("/Default".length)		
		} else {
			locationDisplay = location.substring("/Default".length);	
		}
	} else {
		locationDisplay = location.substring("/Default".length);	
	}
	
	var form = "${formId}";
	
	if(form == "form") {
		if($("form").length > 0) {
			$("form:first").find("#${locationId}Display").val(locationDisplay);
			$("form:first").find("#${locationId}").val(location);
		}		
	} else {
		if($("#${formId}").length > 0) {
			$("#${formId}").find("#${locationId}Display").val(locationDisplay);
			$("#${formId}").find("#${locationId}").val(location);
		}		
	}
}

//폴더 검색
function ${renderTo}_tree_searchFolderTree() {
	var folderName = $("#${renderTo}_folderName").val();

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
	AUIGrid.search(${renderTo}_tree_myGridID, "name", folderName, options);
}

//초기 폴더 세팅
function ${renderTo}_tree_setInitFolder() {
	
	var location = "${location}";
	
	if("${renderTo}" == "epmFolder") {
		var container = $("#container").val();
		
		if(container == "COMMON" || container == "LIBRARY") {
			location = location.substring(("/" + container).length);	
		}
		
		if($("#container").val() != "${container}") {
			location = "";
		}
	}
	
	
	if(location.length > 0) {
		
		var locationArray = location.split("/");
		
		var loc = "/Default";
		for(var i = 0; i < locationArray.length; i++) {
			if("" != locationArray[i]) {
				loc = loc + "/" + locationArray[i];
				
				if(i > 1) {
					var rowItems = AUIGrid.getItemsByValue(${renderTo}_tree_myGridID, "path", loc);
					
					AUIGrid.expandItemByRowId(${renderTo}_tree_myGridID, rowItems[0].oid, true);
				}
			}
		}
		
		var rowIndex = AUIGrid.getRowIndexesByValue(${renderTo}_tree_myGridID, "path", "/Default" + location);
		
		AUIGrid.setSelectionByIndex(${renderTo}_tree_myGridID, rowIndex);
	}
}
//문서관리 폴더 cell 클릭
function docFolderClick(foid){
	
	$("[id=attributeList]").empty();
	var url	= getURLString("/doc/include_docAttribute");
	var param = new Object();
	param["foid"] = foid;
	param["mode"] = $("#mode").val();
	$("[id=attributeList]").load(url, param);
}

function ${renderTo}_changeContainer(){
	${renderTo}_tree_getFolderTree();
	/*
	if("${renderTo}" == "epmFolder") {
		var container = $("#container").val();	
		
		if(container != null && container == "ELECTRIC"){
			$("#createFolder").show();
		} else {
			$("#createFolder").hide();
		}
		
	}
	*/
	
	//스크롤을 통해 추가 검색이 이루어진상태라면 search 메소드를 불러 세션 및 페이지 초기화
	if(document.getElementById("sessionId") && document.getElementById("sessionId").value){
		if(typeof search == 'function') {
			search();
		}
		return;
	}
	
	//session value 가 없다면 getGridData 불러오기
	if(typeof getGridData == 'function') {
		getGridData();
	}
}

/*
 e3psAction.js로 이동
function getProductLibraryList() {
	var url	= getURLString("/common/getProductLibraryList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	$("#container").find("option").remove();
	console.log(list);
	for(var i=0; i<list.length; i++) {
		
			$("#container").append("<option value='" + list[i].name + "'>" + list[i].name + "</option>");
		
	}
}
*/

function folderTree_createFolder(){
	var selectedItems = AUIGrid.getSelectedItems(${renderTo}_tree_myGridID);
	if(selectedItems.length <= 0) return;
	
	var item = selectedItems[0].item;

	var renderTo = "${renderTo}";
	var fOid = item.oid;
	var rootLocation = item.path;
	var container = "${container}";
	if("${renderTo}" == "epmFolder") {
		container = $("#container").val();	
	}
	
	var url = getURLString("/common/createFolderPopup?renderTo="+renderTo+"&fOid="+fOid+"&rootLocation="+rootLocation+"&container="+container);
	openPopup(url, fOid, 500, 400);
}
</script>
<div class="tree" style="width:${gridWidth}px;">	
	
	<!-- 사업장별 구분 없어서 제외 -->
	<%-- <div class="search">
		<select id="container" class="w90" onchange="javascript:${renderTo}_changeContainer();">
			
		</select>
	</div> --%>
	<!-- 사업장별 구분 없어서 제외 -->
	
	<div class="search">
		<input type="text" id="${renderTo}_folderName" name="${renderTo}_folderName">
		<button type="button" class="s_bt" style="padding:1px 10px" onclick="javascript:${renderTo}_tree_searchFolderTree()">${e3ps:getMessage('폴더 검색')}</button>			
	</div>
	<!-- <div style="height:400px;overflow-y:auto;overflow-x:hidden;">
		<div class="grid_tree" id="grid_tree"></div>
	</div> -->
	<div class="${renderTo}_grid_tree" id="${renderTo}_grid_tree" style="height:${gridHeight}px"></div>
</div>