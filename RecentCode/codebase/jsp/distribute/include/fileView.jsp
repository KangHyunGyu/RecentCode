<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_fileView_myGridID);
	
	//grid setting
	rel_fileView_createAUIGrid(rel_fileView_columnLayout);
	
	//get grid data
	rel_fileView_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_fileView_myGridID;

//AUIGrid 칼럼 설정
var rel_fileView_columnLayout = [
	{ dataField : "main_obj_no",				headerText : "${e3ps:getMessage('접수 번호')}",			width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "file_name",				headerText : "${e3ps:getMessage('파일 명')}",			width:"*",		style:"AUIGrid_Left",
		renderer : {
	        type : "TemplateRenderer",
	    },
	    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { //HTML 템플릿 작성
	    	//var location = item.location.replace("D:\\CPC_FILE","").replace("/"+value,"");
	    	//var url = getURLString("/content/cpcFileDownload?location="+location+"&fileName="+value);
	    	var instance_id = item.instance_id;
	    	var url = getURLString("/content/cpcFileDownload?instance_id="+instance_id+"&fileName="+value);
	        
	    	return "<a href='"+ url +"'>" + value + "</a>";
	    }
	},
	{ dataField : "fileSize",				headerText : "${e3ps:getMessage('용량')}",			width:"13%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//AUIGrid 를 생성합니다.
function rel_fileView_createAUIGrid(rel_fileView_columnLayout) {
	
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
		
		groupingFields : ["main_obj_no"],
		enableCellMerge : true,
		showBranchOnGrouping : false,
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_fileView_myGridID = AUIGrid.create("#rel_fileView_grid_wrap", rel_fileView_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_fileView_myGridID, "cellClick", rel_fileView_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_fileView_myGridID, gridData);
	AUIGrid.setGroupBy(rel_fileView_myGridID, ["main_obj_no"] );
}

function rel_fileView_getGridData(){

	var param = new Object();
	
	param.instance_id = "${instance_id}";
	AUIGrid.showAjaxLoader(rel_fileView_myGridID);
	var url = getURLString("/distribute/getReceiptFileList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_fileView_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_fileView_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_fileView_myGridID);
		
	});
}

//셀 클릭 핸들러
function rel_fileView_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.rowIdValue;
	
}

//필터 초기화
function rel_fileView_resetFilter(){
    AUIGrid.clearFilterAll(rel_fileView_myGridID);
}

function rel_fileView_xlsxExport() {
	AUIGrid.setProperty(rel_fileView_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_fileView_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('첨부파일')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_fileView_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_fileView_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_fileView_grid_wrap" style="height:${gridHeight}px">
</div>