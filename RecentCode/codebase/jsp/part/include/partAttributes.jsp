<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	if("${module}" == 'modify'){
		document.querySelectorAll('.attr').forEach((item)=>{
			let attrName = item.dataset.attrname;
			let attrValue = item.innerText?"value='" + item.innerText + "'":'';
			let addClass = attrName=='description'?"class='w70'":'';
			item.innerHTML = "<input type='text' name='"+attrName+"' id='"+attrName+"' "+attrValue+" "+addClass+"/>";
		})
	}
});
</script>
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('품목 속성')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width:15%">
			<col style="width:18%">
			<col style="width:15%">
			<col style="width:18%">
			<col style="width:15%">
			<col style="width:19%">
		</colgroup>
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('외경')}</th>
				<td class="attr" data-attrname="external_diameter">${attributes.EXTERNAL_DIAMETER}</td>
				<th scope="col">${e3ps:getMessage('내경')}</th>
				<td class="attr" data-attrname="internal_diameter">${attributes.INTERNAL_DIAMETER}</td>
				<th scope="col">${e3ps:getMessage('두께')}</th>
				<td class="attr" data-attrname="thickness">${attributes.THICKNESS}</td>
			</tr>
			
			<tr>
				<th scope="col">${e3ps:getMessage('홀 Dia')}</th>
				<td class="attr" data-attrname="hall_dia">${attributes.HALL_DIA}</td>
				<th scope="col">${e3ps:getMessage('홀 개수')}</th>
				<td class="attr" data-attrname="hall_count">${attributes.HALL_COUNT}</td>
				<th scope="col">${e3ps:getMessage('REMARK')}</th>
				<td class="attr" data-attrname="remark">${attributes.REMARK}</td>
			</tr>
			<tr>
				<th scope="col">${e3ps:getMessage('비고')}</th>
				<td class="attr" data-attrname="description" colspan="5">${attributes.DESCRIPTION}</td>
			</tr>
		</tbody>
	</table>	
</div>
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('도면 속성')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- pro_table -->
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col style="width:15%">
			<col style="width:18%">
			<col style="width:15%">
			<col style="width:18%">
			<col style="width:15%">
			<col style="width:19%">
		</colgroup>
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('MATERIAL')}</th>
				<td class="attr" data-attrname="material">${attributes.MATERIAL}</td>
				<th scope="col">${e3ps:getMessage('RESISTIVITY')}</th>
				<td class="attr" data-attrname="resistivity">${attributes.RESISTIVITY}</td>
				<th scope="col">${e3ps:getMessage('SURFACES')}</th>
				<td class="attr" data-attrname="surfaces">${attributes.SURFACES}</td>
			</tr>
			
			<tr>
				<th scope="col">${e3ps:getMessage('OEM_PART_NO')}</th>
				<td class="attr" data-attrname="oem_part_no">${attributes.OEM_PART_NO}</td>
				<th scope="col">${e3ps:getMessage('CUSTOMER')}</th>
				<td class="attr" data-attrname="customer">${attributes.CUSTOMER}</td>
				<th scope="col">${e3ps:getMessage('CUSTOMER_NO')}</th>
				<td class="attr" data-attrname="customer_no">${attributes.CUSTOMER_NO}</td>
			</tr>
			
			<tr>
				<th scope="col">${e3ps:getMessage('DATE')}</th>
				<td class="attr" data-attrname="date">${attributes.DATE}</td>
				<th scope="col">${e3ps:getMessage('DRAWN')}</th>
				<td class="attr" data-attrname="drawn">${attributes.DRAWN}</td>
				<th scope="col">${e3ps:getMessage('EQUIPMENT')}</th>
				<td class="attr" data-attrname="equipment">${attributes.EQUIPMENT}</td>
			</tr>
			
			<tr>
				<th scope="col">${e3ps:getMessage('CHECKED1')}</th>
				<td class="attr" data-attrname="checked1">${attributes.CHECKED1}</td>
				<th scope="col">${e3ps:getMessage('CHECKED2')}</th>
				<td class="attr" data-attrname="checked2">${attributes.CHECKED2}</td>
				<th scope="col">${e3ps:getMessage('APPROVED')}</th>
				<td class="attr" data-attrname="approved">${attributes.APPROVED}</td>
			</tr>
			
			<tr>
				<th scope="col">${e3ps:getMessage('OLD_PN')}</th>
				<td class="attr" data-attrname="old_pn">${attributes.OLD_PN}</td>
				<th scope="col">${e3ps:getMessage('TITLE')}</th>
				<td class="attr" data-attrname="title">${attributes.TITLE}</td>
				<th scope="col">${e3ps:getMessage('DRAW_NO')}</th>
				<td class="attr" data-attrname="draw_no">${attributes.DRAW_NO}</td>
			</tr>
		</tbody>
	</table>	
</div>
<!-- //pro_table -->