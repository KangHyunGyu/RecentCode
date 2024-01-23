<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
function stopEco(){
	var param = new Object();
	$("#mainForm").attr("action", getURLString("/change/stopECOAction"));
	formSubmit("mainForm", param, "${e3ps:getMessage('중단 하시겠습니까?')}",function(){
		opener.location.reload();
	}, true);
}
function restartEco(){
	var param = new Object();
	$("#mainForm").attr("action", getURLString("/change/restartECOAction"));
	formSubmit("mainForm", param, "${e3ps:getMessage('재시작 하시겠습니까?')}",function(){
		opener.location.reload();
	}, true);
// 	if(confirm("중단 하시겠습니까?")){
// 		document.mainForm.action = "/Windchill/worldex/change/stopECOAction";
// 		document.mainForm.submit();
// 	}
	
// 	if(confirm("재시작 하시겠습니까?")){
// 		document.mainForm.action = "/Windchill/worldex/change/restartECOAction";
// 		document.mainForm.submit();
// 	}
}
</script>

<form name="mainForm" id="mainForm" method="post" enctype="multipart/form-data">
<input type="hidden" name="oid" value="${oid}"/>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
	ECO ${s}</h4></div>
	<div class="rightbt">
		<c:if test="${stop eq true}">
			<input type="button" class="s_bt03" value="중단" onclick="stopEco()">
		</c:if>
		<c:if test="${stop ne true}">
			<input type="button" class="s_bt03" value="재시작" onclick="restartEco()">
		</c:if>
		
		<input type="button" class="s_bt03" value="닫기" onclick="self.close()">
	</div>
</div>

<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<colgroup>
			<col width="100">
			<col width="*">
		</colgroup>
		<tr>
			<th>${e3ps:getMessage('사유')}</th>
			<td>
				<textArea name="comment"  style="width: 98%; height: 100px"></textArea>
			</td>
		</tr>
	</table>
</div>
</form>