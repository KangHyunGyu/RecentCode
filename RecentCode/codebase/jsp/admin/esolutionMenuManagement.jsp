<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
.aui-grid-user-custom {
		text-align: center;
	}

	.aui-grid-user-custom .aui-checkLabelBox {
		margin-left: 10px;
		text-align: left;
	}

	.my-active-style {
		background: #ddedde;
		font-weight: bold;
	}

	.my-inactive-style {
		background: #efcefc;
	}
</style>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", (evt) => {
	//grid setting
	createAUIGrid(columnLayout);
	
	getGridData();
	
});

//AUIGrid 생성 후 반환 ID
var tree_myGridID;

// AUIGrid 칼럼 설정
var columnLayout = [
{
	dataField: "code",
	headerText: "${e3ps:getMessage('코드')}",
	width: "10%",
	editable: false
}, 
{
	dataField: "name",
	headerText: "${e3ps:getMessage('이름')}",
	width: "10%"
}, 
{
	dataField: "name_en",
	headerText: "${e3ps:getMessage('영문 이름')}",
	width: "10%"
},
{
	dataField: "href",
	headerText: "${e3ps:getMessage('href')}",
	style: "AUIGrid_Left",
	width: "*%"
},
{
	dataField: "imgsrc",
	headerText: "${e3ps:getMessage('이미지 소스')}",
	style: "AUIGrid_Left",
	width: "*%"
},
{
	dataField: "menuLevel",
	headerText: "${e3ps:getMessage('레벨')}",
	width: "10%",
	editable: false
},
{
	dataField: "sort",
	headerText: "${e3ps:getMessage('소트')}",
	width: "10%",
	dataType: "numeric"
},
{
	dataField: "active_yn",
	headerText: "${e3ps:getMessage('활성화 여부')}",
	width: "10%",
	styleFunction: function (rowIndex, columnIndex, value, headerText, item, dataField) {
		if (!value) {
			return "my-inactive-style";
		} else if (value) {
			return "my-active-style";
		}
		return "";
	},
	renderer: {
		type: "CheckBoxEditRenderer",
		showLabel: false, // 참, 거짓 텍스트 출력여부( 기본값 false )
		editable: true, // 체크박스 편집 활성화 여부(기본값 : false)
		checkValue: true, // true, false 인 경우가 기본
		unCheckValue: false,
		//사용자가 체크 상태를 변경하고자 할 때 변경을 허락할지 여부를 지정할 수 있는 함수 입니다.
		checkableFunction: function (rowIndex, columnIndex, value, isChecked, item, dataField) {
			
			return true;
		}
	}
}
];


// AUIGrid 를 생성합니다.
function createAUIGrid(columnLayout) {

	var auiGridProps = {

		rowIdField : "_$uid",
		treeIdField : "code",
		treeIdRefField : "parentCode",
		editable: true,
		showSelectionBorder : true,
		showStateColumn: true,
		enableRightDownFocus : true, 
		// 칼럼 끝에서 오른쪽 이동 시 다음 행, 처음 칼럼으로 이동할지 여부
		wrapSelectionMove: true,

		selectionMode: "singleRow",
		softRemoveRowMode:false,
		// 사용자가 추가한 새행은 softRemoveRowMode 적용 안함. 
		// 즉, 바로 삭제함.
		softRemovePolicy: "exceptNew"
	};

	// 실제로 #grid_wrap 에 그리드 생성
	tree_myGridID = AUIGrid.create("#grid_wrap", columnLayout, auiGridProps);
	//에디팅 정상 종료 이벤트 바인딩
    AUIGrid.bind(tree_myGridID, "cellEditEnd", auiCellEditingHandler);
}

function save(){
	let editRowItems = AUIGrid.getEditedRowItems(tree_myGridID);
	let addRowItems = AUIGrid.getAddedRowItems(tree_myGridID);
	if(editRowItems.length == 0 && addRowItems.length== 0){
		alert("${e3ps:getMessage('저장할 데이터가 없습니다')}")
		return;
	}else if(!confirm("${e3ps:getMessage('저장하시겠습니까?')}")){
		return;
	}
	
	
	let param = new Object(); 
	param.editRowItems = editRowItems;
	param.addRowItems = addRowItems;
	
	var url	= getURLString("/admin/createEsolutionMenuAction");
	ajaxCallServer(url, param, function(){
		getGridData();
	}, true);
	
}

