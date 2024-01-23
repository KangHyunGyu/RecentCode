<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//roleType setting
	getApprovalRoleTypeList();
	
	//grid setting
	app_line_createAUIGrid(app_line_columnLayout);
	
	//get grid data
	app_line_getGridData();
	
});

var roleTypeList = [];//[{"value":"a","name":"1"}];

//사용자가 검색한 리스트 보관
var userList = [];

//AUIGrid 생성 후 반환 ID
var app_line_myGridID;

//AUIGrid 칼럼 설정
var app_line_columnLayout = [
	{ dataField : "roleType",				headerText : "${e3ps:getMessage('구분')}",			width:"10%",	style:"aui-grid-editable-cell",
		editRenderer : {
			type : "DropDownListRenderer",
			showEditorBtnOver : true, // 마우스 오버 시 에디터버턴 보이기
			list : roleTypeList,
			keyField : "key",
			valueField : "value"
		},
		labelFunction : function( rowIndex, columnIndex, value, item) {
	    	
	    	var retStr = "";
	    	
	    	for(var i=0,len=roleTypeList.length; i<len; i++) {
	    		if(roleTypeList[i]["key"] == value) {
	    			retStr = roleTypeList[i]["value"];
	    			break;
	    		}
	    	}
	    	
	    	return retStr;
	    }
	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('이름')}",			width:"20%",	style:"aui-grid-editable-cell",
		editRenderer : searchApprovalUser = { // 편집 모드 진입 시 원격 리스트 출력하고자 할 때
			    type : "RemoteListRenderer",
			    fieldName : "name",
				remoter : function( request, response ) { // remoter 반드시 지정 필수
					if(request.term && String(request.term).length < 1) {
						alert("1글자 이상 입력하십시오.");				
						response(false); // 데이터 요청이 없는 경우 반드시 false 삽입하십시오.
						return;
					}
					// 데이터 요청
					var param = new Object();
					
					param.name = request.term;
					
					var url = getURLString("/common/searchUserAction");
					ajaxCallServer(url, param, function(data){

						// 그리드 데이터
						userList = data.list;
						
						//중복 제거
						var gridList = AUIGrid.getGridData(app_line_myGridID);
						
						for(var i=0; i < gridList.length; i++) {
							userList = userList.filter(function(item, index, arr){
							    return item.pOid != gridList[i].pOid;
							});
						}
						
						//데이터 수정
						for(var i=0; i < userList.length; i++) {
							userList[i].name = userList[i].name + "[" + userList[i].id + "]";
						}
						
						// 그리드에 데이터 세팅
						response(userList); 
						
					});// end of ajax
				},
				listTemplateFunction : function(rowIndex, columnIndex, text, item, dataField, listItem) {
					
					var html = '<div class="myList-style">';
					html += '<span class="myList-col" style="padding-left:10px; width:100px;" title="' + listItem.name + '">' + listItem.name + '</span>';
					html += '<span class="myList-col" style="width:80px;">' + listItem.duty + '</span>';
					html += '<span class="myList-col" style="width:60px; text-align:right;">' + listItem.departmentName + '</span>';
					html += '<span class="myList-col" style="width:80px; padding-left:20px;">' + listItem.id + '</span>';
					html += '</div>';
					
					return html;
				}
			}
	},
	{ dataField : "departmentName",			headerText : "${e3ps:getMessage('부서')}",			width:"20%",	editable: false},
	{ dataField : "duty",				headerText : "${e3ps:getMessage('직위')}",			width:"20%",	editable: false},
	{ dataField : "id",					headerText : "${e3ps:getMessage('아이디')}",			width:"*",	editable: false},
];

/**************************************************************
*                     결재 Role  리스트 
****************************************************************/
function getApprovalRoleTypeList() { 
	
	var url	= getURLString("/common/getApprovalRoleTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	for(var i = 0 ; i < data.list.length ; i++){
		if("DRAFT" != data.list[i].key){
			var dataMap = new Object();
			
			dataMap["key"] = data.list[i].key;
			dataMap["value"] = data.list[i].value;
			roleTypeList.push(dataMap);
		}
	}
}

