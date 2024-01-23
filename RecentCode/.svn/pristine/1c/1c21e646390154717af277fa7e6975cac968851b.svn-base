<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
	.my-cell-style {
		font-weight:bold;
		color:#cccccc;
		font-style:italic;
	}
	
	.s_bt03{ background:#fff; border:1px solid #1064aa;  color:#1064aa; padding:1px 15px; border-radius: 2px; font-size: 12px;  line-height: 22px;  font-weight:bold;}
</style>
<script type="text/javascript">

$(document).ready(function(){
	
	createAUIGrid(columnLayout);
	
	partCreateNumberProp = getCreateNumberPropList('PARTNUMBERPROP');
	
	addRow();

});

var partCreateNumberProp = {};

var myGridID;

var add_objList = [];

//AUIGrid 칼럼 설정
var columnLayout = [
	
	
	{ dataField : "unit",		headerText : "Unit<span class='required'>*</span>",		width:"8%"	,
		
		renderer : {
	    	type : "DropDownListRenderer",
	    	listAlign : "left",
	    	listFunction : function(rowIndex, columnIndex, item, dataField) {
				var listItems = partCreateNumberProp.UNIT;
				return listItems;
			},
	    	keyField : "code",
	    	valueField : "displayOptionTag"
	    },
		
	},
	{ dataField : "material",		headerText : "Material<span class='required'>*</span>",		width:"8%"	,
		renderer : {
	    	type : "DropDownListRenderer",
	    	listAlign : "left",
	    	listFunction : function(rowIndex, columnIndex, item, dataField) {
				var listItems = partCreateNumberProp.MATERIAL;
				return listItems;
			},
			keyField : "code",
	    	valueField : "displayOptionTag"
	    },
		
	},
	{ dataField : "equipment",		headerText : "Equipment<span class='required'>*</span>",		width:"8%"	,
		renderer : {
	    	type : "DropDownListRenderer",
	    	listAlign : "left",
	    	listFunction : function(rowIndex, columnIndex, item, dataField) {
				var listItems = partCreateNumberProp.EQUIPMENT;
				return listItems;
			},
			keyField : "code",
	    	valueField : "displayOptionTag"
	    },
		
	},
	{ dataField : "product",		headerText : "Product<span class='required'>*</span>",		width:"8%"	,
		renderer : {
	    	type : "DropDownListRenderer",
	    	listAlign : "left",
	    	listFunction : function(rowIndex, columnIndex, item, dataField) {
				var listItems = partCreateNumberProp.PRODUCT;
				return listItems;
			},
			keyField : "code",
	    	valueField : "displayOptionTag"
	    },
		
	},
	{ dataField : "inch",		headerText : "Inch<span class='required'>*</span>",		width:"8%"	,
		renderer : {
	    	type : "DropDownListRenderer",
	    	listAlign : "left",
	    	listFunction : function(rowIndex, columnIndex, item, dataField) {
				var listItems = partCreateNumberProp.INCH;
				return listItems;
			},
			keyField : "code",
	    	valueField : "displayOptionTag"
	    },
		
	},
	{ dataField : "name",		headerText : "${e3ps:getMessage('품목명')}<span class='required'>*</span>",		width:"15%"	, style:"editAble_Blue_Cell AUIGrid_Left",
		
		styleFunction :  function(rowIndex, columnIndex, value, headerText, item, dataField) {
			if(value == "${e3ps:getMessage('40자 이내로 작성해 주세요')}.") {
				return "my-cell-style";
			}
			return null;
		},
		editRenderer : {
			type : "InputEditRenderer",
			
			// 에디팅 유효성 검사
			validator : function(oldValue, newValue, item) {
				var isValid = false;
				if(newValue.length<41){
					isValid = true;
				}
				
				// 리턴값은 Object 이며 validate 의 값이 true 라면 패스, false 라면 message 를 띄움
				return { "validate" : isValid, "message"  : "${e3ps:getMessage('40자 이내로 작성해 주세요')}." };
			}
		}
		
	},
	{ dataField : "location",		headerText : "${e3ps:getMessage('폴더 경로')}",		width:"15%"	, style:"editAble_Blue_Cell",
		editable : false,
		renderer : { // HTML 템플릿 렌더러 사용
			type : "TemplateRenderer"
		},
		labelFunction : function (rowIndex, columnIndex, value, headerText, item ) { // HTML 템플릿 작성
			var template = value;
			var orgLocation = item.orgLocation;
			if(isEmpty(orgLocation)){
				template ="<font color='red'>"+value+"</font>";
			}else{
				if ( (value != item.orgLocation) ){  //O, item.epmDocCheck ==X
					template ="<font color='red'>"+value+"</font>";
				}
			}
						
			
			return template;
		},
		 visible : true,
	},
	{ dataField : "w_description",		headerText : "${e3ps:getMessage('설명')}",		width:"30%"	,
		
	},
	
	
	/* { dataField : "cost",		headerText : "COST",		width:"6%"	,
		dataType : "numeric",
		formatString: "#,##0.00",
		editRenderer : {
			type : "InputEditRenderer",
			onlyNumeric : true, // 0~9만 입력가능
			allowPoint : true, // 소수점( . ) 도 허용할지 여부
			allowNegative : false, // 마이너스 부호(-) 허용 여부
			textAlign : "right", // 오른쪽 정렬로 입력되도록 설정
			autoThousandSeparator : true, // 천단위 구분자 삽입 여부
		}
		
		
	}, */
	
	
	
		
];

//AUIGrid 를 생성합니다.
function createAUIGrid(columnLayout) {
	
	// 그리드 속성 설정
	var gridPros = {
		
		editable : true,
			
		selectionMode : "multipleCells",
		
		rowIdField : "_$uid",

		showStateColumn : false,
		
		showSelectionBorder : true,
		
		noDataMessage : gridNoDataMessage,
		
		showRowNumColumn : true,
		
		showEditedCellMarker : true,
		
		wrapSelectionMove : true,
		
		showRowCheckColumn : true,
		
		headerHeight : gridHeaderHeight,
		
		rowHeight : gridRowHeight,
		
		reverseRowNum : false, //No 정렬  true: desc, false : asc (Default)
	};
	
	

	myGridID = AUIGrid.create("#grid_wrap", columnLayout, gridPros);
	
	//AUI GRID 이벤트 바인딩
	AUIGrid.bind(myGridID, "cellEditBegin", auiEventHandler);
	AUIGrid.bind(myGridID, "cellEditCancel", auiEventHandler);
	AUIGrid.bind(myGridID, "cellEditEnd", auiEventHandler);
	AUIGrid.bind(myGridID, "cellClick", auiEventHandler);
	AUIGrid.bind(myGridID, "selectionChange", auiEventHandler);
	
}
<%----------------------------------------------------------
*                   AUI GRID 이벤트 핸들러
----------------------------------------------------------%>
function auiEventHandler(event){ 
	var dataField = event.dataField;
	var rowItem = event.item;
	switch(event.type){
		case 'selectionChange' :
			// 다수의 선택 셀 중 기본이 되는 주요 셀 정보
		    var primeCell = event.primeCell; // 선택된 대표 셀
		    var multiCells = event.selectedItems ;
		    var ids = [];
		    for( var i = 0 ; i < multiCells.length; i ++ ){
		    	ids[i] = multiCells[i].rowIdValue;
		    }
		    AUIGrid.setCheckedRowsByIds(myGridID, ids);
			break;
		case 'cellEditEnd' :
			break;
		case 'cellClick' :
			break;
		case 'cellEditCancel' :
			break;
		case 'cellEditBegin' :
			break;
		default :
			break;
	}
}

<%----------------------------------------------------------
*                      스타일 변경
----------------------------------------------------------%>
function changeRowStyleFunction(rowIndex,item ) {
	// row Styling 함수를 다른 함수로 변경
	AUIGrid.setProp(myGridID, "rowStyleFunction", function(rowIndex, item) {
		if(item.manual) {
			return "editAble_Blue_Cell";
		}
		return "";
	});
	
	// 변경된 rowStyleFunction 이 적용되도록 그리드 업데이트
	AUIGrid.update(myGridID);
};


/**************************************************************
*           폴더 Tree 에서 선택시 폴더  품목 지정
****************************************************************/
function setPartFolderMenual(event){
	
	var selectedItems = AUIGrid.getSelectedItems(myGridID);
	
	//선택된 행이 없을 시 Return
	if(selectedItems.length <= 0){
		//alert("${e3ps:getMessage('자재유형 ,자재그룹 ')}");
		return;
	}
	
	if(event.type != "cellDoubleClick"){
		return;
	}
	
	var selectItem = selectedItems[0].item;
	
	var folderItem = event.item;
	/*
	var msg = "${e3ps:getMessage('"+selectedItems[index].rowIndex +"의 폴더 경로를 " + folderItem.path + "로 변경하시겠습니까?')}";
	if( !confirm( msg )){
		return;
	}
	*/
	
	//selectItem.targetFolderOid = folderItem.oid;
	selectedItems.forEach(function(elem,index){
		elem.location = folderItem.path;	
		console.log('folderItem.path',folderItem.path)
		AUIGrid.updateRow(myGridID, elem, selectedItems[index].rowIndex);
	});
	//AUIGrid.refreshRows(myGridID, selectItem, "my-flash-style", 200);
}

 <%----------------------------------------------------------
 *                      그리드 Row 추가 
 ----------------------------------------------------------%>
function addRow() {
	let count = document.getElementById('addRowCount').value;
	count = parseInt( count, 10);
	// 그리드의 편집 인푸터가 열린 경우 에디팅 완료 상태로 만듬.
	AUIGrid.forceEditingComplete(myGridID, null);
	var item = new Object();

	item.name = "${e3ps:getMessage('40자 이내로 작성해 주세요')}.";
	item.location = "";
	
	for(let i = 0 ; i < count; i ++){
		AUIGrid.addRow(myGridID, item, "last");
	}
}
<%----------------------------------------------------------
*                      그리드 Row 삭제
----------------------------------------------------------%>
function removeRow() {
	
		var checkItemList = AUIGrid.getCheckedRowItems(myGridID);
		
		for(var i = 0; i < checkItemList.length; i++){
			
			AUIGrid.removeRowByRowId(myGridID, checkItemList[i].item._$uid);
		}
		
		AUIGrid.setAllCheckedRows(myGridID, false); 
	}


<%----------------------------------------------------------
*                      SAVE
----------------------------------------------------------%>
function save() {
	
	//추가된 아이템들
	var addedItemList = AUIGrid.getAddedRowItems(myGridID);
	if(addedItemList.length <= 0){
		alert("${e3ps:getMessage('등록할 리스트가 존재하지 않습니다')}.");
		return false;
	}
	
	
	for(var i = 0; i < addedItemList.length; i++){
		
		const item = addedItemList[i];
		
		let chaebunList = [];
		chaebunList = [...chaebunList, item.unit, item.material, item.product, item.inch, item.equipment, item.customer, item.resistivity];
		
		const chaebunNo = chaebunList.join('');
		item.chaebunNo = chaebunNo;
		AUIGrid.updateRowsById(myGridID, item);
		return;					
		
		if(isEmpty(item.name) || item.name == "${e3ps:getMessage('40자 이내로 작성해 주세요.')}"){
			alert("${e3ps:getMessage('40자 이내로 작성해 주세요.')}");
			return;
		}
		
		if(isEmpty(item.location)){
			alert("${e3ps:getMessage('폴더 경로를 선택해 주세요')}")
			return;
		}
		
	}
	
	if(!confirm("${e3ps:getMessage('등록하시겠습니까?')}")){
		return;
	}
	
	let param = new Object(); 
	var itemList = AUIGrid.getGridData(myGridID);
	param.itemList = itemList;
	console.log('itemList',itemList)
	return;
	//param = JSON.stringify(param);
	
	var url	= getURLString("/part/createPartMultiAction");
	ajaxCallServer(url, param, function(data){}, true);
	
}
</script>
<form name="newMultiPartForm" id="newMultiPartForm" method=post style="padding:0px;margin:0px">
<div class="product">
	<div class="semi_content pl30 pr30">
		<div class="semi_content2">
			<!-- pro_table -->
			<!-- //pro_table -->
			<!-- button -->
			<div class="seach_arm2 pt10 pb5">
				<div class="leftbt">
				<span class="title"><img class="" src="/Windchill/jsp/portal/images/t_icon.png"> ${e3ps:getMessage('품목정보')}</span></div>
				<div class="rightbt">
					<label style="font-size:11px;vertical-align:middle !important;">${e3ps:getMessage('추가할 행의 갯수')} </label>
				 	<select style="width:70px;margin-right:5px;" id="addRowCount">
				 		<option value="1">${e3ps:getMessage('선택')}</option>
						<option value="10">10</option>
						<option value="20">20</option>
						<option value="50">50</option>
						<option value="100">100</option>
					</select>
					<button type="button" class="i_read" style="width:60px;line-height: 26px;" onclick="addRow()">${e3ps:getMessage('추가')}</button>
					<button type="button" class="i_create" style="width:60px;line-height: 26px;" onclick="save()">${e3ps:getMessage('등록')}</button>
					<button type="button" class="i_delete" style="width:60px;line-height: 26px;" onclick="removeRow()">${e3ps:getMessage('삭제')}</button>
				</div>
			</div>
			<!-- //button -->
			<div class="list" id="grid_wrap" style="height:700px; border-top:2px solid #1064aa;"></div>
		</div>
	</div>
	
	<input type="file" id="file" style="visibility:hidden;"></input>
	<input type="file" id="files" style="visibility:hidden;" multiple="multiple"></input>
</div>
</form>