<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
	  
  });

function contractEventHandle(element){
	
	const elementName = element.getAttribute('name');
	
	Array.prototype.slice.call(document.querySelectorAll('label')).forEach((item) => {
		let labelChildArray = Array.prototype.slice.call(item.childNodes)
		let textExist = labelChildArray.some((l)=>l.nodeName='INPUT' && l.type == 'text'  && l.name == elementName )
		let checkTextInput = true;
		labelChildArray.forEach((x)=> {
			if(x.nodeName=='INPUT' && (x.type == 'radio' || x.type =='checkbox') && x.name == elementName && x.checked){
				checkTextInput = false;
			}
		})
		
		if(textExist){
	         labelChildArray.forEach((x)=> {
	            if(x.nodeName=='INPUT' && x.type == 'text' && x.name == elementName){
	               x.disabled = checkTextInput;
	            }
	         })
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
			<col style="width: 10%" />
			<col style="width: 10%" />
			<col style="width: 30%" />
			<col style="width: 20%" />
			<col style="width: 30%" />
		</colgroup>
		<tbody>
			<tr>
				<th colspan="2">${e3ps:getMessage('고객명')}</th>
				<td><input type="text" class="w50" id="customerNameCDR" name="customerNameCDR">
<!-- 					<div class="pro_view"> -->
<!-- 						<select id="customerNameCDR" name="customerNameCDR" data-width="70%"></select>  -->
<!-- 						<span class="pointer verticalMiddle" onclick="javascript:deleteUser('customerNameCDR');"> -->
<!-- 							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" /> -->
<!-- 						</span> -->
<!-- 					</div> -->
				</td>
				<th >${e3ps:getMessage('상담자(고객)')}</th>
				<td>
					<input type="text" class="w50" id="counseCustomerCDR" name="counseCustomerCDR">
				</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('연락처')}</th>
				<td>
					<input type="text" class="w50" id="contactCDR" name="contactCDR">
				</td>
				<th>${e3ps:getMessage('접수일자')}</th>
				<td class="calender">
					<input type="text" class="datePicker w50" id="dateReceiptCDR" name="dateReceiptCDR" readonly>
				</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('상담 방법')}</th>
				<td>
					<input type="radio" id="visit" name="consultMethodCDR" value="visit" checked/> 
						<label for="visit">${e3ps:getMessage('방문')}</label> 
					<input type="radio" id="email" name="consultMethodCDR" value="email" /> 
						<label for="email">${e3ps:getMessage('전자메일')}</label>
					<input type="radio" id="fax" name="consultMethodCDR" value="fax" /> 
						<label for="fax">${e3ps:getMessage('FAX')}</label>
				</td>
				<th>${e3ps:getMessage('접수자')}</th>
				<td>
					<div class="pro_view">
						<select class="searchUser" id="receptionistCDR" name="receptionistCDR" data-width="70%"  data-valuetoname="true"></select> 
						<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('receptionistCDR', 'single');">
							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png" />
						</span> 
						<span class="pointer verticalMiddle" onclick="javascript:deleteUser('receptionistCDR');">
							<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" />
						</span>
					</div>
				</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('접수 자료')}</th>
				<td colspan="3">
					<label for="devRequest">${e3ps:getMessage('도면 [ ')}</label>
					<label class="mr5"><input type="checkbox" name="applicationMaterialCDR" value="CAD" checked>${e3ps:getMessage('CAD')}</label>
					<label class="mr5"><input type="checkbox" name="applicationMaterialCDR" value="PDF" >${e3ps:getMessage('pdf')}</label>
					<label class="mr5"><input type="checkbox" name="applicationMaterialCDR" value="JPG" >${e3ps:getMessage('JPG')}</label>
					<label class="mr5"><input type="checkbox" name="applicationMaterialCDR" value="" onclick="contractEventHandle(this)" >${e3ps:getMessage('기타')} ( <input type="text" name="applicationMaterialCDR" class="w20" disabled /> )</label> 
					<label>${e3ps:getMessage('] ')}</label>	
						
					<label class="ml10" for="reciptionSample">${e3ps:getMessage('제품 SAMPLE (')}</label>
					<label class="mr5"><input type="radio" name="applicationMaterialCDR" value="cov" >${e3ps:getMessage('회수')}</label>
					<label class="mr5"><input type="radio" name="applicationMaterialCDR" value="unCov" >${e3ps:getMessage('미회수')}</label>
					<label>${e3ps:getMessage(') ')}</label>	
				</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('접수 구분')}</th>
				<td colspan="3">
					<input type="radio" id="bidding" name="receptionClassCDR" value="Bidding" onclick="contractEventHandle(this)" checked /> 
						<label for="productSample">${e3ps:getMessage('Bidding : 입찰경쟁')}</label> 
					<input type="radio" id="soleBid" name="receptionClassCDR" value="exBidding" onclick="contractEventHandle(this)" /> 
						<label for="devProposal">${e3ps:getMessage('단독 입찰')}</label> 
					<label for="reciptionClassEtc">
						<input type="radio" id="reciptionClassEtc" name="receptionClassCDR" value="" onclick="contractEventHandle(this)"/>
						${e3ps:getMessage('기타')} ( <input type="text" name="receptionClassCDR" class="w10" disabled /> )
					</label>
			</tr>
			<tr>
				<th rowspan="3" style="text-align: center; border: 1px solid #cdcdcd; border-collapse: collapse;" >${e3ps:getMessage('제품 정보')}</th>
				<th>${e3ps:getMessage('품명')}</th>
				<td>
					<input type="text" class="w50" id="productNameCDR" name="productNameCDR">
				</td>
				<th>${e3ps:getMessage('규격')}</th>
				<td>
					<input type="text" class="w50" id="productStandardCDR" name="productStandardCDR">
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('수량')}</th>
				<td><input type="text" class="w50" id="quanCDR" name="quanCDR"></td>
				<th>${e3ps:getMessage('가격')}</th>
				<td><input type="text" class="w50" id="priceCDR" name="priceCDR"></td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('제출 기한')}</th>
				<td class="calendar">
					<input type="text" class="datePicker w50" id="submissionDeadlineCDR" name="submissionDeadlineCDR" readonly>
				</td>
				<th>${e3ps:getMessage('납기')}</th>
				<td class="calendar">
					<input type="text" class="datePicker w50" id="dueDateCDR" name="dueDateCDR" readonly>
				</td>
			</tr>
			<tr>
				<th rowspan="3" style="text-align: center;border: 1px solid #cdcdcd;border-collapse: collapse;" >${e3ps:getMessage('요구 사항')}</th>
				<th>${e3ps:getMessage('자사 도면화')}</th>
				<td class="calendar">
					<input type="text" class="datePicker w50" id="companyDrawCDR" name="companyDrawCDR" readonly />
				</td>
				<th>${e3ps:getMessage('공정 개발 검토서')}</th>
				<td class="calendar">
					<input type="text" class="datePicker w50" id="processDevRevCDR" name="processDevRevCDR" readonly /> 
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('SAMPLE 제출')}</th>
				<td class="calendar">
					<input type="text" class="datePicker w50" id="sampleSubCDR" name="sampleSubCDR" readonly /> 
				</td>
				<th>${e3ps:getMessage('Re 리포트')}</th>
				<td class="calendar">
					<input type="text" class="datePicker w50" id="reReportCDR" name="reReportCDR" readonly /> 
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('분석 의뢰')}</th>
				<td class="calendar">
					<input type="text" class="datePicker w50" id="reqAnalysisCDR" name="reqAnalysisCDR" readonly /> 
				</td>
				<th>${e3ps:getMessage('기타')}</th>
				<td>
					<input type="text" id="requirementEtcCDR" name="requirementEtcCDR" class="w50" /> 
				</td>
			</tr>
			<tr>
				<th colspan="2">${e3ps:getMessage('설명')}</th>
				<td class="pd15" colspan="3">
					<div class="textarea_autoSize">
						<textarea name="descriptionCDR" id="descriptionCDR"
							onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
