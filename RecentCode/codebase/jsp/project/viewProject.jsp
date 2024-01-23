﻿﻿<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<input type="hidden" id="taskOid" name="taskOid" value="${taskOid }"/>
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title">${e3ps:getMessage('프로젝트')}</span>
		</div>
	</div>
	<div>
		<div class="semi_content pl30 pr30">
			<div class="semi_table1 pr20">
				<div class="tree">	
					<div class="tree_top pb10">
						<div class="tree_t01 pt5">
							<p><img class="pointer" onclick="getMasteredLinkList('part')" src="/Windchill/jsp/project/images/img/bt_12.png"></p><!-- 품목 -->
							<%--<p><img class="pointer" onclick="getMasteredLinkList('drawing')" src="/Windchill/jsp/project/images/img/bt_13.png"></p><!-- 도면 --> --%>
							<p><img class="pointer" onclick="getProjectOutputList()" src="/Windchill/jsp/project/images/img/bt_14.png"></p><!-- 산출물 -->
							<c:if test="${isEditState and isAuth}">
								<p><img class="pointer" onclick="showGanttChartEditor()" src="/Windchill/jsp/project/images/img/bt_15.png"></p><!-- 태스크 -->
							</c:if>
							<p><img class="pointer" onclick="javascript:reloadTree();viewProject();" src="/Windchill/jsp/project/images/img/bt_16.png"></p><!-- 고침 -->
							<%-- <c:if test="${sg eq true and isAuth}">
								<p><button type="button" class="s_bt03" style="height: 43px; width: 33.99px;" onclick="javascript:viewStageGate()">${e3ps:getMessage('GR')}</button></p>
							</c:if> --%>
<%-- 							<c:if test="${sg eq false and isAuth}"> --%>
<%-- 								<p><button type="button" class="s_bt03" style="height: 43px; width: 33.99px;" onclick="javascript:createStageGate()">${e3ps:getMessage('C')}</button></p> --%>
<%-- 							</c:if> --%>
		   				</div>
						<div class="tree_t01 pt15">
							<p> 
								<select id="level" onchange="expendTree(this.value)">
									<option value="">${e3ps:getMessage('열기')}</option>
									<option value="1">1 Level</option>
									<option value="2">2 Level</option>
									<option value="3">3 Level</option>
									<option value="ALL">${e3ps:getMessage('전체')} Level</option>
								</select>
								<!-- <button type="button" class="s_bt07" onclick="JavaScript:excelReport();">엑셀</button> -->
							</p>
						</div>
						<div class="tree_t03 pt10" id="iconDiv">
							<p><img src="/Windchill/jsp/project/images/img/state_g.png">${e3ps:getMessage('완료')}</p>
							<p><img src="/Windchill/jsp/project/images/img/state_b.png">${e3ps:getMessage('진행')}</p>
							<p><img src="/Windchill/jsp/project/images/img/state_r.png">${e3ps:getMessage('지연')}</p>
							<p><img src="/Windchill/jsp/project/images/img/state_d.png">${e3ps:getMessage('예정')}</p>
						</div>
						<c:if test="${isPSO}">
							<div class="tree_t01 pt10">
								<p><input type="button" value="${e3ps:getMessage('기본')}"  class="s_bt03" onclick="javascript:viewDefaultTree()"></p>
								<p><input type="button" value="PSO" class="s_bt03" onclick="viewStepTree(this)"></p>
							</div>
						</c:if>
					</div>
					<div class="projectTree" id="projectTree">
					</div>
				</div>
			</div>
			<div class="semi_content2" id="includePage">
			</div>
		</div>
	</div>
</div>
<style type="text/css">
.tree-column-3 > div {
	text-align:right;
	padding-left:15px !important;
}
</style>
<script>

$(document).ready(function(){
	
	createProjectTreeGrid(tree_columnLayout);
	
	getProjectTree();
	
	
	if($("#taskOid").val() == ""){
		viewProject();
	}else{
		projectTree_itemClick('${taskOid}', '','');
		
	}
});

var tree_myGridID;

//부서 트리 그리드 레이아웃
var tree_columnLayout = [ {
	dataField : "name",
	headerText : "Name",
	width : "100%",
	style : "pointer",
	styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
		//console.log(item._$depth);
		if(item._$depth > 2){
			//console.log(item._$depth);
			return "tree-column-3";
		}
		if(item.disable) {
			return "isDisable";
		}
		return null;
	}
}];

