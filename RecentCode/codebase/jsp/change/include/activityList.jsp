<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('단계 활동')}</h4></div>
	<div class="rightbt">
<%-- 		<button type="button" class="s_bt03" onclick="rel_epm_resetFilter();">${e3ps:getMessage('필터 초기화')}</button> --%>
<%-- 		<button type="button" class="s_bt03" onclick="rel_epm_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button> --%>
	</div>
</div>
<!-- //button -->

<div class="list" id="rel_eca_grid_wrap">
</div>
<div id="includeOutput">
</div>
<script>
$(document).ready(function(){
	AUIGrid.destroy(rel_eca_myGridID);
		
	rel_eca_createAUIGrid(rel_eca_columnLayout);
	
	activity_grid_getGridData();
})

var rel_eca_myGridID;

var rel_eca_columnLayout = [
	{ dataField : "name", headerText : "${e3ps:getMessage('단계')}", width:"*%", style:"AUIGrid_Left",
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
				console.log(oid);
				openView(oid);
			}
		}
	},
	{ dataField : "dept", headerText : "${e3ps:getMessage('부서')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "owner", headerText : "${e3ps:getMessage('담당자')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "state", headerText : "${e3ps:getMessage('작업현황')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "finishDate", headerText : "${e3ps:getMessage('완료예정일')}", width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "ecaFinishDate", headerText : "${e3ps:getMessage('완료일')}", width:"10%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

function rel_eca_createAUIGrid(rel_eca_columnLayout){
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
	rel_eca_myGridID = AUIGrid.create("#rel_eca_grid_wrap", rel_eca_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(rel_eca_myGridID, "cellClick", ref_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_eca_createAUIGrid, gridData);
}

/* template:function(obj){
	return "<img src=\"" + obj.imgUrl+ "\">" + obj.state + "</a>"
} */

//셀 클릭 핸들러
function ref_auiGridCellClickHandler(event) {
	var dataField = event.dataField;
	var oid = event.item.oid;
}

function activity_grid_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(rel_eca_myGridID);
	var url = getURLString("/change/getActivity");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_eca_myGridID, gridData);
	    AUIGrid.removeAjaxLoader(rel_eca_myGridID);
	});
}

//필터 초기화
function rel_epm_resetFilter(){
    
}
function viewChangeActivity(){
    
}

function rel_epm_xlsxExport() {
	webix.toExcel($$("activity_grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
	});
}
function openViewChangeActivity(actOid){
	var url = getURLString("/change/viewChangeActivity");
	var param = new Object();
	param.oid = actOid;
	$("#includeOutput").load(url, param);
}

</script>