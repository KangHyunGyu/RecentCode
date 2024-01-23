<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
});
function addPart(){
	var checkItemList = AUIGrid.getCheckedRowItems(rel_part_myGridID);
	
	var addItemList = new Array();
	
	for(var i = 0; i < checkItemList.length; i++){
		addItemList.push(checkItemList[i].item);
	}
	
	if(opener.window.add_addPartList){	//addObject에서 그리드 리스트 세팅
		opener.window.add_addPartList(addItemList);
	}
	
	window.close();
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>
		<img border="0" src="/Windchill/netmarkets/images/doc_document.gif">
		${e3ps:getMessage('관련 부품 검색')}
		</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
	<!-- //top -->
	<!--tap -->
	<div class="tap pt20 mt20">
		<div class="tapbutton">
			<button type="button" class="s_bt03" onclick="javascript:addPart()">${e3ps:getMessage('추가')}</button>
		</div>
	</div>
	<!--//tap -->

	<div class="con pl25 pr25 pb15">
		<!-- 부품 -->
		<jsp:include page="${e3ps:getIncludeURLString('/part/include_relatedPart')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="rowCheck" value="true"/>
			<jsp:param name="gridHeight" value="300"/>
		</jsp:include>
		<!-- 도면 현황	 -->
		<jsp:include page="${e3ps:getIncludeURLString('/distribute/include_relatedTempEpmList')}" flush="true">
			<jsp:param name="oid" value="${oid}"/>
			<jsp:param name="gridHeight" value="500"/>
		</jsp:include>
	</div>
</div>		
<!-- //pop-->