<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//grid setting
	${roleType}_line_createAUIGrid(${roleType}_line_columnLayout);
	
	if(opener.window.app_line_sendAppLineToPopup) {
		var approvalLineList = opener.window.app_line_sendAppLineToPopup();
		
		for(var i = 0; i < approvalLineList.length; i++){
			var line = approvalLineList[i];
			
			var roleType = line.roleType;
			var name = line.name;
			
			if(roleType != null && roleType.length > 0 && name != null && name.length > 0) {
				if(roleType == "${roleType}") {
					AUIGrid.addRow(${roleType}_line_myGridID, line, "last");	
				}
			}
		}
	}
	
});

//AUIGrid 생성 후 반환 ID
var ${roleType}_line_myGridID;

//AUIGrid 칼럼 설정
var ${roleType}_line_columnLayout = [
	{ dataField : "departmentName",		headerText : "${e3ps:getMessage('부서')}",			width:"33%",	},
	{ dataField : "name",				headerText : "${e3ps:getMessage('이름')}",			width:"33%",	},
	{ dataField : "id",					headerText : "ID",									width:"34%",	},
	/*
	{ dataField : "duty",				headerText : "${e3ps:getMessage('직급')}",			width:"*",	},
	*/
];

//AUIGrid 를 생성합니다.
function ${roleType}_line_createAUIGrid(${roleType}_line_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		showAutoNoDataMessage : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		// 드래깅 행 이동 가능 여부 (기본값 : false)
		enableDrag : true,
		// 다수의 행을 한번에 이동 가능 여부(기본값 : true)
		enableMultipleDrag : false,
		// 셀에서 바로  드래깅 해 이동 가능 여부 (기본값 : false) - enableDrag=true 설정이 선행 
		enableDragByCellDrag : true,
		// 드랍 가능 여부 (기본값 : true)
		// false 설정했다는 의미는 이 그리드는 드래깅만 가능하고, 드랍은 절대 받지 않는다는 뜻임.
		enableDrop : true,
		// 드랍을 받아줄 그리드가 다른 그리드에도 있는지 여부 (기본값 : false)
		// 즉, 드리드 간의 행 이동인지 여부
		dropToOthers : true
	};

	// 실제로 #grid_wrap 에 그리드 생성
	${roleType}_line_myGridID = AUIGrid.create("#${roleType}_line_grid_wrap", ${roleType}_line_columnLayout, gridPros);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(${roleType}_line_myGridID, "cellClick", ${roleType}_line_auiGridCellClickHandler);
	
	var gridData = new Array();
	AUIGrid.setGridData(${roleType}_line_myGridID, gridData);
}

//셀 클릭 핸들러
function ${roleType}_line_auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
	var oid = event.item.oid;
	
}
</script>
<h4>${title}</h4>
<div class="list mb10 approvalLineGrid" id="${roleType}_line_grid_wrap" data-roletype="${roleType}" style="height:150px">
</div>