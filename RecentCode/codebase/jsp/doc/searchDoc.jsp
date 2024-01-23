<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
.SumoSelect {
	width: 58%;
}

/* 헤더 메뉴 스타일 정의 */
#headerMenu {
	position: absolute;
	display: none;
	z-index: 999;
}
/* jQuery UI Menu 스타일 재정의 */
.ui-menu {
	width: 160px;
	font-size: 12px;
	box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
	-webkit-box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
	-moz-box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.3);
}
</style>
<script>
$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//hgkang
	// 그리드 칼럼 구조에 따라 체크박스 메뉴 아이템 만들기
// 	var chkHtml = genColumnHtml(columnLayout);
// 	$("#h_item_ul").append(chkHtml);

	// 헤더 에서 사용할 메뉴 위젯 구성
// 	console.log("headerCheck ::: " + $("#headerMenu"));
// 	$("#headerMenu").menu({
// 		select : headerMenuSelectHandler
// 	});
	//-----------------hgkang
	
	getDocAttributes();
	//-----------------hgkang
	document.getElementById("locationDisplay").addEventListener("change", locationSelectChange);
	//-----------------hgkang
// 	getDocumentTypes();
	
	//lifecycle list
	getLifecycleList("LC_Default");
	autocompleteProject("project_code", null);
	//grid setting
	createAUIGrid(columnLayout);
	
// 	$("input:checkbox[id='likeSearch']").prop("checked", true); 
	docNumberSearchOption();
	
	//get grid data(트리 존재시 트리에서 로딩)
	getGridData();
	
	//프로젝트/설계변경 자동완성 초기화
	//initAutocompleteEChangeProject();
});

//AUIGrid 생성 후 반환 ID
var myGridID;

//현재 페이지
var page = 1;

//row 로딩 개수
var rows = 20;

// 중복 요청을 피하기 위한 플래그
var nowRequesting = false;

//마지막 페이지 여부
var isLastPage = false;

//소트 값
var sortValue = "";

//소트 값으로 소팅되었는지 체크하는 값
var sortCheck = true;

//AUIGrid 칼럼 설정
var columnLayout = [
	
	/* { dataField : "containerName",		headerText : "${e3ps:getMessage('사업구분')}",		width:"5%",		sortValue : "continerName",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	}, */
// 	docAttribute
	{ 
		dataField : "docAttributeName", 
		headerText : "${e3ps:getMessage('문서 유형')}", 
		width:"10%",	
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "number", 
		headerText : "${e3ps:getMessage('문서 번호')}",	 
		width:"13%", 
		style:"AUIGrid_Left", 
		sortValue : "master>number",
		filter : {
			showIcon : true,
			iconWidth:20
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.oid;
				openView(oid);
			}
		}},
	{ 
		dataField : "name", 
		headerText : "${e3ps:getMessage('문서 명')}", 
		width:"*", 
		style:"AUIGrid_Left", 
		sortValue : "master>name",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "version", 
		headerText : "${e3ps:getMessage('버전')}", 
		sortValue : "version",
		width:"5%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "stateName", 
		headerText : "${e3ps:getMessage('상태')}", 
		width:"5%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "lastApprover",		
		headerText : "${e3ps:getMessage('최종 승인자')}",		
		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:20
		}
	},	
	{ 
		dataField : "creatorFullName",		
		headerText : "${e3ps:getMessage('등록자')}",		
		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "createDateFormat",	
		headerText : "${e3ps:getMessage('등록일')}",	
		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "modifierFullName",
		headerText : "${e3ps:getMessage('수정자')}",
		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "modifyDateFormat",
		headerText : "${e3ps:getMessage('수정일')}",
		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "relPartsString", 
		headerText : "${e3ps:getMessage('관련 품목')}", 
		width:"5%", style:"AUIGrid_Left",
		sortValue : "master>name",
		filter : {
			showIcon : true,
			iconWidth:20
		},
		visible : false,
	},
	/*
 	{ dataField : "primaryURL",
     	headerText : "파일",
     	width : "4%",
     	renderer : {
            type : "TemplateRenderer",
	    },
     	labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { //HTML 템플릿 작성
            return '<a href="'+value+'">'+item.fileIcon+'</a>';
    	}
 	},
	{ dataField : "fileSize",		headerText : "${e3ps:getMessage('용량')}",		width:"5%"}, */
];

//AUIGrid 를 생성합니다.
function createAUIGrid(columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		showSelectionBorder : true,
		noDataMessage : gridNoDataMessage,
		rowIdField : "_$uid",
		showRowNumColumn : true,
		showEditedCellMarker : false,
		wrapSelectionMove : true,
		showRowCheckColumn : true,
		enableFilter : true,
		enableMovingColumn : true,
		headerHeight : gridHeaderHeight,
		rowHeight : gridRowHeight,
		enableMovingColumn : true,
		
	};

	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(myGridID, "cellClick", auiGridCellClickHandler);
	
	// 스크롤 체인지 이벤트 바인딩
	AUIGrid.bind(myGridID, "vScrollChange", vScollChangeHandelr);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

