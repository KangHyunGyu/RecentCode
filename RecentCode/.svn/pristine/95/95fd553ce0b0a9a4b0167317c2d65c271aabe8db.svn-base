<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(rel_bom_myGridID);
	
	//grid setting
	rel_bom_createAUIGrid(rel_bom_columnLayout);
	
	//get grid data
	rel_bom_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var rel_bom_myGridID;

//AUIGrid 칼럼 설정
var rel_bom_columnLayout = [
	{ dataField : "pjt_number",		headerText : "${e3ps:getMessage('프로젝트 번호')}",		width:"*",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "bom",		headerText : "${e3ps:getMessage('ERP 전송 BOM')}",		width:"100",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var icon = "/Windchill/jsp/portal/images/bom_icon.png";
			var instance_id = item.m_instance_id;
			var pjt_number = item.pjt_number;
			var url = getURLString("/erp/ERPBOMTree?instance_id="+instance_id+"&pjt_number="+pjt_number);
			var template = "<img title='${e3ps:getMessage('BOM 보기')}' class='pointer' onclick='javascript:openPopup(\"" + url + "\", \"" + instance_id + "_bom\");' src='" + icon + "'/>";
			
			return template;
		}
	},
	{ dataField : "info",			headerText : "",										width:"40",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var instance_id = item.m_instance_id;
			var pjt_number = item.pjt_number;
			var url = getURLString("/erp/relatedBOMHistory?instance_id="+instance_id+"&pjt_number="+pjt_number);
			var template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openPopup(\"" + url + "\", \"" + instance_id + "_bomHistory\");'>";
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		}
	},
	{ dataField : "compareBom",		headerText : "${e3ps:getMessage('BOM 비교')}",		width:"80",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var icon = "/Windchill/jsp/portal/images/bom_icon.png";
			var pjt_number = "${pjt_number}";
			var template = "<img title='${e3ps:getMessage('BOM 보기')}' class='pointer' onclick='openBomComparator(\"" + pjt_number + "\");' src='" + icon + "'/>";

			return template;
		}
	},
	{ dataField : "if_cdateStr",		headerText : "${e3ps:getMessage('I/F 생성일')}",		width:"21%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "if_rdateStr",		headerText : "${e3ps:getMessage('I/F 수령일')}",		width:"21%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "if_statusStr",		headerText : "${e3ps:getMessage('I/F 상태')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
];

//AUIGrid 를 생성합니다.
function rel_bom_createAUIGrid(rel_bom_columnLayout) {
	
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
		
		autoGridHeight : ${autoGridHeight},
		
		height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성
	rel_bom_myGridID = AUIGrid.create("#rel_bom_grid_wrap", rel_bom_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(rel_bom_myGridID, gridData);
}

function rel_bom_getGridData(){
	var param = new Object();
	
	param.instance_id = "${instance_id}";
	param.ecr_number = "${ecr_number}";
	param.pjt_number = "${pjt_number}";
	
	AUIGrid.showAjaxLoader(rel_bom_myGridID);
	var url = getURLString("/erp/getRelatedBOM_ecn");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(rel_bom_myGridID, gridData);

		AUIGrid.setAllCheckedRows(rel_bom_myGridID, false);
		AUIGrid.removeAjaxLoader(rel_bom_myGridID);
		
	});
}

//필터 초기화
function rel_bom_resetFilter(){
    AUIGrid.clearFilterAll(rel_bom_myGridID);
}

function rel_bom_xlsxExport() {
	AUIGrid.setProperty(rel_bom_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(rel_bom_myGridID, exportProps);
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('관련 BOM')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_bom_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_bom_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_bom_grid_wrap" style="height:${gridHeight}px">
</div>