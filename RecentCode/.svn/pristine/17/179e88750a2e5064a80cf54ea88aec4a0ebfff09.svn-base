<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(search_part_myGridID);
	
	//grid setting
	search_part_createAUIGrid(search_part_columnLayout);
	
	//get grid data
	//search_part_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var search_part_myGridID;

//AUIGrid 칼럼 설정
var search_part_columnLayout = [
	{ dataField : "select",		headerText : "${e3ps:getMessage('선택')}",		width:"50",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var template = "<button type='button' onclick='select(" + rowIndex + ");'>${e3ps:getMessage('선택')}</button>";
			return template; // HTML 형식의 스트링
		}
	},
	{ dataField : "icon",		headerText : "",		width:"30",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
	},
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
	{ dataField : "name",				headerText : "${e3ps:getMessage('부품 명')}",			width:"30%",		style:"AUIGrid_Left",	},
	{ dataField : "stateName",				headerText : "${e3ps:getMessage('상태')}",				width:"10%",	},
	{ dataField : "version",			headerText : "${e3ps:getMessage('버전')}",				width:"10%",	},
	{ dataField : "creator",			headerText : "${e3ps:getMessage('등록자')}",			width:"10%",	},
	{ dataField : "modifyDate",			headerText : "${e3ps:getMessage('최종 수정일')}",			width:"15%",	},
];

//AUIGrid 를 생성합니다.
function search_part_createAUIGrid(search_part_columnLayout) {
	
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
	};

	// 실제로 #grid_wrap 에 그리드 생성
	search_part_myGridID = AUIGrid.create("#search_part_grid_wrap", search_part_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(search_part_myGridID, "cellClick", search_part_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(search_part_myGridID, gridData);
}

function search_part_getGridData(){

	var param = new Object();
	
	param["number"] = $("#searchNumber").val();
	param["name"] = $("#searchName").val();
	param["SPECIFICATION"] = $("#searchSpec").val();
	param["unit"] = $("#searchUnit").val();
	
	console.log(param);
	
	AUIGrid.showAjaxLoader(search_part_myGridID);
	var url = getURLString("/part/searchPartAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(search_part_myGridID, gridData);

		AUIGrid.setAllCheckedRows(search_part_myGridID, false);
		AUIGrid.removeAjaxLoader(search_part_myGridID);
		
	});
}

//셀 클릭 핸들러
function search_part_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

function searchValue(input){
	$(input).keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
}
function search() {
	$("#search_part_grid_wrap").css("display","");
	
	$(".list").each(function(){
    	AUIGrid.resize("#" + $(this).attr("id"));
	});
	
	search_part_getGridData();
}

function select(rowIndex) {
	
	var item = AUIGrid.getItemByRowIndex(search_part_myGridID, rowIndex);
	
	var param = new Object();
	
	param["oid"] = item.oid;
	
	var url = getURLString("/part/getPartInfo");
	ajaxCallServer(url, param, function(data){
		var part = data.part;
		
		$("#name").val(part.name);
		$("#unit").val(part.unit);
		
		var attributes = part.attributes;
		
		for(key in attributes){
			if(key == "SNOCK") {
				$("#" + key).prop("checked", attributes[key]);
			} else {
				$("#" + key).val(attributes[key]);
			}
		}
	});
	
	setPartNumber(item.number);
	
	//$("#search_part_grid_wrap").css("display","none");
	
	$(".list").each(function(){
    	AUIGrid.resize("#" + $(this).attr("id"));
	});
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt">
		<span class="title">${e3ps:getMessage('부품 검색')}</span>
	</div>
	<div class="rightbt">
		<span>${e3ps:getMessage('부품 번호')} : </span><input type="text" class="w10" id="searchNumber" name="searchNumber" onkeyup="javascript:strToUpperCase(this);" onkeydown="javascript:searchValue(this);">
		<span>${e3ps:getMessage('부품 명')} : </span><input type="text" class="w10" id="searchName" name="searchName" onkeydown="javascript:searchValue(this);">
		<span>${e3ps:getMessage('규격')} : </span><input type="text" class="w10" id="searchSpec" name="searchSpec" onkeydown="javascript:searchValue(this);">
		<span>${e3ps:getMessage('단위')} : </span><input type="text" class="w10" id="searchUnit" name="searchUnit" onkeydown="javascript:searchValue(this);">
		<button type="button" class="s_bt03" onclick="search();">${e3ps:getMessage('검색')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="search_part_grid_wrap" style="height:${gridHeight}px;display:none;">
</div>