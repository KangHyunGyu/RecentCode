<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_distribute_myGridID);
	
	//grid setting
	rel_distribute_createAUIGrid(rel_distribute_columnLayout);
	
	//get grid data
	rel_distribute_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_distribute_myGridID;

//AUIGrid 칼럼 설정
var rel_distribute_columnLayout = [
	{ dataField : "distributeNumber",				headerText : "${e3ps:getMessage('배포 번호')}",			width:"10%",
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
	{ dataField : "distributeName",				headerText : "${e3ps:getMessage('배포 명')}",			width:"*",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "distributeTypeName",				headerText : "${e3ps:getMessage('타입')}",				width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "classification",			headerText : "${e3ps:getMessage('배포 분류')}",				width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "pjtNo",			headerText : "${e3ps:getMessage('프로젝트 번호')}",			width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "supplierName",			headerText : "${e3ps:getMessage('업체')}",			width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "createDateFormat",			headerText : "${e3ps:getMessage('작성일자')}",			width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "creator",			headerText : "${e3ps:getMessage('접수 담당자')}",			width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",			width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//AUIGrid 를 생성합니다.
function rel_distribute_createAUIGrid(rel_distribute_columnLayout) {
	
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
		
		enableMovingColumn : true,
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_distribute_myGridID = AUIGrid.create("#rel_distribute_grid_wrap", rel_distribute_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_distribute_myGridID, "cellClick", rel_distribute_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_distribute_myGridID, gridData);
}

function rel_distribute_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(rel_distribute_myGridID);
	var url = getURLString("/distribute/getRelatedDistribute");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_distribute_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_distribute_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_distribute_myGridID);
		
	});
}

//셀 클릭 핸들러
function rel_distribute_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.rowIdValue;
	
}

//필터 초기화
function rel_distribute_resetFilter(){
    AUIGrid.clearFilterAll(rel_distribute_myGridID);
}

function rel_distribute_xlsxExport() {
	AUIGrid.setProperty(rel_distribute_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_distribute_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('관련 배포')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_distribute_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_distribute_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_distribute_grid_wrap" style="height:${gridHeight}px">
</div>