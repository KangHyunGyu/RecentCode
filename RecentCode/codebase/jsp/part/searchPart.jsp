<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>

$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	createAUIGrid(columnLayout);
	//lifecycle list
	getLifecycleList("LC_Default");
	
	//NumberCode list
	getNumberCodeList("material", "MATERIAL", false, true); // 재질
	getNumberCodeList("finish", "FINISH", false, true); // FINISH
	getNumberCodeList("special_attribute", "SPECIALATTR", false, true); // 특별특성
	getNumberCodeList("certification_regulations", "CERTIFICATION", false, true); // 인증 및 법규
	getNumberCodeList("carry_over", "CARRYOVER", false, true); // CARRY OVER
	
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

//소트 값
var sortValue = "";

//소트 값으로 소팅되었는지 체크하는 값
var sortCheck = true;

//AUIGrid 칼럼 설정
var columnLayout = [
	
	{ dataField : "icon",		headerText : "",		width:"30",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
	},
	
	{ dataField : "number",		headerText : "${e3ps:getMessage('폼목 번호')}",		width:"9%",		style:"AUIGrid_Left",			sortValue : "master>number",
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
	{ dataField : "name",		headerText : "${e3ps:getMessage('품목 명')}",	style:"AUIGrid_Left",		width:"*",	sortValue : "master>name",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "w_isCostName",		headerText : "${e3ps:getMessage('원가계정')}",			width:"5%", 
		// column 숨겨봄
		visible: false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "version",		headerText : "${e3ps:getMessage('버전')}",			width:"6%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",		headerText : "${e3ps:getMessage('상태')}",			width:"5%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "checkoutStateName",		headerText : "${e3ps:getMessage('체크인')}",		width:"5%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ 
		dataField : "lastApprover",		
		headerText : "${e3ps:getMessage('최종 승인자')}",		
		width:"5%",
		filter : {
			showIcon : true,
			iconWidth:20
		}
	},
	{ dataField : "creatorFullName",		headerText : "${e3ps:getMessage('등록자')}",			width:"5%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "createDateFormat",		headerText : "${e3ps:getMessage('등록일')}",			width:"5%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "modifierFullName",		headerText : "${e3ps:getMessage('수정자')}",			width:"5%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "modifyDateFormat",		headerText : "${e3ps:getMessage('수정일')}",			width:"5%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	
	{ dataField : "epm3DNumber",		headerText : "${e3ps:getMessage('3D')}",			width:"6%", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.epm3DOid;
				
				openView(oid);
			}
		}
	},
	{ dataField : "epm2DNumber",		headerText : "${e3ps:getMessage('2D')}",			width:"6%", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.epm2DOid;
				
				openView(oid);
			}
		}
	},
	{ dataField : "bom",		headerText : "BOM",		width:"50",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			
			var icon = "/Windchill/jsp/portal/images/bom_icon.png";
			
			var template = "<img title='${e3ps:getMessage('BOM 보기')}' class='pointer' onclick='openBomTree(\"" + item.oid + "\")' src='" + icon + "'/>";
			
			return template;
		}
	},
	
	
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
		
		//fixedColumnCount : 6,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight
		
		
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

	$("#searchForm").attr("action", getURLString("/part/searchPartScrollAction"));
	
	var param = new Object();
	
	param["page"] = page;
	param["rows"] = rows;
	param["rows"] = rows;
	param["sortValue"] = sortValue;
	param["sortCheck"] = sortCheck;
	
	
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
	sortValue = "";
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

function changeLoadingRows(selected){
	rows = parseInt($(selected).val());
	isLastPage = false;
	page = 1;
	sortValue = "";
	$("#sessionId").val("");
	getGridData();
}

</script>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
			<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		</div>
		<div class="rightbt">
