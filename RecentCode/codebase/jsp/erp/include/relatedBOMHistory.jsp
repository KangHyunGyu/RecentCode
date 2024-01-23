<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_bom_myGridID);
	
	//grid setting
	rel_bom_createAUIGrid(rel_bom_columnLayout);
	
	//get grid data
	rel_bom_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_bom_myGridID;

//AUIGrid 칼럼 설정
var rel_bom_columnLayout = [
	{ dataField : "pjt_number",		headerText : "${e3ps:getMessage('프로젝트 번호')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "product_number",		headerText : "${e3ps:getMessage('장비 번호')}",		width:"13%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "ecr_number",		headerText : "${e3ps:getMessage('ECR 번호')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "parent_number",		headerText : "${e3ps:getMessage('모 ITEM 번호')}",		width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "parent_version",		headerText : "${e3ps:getMessage('모 ITEM 버전')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "child_number",		headerText : "${e3ps:getMessage('자 ITEM 번호')}",		width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "child_version",		headerText : "${e3ps:getMessage('자 ITEM 버전')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "child_quantity",		headerText : "${e3ps:getMessage('자 ITEM 수량')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "bom_location",		headerText : "${e3ps:getMessage('경로')}",		width:"25%", style:"AUIGrid_Left", 
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "if_cdateStr",		headerText : "${e3ps:getMessage('I/F 생성일')}",		width:"13%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "if_rdateStr",		headerText : "${e3ps:getMessage('I/F 수령일')}",		width:"13%",
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
function rel_bom_createAUIGrid(rel_bom_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		rowIdField : "_$uid", 
		
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
	rel_bom_myGridID = AUIGrid.create("#rel_bom_grid_wrap", rel_bom_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_bom_myGridID, gridData);
}

function rel_bom_getGridData(){
	var param = new Object();
	
	param.instance_id = "${instance_id}";
	param.pjt_number = "${pjt_number}";
	
	AUIGrid.showAjaxLoader(rel_bom_myGridID);
	var url = getURLString("/erp/getRelatedBOMHistory");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_bom_myGridID, gridData);

		AUIGrid.expandAll(rel_bom_myGridID);
		
		AUIGrid.removeAjaxLoader(rel_bom_myGridID);
	});
}

//필터 초기화
function rel_bom_resetFilter(){
    AUIGrid.clearFilterAll(rel_bom_myGridID);
}

function rel_bom_xlsxExport() {
	AUIGrid.setProperty(rel_bom_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_bom_myGridID, exportProps);
}
</script>
<!-- button -->
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>History - ${ecr_number}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->
	
	<div class="con pl25 pr25 pb15">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('관련 BOM History')}</h4></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="rel_bom_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
				<button type="button" class="s_bt03" onclick="rel_bom_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
			</div>
		</div>
		<!-- //button -->
		<div class="list" id="rel_bom_grid_wrap" style="height:${gridHeight}px">
		</div>
	</div>
</div>		
<!-- //pop-->

