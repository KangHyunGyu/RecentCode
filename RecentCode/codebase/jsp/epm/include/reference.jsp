<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(ref_myGridID);
	
	//grid setting
	ref_createAUIGrid(ref_columnLayout);
	
	//get grid data
	ref_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var ref_myGridID;

//AUIGrid 칼럼 설정
var ref_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('도면 번호')}",			width:"*",		style:"AUIGrid_Left",
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
	{ dataField : "thumbnail",		headerText : "도면",		width:"40",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : thumbnailRenderer
	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('도면 명')}",			width:"25%",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",				headerText : "${e3ps:getMessage('상태')}",				width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "version",			headerText : "${e3ps:getMessage('버전')}",				width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "creatorFullName",			headerText : "${e3ps:getMessage('등록자')}",			width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "modifyDate",			headerText : "${e3ps:getMessage('최종 수정일')}",			width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//AUIGrid 를 생성합니다.
function ref_createAUIGrid(ref_columnLayout) {
	
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
		
	};

	// 실제로 #grid_wrap 에 그리드 생성
	ref_myGridID = AUIGrid.create("#ref_grid_wrap", ref_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(ref_myGridID, "cellClick", ref_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(ref_myGridID, gridData);
}

function ref_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(ref_myGridID);
	var url = getURLString("/epm/getReference");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(ref_myGridID, gridData);

		AUIGrid.setAllCheckedRows(ref_myGridID, false);
		AUIGrid.removeAjaxLoader(ref_myGridID);
		
	});
}

//셀 클릭 핸들러
function ref_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

//필터 초기화
function ref_resetFilter(){
    AUIGrid.clearFilterAll(ref_myGridID);
}

function ref_xlsxExport() {
	AUIGrid.setProperty(ref_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(ref_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('참조')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="ref_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="ref_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="ref_grid_wrap" style="height:200px">
</div>