<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	//팝업 리사이즈
	popupResize();
	
	//getNumberCodeList("material", "MATERIAL", false, true);
	//getNumberCodeList("finish", "FINISH", false, true);
	//getNumberCodeList("special_attribute", "SPECIALATTR", false, false);
	//getNumberCodeList("certification_regulations", "CERTIFICATION", false, false);
	//getNumberCodeList("carry_over", "CARRYOVER", false, true);
	
	//$("#material").val("${part.attributes.MATERIAL}");
	//$("#finish").val("${part.attributes.FINISH}");
	//$("#carry_over").val("${part.attributes.CARRY_OVER}");

	//var specialAttr = "${part.attributes.SPECIAL_ATTRIBUTE}";
	//$.each(specialAttr.split(","), function(i, e){
	//	$("#special_attribute option[value='" + e + "']").prop("selected", true);
	//});
    //
	//var certification = "${part.attributes.CERTIFICATION_REGULATIONS}";
	//$.each(certification.split(","), function(i, e){
	//	$("#certification_regulations option[value='" + e + "']").prop("selected", true);
	//});
	
});

function modify(){
	
	let relatedDocList = AUIGrid.getGridData(add_relatedDoc_myGridID);
	
	if(!validationCheck()) {
		return;
	}
	
	if(!confirm("${e3ps:getMessage('수정하시겠습니까?')}")){
		return;
	}
	
	let param = new Object(); 
	param = getFormParams('partForm', param)
	param.relatedDocList = relatedDocList;
	console.log("param",param)
	
	
	var url	= getURLString("/part/modifyPartAction");
	ajaxCallServer(url, param, function(){
		if(opener.window.search) {
			opener.window.search();
		}
	}, true);
	
}

	

function validationCheck(){
		
	let result = false;
		
	if(isEmpty(getElementValue('name'))){
		alert("${e3ps:getMessage('품명을 입력해주세요')}.");
	}else{
		result = true;
	}
			
	return result;	
}
</script>
<!-- pop -->
<div class="pop pb10">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('부품 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:modify()">${e3ps:getMessage('수정')}</button>
				<button type="button" class="s_bt03" onclick="javascript:history.back()">${e3ps:getMessage('뒤로가기')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="partForm" id="partForm" method="post">
			<!-- <input type="hidden" id="location" name="location" value=""> -->
			<input type="hidden" name="oid"  id="oid" value="${part.oid}" />
			<div class="seach_arm2 pb10">
				<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
				<div class="rightbt"></div>
			</div>
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:30%">
						<col style="width:20%">	
						<col style="width:30%">
					</colgroup>
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('부품 분류')}</th>
							<td colspan="3">${part.location}</td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('부품 번호')}</th>
							<td>${part.number}</td>
							<th scope="col">${e3ps:getMessage('부품 명')}</th>
							<td><input type="text" name="name" id="name" value="${part.name}"/></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('등록자')}</th>
							<td>${part.creatorFullName}</td>	
							<th scope="col">${e3ps:getMessage('수정자')}</th>
							<td>${part.modifierFullName}</td>				
						</tr>	
						<tr>
							<th scope="col">${e3ps:getMessage('등록일')}</th>
							<td>${part.createDate}</td>
							<th scope="col">${e3ps:getMessage('최종 수정일')}</th>
							<td>${part.modifyDate}</td>					
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('단위')}</th>
							<td colspan="3">${part.unitDisplay}</td>					
						</tr>
						
					</tbody>
				</table>
			</div>
				
				<jsp:include page="${e3ps:getIncludeURLString('/part/include_partAttributes')}" flush="true">
					<jsp:param name="oid" value="${part.oid}"/>
					<jsp:param name="module" value="modify"/>
				</jsp:include>
				
			      <jsp:include page="${e3ps:getIncludeURLString('/common/include_addObject')}" flush="true">
			         <jsp:param name="oid" value="${part.oid}"/>
			         <jsp:param name="objType" value="doc"/>
			         <jsp:param name="pageName" value="relatedDoc"/>
			         <jsp:param name="title" value="${e3ps:getMessage('관련 문서')}"/>
			         <jsp:param name="gridHeight" value="200"/>
			      </jsp:include>
				
				
		</form>
	</div>
</div>		
<!-- //pop-->