function createProjectTreeGrid(tree_columnLayout){
	tree_auiGridProps = {
		rowIdField : "oid", 
		treeIdField : "oid",				// 트리의 고유 필드명
		treeIdRefField : "parentId", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
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
		treeIconFunction : function(rowIndex, isBranch, isOpen, depth, item) {
			if(depth > 2){
				
			}
			//console.log(item.iconUrl);
			return item.iconUrl;	
			//item.
			//return "{common.icon()} <img src='#iconUrl#'> <span>#name#</span>;
		},
		//template:"{common.icon()} <img src='#iconUrl#'> <span>#name#</span>",
	      
		//autoGridHeight : ${autoGridHeight},
		//"{common.icon()} <img src='#iconUrl#'> <span>#name#</span>",
	    
	    height : 600,
	};
	
	tree_myGridID = AUIGrid.create("#projectTree", tree_columnLayout, tree_auiGridProps);
	
	// 그리드 ready 이벤트 바인딩 폴더 초기화면 설정
	AUIGrid.bind(tree_myGridID, "ready", tree_auiGridCompleteHandler);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(tree_myGridID, "cellClick", tree_auiGridCellClickHandler);
	
	// 선택 변경 이벤트 바인딩
	AUIGrid.bind(tree_myGridID, "selectionChange", tree_auiGridSelectionChangeHandler);
	
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

//셀 클릭 핸들러
function tree_auiGridCellClickHandler(event) {
	//console.log("tree_auiGridCellClickHandler");
	projectTree_itemClick(event.item.oid, event, null);
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

//트리 불러오기
function getProjectTree(){
	var param = new Object();
	
	param.oid = "${oid}"
	
	var url = getURLString("/project/getProjectTreeAUI");
	ajaxCallServer(url, param, function(data){
		//console.log("tree data:",data.list);
		var tree_gridData = data.list;
		AUIGrid.setGridData(tree_myGridID, tree_gridData);
	});
};

//Step 트리 불러오기
function getStepTree(outputType){
	var param = new Object();
	
	param.oid = "${oid}"
	param.outputType = outputType;
	
	$$("projectTree").clearAll();
	$$("projectTree").showProgress();
	var url = getURLString("/project/getStepTree");
	ajaxCallServer(url, param, function(data){
		
		var list = data.list;
		
		$$("projectTree").parse(list);
		$$("projectTree").openAll();
		$$("projectTree").hideProgress();
	});
};

function loadIncludePage(url, param) {
	
	if(param == null) {
		param = new Object();
		
		param.oid = "${oid}";
	}
	//console.log("pa : " +param.oid);
	//console.log("pa : " +url);
	param.isAuth = ${isAuth};
	param.isEditState = ${isEditState};
	
	$("#includePage").load(url, param);
}

function getMasteredLinkList(type) {
	var url = getURLString("/project/masteredLinkList");
	
	var param = new Object();
	
	param.oid = "${oid}";
	param.type = type;
	
	loadIncludePage(url, param);
}

function getProjectOutputList() {
	var url = getURLString("/project/projectOutputList");
	
	var param = new Object();
	
	param.oid = "${oid}";
	
	loadIncludePage(url, param);
}

function reloadTree() {
	$("#level").val("");
	getProjectTree();
}

function projectTree_itemClick(id, event, node) {
	//console.log("test   ",id, event, node);
	if(id == ""){
		return;
	}
	
	var oid = id;
	var param = new Object();
	var url = ""; 
	if(oid.indexOf("EProject") > 0) {//EProject
		url = getURLString("/project/view");
	} else if(oid.indexOf("ETask") > 0) {	//ETask
		url = getURLString("/project/viewTask");
	} else {
		return;
	}
	param.oid = oid;
	loadIncludePage(url, param);
}

function expendTree(value) {
	if(value == "") return;
	
	if(value != "ALL") {
		//AUIGrid.collapseAll(tree_myGridID);
		
		AUIGrid.showItemsOnDepth(tree_myGridID, value);
		
		/* for(value ; value > -1; value--) {
			$("#projectTree").filter("#$level#",Number(value) + 1);
		} */
	} else {
		AUIGrid.expandAll(tree_myGridID);
	}
}

function viewProject(){
	loadIncludePage(getURLString("/project/view"));
}

function viewTask(oid){
	var url = getURLString("/project/viewTask");
	var param = new Object();
	param.oid = oid;
	loadIncludePage(url, param);
}

function taskEditPop(){
	var url = "/Windchill/jsp/project/EditTaskPlan.jsp?oid=${oid}";
	openPopup(url,"taskEditPop","1000","600");
}

function viewDefaultTree() {
	$("#iconDiv").show();
	reloadTree();
	viewProject();
}

function viewStepTree(button) {
	$("#iconDiv").hide();
	getStepTree($(button).val());
}

function updateTaskData(task){
	$("#projectTree").updateItem(task.id, task);
}

function showGanttChartEditor(){
	var url = getURLString("/project/editGanttChart") + "?oid=${oid}";
	openPopup(url,"editGanttChart","1200","600");
}
</script>