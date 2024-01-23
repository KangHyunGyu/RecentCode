<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_myGridID);
	
	//grid setting
	rel_createAUIGrid(rel_columnLayout);
	
	//get grid data
	rel_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_myGridID;

//AUIGrid 칼럼 설정
var rel_columnLayout = [
	{ dataField : "number",			headerText : "${e3ps:getMessage('번호')}",				width:"17%",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('이름')}",				width:"*",			style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "fileName",		headerText : "${e3ps:getMessage('파일 명')}",		width:"30%",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "isSendStr",				headerText : "${e3ps:getMessage('전송 여부')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}
	},
];

//AUIGrid 를 생성합니다.
function rel_createAUIGrid(rel_columnLayout) {
	
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
		
		groupingFields : ["fileTypeStr", "number", "name"],
		enableCellMerge : true,
		showBranchOnGrouping : false,
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_myGridID = AUIGrid.create("#rel_grid_wrap", rel_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_myGridID, gridData);
	AUIGrid.setGroupBy(rel_myGridID, ["fileTypeStr", "number", "name"] );
}

function rel_getGridData(){

	var param = new Object();
	param["oid"] = "${oid}"
	param["item_oid"] = "${item_oid}"
	param["sub_obj_no"] = "${sub_obj_no}"
	AUIGrid.showAjaxLoader(rel_myGridID);
	var url = getURLString("/distribute/getMergeFileList");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_myGridID);
		
	});
}

//필터 초기화
function rel_resetFilter(){
    AUIGrid.clearFilterAll(rel_myGridID);
}

function rel_xlsxExport() {
	AUIGrid.setProperty(rel_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('배포 파일')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_grid_wrap" style="height:${gridHeight}px">
</div>