<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid reset
	AUIGrid.destroy(epmOfPurchasePartList_myGridID);
	
	//grid setting
	epmOfPurchasePartList_createAUIGrid(epmOfPurchasePartList_columnLayout);
	
	//get grid data
	epmOfPurchasePartList_getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var epmOfPurchasePartList_myGridID;

//AUIGrid 칼럼 설정
var epmOfPurchasePartList_columnLayout =[
		{ dataField : "pIcon",		headerText : "",		width:"30",  
			cellMerge: true,
			//병합 참조를 childPartNumber 로 지정하여 같은 품목번호이면 병합되도록 걸어준다. 병합정책 restrict 필수
			mergeRef: "partNumber", 
			mergePolicy: "restrict",
			renderer : { 
				type : "TemplateRenderer"
			},	
		},
		{ dataField : "partNumber",		headerText : "${e3ps:getMessage('품목 번호')}",			width:"10%", 
			cellMerge: true,
			filter : {
				showIcon : true,
				iconWidth:30
			},
			renderer : {
				type : "LinkRenderer",
				baseUrl : "javascript", 
				jsCallback : function(rowIndex, columnIndex, value, item) {
					var oid = item.pOid;
					openView(oid);
				}
			}
			
		},
		{ dataField : "partName",		headerText : "${e3ps:getMessage('품목 명')}",	style:"AUIGrid_Left",		width:"*%",	sortValue : "master>name",  
			cellMerge: true,
			mergeRef: "partNumber", 
			mergePolicy: "restrict",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "partVersion",		headerText : "${e3ps:getMessage('품목 버전')}",			width:"7%", 
			cellMerge: true,
			mergeRef: "partNumber", 
			mergePolicy: "restrict",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "partState",		headerText : "${e3ps:getMessage('상태')}",			width:"7%", 
			cellMerge: true,
			mergeRef: "partNumber", 
			mergePolicy: "restrict",
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "disIcon",		headerText : "",			width:"30", 
			renderer : { 
				type : "TemplateRenderer"
			},	
		},
		{ dataField : "disNumber",		headerText : "${e3ps:getMessage('배포 문서 번호')}",			width:"10%", 
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
		{ dataField : "disVersion",		headerText : "${e3ps:getMessage('버전')}",			width:"7%", 
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		},
		{ dataField : "disState",		headerText : "${e3ps:getMessage('상태')}",			width:"7%", 
			filter : {
				showIcon : true,
				iconWidth:30
			}	
		}
		]
		
/* var isMerged = true; // 최초에는 merged 상태

function setCellMerge() {
	isMerged = !isMerged;

	AUIGrid.setCellMerge(myGridID, isMerged);
}	 */	

//AUIGrid 를 생성합니다.
function epmOfPurchasePartList_createAUIGrid(epmOfPurchasePartList_columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
			selectionMode : "multipleCells",
			
			showSelectionBorder : true,
			
			showAutoNoDataMessage : false,
			
			showRowNumColumn : true,
			
			showEditedCellMarker : false,
			
			wrapSelectionMove : true,
			
			enableFilter : false,
			
			editable : false,
			
			enableSorting : false,
			
			enableMovingColumn : false,
			
			noDataMessage : gridNoDataMessage,
			
			autoGridHeight : false,
			//병합 사용 Prop 필수
			enableCellMerge: true,
			//셀 병합 정책 : withNull - null 도 하나의 값으로 간주하여 다수의 null 을 병합된 하나의 공백으로 출력시킴
			cellMergePolicy: "withNull",
			
			height : 600,
	};
	

	// 실제로 #grid_wrap 에 그리드 생성
	epmOfPurchasePartList_myGridID = AUIGrid.create("#epmOfPurchasePartList_grid_wrap", epmOfPurchasePartList_columnLayout, gridPros);
	
	
	var gridData = new Array();
	AUIGrid.setGridData(epmOfPurchasePartList_myGridID, gridData);
}

function epmOfPurchasePartList_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	AUIGrid.showAjaxLoader(epmOfPurchasePartList_myGridID);
	var url = getURLString("/purchase/getPurchasePartDistributeList");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		console.log('gridData',gridData);
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(epmOfPurchasePartList_myGridID, gridData);

		AUIGrid.setAllCheckedRows(epmOfPurchasePartList_myGridID, false);
		AUIGrid.removeAjaxLoader(epmOfPurchasePartList_myGridID);
		
	});
}


function epmOfPurchasePartList_xlsxExport() {
	AUIGrid.setProperty(epmOfPurchasePartList_myGridID, "exportURL", getURLString("/common/xlsxExport"));
	
	 // 엑셀 내보내기 속성
	  var exportProps = {
			 postToServer : true,
			 exceptColumnFields : ["icon", "epmIcon"],
	  };
	  // 내보내기 실행
	  AUIGrid.exportToXlsx(epmOfPurchasePartList_myGridID, exportProps);
}

function epmDown(){
	var url = getURLString("/purchase/purchasePartListEpmDown?oid=" + "${oid}");
	openPopup(url,"epmDown", 1124, 600);
}

function downloadCadFiles(){
	
	var gridDataList = AUIGrid.getGridData(epmOfPurchasePartList_myGridID);
	
	var oidListToString = "";
	console.log('gridDataList no filter ',gridDataList);
	
	gridDataList = gridDataList.filter((x) => { return x.childEPMOid != null && x.epmCadName != null; });
	
	console.log('gridDataList filter ',gridDataList);
	
	if( gridDataList.length > 0){
		var dwgOidList = [];
		var pdfOidList = [];
		var allList = [];
		gridDataList.forEach((i) => {
			if(i.dwgAppOid != null) {
				dwgOidList.push(i.dwgAppOid);
			}
			
			if(i.pdfAppOid != null) {
				pdfOidList.push(i.pdfAppOid);
			}
		});
		
		allList = [...allList,...dwgOidList,...pdfOidList];
		oidListToString = allList.join(',');
		console.log('oidListToString',oidListToString);
	}else{
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
	oidInput.setAttribute("value", oidListToString);
	form.appendChild(oidInput);

	document.body.appendChild(form);
	form.submit();
	startProgress();
	
	$("#progressSpan").show();
	
	var downloadTimer = setInterval(function() {
		var token = getCookie("fileDownload");
        if(token == "TRUE") {
        	endProgress();
        	clearInterval(downloadTimer);
        }
	}, 1000);
	document.body.removeChild(form); 
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt">
		<h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('구매요청 품목')}</h4>
	</div>
	<div class="rightbt">
		<button type="button" class="i_create" style="width: 70px" onclick="javascript:epmDown();">${e3ps:getMessage('도면 다운로드')}</button>
		<button type="button" class="s_bt03" onclick="epmOfPurchasePartList_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="epmOfPurchasePartList_grid_wrap" style="height:500px">
</div>