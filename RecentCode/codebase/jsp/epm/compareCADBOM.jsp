<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//enter key pressed event
	$("#searchBOMPartNo").keypress(function(e){
		if(e.keyCode==13){
			cadBomTree_search();
		}
	});
	$("#searchERPPartNo").keypress(function(e){
		if(e.keyCode==13){
			plmBomTree_search();
		}
	});
	
	//grid setting
	cadBomTree_createAUIGrid(cadBomTree_columnLayout);
	plmBomTree_createAUIGrid(plmBomTree_columnLayout);
	
	//get grid data
	cadBomComparator_getGridData();
});

//AUIGrid 생성 후 반환 ID
var cadBomTree_myGridID;
var plmBomTree_myGridID;

//bom 트리 컬럼 레이아웃
var cadBomTree_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('도면 번호')}",			width:"*",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "icon",			headerText : "",										width:"40",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "info",			headerText : "${e3ps:getMessage('상세')}",										width:"40",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var oid = item.oid;
		
			var template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openView(\"" + item.oid + "\");'>"
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "compare",			headerText : "${e3ps:getMessage('비교')}",										width:"40",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var oid = item.oid;
		
			var url = getURLString("/epm/compareCADBOM");
			
			url += "?oid=" + oid;

			var template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openPopup(\"" + url + "\",\"compareCADBOM_" + oid + "\", 1100, 550);'>"
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "rev",			headerText : "${e3ps:getMessage('버전')}",				width:"80",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",				width:"8%",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "partNo",			headerText : "PartNo",				width:"8%",	
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "partNoCheck",		headerText : "${e3ps:getMessage('부품 번호 비교')}",		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "isPart",		headerText : "${e3ps:getMessage('부품 존재 여부')}",		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "linkedPart",		headerText : "${e3ps:getMessage('부품 연결 여부')}",		width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "bomMaked",		headerText : "${e3ps:getMessage('BOM 구성 유무')}",		width:"13%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	
];

//plm bom 트리 컬럼 레이아웃
var plmBomTree_columnLayout = [
	{ dataField : "number",				headerText : "${e3ps:getMessage('부품 번호')}",			width:"*",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "info",			headerText : "${e3ps:getMessage('상세')}",										width:"40",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var oid = item.oid;
		
			var template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openView(\"" + item.oid + "\");'>"
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		},
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "rev",			headerText : "${e3ps:getMessage('버전')}",				width:"20%",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",				width:"20%",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "quantity",			headerText : "${e3ps:getMessage('수량')}",				width:"10%",	
		dataType : "numeric",
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(item.difference == 1) {
				return "aui-grid-added-cell";
			} else if(item.difference == 2) {
				return "aui-grid-deleted-cell";
			} else if(item.difference == 3) {
				return "aui-grid-modified-cell";
			}
			return null;
		}
	},
	{ dataField : "cadSync",				headerText : "${e3ps:getMessage('싱크 여부')}",				width:"8%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
];

//Bom 트리 그리드 생성
function cadBomTree_createAUIGrid(cadBomTree_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		rowIdField : "_$uid", 
		
		treeIdField : "treeId",				// 트리의 고유 필드명
		
		treeIdRefField : "parent", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
			
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

		if(item.checkoutState == "c/o") {
			imgSrc = "/Windchill/wt/clients/images/checkout_glyph.gif";
		} else if(item.checkoutState == "wrk") {
			imgSrc = "/Windchill/wt/clients/images/checkout_glyph.gif";
		}
		
		return imgSrc;
	};

	// 실제로 #grid_wrap 에 그리드 생성
	cadBomTree_myGridID = AUIGrid.create("#cadBomTree_grid_wrap", cadBomTree_columnLayout, gridPros);
	
	// H스크롤 체인지 핸들러.
	AUIGrid.bind(cadBomTree_myGridID, "hScrollChange", function(event) {
		//console.log(event.type + ", position : " + event.position + ", (min : " + event.minPosition + ", max : " + event.maxPosition);
		AUIGrid.setHScrollPositionByPx(plmBomTree_myGridID, event.position); // 수평 스크롤 이동 시킴..
	});
		
	// V스크롤 체인지 핸들러.
	AUIGrid.bind(cadBomTree_myGridID, "vScrollChange", function(event) {
		//console.log(event.type + ", position : " + event.position + ", (min : " + event.minPosition + ", max : " + event.maxPosition);
		AUIGrid.setRowPosition(plmBomTree_myGridID, event.position); // 수평 스크롤 이동 시킴..
	});
		
	// 트리 OPEN/CLOSE 핸들러
	AUIGrid.bind(cadBomTree_myGridID, "treeOpenChange", function(event) {
		var item = event.item;
		
		var plmItem = AUIGrid.getItemsByValue(plmBomTree_myGridID, "treeId", item.treeId);
		
		AUIGrid.expandItemByRowId(plmBomTree_myGridID, plmItem[0]._$uid, event.isOpen);
	});
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(cadBomTree_myGridID, "cellClick", function(event) {
		var item = event.item; // 선택된 대표 셀
	    
		var plmItem = AUIGrid.getItemsByValue(plmBomTree_myGridID, "treeId", item.treeId);
		
		AUIGrid.selectRowsByRowId(plmBomTree_myGridID, plmItem[0]._$uid);
	});
	
	var gridData = new Array();
	AUIGrid.setGridData(cadBomTree_myGridID, gridData);
}


