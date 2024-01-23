<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//enter key pressed event
	$("#pjtNo").keypress(function(e){
		if(e.keyCode==13){
			erpTree_getGridData();
		}
	});
	$("#partNo").keypress(function(e){
		if(e.keyCode==13){
			erpTree_getGridData();
		}
	});
	$("#searchERPPartNo").keypress(function(e){
		if(e.keyCode==13){
			erpTree_search();
		}
	});
	
	//grid setting
	erpTree_createAUIGrid(erpTree_columnLayout);
	
	//get grid data
	erpTree_getGridData();
});

//AUIGrid 생성 후 반환 ID
var erpTree_myGridID;

//erp 트리 컬럼 레이아웃
var erpTree_columnLayout = [
	{ dataField : "child_number",		headerText : "${e3ps:getMessage('부품 번호')}",		width:"20%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "pjt_number",		headerText : "${e3ps:getMessage('프로젝트 번호')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "product_number",		headerText : "${e3ps:getMessage('장비 번호')}",		width:"13%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "ecr_number",		headerText : "${e3ps:getMessage('ECR 번호')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "parent_number",		headerText : "${e3ps:getMessage('모 ITEM 번호')}",		width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "parent_version",		headerText : "${e3ps:getMessage('모 ITEM 버전')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "child_number",		headerText : "${e3ps:getMessage('자 ITEM 번호')}",		width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "child_version",		headerText : "${e3ps:getMessage('자 ITEM 버전')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "child_quantity",		headerText : "${e3ps:getMessage('자 ITEM 수량')}",		width:"11%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "bom_location",		headerText : "${e3ps:getMessage('경로')}",		width:"25%", style:"AUIGrid_Left", 
		filter : {
			showIcon : true,
			iconWidth:30
		},
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
	{ dataField : "if_statusStr",		headerText : "${e3ps:getMessage('I/F 상태')}",		width:"9%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//erp 트리 그리드 생성
function erpTree_createAUIGrid(erpTree_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		rowIdField : "_$uid", 
		
		treeIdField : "bom_location",				// 트리의 고유 필드명
		
		treeIdRefField : "parentTreeId", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
			
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showAutoNoDataMessage : false,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		softRemoveRowMode : false,
		
		// 일반 데이터를 트리로 표현할지 여부(treeIdField, treeIdRefField 설정 필수)
		flat2tree : true,
		
		// 엑스트라 행 체크박스 칼럼이 트리 의존적인지 여부
		// 트리 의존적인 경우, 부모를 체크하면 자식도 체크됨.
		rowCheckDependingTree : true,
	};

	// 트리 아이콘을 바꿀 수 있는 트리 아이콘 펑션입니다.
	gridPros.treeIconFunction = function(rowIndex, isBranch, isOpen, depth, item) {
		var imgSrc = null;
		
		//checkoutState 에 때라 이미지 변경 필요
		imgSrc = "/Windchill/wtcore/images/part.gif";

		return imgSrc;
	};
	
	// 실제로 #grid_wrap 에 그리드 생성
	erpTree_myGridID = AUIGrid.create("#erpTree_grid_wrap", erpTree_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(erpTree_myGridID, gridData);
}

//bom 트리 불러오기
function erpTree_getGridData(){
	
	var param = new Object();
	
	param.instance_id = "${instance_id}";
	param.pjt_number = "${pjt_number}";
	
	AUIGrid.showAjaxLoader(erpTree_myGridID);
	var url = getURLString("/erp/getRelatedBOMHistory");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(erpTree_myGridID, gridData);

		AUIGrid.expandAll(erpTree_myGridID);
		
		erpTree_showItemsOnLevel();
		
		AUIGrid.removeAjaxLoader(erpTree_myGridID);
	});
}

function erpTree_showItemsOnLevel() {
	var openLevel = $("#openLevel").val();
	
	// 해당 depth 까지 오픈함
	if(openLevel != "ALL") {
		AUIGrid.showItemsOnDepth(erpTree_myGridID, Number(openLevel) + 1);
	} else {
		AUIGrid.expandAll(erpTree_myGridID);
	}
};

function erpTree_search() {
	var searchERPPartNo = $("#searchERPPartNo").val();

	if(searchERPPartNo.trim() == "") {
		openNotice("${e3ps:getMessage('검색할 단어를 입력하십시오.')}");
		return;
	}
	
	var options = {
		direction : true, // 검색 방향  (true : 다음, false : 이전 검색)
		caseSensitive : false, // 대소문자 구분 여부 (true : 대소문자 구별, false :  무시)
		wholeWord : false, // 온전한 단어 여부
		wrapSearch : true, // 끝에서 되돌리기 여부
	};

	// 검색 실시
	//options 를 지정하지 않으면 기본값이 적용됨(기본값은 direction : true, wrapSearch : true)
	AUIGrid.search(erpTree_myGridID, "child_number", searchERPPartNo, options);
	
}
</script>
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>ERP BOM / ${pjt_number}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->
	
	<div class="con pl25 pr25 pb15">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt">
				<%-- <span>${e3ps:getMessage('부품 번호')} : </span>
				<input type="text" id="partNo" name="partNo" class="w20">
				<button type="button" class="s_bt03" onclick="erpTree_getGridData();">${e3ps:getMessage('검색')}</button> --%>
			</div>
			<div class="rightbt">
				<select id="openLevel" name="openLevel" style="width:130px;" onchange="javascript:erpTree_showItemsOnLevel();">
					<option value="1">1 Level</option>
					<option value="2">2 Level</option>
					<option value="3">3 Level</option>
					<option value="4">4 Level</option>
					<option value="ALL">ALL</option>
				</select>
				<span>${e3ps:getMessage('그리드 내 검색')} : </span>
				<input type="text" id="searchERPPartNo" name="searchERPPartNo" class="w20">
				<button type="button" class="s_bt03" onclick="javascript:erpTree_search();">${e3ps:getMessage('찾기')}</button>
			</div>
		</div>
		<div class="list" id="erpTree_grid_wrap" style="height:500px">
		</div>
	</div>
</div>