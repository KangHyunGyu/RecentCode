<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="seach_arm2 pt5 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		${e3ps:getMessage('중단/재시작 이력')}</span>
	</div>
	<div class="rightbt">
	</div> 
</div>
<table class="mainTable mb25">
	<col width="15%">
	<col width="15%">
	<col width="*">
	<%-- <col width="20%"> --%>
	<tr>
		<th class="tac">${e3ps:getMessage('구분')}</th>
	    <th class="tac">${e3ps:getMessage('중단/재시작 일자')}</th>
	    <th class="tac">${e3ps:getMessage('사유')}</th>
	    <%-- <th class="tac">${e3ps:getMessage('참조 파일')}</th> --%>
	</tr>
	<c:forEach var="history" items="${stopHistoryList}" varStatus="status">
		<tr>
			<td class="tac">${history.gubun}</td>
			<td class="tac">${history.createDate}</td>
			<td> ${history.historyComment}</td>
			<%-- <td class="tac">
				<c:if test="${history.fileName.length() > 0}">
					<a href="${history.primaryURL}">${history.fileIcon} ${history.fileName}</a>
				</c:if>
			</td> --%>
		</tr>
	</c:forEach>
</table>
 
