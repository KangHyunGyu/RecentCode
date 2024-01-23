<%-----------------------------------------------------------------------------------------------------
* 화면명         	: 부품,도면,문서 추가 화면
* 프로그램 명   	: common/include/addObject.jsp
* 설명		: 부품,도면,문서 추가 화면
* objType   : part,epm,doc 
* pageName  : include에서 의 구분 UI
* title	    : 
--------------------------------------------------------------------------------------------------------%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script>
$(document).ready(function(){
	
	AUIGrid.destroy(add_${pageName}_myGridID);
	
	add_${pageName}_createAUIGrid(add_${pageName}_columnLayout);
	
	AUIGrid.resize(add_${pageName}_myGridID);
	//get grid data
	if("${oid}".length > 0) {
		add_${pageName}_getGridData();
	}
	
	if("${pageName}" == "ownershipPart") {
		add_${pageName}_addRow();
	}
	
	if("${pageName}" == "addDetailProject"){
		loadPageCache();
	}
	
	/* if("${oid}") {
		var oid = "${oid}";
		var url = getURLString("/multi/getTargetObjects");
		ajaxCallServer(url, {oid: oid}, function(data){
			add_${pageName}_addObjectList(data.list);
		});
		
	} */
});
//사용자 타이핑의 민감도 체크를 위한 타이머
var timerId = null;

var add_${pageName}_objList = [];

//AUIGrid 생성 후 반환 ID
var add_${pageName}_myGridID;

