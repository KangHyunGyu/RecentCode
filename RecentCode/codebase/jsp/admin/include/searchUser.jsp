<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>    
<script type="text/javascript">
$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			preRequestData();
		}
	});
	
	//grid setting
	createAUIGrid(columnLayout);
	
	//get grid data
	preRequestData();
	
	$(window).resize(function(){
		if (window.myGridID){
			AUIGrid.resize(window.myGridID);
		}
	});
});

//AUIGrid 생성 후 반환 ID
var myGridID;

//현재 페이지
var page = 1;

//row 로딩 개수
var rows = 20;

//grid 표준  로딩 할 개수.
var oneGridListCount = 30;

// 호출할 데이터 개수
var oneGridDataCount = 30;

// 재호출 될 남은 데이터 개수
var oneGridReRequestCount = 30;

// 중복 요청을 피하기 위한 플래그
var nowRequesting = false;

//마지막 페이지
var isLastPage = false;

var dutyList = ["대표이사", "부사장", "전무", "상무", "이사", "상무보", "수석연구원", "부장", "책임연구원", "차장", "선임연구원", "과장", "전임연구원", "대리", "연구원", "사원", "실장"];
//var dutyList = [];

//AUIGrid 칼럼 설정
var columnLayout = [
	{ dataField : "id",		headerText : "아이디",		width:"20%",	style:"pointer", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "name",		headerText : "이름",		width:"20%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "departmentName",		headerText : "부서",			width:"19%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "duty",			headerText : "직위",			width:"10%", style:"aui-grid-editable-cell",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "IconRenderer",
			iconWidth : 16, // icon 사이즈, 지정하지 않으면 rowHeight에 맞게 기본값 적용됨
			iconHeight : 16,
			iconPosition : "aisleRight",
			iconTableRef :  { // icon 값 참조할 테이블 레퍼런스
				"default" : "/Windchill/jsp/component/AUIGrid/images/arrow-down-black-icon.png" // default
			},
			onClick : function(event) {
				// 아이콘을 클릭하면 수정으로 진입함.
				AUIGrid.openInputer(event.pid);
			}
		},
		editRenderer : {
			type : "DropDownListRenderer",
			showEditorBtn : false,
			showEditorBtnOver : false, // 마우스 오버 시 에디터버턴 보이기
			list : dutyList
		}	
	},
	{ dataField : "email",		headerText : "이메일",		width:"20%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}
	},
	{ dataField : "chief",		headerText : "부서장",		width:"10%", style:"aui-grid-editable-cell",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "CheckBoxEditRenderer",
			editable : true, // 체크박스 편집 활성화 여부(기본값 : false)
			
			//사용자가 체크 상태를 변경하고자 할 때 변경을 허락할지 여부를 지정할 수 있는 함수 입니다.
			/* checkableFunction :  function(rowIndex, columnIndex, value, isChecked, item, dataField ) {
				// 행 아이템의 charge 가 Anna 라면 수정 불가로 지정. (기존 값 유지)
				if(item.charge == "Anna") {
					return false;
				}
				return true;
			} */
		}
	},
];

//AUIGrid 를 생성합니다.
function createAUIGrid(columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		editable : true,
			
		selectionMode : "singleRow",
		
		showSelectionBorder : false,
		
		noDataMessage : gridNoDataMessage,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight
	};

	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	// 스크롤 체인지 이벤트 바인딩
	AUIGrid.bind(myGridID, "vScrollChange", vScollChangeHandelr);
	
	//에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(myGridID, "cellEditEnd", auiCellEditingHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

//최초 데이터 요청
function preRequestData() {
	var param = new Object();
	
	var paramArray = $("#searchForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	page = 1;
	
	param["page"] = page;
	param["rows"] = rows;
	AUIGrid.showAjaxLoader(myGridID);
	var url = getURLString("/admin/searchUserAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		$("#total").html(gridData.length);
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(myGridID, gridData);

		AUIGrid.setAllCheckedRows(myGridID, false);
		AUIGrid.removeAjaxLoader(myGridID);
	});
}

//편집 핸들러
function auiCellEditingHandler(event) {
	var dataField = event.dataField;
	
	if(event.type == "cellEditEnd") {
		if(dataField == "duty") {

			var param = new Object();
			param["oid"] = event.item.oid;
			param["dutyName"] = event.value;

			var url = getURLString("/admin/setDutyAction");
			ajaxCallServer(url, param, function(data){
				isLastPage = false;
				preRequestData();
			});
			
		}else if(dataField == "chief") {

			var param = new Object();
			param["oid"] = event.item.oid;
			param["checked"] = event.value;
			
			var url = getURLString("/admin/setChiefAction");
			ajaxCallServer(url, param, function(data){
				isLastPage = false;
				preRequestData();
			});
		}
	}
};

//스크롤 체인지 핸들어에서 무리한 작업을 하면 그리드 퍼포먼스가 떨어집니다.
//따라서 무리한 DOM 검색은 자제하십시오.
function vScollChangeHandelr(event) {
	
	// 스크롤 위치가 마지막과 일치한다면 추가 데이터 요청함
	if(event.position == event.maxPosition) {
		if(!isLastPage) {
			requestAdditionalData();
		}
	}
}

//추가 데이터 요청 Ajax
function requestAdditionalData() {
	
	if(nowRequesting) {
		return;
	}
	page++;
	
	var param = new Object();
	
	var paramArray = $("#searchForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	param["page"] = page;
	param["rows"] = rows;
	
	nowRequesting = true;

	var url = getURLString("/admin/searchUserAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		if(gridData.length > 0) {
			var total = $("#total").html();
			
			total = parseInt(total) + gridData.length;
			
			$("#total").html(total);
			
			// 그리드에 데이터 세팅
			AUIGrid.appendData(myGridID, gridData);
			
		} else {
			isLastPage = true;
		}
		
		nowRequesting = false;
		
	});
}

//검색
function search(){
	isLastPage = false;
	preRequestData();
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

function setDepartment() {
	
	var departmentOid = $("#departmentOid").val();
	
	var url = getURLString("/admin/viewSetDepartmentPopup") + "?departmentOid=" + departmentOid;
	
	openPopup(url, "setDepartment");
}
</script>

<!-- pro_table -->
<div class="semi_table2">
<form name="searchForm" id="searchForm">
	<input type="hidden" name="departmentOid" id="departmentOid" value="" />
	<table summary="">
		<caption></caption>
		<colgroup>
			<col style="width:15%">
			<col style="width:35%">
			<col style="width:15%">
			<col style="width:35%">
		</colgroup>
		
		<tbody>
			<tr>
				<th scope="col">아이디</th>
				<td><input type="text" id="userId" name="userId" class="w100" /></td>
				<th scope="col">이름</th>
				<td><input type="text" id="userName" name="userName" class="w100" /></td>
			</tr>
		</tbody>
	</table>
</form>
</div>
<!-- //pro_table -->
<!-- table list-->
<div>
	<!-- button -->
	<div class="seach_arm pt15">
		<div class="leftbt">검색결과 (<span id="total">0</span> 개)</div>
		<div class="rightbt" style="margin-bottom:10px;">
			<button type="button" class="m_update" id="setDept" onclick="setDepartment();">부서설정</button>
			<button type="button" class="m_read" id="excelDown" onclick="xlsxExport();">엑셀 다운로드</button>
		</div>
	</div>
	<!-- //button -->
<!-- 	<div class="list" id="grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
	<div class="list" id="grid_wrap" style="height:500px; border-top:2px solid #74AF2A;"></div>
</div>
<!-- //table list-->