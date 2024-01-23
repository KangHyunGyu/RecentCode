<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree_top3 pl15 pr15">
	<div class="seach_arm2 pt10 pb5">
		<div class="leftbt">
			<span class="title">${e3ps:getMessage('프로젝트 전체 산출물 목록')}</span>
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" onclick="javascript:excelDownOid('${oid}', 'excelDownProjectOutput');">${e3ps:getMessage('엑셀 다운로드')}</button>
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
			</span>
		</div>
	</div>
	<div class="list" id="grid_wrap"></div>
</div>
<script>
$(document).ready(function(){
	AUIGrid.destroy(ref_pjt_output_myGridID);
	
	ref_pjt_output_createAUIGrid(ref_pjt_output_columnLayout);
	
	ref_pjt_output_getGridData();
});

var ref_pjt_output_myGridID;

var ref_pjt_output_columnLayout = [
	
	{ dataField : "taskName", headerText : "${e3ps:getMessage('태스크 명')}", width:"25%", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "name", headerText : "${e3ps:getMessage('산출물')}", width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "roleName", headerText : "${e3ps:getMessage('프로젝트 롤')}", width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "docTypeDisplay", headerText : "${e3ps:getMessage('인증 타입')}", width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "docStateName", headerText : "${e3ps:getMessage('작업 현황')}", width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "docNumber", headerText : "${e3ps:getMessage('문서')}", width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer: {
			type: "LinkRenderer",
			baseUrl: "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback: function (rowIndex, columnIndex, value, item) {
				var oid = item.docOid;
				openView(oid);
			},
		},
	}
	
];


function ref_pjt_output_createAUIGrid(ref_pjt_output_columnLayout){
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
	
	ref_pjt_output_myGridID = AUIGrid.create("#grid_wrap", ref_pjt_output_columnLayout, gridPros);
	
	AUIGrid.bind(ref_pjt_output_myGridID, "cellClick", ref_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(ref_pjt_output_myGridID, gridData);
}


function ref_pjt_output_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	
	AUIGrid.showAjaxLoader(ref_pjt_output_myGridID);
	var url = getURLString("/project/projectOutputListAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		AUIGrid.setGridData(ref_pjt_output_myGridID, gridData);
		AUIGrid.removeAjaxLoader(ref_pjt_output_myGridID);
	});
	
}


//셀 클릭 핸들러
function ref_auiGridCellClickHandler(event) {
	var dataField = event.dataField;
	var oid = event.item.oid;
}

</script>