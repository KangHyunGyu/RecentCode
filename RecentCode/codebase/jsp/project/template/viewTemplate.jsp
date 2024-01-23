<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title">${e3ps:getMessage('템플릿')}</span>
		</div>
	</div>
	<div>
		<div class="semi_content pl30 pr30">
			<div class="semi_table1 pr20">
				<!-- 트리 영역 -->
				<div class="tree">	
					<div class="tree_top">
						<div class="tree_t01 pt5">
							<p><img class="pointer" onclick="viewAllOutput()" src="/Windchill/jsp/project/images/img/bt_14.png"></p>
							<p><img class="pointer" onclick="reloadTree()" src="/Windchill/jsp/project/images/img/bt_16.png"></p>
							<p><img class="pointer" onclick="moveUpTask()" src="/Windchill/jsp/project/images/img/up.png"></p>
							<p><img class="pointer" onclick="moveDownTask()" src="/Windchill/jsp/project/images/img/down.png"></p>			
		   				</div>
					</div>
					<input type="hidden" id="templateOid" name="templateOid" value="${oid}">
					<div class="tempTree" id="tempTree">
					</div>
				</div>
			</div>
			<div class="semi_content2" id="includePage">
			</div>
		</div>
	</div>
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	
	var tempTree = webix.ui({
		  view:"tree",
		  container : "tempTree",
	      id:"tempTree",
	      height:650,
	      template:"{common.icon()} {common.folder()} <span>#name#</span>",
	      select:true,
	      on: {
	    	  onItemClick: tempTree_itemClick,
	    	  onBeforeContextMenu: tempTree_beforeContextMenu,
		  }
	});
	
	webix.extend($$("tempTree"), webix.ProgressBar);
	
	getTemplateTree();
	
	webix.ui({
		view:"contextmenu",
		id:"tempTreeMenu",
		width:140,
		css: "custom_webix_contextMenu",
		data:[
			{
				id : "addChildTask",
				value : "${e3ps:getMessage('하위 추가')}",
			},
			{
				id : "addNextTask",
				value : "${e3ps:getMessage('태스크 추가(다음)')}",
			},
			{
				id : "deleteTask",
				value : "${e3ps:getMessage('삭제')}",
			},
		],
		on : {
			onMenuItemClick : function(id, event, node){
				var selectedItem = $$("tempTree").getSelectedItem(false);
				if("addChildTask" === id) {
					addChildTask(selectedItem);
				} else if("addNextTask" === id) {
					addNextTask(selectedItem);
				} else if("deleteTask" === id) {
					deleteTask(selectedItem);
				}
			}
		}
	});

	$$("tempTreeMenu").attachTo($$("tempTree"));
	
	loadIncludePage(getURLString("/project/template/view"));
});

//트리 불러오기
function getTemplateTree(){
	var param = new Object();
	
	param.oid = "${oid}"
	
	$$("tempTree").clearAll();
	$$("tempTree").showProgress();
	var url = getURLString("/project/template/getTemplateTree");
	ajaxCallServer(url, param, function(data){
		
		var list = data.list;
		$$("tempTree").parse(list);
		$$("tempTree").openAll();
		$$("tempTree").hideProgress();
	});
};

function tempTree_beforeContextMenu(id, event, node) {
	var selectedItem = $$("tempTree").getSelectedItem(true);
	
	if(selectedItem.length < 2) {
		$$("tempTree").select(id);
		tempTree_itemClick(id);
	}
}

function loadIncludePage(url, param) {
	
	if(param == null) {
		param = new Object();
		
		param.oid = "${oid}";
	}
	
	$("#includePage").load(url, param);
}

function viewAllOutput() {
	var url = getURLString("/project/template/outputList");
	
	var param = new Object();
	
	param.oid = "${oid}";
	
	loadIncludePage(url, param);
}

function tempTree_itemClick(id, event, node) {
	
	var item = $$("tempTree").getItem(id);
	
	var oid = item.oid;
	
	var param = new Object();
	console.log(item);
	var url = ""; 
	if(oid.indexOf("EProjectTemplate") > 0) {//EProjectTemplate
		url = getURLString("/project/template/view");
	}else{	//ETask
		param.templateOid = $("#templateOid").val();
		url = getURLString("/project/template/viewTask");
	}
	
	param.oid = oid;
	
	loadIncludePage(url, param);
}

function reloadTree() {
	getTemplateTree();
	loadIncludePage(getURLString("/project/template/view"));
}

function addChildTask(selectedItem) {
	var param = new Object();
	
	param.oid = selectedItem.oid;
	var url = getURLString("/project/template/addChildTask");
	$$("tempTree").showProgress();
	ajaxCallServer(url, param, function(data){
		if(data.newTask != null) {
			var newTask = data.newTask;
			$$("tempTree").add(newTask, newTask.sort - 1, newTask.parentId);
			$$("tempTree").open(newTask.parentId);
		}
		$$("tempTree").hideProgress();
	});
}

function addNextTask(selectedItem) {
	var param = new Object();
	
	param.oid = selectedItem.oid;
	var url = getURLString("/project/template/addNextTask");
	$$("tempTree").showProgress();
	ajaxCallServer(url, param, function(data){
		if(data.newTask != null) {
			var newTask = data.newTask;
			var nextTaskList = data.nextTaskList;
			
			nextTaskList.forEach(function(nextTask){
				$$("tempTree").updateItem(nextTask.id, nextTask);
			});
			
			$$("tempTree").add(newTask, newTask.sort - 1, newTask.parentId);
			$$("tempTree").open(newTask.parentId);
		}
		$$("tempTree").hideProgress();
	});
}

function deleteTask(selectedItem) {
	var param = new Object();
	
	param.oid = selectedItem.oid;
	var url = getURLString("/project/template/deleteTask");
	$$("tempTree").showProgress();
	ajaxCallServer(url, param, function(data){
		//$$("tempTree").remove(selectedItem.oid);
		if(data.msg == null) {
			reloadTree();
		}
		$$("tempTree").hideProgress();
	});
}

function moveUpTask(){
	var selectedItem = $$("tempTree").getSelectedItem(false);
	
	var param = new Object();
	
	param.oid = selectedItem.oid;
	var url = getURLString("/project/template/moveUpTask");
	ajaxCallServer(url, param, function(data){
		$$("tempTree").updateItem(data.task.id, data.task);
		$$("tempTree").updateItem(data.prevTask.id, data.prevTask);
		
		$$("tempTree").move(data.task.id, $$("tempTree").getBranchIndex(data.prevTask.id), null, {parent:data.task.parentId});
	});
}

function moveDownTask(){
	var selectedItem = $$("tempTree").getSelectedItem(false);
	
	var param = new Object();
	
	param.oid = selectedItem.oid;
	var url = getURLString("/project/template/moveDownTask");
	ajaxCallServer(url, param, function(data){
		$$("tempTree").updateItem(data.task.id, data.task);
		$$("tempTree").updateItem(data.nextTask.id, data.nextTask);
		
		$$("tempTree").move(data.task.id, $$("tempTree").getBranchIndex(data.nextTask.id), null, {parent:data.task.parentId});
	});
}
</script>