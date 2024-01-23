<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
function openViewChangeActivityPopup(actOid){
	var sURL = "/Windchill/worldex/change/viewChangeActivityPopup";
	var url = getURLString("/change/viewChangeActivityPopup") + "?oid="+actOid;
	openPopup(url, actOid, 900, 600);
}
</script>
<%-- ${data.size()} --%>
<table class="stbltc10 pdt10" style="width:100%;">
	<tr> 	       
		<td valign="top" class="pdt10 pdb10" >	
		  <table border="0" cellspacing="0" cellpadding="0" align="center">
			<tr> 
			  <c:if test="${data ne null}">
			  	<c:forEach var="list" items="${data}" varStatus="vs">
				<td width="176" valign="top" style="padding-left: 0px;text-align: center;">
				<table width="176" border="0" cellspacing="0" cellpadding="0" valign="top">
				  <tr> 
					<td style="padding-left: 0px"><img src="/Windchill/jsp/project/images/STEP${list.key }_COMPLETE.gif"></td>
				  </tr>
				    <tr>
					<td style="padding-left: 0px" height="170" valign="top" background="/Windchill/jsp/project/images/step_img01.gif">
					<table width="160" align="center"  class="pdt15"  border="0" cellspacing="0" cellpadding="0">
						<tr height="10"> 											  
							<td colspan="2" style="padding-left: 0px"></td>
						</tr>
						 <c:if test="${data.get(list.key).size() > 0}">
						 	<c:forEach var="item" items="${data.get(list.key)}" varStatus="loop">
						 		<tr> 											  
									<td colspan="1" style="padding-left: 15px;padding-right: 0px"width="30" height="25" class="center">
									<img src="${data.get(list.key).get(loop.index).stepIconUrl}"></td>											  
									<td colspan="3"style="padding-left: 0px">
									<a href="JavaScript:void(openViewChangeActivityPopup('${data.get(list.key).get(loop.index).oid}'))">
									${data.get(list.key).get(loop.index).name}</a></td>
								</tr>
						 	</c:forEach>
						 </c:if>
					 </table>
					 <tr>								
						<td style="padding-left: 0px"><img src="/Windchill/jsp/project/images/step_img02.gif"></td>
				  </tr>
				 </table>
				 </td>
				 <td width="30" style="padding-left: 0px">&nbsp;</td>
				</c:forEach>
			</c:if>
		</tr>
	</table>
</table>      
