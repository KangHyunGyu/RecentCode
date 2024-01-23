<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree" style="height:100%">	
	<input type="hidden" id="parentCode" name="parentCode"/>
	<input type="hidden" id="parentOid" name="parentOid"/>
	<div class="grid_tree" id="grid_tree" style="height:${gridHeight}px">
	</div>
</div>
<script>
var grid_tree = webix.ui({
	  container : "grid_tree",
	  view:"tree",
	  id:"grid_tree",
	  template:"{common.icon()} {common.folder()} <span>#name#</span>",
	  select:true,
	  on: {
	       onDataRequest: CodeTypeTree_loadChildren,
	       onItemClick: CodeTypeTree_itemClick,
	  }  
});
$(document).ready(function(){
	webix.extend($$("grid_tree"), webix.ProgressBar);
	
});
//코드 타입 트리 불러오기
function getCodeTypeTree(){
	var param = new Object();
	var codeType = $("#codeType option:selected").val();
	param["codeType"] = codeType;
	
	var endLevel = 0;
	
	if(codeType == "CUSTOMER" || "PARTCODE") {
		endLevel = 2;
	} else if(codeType == "PROCESSDIVISIONCODE" || codeType == "PROCESSDIVISIONOLD") {
		endLevel = 3;
	}
	 
	param["endLevel"] = endLevel;
	
	$$("grid_tree").clearAll();
	$$("grid_tree").showProgress();
	var url = getURLString("/admin/getSgCodeTypeTree");
	ajaxCallServer(url, param, function(data){
		var tree_gridData = data.list;
		console.log(data.list);
		for(var i=0; i < tree_gridData.length; i++) {
			if(tree_gridData[i].parentOid == tree_gridData[0].oid) {
				tree_gridData[0].webix_kids = true;
			}
		}
		$$("grid_tree").parse(tree_gridData[0]);
		$$("grid_tree").open(tree_gridData[0].id);
		$$("grid_tree").hideProgress();
	});
}

function CodeTypeTree_itemClick(id, event, node){
	let item = $$("grid_tree").getItem(id);
	var code = item.code;
	var oid = item.oid;
	$("#parentCode").val(code);
	$("#parentOid").val(oid);
	getGridData();
}

function CodeTypeTree_loadChildren(id){
	let item = $$("grid_tree").getItem(id);
	var oid = item.oid;
	var codeType = $("#codeType option:selected").val();
	var param = new Object();
	param["codeType"] = codeType;
	param["code"] = item.code;
	if(oid == codeType){
		param["parentOid"] = "0";
	}else{
		param["parentOid"] = oid;
	}
	var url = getURLString("/admin/getCodeTypeChildrenList");
	$$("grid_tree").showProgress();
	ajaxCallServer(url, param, function(data){
		// 트리 데이터
		var grid_tree_Data = data.list;
		
		grid_tree_Data = grid_tree_Data.map(function(child) {
			if(child.children !== null) {
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
		$$("grid_tree").hideProgress();
	});
}
</script>