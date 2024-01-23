<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
    //팝업 리사이즈
    popupResize();
  });
</script>
<br />
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"> ${e3ps:getMessage('활동 정보')}</h4></div>
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
				<th scope="col">${e3ps:getMessage('ECO 제목')}</th>
				<td colspan="3">${eca.orderName}</td>
				
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('ECO 번호')}</th>
				<td colspan="3">
				<a href="javascript:openView('${eca.orderOid}')">
				${eca.orderNumber}
				</a>
				</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('담당자')}</th>
				<td>${eca.owner}</td>
				<th scope="col">${e3ps:getMessage('요청자')}</th>
				<td>${eca.orderCreator}</td>
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('활동')}</th>
				<td>${eca.name}</td>
				<th scope="col">${e3ps:getMessage('활동 상태')}</th>
				<td>${eca.state}</td>				
			</tr>	
			<tr>
				<th scope="col">${e3ps:getMessage('완료요청일')}</th>
				<td>${eca.finishDate}</td> 
				<th scope="col">${e3ps:getMessage('완료일')}</th>
				<td>${eca.ecaFinishDate}</td> 
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('의견')}</th>
		        <td colspan="3" class="pd10">
			        <div class="textarea_autoSize">
			        	<textarea name="description" id="description" readonly><c:out value="${eca.comment}" escapeXml="false" /></textarea>
			        </div>
		        </td>
			</tr>
		</tbody>
	</table>
</div>
<jsp:include page="${e3ps:getIncludeURLString('/change/include_ecaActiveView')}" flush="true">
	<jsp:param name="oid" value="${eca.oid}"/>
</jsp:include>