//AUIGrid 칼럼 설정
var add_${pageName}_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('번호')}",			width:"20%",	style:"aui-grid-editable-cell AUIGrid_Left",
		editRenderer : { // 편집 모드 진입 시 원격 리스트 출력하고자 할 때
		    //type : "RemoteListRenderer",
		    //fieldName : "number",
			type : "RemoteListRenderer",
		    showEditorBtnOver : true, // 마우스 오버 시 에디터버턴 보이기
		    autoCompleteMode : true, // 자동완성 모드 설정 (기본값 :false)
			autoEasyMode : true, // 자동완성 모드일 때 첫 아이템 자동 선택할지 여부 (기본값 : false)
			keyField : "number", // key 에 해당되는 필드명
		    fieldName : "number",
		    placeholder : "Please enter at least 3 characters.",
		    remoter : function( request, response ) { // remoter 반드시 지정 필수
		    	/*
				if(request.term && String(request.term).length < 4) {
					alert("4글자 이상 입력하십시오.");				
					response(false); // 데이터 요청이 없는 경우 반드시 false 삽입하십시오.
					return;
				}
		    	*/
		    	if(String(request.term).length <= 1 ) {
					response([{number: "Please enter 2 more characters.", name: "", rev: ""}]); // term 이 없는 경우 기본값을 보여줄지, 아니면 빈 것을 보여줄지...선택해서 코딩.
					response(false); // 데이터 요청이 없는 경우 반드시 false 삽입하십시오.
					return;
				}else if(String(request.term).length <= 2){
					response([{number: "Please enter 1 more characters.", name: "", rev: ""}]); // term 이 없는 경우 기본값을 보여줄지, 아니면 빈 것을 보여줄지...선택해서 코딩.
					response(false); // 데이터 요청이 없는 경우 반드시 false 삽입하십시오.
					return;
				}
		    	
		    	if(timerId != null) {
					clearTimeout(timerId);
				}
		    	
		    	// 데이터 요청
				var param = new Object();
				param["number"] = request.term;
				param["objType"] = "${objType}";
				param["likeSearch"] = "like";
				
				
				if("${moduleType}" == "ECO" || "${moduleType}" == "ECR") {
					param["state"] = "APPROVED";
					param["moduleType"] = "${moduleType}";
				} else if("${moduleType}" == "multiApproval") {
					param["state"] = "INWORK";
					param["moduleType"] = "${moduleType}";
				}else if("${moduleType}" == "distribute" || "${moduleType}" == "distributeTemp") {
					//param["state"] = "APPROVED";
					param["moduleType"] = "${moduleType}";
				}
					
				var url = getURLString("/common/searchObjectAction");
				ajaxCallServer(url, param, function(data){
					
					// 그리드 데이터
					add_${pageName}_objList = data.list;
					
					//중복 제거
					var gridList = AUIGrid.getGridData(add_${pageName}_myGridID);

					for(var i=0; i < gridList.length; i++) {
						add_${pageName}_objList = add_${pageName}_objList.filter(function(item, index, arr){
						    return item.oid != gridList[i].oid;
						});
					}
					
					// 그리드에 데이터 세팅
					response(add_${pageName}_objList); 
					
				});// end of ajax
				
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
	{ dataField : "name",				headerText : "${e3ps:getMessage('이름')}",			width:"*",	editable: false,		style:"AUIGrid_Left",
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
	//{ dataField : "location",			headerText : "${e3ps:getMessage('분류')}",			width:"20%",	editable: false},
	{ dataField : "rev",			headerText : "${e3ps:getMessage('버전')}",			width:"20%",	editable: false},
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",			width:"20%",	editable: false},
	//{ dataField : "creatorFullName",	headerText : "${e3ps:getMessage('작성자')}",			width:"20%",	editable: false},
	{ dataField : "linkStateStr",			headerText : "${e3ps:getMessage('배포 상태')}",			width:"13%",	editable: false, visible : false},
];

//AUIGrid 를 생성합니다.
function add_${pageName}_createAUIGrid(add_${pageName}_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
// 		selectionMode : "singleRow",
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
	add_${pageName}_myGridID = AUIGrid.create("#add_${pageName}_grid_wrap", add_${pageName}_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(add_${pageName}_myGridID, "cellClick", add_${pageName}_auiGridCellClickHandler);
	
	// 에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(add_${pageName}_myGridID, "cellEditEnd", add_${pageName}_auiCellEditHandler);
	
	// 에디팅 취소 이벤트 바인딩
	AUIGrid.bind(add_${pageName}_myGridID, "cellEditCancel", add_${pageName}_auiCellEditHandler);
	

	AUIGrid.bind(add_${pageName}_myGridID, "keyDown",	function(event) {
		
		// 정보 출력
		if( typeof search == 'function' ) {
			search();
		}
		
		if(event.keyCode == 13) { // 엔터 키
			if( typeof search == 'function' ) {
				search();
			}
		}
		return true; // 기본 행위 유지
	});
	
	
	if("${objType}"=="part" && ("${moduleType}" == "distributeTemp" || "${moduleType}" == "distribute")){
		AUIGrid.bind(add_${pageName}_myGridID, "removeRow", function( event ) {
			//isCheckDWG = false;
			var rows = AUIGrid.getRowIndexesByValue(check_drawing_myGridID, "number", event.items[0].number);
			AUIGrid.removeRow(check_drawing_myGridID, rows);
		});
		AUIGrid.bind(add_${pageName}_myGridID, "addRow", function( event ) {
			isCheckDWG = false;
		});	
	}
	if("${objType}"=="epm" && "${moduleType}" == "distribute"){
		AUIGrid.showColumnByDataField(add_${pageName}_myGridID, "linkStateStr");
		/* AUIGrid.bind(add_${pageName}_myGridID, "removeRow", function( event ) {
			isCheckDWGEpm = false;
		}); */
		AUIGrid.bind(add_${pageName}_myGridID, "addRow", function( event ) {
			isCheckDWGEpm = false;
		});	
	}
	
	var gridData = new Array();
	AUIGrid.setGridData(add_${pageName}_myGridID, gridData);
	
	if("part" == "${objType}") {
		var columnObj = { dataField : "bom",		headerText : "BOM",		width:"8%",	editable: false,
			/* renderer : {
				type : "ButtonRenderer",
				labelText : "BOM",
				onclick : function(rowIndex, columnIndex, value, item) {
					var oid = item.oid;
					
					if(oid.length > 0) {
						var url = getURLString("/bom/searchBomPopup") + "?oid=" + oid + "&pageName=${pageName}";
						
						openPopup(url,"searchBomPopup", 1024, 600);
					}
				}
			} */
			renderer : { // HTML 템플릿 렌더러 사용
				type : "TemplateRenderer"
			},
			labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
				
				var icon = "/Windchill/jsp/portal/images/bom_icon.png";
				
				var template = "";
				
				if(item.number != null && item.number.length > 0) {
					template = "<img title='${e3ps:getMessage('BOM 검색')}' class='pointer' onclick='add_${pageName}_searchBomPopup(\"" + item.oid + "\")' src='" + icon + "'/>";
				}
				
				return template;
			}
		};
		
		AUIGrid.addColumn(add_${pageName}_myGridID, columnObj, "last");
	}
}

function add_${pageName}_getGridData(){

	var param = new Object();
	
	param["oid"] = "${oid}";
	param["objType"] = "${objType}";
	param["pageName"] = "${pageName}";
	
	AUIGrid.showAjaxLoader(add_${pageName}_myGridID);
	var url = getURLString("/common/getObjectList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(add_${pageName}_myGridID, gridData);

		AUIGrid.setAllCheckedRows(add_${pageName}_myGridID, false);
		AUIGrid.removeAjaxLoader(add_${pageName}_myGridID);
		
	});
}

//셀 클릭 핸들러
function add_${pageName}_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
}

//편집 핸들러
function add_${pageName}_auiCellEditHandler(event) {

	switch(event.type) {
	case "cellEditEnd" :
		if(event.dataField == "number") {
			var item = add_${pageName}_getItem(event.value);

			if(typeof item === "undefined") {
				AUIGrid.updateRow(add_${pageName}_myGridID, {
					number : "",
					name : "",
					location : "",
					version : "",
					rev : "",
					creatorFullName : "",
					oid : "",
				}, event.rowIndex);
				return;
			}
			
			// ISBN 수정 완료하면, 책제목, 저자 등의 필드도 같이 업데이트 함.
			AUIGrid.updateRow(add_${pageName}_myGridID, {
				number : item.number,
				name : item.name,
				location : item.location,
				version : item.version,
				rev : item.rev,
				creatorFullName : item.creatorFullName,
				stateName : item.stateName,
				oid : item.oid,
			}, event.rowIndex);
			
			if("${pageName}" == "addDetailProject"){
				pageCaching();
			}
		}
		break;
	case "cellEditCancel" :
		break;
	}
	
};

