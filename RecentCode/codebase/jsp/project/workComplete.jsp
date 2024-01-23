<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('작업완료')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:saveDate()">${e3ps:getMessage('작업 완료')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="editForm" id="editForm" method="post">
			<input type="hidden" name="oid" value="${oid}">
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('계획 시작일자')}</th>
							<td>
								${task.planStartDate}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('계획 종료일자')}</th>
							<td>
								${task.planEndDate}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('실제 시작일자')}</th>
							<td class="calendar">
								<input type="text" class="datePicker w50" name="startDate" id="startDate" readonly value="${task.startDate}"/>
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('실제 종료일자')}</th>
							<td class="calendar">
								<input type="text" class="datePicker w50" name="endDate" id="endDate" readonly/>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	
	if("${task.endDate}" != "") {
		$("#endDate").val("${task.endDate}");
	} else {
		var today = new Date();
		$("#endDate").val(today.toISOString().slice(0,10).replace(/-/g,"/"));
	}
});

function saveDate(){
	
	if(!checkValidate()) {
		return;
	}
	
	$("#editForm").attr("action",getURLString("/project/workCompleteAction"));
	
	formSubmit("editForm", null, "${e3ps:getMessage('해당 태스크 작업을 완료 하시겠습니까?')}",function(){
		if(opener.window.reloadTree) {
			opener.window.reloadTree();
		}
		if(opener.window.viewTask) {
			opener.window.viewTask("${oid}");
		}
		window.close();
	}, true);
}

function checkValidate() {
	
	if($("#startDate").val() == null ||$("#startDate").val().length == 0){
		$("#startDate").focus();
		openNotice("${e3ps:getMessage('시작일자를 입력해 주십시오.')}");
		return false;
	}
	
	if($("#endDate").val() == null ||$("#endDate").val().length == 0){
		$("#endDate").focus();
		openNotice("${e3ps:getMessage('종료일자를 입력해 주십시오.')}");
		return false;
	}
	
	/* if(checkDate($("#startDate").val(),new Date())){
		openNotice("${e3ps:getMessage('실제 시작일자는 오늘 날짜보다 빠를 수 없습니다.')}");
		return;
	} */
	
	/* if(!checkToDate(document.editForm.startDate,document.editForm.toDay)){
		alert("실제 시작일자는 오늘 날짜보다 느릴 수 없습니다.");
		return;
	} */
	
	/* if(!checkDate($("#endDate").val(),new Date())){
		openNotice("${e3ps:getMessage('실제 종료일자는 오늘 날짜보다 느릴 수 없습니다.')}");
		return;
	} */
	
	if(!checkDate($("#startDate").val(),$("#endDate").val())){
		openNotice("${e3ps:getMessage('실제 종료일자는 실제 시작일자보다 빠를 수 없습니다.')}");
		return;
	}
	
	if(!checkDate("${task.pjtStartDate}",$("#startDate").val())){
		openNotice("${e3ps:getMessage('실제 시작일자는 프로젝트 실제 시작일 보다 빠를 수 없습니다.')}");
		return;
	}
	
	if(!checkDate("${task.pjtStartDate}",$("#endDate").val())){
		openNotice("${e3ps:getMessage('실제 종료일자는 프로젝트 실제 시작일 보다 빠를 수 없습니다.')}");
		return;
	}
	
	return true;
}

function checkDate(a,b){

	var start = new Date(a);
	var end = new Date(b);
	
	if(end==null || end.length==0)return true;
	if(start > end){
		try{
			b.focus();
		}catch(e){}
		return false;
	}
	return true;
}
</script>
