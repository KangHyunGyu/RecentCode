<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(purchase_byOrderNumber_myGridID);
	
	//grid setting
	purchase_byOrderNumber_createAUIGrid(purchase_byOrderNumber_columnLayout);
	
	//수정화면, 상세화면의 경우 oid가 존재하므로 관련 구매요청 리스트 가져오기
	if("${oid}" != ""){
		getRelatedPurchaseList();
	}
	
});


//AUIGrid 생성 후 반환 ID
var purchase_byOrderNumber_myGridID;

var related_purchase_cache = [];

//AUIGrid 칼럼 설정
var purchase_byOrderNumber_columnLayout = 
[
	{
	      dataField: "purchaseNumber",
	      headerText: "${e3ps:getMessage('요청 번호')}",
	      width: "20%",
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
	      style:"AUIGrid_Left",
	      width: "*",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	    {
	      dataField: "orderNumber",
	      headerText: "${e3ps:getMessage('수주번호')}",
	      width: "20%",
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
];

//AUIGrid 를 생성합니다.
function purchase_byOrderNumber_createAUIGrid(purchase_byOrderNumber_columnLayout){
	
	var showRowCheckbox = true;
	
	if("${type}" == "view"){
		showRowCheckbox = false;
	}
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		rowIdField : "_$uid",
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		showAutoNoDataMessage : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : showRowCheckbox,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성
	purchase_byOrderNumber_myGridID = AUIGrid.create("#purchase_byOrderNumber_grid_wrap", purchase_byOrderNumber_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(purchase_byOrderNumber_myGridID, gridData);
}

//수주 관련 구매요청 리스트 전부 가져오기
function purchase_byOrderNumber_getGridData(orderNumber){

	AUIGrid.setGridData(purchase_byOrderNumber_myGridID,[]);
	
	var param = new Object();
	param.orderNumber = orderNumber;
	AUIGrid.showAjaxLoader(purchase_byOrderNumber_myGridID);
	var url = getURLString("/purchase/getPurchaseListByOrderNumber");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		gridData = gridData.filter((item) => {
			return item.oid != "${oid}";
		})
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(purchase_byOrderNumber_myGridID, gridData);

		AUIGrid.removeAjaxLoader(purchase_byOrderNumber_myGridID);
		
	});
}

//현재 연결되어있는 관련구매요청 링크 리스트 가져오기
function getRelatedPurchaseList(){
	
	AUIGrid.setGridData(purchase_byOrderNumber_myGridID,[]);
	
	var param = new Object();
	param.oid = "${oid}";
	var url = getURLString("/purchase/getRelatedPurchaseList");
	ajaxCallServer(url, param, function(data){
		
		related_purchase_cache = data.list;
		
		if("${type}" == "modify"){
			relatedPurchaseCheck();
		}else if("${type}" == "view"){
			AUIGrid.setGridData(purchase_byOrderNumber_myGridID, related_purchase_cache);	
		}
		
	});
	
}

//수정 화면에서 현재 연결되어있는 관련구매요청 링크 체크
function relatedPurchaseCheck(){
	if(related_purchase_cache.length > 0){
		let allList = AUIGrid.getGridData(purchase_byOrderNumber_myGridID);
		let checkList = [];
		
		allList.forEach((item) => {
			if(related_purchase_cache.some( it => item.oid == it.oid )){
				checkList = [...checkList, item._$uid];
			}
		})
		
		AUIGrid.setCheckedRowsByIds(purchase_byOrderNumber_myGridID, checkList);
	}
}

function purchase_byOrderNumber_xlsxExport() {
	AUIGrid.setProperty(purchase_byOrderNumber_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
			 exceptColumnFields : ["icon", "bom"],
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(purchase_byOrderNumber_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10  pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${title}</h4></div>
	<div class="rightbt">
		<c:if test="${type == 'view' }">
			<button type="button" class="s_bt03" onclick="productPart_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		</c:if>
	</div>
</div>
<!-- //button -->
<div class="list" id="purchase_byOrderNumber_grid_wrap" style="height:${gridHeight}px">
</div>