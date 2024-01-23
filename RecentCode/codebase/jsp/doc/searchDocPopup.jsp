<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>

.SumoSelect {
    width: 58%;
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
	
	//팝업 리사이즈
	popupResize();
	
	//getDocAttributes();
	
	//getDocumentTypes();
	
	//lifecycle list
	getLifecycleList("LC_Default");
	
	//승인됨 문서 초기 선택
	//$("#state").val("APPROVED");
	
	//grid setting
	createAUIGrid(columnLayout);
	
	//get grid data(트리 존재시 트리에서 로딩)
	getGridData();
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

//AUIGrid 칼럼 설정
//AUIGrid 칼럼 설정
var columnLayout = [
	{ 
		dataField : "location", 
		headerText : "${e3ps:getMessage('문서 분류')}", 
		width:"10%", 
		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:20,
		}},
	{ 
		dataField : "docAttributeName", 
		headerText : "${e3ps:getMessage('문서 속성')}", 
		width:"10%",	
		style:"AUIGrid_Left", 
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "number", 
		headerText : "${e3ps:getMessage('문서 번호')}",	 
		width:"15%", 
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
		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
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
	/*
	{ 
		dataField : "modifierFullName",
		headerText : "${e3ps:getMessage('수정자')}",
		width:"6%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	{ 
		dataField : "modifyDateFormat",
		headerText : "${e3ps:getMessage('수정일')}",
		width:"6%",
		filter : {
			showIcon : true,
			iconWidth:20
		}},
	*/
	
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
		
		//independentAllCheckBox : true,
		
		rowCheckableFunction : function(rowIndex, isChecked, item) {
			if(!item.select) {
				return false;
			}
			return true;
		},
		
		rowCheckDisabledFunction : function(rowIndex, isChecked, item) {
			if(!item.select) {
				return false;
			}
			return true;
		}
		
	};

	if("${type}" == "single") {
		gridPros["rowCheckToRadio"] = true;	
	}

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
	
	//if("${moduleType}".length > 0) {
	//	param["moduleType"] = "${moduleType}";
	//}
	
	
	if(page == 1) {
		AUIGrid.showAjaxLoader(myGridID);
	}
	
	formSubmit("searchForm", param, null, function(data){

		// 그리드 데이터
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
	$("#sessionId").val("");
	getGridData();
}

//검색조건 초기화
function reset(){
	var locationDisplay = $("#locationDisplay").val();
	$("#searchForm")[0].reset();
	$("#locationDisplay").val(locationDisplay);
	
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

function addDoc(){
	var selectedItem = AUIGrid.getCheckedRowItems(myGridID);
	if(selectedItem == ''){
		openNotice("${e3ps:getMessage('산출물을 선택하세요.')}");
		return false;
	}
	selectedItem = selectedItem.map(function(item){
		delete item.check;
		return item;
	});
	
	if("pDocument" == "${pageName}"){
		if(opener.window.add_${pageName}_addObjectList){	//addObject에서 그리드 리스트 세팅
			opener.window.add_${pageName}_addObjectList("${outputOid}", selectedItem);
		}	
	}else{
		if(opener.window.add_${pageName}_addObjectList){	//addObject에서 그리드 리스트 세팅
			opener.window.add_${pageName}_addObjectList(selectedItem.map(x=>x.item));
		}
	}
	
	
	if("${type}" == "single") {
		window.close();
	}
}



//function addECARelatedDoc_createDocLinkAll(list) {
//	
//	var param = new Object();
//	
//	param["list"] = list;
//	param["oid"] = opener.window.addECARelatedDoc_getOid();  //run Object Oid(ECA)
//	
//	var url = getURLString("/task/createDocLinkAll");
//	ajaxCallServer(url, param, function(data){
//		opener.window.addECARelatedDoc_getGridData();
//		
//		window.close();
//	});
//	
//};
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('문서 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl30 pr30">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt">
				<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png"><b>&nbsp;${e3ps:getMessage('검색 조건')}</b>
			</div>
			<div class="rightbt">
				<button type="button" class="s_bt03" id="switchDetailBtn" onclick="switchDetailBtn();">${e3ps:getMessage('상세검색')}</button>
				<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
				<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
				<button type="button" class="s_bt03" onclick="javascript:addDoc()">${e3ps:getMessage('추가')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="searchForm" id="searchForm">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<input type="hidden" id="mode" name="mode" value="search">
			<input type="hidden" id="cFolderOid" name="cFolderOid" value="">
			<input type="hidden" id="cFolderPath" name="cFolderPath" value="">
			<div class="pro_table">
				<table class="mainTable">
				<colgroup>
					<col style="width:15%">
					<col style="width:35%">
					<col style="width:15%">
					<col style="width:35%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('문서 분류')}</th>
						<td>
						<div style="position:relative">
							<input type="text" id="locationDisplay" name="locationDisplay" class="w58" disabled>
							<button type="button" class="s_bt03" id="toggleBtn" onclick="toggleFolderList(this)">${e3ps:getMessage('목록')}</button>
							<div id="folderList" style="display:none;position:absolute;z-index:999">
								<!-- tree -->
								<jsp:include page="${e3ps:getIncludeURLString('/common/include_folderTree')}" flush="true">
									<jsp:param name="container" value="intellian"/>
									<jsp:param name="renderTo" value="docFolder"/>
									<jsp:param name="formId" value="searchForm"/>
									<jsp:param name="rootLocation" value="/Default/Document"/>
									<jsp:param name="autoGridHeight" value="true"/>
								</jsp:include>
								<!-- //tree -->
							</div>
						</div>
						</td>
						<th scope="col">${e3ps:getMessage('버전')}</th>
						<td>
							<input type="radio" id="version" name="version" value="new" checked>
							<label>${e3ps:getMessage('최신 버전')}</label>
							<!-- 
							<span style="width: 10%; display: inline-block;"></span>
							<input type="radio" id="version" name="version" value="all">
							<label>${e3ps:getMessage('모든 버전')}</label>
							 -->
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('문서 명')}</th>
						<td><input type="text" id="name" name="name" class="w58"></td>
						<th scope="col">${e3ps:getMessage('상태')}</th>
						<td>
							<select class="multiSelect w10" id="state" name="state" multiple style="height:20px;overflow-y: hidden;">
							</select>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('문서 유형')}</th>
						<td>
							<select class="multiSelect w10" id="docAttribute" name="docAttribute" multiple style="height:20px;overflow-y: hidden;">
							</select>
						</td>
						<th scope="col">${e3ps:getMessage('문서 번호')}</th>
						<td>
							<select class="searchRelatedObject" id="relatedDoc" name="relatedDoc" multiple data-param="doc" data-width="58%"></select>
							<span class="pointer verticalMiddle" onclick="javascript:deleteUser('relatedDoc');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</td>
					</tr>	
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('등록일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w21" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w21" name="postdate" id="postdate" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('수정일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w21" name="predate_modify" id="predate_modify" readonly/>
							~
							<input type="text" class="datePicker w21" name="postdate_modify" id="postdate_modify" readonly/>
						</td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('등록자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="creator" name="creator" multiple data-width="58%" style="width: 58%;">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('수정자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="modifier" name="modifier" multiple data-width="58%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('modifier', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('modifier');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<!-- 속성 include 화면 -->
			<%-- <div id="attributeList">
				<jsp:include page="${e3ps:getIncludeURLString('/doc/include_docAttribute')}" flush="true">
				    <jsp:param name="foid" value=""/>
				    <jsp:param name="mode" value="search"/>
			    </jsp:include>
			</div> --%>
			</div>
		</form>
	</div>
	<!-- button -->
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="count">0</span>/<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:500px"></div>
	</div>
	<!-- //table list-->
</div>		
<!-- //pop-->