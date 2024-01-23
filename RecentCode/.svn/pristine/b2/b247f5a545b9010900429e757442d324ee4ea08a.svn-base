<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="seach_arm2 pt5 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		${e3ps:getMessage('하위 테스크 진행 현황')}</span>
	</div>
	<div class="rightbt">
	</div> 
</div>
<table class="mainTable mb25">
	<tr>
		<th class="tac">${e3ps:getMessage('테스크 명')}</th>
		<th class="tac">${e3ps:getMessage('계획 시작일')}</th>
		<th class="tac">${e3ps:getMessage('계획 종료일')}</th>
		<th class="tac">${e3ps:getMessage('기간')}(${e3ps:getMessage('일')})</th>
		<th class="tac">${e3ps:getMessage('현재')}/${e3ps:getMessage('적정')}(%)</th>
		<th class="tac">${e3ps:getMessage('작업 현황')}</th>
	</tr>
	<c:forEach var="task" items="${taskList}" varStatus="status">
		<tr>
			<td> 
			<a href="JavaScript:viewTask('${task.oid}')">${task.name}</a>
			</td>
			<td class="tac">${task.planStartDate}</td>
			<td class="tac">${task.planEndDate}</td>
			<td class="tac">${task.planDuration}</td>
			<td class="tac">${task.completionFormat}/${task.preferCompFormat}</td>
			<td class="tac">${task.stateTag}</td>
		</tr>
	</c:forEach>
</table>
