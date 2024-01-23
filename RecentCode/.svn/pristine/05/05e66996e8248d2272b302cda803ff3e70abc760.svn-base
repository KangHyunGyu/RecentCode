<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="pro_table">
<table class="mainTable">
	<colgroup>
		<col width="15%">
		<col width="35%">
		<col width="15%">
		<col width="35%">
	</colgroup>
	<tr> 
		<th>
			ECR 번호
		</th><!-- ECR 제목 -->
		<td colspan="3">
			${data.ecrNumber}
		</td>
	</tr>
	<tr> 
		<th>
			ECR 제목
		</th><!-- ECR 번호 -->
		<td>
			${data.name}
		</td>
		<th>작업현황</th><!-- 작업현황 -->
		<td>
			${data.state}
		</td>
	</tr>
	
	<tr> 
		<th>등록자</th><!-- 등록자 -->
		<td>
			${data.tempcreator}
		</td>
		<th>최초등록일</th>
		<td>
			${data.createDate}
		</td>
	</tr>
	<tr> 
		<th>적용요구시점</th>
		<td>
			${data.requestDate}
		</td>
	</tr>
	<tr> 
<!-- 		<th>차종</th> -->
<!-- 		<td> -->
<%-- 			${data.carType} --%>
<!-- 		</td> -->
		<th>주관</th>
		<td>
			${data.changeOwner}
		</td>
		<th>내역</th>
		<td>
			${data.changeDesc}
		</td>
	</tr>
<!-- 	<tr>  -->
<!-- 		<th>주관</th> -->
<!-- 		<td> -->
<%-- 			${data.changeOwner} --%>
<!-- 		</td> -->
<!-- 		<th>UPG</th> -->
<!-- 		<td> -->
<%-- 			${data.upg} --%>
<!-- 		</td> -->
<!-- 	</tr> -->
	<tr> 
		<th scope="col">${e3ps:getMessage('첨부파일')}</th>
		<td colspan="3">
			<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
				<jsp:param name="oid" value="${data.oid}"/>
			 </jsp:include>
		</td>
	</tr>
</table>

<%-- <jsp:include page="/jsp/change/include/ViewActivity_include.jsp" flush="true"> --%>
<%-- 	<jsp:param value="${data.oid}" name="oid"/> --%>
<%-- </jsp:include> --%>
</div>