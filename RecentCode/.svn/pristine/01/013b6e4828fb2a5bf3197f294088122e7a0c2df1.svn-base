<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(doc_rel_project_myGridID);
	
	//grid setting
	doc_rel_project_createAUIGrid(doc_rel_project_columnLayout);
	
	//get grid data
	doc_rel_project_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var doc_rel_project_myGridID;

//AUIGrid 칼럼 설정
var doc_rel_project_columnLayout = 
[
	    {
	      dataField: "code",
	      headerText: "${e3ps:getMessage('프로젝트 번호')}",
	      width: "*%",
	      sortValue: "code",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	    {
	      dataField: "name",
	      headerText: "${e3ps:getMessage('프로젝트 명')}",
	      width: "*%",
	      sortValue: "name",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	      renderer: {
	        type: "LinkRenderer",
	        baseUrl: "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
	        // baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
	        jsCallback: function (rowIndex, columnIndex, value, item) {
	          var oid = item.oid;
	          openView(oid);
	        },
	      },
	    },
	    {
	      dataField: "planDurationHoliday",
	      headerText: "${e3ps:getMessage('기간')}",
	      width: "9%",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	    {
	      dataField: "planStartDate",
	      headerText: "${e3ps:getMessage('계획 시작일')}",
	      width: "7%",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	    {
	      dataField: "planEndDate",
	      headerText: "${e3ps:getMessage('계획 종료일')}",
	      width: "7%",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	    {
	      dataField: "pmName",
	      headerText: "PM",
	      width: "10%",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	    {
	      dataField: "stateTag",
	      headerText: "${e3ps:getMessage('상태')}",
	      width: "5%",
	      renderer: {
	          type: "TemplateRenderer",
	      },
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	    {
	      dataField: "creatorFullName",
	      headerText: "${e3ps:getMessage('등록자')}",
	      width: "10%",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
	    {
	      dataField: "createDate",
	      headerText: "${e3ps:getMessage('등록일')}",
	      width: "7%",
	      filter: {
	        showIcon: true,
	        iconWidth: 30,
	      },
	    },
];

//AUIGrid 를 생성합니다.
function doc_rel_project_createAUIGrid(doc_rel_project_columnLayout) {
	
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
	doc_rel_project_myGridID = AUIGrid.create("#doc_rel_project_grid_wrap", doc_rel_project_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(doc_rel_project_myGridID, gridData);
}

function doc_rel_project_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(doc_rel_project_myGridID);
	var url = getURLString("/project/getRelatedProject");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(doc_rel_project_myGridID, gridData);

		AUIGrid.removeAjaxLoader(doc_rel_project_myGridID);
		
	});
}


//필터 초기화
function doc_rel_project_resetFilter(){
    AUIGrid.clearFilterAll(doc_rel_project_myGridID);
}

function doc_rel_project_xlsxExport() {
	AUIGrid.setProperty(doc_rel_project_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(doc_rel_project_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('관련 프로젝트')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="doc_rel_project_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="doc_rel_project_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="doc_rel_project_grid_wrap" style="height:200px">
</div>