//AUIGrid 를 생성합니다.
function app_line_createAUIGrid(app_line_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : true,
		
		editable : true,
		
		enableSorting : false,
		
		softRemoveRowMode : false,
		
		//드래그 앤 드랍 프로퍼티
		enableDrag : true, // 드래깅 행 이동 가능 여부 (기본값 : false)
		enableMultipleDrag : false, // 다수의 행을 한번에 이동 가능 여부 (기본값 : true)
		enableDragByCellDrag : true, // 셀에서 바로 드래깅 해 이동 가능 여부 (기본값 : false) - enableDrag=true 설정이 선행되야 함
		enableDrop : true, // 드랍 가능 여부 (기본값 : true)
		dropToOthers : false, // 드랍을 받아줄 그리드가 다른 그리드에도 있는지 여부 (기본값 : false) - 그리드 간 행 이동인지 여부
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성
	app_line_myGridID = AUIGrid.create("#app_line_grid_wrap", app_line_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(app_line_myGridID, "cellClick", app_line_auiGridCellClickHandler);
	
	// 에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(app_line_myGridID, "cellEditEnd", app_line_auiCellEditHandler);
	
	// 에디팅 취소 이벤트 바인딩
	AUIGrid.bind(app_line_myGridID, "cellEditCancel", app_line_auiCellEditHandler);
	
	// 드래그 앤 드랍 종료 이벤트 바인딩
	AUIGrid.bind(app_line_myGridID, "dropEndBefore", app_line_dropEndEventHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(app_line_myGridID, gridData);
}

function app_line_getGridData(){

	var param = new Object();
	
	param["oid"] = "${appLineOid}";
	
	AUIGrid.showAjaxLoader(app_line_myGridID);
	var url = getURLString("/approval/getApprovalLine");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(app_line_myGridID, gridData);

		AUIGrid.setAllCheckedRows(app_line_myGridID, false);
		AUIGrid.removeAjaxLoader(app_line_myGridID);
		
	});
}

//셀 클릭 핸들러
function app_line_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
}

//편집 핸들러
function app_line_auiCellEditHandler(event) {

	switch(event.type) {
	case "cellEditEnd" :
		if(event.dataField == "name") {
			var item = getItem(event.value);
			if(typeof item === "undefined") {
				return;
			}
			
			// ISBN 수정 완료하면, 책제목, 저자 등의 필드도 같이 업데이트 함.
			AUIGrid.updateRow(app_line_myGridID, {
				name : item.name,
				duty : item.duty,
				departmentName : item.departmentName,
				id : item.id,
				pOid : item.oid,
			}, event.rowIndex);
		}
		break;
	case "cellEditCancel" :
		break;
	}
};

//추가 버튼
function app_line_addRow() {
	var item = new Object();

	AUIGrid.addRow(app_line_myGridID, item, "last");
}

//삭제 버튼
function app_line_removeRow() {

	var checkItemList = AUIGrid.getCheckedRowItems(app_line_myGridID);
	
	for(var i = 0; i < checkItemList.length; i++){
		
		AUIGrid.removeRowByRowId(app_line_myGridID, checkItemList[i].item._$uid);
	}
	
	AUIGrid.setAllCheckedRows(app_line_myGridID, false);
}

function addApprovalLinePopup() {

	var url = getURLString("/approval/addApprovalLinePopup") + "?oid=${appLineOid}";
	
	openPopup(url, "addApprovalLinePopup");
}

//name 으로 검색해온 정보 아이템 반환.
function getItem(name) {
	var item;
	$.each(userList, function(n, v) {
		if(v.name == name) {
			item = v;
			return false;
		}
	});
	return item;
};

//드래드 앤 드랍 핸들러
function app_line_dropEndEventHandler(event) {
	var item = event.items[0];
	
	var toRowIndex = event.toRowIndex;
	var type = item.type;
	
	/* var upItem = AUIGrid.getItemByRowIndex(app_line_myGridID, toRowIndex - 1);

	if(type != upItem.type) {
		return false;
	}
	if(type == "합의") {
		if(!(upItem.type == "합의" || upItem.type == "기안")) {
			return false;
		}
	} else if(type == "결재") {
		if(!(upItem.type == "결재" || upItem.type == "합의")) {
			return false;
		}
	} else if(type == "수신") {
		if(!(upItem.type == "수신" || upItem.type == "결재")) {
			return false;
		}
	} */
}

//팝업에서 리스트 가져오기
function app_line_getAppLineFromPopup(list) {
	
	AUIGrid.setGridData(app_line_myGridID, list);
			
}

//팝업으로 리스트 보내기
function app_line_sendAppLineToPopup() {
	
	var list = AUIGrid.getGridData(app_line_myGridID);
	
	return list;
}
</script>
<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('결재선 지정')}</span>
		<c:if test="${taskType eq 'CreateTRCR'}">
			<span class="required">*${e3ps:getMessage('결재선 지정 시 품질팀 담당자를 최종 결재자로 지정 바랍니다.')}</span>
		</c:if>
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="app_line_removeRow()">${e3ps:getMessage('삭제')}</button>
		<button type="button" class="s_bt03" onclick="app_line_addRow()">${e3ps:getMessage('추가')}</button>
		<button type="button" class="s_bt03" onclick="addApprovalLinePopup()">${e3ps:getMessage('결재선 추가')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="app_line_grid_wrap" style="height:${gridHeight}px">
</div>