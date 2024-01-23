<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(){
	if(!checkValidate()) {
		return;
	}
	var dsList = $$("add_addDs_grid_wrap").data.serialize();
	$("#dsForm").attr("action",getURLString("/calendar/createDsAction"));
	var param = new Object();
	param["dsList"] = dsList;
	
	var msg = "${e3ps:getMessage('등록하시겠습니까?')}";
	formSubmit("dsForm", param, msg, null, true);
}

function checkValidate() {
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('대일정 이름을 입력하세요.')}");
		return false;
	}
	if($("#startDate").val() == null || $("#startDate").val() == "") {
		$("#startDate").focus();
		openNotice("${e3ps:getMessage('시작일을 입력하세요.')}");
		return false;
	}
	if($("#endDate").val() == null || $("#endDate").val() == "") {
		$("#endDate").focus();
		openNotice("${e3ps:getMessage('종료일을 입력하세요.')}");
		return false;
	}
	
	var dsList = $$("add_addDs_grid_wrap").data.serialize();
	if(dsList.length == 0) {
		openNotice("${e3ps:getMessage('자동차개발단계를 입력하세요.')}");
		return false;
	}else{
		for(var i=0;i<dsList.length;i++){
			var ds = dsList[i];
			if(ds.name == null || ds.name == ""){
				openNotice("${e3ps:getMessage('자동차개발단계 : 이름을 모두 입력해주세요.')}");
				return false;
			}
			if(ds.startDate == null || ds.startDate == ""){
				openNotice("${e3ps:getMessage('자동차개발단계 : 시작일을 모두 입력해주세요.')}");
				return false;
			}
			if(ds.endDate == null || ds.endDate == ""){
				openNotice("${e3ps:getMessage('자동차개발단계 : 종료일을 모두 입력해주세요.')}");
				return false;
			}
		}
	}
	return true;
}
</script>
<div class="product"> 
<form name="dsForm" id="dsForm" method="post">
	<input type="hidden" id="location" name="location" value="">
	<input type="hidden" id="mode" name="mode" value="input">
	<input type="hidden" id="docCode" name="docCode" value="">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('자동차 일정')}</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="save()">${e3ps:getMessage('등록')}</button>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:20%">
				<col style="width:80%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('대일정 이름')}<span class="required">*</span></th>
					<td>
						<input type="text" class="w50" id="name" name="name">
					</td>
				</tr>
				<tr>
					<th scope="col">${e3ps:getMessage('시작일 ~ 종료일')}<span class="required">*</span></th>
					<td class="calendar">
				        <input type="text" class="datePicker w25" name="startDate" id="startDate" readonly/>
						~
						<input type="text" class="datePicker w25" name="endDate" id="endDate" readonly/>
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('설명')}</th>
					<td class="pd15">
						<div class="textarea_autoSize">
							<textarea name="remark" id="remark" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
						</div>
					</td>
				</tr>
<!-- 	            <tr>  -->
<%-- 	               <th>${e3ps:getMessage('첨부파일')}</th> --%>
<!-- 	               <td> -->
<%-- 	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true"> --%>
<%-- 	                     <jsp:param name="formId" value="dsForm"/> --%>
<%-- 	                     <jsp:param name="command" value="insert"/> --%>
<%-- 	                     <jsp:param name="btnId" value="createBtn" /> --%>
<%-- 	                  </jsp:include> --%>
<!-- 	               </td> -->
<!-- 	            </tr> -->
			</tbody>
		</table>
	</div>
	<div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/calendar/include_addObject')}" flush="true">
			<jsp:param name="objType" value="ds"/>
			<jsp:param name="fType" value="create"/>
			<jsp:param name="pageName" value="addDs"/>
			<jsp:param name="title" value="${e3ps:getMessage('자동차 개발 단계')}"/>
			<jsp:param name="gridHeight" value="200"/>
		</jsp:include>
	</div>
</form>
</div>
