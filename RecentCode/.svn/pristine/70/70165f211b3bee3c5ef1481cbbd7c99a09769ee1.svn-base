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
					<col style="width:10%">
					<col style="width:23%">
					<col style="width:10%">
					<col style="width:23%">
					<col style="width:10%">
					<col style="width:24%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">ID</th>
						<td ><input type="text" id="userId" name="userId" class="w100"></td>
						<th scope="col">${e3ps:getMessage('이름')}</th>
						<td ><input type="text" id="name" name="name" class="w100"></td>
						<th scope="col">${e3ps:getMessage('접속일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
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
				${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
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
<script>
$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	createAUIGrid(columnLayout);
	getGridData();
});


var myGridID;
var columnLayout = [
	{ dataField : "userId", headerText : "ID", width:"20%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "name", headerText : "${e3ps:getMessage('유저 이름')}", width:"20%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "conTime", headerText : "${e3ps:getMessage('접속일')}", width:"20%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "ip", headerText : "${e3ps:getMessage('server')}", width:"20%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "browser", headerText : "${e3ps:getMessage('브라우저')}", width:"20%",
		filter : {	showIcon : true, iconWidth:30		}	
	}
];

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
		enableMovingColumn : true
		
	};

	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

function getGridData(){
	var params = new Object();
	
	params.userId = $("#userId").val();
	params.name = $("#name").val();
	params.predate = $("#predate").val();
	params.postdate = $("#postdate").val();
	params["start"] = 0;
	params["count"] = 999;
	
	AUIGrid.showAjaxLoader(myGridID);
	var url = getURLString("/admin/searchLoginHistoryScrollAction2");
	ajaxCallServer(url, params, function(data){
		var gridData = data.list;
		
		$("#total").html(data.total_count);
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(myGridID, gridData);

		AUIGrid.setAllCheckedRows(myGridID, false);
		AUIGrid.removeAjaxLoader(myGridID);
	});
	
}

//검색
function search(){
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
</script>