<%-- 			<button type="button" class="s_bt03" id="switchDetailBtn" onclick="switchDetailBtn();">${e3ps:getMessage('상세검색')}</button> --%>
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table -->
	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<table class="mainTable">
				<colgroup>
					<col style="width:10%">
					<col style="width:23%">
					<col style="width:10%">
					<col style="width:23%">
					<col style="width:10%">
					<col style="width:24%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('부품 번호')}</th>
						<td>
							<input type="text" id="number" name="number" class="w40">
							~
							<input type="text" id="numberEnd" name="numberEnd" class="w40">
						</td>
						<th scope="col">${e3ps:getMessage('부품 명')}</th>
						<td><input type="text" id="name" name="name" class="w100"></td>
						<th scope="col">${e3ps:getMessage('부품 분류')}</th>
						<td><input type="text" id="locationDisplay" name="locationDisplay" class="w100" disabled></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('작성자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="creator" name="creator" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('작성일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('버전')}</th>
						<td>
							<input type="radio" name="version" value="new" checked>
							<label>${e3ps:getMessage('최신 버전')}</label>
							<input type="radio" name="version" value="all">
							<label>${e3ps:getMessage('모든 버전')}</label>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('최종 수정자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="modifier" name="modifier" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('modifier', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('modifier');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('최종 수정일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" name="predate_modify" id="predate_modify" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate_modify" id="postdate_modify" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('상태')}</th>
						<td>
							<select class="multiSelect w20" id="state" name="state" multiple style="height:20px;overflow-y: hidden;">
							</select>
						</td>						
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('관련 프로젝트')}</th>
						<td>
						<div class="pro_view">
							<select class="searchRelatedProject" id="relatedProject" name="relatedProject" multiple></select>
						</div>
						</td>
						<th></th>
						<td></td>
						<th></th>
						<td></td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('재질')}</th>
						<td>
							<select id="material" name="material" class="w100"></select>
						</td>
						<th>${e3ps:getMessage('중량')}</th>
						<td>
							<input type="text" id="weight" name="weight" class="w95">
						</td>
						<th>UPG</th>
						<td>
							<input type="text" id="upg" name="upg" class="w95">
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('고객품번')}</th>
						<td>
							<input type="text" id="customer_part_number" name="customer_part_number" class="w95">
						</td>
						<th>${e3ps:getMessage('단가')}</th>
						<td>
							<input type="text" id="unit_cost" name="unit_cost" class="w95">
						</td>
						<th>FINISH</th>
						<td>
							<select id="finish" name="finish" class="w100"></select>
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('특별특성')}</th>
						<td>
							<select id="special_attribute" name="special_attribute" class="w100"></select>
						</td>
						<th>${e3ps:getMessage('인증 및 법규')}</th>
						<td>
							<select id="certification_regulations" name="certification_regulations" class="w100"></select>
						</td>
						<th>C/O</th>
						<td>
							<select id="carry_over" name="carry_over" class="w100"></select>
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('허용압력')}</th>
						<td>
							<input type="text" class="w95" id="allowable_pressure" name="allowable_pressure">
						</td>
						<th>${e3ps:getMessage('가스종류')}</th>
						<td>
							<input type="text" class="w95" id="gas_type" name="gas_type">
						</td>
						<th></th>
						<td></td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('공법')}</th>
						<td>
							<input type="text" class="w95" id="method" name="method">
						</td>
						<th>${e3ps:getMessage('규격 및 특징')}</th>
						<td>
							<input type="text" class="w95" id="specification" name="specification">
						</td>
						<th>${e3ps:getMessage('비고')}</th>
						<td>
							<input type="text" class="w95" id="description" name="description">
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('시작업체 명')}</th>
						<td>
							<input type="text" class="w95" id="start_company_name" name="start_company_name">
						</td>
						<th>${e3ps:getMessage('시작업체 담당자')}</th>
						<td>
							<input type="text" class="w95" id="start_company_manager" name="start_company_manager">
						</td>
						<th>${e3ps:getMessage('시작업체 연락처')}</th>
						<td>
							<input type="text" class="w95" id="start_company_tell" name="start_company_tell">
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('양산업체 명')}</th>
						<td>
							<input type="text" class="w95" id="prod_company_name" name="prod_company_name">
						</td>
						<th>${e3ps:getMessage('양산업체 담당자')}</th>
						<td>
							<input type="text" class="w95" id="prod_company_manager" name="prod_company_manager">
						</td>
						<th>${e3ps:getMessage('양산업체 연락처')}</th>
						<td>
							<input type="text" class="w95" id="prod_company_tell" name="prod_company_tell">
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('시작 단가')}</th>
						<td>
							<input type="text" class="w95" id="start_unit_cost" name="start_unit_cost">
						</td>
						<th>${e3ps:getMessage('양산 단가')}</th>
						<td>
							<input type="text" class="w95" id="prod_unit_cost" name="prod_unit_cost">
						</td>
						<th></th>
						<td></td>
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
				${e3ps:getMessage('검색 결과')} (<span id="count">0</span> /
        <span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
<%-- 			<button type="button" class="s_bt03" onclick="excelDown('searchForm', 'excelDownPart');">${e3ps:getMessage('엑셀 다운로드')}</button> --%>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
			<!-- <img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownPart');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0"> -->
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap"  style="height:500px"></div>
	</div>
	<!-- //table list-->
</div>