//추가 버튼
function add_${pageName}_addRow() {
	
	if("${type}" == "single") {
		var gridList = AUIGrid.getGridData(add_${pageName}_myGridID);
		
		if(gridList.length > 0) {
			return; 
		}
	}
	var item = new Object();

	AUIGrid.addRow(add_${pageName}_myGridID, item, "last");
}

//삭제 버튼
function add_${pageName}_removeRow() {

	var checkItemList = AUIGrid.getCheckedRowItems(add_${pageName}_myGridID);
	
	for(var i = 0; i < checkItemList.length; i++){
		
		AUIGrid.removeRowByRowId(add_${pageName}_myGridID, checkItemList[i].item._$uid);
	}
	
	AUIGrid.setAllCheckedRows(add_${pageName}_myGridID, false);
	
	if("${pageName}" == "addDetailProject"){
		deleteProjectPageCache(checkItemList);
	}
}

//name 으로 검색해온 정보 아이템 반환.
function add_${pageName}_getItem(number) {
	var item;
	
	$.each(add_${pageName}_objList, function(n, v) {
		if(v.number == number) {
			item = v;
			return false;
		}
	});
	return item;
};

function add_${pageName}_addObjectList(list) {
	
	if("${pageName}"=="createDocMulti"){
		return;
	}
	
	if("${type}" == "single") {
		AUIGrid.clearGridData(add_${pageName}_myGridID);
	} else {
		var gridList = AUIGrid.getGridData(add_${pageName}_myGridID);
		
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
		AUIGrid.addRow(add_${pageName}_myGridID, item, "last");	
	}
	
};

function add_${pageName}_searchObjectPopup(rowId) {

	var urlAppender = "?objType=${objType}&pageName=${pageName}&type=${type}&moduleType=${moduleType}";
	
	if(rowId != null && rowId.length > 0 ){
		urlAppender += "&rowId="+rowId;
	}
	
	var url = getURLString("/common/searchObjectPopup") + urlAppender;
	
	openPopup(url, "searchObjectPopup");
}

function add_${pageName}_searchBomPopup(oid) {

	var url = getURLString("/bom/searchBomPopup") + "?oid=" + oid + "&pageName=${pageName}&moduleType=${moduleType}";
	
	openPopup(url,"searchBomPopup", 1400, 600);
	
}

/**
 * 그리드 데이터 입력 여부 체크
 */
function check_${pageName}_grid(){
	var gridList = AUIGrid.getGridData(add_${pageName}_myGridID);
	
	if(gridList.length ==0){
		return false;
	}
	
	for(var i = 0 ; i < gridList.length ; i++){
		var item = gridList[i];
		if(isEmpty(item.oid)){
			return false;
		}
	}
	
	return true;
}


function related_${objType}Forming(form){
	
	var param = form;
	
	var addedItem =	AUIGrid.getAddedRowItems(add_${pageName}_myGridID);
	Array.prototype.forEach.call(addedItem, function(cacheData, idx){ 
		param.append("multi_rel_${objType}_target", cacheData.targetRowId);           
		param.append("multi_rel_${objType}_oid", cacheData.oid);                      
	});                                                                             

	
	//삭제 대상 링크                                                            
	var removedItem = AUIGrid.getRemovedItems(add_${pageName}_myGridID);
	if(removedItem.length > 0){                                       
		Array.prototype.forEach.call(removedItem, function(part, idx){
			param.append("del_rel_part_oid", part.oid);                   
		});                                                               
	}                                                                     
	
	return param;
}

</script>

<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<span class="title">
			<c:if test="${toggle}">
				<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			</c:if>
			${title}
		</span>
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="add_${pageName}_removeRow()">${e3ps:getMessage('삭제')}</button>
		<button type="button" class="s_bt03" onclick="add_${pageName}_addRow()">${e3ps:getMessage('추가')}</button>
		<button type="button" class="s_bt03" onclick="add_${pageName}_searchObjectPopup()">${e3ps:getMessage('검색 추가')}</button>
		<c:if test="${moduleType=='distribute' && objType == 'part'}">
			<button type="button" class="s_bt03" onclick="checkDrawing()">${e3ps:getMessage('검증')}</button>
		</c:if>
		<c:if test="${moduleType=='distribute' && objType == 'epm'}">
			<button type="button" class="s_bt03" onclick="checkDrawingEpm()">${e3ps:getMessage('검증')}</button>
		</c:if>
	</div>
</div>
<!-- //button -->
<div class="list" id="add_${pageName}_grid_wrap">
</div>
