<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_issue_myGridID);
	
	//grid setting
	rel_issue_createAUIGrid(rel_issue_columnLayout);
	
	//get grid data
	rel_issue_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_issue_myGridID;

//AUIGrid 칼럼 설정
var rel_issue_columnLayout = [
	{ dataField : "ecr_number",		headerText : "${e3ps:getMessage('ECR 번호')}",		width:"*%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "issue_number",		headerText : "${e3ps:getMessage('ISSUE 번호')}",		width:"20%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "if_cdateStr",		headerText : "${e3ps:getMessage('I/F 생성일')}",		width:"20%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "if_rdateStr",		headerText : "${e3ps:getMessage('I/F 수령일')}",		width:"20%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "if_statusStr",		headerText : "${e3ps:getMessage('I/F 상태')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
];

//AUIGrid 를 생성합니다.
function rel_issue_createAUIGrid(rel_issue_columnLayout) {
	
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
	rel_issue_myGridID = AUIGrid.create("#rel_issue_grid_wrap", rel_issue_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_issue_myGridID, gridData);
}

function rel_issue_getGridData(){
	var param = new Object();
	
	param.ecr_number = "${ecr_number}";
	
	AUIGrid.showAjaxLoader(rel_issue_myGridID);
	var url = getURLString("/erp/getRelatedECRIssue");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_issue_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_issue_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_issue_myGridID);
		
	});
}

//필터 초기화
function rel_issue_resetFilter(){
    AUIGrid.clearFilterAll(rel_issue_myGridID);
}

function rel_issue_xlsxExport() {
	AUIGrid.setProperty(rel_issue_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_issue_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('관련 ECR ISSUE')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_issue_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_issue_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_issue_grid_wrap" style="height:${gridHeight}px">
</div>