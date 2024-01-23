<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('부품 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt">
			</div>
			<div class="rightbt">
				<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
				<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
				<button type="button" class="s_bt03" onclick="javascript:addPart()">${e3ps:getMessage('추가')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="searchForm" id="searchForm">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<div class="pro_table">
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
						<th scope="col">${e3ps:getMessage('부품 번호')}</th>
						<td><input type="text" id="number" name="number" class="w100"></td>
						<th scope="col">${e3ps:getMessage('부품 명')}</th>
						<td><input type="text" id="name" name="name" class="w100"></td>
						<th scope="col">${e3ps:getMessage('My 부품')}</th>
						<td><input type="checkbox" id="isCreator" name="isCreator" value="true" checked></td>
					</tr>
					<%-- <tr>
						<th scope="col">${e3ps:getMessage('규격')}</th>
						<td><input type="text" id="SPECIFICATION" name="SPECIFICATION" class="w100"></td>
						<th scope="col">Maker</th>
						<td><input type="text" id="Maker" name="Maker" class="w100"></td>
						<th scope="col">${e3ps:getMessage('비고')}</th>
						<td><input type="text" id="NOTICE" name="NOTICE" class="w100"></td>
					</tr> --%>
				</tbody>
			</table>
			</div>
		</form>
	</div>
	<!-- button -->
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap"></div>
	</div>
	<!-- //table list-->
</div>		
<!-- //pop-->
<script type="text/javascript">
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
		view : "datatable",
		container : "grid_wrap",
		id : "grid_wrap",
		select : "row",
		select:"row",
		dragColumn: true,
		multiselect : multiSelect,
		scroll:"xy",
		scrollAlignY:true,
	    autoConfig:false,
		tooltip: true,
		resizeColumn:true,
		yCount: 13,
		datafetch:50,
		columns:[
			{ id:"index", header:"No.",	 css:"custom_webix_center", sort:"int",	width:40, tooltip:false},
			{ id:"check", header:"", editor:"checkbox",  template:checkType, width:40, tooltip:false,  },
			{ id:"number",	header:"${e3ps:getMessage('부품 번호')}",	fillspace:1,
				css: "custom_webix_ellipsis",
			},
			{ id:"rev",	header:"${e3ps:getMessage('버전')}", 	width:60,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"name",	header:"${e3ps:getMessage('부품 명')}", 	fillspace:1.5,
				css:"custom_webix_ellipsis",	
			},
			{ id:"location",	header:"${e3ps:getMessage('부품 분류')}", 	fillspace:1.5,
				css:"custom_webix_ellipsis",	
			},
			{ id:"stateName",	header:"${e3ps:getMessage('상태')}", 	width:80,
				css:"custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"creatorFullName",	header:"${e3ps:getMessage('작성자')}", 	width:70,
				css:"custom_webix_ellipsis custom_webix_center",		
			},
			{ id:"createDateFormat",	header:"${e3ps:getMessage('작성일')}", width:100,
				css: "custom_webix_center",	
			},
			{ id:"modifyDateFormat",	header:"${e3ps:getMessage('수정일')}", width:100,
				css: "custom_webix_center",	
			},
			{ id:"bom",		header : "BOM",	width:60, tooltip:false,
				css: "custom_webix_imgCenter",
				template: function(obj) {
					var icon = "/Windchill/jsp/portal/images/bom_icon.png";
					
					var template = "<img title='${e3ps:getMessage('BOM 보기')}' class='pointer' onclick='openBomTree(\"" + obj.oid + "\")' src='" + icon + "'/>";
					
					return template;
				}	
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
		    	item.check = true;
			},
			onBeforeUnSelect:function(selection){
				var item = $$("grid_wrap").getItem(selection.row);
				if(item){
					item.check = false;
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
})

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
		
		params["parentOid"] = "${parentOid}";
		
		let response = ajaxCallServer(getURLString("/bomEditor/searchBomPartScrollAction"), params, null);
	    $("#total").html(response.total_count);
	    $("#sessionId").val(response.sessionId);
	    
	    return response;
		
// 		params = JSON.stringify(params);
// 		return webix.ajax().headers({
// 		    	"Content-Type": "application/json; charset=UTF-8"
// 		    }).post(getURLString("/bomEditor/searchBomPartScrollAction"), params)
// 		    .then(function(response){
// 		    	var data = response.json();
		    	
// 				$("#total").html(data.total_count);
// 				$("#sessionId").val(data.sessionId);
// 		    	return data;
// 		    });
	
	});
}


//검색
function search(){
	if(!checkValidate()) {
		return;
	}
	
	$$("grid_wrap").clearAll();
	$("#sessionId").val("");
	getGridData();	
}

//검색조건 초기화
function reset(){
	$("#searchForm")[0].reset();
}

function addPart(){
	
	var selectedItem = $$("grid_wrap").getSelectedItem(true);
	
	selectedItem = selectedItem.map(function(item){
		delete item.check;
		return item;
	});

	if("${parentOid}".length > 0) {
		saveAddedPartListAction(selectedItem);
	} else {
		if(opener.window.setRootPart){
			opener.window.setRootPart(selectedItem);
			window.close();
		}	
	}
}

function saveAddedPartListAction(addItemList) {
	var param = new Object();
	
	param["parentOid"] = "${parentOid}";
	param["addItemList"] = addItemList;
	
	var url = getURLString("/bomEditor/saveAddedPartListAction");
	ajaxCallServer(url, param, function(data){
		
		if(opener.window.bomTree_addPartReloadBomChildren){
			opener.window.bomTree_addPartReloadBomChildren();
			window.close();
		}
	});
}

function checkValidate() {
	if($("#number").val() != "" || $("#name").val() != "" || $("#SPECIFICATION").val() != "" || $("#Maker").val() != "" || $("#NOTICE").val() != "") {
		return true;
	} else {
		openNotice("${e3ps:getMessage('검색 조건을 하나 이상 입력하세요.')}");
		return false;		
	}
}

</script>