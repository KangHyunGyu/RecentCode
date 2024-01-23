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
	String alias = request.getParameter("alias");
	EsolutionMenuData mainMenuData = AdminHelper.manager.getEsolutionMenu(alias);
	List<EsolutionMenuData> childList = mainMenuData.getChildren();
%>
<div class="ss_menu pt20">
	<ul class="line">
		<% for(EsolutionMenuData emData :childList){ %>
			
		<li>
			<%if(emData.getChildren()!= null && emData.getChildren().size()>0){ %>	
				<h4><a class="on" onclick="javascript:subMenuSlide(this);" href="<%=emData.getHref().length()==0?"#":emData.getHref()%>"><%=emData.getDisplayName()%></a></h4>
				<ul class="subtt pdt7 pb15" style="display:none">
			<%	for(EsolutionMenuData childEMData :emData.getChildren()){%> 
					<li><a class="linkMenu subLinkMenu" href="<%=childEMData.getHref().length()==0?"#":childEMData.getHref()%>"><%=childEMData.getDisplayName()%></a></li>
			<%	}%>
				</ul>
			<%}else{%>
				<h4><a class="linkMenu" href="<%=emData.getHref().length()==0?"#":emData.getHref()%>"><%=emData.getDisplayName()%></a></h4>
			<% } %>	
		</li>
		<% } %>
	</ul>		
</div>
