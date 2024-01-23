<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table -->
	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<table class="mainTable">
				<colgroup>
					<col style="width:15%">
					<col style="width:35%">
					<col style="width:15%">
					<col style="width:35%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('전송타입')}</th>
						<td>
							<select class="w50" id="sendType" name="sendType">
								<option value="">ALL</option>
								<option>ITEM</option>
								<option>BOM</option>
								<option>PMS</option>
							</select>
						</td>
						<th scope="col">${e3ps:getMessage('전송객체 번호')}</th>
						<td ><input type="text" id="sendObjNumber" name="sendObjNumber" class="w100"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('객체상태')}</th>
						<td ><input type="text" id="state" name="state" class="w100"></td>
						<th scope="col">${e3ps:getMessage('메세지')}</th>
						<td ><input type="text" id="msg" name="msg" class="w100"></td>
					</tr>	
					<tr>
						<th scope="col">${e3ps:getMessage('전송일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('전송자')}</th>
						<td ><input type="text" id="sender" name="sender" class="w100"></td>
					</tr>
				</tbody>
			</table>
		</form>	
	</div>
	<!-- //pro_table -->
	<!-- button -->
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="count">0</span>/<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
<%-- 			<button type="button" class="s_bt03" onclick="reset();">${e3ps:getMessage('필터 초기화')}</button> --%>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:500px"></div>
	</div>
	<!-- //table list-->
</div>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();
webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"grid_wrap",
		id:"grid_wrap",
		clipboard:"block",
		multiselect:true,
		select:"cell",
		dragColumn: true,
		scroll:"xy",
		sort:"multi",
		yCount: 13,
		tooltip:true,
		resizeColumn:true,
		datafetch:50,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"sendType", header:["${e3ps:getMessage('전송타입')}"],	width:130,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"sendObjNumber", header:["${e3ps:getMessage('전송객체 번호')}"] , width:200,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"state", header:["${e3ps:getMessage('객체상태')}"] , width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"sendObjVersion", header:["${e3ps:getMessage('객체버전')}"] , width:80,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"msg", header:["${e3ps:getMessage('메시지')}"] ,fillspace:true, minWidth:300,
				css: "custom_webix_ellipsis",
			},
			{ id:"sender", header:["${e3ps:getMessage('전송자')}"],	width:130,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"sendTime", header:["${e3ps:getMessage('전송일자')}"],	width:130, sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",
			}
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("grid_wrap").getColumnConfig(header.column);
				
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
		scroll:"y",
		sort:"multi"
	});
	webix.event(window, "resize", function(){ grid.adjust(); });

	$(".webix_excel_filter").click(function(){
		 $$("grid_wrap").unselect(); 
	});

	getGridData();
});

$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//모듈 종류select option 가져오기
	getModuleTypeList();
	
});


function getGridData(){

	$$("grid_wrap").load(function(params){
		if(!params){
			params = new Object();
			params["start"] = 0;
			params["count"] = $$("grid_wrap").config.datafetch;
			params["continue"] = true;
		}
		
		getFormParams("searchForm", params);
		
		let response = ajaxCallServer(getURLString("/admin/searchErpHistoryAction"), params, null);
	      $("#total").html(response.total_count);
	      $("#sessionId").val(response.sessionId);
	      
	      return response;
		
// 		params = JSON.stringify(params);
// 		return webix.ajax().headers({
// 		    	"Content-Type": "application/json; charset=UTF-8"
// 		    }).post(getURLString("/admin/searchErpHistoryAction"), params)
// 		    .then(function(response){
// 		    	var data = response.json();
// 		    	console.log(data);
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

//검색조건 초기화
function reset(){
	var locationDisplay = $("#locationDisplay").val();
	$("#searchForm")[0].reset();
	$("#locationDisplay").val(locationDisplay);
}

function xlsxExport() {
	webix.toExcel($$("grid_wrap"), {
	   	filename: "xlsxExport"
	});
}
</script>