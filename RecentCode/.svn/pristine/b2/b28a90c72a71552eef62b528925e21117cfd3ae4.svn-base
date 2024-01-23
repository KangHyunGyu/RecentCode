<%@page import="com.e3ps.common.util.WebUtil"%>
<%@page import="wt.content.ContentHelper"%>
<%@page import="wt.content.ApplicationData"%>
<%@page import="java.util.Vector"%>
<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@page import="wt.content.ContentHolder"%>
<%@page import="com.e3ps.common.util.DateUtil"%>
<%@page import="com.e3ps.project.issue.IssueSolution"%>
<%@page import="com.e3ps.project.beans.IssueData"%>
<%@page import="com.e3ps.project.issue.IssueRequest"%>
<%@page import="wt.fc.ReferenceFactory"%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="seach_arm pt10 pb5">
	<div class="leftbt">
		<h4>
			<img class="pointer" onclick="switchPopupDiv(this);" 
				src="/Windchill/jsp/portal/images/minus_icon.png">
					${e3ps:getMessage('해결방안')}
		</h4>
	</div>
</div>
<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<colgroup>
			<col style="width:20%">
			<col style="width:30%">
			<col style="width:20%">
			<col style="width:30%">
		</colgroup>	
		<tbody>
			<tr>
			    <th >답변 제기자</th>
				<td >
				&nbsp;${fullName}
			    </td>
			    <th >답변 제기일자</th>
				<td >
				&nbsp;${createStamp}
			    </td>
			</tr>
			<tr height=150>
			    <th height=100>해결방안</th>
				<td  colspan=3 valign=top>
				   ${solution}
			    </td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${solutionOid}"/>
					 </jsp:include>
				</td>								
			</tr>	
<!-- 			<tr> -->
<!-- 				<th> -->
<!-- 					파일첨부 -->
<!-- 				</th> -->
<!-- 				<td colspan=3 > -->
<%-- 					<jsp:include page="/extcore/worldex/common/include/attachedFile.jsp" flush="true"> --%>
<%-- 						<jsp:param name="mode" value="view"/> --%>
<%-- 						<jsp:param name="choid" value="<%=CommonUtil.getOIDString(solution) %>"/> --%>
<%-- 						<jsp:param name="role" value="secondary"/> --%>
<%-- 					</jsp:include> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
		</tbody>
	</table>
</div>
