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
  
  //구분
  function selectedDivisionSR(element) {
   var divisionSR = $("input[name=divisionSR]:checked").val();
  }
  //용도
  function selectedUsageSR(element) {
   var usageSR = $("input[name=usageSR]:checked").val();
  }
  
  function etcEventHandle(element){
		
		const elementName = element.getAttribute('name');
		
		Array.prototype.slice.call(document.querySelectorAll('label')).forEach((item) => {
			let labelChildArray = Array.prototype.slice.call(item.childNodes)
			let textExist = labelChildArray.some((l)=>l.nodeName='INPUT' && l.type == 'text'  && l.name == elementName )
			labelChildArray.forEach((x)=> {
				if(x.nodeName=='INPUT' && x.type == 'radio' && x.name == elementName && textExist){
					element = x;
				}
			})
		})
		
		let textNode = document.querySelector('input[name='+elementName+'][type=text]');
		
		if(element.checked){
			textNode.disabled = false;
		}else{
			textNode.disabled = true;
		}
		
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
				<td><input type="text" class="w50" id="customerNameSR" name="customerNameSR" value="${doc.attributes.CUSTOMERNAMESR}" >
<!-- 					<div class="pro_view"> -->
<!-- 						<select id="customerNameSR" name="customerNameSR" data-width="70%"></select> -->
<!-- 						<span class="pointer verticalMiddle" onclick="javascript:deleteUser('customerNameSR');"> -->
<!-- 							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" /> -->
<!-- 						</span> -->
<!-- 					</div> -->
				</td>
				<th>${e3ps:getMessage('담당자')}</th>
				<td>
					<div class="pro_view"> 
						<select class="searchUser" id="managerSR" name="managerSR" data-width="70%" data-valuetoname="true" >
							<option value="<c:out value="${doc.attributes.MANAGERSR}"/>">
				       			<c:out value="${doc.attributes.MANAGERSR}"/>
						</select> 
						<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('managerSR', 'single');">
							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png" />
						</span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteUser('managerSR');">
							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" />
						</span>
					</div>
				</td>
			</tr>

			<tr>
				<th>${e3ps:getMessage('PartNumber')}</th>
				<td>
					<input type="text" class="w50" id="partNumberSR" name="partNumberSR" value="${doc.attributes.PARTNUMBERSR}" >
				</td>
				<th>${e3ps:getMessage('품명')}</th>
				<td>
					<input type="text" class="w50" id="productNameSR" name="productNameSR" value="${doc.attributes.PRODUCTNAMESR}" >
				</td>
			</tr>

			<tr>
				<th>${e3ps:getMessage('도번')}</th>
				<td>
					<input type="text" class="w50" id="drawNumberSR" name="drawNumberSR" value="${doc.attributes.DRAWNUMBERSR}" >
				</td>
				<th>${e3ps:getMessage('BarcodeNumber')}</th>
				<td>
					<input type="text" class="w50" id="barcodeNumberSR" name="barcodeNumberSR" value="${doc.attributes.BARCODENUMBERSR}" >
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('MarkingNumber')}</th>
				<td>
					<input type="text" class="w50" id="markNumberSR" name="markNumberSR" value="${doc.attributes.MARKNUMBERSR}" >
				</td>
				<th>${e3ps:getMessage('수량')}</th>
				<td>
					<input type="text" class="w50" id="quanSR" name="quanSR" value="${doc.attributes.QUANSR}" >
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('구분')}</th>
				<td>
					<input type="radio" id="paid" name="divisionSR" value="paid" onchange="selectedDivisionSR(this)" /> 
					<label for="paid">${e3ps:getMessage('유상')}</label>
					<input type="radio" id="free" name="divisionSR" value="free" onchange="selectedDivisionSR(this)" /> 
					<label for="free">${e3ps:getMessage('무상')}</label>
				</td>
				<th>${e3ps:getMessage('용도')}</th>
				<td>
					<input type="radio" id="custReq" name="usageSR" value="custReq" onchange="selectedUsageSR(this)" onclick="etcEventHandle(this)" /> 
					<label for="custReq">${e3ps:getMessage('고객요청')}</label>
					<input type="radio" id="selfDefect" name="usageSR" value="selfDefect" onchange="selectedUsageSR(this)" onclick="etcEventHandle(this)"/> 
					<label for="selfDefect">${e3ps:getMessage('자사불량')}</label>
					<input type="radio" id="rework" name="usageSR" value="rework" onchange="selectedUsageSR(this)" onclick="etcEventHandle(this)"/> 
					<label for="rework">${e3ps:getMessage('RE')}</label>
					<br>
					<label for="usageEtc">
						<input type="radio" id="usageEtc" name="usageSR" value="" onchange="selectedUsageSR(this)" onclick="etcEventHandle(this)"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="usageSR" name="usageSR" class="w20" /> )
					</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('설명')}</th>
				<td class="pd15" colspan="3">
					<div class="textarea_autoSize">
						<textarea name="descriptionSR" id="descriptionSR"
							onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${doc.attributes.DESCRIPTIONSR}</textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
