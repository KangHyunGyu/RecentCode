<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
});
</script>
<div class="main">
	<h2 class="mt30 ml30">SITE MAP</h2>
       <!--sitemap-->
	<div class="sitemap mt30">			
		<div class="sitearm ml30">				
			<h4>${e3ps:getMessage('업무 관리')}</h4>				
			<ul>
				<li class="tx01 pl20 pt10"><a href="javascript:moveLocation('/workspace/searchNotice')" class="sitemapIcon">${e3ps:getMessage('공지사항')}</a></li>		
				<li class="tx01 pl20"><span class="sitemapIcon">${e3ps:getMessage('작업 공간')}</span>
				      <ul class="tx02 pl15 pb10">
						 <li><a href="javascript:moveLocation('/workspace/listWorkItem?type=tempStorage')">- ${e3ps:getMessage('임시저장함')}</a></li>
						 <li><a href="javascript:moveLocation('/workspace/listWorkItem?type=approval')">- ${e3ps:getMessage('결재함')}</a></li>
						 <li><a href="javascript:moveLocation('/workspace/listItem?type=ing')">- ${e3ps:getMessage('진행함')}</a></li>
						 <li><a href="javascript:moveLocation('/workspace/listItem?type=complete')">- ${e3ps:getMessage('완료함')}</a></li>
						 <li><a href="javascript:moveLocation('/workspace/listItem?type=receive')">- ${e3ps:getMessage('수신함')}</a></li>
						 <li><a href="javascript:moveLocation('/workspace/listSendItem')">- ${e3ps:getMessage('발신함')}</a></li>
					  </ul>
				</li>
				<li class="tx01 pl20"><a href="javascript:moveLocation('/workspace/searchMultiApproval');" class="sitemapIcon">${e3ps:getMessage('일괄 결재 검색')}</a></li>
				<li class="tx01 pl20"><a href="javascript:moveLocation('/workspace/companyTree');" class="sitemapIcon">${e3ps:getMessage('일반 메뉴')}</a></li>
			</ul>
		</div>
		<div class="sitearm ml30">				
			<h4>${e3ps:getMessage('프로젝트')}</h4>				
			<ul>
				<li class="tx01 pl20 pt10"><a href="javascript:moveLocation('/project/searchProject')" class="sitemapIcon">${e3ps:getMessage('프로젝트 검색')}</a></li>	
				<li class="tx01 pl20"><a href="javascript:moveLocation('/project/createProject');" class="sitemapIcon">${e3ps:getMessage('프로젝트 등록')}</a></li>
			</ul>
		</div>
	
		<div class="sitearm ml30">				
			<h4>${e3ps:getMessage('도면 관리')}</h4>				
			<ul >
				<li class="tx01 pl20 pt10"><a href="javascript:moveLocation('/epm/searchEpm');" class="sitemapIcon">${e3ps:getMessage('도면 검색')}</a></li>	
			</ul>
		</div>	
		<div class="sitearm ml30">				
			<h4>${e3ps:getMessage('설계 변경 관리')}</h4>				
			<ul >
				<li class="tx01 pl20 pt10"><a href="javascript:moveLocation('/change/searchECR');" class="sitemapIcon">${e3ps:getMessage('ECR 검색')}</a></li>
				<li class="tx01 pl20"><a href="javascript:moveLocation('/change/createECR');" class="sitemapIcon">${e3ps:getMessage('ECR 등록')}</a></li>
				<li class="tx01 pl20"><a href="javascript:moveLocation('/change/searchECO');" class="sitemapIcon">${e3ps:getMessage('ECO 검색')}</a></li>						
				<li class="tx01 pl20"><a href="javascript:moveLocation('/change/createECO');" class="sitemapIcon">${e3ps:getMessage('ECO 등록')}</a></li>	
			</ul>
		</div>	
	</div>

	<div class="sitemap mt30">			
		<div class="sitearm ml30">				
			<h4>${e3ps:getMessage('부품 관리')}</h4>				
			<ul >
				<li class="tx01 pl20 pt10"><a href="javascript:moveLocation('/part/searchPart');" class="sitemapIcon">${e3ps:getMessage('부품 검색')}</a></li>	
				<li class="tx01 pl20"><a href="javascript:moveLocation('/part/createPart');" class="sitemapIcon">${e3ps:getMessage('부품 채번')}</a></li>
				<li class="tx01 pl20"><a href="javascript:openBomEditor()" class="sitemapIcon">BOM EDITOR</a></li>
			</ul>
		</div>
		<div class="sitearm ml30">				
			<h4>${e3ps:getMessage('문서 관리')}</h4>				
			<ul >
				<li class="tx01 pl20 pt10"><a href="javascript:moveLocation('/doc/searchDoc');" class="sitemapIcon">${e3ps:getMessage('문서 검색')}</a></li>						
				<li class="tx01 pl20"><a href="javascript:moveLocation('/doc/createDoc');" class="sitemapIcon">${e3ps:getMessage('문서 등록')}</a></li>	
			</ul>
		</div>
		<div class="sitearm ml30">				
			<h4>${e3ps:getMessage('배포요청')}</h4>				
			<ul >
				<li class="tx01 pl20 pt10"><a href="javascript:moveLocation('/distribute/searchDistribute');" class="sitemapIcon">${e3ps:getMessage('도면출도의뢰서 검색')}</a></li>
				<li class="tx01 pl20"><a href="javascript:moveLocation('/distribute/requestFormDistribute');" class="sitemapIcon">${e3ps:getMessage('도면출도의뢰서')}</a></li>
				<li class="tx01 pl20"><a href="javascript:moveLocation('/distribute/distributeRegistration');" class="sitemapIcon">${e3ps:getMessage('배포요청 등록')}</a></li>							 
				<li class="tx01 pl20"><a href="javascript:moveLocation('/distribute/distributeRereption');" class="sitemapIcon">${e3ps:getMessage('배포요청 수신')}</a></li>	
			</ul>
		</div>
		<div class="sitearm ml30">				
			<h4>${e3ps:getMessage('대시보드')}</h4>				
			<ul >
				<li class="tx01 pl20 pt10"><a href="javascript:moveLocation('/dashboard/progress');" class="sitemapIcon">${e3ps:getMessage('프로젝트 진척현황')}</a></li>	
			</ul>
		</div>
	</div>
	<!--//sitemap-->
</div>