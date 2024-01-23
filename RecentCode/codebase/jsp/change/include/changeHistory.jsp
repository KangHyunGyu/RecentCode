<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="list" id="changeHistory_grid_wrap">
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"changeHistory_grid_wrap",
		id:"changeHistory_grid_wrap",
		select:"row",
		scroll:"xy",
		sort:"multi",
		yCount: 3,
		tooltip:true,
		resizeColumn:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"stop",	header:"${e3ps:getMessage('구분')}", 	width:120,
				css:"custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"activeDate",	header:"${e3ps:getMessage('날짜')}", 	width:120,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"owner",	header:"${e3ps:getMessage('담당자')}", 	width:120,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"comments",	header:"${e3ps:getMessage('사유')}", 	fillspace:1,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
		],
		on:{
			"data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        }
		}
	});
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("changeHistory_grid_wrap"), webix.ProgressBar);
	
	changeHistory_getGridData();
});

function changeHistory_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	var url = getURLString("/change/getChangeHistory");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("changeHistory_grid_wrap").parse(gridData);
	});
}

//필터 초기화
function rel_epm_resetFilter(){
    
}

function rel_epm_xlsxExport() {
	webix.toExcel($$("changeHistory_grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
	});
}
</script>