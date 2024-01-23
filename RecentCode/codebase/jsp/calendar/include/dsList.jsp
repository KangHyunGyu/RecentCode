
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- button -->
<div class="seach_arm2 pt15 pb5">
	<div class="leftbt">
		<span class="title">
			<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			${title}
		</span>
	</div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<div class="list" id="ds_grid_wrap">
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	
	var yCount = 10;
	if("${type}" == "single") {
		yCount = 1;
	}
	
	var grid = webix.ui({
		view:"datatable",
		container:"ds_grid_wrap",
		id:"ds_grid_wrap",
		select:"row",
		multiselect : true,
		scroll:"xy",
		yCount: yCount,
		tooltip:true,
		editable:false,
		autoConfig:false,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"name", header:["${e3ps:getMessage('이름')}"] , width:100,
				css: "custom_webix_ellipsis",
				editor:"text",
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}
			},
			{ id:"startDate", header:["${e3ps:getMessage('시작일')}"] , width:120,
				css: "custom_webix_ellipsis custom_webix_center",	
				editor:"date",
				format:webix.Date.dateToStr("%Y-%m-%d"),
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}	
			},
			{ id:"endDate", header:["${e3ps:getMessage('종료일')}"] , width:120,
				css: "custom_webix_ellipsis custom_webix_center",	
				editor:"date",
				format:webix.Date.dateToStr("%Y-%m-%d"),
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}	
			},
			{ id:"remark", header:["${e3ps:getMessage('설명')}"] , fillspace:1, minWidth:180,
				css: "custom_webix_ellipsis",
				editor:"text",
				cssFormat: function(value, config){
					return "custom_webix_editable";
				}
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
	//get grid data
	if("${oid}".length > 0) {
		ds_getGridData();
	}
});

function ds_getGridData(){
	
	var param = {
			oid : "${oid}"
		}
	var loadUrl = getURLString("/calendar/getDsList");
	window.startProgress();
	fetch(loadUrl, {
		method: "POST", 
		headers: {
	        "Content-Type": "application/json; charset=UTF-8",
	    },
	    body: JSON.stringify(param),
	}).then(function(response){
		return response.json();
	}).then(function(data){
		var gridData = data.list;
		$$("ds_grid_wrap").parse(gridData);
		window.endProgress();
	});
}

</script>