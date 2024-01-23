<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style type="text/css">
/* .aui-grid-tree-leaf-icon {
    background: url(/Windchill/jsp/component/AUIGrid/images/b_folder2.png) 50% 50% no-repeat;
} */

	
</style>
<script>
$(document).ready(function(){
	AUIGrid.destroy(deptTree_myGridID);
	AUIGrid.destroy(deptUser_myGridID);
	createDeptTreeAUIGrid(deptTreeColumnLayout);
	createDeptUserAUIGrid(deptUserColumnLayout);
	getDeptTreeGridData();
	preRequestData();
	
	let currentSearchValue2 = '';
	
	document.querySelector('#searchName2').addEventListener('keyup', function(evt){
		let term = evt.target.value.trim();
		let direction = true;
		if(term == '') {
			AUIGrid.clearSelection(deptUser_myGridID);
		}
		
		if(evt.keyCode == 38 ){
			direction = false;
		}
		
		var options = {
			direction: direction,
			caseSensitive: false,
			wholeWord: false,
			wrapSearch: true
		};
		
		if(currentSearchValue2 != term){
			AUIGrid.searchAll(deptUser_myGridID, term, options);
		}else if(evt.keyCode == 38 || evt.keyCode == 40 || evt.keyCode == 13){
			AUIGrid.searchAll(deptUser_myGridID, term, options);
		}
		
		currentSearchValue2 = term;

	})
	
});

//AUIGrid 생성 후 반환 ID
var deptTree_myGridID;
var deptUser_myGridID;

// AUIGrid 칼럼 설정
var deptTreeColumnLayout = [
{
	dataField: "name",
	headerText: "${e3ps:getMessage('이름')}",
	width: "*%",
	style : "pointer",
}
];

var deptUserColumnLayout = [

	{
	    dataField: "id",
	    headerText: "아이디",
	    width: "20%",
	    filter: {
	      showIcon: true,
	      iconWidth: 30,
	    },
	    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
	      //HTML 템플릿 작성

	      var display = value;

	      if (item.wtuserDeleted) {
	        display += "(삭제됨)";
	      }

	      return display;
	    },
	  },
	  {
	    dataField: "name",
	    headerText: "이름",
	    width: "*%",
	    filter: {
	      showIcon: true,
	      iconWidth: 30,
	    },
	  },
	  {
	    dataField: "departmentName",
	    headerText: "부서",
	    width: "*%",
	    filter: {
	      showIcon: true,
	      iconWidth: 30,
	    },
	  },
	  {
	      dataField: "dutyDisplay",
	      headerText: "직책",
	      width: "20%",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	 ];

function getDeptTreeGridData(){
	var param = new Object();
	param.rootLocation = "worldexint";
	param.isAdmin = "${e3ps:isAdmin()}";
	param.disabled = true;
	var url = getURLString("/department/getDepartmentTree");
	ajaxCallServer(url, param, function(data){
		var tree_gridData = data.list;
		AUIGrid.setGridData(deptTree_myGridID, tree_gridData);
		
		AUIGrid.expandAll(deptTree_myGridID);
	});
}

function getDeptUserGridData(){
	var param = new Object();
	param.rootLocation = "worldexint";
	param.isAdmin = "${e3ps:isAdmin()}";
	param.disabled = true;
	var url = getURLString("/department/getDepartmentTree");
	ajaxCallServer(url, param, function(data){
		var tree_gridData = data.list;
		AUIGrid.setGridData(deptUser_myGridID, tree_gridData);
		
		AUIGrid.expandAll(deptUser_myGridID);
	});
}

//AUIGrid 를 생성합니다.
function createDeptTreeAUIGrid(columnLayout) {

	var auiGridProps = {

			rowIdField : "code", 
			treeIdField : "code",				
			treeIdRefField : "parentCode",
			editable: false,
			showSelectionBorder : false,
		  	flat2tree:true,
			enableRightDownFocus : true, 
			// 칼럼 끝에서 오른쪽 이동 시 다음 행, 처음 칼럼으로 이동할지 여부
			wrapSelectionMove: true,
			showHeader : false,
			selectionMode: "singleRow",
			showRowNumColumn : false,
			//드래그 앤 드랍 프로퍼티
			editable : false, // 드래깅 행 이동 가능 여부 (기본값 : false)
			enableDrag : true, // 다수의 행을 한번에 이동 가능 여부 (기본값 : true)
			enableMultipleDrag : true, // 셀에서 바로 드래깅 해 이동 가능 여부 (기본값 : false) - enableDrag=true 설정이 선행되야 함
			enableDragByCellDrag : true, // 드랍 가능 여부 (기본값 : true)
			enableDrop : true, // 드랍을 받아줄 그리드가 다른 그리드에도 있는지 여부 (기본값 : false) - 그리드 간 행 이동인지 여부
			dropToOthers : true,
			dropAcceptableGridIDs : ['#grid_wrap'], //해당 그리드에만 드랍 가능

			// 사용자가 추가한 새행은 softRemoveRowMode 적용 안함. 
			// 즉, 바로 삭제함.
			softRemovePolicy: "exceptNew"
			
	};

	// 실제로 #grid_wrap 에 그리드 생성
	deptTree_myGridID = AUIGrid.create("#grid_tree2", columnLayout, auiGridProps);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(deptTree_myGridID, "cellClick", deptTree_EventHandler);
	AUIGrid.bind(deptTree_myGridID, "dropEndBefore", function( event ) {
		   // 이벤트의 isMoveMode 속성을 false 설정하면 행(Row) 복사를 합니다.
		   event.isMoveMode = false;
		   var pidToDrop = event.pidToDrop;
		   var param = new Object();
			param.isDisable = 'false';
			param.departmentOid = event.items[0].oid;
			param.groupOid = document.getElementById('groupOid').value;
			let parentGroupOidEle = document.getElementById('parentGroupOid');
			let parentGroupOid = '';
			if(parentGroupOidEle) parentGroupOid = parentGroupOidEle.value;
			if(parentGroupOid && parentGroupOid.length > 0) param.parentGroupOid = parentGroupOid;
			
			var url = getURLString("/admin/searchUserAction");
			ajaxCallServer(url, param, function(data){
				var gridData = data.list;
				saveGroupUser(gridData);
			});
		   return false; // 기본 행위 안함.
	});
	
}

