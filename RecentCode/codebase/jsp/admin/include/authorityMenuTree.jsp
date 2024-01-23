<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style type="text/css">
/* .aui-grid-tree-leaf-icon {
    background: url(/Windchill/jsp/component/AUIGrid/images/b_folder2.png) 50% 50% no-repeat;
} */

	/* 트리 아이콘 재정의 */
	
	/* .aui-grid-tree-plus-icon {
		display: inline-block;
		width: 10px;
		height: 10px;
		border: 1px solid #eeeeee;
		background: #ffffff url(/Windchill/jsp/portal/images/icons8-plus-24.png) 50% 50% no-repeat;
		margin: 0 4px 0 0;
	}

	.aui-grid-tree-minus-icon {
		display: inline-block;
		width: 10px;
		height: 10px;
		border: 1px solid #eeeeee;
		background: #ffffff url(/Windchill/jsp/portal/images/icons8-minus-24.png) 50% 50% no-repeat;
		margin: 0 4px 0 0;
	} */

	/* .aui-grid-tree-branch-icon {
		display: inline-block;
		width: 20px;
		height: 20px;
		background: url(/Windchill/jsp/portal/images/icons8-folder-20.png) 50% 50% no-repeat;
		vertical-align: bottom;
		margin: 0 2px 0 0;
	}

	.aui-grid-tree-branch-open-icon {
		display: inline-block;
		width: 20px;
		height: 20px;
		background: url(/Windchill/jsp/portal/images/icons8-opened-folder-20.png) 50% 50% no-repeat;
		vertical-align: bottom;
		margin: 0 2px 0 0;
	} */

	.aui-grid-tree-leaf-icon {
		display: inline-block;
		width: 18px;
		height: 18px;
		background: url(/Windchill/jsp/portal/images/icons8-file-18.png) no-repeat;
		vertical-align: bottom;
		margin: 0 2px 0 4px;
	}
	
</style>
<script>
$(document).ready(function(){
	AUIGrid.destroy(menuTree_myGridID);
	//grid setting
	createMenuTreeAUIGrid(menuTreeColumnLayout);
	
	getMenuTreeGridData();
	
	/* document.querySelectorAll('.aui-grid-tree-plus-icon').forEach((item)=>{
		item.addEventListener('click', treeRe);
	}) */
});

//AUIGrid 생성 후 반환 ID
var menuTree_myGridID;

// AUIGrid 칼럼 설정
var menuTreeColumnLayout = [
{
	dataField: "name",
	headerText: "${e3ps:getMessage('이름')}",
	width: "*%"
}
];

function getMenuTreeGridData(){
	var url	= getURLString("/admin/getEsolutionMenu");
	var param = new Object();
	param.disabled = false;
	var data = ajaxCallServer(url, param, null);
	let gridData = data.list;
	AUIGrid.setGridData(menuTree_myGridID, data.list);
	AUIGrid.setSelectionByIndex(menuTree_myGridID, 0, 0);
	let firstRow = AUIGrid.getItemByRowIndex(menuTree_myGridID,0);
	document.querySelectorAll('.gridTitle').forEach((ele)=>{
		ele.innerText = '[ '+firstRow.displayName+' ]';
	})
	document.getElementById('groupOid').value=firstRow.groupOid;
	getGridData();
}

//AUIGrid 를 생성합니다.
function createMenuTreeAUIGrid(columnLayout) {

	var auiGridProps = {

		rowIdField : "_$uid",
		treeIdField : "code",
		treeIdRefField : "parentCode",
		editable: false,
		showSelectionBorder : false,
		enableRightDownFocus : true, 
		// 칼럼 끝에서 오른쪽 이동 시 다음 행, 처음 칼럼으로 이동할지 여부
		wrapSelectionMove: true,
		showHeader : false,
		selectionMode: "singleRow",
		showRowNumColumn : false,

		// 사용자가 추가한 새행은 softRemoveRowMode 적용 안함. 
		// 즉, 바로 삭제함.
		softRemovePolicy: "exceptNew"
	};

	// 실제로 #grid_wrap 에 그리드 생성
	menuTree_myGridID = AUIGrid.create("#grid_tree", columnLayout, auiGridProps);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(menuTree_myGridID, "cellClick", authTree_EventHandler);
}

//코드 타입 트리 불러오기
function getAuthorityGroupList(code){
	
	var param = new Object();
	param["code"] = code;
	
	var url = getURLString("/admin/getAuthorityGroupList");
	ajaxCallServer(url, param, function(data){
		var tree_gridData = data.list;
		AUIGrid.setGridData(menuTree_myGridID, tree_gridData);
	});
	
   	AUIGrid.resize(menuTree_myGridID);
}

function authTree_EventHandler(evt){
	
	let row = evt.item;
	if(row.groupOid){
		document.querySelectorAll('.gridTitle').forEach((ele)=>{
			ele.innerText = '[ '+row.displayName+' ]';
		})
		
		document.getElementById('groupOid').value=row.groupOid;
		getGridData();
		preRequestData();
	}
}

/* function treeRe(){
	document.querySelectorAll('.aui-grid-tree-column-renderer').forEach((item)=>{
		if(item.style.textIndent && item.style.textIndent != '0px'){
			let tagClassList = item.firstElementChild.classList;
			
			let currentIndent = item.style.textIndent;
			
			currentIndent = currentIndent + '';
			   
		    if (currentIndent.indexOf('px') > -1) {
		    	currentIndent = currentIndent.replace('px', '');
		    }
		   
		    if (currentIndent.indexOf('PX') > -1) {
		    	currentIndent = currentIndent.replace('PX', '');
		    }
		   
		    let result = parseInt(currentIndent, 10);
			
			if(tagClassList.length > 0){
				
				item.style.textIndent = (result-8) + 'px';
				
			}else{
				item.style.textIndent = (result+6) + 'px';
			}
		}
	})
} */

</script>
<div class="tree" style="height:100%;">	
	<input type="hidden" id="groupOid" value="" />
	<div class="grid_tree" id="grid_tree" style="height:100%">
	</div>
</div>