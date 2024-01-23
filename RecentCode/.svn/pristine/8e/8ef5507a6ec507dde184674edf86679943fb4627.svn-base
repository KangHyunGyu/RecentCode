<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="seach_arm2 pt5 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		${e3ps:getMessage('진행 현황')}</span>
	</div>
	<div class="rightbt">
	</div> 
</div>
<table class="mainTable mb25">
	<col width="15%">
	<col width="18%">
	<col width="15%">
	<col width="18%">
	<col width="15%">
	<col width="19%">
	<tr>
		<th>${e3ps:getMessage('기간')}[${e3ps:getMessage('업무일')}]</th>
		<td>
			${schedule.planDuration} [${schedule.planDurationHoliday}]
	    </td>
	    <th>${e3ps:getMessage('계획 시작일')}</th>
		<td>
			${schedule.planStartDateStr}
	    </td>
	    <th>${e3ps:getMessage('계획 종료일')}</th>
		<td>
			${schedule.planEndDateStr}
	    </td>
	 </tr>
	 <tr>
		<th>${e3ps:getMessage('진행')}/${e3ps:getMessage('적정')}(%)</th>
		<td>
			${schedule.completionFormat} /  ${schedule.preferCompFormat}
	    </td>
	    <th>${e3ps:getMessage('실제 시작일')}</th>
		<td>
			${schedule.startDateStr}
			<c:if test="${isAuth && isEditState && proAuth}">
        		<input type="button" class="s_bt03" value="${e3ps:getMessage('수정')}"  onclick="modifyProjectStartDate()"/>
        	</c:if>
	    </td>
	    <th>${e3ps:getMessage('실제 종료일')}</th>
		<td>
			${schedule.endDateStr}
	    </td>
	 </tr>
	 <tr>
		<th>${e3ps:getMessage('작업 현황')}</th>
		<td>
			<c:if test="${schedule.multiStart && isAuth}">
				<input type="button" value="${e3ps:getMessage('하위태스크')}" class="button02" onclick="javascript:startChildTask('${oid}')">
			</c:if>
			<c:if test="${schedule.start && isTaskAuth}">
				<input type="button" value="${e3ps:getMessage('시작')}" class="button02" onclick="javascript:startTask('${oid}')">
			</c:if>
			${schedule.stateTag}
	    </td>
	    <td colspan="4">
			<!--//진행율 그림-->
			<table class="w100">
				<colgroup>
					<col align=right width="${schedule.completion}%">
					<col align=left width="${100 - schedule.completion}%">
				</colgroup>
				<tr height=10>
					<td <c:if test="${schedule.completion != 0}">background=/Windchill/jsp/project/images/project/bar_full.gif</c:if>></td>
					<td <c:if test="${schedule.completion != 100}">background=/Windchill/jsp/project/images/project/bar_blank.gif</c:if>></td>
				</tr>
			</table>
			<!--//-->
	    </td>
	</tr>
</table>
<script type="text/javascript">
function startTask(oid){
		
	openConfirm("${e3ps:getMessage('시작하시겠습니까?')}", function(){
		var param = new Object();
			
		param.oid = "${oid}"
			
		var url = getURLString("/project/startTask");
		ajaxCallServer(url, param, function(data) {
			reloadTree();
			viewTask(data.oid);
		});
	})
}

function startChildTask(oid){
	
	openConfirm("${e3ps:getMessage('하위태스크를 모두 시작 하시겠습니까?')}", function(){
		var param = new Object();
			
		param.oid = "${oid}"
			
		var url = getURLString("/project/startChildTask");
		ajaxCallServer(url, param, function(data) {
			reloadTree();
			viewTask(data.oid);
		});
	})
}

function modifyProjectStartDate(){
	var url = getURLString("/project/modifyProjectStartDate") + "?oid=${oid}";
	openPopup(url,"changeStartDate","700","400");
}
</script>