<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
	  dataInit_AnalysisRe();
  });
  
  function dataInit_AnalysisRe(){
		
		let customerNameARList = "${doc.attributes.CUSTOMERNAMEAR}";
		let refDepartmentARList = "${doc.attributes.REFDEPARTMENTAR}";
		let analysisAgencyInARList = "${doc.attributes.ANALYSISAGENCYINAR}";
		let analysisAgencyOutARList = "${doc.attributes.ANALYSISAGENCYOUTAR}";
		let divisionARList= "${doc.attributes.DIVISIONAR}";
		
		let exist = false;
		
		  document.getElementsByName('customerNameAR').forEach((tag) => {
			  if(customerNameARList){
				  tag.checked = true;
				  exist = true;
				  $('input[name=customerNameAR][type=text]').attr('value',customerNameARList);
			  }
		  })
		  document.getElementsByName('refDepartmentAR').forEach((tag) => {
			  if(refDepartmentARList){
				  tag.checked = true;
				  exist = true;
				  $('input[name=refDepartmentAR][type=text]').attr('value',refDepartmentARList);
			  }
		  })
		  document.getElementsByName('analysisAgencyInAR').forEach((tag) => {
			  if(analysisAgencyInARList){
				  tag.checked = true;
				  exist = true;
				  $('input[name=analysisAgencyInAR][type=text]').attr('value',analysisAgencyInARList);
			  }
		  })
		  document.getElementsByName('analysisAgencyOutAR').forEach((tag) => {
			  if(analysisAgencyOutARList){
				  tag.checked = true;
				  exist = true;
				  $('input[name=analysisAgencyOutAR][type=text]').attr('value',analysisAgencyOutARList);
			  }
		  })
		
		document.getElementsByName('divisionAR').forEach((tag) => {
			  if(divisionARList == tag.value){
				  tag.checked = true;
				  exist = true;
			  }
		  })  

	}
  
  function radioTextEventHandle(element){
		
	  let targetEle = document.getElementById(element.dataset.opp);
	  if(element.checked){
		  targetEle.checked = !element.checked;
		  let textTag1 = document.querySelector("input[name="+element.dataset.opp+"][type=text]");
		  let textTag2 = document.querySelector("input[name="+element.name+"][type=text]");
		  if(textTag1) textTag1.disabled = true;
		  if(textTag2) textTag2.disabled = false;
	  }
	}
  
  function radioSelectEventHandle(element){
		
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
				<td class="calendar"><input type="text" class="datePicker w50" id="dateReceiptAR" name="dateReceiptAR" value="${doc.attributes.DATERECEIPTAR}" ></td>
				<th>${e3ps:getMessage('완료 희망일')}</th>
				<td class="calendar"><input type="text" class="datePicker w50" id="compDateAR" name="compDateAR" value="${doc.attributes.COMPDATEAR}" ></td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('사용처')}</th>
					<td>
					<label for="customerNameAR">
						<input type="radio" id="customerNameAR" name="customerNameAR" data-opp="refDepartmentAR" value="" onclick="radioSelectEventHandle(this)" />
						${e3ps:getMessage('업체(고객사명)')} : <input type="text" name="customerNameAR" class="w40" disabled />
					</label>
					<br>
					<input type="radio" id="refDepartmentAR" name="refDepartmentAR" data-opp="customerNameAR" value="" onclick="radioSelectEventHandle(this)" />
					<label for="refDepartmentAR">${e3ps:getMessage('사내(의뢰부서)')}</label>
					<div class="pro_view">
	                	<select class="searchDepartment" id="refDepartmentAR_select" name="refDepartmentAR" data-width="60%" data-valuetoname="true" >
	                		<option value="<c:out value="${doc.attributes.REFDEPARTMENTAR}"/>">
				       			<c:out value="${doc.attributes.REFDEPARTMENTAR}"/>
	                	</select>
                		<span class="pointer verticalMiddle" onclick="javascript:openDepartmentPopup('refDepartmentAR_select');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteDepartment('refDepartmentAR_select');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
	              </div>
				</td>
				<th>${e3ps:getMessage('분석 수량')}</th>
				<td><input type="text" class="w50" id="quanAR" name="quanAR" value="${doc.attributes.QUANAR}" ></td>
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
				<td><input type="text" class="w90" id="productNameAR" name="productNameAR" value="${doc.attributes.PRODUCTNAMEAR}" ></td>
				<th>${e3ps:getMessage('시편(제품)업체명')}</th>
				<td><input type="text" class="w90" id="specManufactureAR" name="specManufactureAR" value="${doc.attributes.SPECMANUFACTUREAR}" ></td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('목적')}</th>
				<td><input type="text" class="w90" id="purposeAR" name="purposeAR" value="${doc.attributes.PURPOSEAR}" ></td>
				<th>${e3ps:getMessage('분석 기관')}</th>
				<td>
					<label for="analysisAgencyInAR">
						<input type="radio" id="analysisAgencyInAR" name="analysisAgencyInAR" data-opp="analysisAgencyOutAR" value="" onclick="radioTextEventHandle(this)"/>
						${e3ps:getMessage('사내')} ( <input type="text" name="analysisAgencyInAR" class="w20" /> )
					</label>
					
					<label for="analysisAgencyOutAR">
						<input type="radio" id="analysisAgencyOutAR" name="analysisAgencyOutAR" data-opp="analysisAgencyInAR" value="" onclick="radioTextEventHandle(this)"/>
						${e3ps:getMessage('사외')} ( <input type="text" name="analysisAgencyOutAR" class="w20" /> )
					</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('분석 항목')}</th>
				<td><input type="text" class="w90" id="analysisItemAR" name="analysisItemAR" value="${doc.attributes.ANALYSISITEMAR}" ></td>
				<th>${e3ps:getMessage('소재 재질')}</th>
				<td><input type="text" class="w90" id="docMaterialAR" name="docMaterialAR" value="${doc.attributes.DOCMATERIALAR}" ></td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('시료명')}</th>
				<td><input type="text" class="w90" id="sampleDataNameAR" name="sampleDataNameAR" value="${doc.attributes.SAMPLEDATANAMEAR}" ></td>
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
							onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${doc.attributes.DESCRIPTIONAR}</textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
