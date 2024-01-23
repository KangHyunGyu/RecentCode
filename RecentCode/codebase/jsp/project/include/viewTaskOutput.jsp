<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="seach_arm2 pt5 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		${e3ps:getMessage('산출물')}</span>
	</div>
	<div id="total_count" align="right" class="rightbt">
		<c:if test="${isTaskEdit && !task.child}">
   			<input type="button" class="s_bt03" value="추가"  onclick="addOutput()"/>
		</c:if>
	</div> 
</div>
<table class="mainTable mb25">
	<c:choose>
		<c:when test="${'PROGRESS' eq task.pjtState && isTaskAuth && 'PROGRESS' eq task.state && !task.child}">
			<tr>
				<th width="90">${e3ps:getMessage('작업 상황')}</th>
				<td>
					${e3ps:getMessage('진행율')}(%) : <input class="w20" type="text" name="completion" id="completion" value="${task.completion}" size="3" maxlength="6" onkeypress='return onlyNum(event)'/>
			        &nbsp;<input type="button" value="${e3ps:getMessage('수정')}" class="s_bt03" onclick="saveCompletion()" />
					&nbsp;<input type="button" value="${e3ps:getMessage('시작일자 수정')}" class="s_bt03" onclick="changeStartDate()" />
					<c:if test="${isAllOutputRegist}">
						<input type="button" value="${e3ps:getMessage('작업 완료')}" class="s_bt03" onclick="workComplete()" />
					</c:if>
				</td>
			</tr>
		</c:when>
		<c:when test="${'PROGRESS' eq task.pjtState && isTaskAuth && 'COMPLETED' eq task.state && !task.child}">
			<tr>
				<th width="90">${e3ps:getMessage('작업 상황')}</th>
				<td>
					<input type="button" value="${e3ps:getMessage('완료 취소')}" class="s_bt03" onclick="cancelComplete()" />
				</td>
			</tr>
		</c:when>
	</c:choose>
	<tr>
	    <th width="90">${e3ps:getMessage('문서')}</th>
		<td>
			<c:choose>
				<c:when test="${outputList.size() == 0 && 'PROGRESS' eq task.pjtState}">
					${e3ps:getMessage('작성할 문서가 없습니다.')}
				</c:when>
				<c:otherwise>
					<div class="pt5 pb5">
					<table class="mainTable">
				  	 	<colgroup>
							<col style="width:*">
							<col style="width:30%">
							<col style="width:10%">
							<col style="width:30%">
						</colgroup>	
						<tbody>
							<tr>
								<th>${e3ps:getMessage('이름')}</th>
								<th>${e3ps:getMessage('문서 분류')}</th>
								<th>${e3ps:getMessage('작업 현황')}</th>
								<th>${e3ps:getMessage('문서')}</th>
							</tr>
							<c:forEach var="output" items="${outputList}" varStatus="status">
								<tr>
									<td>
										<c:if test="${isTaskEdit && !task.child}">
											<input type="button" class="s_bt03" value="${e3ps:getMessage('수정')}" onclick="modifyOutput('${output.oid}')">
										</c:if>
										${output.name}
										<%-- <c:choose>
											<c:when test="${output.docOid != null}">
												<a href="JavaScript:openView('${output.docOid}')">${output.name}</a>
											</c:when>
											<c:otherwise>
												${output.name}
											</c:otherwise>
										</c:choose> --%>
									</td>
									<td>${output.docLocation == null ? output.location : output.docLocation}</td>
									<td>${output.docStateName == null ? e3ps:getMessage('등록 전') : output.docStateName}</td>
									<td>
									<c:choose>
										<c:when test="${chkObj}">
											<c:choose>
												<c:when test="${output.docOid == null}">
													<c:if test="${('PROGRESS' eq task.pjtState or 'MODIFY' eq task.pjtState)&& isTaskAuth && ('PROGRESS' eq task.state or 'COMPLETED' eq task.state)}">
												        <input type="button" value="${e3ps:getMessage('직접 작성')}" class="s_bt03" onclick="inputOutputResult('${output.oid}', '${output.foid}')" />
														<input type="button" value="${e3ps:getMessage('링크 등록')}" class="s_bt03" onclick="inputLinkOutput('${output.oid}', '${output.foid}')" />
													</c:if>
												</c:when>
												<c:otherwise>
													<a href="JavaScript:openView('${output.docOid}')">${output.docNumber}</a>
													(${output.docVersion})/(${output.lastDocVersion})
													<c:if test="${('PROGRESS' eq task.pjtState or 'MODIFY' eq task.pjtState)&& isTaskAuth && ('PROGRESS' eq task.state or 'COMPLETED' eq task.state)}">
													<a href="JavaScript:linkOut('${output.oid}')" title="${e3ps:getMessage('링크 끊기')}"><img src="/Windchill/jsp/project/images/project/del.gif"></a>
													</c:if>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											${e3ps:getMessage('문서 권한이 없습니다.')}
										</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<script type="text/javascript">