//plm 트리 그리드 생성
function plmBomTree_createAUIGrid(plmBomTree_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		rowIdField : "_$uid", 
		
		treeIdField : "treeId",				// 트리의 고유 필드명
		
		treeIdRefField : "parent", 		// 계층 구조에서 내 부모 행의 treeIdField 참고 필드명
			
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
	plmBomTree_myGridID = AUIGrid.create("#plmBomTree_grid_wrap", plmBomTree_columnLayout, gridPros);
	
	if(${e3ps:isAdmin()}) {
		AUIGrid.showColumnByDataField(plmBomTree_myGridID, "cadSync");
	} else {
		AUIGrid.hideColumnByDataField(plmBomTree_myGridID, "cadSync");
	}
	
	// H스크롤 체인지 핸들러.
	AUIGrid.bind(plmBomTree_myGridID, "hScrollChange", function(event) {
		//console.log(event.type + ", position : " + event.position + ", (min : " + event.minPosition + ", max : " + event.maxPosition);
		AUIGrid.setHScrollPositionByPx(cadBomTree_myGridID, event.position); // 수평 스크롤 이동 시킴..
	});
		
	// V스크롤 체인지 핸들러.
	AUIGrid.bind(plmBomTree_myGridID, "vScrollChange", function(event) {
		//console.log(event.type + ", position : " + event.position + ", (min : " + event.minPosition + ", max : " + event.maxPosition);
		AUIGrid.setRowPosition(cadBomTree_myGridID, event.position); // 수평 스크롤 이동 시킴..
	});
		
	// 트리 OPEN/CLOSE 핸들러
	AUIGrid.bind(plmBomTree_myGridID, "treeOpenChange", function( event ) {
		var item = event.item;
		
		var cadbomItem = AUIGrid.getItemsByValue(cadBomTree_myGridID, "treeId", item.treeId);
		
		AUIGrid.expandItemByRowId(cadBomTree_myGridID, cadbomItem[0]._$uid, event.isOpen);
	});
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(plmBomTree_myGridID, "cellClick", function(event) {
		var item = event.item; // 선택된 대표 셀
	    
		var cadbomItem = AUIGrid.getItemsByValue(cadBomTree_myGridID, "treeId", item.treeId);
		
		AUIGrid.selectRowsByRowId(cadBomTree_myGridID, cadbomItem[0]._$uid);
	});
	
	var gridData = new Array();
	AUIGrid.setGridData(cadBomTree_myGridID, gridData);
}

//bom 트리 불러오기
function cadBomComparator_getGridData(){
	
	var param = new Object();
	
	param.oid = "${epm.oid}";	
	
	AUIGrid.showAjaxLoader(cadBomTree_myGridID);
	AUIGrid.showAjaxLoader(plmBomTree_myGridID);
	var url = getURLString("/bomComparator/getCADBomComparatorData");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var cadBomList = data.cadBomList;
		var plmBomList = data.plmBomList;
		
		console.log(cadBomList);
		console.log(plmBomList);
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(cadBomTree_myGridID, cadBomList);

		AUIGrid.removeAjaxLoader(cadBomTree_myGridID);
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(plmBomTree_myGridID, plmBomList);

		AUIGrid.removeAjaxLoader(plmBomTree_myGridID);
		
		cadBomComparator_showItemsOnLevel();
		//AUIGrid.expandAll(cadBomTree_myGridID);
		//AUIGrid.expandAll(plmBomTree_myGridID);
	});
}

function cadBomComparator_showItemsOnLevel() {
	var openLevel = $("#openLevel").val();
	
	// 해당 depth 까지 오픈함
	if(openLevel != "ALL") {
		AUIGrid.showItemsOnDepth(cadBomTree_myGridID, Number(openLevel) + 1);
		AUIGrid.showItemsOnDepth(plmBomTree_myGridID, Number(openLevel) + 1);
	} else {
		AUIGrid.expandAll(cadBomTree_myGridID);
		AUIGrid.expandAll(plmBomTree_myGridID);
	}
};

function cadBomTree_search() {
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
	AUIGrid.search(cadBomTree_myGridID, "number", searchBOMPartNo, options);
	
}

function plmBomTree_search() {
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
	AUIGrid.search(plmBomTree_myGridID, "number", searchERPPartNo, options);
	
}
</script>
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('CAD BOM 비교')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->
	
	<div class="pop_semi">
		<div class="pop_cadBomComparator_cadList">
			<div class="seach_arm2 mb5">
				<div class="leftbt" style="width:30%;">
					<h4>${e3ps:getMessage('CAD 구조')}</h4>
				</div>
				<div class="rightbt" style="width:70%;">
					<select id="openLevel" name="openLevel" style="width:130px;" onchange="javascript:cadBomComparator_showItemsOnLevel();">
						<option value="1">1 Level</option>
						<option value="2">2 Level</option>
						<option value="3">3 Level</option>
						<option value="4">4 Level</option>
						<option value="ALL" selected="selected">ALL</option>
					</select>
					<input type="text" id="searchBOMPartNo" name="searchBOMPartNo" class="w50">
					<button type="button" class="s_bt03" onclick="javascript:cadBomTree_search();">${e3ps:getMessage('검색')}</button>
				</div>
			</div>
			<div class="list" id="cadBomTree_grid_wrap" style="height:500px">
			</div>
		</div>
		<div class="pop_cadBomComparator_plmList">
			<div class="seach_arm2 mb5">
				<div class="leftbt">
					<h4>PLM BOM</h4>
				</div>
				<div class="rightbt">
					<input type="text" id="searchERPPartNo" name="searchERPPartNo" class="w50">
					<button type="button" class="s_bt03" onclick="javascript:plmBomTree_search();">${e3ps:getMessage('검색')}</button>
				</div>
			</div>
			<div class="list" id="plmBomTree_grid_wrap" style="height:500px">
			</div>
		</div>
	</div>
</div>