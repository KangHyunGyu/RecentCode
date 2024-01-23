<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="sub_menu" id="sub_menu">
	<h2>${e3ps:getMessage('관리자')}</h2>
	
	<jsp:include page="${e3ps:getIncludeURLString('/portal/include_menuContents')}" flush="true">
		<jsp:param name="alias" value="admin" />
	</jsp:include>
	<%-- <!-- ss_menu -->
	<div class="ss_menu pt20">
		<ul class="line">
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/departmentManagement')">부서 관리</a></h4>
			</li>
			<li>
				<h4>
					<a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('권한 관리')}</a>
				</h4>
				<ul class="subtt pdt7 pb15" style="display: none">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/admin/authorityGroupMemberManagement')">${e3ps:getMessage('그룹 멤버 관리')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/admin/authorityGroupPolicyManagement')">${e3ps:getMessage('그룹 정책 관리')}</a></li>
				</ul>
			</li>
<!-- 			<li> -->
<!-- 				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/docTypeManagement')">문서 타입 관리</a></h4> -->
<!-- 			</li> -->
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/numberCodeManagement')">코드 관리</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/downloadHistory')">다운로드 이력</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/loginHistory')">접속 이력</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/changeObjectState')">객체 상태 변경</a></h4>
			</li>
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/updateEChangeActivityDefinition')">설계변경 활동 단계</a></h4>
			</li>
<!-- 			<li> -->
<!-- 				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/projectOutput')">프로젝트 PSO단계</a></h4> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/erpHistory')">ERP 전송 이력</a></h4> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/sgCodeManagement')">SG코드 관리</a></h4> -->
<!-- 			</li> -->
			<!--  
			<li>
				<h4><a class="linkMenu" href="javascript:moveLocation('/admin/stageGateManagement')">StageGate 템플릿 관리</a></h4>
			</li>
			-->
			<!-- <li>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">문서등록</a></h4>
				<ul class="subtt pdt7 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="/Windchill/worldex/doc/searchDoc">- 문서등록 1</a></li>
					<li><a class="linkMenu subLinkMenu" href="/Windchill/worldex/doc/searchDoc2">- 문서등록 2</a></li>
				</ul>
			</li>
			<li>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">문서검색</a></h4>
				<ul class="subtt pdt7 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="/Windchill/worldex/doc/createDoc">- 문서관리 1</a></li>
					<li><a class="linkMenu subLinkMenu" href="/Windchill/worldex/doc/createDoc2">- 문서관리 2</a></li>
				</ul>
			</li> -->
		</ul>		
	</div>
	<!-- //ss_menu --> --%>
</div>