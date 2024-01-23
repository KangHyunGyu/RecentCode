<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree_top3 pl15 pr15" style="margin-bottom:30px;">
	<div class="pro_table mr25 ml25 mb25">
		<div class="seach_arm2 pt5 pb5">
			<div class="leftbt">
				<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
				${e3ps:getMessage('기본 정보')}</span>
			</div>
			<div class="rightbt">
				<c:if test="${isAuth && isEditState}">
					<input type="button" class="s_bt03" value="${e3ps:getMessage('수정')}"  onclick="modifyTask('${oid}')"/>
				</c:if>
			</div> 
		</div>
		<table class="mainTable mb25">
			<tr>
				<th width="90">${e3ps:getMessage('태스크 명')}</th>
				<td>${task.name}</td>
			</tr>
			<tr>
				<th width="90">${e3ps:getMessage('설명')}</th> 
				<td>${task.description}</td>
			</tr>
		</table>
		
		<!-- //진행 현황 -->
		<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewSchedule')}" flush="true">
	 		<jsp:param name="oid" value="${oid}"/>
	 		<jsp:param name="isAuth" value="${isAuth}"/>
	 		<jsp:param name="proAuth" value="false"/>
	 	</jsp:include>
	
		<c:if test="${!task.child}">
			<!-- 담당자 -->
			<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewOwner')}" flush="true">
				<jsp:param name="oid" value="${oid}" />
				<jsp:param name="isTaskEdit" value="${isTaskEdit}"/>
			</jsp:include>
		
			<!-- 산출물 -->
			<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewTaskOutput')}" flush="true">
				<jsp:param name="oid" value="${oid}"/>
				<jsp:param name="isTaskEdit" value="${isTaskEdit}"/>
				<jsp:param name="isTaskAuth" value="${isTaskAuth}"/>
			</jsp:include>
		</c:if>
		
		<c:if test="${task.child}">
			<!-- 하위 태스크 진행현황 -->
			<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewChild')}" flush="true">
				<jsp:param name="oid" value="${oid}"/>
		 	</jsp:include>
	 	</c:if>
	 	
		<c:if test="${!task.child}">
		<!-- 선후행 -->
		<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewPreTask')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="isChild" value="${task.child}"/> 
			<jsp:param name="isTaskEdit" value="${isTaskEdit}"/>
		</jsp:include>
		</c:if>

		<c:if test="${task.pjtState ne 'READY'}">
			<jsp:include page="${e3ps:getIncludeURLString('/project/issue/include_issueList')}" flush="true">
				<jsp:param name="isView" value="false"/>
				<jsp:param name="isTaskEdit" value="${isTaskEdit}"/>
			</jsp:include>
		</c:if>
	</div>
</div>
<script type="text/javascript">
//태스크 수정
function modifyTask(oid){
	
	var url = getURLString("/project/updateTask") + "?oid=" + oid;
	
	openPopup(url, "addOutput", 900, 400);	
}
</script>