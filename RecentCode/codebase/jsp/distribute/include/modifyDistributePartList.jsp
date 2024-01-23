<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script>
$(document).ready(function(){
	
	//grid setting
	modify_distParts_createAUIGrid(modify_distParts_columnLayout);
	
	//기존데이타 삽입
	distributePartList_getGridData();
	
});

//사용자 타이핑의 민감도 체크를 위한 타이머
var timerId = null;

//AUIGrid 생성 후 반환 ID
var modify_distParts_myGridID;

var add_objList = [];


//AUIGrid 칼럼 설정
var modify_distParts_columnLayout = [
	{ dataField : "icon",		headerText : "",		width:"30", editable : false,
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
	},
	{ dataField : "distPartNumber",		headerText : "${e3ps:getMessage('품목 번호')}<span class='required'>*</span>",			width:"15%",
		editRenderer : { // 편집 모드 진입 시 원격 리스트 출력하고자 할 때
		    type : "RemoteListRenderer",
		    showEditorBtnOver : true, // 마우스 오버 시 에디터버턴 보이기
		    autoCompleteMode : true, // 자동완성 모드 설정 (기본값 :false)
			autoEasyMode : true, // 자동완성 모드일 때 첫 아이템 자동 선택할지 여부 (기본값 : false)
			keyField : "distPartNumber", // key 에 해당되는 필드명
		    fieldName : "number",
			remoter : function( request, response ) { 
				
				if(String(request.term).length <= 3 ) {
					response([]); 
					return;
				}

				if(timerId != null) {
					clearTimeout(timerId);
				}
				
				var param = new Object();
				
				param["viewName"] ="Design";
				param["number"] = request.term;
				param["likeSearch"] = "like";
				param["state"] = "APPROVED";
				console.log('param',param)
				var url = getURLString("/part/searchPartAction");
				
				ajaxCallServer(url, param, function(data){

					add_objList = data.list;
					response(add_objList); 
					
				});
				
				timerId = setTimeout(function() {
					
				}, 200);  
				
				
				
			},
			listTemplateFunction : function(rowIndex, columnIndex, text, item, dataField, listItem) {
				
				var html = '<div class="myList-style">';
				html += '<span class="myList-col" style="padding-left:10px; width:120px;" title="' + listItem.number + '">' + listItem.number + '</span>';
				html += '<span class="myList-col" style="width:150px;">' + listItem.name + '</span>';
				html += '<span class="myList-col" style="width:80px; text-align:right;">' + listItem.rev + '</span>';
				html += '</div>';
				
				return html;
			}
		
		}
	},
	{ dataField : "distPartName",		headerText : "${e3ps:getMessage('품목 명')}",	style:"AUIGrid_Left",		width:"*%",	sortValue : "master>name", editable : false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "version",		headerText : "${e3ps:getMessage('버전')}",			width:"9%", editable : false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",		headerText : "${e3ps:getMessage('상태')}",			width:"9%", editable : false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];


function modify_distParts_createAUIGrid(modify_distParts_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : true,
		
		editable : true,
		
		enableSorting : false,
		
		softRemoveRowMode : false,
		
		noDataMessage : gridNoDataMessage,
		
		autoGridHeight : false,
		
		height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성
	modify_distParts_myGridID = AUIGrid.create("#modify_distParts_grid_wrap", modify_distParts_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(modify_distParts_myGridID, "cellClick", modify_distParts_auiGridCellClickHandler);
	
	// 에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(modify_distParts_myGridID, "cellEditEnd", modify_distParts_auiCellEditHandler);
	
	// 에디팅 취소 이벤트 바인딩
	AUIGrid.bind(modify_distParts_myGridID, "cellEditCancel", modify_distParts_auiCellEditHandler);
	
	resizeGrid();
	
	var gridData = new Array();
	AUIGrid.setGridData(modify_distParts_myGridID, gridData);
	
}


//셀 클릭 핸들러
function modify_distParts_auiGridCellClickHandler(event) {
	
}

//편집 핸들러
function modify_distParts_auiCellEditHandler(event) {
	
	var editedItem = new Object();
		
	switch(event.type) {
	case "cellEditEnd" :
		if(event.dataField == "distPartNumber") {
			
			var item = add_getItem(event.value);
			
			if(typeof item === "undefined" || isEmpty(item) ) {
				AUIGrid.updateRow(modify_distParts_myGridID, {
					icon : "",
					distPartNumber : "",
					distPartName : "",
					version : "",
					stateName : "",
					distPartOid : "",
				}, event.rowIndex);
				return;
			}
			
			let currentPartsList = AUIGrid.getGridData(modify_distParts_myGridID);
			if(currentPartsList.some( (x) => {return x.distPartOid == item.oid})) {
				alert("${e3ps:getMessage('이미 추가되어있는 부품입니다')}");
				AUIGrid.updateRow(modify_distParts_myGridID, {
					distPartNumber : "",
				}, event.rowIndex);
				return;
			}
			
			AUIGrid.updateRow(modify_distParts_myGridID, {
				icon : item.icon,
				distPartName : item.name,
				version : item.rev,
				stateName : item.stateName,
				distPartOid : item.oid,
			}, event.rowIndex);
		}
		
		
		break;
	case "cellEditCancel" :
		break;
	}
};

function distributePartList_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	//AUIGrid.showAjaxLoader(modify_distParts_myGridID);
	var url = getURLString("/distribute/getDistributePartListByDistribute");
	/* ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		
		if(gridData && gridData.length > 0){
			// 그리드에 데이터 세팅
			AUIGrid.setGridData(modify_distParts_myGridID, gridData);
		}

		AUIGrid.setAllCheckedRows(modify_distParts_myGridID, false);
		AUIGrid.removeAjaxLoader(modify_distParts_myGridID);
		
	}); */
}


<%----------------------------------------------------------
*                      그리드 Row 추가 
----------------------------------------------------------%>
function addRow() {

	// 그리드의 편집 인푸터가 열린 경우 에디팅 완료 상태로 만듬.
	AUIGrid.forceEditingComplete(modify_distParts_myGridID, null);
	
	var item = new Object();
	item.distPartNumber ="";
	item.distPartName = "";
	item.version = "";
	item.stateName ="";
	
	
	AUIGrid.addRow(modify_distParts_myGridID, item, "last");
}
<%----------------------------------------------------------
*                      그리드 Row 삭제
----------------------------------------------------------%>
function removeRow() {
	
		var checkItemList = AUIGrid.getCheckedRowItems(modify_distParts_myGridID);
		
		for(var i = 0; i < checkItemList.length; i++){
			
			AUIGrid.removeRowByRowId(modify_distParts_myGridID, checkItemList[i].item._$uid);
		}
		
		AUIGrid.setAllCheckedRows(modify_distParts_myGridID, false); 
}
<%----------------------------------------------------------
*                      선택 품목 Item 
----------------------------------------------------------%>
function add_getItem(number) {
	var item;
	
	$.each(add_objList, function(n, v) {
		if(v.number == number) {
			item = v;
			return false;
		}
	});
	return item;
};

</script>
<!-- button -->
<br/>
<div class="seach_arm2 pb5">
	<div class="leftbt">
		<span class="title">
			<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			${e3ps:getMessage('배포요청 품목')}
		</span>
	</div>
	<div class="rightbt" id="partListRightBtn">
		<button type="button" class="i_read" style="width:60px" onclick="addRow()" id="addBtn">${e3ps:getMessage('추가')}</button>
		<button type="button" class="i_delete" style="width:60px" onclick="removeRow()">${e3ps:getMessage('삭제')}</button>
	</div>
</div>
<!-- //button -->
<div class="list " id="modify_distParts_grid_wrap" style="height:${gridHeight};margin-bottom:50px;">
</div>
