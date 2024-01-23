<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="seach_arm2 pt5 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		${e3ps:getMessage('선후행 정보')}</span>
	</div>
	<div class="rightbt">
		<c:if test="${isTaskEdit && !isChild}">
			<input type="button" class="s_bt03" value="${e3ps:getMessage('편집')}" onclick="javascript:insertPre();"/>
		</c:if>
	</div> 
</div>
<table class="mainTable mb25">
	<colgroup>
		<col style="width:15%">
		<col style="width:*">
	</colgroup>	
	<tbody>
		<tr>
			<th width="90">${e3ps:getMessage('선행 태스크')}</th>
			<td>
				<div class="pt5 pb5">
					<table class="mainTable">
				  	 	<colgroup>
							<col style="width:*">
							<col style="width:15%">
							<col style="width:15%">
							<col style="width:15%">
						</colgroup>	
						<tbody>
							<tr>
								<th>${e3ps:getMessage('태스크 명')}</th>
								<th>${e3ps:getMessage('계획 시작일')}</th>
								<th>${e3ps:getMessage('계획 종료일')}</th>
								<th>${e3ps:getMessage('작업 현황')}</th>
							</tr>
							<c:forEach var="preTask" items="${preTaskList}" varStatus="status">
								<tr>
									<td><a href="javascript:viewTask('${preTask.oid}')">${preTask.name}</a></td>
									<td>${preTask.planStartDate}</td>
									<td>${preTask.planEndDate}</td>
									<td>${preTask.stateTag}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<th width="90">${e3ps:getMessage('후행 태스크')}</th>
			<td>
				<div class="pt5 pb5">
					<table class="mainTable">
				  	 	<colgroup>
							<col style="width:*">
							<col style="width:15%">
							<col style="width:15%">
							<col style="width:15%">
						</colgroup>	
						<tbody>
							<tr>
								<th>${e3ps:getMessage('태스크 명')}</th>
								<th>${e3ps:getMessage('계획 시작일')}</th>
								<th>${e3ps:getMessage('계획 종료일')}</th>
								<th>${e3ps:getMessage('작업 현황')}</th>
							</tr>
							<c:forEach var="postTask" items="${postTaskList}" varStatus="status">
								<tr>
									<td><a href="javascript:viewTask('${postTask.oid}')">${postTask.name}</a></td>
									<td>${postTask.planStartDate}</td>
									<td>${postTask.planEndDate}</td>
									<td>${postTask.stateTag}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</td>
		</tr>
	</tbody>
</table>
<script type="text/javascript">
function insertPre(){
	
	var oid = "${oid}";
	
	var url = getURLString("/project/selectPreTask") + "?oid=" + oid;

	openPopup(url, "selectPreTask", 600, 800);
}
</script>