function getGridData(){
	
	$("#searchForm").attr("action", getURLString("/doc/searchDocScrollAction"));
	
	var param = new Object();
	
	param["page"] = page;
	param["rows"] = rows;
	param["container"] = $("#container option:selected").val();
	//param["sortValue"] = sortValue;
	//param["sortCheck"] = sortCheck;
	
	if(page == 1) {
		AUIGrid.showAjaxLoader(myGridID);
	}
	
	var data = formSubmit("searchForm", param, null, function(data){
		
		var gridData = data.list;
		var count = $("#count").html();
		if(page == 1) {
			// 그리드에 데이터 세팅(첫 요청)
			AUIGrid.setGridData(myGridID, gridData);	
			count = gridData.length;
		} else {
			// 그리드에 데이터 세팅(추가 요청)
			AUIGrid.appendData(myGridID, gridData);
			
			count = parseInt(count) + gridData.length;
		}
		
		$("#count").html(count);
		$("#total").html(data.totalSize);
		$("#sessionId").val(data.sessionId);
		
		if(gridData.length == 0) {
			isLastPage = true;
		}
		
		AUIGrid.removeAjaxLoader(myGridID);
		nowRequesting = false;
	});
	
}

//셀 클릭 핸들러
function auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}


//스크롤 체인지 핸들어에서 무리한 작업을 하면 그리드 퍼포먼스가 떨어집니다.
//따라서 무리한 DOM 검색은 자제하십시오.
function vScollChangeHandelr(event) {
	
	// 스크롤 위치가 마지막과 일치한다면 추가 데이터 요청함
	if(event.position == event.maxPosition) {
		if(!isLastPage) {
			if(!nowRequesting) {
				page++;
				getGridData();
			}
		}
	}
	
}

//검색
function search(){
	isLastPage = false;
	page = 1;
	sortValue = "";
	$("#sessionId").val("");
	getGridData();
}

//검색조건 초기화
function reset(){
	var locationDisplay = $("#locationDisplay").val();
	$("#searchForm")[0].reset();
	$("#locationDisplay").val(locationDisplay);
	//AUIGrid.setSelectionByIndex(docFolder_tree_myGridID, 0, 0);
	//search();
}

//필터 초기화
function resetFilter(){
    AUIGrid.clearFilterAll(myGridID);
}

