<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<style>
</style>

<script src="/Windchill/jsp/stagegate/stagegateHTML/modules/risk/risk.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	getTableRows(getTBodyData());
	toggleImgBinding();
	
	var viewOnly = "${viewOnly}";
	if(viewOnly === "true"){
		$("button").attr("disabled", true);
		$("button").attr("hidden", true);
	}
})
$("#save").click(function(e) {
	//저장 버튼일 때만 작동
	if (!e.target.classList.contains("property_submit")) {
		toggleUpdateBtn(e, "${e3ps:getMessage('저장')}");
		addAddButtonRow();
    	return;
    }
	if(!confirm("${e3ps:getMessage('저장 하시겠습니까?')}")){
		let onTab = document.querySelector("div .tap>ul>li.on");
		loadIncludePage(onTab);
		return;
	};
	toggleUpdateBtn(e, "${e3ps:getMessage('수정')}");
	deleteAddButtonRow();
	
	var tdArr = getTableData("riskTable");
	var url = getURLString("/gate/upsertObjectValueList");
	var param = new Object();
	param["list"] = tdArr;
	param["oid"] = "${oid}";
	param["objType"] = "${objType}";
	
	console.log(param);
	
	ajaxCallServer(url, param, function(data){
		
	}, true);
});

function getTBodyData(){
	
	let params = new Object();
	params["oid"] = "${oid}";
	params["objType"] = "${objType}";
	//console.log(params);
	let data = new Object();
	//data
	let url = getURLString("/gate/getValueList");
	data = ajaxCallServer(url, params).list;
	
	return data;
}

$("#export").click(function(e){
	let url = getURLString("/gate/exportExcelRisk");
	let params = new Object();
	params["oid"] = "${oid}";
	params["objType"] = "${objType}";
	exportExcelForm(params, url);
})

function filePopup(oid){
	let url = getURLString("/gate/filePopup")+"?oid="+oid;
	openPopupCenter(url,"filePopup","550","300");
}
</script>

<input type="hidden" name="objType" value="${objType}">
<div class="seach_arm3 pl25 pt5 pr25">
	<div class="leftbt">
		<img class="pointer content--toggle" src="/Windchill/jsp/portal/images/minus_icon.png" data-target="risk">
		Risk
		<button class="sm_bt03 property_update" data-target="risk" id="save">${e3ps:getMessage('수정')}</button>
	</div>
	<div class="rightbt">
		<!-- <button class="sm_bt03 excel_export" data-target="risk" id="export">export</button> -->
	</div>
</div>

<div class="pro_table2 pt5 pl25 pr25 ">
	<table class="mainTable risk customTB" summary="risk" id="riskTable">
		<caption>
			기본 정보
		</caption>
		<colgroup>
			<col style="width:3%">
			<col style="width:16%">
			<col style="width:6%">
			<col style="width:2%">
			<col style="width:4%">
			<col style="width:4%">
			<col style="width:4%">
			<col style="width:6%">
			<col style="width:*%">
			<col style="width:6%">
			<col style="width:8%">
			<col style="width:8%">
		</colgroup>
		<thead>
			<tr>
				<th scope="col" rowspan="2">
					N°
				</th>
				<th scope="col" rowspan="2" class="tac">
					Risk Description
				</th>
				<th scope="col" rowspan="2" class="tac">
					Program Review Type
				</th>
				<th scope="col" rowspan="2" class="tac">
					File
				</th>
				<th scope="col" colspan="4" class="tac">
					Risk Assessment(
					<font class="font_red">R</font> /
					<font class="font_yellow">Y</font> /
					<font class="font_green">G</font>
					)
				</th>
				<th scope="col" colspan="4" class="tac">
					Recovry Plan
				</th>
			</tr>
			<tr class="height65">
				<th scope="col" class="tac pd0">
					<div class="rotate270">
						Customer
					</div>
				</th>
				<th scope="col" class="tac pd0">
					<div class="rotate270">
						Budget
					</div>
				</th>
				<th scope="col" class="tac pd0">
					<div class="rotate270">
						Timing
					</div>
				</th>
				<th scope="col" class="tac pd0">
					<div class="rotate270">
						Perform.
					</div>
				</th>
				<th scope="col" class="tac">
					Proposed Action
				</th>
				<th scope="col" class="tac">
					Leader
				</th>
				<th scope="col" class="tac">
					<div class="lineH20">
						Target <br />
						completion <br />
						date
					</div>
				</th>
				<th scope="col" class="tac">
					<div class="lineH20">
						Actual <br />
						completion <br />
						date
					</div>
				</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>