<%@page import="java.util.Map"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="javax.crypto.Mac"%>
<%@page import="com.e3ps.common.jdf.config.ConfigImpl"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.Base64.Decoder"%>
<%@page import="wt.session.SessionHelper"%>
<%@page import="com.e3ps.org.People"%>
<%@page import="com.e3ps.org.service.PeopleHelper"%>
<%@page import="com.e3ps.admin.service.AdminHelper"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.e3ps.common.util.LoginAuthUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%
JSONObject data = new JSONObject();
String companyCd = request.getParameter("companyCd");
String empId = request.getParameter("empId");
String apiTime = request.getParameter("apiTime");
String signiture = request.getParameter("signiture");

Map<String, Object> map = CommonUtil.getHmacInKey(companyCd, empId, apiTime, signiture);

data.put("pass", map.get("pass"));
data.put("isChk", map.get("isChk"));
response.setContentType("application/json");
response.getWriter().write(data.toString());
%>