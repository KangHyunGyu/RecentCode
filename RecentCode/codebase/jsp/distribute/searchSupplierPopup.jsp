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
		container:"sup_grid_wrap",
		id:"sup_grid_wrap",
		tooltip:true,
		yCount:15,
		clipboard:"block",
		multiselect:false,
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
			{ id:"mark", header:"", 
				template:"{common.radio()}", checkValue:"On", uncheckValue:"Off", width:40
			},
			{ id:"supplierCode", header:"${e3ps:getMessage('업체 코드')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"supplierName", header:"${e3ps:getMessage('업체명')}", width:100,fillspace:true,
				css: "custom_webix_ellipsis",
				template:function(obj){
					var oid = obj.oid;
					return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.supplierName+"</a>";
				}	
			},
			{ id:"supplierID", header:"${e3ps:getMessage('업체 아이디')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"nationalCode", header:"${e3ps:getMessage('국가')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"businessCondition", header:"${e3ps:getMessage('업태')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"businessType", header:"${e3ps:getMessage('업종')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"email", header:"${e3ps:getMessage('e-mail')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"telNo", header:"${e3ps:getMessage('전화 번호')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"faxNo", header:"${e3ps:getMessage('팩스 번호')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
		],
		on:{
			onHeaderClick:function(header, event, target) {
				var column = $$("sup_grid_wrap").getColumnConfig(header.column);
				
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
	
	webix.extend($$("sup_grid_wrap"), webix.ProgressBar);
	
	$(".webix_excel_filter").click(function(){
		 $$("sup_grid_wrap").unselect(); 
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
	$$("sup_grid_wrap").load(function(params){
		
		if(!params){
			params = new Object();
			params["start"] = 0;
			params["count"] = $$("sup_grid_wrap").config.datafetch;
			params["continue"] = true;
		}
		
		getFormParams("searchForm", params);
		
		params = JSON.stringify(params);
		return webix.ajax().headers({
		    	"Content-Type": "application/json; charset=UTF-8"
		    }).post(getURLString("/distribute/searchSupplierScrollAction"), params)
		    .then(function(response){
		    	var data = response.json();
		    	
				$("#total").html(data.total_count);
				$("#sessionId").val(data.sessionId);
		    	return data;
		    });
	
	});
}
function addSupplier(){
	var selectedItem = new Array();
	var checkList = $$("sup_grid_wrap").data.serialize();
	for(var i=0; i<checkList.length; i++) {
		if(undefined != checkList[i]){
			if(checkList[i].mark == "On") {
				selectedItem.push(checkList[i]);
			}
		}
	}
	if(opener.window.add_supplier){
		opener.window.add_supplier(selectedItem);
	}
	window.close();
	
}
</script>
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('업체 검색')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt">
				<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			</div>
			<div class="rightbt">
				<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
				<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
				<button type="button" class="s_bt03" onclick="javascript:addSupplier()">${e3ps:getMessage('추가')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="searchForm" id="searchForm">
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:30%">
						<col style="width:20%">
						<col style="width:30%">
					</colgroup>	
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('업체 코드')}</th>
							<td><input type="text" id="supplierCode" name="supplierCode" class="w100"></td>
							<th scope="col">${e3ps:getMessage('업체 명')}</th>
							<td><input type="text" id="supplierName" name="supplierName" class="w100"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('업체 아이디')}</th>
							<td><input type="text" id="supplierID" name="supplierID" class="w100"></td>
							<th scope="col">${e3ps:getMessage('업태')}</th>
							<td><input type="text" id="businessCondition" name="businessCondition" class="w100"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('업종')}</th>
							<td><input type="text" id="businessType" name="businessType" class="w100"></td>
							<th scope="col">${e3ps:getMessage('국가')}</th>
							<td><input type="text" id="nationalCode" name="nationalCode" class="w100"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>	
	</div>
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="sup_grid_wrap"></div>
	</div>
	<!-- //table list-->
</div>