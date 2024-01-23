<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(productPart_myGridID);
	
	//grid setting
	productPart_createAUIGrid();
	
	//get grid data
	productPart_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var productPart_myGridID;

//AUIGrid 칼럼 설정
var productPart_columnLayout = [
	{ dataField : "icon",		headerText : "",		width:"30",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
	},
	
	{ dataField : "number",		headerText : "${e3ps:getMessage('폼목 번호')}",		width:"15%",	 sortValue : "master>number",
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", 
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.oid;
				
				openView(oid);
			}
		}
	},
	{ dataField : "name",		headerText : "${e3ps:getMessage('품목 명')}",	style:"AUIGrid_Left",		width:"*",	sortValue : "master>name",
	},
	{ dataField : "rev",		headerText : "${e3ps:getMessage('버전')}",			width:"10%",
	},
	{ dataField : "stateName",		headerText : "${e3ps:getMessage('상태')}",			width:"10%",
	},
	{ dataField : "bom",		headerText : "BOM",		width:"50",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var icon = "/Windchill/jsp/portal/images/bom_icon.png";
			
			var template = "<img title='${e3ps:getMessage('BOM 보기')}' class='pointer' onclick='openPurchaseBomTree(\"" + item.oid + "\")' src='" + icon + "'/>";
			
			return template;
		}
	},
];

//AUIGrid 를 생성합니다.
function productPart_createAUIGrid() {
	
	// 그리드 속성 설정
	var gridPros = {
			
			selectionMode : "multipleCells",
			
			showSelectionBorder : true,
			
			showAutoNoDataMessage : false,
			
			showRowNumColumn : false,
			
			showEditedCellMarker : false,
			
			wrapSelectionMove : true,
			
			enableSorting : false,
			
			enableMovingColumn : false,
			
			noDataMessage : gridNoDataMessage,
			
			autoGridHeight : true,
			
			rowHeight :  ${gridHeight},
			
			height : ${gridHeight} + 28,
		};

		// 실제로 #grid_wrap 에 그리드 생성
		productPart_myGridID = AUIGrid.create("#productPart_grid_wrap", productPart_columnLayout, gridPros);
		
		var gridData = new Array();
		AUIGrid.setGridData(productPart_myGridID, gridData);
}

function productPart_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(productPart_myGridID);
	var url = getURLString("/purchase/getPurchaseProductByPurchase");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(productPart_myGridID, gridData);

		AUIGrid.setAllCheckedRows(productPart_myGridID, false);
		AUIGrid.removeAjaxLoader(productPart_myGridID);
		
	});
}


</script>
<!-- button -->
<br>
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt">
		<h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('제품 목록')}</h4>
	</div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<div class="list" id="productPart_grid_wrap">
</div>