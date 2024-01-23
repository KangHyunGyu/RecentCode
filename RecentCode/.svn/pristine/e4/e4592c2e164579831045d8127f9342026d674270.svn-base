<%@page import="com.e3ps.project.key.ProjectKey.STATEKEY"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="pop">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="searchBtn" onclick="addCalendar();">${e3ps:getMessage('수정')}</button>
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table -->
	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm">
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
						<th scope="col">${e3ps:getMessage('대일정 이름')}</th>
						<td colspan="3"><input type="text" class="w100" id="name" name="name"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('시작일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="startDate" id="startDate" readonly/>
							~
							<input type="text" class="datePicker w25" name="startDateE" id="startDateE" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('종료일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="endDate" id="endDate" readonly/>
							~
							<input type="text" class="datePicker w25" name="endDateE" id="endDateE" readonly/>
						</td>
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
	
	var checkType = "{common.checkbox()}";
	var checkHeader = { content:"masterCheckbox" };
	var multiSelect = true;
	if("${type}" === "single") {
		checkType = "{common.radio()}";
		checkHeader = "";
		multiSelect = false;
	}
	
	var grid = webix.ui({
		view:"datatable",
		container:"grid_wrap",
		id:"grid_wrap",
		tooltip:true,
		yCount:15,
		clipboard:"block",
		multiselect:multiSelect,
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
			{ id:"check", header:checkHeader, editor:"checkbox", width:40, tooltip:false,  
				template:function(obj, common, value, config){
					if("${type}" == "single") {
						return common.radio(obj, common, value, config);
					}else {
						return common.checkbox(obj, common, value, config);
					}
					return "";
		        }	
			},
			{ id:"name", header:"${e3ps:getMessage('대일정 이름')}" ,width:150 , sort:"server",
				css: "custom_webix_ellipsis",
				template:function(obj){
					var oid = obj.oid;
					var url = getURLString("/calendar/viewDs") + "?oid=" + oid;
					return "<a onclick=viewDs('"+oid+"')>"+obj.name+"</a>";
				}
			},
			{ id:"remark", header:"${e3ps:getMessage('설명')}", width:100,fillspace:true,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"startDate", header:"${e3ps:getMessage('시작일')}",	sort:"server",	width:120 ,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"endDate", header:"${e3ps:getMessage('종료일')}" , sort:"server", width:120,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"creatorFullName", header:"${e3ps:getMessage('작성자')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"createDate", header:"${e3ps:getMessage('작성일')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"modifyDate", header:"${e3ps:getMessage('수정일')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
		],
		on:{
			onCheck: function(row, column, state) {
		        if (state) {
		        	if(!multiSelect){
		        		this.unselectAll();
		        	}
		            this.select(row, true);
		        } else {
		            this.unselect(row);
		        }
		    },
		    onBeforeSelect: function(selection, preserve){
		    	var item = $$("grid_wrap").getItem(selection.row);
		    	
		    	if(item.select){
			    	item.check = true;
		    	}
			},
			onBeforeUnSelect:function(selection){
				var item = $$("grid_wrap").getItem(selection.row);
				if(item){
					if(item.select){
						item.check = false;
					}
				}
			},
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
	
	webix.extend($$("grid_wrap"), webix.ProgressBar);
	
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
		    }).post(getURLString("/calendar/searchDsScrollAction"), params)
		    .then(function(response){
		    	var data = response.json();
		    	
				$("#total").html(data.total_count);
				$("#sessionId").val(data.sessionId);
		    	return data;
		    });
	
	});
}

//검색조건 초기화
function reset(){
	$("#searchForm")[0].reset();
}

function xlsxExport() {
	webix.toExcel($$("grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
		ignore: { bom:true,  icon:true, thumbnail:true, stateTag:true},
	});
}

//검색
function search(){
	$$("grid_wrap").clearAll();
	$("#sessionId").val("");
	getGridData();
}

function viewDs(oid){
	var url = getURLString("/calendar/viewDs") + "?oid="+oid;
	
	openPopup(url,"viewDs","1000","600");
	
}

function addCalendar(){
	var selectedItem = new Array();
	
	var checkList = $$("grid_wrap").data.serialize(true);
	for(var i=0; i<checkList.length; i++) {
		if("${moduleType}" == "multiApproval"){
			if(checkList[i].check && checkList[i].state == "INWORK") {
				selectedItem.push(checkList[i]);			
			}
		}else{
			if(checkList[i].check) {
				selectedItem.push(checkList[i]);			
			}
		}
		
	}
	
	selectedItem = selectedItem.map(function(item){
		delete item.check;
		return item;
	});
	var listStringData = JSON.stringify(selectedItem);
	var param = new Object();
	var list = JSON.parse(listStringData);
	if(list.length > 0){
		param["soid"] = list[0].oid;
		param["poid"] = "${poid}";
	}else{
		openNotice("${e3ps:getMessage('대일정을 선택하세요.')}");
		return false;
	}
	
	
	var url = getURLString("/calendar/modifyLinkAction");
	ajaxCallServer(url, param, function(data){
		opener.window.location.reload();
	}, false);
	
// 	if("${type}" == "single") {
// 		window.close();
// 	}
}
</script>
