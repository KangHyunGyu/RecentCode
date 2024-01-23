<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="tree_top3 ml10">
	<div class="pro_table mr25 ml25 mb25">
		<div class="seach_arm2 pt5 pb5">
			<div class="leftbt">
				<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
				${e3ps:getMessage('하위 태스크 진행현황')}</span>
			</div>
			<div id="total_count" class="rightbt">
				<c:if test="${e3ps:isAdmin() || isManager}">
			 		<input type="button" class="s_bt03" value="수정"  onclick="save_task()"/>
			 	</c:if>
			</div> 
		</div>
		<table class="mainTable mb25">
			<colgroup>
				<col style="width:15%">
				<col style="width:*">
			</colgroup>	
			<tbody>
				<c:choose>
					<c:when test="${e3ps:isAdmin() || isManager}">
						<tr>
							<th>${e3ps:getMessage('태스크 명')} ${isManager}</th>
							<td colspan="3"><input type="text" name="taskName" id="taskName"  value="${task.name}" maxlength="80" style="width: 99%"></td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('기간')}(${e3ps:getMessage('일')})</th>
							<c:choose>
								<c:when test="${!task.child}">
									<td colspan=3><input type="text" id="manDay" name="manDay" value="${task.manDay}" maxlength="5">
									</td>
								</c:when>
								<c:otherwise>
									<td colspan="3">
										${task.manDay}
									</td>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<th>${e3ps:getMessage('설명')}</th>
							<td colspan="3" class="pt5">
								<div class="textarea_autoSize">
									<textarea name="description" id="description" cols="90%">${task.description}</textarea>
								</div>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
						    <th>${e3ps:getMessage('태스크 명')}</th>
							<td colspan="3">
							      ${task.name}
						    </td>
						   </tr>
						<tr>
					    <th>${e3ps:getMessage('기간')}(${e3ps:getMessage('일')})</th>
							<td colspan="3">
							      ${task.manDay}
						    </td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('설명')}</th>
							<td colspan="3">
						        ${task.description}
						    </td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		
		<div class="seach_arm2 pt5 pb5">
			<div class="leftbt">
				<span class="title">
					<img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
				 	${e3ps:getMessage('태스크 상세 정보')}
				</span>
			</div>
			<div id="total_count" class="rightbt">
			</div> 
		</div>
		<table class="mainTable mb25">
			<colgroup>
				<col style="width:15%">
				<col style="width:*">
			</colgroup>	
			<tbody>
				<tr>
				    <th>${e3ps:getMessage('선행 태스크')}</th>
					<td>
						<c:if test="${e3ps:isAdmin() || isManager && !task.child}">
							<div class="seach_arm2 pt5">
								<div class="leftbt">
									<span class="title">
										<input type="button" class="s_bt03" value="${e3ps:getMessage('편집')}" onclick="insertPre()"/>
									</span>
								</div>
							</div>
						</c:if>
						<div class="pt5 pb5">
						  	 <table class="mainTable">
						  	 	<colgroup>
									<col style="width:*">
									<col style="width:15%">
									<col style="width:25%">
								</colgroup>	
								<tbody>
									<tr>
										<th>${e3ps:getMessage('태스크 명')}</th>
										<th>${e3ps:getMessage('기간')}</th>
										<th>${e3ps:getMessage('프로젝트 Role')}</th>
									</tr>
									<c:forEach var="preTask" items="${preTaskList}" varStatus="status">
										<tr>
											<td><a href="javascript:viewTask('${preTask.oid}')">${preTask.name}</a></td>
											<td>${preTask.manDay}${e3ps:getMessage('일')}</td>
											<td>${preTask.roleName}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
				    </td>
			    </tr>
			    <tr>
				    <th>${e3ps:getMessage('후행 태스크')}</th>
					<td>
						<div class="pt5 pb5">
						  	 <table class="mainTable">
						  	 	<colgroup>
									<col style="width:*">
									<col style="width:15%">
									<col style="width:25%">
								</colgroup>	
								<tbody>
									<tr>
										<th>${e3ps:getMessage('태스크 명')}</th>
										<th>${e3ps:getMessage('기간')}</th>
										<th>${e3ps:getMessage('프로젝트 Role')}</th>
									</tr>
									<c:forEach var="postTask" items="${postTaskList}" varStatus="status">
										<tr>
											<td><a href="javascript:viewTask('${postTask.oid}')">${postTask.name}</a></td>
											<td>${postTask.manDay}${e3ps:getMessage('일')}</td>
											<td>${postTask.roleName}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
				    </td>
				</tr>
				<tr>
				    <th>${e3ps:getMessage('산출물')}</th>
					<td>
						<c:if test="${e3ps:isAdmin() && !task.child}">
							<div class="seach_arm2 pt5">
								<div class="leftbt">
									<span class="title">
										<input type="button" class="s_bt03" value="${e3ps:getMessage('추가')}" onclick="addOutput()"/>
									</span>
								</div>
							</div>
						</c:if>
						<div class="pt5 pb5">
						  	 <table class="mainTable">
						  	 	<colgroup>
									<col style="width:*">
									<col style="width:40%">
								</colgroup>	
								<tbody>
									<tr>
										<th>${e3ps:getMessage('이름')}</th>
										<th>${e3ps:getMessage('문서 분류')}</th>
									</tr>
									<c:forEach var="output" items="${outputList}" varStatus="status">
										<tr>
											<td>
												<c:choose>
													<c:when test="${e3ps:isAdmin()}">
														<a href="JavaScript:modifyOutput('${output.oid}')">${output.name}</a>	
													</c:when>
													<c:otherwise>
														<a href="JavaScript:viewOutput('${output.oid}')">${output.name}</a>
													</c:otherwise>
												</c:choose>
											</td>
											<td>${output.location}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
				    </td>
				</tr>
			</tbody>
		</table>

		<div class="seach_arm2 pt5 pb5">
			<div class="leftbt">
				<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
				${e3ps:getMessage('태스크')} Role</span>
			</div>
			<div id="total_count" class="rightbt">
			
			</div> 
		</div>		
		<table class="mainTable mb25">
			<colgroup>
				<col style="width:15%">
				<col style="width:*">
			</colgroup>	
			<tbody>
				<tr>
				    <th>${e3ps:getMessage('프로젝트')} Role</th>
					<td>
						<c:if test="${e3ps:isAdmin() && !task.child}">
							<div class="seach_arm2 pt5">
								<div class="leftbt">
									<span class="title">
										<input type="button" class="s_bt03" value="${e3ps:getMessage('편집')}" onclick="selectRole('${oid}')"/>
									</span>
								</div>
							</div>
						</c:if>
						<div class="<c:if test="${roleList.size() > 0}">pt5</c:if> pb5">
							<c:forEach var="role" items="${roleList}" varStatus="status">
								${role.name}<c:if test="${!status.last}">, </c:if>
							</c:forEach>
						</div>
				    </td>
				</tr>
			</tbody>
		</table>
 	</div>
 </div>
