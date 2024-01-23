<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	AUIGrid.destroy(ref_part_pjt_master_myGridID);
	
	ref_part_pjt_master_createAUIGrid(ref_part_pjt_master_columnLayout);
	
	ref_part_pjt_master_getGridData();
	
});

var ref_part_pjt_master_myGridID;

var ref_part_pjt_master_columnLayout = 
	[
	{ dataField : "number", headerText : "${e3ps:getMessage('번호')}", width:"25%", style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "name", headerText : "${e3ps:getMessage('이름')}", width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		},
	},
	{ dataField : "rev",			headerText : "${e3ps:getMessage('버전')}",				width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "stateName",			headerText : "${e3ps:getMessage('작업 현황')}",				width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "creatorFullName",			headerText : "${e3ps:getMessage('등록자')}",			width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "createDateFormat",			headerText : "${e3ps:getMessage('최초 등록일')}",			width:"15%",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "linkOid",			headerText : "${e3ps:getMessage('삭제')}",			width:"15%",
		filter : {
			showIcon : true,
			iconWidth: 30,
		},
		width : 80,
		renderer: {
	        type: "ButtonRenderer",
	        onclick :  function (rowIndex, columnIndex, value, item) {
	          	var linkOid = item.linkOid;
	          	deleteMasteredLink(linkOid);
	        },
	        labelText : "X",
	        labelSize : 5
	       
	    }
	}
	
];

function ref_part_pjt_master_createAUIGrid(ref_part_pjt_master_columnLayout){
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
	
	ref_part_pjt_master_myGridID = AUIGrid.create("#grid_part_pjt_wrap", ref_part_pjt_master_columnLayout, gridPros);
	
	AUIGrid.bind(ref_part_pjt_master_myGridID, "cellClick", ref_part_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(ref_part_pjt_master_myGridID, gridData);
}

function ref_part_pjt_master_getGridData(){
	
	var param = new Object();
	
	param.oid = "${oid}";
	param.type = "part";
	
	AUIGrid.showAjaxLoader(ref_part_pjt_master_myGridID);
	var url = getURLString("/project/masteredLinkListAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		AUIGrid.setGridData(ref_part_pjt_master_myGridID, gridData);
		AUIGrid.removeAjaxLoader(ref_part_pjt_master_myGridID);
		
	});
	
	
}

//셀 클릭 핸들러
function ref_part_auiGridCellClickHandler(event) {
	var dataField = event.dataField;
	var oid = event.item.oid;
}

function add_pjtMasterPart_addObjectList(list){
	createMasteredLink(list);
}

function searchObjectPopup(objType) {
	var url = getURLString("/common/searchObjectPopup") + "?type=multi&pageName=pjtMasterPart&objType=" + objType;
	
	openPopup(url, "searchObjectPopup", 1100, 700);
}

function createMasteredLink(list) {
	var param = new Object();
	
	param.oid = "${oid}";
	param.list = list;
	
	var url = getURLString("/project/createMasterLink");
	
	ajaxCallServer(url, param, function(data){
		ref_part_pjt_master_getGridData();
	});
}

function deleteMasteredLink(oid) {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		var param = new Object();
		
		param.oid = oid;
		
		var url = getURLString("/project/deleteMasterLink");
		
		ajaxCallServer(url, param, function(data){
			ref_part_pjt_master_getGridData();
		});
	})
}

function allDownload(){
	var aoid = "${oid}";
	var atype = "part";
	document.location.href=getURLString("/project/allDownloadAction?oid="+aoid+"&type="+atype);
}
</script>


<div class="tree_top3 pl15 pr15">
	<div class="seach_arm2 pt10 pb5">
		<div class="leftbt">
			<span class="title">
				<c:if test="${type eq 'part'}">
					${e3ps:getMessage('프로젝트  부품 링크 목록')}
				</c:if>
				<c:if test="${type eq 'drawing'}">
					${e3ps:getMessage('프로젝트  도면 링크 목록')}
				</c:if>
			</span>
		</div>
		<div class="rightbt">
<%-- 			<input type="button" class="s_bt03" value="${e3ps:getMessage('일괄 다운로드')}" onclick="allDownload()"> --%>
			<c:if test="${type eq 'drawing'}">
				<input type="button" class="s_bt03" value="${e3ps:getMessage('도면 추가')}" onclick="searchObjectPopup('epm')">
			</c:if>
			<c:if test="${type eq 'part'}">
				<input type="button" class="s_bt03" value="${e3ps:getMessage('부품 추가')}" onclick="searchObjectPopup('part')">
			</c:if>
		</div>
	</div>
	<div class="list" id="grid_part_pjt_wrap" style="height:600px"></div>
</div>