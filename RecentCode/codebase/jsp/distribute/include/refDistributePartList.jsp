<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(refPurchasePartList_myGridID);
	
	//grid setting
	refPurchasePartList_createAUIGrid(refPurchasePartList_columnLayout);
	
	//get grid data
	refPurchasePartList_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var refPurchasePartList_myGridID;

//AUIGrid 칼럼 설정
var refPurchasePartList_columnLayout = 
	[ 
		{ dataField : "icon",		headerText : "",		width:"30", 
			renderer : { // HTML 템플릿 렌더러 사용
				type : "TemplateRenderer"
			},	
		},
		{ dataField : "distPartNumber",		headerText : "${e3ps:getMessage('품목 번호')}",	width:"15%", 
			filter : {
				showIcon : true,
				iconWidth:30
			},
			renderer : {
				type : "LinkRenderer",
				baseUrl : "javascript", 
				jsCallback : function(rowIndex, columnIndex, value, item) {
					var oid = item.distPartOid;
					openView(oid);
				}
			}
			
		},
		{ dataField : "distPartName",		headerText : "${e3ps:getMessage('품목 명')}",	style:"AUIGrid_Left",		width:"*%",	sortValue : "master>name", 
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "spec",		headerText : "${e3ps:getMessage('사양')}",			width:"9%",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "maker",		headerText : "${e3ps:getMessage('제조사')}",			width:"9%",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "version",		headerText : "${e3ps:getMessage('버전')}",			width:"9%", 
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "stateName",		headerText : "${e3ps:getMessage('상태')}",			width:"9%", 
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "quantity",		headerText : "${e3ps:getMessage('수량')}"	,	width:"9%",
			filter : {
				showIcon : true,
				iconWidth:30
			},
		},
		{ dataField : "unit",		headerText : "${e3ps:getMessage('단위')}",			width:"5%", 
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
	]

//AUIGrid 를 생성합니다.
function refPurchasePartList_createAUIGrid(refPurchasePartList_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
			selectionMode : "multipleCells",
			
			showSelectionBorder : true,
			
			showAutoNoDataMessage : false,
			
			showRowNumColumn : true,
			
			showEditedCellMarker : false,
			
			wrapSelectionMove : true,
			
			enableFilter : true,
			
			editable : false,
			
			enableSorting : false,
			
			enableMovingColumn : false,
			
			noDataMessage : gridNoDataMessage,
			
			autoGridHeight : false,
			
			height : ${gridHeight},
			
			 fixedColumnCount: 9,
	};

	// 실제로 #grid_wrap 에 그리드 생성
	refPurchasePartList_myGridID = AUIGrid.create("#refPurchasePartList_grid_wrap", refPurchasePartList_columnLayout, gridPros);
	
	
	var gridData = new Array();
	AUIGrid.setGridData(refPurchasePartList_myGridID, gridData);
}

function refPurchasePartList_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(refPurchasePartList_myGridID);
	var url = getURLString("/purchase/getPurchasePartListByPurchase");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(refPurchasePartList_myGridID, gridData);

		AUIGrid.setAllCheckedRows(refPurchasePartList_myGridID, false);
		AUIGrid.removeAjaxLoader(refPurchasePartList_myGridID);
		
	});
}


function refPurchasePartList_xlsxExport() {
	AUIGrid.setProperty(refPurchasePartList_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
			 exceptColumnFields : ["icon"],
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(refPurchasePartList_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt">
		<h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('구매요청 품목')}</h4>
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="refPurchasePartList_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="refPurchasePartList_grid_wrap" style="height:${gridHeight}px;margin-bottom:5rem;">
</div>