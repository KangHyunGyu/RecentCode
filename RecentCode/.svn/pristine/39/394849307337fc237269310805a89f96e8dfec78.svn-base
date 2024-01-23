<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="sub_menu" id="sub_menu">
	<h2>${e3ps:getMessage('ERP HISTORY')}</h2>
	
	<jsp:include page="${e3ps:getIncludeURLString('/statistics/include_statistics')}" flush="true">
		<jsp:param name="obj" value="erp"/>
		<jsp:param name="active" value="true"/>	
		<jsp:param name="today" value="false"/>	
	</jsp:include>

	<!-- ss_menu -->
	<div class="ss_menu pt20">
		<ul class="line">
			<li>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('ERP 조회')}</a></h4>
				<ul class="subtt pdt7 pb15">
					<li><a class="linkMenu subLinkMenu" href="javascript:openERPBom();">${e3ps:getMessage('BOM 조회')}</a></li>
				</ul>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('PLM Send')}</a></h4>
				<ul class="subtt pdt7 pb15">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchPartInfo')">${e3ps:getMessage('부품 전송')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchECRInfo')">${e3ps:getMessage('ECR 전송')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchECNInfo')">${e3ps:getMessage('ECN 전송')}</a></li>
					<%-- 
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchECRIssueInfo')">${e3ps:getMessage('ECR ISSUE 전송')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchBOMInfo')">${e3ps:getMessage('BOM 전송')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchBOMChangeInfo')">${e3ps:getMessage('BOM Change 전송')}</a></li>
					 --%>
				</ul>
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="#">${e3ps:getMessage('ERP Receive')}</a></h4>
				<ul class="subtt mt10 pb15" style="display:none">
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchProjectInfo')">${e3ps:getMessage('프로젝트 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchProjectUserInfo')">${e3ps:getMessage('프로젝트 구성원 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchIssueInfo')">${e3ps:getMessage('ISSUE 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchPurchaseInfo')">${e3ps:getMessage('구매발주서 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchPurchaseListInfo')">${e3ps:getMessage('구매발주서 부품 리스트 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchEstimateInfo')">${e3ps:getMessage('견적의뢰서 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchEstimateListInfo')">${e3ps:getMessage('견적의뢰서 부품 리스트 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchSupplierInfo')">${e3ps:getMessage('협력업체 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchDepartmentInfo')">${e3ps:getMessage('부서 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchUserInfo')">${e3ps:getMessage('PLM 사용자 정보')}</a></li>
					<li><a class="linkMenu subLinkMenu" href="javascript:moveLocation('/erp/searchSiteInfo')">${e3ps:getMessage('Site 정보')}</a></li>
				</ul>
			</li>
		</ul>		
	</div>
	<!-- //ss_menu -->
</div>