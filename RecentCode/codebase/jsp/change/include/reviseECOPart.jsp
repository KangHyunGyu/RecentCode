<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_part_myGridID);
	
	//grid setting
	rel_part_createAUIGrid(rel_part_columnLayout);
	
	//get grid data
	rel_part_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_part_myGridID;
var timerId = null;

var add_part_objList = [];
//AUIGrid 칼럼 설정
var rel_part_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('품목 번호')}",			width:"10%",		style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		editRenderer : { // 편집 모드 진입 시 원격 리스트 출력하고자 할 때
		    //type : "RemoteListRenderer",
		    //fieldName : "number",
			type : "RemoteListRenderer",
		    showEditorBtnOver : true, // 마우스 오버 시 에디터버턴 보이기
		    autoCompleteMode : true, // 자동완성 모드 설정 (기본값 :false)
			autoEasyMode : true, // 자동완성 모드일 때 첫 아이템 자동 선택할지 여부 (기본값 : false)
			keyField : "number", // key 에 해당되는 필드명
		    fieldName : "number",
		    
		    remoter : function( request, response ) { // remoter 반드시 지정 필수
		    	if(String(request.term).length <= 3 ) {
					response([]); // term 이 없는 경우 기본값을 보여줄지, 아니면 빈 것을 보여줄지...선택해서 코딩.
					//response(false); // 데이터 요청이 없는 경우 반드시 false 삽입하십시오.
					return;
				}
		    	
		    	if(timerId != null) {
					clearTimeout(timerId);
				}
		    	
		    	// 데이터 요청
				var param = new Object();
				param["number"] = request.term;
				param["objType"] = "part";
				param["likeSearch"] = "like";
				param["moduleType"] = "ECO";
				param["viewName"] ="Design";
				
				var url = getURLString("/common/searchObjectAction");
				ajaxCallServer(url, param, function(data){
					
					// 그리드 데이터
					add_part_objList = data.list;
					
					//중복 제거
					var gridList = AUIGrid.getGridData(rel_part_getGridData);

					for(var i=0; i < gridList.length; i++) {
						add_part_objList = add_part_objList.filter(function(item, index, arr){
						    return item.oid != gridList[i].oid;
						});
					}
					
					// 그리드에 데이터 세팅
					response(add_part_objList); 
					
				});// end of ajax
				
				timerId = setTimeout(function() {
					
				}, 200); 
			},
			listTemplateFunction : function(rowIndex, columnIndex, text, item, dataField, listItem) {
				
				var html = '<div class="myList-style">';
				html += '<span class="myList-col" style="padding-left:10px; width:120px;" title="' + listItem.number + '">' + listItem.number + '</span>';
				html += '<span class="myList-col" style="width:150px;">' + listItem.name + '</span>';
				html += '<span class="myList-col" style="width:80px; text-align:right;">' + listItem.rev + '</span>';
				html += '</div>';
				
				return html;
			}
		}	
		
	},
	{ dataField : "name",		headerText : "${e3ps:getMessage('품목 명')}",		width:"*",		style:"AUIGrid_Left",	editable: false,
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.partOid;
				console.log(oid);
				
				openView(oid);
			}
		}
	},
	{
		dataField : "beforePart", headerText : "${e3ps:getMessage('개정')}",
		children : [
			{ dataField : "isRevisePartName",	headerText : "${e3ps:getMessage('대상')}",	width:"8%",		editable: false,
				visible : true,	
				filter : {
					showIcon : true,
					iconWidth:30
				}	
					
			},
			{ dataField : "reviseResult",		headerText : "${e3ps:getMessage('결과')}",	width:"8%",		editable: false,
				visible : true,	
				renderer : {
					type : "ImageRenderer",
					imgHeight : 18, // 이미지 높이, 지정하지 않으면 rowHeight에 맞게 자동 조절되지만 빠른 렌더링을 위해 설정을 추천합니다.
					altField : "message", // alt(title) 속성에 삽입될 필드명, 툴팁으로 출력됨
					srcFunction : function(rowIndex, columnIndex, value, item) {
						switch(value) {
						case true :
							return "/Windchill/jsp/change/images/task_complete.gif";
						case false:
							return "/Windchill/jsp/change/images/task_red.gif";
						default :
							return null;// 반환하면 이미지 표시 안함.
						}
					} // end of srcFunction
				},
			},
		],
	},
	
	
	{ dataField : "isRevisePart",	headerText : "${e3ps:getMessage('개정')}",	width:"3%",		editable: false,
		visible : false,	
			
	},
	{ dataField : "isDelete",		headerText : "${e3ps:getMessage('삭제')}",	width:"3%",		editable: false,
		visible : false,	
			
	},
	{ dataField : "isChange",		headerText : "${e3ps:getMessage('수정')}",	width:"3%",		editable: false,
		visible : false,	
			
	},
	{ dataField : "message",		headerText : "msg",	width:"3%",		editable: false,
		visible : false,	
			
	},
	
	{ dataField : "bom",		headerText : "BOM",		width:"5%",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			
			var icon = "/Windchill/jsp/portal/images/bom_icon.png";
			
			var template = "<img title='${e3ps:getMessage('BOM 보기')}' class='pointer' onclick='openBomTree(\"" + item.partOid + "\")' src='" + icon + "'/>";
			
			return template;
		}
	},
	{ dataField : "appleyName",				headerText : "${e3ps:getMessage('ECO 적용 유형')}",				width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		visible : false,	
	},
	{ dataField : "serial",				headerText : "${e3ps:getMessage('S/N관리')}",				width:"8%",
		visible : false,	
			
	},
	{
		dataField : "beforePart", headerText : "${e3ps:getMessage('개정전')}",
		children : [
			{ dataField : "rev",			headerText : "${e3ps:getMessage('버전')}",		width:"8%", 	editable: false,
						
			},
			{ dataField : "stateName",		headerText : "${e3ps:getMessage('상태')}",		width:"8%",		editable: false,
				filter : {
					showIcon : true,
					iconWidth:30
				}	
			},
			
		],
	},
	{
		dataField : "nextPart", headerText : "${e3ps:getMessage('개정후')}",
		children : [
			{ dataField : "nextRev",			headerText : "${e3ps:getMessage('버전')}",	width:"8%", 	editable: false,
				renderer : {
					type : "LinkRenderer",
					baseUrl : "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
					// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
					jsCallback : function(rowIndex, columnIndex, value, item) {
						var oid = item.nextOid;
						openView(oid);
					}
				}	
			},
			{ dataField : "nextStateName",			headerText : "${e3ps:getMessage('상태')}",	width:"8%",		editable: false,
						
			},
		],
	},
