<%@page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="e3ps"
uri="/WEB-INF/tlds/e3ps-functions.tld"%><%@ taglib
uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script>

$(document).ready(function () {
	popupResize();
	  
});


function registrationApproval(){
	
	var approvalLists = AUIGrid.getGridData(app_line_myGridID);
	
	if(approvalLists.length == 0) {
		alert("${e3ps:getMessage('결재선을 지정해주세요')}");
		return;
	}else if(!confirm("${e3ps:getMessage('등록하시겠습니까?')}")){
		return;
	}
	
	
	var param = new Object();
	param.oid = "${oid}";
	param.approvalList = approvalLists;
	param.appState = "APPROVING";
	if(approvalLists.length > 0){
		param.appDescription = document.getElementById('appDescription').value;
	}
	
	var url = getURLString("/approval/registrationApprovalAction");
	
	ajaxCallServer(url, param, function(data){
		if(data.result){
			window.close();
		}
	}, false);
}


</script>
<!-- pop -->
<div class="pop3">
  <!-- top -->
  <div class="top">
    <h2>
      ${e3ps:getMessage('결재 등록')}
    </h2>
    <span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기" /></a></span>
  </div>

    <div class="seach_arm2 pt10 pb5 mr30 ml30" style="border-bottom:1px solid #1064aa;">
      <div class="leftbt">
      </div>
      <div class="rightbt">
        <button class="i_read" style="width:70px" onclick="registrationApproval()">
          ${e3ps:getMessage('등록')}
        </button>
      </div>
    </div>
  <form>
    <div class="ml30 mr30">
		<jsp:include page="${e3ps:getIncludeURLString('/approval/include_addApprovalLine')}" flush="true">
			<jsp:param name="gridHeight" value="180"/>
		</jsp:include>
	</div>
	
	<!-- 결재 의견 -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
			<h4>${e3ps:getMessage('결재 의견')}</h4>
		</div>
	</div>
	<div class="pro_table mr30 ml30 mb200" >
		<table class="mainTable">
			<tr>
				<td class="pd15">
				<div class="textarea_autoSize">
					<textarea name="appDescription" id="appDescription" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
				</div>
				</td>
			</tr>
		</table>
	</div>
  </form>
</div>

