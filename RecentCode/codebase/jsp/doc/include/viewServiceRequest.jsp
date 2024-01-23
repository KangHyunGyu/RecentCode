<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
		dataInit_ServiceRe();
  });

  function dataInit_ServiceRe(){
	  
	  let divisionSRList = "${doc.attributes.DIVISIONSR}";
	  let usageSRList = "${doc.attributes.USAGESR}";
	  
	  let exist = false;
	  document.getElementsByName('divisionSR').forEach((tag) => {
		  if(divisionSRList == tag.value){
			  tag.checked = true;
		  }
	  })
	  document.getElementsByName('usageSR').forEach((tag) => {
		  if(tag.value != ""){
			  if(usageSRList == tag.value){
				  tag.checked = true;
				  exist = true;
			  }
		  }else{
			  if(!exist){
					document.getElementsByName('usageSR').forEach((tag) => {
						if(tag.value == ""){
							tag.checked = true;
							$('input[id=usageSR]').attr('value',usageSRList);
						}
					})
				}
		  }
	  })
	  
  }
</script>
<br />
<div class="seach_arm2 pb5">
  <div class="leftbt">
    <span class="title">
				<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			${title}
		</span>
  </div>
  <div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width: 20%" />
			<col style="width: 30%" />
			<col style="width: 20%" />
			<col style="width: 30%" />
		</colgroup>
		<tbody>
			<tr>
				<th>${e3ps:getMessage('거래처명')}</th>
				<td>${doc.attributes.CUSTOMERNAMESR}</td>
				<th>${e3ps:getMessage('담당자')}</th>
				<td>${doc.attributes.MANAGERSR}</td>
			</tr>

			<tr>
				<th>${e3ps:getMessage('PartNumber')}</th>
				<td>${doc.attributes.PARTNUMBERSR}</td>
				<th>${e3ps:getMessage('품명')}</th>
				<td>${doc.attributes.PRODUCTNAMESR}</td>
			</tr>

			<tr>
				<th>${e3ps:getMessage('도번')}</th>
				<td>${doc.attributes.DRAWNUMBERSR}</td>
				<th>${e3ps:getMessage('BarcodeNumber')}</th>
				<td>${doc.attributes.BARCODENUMBERSR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('MarkingNumber')}</th>
				<td>${doc.attributes.MARKNUMBERSR}</td>
				<th>${e3ps:getMessage('수량')}</th>
				<td>${doc.attributes.QUANSR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('구분')}</th>
<%-- 				<td>${doc.attributes.DIVISIONSR}</td> --%>
				<td>
					<input type="radio" id="paid" name="divisionSR" value="paid" onclick="return false;" /> 
					<label for="paid">${e3ps:getMessage('유상')}</label>
					<input type="radio" id="free" name="divisionSR" value="free" onclick="return false;" /> 
					<label for="free">${e3ps:getMessage('무상')}</label>
				</td>
				<th>${e3ps:getMessage('용도')}</th>
<%-- 				<td>${doc.attributes.USAGESR}</td> --%>
				<td>
					<input type="radio" id="custReq" name="usageSR" value="custReq" onclick="return false;" /> 
					<label for="custReq">${e3ps:getMessage('고객요청')}</label>
					<input type="radio" id="selfDefect" name="usageSR" value="selfDefect" onclick="return false;"/> 
					<label for="selfDefect">${e3ps:getMessage('자사불량')}</label>
					<input type="radio" id="rework" name="usageSR" value="rework" onclick="return false;"/> 
					<label for="rework">${e3ps:getMessage('RE')}</label>
					<br>
					<label for="usageEtc">
						<input type="radio" id="usageEtc" name="usageSR" value="" onclick="return false;"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="usageSR" name="usageSR" class="w30" disabled /> )
					</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('설명')}</th>
				<td class="pd15" colspan="3">${doc.attributes.DESCRIPTIONSR}</td>
			</tr>
		</tbody>
	</table>
</div>