// 	{ dataField : "controlDoc",		headerText : "${e3ps:getMessage('제어문서')}",			width:"6%",
// 		filter : {
// 			showIcon : true,
// 			iconWidth:30
// 		}	
// 	},
// 	{ dataField : "controlDWG",		headerText : "${e3ps:getMessage('제어도면')}",			width:"6%",
// 		filter : {
// 			showIcon : true,
// 			iconWidth:30
// 		}	
// 	},
	
// 	{ dataField : "stock",			headerText : "${e3ps:getMessage('재고')}",				width:"5%",	editable: false, },
// 	{ dataField : "price",			headerText : "${e3ps:getMessage('단가')}",				width:"5%",	editable: false, },
// 	{ dataField : "currency",			headerText : "${e3ps:getMessage('통화')}",			width:"5%",	editable: false, },
// 	{ dataField : "peinh",			headerText : "${e3ps:getMessage('단위')}",				width:"7%",	editable: false, },
// 	{ dataField : "vendorName",			headerText : "${e3ps:getMessage('업체명')}",			width:"5%",	editable: false, },
	
];

//AUIGrid 를 생성합니다.
function rel_part_createAUIGrid(rel_part_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		softRemoveRowMode : false,
		
		noDataMessage : gridNoDataMessage,
		
		rowIdField : "_$uid",
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showAutoNoDataMessage : false,
		
		showRowCheckColumn : true,
		
		enableFilter : true,
		
		editable	: true,
		
		enableMovingColumn : true,
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
		
		reverseRowNum : false, //No 정렬  true: desc, false : asc (Default)
		rowCheckableFunction : function(rowIndex, isChecked, item) {
			
			return true;
		},
		
// 		rowCheckDisabledFunction : function(rowIndex, isChecked, item) {
// 			if(!item.isDelete) { //삭제 가능 영부
// 				return false;
// 			}
// 			return true;
// 		}
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_part_myGridID = AUIGrid.create("#rel_part_grid_wrap", rel_part_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_part_myGridID, "cellClick", rel_part_auiGridCellClickHandler);
	
	//에디팅 시작 시 이벤트
	AUIGrid.bind(rel_part_myGridID, "cellEditBegin", rel_part_aauiEditBeginHandler);
	
	// 에디팅 정상 종료 이벤트 바인딩
	AUIGrid.bind(rel_part_myGridID, "cellEditEnd", rel_part_auiCellEditEngHandler);
	
	// 전체 체크박스 클릭 이벤트 바인딩
// 	AUIGrid.bind(rel_part_myGridID, "rowAllChkClick", function( event ) {
// 		if(event.checked) {
// 			AUIGrid.setCheckedRowsByValue(event.pid, "isDelete", ["true"]);
// 		} else {
// 			AUIGrid.setCheckedRowsByValue(event.pid, "isDelete", []);
// 		}
// 	});
	
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_part_myGridID, gridData);
}

