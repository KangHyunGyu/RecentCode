<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//grid setting
	createAUIGrid(columnLayout);
	//결재 구분 
	getApprovalObjectTypeList();
	//결재 상태
	getApprovalStateList();
	//get grid data
	getGridData();
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
// 				console.log(item);
				var url = getURLString("/distribute/viewRegistrationDistribute") + "?oid="+item.objectOid;
	    		openPopup(url,"setPassword", 1100, 580);
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
	{ dataField : "completeDateFormat",		headerText : "${e3ps:getMessage('완료일')}",		width:"10%",
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
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

function getGridData(){

	var param = new Object();
	$("#searchForm").attr("action", getURLString("/distribute/listRegistrationAction"));
	param["type"] = "${type}";
	AUIGrid.showAjaxLoader(myGridID);
	formSubmit("searchForm", param, null, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		$("#total").html(gridData.length);
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(myGridID, gridData);

		AUIGrid.setAllCheckedRows(myGridID, false);
		AUIGrid.removeAjaxLoader(myGridID);
	});
}

//셀 클릭 핸들러
function auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
}

//검색조건 초기화
function reset(){
	$("#searchForm")[0].reset();
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

//검색
function search(){
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
							<th scope="col">${e3ps:getMessage('제목')}</th>
							<td><input type="text" id="title" name="title" class="w100"></td>
							
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
							<th scope="col">${e3ps:getMessage('완료일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" name="predate_complete" id="predate_complete" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate_complete" id="postdate_complete" readonly/>
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
				<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
	<%-- 			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button> --%>
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
