<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
	.rightBtn-custom-style{
		margin-right:10px;
		vertical-align:middle !important;
		height:30px;
		padding-bottom:10px;
	}
	.full_close{ background:#bbbdbe; border:1px solid #bbbdbe;  color:#fff;  border-radius: 2px; font-size: 12px;  line-height: 22px;  font-weight:bold; min-width: fit-content; white-space: nowrap;}
	.full_delete{ background:#A41E22; border:1px solid #A41E22;  color:#fff;  border-radius: 2px; font-size: 12px;  line-height: 22px;  font-weight:bold; min-width: fit-content; white-space: nowrap;}

</style>
<script>
$(document).ready(function(){
	
	$("#searchBOMPartNo").keypress(function(e){
		if(e.keyCode==13){
			bomTree_search();
		}
	})
	
	//팝업 리사이즈
	popupResize();
	
	//grid reset
	AUIGrid.destroy(bomTree_myGridID);
	
	//grid setting
	bomTree_createAUIGrid(bomTree_columnLayout);
	
	//get grid data
	bomTree_getGridData();
	
	
});

//필드 초기값
let isCost = 'A';
let pcbExcept = true;
let commonPart = true;

//AUIGrid 생성 후 반환 ID
var bomTree_myGridID;

//AUIGrid 칼럼 설정
var bomTree_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('품목 번호')}",			width:"*",		style:"AUIGrid_Left",
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", 
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.oid;
				
				openBomTree(oid);
			}
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.state == "WITHDRAWN") {
				return "aui-grid-withdrawn-cell";
			}
			return null;
		}
	},
	{ dataField : "level",				headerText : "level",									width:"7%",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.state == "WITHDRAWN") {
				return "aui-grid-withdrawn-cell";
			}
			return null;
		}
	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('품목 명')}",				width:"20%",		style:"AUIGrid_Left",
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript",
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.oid;
				
				openView(oid);
			}
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.state == "WITHDRAWN") {
				return "aui-grid-withdrawn-cell";
			}
			return null;
		}	
	},
	{ dataField : "w_isCostName",				headerText : "${e3ps:getMessage('원가계정')}",				width:"7%"
	},
	/* { dataField : "commonPart",				headerText : "${e3ps:getMessage('공통품목')}",				width:"7%"
	}, */
	{ dataField : "rev",			headerText : "${e3ps:getMessage('버전')}",				width:"7%"},
	{ dataField : "stateName",				headerText : "${e3ps:getMessage('상태')}",				width:"7%",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.state == "WITHDRAWN") {
				return "aui-grid-withdrawn-cell";
			}
			return null;
		}	
	},
	{ dataField : "quantity",			headerText : "${e3ps:getMessage('수량')}",				width:"7%",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.state == "WITHDRAWN") {
				return "aui-grid-withdrawn-cell";
			}
			return null;
		}
	},
	{ dataField : "unit",				headerText : "${e3ps:getMessage('단위')}",				width:"7%",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.state == "WITHDRAWN") {
				return "aui-grid-withdrawn-cell";
			}
			return null;
		}
	},
	{ dataField : "thumbnail",			headerText : "${e3ps:getMessage('형상')}",										width:"7%",		style:"pointer",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : thumbnailRenderer,
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.state == "WITHDRAWN") {
				return "aui-grid-withdrawn-cell";
			}
			return null;
		}	
	},
	{ dataField : "info",			headerText : "BOM",										width:"7%",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openBomTree(\"" + item.oid + "\");'>"
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.state == "WITHDRAWN") {
				return "aui-grid-withdrawn-cell";
			}
			return null;
		}
	},
];

