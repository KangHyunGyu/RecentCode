<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree" style="height:100%">	
	<div class="search">
		<input class="w70" type="text" id="folderName" name="folderName">
		<!-- <div class="w70" id="folderName" name="folderName"></div> -->
		<button type="button" class="s_bt" onclick="javascript:PSO_searchFolderTree()" style="padding: 1px 10px;">${e3ps:getMessage('검색')}</button>			
	</div>
	<input type="hidden" id="parentCode" name="parentCode"/>
	<input type="hidden" id="parentOid" name="parentOid"/>
	<div class="grid_tree" id="grid_tree" style="height:555px">
	</div>
</div>
<script>
webix.ready(function(){
	var grid_tree = webix.ui({
		  container : "grid_tree",
		  view:"tree",
		  id:"grid_tree",
		  template:"{common.icon()} {common.folder()} <span>#name#</span>",
		  select:true,
		  on: {
		       onDataRequest: PSOTree_loadChildren,
		       onItemClick: PSOTree_itemClick,
		  }  
	});
	getPSOTree();
});

$(document).ready(function(){
	//enter key pressed event
	$("#folderName").keypress(function(e){
		if(e.keyCode==13){
			PSO_searchFolderTree();
		}
	});
	
});

//코드 타입 트리 불러오기
function getPSOTree(){
	var param = new Object();
	 
	param["codetype"] = "PSO";
	
	$$("grid_tree").clearAll();
	var url = getURLString("/admin/getPSOTree");
	ajaxCallServer(url, param, function(data){
		
		var tree_gridData = data.list;

		for(var i=0; i < tree_gridData.length; i++) {
			if(tree_gridData[i].parentOid == tree_gridData[0].oid) {
				tree_gridData[0].webix_kids = true;
			}
		}
		$$("grid_tree").parse(tree_gridData[0]);
		$$("grid_tree").open(tree_gridData[0].id);
	});
}

function PSOTree_itemClick(id, event, node){
	let item = $$("grid_tree").getItem(id);
	var code = item.code;
	var oid = item.oid;
	$("#parentCode").val(code);
	$("#parentOid").val(oid);
	getGridData();
}

function PSOTree_loadChildren(id){
	let item = $$("grid_tree").getItem(id);
	var oid = item.oid;
	var codeType = item.codeType;
	var param = new Object();
	param["codeType"] = codeType;
	param["code"] = item.code;
	if(oid == codeType){
		param["parentOid"] = "0";
	}else{
		param["parentOid"] = oid;
	}
	var url = getURLString("/admin/getPSOTreeChildrenList");
	ajaxCallServer(url, param, function(data){
		// 트리 데이터
		var grid_tree_Data = data.list;
		//child.children === null  ::: children 없음
		//child.children !== null(list length 0) ::: children 있음
		
		grid_tree_Data = grid_tree_Data.map(function(child) {
			if(child.children !== null) {
				//folderTree_loadChildren(child.id);
				child.webix_kids = true;
			}
		
			return child;
		});
			
		// 성공 시 완전한 배열 객체로 삽입하십시오.
		const children = {
	    	parent : id,
	   		data : grid_tree_Data
	   	};
		$$("grid_tree").parse(children);
		//$$("folderTree").open(children);
		//$$("folderTree").hideProgress();
		grid_tree_Data = grid_tree_Data.map(function(child) {
			if(child.children !== null) {
				PSOTree_loadChildren(child.id);
			}
		});
	});
}

//폴더 검색
function PSO_searchFolderTree() {
	var nameValue = $("#folderName").val();
	var treeList = $$("grid_tree").serialize(null, true);

	$$("grid_tree").closeAll();
	if(treeList.length > 0){
		$$("grid_tree").open(treeList[0].id);
	}
	$$("grid_tree").filter("#name#",nameValue);
}
</script>