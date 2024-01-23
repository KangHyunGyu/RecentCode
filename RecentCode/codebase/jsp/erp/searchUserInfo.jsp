<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript" src="/Windchill/jsp/js/distribute.js"></script>
<script>
$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});

	// 날짜 자동입력
	setDate("precdate");
	
	//grid setting
	createAUIGrid(columnLayout);
	
	//get grid data(트리 존재시 트리에서 로딩)
	getGridData();
	
	//ERP 상태 가져오기
	getErpStateList();
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
var columnLayout = [
	{ dataField : "id",		headerText : "${e3ps:getMessage('아이디')}",		width:"8%", 
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "name",		headerText : "${e3ps:getMessage('이름')}",		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "dept_code",		headerText : "${e3ps:getMessage('부서코드')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "grade_code",		headerText : "${e3ps:getMessage('직위코드')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "grade_name",		headerText : "${e3ps:getMessage('직위 명')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "duty_code",		headerText : "${e3ps:getMessage('직책코드')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "duty_name",		headerText : "${e3ps:getMessage('직책 명')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "home_tel",		headerText : "${e3ps:getMessage('전화번호')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "cell_tel",		headerText : "${e3ps:getMessage('휴대 전화번호')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "email",		headerText : "${e3ps:getMessage('이메일')}",		width:"13%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "address",		headerText : "${e3ps:getMessage('주소')}",		width:"25%", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "national_code",		headerText : "${e3ps:getMessage('국가')}",		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "zip_no",		headerText : "${e3ps:getMessage('우편번호')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "enter_date",		headerText : "${e3ps:getMessage('입사일')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "retire_date",		headerText : "${e3ps:getMessage('퇴사일')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "if_cdateStr",		headerText : "${e3ps:getMessage('I/F 생성일')}",		width:"13%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "if_rdateStr",		headerText : "${e3ps:getMessage('I/F 수령일')}",		width:"13%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "if_statusStr",		headerText : "${e3ps:getMessage('I/F 상태')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
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
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight,
		
		fixedColumnCount : 3		
	};

	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(myGridID, "cellClick", auiGridCellClickHandler);
	
	// 스크롤 체인지 이벤트 바인딩
	AUIGrid.bind(myGridID, "vScrollChange", vScollChangeHandelr);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
	
	// 그리드 ready 이벤트 바인딩
	AUIGrid.bind(myGridID, "ready", auiGridCompleteHandler);
}

function auiGridCompleteHandler(){
	// 수평 스크롤 이동 시킴 칼럼 인덱스
	AUIGrid.setHScrollPosition(myGridID, 13); 
}
function getGridData(){

	$("#searchForm").attr("action", getURLString("/erp/searchUserInfoScrollAction"));
	
	var param = new Object();
	
	param["page"] = page;
	param["rows"] = rows;
	
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
		if(data.totalSize != "0"){
			$("#total").html(data.totalSize);
		}
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
					<col style="width:20%">
					<col style="width:30%">
					<col style="width:20%">
					<col style="width:30%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('아이디')}</th>
						<td><input type="text" id="id" name="id" class="w100"></td>
						<th scope="col">${e3ps:getMessage('이름')}</th>
						<td><input type="text" id="name" name="name" class="w100"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('부서 코드')}</th>
						<td><input type="text" id="dept_code" name="dept_code" class="w100"></td>
						<th scope="col">${e3ps:getMessage('I/F 상태')}</th>
						<td><select class="w30" id="if_status" name="if_status"></select></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('I/F 생성일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="precdate" id="precdate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postcdate" id="postcdate" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('I/F 수령일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="prerdate" id="prerdate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postrdate" id="postrdate" readonly/>
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
				${e3ps:getMessage('검색 결과')} (<span id="count">0</span>/<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<select id="rows" name="rows" style="width:100px;" onchange="javascript:changeLoadingRows(this)">
				<option value="20">20</option>
				<option value="40">40</option>
				<option value="60">60</option>
				<option value="80">80</option>
				<option value="100">100</option>
			</select>
			<c:if test="${e3ps:isSyncBtn()}">
				<button type="button" class="s_bt03" onclick="sync('syncERPPLMUser');">${e3ps:getMessage('동기화')}</button>
			</c:if>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
			<img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownERPUser');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:500px"></div>
	</div>
	<!-- //table list-->
</div>