<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="seach_arm2 pt5 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		${e3ps:getMessage('담당자')}</span>
	</div>
	<div class="rightbt">
		<c:if test="${isTaskEdit}">
			<input type="button" class="s_bt03" value="${e3ps:getMessage('편집')}"  onclick="selectRole()"/>
		</c:if>
	</div> 
</div>
<table class="mainTable mb25">
	<tr>
		<th class="tac">${e3ps:getMessage('프로젝트')} Role</th>
		<th class="tac">${e3ps:getMessage('직위')}</th>
		<th class="tac">${e3ps:getMessage('이름')}</th>
		<th class="tac">E-Mail</th>
	</tr>
	<c:forEach var="owner" items="${owners}" varStatus="status">
		<tr>
 			<td class="tac">${owner.roleName}</td>
		    <td class="tac">${owner.duty}</td>
		    <td class="tac">${owner.userName}</td>
		    <td>${owner.email}</td>
		 </tr>
	</c:forEach>
</table>
<script type="text/javascript">
function selectRole(){
	
	var oid = "${oid}";
	
	var url = getURLString("/project/selectRole") + "?oid=" + oid;
	
	openPopup(url, "addOutput", 500, 550);
}
</script>