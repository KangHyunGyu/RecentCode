<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script>
$(document).ready(function(){
	
	//grid setting
	add_${codeType}_createAUIGrid(add_${codeType}_columnLayout);
	
	//get grid data
	if("${oid}".length > 0) {
		add_${codeType}_getGridData();
	}
});

//사용자가 검색한 리스트 보관
var add_${codeType}_objList = [];

//AUIGrid 생성 후 반환 ID
var add_${codeType}_myGridID;

//AUIGrid 칼럼 설정
var add_${codeType}_columnLayout = [
	{ dataField : "code",				headerText : "${e3ps:getMessage('코드')}",			width:"20%",	style:"aui-grid-editable-cell AUIGrid_Left",
		editRenderer : { // 편집 모드 진입 시 원격 리스트 출력하고자 할 때
		    type : "RemoteListRenderer",
		    fieldName : "code",
			remoter : function( request, response ) { // remoter 반드시 지정 필수
				if(request.term && String(request.term).length < 1) {
					alert("1글자 이상 입력하십시오.");				
					response(false); // 데이터 요청이 없는 경우 반드시 false 삽입하십시오.
					return;
				}
				// 데이터 요청
				var param = new Object();
				
				param["value"] = request.term;
				param["codeType"] = "${codeType}";
				param["endLevel"] = ${endLevel};
				
				var url = getURLString("/common/getNumberCodeListAutoComplete");
				ajaxCallServer(url, param, function(data){

					// 그리드 데이터
					add_${codeType}_objList = data.list;
					
					//중복 제거
					var gridList = AUIGrid.getGridData(add_${codeType}_myGridID);

					for(var i=0; i < gridList.length; i++) {
						add_${codeType}_objList = add_${codeType}_objList.filter(function(item, index, arr){
						    return item.oid != gridList[i].oid;
						});
					}
					
					// 그리드에 데이터 세팅
					response(add_${codeType}_objList); 
					
				});// end of ajax
			},
			listTemplateFunction : function(rowIndex, columnIndex, text, item, dataField, listItem) {
				
				var html = '<div class="myList-style">';
				/* if(listItem.parentName != null && listItem.parentName.length > 0) {
					html += '<span class="myList-col" style="padding-left:10px; width:150px;" title="' + listItem.parentName + '">' + listItem.parentName + '</span>';	
				} */
				html += '<span class="myList-col" style="width:100px;" title="' + listItem.code + '">' + listItem.code + '</span>';
				html += '<span class="myList-col" style="width:360px;" title="' + listItem.name + '">' + listItem.name + '</span>';
				html += '<span class="myList-col" style="width:130px;" title="' + listItem.description + '">' + listItem.description + '</span>';
				html += '</div>';
				
				return html;
			}
		}
	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('이름')}",			width:"20%",	editable: false,		style:"AUIGrid_Left",	},
	{ dataField : "description",			headerText : "${e3ps:getMessage('설명')}",			width:"*",	editable: false},
];

//AUIGrid 를 생성합니다.
function add_${codeType}_createAUIGrid(add_${codeType}_columnLayout) {
	
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
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성
	add_${codeType}_myGridID = AUIGrid.create("#add_${codeType}_grid_wrap", add_${codeType}_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(add_${codeType}_myGridID, "cellClick", add_${codeType}_auiGridCellClickHandler);
	
	// 에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(add_${codeType}_myGridID, "cellEditEnd", add_${codeType}_auiCellEditHandler);
	
	// 에디팅 취소 이벤트 바인딩
	AUIGrid.bind(add_${codeType}_myGridID, "cellEditCancel", add_${codeType}_auiCellEditHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(add_${codeType}_myGridID, gridData);
	
}

function add_${codeType}_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.linkTableName = "${linkTableName}";
	
	AUIGrid.showAjaxLoader(add_${codeType}_myGridID);
	var url = getURLString("/common/getRelatedCodeList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(add_${codeType}_myGridID, gridData);

		AUIGrid.setAllCheckedRows(add_${codeType}_myGridID, false);
		AUIGrid.removeAjaxLoader(add_${codeType}_myGridID);
		
	});
	
}

//셀 클릭 핸들러
function add_${codeType}_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
}

//편집 핸들러
function add_${codeType}_auiCellEditHandler(event) {

	switch(event.type) {
	case "cellEditEnd" :
		if(event.dataField == "code") {
			var item = add_${codeType}_getItem(event.value);

			if(typeof item === "undefined") {
				return;
			}
			
			// ISBN 수정 완료하면, 책제목, 저자 등의 필드도 같이 업데이트 함.
			AUIGrid.updateRow(add_${codeType}_myGridID, {
				code : item.code,
				name : item.name,
				description : item.description,
				oid : item.oid,
			}, event.rowIndex);
		}
		break;
	case "cellEditCancel" :
		break;
	}
};

//추가 버튼
function add_${codeType}_addRow() {
	
	if("${type}" == "single") {
		var gridList = AUIGrid.getGridData(add_${codeType}_myGridID);
		
		if(gridList.length > 0) {
			return; 
		}
	}
	var item = new Object();

	AUIGrid.addRow(add_${codeType}_myGridID, item, "last");
}

//삭제 버튼
function add_${codeType}_removeRow() {

	var checkItemList = AUIGrid.getCheckedRowItems(add_${codeType}_myGridID);
	
	for(var i = 0; i < checkItemList.length; i++){
		
		AUIGrid.removeRowByRowId(add_${codeType}_myGridID, checkItemList[i].item._$uid);
	}
	
	AUIGrid.setAllCheckedRows(add_${codeType}_myGridID, false);
}

//code로 검색해온 정보 아이템 반환.
function add_${codeType}_getItem(code) {
	var item;
	
	$.each(add_${codeType}_objList, function(n, v) {
		if(v.code == code) {
			item = v;
			return false;
		}
	});
	return item;
};

function add_${codeType}_addCodeList(list) {
	
	if("${type}" == "single") {
		AUIGrid.clearGridData(add_${codeType}_myGridID);
	} else {
		var gridList = AUIGrid.getGridData(add_${codeType}_myGridID);
		
		for(var i=0; i < gridList.length; i++) {
			list = list.filter(function(item, index, arr){
			    return item.oid != gridList[i].oid;
			});
		}
	}

	for(var i=0; i < list.length; i++) {
		var item = list[i];
		if(item.hasOwnProperty("children")) {
			delete item.children;
		}
		AUIGrid.addRow(add_${codeType}_myGridID, item, "last");	
	}
};

function add_${codeType}_openCodePopup() {

	var url = getURLString("/common/openCodePopup") + "?codeType=${codeType}&type=${type}";
	
	openPopup(url, "searchCodePopup", 1000, 600);
}
</script>
<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<h4>${title}</h4>
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="add_${codeType}_removeRow()">${e3ps:getMessage('삭제')}</button>
		<c:if test="${codeType ne 'COWORKTEAM'}">
			<button type="button" class="s_bt03" onclick="add_${codeType}_addRow()">${e3ps:getMessage('추가')}</button>
		</c:if>
		<button type="button" class="s_bt03" onclick="add_${codeType}_openCodePopup()">${e3ps:getMessage('검색 추가')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="add_${codeType}_grid_wrap" style="height:${gridHeight}px">
</div>