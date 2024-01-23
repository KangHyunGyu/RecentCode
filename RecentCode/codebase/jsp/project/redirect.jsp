<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@
	page import="java.util.List"%><%@
	page import="java.util.Iterator"%><%@
	page import="java.util.Map"%><%@
	page import="com.e3ps.common.util.TypeUtil"%><%
	
	// #. 파라메터 조회
	String url = TypeUtil.stringValue(request.getAttribute("url")).trim(); // 리다이렉트 URL
	String method = TypeUtil.stringValue(request.getAttribute("method"), "GET"); // 제출 메쏘드 형식 (Ex. POST or GET)
	String message = TypeUtil.stringValue(request.getAttribute("message")).trim(); // alert 메시지
	System.out.println("message="+message);
	boolean close = TypeUtil.booleanValue(request.getAttribute("close"), false); // 창닫기 여부
	boolean back = TypeUtil.booleanValue(request.getAttribute("back"), false);
	
	String callbackName = TypeUtil.stringValue(request.getAttribute("callbackName")).trim(); // 콜백 함수명 (Ex. opener.myCallback)
	String callbackArg = TypeUtil.stringValue(request.getAttribute("callbackArg")).trim(); // 콜백 인자 문자열 (Ex. 'a', 'b')
	
	Map<String, Object> paramMap = (Map<String, Object>)request.getAttribute("paramMap"); // 다시쓰기 할 파라메터
%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script language="javascript">
/**
 * 페이지 초기화
 */
function initPage() {
	var f = document.redirectForm;
	
<% if (message.length() > 0) { %>
	// #. 메시지 출력
	var messageTextArea = document.getElementById("message");
	if (messageTextArea != null && messageTextArea.value.length > 0) {
		console.log(messageTextArea.value);
		alert(messageTextArea.value);
	}
<% } %>

<% if (callbackName.length() > 0) { %>
	try {
		var callbackFunc = eval("<%= callbackName %>");
		if (callbackFunc != null) {
		<% if (callbackArg.length() > 0) { %>
			callbackFunc.call(this, <%= callbackArg %>);
		<% } else { %>
			callbackFunc.call(this);
		<% } %>
		}
	} catch (e) {
		// ignore
	}
<% } %>

<% if (back) { %>
	history.back();
<% } else if (close) { %>
	self.close();
<% } else { %>
	// #. 폼 제출
	f.submit();
<% } %>
}
</script>
</head>
<body onload="initPage()">
<form name="redirectForm" method="<%= method %> %>" action="<%= url %>">
<textarea id="message" style="display:none"><%= StringEscapeUtils.unescapeJava(message).toString() %></textarea>
<%
	if (paramMap != null) {
		Iterator<String> paramNames = paramMap.keySet().iterator();
		while (paramNames.hasNext()) {
			String paramName = paramNames.next();
			Object value = paramMap.get(paramName);
			
			if (value instanceof String[]) {
				String[] paramValues = (String[])value;
				for (String paramValue : paramValues) {
					paramValue = TypeUtil.stringValue(paramValue).trim();
					out.println("<INPUT type=\"hidden\" name=\"" + paramName + "\" value=\"" + paramValue + "\"/>");
				}
			} else {
				String paramValue = TypeUtil.stringValue(value.toString()).trim();
				out.println("<INPUT type=\"hidden\" name=\"" + paramName + "\" value=\"" + paramValue + "\"/>");
			}
		}
	}
%>
</form>
</body>
</html>
