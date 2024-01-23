<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_part_myGridID);
	
	//grid setting
	rel_part_createAUIGrid(rel_part_columnLayout);
	
	// 배포 등록 팝업창일 경우
	var partList = opener.getCheckPartList();
	for(var i=0; i<partList.length; i++){
		var item = partList[i];
		AUIGrid.addRow(rel_part_myGridID, item, "last");	
	}
});

//AUIGrid 생성 후 반환 ID
var rel_part_myGridID;

//AUIGrid 칼럼 설정
var rel_part_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('부품 번호')}",			width:"*",		style:"AUIGrid_Left",
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
	{ dataField : "name",				headerText : "${e3ps:getMessage('부품 명')}",			width:"30%",		style:"AUIGrid_Left",
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
	{ dataField : "rev",			headerText : "${e3ps:getMessage('버전')}",				width:"10%",
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

//AUIGrid 를 생성합니다.
function rel_part_createAUIGrid(rel_part_columnLayout) {
	
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
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
		
		showRowCheckColumn : true,
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_part_myGridID = AUIGrid.create("#rel_part_grid_wrap", rel_part_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_part_myGridID, "cellClick", rel_part_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_part_myGridID, gridData);
}

function rel_part_getGridData(){

	/* var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(rel_part_myGridID);
	var url = getURLString("/part/getRelatedPart");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_part_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_part_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_part_myGridID);
		
	}); */
}

//셀 클릭 핸들러
function rel_part_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

function removeRow(){
	var checkItemList = AUIGrid.getCheckedRowItems(rel_part_myGridID);
	
	for(var i = 0; i < checkItemList.length; i++){
		AUIGrid.removeRowByRowId(rel_part_myGridID, checkItemList[i].item._$uid);
	}
	
	AUIGrid.setAllCheckedRows(rel_part_myGridID, false);
}

function searchTempPartPopup(){
	var url = getURLString("/distribute/searchTempPartPopup") + "?oid=${oid}";
	
	openPopup(url, "searchTempPartPopup");
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('관련 부품')}<span class="required">*</span></h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="removeRow()">${e3ps:getMessage('삭제')}</button>
		<button type="button" class="s_bt03" onclick="searchTempPartPopup()">${e3ps:getMessage('검색 추가')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_part_grid_wrap" style="height:${gridHeight}px">
</div>