//AUIGrid 를 생성합니다.
function bomTree_createAUIGrid(bomTree_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		rowIdField : "treeId", 
		
		treeIdField : "treeId",				// 트리의 고유 필드명
		
		treeIdRefField : "parent", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
			
		selectionMode : "multipleRows",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : true,
		
		enableFilter : true,
		
		enableMovingColumn : false,
		
		// 일반 데이터를 트리로 표현할지 여부(treeIdField, treeIdRefField 설정 필수)
		flat2tree : true,
		
		// 엑스트라 행 체크박스 칼럼이 트리 의존적인지 여부
		// 트리 의존적인 경우, 부모를 체크하면 자식도 체크됨.
		rowCheckDependingTree : false,
		
		// 트리그리드에서 하위 데이터를 나중에 요청하기 위한 true 설정
		treeLazyMode : true,

		//fixedColumnCount : 6,
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
	};

	// 트리 아이콘을 바꿀 수 있는 트리 아이콘 펑션입니다.
	gridPros.treeIconFunction = function(rowIndex, isBranch, isOpen, depth, item) {
		var imgSrc = null;
		
		//checkoutState 에 때라 이미지 변경 필요
		imgSrc = "/Windchill/wtcore/images/part.gif";
		
		return imgSrc;
	};
	
	if(${e3ps:isAdmin()}) {
		AUIGrid.showColumnByDataField(bomTree_myGridID, "cadSync");
	} else {
		AUIGrid.hideColumnByDataField(bomTree_myGridID, "cadSync");
	}
	
	// 실제로 #grid_wrap 에 그리드 생성
	bomTree_myGridID = AUIGrid.create("#bomTree_grid_wrap", bomTree_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(bomTree_myGridID, "cellClick", bomTree_auiGridCellClickHandler);
	
	// 트리그리드 lazyLoading 요청 이벤트 핸들러 
	AUIGrid.bind(bomTree_myGridID, "treeLazyRequest", bomTree_auiGridTreeLazeRequestHandler);
	var gridData = new Array();
	AUIGrid.setGridData(bomTree_myGridID, gridData);
}

function bomTree_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.desc = $("#desc").val();
	param.initLevel = $("#initLevel").val();
	
	AUIGrid.showAjaxLoader(bomTree_myGridID);
	var url = getURLString("/bom/getBomRoot");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		

		// 그리드에 데이터 세팅
		AUIGrid.setGridData(bomTree_myGridID, gridData);

		AUIGrid.expandAll(bomTree_myGridID);
		
		AUIGrid.removeAjaxLoader(bomTree_myGridID);
		
		$("#openLevel option").remove();
		
		var maxLevel = $("#initLevel").val();
		if($("#initLevel").val() == "ALL") {
			maxLevel = 4;
		}
		
		for(var i=1; i <= maxLevel; i++) {
			var selected = "";
			var levelText = "";
			
			var lang = navigator.language || navigator.userLanguage;

			if(lang == 'ko'){
				levelText =  i + "${e3ps:getMessage('레벨까지 보이기')}";
			}else{
				levelText =  "${e3ps:getMessage('레벨까지 보이기')}" + i;
			}
			
			if(i == maxLevel) {
				selected = "selected";
			}
			$("#openLevel").append($("<option>", { 
				value: i,
				text : levelText,
				selected : selected
			}));
		}
		
		if($("#initLevel").val() == "ALL") {
			$("#openLevel").append($("<option>", { 
				value: "ALL",
				text : "ALL",
			}));
		}
	},true);
}

//셀 클릭 핸들러
function bomTree_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
	if("thumbnail" == dataField){
		openCreoViewWVSPopup(oid);
	}
}

//lazyLoading 핸들러
function bomTree_auiGridTreeLazeRequestHandler(event) {
	
	var item = event.item;

	var oid = item.oid;
	var level = item.level;
	var treeId = item.treeId;
	
	// 자식 데이터 요청
	var param = new Object();
	
	param.oid = oid;
	param.level = level;
	param.treeId = treeId;
	param.desc = $("#desc").val();
	
	var url = getURLString("/bom/getBomChildren");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var childrenList = data.list;
		
		// 성공 시 완전한 배열 객체로 삽입하십시오.
		event.response(childrenList);
	});
}

function showItemsOnLevel(event) {
	var openLevel = $("#openLevel").val();
	
	// 해당 depth 까지 오픈함
	if(openLevel != "ALL") {
		AUIGrid.showItemsOnDepth(bomTree_myGridID, Number(openLevel) + 1);	
	} else {
		AUIGrid.expandAll(bomTree_myGridID);
	}
	
};

//필터 초기화
function bomTree_resetFilter(){
    AUIGrid.clearFilterAll(bomTree_myGridID);
}


