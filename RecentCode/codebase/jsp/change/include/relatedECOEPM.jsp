<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_epm_myGridID);
	
	//grid setting
	rel_epm_createAUIGrid(rel_epm_columnLayout);
	
	//get grid data
	rel_epm_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_epm_myGridID;

//AUIGrid 칼럼 설정
var rel_epm_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('도변 번호')}",			width:"15%",		style:"AUIGrid_Left",
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
	{ dataField : "name",				headerText : "${e3ps:getMessage('도면 명')}",			width:"*",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}		
	},
	{
		dataField : "beforePart", headerText : "${e3ps:getMessage('개정전')}",
		children : [
			{ dataField : "ver",			headerText : "${e3ps:getMessage('버전')}",				width:"13%",
					
			},
			{ dataField : "stateName",				headerText : "${e3ps:getMessage('상태')}",				width:"13%",
				filter : {
					showIcon : true,
					iconWidth:30
				}	
			},
			
		],
	},
	{
		dataField : "nextPart", headerText : "${e3ps:getMessage('개정후')}",
		children : [
			{ dataField : "nextVer",			headerText : "${e3ps:getMessage('버전')}",			width:"13%",
				
				renderer : {
					type : "LinkRenderer",
					baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
					// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
					jsCallback : function(rowIndex, columnIndex, value, item) {
						var oid = item.nextOid;
						
						openView(oid);
					}
				}
						
			},
			{ dataField : "nextState",			headerText : "${e3ps:getMessage('상태')}",			width:"13%",
						
			},
		],
	}
	
];

//AUIGrid 를 생성합니다.
function rel_epm_createAUIGrid(rel_epm_columnLayout) {
	
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
		reverseRowNum : true, //No 정렬  true: desc, false : asc (Default)
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_epm_myGridID = AUIGrid.create("#rel_epm_grid_wrap", rel_epm_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_epm_myGridID, "cellClick", rel_epm_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_epm_myGridID, gridData);
}

function rel_epm_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(rel_epm_myGridID);
	var url = getURLString("/change/getECOActiveEPMList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		console.log(gridData);
		//alert("data.list =" + gridData);
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_epm_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_epm_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_epm_myGridID);
		
	});
}

//셀 클릭 핸들러
function rel_epm_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

//필터 초기화
function rel_epm_resetFilter(){
    AUIGrid.clearFilterAll(rel_epm_myGridID);
}

function rel_epm_xlsxExport() {
	AUIGrid.setProperty(rel_epm_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_epm_myGridID, exportProps);
}
</script>

<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('도면')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_epm_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_epm_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_epm_grid_wrap" style="height:${gridHeight}px">
</div>