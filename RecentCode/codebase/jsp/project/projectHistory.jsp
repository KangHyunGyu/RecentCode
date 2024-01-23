<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 수정이력')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt"></div>
			<div class="rightbt">
			</div>
		</div>
		
		<div class="pro_table">
			<table class="mainTable">
				<colgroup>
					<col style="width:10%">
					<col style="width:30%">
					<col style="width:20%">	
					<col style="width:20%">
					<col style="width:20%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col" align="center">${e3ps:getMessage('버전')}</th>
						<th scope="col" align="center">${e3ps:getMessage('프로젝트 명')}</th>
						<th scope="col" align="center">${e3ps:getMessage('최초 등록일')}</th>
						<th scope="col" align="center">${e3ps:getMessage('등록자')}</th>
						<th scope="col" align="center">${e3ps:getMessage('수정사유')}</th>
					</tr>
					<c:forEach var="project" items="${projectList}" varStatus="status">
						<tr>
							<td align="center">${project.version}</td>
							<td align="left"><a href="javascript:viewProject('${project.oid}')">${project.name}</a></td>
							<td align="center">${project.createDate}</td>
							<td align="center">${project.creatorFullName}</td>
							<td align="left">${project.checkInComment}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script type="text/javascript">
function viewProject(oid){
	
	var url= getURLString("/project/viewMain") + "?oid=" + oid;
	
	opener.window.open(url);
}
</script>