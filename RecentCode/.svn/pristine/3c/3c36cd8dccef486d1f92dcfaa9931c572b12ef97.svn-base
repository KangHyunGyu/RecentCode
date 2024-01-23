<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="com.e3ps.admin.EsolutionMenu"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Collections"%>
<%@page import="com.e3ps.admin.service.AdminHelper"%>
<%@page import="com.e3ps.admin.bean.EsolutionMenuData"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<%
	List<EsolutionMenuData> list = AdminHelper.manager.getEsolutionMenu(false);
	List<EsolutionMenu> authMenuList = AdminHelper.manager.getEsolutionAuthMenuAll();
	
%>
<div class="c_menu">
	<div class="s_menu">		
		<ul id="mainMenu-container">
			<% for(EsolutionMenuData emData :list){ 	
				boolean hasHref = false;
				String href = emData.getHref();
				for(EsolutionMenu menu:authMenuList){
					if(menu.getMenuLevel() != 1 &&menu.getHref().equals(emData.getHref())){
						hasHref = true;
						break;
					}
				}
				
				boolean hasChildHref = false;
				if(!hasHref){
					
					for(EsolutionMenu menu:authMenuList){
						if(menu.getMenuLevel() != 1 && menu.getAlias().equals(emData.getModuleName())){
							hasChildHref = true;
							href = menu.getHref();
							break;
						}
					}
					
					if(!hasChildHref){
						href = "#";
					}
				}
				
			%>
			<li class="icon" onclick="">
				<a class="mainMenu" href="<%=StringUtil.checkString(href)?href:"#"%>">
					<img src="<%=emData.getImgsrc()%>">  
					<br>
					<span class="menuTitle"><%=emData.getDisplayName()%></span>
				</a>
			</li>	
			<%-- <li class="icon">
				<a class="mainMenu" href="javascript:moveLocation('/workspace/listWorkItem?type=approval')">
					<img src="/Windchill/jsp/portal/images/m_icon1.png">  
					<br>
					<span class="menuTitle">${e3ps:getMessage('업무 관리')}</span>
				</a>
			</li>	
			<c:if test="${e3ps:hasAuthority('PROJECT')}">
				<li class="icon">
					<a class="mainMenu" href="javascript:moveLocation('/project/searchProject')">
						<img src="/Windchill/jsp/portal/images/m_icon2.png">  
						<br>
						<span class="menuTitle"> ${e3ps:getMessage('프로젝트')}</span>
					</a>
				</li>
			</c:if>
			<c:if test="${e3ps:hasAuthority('CHANGE')}">
				<li class="icon">
					<a class="mainMenu" href="javascript:moveLocation('/change/searchECR')">
						<img src="/Windchill/jsp/portal/images/m_icon7.png">  
						<br>
						<span class="menuTitle"> ${e3ps:getMessage('설계 변경')}</span>
					</a>
				</li>	
			</c:if>	
			<c:if test="${e3ps:hasAuthority('EPM')}">
				<li class="icon">
					<a class="mainMenu" href="javascript:moveLocation('/epm/searchEpm')">
						<img src="/Windchill/jsp/portal/images/m_icon4.png">  
						<br>
						<span class="menuTitle"> ${e3ps:getMessage('도면 관리')}</span>
					</a>
				</li>
			</c:if>	
			<c:if test="${e3ps:hasAuthority('PART')}">
				<li class="icon">
					<a class="mainMenu" href="javascript:moveLocation('/part/searchPart')">
						<img src="/Windchill/jsp/portal/images/m_icon5.png">  
						<br>
						<span class="menuTitle"> ${e3ps:getMessage('부품 관리')}</span>
					</a>
				</li>	
			</c:if>
			<c:if test="${e3ps:hasAuthority('DOC')}">
				<li class="icon">
					<a class="mainMenu" href="javascript:moveLocation('/doc/searchDoc')">
						<img src="/Windchill/jsp/portal/images/m_icon6.png">  
						<br>
						<span class="menuTitle"> ${e3ps:getMessage('문서 관리')}</span>
					</a>
				</li>
			</c:if>
			<c:if test="${e3ps:hasAuthority('DISTRIBUTE')}">
				<li class="icon">
					<a class="mainMenu" href="javascript:moveLocation('/distribute/searchDistribute')">
						<img src="/Windchill/jsp/portal/images/m_icon17.png">  
						<br>
						<span class="menuTitle"> ${e3ps:getMessage('배포 관리')}</span>
					</a>
				</li>	
			</c:if>
			<c:if test="${e3ps:hasAuthority('DASHBOARD')}">
				<li class="icon">
					<a class="mainMenu" href="javascript:moveLocation('/dashboard/progress')">
						<img src="/Windchill/jsp/portal/images/m_icon18.png">
						<br>
						<span class="menuTitle"> ${e3ps:getMessage('대시 보드')}</span>
					</a>
				</li>
			</c:if>
			<c:if test="${e3ps:isAdmin()}">
				<li class="icon">
					<a class="mainMenu" href="javascript:moveLocation('/admin/departmentManagement')">
						<img src="/Windchill/jsp/portal/images/m_icon8.png">  
						<br>
						<span class="menuTitle"> ${e3ps:getMessage('관리자')}</span>
					</a>
				</li>	
			</c:if> --%>
			<% } %>
		</ul>
	</div>	
</div>