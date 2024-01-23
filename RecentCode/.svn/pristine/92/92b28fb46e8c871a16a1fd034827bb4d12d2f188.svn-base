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
		container:"sturcture_grid_wrap",
		id:"sturcture_grid_wrap",
		select:"row",
		scroll:"xy",
		sort:"multi",
		yCount: 5,
		tooltip:true,
		resizeColumn:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"number",	header:"${e3ps:getMessage('도면 번호')}",	fillspace:1,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.oid+ "\")'>" + obj.number + "</a>"
				}	
			},
			{ id:"thumbnail",  header:"", width:40,	tooltip:false,
				css:"custom_webix_imgCenter pointer",
				template:custom_webix_thumbnailTemplate
			},
			{ id:"name",	header:"${e3ps:getMessage('도면 명')}", 	fillspace:1.5,
				css:"custom_webix_ellipsis",	
			},
			{ id:"stateName",	header:"${e3ps:getMessage('상태')}", 	width:80,
				css:"custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"version",	header:"${e3ps:getMessage('버전')}", 	width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"creatorFullName", header:"${e3ps:getMessage('등록자')}",	width:80,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"depType", header:"${e3ps:getMessage('종속성 유형')}",	width:80,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"suppressed", header:"${e3ps:getMessage('억제됨')}",	width:80,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"modifyDate", header:"${e3ps:getMessage('최종 수정일')}",  width:100,
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
	
	webix.extend($$("sturcture_grid_wrap"), webix.ProgressBar);
	
	sturcture_getGridData();
});

function sturcture_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	var url = getURLString("/epm/getStructure");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("sturcture_grid_wrap").parse(gridData);
	});
}

//필터 초기화
function sturcture_resetFilter(){
    
}

function sturcture_xlsxExport() {
	webix.toExcel($$("sturcture_grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
	});
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('구조')}</h4></div>
	<div class="rightbt">
<%-- 		<button type="button" class="s_bt03" onclick="sturcture_resetFilter();">${e3ps:getMessage('필터 초기화')}</button> --%>
		<button type="button" class="s_bt03" onclick="sturcture_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="sturcture_grid_wrap">
</div>