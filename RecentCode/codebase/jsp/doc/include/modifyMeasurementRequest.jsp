<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function () {
	dataInit_MeasurementRe();
});

function dataInit_MeasurementRe(){
	
	let docMaterialMRList = '${doc.attributes.DOCMATERIALMR}'.split(', ');
	let productStatusMRList = "${doc.attributes.PRODUCTSTATUSMR}";
	let illuminanceMeasMRList = "${doc.attributes.ILLUMINANCEMEASMR}";
	let productSurfaceConMRTList = '${doc.attributes.PRODUCTSURFACECONMRT}'.split(', ');
	let productSurfaceConMRBList = '${doc.attributes.PRODUCTSURFACECONMRB}'.split(', ');
	let productSurfaceConMREList = '${doc.attributes.PRODUCTSURFACECONMRE}'.split(', ');
	let importanceMRList = "${doc.attributes.IMPORTANCEMR}";
	let meaWeightMRList = "${doc.attributes.MEAWEIGHTMR}";
	let meaResistanceMRList = "${doc.attributes.MEARESISTANCEMR}";
	let meaCornerMRList = "${doc.attributes.MEACORNERMR}";
	let meaThicknessMRList = "${doc.attributes.MEATHICKNESSMR}";
	
	console.log('docMaterialMRList :' ,docMaterialMRList);
	
	let exist = false;
	
	docMaterialMRList.forEach((val)=> {
		let exist = false;
		document.getElementsByName('docMaterialMR').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			} 
		})
		if(!exist){
			document.getElementsByName('docMaterialMR').forEach((tag) => {
				if(tag.value == ""){
					tag.checked = true;
					$('input[id=docMaterialMR]').attr('value',val);
				}
			})
		}
	})
	document.getElementsByName('productStatusMR').forEach((tag) => {
		if(tag.value != ""){
			if(productStatusMRList == tag.value){
				  tag.checked = true;
				  exist = true;
			  }
		}else {
			if(!exist){
				document.getElementsByName('productStatusMR').forEach((tag) => {
					if(tag.value == ""){
						tag.checked = true;
						$('input[id=productStatusMR]').attr('value',productStatusMRList);
					}
				})
			}
		}
	  })
	  document.getElementsByName('illuminanceMeasMR').forEach((tag) => {
		  let exist = false;
		  if(tag.value == illuminanceMeasMRList){
			  tag.checked = true;
			  exist = true;
		  }else {
			  if(!exist){
					document.getElementsByName('illuminanceMeasMR').forEach((tag) => {
						if(tag.value == "" && tag.value !="무"){
							tag.checked = true;
							if(illuminanceMeasMRList !="무"){
								$('input[id=illuminanceMeasMR]').attr('value',illuminanceMeasMRList);
							}
						}
					})
				}
		  }
	  })
	  productSurfaceConMRTList.forEach((val)=> { 
		let exist = false;
		document.getElementsByName('productSurfaceConMRT').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			} 
		})
		if(!exist){
			document.getElementsByName('productSurfaceConMRT').forEach((tag) => {
				if(tag.value == ""){
					tag.checked = true;
					$('input[id=productSurfaceConMRT]').attr('value',val);
				}
			})
		}
	})
	productSurfaceConMRBList.forEach((val)=> { 
		let exist = false;
		document.getElementsByName('productSurfaceConMRB').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			} 
		})
		if(!exist){
			document.getElementsByName('productSurfaceConMRB').forEach((tag) => {
				if(tag.value == ""){
					tag.checked = true;
					$('input[id=productSurfaceConMRB]').attr('value',val);
				}
			})
		}
	})
	productSurfaceConMREList.forEach((val)=> { 
		let exist = false;
		document.getElementsByName('productSurfaceConMRE').forEach((tag) => {
			if(val == tag.value){
				tag.checked = true;
				exist = true;
			} 
		})
		if(!exist){
			document.getElementsByName('productSurfaceConMRE').forEach((tag) => {
				if(tag.value == ""){
					tag.checked = true;
					$('input[id=productSurfaceConMRE]').attr('value',val);
				}
			})
		}
	})
	document.getElementsByName('importanceMR').forEach((tag) => {
		let exist = false;
		  if(tag.value == importanceMRList){
			  tag.checked = true;
			  exist = true;
		  }else {
			  if(!exist){
					document.getElementsByName('importanceMR').forEach((tag) => {
						if(tag.value == ""){
							tag.checked = true;
							if(meaResistanceMRList !="무"){
								$('input[id=importanceMR]').attr('value',importanceMRList);
							}
						}
					})
				}
		  }
	  })
	  document.getElementsByName('meaWeightMR').forEach((tag) => {
		if(meaWeightMRList == tag.value){
			tag.checked = true;
		}
	})
	document.getElementsByName('meaResistanceMR').forEach((tag) => {
		let exist = false;
		  if(tag.value == meaResistanceMRList){
			  tag.checked = true;
			  exist = true;
		  }else {
			  if(!exist){
					document.getElementsByName('meaResistanceMR').forEach((tag) => {
						if(tag.value == ""){
							tag.checked = true;
							if(meaResistanceMRList !="무"){
								$('input[id=meaResistanceMR]').attr('value',meaResistanceMRList);
							}
						}
					})
				}
		  }
	  })
	  document.getElementsByName('meaCornerMR').forEach((tag) => {
		if(meaCornerMRList == tag.value){
			tag.checked = true;
		}
	})
	document.getElementsByName('meaThicknessMR').forEach((tag) => {
		if(meaThicknessMRList == tag.value){
			tag.checked = true;
		}
	})
	
}

