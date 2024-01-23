<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>

.SumoSelect {
    width: 58%;
}
</style>
<script>
$(document).ready(function(){
	
	//grid setting
	createAUIGrid(columnLayout);

	//get grid data
	getGridData();
	
	
});

//AUIGrid 생성 후 반환 ID
var myGridID;

//현재 페이지
var page = 1;

//row 로딩 개수
var rows = 20;

// 중복 요청을 피하기 위한 플래그
var nowRequesting = false;

//마지막 페이지 여부
var isLastPage = false;

//소트 값
var sortValue = "";

//소트 값으로 소팅되었는지 체크하는 값
var sortCheck = true;

//AUIGrid 칼럼 설정
var columnLayout = [
	{ dataField : "disIcon",		headerText : "",			width:"30", 
		cellMerge: true,
		mergeRef: "disNumber", 
		mergePolicy: "restrict",
		renderer : { 
			type : "TemplateRenderer"
		},	
	},
	{ dataField : "disNumber",		headerText : "${e3ps:getMessage('배포 문서 번호')}",			width:"15%", 
		cellMerge: true,
		filter : {
			showIcon : true,
			iconWidth:30
		},
		renderer : {
			type : "LinkRenderer",
			baseUrl : "javascript",
			jsCallback : function(rowIndex, columnIndex, value, item) {
				var oid = item.disOid;
				openView(oid);
			}
		}
	},
	{ dataField : "disName",		headerText : "${e3ps:getMessage('배포 문서 명')}",			width:"*%", style:"AUIGrid_Left",
		cellMerge: true,
		mergeRef: "disNumber", 
		mergePolicy: "restrict",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "disVersion",		headerText : "${e3ps:getMessage('버전')}",			width:"7%", 
		cellMerge: true,
		mergeRef: "disNumber", 
		mergePolicy: "restrict",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "disState",		headerText : "${e3ps:getMessage('상태')}",			width:"7%", 
		cellMerge: true,
		mergeRef: "disNumber", 
		mergePolicy: "restrict",
		filter : {
			showIcon : true,
			iconWidth:30
		}	
	},
	{ dataField : "disFileName",		headerText : "${e3ps:getMessage('파일 명')}",			width:"*%", style:"AUIGrid_Left",	
		filter : {
			showIcon : true,
			iconWidth:30
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
		
		showAutoNoDataMessage : false,
		
		rowIdField : "_$uid",
		
		showRowNumColumn : true,
		
		showEditedCellMarker : false,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : true,
		
		enableFilter : false,
		
		enableMovingColumn : true,
		
		autoGridHeight : false,
		//병합 사용 Prop 필수
		enableCellMerge: true,
		//셀 병합 정책 : withNull - null 도 하나의 값으로 간주하여 다수의 null 을 병합된 하나의 공백으로 출력시킴
		cellMergePolicy: "withNull",
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight
		
		
	};

	// 실제로 #grid_wrap 에 그리드 생성
	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(myGridID, gridData);
	
	
}
//var selectedItems = AUIGrid.getSelectedItems(myGridID);
function getGridData(){
	
	let param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(myGridID);
	let url = getURLString("/purchase/getPurchasePartDistributeList");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		let gridData = data.list;
		console.log('gridData',gridData);
		
		/* let newGridData = [];
		
		let item;
		
		gridData.forEach((it) => {
			if(it.epm){
				if(it.pdfAppOid != null){
					item = it;
					item.disFileName = it.pdfAppFileName;
					newGridData.push(item);
				}
				
				if(it.dwgAppOid != null){
					item = it;
					item.disFileName = it.dwgAppFileName;
					newGridData.push(item);
				}
			}else{
				item = it;
				item.disFileName = it.docAppFileName;
				newGridData.push(item);
			}
		}) */
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(myGridID, gridData);

		AUIGrid.setAllCheckedRows(myGridID, false);
		AUIGrid.removeAjaxLoader(myGridID);
		
	});
}


//에셀 다운로드
function xlsxExport() {
	
	//필드  숨기 처리 및  보이기
	
	
	AUIGrid.setProperty(myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
			
	  };
	  // 내보내기 실행
	AUIGrid.exportToXlsx(myGridID, exportProps);
	
	
}

function  epmDownAction(){
	
	var checkedItems = AUIGrid.getCheckedRowItems(myGridID);
	console.log("checkedItems", checkedItems);
	
	var chkArr = new Array();
	
	for(var i = 0 ; i < checkedItems.length ;  i++){
		var checkedItem = checkedItems[i].item;
		chkArr.push(checkedItem.disOid);
	}
	/* 
	chkArr = chkArr.filter((element, index) => {
	    return chkArr.indexOf(element) === index;
	}); */

	if(chkArr.length === 0){
		openNotice("${e3ps:getMessage('다운로드할 파일이 없습니다..')}");
		return;
	}
	
	var url = getURLString("/purchase/purchasePartListEpmDownAction");
	
	var form = document.createElement("form");
	form.setAttribute("method", "post");
	form.setAttribute("action", url);

	var oidInput = document.createElement("input");

	oidInput.setAttribute("type", "hidden");
	oidInput.setAttribute("name", "fileList");
	oidInput.setAttribute("value", chkArr.join(','));
	form.appendChild(oidInput);

	document.body.appendChild(form);
	form.submit();
	//startProgress();
	
	$("#progressSpan").show();
	
	var downloadTimer = setInterval(function() {
		var token = getCookie("fileDownload");
        if(token == "TRUE") {
        	//endProgress();
        	clearInterval(downloadTimer);
        }
	}, 1000);
	document.body.removeChild(form);
}



</script>
<div class="pop">
	<!-- button -->
	<div class="seach_arm pt15 pb5">
		
	</div>
	<!-- //button -->
	<!-- pro_table -->
	
	<!-- //pro_table -->
	<!-- button -->
	<div class="seach_arm pb5">
		<div class="leftbt">
			<span>
				
			</span>
		</div>
		<div class="rightbt">
			<button type="button" class="i_read" style="width:80px" onclick="epmDownAction();">${e3ps:getMessage('다운로드')}</button>
			<button type="button" class="i_delete" style="width:80px" onclick="self.close();">${e3ps:getMessage('취소')}</button>
			
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:500px"></div>
	</div>
	<!-- //table list-->
</div>
