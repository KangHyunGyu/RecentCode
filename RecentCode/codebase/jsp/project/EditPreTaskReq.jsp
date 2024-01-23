<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.e3ps.project.beans.TaskEditHelper"%>
<%@page import="java.util.Vector"%>
<%@page import="com.e3ps.project.service.TemplateHelper"%>
<%@ page contentType="text/xml; charset=UTF-8" %>

<%
String httpVersion = request.getProtocol();
if(httpVersion.equals("HTTP/1.0")){
 response.setDateHeader("Expires", 0);
 response.setHeader("Pragma", "no-cache");
}else if(httpVersion.equals("HTTP/1.1")){
 response.setHeader("Cache-Control", "no-cache");
}
%>

<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.e3ps.project.ETaskNode"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="com.e3ps.project.PrePostLink"%>
<%@page import="wt.fc.PersistenceHelper"%>
<%@page import="wt.fc.QueryResult"%>
<%@page import="com.e3ps.project.ScheduleNode"%>
<%@page import="com.e3ps.project.service.ProjectHelper"%>
<%@page import="java.util.Hashtable"%>

<%

	String oid = request.getParameter("oid");
	String selectId = request.getParameter("selectId");
	String selectChild = request.getParameter("selectChild");
	String preTask = request.getParameter("preTask"); //

	Hashtable hash = new Hashtable();
	
	hash.put("oid", oid);
	hash.put("selectId", selectId);
	hash.put("preTask", preTask);
	hash.put("selectChild", selectChild);
	
	Vector chkList = new Vector();
	
	try{
		
		//사전체크
		String pre[] = preTask.split(",");
		ScheduleNode isPreNode = null;
		
		for(int i=0; pre!=null && i< pre.length; i++){
			if(pre[i] != null && pre[i].length() > 0){
				if(CommonUtil.getObject(pre[i]) != null){
	        		ScheduleNode node = (ScheduleNode)CommonUtil.getObject(pre[i]);
	        		chkList.addElement(node);
	        		isPreNode = TaskEditHelper.service.isPreTaskNode(selectChild, node);
				}
    		}
		}
		ArrayList list = null;
		
		if(isPreNode == null){
			list = ProjectHelper.service.setPreTask2(hash);
		}
		
		if(isPreNode != null){
	%>
		<result>
			<code>posttask</code>
			<name><%=isPreNode.getName()%></name>
		</result>
	<%
		}else if(list == null){
	%>
	<result>
		<code>pretaskNull</code>
	</result>
	<%}else{%>
	<result>
		<code>pretask</code>
		<data><![CDATA[
		[
		<%
		int size = list.size();
		int index = 0;
		
			for(int i=0; i<list.size(); i++){
				
				ScheduleNode node = (ScheduleNode)list.get(i);
				
				index ++;
				
			%>
			{
				oid : '<%=node.getPersistInfo().getObjectIdentifier().toString()%>',
				name : '<%=node.getName()%>'
			}
			<%if(index < size){%>,<%}
			}
			%>
			]
		]]></data>
		<chklist><![CDATA[
		[
		<%
		int chksize = chkList.size();
		int chkindex = 0;
		
			for(int i=0; i<chksize; i++){
				
				ScheduleNode node = (ScheduleNode)chkList.get(i);
				
				chkindex ++;
				
			%>
			{
				oid : '<%=node.getPersistInfo().getObjectIdentifier().toString()%>',
				name : '<%=node.getName()%>'
			}
			<%if(chkindex < chksize){%>,<%}
			}
			%>
			]
		]]></chklist>
	</result>
	
	<%}
	}catch(Exception e){
		e.printStackTrace();
	%>
	<result>
		<code>error</code>
		<message><![CDATA[<%=e.getMessage()%>]]></message>
	</result>
	<%
	}
 %>