function measurementEventHandle(element){
	const elementName = element.getAttribute('name');
	Array.prototype.slice.call(document.querySelectorAll('label')).forEach((item) => {
		let labelChildArray = Array.prototype.slice.call(item.childNodes)
		let textExist = labelChildArray.some((l)=>l.nodeName='INPUT' && l.type == 'text'  && l.name == elementName )
		let checkTextInput = true;
		
		labelChildArray.forEach((x)=> {
			if(x.nodeName=='INPUT' && (x.type == 'radio' || x.type == 'checkbox') && x.name == elementName && x.checked){
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
			<col style="width: 11%" />
			<col style="width: 22%" />
			<col style="width: 11%" />
			<col style="width: 22%" />
			<col style="width: 11%" />
			<col style="width: 23%" />
		</colgroup>
		<tbody>
		<tr>
				<th>${e3ps:getMessage('의뢰일')}</th>
				<td colspan="2" class="calendar">
					<input type="text" class="datePicker w50" id="dateReceiptMR" name="dateReceiptMR" value="${doc.attributes.DATERECEIPTMR}" readonly>
				</td>
				<th>${e3ps:getMessage('완료 희망일')}</th>
				<td colspan="2" class="calendar">
					<input type="text" class="datePicker w50" id="compDateMR" name="compDateMR" value="${doc.attributes.COMPDATEMR}" readonly>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('측정수량')}</th>
				<td colspan="2">
					<input type="text" class="w50" id="quanMR" name="quanMR" value="${doc.attributes.QUANMR}" >
				</td>
				<th>${e3ps:getMessage('제품명')}</th>
				<td colspan="2">
					<input type="text" class="w50" id="productNameMR" name="productNameMR" value="${doc.attributes.PRODUCTNAMEMR}" >
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('OEM No')}</th>
				<td colspan="2">
					<input type="text" class="w50" id="partNumberMR" name="partNumberMR" value="${doc.attributes.PARTNUMBERMR}" >
				</td>
				<th>${e3ps:getMessage('업체명')}</th>
				<td colspan="2"><input type="text" class="w50" id="customerNameMR" name="customerNameMR" value="${doc.attributes.CUSTOMERNAMEMR}" >
<!-- 	              <div class="pro_view"> -->
<!-- 	                <select id="customerNameMR" name="customerNameMR" data-width="70%" ></select> -->
<!-- 	                <span class="pointer verticalMiddle" onclick="javascript:deleteUser('customerNameMR');" > -->
<!-- 	                	<img class="verticalMiddle" src="/Windchill/jsp/portal/images/delete_icon.png" /> -->
<!-- 	                </span> -->
<!-- 	              </div> -->
	            </td>
			</tr>
			<tr>
				<th colspan="6" style="gorder-top: 2px solid #dedede; border-bottom:1px solid #eff3f6;" >
			</tr>
			<tr>
				<th colspan="6" style="border-top: 10px solid #eff3f6; border-bottom: 2px solid #74AF2A;" >
				</th>
			</tr>
			<tr>
				<th colspan="6" style="text-align:center">${e3ps:getMessage('의뢰 내용')}</th>
			</tr>
			<tr>
				<th>${e3ps:getMessage('소재 재질')}</th>
				<td colspan="5">
					<label >${e3ps:getMessage('Silicon [')}</label>
					<label for="sSingle"><input type="checkbox" id="sSingle" name="docMaterialMR" value="Single" onclick="measurementEventHandle(this)" />${e3ps:getMessage('Single')}</label>
					<label for="sPoly"><input type="checkbox" id="sPoly" name="docMaterialMR" value="Poly" onclick="measurementEventHandle(this)" />${e3ps:getMessage('Poly')}</label>
					<label >${e3ps:getMessage(']')}</label>
					<label for="quatz"><input type="checkbox" id="quatz" name="docMaterialMR" value="Quartz" onclick="measurementEventHandle(this)" />${e3ps:getMessage('Quartz')}</label>
					<label for="ai2o3"><input type="checkbox" id="ai2o3" name="docMaterialMR" value="AI2O3" onclick="measurementEventHandle(this)" />${e3ps:getMessage('AI2O3')}</label>
					<label for="sapphire"><input type="checkbox" id="sapphire" name="docMaterialMR" value="Sapphire" onclick="measurementEventHandle(this)" />${e3ps:getMessage('Sapphire')}</label>
					<br>
					<label >${e3ps:getMessage('SiC [')}</label>
					<label for="sSintered"><input type="checkbox" id="sSintered" name="docMaterialMR" value="Sintered" onclick="measurementEventHandle(this)" />${e3ps:getMessage('Sintered')}</label>
					<label for="sRB"><input type="checkbox" id="sRB" name="docMaterialMR" value="RB" onclick="measurementEventHandle(this)" />${e3ps:getMessage('RB')}</label>
					<label for="sCVD"><input type="checkbox" id="sCVD" name="docMaterialMR" value="CVD" onclick="measurementEventHandle(this)" />${e3ps:getMessage('CVD')}</label>
					<label >${e3ps:getMessage(']')}</label>
					<br>
					<label >${e3ps:getMessage('AIN [')}</label>
					<label for="asam"><input type="checkbox" id="asam" name="docMaterialMR" value="삼압" onclick="measurementEventHandle(this)" />${e3ps:getMessage('삼압')}</label>
					<label for="agab"><input type="checkbox" id="agab" name="docMaterialMR" value="가압" onclick="measurementEventHandle(this)" />${e3ps:getMessage('가압')}</label>
					<label >${e3ps:getMessage(']')}</label>
					<br>
					<label>
						<input type="checkbox" id="docMaterialMRetc" name="docMaterialMR" value="" onclick="measurementEventHandle(this)"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="docMaterialMR" name="docMaterialMR" class="w20" /> )
					</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('제품 상태')}</th>
				<td colspan="2">
					
					<label for="stain"><input type="radio" id="stain" name="productStatusMR" value="얼룩" onclick="measurementEventHandle(this)" />${e3ps:getMessage('얼룩')}</label>
					<label for="scratch"><input type="radio" id="scratch" name="productStatusMR" value="스크래치" onclick="measurementEventHandle(this)"/>${e3ps:getMessage('스크래치')}</label>
					<label for="break"><input type="radio" id="break" name="productStatusMR" value="파손" onclick="measurementEventHandle(this)"/>${e3ps:getMessage('파손')}</label>
					<label for="psEtc">
						<input type="radio" name="productStatusMR" value="" onclick="measurementEventHandle(this)"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="productStatusMR" name="productStatusMR" class="w20" /> )
					</label>
				</td>
				<th>${e3ps:getMessage('조도측정(2회씩)')}</th>
				<td colspan="2">
					<label for="illuminYes">
						<input type="radio" name="illuminanceMeasMR" value="" onclick="measurementEventHandle(this)" />
						${e3ps:getMessage('유')} ( <input type="text" id="illuminanceMeasMR" name="illuminanceMeasMR" class="w20" /> )
					</label>
					<input type="radio" id="illuminNo" name="illuminanceMeasMR" value="무" onclick="measurementEventHandle(this)"/>
					<label for="illuminNo">${e3ps:getMessage('무')}</label>
				</td>
			</tr>
			<tr>
				<th colspan="6" style="gorder-top: 2px solid #dedede; border-bottom:1px solid #eff3f6;" >
			</tr>
			<tr>
				<th colspan="6" style="border-top: 10px solid #eff3f6; border-bottom: 2px solid #74AF2A;" >
				</th>
			</tr>
			<tr> 
					<th colspan="6" style="text-align:center">${e3ps:getMessage('제품 표면 상태')}</th> 
			</tr> 
			<tr> 
				<th>${e3ps:getMessage('상부 표면 상태')}</th> 
				<td>
					<label for="grinding"><input type="checkbox" id="stain" name="productSurfaceConMRT" value="Grinding" />${e3ps:getMessage('Grinding')}</label>
					<label for="lapping"><input type="checkbox" id="scratch" name="productSurfaceConMRT" value="Lapping" />${e3ps:getMessage('Lapping')}</label>
					<label for="sanding"><input type="checkbox" id="break" name="productSurfaceConMRT" value="Sanding" />${e3ps:getMessage('Sanding')}</label>
					<label for="polising"><input type="checkbox" id="polising" name="productSurfaceConMRT" value="Polising" />${e3ps:getMessage('Polising')}</label>
					<label for="cpae"><input type="checkbox" id="cpae" name="productSurfaceConMRT" value="CPAE" />${e3ps:getMessage('CPAE')}</label>
					<label for="psCEtc">
						<input type="checkbox" id="productSurfaceConMRT" name="productSurfaceConMRT" value="" onclick="measurementEventHandle(this)"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="productSurfaceConMRT" name="productSurfaceConMRT" class="w20" /> )
					</label>
				</td>
				<th>${e3ps:getMessage('하부 표면 상태')}</th>
				<td>
					<label for="grinding"><input type="checkbox" id="stain" name="productSurfaceConMRB" value="Grinding" />${e3ps:getMessage('Grinding')}</label>
					<label for="lapping"><input type="checkbox" id="scratch" name="productSurfaceConMRB" value="Lapping" />${e3ps:getMessage('Lapping')}</label>
					<label for="sanding"><input type="checkbox" id="break" name="productSurfaceConMRB" value="Sanding" />${e3ps:getMessage('Sanding')}</label>
					<label for="polising"><input type="checkbox" id="polising" name="productSurfaceConMRB" value="Polising" />${e3ps:getMessage('Polising')}</label>
					<label for="cpae"><input type="checkbox" id="cpae" name="productSurfaceConMRB" value="CPAE" />${e3ps:getMessage('CPAE')}</label>
					<label for="psCEtc">
						<input type="checkbox" name="productSurfaceConMRB" value="" onclick="measurementEventHandle(this)"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="productSurfaceConMRB" name="productSurfaceConMRB" class="w20" /> )
					</label>
				</td>
				<th>${e3ps:getMessage('기타부 표면 상태')}</th>
				<td>
					<label for="grinding"><input type="checkbox" id="stain" name="productSurfaceConMRE" value="Grinding" />${e3ps:getMessage('Grinding')}</label>
					<label for="lapping"><input type="checkbox" id="scratch" name="productSurfaceConMRE" value="Lapping" />${e3ps:getMessage('Lapping')}</label>
					<label for="sanding"><input type="checkbox" id="break" name="productSurfaceConMRE" value="Sanding" />${e3ps:getMessage('Sanding')}</label>
					<label for="polising"><input type="checkbox" id="polising" name="productSurfaceConMRE" value="Polising" />${e3ps:getMessage('Polising')}</label>
					<label for="cpae"><input type="checkbox" id="cpae" name="productSurfaceConMRE" value="CPAE" />${e3ps:getMessage('CPAE')}</label>
					<label for="psCEtc">
						<input type="checkbox" name="productSurfaceConMRE" value="" onclick="measurementEventHandle(this)"/>
						${e3ps:getMessage('기타')} ( <input type="text" id="productSurfaceConMRE" name="productSurfaceConMRE" class="w20" /> )
					</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('비중')}</th>
				<td>
					<label for="importYes">
						<input type="radio" name="importanceMR" value="" onclick="measurementEventHandle(this)" />
						${e3ps:getMessage('유')} ( <input type="text" id="importanceMR" name="importanceMR" class="w20" /> )
					</label>
					<input type="radio" id="importNo" name="importanceMR" value="무" onclick="measurementEventHandle(this)"/>
					<label for="importNo">${e3ps:getMessage('무')}</label>
				</td>
				<th>${e3ps:getMessage('무게')}</th>
				<td>
					<input type="radio" id="weightYes" name="meaWeightMR" value="유" />
					<label for="weightYes">${e3ps:getMessage('유')}</label>
					<input type="radio" id="weightNo" name="meaWeightMR" value="무" />
					<label for="weightNo">${e3ps:getMessage('무')}</label>
				</td>
				<th>${e3ps:getMessage('저항')}</th>
				<td>
					<label for="resistYes">
						<input type="radio" name="meaResistanceMR" value="" onclick="measurementEventHandle(this)" />
						${e3ps:getMessage('유')} ( <input type="text" id="meaResistanceMR" name="meaResistanceMR" class="w20" /> )
					</label>
					<input type="radio" id="resistNo" name="meaResistanceMR" value="무" onclick="measurementEventHandle(this)"/>
					<label for="resistNo">${e3ps:getMessage('무')}</label>
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('모서리 측정(R/C)')}</th> 
				<td>
					<input type="radio" id="cornerYes" name="meaCornerMR" value="유" />
					<label for="cornerYes">${e3ps:getMessage('유')}</label>
					<input type="radio" id="cornerNo" name="meaCornerMR" value="무" />
					<label for="cornerNo">${e3ps:getMessage('무')}</label>
				</td>
				<th>${e3ps:getMessage('두께 측정(16POINT, PCD당8POINT)')}</th>
				<td>
					<input type="radio" id="thickYes" name="meaThicknessMR" value="유" />
					<label for="thickYes">${e3ps:getMessage('유')}</label>
					<input type="radio" id="thickNo" name="meaThicknessMR" value="무" />
					<label for="thickNo">${e3ps:getMessage('무')}</label>
				</td>
				<th>${e3ps:getMessage('마킹')}</th>
				<td>
					<input type="text" class="w50" id="markNumberMR" name="markNumberMR" value="${doc.attributes.MARKNUMBERMR} ">
				</td>
			</tr>
			<tr>
				<th>${e3ps:getMessage('설명')}</th>
				<td class="pd15" colspan="5">
					<div class="textarea_autoSize">
						<textarea name="descriptionMR" id="descriptionMR"
							onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${doc.attributes.DESCRIPTIONMR}</textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
