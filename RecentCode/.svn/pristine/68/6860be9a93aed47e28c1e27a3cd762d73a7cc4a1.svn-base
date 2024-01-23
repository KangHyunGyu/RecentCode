<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
(!webix.env.touch && webix.env.scrollSize)
webix.CustomScroll.init();

webix.ready(function(){
var grid = webix.ui({
	view:"datatable",
	container:"check_drawing_grid_wrap",
	id:"check_drawing_grid_wrap",
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
		{ id:"index",   header:"No.",     width:40,      tooltip:false,
			css: "custom_webix_center",		
		},
		{ id:"number", header:"${e3ps:getMessage('부품 번호')}", width:100,fillspace:true,
			css: "custom_webix_ellipsis",
			template:function(obj){
				var oid = obj.oid;
				return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.name+"</a>";
			}	
		},
		{ id:"name", header:"${e3ps:getMessage('부품 명')}", width:100,
			css: "custom_webix_ellipsis custom_webix_center",	
		},
		{ id:"stateName", header:"${e3ps:getMessage('상태')}", width:100,
			css: "custom_webix_ellipsis custom_webix_center",	
		},
		{ id:"rev", header:"${e3ps:getMessage('부품 버전')}", width:100,
			css: "custom_webix_ellipsis custom_webix_center",	
		},
		{ id:"epmNo", header:"${e3ps:getMessage('도면 번호')}" ,width:150 , sort:"server",
			css: "custom_webix_ellipsis",
			template:function(obj){
				var oid = obj.epmOid;
				return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.epmNo+"</a>";
			}
		},
		{ id:"epmver", header:"${e3ps:getMessage('도면 버전')}", width:100,
			css: "custom_webix_ellipsis custom_webix_center",	
		},
		{ id:"linkStateStr", header:"${e3ps:getMessage('배포 상태')}", width:100,
			css: "custom_webix_ellipsis custom_webix_center",	
		},
	],
	on:{
		onHeaderClick:function(header, event, target) {
			var column = $$("check_drawing_grid_wrap").getColumnConfig(header.column);
			
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
// if("${oid}".length > 0) {
// 	check_drawing_getGridData();
// }
});
function check_drawing_getGridData(){
	
	var param = {
			oid : "${oid}"
		}
	var loadUrl = getURLString("/distribute/checkDrawingEpmAction");
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
		$$("check_drawing_grid_wrap").parse(gridData);
		window.endProgress();
	});
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt">
		<span class="title">
			<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('도면 검증')}
		</span>
	</div>
	<div class="rightbt"></div>
</div>
<!-- //button -->
<div class="list" id="check_drawing_grid_wrap">
</div>