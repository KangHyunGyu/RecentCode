<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="sub_menu" id="sub_menu">
	<h2>${e3ps:getMessage('업무 관리')}</h2>
	
	<jsp:include page="${e3ps:getIncludeURLString('/statistics/include_statistics')}" flush="true">
		<jsp:param name="obj" value="workspace"/>
		<jsp:param name="active" value="true"/>	
		<jsp:param name="today" value="false"/>	
	</jsp:include>
	
	<jsp:include page="${e3ps:getIncludeURLString('/portal/include_menuContents')}" flush="true">
		<jsp:param name="alias" value="workspace" />
	</jsp:include>

	<%-- <!-- ss_menu -->
	<div class="ss_menu pt20">
		<ul class="line">
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/workspace/searchNotice')">${e3ps:getMessage('공지사항')}</a></h4>
			</li>
			<li>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('작업 공간')}</a></h4>
				<ul class="subtt mt10 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/listWorkItem?type=tempStorage')">${e3ps:getMessage('임시저장함')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/listWorkItem?type=approval')">${e3ps:getMessage('결재함')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/listItem?type=ing')">${e3ps:getMessage('진행함')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/listItem?type=complete')">${e3ps:getMessage('완료함')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/listItem?type=receive')">${e3ps:getMessage('수신함')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/listSendItem')">${e3ps:getMessage('발신함')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/listToDo')">${e3ps:getMessage('설변업무')}</a></li>
				</ul>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/workspace/searchMultiApproval');" >${e3ps:getMessage('일괄 결재  검색')}</a></h4>
				
				<ul class="subtt mt10 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/searchMultiApproval')">${e3ps:getMessage('일괄 결재 검색')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/createMultiApproval')">${e3ps:getMessage('일괄 결재 등록')}</a></li> 
				</ul>
				
			</li>
			<li>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('일반 메뉴')}</a></h4>
				<ul class="subtt pdt7 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/changePassword')">${e3ps:getMessage('비밀번호 변경')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/workspace/companyTree')">${e3ps:getMessage('조직도')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="/Windchill/install/WorkGroupManager.zip">${e3ps:getMessage('WGM 설치파일')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="/Windchill/install/CreoView_64.exe">${e3ps:getMessage('뷰어 설치파일')}</a></li>
					D:\ptc\Windchill_12.1\Windchill\codebase\install\WorkGroupManager.zip
				</ul>
			</li>
		</ul>		
	</div>
	<!-- //ss_menu --> --%>
</div>