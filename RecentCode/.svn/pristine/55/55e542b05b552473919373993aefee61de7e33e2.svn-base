<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script>
$(document).ready(function(){
	AUIGrid.destroy(add_distributePartList_myGridID);
	
	//grid setting
	add_distributePartList_createAUIGrid(add_distributePartList_columnLayout);
	
	AUIGrid.resize(add_distributePartList_myGridID);
	
	if('${type}' == 'view'){
		document.querySelectorAll('.required').forEach((ele)=>{
			ele.style.display = 'none';
		})
	}
	
	if('${oid}' != ''){
		add_distributePartList_getGridData();
	}
	
});

//사용자 타이핑의 민감도 체크를 위한 타이머
var timerId = null;

//AUIGrid 생성 후 반환 ID
var add_distributePartList_myGridID;

var add_objList = [];


//AUIGrid 칼럼 설정
var add_distributePartList_columnLayout = [
	{ dataField : "icon",		headerText : "",		width:"30", editable : false,
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
	},
	{ dataField : "distPartNumber",		headerText : "${e3ps:getMessage('품목 번호')}<span class='required'>*</span>",			width:"20%",
		editRenderer : { // 편집 모드 진입 시 원격 리스트 출력하고자 할 때
		    type : "RemoteListRenderer",
		    showEditorBtnOver : true, // 마우스 오버 시 에디터버턴 보이기
		    autoCompleteMode : true, // 자동완성 모드 설정 (기본값 :false)
			autoEasyMode : true, // 자동완성 모드일 때 첫 아이템 자동 선택할지 여부 (기본값 : false)
			keyField : "distPartNumber", // key 에 해당되는 필드명
		    fieldName : "number",
			remoter : function( request, response ) { 
				
				if(String(request.term).length <= 2 ) {
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
		
		},
		tooltip : {
	        tooltipFunction : function(rowIndex, columnIndex, value, headerText, item, dataField) {
	        	var str ='';
	        	if('${type}' != 'view'){
	        		str = "<span style='color:#f00;'>※ ${e3ps:getMessage('승인된 품목만 검색됩니다')}</span>";
	        	}
	            return str;
	   		}
		}
	},
	{ dataField : "distPartName",		headerText : "${e3ps:getMessage('품목 명')}",	style:"AUIGrid_Left",		width:"*%",	sortValue : "master>name", editable : false,
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", 
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.distPartOid;
				openView(oid);
			}
		}	
	},
	{ dataField : "version",		headerText : "${e3ps:getMessage('버전')}",			width:"6%", editable : false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",		headerText : "${e3ps:getMessage('상태')}",			width:"6%", editable : false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "bom",		headerText : "BOM",		width:"4%", editable : false,
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			
			var icon = "/Windchill/jsp/portal/images/bom_icon.png";
			
			var template = "<img title='${e3ps:getMessage('BOM 보기')}' class='pointer' onclick='openBomTree(\"" + item.distPartOid + "\")' src='" + icon + "'/>";
			
			return template;
		}
	},
];


