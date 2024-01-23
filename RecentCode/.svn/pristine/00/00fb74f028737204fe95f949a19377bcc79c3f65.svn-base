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
			{ id : "supplierCode",		header : "${e3ps:getMessage('업체 코드')}",		width:"140",	
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "supplierName",		header : "${e3ps:getMessage('업체 명')}",		width:"140", fillspace: true,		
				css: "custom_webix_ellipsis",
				template:function(obj){
					var oid = obj.oid;
					return "<a href='#' onclick='openView(\""+oid+"\")'>"+obj.supplierName+"</a>";
				},
			},
			{ id : "supplierID",		header : "${e3ps:getMessage('업체 아이디')}",		width:"140",		
				css: "custom_webix_ellipsis",
			},
			{ id : "nationalCode",		header : "${e3ps:getMessage('국가')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "businessCondition",		header : "${e3ps:getMessage('업태')}",		width:"140",  
				css: "custom_webix_ellipsis",
			},
			{ id : "businessType",		header : "${e3ps:getMessage('업종')}",			width:"140", 
				css: "custom_webix_ellipsis",
			},
			{ id : "email",		header : "${e3ps:getMessage('e-mail')}",			width:"180", 
				css: "custom_webix_ellipsis",
			},
			{ id : "telNo",			header : "${e3ps:getMessage('전화 번호')}",			width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "faxNo",		header : "${e3ps:getMessage('팩스 번호')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
			},
			{ id : "enabledStr",		header : "${e3ps:getMessage('활성화')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
				template:function(obj){
					return "<div>"+obj.enabled+"</div>";
				},
			},
			{ id : "enabled",		header : "${e3ps:getMessage('활성화')}",		width:"100",
				css: "custom_webix_ellipsis custom_webix_center",
				template: "{common.checkbox()}",
				checkValue: "Y",
				uncheckValue: "N",
				renderer : {
					type : "CheckBoxEditRenderer",
					editable : true, // 체크박스 편집 활성화 여부(기본값 : false)
					checkValue : "Y",
					unCheckValue : "N",
				},
				hidden: true,
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
		    }).post(getURLString("/distribute/searchSupplierScrollAction"), params)
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

// 업체정보 전송 ( PLM -> CPC 인터페이스 )
function sendCPC(){
	var editedItemList = AUIGrid.getEditedRowItems(myGridID);
	if(editedItemList.length > 0){
		alert("${e3ps:getMessage('저장되지 않은 수정사항이 있습니다.\\n저장을 먼저 진행하십시오.')}");	
		return;
	}
	if(!confirm("${e3ps:getMessage('CPC로 전송하시겠습니까?')}")) return;
	
	var dataList = AUIGrid.getGridData(myGridID); 
	var param = new Object();
	param["dataList"] = dataList;
	
	var url = getURLString("/distribute/sendSupplierToCPC");
	ajaxCallServer(url, param, function(data){}, true);
}
// 업체 활성화여부 저장
function supplierSave(){
	if(!confirm("${e3ps:getMessage('저장하시겠습니까?')}")) return;
	
	var editedItemList = AUIGrid.getEditedRowItems(myGridID);
	var param = new Object();
	param["editedItemList"] = editedItemList;
	
	var url = getURLString("/distribute/saveSupplierAction");
	ajaxCallServer(url, param, function(data){
		getGridData();
	}, true);
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
		<c:if test="${e3ps:isAdmin()}">
			<button type="button" class="s_bt03" id="supplierBtn" onclick="supplierSave();">${e3ps:getMessage('저장')}</button>
			<button type="button" class="s_bt03" onclick="sendCPC();">${e3ps:getMessage('CPC 전송')}</button>
		</c:if>
			<button type="button" class="s_bt03" onclick="resetFilter();">${e3ps:getMessage('필터 초기화')}</button>
			<button type="button" class="s_bt03" onclick="xlsxExport();">${e3ps:getMessage('엑셀 다운로드')}</button>
			<img class="pointer mb5" onclick="excelDown('searchForm', 'excelDownSupplier');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div class="list" id="grid_wrap" style="height:500px"></div>
	</div>
	<!-- //table list-->
</div>