<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script src="/Windchill/jsp/stagegate/stagegateHTML/modules/c_stop/c_stop.js"></script>
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
$("#save").click(function(e){
	//저장 버튼일 때만 작동
	if(!e.target.classList.contains("property_submit")){
		toggleUpdateBtn(e, "${e3ps:getMessage('저장')}");
		toggleImportBtn(false);
		toggleSpanInputView();
		return;
	}
	
	var tdArr = getTableData("cstopTable");
	var url = getURLString("/gate/modifyObjectValueList");
	var param = new Object();
	param["list"] = tdArr;
	
	console.log(param);
	if(!confirm("${e3ps:getMessage('저장 하시겠습니까?')}")){
		let onTab = document.querySelector("div .tap>ul>li.on");
		loadIncludePage(onTab);
		return;
	};
	
	ajaxCallServer(url, param, function(data){
		toggleUpdateBtn(e, "${e3ps:getMessage('수정')}");
		toggleImportBtn(true);
		toggleSpanInputView();
	}, true);
})
$("#import").click(function(e){
	let url = getURLString("/gate/importExcelCStop?cOid=${oid}&cObjType=${objType}");
	openPopupCenter(url,"Import C_Stop","550","235");
})

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

function viewObject(oid){
	var viewOnly = "${viewOnly}";
	if(viewOnly === "true"){
		return false;
	}
	
	var url = getURLString("/gate/viewObject") + "?oid="+oid;
	openPopup(url,"viewStageGate","700","400");
}

function filePopup(){
	let url = getURLString("/gate/filePopup")+"?oid="+"${objOid}";
	openPopupCenter(url,"filePopup","550","300");
}
</script>

<input type="hidden" name="objType" value="${objType}">
<div class="seach_arm3 pl25 pt5 pr25">
	<div class="leftbt">
		<img class="pointer content--toggle" src="/Windchill/jsp/portal/images/minus_icon.png" data-target="c_stop">
		C_Stop
		<button class="sm_bt03 property_update" data-target="c_stop" id="save">${e3ps:getMessage('수정')}</button>
	</div>
	<div class="rightbt">
		 <img src="/Windchill/netmarkets/images/doc_document.gif" onclick="filePopup()">
		<button class="sm_bt03 excel_import" data-target="c_stop" id="import">import</button>
	</div>
</div>

<div class="pro_table pt5 pl25 pr25 ">
	<table class="mainTable c_stop" summary="c_stop" id="cstopTable">
		<caption>
			기본 정보
		</caption>
		<colgroup>
			<col style="width:auto">
			<col style="width:7%">
			<col style="width:5%">
			<col style="width:7%">
			<col style="width:5%">
			<col style="width:7%">
			<col style="width:5%">
			<col style="width:7%">
			<col style="width:5%">
			<col style="width:7%">
			<col style="width:5%">
			<col style="width:7%">
			<col style="width:5%">
			<col style="width:7%">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">KRW</th>
				<th scope="col" class="tac">GR1</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">GR2</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">GR3</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">GR4</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">GR5</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">GR6</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">Target</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>