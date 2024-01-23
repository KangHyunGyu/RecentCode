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
			{ id : "submit_number",		header : "${e3ps:getMessage('번호')}",		width:"100",	
				css: "custom_webix_ellipsis custom_webix_center",
				template:function(obj){
					var oid = obj.oid;
					var popUrl = getURLString("/distribute/viewReceipt?instance_id="+oid);
					return "<a href='#' onclick='openPopup(\""+popUrl+"\", \""+oid+"\", "+1124+", "+600+")'>"+obj.submit_number+"</a>";
				},
			},
			{ id : "submit_name",		header : "${e3ps:getMessage('제목')}",		width:"140", fillspace: true,		
				css: "custom_webix_ellipsis",
			},
			{ id : "order_typeStr",		header : "${e3ps:getMessage('발신 종류')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "submit_typeStr",		header : "${e3ps:getMessage('타입')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "submit_classification_name",		header : "${e3ps:getMessage('분류')}",		width:"10%",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "pjt_no",		header : "${e3ps:getMessage('프로젝트 번호')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "state_name",		header : "${e3ps:getMessage('상태')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "company_name",		header : "${e3ps:getMessage('업체')}",		width:"140",	
				css: "custom_webix_ellipsis",
			},
			{ id : "c_dateStr",		header : "${e3ps:getMessage('작성일')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "receptionist_name",		header : "${e3ps:getMessage('접수 담당자')}",		width:"100",
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
	
	// 상태값 리스트
	getReceiptStateList();
});

function getGridData(){
	$$("grid_wrap").load(function(params){
		var receptionists = new Array();
		
		if(!params){
			params = new Object();
			params["start"] = 0;
			params["count"] = $$("grid_wrap").config.datafetch;
			params["continue"] = true;
		}
		
		getFormParams("searchForm", params);
		//receptionists
		params["receptionists"] = receptionists; 
		
		params = JSON.stringify(params);
		return webix.ajax().headers({
		    	"Content-Type": "application/json; charset=UTF-8"
		    }).post(getURLString("/distribute/searchReceiptScrollAction"), params)
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
	$("#searchForm")[0].reset();
}

function xlsxExport() {
	webix.toExcel($$("grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
		ignore: { bom:true,  icon:true, thumbnail:true, stateTag:true},
	});
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
			<table class="mainTable">
				<colgroup>
					<col style="width:15%">
					<col style="width:35%">
					<col style="width:15%">
					<col style="width:35%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('번호')}</th>
						<td><input type="text" id="submit_number" name="submit_number" class="w100"></td>
						<th scope="col">${e3ps:getMessage('제목')}</th>
						<td><input type="text" id="submit_name" name="submit_name" class="w100"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('발신 종류')}</th>
						<td>
							<input type="radio" id="order_type" name="order_type" value="" checked>
							<label>${e3ps:getMessage('전체')}</label>
							<input type="radio" id="order_type" name="order_type" value="E">
							<label>${e3ps:getMessage('견적')}</label>
							<input type="radio" id="order_type" name="order_type" value="P">
							<label>${e3ps:getMessage('구매')}</label>
						</td>
						<th scope="col">${e3ps:getMessage('상태')}</th>
						<td>
							<select class="w30" id="state" name="state"></select>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('타입')}</th>
						<td>
							<input type="radio" id="submit_type" name="submit_type" value="" data-rolecode="" checked>
							<label>${e3ps:getMessage('전체')}</label>
							<input type="radio" id="submit_type" name="submit_type" value="epm" data-rolecode="ECF">
							<label>${e3ps:getMessage('도면')}</label>
							<input type="radio" id="submit_type" name="submit_type" value="doc" data-rolecode="DCF">
							<label>${e3ps:getMessage('문서')}</label>
						</td>
						<th scope="col">${e3ps:getMessage('분류')}</th>
						<td>
							<select class="searchCode" id="classification" name="submit_classification" data-rolecode=""  data-codetype="CLASSIFICATION" data-endlevel="2" data-width="70%"></select>
							<span class="pointer verticalMiddle" id="classificationPop" data-rolecode="" onclick="javascript:openCodePopup('classification', 'CLASSIFICATION', this);"><img class="vm" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteCode('classification');"><img class="vm" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('작성일자')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
						<th scope="col">${e3ps:getMessage('접수 담당자')}</th>
						<td>
						<div class="pro_view">
							<select class="searchUser" id="receptionists" name="receptionists" multiple data-width="60%"></select>
							<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('receptionists', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteUser('receptionists');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</div>
						</td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('프로젝트 번호')}</th>
						<td><input type="text" id="pjt_no" name="pjt_no" class="w100" placeholder="Insert Info (Number or NAME)"></td>
						<th scope="col">${e3ps:getMessage('업체')}</th>
						<td>
							<select class="searchSupplier" data-width="70%" name="company_id" id="supplierId"></select>
							<span class="pointer verticalMiddle" onclick="javascript:openSupplierPopup();"><img class="vm" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteCode('supplierId');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
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
			<select id="rows" name="rows" style="width:100px;" onchange="javascript:changeLoadingRows(this)">
				<option value="20">20</option>
				<option value="40">40</option>
				<option value="60">60</option>
				<option value="80">80</option>
				<option value="100">100</option>
			</select>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
			<img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownReceipt');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:500px"></div>
	</div>
	<!-- //table list-->
</div>