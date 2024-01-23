<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions"%>
<script>
$(document).ready(function(){
	mainContentDivResize();
	window.history.replaceState({}, null, location.pathname);
});

function mainContentDivResize() {
	const conDIV = document.querySelector('.con');
    const mainContentDIV = document.querySelector('.main_con');
    const fillingDIV = document.querySelector('#internalFilling');
    const setHeight = conDIV.offsetHeight - mainContentDIV.offsetHeight - 19;
    fillingDIV.style.height = setHeight + 'px';
}
</script>
<div class="main_s">
	<div class="con">
		<!-- main_con -->
		<div class="main_con">
			<!-- main_img -->
			<%-- <div class="main_img">
				<div class="left"><img src="/Windchill/jsp/portal/images/main_img.png" alt="main이미지">  </div>
				<div class="right_notice">
				  <h3>${e3ps:getMessage('공지사항')}<span class="more" onclick="javascript:moveLocation('/workspace/searchNotice')">${e3ps:getMessage('더보기 ')}+</span></h3>
					  <ul>
					  	<c:if test="${fn:length(notice) != 0 }">
						  	<c:forEach items="${notice}" var="notice"> 
							 	<li><span class="pointer main_span_hover" onclick="javascript:openView('${notice.oid}');">${notice.title}</span></li>
							</c:forEach>
						</c:if>
					  	<c:if test="${fn:length(notice) == 0 }">
					  		<li class="t_align_c mt90">${e3ps:getMessage('공지사항이 존재하지 않습니다.')}</li>
					  	</c:if>
					  </ul>
				</div>
			</div> --%>
			<!--// main_img -->

			<section class="component-main-hero">
				<div class="img-cnt">
					<img class="img-fluid heroimg lazyloaded img-custom-size" style="pointer-events: none;" alt="E3PS" src="/Windchill/jsp/portal/images/main_img.jpg" />
				</div>
				<!-- <div class="inside">
					<h1 style="user-select: none">WORLDEX PLM</h1>
					<p style="user-select: none">Product LifeCycle Management</p>
				</div> -->
			</section>

			<!-- main_list -->
			<div class="main_list mt20">
				<div class="decide_left ml20">
					<h4>${e3ps:getMessage('결재 목록')}<span class="more2 pointer" onclick="javascript:moveLocation('/workspace/listWorkItem?type=approval')">${e3ps:getMessage('더보기 ')} +</span></h4>
						<ul>
							<c:if test="${fn:length(approval) != 0 }">
								<c:forEach items="${approval}" var="approval">
							 		<li><span class="m_bt">${approval.objectTypeName}</span> <span class="pointer main_span_hover" onclick="javascript:moveLocation('/workspace/detailApproval?type=${approval.objectType}&oid=${approval.oid}');">${approval.title}</span></li>
								</c:forEach>
							</c:if>
							<c:if test="${fn:length(approval) == 0 }">
					  			<li class="t_align_c mt65">${e3ps:getMessage('결재 목록이 존재하지 않습니다.')}</li>
					  		</c:if>
					  </ul>
				</div>
				<div class="decide_right">
					<h4>${e3ps:getMessage('나의 설계변경 지연 업무 목록')}<span class="more2 pointer" onclick="javascript:moveLocation('/workspace/listToDo')">${e3ps:getMessage('더보기 ')} +</span></h4>
					<ul>
						<c:if test="${fn:length(eca) != 0 }">
							<c:forEach items="${eca}" var="eca">
								<li class="l_2">				 
								   <div class="m_t1"><span class="pointer main_span_hover" onclick="javascript:openView('${eca.order}');">${eca.orderTitle}</span></div>
								   <div class="m_t1">${eca.name}</div>	
								   <div class="m_t2"><span class="red">${eca.delay}${e3ps:getMessage('일 지연')}</span></div>				 
							  	</li>
							</c:forEach>
						</c:if>
						<c:if test="${fn:length(eca) == 0 }">
							<li class="t_align_c mt65">${e3ps:getMessage('설계변경 지연 업무 목록이 존재하지 않습니다.')}</li>
						</c:if>
				  	</ul>
				</div>
			</div>
			 <!-- //main_list -->

			 <!-- main_list02-->
			 <div class="main_list mt20">
				<div class="decide_left ml20">
					<h4>${e3ps:getMessage('나의 지연 태스크 목록')}<span class="more2 pointer" onclick="javascript:moveLocation('/project/searchMyTask')">${e3ps:getMessage('더보기 ')} +</span></h4>
					<ul>
						<c:if test="${fn:length(task) != 0}">
							<c:forEach items="${task}" var="task">
								<li class="l_2">
									<div class="m_t1"><span class="pointer main_span_hover" onclick="javascript:openView('${task.pjtOid}');">[${task.pjtNumber}] ${task.pjtName}</span></div>
									<div class="m_t1"><span class="pointer main_span_hover" onclick="javascript:openView('${task.oid}');">${task.name}</span></div>
									<div class="m_t2"><span class="red">${task.delayDate}${e3ps:getMessage('일 지연')}</span></div>
								</li>
							</c:forEach>
						</c:if>
						<c:if test="${fn:length(task) == 0}">
							<li class="t_align_c mt65">${e3ps:getMessage('지연 태스크 목록이 존재하지 않습니다.')}</li>
						</c:if>
					</ul>
				</div>
				<div class="decide_right">
					<h4>${e3ps:getMessage('나의 이슈 목록')}<span class="more2 pointer" onclick="javascript:moveLocation('/project/issue/searchMyIssue')">${e3ps:getMessage('더보기 ')} +</span></h4>
						<ul>
							<c:if test="${fn:length(issue) != 0 }">
								<c:forEach items="${issue}" var="issue">
							 		<li class="l_2">
							 			<div class="m_t1"><span class="pointer main_span_hover" onclick="javascript:openView('${issue.pjtOid}');">[${issue.number}] ${issue.pjtName }</span></div>
							 			<div class="m_t1"><span class="pointer main_span_hover" onclick="javascript:openView('${issue.oid}');">${issue.title}</span></div>
							 			<div class="m_t2">${issue.state }</div>
							 			
							 		</li>
								</c:forEach>
							</c:if>
							<c:if test="${fn:length(issue) == 0 }">
								<li class="t_align_c mt65">${e3ps:getMessage('이슈 목록이 존재하지 않습니다.')}</li>
							</c:if>
					  </ul>
				</div>
			 </div>
			<!-- //main_list02-->
            <div id="internalFilling"></div>                         
			<!--footer-->
			<div class="footer">
				<p>Copyright (C)  WORLDEX CO. LTD   ALLRIGHT RESERVED </p>			
			</div>
			<!--//footer-->


		</div>	
		<!--// main_con -->	
	</div>
	<!--// con -->
</div>
