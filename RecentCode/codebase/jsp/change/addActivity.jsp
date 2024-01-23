<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
function addActivity(){
	var f = document.changeForm;
// 	if(checkField(f.name,"이름")){
// 		return;
// 	}
// 	if(checkField(f.engName,"영문명")){
// 		return;
// 	}
// 	if(checkField(f.step,"단계")){
// 		return;
// 	}
// 	if(checkField(f.activeType,"활동구분")){
// 		return;
// 	}	
	var data = new Object();
	data.name = f.name.value;
	data.engName = f.engName.value;
	data.step = f.step.value;
	data.stepName = f.step.value;
	data.type = f.activeType.value;
	data.ownerOid = f.creator.value;
	data.ownerName = f.creator.value;
	data.creator = f.creator.value;
	data.description = f.description.value;
	opener.addActive(data);
	opener.resetRowspan();
	self.close();
}
</script>
<form name="changeForm" method="post">
<input type="hidden" name="root" value="${oid }">
<div class="seach_arm pt20 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">설계변경 활동 생성</span>
	</div>
	<div class="rightbt">
		<input onclick="addActivity()" class="s_bt03" type="button" value="저장"> 
		<input onclick="self.close()" class="s_bt06" type="button" value="닫기">		
	</div>
</div>
<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
			<col width="150">
			<col width="*">
			</colgroup>
				<tr> 
					<th>이름</th>
					<td>
					<input type="text" name="name" size="85" class="input01" style="width:98%" />
					</td>
				</tr>
				<tr>
					<th>영문명</th>
					<td>
					<input type="text" name="engName"  size="85" class="input01" style="width:98%" />
					</td>
				</tr>
				<tr> 
					<th>단계</th>
					<td>
							<jsp:include page="/jsp/change/include2/numberCodeSelect.jsp" flush="true">
							<jsp:param name="inputName" value="step"/>
							<jsp:param name="codeType" value="EOSTEP"/>
							</jsp:include>
					</td>
				</tr>
				<tr>
					<th>활동구분</th>
					<td>
							<jsp:include page="/jsp/change/include2/numberCodeSelect.jsp" flush="true">
							<jsp:param name="inputName" value="activeType"/>
							<jsp:param name="codeType" value="EOACTIVETYPE"/>
							</jsp:include>
					</td>
				</tr>
				<tr>
					<th>담당자</th>
					<td>
						<div class="pro_view">
							<select class="searchUser" id="creator" name="creator" multiple data-width="60%">
							</select>
							<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
							<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
						</div>
					</td>
				</tr>
				<tr>
					<th>설명</th>
					<td>
					<textArea name="description"  size="85" class="input01" style="width:98%;height:100px" ></textArea>
					</td>
				</tr>
			</table>
			</div>					
</form>