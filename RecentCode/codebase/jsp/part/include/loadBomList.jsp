<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"></div>
	<div class="rightbt"></div>
</div>
<!-- //button -->
<div class="list" id="load_bom_grid_wrap">
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
	  
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"load_bom_grid_wrap",
		id:"load_bom_grid_wrap",
		clipboard:"block",
		multiselect:true,
		select:"cell",
		dragColumn: true,
		scroll:"xy",
		yCount: 13,
		tooltip:true,
		resizeColumn:true,
		editable:true,
		columns:[
			{ id:"index",   header:"No.",     width:60,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"line",   header:"Line",     width:60,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"level",   header:"Level",     width:60,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"number", header:"Number" , width:180,
				css: "custom_webix_ellipsis",	
			},
			{ id:"name", header:"Name" , fillspace:1, minWidth:180,
				css: "custom_webix_ellipsis",
			},
			{ id:"unit", header:"${e3ps:getMessage('단위')}" , width:80,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"quantity", header:"${e3ps:getMessage('수량')}" , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"result", header:"${e3ps:getMessage('결과')}" , width:200,
				css: "custom_webix_ellipsis",	
			},
			{ id:"checkChange", header:"${e3ps:getMessage('선택')}" , width:70,
				cssFormat: function(value, obj, row, column){
					var format = "";
					if(obj.change == "REVISE" || obj.change == "REVISECHANGEQTY") {
						format = "custom_webix_disabled"
					}
					return "custom_webix_ellipsis custom_webix_center" + " " + format;
				},
				editor:"select",
				options:["Y","N"],
			},
			{ id:"purchaseFlag", header:"${e3ps:getMessage('구매여부')}" , width:90,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"endDate", header:"${e3ps:getMessage('종료일자')}" , width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"summary", header:"${e3ps:getMessage('개요')}" , width:250,
				css: "custom_webix_ellipsis",	
			},
			{ id:"specification", header:"${e3ps:getMessage('규격')}" , width:220,
				css: "custom_webix_ellipsis",	
			},
			{ id:"notice", header:"${e3ps:getMessage('비고')}" , width:220,
				css: "custom_webix_ellipsis",	
			},
			{ id:"material", header:"${e3ps:getMessage('MATERIAL')}" , width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"snock", header:"${e3ps:getMessage('SN자재여부')}" , width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"cPartNum", header:"${e3ps:getMessage('참조부품')}" , width:120,
				css: "custom_webix_ellipsis",},
			{ id:"partType", header:"${e3ps:getMessage('자재타입')}", width:120,
				css: "custom_webix_ellipsis",
			},
			{ id:"surfaceTreatment", header:"${e3ps:getMessage('표면처리')}", width:120,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"maker", header:"Maker", 	width:120,
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		on:{
			onBeforeEditStart:function(cell){
				if("checkChange" === cell.column) {
					var item = $$("load_bom_grid_wrap").getItem(cell.row);
					if(item.change === "REVISE" || item.change === "REVISECHANGEQTY") {
						return false;
					} else {
						return true;
					}
					return true;
				}
				return false;
			},
			"data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        }
		}
	});
	
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("load_bom_grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("load_bom_grid_wrap").unselect(); 
	});

});
</script>