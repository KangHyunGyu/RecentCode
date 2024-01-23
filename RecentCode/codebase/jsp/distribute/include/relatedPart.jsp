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
		container:"rel_part_grid_wrap",
		id:"rel_part_grid_wrap",
		clipboard:"block",
		multiselect:true,
		select:"cell",
		scroll:"xy",
		sort:"multi",
		yCount: 5,
		tooltip:true,
		resizeColumn:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"number", header:[{text:"${e3ps:getMessage('부품 번호')}", content:"excelFilter", mode:"text"}] , sort:"string", width:180,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.oid+ "\")'>" + obj.number + "</a>"
				}	
			},
			{ id:"name", header:[{text:"${e3ps:getMessage('부품 명')}", content:"excelFilter", mode:"text"}] , fillspace:1, minWidth:180, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"stateName", header:"${e3ps:getMessage('상태')}", width:80,  sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"rev", header:["${e3ps:getMessage('부품 버전')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"epmNo", header:["${e3ps:getMessage('도면 번호')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"epmver", header:["${e3ps:getMessage('도면 버전')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"linkStateStr", header:["${e3ps:getMessage('배포 상태')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"prUser", header:["${e3ps:getMessage('PR 요청자')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"pjtNo", header:["${e3ps:getMessage('프로젝트 번호')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"downloadDeadline", header:["${e3ps:getMessage('납품기일')}"] , width:60,
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
	
	if("${isPurchase}" == "Y"){
		$$("rel_part_grid_wrap").showColumn("downloadDeadline");
		if("${orderType}"=="P") {	// 구매배포
			$$("rel_part_grid_wrap").showColumn("prUser");
			$$("rel_part_grid_wrap").hideColumn("pjtNo");
		} else {				// 견적 배포
			$$("rel_part_grid_wrap").hideColumn("prUser");
			$$("rel_part_grid_wrap").showColumn("pjtNo");
// 			AUIGrid.setColumnProp(rel_part_myGridID, 9, {headerText : "${e3ps:getMessage('견적 마감일')}"});
		}
	}
	
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("rel_part_grid_wrap"), webix.ProgressBar);
	

	rel_part_getGridData();
	
	$(".webix_excel_filter").click(function(){
		 $$("rel_part_grid_wrap").unselect(); 
	});
});


function rel_part_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	var url = getURLString("/distribute/getRelatedPart");
	ajaxCallServer(url, param, function(data){

		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("rel_part_grid_wrap").parse(gridData);
	});
}

//필터 초기화
function rel_part_resetFilter(){
	$$("rel_part_grid_wrap").eachColumn(function(id, col){
		var filter = this.getFilter(id);
		if(filter) {
			if(filter.setValue) filter.setValue("");
			else filter.value = "";
		}
	});
	$$("rel_part_grid_wrap").filterByAll();
}

function rel_part_xlsxExport() {
	webix.toExcel($$("rel_part_grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
		ignore: { bom:true},
	});
}

</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('관련 부품')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_part_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_part_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_part_grid_wrap">
</div>