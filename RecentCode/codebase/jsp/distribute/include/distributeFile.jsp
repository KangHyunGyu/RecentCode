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
		container:"rel_file_grid_wrap",
		id:"rel_file_grid_wrap",
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
			{ id : "fileTypeStr",		header : "${e3ps:getMessage('타입')}",		width:"140", fillspace: true,		
				css: "custom_webix_ellipsis",
			},
			{ id : "number",		header : "${e3ps:getMessage('번호')}",		width:"140",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "name",		header : "${e3ps:getMessage('이름')}",		width:"140",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "fileName",		header : "${e3ps:getMessage('파일 명')}",		width:"140",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "isSendStr",		header : "${e3ps:getMessage('전송 여부')}",		width:"140",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("rel_file_grid_wrap").getColumnConfig(header.column);
				
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
	
	webix.extend($$("rel_file_grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("rel_file_grid_wrap").unselect(); 
	});

	rel_file_getGridData();
});

$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	rel_file_getGridData();
});
	
// 	if("${dis.isPurchase}"=="Y") {	// 정규 배포
// 		AUIGrid.hideColumnByDataField(rel_file_myGridID, "fileTypeStr");
// 	} else {				// 비정규 배포
// 		AUIGrid.showColumnByDataField(rel_file_myGridID, "fileTypeStr");
// 	}

function rel_file_getGridData(){
	var param = new Object();
	param["oid"] = "${oid}"
	
	var url = getURLString("/distribute/getMergeFileList");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		$$("rel_file_grid_wrap").parse(gridData);
		
	});
}
</script>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('배포 파일')}</h4></div>
	<div class="rightbt">
		<button type="button" class="s_bt03" onclick="rel_file_resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
		<button type="button" class="s_bt03" onclick="rel_file_xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
	</div>
</div>
<!-- //button -->
<div class="list" id="rel_file_grid_wrap">
</div>