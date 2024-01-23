<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree_top3 pl15 pr15">
	<!-- button -->
	<div class="seach_arm2 pt10 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('템플릿 전체 산출물 목록')}</span>
		</div>
		<div class="rightbt">
			<span> ${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
			</span>
		</div>
	</div>
	<!-- //button -->
	<div class="list" id="grid_wrap"></div>
</div>
<script type="text/javascript">

$(document).ready(function(){
	AUIGrid.destroy(ref_tmp_output_myGridID);
	
	ref_tmpt_output_createAUIGrid(ref_tmp_output_columnLayout);
	
	ref_tmp_output_getGridData();
});

var ref_tmp_output_myGridID;

var ref_tmp_output_columnLayout = [
	{ dataField : "taskName", headerText : "${e3ps:getMessage('태스크 명')}", width:"25%", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},{ dataField : "name", headerText : "${e3ps:getMessage('산출물 명')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},{ dataField : "docTypeDisplay", headerText : "${e3ps:getMessage('인증타입')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},{ dataField : "location", headerText : "${e3ps:getMessage('산출물위치')}", width:"25%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},{ dataField : "roleName", headerText : "${e3ps:getMessage('역할')}", width:"20%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
];

function ref_tmpt_output_createAUIGrid(ref_tmp_output_columnLayout){
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
	
	ref_tmp_output_myGridID = AUIGrid.create("#grid_wrap", ref_tmp_output_columnLayout, gridPros);
	
	AUIGrid.bind(ref_tmp_output_myGridID, "cellClick", ref_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(ref_tmp_output_myGridID, gridData);
}


function ref_tmp_output_getGridData(){
	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(ref_tmp_output_myGridID);
	var url = getURLString("/project/template/searchTemplateOutputAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		AUIGrid.setGridData(ref_tmp_output_myGridID, gridData);
		AUIGrid.removeAjaxLoader(ref_tmp_output_myGridID);
	});
}


//셀 클릭 핸들러
function ref_auiGridCellClickHandler(event) {
	var dataField = event.dataField;
	var oid = event.item.oid;
}
</script>