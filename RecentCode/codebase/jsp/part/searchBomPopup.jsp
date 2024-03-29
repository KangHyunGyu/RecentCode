<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//grid setting
	add_bomTree_createAUIGrid(add_bomTree_columnLayout);
	
	//get grid data
	add_bomTree_getGridData();
	
	
});

//AUIGrid 생성 후 반환 ID
var add_bomTree_myGridID;

//AUIGrid 칼럼 설정
var add_bomTree_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('부품 번호')}",			width:"*",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.oid;
				
				openBomTree(oid);
			}
		}
	},
	{ dataField : "level",				headerText : "level",									width:"70",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "thumbnail",			headerText : "",										width:"40",		style:"pointer",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : thumbnailRenderer	
	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('부품 명')}",				width:"30%",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",				headerText : "${e3ps:getMessage('상태')}",				width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "version",			headerText : "${e3ps:getMessage('버전')}",				width:"10%"},
	{ dataField : "unit",				headerText : "${e3ps:getMessage('단위')}",				width:"10%"},
	{ dataField : "quantity",			headerText : "${e3ps:getMessage('수량')}",				width:"10%"},
];

//AUIGrid 를 생성합니다.
function add_bomTree_createAUIGrid(add_bomTree_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		rowIdField : "treeId", 
		
		treeIdField : "treeId",				// 트리의 고유 필드명
		
		treeIdRefField : "parent", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
			
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showRowNumColumn : false,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : true,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		// 일반 데이터를 트리로 표현할지 여부(treeIdField, treeIdRefField 설정 필수)
		flat2tree : true,
		
		// 엑스트라 행 체크박스 칼럼이 트리 의존적인지 여부
		// 트리 의존적인 경우, 부모를 체크하면 자식도 체크됨.
		rowCheckDependingTree : false,
		
		// 트리그리드에서 하위 데이터를 나중에 요청하기 위한 true 설정
		treeLazyMode : true,
		
		rowCheckableFunction : function(rowIndex, isChecked, item) {
			if("${moduleType}".length > 0) {
				if("${moduleType}" == "multiApproval") {
					if(item.state != "INWORK") {
						return false;
					}
				}
			}
			return true;
		},
		
		rowCheckDisabledFunction : function(rowIndex, isChecked, item) {
			if("${moduleType}".length > 0) {
				if("${moduleType}" == "multiApproval") {
					if(item.state != "INWORK") {
						return false;
					}
				}
			}
			return true;
		}

	};

	// 트리 아이콘을 바꿀 수 있는 트리 아이콘 펑션입니다.
	gridPros.treeIconFunction = function(rowIndex, isBranch, isOpen, depth, item) {
		var imgSrc = null;
		
		//checkoutState 에 때라 이미지 변경 필요
		imgSrc = "/Windchill/wtcore/images/part.gif";
		
		return imgSrc;
	};
	
	// 실제로 #grid_wrap 에 그리드 생성
	add_bomTree_myGridID = AUIGrid.create("#add_bomTree_grid_wrap", add_bomTree_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(add_bomTree_myGridID, "cellClick", add_bomTree_auiGridCellClickHandler);
	
	// 트리그리드 lazyLoading 요청 이벤트 핸들러 
	AUIGrid.bind(add_bomTree_myGridID, "treeLazyRequest", add_bomTree_auiGridTreeLazeRequestHandler);
	
	// 전체 체크박스 클릭 이벤트 바인딩
	AUIGrid.bind(add_bomTree_myGridID, "rowAllChkClick", function( event ) {
		console.log(event);
		if("${moduleType}".length > 0) {
			if("${moduleType}" == "multiApproval") {
				if(event.checked) {
					AUIGrid.setCheckedRowsByValue(event.pid, "state", ["INWORK"]);
				} else {
					AUIGrid.setCheckedRowsByValue(event.pid, "state", []);
				}
			}
		}
		
	});
	
	var gridData = new Array();
	AUIGrid.setGridData(add_bomTree_myGridID, gridData);
}

