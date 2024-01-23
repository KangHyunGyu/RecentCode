<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('구성원 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="saveMember();">${e3ps:getMessage('저장')}</button>
				<button type="button" class="s_bt05" onclick="self.close();">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		
		<div class="pro_table">
			<form name="editForm" id="editForm">
				<input type="hidden" id="oid" name="oid" value="${oid}">
				<table class="mainTable">
					<colgroup>
						<col style="width:30%">
						<col style="width:70%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('프로젝트 Role')}</th>
					    	<th>${e3ps:getMessage('이름')}</th>
						</tr>
						<c:forEach var="member" items="${members}" varStatus="status">
							<tr>
							    <td>${member.roleName}</td>
							    <td>
							    	<div class="pro_view">
										<select class="searchUser" id="${member.roleCode}" name="${member.roleCode}" data-width="60%">
											<c:if test="${member.peopleOid != null}">
												<option value="${member.peopleOid}">${member.userName}</option>
											</c:if>
										</select>
										<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('${member.roleCode}');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
										<span class="pointer verticalMiddle" onclick="javascript:deleteUser('${member.roleCode}');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
									</div>
							    </td>
							 </tr>
						 </c:forEach>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
function saveMember(){
	
	$("#editForm").attr("action",getURLString("/project/editMemberAction"));
	formSubmit("editForm", null, "${e3ps:getMessage('수정하시겠습니까?')}",function(){
		if(opener.window.viewProject){
			opener.window.viewProject();
		}
		window.close();
	}, true);
}
</script>