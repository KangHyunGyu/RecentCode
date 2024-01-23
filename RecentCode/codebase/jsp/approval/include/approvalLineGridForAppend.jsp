<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
webix.ready(function(){
	
	if("${roleType}" == "APPROVE" ){
		var ${roleType}_line_List = webix.ui({
			view:"datatable",
			container : "${roleType}_line_grid_wrap",
			id:"${roleType}_line_grid_wrap",
			height:250,
			columns:[
				{ id:"index",   header:"No.",     width:40,	tooltip:false,
					css: "custom_webix_center",		
				},
				{ id:"departmentName", header:"${e3ps:getMessage('부서')}" , width:140,
					css: "custom_webix_center",	
				},
				{ id:"name", header:"${e3ps:getMessage('이름')}" , minWidth:80, fillspace:true,
					css: "custom_webix_center",	
				},
				{ id:"userId", header:"ID", width:110,
					css: "custom_webix_center",	
				},
				{ id:"duty", header:"${e3ps:getMessage('직급')}", width:110,
					css: "custom_webix_center",	
				}
			],
			hover:"webix_datatable_hover",
			drag:true, 
			scroll:"y",
			on: {
				onItemClick: ${roleType}_line_itemClick,
				"data->onStoreUpdated":function(){
		            this.data.each(function(obj, i){
		                obj.index = i+1;
		            });
		        },
		        
		        onBeforeDrop : ${roleType}_BeforeDrop_Handler
			}
		});
	}else{
		var ${roleType}_line_List = webix.ui({
			view:"dataview",
			container : "${roleType}_line_grid_wrap",
			id:"${roleType}_line_grid_wrap",
			template:"<div style='text-align: center;'><b>#name#</b> #duty#<br>#departmentName#</div>",
			hover:"webix_datatable_hover",
			drag:true, 
			height:513,
			on: {
				onItemClick: ${roleType}_line_itemClick,
				"data->onStoreUpdated":function(){
		            this.data.each(function(obj, i){
		                obj.index = i+1;
		            });
		        },
		        
		        onBeforeDrop : ${roleType}_BeforeDrop_Handler
			}
		});
	}
	
	
	if(opener.window.app_line_sendAppLineToPopup) {
		var approvalLineList = opener.window.app_line_sendAppLineToPopup();
		for(var i = 0; i < approvalLineList.length; i++){
			var line = approvalLineList[i];
			
			var roleType = line.roleType;
			var name = line.name;
			
			if(roleType != null && roleType.length > 0 && name != null && name.length > 0) {
				if(roleType == "${roleType}") {
					var item = new Object();
					
					item["name"] = name;
					$$("${roleType}_line_grid_wrap").add(line);
				}
			}
		}
	}
	
	${roleType}_getGridData();
	
});

//셀 클릭 핸들러
function ${roleType}_line_itemClick(oid, event) {
	
	let isExLine = false;
	
	Array.prototype.forEach.call(exReceiveLine, function(item, idx){
		
		if(item.id == oid){
			isExLine = true;
		}
	});
	
	
	if(!isExLine){
		$$("${roleType}_line_grid_wrap").remove(oid);
	}else{
		alert("저장된 라인은 삭제할 수 없습니다.");
	}
}

let exReceiveLine = [];
function ${roleType}_getGridData(){
	
	var param = new Object();
	
	param["oid"] = "${appLineOid}";
	param["searchRoleType"] = "RECEIVE";
	param["type"] = "view";
	
	//$$("app_rec_line_view_grid_wrap").clearAll();
	var url = getURLString("/approval/getApprovalLine");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		for(let i = 0; i < gridData.length; i++){
			gridData[i].isExReceiveLine = true;
		}
		
		exReceiveLine = gridData;
		
		// 그리드에 데이터 세팅
		$$("${roleType}_line_grid_wrap").parse(gridData);
	});
	
	
}

function ${roleType}_BeforeDrop_Handler(context, native_event) {
	
	var dnd = webix.DragControl.getContext();
	var item = dnd.from.getItem(dnd.source[0]);
	var itemClassName = item.oid.substring(0, item.oid.indexOf(":"));	
	
	if(itemClassName.indexOf("com.e3ps.org.People") > -1){
		for (var i = 0; i < context.source.length; i++){
	    	item = context.from.getItem(context.source[i]);
	    	$$("${roleType}_line_grid_wrap").parse(item);
	    	$$("${roleType}_line_grid_wrap").move(item.id, context.index);
	    	$$("grid_list").unselectAll();
		}
	}
	
	if(itemClassName.indexOf("com.e3ps.org.Department") > -1){
		var param = new Object();
		param["departmentOid"] = item.oid;
		var url = getURLString("/common/searchUserAction");
		
		ajaxCallServer(url, param, function(data){
			var gridData = data.list;
			console.log(gridData);
			$$("${roleType}_line_grid_wrap").parse(gridData);
		});
		
	}
	
    return false;
	
	
}
</script>
<h4>${title}</h4>
<div class="list mb10 approvalLineGrid" id="${roleType}_line_grid_wrap" data-roletype="${roleType}">
</div>