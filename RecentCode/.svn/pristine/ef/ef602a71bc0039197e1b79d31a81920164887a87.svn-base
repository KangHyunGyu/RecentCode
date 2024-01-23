<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//팝업 리사이즈
	popupResize();
});
</script>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width:15%">
			<col style="width:35%">
			<col style="width:15%">	
			<col style="width:35%">			
		</colgroup>
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('일괄결재 번호')}</th>
				<td>${multi.number}</td>
            	<th></th>
           		<td></td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('일괄결재 명')}</th>
				<td>${multi.name}</td>	
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>${multi.stateName}</td>		
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('등록자')}</th>
				<td>${multi.ownerFullName}</td>	
				<th scope="col">${e3ps:getMessage('등록일')}</th>
				<td>${multi.createDateFormat}</td>			
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('설명')}</th>
				<td colspan="3" class="pd10">
					<div class="textarea_autoSize">
						<textarea name="description" id="description" readonly><c:out value="${multi.description }" escapeXml="false" /></textarea>
					</div>
				</td>								
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${multi.oid}"/>
					 </jsp:include>
				</td>								
			</tr>	
		</tbody>
	</table>	
</div>


<c:if test="${multi.userOnGoingApproval}">
   	<jsp:include page="${e3ps:getIncludeURLString('/approval/include_onItemDetailApproval')}" flush="true">
	    <jsp:param name="oid" value="${multi.userOnGoingApprovalOid}"/>
	    <jsp:param name="type" value="approval"/>
   	</jsp:include>
</c:if>