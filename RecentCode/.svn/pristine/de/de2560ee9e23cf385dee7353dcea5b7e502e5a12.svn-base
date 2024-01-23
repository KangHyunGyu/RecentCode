<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	//팝업 리사이즈
// 	popupResize();
});
</script>
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('기본 정보')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width:11%">
			<col style="width:22%">
			<col style="width:11%">	
			<col style="width:22%">
			<col style="width:33%">			
		</colgroup>
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('부품 분류')}</th>
				<td colspan="3">${part.location}</td>
				<td rowspan="7" style="border-left:1px solid #dedede;" align="center">
					<a href="${part.publishURL}" >
						<img src="${part.thumbnail}"  border="0" style="display:block; height:100%; width:auto;">
					</a>
					<div style="max-width: 1px; max-height: 1px;">
						<form action="" id="pvsForm" style="visibility: hidden;">
						<iframe name="executeFrame"></iframe>
						</form>
					</div>
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('부품 번호')}</th>
				<td>${part.number}</td>
				<th scope="col">${e3ps:getMessage('부품 명')}</th>
				<td>${part.icon}${part.name}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('상태')}</th>
				<td>${part.stateName}</td>	
				<th scope="col">${e3ps:getMessage('버전')}</th>
				<td>${part.version}</td>				
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
			
			<%-- <tr>
				<th scope="col">${e3ps:getMessage('BOM End Item')}</th>
				<td style="padding-top: 2px; padding-bottom: 4px;">
					<c:forEach items="${part.bomEndItemHash}" var="endItem">
						<c:if test="${endItem.key ne part.oid}">
							<a style="padding-bottom: 2px;" href="javascript:openView('${endItem.key}')">${endItem.value}</a><br/>
						</c:if>
					</c:forEach>
				</td>
			</tr> --%>
		</tbody>
	</table>
</div>
<!-- //pro_table -->

<jsp:include page="${e3ps:getIncludeURLString('/part/include_partAttributes')}" flush="true">
	<jsp:param name="oid" value="${part.oid}"/>
	<jsp:param name="module" value="view"/>
</jsp:include>

<%--
<c:if test="${part.userOnGoingApproval}">
   	<jsp:include page="${e3ps:getIncludeURLString('/approval/include_onItemDetailApproval')}" flush="true">
	    <jsp:param name="oid" value="${part.userOnGoingApprovalOid}"/>
	    <jsp:param name="type" value="approval"/>
   	</jsp:include>
</c:if>
 --%>