<script type="text/javascript">
function save_task(){
	
	if(!checkValidate()) {
		return;
	}
	
	openConfirm("${e3ps:getMessage('수정하시겠습니까?')}",function(){
		
		var param = new Object();
		
		param.oid = "${oid}";
		param.taskName = $("#taskName").val();
		param.manDay = $("#manDay").val();
		param.description = $("#description").val();
		
		var url = getURLString("/project/template/updateTaskAction"); 
		ajaxCallServer(url, param, function(data){
			var task = data.task;
			$$("tempTree").updateItem(task.id, task);
		}, false);
	});
}

function checkValidate() {
	
	if($("#taskName").val() == null || $("#taskName").val() == ""){
		$("#taskName").focus();
		openNotice("${e3ps:getMessage('태스크 명을 입력해 주십시오.')}");
		return false;
	}
	
	if($("#manDay").val() != null && isNaN($("#manDay").val())) {
		$("#manDay").focus();
		openNotice("${e3ps:getMessage('정수로 입력해 주십시오.')}");
		return false;
	}
	
	if($("#manDay").val() < 0) {
		$("#manDay").focus();
		openNotice("${e3ps:getMessage('0보다 작은 수를 입력할 수 없습니다.')}");
		return false;
	}
	
	return true;
}

function insertPre(){
	
	var oid = "${oid}";
	
	var url = getURLString("/project/template/selectPreTask") + "?oid=" + oid;
	
	openPopup(url, "selectPreTask", 600, 800);
}

function viewTask(oid){
	var url = getURLString("/project/template/viewTask");
	var param = new Object();
	param.oid = oid;
	loadIncludePage(url, param);
}

function addOutput(){
	
	var oid = "${oid}";
	
	var url = getURLString("/project/template/createOutput") + "?oid=" + oid;
	
	openPopup(url, "addOutput", 900, 400);
}

function viewOutput(oid){
	
	var url = getURLString("/project/template/viewOutput") + "?oid=" + oid + "&taskOid=${oid}";
	
	openPopup(url, "addOutput", 900, 400);
}

function modifyOutput(oid){
	
	var url = getURLString("/project/template/modifyOutput") + "?oid=" + oid + "&taskOid=${oid}";
	
	openPopup(url, "addOutput", 900, 400);
}

function selectRole(){
	var oid = "${oid}";
	
	var url = getURLString("/project/template/selectRole") + "?oid=" + oid;
	
	openPopup(url, "addOutput", 500, 550);
}
</script>