function rel_part_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	param.checkType = "check"; //개정 유무 체크
	AUIGrid.showAjaxLoader(rel_part_myGridID);
	var url = getURLString("/change/getECOActivePartList");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		console.log(gridData);
		//alert("data.list =" + gridData);
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_part_myGridID, gridData);
		rel_part_setSorting();
		AUIGrid.setAllCheckedRows(rel_part_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_part_myGridID);
		
	});
}

function rel_part_setSorting() {
	//mNumber,pporderAX,orderDate
	var sortingInfo = [];
	
	sortingInfo[0] = { dataField : "number", sortType : 1 };
	AUIGrid.setSorting(rel_part_myGridID, sortingInfo);
}


<%----------------------------------------------------------
*                 편집 시작 핸들러 
----------------------------------------------------------%>
function rel_part_aauiEditBeginHandler(event) {
	var item = event.item;
	if(event.dataField == "number" ) {  //품목 수정 불가
		if(!item.isChange){
			return false;
		}
	}
	
};

<%----------------------------------------------------------
*                편집 종료 핸들러   
----------------------------------------------------------%>
function rel_part_auiCellEditEngHandler(event) {

	switch(event.type) {
	case "cellEditEnd" :
		if(event.dataField == "number") {
			
			var item = add_part_getItem(event.value);

			if(typeof item === "undefined") {
				AUIGrid.updateRow(rel_part_myGridID, {
					number : "",
					name : "",
					rev : "",
					stateName : "",
					oid : "",
				}, event.rowIndex);
				return;
			}
			
			// ISBN 수정 완료하면, 책제목, 저자 등의 필드도 같이 업데이트 함.
			/*
			AUIGrid.updateRow(rel_part_myGridID, {
				number : item.number,
				name : item.name,
				rev : item.rev,
				stateName : item.stateName,
				oid : item.oid,
			}, event.rowIndex);
			*/
			// 품목 추가 EcoPartLink에 추가
			addRowUpdate(item.number,event.rowIndex);
		}
		break;
	case "cellEditCancel" :
		break;
	}
	
};
<%----------------------------------------------------------
*                 품목 추가 시 품목 정합성 및 도면 리스트 추가
----------------------------------------------------------%>
function addRowUpdate(number,rowIndex){
	
	var url = getURLString("/eco/getECOAddPart");
	var param = new Object();
	param.oid = "${oid}";
	param["number"] = number;
	ajaxCallServer(url, param, function(data){
		
		// 그리드 데이터
		var result	= data.result
		
		if(result){
			rel_part_getGridData();
			rel_epm_getGridData();
		}
		/*
		var partItem = data.part;
		var epmList = data.epmList;
		
		AUIGrid.updateRow(rel_part_myGridID, {
			number 			: partItem.number,
			name 			: partItem.name,
			rev 			: partItem.rev,
			stateName 		: partItem.stateName,
			oid 			: partItem.oid,
			isRevisePart 	: partItem.isRevisePart,
			isDelete		: partItem.isDelete,
			isChange		: partItem.isChange,
		}, rowIndex);
		*/
		
		console.log("result = " +result);
		//console.log("partItem = " +partItem);
		//console.log("epmList = " +epmList);
				
	});// end of ajax
}

