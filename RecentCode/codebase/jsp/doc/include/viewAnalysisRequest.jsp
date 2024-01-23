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
				<td class="calendar">${doc.attributes.DATERECEIPTAR}</td>
				<th>${e3ps:getMessage('완료 희망일')}</th>
				<td class="calendar">${doc.attributes.COMPDATEAR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('사용처')}</th>
<%-- 				<td>${doc.attributes.WTUSEAR}</td> --%>
				<td>
<!-- 					<input type="radio" id="customerNameAR" name="wtUseAR" value="" onclick="return false;" /> -->
<%-- 					<label for="customerName">${e3ps:getMessage('업체')} --%>
<!-- 						( <input type="text" id="customerNameAR" name="wtUseAR" class="w20" disabled/> ) -->
<!-- 					</label> -->
					<label for="customerNameAR">
						<input type="radio" id="customerNameAR" name="customerNameAR" value="" onclick="return false;" />
						${e3ps:getMessage('업체(고객사명)')} : <input type="text"  name="customerNameAR" class="w40" disabled />
					</label>
					<br>
					<input type="radio" id="refDepartmentAR" name="refDepartmentAR" value="" onclick="return false;" />
					<label for="refDepartmentAR">
						${e3ps:getMessage('사내(의뢰부서)')} : <input type="text"  name="refDepartmentAR" class="w40" disabled/> 
					</label>
				</td>
				<th>${e3ps:getMessage('분석 수량')}</th>
				<td>${doc.attributes.QUANAR}</td>
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
				<td>${doc.attributes.PRODUCTNAMEAR}</td>
				<th>${e3ps:getMessage('시편(제품)업체명')}</th>
				<td>${doc.attributes.SPECMANUFACTUREAR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('목적')}</th>
				<td>${doc.attributes.PURPOSEAR}</td>
				<th>${e3ps:getMessage('분석 기관')}</th>
<%-- 				<td>${doc.attributes.ANALYSISAGENCYAR}</td> --%>
				<td>
					<label for="withinComp">
						<input type="radio" id="analysisAgencyInAR" name="analysisAgencyInAR" value="" onclick="return false;" />
						${e3ps:getMessage('사내')} ( <input type="text"  name="analysisAgencyInAR" class="w20" disabled /> )
					</label>
					
					<label for="outsideComp">
						<input type="radio" id="analysisAgencyOutAR" name="analysisAgencyOutAR" value="" onclick="return false;" />
						${e3ps:getMessage('사외')} ( <input type="text"  name="analysisAgencyOutAR" class="w20" disabled /> )
					</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('분석 항목')}</th>
				<td>${doc.attributes.ANALYSISITEMAR}</td>
				<th>${e3ps:getMessage('소재 재질')}</th>
				<td>${doc.attributes.DOCMATERIALAR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('시료명')}</th>
				<td>${doc.attributes.SAMPLEDATANAMEAR}</td>
				<th>${e3ps:getMessage('분석 비용 처리')}</th>
<%-- 				<td>${doc.attributes.DIVISIONAR}</td> --%>
				<td>
					<input type="radio" id="paid" name="divisionAR" value="유상(내부결재 품의)" onclick="return false;" />
					<label for="paid">${e3ps:getMessage('유상(내부결재 품의)')}</label>
					<input type="radio" id="free" name="divisionAR" value="무상" onclick="return false;" />
					<label for="free">${e3ps:getMessage('무상')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('설명')}</th>
				<td class="pd15" colspan="3">${doc.attributes.DESCRIPTIONAR}</td>
			</tr>
		</tbody>
	</table>
</div>
