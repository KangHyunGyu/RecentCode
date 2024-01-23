<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
			<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="switchDetailBtn" onclick="switchDetailBtn();">${e3ps:getMessage('상세검색')}</button>
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table -->
	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm" style="margin-bottom: 0px;">
		<input type="hidden" name="callbackName" value="${param.callbackName}"/>
		<textarea style="display:none" name="callbackObj">${param.callbackObj}</textarea>
			<input type="hidden" id="documentOid" name="documentOid" value="">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<input type="hidden" id="mode" name="mode" value="search">
			<input type="hidden" id="cFolderOid" name="cFolderOid" value="">
			<input type="hidden" id="cFolderPath" name="cFolderPath" value="">
			<table class="mainTable">
				<colgroup>
					<col style="width:13%">
					<col style="width:37%">
					<col style="width:13%">
					<col style="width:37%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('제목')}</th>
						<td><input type="text" id="oldcarName" name="oldcarName" class="w95"></td>
						<th scope="col">${e3ps:getMessage('번호')}</th>
						<td><input type="text" id="oldcarNumber" name="oldcarNumber" class="w95"></td>
					</tr>
					<tr>
						
						<th scope="col">${e3ps:getMessage('발생일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" id="preOccurrenceDate" name="preOccurrenceDate" readonly>
							~
							<input type="text" class="datePicker w25" id="postOccurrenceDate" name="postOccurrenceDate" readonly>
						</td>
						<th scope="col">${e3ps:getMessage('발생시점')}</th>
						<td><select id="occurrenceStep" name="occurrenceStep" class="w50"></select></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('차종')}</th>
						<td><select id="occurrenceModel" name="occurrenceModel" class="w50"></select></td>
						<th scope="col">${e3ps:getMessage('발생장소')}</th>
						<td><select id="occurrencePlace" name="occurrencePlace" class="w50"></select></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('관련 부품')}</th>
						<td>
						<div class="pro_view">
							<select class="searchRelatedObject" id="relatedPart" name="relatedPart" multiple data-param="part"></select>
						</div>
						</td>
						<th scope="col">${e3ps:getMessage('버전')}</th>
						<td>
							<input type="radio" id="version" name="version" value="new" checked>
							<label>${e3ps:getMessage('최신 버전')}</label>
							<input type="radio" id="version" name="version" value="all">
							<label>${e3ps:getMessage('모든 버전')}</label>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('문제공정')}</th>
						<td colspan="3"><input type="text" id="problemProcess" name="problemProcess" class="w95"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('문제내용')}</th>
						<td colspan="3"><input type="text" id="problemContent" name="problemContent" class="w95"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('작성자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="creator" name="creator" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('최종수정일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
					</tr>	
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('문제점 및 원인')}</th>
						<td colspan="3"><input type="text" id="cause" name="cause" class="w95"></td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('개선대책 및 처리내용')}</th>
						<td colspan="3"><input type="text" id="improve" name="improve" class="w95"></td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('시행자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="worker" name="worker" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('worker', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('worker');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('처리상태')}</th>
						<td><select id="oldcarState" name="oldcarState" class="multiSelect w50" multiple></select></td>
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
				${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
<%-- 			<button type="button" class="s_bt03" onclick="reset();">${e3ps:getMessage('필터 초기화')}</button> --%>
			<!-- <button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button> -->
			<button type="button" class="s_bt03" onclick="excelDown('searchForm', 'excelDownOldcar');">${e3ps:getMessage('엑셀 다운로드')}</button>
			<!-- <img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownDoc');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0"> -->
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
		sort:"multi",
		yCount: 13,
		leftSplit:4,
		tooltip:true,
		resizeColumn:true,
		datafetch:50,
		columns:[
			{ id:"index",   header:"No.",     width:40,	tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"oldcarNumber", header:["${e3ps:getMessage('번호')}"] , sort:"server", width:150,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"rev", header:["${e3ps:getMessage('버전')}"],	width:60,
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"oldcarName", header:["${e3ps:getMessage('제목')}"] , fillspace:true, minWidth:180, sort:"server",
				css: "custom_webix_ellipsis",
				template:function(obj){
					var oid = obj.oid;
					return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.name+"</a>";
				}
			},
			{ id:"occurrenceModelName", header:[{text:"${e3ps:getMessage('차종')}", content:"excelFilter", mode:"text"}] , width:120, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id:"oldcarStateName", header:[{text:"${e3ps:getMessage('처리상태')}", content:"excelFilter", mode:"text"}] , width:120,	sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"creatorFullName", header:"${e3ps:getMessage('작성자')}", width:120, sort:"string",
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"createDateFormat", header:"${e3ps:getMessage('등록일')}", width:100,  sort:"server",
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"modifyDateFormat", header:"${e3ps:getMessage('최종수정일')}", width:100,  sort:"server",
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
	        "data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        }
	    },
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
	
	//lifecycle list
	getLifecycleList("LC_Default");
	
	getNumberCodeList("occurrenceModel", "CARTYPE", false, true);
	getNumberCodeList("occurrenceStep", "OCCURRENCESTEP", false, true);
	getNumberCodeList("occurrencePlace", "OCCURRENCEPLACE", false, true);
	getNumberCodeList("oldcarState", "OLDCARSTEP", false, false);
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
		    }).post(getURLString("/oldcar/searchOldcarScrollAction"), params)
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

function xlsxExport() {
	webix.toExcel($$("grid_wrap"), {
	   	filename: "xlsxExport",
	   	rawValues:true,
	});
}

function resetFilter() {
	$$("grid_wrap").eachColumn(function(id, col){
		var filter = this.getFilter(id);
		if(filter) {
			if(filter.setValue) filter.setValue("");
			else filter.value = "";
		}
	});
	$$("grid_wrap").filterByAll();
}
</script>