function xlsxExport() {
	AUIGrid.setProperty(myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(myGridID, exportProps);
}

function changeLoadingRows(selected){
	rows = parseInt($(selected).val());
	isLastPage = false;
	page = 1;
	sortValue = "";
	$("#sessionId").val("");
	getGridData();
}

function setOutPeople(id, item){
	$("#" + id).trigger("change");
	$("#" + id).append("<option value='" + item.oid + "' selected>" + item.name +"[" + item.personalId + "]"  + "</option>");
	$("#" + id).val(item.oid);
}

function locationSelectChange(){ 
	const locationBox = document.getElementById("locationDisplay");
	const locationValue = locationBox.value;
	console.log('locationValue : ',locationValue);
	
}

</script>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
			<h4>
				<img class="pointer" onclick="switchSearchMenu(this);"
					src="/Windchill/jsp/portal/images/minus_icon.png">&nbsp;${e3ps:getMessage('검색 조건')}
			</h4>
		</div>
		<div class="rightbt">
			<button type="button" class="i_read" style="width: 70px;"
				id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="i_read" style="width: 70px;"
				id="switchDetailBtn" onclick="switchDetailBtn();">${e3ps:getMessage('상세검색')}</button>
			<button type="button" class="i_update" style="width: 70px;"
				id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table -->
	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm" style="margin-bottom: 0px;">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<input type="hidden" id="mode" name="mode" value="search"> <input
				type="hidden" id="cFolderOid" name="cFolderOid" value=""> <input
				type="hidden" id="cFolderPath" name="cFolderPath" value="">
			<table class="mainTable">
				<colgroup>
					<col style="width: 15%">
					<col style="width: 35%">
					<col style="width: 15%">
					<col style="width: 35%">
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('문서 분류')}</th>
						<td><input type="text" id="locationDisplay"
							name="locationDisplay" class="w60" disabled></td>
						<th scope="col">${e3ps:getMessage('버전')}</th>
						<td><input type="radio" id="version" name="version"
							value="new" checked> <label>${e3ps:getMessage('최신 버전')}</label>
							<span style="width: 10%; display: inline-block;"></span> <input
							type="radio" id="version" name="version" value="all"> <label>${e3ps:getMessage('모든 버전')}</label>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('문서 명')}</th>
						<td><input type="text" id="name" name="name" class="w60"></td>
						<th scope="col">${e3ps:getMessage('상태')}</th>
						<td><select class="multiSelect w10" id="state" name="state"
							multiple style="height: 20px; overflow-y: hidden;">
						</select></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('문서 유형')}</th>
						<td><select class="multiSelect w10" id="docAttribute"
							name="docAttribute" multiple
							style="height: 20px; overflow-y: hidden;">
						</select></td>
						<th scope="col">${e3ps:getMessage('문서 번호')}</th>
						<td>
						<span id="autoDocSearch"> 
							<select
								class="searchRelatedObject" id="relatedDoc" name="relatedDoc"
								multiple data-param="doc" data-width="58%">
							</select> 
								<span
								class="pointer verticalMiddle"
								onclick="javascript:deleteUser('relatedDoc');"><img
									class="verticalMiddle"
									src='/Windchill/jsp/portal/images/delete_icon.png'>
								</span>
						</span> 
						<span id="textDocSearch" style="display: none"> <input
								type="text" name="number" id="number" class="w58">
						</span> <input type="checkBox" id="likeSearch" name="likeSearch"
							onchange="docNumberSearchOption()"><label>Like
								${e3ps:getMessage('검색')} </label></td>
						<%-- <th scope="col">${e3ps:getMessage('관련 품목')}</th>
						<td>
						<div class="pro_view">
							<select class="searchRelatedObject" id="relatedPart" name="relatedPart" multiple data-param="part" data-width="58%"></select>
							<span class="pointer verticalMiddle" onclick="javascript:deleteUser('relatedPart');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</div>
						</td> --%>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('등록일')}</th>
						<td class="calendar"><input type="text"
							class="datePicker w21" name="predate" id="predate" readonly /> ~
							<input type="text" class="datePicker w21" name="postdate"
							id="postdate" readonly /></td>
						<th scope="col">${e3ps:getMessage('수정일')}</th>
						<td class="calendar"><input type="text"
							class="datePicker w21" name="predate_modify" id="predate_modify"
							readonly /> ~ <input type="text" class="datePicker w21"
							name="postdate_modify" id="postdate_modify" readonly /></td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('등록자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="creator" name="creator" multiple
									data-width="58%" style="width: 58%;">
								</select> <span class="pointer verticalMiddle"
									onclick="javascript:openUserPopup('creator', 'multi');"><img
									class="verticalMiddle"
									src="/Windchill/jsp/portal/images/search_icon2.png"></span> <span
									class="pointer verticalMiddle"
									onclick="javascript:deleteUser('creator');"><img
									class="verticalMiddle"
									src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('수정자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="modifier" name="modifier"
									multiple data-width="58%">
								</select> <span class="pointer verticalMiddle"
									onclick="javascript:openUserPopup('modifier', 'multi');"><img
									class="verticalMiddle"
									src="/Windchill/jsp/portal/images/search_icon2.png"></span> <span
									class="pointer verticalMiddle"
									onclick="javascript:deleteUser('modifier');"><img
									class="verticalMiddle"
									src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('품목 번호')}</th>
						<td><span id="autoPArtSearch"> <select
								class="searchRelatedObject" id="relatedPart" name="relatedPart"
								multiple data-param="part" data-width="58%"></select> <span
								class="pointer verticalMiddle"
								onclick="javascript:deleteUser('relatedPart');"><img
									class="verticalMiddle"
									src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</span></td>
						<th scope="col">${e3ps:getMessage('프로젝트 번호')}</th>
						<td><span id="project_codeAuto"> <select
								id="project_code" name="project_code" multiple
								data-param="project_code" data-width="58%"></select> <span
								class="pointer verticalMiddle"
								onclick="javascript:deleteUser('project_code');"><img
									class="verticalMiddle"
									src="/Windchill/jsp/portal/images/delete_icon.png" /></span>
						</span></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<!-- //pro_table -->
	<!-- button -->
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span> <img class=""
				src="/Windchill/jsp/portal/images/t_icon.png">
				${e3ps:getMessage('검색 결과')} (<span id="count">0</span>/<span
				id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid=""
				onclick="add_favorite();"
				src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0" />
<!-- 			<select id="rows" name="rows" style="width: 100px;" -->
<!-- 				onchange="javascript:changeLoadingRows(this)"> -->
<!-- 				<option value="20">20</option> -->
<!-- 				<option value="40">40</option> -->
<!-- 				<option value="60">60</option> -->
<!-- 				<option value="80">80</option> -->
<!-- 				<option value="100">100</option> -->
<!-- 			</select> -->
			<button type="button" class="i_read" style="width: 100px"
				onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="i_read" style="width: 100px"
				onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
			<img class="pointer mb5"
				onclick="excelDown('searchForm', 'excelDownDoc');"
				src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->

	<!-- ColumnSelectTest -->
	<!--  헤더 메뉴 구성하기 위한 메뉴 -->
	<ul id="headerMenu">
		<li id="h_item_1">오름 차순 정렬</li>
		<li id="h_item_2">내림 차순 정렬</li>
		<li id="h_item_3">정렬 초기화</li>
		<li></li>
		<li id="h_item_4">현재 칼럼 숨기기</li>
		<li id="h_item_5">칼럼 보이기/숨기기
			<ul id="h_item_ul">
			</ul>
		</li>
		<li></li>
		<li id="h_item_6">모든 칼럼 보이기</li>
	</ul>
	<!-- //ColumnSelectTest -->

	<div class="table_list">
		<div class="list" id="grid_wrap" style="height: 550px"></div>
	</div>
	<!-- //table list-->
</div>