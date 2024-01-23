<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<form name="projectForm" id="projectForm" method="post">
		<div class="seach_arm pt20 pb5">
			<div class="leftbt">
				<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('프로젝트 등록')}</span>
			</div>
			<div class="rightbt">
				<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
				<button type="button" class="s_bt03" onclick="createAction()">${e3ps:getMessage('등록')}</button>
				<%-- <button type="button" class="s_bt03" onclick="copy()">${e3ps:getMessage('복사')}</button> --%>
			</div>
		</div>
		<div class="pro_table mr30 ml30">
			<table class="mainTable">
				<colgroup>
					<col style="width:20%">
					<col style="width:30%">
					<col style="width:20%">
					<col style="width:30%">
				</colgroup>	
				<tbody>
					<tr>
						<th>${e3ps:getMessage('생성 타입')}<span class="required">*</span></th>
						<td colspan="3">
							<label><input type="checkbox" name="createType" value="new" onclick="javascript:checkView(this)" checked>${e3ps:getMessage('신규 프로젝트 생성')}</label>
							<label><input type="checkbox" name="createType" value="copy" onclick="javascript:checkView(this)">${e3ps:getMessage('복제 프로젝트 생성')}</label>
						</td>
					</tr>
					<tr style="display:none;" class="basic">
						<th>${e3ps:getMessage('적용 템플릿')}<span class="required">*</span></th>
						<td class="pt5" colspan="3">
							<div class="pro_view">
								<select class="searchTemplate" id="template" name="template" data-width="70%" onchange="showProjectType(this)"></select>
								<span class="pointer verticalMiddle" onclick="javascript:deleteCode('template');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
					</tr>
					
					<tr style="display:none;" class="copyTemp">
						<th>${e3ps:getMessage('복사할 프로젝트 선택')}<span class="required">*</span></th>
						<td class="pt5" colspan="3">
							<select class="searchRelatedProject" id="relatedProject" name="relatedProject" data-width="70%"></select>
							<span class="pointer verticalMiddle" onclick="javascript:deleteCode('relatedProject');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</td>
					</tr>
					
					<tr>
						<th>${e3ps:getMessage('프로젝트 명')}<span class="required">*</span></th>
						<td colspan="3">
							<input type="text" class="w50" id="name" name="name">
						</td>
					</tr>
					
					<tr style="display:none;" class="basic">
						<th>Group<span class="required">*</span></th>
						<td><select class="w100" id="groupCode" name="groupCode"></select></td>
						<th>Material<span class="required">*</span></th>
						<td><select class="w100" id="materialCode" name="materialCode"></select></td>
					</tr>
					
					<tr style="display:none;" class="basic">
						<th>Level<span class="required">*</span></th>
						<td><select class="w100" id="levelCode" name="levelCode"></select></td>
						<th></th>
						<td></td>
					</tr>
					
					<tr>
						<th>PM<span class="required">*</span></th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="pm" name="pm" data-width="70%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('pm', 'single');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('pm');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th>${e3ps:getMessage('계획 시작일자')}<span class="required">*</span></th>
						<td class="calendar">
							<input type="text" class="datePicker w50" name="planStartDate" id="planStartDate" readonly/>
						</td>
					</tr>
					
					<tr>
						<th>${e3ps:getMessage('설명')}</th>
						<td class="pt5" colspan="3">
							<textarea name="description" id="description" style="width:90%;"></textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	<!-- END -->
	</form>
</div>
<script type="text/javascript">
$(document).ready(function(){
	//getNumberCodeList("group", "PROJECTNUMBERPROP", false, true);
	
	getNumberCodeChildList("groupCode", "PROJECTNUMBERPROP", "GROUP", true, true, true);
	getNumberCodeChildList("materialCode", "PROJECTNUMBERPROP", "MATERIAL", true, true, true);
	getNumberCodeChildList("levelCode", "PROJECTNUMBERPROP", "LEVEL", true, true, true);
	
	let obj = document.getElementsByName("createType");
	checkView(obj[0]);
	
});
function save(){
	if(!checkValidate()) {
		return;
	}
	$("#projectForm").attr("action",getURLString("/project/createProjectAction"));
	var param = new Object();
	formSubmit("projectForm", param, "${e3ps:getMessage('등록하시겠습니까?')}", function(){}, true);
}


