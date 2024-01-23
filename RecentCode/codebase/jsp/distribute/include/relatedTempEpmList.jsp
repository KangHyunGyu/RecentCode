<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();

webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"rel_temp_epm_grid_wrap",
		id:"rel_temp_epm_grid_wrap",
		tooltip:true,
		yCount:15,
		clipboard:"block",
		multiselect:true,
		select:"cell",
		dragColumn:true, // header drag
		resizeColumn:true, // resizeColumn
		scroll:"y",
		sort:"multi",
		datafetch:50,
		columns:[
			{ id : "number",				header : "${e3ps:getMessage('부품 번호')}",			width:"140",		
				css: "custom_webix_ellipsis",
			},
			{ id : "name",				header : "${e3ps:getMessage('부품 명')}",			width:"140", fillspace: true,	
				css: "custom_webix_ellipsis",
			},
			{ id : "stateName",				header : "${e3ps:getMessage('상태')}",				width:"100",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "rev",			header : "${e3ps:getMessage('부품 버전')}",				width:"100",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "epmNo",			header : "${e3ps:getMessage('도면 번호')}",				width:"140",	
				css: "custom_webix_ellipsis custom_webix_center",
				template:function(obj){
					var oid = obj.oid;
					return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.epmNo+"</a>";
				},
			},
			{ id : "epmver",			header : "${e3ps:getMessage('도면 버전')}",				width:"100",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "linkStateStr",			header : "${e3ps:getMessage('배포 상태')}",				width:"100",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "supplierNames",			header : "${e3ps:getMessage('업체명')}",				width:"140",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "requestNames",			header : "${e3ps:getMessage('배포자')}",				width:"140",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("rel_temp_epm_grid_wrap").getColumnConfig(header.column);
				
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
	        "data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        }
	    },
	});
	
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("rel_temp_epm_grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("rel_temp_epm_grid_wrap").unselect(); 
	});

	rel_temp_epm_getGridData();
});
$(document).ready(function(){
	
});

function rel_temp_epm_getGridData(){
	$$("rel_temp_epm_grid_wrap").load(function(params){
		
		if(!params){
			params = new Object();
			params["start"] = 0;
			params["count"] = $$("rel_temp_epm_grid_wrap").config.datafetch;
			params["continue"] = true;
		}
		
		params["oid"] = "${oid}";
		
		params = JSON.stringify(params);
		return webix.ajax().headers({
		    	"Content-Type": "application/json; charset=UTF-8"
		    }).post(getURLString("/distribute/getRelatedTempEpmList"), params)
		    .then(function(response){
		    	var data = response.json();
		    	
				$("#total").html(data.total_count);
				$("#sessionId").val(data.sessionId);
		    	return data;
		    });
	});
}


function rel_temp_epm_xlsxExport() {
	webix.toExcel($$("rel_temp_epm_grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
		ignore: { bom:true,  icon:true, thumbnail:true, stateTag:true},
	});
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('도면 현황')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_temp_epm_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_temp_epm_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_temp_epm_grid_wrap">
</div>