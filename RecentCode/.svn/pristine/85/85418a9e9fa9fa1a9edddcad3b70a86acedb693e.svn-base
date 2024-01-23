<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	
	document.getElementById("keyword").addEventListener("keyup", (e) => {
		preRequestData();
	})
	
	//grid setting
	createAUIGrid(columnLayout);
	
	//get grid data
	preRequestData();
	
});

//AUIGrid 생성 후 반환 ID
var myGridID;

//AUIGrid 칼럼 설정
var columnLayout = [
	
	{ dataField : "roleName",		headerText : "${e3ps:getMessage('프로젝트')} Role",			width:"20%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "duty",			headerText : "${e3ps:getMessage('직위')}",			width:"15%", 
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "userName",		headerText : "${e3ps:getMessage('이름')}",		width:"25%", editable:false,
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	
	{ dataField : "email",		headerText : "${e3ps:getMessage('이메일')}",		width:"*", editable:false,
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
		
		rowCheckToRadio : true,
		
		rowHeight : gridRowHeight
	};

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
	var paramArray = $("#searchForm").serializeArray();
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(myGridID);
	var url = getURLString("/project/searchProjectRoleUser");
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


function selectUser(){
	var checkItemList = AUIGrid.getCheckedRowItems(myGridID);
	checkItemList = checkItemList.map((x)=>x.item)
	
	if(checkItemList.length == 0) {
		openNotice("${e3ps:getMessage('유저를 선택하세요.')}");
		return;
	}
	
	if(opener.window.setRoleUser){
		if("${id}" != ""){
			opener.window.setRoleUser("${id}", checkItemList);
		}
	}
	
	window.close();
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트')} Role ${e3ps:getMessage('유저 리스트')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<div class="semi_content">
		<div class="semi_content2">
			<!-- button -->
			<div class="seach_arm pt10">
				<div class="leftbt">
				</div>
				<div class="rightbt">
				</div>
			</div>
			<!-- //button -->
			<!-- pro_table -->
			<div class="semi_table2 pt10">
				<div class="table_list">
					<form name="searchForm" id="searchForm" style="margin-bottom:0">
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
									<th scope="col">${e3ps:getMessage('이름')}</th>
									<td><input type="text" id="keyword" name="keyword" class="w100" /></td>
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
