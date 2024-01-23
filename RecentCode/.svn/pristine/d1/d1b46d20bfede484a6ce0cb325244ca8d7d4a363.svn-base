<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	$("#bomitem_search_value").keypress(function(e){
		if(e.keyCode==13){
			bomitem_search();
		}
	});
	
	//팝업 리사이즈
	popupResize();
	
	//grid reset
	AUIGrid.destroy(bom_item_myGridID);
	
	//grid setting
	bom_item_createAUIGrid(bom_item_columnLayout);
	
	//get grid data
	bom_item_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var bom_item_myGridID;

//AUIGrid 칼럼 설정
var bom_item_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('품목 번호')}",			width:"*",		style:"AUIGrid_Left",
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
	{ dataField : "version",				headerText : "${e3ps:getMessage('버전')}",				width:"10%"},
	{ dataField : "name",				headerText : "${e3ps:getMessage('품목 명')}",			width:"30%",			style:"AUIGrid_Left",
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
	{ dataField : "stateName",				headerText : "${e3ps:getMessage('상태')}",				width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "unit",				headerText : "${e3ps:getMessage('단위')}",				width:"10%"},
	{ dataField : "quantity",			headerText : "${e3ps:getMessage('수량')}",				width:"10%"},
];

//AUIGrid 를 생성합니다.
function bom_item_createAUIGrid(bom_item_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showAutoNoDataMessage : false,
		
		rowIdField : "_$uid",
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
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
	bom_item_myGridID = AUIGrid.create("#bom_item_grid_wrap", bom_item_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(bom_item_myGridID, "cellClick", bom_item_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(bom_item_myGridID, gridData);
}

function bom_item_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	//AUIGrid.showAjaxLoader(bom_item_myGridID);
	var url = getURLString("/bom/getBomItemList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		var firstChildLength = 0;
		if(gridData[0].children){
			firstChildLength = gridData[0].children.length;
		}
		$("#total").html(gridData.length + firstChildLength);
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(bom_item_myGridID, gridData);

		AUIGrid.setAllCheckedRows(bom_item_myGridID, false);
		//AUIGrid.removeAjaxLoader(bom_item_myGridID);
		
		AUIGrid.expandAll(bom_item_myGridID);
	},true);
}

//셀 클릭 핸들러
function bom_item_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

//필터 초기화
function bom_item_resetFilter(){
    AUIGrid.clearFilterAll(bom_item_myGridID);
}

function bom_item_xlsxExport() {
	AUIGrid.setProperty(bom_item_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(bom_item_myGridID, exportProps);
}
//Bom Item 검색
function bomitem_search() {
	var bomitem = $("#bomitem_search_value").val();

	if(bomitem.trim() == "") {
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
	AUIGrid.search(bom_item_myGridID, "number", bomitem, options);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('BOM ITEM')}</span>
		(Total : <span id="total">0</span>)
	</div>
	<div class="rightbt">
<%-- 		<button type="button" class="s_bt03" onclick="bom_item_resetFilter();">${e3ps:getMessage('필터 초기화')}</button> --%>
		<button type="button" class="s_bt03" onclick="bom_item_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="bom_item_grid_wrap">
</div>