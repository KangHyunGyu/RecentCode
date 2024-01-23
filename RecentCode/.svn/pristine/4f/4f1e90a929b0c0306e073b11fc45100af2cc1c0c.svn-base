<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script>

$(document).ready(function(){
	
	
	
});

</script>
<!-- button -->
<div class="seach_arm pt10 pb5">
	<div class="leftbt">
		<h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png"></h4>
	</div>
	<div class="rightbt">
	</div>
</div>
<!-- //button -->
<!-- pro_table -->
<div class="pro_table mr30 ml30">
	<table class="mainTable" >
		<colgroup>
			<col style="width:10%">
			<col style="width:90%">
		</colgroup>	
		<tbody>
			<tr>
				<th scope="col">${e3ps:getMessage('품목/도면 개정')}<span class="required">*</span></th>
				<td >
				<jsp:include page="${e3ps:getIncludeURLString('/change/include_reviseECOPart')}" flush="true">
					<jsp:param name="oid" value="${ecoData.oid}"/>
					<jsp:param name="gridHeight" value="340"/>
				</jsp:include>	
				
				<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedECOEPMDoc')}" flush="true">
					<jsp:param name="oid" value="${ecoData.oid}"/>
					<jsp:param name="gridHeight" value="340"/>
				</jsp:include>
				<br>
				</td>
			</tr>
			
		</tbody>
	</table>
</div>
<!-- //pro_table -->