<%----------------------------------------------------------
*                 셀 클릭 핸들러
----------------------------------------------------------%>
function rel_part_auiGridCellClickHandler(event) {

}

<%----------------------------------------------------------
*                  해당 품목의 BOM Search Popup
----------------------------------------------------------%>
function add_part_searchBomPopup(oid) {

	var url = getURLString("/bom/searchBomPopup") + "?oid=" + oid + "&pageName=part&moduleType=ECO";
	
	openPopup(url,"searchBomPopup", 1400, 600);
	
}

<%----------------------------------------------------------
*                  해당 품목의 도면/문서 정합성
----------------------------------------------------------%>
function partDrawingDocCheck(oid) {

	var url = getURLString("/part/partDrawingDocCMainheck") + "?oid=" + oid + "&pageName=part&moduleType=ECO";
	
	openPopup(url,"partDrawingDocCMainheck", 1024, 600);
	
}

<%----------------------------------------------------------
*                   폼목 검색 한 Itme return
----------------------------------------------------------%>
function add_part_getItem(number) {
	
	var item;
	$.each(add_part_objList, function(n, v) {
		if(v.number == number) {
			item = v;
			return false;
		}
	});
	return item;
};

<%----------------------------------------------------------
*                   BOM Search 에서 품목 추가
----------------------------------------------------------%>
function add_part_addObjectList(list) {
	
	var gridList = AUIGrid.getGridData(rel_part_myGridID);
	
	for(var i=0; i < gridList.length; i++) {
		list = list.filter(function(item, index, arr){
		    return item.oid != gridList[i].oid;
		});
	}
	var itemList = [];
	for(var i=0; i < list.length; i++) {
		var item = list[i];
		if(item.hasOwnProperty("children")) {
			delete item.children;
		}
		
		itemList[i] = item;
		
		console.log(item);
		//AUIGrid.addRow(rel_part_myGridID, item, "first");	
	}
	
	var url = getURLString("/eco/getECOAddBatchPart");
	var param = new Object();
	param.oid = "${oid}";
	param.itemList = itemList;
	ajaxCallServer(url, param, function(data){
		
		// 그리드 데이터
		var result	= data.result
		
		if(result){
			rel_part_getGridData();
			rel_epm_getGridData();
		}
	});// end of ajax
	
	/*
	
	*/
	
};

<%----------------------------------------------------------
*                 Row 추가
----------------------------------------------------------%>
function addRow() {

	// 그리드의 편집 인푸터가 열린 경우 에디팅 완료 상태로 만듬.
	AUIGrid.forceEditingComplete(rel_part_myGridID, null);
	
	var item = new Object();
	item.number ="";
	item.name = "";
	item.enName = "";
	item.isRevisePart = true;
	item.isDelete = true;
	item.isChange = true;
	item.rev = "";
	item.stateName = "";
	item.nextRev = "";
	item.nextStateName = "";
	AUIGrid.addRow(rel_part_myGridID, item, "first");
	
	
}
<%----------------------------------------------------------
*                 Row 삭제
----------------------------------------------------------%>
function removeRow() {
	
	
	
	var checkItemList = AUIGrid.getCheckedRowItems(rel_part_myGridID);
	
	if(checkItemList.length == 0){
		alert("${e3ps:getMessage('삭제할 품목을 선택해 주세요!')}")
		return false;
	}
	
	if(!confirm("${e3ps:getMessage('선택한 품목을 ECO 대상 품목에서 삭제 하시겠습니까?')}")){
		return false;
	}
	
	var url = getURLString("/eco/getECODeletePart");
	var param = new Object();
	param.oid = "${oid}";
	param.checkItemList = checkItemList;
	ajaxCallServer(url, param, function(data){
		
		// 그리드 데이터
		var result	= data.result
		
		if(result){
			rel_part_getGridData();
			rel_epm_getGridData();
		}
	});// end of ajax
	
	/*
	for(var i = 0; i < checkItemList.length; i++){
		
		AUIGrid.removeRowByRowId(rel_part_myGridID, checkItemList[i].item._$uid);
	}
	
	AUIGrid.setAllCheckedRows(rel_part_myGridID, false); 
	*/
	/*
	var data = AUIGrid.getGridData(rel_part_myGridID);
	
	if( data.length > 0) {
		var rowPos = "selectedIndex";
		AUIGrid.removeRow(rel_part_myGridID, rowPos);
		//resizeGrid();
	}
	*/
}

