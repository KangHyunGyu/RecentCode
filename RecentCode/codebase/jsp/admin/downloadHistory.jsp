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
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<table class="mainTable">
				<colgroup>
					<col style="width:15%">
					<col style="width:35%">
					<col style="width:15%">
					<col style="width:35%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">모듈</th>
						<td>
							<select class="multiSelect w20" id="module" name="module" multiple style="height:20px;overflow-y: hidden;">
							</select>
						</td>
						<th scope="col">ID</th>
						<td ><input type="text" id="userId" name="userId" class="w100"></td>
					</tr>	
					<tr>
						<th scope="col">${e3ps:getMessage('이름')}</th>
						<td ><input type="text" id="name" name="name" class="w100"></td>
						<th scope="col">${e3ps:getMessage('다운로드 일자')}</th>
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
<script>
$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//모듈 종류select option 가져오기
	getModuleTypeList();
	
	createAUIGrid(columnLayout);
	
	getGridData();
});

var myGridID;
var columnLayout = [
	{ dataField : "module", headerText : "${e3ps:getMessage('모듈')}", width:"*",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "number", headerText : "${e3ps:getMessage('번호')}", width:"15%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "title", headerText : "${e3ps:getMessage('제목')}", width:"15%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "name", headerText : "${e3ps:getMessage('유저 이름')}", width:"15%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "userId", headerText : "ID", width:"8%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "fName", headerText : "${e3ps:getMessage('파일 명')}", width:"15%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "downloadDate", headerText : "${e3ps:getMessage('다운로드 시간')}", width:"8%",
		filter : {	showIcon : true, iconWidth:30		}	
	},
	{ dataField : "dCount", headerText : "${e3ps:getMessage('다운로드 횟수')}", width:"4%",
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
	
	params.module = $("#module").val();
	params.userId = $("#userId").val();
	params.name = $("#name").val();
	params.predate = $("#predate").val();
	params.postdate = $("#postdate").val();
	params["start"] = 0;
	params["count"] = 999;
	
	
	AUIGrid.showAjaxLoader(myGridID);
	var url = getURLString("/admin/searchDownloadHistoryScrollAction2");
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
</script>