function copy(){
	if(!checkValidateCopy()) {
		return;
	}
	$("#projectForm").attr("action",getURLString("/project/copyProjectCreate"));
	var param = new Object();
	formSubmit("projectForm", param, "${e3ps:getMessage('복사하시겠습니까?')}", function(){}, true);
}

function checkValidateCopy(){
	if($("#relatedProject").val() == null || $("#relatedProject").val() == "") {
		$("#relatedProject").focus();
		openNotice("${e3ps:getMessage('복사할 프로젝트를 선택하세요.')}");
		return false;
	}
	
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('프로젝트 명을 선택하세요.')}");
		return false;
	}
	if($("#pm").val() == null || $("#pm").val() == "") {
		$("#pm").focus();
		openNotice("${e3ps:getMessage('PM을 선택하세요.')}");
		return false;
	}
	
	if($("#planStartDate").val() == null || $("#planStartDate").val() == "") {
		$("#planStartDate").focus();
		openNotice("${e3ps:getMessage('계획 시작일자를 입력하세요.')}");
		return false;
	}
	return true;
}

function checkValidate() {
	if($("#template").val() == null || $("#template").val() == "") {
		$("#template").focus();
		openNotice("${e3ps:getMessage('적용 템플릿을 선택하세요.')}");
		return false;
	}
	
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('프로젝트 명을 선택하세요.')}");
		return false;
	}
	
	if($("#groupCode").val() == null || $("#groupCode").val() == "") {
		$("#groupCode").focus();
		openNotice("${e3ps:getMessage('gorup 을 선택하세요.')}");
		return false;
	}
	
	if($("#materialCode").val() == null || $("#materialCode").val() == "") {
		$("#materialCode").focus();
		openNotice("${e3ps:getMessage('material 을 선택하세요.')}");
		return false;
	}
	
	if($("#levelCode").val() == null || $("#levelCode").val() == "") {
		$("#levelCode").focus();
		openNotice("${e3ps:getMessage('level 을 선택하세요.')}");
		return false;
	}
	
	if($("#pm").val() == null || $("#pm").val() == "") {
		$("#pm").focus();
		openNotice("${e3ps:getMessage('PM을 선택하세요.')}");
		return false;
	}
	
	if($("#planStartDate").val() == null || $("#planStartDate").val() == "") {
		$("#planStartDate").focus();
		openNotice("${e3ps:getMessage('계획 시작일자를 입력하세요.')}");
		return false;
	}
	return true;
}

function showProjectType(select) {
	var id = $(select).attr("id");
	var item = $("#" + id).select2("data")[0];
	if(item == null || item.name.length == 0) {
		return;
	} 
	$("#projectType").val(item.outputDisplay);
}

function checkView(obj){
	let selectBox = document.getElementsByName("createType");
	
	selectBox.forEach((sb) =>{
		sb.checked = false;
	});
	obj.checked = true;
	
	if(obj.value == "new"){
		$(".basic").each(function(){
			$(".basic").show();
		});
		$(".copyTemp").each(function(){
			$(".copyTemp").hide();
		});
	}else if(obj.value == "copy"){
		$(".basic").each(function(){
			$(".basic").hide();
		});
		$(".copyTemp").each(function(){
			$(".copyTemp").show();
		});
	}
}

function createAction(){
	console.log("createAction");
	let size =  document.getElementsByName("createType").length;
	let selectType = "";
	for(let i=0; i<size; i++){
		if(document.getElementsByName("createType")[i].checked == true){
			selectType += document.getElementsByName("createType")[i].value;
		}
	}
	if(selectType == "new"){
		save();
	}else if(selectType == "copy"){
		copy();
	}else if(selectType == ""){
		alert("생성타입을 선택하세요.");
	}
}
</script>
