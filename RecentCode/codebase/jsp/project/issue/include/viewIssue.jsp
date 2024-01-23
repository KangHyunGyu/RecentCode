<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
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
				<th>이슈 </th>
				<td colspan="3">
					&nbsp;${data.name}
				</td>
			</tr>
			<tr>
				<th>타입 </th>
				<td>
					&nbsp;${data.issueType}
				</td>
				<th>작업현황</th>
				<td>
					<table>
			        	<tr>
			        		<td>&nbsp;${data.state}</td>
			        	<%-- 	<td>
			        			<c:choose>
									<c:when test="${(isCreator || isAdmin) && !isView }">
									<c:choose>
										<c:when test="${isState}">
											<input type="button" class="button01" value="완료확인" onclick="issueComplete()">
										</c:when>
										<c:when test="${isComplete}">
											<input type="button" class="button01" value="완료취소" onclick="cancelComplete()">
										</c:when>
									</c:choose>
									</c:when>
			        			</c:choose>
							</td> --%>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<th>프로젝트</th>
				<td>
					&nbsp;${pjtName}
				</td>
				<th>태스크</th>
				<td>
					&nbsp;${data.taskName}
				</td>
			</tr>
			<tr>
				<th>제기자</th>
				<td>
					&nbsp;${data.creatorFullName}
				</td>
				<th>제기일</th>
				<td>
					&nbsp;${data.createDate}
				</td>
			</tr>
			<tr>
				<th>담당자</th>
				<td>
					&nbsp;${data.managerFullName}
				</td>
				<th>완료예정일</th>
				<td>
					&nbsp;${data.requestDate}
				</td>
			</tr>
			<tr>
				<th>완료요청일</th>
				<td colspan="3">
					&nbsp;${data.deadLine}
				</td>
			</tr>
			<tr height=150>
				<th>이슈 개요</th>
				<td colspan="3" valign="top"  height="100">
					&nbsp;${data.problem}
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 				<th>참조파일</th> -->
<!-- 				<td colspan="3"> -->
<%-- 					<jsp:include page="/jsp/worldex/common/include/attachedFile.jsp" flush="true"> --%>
<%-- 						<jsp:param name="mode" value="view"/> --%>
<%-- 						<jsp:param name="choid" value="<%=oid %>"/> --%>
<%-- 						<jsp:param name="role" value="secondary"/> --%>
<%-- 					</jsp:include> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
			<tr>
				<th scope="col">${e3ps:getMessage('첨부파일')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
						<jsp:param name="oid" value="${oid}"/>
					 </jsp:include>
				</td>								
			</tr>	
		</tbody>
	</table>
</div>

<script type="text/javascript">
function issueComplete(){
	if (!confirm("완료확인 하시겠습니까?"))
		return;
	var url = getURLString("/project/issue/complate");
	
	var param = new Object();
	
	var paramArray = $("#IssueForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	ajaxCallServer(url, param, function(data){
		window.location.href=window.location.href
		if (opener.window.getGridData) {
			opener.window.getGridData();
		}
	}, true);
}
function cancelComplete(){
	if (!confirm("완료취소 하시겠습니까?"))
		return;
	var url = getURLString("/project/issue/complateCancle");
	
	var param = new Object();
	
	var paramArray = $("#IssueForm").serializeArray();
	
	$(paramArray).each(function(idx, obj){
		param[obj.name] = obj.value;
	});
	
	ajaxCallServer(url, param, function(data){
		window.location.href=window.location.href
		if (opener.window.getGridData) {
			opener.window.getGridData();
		}
	}, true);
}
</script>