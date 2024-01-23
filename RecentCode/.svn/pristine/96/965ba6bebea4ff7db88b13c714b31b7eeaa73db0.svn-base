<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"></h4></div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<div class="list" id="multi_epm_grid_wrap">
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"multi_epm_grid_wrap",
		id:"multi_epm_grid_wrap",
		clipboard:"block",
		multiselect:true,
		select:"cell",
		dragColumn: true,
		scroll:"xy",
		yCount: 15,
		tooltip:true,
		resizeColumn:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"partNumber",	header:"${e3ps:getMessage('주부품')}",	width:120,
				css: "custom_webix_ellipsis",
			},
			{ id:"epmNumber",	header:"${e3ps:getMessage('도면 번호')}",	width:120,
				css: "custom_webix_ellipsis",
			},
			{ id:"epmName",	header:"${e3ps:getMessage('도면 명')}", 	width:180,
				css:"custom_webix_ellipsis",	
			},
			{ id:"fileName",	header:"${e3ps:getMessage('도면 파일명')}", 	width:150,
				css:"custom_webix_ellipsis",	
			},
			{ id:"relatedPartNumber",	header:"${e3ps:getMessage('관련 부품')}", 	width:150,
				css:"custom_webix_ellipsis",	
			},
			{ id:"description",	header:"${e3ps:getMessage('설명')}", 	fillspace:1,
				css:"custom_webix_ellipsis",	
			},
			{ id:"result",	header:"${e3ps:getMessage('결과')}", 	width:100,
				css:"custom_webix_ellipsis",	
			},
			{ id:"resultMessage",	header:"${e3ps:getMessage('결과 메세지')}", 	fillspace:1,
				css:"custom_webix_ellipsis",	
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
	
	webix.extend($$("multi_epm_grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("multi_epm_grid_wrap").unselect(); 
	});

});

//필터 초기화
function multi_epm_resetFilter(){
    
}

function multi_epm_xlsxExport() {
	webix.toExcel($$("multi_epm_grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
	});
}
</script>