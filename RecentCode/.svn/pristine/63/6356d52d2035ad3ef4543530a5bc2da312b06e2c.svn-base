<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
function save(){
	
	$("#editForm").attr("action",getURLString("/gate/modifyObjectValue"));
	formSubmit("editForm", null, "${e3ps:getMessage('수정하시겠습니까?')}",function(){
		opener.window.location.reload();
		window.close();
	}, true);
}
</script>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('항목 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb5">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="save();">${e3ps:getMessage('저장')}</button>
				<button type="button" class="s_bt05" onclick="self.close();">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		
		<div class="pro_table">
			<form name="editForm" id="editForm">
				<input type="hidden" id="oid" name="oid" value="${data.oid}">
				<table class="mainTable">
					<colgroup>
						<col style="width:15%">
						<col style="width:35%">
						<col style="width:15%">
						<col style="width:35%">
					</colgroup>	
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('code')}</th>
							<td><input type="text" name="code" id="code" value="${data.code}" readonly="readonly" style="background-color: #e2e0e0;"></td>
					    	<th scope="col">${e3ps:getMessage('name')}</th>
					    	<td><input type="text" name="name" id="name" value="${data.name}"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('value0')}</th>
							<td><input type="text" name="value0" id="value0" value="${data.value0}" readonly="readonly" style="background-color: #e2e0e0;"></td>
					    	<th scope="col">${e3ps:getMessage('value1')}</th>
						    <td><input type="text" name="value1" id="value1" value="${data.value1}" readonly="readonly" style="background-color: #e2e0e0;"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('value2')}</th>
							<td><input type="text" name="value2" id="value2" value="${data.value2}" readonly="readonly" style="background-color: #e2e0e0;"></td>
					    	<th scope="col">${e3ps:getMessage('value3')}</th>
						    <td><input type="text" name="value3" id="value3" value="${data.value3}" readonly="readonly" style="background-color: #e2e0e0;"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('value4')}</th>
							<td><input type="text" name="value4" id="value4" value="${data.value4}" readonly="readonly" style="background-color: #e2e0e0;"></td>
					    	<th scope="col">${e3ps:getMessage('value5')}</th>
						    <td><input type="text" name="value5" id="value5" value="${data.value5}" readonly="readonly" style="background-color: #e2e0e0;"></td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('value6')}</th>
							<td><input type="text" name="value6" id="value6" value="${data.value6}" readonly="readonly" style="background-color: #e2e0e0;"></td>
					    	<th scope="col">${e3ps:getMessage('value7')}</th>
						    <td><input type="text" name="value7" id="value7" value="${data.value7}" readonly="readonly" style="background-color: #e2e0e0;"></td>
						</tr>
						<tr>
					    	<th scope="col">${e3ps:getMessage('value8')}</th>
					    	<td><input type="text" name="value8" id="value8" value="${data.value8}" readonly="readonly" style="background-color: #e2e0e0;"></td>
					    	<th scope="col">${e3ps:getMessage('value9')}</th>
						    <td><input type="text" name="value9" id="value9" value="${data.value9}" readonly="readonly" style="background-color: #e2e0e0;"></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
