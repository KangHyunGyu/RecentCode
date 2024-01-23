<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="sub_menu" id="sub_menu">
	<h2>${e3ps:getMessage('프로젝트 관리')}</h2>
	
	<!-- ss_menu -->
	<div class="ss_menu pt20">
		<ul class="line">
			<li>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('프로젝트 관리')}</a></h4>
				<ul class="subtt pdt7 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/searchProject')">${e3ps:getMessage('프로젝트 검색')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/createProject')">${e3ps:getMessage('프로젝트 등록')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/searchMyProject')">${e3ps:getMessage('나의 프로젝트')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/searchMyTask')">${e3ps:getMessage('나의 태스크')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/issue/searchMyIssue')">${e3ps:getMessage('나의 이슈')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/searchOutputList')">${e3ps:getMessage('자료 등록 현황')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/issue/searchIssue')">${e3ps:getMessage('이슈 관리')}</a></li>
				</ul>
<%-- 				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('접수 관리')}</a></h4> --%>
<!-- 				<ul class="subtt mt10 pb15" style="display:none"> -->
<%-- 					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/distribute/searchReceipt')">${e3ps:getMessage('접수 검색')}</a></li> --%>
<!-- 				</ul> -->
<%-- 				<h4><a class="linkMenu" href="javascript:moveLocation('/distribute/searchSupplier')">${e3ps:getMessage('업체목록')}</a></h4> --%>
			</li>
			<li>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('템플릿 관리')}</a></h4>
				<ul class="subtt mt10 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/template/searchTemplate')">${e3ps:getMessage('템플릿 목록')}</a></li>
					<c:if test="${e3ps:isAdmin()}">
						<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/project/template/createTemplate')">${e3ps:getMessage('템플릿 등록')}</a></li>
					</c:if>
				</ul>
			</li>
			<li>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('대일정 관리')}</a></h4>
				<ul class="subtt mt10 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/calendar/searchDs')">${e3ps:getMessage('대일정 목록')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/calendar/createDs')">${e3ps:getMessage('대일정 등록')}</a></li>
<%-- 					<c:if test="${e3ps:isAdmin()}"> --%>
						
<%-- 					</c:if> --%>
				</ul>
			</li>
		</ul>		
	</div>
	<!-- //ss_menu -->
</div>