<%----------------------------------------------------------
*                 품목 개정
----------------------------------------------------------%>
function batchECOPartRevise(){
	var checkItemList = AUIGrid.getCheckedRowItems(rel_part_myGridID);
	for(var i = 0 ; i< checkItemList.length ; i++){
		
		var item =checkItemList[i].item
		
		console.log(item);
		
		//var item = checkItemList[i];
		var number = item.number;
		var state =  item.state;
		var isRevisePart = item.isRevisePart;
		var oid = item.oid;
		var pratOid = item.pratOid;
		var linkOid = item.linkOid;
		var isRevisePartName = item.isRevisePartName
		//console.log("checkItemList[i] =" + checkItemList[i]);
		console.log(number +","+state +","+isRevisePart +","+oid +","+isRevisePartName);
		
		if(isRevisePartName =="-"){
			alert(number +"${e3ps:getMessage('은 개정 대상이 아닙니다.')}")
			return false;
		}
	}
	
	
	
	if(checkItemList.length == 0){
		alert("${e3ps:getMessage('개정 대상 품목을 선택해 주세요!')}")
		return false;
	}
	
	if(!confirm("${e3ps:getMessage('품목을 개정 하시겠습니까?')}")){
		return false;
	}
	
	var url = getURLString("/change/batchECOPartRevise");
	var param = new Object();
	param.oid = "${oid}";
	param.checkItemList = checkItemList;
// 	var data = ajaxCallServer(url, param, function(){}, true);
	var data = ajaxCallServer(url, param, null);
	
	var result	= data.result
	var itemList	= data.list
	var isResult = false;
	console.log("1.result =" + result);
	
	
	if(result =="true" || result){
		alert("정상적으로 개정 되었습니다.");
		rel_epm_getGridData();
	
	}else{
		alert("개정결과에 빨강 신호등이 존재시 개정을 할수 없습니다.\n개정 결과를 확인 하시기 바랍니다.");
		isResult = true;
		//alert("${e3ps:getMessage('개정시 에러가 발생 하였습니다.')}\n${e3ps:getMessage('관리자 에게 문의 하시기 바랍니다.')}" );
		
	}
	
	var gridList = AUIGrid.getGridData(rel_part_myGridID);
		
	for(var i = 0 ; i < gridList.length ; i ++){
		
		var item = gridList[i];
		
		var number = item.number;
		
		
		for(var ii = 0 ; ii <itemList.length ; ii++){
			
			//console.log("1.number =" + number + partItem.isRevisePart);
			var partItem = itemList[ii];
			var partNumber = partItem.number;
			var message = partItem.message;
			var reviseResult = partItem.reviseResult;
			var nextRev = partItem.nextRev;
			var nextStateName = partItem.nextStateName;
			var isDelete = partItem.isDelete;
			if(number == partNumber){
			//console.log(" 2.number = " + number +",message =" + message +",reviseResult =" + reviseResult );
				if(result){
					var itemUpdate = { _$uid :item._$uid ,message : partItem.message , 
							  reviseResult : reviseResult , nextRev:nextRev,nextStateName:nextStateName,
							  isDelete : isDelete,isRevisePart:isResult,isRevisePartName:"-"}; 
					  //AUIGrid.updateRow(rel_part_myGridID, itemUpdate, 0);
					  AUIGrid.updateRowsById(rel_part_myGridID, itemUpdate);
					  
					  AUIGrid.addUncheckedRowsByValue(rel_part_myGridID, "isDelete", false);
				}else{
					if(!reviseResult){
						var itemUpdate = { _$uid :item._$uid ,message : partItem.message , 
								  reviseResult : reviseResult ,
								  isRevisePart:isResult}; 
						  //AUIGrid.updateRow(rel_part_myGridID, itemUpdate, 0);
						  AUIGrid.updateRowsById(rel_part_myGridID, itemUpdate);
					}
					
				}
				  
			}
		}
		
	}
	
	
}

