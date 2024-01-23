<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript" src="/Windchill/jsp/js/distribute.js"></script>
<script>
if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();

webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"grid_wrap",
		id:"grid_wrap",
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
			{ id:"orderTypeStr", header:"${e3ps:getMessage('발주 타입')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"requestNumber", header:"${e3ps:getMessage('견적(구매) 번호')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"distributeNumber", header:"${e3ps:getMessage('배포 번호')}" ,width:150 , fillspace:true,sort:"server",
				css: "custom_webix_ellipsis",
				template:function(obj){
					var oid = obj.oid;
					return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.distributeNumber+"</a>";
				}
			},
			{ id:"supplierName", header:"${e3ps:getMessage('배포 업체')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"info", header:"${e3ps:getMessage('')}" ,width:150 , sort:"server",
				css: "custom_webix_ellipsis",
				template:function(obj){
					return "<img class='pointer' src='/Windchill/netmarkets/images/details.gif' onclick='javascript:openItemInfo(\"" + obj.dis_oid + "\",\"" + obj.part_oid + "\",\"" + obj.partNumber + "\");'>";
				}
			},
			{ id:"partNumber", header:"${e3ps:getMessage('부품 번호')}" , sort:"server", width:120,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"epmNumber", header:"${e3ps:getMessage('도면 번호')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"linkStateStr", header:"${e3ps:getMessage('배포 상태')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"downloadDeadline", header:"${e3ps:getMessage('견적(납품) 일')}", width:100,
				css: "custom_webix_ellipsis custom_webix_center",	
			},
			{ id:"requesterName", header:"${e3ps:getMessage('담당자')}", width:100,
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

function openItemInfo(oid, item_oid, sub_obj_no){
	var url = getURLString("/distribute/viewItemInfo") + "?oid=" + oid + "&item_oid=" + item_oid + "&sub_obj_no=" + sub_obj_no;
	
	openPopup(url, oid);
}

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
		    }).post(getURLString("/distribute/searchDistributePartScrollAction"), params)
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
</script>
<div class="product">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
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
						<th scope="col">${e3ps:getMessage('견적(구매) 번호')}</th>
						<td><input type="text" id="requestNumber" name="requestNumber" class="w100"></td>
						<th scope="col">${e3ps:getMessage('발주 타입')}</th>
						<td>
							<input type="radio" id="orderType" name="orderType" value="" checked>
							<label>${e3ps:getMessage('전체')}</label>
							<input type="radio" id="orderType" name="orderType" value="E">
							<label>${e3ps:getMessage('견적 발주')}</label>
							<input type="radio" id="orderType" name="orderType" value="P">
							<label>${e3ps:getMessage('구매 발주')}</label>
							<input type="radio" id="orderType" name="orderType" value="I">
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('배포 업체')}</th>
						<td>
							<select class="searchSupplier" data-width="70%" name="supplierId" id="supplierId"></select>
						<span class="pointer verticalMiddle" onclick="javascript:openSupplierPopup();"><img class="vm" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteCode('supplierId');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</td>
						<th scope="col">${e3ps:getMessage('배포 번호')}</th>
						<td><input type="text" id="distributeNumber" name="distributeNumber" class="w100"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('부품')}</th>
						<td>
							<div class="pro_view">
								<select class="searchRelatedObject" id="relatedPart" name="relatedPart" multiple data-param="part"></select>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('도면 번호')}</th>
						<td><input type="text" id="epmNumber" name="epmNumber" class="w100"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('배포 상태')}</th>
						<td><select class="multiSelect w30" id="linkState" name="linkState" multiple style="height:20px;overflow-y: hidden;"></select></td>
						<th scope="col">${e3ps:getMessage('견적(납품) 일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('담당자')}</th>
						<td>
							<div class="pro_view">
								<select class="outPeople" id="outPeople" name="outPeople" multiple data-type="all" data-width="80%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openOutpeoplePopup('outPeople', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('outPeople');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<td colspan="2"></td>
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
			<select id="rows" name="rows" style="width:100px;" onchange="javascript:changeLoadingRows(this)">
				<option value="20">20</option>
				<option value="40">40</option>
				<option value="60">60</option>
				<option value="80">80</option>
				<option value="100">100</option>
			</select>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
			<img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownDistributePart');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap"></div>
	</div>
	<!-- //table list-->
</div>