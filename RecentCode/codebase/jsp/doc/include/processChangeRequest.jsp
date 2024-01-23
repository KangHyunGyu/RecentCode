<%@page import="com.e3ps.doc.bean.DocData"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="e3ps" uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
  $(document).ready(function () {
    
  });
	
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
				<th colspan="4" style="text-align:left">${e3ps:getMessage('1.공정 변경 & 목표값 변경')}</th>
			</tr>
			<tr>
				<th colspan="2" style="text-align:center">${e3ps:getMessage('변경 전')}</th>
				<th colspan="2" style="text-align:center">${e3ps:getMessage('변경 후')}</th>
			</tr>
			<tr>
				<td class="pd15" colspan="2">
					<div class="textarea_autoSize">
						<textarea name="beforeChangePCR" id="beforeChangePCR" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
				<td class="pd15" colspan="2">
					<div class="textarea_autoSize">
						<textarea name="afterChangePCR" id="afterChangePCR" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<th colspan="4" style="text-align:left">${e3ps:getMessage('2.변경 사유 & 요청 사항')}</th>
			</tr>
			<tr>
				<td class="pd15" colspan="4">
					<div class="textarea_autoSize">
						<textarea name="changeReasonPCR" id="changeReasonPCR" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<th colspan="4" style="text-align:left">${e3ps:getMessage('3.생산파트 검토내역')}</th>
			</tr>
			<tr>
				<td class="pd15" colspan="4">
					<div class="textarea_autoSize">
						<textarea name="productPartReviewPCR" id="productPartReviewPCR" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<th colspan="4" style="text-align:left">${e3ps:getMessage('4.품질파트 검토내역')}</th>
			</tr>
			<tr>
				<td class="pd15" colspan="4">
					<div class="textarea_autoSize">
						<textarea name="qualityPartReviewPCR" id="qualityPartReviewPCR" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<th colspan="4" style="text-align:left">${e3ps:getMessage('5.기타 관련부서 협의 내역')}</th>
			</tr>
			<tr>
				<td class="pd15" colspan="4">
					<div class="textarea_autoSize">
						<textarea name="relatedDepReviewPCR" id="relatedDepReviewPCR" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
