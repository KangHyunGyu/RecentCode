<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_eco_myGridID);
	
	//grid setting
	rel_eco_createAUIGrid(rel_eco_columnLayout);
	
	//get grid data
	rel_eco_getGridData();
	
// 	console.log("${oid};")
	
});

//AUIGrid 생성 후 반환 ID
var rel_eco_myGridID;

//AUIGrid 칼럼 설정
var rel_eco_columnLayout = [
	{ dataField : "orderNumber",		headerText : "${e3ps:getMessage('ECO 번호')}",		width:"12%",
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
	{ dataField : "name",		headerText : "${e3ps:getMessage('제목')}",		width:"*",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "state",			headerText : "${e3ps:getMessage('상태')}",			width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "createDate",		headerText : "${e3ps:getMessage('작성일')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "creator",		headerText : "${e3ps:getMessage('작성자')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "echangeReason",		headerText : "${e3ps:getMessage('설계 변경 사유')}",		width:"20%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//AUIGrid 를 생성합니다.
function rel_eco_createAUIGrid(rel_eco_columnLayout) {
	
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
	rel_eco_myGridID = AUIGrid.create("#rel_eco_grid_wrap", rel_eco_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_eco_myGridID, "cellClick", rel_eco_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_eco_myGridID, gridData);
}

function rel_eco_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(rel_eco_myGridID);
	var url = getURLString("/change/getRelatedECO");
	ajaxCallServer(url, param, function(data){
		
		// 그리드 데이터
		var gridData = data.list;
		console.log(gridData);
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_eco_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_eco_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_eco_myGridID);
		
	}); 
}

//셀 클릭 핸들러
function rel_eco_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

//필터 초기화
function rel_eco_resetFilter(){
    AUIGrid.clearFilterAll(rel_eco_myGridID);
}

function rel_eco_xlsxExport() {
	AUIGrid.setProperty(rel_eco_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_eco_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('관련 ECO')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_eco_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_eco_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_eco_grid_wrap" style="height:250px">
</div>