function bomTree_search() {
	var searchBOMPartNo = $("#searchBOMPartNo").val();

	if(searchBOMPartNo.trim() == "") {
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
	AUIGrid.search(bomTree_myGridID, "number", searchBOMPartNo, options);
	
}

//버튼 타입과 체크된 행들 부모 메소드에 전달
function addPurchasePart(type){
	
	
	let returnRowData = [];
	let checkParts = AUIGrid.getCheckedRowItems(bomTree_myGridID);
	let notApprovedList = [];
	
	if(checkParts.length == 0){
		alert("${e3ps:getMessage('체크된 부품이 없습니다')}")
		return;
	}
	
	if(!confirm("${e3ps:getMessage('승인된 부품만 추가됩니다')}.\n${e3ps:getMessage('선택된 부품을 추가하시겠습니까?')}")){
		return;
	}
	
	//ROW 순대로 정렬
	checkParts.sort(function(a, b) {return a.rowIndex - b.rowIndex;});
	
	//ROW 의 ITEM 객체(품목정보)만 배열에 다시 담아준다.
	checkParts =  checkParts.map((it) => {
		return it.item;
	})
	
	let itemTreeIds = checkParts.map((it)=>{return it.treeId});
	startProgress();
	
	checkParts.forEach((rowData)=>{
		if(type == 'assy'){
			//해당품목보다 상위 부모 품목이있으면 추가하지 않는다.
			let existParent = itemTreeIds.some( (x) => {return x == rowData.parent} );
			if(existParent)return false;
			
			//일단 해당 part 넣어준다.
			//returnRowData = [...returnRowData, rowData];
			
			//Assy 경우로직 추가
			
			let url = getURLString("/bom/getBomItemListAll");
			let param = new Object();
			param.oid = rowData.oid;
			let data = ajaxCallServer(url, param, null, false);
			let childrenList = data.list;
			
			
			childrenList = childrenList.filter((f)=> {
				let isAssy = assy_validate(f);
				return !isAssy;
			})
			//자식 part 서버에서 가져와 넣어준다.
			if( childrenList.length > 0  ){
				returnRowData = [...returnRowData,...childrenList];
			}
			
			
		}else{
			//그냥 품목 추가 시 그대로 추가하여 return
			returnRowData = [...returnRowData,rowData];
		}
	});
	
	
	//리턴해줄 부품 리스트에서 승인되지 않은 부품은 제외
	returnRowData = returnRowData.filter((r) => {
			
		let compareCost = false;
		if(isCost == 'P' || isCost == 'M'){
			compareCost = cost_validate(r, isCost);
		}else{
			compareCost = true;
		}
		
		let isPcbExcept = false;
		if(pcbExcept){
			isPcbExcept = PCBException_validate(r);
		}
			
		/* let isCommonPart = false;
		if(commonPart){
			isCommonPart = commonPart_validate(r);
		} */
		
		if(pcbExcept){
			return compareCost && isPcbExcept;
		}else{
			return compareCost;
		}
	});
	
	//위 validation 체크 후 작업중인 부품 alert 표시를 위해 따로 다시 필터..
	//위에서하면 문제생김
	returnRowData = returnRowData.filter((a) => {
		
		let isApp = false;
		if(a.state == 'APPROVED'){
			isApp = true;
		}else{
			notApprovedList.push(a.number);
		}
		
		return isApp;
	})
	
	endProgress();
	
	if(notApprovedList.length > 0){
		alert(notApprovedList.join(', ')+" ${e3ps:getMessage('는 [작업중] 입니다')}.\n구매요청 대상 품목이 아닙니다.")
	}
	
	//BOM 수량 * 생산요청 수량 처리
	let qty = opener.document.getElementById('qty').value;
	returnRowData.map(a => a.bomquantity = a.quantity )
	returnRowData.map(a => a.quantity = a.quantity * qty )
	
	opener.window.getPurchasePartsByBom(returnRowData);
}


//onchange function
const common_changeEvent = (ele, type) => {
	switch (type) {
	case 'cost': isCost = ele.value;
		break;
	case 'pcbException': 
		if(pcbExcept){
			pcbExcept = false;
			ele.className = 'full_close'
		}else{
			pcbExcept = true;
			ele.className = 'full_delete'
		}
		break;
	/* case 'commonPart': commonPart = ele.checked;
		break; */
	default : break;
	}
}

const assy_validate = (item) => {
	console.log('item',item)
	if(item.children != null){
		return true;
	}else{
		return false;
	}
	/* if(item.container == "${e3ps:getContainerName('product')}"){
		let compareStr = item.number.substring(0, 4).toUpperCase() == 'ME16';
		let compareStrLength = item.number.split('-')[0].length == 4 ;
		
		if(compareStrLength && compareStr){
			return true;
		}else{
			return false;
		}
	}else{
		if(item.partclassfication == 'Assy'){
			return true;
		}else{
			return false;
		}
	} */
}

const PCBException_validate = (item) => {
	const compareStr = item.number.substring(0, 2).toUpperCase() == 'EN';
	const compareStrLength = item.number.split('-')[0].length == 4 ;
	
	if(compareStrLength && compareStr){
		return false;
	}else{
		return true;
	}
}

/* const commonPart_validate = (item) => {
	const comareCommonPart = item.attributes.COMMONPART;
	if(typeof comareCommonPart != "undefined" && comareCommonPart == "O"){
		return true;
	}else{
		return false;
	}	
} */

const cost_validate = (item, value) => {
	const compareCost = item.attributes.W_ISCOST;
	if(typeof compareCost != "undefined" && compareCost == value){
		return true;
	}else{
		return false;
	}
}


</script>
<div class="seach_arm2 pt10 pb5 mb10">
	<div class="leftbt">
		<span class="title" style="vertical-align:top !important;"><img class="" src="/Windchill/jsp/portal/images/t_icon.png"> BOM</span>
		<input style="width:185px" type="text" id="searchBOMPartNo" name="searchBOMPartNo">
		<button type="button" class="s_bt03" style="vertical-align:top !important;"  onclick="javascript:bomTree_search();">${e3ps:getMessage('검색')}</button>
		
		
	</div>
	<div class="rightbt">
		<select style="width:70px;"  onchange="common_changeEvent(this,'cost');">
			<option value="A">${e3ps:getMessage('전체')}</option>
			<option value="P">${e3ps:getMessage('구매')}</option>
			<option value="M">${e3ps:getMessage('가공')}</option>
		</select>
		<%-- <input type="checkBox" class="rightBtn-custom-style" style="margin-left:15px;" onchange="common_changeEvent(this,'pcbException');" checked/><label class="rightBtn-custom-style" style="font-size:15px;">PCB${e3ps:getMessage('제외')}</label> --%>
		<%-- <input type="checkBox" class="rightBtn-custom-style" onchange="common_changeEvent(this,'commonPart');" checked/><label class="rightBtn-custom-style" style="font-size:15px;">${e3ps:getMessage('공통품목')}</label> --%>
		<button type="button" class="full_delete" style="width:60px;height:30px" onclick="common_changeEvent(this,'pcbException');">PCB${e3ps:getMessage('제외')}</button>
		<button type="button" class="i_create" style="width:60px;height:30px" onclick="addPurchasePart('assy')">${e3ps:getMessage('하위 품목 일괄 추가')}</button>
		<button type="button" class="i_create" style="width:60px;height:30px" onclick="addPurchasePart('')">${e3ps:getMessage('품목 추가')}</button>
		<select id="desc" name="desc" style="width:70px;" class="" onchange="javascript:bomTree_getGridData()">
			<option value="true">${e3ps:getMessage('정전개')}</option>
			<option value="false">${e3ps:getMessage('역전개')}</option>
		</select>
		<select id="initLevel" name="initLevel" class="" style="width:70px;" onchange="javascript:bomTree_getGridData();">
			<option value="1">1 Level</option>
			<option value="2">2 Level</option>
			<option value="3">3 Level</option>
			<option value="4">4 Level</option>
			<option value="ALL">ALL</option>
		</select>
		<select id="openLevel" name="openLevel" style="width:130px;" class="" onchange="javascript:showItemsOnLevel();">
		</select>
	</div>
</div>
<div class="list" id="bomTree_grid_wrap" style="height:${gridHeight}px">
</div>