<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree_top3 pl15 pr15" style="margin-bottom:30px;">
	<div class="pro_table mr25 ml25 mb25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt">
				<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
				${e3ps:getMessage('기본 정보')}</span>
			</div>
			<div class="rightbt">
				<%-- <%if(CommonUtil.isAdmin()){%>
				<a>
					<!-- <img src="/Windchill/jsp/project/images/img/bt_08.png" alt="ERP 전송" name="leftbtn_045" border="0" /> -->
					<button onclick="javascript:sendErpProject();">ERP 전송</button>
				</a>
				<%}%>  --%>
				<a href="javascript:viewGanttChart()">
					<img src="/Windchill/jsp/project/images/img/bt_01.png" alt="차트" name="leftbtn_011" border="0">
				</a>
				<c:if test="${root.state ne 'READY'}">
				<a href="javascript:viewProjectHistory();">
					<img src="/Windchill/jsp/project/images/img/bt_09.png" alt="이력보기" name="leftbtn_012" border="0">
				</a>
				</c:if>
				<c:if test="${isAuth}">
					<c:choose>
						<c:when test="${root.state eq 'PROGRESS'}">
							<a href="javascript:checkoutProject();">
								<img src="/Windchill/jsp/project/images/img/bt_10.png" alt="체크아웃" name="leftbtn_013" border="0" />
							</a>
					    	<a href="javascript:stopProject();">
								<img src="/Windchill/jsp/project/images/img/bt_05.png" alt="중단" name="leftbtn_014" border="0" />
							</a>
						</c:when>
						<c:when test="${root.state eq 'READY'}">
							<a href="javascript:startProject();">
								<img src="/Windchill/jsp/project/images/img/bt_03.png" alt="시작" name="leftbtn_015" border="0">
							</a>
							<a href="javascript:editMember2();">
								<img src="/Windchill/jsp/project/images/img/bt_04.png" alt="구성원" name="leftbtn_013" border="0" />
							</a>
							<a href="javascript:updateProject();">
								<img src="/Windchill/jsp/project/images/img/bt_02.png" alt="수정" name="leftbtn_215" border="0" />
							</a>
						</c:when>
						<c:when test="${root.state eq 'STOP'}">
							<a href="javascript:restartProject();">
								<img src="/Windchill/jsp/project/images/img/bt_08.png" alt="재시작" name="leftbtn_045" border="0" />
							</a>
						</c:when>
						<c:when test="${root.state eq 'MODIFY' }"><!-- or root.writer -->
							<a href="javascript:editMember2();">
								<img src="/Windchill/jsp/project/images/img/bt_04.png" alt="구성원" name="leftbtn_013" border="0" />
							</a>
							<a href="javascript:updateProject();">
								<img src="/Windchill/jsp/project/images/img/bt_02.png" alt="수정" name="leftbtn_215" border="0" />
							</a>
							<a href="javascript:checkinProject();">
								<img src="/Windchill/jsp/project/images/img/bt_07.png" alt="체크인" name="leftbtn_002" border="0" />
							</a>
							<a href="javascript:undoCheckoutProject();">
								<img src="/Windchill/jsp/project/images/img/bt_11.png" alt="체크아웃 취소" name="leftbtn_003" border="0" />
							</a>
						</c:when>
						<c:when test="${root.state eq 'COMPLETED' and (root.writer or isAuth)}"> <!-- 완료됨 상태이고 작성자일 때 -->
							<a href="javascript:editMember2();">
								<img src="/Windchill/jsp/project/images/img/bt_04.png" alt="구성원" name="leftbtn_013" border="0" />
							</a>
							<a href="javascript:updateProject();">
								<img src="/Windchill/jsp/project/images/img/bt_02.png" alt="수정" name="leftbtn_215" border="0" />
							</a>
							<a href="javascript:checkoutProject();">
								<img src="/Windchill/jsp/project/images/img/bt_10.png" alt="체크아웃" name="leftbtn_013" border="0" />
							</a>
						</c:when>
					</c:choose>
					
					<c:if test="${isAuth && root.state ne 'PROGRESS'}">
						<a href="javascript:deleteProject();">
							<img src="/Windchill/jsp/project/images/img/bt_06.png" alt="삭제" name="leftbtn_016" border="0" />
						</a>
					</c:if>
				</c:if>
			</div>
		</div>
		<table class="mainTable mb25">
			<colgroup>
				<col width="15%">
				<col width="18%">
				<col width="15%">
				<col width="18%">
				<col width="15%">
				<col width="19%">
			</colgroup>
			<tr>
				<th>${e3ps:getMessage('프로젝트 번호')}</th>
				<td>${root.code }</td>
			    <th>${e3ps:getMessage('프로젝트 명')}</th>
				<td>${root.name}</td>
			    <th>${e3ps:getMessage('버전')}</th>
				<td>${root.version}</td>
			</tr>
			<tr>
			    <th>${e3ps:getMessage('프로젝트 타입')}</th>
				<td>${root.projectTypeDisplay}</td>
			    <th>${e3ps:getMessage('템플릿')}</th>
				<td>${root.templateName}</td>
				<th></th>
				<td></td>
			</tr>
			<tr>
			    <th>${e3ps:getMessage('등록자')}</th>
				<td>${root.creatorFullName}</td>
			    <th>${e3ps:getMessage('최초 등록일')}</th>
				<td>${root.createDate}</td>
			     <th>${e3ps:getMessage('수정일')}</th>
				<td>${root.modifyDate}</td>
			</tr>
		</table>
		
		<!-- 진행현황 -->
	 	<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewSchedule')}" flush="true">
	 		<jsp:param name="oid" value="${oid}"/>
	 		<jsp:param name="isAuth" value="${isAuth}"/>
	 		<jsp:param name="proAuth" value="true"/>
	 	</jsp:include>
	 	
		<!-- 하위 태스크 진행현황 -->
		<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewChild')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
	 	</jsp:include>
		
		<!-- 구성원 -->
		<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewMember')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="isAuth" value="${isAuth}"/>
			<jsp:param name="isEditState" value="${isEditState}"/>
	 	</jsp:include>
	 	
	 	<!-- 재시작 이력 -->
	 	<jsp:include page="${e3ps:getIncludeURLString('/project/include_viewStopHistory')}" flush="true">
	  		<jsp:param name="oid" value="${oid}"/>
	 	</jsp:include>
	</div>
