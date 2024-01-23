<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('템플릿 선행 Task')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:addPreTask()">${e3ps:getMessage('등록')}</button>
			</div>
		</div>
		
		<div class="list" id="treeGrid">
		</div>
	</div>
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	var grid = webix.ui({
		view:"treetable",
		container : "treeGrid",
		id:"treeGrid",
		clipboard:"selection",
		dragColumn: true,
		multiselect : true,
		scroll : "y",
		scrollAlignY:true,
		tooltip: true,
		yCount: 13,
		columns:[
			{ id:"check", header:"", editor:"checkbox",   width:40, tooltip:false,  
				template:"{common.rcheckbox()}"
			},
			{ id:"name",	header:"${e3ps:getMessage('이름')}", 	fillspace:true,
				css: "custom_webix_ellipsis",
				template:"{common.treetable()}#name#",
				tooltip:function(data){
					return data.name;
				}
			},
			{ id:"$level",	header:"LEVEL", 	width:60,
				css: "custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"manDay",	header:"${e3ps:getMessage('공수')}(${e3ps:getMessage('일')})", 	width:100,
				css: "custom_webix_ellipsis custom_webix_center",		
			},
		],
		type:{
			rcheckbox:function(obj, common, value, config){
				if(obj.oid != "${oid}"){
					if(obj.$count == 0 && (obj.noCheckbox == null || !obj.noCheckbox)) {
						return common.checkbox(obj, common, value, config);
					}
				}
				return "";
			}
		}
	});
	
	webix.extend($$("treeGrid"), webix.ProgressBar);
	
	getGridData();
});

function getGridData(){
	
	var param = new Object();
	
	param.oid = "${rootOid}"
	
	$$("treeGrid").clearAll();
	$$("treeGrid").showProgress();
	var url = getURLString("/project/template/getTemplateTree");
	ajaxCallServer(url, param, function(data){
		
		var list = data.list;
		$$("treeGrid").parse(list);
		$$("treeGrid").openAll();
		
		getPreTask();
		getPostTask();
		$$("treeGrid").hideProgress();
	});
}

function getPreTask() {
	var param = new Object();
	
	param.oid = "${oid}";
	
	var url = getURLString("/project/template/getPreTask");
	ajaxCallServer(url, param, function(data){
		
		var preTaskList = data.list;
		preTaskList.forEach(function(obj){
			var resultItem = $$("treeGrid").find(function(item){
				return item.oid == obj.oid;
			}, true);
			
			resultItem.check = true;
			$$("treeGrid").updateItem(resultItem.oid, resultItem);
		});
	});
}

function getPostTask() {
	var param = new Object();
	
	param.oid = "${oid}";
	
	var url = getURLString("/project/template/getPostTask");
	ajaxCallServer(url, param, function(data){
		
		var postTaskList = data.list;
		postTaskList.forEach(function(obj){
			var resultItem = $$("treeGrid").find(function(item){
				return item.oid == obj.oid;
			}, true)
			
			resultItem.noCheckbox = true;
			$$("treeGrid").updateItem(resultItem.oid, resultItem);
		});
	});
}

function addPreTask() {
	var checkItemList = $$("treeGrid").find(function(obj) {
		return obj.check == true;
	});
	
	var param = new Object();
	
	param.oid = "${oid}";
	param.items = checkItemList;
		
	var url = getURLString("/project/template/setPreTask");
	ajaxCallServer(url, param, function(data){
		if(opener.window.viewTask) {
			opener.window.viewTask("${oid}");
		}
		window.close();
	},true);
}
</script>