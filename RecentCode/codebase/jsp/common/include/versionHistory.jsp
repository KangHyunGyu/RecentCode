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
	AUIGrid.destroy(vh_myGridID);
	
	//grid setting
	vh_createAUIGrid(vh_columnLayout);
	
	//get grid data
	vh_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var vh_myGridID;

//AUIGrid 칼럼 설정
var vh_columnLayout = [
	{ dataField : "version",			headerText : "${e3ps:getMessage('버전')}",			width:"13%",	style:"AUIGrid_Left",
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
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "creatorFullName",			headerText : "${e3ps:getMessage('등록자')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "modifierFullName",			headerText : "${e3ps:getMessage('수정자')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "createDateToMinute",			headerText : "${e3ps:getMessage('등록일')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "modifyDateToMinute",			headerText : "${e3ps:getMessage('수정일')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "iterationNote",		headerText : "${e3ps:getMessage('이력 내용')}",		width:"*", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//AUIGrid 를 생성합니다.
function vh_createAUIGrid(vh_columnLayout) {
	
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
	vh_myGridID = AUIGrid.create("#vh_grid_wrap", vh_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(vh_myGridID, "cellClick", vh_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(vh_myGridID, gridData);
}

function vh_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(vh_myGridID);
	var url = getURLString("/common/getVersionHistory");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(vh_myGridID, gridData);

		AUIGrid.setAllCheckedRows(vh_myGridID, false);
		AUIGrid.removeAjaxLoader(vh_myGridID);
		
	});
}

//셀 클릭 핸들러
function vh_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.rowIdValue;
	
}

//필터 초기화
function vh_resetFilter(){
    AUIGrid.clearFilterAll(vh_myGridID);
}

function vh_xlsxExport() {
	AUIGrid.setProperty(vh_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(vh_myGridID, exportProps);
}
</script>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('버전 이력')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="vh_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="vh_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<div class="list" id="vh_grid_wrap" style="height:400px">
</div>
