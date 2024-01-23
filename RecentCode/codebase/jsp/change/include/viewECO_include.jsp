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
				${e3ps:getMessage('관련 ECR')}
			</th>
			<td colspan="3">
			<a href="JavaScript:openView('${ecrOid}')">${ecrNumber}</a>
			</td>
		</tr>
		<tr> 
			<th>
				${e3ps:getMessage('ECO 제목')}
			</th>
			<td colspan="3">
			${data.name}</td>
		</tr>
		<tr> 
			<th>
				${e3ps:getMessage('ECO 번호')}
			</th>
			<td>
				${data.orderNumber}
			</td>
			<th>${e3ps:getMessage('작업현황')}</th><!-- 작업현황 -->
			<td>
				${data.state}
			</td>
		</tr>
		
		<tr> 
			<th>${e3ps:getMessage('등록자')}</th><!-- 등록자 -->
			<td>
				${data.creator}
			</td>
			<th>${e3ps:getMessage('최초등록일')}</th>
			<td>
				${data.createDate}
			</td>
		</tr>
		<tr> 
<%-- 			<th>${e3ps:getMessage('차종')}</th> --%>
<!-- 			<td> -->
<%-- 				${data.carType} --%>
<!-- 			</td> -->
			<th>${e3ps:getMessage('주관')}</th>
			<td>
				${data.changeOwner}
			</td>
		</tr>
<!-- 		<tr>  -->
<%-- 			<th>${e3ps:getMessage('주관')}</th> --%>
<!-- 			<td> -->
<%-- 				${data.changeOwner} --%>
<!-- 			</td> -->
<!-- 			<th>UPG</th> -->
<!-- 			<td> -->
<%-- 				${data.upg} --%>
<!-- 			</td> -->
<!-- 		</tr> -->
		<tr>
			<th>${e3ps:getMessage('적용요구시점')}</th>
			<td colspan=3>
				${data.applyDateName}
			</td>
		</tr>
		<tr>
			<th scope="col">${e3ps:getMessage('첨부파일')}</th>
			<td colspan="3">
				<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
					<jsp:param name="oid" value="${data.oid}"/>
				 </jsp:include>
			</td>								
		</tr>
	</table>
</div>
