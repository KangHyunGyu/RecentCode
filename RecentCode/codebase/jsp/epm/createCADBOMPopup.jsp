<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(myGridID);
	
	//grid setting
	createAUIGrid(columnLayout);
	
	checkCADBOM();
});

var checkCADBOMFinish = false;

function checkCADBOM(){
	$("#createCADBOMForm").attr("action",getURLString("/epm/checkCADBOMAction"));
	
	var param = new Object();

	param["oid"] = "${epm.oid}";
	
	AUIGrid.showAjaxLoader(myGridID);
	formSubmit("createCADBOMForm", param, null, function(data){
		var gridData = data.list;
		
		AUIGrid.setGridData(myGridID, gridData);
		
		AUIGrid.removeAjaxLoader(myGridID);
		
		checkCADBOMFinish = true;
	});
}

function createCADBOM(){
	if(!checkCADBOMFinish) {
		openNotice("${e3ps:getMessage('정합성 체크를 먼저 진행하세요.')}");
		return;
	}
	
	$("#createCADBOMForm").attr("action",getURLString("/epm/createCADBOMAction"));
	
	var param = new Object();

	param["oid"] = "${epm.oid}";
	
	formSubmit("createCADBOMForm", param, "${e3ps:getMessage('CADBOM을 생성하시겠습니까?')}", function(data) {
		var gridData = data.list;
		
		AUIGrid.setGridData(load_bom_myGridID, gridData);
		
		AUIGrid.removeAjaxLoader(load_bom_myGridID);
		
		checkCADBOMFinish = false;
	}, true);
}

//AUIGrid 생성 후 반환 ID
var myGridID;

//AUIGrid 칼럼 설정
var columnLayout = [
	{ dataField : "icon",		headerText : "",		width:"30",
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},	
	},
	{ dataField : "number",		headerText : "${e3ps:getMessage('도면 번호')}",		width:"*",	style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "info",			headerText : "",										width:"40",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var oid = item.oid;
		
			var template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openView(\"" + item.oid + "\");'>"
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		}
	},
	{ dataField : "name",		headerText : "${e3ps:getMessage('도면 명')}",		width:"15%",	style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "rev",			headerText : "${e3ps:getMessage('버전')}",				width:"80",
	},
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('상태')}",				width:"8%",
	},
	{ dataField : "partNo",		headerText : "${e3ps:getMessage('부품 번호')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "linkedPart",		headerText : "${e3ps:getMessage('부품 연결 여부')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "partNoCheck",		headerText : "${e3ps:getMessage('부품 번호 비교')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "isPart",		headerText : "${e3ps:getMessage('부품 존재 여부')}",		width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "partInfo",			headerText : "${e3ps:getMessage('부품 정보')}",										width:"70",
		renderer : {
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var partOid = item.partOid;
		
			var template = "";
			if(partOid != null && partOid != "") {
				template = "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openView(\"" + partOid + "\");'>"	
			}
			
			return template; // HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
		}
	},
];

//AUIGrid 를 생성합니다.
function createAUIGrid(columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,

		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		editable : false,
		
		enableFilter : true,
		
		enableMovingColumn : false,
	};

	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
}

function xlsxExport() {
	AUIGrid.setProperty(myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(myGridID, exportProps);
}

function openCompareCADBOM() {
	var url = getURLString("/epm/compareCADBOM");
	
	url += "?oid=${epm.oid}";

	openPopup(url, "compareCADBOM_${epm.oid}", 1100, 550);
}
</script>
<!-- pop -->
<div class="pop">
<form name="createCADBOMForm" id="createCADBOMForm" method="post">
	<!-- top -->
	<div class="top">
		<h2>${epm.icon} ${e3ps:getMessage('도면')} - ${epm.number}, ${epm.name}, ${epm.version}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<div class="semi_content">
		<div class="semi_content2">
			<div class="seach_arm pt5 pb5">
				<div class="leftbt">
				</div>
				<div class="rightbt">
					<button type="button" class="s_bt03" onclick="javascript:openCompareCADBOM();">${e3ps:getMessage('CAD BOM 비교')}</button>
					<%-- 
					<button type="button" class="s_bt03" onclick="javascript:checkCADBOM();">${e3ps:getMessage('정합성 체크')}</button>
					--%>
					<c:if test="${e3ps:isAdmin()}">
					<%-- 
						<button type="button" class="s_bt03" onclick="javascript:createCADBOM();">${e3ps:getMessage('CADBOM 생성')}</button>
						--%>
					</c:if>
					<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
					<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
				</div>
			</div>
			<!-- //button -->
			<!-- table list-->
			<div class="table_list">
				<div class="list" id="grid_wrap" style="height:500px"></div>
			</div>
			<!-- //table list-->
		</div>
	</div>
</form>
</div>		
<!-- //pop-->