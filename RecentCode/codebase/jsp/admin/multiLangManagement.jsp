<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	
	var fileChange = document.getElementById('file');
	fileChange.addEventListener('change', attachFile);
	
});

function downloadTemplate(){
	
	$("#updateMultiLang").attr("method", "post");
	$("#updateMultiLang").attr("action", getURLString("/admin/multiLangTemplateDownload"));
	$("#updateMultiLang").submit();
	
}

var fileCache = null;
function attachFile(evt){
	
	if(evt.target.files.length == 0){
		fileCache = null;
		return;
	}
	
	var file = evt.target.files[0];
	
	//if(file.size > 209715200) {
	//	alert("개별 파일은 200MB를 초과해선 안됩니다.");
	//	return;
	//}
	
	fileCache = file;
	$("#fileNameDisplay").html(fileCache.name);
	
	$("#deleteIcon").css("visibility", "visible");
	
}

function addFile(){
	var input = $("#file");
	
	input.trigger('click');	
}

function deleteFileCache(){
	fileCache = null;
	$("#fileNameDisplay").html("");
	$("#deleteIcon").css("visibility", "hidden");
	
}

function saveLang(){
	
	if(fileCache == null){
		alert("${e3ps:getMessage('다국어 템플릿을 지정해주십시오.')}");
		return;
	}else{
		
		var fileName = fileCache.name;
		var fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		
		if(fileExtension != ".xlsx"){
			alert("${e3ps:getMessage('지원하는 형식이 아닙니다.')}");
			return;
		}
	}
	
	var param = new FormData();
	
	param.append("uploadTemplate", fileCache);
	var url = getURLString("/admin/multiLangTemplateUpload");
	callFormAjax(url, param, null, true);
	
}
</script>
<form id="updateMultiLang" name="updateMultiLang">
</form>
	<div class="product pop">
		<div class="con pl25 pr25 pb15" id="includePage">
		
			<div class="seach_arm2 pt10 pb5">
				<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('다국어 템플릿')}</h4></div>
				<div class="rightbt"></div>
			</div>
			<!-- pro_table -->
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:10%">
						<col style="width:40%">
					</colgroup>
					<tbody>
						<tr>
								<th>${e3ps:getMessage('템플릿 다운로드')}</th>
								<td>
									<button type="button" class="s_bt03" onclick="downloadTemplate()">${e3ps:getMessage('다운로드')}</button>
								</td>
							</tr>
							<tr>
								<th>${e3ps:getMessage('템플릿 업로드')}<span class="required">*</span></th>
								<td>
									<%-- <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
										<jsp:param name="type" value="PRIMARY"/>
						 			</jsp:include> --%>
						 			<button type="button" class="s_bt03" onclick="addFile()">${e3ps:getMessage('파일 선택')}</button>
						 			<input type="file" id="file" style="visibility:hidden; width: 1px;"></input>
						 			<span id="fileNameDisplay"></span>
						 			<span class="pointer verticalMiddle hidden" id="deleteIcon" onclick="javascript:deleteFileCache();"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'>
						 				<button type="button" class="s_bt03" onclick="saveLang()">${e3ps:getMessage('저장')}</button>
						 			</span>
								</td>
							</tr>
					</tbody>
				</table>
			</div>
		
		</div>
	</div>
