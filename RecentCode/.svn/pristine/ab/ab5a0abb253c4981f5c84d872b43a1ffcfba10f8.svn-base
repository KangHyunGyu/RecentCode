<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(ref_purchase_gridID);
	
	//grid setting
	ref_purchase_createAUIGrid(ref_purchase_columnLayout);
	
	//get grid data
	ref_purchase_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var ref_purchase_gridID;

//AUIGrid 칼럼 설정
var ref_purchase_columnLayout = [
	{ dataField : "containerName",		headerText : "${e3ps:getMessage('사업구분')}",		width:"5%",		sortValue : "containerName",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{
	  dataField: "purchaseNumber",
	  headerText: "${e3ps:getMessage('요청 번호')}",
	  width: "10%",
	  renderer: {
	    type: "LinkRenderer",
	    baseUrl: "javascript",
	    jsCallback: function (rowIndex, columnIndex, value, item) {
	      var oid = item.oid;
	      openView(oid);
	    },
	  },
	  filter: {
	    showIcon: true,
	    iconWidth: 30,
	  },
	},
	{
	  dataField: "purchaseName",
	  headerText: "${e3ps:getMessage('요청 명')}",
	  width: "*",
	  filter: {
	    showIcon: true,
	    iconWidth: 30,
	  },
	},
	{
	  dataField: "orderNumber",
	  headerText: "${e3ps:getMessage('수주번호')}",
	  width: "10%",
	  renderer: {
	      type: "LinkRenderer",
	      baseUrl: "javascript",
	      jsCallback: function (rowIndex, columnIndex, value, item) {
	    	  const url = "/Windchill/worldex/production/view?oid=" + item.productOid;
	          openPopup(url, "View Production", 1100, 920);
	      },
	    },
	  filter: {
	    showIcon: true,
	    iconWidth: 30,
	  },
	},
	{
	  dataField: "stateName",
	  headerText: "${e3ps:getMessage('상태')}",
	  width: "10%",
	  filter: {
	    showIcon: true,
	    iconWidth: 30,
	  },
	},
	{
	  dataField: "creator",
	  headerText: "${e3ps:getMessage('작성자')}",
	  width: "10%",
	  filter: {
	    showIcon: true,
	    iconWidth: 30,
	  },
	},
	{
	  dataField: "createDate",
	  headerText: "${e3ps:getMessage('작성일')}",
	  width: "10%",
	  filter: {
	    showIcon: true,
	    iconWidth: 30,
	  },
	},
	{
	  dataField: "updateDate",
	  headerText: "${e3ps:getMessage('수정일')}",
	  width: "10%",
	  filter: {
	    showIcon: true,
	    iconWidth: 30,
	  },
	},
	{
	   dataField: "requestDate",
	   headerText: "${e3ps:getMessage('입고 요청일')}",
	   width: "10%",
	   filter: {
	     showIcon: true,
	     iconWidth: 30,
	   },
	 },
];

//AUIGrid 를 생성합니다.
function ref_purchase_createAUIGrid(ref_purchase_columnLayout) {
	
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
		
		autoGridHeight : false,
		
		height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성
	ref_purchase_gridID = AUIGrid.create("#ref_purchase_grid_wrap", ref_purchase_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(ref_purchase_gridID, gridData);
}

function ref_purchase_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(ref_purchase_gridID);
	var url = getURLString("/purchase/getRefPurchaseListByPart");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		console.log('gridData',gridData)
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(ref_purchase_gridID, gridData);

		AUIGrid.setAllCheckedRows(ref_purchase_gridID, false);
		AUIGrid.removeAjaxLoader(ref_purchase_gridID);
		
	});
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
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('구매 요청')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="ref_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="ref_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="ref_purchase_grid_wrap" style="height:${gridHeight}px">
</div>