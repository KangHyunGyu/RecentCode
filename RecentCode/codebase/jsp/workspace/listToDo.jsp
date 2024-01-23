<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//grid setting
	createAUIGrid(columnLayout);
	
	// 메인 개인 지연 업무의 더보기 클릭 시 검색조건 나의 지연업무 체크
	if("${isDelay}" == "true"){
		$("#isDelay").prop('checked', true);
	}
	
	//get grid data
	getGridData();	
	selectField();
	
});

//AUIGrid 생성 후 반환 ID
var myGridID;

//AUIGrid 칼럼 설정
var columnLayout = [
	{ dataField : "objectTypeName",		headerText : "${e3ps:getMessage('구분')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}
	},
	{ dataField : "roleName",		headerText : "${e3ps:getMessage('활동')}",		width:"10%", style:"editAble_Blue_Cell",
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
				
				var url = getURLString("/workspace/detailApproval") + "?type=${type}&oid=" + oid;
				
				location.href = url;
			}
		}
	},
	{ dataField : "title",		headerText : "${e3ps:getMessage('제목')}",		width:"*", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.objectOid;
				
				openView(oid);
			}
		}
	},
	{ dataField : "createFullName",		headerText : "${e3ps:getMessage('등록자')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "createDateFormat",		headerText : "${e3ps:getMessage('등록일')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",			width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "ownerFullName",		headerText : "${e3ps:getMessage('결재자')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "startDateFormat",		headerText : "${e3ps:getMessage('수신일')}",		width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

function selectField(){
	$('#selectField').empty();
	$("#selectField").append("<option value=''>선택</option>");
	 for(var idx=0;idx<columnLayout.length;idx++){
		 //console.log("####"+columnLayout[idx].dataField+"\t"+columnLayout[idx].headerText);
		 $("#selectField").append("<option value='"+columnLayout[idx].dataField+"'>"+columnLayout[idx].headerText+"</option>");
	 }
}
 
function openBtnClick() {
	
	var dataField = document.getElementById("selectField").value;
	AUIGrid.openFilterLayer(myGridID, dataField);
    // 이름 칼럼의 필터 레이어(필터 메뉴)를 오픈합니다.
    // AUIGrid.openFilterLayer(myGridID, "name");
};

function closeBtnClick() {
	// 필터 레이어(필터 메뉴)가 열린 경우 닫습니다.
    AUIGrid.closeFilterLayer(myGridID);
};

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
	
	//에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(myGridID, "cellEditEnd", auiCellEditingHandler);
	
	//에디팅 진입 이벤트 바인딩
	AUIGrid.bind(myGridID, "cellEditBegin", auiCellEditingHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

function getGridData(){

	var param = new Object();

	$("#searchForm").attr("action", getURLString("/workspace/listToDoAction"));
	
	
	AUIGrid.showAjaxLoader(myGridID);
	
	formSubmit("searchForm", param, null, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		console.log(gridData);
		
		
		AUIGrid.setGridData(myGridID, gridData);	
		var count = gridData.length;
		
		$("#total").html(count);
		
		AUIGrid.removeAjaxLoader(myGridID);
		
	});
}

//셀 클릭 핸들러
function auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}

function auiCellEditingHandler(event) {
	var dataField = event.dataField;
	
	if(event.type == "cellEditEnd") {
	
	} else if(event.type == "cellEditBegin"){
		
	}
};

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
						<th scope="col">${e3ps:getMessage('제목')}</th>
						<td colspan="3"><input type="text" id="title" name="title" class="w100"></td>
<%-- 						<th scope="col">${e3ps:getMessage('등록자')} </th> --%>
<!-- 						<td > -->
<!-- 							<div class="pro_view"> -->
<!-- 								<select class="searchUser" id="creator" name="creator" multiple data-width="60%"> -->
<!-- 								</select> -->
<!-- 								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span> -->
<!-- 								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span> -->
<!-- 							</div> -->
<!-- 						</td>	 -->
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('등록자')} </th>
						<td >
							<div class="pro_view">
								<select class="searchUser" id="creator" name="creator" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						
						<th scope="col">${e3ps:getMessage('등록일')}</th>
						<td class="calendar" >
							<input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
					</tr>
					<c:if test= "${e3ps:isAdmin()}">
					<tr>
						<th scope="col">${e3ps:getMessage('결재자')} </th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="approver" name="approver" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('approver', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('approver');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('수신일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" name="predate_startDate" id="predate_startDate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate_startDate" id="postdate_startDate" readonly/>
						</td>
					</tr>		
					</c:if>
					
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
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:600px"></div>
	</div>
	<!-- //table list-->
</div>