<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//grid reset
	AUIGrid.destroy(user_myGridID);
	
	//grid setting
	user_createAUIGrid(user_columnLayout);

	//get grid data
	user_getGridData();
	
	//팝업 리사이즈
	popupResize();
});

//AUIGrid 생성 후 반환 ID
var user_myGridID;

// 중복 요청을 피하기 위한 플래그
var nowRequesting = false;

//마지막 페이지 여부
var isLastPage = false;

//AUIGrid 칼럼 설정
var user_columnLayout = [
	{ dataField : "id",		headerText : "${e3ps:getMessage('아이디')}",			width:"10%",
		filter : {
			showIcon : true,
			iconWidth:35
		},
		editable : true,
	},
	{ dataField : "name",		headerText : "${e3ps:getMessage('이름')}",		width:"*",	
		filter : {
			showIcon : true,
			iconWidth:35
		}
	},
	{ dataField : "email",		headerText : "${e3ps:getMessage('이메일')}",		width:"30%",	style:"AUIGrid_Left",
		filter : {
			showIcon : true,
			iconWidth:35
		}	
	},
	{ dataField : "c_date",		headerText : "${e3ps:getMessage('생성일')}",		width:"15%",	
		filter : {
			showIcon : true,
			iconWidth:35
		}	
	},
	{ dataField : "enabled",		headerText : "${e3ps:getMessage('활성화 여부')}",			width:"12%",
		filter : {
			showIcon : true,
			iconWidth:30
		}
	},
];

//AUIGrid 를 생성합니다.
function user_createAUIGrid(user_columnLayout) {
	// 그리드 속성 설정
	var user_gridPros = {
		
		selectionMode : "multipleCells",
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		rowIdField : "_$uid",
		
		showRowNumColumn : true,
		
		showEditedCellMarker : true,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : false,
		
		enableFilter : true,
		
		enableMovingColumn : true,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight
		
		
	};
	// 실제로 #grid_wrap 에 그리드 생성
	user_myGridID = AUIGrid.create("#user_grid_wrap", user_columnLayout, user_gridPros);
	
	var user_getGridData = new Array();
	AUIGrid.setGridData(user_myGridID, user_getGridData);
}

//셀 클릭 핸들러
function auiGridCellClickHandler(event) {
	
	var dataField = event.dataField;
}

function user_getGridData(){
	var param = new Object();
	param["supplierCode"] = "${supplierCode}";
	
	AUIGrid.showAjaxLoader(user_myGridID);
	
	var url = getURLString("/distribute/searchSupplierUserListAction");
	
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		console.log(data);

		// 그리드에 데이터 세팅
		AUIGrid.setGridData(user_myGridID, gridData);

		AUIGrid.setAllCheckedRows(user_myGridID, false);
		AUIGrid.removeAjaxLoader(user_myGridID);
	});
		
}
</script>
<div class="user">
	<div class="seach_arm2 pt10 pb5">
		<div class="leftbt">
			<h4>
			<img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			${e3ps:getMessage('사용자')}
			</h4>
		</div>
		<div class="rightbt">
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table list-->
	<div class="pro_table">
		<div class="list" id="user_grid_wrap" style="height:250px"></div>
	</div>
	<!-- //pro_table list-->
</div>
