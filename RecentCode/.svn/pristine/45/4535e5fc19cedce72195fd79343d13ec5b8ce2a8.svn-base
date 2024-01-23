<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
   $(document).ready(function(){

   	//팝업 리사이즈
//    	popupResize();

   	//grid reset
   	AUIGrid.destroy(ah_myGridID);

   	//grid setting
   	ah_createAUIGrid(ah_columnLayout);

   	//get grid data
   	ah_getGridData();

   });

   //AUIGrid 생성 후 반환 ID
   var ah_myGridID;

   //AUIGrid 칼럼 설정
   var ah_columnLayout = [
   	//{ dataField : "old",				headerText : "${e3ps:getMessage('old')}",			width:"5%"},
	   { dataField : "seq",				headerText : "${e3ps:getMessage('순서')}",			width:"5%"},
		{ dataField : "roleName",			headerText : "${e3ps:getMessage('구분')}",			width:"8%",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "ownerFullName",				headerText : "${e3ps:getMessage('이름')}",			width:"13%",
			filter : {
				showIcon : true,
				iconWidth:30
			},
			cellColSpan : 5,
			cellColMerge : true,
			
		},
		{ dataField : "departmentName",			headerText : "${e3ps:getMessage('부서')}",			width:"15%",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "approveDate",		headerText : "${e3ps:getMessage('결재일')}",		width:"13%",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "stateName",			headerText : "${e3ps:getMessage('결재')}",			width:"10%",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "description",		headerText : "${e3ps:getMessage('결재 의견')}",		width:"*",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
   ];

   //AUIGrid 를 생성합니다.
   function ah_createAUIGrid(ah_columnLayout) {

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

   		filterLayerHeight : 200,

   		enableCellMerge : true,

   	};

   	// 실제로 #grid_wrap 에 그리드 생성
   	ah_myGridID = AUIGrid.create("#ah_grid_wrap", ah_columnLayout, gridPros);

   	// 셀 클릭 이벤트 바인딩
   	AUIGrid.bind(ah_myGridID, "cellClick", ah_auiGridCellClickHandler);

   	var gridData = new Array();
   	AUIGrid.setGridData(ah_myGridID, gridData);
   }

   function ah_getGridData(){

   	var param = new Object();

   	param.oid = "${oid}";

   	AUIGrid.showAjaxLoader(ah_myGridID);
   	var url = getURLString("/common/getApproveHistory");
   	ajaxCallServer(url, param, function(data){
   		// 그리드 데이터
   		var gridData = data.list;

   		// 그리드에 데이터 세팅
   		AUIGrid.setGridData(ah_myGridID, gridData);

   		AUIGrid.setAllCheckedRows(ah_myGridID, false);
   		AUIGrid.removeAjaxLoader(ah_myGridID);
   	});
   }

   function setSorting() {
   	//mNumber,pporderAX,orderDate
   	var sortingInfo = [];
   	// ver>seq
   	sortingInfo[0] = { dataField : "createDate", sortType : -1 };
   	//sortingInfo[1] = { dataField : "ver", sortType : 1 }; // 오름차순 1, -1 래림 차순 생산분류
   	//sortingInfo[2] = { dataField : "seq", sortType : 1 }; // 오름차순 1, -1 래림 차순 HIER 1

   	AUIGrid.setSorting(ah_myGridID, sortingInfo);
   	//AUIGrid.setSorting(myGridID2, sortingInfo);
   }

   //셀 클릭 핸들러
   function ah_auiGridCellClickHandler(event) {

   	var dataField = event.dataField;
   	var oid = event.rowIdValue;

   }

   //필터 초기화
   function ah_resetFilter(){
       AUIGrid.clearFilterAll(ah_myGridID);
   }

   function ah_xlsxExport() {
   	AUIGrid.setProperty(ah_myGridID, "exportURL", getURLString("/common/xlsxExport"));

   	 // 엑셀 내보내기 속성
   	  var exportProps = {
   			 postToServer : true,
   	  };
   	  // 내보내기 실행
   	  AUIGrid.exportToXlsx(ah_myGridID, exportProps);
   }
</script>
<div class="seach_arm2 pt10 pb5">
  <div class="leftbt">
    <h4>
      <img
        class="pointer"
        onclick="switchPopupDiv(this);"
        src="/Windchill/jsp/portal/images/minus_icon.png"
      />
      ${e3ps:getMessage('결재 이력')}
    </h4>
  </div>
  <div class="rightbt">
    <button type="button" class="s_bt03" onclick="ah_resetFilter();">
      ${e3ps:getMessage('필터 초기화')}
    </button>
    <button type="button" class="s_bt03" onclick="ah_xlsxExport();">
      ${e3ps:getMessage('엑셀 다운로드')}
    </button>
  </div>
</div>
<div class="list" id="ah_grid_wrap" style="height:400px"></div>
