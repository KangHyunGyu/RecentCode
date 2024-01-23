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
	AUIGrid.destroy(folderTree_myGridID);
	
	createFolderTreeAUIGrid(folderTreeColumnLayout);
	
	getFolderTreeGridData();
	
});

//AUIGrid 생성 후 반환 ID
var folderTree_myGridID;

// AUIGrid 칼럼 설정
var folderTreeColumnLayout = [
{
	dataField: "name",
	headerText: "${e3ps:getMessage('이름')}",
	width: "*%"
}
];

function getFolderTreeGridData(){
	var url	= getURLString("/common/getFolderTree");
	var param = new Object();
	param.rootLocation = '/Default/Document';
	var data = ajaxCallServer(url, param, null);
	let gridData = data.list;
	AUIGrid.setGridData(folderTree_myGridID, gridData);
	AUIGrid.expandAll(folderTree_myGridID);
	AUIGrid.setSelectionByIndex(folderTree_myGridID, 1, 0);
	let firstRow = AUIGrid.getItemByRowIndex(folderTree_myGridID,1);
	document.querySelectorAll('.gridTitle').forEach((ele)=>{
		ele.innerText = "[ "+firstRow.name+" - ${e3ps:getMessage('전체 권한')} ]";
	})
	document.getElementById('groupOid').value=firstRow.permissionGroupOid;
	document.getElementById('parentGroupOid').value=firstRow.parentGroupOid;
	getGridData();
}

//AUIGrid 를 생성합니다.
function createFolderTreeAUIGrid(columnLayout) {

	var auiGridProps = {

		rowIdField : "oid",
		treeIdField : "oid",
		treeIdRefField : "parent",
		editable: false,
		flat2tree:true,
	  	hScrollPolicy : "off",
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
	folderTree_myGridID = AUIGrid.create("#grid_folder_tree", columnLayout, auiGridProps);
	
	// 셀 클릭 이벤트 바인딩
	AUIGrid.bind(folderTree_myGridID, "cellClick", authTree_EventHandler);
	
	//AUIGrid.bind(folderTree_myGridID, "selectionChange", authTree_EventHandler);
}


function authTree_EventHandler(evt){
	
	let row = evt.item;
	//if(!row) row = evt.selectedItems[0].item;
	if(!row.parentGroupOid)return;
	let authTypeButtonValue = document.getElementById('authTypeBtn').value;
	if(authTypeButtonValue == 'ALL'){
		if(row.permissionGroupOid){
			document.getElementById('groupOid').value=row.permissionGroupOid;
		}
		document.querySelectorAll('.gridTitle').forEach((ele)=>{
			ele.innerText = "[ "+row.name+" - ${e3ps:getMessage('전체 권한')} ]";
		})
	}else{
		if(row.readGroupOid){
			document.getElementById('groupOid').value=row.readGroupOid;
		}
		document.querySelectorAll('.gridTitle').forEach((ele)=>{
			ele.innerText = "[ "+row.name+" - ${e3ps:getMessage('읽기/다운')} ]";
		})
	}
	
	document.getElementById('parentGroupOid').value=row.parentGroupOid;
	getGridData();
	preRequestData();
	
}

</script>
<div class="tree" style="height:100%;">	
	<input type="hidden" id="groupOid" value="" />
	<input type="hidden" id="parentGroupOid" value="" />
	<div class="grid_folder_tree" id="grid_folder_tree" style="height:100%">
	</div>
</div>