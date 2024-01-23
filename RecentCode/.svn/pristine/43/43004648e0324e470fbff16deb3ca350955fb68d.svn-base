<%@page import="wt.org.OrganizationServicesHelper"%>
<%@page import="com.e3ps.common.jdf.config.ConfigImpl"%>
<%@page import="wt.org.LicenseGroups"%>
<%@page import="java.util.ArrayList"%>
<%@page import="wt.org.WTGroup"%>
<%@page import="java.util.List"%>
<%@page import="com.e3ps.org.People"%>
<%@page import="wt.session.SessionHelper"%>
<%@page import="com.e3ps.org.service.PeopleHelper"%>
<%@page import="com.e3ps.admin.service.AdminHelper"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.e3ps.common.util.LoginAuthUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%
System.out.println("loginCheck.jsp");
SessionHelper.manager.setAdministrator();
String serverType = ConfigImpl.getInstance().getString("server.type");

JSONObject data = new JSONObject();
JSONObject body = new JSONObject();
String command = request.getParameter("command");

String id = request.getParameter("id");
String password = request.getParameter("password");

if("wcadmin".equals(id)){
	request.setAttribute("j_username", id);
	request.setAttribute("j_password", password);
	
	if("login".equals(command)) {
		AdminHelper.service.createLoginHistory(request);
	}
	data.put("body", body);
	data.put("success", true);
	
	
}else{
	People peo = PeopleHelper.manager.getIDPeople(id);
	boolean checkUser = false;
	if(peo != null){
		List<WTGroup> list = new ArrayList<WTGroup>();
		
		List<String> gNameList = AdminHelper.service.getLicenseGroupName();
		for(int i=0; i<gNameList.size(); i++){
			list = AdminHelper.service.searchGetGroup(gNameList.get(i), list);
		}
		
		for(int j=0; j<list.size(); j++){
			if(OrganizationServicesHelper.manager.isMember(list.get(j), peo.getUser())){
				checkUser = true;	
			}
		}
		if(checkUser){
			request.setAttribute("j_username", id);
			request.setAttribute("j_password", password);
			
			if("login".equals(command)) {
				AdminHelper.service.createLoginHistory(request);
			}
			
			data.put("body", body);
			data.put("success", true);
			data.put("message", "");
		}else{
			data.put("success", false);
			data.put("message", "권한이 없습니다. 관리자에게 문의하세요.");
		}
	}else{
		data.put("success", false);
		data.put("message", "잘못된 유저 정보입니다.");
	}
}


response.setContentType("application/json");
response.getWriter().write(data.toString());
%>