</div>
<script>
function viewChart(){
	var url= getURLString("/project/ganttChart") + "?oid=${oid}";
	openPopup(url, "ganttChart", 1150, 650);
}

function startProject(){
	openConfirm("${e3ps:getMessage('프로젝트를 시작 하시겠습니까?')}", function() {
		var param = new Object();
		param.oid = "${oid}"
		var url = getURLString("/project/startProjectAction");
		ajaxCallServer(url, param, function(data){
		}, true);
	})
}

function deleteProject(){
	openConfirm("${e3ps:getMessage('해당 프로젝트를 삭제 하시겠습니까?')}", function() {
		var param = new Object();
		param.oid = "${oid}"
		var url = getURLString("/project/deleteProjectAction");
		ajaxCallServer(url, param, function(data){
		}, true);
	})
}

function stopProject(){
	var url= getURLString("/project/stopProject") + "?oid=${oid}";
	openPopup(url, "stopProject", 500, 250);
}

function restartProject(){
	var url= getURLString("/project/restartProject") + "?oid=${oid}";
	openPopup(url, "restartProject", 600, 300);
}

function updateProject(){
	var url= getURLString("/project/updateProject") + "?oid=${oid}";
	openPopup(url, "updateProject", 650, 350);
}

function checkoutProject(){
	openConfirm("${e3ps:getMessage('프로젝트를 체크아웃 하시겠습니까?')}", function() {
		var param = new Object();
		param.oid = "${oid}"
		var url = getURLString("/project/checkoutAction");
		ajaxCallServer(url, param, function(data){
		}, true);
	})
}

function checkinProject(){
	var url= getURLString("/project/checkIn") + "?oid=${oid}";
	openPopup(url, "checkIn", 500, 250);
}

function undoCheckoutProject(){
	openConfirm("${e3ps:getMessage('프로젝트 체크아웃을 취소 하시겠습니까?')}", function() {
		var param = new Object();
		param.oid = "${oid}"
		var url = getURLString("/project/undoCheckoutAction");
		ajaxCallServer(url, param, function(data){
		}, true);
	})
}

function viewProjectHistory(){
	var url= getURLString("/project/projectHistory") + "?oid=${oid}";
	openPopup(url, "projectHistory", 800, 400);
}

function viewGanttChart(){
	var url = getURLString("/project/viewGanttChart") + "?oid=${oid}";
	openPopup(url,"viewGanttChart","1500","800");
}

function sendErpProject(){
	openConfirm("${e3ps:getMessage('보내겠습니까?')}", function() {
		var url = getURLString("/project/sendErpAction");
		var param = new Object();
		param.oid = "${oid}";
		
		ajaxCallServer(url, param, function(data){
		}, true);
	});
}

</script>