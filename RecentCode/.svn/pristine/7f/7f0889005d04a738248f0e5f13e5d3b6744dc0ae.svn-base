<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt15 pb5">
		<div class="leftbt">
			<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
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
			<!-- <input type="hidden" id="location" name="location" value=""> -->
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<table class="mainTable">
				<colgroup>
					<col style="width:10%">
					<col style="width:23%">
					<col style="width:10%">
					<col style="width:23%">
					<col style="width:10%">
					<col style="width:24%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('도면 번호')}</th>
						<td><input type="text" id="epmNumber" name="epmNumber" class="w100"></td>
						<th scope="col">${e3ps:getMessage('도면 명')}</th>
						<td ><input type="text" id="epmName" name="epmName" class="w100"></td>
						<th scope="col">${e3ps:getMessage('도면 상태')}</th>
						<td>
							<select class="multiSelect w30" id="epmState" name="epmState" multiple style="height:20px;overflow-y: hidden;">
							</select>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('부품 번호')}</th>
						<td><input type="text" id="partNumber" name="partNumber" class="w100"></td>
						<th scope="col">${e3ps:getMessage('부품 명')}</th>
						<td ><input type="text" id="partName" name="partName" class="w100"></td>
						<th scope="col">${e3ps:getMessage('부품 상태')}</th>
						<td>
							<select class="multiSelect w30" id="partState" name="partState" multiple style="height:20px;overflow-y: hidden;">
							</select>
						</td>
					</tr>	
					<tr>
						<th scope="col">${e3ps:getMessage('도면 작성자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="epmCreator" name="epmCreator" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('epmCreator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('epmCreator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('부품 작성자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="partCreator" name="partCreator" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('partCreator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('partCreator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('다른 항목')}</th>
						<td>
							<input type="radio" id="difference" name="difference" value="version" checked>
							<label>${e3ps:getMessage('버전')}</label>
							<input type="radio" id="difference" name="difference" value="state">
							<label>${e3ps:getMessage('상태')}</label>
						</td>
					</tr>	
				</tbody>
			</table>
		</form>	
	</div>
	<!-- //pro_table -->
	<!-- button -->
	<div class="seach_arm pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="count">0</span>/<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
<%-- 			<button type="button" class="s_bt03" onclick="reset();">${e3ps:getMessage('필터 초기화')}</button> --%>
			<button type="button" class="s_bt03" onclick="excelDown('searchForm', 'excelDownEpmPartState');">${e3ps:getMessage('엑셀 다운로드')}</button>
			<!-- <button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>  -->
			<!-- <img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownEpmPartState');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0"> --> 
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap"></div>
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
		yCount: 13,
		leftSplit:4,
		tooltip:true,
		resizeColumn:true,
		datafetch:50,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"epmNumber", header:["${e3ps:getMessage('도면 번호')}"] , width:180,
				css: "custom_webix_ellipsis",	
			},
			{ id:"epmRev", header:["${e3ps:getMessage('버전')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"epmName", header:["${e3ps:getMessage('도면 명')}"] , fillspace:1, minWidth:180,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.epmOid+ "\")'>" + obj.epmName + "</a>"
				}	
			},
			{ id:"epmStateName", header:"${e3ps:getMessage('도면 상태')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"epmCreatorFullName", header:"${e3ps:getMessage('도면 작성자')}", 	width:120,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"partNumber", header:["${e3ps:getMessage('부품 번호')}"], width:180,
				css: "custom_webix_ellipsis",	
			},
			{ id:"partRev", header:["${e3ps:getMessage('버전')}"] , width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"partName", header:["${e3ps:getMessage('부품 명')}"] , fillspace:1, minWidth:180,
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<a href='javascript:openView(\"" + obj.partOid+ "\")'>" + obj.partName + "</a>"
				}	
			},
			{ id:"partStateName", header:"${e3ps:getMessage('부품 상태')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"partCreatorFullName", header:"${e3ps:getMessage('부품 작성자')}", 	width:120,
				css: "custom_webix_ellipsis custom_webix_center",
			},
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("grid_wrap").getColumnConfig(header.column);
				
				if("server" === column.sort) {
					$("#sessionId").val("");
				}
			},
			onItemClick:function(id, event, node) {
				
				let item = $$("grid_wrap").getItem(id);
				
				if("thumbnail" == id.column){
					openMiniWVSPopup(item.minPublishURL);
				}
			},
			"data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        }
		}
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
	
	getNumberCodeList("PartType", "PARTTYPE", false, true);
	
	getUnitList();
	
	getMaterialList();
	
	//lifecycle list
	getLifecycleListToId("epmState", "LC_Default");
	getLifecycleListToId("partState", "LC_Default");
	
	//Cad Division list
	getCadDivisionList();
	
	//Cad Type list
	getCadTypeList();
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
		
		params = JSON.stringify(params);
		return webix.ajax().headers({
		    	"Content-Type": "application/json; charset=UTF-8"
		    }).post(getURLString("/epm/searchEpmPartStateScrollAction"), params)
		    .then(function(response){
		    	var data = response.json();
		    	
				$("#total").html(data.total_count);
				$("#sessionId").val(data.sessionId);
		    	return data;
		    });
	
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

//필터 초기화
function resetFilter(){
    $$("grid_wrap").eachColumn(function(id, col){
    	var filter = this.getFilter(id);
    	if(filter) {
    		if(filter.setValue) filter.serValue("");
    		else filter.value = "";
    	}
    });
    $$("grid_wrap").filterByAll();
}

function xlsxExport() {
	webix.toExcel($$("grid_wrap"), {
	   	filename: "xlsxExport"
	});
}
</script>