<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
	  dataInit_ProcessDev();
	  searchPartNamePDRModify("#productNamePDR");//제품명 찾기
	  searchDrawNumberPDRModify("#drawNumberPDR");//도번번호 찾기
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
  
function searchPartNamePDRModify(){
	  
	  var searchPartNamePDRModify = $("#productNamePDR");
	  
	  let width = searchPartNamePDRModify.data("width");
	  var partPDR = searchPartNamePDRModify.data("partPDR");
	  
		if(width == null) {
			width = "100%";
		}
		
		var url = getURLString("/part/searchPartListAction");
		
		searchPartNamePDRModify.select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					param["number"] = params.term;
					param = JSON.stringify(param);
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	var value = item.name;
// 			            	if(partPDR) value = item.oid;
			            	var name = item.name;
			              return { id: value, text: name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Object Info (Number or NAME)",
			minimumInputLength: 3,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
}

function searchDrawNumberPDRModify(){
	  
	  var searchDrawNumberPDRModify = $("#drawNumberPDR");
	  
	  let width = searchDrawNumberPDRModify.data("width");
	  var drawNumPDR = searchDrawNumberPDRModify.data("drawNumPDR");
	  
		if(width == null) {
			width = "100%";
		}
		
		var url = getURLString("/epm/searchEpmAction");
		
		searchDrawNumberPDRModify.select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					param["number"] = params.term;
					param["likeSearchNumber"] = "true";
					param = JSON.stringify(param);
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	var value = item.name;
// 			            	if(drawNumPDR) value = item.oid;
			            	var name = item.name;
			              return { id: value, text: name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Object Info (Number or NAME)",
			minimumInputLength: 3,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
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
				<td>
					<input type="text" class="w50" id="customerNamePDR" name="customerNamePDR" value="${doc.attributes.CUSTOMERNAMEPDR}" >
	            </td>
				<th>${e3ps:getMessage('제품명')}</th>
				<td>
					<div class="pro_view">
					<select id="productNamePDR" name="productNamePDR" data-width="70%" data-param="partPDR">
						<option value="<c:out value="${doc.attributes.PRODUCTNAMEPDR}"/>">
			       						<c:out value="${doc.attributes.PRODUCTNAMEPDR}"/>
	                </select>
	                </div>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('제품 외경')}</th>
				<td>
					<input type="text" class="w50" id="productOutDiameterPDR" name="productOutDiameterPDR" value="${doc.attributes.PRODUCTOUTDIAMETERPDR}">
				</td>
				<th>${e3ps:getMessage('제품 내경')}</th>
				<td>
					<input type="text" class="w50" id="productInDiameterPDR" name="productInDiameterPDR" value="${doc.attributes.PRODUCTINDIAMETERPDR}">
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('제품 두께')}</th>
				<td>
					<input type="text" class="w50" id="productThicknessPDR" name="productThicknessPDR" value="${doc.attributes.PRODUCTTHICKNESSPDR}">
				</td>
				<th>${e3ps:getMessage('도면 번호')}</th>
				<td>
<!-- 					<input type="text" class="w50" id="drawNumberPDR" name="drawNumberPDR"> -->
					<div class="pro_view">
					<select id="drawNumberPDR" name="drawNumberPDR" data-width="70%" data-param="drawNumPDR">
						<option value="<c:out value="${doc.attributes.DRAWNUMBERPDR}"/>">
			       						<c:out value="${doc.attributes.DRAWNUMBERPDR}"/>
	                </select>
	                </div>
                </td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('소재 가격')}</th>
				<td><input type="text" class="w50" id="pricePDR" name="pricePDR" value="${doc.attributes.PRICEPDR}" ></td>
				<th>${e3ps:getMessage('작업 구분')}</th>
				<td>
					<input type="radio" id="inProcess" name="divisionPDR" value="inProcess" />
					<label for="inProcess">${e3ps:getMessage('자사가공')}</label>
					<input type="radio" id="outProcess" name="divisionPDR" value="outProcess" />
					<label for="outProcess">${e3ps:getMessage('외주가공')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('가공임율')}</th>
				<td><input type="text" class="w50" id="inputRatePDR" name="inputRatePDR" value="${doc.attributes.INPUTRATEPDR}"></td>
				<th>${e3ps:getMessage('외주임율')}</th>
				<td><input type="text" class="w50" id="outsourceRatePDR" name="outsourceRatePDR" value="${doc.attributes.OUTSOURCERATEPDR}"></td>
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
					<input type="radio" id="changeMaterialUnitPriceYes" name="changeMaterialUnitPricePDR" value="changeMaterialUnitPriceYes" />
					<label for="changeMaterialUnitPriceYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="changeMaterialUnitPriceNo" name="changeMaterialUnitPricePDR" value="changeMaterialUnitPriceNo" />
					<label for="changeMaterialUnitPriceNo">${e3ps:getMessage('無')}</label>
				</td>
				<th>${e3ps:getMessage('시장 변동성')}</th>
				<td>
					<input type="radio" id="marketVolatilityYes" name="marketVolatilityPDR" value="marketVolatilityYes" />
					<label for="marketVolatilityYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="marketVolatilityNo" name="marketVolatilityPDR" value="marketVolatilityNo" />
					<label for="marketVolatilityNo">${e3ps:getMessage('無')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('경쟁사 가격 대응력')}</th>
				<td>
					<input type="radio" id="competPriceResponsYes" name="competPriceResponsPDR" value="competPriceResponsYes" />
					<label for="competPriceResponsYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="competPriceResponsNo" name="competPriceResponsPDR" value="competPriceResponsNo" />
					<label for="competPriceResponsNo">${e3ps:getMessage('無')}</label>
				</td>
				<th>${e3ps:getMessage('자사가격 대응력')}</th>
				<td>
					<input type="radio" id="inPriceResponsYes" name="inPriceResponsPDR" value="inPriceResponsYes" />
					<label for="inPriceResponsYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="inPriceResponsNo" name="inPriceResponsPDR" value="inPriceResponsNo" />
					<label for="inPriceResponsNo">${e3ps:getMessage('無')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('추후 Order 확산 가능성')}</th>
				<td colspan="3">
					<input type="radio" id="possibilityOrderInFutureYes" name="possibilityOrderInFuturePDR" value="possibilityOrderInFutureYes" checked/>
					<label for="possibilityOrderInFutureYes">${e3ps:getMessage('有')}</label>
					<input type="radio" id="possibilityOrderInFutureNo" name="possibilityOrderInFuturePDR" value="possibilityOrderInFutureNo" />
					<label for="possibilityOrderInFutureNo">${e3ps:getMessage('無')}</label>
				</td>
			</tr>
			<tr> 
				<th>${e3ps:getMessage('내용')}</th>
				<td colspan="3">
					<jsp:include page="${e3ps:getIncludeURLString('/doc/include_modifyPdrContent')}" flush="true">
						<jsp:param name="oid" value="${doc.oid}" />
					</jsp:include>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('최종 결정사항')}</th>
				<td class="pd15" colspan="3">
					<div class="textarea_autoSize">
						<textarea name="finalDecisionPDR" id="finalDecisionPDR"
							onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${doc.attributes.FINALDECISIONPDR}</textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
