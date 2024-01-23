<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	let alphabetList = '${list}';
	alphabetList = Array.from(alphabetList);
	alphabetList = alphabetList.filter((x) => {
		return x != ' ' && x != ',' && x != '[' && x != ']'
	})
	
	let revesionEle = document.querySelector('#revision');
	
	var objOption = document.createElement("option");
	objOption.text = `${e3ps:getMessage('선택')}`;
	objOption.value = '';
	revesionEle.options.add(objOption);
	
	alphabetList.forEach((alphabet) => {
		var objOption = document.createElement("option");
		objOption.text = '[ '+alphabet+' ]';
		objOption.value = alphabet;
		revesionEle.options.add(objOption);
	})
	
})

function createNewRevisionPart(){
	
	if(isEmpty(getElementValue('revision'))){
		alert("${e3ps:getMessage('리비전을 선택해주세요')}.");
		return;
	}
	
	openConfirm("${e3ps:getMessage('생성하시겠습니까?')}", function(){
		
		let param = new Object(); 
		param = getFormParams('partRevisionForm', param);
		
		var url	= getURLString("/part/addRevisionAction");
		ajaxCallServer(url, param, function(data){
			
			window.opener.location.href= data.redirectUrl;
			
			if(opener.parent.opener.parent.search) {
				opener.parent.opener.parent.search();
			}
			
			window.close();
		}, true);
	})
}

</script>
<div class="pop5">
	<!-- top -->
	<div class="top">
		<%-- <h2>${part.icon} ${e3ps:getMessage('부품')} - ${part.number}, ${part.name}, ${part.version}</h2> --%>
		<h2>${e3ps:getMessage('리비전 추가')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->
	<div class="pl25 pr25">
		<form name="partRevisionForm" id="partRevisionForm" method="post">
			<input type="hidden" name="oid"  id="oid" value="${part.oid}" />
			<input type="hidden" name="wtPartType"		id="wtPartType"		 	value="separable"     />
			<input type="hidden" name="source"			id="source"	      		value="make"            />
			<input type="hidden" name="lifecycle"   	id="lifecycle"			value="LC_Default"  />
			<input type="hidden" name="view"			id="view"        		value="Design" />
			<div class="seach_arm2 pt10 pb10">
				<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('리비전 선택')}</h4></div>
				<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:createNewRevisionPart()">${e3ps:getMessage('저장')}</button>
				</div>
			</div>
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:40%">
						<col style="width:60%">
					</colgroup>
					<tbody>
						<tr>
							<th scope="col">${e3ps:getMessage('현재 부품 번호')}</th>
							<td>${part.number}</td>
						</tr>
						<tr>
							<th scope="col">${e3ps:getMessage('추가할 리비전 선택')}</th>
							<td><select id="revision" name="revision" class="w30"></select></td>
						</tr>
					</tbody>
				</table>
			</div>
				
				<%-- <jsp:include page="${e3ps:getIncludeURLString('/part/include_partAttributes')}" flush="true">
					<jsp:param name="oid" value="${part.oid}"/>
					<jsp:param name="module" value="modify"/>
				</jsp:include> --%>
		</form>
	</div>
</div>