function addOutput(){
	
	var oid = "${oid}";
	
	var url = getURLString("/project/createOutput") + "?oid=" + oid;
	
	openPopup(url, "addOutput", 900, 400);
}

function modifyOutput(oid){
	
	var url = getURLString("/project/modifyOutput") + "?oid=" + oid + "&taskOid=${oid}";
	
	openPopup(url, "addOutput", 900, 400);
}

function inputOutputResult(outputOid, foid){
	var url = getURLString("/doc/createDocPopup") + "?outputOid=" + outputOid + "&foid="+foid;
	
	openPopup(url,"createDocPopup",900,600);
}

function inputLinkOutput(outputOid, foid) {
	
	var url = getURLString("/doc/searchDocPopup") + "?type=single&pageName=pDocument&foid=" + foid + "&outputOid=" + outputOid;
	
	openPopup(url, "addDocOutputPopup", 1300, 700);
	
}
function add_pDocument_addObjectList(outputOid, list){
	var oid = "";
	
	if(list.length > 0){
		oid = list[0].item.oid;
	}
	set_Document(outputOid, oid);
}

function set_Document(outputOid, docOid){
	var param = new Object();
	param.outputOid = outputOid;
	param.documentOid = docOid;
	var url = getURLString("/project/createOutputDocLink"); 
	ajaxCallServer(url, param, function(data){
		viewTask("${oid}");
	}, false);
}

function linkOut(outputId){
	
	openConfirm("${e3ps:getMessage('해당 산출물 과 문서의 관계를 끊으시겠습니까?')}",function(){
		
		var param = new Object();
		
		param.outputOid = outputId;
		
		var url = getURLString("/project/deleteOutputLink"); 
		ajaxCallServer(url, param, function(data){
			viewTask("${oid}");
		}, false);
	});
}

function saveCompletion(){
	
	var param = new Object();
	
	param.oid = "${oid}";
	param.completion = $("#completion").val();
	
	var url = getURLString("/project/saveCompletion"); 
	ajaxCallServer(url, param, function(data){
		viewTask("${oid}");
		updateTaskData(data.task);
	}, false);
}

function changeStartDate(){
	var url = getURLString("/project/changeStartDate") + "?oid=${oid}";
	openPopup(url,"changeStartDate","700","400");
}

function workComplete(){
	var url = getURLString("/project/workComplete") + "?oid=${oid}";
	openPopup(url,"workComplete","700","500");
}

function cancelComplete(){
	
	openConfirm("${e3ps:getMessage('완료를 취소하시겠습니까?')}",function(){
		
		var param = new Object();
		
		param.oid = "${oid}";
		
		var url = getURLString("/project/cancelComplete"); 
		ajaxCallServer(url, param, function(data){
			reloadTree();
			viewTask("${oid}");
		}, false);
	});
}
</script>