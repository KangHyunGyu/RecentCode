<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script src="/Windchill/jsp/stagegate/stagegateHTML/modules/deliverable/deliverable.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var viewOnly = "${viewOnly}";
	if(viewOnly === "true"){
		$("button").attr("disabled", true);
		$("button").attr("hidden", true);
	}
	
	toggleImgBinding();
	
	addTableRows("ds_table", getTBodyData());
})

function getTBodyData(){
	
	let params = new Object();
	params["oid"] = "${oid}";
	params["objType"] = "${objType}";
	
	let data = new Object();
	//parent
	let url = getURLString("/gate/getParentValueList");
	data.parent = ajaxCallServer(url, params).list;
	//child
	url = getURLString("/gate/getChildValueList");
	data.child = ajaxCallServer(url, params).list;
	
	return data;
}

$("#save").click(function(e){
	//저장 버튼일 때만 작동
	if(!e.target.classList.contains("property_submit")){
		toggleUpdateBtn(e, "${e3ps:getMessage('저장')}");
		//toggleImportBtn(false);
		return;
	}
	if(!confirm("${e3ps:getMessage('저장 하시겠습니까?')}")){
		let onTab = document.querySelector("div .tap>ul>li.on");
		loadIncludePage(onTab);
		return;
	};
	var tdArr = getTableData("ds_table", 1);
	
	var url = getURLString("/gate/upsertObjectValueList");
	var param = new Object();
	param["list"] = tdArr;
	param["oid"] = "${oid}";
	param["objType"] = "${objType}";
	
	//console.log(param);
	
	ajaxCallServer(url, param, function(data){
		toggleUpdateBtn(e, "${e3ps:getMessage('수정')}");
		//toggleImportBtn(true);
	}, true);
})

$("#export").click(function(e){
	//exportExcel("ds_table", "Deliverable Status.xlsx", "Deliverable Status");
})

</script>
<input type="hidden" name="objType" value="${objType}">
<div class="seach_arm3 pl25 pt5 pr25">
	<div class="leftbt">
		<img class="pointer content--toggle" src="/Windchill/jsp/portal/images/minus_icon.png" data-target="ds">
		Deliverable Status
		<button class="sm_bt03 property_update" data-target="ds" id="save">${e3ps:getMessage('수정')}</button>
	</div>
	<div class="rightbt">
		<!-- <button class="sm_bt03 excel_export" data-target="ds" id="export">export</button> -->
	</div>
</div>

<div class="pro_table pt5 pl25 pr25 ">
	<table class="mainTable ds" summary="Deliverable Status" id="ds_table">
		<caption>
			기본 정보
		</caption>
		<colgroup>
			<col style="width:10%">
			<col style="width:auto">
			<col style="width:7%">
			<col style="width:7%">
			<col style="width:7%">
			<col style="width:7%">
			<col style="width:7%">
			<col style="width:7%">
		</colgroup>
		<thead>
			<tr>
				<th scope="col" rowspan="2" class="row1 col1">
					#
				</th>
				<th scope="col" rowspan="2" class="tac row1 col2">
					Deliverables
				</th>
				<th scope="col" class="tac" class="row1 col3">
					1
				</th>
				<th scope="col" class="tac" class="row1 col4">
					2
				</th>
				<th scope="col" class="tac" class="row1 col5">
					3
				</th>
				<th scope="col" class="tac" class="row1 col6">
					4
				</th>
				<th scope="col" class="tac" class="row1 col7">
					5
				</th>
				<th scope="col" class="tac" class="row1 col8">
					6
				</th>
			</tr>
			<tr class="height65">
				<th scope="col" class="tac row2 col3">
					Quotation
				</th>
				<th scope="col" class="tac row2 col4">
					Concept part & Proto Design
				</th>
				<th scope="col" class="tac row2 col5">
					Proto & Prod Intent Design
				</th>
				<th scope="col" class="tac row2 col6">
					Prod. Tooling
				</th>
				<th scope="col" class="tac row2 col7">
					Pre-Series
				</th>
				<th scope="col" class="tac row2 col8">
					Ramp-Up
				</th>
			</tr>
		</thead>
		<tbody id="ds_tbody">
		</tbody>
	</table>
</div>