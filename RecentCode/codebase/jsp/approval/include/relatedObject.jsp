<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${objectTypeName} ${e3ps:getMessage('목록')}</h4></div>
	<div class="rightbt">
<%-- 		<button type="button" class="s_bt03" onclick="rel_obj_resetFilter();">${e3ps:getMessage('필터 초기화')}</button> --%>
		<button type="button" class="s_bt03" onclick="rel_doc_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_object_grid_wrap">
</div>
<script>
$(document).ready(function(){
	AUIGrid.destroy(rel_obj_myGridID);
		
	rel_obj_createAUIGrid(rel_obj_columnLayout);
	
	rel_obj_getGridData();
})

var rel_obj_myGridID;

var rel_obj_columnLayout = [
	{ dataField : "icon",		headerText : "",		width:"30",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
	},
	{ dataField : "number", headerText : "${objectTypeName} ${e3ps:getMessage('번호')}", width:"15%", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "name", headerText : "${objectTypeName} ${e3ps:getMessage('명')}", width:"*%",
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
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",				width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "version",			headerText : "${e3ps:getMessage('버전')}",				width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "creatorFullName",			headerText : "${e3ps:getMessage('등록자')}",			width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "createDateFormat",			headerText : "${e3ps:getMessage('작성일')}",			width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "modifyDate",			headerText : "${e3ps:getMessage('최종 수정일')}",			width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	}
];


function rel_obj_createAUIGrid(rel_obj_columnLayout){
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
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_obj_myGridID = AUIGrid.create("#rel_object_grid_wrap", rel_obj_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_obj_myGridID, "cellClick", ref_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_obj_myGridID, gridData);
}

//셀 클릭 핸들러
function ref_auiGridCellClickHandler(event) {
	var dataField = event.dataField;
	var oid = event.item.oid;
}

function rel_obj_getGridData(){
	var param = new Object();
	
	param.oid = "${oid}";
	var url = getURLString("/multi/getRelatedObject");
	
	
	AUIGrid.showAjaxLoader(rel_obj_myGridID);
	ajaxCallServer(url, param, function(data){
	
	    // 그리드 데이터
	   var gridData = data.list;
	      
	   console.log(gridData);
	      
	   AUIGrid.setGridData(rel_obj_myGridID, gridData);
	   AUIGrid.removeAjaxLoader(rel_obj_myGridID);
	});
}

//필터 초기화
function rel_obj_resetFilter(){
	AUIGrid.clearFilterAll(rel_obj_myGridID);
}

</script>