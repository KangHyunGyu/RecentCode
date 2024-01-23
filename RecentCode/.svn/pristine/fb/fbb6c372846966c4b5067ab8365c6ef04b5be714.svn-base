<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions"	%>

<c:if test="${fn:length(list) ne 0 && 'image' ne fileType}">

	<c:forEach items="${list }" var="list" varStatus="i" >
		
		<li><i class="fa fa-save"></i> <a href="<c:out value='${list.url }' />" target="_blank" download="${list.name }"><c:out value="${list.name }"/> </a></li>
			
		
		
		<c:if test="${!i.last}">
			
		</c:if>
		
	</c:forEach>

</c:if>
