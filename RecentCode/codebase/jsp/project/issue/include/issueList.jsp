<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<div class="seach_arm2 pt5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		${e3ps:getMessage('이슈')}</span>
	</div>
	<div class="rightbt">
	<c:if test="${isCreateIssueState && !isView}">
       <button type="button" class="s_bt03" id="searchBtn" onclick="createIssue();">${e3ps:getMessage('이슈등록')}</button>
	</c:if>
	</div> 
</div>
<div class="list" id="grid_issue_wrap"  style="height:100px" ></div>
<script type="text/javascript">

$(document).ready(function(){
	AUIGrid.destroy(ref_issue_myGridID);
	
	ref_issue_createAUIGrid(ref_issue_columnLayout);
	
	ref_issue_getGridData();
});

var ref_issue_myGridID;
var ref_issue_columnLayout = [
	{ dataField : "title", headerText : "${e3ps:getMessage('이슈')}", width:"25%", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		},	renderer: {
			type: "LinkRenderer",
			baseUrl: "javascript", // 자바스크립 함수 호출로 사용하고자 하는 경우에 baseUrl 에 "javascript" 로 설정
			// baseUrl 에 javascript 로 설정한 경우, 링크 클릭 시 callback 호출됨.
			jsCallback: function (rowIndex, columnIndex, value, item) {
				var oid = item.oid;
				viewIssue(item.issuePersist);
			}
		},
	},{ dataField : "creatorFullName", headerText : "${e3ps:getMessage('제기자')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},{ dataField : "createDate", headerText : "${e3ps:getMessage('제기일자')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},{ dataField : "deadLine", headerText : "${e3ps:getMessage('완료요청일')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},{ dataField : "managerFullName", headerText : "${e3ps:getMessage('담당자')}", width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},{ dataField : "state", headerText : "${e3ps:getMessage('작업현황')}", width:"14%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
];

function ref_issue_createAUIGrid(ref_issue_columnLayout){
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
	
	ref_issue_myGridID = AUIGrid.create("#grid_issue_wrap", ref_issue_columnLayout, gridPros);
	
	AUIGrid.bind(ref_issue_myGridID, "cellClick", ref_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(ref_issue_myGridID, gridData);
}

//최초 데이터 요청
function ref_issue_getGridData() {
	var param = new Object();
	
	param["oid"] = "${oid}";
	
	AUIGrid.showAjaxLoader(ref_issue_myGridID);
	var url = getURLString("/project/issue/issueListAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		AUIGrid.setGridData(ref_issue_myGridID, gridData);
		AUIGrid.removeAjaxLoader(ref_issue_myGridID);
	});
}

function viewIssue(ooid){
	var loc= "/Windchill/worldex/project/issue/viewIssue?oid=" + ooid;
	openPopup(loc, "viewIssue", 1000, 800);
}
function createIssue(){
	var loc= "/Windchill/worldex/project/issue/createIssue?taskOid=" + "${oid}";
	openPopup(loc, "createIssue", 1000, 600);
}

function ref_auiGridCellClickHandler(event) {
	var dataField = event.dataField;
	var oid = event.item.oid;
}
</script>