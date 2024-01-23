<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<div class="product">
	
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table -->
	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm">
			<table class="mainTable">
				<colgroup>
					<col style="width:15%">
					<col style="width:35%">
					<col style="width:15%">
					<col style="width:35%">
					
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('이슈제목')}</th>
						<td><input type="text" id="name" name="name" class="w100"></td>
						<th scope="col">상태</th>
						<td>
							<select name="state" id="state" class="w25">
								<option value="" checked>전체</option>
								<option value="PROGRESS">진행중</option>
								<option value="COMPLETED">완료됨</option>
							</select>
						</td>
					</tr>
				</tbody>
			</table>
		</form>	
	</div>
	<!-- //pro_table -->
	
	<!-- button -->
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="count">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="width:100%;height:600px"></div>
	</div>
	<!-- //table list-->
</div>


<script>
$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//grid setting
    createAUIGrid(columnLayout);

    //get grid data
    getGridData();
});


//현재 페이지를 구분하기 위한 상수값
var PROJECTSEARCHPAGE = true;

//AUIGrid 생성 후 반환 ID
var myGridID;

//현재 페이지
var page = 1;

//row 로딩 개수
var rows = 20;

//중복 요청을 피하기 위한 플래그
var nowRequesting = false;

//마지막 페이지 여부
var isLastPage = false;

//소트 값
var sortValue = "";

//소트 값으로 소팅되었는지 체크하는 값
var sortCheck = true;

//AUIGrid 칼럼 설정
var columnLayout = [
	{
		dataField: "number",
		headerText: "${e3ps:getMessage('프로젝트 번호')}",
		width: "8%",
		sortValue: "number",
		filter: {
			showIcon: true,
			iconWidth: 30,
		},
	},
	{
		dataField: "name",
		headerText: "${e3ps:getMessage('프로젝트 명')}",
		width: "*%",
		sortValue: "name",
		style: "AUIGrid_Left",
		filter: {
			showIcon: true,
			iconWidth: 30,
		},
		renderer: {
			type: "LinkRenderer",
			baseUrl: "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback: function (rowIndex, columnIndex, value, item) {
				var oid = item.pjtOid;
				openView(oid);
			},
		},
	},
	{
		dataField: "taskName",
		headerText: "${e3ps:getMessage('태스크 명')}",
		width: "15%",
		sortValue: "taskName",
		filter: {
			showIcon: true,
			iconWidth: 30,
		},
		renderer: {
			type: "LinkRenderer",
			baseUrl: "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback: function (rowIndex, columnIndex, value, item) {
				var oid = item.pjtOid;
				openView(oid);
			},
		},
	},
	{
		dataField: "title",
		headerText: "${e3ps:getMessage('이슈 제목')}",
		width: "15%",
		sortValue: "title",
		filter: {
			showIcon: true,
			iconWidth: 30,
		},
		renderer: {
			type: "LinkRenderer",
			baseUrl: "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback: function (rowIndex, columnIndex, value, item) {
				var oid = item.oid;
				openView(oid);
			},
		},
	},
	{
		dataField: "creatorFullName",
		headerText: "${e3ps:getMessage('제기자')}",
		width: "8%",
		filter: {
			showIcon: true,
			iconWidth: 30,
		},
	},
	{
		dataField: "createDate",
		headerText: "${e3ps:getMessage('제기일자')}",
		width: "8%",
		filter: {
			showIcon: true,
			iconWidth: 30,
		},
	},
	{
		dataField: "managerFullName",
		headerText: "${e3ps:getMessage('담당자')}",
		width: "8%",
		filter: {
			showIcon: true,
			iconWidth: 30,
		},
	},
	{
		dataField: "state",
		headerText: "${e3ps:getMessage('작업 현황')}",
		width: "8%",
		filter: {
			showIcon: true,
			iconWidth: 30,
		},
	}
];



//AUIGrid 를 생성합니다.
function createAUIGrid(columnLayout) {
//그리드 속성 설정
	var gridPros = {
		selectionMode: "multipleCells",
		showSelectionBorder: true,
		noDataMessage: gridNoDataMessage,
		rowIdField: "_$uid",
		showRowNumColumn: true,
		showEditedCellMarker: false,
		wrapSelectionMove: true,
		showRowCheckColumn: false,
		enableFilter: true,
		enableMovingColumn: true,
		headerHeight: gridHeaderHeight,
		rowHeight: gridRowHeight,
		// 고정칼럼 카운트 지정
		fixedColumnCount: 4,
	};

	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);

	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(myGridID, "cellClick", auiGridCellClickHandler);

	// 스크롤 체인지 이벤트 바인딩
	AUIGrid.bind(myGridID, "vScrollChange", vScollChangeHandelr);

	// 헤더 클릭 이벤트 바인딩
	//AUIGrid.bind(myGridID, "headerClick", auiGridHeaderClickHandler);

	AUIGrid.addRow(myGridID, new Object(), "last");
	var gridData = new Array();
	//AUIGrid.setGridData(myGridID, gridData);
}


function getGridData(){
	$("#searchForm").attr("action", getURLString("/project/issue/searchIssueAction"));

	var param = new Object();

	param["page"] = page;
	param["rows"] = rows;
	param["sortValue"] = sortValue;
	param["sortCheck"] = sortCheck;
	
	if (page == 1) {
		AUIGrid.showAjaxLoader(myGridID);
	}
	
	formSubmit("searchForm", param, null, function(data){
		// 그리드 데이터
		var gridData = data.list;
		var count = $("#count").html();
	      
		if (page == 1) {
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

		if (gridData.length == 0) {
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

function vScollChangeHandelr(event) {
	// 스크롤 위치가 마지막과 일치한다면 추가 데이터 요청함
	if (event.position == event.maxPosition) {
		if (!isLastPage) {
			if (!nowRequesting) {
				page++;
				getGridData();
			}
		}
	}
}

//검색조건 초기화
function reset(){
	var locationDisplay = $("#locationDisplay").val();
	$("#searchForm")[0].reset();
	$("#locationDisplay").val(locationDisplay);
}

//필터 초기화
function resetFilter() {
	AUIGrid.clearFilterAll(myGridID);
}

function xlsxExport() {
	AUIGrid.setProperty(
  	myGridID,
  	"exportURL",
  	getURLString("/common/xlsxExport")
	);

	// 엑셀 내보내기 속성
	var exportProps = {
		postToServer: true,
	};
	// 내보내기 실행
	AUIGrid.exportToXlsx(myGridID, exportProps);
}

//검색
function search(){
	isLastPage = false;
	page = 1;
	sortValue = "";
	$("#sessionId").val("");
	getGridData();
}
</script>
