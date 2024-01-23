<%@page import="com.e3ps.common.util.CommonUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	loadIncludePage();
	
});

function modifyPart() {
	var url = getURLString("/part/modifyPart") + "?oid=${part.oid}";
	
	location.href = url;
}

function modifyPartAttribute() {
	var url = getURLString("/part/modifyPartAttribute") + "?oid=${part.oid}";
	
	location.href = url;
}

function deletePart() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${part.oid}";
		
		var url = getURLString("/part/deletePartAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}

function revisePart() {
	
	openConfirm("${e3ps:getMessage('개정하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${part.oid}";
		
		var url = getURLString("/part/revisePartAction");
		ajaxCallServer(url, param, function(data){
			if(opener.window.search){
				opener.window.search();				
			}
		}, true);
	});
}

function loadIncludePage(tab) {
	
	if(tab == null) {
		tab = $(".tap>ul>li:first");
	}
	
	$(".tap ul li").removeClass("on");
	
	$(tab).addClass("on");
	
	var url = $(tab).data("url");
	var param = $(tab).data("param");
	
	if(param == null) {
		param = new Object();
	}
	
	param["oid"] = "${part.oid}";
	
	$("#includePage").load(url, param);
}

function sendERPPartInfo() {
	openConfirm("${e3ps:getMessage('ERP 전송하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = "${part.oid}";
		
		var url = getURLString("/part/sendErpAction");
		ajaxCallServer(url, param, function(data){
			
		}, true);
	});
}

function redirectPartView(){
	
}

</script>
<!-- pop -->
<div class="pop" style="overflow-x:hidden;">
	<!-- top -->
	<div class="top">
		<h2>${part.icon} ${e3ps:getMessage('부품')} - ${part.number}, ${part.name}, ${part.version}</h2>
		<c:if test="${!part.lastVersionBtn()}">
			<span style="padding-top:5px;padding-left:5px;">
				<button type="button" class="s_bt03" style="height:30px;" onclick="openView('${part.lastVersionOid()}')">${e3ps:getMessage('최신 버전 보기')}</button>
			</span>
		</c:if>
		<%-- <c:if test="${part.lastVersionBtn() && e3ps:isAdmin()}">
			<span style="padding-top:5px;padding-left:5px;">
				<button type="button" class="s_bt03" style="height:30px;" onclick="sendERPPartInfo()">${e3ps:getMessage('ERP 전송')}</button>
			</span>
		</c:if> --%>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->

	<!--tap -->
	<div class="tap pt20">
		<ul>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/part/include_detailPart')}">${e3ps:getMessage('세부 내용')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/part/include_relatedObject')}">${e3ps:getMessage('관련 객체')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_versionHistory')}">${e3ps:getMessage('버전 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/common/include_approveHistory')}">${e3ps:getMessage('결재 이력')}</li>
			<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/part/include_usedPart')}">${e3ps:getMessage('사용 부품')}</li>
			<%-- <li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/admin/include_masterAclList')}">${e3ps:getMessage('권한 목록')}</li> --%>
		</ul>
		<div class="tapbutton">
			 <%-- <%if(CommonUtil.isAdmin()){%>
				<a>
					<!-- <img src="/Windchill/jsp/project/images/img/bt_08.png" alt="ERP 전송" name="leftbtn_045" border="0" /> -->
					<button onclick="javascript:sendERPPartInfo();">ERP 전송</button>
				</a>
				<%}%> --%>
			<button type="button" class="s_bt03" onclick="openAddRevision('${part.oid}');">${e3ps:getMessage('리비전 추가')}</button>	
			<button type="button" class="s_bt03" onclick="openBomEditor('${part.oid}');">BOM 편집</button>
			<button type="button" class="s_bt03" onclick="openBomTree('${part.oid}');">BOM</button>
			<c:if test="${part.modifyAttributeBtn()}">
				<%-- <button type="button" class="s_bt03" onclick="javascript:modifyPartAttribute()">${e3ps:getMessage('속성 수정')}</button> --%>
			</c:if>
			<c:if test="${part.modifyBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:modifyPart()">${e3ps:getMessage('수정')}</button>
			</c:if>
			<c:if test="${part.deleteBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:deletePart()">${e3ps:getMessage('삭제')}</button>
			</c:if>
			<%-- <c:if test="${part.reviseBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:revisePart()">${e3ps:getMessage('개정')}</button>
			</c:if> --%>
			<%-- <c:if test="${part.recallBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:recallApproval('${part.oid}')">${e3ps:getMessage('회수')}</button>
			</c:if> --%>
			<%-- <c:if test="${part.withdrawnBtn()}">
				<button type="button" class="s_bt03" onclick="javascript:withdrawPart()">${e3ps:getMessage('폐기')}</button>
			</c:if> --%>
			<!-- 
			<img class="pointer verticalMiddle mb5" title="${e3ps:getMessage('일괄 등록 양식으로 BOM 다운로드')}" onclick="bomExcelDown('${part.oid}');" src="/Windchill/jsp/portal/icon/fileicon/xls.gif" border="0">
			 -->
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15" id="includePage">
	</div>
</div>		
<!-- //pop-->