function batchECOPartUpdate(){
	var checkItemList = AUIGrid.getCheckedRowItems(rel_part_myGridID);

	if(checkItemList.length == 0){
		alert("${e3ps:getMessage('수정할 품목을 선택해 주세요!')}")
		return false;
	
	}
	
	for(var i = 0 ; i< checkItemList.length ; i++){
		
		var item =checkItemList[i].item
		
		var number = item.number;
		var state =  item.state;
		var isRevisePart = item.isRevisePart;
		var oid = item.oid;
		var pratOid = item.pratOid;
		var linkOid = item.linkOid;
		var isRevisePartName = item.isRevisePartName
		console.log(number +","+state +","+isRevisePart +","+oid +","+isRevisePartName, item);
		
		if(state != "승인됨" && state != "APPROVED" ){
			alert("${e3ps:getMessage('승인됨 상태의 품목만 가능합니다.')}")
			return false;
		}
		
		if(item.isCheckInPart){
			alert("${e3ps:getMessage('이미 수정을 진행하였습니다.')}")
			return false;
		}
	}
	
	
	
	if(!confirm("${e3ps:getMessage('품목을 수정 하시겠습니까?')}")){
		return false;
	}
	
	var url = getURLString("/eco/batchMCOPartChange");
	var param = new Object();
	param.oid = "${oid}";
	param.checkItemList = checkItemList;
	var data = ajaxCallServer(url, param, null);
	
	var result	= data.result
	var itemList	= data.list
	var isResult = false;
	console.log("1.result =" + result);

	if(result =="true" || result){
		alert("정상적으로 상태 변경  되었습니다.");
		rel_epm_getGridData();
	
	}else{
		alert("상태 변경에 빨강 신호등이 존재시 상태 변경을 할수 없습니다.\n상태변경 결과를 확인 하시기 바랍니다.");
		isResult = true;
		//alert("${e3ps:getMessage('개정시 에러가 발생 하였습니다.')}\n${e3ps:getMessage('관리자 에게 문의 하시기 바랍니다.')}" );
		
	}
	
	rel_part_getGridData();
	rel_epm_getGridData();
	
	
	
	
}


<%----------------------------------------------------------
*                   필터 초기화
----------------------------------------------------------%>
function rel_part_resetFilter(){
  AUIGrid.clearFilterAll(rel_part_myGridID);
}
<%----------------------------------------------------------
*                   엑샐 출력
----------------------------------------------------------%>
function rel_part_xlsxExport() {
	AUIGrid.setProperty(rel_part_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_part_myGridID, exportProps);
}
</script>

<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('품목')}</h4></div>
	<div class="rightbt">
<%-- 		<button type="button" class="i_update" style="width:80px"  onclick="addRow()">${e3ps:getMessage('품목 추가')}</button> --%>
<%-- 		<button type="button" class="i_delete" style="width:80px"  onclick="removeRow();">${e3ps:getMessage('품목 삭제')}</button> --%>
		<button type="button" class="i_create" style="width:80px"  onclick="batchECOPartRevise();">${e3ps:getMessage('품목 개정')}</button>
<%-- 		<button type="button" class="i_update" style="width:80px"  onclick="batchECOPartUpdate();">${e3ps:getMessage('MCO')}</button> --%>
		<!-- 
		<button type="button" class="s_bt03" onclick="rel_part_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_part_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	 	-->
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_part_grid_wrap" style="height:${gridHeight}px">
</div>