function add_distributePartList_createAUIGrid(add_distributePartList_columnLayout) {
	
	let editYN = '${type}'=='view'?false:true; 
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : editYN,
		
		editable : editYN,
		
		enableSorting : false,
		
		softRemoveRowMode : false,
		
		noDataMessage : gridNoDataMessage,
		
		autoGridHeight : false,
		
		height : ${gridHeight},
		
		//툴팁 출력 지정
		showTooltip : true,
		
		//툴팁 마우스 대면 바로 나오도록 
		tooltipSensitivity: 500
	};

	// 실제로 #grid_wrap 에 그리드 생성
	add_distributePartList_myGridID = AUIGrid.create("#add_distributePartList_grid_wrap", add_distributePartList_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(add_distributePartList_myGridID, "cellClick", add_distributePartList_auiGridCellClickHandler);
	
	// 에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(add_distributePartList_myGridID, "cellEditEnd", add_distributePartList_auiCellEditHandler);
	
	// 에디팅 취소 이벤트 바인딩
	AUIGrid.bind(add_distributePartList_myGridID, "cellEditCancel", add_distributePartList_auiCellEditHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(add_distributePartList_myGridID, gridData);
	
}


//셀 클릭 핸들러
function add_distributePartList_auiGridCellClickHandler(event) {
	
}

//편집 핸들러
function add_distributePartList_auiCellEditHandler(event) {
	
	var editedItem = new Object();
		
	switch(event.type) {
	case "cellEditEnd" :
		if(event.dataField == "distPartNumber") {
			
			var item = add_getItem(event.value);
			
			if(typeof item === "undefined" || isEmpty(item) ) {
				AUIGrid.updateRow(add_distributePartList_myGridID, {
					icon : "",
					distPartNumber : "",
					distPartName : "",
					version : "",
					stateName : "",
					distPartOid : "",
					bom : "",
				}, event.rowIndex);
				return;
			}
			
			let currentPartsList = AUIGrid.getGridData(add_distributePartList_myGridID);
			if(currentPartsList.some( (x) => {return x.distPartOid == item.oid})) {
				alert("${e3ps:getMessage('이미 추가되어있는 부품입니다')}");
				AUIGrid.updateRow(add_distributePartList_myGridID, {
					distPartNumber : "",
				}, event.rowIndex);
				return;
			}
			
			AUIGrid.updateRow(add_distributePartList_myGridID, {
				icon : item.icon,
				distPartName : item.name,
				version : item.rev,
				stateName : item.stateName,
				distPartOid : item.oid,
				bom : item.bom,
			}, event.rowIndex);
		}
		
		
		
		break;
	case "cellEditCancel" :
		break;
	}
};

//추가 버튼
function add_distributePart(checkPartList) {
	let currentPartsList = AUIGrid.getGridData(add_distributePartList_myGridID);
	
	//AUIGrid Column 에 맞게 수정
	checkPartList.map((x) => {
		x.distPartOid = x.oid;
		x.distPartNumber = x.number;
		x.distPartName = x.name;
		x.version = x.rev;
	})
	
	//같은 품목 필터링
	checkPartList = checkPartList.filter((x) => {
		return !currentPartsList.some(z => z.distPartOid == x.distPartOid )
	})

	AUIGrid.appendData(add_distributePartList_myGridID, checkPartList);
}


<%----------------------------------------------------------
*                      그리드 Row 추가 
----------------------------------------------------------%>
function addRow() {

	// 그리드의 편집 인푸터가 열린 경우 에디팅 완료 상태로 만듬.
	AUIGrid.forceEditingComplete(add_distributePartList_myGridID, null);
	
	var item = new Object();
	item.distPartNumber ="";
	item.distPartName = "";
	item.version = "";
	item.stateName ="";
	item.bom ="";
	
	
	AUIGrid.addRow(add_distributePartList_myGridID, item, "last");
}
<%----------------------------------------------------------
*                      그리드 Row 삭제
----------------------------------------------------------%>
function removeRow() {
		
		var checkItemList = AUIGrid.getCheckedRowItems(add_distributePartList_myGridID);
		
		for(var i = 0; i < checkItemList.length; i++){
			
			AUIGrid.removeRowByRowId(add_distributePartList_myGridID, checkItemList[i].item._$uid);
		}
		
		AUIGrid.setAllCheckedRows(add_distributePartList_myGridID, false); 
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

function add_distributePartList_getGridData() {
	
	var param = new Object();
	param["oid"] = '${oid}';
	
	AUIGrid.showAjaxLoader(add_distributePartList_myGridID);
	var url = getURLString("/distribute/getDistributePartListByDistribute");
	ajaxCallServer(url, param, function(data){
		var gridData = data.list;
		AUIGrid.setGridData(add_distributePartList_myGridID, gridData);
		AUIGrid.setAllCheckedRows(add_distributePartList_myGridID, false);
		AUIGrid.removeAjaxLoader(add_distributePartList_myGridID);
		
	},false);
}

function add_part_searchObjectPopup() {

	var url = getURLString("/distribute/searchDistPartPopup");
	
	openPopup(url, "searchObjectPopup", 1300, 800);
}

</script>
<!-- button -->
<br/>
<div class="seach_arm2 pb5">
	<div class="leftbt">
		<span class="title">
			<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			${title}
		</span>
	</div>
	<div class="rightbt" id="partListRightBtn">
	<c:if test="${type ne 'view'}">
		<button type="button" class="i_read" onclick="add_part_searchObjectPopup()">${e3ps:getMessage('검색 추가')}</button>
		<button type="button" class="i_read" style="width:60px" onclick="addRow()" id="addBtn">${e3ps:getMessage('추가')}</button>
		<button type="button" class="i_delete" style="width:60px" onclick="removeRow()">${e3ps:getMessage('삭제')}</button>
	</c:if>
	</div>
</div>
<!-- //button -->
<div class="list " id="add_distributePartList_grid_wrap" style="height:${gridHeight};">
</div>
