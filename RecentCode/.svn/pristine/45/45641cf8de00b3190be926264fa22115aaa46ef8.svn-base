<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
});
</script>
<!-- button -->
<div class="seach_arm pt10 pb10">
	<div class="leftbt">
		<h4>${e3ps:getMessage('결재 위임')}</h4>
	</div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<!-- pro_table -->
<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<colgroup>
			<col style="width:15%">
			<col style="width:85%">
		</colgroup>	
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('결재 위임하기')}</th>
				<td>
					${e3ps:getMessage('위임자')} : <select class="searchUser w20" id="delegateUser" name="delegateUser" data-width="20%"></select> <button type="button" class="s_bt03" onclick="delegateAction(this)">${e3ps:getMessage('위임')}</button>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<!-- //pro_table -->