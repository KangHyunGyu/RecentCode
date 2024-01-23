<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 산출물 조회')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
			</div>
		</div>

		<div class="pro_table">
			<table class="mainTable">
				<colgroup>
					<COL width="30%">
					<COL width="70%">
				</colgroup>
				<tr>
					<th>산출물 인증타입</th>
					<td>${output.docType} ${output.stepName}</td>
				</tr>
				<tr>
					<th>위치</th>
					<td>${output.location}
					</td>
				</tr>
				<tr>
					<th>이름</th>
					<td>${output.name}
					</td>
				</tr>
				<tr>
					<th>설명</th>
					<td class="pt5">
						<div class="textarea_autoSize">
							<textarea>${output.description}</textarea>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>