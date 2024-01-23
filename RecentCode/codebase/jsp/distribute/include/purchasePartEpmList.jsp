<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//grid setting
	purchasePart_epmList_createAUIGrid(purchasePart_epmList_columnLayout);
	
});

//AUIGrid 생성 후 반환 ID
var purchasePart_epmList_myGridID;

//AUIGrid 칼럼 설정
var purchasePart_epmList_columnLayout = [
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
	];


function purchasePart_epmList_createAUIGrid(purchasePart_epmList_columnLayout) {
	
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
		
			height : ${gridHeight},
	};

	// 실제로 #grid_wrap 에 그리드 생성 
	purchasePart_epmList_myGridID = AUIGrid.create("#purchasePart_epmList_grid_wrap", purchasePart_epmList_columnLayout, gridPros);
	
	var gridData = new Array();
	AUIGrid.setGridData(purchasePart_epmList_myGridID, gridData);
	
}

//추가 버튼
function purchasePart_epmList_addRow() {
	let purchaseParts = AUIGrid.getGridData(add_purchaseRequestParts_myGridID);
	console.log('purchaseParts',purchaseParts)
	let purchaseParts_oids = [];
	
	purchaseParts.forEach((item) => {
		if( typeof item.childPartOid != 'undefined'){
			purchaseParts_oids = [...purchaseParts_oids, item.childPartOid];
		}
	})
	
	if(purchaseParts_oids.length == 0){
		AUIGrid.setGridData(purchasePart_epmList_myGridID, []);
		return;
	}
	
	var param = new Object();
	param.oids = purchaseParts_oids;
	
	AUIGrid.showAjaxLoader(purchasePart_epmList_myGridID);
	var url = getURLString("/purchase/getPurchasePartDistributeListPreview");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		console.log('gridData',gridData);
		
		// 그리드에 데이터 세팅
		AUIGrid.setGridData(purchasePart_epmList_myGridID, gridData);

		AUIGrid.setAllCheckedRows(purchasePart_epmList_myGridID, false);
		AUIGrid.removeAjaxLoader(purchasePart_epmList_myGridID);
		
	});
		
		
}






</script>
<!-- button -->
<br/>
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<span class="title">
			<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			${title}
		</span>
	</div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<div class="list" id="purchasePart_epmList_grid_wrap" style="height:${gridHeight};margin-bottom:200px;">
</div>