function getGridData(){
	var url	= getURLString("/admin/getEsolutionMenu");
	var param = new Object();
	param.disabled = true;
	var data = ajaxCallServer(url, param, null);
	let gridData = data.list;
	AUIGrid.setGridData(tree_myGridID, data.list);
}

//편집 핸들러
function auiCellEditingHandler(event) {
  var dataField = event.dataField;

  if (dataField == "active_yn") {
	  event.item.disabled = !event.item.disabled;
	  AUIGrid.updateRowsById(tree_myGridID, event.item); 
  }else if(dataField == "sort"){
	  if(typeof event.item.sort != 'number'){
		  event.item.sort = '';
		  AUIGrid.updateRowsById(tree_myGridID, event.item); 
	  }
  }
}

function addTreeRow() {
	var selectedItems = AUIGrid.getSelectedItems(tree_myGridID);
	
	if (selectedItems.length <= 0) {
		alert("${e3ps:getMessage('행을 선택해 주세요')}")
		return;
	}else if(selectedItems[0].item.menuLevel > 2){
		alert("${e3ps:getMessage('3레벨 아래 메뉴는 생성이 불가능합니다')}")
		return;
	}else if(!selectedItems[0].item.oid){
		alert("${e3ps:getMessage('메뉴를 저장해주세요')}")
		return;
	}

	var selItem = selectedItems[0].item;
	var parentRowId = selItem._$uid; // 선택행의 자식으로 행 추가
	//console.log('selItem',selItem)
	var newItem = new Object();
	newItem.parentCode = selItem.code; // 부모의 rowId 값을 보관해 놓음...나중에 개발자가 유용하게 쓰기 위함...실제 그리드는 사용하지 않음.
	newItem.menuLevel = selItem.menuLevel + 1;
	newItem.sort = selItem.children?selItem.children.length + 1:1;
	newItem.active_yn = true;
	newItem.disabled = false;
	newItem.code = newItem.parentCode + '-' + newItem.sort;
	// parameter
	// item : 삽입하고자 하는 아이템 Object 또는 배열(배열인 경우 다수가 삽입됨)
	// rowId : 삽입되는 행의 부모 rowId 값
	// rowPos : first : 상단, last : 하단, selectionUp : 선택된 곳 위, selectionDown : 선택된 곳 아래
	AUIGrid.addTreeRow(tree_myGridID, newItem, parentRowId, "last");
	//console.log(AUIGrid.getGridData(tree_myGridID))
}

function removeTreeRow() {
	var selectedItems = AUIGrid.getSelectedItems(tree_myGridID);
	
	if (selectedItems.length <= 0) {
		alert("${e3ps:getMessage('행을 선택해 주세요')}")
		return;
	}else if(!confirm("${e3ps:getMessage('삭제하시겠습니까')}?")){
		return;
	}
	let removeTarget = selectedItems.find( x =>  x.item.oid)
	console.log('removeTarget',removeTarget)
	let param = new Object(); 
	
	param.oid = removeTarget.item.oid;
	
	var url	= getURLString("/admin/deleteEsolutionMenuAction");
	ajaxCallServer(url, param, function(){
		AUIGrid.removeRow(tree_myGridID, "selectedIndex");
	}, true);
	

}

var isExpanded = false;
function expand() {
	if (!isExpanded) {
		AUIGrid.expandAll(tree_myGridID);
		isExpanded = true;
	} else {
		AUIGrid.collapseAll(tree_myGridID);
		isExpanded = false;
	}
}
</script>
<div class="product">
	<!-- button -->
	<input type="hidden" id="authGroupId" name="authGroupId" value=""/>
	<div class="seach_arm pt15 pb5">
		<div class="leftbt">
			<button type="button" class="s_bt03" onclick="expand();">${e3ps:getMessage('모두 열기/닫기')}</button>
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" onclick="save();">${e3ps:getMessage('저장')}</button>
			<button type="button" class="s_bt03" onclick="addTreeRow();">${e3ps:getMessage('선택 행 자식 추가')}</button>
			<button type="button" class="s_bt03" onclick="removeTreeRow();">${e3ps:getMessage('행 삭제')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="semi_content pl30 pr30">
		<div class="semi_content2">
			<!-- //button -->
			<!-- 			<div class="list" id="grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
			<div class="list" id="grid_wrap" style="height: 594px; border-top: 2px solid #74AF2A;"></div>
		</div>
	</div>
</div>