function add_bomTree_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.desc = $("#desc").val();
	param.initLevel = $("#initLevel").val();
	
	AUIGrid.showAjaxLoader(add_bomTree_myGridID);
	var url = getURLString("/bom/getBomRoot");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;

		// 그리드에 데이터 세팅
		AUIGrid.setGridData(add_bomTree_myGridID, gridData);

		AUIGrid.expandAll(add_bomTree_myGridID);
		
		AUIGrid.removeAjaxLoader(add_bomTree_myGridID);
		
		$("#openLevel option").remove();
		
		var maxLevel = $("#initLevel").val();
		if($("#initLevel").val() == "ALL") {
			maxLevel = 4;
		}
		
		for(var i=1; i <= maxLevel; i++) {
			var selected = "";
			if(i == maxLevel) {
				selected = "selected";
			}
			$("#openLevel").append($("<option>", { 
				value: i,
				text : i + "레벨까지 보이기",
				selected : selected
			}));
		}
		
		if($("#initLevel").val() == "ALL") {
			$("#openLevel").append($("<option>", { 
				value: "ALL",
				text : "ALL",
			}));
		}
	});
}

//셀 클릭 핸들러
function add_bomTree_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
	if("thumbnail" == dataField){
		openMiniWVSPopup(event.item.minPublishURL);
	}
}

//lazyLoading 핸들러
function add_bomTree_auiGridTreeLazeRequestHandler(event) {
	var item = event.item;

	var oid = item.oid;
	var level = item.level;
	var treeId = item.treeId;
	
	// 자식 데이터 요청
	var param = new Object();
	
	param.oid = oid;
	param.level = level;
	param.treeId = treeId;
	param.desc = $("#desc").val();
	
	var url = getURLString("/bom/getBomChildren");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var childrenList = data.list;
		
		// 성공 시 완전한 배열 객체로 삽입하십시오.
		event.response(childrenList);
	});
}

function showItemsOnLevel(event) {
	var openLevel = $("#openLevel").val();
	
	// 해당 depth 까지 오픈함
	if(openLevel != "ALL") {
		AUIGrid.showItemsOnDepth(add_bomTree_myGridID, Number(openLevel) + 1);	
	} else {
		AUIGrid.expandAll(add_bomTree_myGridID);
	}
};

//검색조건 초기화
function reset(){
	var locationDisplay = $("#locationDisplay").val();
	$("#searchForm")[0].reset();
	$("#locationDisplay").val(locationDisplay);
	
}

//필터 초기화
function add_bomTree_resetFilter(){
    AUIGrid.clearFilterAll(add_bomTree_myGridID);
}

function add_bomTree_xlsxExport() {
	AUIGrid.setProperty(add_bomTree_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	// 엑셀 내보내기 속성
	var exportProps = {
		postToServer : true,
	};
	// 내보내기 실행
	AUIGrid.exportToXlsx(add_bomTree_myGridID, exportProps);
}

function addPart(){
	var checkItemList = AUIGrid.getCheckedRowItems(add_bomTree_myGridID);
	
	var addItemList = new Array();
	
	for(var i = 0; i < checkItemList.length; i++){
		addItemList.push(checkItemList[i].item);
	}

	if(opener.window.add_${pageName}_addObjectList){
		opener.window.add_${pageName}_addObjectList(addItemList);
	}
}
</script>
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${part.icon} ${e3ps:getMessage('부품')} - ${part.number}, ${part.name}, ${part.version}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<div class="con pl25 pr25 pb15">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt">
				<h3><img src="/Windchill/jsp/portal/images/minus_icon.png">BOM</h3>
			</div>
			<div class="rightbt">
				<select id="desc" name="desc" style="width:100px;" onchange="javascript:add_bomTree_getGridData()">
					<option value="true">${e3ps:getMessage('정전개')}</option>
					<option value="false">${e3ps:getMessage('역전개')}</option>
				</select>
				<select id="initLevel" name="initLevel" style="width:80px;" onchange="javascript:add_bomTree_getGridData();">
					<option value="1">1 Level</option>
					<option value="2">2 Level</option>
					<option value="3">3 Level</option>
					<option value="4">4 Level</option>
					<option value="ALL">ALL</option>
				</select>
				<select id="openLevel" name="openLevel" style="width:150px;" onchange="javascript:showItemsOnLevel();">
				</select>
				<button type="button" class="s_bt03" onclick="javascript:addPart()">${e3ps:getMessage('추가')}</button>
			</div>
		</div>
		<div class="list" id="add_bomTree_grid_wrap" style="height:500px">
		</div>
	</div>
</div>