<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//팝업 리사이즈
// 	popupResize();
	
	//grid reset
	AUIGrid.destroy(${type}_bom_part_myGridID);
	
	//grid setting
	${type}_bom_part_createAUIGrid(${type}_bom_part_columnLayout);
	
	//get grid data
	${type}_bom_part_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var ${type}_bom_part_myGridID;

//AUIGrid 칼럼 설정
var ${type}_bom_part_columnLayout = [
	{ dataField : "info",			headerText : "BOM",										width:"40",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openBomTree(\"" + item.oid + "\");'>"
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		}
	},
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
				
				openView(oid);
			}
		}		
	},
	{ dataField : "version",			headerText : "${e3ps:getMessage('버전')}",				width:"10%"},
	{ dataField : "name",				headerText : "${e3ps:getMessage('품목 명')}",			width:"40%",		style:"AUIGrid_Left",
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
	
];

//AUIGrid 를 생성합니다.
function ${type}_bom_part_createAUIGrid(${type}_bom_part_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		rowIdField : "_$uid",
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true
		
	};

	// 실제로 #grid_wrap 에 그리드 생성
	${type}_bom_part_myGridID = AUIGrid.create("#${type}_bom_part_grid_wrap", ${type}_bom_part_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(${type}_bom_part_myGridID, "cellClick", ${type}_bom_part_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(${type}_bom_part_myGridID, gridData);
}

function ${type}_bom_part_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.type = "${type}";
	
	AUIGrid.showAjaxLoader(${type}_bom_part_myGridID);
	var url = getURLString("/bom/getBomPartList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(${type}_bom_part_myGridID, gridData);

		AUIGrid.setAllCheckedRows(${type}_bom_part_myGridID, false);
		AUIGrid.removeAjaxLoader(${type}_bom_part_myGridID);
		
	});
}

//셀 클릭 핸들러
function ${type}_bom_part_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

//필터 초기화
function ${type}_bom_part_resetFilter(){
    AUIGrid.clearFilterAll(${type}_bom_part_myGridID);
}

function ${type}_bom_part_xlsxExport() {
	AUIGrid.setProperty(${type}_bom_part_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(${type}_bom_part_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${title}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="${type}_bom_part_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="${type}_bom_part_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="${type}_bom_part_grid_wrap" style="height:150px">
</div>