<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
});

function modifyNotice() {
	var url = getURLString("/workspace/modifyNotice") + "?oid=${notice.oid}";
	
	location.href = url;
}

function deleteNotice() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${notice.oid}";
		
		var url = getURLString("/workspace/deleteNoticeAction");
		ajaxCallServer(url, param, function(data){
		});
	});
}

</script>
<div class="product">
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<h4>${e3ps:getMessage('기본 정보')}</h4>
		</div>
		<div class="rightbt">
			<c:if test="${e3ps:isAdmin()}">
				<button type="button" class="s_bt03" onclick="javascript:modifyNotice();">${e3ps:getMessage('수정')}</button>				<button type="button" class="s_bt03" onclick="javascript:deleteNotice();">${e3ps:getMessage('삭제')}</button>
				<button type="button" class="s_bt03" onclick="javascript:history.back();">${e3ps:getMessage('뒤로가기')}</button>
			</c:if>
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:15%">
				<col style="width:20%">
				<col style="width:15%">
				<col style="width:50%">
			</colgroup>	
			<tbody>
				<tr>
					<th>${e3ps:getMessage('제목')}</th>
					<td colspan="3">
						${notice.title}
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('등록자')}</th>
					<td>
						${notice.creatorFullName}
					</td>
					<th>${e3ps:getMessage('등록일자')}</th>
					<td>
						${notice.createDateFormat}
					</td>
				</tr>
				<tr>
					<th>${e3ps:getMessage('게시 기한일자')}</th>
					<td>
						${notice.deadline}
					</td>
					<th>${e3ps:getMessage('조회')}</th>
					<td>
						${notice.cnt}
					</td>
				</tr>
	            <tr>
					<th scope="col">${e3ps:getMessage('첨부파일')}</th>
					<td colspan="3" class="pd15">
						<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
							<jsp:param name="oid" value="${notice.oid}"/>
						 </jsp:include>
					</td>								
				</tr>	
				<tr>
					<th scope="col">${e3ps:getMessage('설명')}</th>
					<td colspan="3" class="pd25">
						<div class="editor" id="editor">
						${notice.contents}
						</div>
						<%-- <div class="textarea_autoSize">
							<textarea name="contents" id="contents" readonly><c:out value="${notice.contents}" escapeXml="false" /></textarea>
						</div> --%>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>