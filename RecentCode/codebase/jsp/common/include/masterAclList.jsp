<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
webix.ready(function(){
	let grid = webix.ui({
		view:"datatable",
		container:"grid_wrap",
		id:"grid_wrap",
		tooltip:true,			// tooltip
		yCount:10,				// rowCount
		headermenu:true, 		// header menu
		dragColumn:true, 		// header size drag
		resizeColumn:true,		// resize header Column
		scroll:"y",				// scroll
		sort:"multi",			// sort
		datafetch:13,
		checkboxRefresh:true,
		columns:[
			{ 
				id:"index",
				header:"No.",
				width:40,
				tooltip:false,
				css: "custom_webix_center"
			},
			{ 
				id:"check",
				header:{ content:"masterCheckbox" ,contentId:"masterCheck"  },
				template:"{common.checkbox()}",
				width:40,
				tooltip:false,
				css: "custom_webix_pointer", 
			},
			{ 
				id:"userId", 
				header:[{mode:"text", content:"excelFilter", text:"아이디"}],
				minWidth:180,
				sort:"server",
				css: "custom_webix_ellipsis custom_webix_center"
			},
			{ 
				id:"userName", 
				header:[{mode:"text", content:"excelFilter", text:"이름"}],
				minWidth:180,
				fillspace: true,
				sort:"server",
				css: "custom_webix_ellipsis custom_webix_center"
			},
			{ 
				id:"duty", 
				header:[{mode:"text", content:"excelFilter", text:"직위"}],
				minWidth:180,
				sort:"server",
				css: "custom_webix_ellipsis custom_webix_center"
			},
			{ 
				id:"deptName", 
				header:[{mode:"text", content:"excelFilter", text:"부서"}],
				minWidth:180,
				sort:"server",
				css: "custom_webix_ellipsis custom_webix_center"
			}
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("grid_wrap").getColumnConfig(header.column);
				if("index" === header.column) {
					return false;
				}
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
	      "data->onStoreUpdated":function(){
	          this.data.each(function(obj, i){
	              obj.index = i+1;
	          });
	      },
	  },
	});
	
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("grid_wrap").unselect(); 
	});
});
$(document).ready(function(){
	//권한 설정 버튼
	$("#settingACLBtn").click(function () {
		let perOid = $("#perOid").val();
		let url = getURLString("/admin/searchAclUserPopup?perOid="+perOid);
		openPopup(url, "popup", 1200, 700);
	});
	
	//권한 제거 버튼
	$("#deleteACLBtn").click(function () {
		let param = getFormParams("searchForm");
		let checkItemList = $$("grid_wrap").find(function(obj) {
			return obj.check == true;
		});
		param.checkItemList = checkItemList;
		//console.log(param);
		
		let url = getURLString("/admin/deleteMasterAclAction");
		ajaxCallServer(url, param, function(data){
				if(opener.window.loadDocAclList){
					opener.window.loadDocAclList();
				}
				let onTab = document.querySelector("div .tap>ul>li.on");
				if(onTab){
					loadIncludePage(onTab);
				}
		}, true);
	});
	
	$("#excelBtn").click(function () {
		webix.toExcel($$("grid_wrap"), {
		   	filename: "xlsxExport",
		   	rawValues:true,
		});
	});
	search();
});
//get grid data
function getGridData() {
	$$("grid_wrap").load(function(params){
		if(!params){
			params = new Object();
			params["start"] = 0;
			params["count"] = $$("grid_wrap").config.datafetch;
			params["continue"] = true;
		}
		
		getFormParams("searchForm", params);
		
		let response = ajaxCallServer(getURLString("/admin/searchMasterAclAction"), params, null);
	      $("#total").html(response.total_count);
	      $("#sessionId").val(response.sessionId);
	      
	      return response;
		
// 		params = JSON.stringify(params);
// 		return webix.ajax().headers({
// 		    	"Content-Type": "application/json; charset=UTF-8"
// 		    }).post(getURLString("/admin/searchMasterAclAction"), params)
// 		    .then(function(response){
// 		    	var data = response.json();
// 		    	//console.log(params, data);
// 				$("#total").html(data.total_count);
// 				$("#sessionId").val(data.sessionId);
// 		    	return data;
// 		    });
	
	});
}

//검색
function search(){
	$$("grid_wrap").clearAll();
	$("#sessionId").val("");
	getGridData();
}

</script>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
	<div class="rightbt">
		<c:if test="${isAdmin || isAuth}">
			<button type="button" class="s_bt03" id="settingACLBtn">권한설정</button>
			<button type="button" class="s_bt03" id="deleteACLBtn">권한제거</button>
		</c:if>
		
		<button type="button" class="s_bt03" id="filterRefreshBtn">필터 초기화</button>
		<!-- <button type="button" class="s_bt03" id="excelBtn">엑셀 다운로드</button> -->
	</div>
</div>

<form name="searchForm" id="searchForm" style="margin-bottom: 0px;">
	<input type="hidden" id="sessionId" name="sessionId" value="">
	<input type="hidden" id="perOid" name="perOid" value="${ oid }">
</form>

<!-- table list-->
<div>
	<div class="list" id="grid_wrap"></div>
</div>
<!-- //table list-->
