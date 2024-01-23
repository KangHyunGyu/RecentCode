<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	$("#searchBOMPartNo").keypress(function(e){
		if(e.keyCode==13){
			bomTree_search();
		}
	})
	
	//팝업 리사이즈
	popupResize();
	
	//grid reset
	AUIGrid.destroy(bomTree_myGridID);
	
	//grid setting
	bomTree_createAUIGrid(bomTree_columnLayout);
	
	//get grid data
	bomTree_getGridData();
	
	
});

//AUIGrid 생성 후 반환 ID
var bomTree_myGridID;

//AUIGrid 칼럼 설정
var bomTree_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('부품 번호')}",			width:"20%",		style:"AUIGrid_Left",
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
				
				openOldBomTree(oid);
			}
		}
	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('부품 명')}",				width:"20%",		style:"AUIGrid_Left",
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
				
				openView(oid);
			}
		}	
	},
	{ dataField : "version",			headerText : "${e3ps:getMessage('버전')}",				width:"7%"},
	{ dataField : "level",				headerText : "level",									width:"70",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "info",			headerText : "",										width:"40",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openOldBomTree(\"" + item.oid + "\");'>"
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		}
	},
	{ dataField : "quantity",			headerText : "${e3ps:getMessage('수량')}",				width:"4%"},
	{ dataField : "isPurchase",			headerText : "${e3ps:getMessage('구매 여부')}",				width:"7%"},
	{ dataField : "isDocRef",			headerText : "${e3ps:getMessage('참조 여부')}",				width:"7%"},
	/* { dataField : "state",				headerText : "${e3ps:getMessage('상태')}",				width:"6%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	}, */
	{ dataField : "unit",				headerText : "${e3ps:getMessage('단위')}",				width:"6%"},
	{ dataField : "specification",				headerText : "${e3ps:getMessage('규격')}",				width:"12%"},
	{ dataField : "partType",				headerText : "${e3ps:getMessage('자재 타입')}",				width:"8%"},
];

//AUIGrid 를 생성합니다.
function bomTree_createAUIGrid(bomTree_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		rowIdField : "treeId", 
		
		treeIdField : "treeId",				// 트리의 고유 필드명
		
		treeIdRefField : "parent", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
			
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		// 일반 데이터를 트리로 표현할지 여부(treeIdField, treeIdRefField 설정 필수)
		flat2tree : true,
		
		// 엑스트라 행 체크박스 칼럼이 트리 의존적인지 여부
		// 트리 의존적인 경우, 부모를 체크하면 자식도 체크됨.
		rowCheckDependingTree : true,
		
		// 트리그리드에서 하위 데이터를 나중에 요청하기 위한 true 설정
		treeLazyMode : true,

		fixedColumnCount : 6,
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
	};

	// 트리 아이콘을 바꿀 수 있는 트리 아이콘 펑션입니다.
	gridPros.treeIconFunction = function(rowIndex, isBranch, isOpen, depth, item) {
		var imgSrc = null;
		
		//checkoutState 에 때라 이미지 변경 필요
		imgSrc = "/Windchill/wtcore/images/part.gif";
		
		return imgSrc;
	};
	
	// 실제로 #grid_wrap 에 그리드 생성
	bomTree_myGridID = AUIGrid.create("#bomTree_grid_wrap", bomTree_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(bomTree_myGridID, "cellClick", bomTree_auiGridCellClickHandler);
	
	// 트리그리드 lazyLoading 요청 이벤트 핸들러 
	AUIGrid.bind(bomTree_myGridID, "treeLazyRequest", bomTree_auiGridTreeLazeRequestHandler);
	var gridData = new Array();
	AUIGrid.setGridData(bomTree_myGridID, gridData);
}

function bomTree_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.desc = $("#desc").val();
	param.initLevel = $("#initLevel").val();
	
	//AUIGrid.showAjaxLoader(bomTree_myGridID);
	var url = getURLString("/bom/getOldBomRoot");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;

		// 그리드에 데이터 세팅
		AUIGrid.setGridData(bomTree_myGridID, gridData);

		AUIGrid.expandAll(bomTree_myGridID);
		
		//AUIGrid.removeAjaxLoader(bomTree_myGridID);
		
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
	},true);
}

//셀 클릭 핸들러
function bomTree_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
	if("thumbnail" == dataField){
		openMiniWVSPopup(event.item.minPublishURL);
	}
}

//lazyLoading 핸들러
function bomTree_auiGridTreeLazeRequestHandler(event) {
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
	
	var url = getURLString("/bom/getOldBomChildren");
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
		AUIGrid.showItemsOnDepth(bomTree_myGridID, Number(openLevel) + 1);	
	} else {
		AUIGrid.expandAll(bomTree_myGridID);
	}
	
};

//필터 초기화
function bomTree_resetFilter(){
    AUIGrid.clearFilterAll(bomTree_myGridID);
}

function bomTree_xlsxExport() {
	AUIGrid.setProperty(bomTree_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(bomTree_myGridID, exportProps);
}

function bomTree_search() {
	var searchBOMPartNo = $("#searchBOMPartNo").val();

	if(searchBOMPartNo.trim() == "") {
		openNotice("${e3ps:getMessage('검색할 단어를 입력하십시오.')}");
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
	AUIGrid.search(bomTree_myGridID, "number", searchBOMPartNo, options);
	
}
</script>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">BOM</span>
		<input style="width:120px" type="text" id="searchBOMPartNo" name="searchBOMPartNo">
		<button type="button" class="s_bt03" onclick="javascript:bomTree_search();">${e3ps:getMessage('검색')}</button>
	</div>
	<div class="rightbt">
		<select id="desc" name="desc" style="width:70px;" onchange="javascript:bomTree_getGridData()">
			<option value="true">${e3ps:getMessage('정전개')}</option>
			<option value="false">${e3ps:getMessage('역전개')}</option>
		</select>
		<select id="initLevel" name="initLevel" style="width:70px;" onchange="javascript:bomTree_getGridData();">
			<option value="1">1 Level</option>
			<option value="2">2 Level</option>
			<option value="3">3 Level</option>
			<option value="4">4 Level</option>
			<option value="ALL">ALL</option>
		</select>
		<select id="openLevel" name="openLevel" style="width:130px;" onchange="javascript:showItemsOnLevel();">
		</select>
<%-- 		<button type="button" class="s_bt03" onclick="bomTree_resetFilter();">${e3ps:getMessage('필터 초기화')}</button> --%>
		<button type="button" class="s_bt03" onclick="bomTree_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		<img class="pointer mb5" title="${e3ps:getMessage('일괄 등록 양식으로 BOM 다운로드')}" onclick="oldBomExcelDown('${oid}');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
	</div>
</div>
<div class="list" id="bomTree_grid_wrap" style="height:${gridHeight}px">
</div>