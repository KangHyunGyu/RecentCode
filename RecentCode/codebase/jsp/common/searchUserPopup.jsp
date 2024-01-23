<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	if("true" === "${except}"){
		$("#isDisable option[value='false']").attr("selected", "selected");
		$("#isDisable option").not(":selected").attr("disabled", "disabled");
	}
	
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
	
});

//AUIGrid 생성 후 반환 ID
var myGridID;

var dutyList = ["대표이사", "부사장", "전무", "상무", "이사", "상무보", "수석연구원", "부장", "책임연구원", "차장", "선임연구원", "과장", "전임연구원", "대리", "연구원", "사원", "실장"];


//AUIGrid 칼럼 설정
var columnLayout = [
	
	{ dataField : "name",		headerText : "이름",		width:"25%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "id",		headerText : "아이디",		width:"25%",	editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	
	{ dataField : "duty",			headerText : "직위",			width:"10%", 
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "departmentName",		headerText : "부서",			width:"15%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "email",		headerText : "이메일",		width:"*", editable:false,
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
		
		editable : false,
			
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : true,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight
	};

	if("${type}" == "single") {
		gridPros["rowCheckToRadio"] = true;	
	}
	
	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	// 선택 변경 이벤트 바인딩
	AUIGrid.bind(myGridID, "selectionChange", auiGridSelectionChangeHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

function auiGridSelectionChangeHandler(evt){
	AUIGrid.setCheckedRowsByIds(myGridID, evt.selectedItems.map((x)=>x.rowIdValue))
}

//최초 데이터 요청
function preRequestData() {
	var param = new Object();
	param.isDisable = 'false';
	var paramArray = $("#searchForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
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

//검색
function search(){
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

function reset(){
	var locationDisplay = $("#locationDisplay").val();
	$("#searchForm")[0].reset();
	$("#locationDisplay").val(locationDisplay);
	
}

function selectUser(){
	var checkItemList = AUIGrid.getCheckedRowItems(myGridID);
	checkItemList = checkItemList.map((x)=>x.item)
	
	if(checkItemList.length == 0) {
		openNotice("${e3ps:getMessage('유저를 선택하세요.')}");
		return;
	}
	
	if(opener.window.setUser){
		if("${id}" != ""){
			opener.window.setUser("${id}", checkItemList);
		}
	}
	
	if(opener.window.saveGroupUser && typeof opener.window.saveGroupUser == 'function') {
		if(!confirm("${e3ps:getMessage('선택하신 사용자를 추가하시겠습니까?')}")){
			return false;
		}
		opener.window.saveGroupUser(checkItemList);
	}
	
	window.close();
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('유저 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<div class="semi_content">
		<div class="con semi_table1 ml10 mt10" >
			<jsp:include page="${e3ps:getIncludeURLString('/department/include_departmentTree')}" flush="true">
				<jsp:param name="code" value="00000"/>
				<jsp:param name="autoGridHeight" value="false"/>
				<jsp:param name="gridHeight" value="600"/>
			</jsp:include>
		</div>
		<div class="semi_content2">
			<!-- button -->
			<div class="seach_arm pt10">
				<div class="leftbt">
				</div>
				<div class="rightbt">
					<button type="button" class="s_bt03" id="searchBtn" onclick="search();">검색</button>
					<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
				</div>
			</div>
			<!-- //button -->
			<!-- pro_table -->
			<div class="semi_table2 pt10">
				<div class="table_list">
					<form name="searchForm" id="searchForm" style="margin-bottom:0">
						<input type="hidden" name="departmentOid" id="departmentOid" value="" />
						<table summary="">
							<caption></caption>
							<colgroup>
								<col style="width:10%">
								<col style="width:20%">
								<col style="width:10%">
								<col style="width:20%">
								<col style="width:20%">
								<col style="width:20%">
							</colgroup>
							
							<tbody>
								<tr>
									<th scope="col">${e3ps:getMessage('아이디')}</th>
									<td><input type="text" id="userId" name="userId" class="w100" /></td>
									<th scope="col">${e3ps:getMessage('이름')}</th>
									<td><input type="text" id="userName" name="userName" class="w100" /></td>
									<%-- <th scope="col">${e3ps:getMessage('재직 여부')}</th>
									<td>
										<select class="" id="isDisable" name="isDisable" >
											<option value="">${e3ps:getMessage('전체')}</option>
											<option value="false">${e3ps:getMessage('재직')}</option>
											<option value="true">${e3ps:getMessage('퇴사')}</option>
										</select>
									</td> --%>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<!-- //pro_table -->
			<!-- table list-->
			<div class="table_list">
				<!-- button -->
				<div class="seach_arm2 pt5 pb5">
					<div class="leftbt">검색결과 (<span id="total">0</span> 개)</div>
					<div class="rightbt">
						<button type="button" class="s_bt03" onclick="javascript:selectUser()">${e3ps:getMessage('선택')}</button>
					</div>
				</div>
				<!-- //button -->
				<div class="list" id="grid_wrap" style="height: 500px;"></div>
			</div>
			<!-- //table list-->
		</div>
	</div>
</div>		
