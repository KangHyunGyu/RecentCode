<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	AUIGrid.destroy(rel_doc_myGridID);
	
	//grid stting
	ref_doc_createAUIGrid(ref_doc_columnLayout);
	
	//get grid data
	ref_doc_getGridData();
	
});

function ref_doc_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(rel_doc_myGridID);
	var url = getURLString("/doc/getRelatedDoc");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_doc_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_doc_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_doc_myGridID);
		
	});
}

function rel_doc_xlsxExport() {
	AUIGrid.setProperty(rel_doc_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_doc_myGridID, exportProps);
}

//필터 초기화
function rel_doc_resetFilter(){
	AUIGrid.clearFilterAll(rel_doc_myGridID);
}

//AUIGrid 를 생성합니다.
function ref_doc_createAUIGrid(ref_doc_columnLayout) {
	
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
	rel_doc_myGridID = AUIGrid.create("#ref_doc_grid_wrap", ref_doc_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_doc_myGridID, "cellClick", ref_doc_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_doc_myGridID, gridData);
}

//셀 클릭 핸들러
function ref_doc_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

//필터 초기화
function resetFilter(){
    AUIGrid.clearFilterAll(rel_doc_myGridID);
}

var rel_doc_myGridID;

var ref_doc_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('문서 번호')}",			width:"*",		style:"AUIGrid_Left",
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
	{ dataField : "name",				headerText : "${e3ps:getMessage('문서 명')}",			width:"20%",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",				headerText : "${e3ps:getMessage('상태')}",				width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "version",			headerText : "${e3ps:getMessage('버전')}",				width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "creatorFullName",			headerText : "${e3ps:getMessage('등록자')}",			width:"10%",
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

</script>

<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('관련 문서')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_doc_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="ref_doc_grid_wrap" style="height:200px">
</div>
