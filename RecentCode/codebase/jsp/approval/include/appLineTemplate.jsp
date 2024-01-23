<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style type="text/css">
</style>
<script>
$(document).ready(function(){
	
	//팝업 리사이즈
	popupResize();
	
	//grid reset
	AUIGrid.destroy(appLineTemp_myGridID);
	
	//grid reset
	AUIGrid.destroy(detailTemp_myGridID);
	
	createAppLineTemplateGrid(appLineTemp_columnLayout);
	
	createDetailTemplateGrid(detailTemp_columnLayout);
	
	getAppLineTemplate();
	
});

//AUIGrid 생성 후 반환 ID
var appLineTemp_myGridID;
var detailTemp_myGridID;

//템플릿 그리드 레이아웃
var appLineTemp_columnLayout = [ {
	dataField : "name",
	headerText : "Name",
	style:"AUIGrid_Left",
	width : "70%",
},{
	dataField : "createDateFormat",
	headerText : "${e3ps:getMessage('등록일')}",
	visible : true,
}];

//템플릿 상세 그리드 레이아웃
var detailTemp_columnLayout = [ 
	{ dataField : "roleName",				headerText : "${e3ps:getMessage('구분')}",			width:"20%",	},
	{ dataField : "departmentName",		headerText : "${e3ps:getMessage('부서')}",			width:"20%",	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('이름')}",			width:"*",	},
	{ dataField : "id",					headerText : "ID",									width:"20%",	},
];

//템플릿 그리드 생성
function createAppLineTemplateGrid(appLineTemp_columnLayout) {
	 appLineTemp_auiGridProps = {
				
		selectionMode : "singleRow",
		
		showSelectionBorder : true,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : false,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		showHeader : false,
		
		enableDrop : false,
	};
	
	appLineTemp_myGridID = AUIGrid.create("#appLineTemp_grid_wrap", appLineTemp_columnLayout, appLineTemp_auiGridProps);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(appLineTemp_myGridID, "cellClick", appLineTemp_auiGridCellClickHandler);
	
};

//템플릿 상세 그리드 생성
function createDetailTemplateGrid(detailTemp_columnLayout) {
	var detailTemp_auiGridProps = {
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : false,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableDrop : false,
	};
	
	detailTemp_myGridID = AUIGrid.create("#detailTemp_grid_wrap", detailTemp_columnLayout, detailTemp_auiGridProps);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(detailTemp_myGridID, "cellClick", detailTemp_auiGridCellClickHandler);
	
};

//템플릿 불러오기
function getAppLineTemplate(){
	
	var param = new Object();
	
	var url = getURLString("/approval/getAppLineTemplate");
	ajaxCallServer(url, param, function(data){
		var appLineTemp_gridData = data.list;
		AUIGrid.setGridData(appLineTemp_myGridID, appLineTemp_gridData);
	});
}

//템플릿 상세 불러오기
function getDetailTemplate(oid){
	
	var param = new Object();
	param["oid"] = oid;

	AUIGrid.showAjaxLoader(detailTemp_myGridID);
	var url = getURLString("/approval/getDetailTemplate");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(detailTemp_myGridID, gridData);

		AUIGrid.removeAjaxLoader(detailTemp_myGridID);
	});
}

//셀 클릭 핸들러
function appLineTemp_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
	getDetailTemplate(oid);
}

//셀 클릭 핸들러
function detailTemp_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
}

function deleteTemplate(){

	var selectedItemList = AUIGrid.getSelectedItems(appLineTemp_myGridID);

	if(selectedItemList.length > 0) {
		openConfirm("${e3ps:getMessage('선택한 템플릿을 삭제하시겠습니까?')}", function(){
			
			var param = new Object();
			
			var deleteItemList = new Array();
			
			for(var i = 0; i < selectedItemList.length; i++){
				deleteItemList.push(selectedItemList[i].item);
			}
			param["deleteItemList"] = deleteItemList;
			
			var url = getURLString("/approval/deleteTemplateAction");
			ajaxCallServer(url, param, function(data){
				getAppLineTemplate();
				AUIGrid.setGridData(detailTemp_myGridID, new Array());
			});
		});
	} else {
		openNotice("${e3ps:getMessage('템플릿을 선택하세요')}");
	}
}

function applyTemplate(){

	var selectedItemList = AUIGrid.getSelectedItems(appLineTemp_myGridID);

	if(selectedItemList.length == 1) {
		openConfirm("${e3ps:getMessage('선택한 템플릿을 적용하시겠습니까?')}", function(){
			var approvalLineList = AUIGrid.getGridData(detailTemp_myGridID);
			
			AUIGrid.setGridData(DISCUSS_line_myGridID, new Array());
			AUIGrid.setGridData(APPROVE_line_myGridID, new Array());
			AUIGrid.setGridData(RECEIVE_line_myGridID, new Array());
			
			for(var i = 0; i < approvalLineList.length; i++){
				var line = approvalLineList[i];
				
				var roleType = line.roleType;
				
				if(roleType != null && roleType.length > 0) {
					if(roleType == "DISCUSS") {
						AUIGrid.addRow(DISCUSS_line_myGridID, line, "last");	
					} else if(roleType == "APPROVE") {
						AUIGrid.addRow(APPROVE_line_myGridID, line, "last");	
					} else if(roleType == "RECEIVE") {
						AUIGrid.addRow(RECEIVE_line_myGridID, line, "last");	
					}
				}
			}
		});
	} else {
		openNotice("${e3ps:getMessage('하나의 템플릿만 선택가능합니다.')}");
	}
}
</script>
<div class="pop_left pb5">
	<div class="left"><h4>${e3ps:getMessage('결재 Template')}</h4></div>
	<div class="search">
		<button type="button" class="s_bt03" onclick="deleteTemplate()">${e3ps:getMessage('삭제')}</button>
		<button type="button" class="s_bt03" onclick="applyTemplate()">${e3ps:getMessage('적용')}</button>
	</div>
</div>
<div class="list mb10" id="appLineTemp_grid_wrap" style="height:220px;"></div>
<div class="pop_left pb5">
	<div class="left"><h4>${e3ps:getMessage('Template 상세보기')}</h4></div>
	<div class="search">
	</div>
</div>
<div class="list mb10" id="detailTemp_grid_wrap" style="height:220px;"></div>