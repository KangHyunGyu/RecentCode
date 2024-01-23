<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 Role 목록')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:editRole()">${e3ps:getMessage('등록')}</button>
			</div>
		</div>
		
		<div class="list" id=grid_wrap>
		</div>
	</div>
</div>

<script type="text/javascript"> 
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	
	var grid = webix.ui({
		view:"datatable",
		container:"grid_wrap",
		id:"grid_wrap",
		clipboard:"selection",
		dragColumn: true,
		multiselect : true,
		scroll : "y",
		scrollAlignY:true,
		tooltip: true,
		yCount: 10,
		columns:[
			{ id:"check", header:{ content:"masterCheckbox" }, editor:"checkbox",  template:"{common.checkbox()}", width:40, tooltip:false,  },
			{ id:"key", header:["${e3ps:getMessage('코드')}"] , fillspace:true,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"value", header:["${e3ps:getMessage('이름')}"] , fillspace:true, 
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
	});
	
	webix.extend($$("grid_wrap"), webix.ProgressBar);
	
	getGridData();
});

function getGridData(){
	
	var param = new Object();
	
	param.oid = "${oid}"
	param.codeType = "PROJECTROLE";
	
	$$("grid_wrap").clearAll();
	$$("grid_wrap").showProgress();
	var url = getURLString("/common/getNumberCodeList");
	ajaxCallServer(url, param, function(data){
		
		var list = data.list;
		$$("grid_wrap").parse(list);
		
		getTaskRole();
		$$("grid_wrap").hideProgress();
	});
}

function getTaskRole() {
	var param = new Object();
	
	param.oid = "${oid}";
	
	var url = getURLString("/project/template/getTaskRole");
	ajaxCallServer(url, param, function(data){
		
		var taskRoleList = data.list;
		taskRoleList.forEach(function(obj){
			var resultItem = $$("grid_wrap").find(function(item){
				return item.key == obj.code;
			}, true);
			
			resultItem.check = true;
			$$("grid_wrap").updateItem(resultItem.id, resultItem);
		});
	});
}

function editRole(){
	var checkItemList = $$("grid_wrap").find(function(obj) {
		return obj.check == true;
	});
	
	var param = new Object();
	
	param.oid = "${oid}";
	param.items = checkItemList;
		
	var url = getURLString("/project/template/selectRoleAction");
	ajaxCallServer(url, param, function(data){
		if(opener.window.viewTask) {
			opener.window.viewTask("${oid}");
		}
		window.close();
	},true);
}
 
</script>