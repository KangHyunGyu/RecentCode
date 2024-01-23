<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
	  dataInit_ContractDev();
  });

  function dataInit_ContractDev(){
	  
	let consultMethodCDRList = '${doc.attributes.CONSULTMETHODCDR}';
	let applicationMaterialCDRList = '${doc.attributes.APPLICATIONMATERIALCDR}'.split(', ');
	let reciptionClassCDRList = "${doc.attributes.RECEPTIONCLASSCDR}";
	
	let exist = false;
	document.getElementsByName('consultMethodCDR').forEach((tag) => {
		if(consultMethodCDRList == tag.value){
			tag.checked = true;
		}
	})
	applicationMaterialCDRList.forEach((val)=> {
		let exist = false;
		document.getElementsByName('applicationMaterialCDR').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			} 
		})
		if(!exist){
			document.getElementsByName('applicationMaterialCDR').forEach((tag) => {
				if(tag.value == ""){
					tag.checked = true;
					$('input[id=applicationMaterialCDR]').attr('value',val);
				}
			})
		}
	})
	document.getElementsByName('receptionClassCDR').forEach((tag) => {
		if(tag.value != ""){
			if(reciptionClassCDRList == tag.value){
				  tag.checked = true;
				  exist = true;
			  }
		}else {
			if(!exist){
				document.getElementsByName('receptionClassCDR').forEach((tag) => {
					if(tag.value == ""){
						tag.checked = true;
						$('input[id=receptionClassCDR]').attr('value',reciptionClassCDRList);
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
			<col style="width: 9%" />
			<col style="width: 11%" />
			<col style="width: 30%" />
			<col style="width: 20%" />
			<col style="width: 30%" />
		</colgroup>
		<tbody>
			<tr>
				<th colspan="2">${e3ps:getMessage('고객명')}</th>
				<td>${doc.attributes.CUSTOMERNAMECDR}</td>
				<th >${e3ps:getMessage('상담자(고객)')}</th>
				<td>${doc.attributes.COUNSECUSTOMERCDR}</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('연락처')}</th>
				<td>${doc.attributes.CONTACTCDR}</td>
				<th>${e3ps:getMessage('접수일자')}</th>
				<td class="calender">${doc.attributes.DATERECEIPTCDR}</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('상담 방법')}</th>
<%-- 				<td>${doc.attributes.CONSULTMETHODCDR}</td> --%>
				<td>
					<input type="radio" id="visit" name="consultMethodCDR" value="visit" onclick="return false;" /> 
						<label for="visit">${e3ps:getMessage('방문')}</label> 
					<input type="radio" id="email" name="consultMethodCDR" value="email" onclick="return false;" /> 
						<label for="email">${e3ps:getMessage('전자메일')}</label>
					<input type="radio" id="fax" name="consultMethodCDR" value="fax" onclick="return false;" /> 
						<label for="fax">${e3ps:getMessage('FAX')}</label>
				</td>
				<th>${e3ps:getMessage('접수자')}</th>
				<td>${doc.attributes.RECEPTIONISTCDR}</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('접수 자료')}</th>
<%-- 				<td colspan="3">${doc.attributes.APPLICATIONMATERIALCDR}</td> --%>
				<td colspan="3">
					<label for="devRequest">${e3ps:getMessage('도면 [ ')}</label>
					<label class="mr5"><input type="checkbox" name="applicationMaterialCDR" value="CAD" onclick="return false;" >${e3ps:getMessage('CAD')}</label>
					<label class="mr5"><input type="checkbox" name="applicationMaterialCDR" value="PDF" onclick="return false;" >${e3ps:getMessage('pdf')}</label>
					<label class="mr5"><input type="checkbox" name="applicationMaterialCDR" value="JPG" onclick="return false;" >${e3ps:getMessage('JPG')}</label>
					<label class="mr5"><input type="checkbox" name="applicationMaterialCDR" value="" onclick="return false;" >${e3ps:getMessage('기타')} ( <input type="text" id="applicationMaterialCDR" name="applicationMaterialCDR" class="w20" disabled /> )</label> 
					<label>${e3ps:getMessage('] ')}</label>	
						
					<label class="ml10" for="reciptionSample">${e3ps:getMessage('제품 SAMPLE (')}</label>
					<label class="mr5"><input type="radio" name="applicationMaterialCDR" value="cov" onclick="return false;" >${e3ps:getMessage('회수')}</label>
					<label class="mr5"><input type="radio" name="applicationMaterialCDR" value="unCov" onclick="return false;" >${e3ps:getMessage('미회수')}</label>
					<label>${e3ps:getMessage(') ')}</label>	
				</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('접수 구분')}</th>
<%-- 				<td colspan="3">${doc.attributes.RECEPTIONCLASSCDR}</td> --%>
				<td colspan="3">
					<input type="radio" id="bidding" name="receptionClassCDR" value="Bidding" onclick="return false;"  /> 
						<label for="productSample">${e3ps:getMessage('Bidding : 입찰경쟁')}</label> 
					<input type="radio" id="soleBid" name="receptionClassCDR" value="exBidding" onclick="return false;" /> 
						<label for="devProposal">${e3ps:getMessage('단독 입찰')}</label> 
					<label for="reciptionClassEtc">
						<input type="radio" id="reciptionClassEtc" name="receptionClassCDR" value="" onclick="return false;"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="receptionClassCDR" name="receptionClassCDR" class="w10" disabled /> )
					</label>
			</tr>
			<tr>
				<th rowspan="3" style="text-align: center; border: 1px solid #cdcdcd; border-collapse: collapse;" >${e3ps:getMessage('제품 정보')}</th>
				<th>${e3ps:getMessage('품명')}</th>
				<td>${doc.attributes.PRODUCTNAMECDR}</td>
				<th>${e3ps:getMessage('규격')}</th>
				<td>${doc.attributes.PRODUCTSTANDARDCDR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('수량')}</th>
				<td>${doc.attributes.QUANCDR}</td>
				<th>${e3ps:getMessage('가격')}</th>
				<td>${doc.attributes.PRICECDR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('제출 기한')}</th>
				<td class="calender">${doc.attributes.SUBMISSIONDEADLINECDR}</td>
				<th>${e3ps:getMessage('납기')}</th>
				<td class="calender">${doc.attributes.DUEDATECDR}</td>
			</tr>
			<tr>
				<th rowspan="3" style="text-align: center;border: 1px solid #cdcdcd;border-collapse: collapse;" >${e3ps:getMessage('요구 사항')}</th>
				<th>${e3ps:getMessage('자사 도면화')}</th>
				<td class="calender">${doc.attributes.COMPANYDRAWCDR}</td>
				<th>${e3ps:getMessage('공정 개발 검토서')}</th>
				<td class="calender">${doc.attributes.PROCESSDEVREVCDR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('SAMPLE 제출')}</th>
				<td class="calender">${doc.attributes.SAMPLESUBCDR}</td>
				<th>${e3ps:getMessage('Re 리포트')}</th>
				<td class="calender">${doc.attributes.REREPORTCDR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('분석 의뢰')}</th>
				<td class="calender">${doc.attributes.REQANALYSISCDR}</td>
				<th>${e3ps:getMessage('기타')}</th>
				<td>${doc.attributes.REQUIREMENTETCCDR}</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('설명')}</th>
				<td class="pd15" colspan="3">${doc.attributes.DESCRIPTIONCDR}</td>
<!-- 				<td class="pd15" colspan="3"> -->
<!-- 					<div class="textarea_autoSize"> -->
<!-- 						<textarea name="descriptionCDR" id="descriptionCDR" readonly> -->
<%-- 							<c:out value="${doc.attributes.DESCRIPTIONCDR}" escapeXml="false" /> --%>
<!-- 						</textarea> -->
<!-- 					</div> -->
<!-- 				</td> -->
			</tr>
		</tbody>
	</table>
</div>
