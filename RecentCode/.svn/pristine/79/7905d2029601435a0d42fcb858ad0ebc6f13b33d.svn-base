<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
	  
  });
	
  
  function radioTextEventHandle(element){
		
	  let targetEle = document.getElementById(element.dataset.opp);
	  if(element.checked){
		  targetEle.checked = !element.checked;
		  let textTag1 = document.querySelector("input[name="+element.dataset.opp+"][type=text]");
		  let textTag2 = document.querySelector("input[name="+element.name+"][type=text]");
		  
		  if(textTag1) {
			  textTag1.disabled = true;
			  $("#refDepartmentAR_select").removeAttr("disabled");
		  }
		  if(textTag2) {
			  textTag2.disabled = false;
			  $("#refDepartmentAR_select").attr("disabled", true);
		  } 
		  
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
				<th>${e3ps:getMessage('요청일')}</th>
				<td class="calendar"><input type="text" class="datePicker w50" id="dateReceiptAR" name="dateReceiptAR" readonly></td>
				<th>${e3ps:getMessage('완료 희망일')}</th>
				<td class="calendar"><input type="text" class="datePicker w50" id="compDateAR" name="compDateAR" readonly></td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('사용처')}</th>
				<td>
					<label for="customerNameAR">
						<input type="radio" id="customerNameAR" name="customerNameAR" data-opp="refDepartmentAR" value="" onclick="radioTextEventHandle(this)" />
						${e3ps:getMessage('업체(고객사명)')} : <input type="text" name="customerNameAR" class="w40" disabled />
					</label>
<!-- 					<br> -->
<!-- 					<input type="text" class="w40" id="customerNameAR" name="wtUseAR"> -->
					<br>
<!-- 					<div class="pro_view"> -->
<!-- 	                	<select id="customerNameAR" name="wtUseAR" data-width="80%" ></select> -->
<!-- 	                	<span class="pointer verticalMiddle" onclick="javascript:deleteUser('customerNameAR');" > -->
<!-- 	                		<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" /> -->
<!-- 	                </span> -->
<!-- 	              </div> -->
					<input type="radio" id="refDepartmentAR" name="refDepartmentAR" data-opp="customerNameAR" value="" onclick="radioTextEventHandle(this)" />
					<label for="refDepartmentAR">${e3ps:getMessage('사내(의뢰부서)')}</label>
					<div class="pro_view">
<!-- 	                	<select id="refDepartmentAR" name="wtUseAR" data-width="80%" ></select> -->
	                	<select class="searchDepartment" id="refDepartmentAR_select" name="refDepartmentAR" data-width="60%" data-valuetoname="true" ></select>
<!-- 	                	<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('refDepartmentAR', 'single');" > -->
<!-- 	                		<img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png" /></span> -->
<!-- 	                	<span class="pointer verticalMiddle" onclick="javascript:deleteUser('refDepartmentAR');" > -->
<!-- 	                		<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" /></span> -->
                		<span class="pointer verticalMiddle" onclick="javascript:openDepartmentPopup('refDepartmentAR_select');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteDepartment('refDepartmentAR_select');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
	              </div>
				</td>
				<th>${e3ps:getMessage('분석 수량')}</th>
				<td><input type="text" class="w50" id="quanAR" name="quanAR"></td>
			</tr>
			<tr>
				<th colspan="4" style="gorder-top: 2px solid #dedede; border-bottom:1px solid #eff3f6;" >
			</tr>
			<tr>
				<th colspan="4" style="border-top: 10px solid #eff3f6; border-bottom: 2px solid #74AF2A;" >
				</th>
			</tr>
			<tr>
				<th colspan="4" style="text-align:center;">${e3ps:getMessage('분석 요청 내용 (전 항목 필수 기재, 누락 시 분석 불가)')}</th>
			</tr>
			<tr>
				<th>${e3ps:getMessage('개발(제품)명')}</th>
				<td><input type="text" class="w50" id="productNameAR" name="productNameAR"></td>
				<th>${e3ps:getMessage('시편(제품)업체명')}</th>
				<td><input type="text" class="w50" id="specManufactureAR" name="specManufactureAR"></td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('목적')}</th>
				<td><input type="text" class="w50" id="purposeAR" name="purposeAR"></td>
				<th>${e3ps:getMessage('분석 기관')}</th>
				<td>
					<label for="analysisAgencyInAR">
						<input type="radio" id="analysisAgencyInAR" name="analysisAgencyInAR" data-opp="analysisAgencyOutAR" value="" onclick="radioTextEventHandle(this)"/>
						${e3ps:getMessage('사내')} ( <input type="text" name="analysisAgencyInAR" class="w20" disabled /> )
					</label>
					
					<label for="analysisAgencyOutAR">
						<input type="radio" id="analysisAgencyOutAR" name="analysisAgencyOutAR" data-opp="analysisAgencyInAR" value="" onclick="radioTextEventHandle(this)"/>
						${e3ps:getMessage('사외')} ( <input type="text" name="analysisAgencyOutAR" class="w20" disabled /> )
					</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('분석 항목')}</th>
				<td><input type="text" class="w50" id="analysisItemAR" name="analysisItemAR"></td>
				<th>${e3ps:getMessage('소재 재질')}</th>
				<td><input type="text" class="w50" id="docMaterialAR" name="docMaterialAR"></td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('시료명')}</th>
				<td><input type="text" class="w50" id="sampleDataNameAR" name="sampleDataNameAR"></td>
				<th>${e3ps:getMessage('분석 비용 처리')}</th>
				<td>
					<input type="radio" id="paid" name="divisionAR" value="유상(내부결재 품의)" />
					<label for="paid">${e3ps:getMessage('유상(내부결재 품의)')}</label>
					<input type="radio" id="free" name="divisionAR" value="무상" />
					<label for="free">${e3ps:getMessage('무상')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('설명')}</th>
				<td class="pd15" colspan="3">
					<div class="textarea_autoSize">
						<textarea name="descriptionAR" id="descriptionAR"
							onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
