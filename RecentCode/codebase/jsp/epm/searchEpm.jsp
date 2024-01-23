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
	//$('#textEPMSearch').hide();
	//lifecycle list
	getLifecycleList("LC_Default");
	
	
	$("#state option[value='RETURN']").remove();
	$("#state option[value='REWORK']").remove();
	$("#state option[value='DEATH']").remove();
	$("#state option[value='APPROVING']").remove();
	
	//Cad Division list
	getCadDivisionList();
	
	//Cad Type list
	getCadTypeList();
	$("#cadType option[value='OTHER']").remove();
	//grid setting
	createAUIGrid(columnLayout);
	
	AUIGrid.hideColumnByDataField(myGridID, ["checkoutStateName"]);

	//$("input:checkbox[id='likeSearch']").prop("checked", true); 
	//numberSearchOption();
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
	
	{ dataField : "icon",		headerText : "",		width:"30",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
		filter : {
			showIcon : true,
			iconWidth:30
		},
		visible : true,
	},
	{ dataField : "cadType",		headerText : "${e3ps:getMessage('CAD 유형')}",		width:"8%", style:"AUIGrid_Left",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
		filter : {
			showIcon : true,
			iconWidth:30
		},
		visible : false,
	},
	/*
	{ dataField : "cadType",		headerText : "${e3ps:getMessage('CAD 유형')}",		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}
	},
	*/
	{ dataField : "thumbnail",		headerText : "${e3ps:getMessage('도면')}",		width:"60",		style:"pointer",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : thumbnailRenderer,
		visible : true,
		
	},
	{ dataField : "publishMsg",		headerText : "${e3ps:getMessage('변환유무')}",		width:"60",		style:"pointer",
		visible : false,
	},
	
	{ dataField : "number",		headerText : "${e3ps:getMessage('도면 번호')}",		width:"10%",		style:"AUIGrid_Left",			sortValue : "master>number",
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
	{ dataField : "name",		headerText : "${e3ps:getMessage('도면 명')}",		width:"*", style:"AUIGrid_Left",			sortValue : "master>name",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "rev",		headerText : "${e3ps:getMessage('버전')}",			width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "checkoutStateName",		headerText : "${e3ps:getMessage('체크인')}",		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",			width:"7%",
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
	{ dataField : "creatorFullName",		headerText : "${e3ps:getMessage('등록자')}",		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "createDateFormat",		headerText : "${e3ps:getMessage('등록일')}",		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "modifierFullName",		headerText : "${e3ps:getMessage('수정자')}",		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "modifyDateFormat",		headerText : "${e3ps:getMessage('수정일')}",		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "refOwnerPartNumber",		headerText : "${e3ps:getMessage('주/관련품목')}",		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var partOid = item.refOwnerPartOid;
				openView(partOid);
			}
		},
	},
	{ dataField : "refOwnerPartVer",		headerText : "${e3ps:getMessage('품목버전')}",		width:"7%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	/*
	{ dataField : "applicationType",		headerText : "${e3ps:getMessage('응용')}",		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	*/
	/*
	{ dataField : "dwgFileImg",		headerText : "${e3ps:getMessage('AUTOCAD')}",		width:"6%",
		
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction :	imageDWGRenderer
	},
	{ dataField : "pdfFileImg",		headerText : "${e3ps:getMessage('PDF')}",		width:"6%",
		
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction :	imagePDFRenderer
	},
	*/
];

//AUIGrid 를 생성합니다.
function createAUIGrid(columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showAutoNoDataMessage : false,
		
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

	$("#searchForm").attr("action", getURLString("/epm/searchEpmScrollAction"));
	
	var param = new Object();
	
	param["container"] = $("#container").val();
	
	param["page"] = page;
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
	
	if("thumbnail" == dataField){
		if(!isEmpty(event.item.thumbnail)){
			openCreoViewWVSPopup(oid);
		}
		//openCreoViewNEW(oid);
	}
	
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

function epmDown(){
	var url = getURLString("/epm/epmDown");

	openPopup(url,"epmDown", 1124, 600);
	
	
}

function searchGridEPMList(){
	var itemList = AUIGrid.getGridData(myGridID);
	console.log("searchGridEPMList itemList =" + itemList);
	return itemList
}

/* function numberSearchOption(){
	
	
	if($('#likeSearch').is(':checked')){
		$('#autoEPMSearch').hide();
		$('#textEPMSearch').show();
	}else{
		$('#autoEPMSearch').show();
		$('#textEPMSearch').hide();
	}
} */
</script>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt15 pb5">
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
						<th scope="col">${e3ps:getMessage('도면 번호')}</th>
						<td style="white-space: nowrap;">
							<input type="text" id="number" name="number" class="w70">
							<input type="checkbox" id="likeSearchNumber" name="likeSearchNumber"  value="true" checked/>Like 검색
						</td>
						<th scope="col">${e3ps:getMessage('도면 명')}</th>
						<td style="white-space: nowrap;">
							<input type="text" id="nme" name="name" class="w70">
							<input type="checkbox" id="likeSearchName" name="likeSearchName"  value="true" checked/>Like 검색
						</td>
						<th scope="col">${e3ps:getMessage('도면 분류')}</th>
						<td><input type="text" id="locationDisplay" name="locationDisplay" class="w95" disabled></td>
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
							<input type="radio" id="version" name="version" value="new" checked>
							<label>${e3ps:getMessage('최신 버전')}</label>
							<input type="radio" id="version" name="version" value="all">
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
							<select class="multiSelect w30" id="state" name="state" multiple style="height:20px; overflow-y:hidden;">
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
						<%-- <th scope="col">${e3ps:getMessage('CAD 구분')}</th>
						<td>
							<select class="multiSelect w30" id="cadDivision" name="cadDivision" multiple style="height:20px; overflow-y:hidden;">
							</select>
						</td> --%>
						<th scope="col">${e3ps:getMessage('CAD 타입')}</th>
						<td>
							<select class="multiSelect w30" id="cadType" name="cadType" multiple style="height:20px; overflow-y:hidden;">
							</select>
						</td>
						<%-- <th scope="col">${e3ps:getMessage('하위 폴더 미포함')}</th>
						<td>
							<input type="checkbox" id="searchChildFolder" name="searchChildFolder"  value="false" checked="checked"/>
						</td> --%>								
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('고객사')}</th>
						<td>
							<select class="w100" id="customer" name="customer"></select>
						</td>
						<th>UPG</th>
						<td>
							<input type="text" class="w95" id="upg" name="upg">
						</td>
						<th>${e3ps:getMessage('확장자')}</th>
						<td>
							<select class="w100" id="extensions" name="extensions"></select>
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('설계자')}</th>
						<td>
							<input type="text" class="w95" id="designed" name="designed">
						</td>
						<th>${e3ps:getMessage('고객품번')}</th>
						<td>
							<input type="text" class="w95" id="customer_part_number" name="customer_part_number">
						</td>
						<th>${e3ps:getMessage('EO번호')}</th>
						<td>
							<input type="text" class="w95" id="eo_number" name="eo_number">
						</td>
					</tr>
					<tr class="switchDetail">
						<th>${e3ps:getMessage('재질')}</th>
						<td>
							<input type="text" class="w95" id="material" name="material">
						</td>
						<th>${e3ps:getMessage('중량')}</th>
						<td>
							<input type="text" class="w95" id="weight" name="weight">
						</td>
						<th>${e3ps:getMessage('두께')}</th>
						<td>
							<input type="text" class="w95" id="thickness" name="thickness">
						</td>
					</tr>
					<tr class="switchDetail">
						<th>FINISH</th>
						<td>
							<input type="text" class="w95" id="finish" name="finish">
						</td>
						<th></th>
						<td></td>
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
			<button type="button" class="s_bt03 hide" id="createFolder" onclick="folderTree_createFolder();">${e3ps:getMessage('폴더 생성')}</button>
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="excelDown('searchForm', 'excelDownEpm');">${e3ps:getMessage('엑셀 다운로드')}</button>
<%-- 			<<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button> --%>
			<%-- <img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownEpm');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0"> --%>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:500px"></div>
	</div>
	<!-- //table list-->
</div>
