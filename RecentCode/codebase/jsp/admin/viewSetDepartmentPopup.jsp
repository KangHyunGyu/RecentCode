<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop">
<input type="hidden" id="departmentOid" name="departmentOid" value="${departmentOid }" />
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('사용자 부서 설정')}</h2>
		<span class="close">
			<a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a>
		</span>
	</div>
	
	<!-- button -->
	<div class="seach_arm pt10 pb10">
		<div class="leftbt"></div>
		<div class="rightbt"></div>
	</div>
	<!-- //button -->
	
	<div class="semi_content">
		<div class="pl20 pr10">
			<div class="semi_table3">
				<table>
					<tr>
						<th style="text-align:right;">${departmentName } ${e3ps:getMessage('등록 인원')}
						</th>
						<th style="text-align:right;">
							<input type="text" id="deptUser" name="deptUser"  style="width:100px;"/>
							<button type="button" class="s_bt" onclick="javascript:searchDeptUser()">${e3ps:getMessage('검색')}</button>
						</th>
					</tr>
				</table>
			</div>
			<!-- table list-->
			<div>
<!-- 				<div class="list" id="du_grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
				<div class="list" id="du_grid_wrap" style="height:500px; border-top:2px solid #74AF2A;"></div>
			</div>
			<!-- //table list-->
		</div>
		
		<div class="pl10 pr20">
			<div class="semi_table3">
				<table>
					<tr>
						<th style="text-align:right;">${departmentName } ${e3ps:getMessage('미등록 인원')}</th>
						<th style="text-align:right;">
							<input type="text" id="nonDeptUser" name="nonDeptUser" style="width:100px;"/>
							<button type="button" class="s_bt" onclick="javascript:searchNonDeptUser()">${e3ps:getMessage('검색')}</button>
						</th>
					</tr>
				</table>
			</div>
			<!-- table list-->
			<div>
<!-- 				<div class="list" id="ndu_grid_wrap" style="height:500px; border-top:2px solid #1064aa;"></div> -->
				<div class="list" id="ndu_grid_wrap" style="height:500px; border-top:2px solid #74AF2A;"></div>
			</div>
			<!-- //table list-->
		</div>
	</div>
</div>
<script type="text/javascript">
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){

	var du_grid_wrap = webix.ui({
		view:"datatable",
		container:"du_grid_wrap",
		id:"du_grid_wrap",
		select:true,
		multiselect:true,
		resizeColumn:true,
		drag:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"id", header:"${e3ps:getMessage('아이디')}", fillspace:true,  sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"name", header:"${e3ps:getMessage('이름')}", width:100, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"duty", header:"${e3ps:getMessage('직위')}", width:100, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"departmentName", header:"${e3ps:getMessage('부서명')}", width:100, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("du_grid_wrap").getColumnConfig(header.column);
				
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
	        "data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        },
	        onAfterDrop : dropEndEventHandler_du,
	    },
	    hover:"webix_datatable_hover",
		select:"row",
		scroll:"y",
		sort:"multi",
	});
	
	var ndu_grid_wrap = webix.ui({
		view:"datatable",
		container:"ndu_grid_wrap",
		id:"ndu_grid_wrap",
		select:true,
		multiselect:true,
		resizeColumn:true,
		drag:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"id", header:"${e3ps:getMessage('아이디')}", fillspace:true,  sort:"string",
				css:"custom_webix_ellipsis custom_webix_center",
			},
			{ id:"name", header:"${e3ps:getMessage('이름')}", width:100, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"duty", header:"${e3ps:getMessage('직위')}", width:100, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"departmentName", header:"${e3ps:getMessage('부서명')}", width:100, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("ndu_grid_wrap").getColumnConfig(header.column);
				
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
	        "data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        },
	        onAfterDrop : dropEndEventHandler_ndu,
	    },
	    hover:"webix_datatable_hover",
		select:"row",
		scroll:"y",
		sort:"multi",
	});
		  
	webix.event(window, "resize", function(){ grid.adjust(); });
	//getGridData();	
});
$(document).ready(function(){
	
	$("#deptUser").keypress(function(e){
		if(e.keyCode==13){
			searchDeptUser();
		}
	});
	$("#nonDeptUser").keypress(function(e){
		if(e.keyCode==13){
			searchNonDeptUser();
		}
	});
	
	//get grid data (부서 등록 인원)
	preRequestData();
});


//부서 등록 인원 최초 데이터 요청
function preRequestData() {
	var param = new Object();
	
	param["departmentOid"] = $("#departmentOid").val();
	
	var url = getURLString("/admin/searchDeptUserListAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("du_grid_wrap").parse(gridData);
		
	});
	
	url = getURLString("/admin/searchNonDeptUserListAction");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("ndu_grid_wrap").parse(gridData);

	});
}

//드래드 앤 드랍 핸들러 - du_grid_wrap
function dropEndEventHandler_du(context, native_event) {
	var param = new Object();
	var id = context.source.toString();
	var itemList = new Array();
	var departmentOid = $("#departmentOid").val();
	var idList = id.split(',');
	
	for(var i=0; i < idList.length; i++){
		itemList.push($$("du_grid_wrap").getItem(idList[i]));
	}
	
	param["gridName"] = "#du_grid_wrap";
	param["departmentOid"] = departmentOid;
	param["items"] = itemList;

	var url = getURLString("/admin/setDepartmentAction");
	ajaxCallServer(url, param, function(data){
		preRequestData();
	});
	opener.window.preRequestData();
}

//드래드 앤 드랍 핸들러 - ndu_grid_wrap
function dropEndEventHandler_ndu(context, native_event) {
	var param = new Object();
	var id = context.source.toString();
	var itemList = new Array();
	var departmentOid = $("#departmentOid").val();
	var idList = id.split(',');
	
	for(var i=0; i < idList.length; i++){
		itemList.push($$("ndu_grid_wrap").getItem(idList[i]));
	}
	
	param["gridName"] = "#ndu_grid_wrap";
	param["departmentOid"] = departmentOid;
	param["items"] = itemList;
	var url = getURLString("/admin/setDepartmentAction");
	ajaxCallServer(url, param, function(data){
		preRequestData();
	});
	opener.window.preRequestData();
}



//부서 등록 인원 검색
function searchDeptUser() {
	var deptUser = $("#deptUser").val();
	
	// 검색 실시
	$$("du_grid_wrap").filter("#name", deptUser);
}
//부서 미등록 인원 검색
function searchNonDeptUser() {
	var nonDeptUser = $("#nonDeptUser").val();

	// 검색 실시
	$$("ndu_grid_wrap").filter("#name", nonDeptUser);
}
</script>