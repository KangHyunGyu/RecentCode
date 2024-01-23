<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>

function checkFunction(){
}

</script>
<div class="product"> 
<div class="pro_table mr30 ml30">
	<div class="seach_arm2 pt5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
			활동 상세정보</span>
		</div>
		<div class="rightbt">
		</div> 
	</div>
	<table class="mainTable">
				<colgroup>
					<col width="100">
					<col width="*">
				</colgroup>
				<tr> 
					<th>
						임무설명
					</th>
					<td align="center">
					<c:if test="${data ne null}">
					<b>아래 설계변경 활동 중 '[${data.step}] ${data.name }'에 해당하는 산출물을 등록 해 주십시오.</b>
						<c:if test="${data.description ne null}">
							<br>${data.description }
						</c:if>
					</c:if>
					</td>
				</tr>
				<tr>
					<th>
						임무
					</th>
					<td>
						<c:if test="${data.activeType eq 'PART'}">
						<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedPart')}" flush="true">
							<jsp:param name="oid" value="${oid}"/>
							<jsp:param name="isEdit" value="true"/>
						</jsp:include>
						</c:if>
						<c:if test="${data.activeType eq 'DRAWING'}">
						<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedEpm')}" flush="true">
							<jsp:param name="oid" value="${oid}"/>
							<jsp:param name="isEdit" value="true"/>
						</jsp:include>
						</c:if>
						<c:if test="${data.activeType eq 'DOCUMENT'}">
						<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedDoc')}" flush="true">
							<jsp:param name="oid" value="${oid}"/>
							<jsp:param name="isEdit" value="true"/>
						</jsp:include>
						</c:if>
						<c:if test="${data.activeType eq 'COMMON'}">
						<jsp:include page="${e3ps:getIncludeURLString('/change/include_relatedCommon')}" flush="true">
							<jsp:param name="oid" value="${oid}"/>
							<jsp:param name="isEdit" value="true"/>
						</jsp:include>
						</c:if>
					</td>
				</tr>
			</table>
			<c:if test="${data.orderName eq 'EChangeOrder2'}">
				<div class="seach_arm2 pt5">
				<div class="leftbt">
					<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
					ECO 상세정보</span>
				</div>
				<div class="rightbt">
				</div> 
				</div>
				<jsp:include page="${e3ps:getIncludeURLString('/change/viewECO_include')}" flush="true">
					<jsp:param name="oid" value="${data.order }"/>
				</jsp:include>
				</c:if>
			<c:if test="${data.orderName eq 'EChangeRequest2'}">
				<div class="seach_arm2 pt5">
				<div class="leftbt">
					<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
					ECR 상세정보</span>
				</div>
				<div class="rightbt">
				</div> 
				</div>
				<jsp:include page="${e3ps:getIncludeURLString('/change/viewECR_include')}" flush="true">
					<jsp:param name="oid" value="${data.order }"/>
				</jsp:include>
			</c:if>
</div>
</div>