function createDeptUserAUIGrid(columnLayout) {

	var auiGridProps = {

			selectionMode : "multipleRows",
			rowIdField : "_$uid",
			showSelectionBorder : true,
			noDataMessage : gridNoDataMessage,
			showRowNumColumn : true,
			wrapSelectionMove : true,
			headerHeight : gridHeaderHeight,
			rowHeight : gridRowHeight,
			
			//드래그 앤 드랍 프로퍼티
			editable : false, // 드래깅 행 이동 가능 여부 (기본값 : false)
			enableDrag : true, // 다수의 행을 한번에 이동 가능 여부 (기본값 : true)
			enableMultipleDrag : true, // 셀에서 바로 드래깅 해 이동 가능 여부 (기본값 : false) - enableDrag=true 설정이 선행되야 함
			enableDragByCellDrag : true, // 드랍 가능 여부 (기본값 : true)
			enableDrop : true, // 드랍을 받아줄 그리드가 다른 그리드에도 있는지 여부 (기본값 : false) - 그리드 간 행 이동인지 여부
			dropToOthers : true,
			dropAcceptableGridIDs : ['#grid_wrap'], //해당 그리드에만 드랍 가능
	};

	// 실제로 #grid_wrap 에 그리드 생성
	deptUser_myGridID = AUIGrid.create("#grid_wrap2", columnLayout, auiGridProps);
	
	// Drag End 시 Event 호출	
	AUIGrid.bind(deptUser_myGridID, "dropEndBefore", function( event ) {
		   // 이벤트의 isMoveMode 속성을 false 설정하면 행(Row) 복사를 합니다.
		   event.isMoveMode = true;
		   var pidToDrop = event.pidToDrop;
		   saveGroupUser(event.items);
		   return true; // 기본 행위 안함.
	});
	// 셀 클릭 이벤트 바인딩
	//AUIGrid.bind(deptTree_myGridID, "cellClick", getGridData);
}

function preRequestData() {
	let selectedDeptRows = AUIGrid.getSelectedRows(deptTree_myGridID);
	
	var param = new Object();
	param.isDisable = 'false';
	if(selectedDeptRows.length > 0) param.departmentOid = selectedDeptRows[0].oid;
	param.groupOid = document.getElementById('groupOid').value;
	let parentGroupOidEle = document.getElementById('parentGroupOid');
	let parentGroupOid = '';
	if(parentGroupOidEle) parentGroupOid = parentGroupOidEle.value;
	if(parentGroupOid && parentGroupOid.length > 0) param.parentGroupOid = parentGroupOid;
	
	AUIGrid.showAjaxLoader(deptUser_myGridID);
	var url = getURLString("/admin/searchUserAction");
	ajaxCallServer(url, param, function(data){
		var gridData = data.list;
		document.querySelector("#total2").innerHTML = ' ('+gridData.length+')';
		AUIGrid.setGridData(deptUser_myGridID, gridData);
		AUIGrid.removeAjaxLoader(deptUser_myGridID);
	});
}

function deptUser_EventHandler(event){
	
}

function deptTree_EventHandler(event) {
	if(event.type = 'cellClick'){
		preRequestData();
	}
}
</script>
<div class="semi_table4 pr20">
	<div class="seach_arm3">
		<div class="leftbt">
			<span style="font-weight:bold;font-size:15px;">DEPARTMENT</span>
		</div>
		<div class="rightbt">
		</div>
	</div>
	<div class="tree" style="height:100%">	
		<div class="grid_tree2" id="grid_tree2" style="height:100%">
		</div>
	</div>
</div>
<div class="semi_content3">
	<div class="seach_arm3">
		<div class="leftbt" style="width:75% !important">
			<span class="gridTitle"></span><span> - ${e3ps:getMessage('권한 제외 사용자 수')}</span><span id="total2"> (0)</span>
		</div>
		<div class="rightbt" style="width:25% !important">
			<input type="text" id="searchName2" class="w65" style="height:20px;" placeholder="검색"/>
			<%-- <button type="button" class="i_update" onclick="openUserPopup('', 'multi');">${e3ps:getMessage('추가')}</button>
			<button type="button" class="i_delete" onclick="deleteGroupUser();">${e3ps:getMessage('삭제')}</button> --%>
			<%-- <button type="button" class="full_delete" value="user" onclick="changeOption(this);">${e3ps:getMessage('사용자 보기')}</button> --%>
		</div>
	</div>
	<!-- //button -->
	<!-- 			<div class="list" id="grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
	<div class="list" id="grid_wrap2" style="height: 100%; border-top: 2px solid #74AF2A;"></div>
</div>