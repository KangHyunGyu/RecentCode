<%@page import="com.e3ps.change.service.ChangeHelper"%>
<%@page import="java.util.List"%>
<%@page import="com.e3ps.project.key.ProjectKey.STATEKEY"%>
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
	
	getLifecycleList("LC_Default");
	
	//grid setting
	createAUIGrid(columnLayout);
	
	document.querySelector("input[name=preSpecificDate][type=text]").disabled=true;
	document.querySelector("input[name=postSpecificDate][type=text]").disabled=true;
	
	document.querySelectorAll("input[name=applyDate][type=radio]").forEach((ele)=>{
		ele.addEventListener('click',(evt) => {
			let target = evt.target;
			if(target.value == 'specificDate'){
				document.querySelector("input[name=preSpecificDate][type=text]").disabled=false;
				document.querySelector("input[name=postSpecificDate][type=text]").disabled=false;
			}else{
				document.querySelector("input[name=preSpecificDate][type=text]").disabled=true;
				document.querySelector("input[name=postSpecificDate][type=text]").disabled=true;
			}
		})
	})
	
	//get grid data
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
	
	{ dataField : "ecrNumber",		headerText : "${e3ps:getMessage('ECR 번호')}",		width:"8%",			sortValue : "eoNumber",
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
	
	{ dataField : "name",		headerText : "${e3ps:getMessage('ECR 제목')}",		width:"*",		style:"AUIGrid_Left",	sortValue : "eoName",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "applyDateName",			headerText : "${e3ps:getMessage('적용요구시점')}",			width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "state",			headerText : "${e3ps:getMessage('상태')}",			width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ 
		dataField : "lastApprover",		
		headerText : "${e3ps:getMessage('최종 승인자')}",		
		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:20
		}
	},
	{ dataField : "createDate",			headerText : "${e3ps:getMessage('등록일')}",			width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "updateDate",			headerText : "${e3ps:getMessage('수정일')}",			width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "creator",			headerText : "${e3ps:getMessage('등록자')}",			width:"7%",
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

	$("#searchForm").attr("action", getURLString("/change/searchECRAction"));
	
	var param = new Object();
	
	param["page"] = page;
	param["rows"] = rows;
	param["rows"] = rows;
	param["sortValue"] = sortValue;
	param["sortCheck"] = sortCheck;
	param["container"] = $("#container option:selected").val();
	
	
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
	
	let applyPreDateCalendar = document.querySelector("input[name=preSpecificDate][type=text]");
	let applyPostDateCalendar = document.querySelector("input[name=postSpecificDate][type=text]");
	
	document.getElementsByName('applyDate').forEach(function(radio) {
        if (radio.checked && radio.value === 'specificDate') {
        	if((!applyPreDateCalendar.disabled && applyPreDateCalendar.value == 0)
    				&& (!applyPostDateCalendar.disabled && applyPostDateCalendar.value == 0)){
    			alert('적용 시점 특정 일자를 선택해 주세요');
    			radio.checked = false;
    			document.querySelector("input[name=preSpecificDate][type=text]").disabled=true;
				document.querySelector("input[name=postSpecificDate][type=text]").disabled=true;
    			return;
    		}
        }
    });
	
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

// function xlsxExport() {
// 	AUIGrid.setProperty(myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
// 	 // 엑셀 내보내기 속성
// 	   // 엑셀 내보내기 속성
// 	  var exportProps = {
// 			 postToServer : true,
			
// 			 beforeRequestCallback : function() { // 엑셀 내보내기 전 콜백

// 				AUIGrid.hideColumnByDataField(myGridID, "icon");
// 				AUIGrid.showColumnByDataField(myGridID, ["divisionName","checkoutStateName","prodhier1Name"
// 					,"prodhier2Name","prodhier3Name","material","spec","surface","netWeight","weightUnit"
// 					,"spareGroupName","spareTypeName","tschecked","basicData"]);
// 		     }, 
		     
// 		     exceptColumnFields : ["icon","bom"] ,
		     
// 		     afterRequestCallback : function() { // 엑셀 내보내기 후 콜백
// 		    	 AUIGrid.hideColumnByDataField(myGridID, [
// 		    		 "divisionName","checkoutStateName","prodhier1Name","prodhier2Name"
// 		    		 ,"prodhier3Name","material","spec","surface","netWeight","weightUnit"]);
// 		    	 AUIGrid.showColumnByDataField(myGridID, ["icon","spareGroupName","spareTypeName"
// 		    		 ,"tschecked","basicData"]);
		    	 
// 		     },
// 		     //현재 그리드의 히든 처리된 칼럼의 dataField 들 얻어 똑같이 동기화 시키기
		     
			
// 	  };
// 	  // 내보내기 실행
// 	  AUIGrid.exportToXlsx(myGridID, exportProps);
// }

function xlsxExport() {
	AUIGrid.setProperty(myGridID, "exportURL", getURLString("/common/xlsxExport"));
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
			 exportWithStyle : true,
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
						<th scope="col">${e3ps:getMessage('ECR 번호')}</th>
						<td><input type="text" id="requestNumber" name="requestNumber" class="w100"></td>
						<th scope="col">${e3ps:getMessage('ECR 제목')}</th>
						<td><input type="text" id="name" name="name" class="w100"></td>
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
						<th scope="col">${e3ps:getMessage('상태')}</th>
						<td>
							<select class="multiSelect w10" id="state" name="state" multiple style="height: 20px; overflow-y: hidden;">
							</select>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('등록일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('수정일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="modifyPredate" id="modifyPredate" readonly/>
							~
							<input type="text" class="datePicker w25" name="modifyPostdate" id="modifyPostdate" readonly/>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('적용요구시점')}</th>
						<td colspan="3" class="calendar">
							<label><input type="radio" name="applyDate" value="outOfStock" /> ${e3ps:getMessage('재고소진 후')}</label>
							<label><input type="radio" name="applyDate" value="immediately" /> ${e3ps:getMessage('즉시')}</label>
							<label><input type="radio" name="applyDate" value="specificDate" /> ${e3ps:getMessage('특정일시')}</label> (
							<input type="text" class="datePicker w13" name="preSpecificDate" id="preSpecificDate" readonly/>
							~
							<input type="text" class="datePicker w13" name="postSpecificDate" id="postSpecificDate" readonly/> 	
							)
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
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
<%-- 			<button type="button" class="s_bt03" onclick="excelDown('searchForm', 'excelDownECR');">${e3ps:getMessage('엑셀 다운로드')}</button> --%>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="width:100%;height:500px"></div>
	</div>
	<!-- //table list-->
</div>
