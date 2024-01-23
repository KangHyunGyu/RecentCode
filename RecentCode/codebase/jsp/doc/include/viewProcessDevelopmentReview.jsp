<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
	  dataInit_ProcessDev();
  });
  
  function dataInit_ProcessDev(){
	  
	  let divisionPDRList = "${doc.attributes.DIVISIONPDR}";
	  let changeMaterialUnitPricePDRList = "${doc.attributes.CHANGEMATERIALUNITPRICEPDR}";
	  let marketVolatilityPDRList = "${doc.attributes.MARKETVOLATILITYPDR}";
	  let competPriceResponsPDRList = "${doc.attributes.COMPETPRICERESPONSPDR}";
	  let inPriceResponsPDRList = "${doc.attributes.INPRICERESPONSPDR}";
	  let possibilityOrderInFuturePDRList = "${doc.attributes.POSSIBILITYORDERINFUTUREPDR}";
	  
	  let exist = false;
	  
	  document.getElementsByName('divisionPDR').forEach((tag) => {
		  if(divisionPDRList == tag.value){
			  tag.checked = true;
			  exist = true;
		  }
	  })
	  document.getElementsByName('changeMaterialUnitPricePDR').forEach((tag) => {
		  if(changeMaterialUnitPricePDRList == tag.value){
			  tag.checked = true;
			  exist = true;
		  }
	  }) 
	  document.getElementsByName('marketVolatilityPDR').forEach((tag) => {
		  if(marketVolatilityPDRList == tag.value){
			  tag.checked = true;
			  exist = true;
		  }
	  }) 
	  document.getElementsByName('competPriceResponsPDR').forEach((tag) => {
		  if(competPriceResponsPDRList == tag.value){
			  tag.checked = true;
			  exist = true;
		  }
	  }) 
	  document.getElementsByName('inPriceResponsPDR').forEach((tag) => {
		  if(inPriceResponsPDRList == tag.value){
			  tag.checked = true;
			  exist = true;
		  }
	  })
	  document.getElementsByName('possibilityOrderInFuturePDR').forEach((tag) => {
		  if(possibilityOrderInFuturePDRList == tag.value){
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
				<th>${e3ps:getMessage('거래처명')}</th>
				<td>${doc.attributes.CUSTOMERNAMEPDR}</td>
				<th>${e3ps:getMessage('제품명')}</th>
				<td>${doc.attributes.PRODUCTNAMEPDR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('제품 외경')}</th>
				<td>${doc.attributes.PRODUCTOUTDIAMETERPDR}</td>
				<th>${e3ps:getMessage('제품 내경')}</th>
				<td>${doc.attributes.PRODUCTINDIAMETERPDR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('제품 두께')}</th>
				<td>${doc.attributes.PRODUCTTHICKNESSPDR}</td>
				<th>${e3ps:getMessage('도면 번호')}</th>
				<td>${doc.attributes.DRAWNUMBERPDR}</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('소재 가격')}</th>
				<td>${doc.attributes.PRICEPDR}</td>
				<th>${e3ps:getMessage('작업 구분')}</th>
				<td>
					<input type="radio" id="inProcess" name="divisionPDR" value="inProcess" onclick="return false;" />
					<label for="inProcess">${e3ps:getMessage('자사가공')}</label>
					<input type="radio" id="outProcess" name="divisionPDR" value="outProcess" onclick="return false;" />
					<label for="outProcess">${e3ps:getMessage('외주가공')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('가공임율')}</th>
				<td>${doc.attributes.INPUTRATEPDR}</td>
				<th>${e3ps:getMessage('외주임율')}</th>
				<td>${doc.attributes.OUTSOURCERATEPDR}</td>
			</tr>
			<tr>
				<th colspan="4" style="gorder-top: 2px solid #dedede; border-bottom:1px solid #eff3f6;" >
			</tr>
			<tr>
				<th colspan="4" style="border-top: 10px solid #eff3f6; border-bottom: 2px solid #74AF2A;" >
				</th>
			</tr>
			<tr>
				<th colspan="4" style="text-align:center">${e3ps:getMessage('검토항목')}</th>
			</tr>
			<tr>
				<th>${e3ps:getMessage('소재단가 변동사항')}</th>
				<td>
					<input type="radio" id="changeMaterialUnitPriceYes" name="changeMaterialUnitPricePDR" value="changeMaterialUnitPriceYes" onclick="return false;" />
					<label for="changeMaterialUnitPriceYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="changeMaterialUnitPriceNo" name="changeMaterialUnitPricePDR" value="changeMaterialUnitPriceNo" onclick="return false;" />
					<label for="changeMaterialUnitPriceNo">${e3ps:getMessage('無')}</label>
				</td>
				<th>${e3ps:getMessage('시장 변동성')}</th>
				<td>
					<input type="radio" id="marketVolatilityYes" name="marketVolatilityPDR" value="marketVolatilityYes" onclick="return false;" />
					<label for="marketVolatilityYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="marketVolatilityNo" name="marketVolatilityPDR" value="marketVolatilityNo" onclick="return false;" />
					<label for="marketVolatilityNo">${e3ps:getMessage('無')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('경쟁사 가격 대응력')}</th>
				<td>
					<input type="radio" id="competPriceResponsYes" name="competPriceResponsPDR" value="competPriceResponsYes" onclick="return false;" />
					<label for="competPriceResponsYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="competPriceResponsNo" name="competPriceResponsPDR" value="competPriceResponsNo" onclick="return false;" />
					<label for="competPriceResponsNo">${e3ps:getMessage('無')}</label>
				</td>
				<th>${e3ps:getMessage('자사가격 대응력')}</th>
				<td>
					<input type="radio" id="inPriceResponsYes" name="inPriceResponsPDR" value="inPriceResponsYes" onclick="return false;" />
					<label for="inPriceResponsYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="inPriceResponsNo" name="inPriceResponsPDR" value="inPriceResponsNo" onclick="return false;" />
					<label for="inPriceResponsNo">${e3ps:getMessage('無')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('추후 Order 확산 가능성')}</th>
				<td colspan="3">
					<input type="radio" id="possibilityOrderInFutureYes" name="possibilityOrderInFuturePDR" value="possibilityOrderInFutureYes" onclick="return false;" />
					<label for="possibilityOrderInFutureYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="possibilityOrderInFutureNo" name="possibilityOrderInFuturePDR" value="possibilityOrderInFutureNo" onclick="return false;" />
					<label for="possibilityOrderInFutureNo">${e3ps:getMessage('無')}</label>
				</td>
			</tr>
			<tr> 
				<th>${e3ps:getMessage('내용')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/doc/include_viewPdrContent')}" flush="true">
						<jsp:param name="oid" value="${doc.oid}" />
					</jsp:include>
				</td>
		   </tr>
			<tr>
				<th>${e3ps:getMessage('최종 결정사항')}</th>
				<td class="pd15" colspan="3">${doc.attributes.FINALDECISIONPDR}</td>
			</tr>
		</tbody>
	</table>
</div>
