<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_esti_myGridID);
	
	//grid setting
	rel_esti_createAUIGrid(rel_esti_columnLayout);
	
	//get grid data
	rel_esti_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_esti_myGridID;

//AUIGrid 칼럼 설정
var rel_esti_columnLayout = [
	{ dataField : "request_number",		headerText : "${e3ps:getMessage('견적서 번호')}",		width:"13%",	
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "pjt_number",		headerText : "${e3ps:getMessage('프로젝트 번호')}",		width:"13%",	
		filter : {
			showIcon : true,
			iconWidth:30
		}		
	},
	{ dataField : "part_number",		headerText : "${e3ps:getMessage('견적의뢰서 부품 번호')}",		width:"*",	style:"AUIGrid_Left", 
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "part_version",		headerText : "${e3ps:getMessage('견적의뢰서 부품 버전')}",		width:"14%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "request_date",		headerText : "${e3ps:getMessage('견적서 마감일')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "if_cdateStr",		headerText : "${e3ps:getMessage('I/F 생성일')}",		width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "if_rdateStr",		headerText : "${e3ps:getMessage('I/F 수령일')}",		width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}		
	},
	{ dataField : "if_statusStr",		headerText : "${e3ps:getMessage('I/F 상태')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//AUIGrid 를 생성합니다.
function rel_esti_createAUIGrid(rel_esti_columnLayout) {
	
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
	rel_esti_myGridID = AUIGrid.create("#rel_esti_grid_wrap", rel_esti_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_esti_myGridID, gridData);
}

function rel_esti_getGridData(){
	var param = new Object();
	
	param.instance_id = "${instance_id}";
	
	AUIGrid.showAjaxLoader(rel_esti_myGridID);
	var url = getURLString("/erp/getRelatedEstimateList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_esti_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_esti_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_esti_myGridID);
		
	});
}

//필터 초기화
function rel_esti_resetFilter(){
    AUIGrid.clearFilterAll(rel_esti_myGridID);
}

function rel_esti_xlsxExport() {
	AUIGrid.setProperty(rel_esti_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_esti_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('관련 구매 발주 부품')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_esti_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_esti_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_esti_grid_wrap" style="height:${gridHeight}px">
</div>