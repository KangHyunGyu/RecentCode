<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){

});

<%----------------------------------------------------------
*                      임시 저장 및 등록 버튼
----------------------------------------------------------%>
function save(btn){
	if(!checkValidate()) {
		return;
	}
	$("#epmForm").attr("action",getURLString("/epm/createMultiEpmAction"));
	
	var param = new Object();

	param["container"] = $("#container").val();
	
	//param["multiEpmList"] = multiEpmList;
	
	formSubmit("epmForm", param, "${e3ps:getMessage('등록하시겠습니까?')}", function(data){
		var resultList = data.resultList;
		
		$$("multi_epm_grid_wrap").parse(resultList);

	}, true);
}

function checkValidate() {
	
	if($("[id=PRIMARY]").length == 0){
		openNotice("${e3ps:getMessage('템플릿 파일을 첨부하여 주십시오.')}");
		return false;
	}
	
	if($("[id=SECONDARY]").length == 0){
		openNotice("${e3ps:getMessage('도면 파일을 첨부하여 주십시오.')}");
		return false;
	}
	
	return true;
}
</script>
<div class="product"> 
	<form name="epmForm" id="epmForm" method="post">
	<input type="hidden" id="location" name="location" value="">
	<input type="hidden" name="lifecycle"	id="lifecycle" 	value="LC_Default" />
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
			<button type="button" class="s_bt03" onclick="save(this)">${e3ps:getMessage('등록')}</button>
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
						<th scope="col">${e3ps:getMessage('도면 분류')}</th>
						<td><input type="text" id="locationDisplay" name="locationDisplay" class="w100" disabled></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('템플릿 파일')}&nbsp;<img src="/Windchill/jsp/portal/icon/fileicon/xls.gif" title="${e3ps:getMessage('템플릿 다운로드')}" border="0" onclick="excelDown('epmForm', 'excelDownEpmUploadTemplate')"></th>
						<td>
							<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
								<jsp:param name="formId" value="epmForm"/>
								<jsp:param name="command" value="insert"/>
								<jsp:param name="type" value="PRIMARY"/>
								<jsp:param name="btnId" value="createBtn" />
							</jsp:include>
						</td>
					</tr>
		            <tr> 
		               <th>${e3ps:getMessage('도면 파일')}</th>
		               <td>
		                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
		                     <jsp:param name="formId" value="epmForm"/>
		                     <jsp:param name="command" value="insert"/>
		                     <jsp:param name="btnId" value="createBtn" />
		                     <jsp:param name="dropHeight" value="300" />
		                     <jsp:param name="fileCount" value="100" />
		                  </jsp:include>
		               </td>
		            </tr>
				</tbody>
			</table>
		</div>
		
		<!-- 도면 일괄 등록 리스트 include 화면 -->
		<div class="ml30 mr30">
			<jsp:include page="${e3ps:getIncludeURLString('/epm/include_multiEpmList')}" flush="true"/>
		</div>
	</form>
</div>