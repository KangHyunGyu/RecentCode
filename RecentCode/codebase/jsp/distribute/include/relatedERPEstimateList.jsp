<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_estimate_myGridID);
	
	//grid setting
	rel_estimate_createAUIGrid(rel_estimate_columnLayout);
	
	//get grid data
	rel_estimate_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_estimate_myGridID;

//AUIGrid 칼럼 설정
var rel_estimate_columnLayout = [
	{ dataField : "request_number",		headerText : "${e3ps:getMessage('견적서 번호')}",		width:"*",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.instance_id;
				var url = getURLString("/erp/viewEstimateInfo?instance_id="+oid)
				openPopup(url, oid);
			}
		}
	},
	{ dataField : "company_id",		headerText : "${e3ps:getMessage('협력업체 코드')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}		
	},
	{ dataField : "request_user_name",		headerText : "${e3ps:getMessage('견적 요청자 이름')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "request_user_id",		headerText : "${e3ps:getMessage('견적 요청자 사번')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "request_user_email",		headerText : "${e3ps:getMessage('견적 요청자 e-mail')}",		width:"18%",	style:"AUIGrid_Left", 
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "estimate_stateStr",		headerText : "${e3ps:getMessage('견적서 상태')}",		width:"9%",
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
	{ dataField : "if_statusStr",		headerText : "${e3ps:getMessage('I/F 상태')}",		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//AUIGrid 를 생성합니다.
function rel_estimate_createAUIGrid(rel_estimate_columnLayout) {
	
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
		
		//fixedColumnCount : 4,
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_estimate_myGridID = AUIGrid.create("#rel_estimate_grid_wrap", rel_estimate_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_estimate_myGridID, "cellClick", rel_estimate_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_estimate_myGridID, gridData);
}

function rel_estimate_getGridData(){

	var param = new Object();
	
	param.requestNumber = "${requestNumber}";
	
	AUIGrid.showAjaxLoader(rel_estimate_myGridID);
	var url = getURLString("/distribute/getRelatedERPEstimateList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_estimate_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_estimate_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_estimate_myGridID);
		
	});
}

//셀 클릭 핸들러
function rel_estimate_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

//필터 초기화
function rel_estimate_resetFilter(){
    AUIGrid.clearFilterAll(rel_estimate_myGridID);
}

function rel_estimate_xlsxExport() {
	AUIGrid.setProperty(rel_estimate_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_estimate_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('ERP History')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_estimate_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_estimate_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_estimate_grid_wrap" style="height:${gridHeight}px">
</div>