<%@page import="com.e3ps.common.util.AuthHandler"%>
<%@page import="com.e3ps.common.util.AuthHandler.RoleKey"%>
<%@page import="com.e3ps.common.message.M"%>
<%@page import="com.e3ps.approval.service.ApprovalHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page import="wt.lifecycle.State"%>
<%@page import="com.e3ps.common.util.TypeUtil"%>
<%@page import="com.e3ps.common.util.WebUtil"%>
<%@page import="com.e3ps.groupware.service.WFItemHelper"%>
<%@page import="com.e3ps.common.util.StringUtil"%>
<%@page import="com.e3ps.change.EChangeActivity"%>

<%
	boolean popup = TypeUtil.booleanValue(request.getParameter("popup"));
	boolean singleSelect = TypeUtil.booleanValue(request.getParameter("singleSelect"));
%>
<style type="text/css">
/** Advanced Search Condition TR */
.advSearchConditionTr { display: none; }
</style>
<script type="text/javascript">
//#. global 변수
var g_popup = <%= popup %>;
var g_singleSelect = <%= singleSelect %>;
var g_gridRows = null; // popup 일때 콜백을 위해 loadComplete 시에 json 정보를 담는다
var g_search = false;

// #. 페이지 초기화
$(function() {
	// #. grid 생성
	$("#ecrGridTable").jqGrid({
		url : "/Windchill/kores/change/getECRListJson",
		datatype : "json",
		mtype : "post",
		/* ECR번호 제목 유형 작업현황 등록자 최초등록일 요청자 검토완료일 */
		colNames : [
			"<%=M.get("ECR 번호")%>", 
			"<%=M.get("ECR 제목")%>", 
			"<%=M.get("구분")%>",
			"<%=M.get("변경원인")%>",
			"<%=M.get("제품군")%>",
			"<%=M.get("작업현황")%>",
			"<%=M.get("최초등록일")%>",
			"<%=M.get("수정일")%>",
			"<%=M.get("등록자")%>",
			"<%=M.get("관련기종")%>"
		],
		colModel : [{
			name : "requestNumber", width : 120, align : "center", sortable : true, frozen : true
		},{
			name : "name", width : 300, align : "left", sortable : true, 
			formatter: function (cellvalue, options, rowObject) {
				return "<a href=\"javascript:void(gotoViewECR('" + rowObject.id + "'))\">" + rowObject.name + "</a>";
			}
		}, {
			name : "requestType", width : 70, align : "center", sortable : true
		}, {
			name : "reason", width : 70, align : "center", sortable : true
		}, {
			name : "productGroup", width : 100, align : "center", sortable : true
		}, {
			name : "state", width : 70, align : "center", sortable : true
		}, {
			name : "createDate", width : 70, align : "center", sortable : true
		}, {
			name : "updateDate", width : 70, align : "center", sortable : true
		}, {
			name : "tempcreator", width : 70, align : "center", sortable : true
		}, {
			name : "project", width : 70, align : "center", sortable : true
		}],
		jsonReader : {
			root : "rows",
			page : "page",
			total : "total", // total page count
			records : "records", // 
			repeatitems : false,
			id : "id",
			userdata : "userdata"
		},
		rownumbers : true,
		rowNum : 20, 
		recordpos : "left",
		
		hidegrid : false,
		multiselect : g_popup, // display checkbox
		multiboxonly : g_singleSelect, // as radio
		autowidth : true, 
		shrinkToFit : false,
		loadtext : "<%= M.get("로딩중입니다.") %>",
		sortable : false, 
		height : 350,
		
		beforeSelectRow : function(rowid, e) {
			if (g_popup) {
				// #. checkbox selection 전에 리셋. 체크박스가 multiselection 되지 않도록 하기위해 설정
				handleBeforeSelectRow(g_singleSelect, "#ecrGridTable");
				return true;
			} else {
				return false;
			}
			
		},
		loadComplete : function(data) {
			// #. 팝업이면 callback을 위해 저장
			if (g_popup) {
				g_gridRows = data.rows;
			}
			
			// #. paging 갱신
			refreshPaginate("#ecrGridTable");
		},
		loadError : handleLoadError,
		onSortCol : handleOnSortColumn
	}); // jqGrid
	
	// #. window resize 핸들러 등록
	addJqGridResizeListener("ecrGridTable", "ecrGridDiv");
	
	// #. 페이징 초기화
	//refreshPaginate("#ecrGridTable");
	loadECRGrid();
	
	// #. 팝업시 더블클릭 바인드
	if (g_popup) {
		addDblClickRowListener("ecrGridTable", function(dataIndex) {
			var data = g_gridRows[dataIndex];
			applyECR([data]);
		});
	}
	
});//ready

/**
 * 페이지 내의 Grid 를 갱신한다
 */
function refreshPage() {
	loadECRGrid();
}

/**
 * ECR을 검색한다
 */
function loadECRGrid() {

	var requestTypeVal = "";
	var requestType = $("input[type='radio'][name='requestType']:checked");
	if (requestType.length > 0) {
		requestTypeVal = requestType.val();
	}
	
	var reasonVal = "";
	var reason = $("#reason > option:selected");
	if (reason.length > 0) {
		reasonVal = reason.val();
	}
	
	var productGroupVal ="";
	var productGroup = $("#productGroup > option:selected");
	if (productGroup.length > 0) {
		productGroupVal = productGroup.val();
	}
	
	g_search = true;
	$("#ecrGridTable").setGridParam({
		
		postData:{
			requestNumber : $("#requestNumber").val(), // 번호
			name : $("#name").val(), // 제목
			drawingNumber : $("#drawingNumber").val(), //도면번호
			partNumber : $("#partNumber").val(), //부품번호
			problem : $("#problem").val(), // 변경내용
			project : $("#project").val(), // 관련기종
			requestType : requestTypeVal, // 요청구분
			reason : reasonVal, // 변경원인
			productGroup : productGroupVal, // 제품군
			preRequestdate : $("#preRequestdate").val(), // 적용요구시점전
			postRequestdate : $("#postRequestdate").val(), // 적용요구시점후
			postdate : $("#postdate").val(), // 작성일_이후
			predate : $("#predate").val(), // 작성일_이전
			tempworker : $("#tempworker").val(), // ECO 담당자
			tempcreator : $("#tempcreator").val(), // 등록자
			state : $("#state").val(), //작업현황
			changeTarget : $("#changeTarget").val(),//변경대상번호
			department : $("#department").val(),//요청팀
			
			searchChk : true
	   	}
	}).trigger("reloadGrid");
}

/**
 * 엔터키를 치면 ECR검색 수행
 */
function searchECROnKeyDown(event) {
	if (event.keyCode == 13) {
		loadECRGrid();
		event.returnValue = false;
	}
}

/**
 * 검색폼을 초기화한다
 */
function resetECRSearchForm() {
	document.mainForm.reset();
	document.mainForm.creator.value = "";
}

/**
 * ECR 상세보기
 */
function gotoViewECR(oid){
	var callbackName = "opener.callbackViewECR";
	openCommonViewECRPopup(oid, callbackName);
}

/**
 * ECR 상세 팝업 콜백. 삭제 후 목록을 리프레쉬한다
 */
function callbackViewECR() {
	loadECRGrid();
}

/**
 * ECR 목록 excel 다운로드
 */
function downloadECRExcel() {
	form = document.mainForm;
	form.action = "/Windchill/kores/change/downloadECRListExcel";
	form.target = "_self";
	form.method = "post";
	form.submit();
}

/**
 * 선택된 ECR을 적용한다
 */
function applyECR() {
	var f = document.ecrForm;
	
	// #. 선택된 부품을 g_gridRows 조회
	var ecrs = [];
	var rowIds = $("#ecrGridTable").jqGrid("getGridParam", "selarrrow");
	for (var i = 0; i < rowIds.length; i++) {
		var rowId = rowIds[i];
		if (g_gridRows != null) {
			for (var j = 0; j < g_gridRows.length; j++) {
				if (g_gridRows[j].id == rowId) {
					ecrs.push(g_gridRows[j]);
					break;
				}
			}
		}
	}
	
	// #. 검증
	if (ecrs.length == 0) {
		alert("<%=M.get("ECR을 선택하시기 바랍니다")%>");
		return;
	}
	
	//console.log(">>> ecr=\n" + JSON.stringify(ecrs));
	var tt = JSON.stringify(ecrs);
	opener.document.getElementById("c1").value=tt;
	
	// #. 닫기
	window.self.close();
}

/**
 * ECR 등록화면 이동
 */
function createECR(){
	location.href="/Windchill/kores/change/createECR";
}

/**
 * 상세검색 toggle
 * advSearchConditionTr 클래스를 select 하여 show 또는 hide
 */
function toggleAdvSearchConditions() {
	var className = "advSearchConditionTr";
	var iconBaseURL = "/Windchill/extcore/kores/pdm/images/";
	
	// #. 현재 display 판단
	if ($("." + className).css("display") == "none") {
		$("." + className).show();
		$("#advSearchConditionBtn").attr("src", iconBaseURL + "bt_search_02<%=M.getImg()%>.gif");
	} else {
		$("." + className).hide();
		$("#advSearchConditionBtn").attr("src", iconBaseURL + "bt_search_01<%=M.getImg()%>.gif");
	}
}

</script>

<form name="mainForm" method="post">
<!-- opener callback 관련 -->
<input type="hidden" name="callbackName" value="${param.callbackName}"/>
<textarea style="display:none" name="callbackObj">${param.callbackObj}</textarea>

<!-- title 시작-->
<table width="100%" class="pdl10" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<img src="/Windchill/extcore/kores/pdm/images/title_bullet.gif">
			<span class="ctitle"><%=M.get("ECR검색")%></span>
		</td>
	</tr>
	<tr>
		<td bgcolor="#e3e3e3" height="1"></td>
	</tr>
</table>

<!--contents 시작-->
<table width="100%" class="pdt10 pdl10" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top">
			<!--검색 조건 시작-->
			<table width="100%" class="stbltc" border="0" cellspacing="0" cellpadding="0">
				<colgroup>
					<col width="10%">
					<col width="40%">
					<col width="12%">
					<col width="38%">
				</colgroup>
				<tr>
					<th><%=M.get("ECR 번호")%></th>
					<td>
						<input name="requestNumber" id="requestNumber" size="60" value="${param.requestNumber}" class="input01" style="width: 50%"
								onkeydown="searchECROnKeyDown(event)"/>
					</td>
					<th><%=M.get("ECR 제목")%></th>
					<td>
						<input name="name" id="name" size="60" value="${param.name}" class="input01" style="width: 50%"
								onkeydown="searchECROnKeyDown(event)"/>
					</td>
				</tr>
				<tr>
					<th><%=M.get("도면번호")%></th>
					<td>
						<input id="drawingNumber" name="drawingNumber" type="text" class="input01" style="width: 50%" onblur="this.value=this.value.toUpperCase();" onkeydown="searchECROnKeyDown(event)">
					</td>
					<th><%=M.get("부품번호")%></th>
					<td>
						<input id="partNumber" name="partNumber" type="text" class="input01" style="width: 50%" onkeydown="searchECROnKeyDown(event)">
					</td>
				</tr>
				<tr>
					<th><%=M.get("변경내용")%></th>
					<td>
						<input name="problem" id="problem" size="60" value="${param.problem}" class="input01" style="width: 50%"
								onkeydown="searchECROnKeyDown(event)"/>
					</td>
					<th><%=M.get("관련기종")%></th>
					<td>
						<input name="project" id="project" size="60" value="${param.project}" class="input01" style="width: 50%"
								onkeydown="searchECROnKeyDown(event)"/>
					</td>
				</tr>
				<tr>
					<th><%=M.get("구분")%></th>
					<td>
						<input type="radio" name="requestType" id="requestType" value="" checked><%=M.get("전체")%></input>
						<input type="radio" name="requestType" id="requestType" value="innerEcr"><%=M.get("자체설변")%></input>
						<input type="radio" name="requestType" id="requestType" value="outerEcr"><%=M.get("외부설변")%></input>
					</td>
					<th><%=M.get("작업현황")%></th>
					
					<td>
						<select id="state" name="state" class="select01 w150">
							<option value=""><%=M.get("전체")%></option>
							<%
							List<String> stateOptions = ApprovalHelper.service.getSelectStateList();
							String state = request.getParameter("state");
							for(String ss: stateOptions){
							%> 
							<option value="<%=ss%>"><%=M.get(ss)%></option>
							<%} %>
						</select>
					</td>
				</tr>
				<tr>
					<th><%=M.get("변경원인")%></th>
					<td>
						<jsp:include page="/extcore/kores/common/include/numberCodeSelect.jsp" flush="true">
							<jsp:param name="inputName" value="reason"/>
							<jsp:param name="codeType" value="CHANGEREASON"/>
						</jsp:include>
					</td>
					
					<th><%=M.get("제품군")%></th>
					<td>
						<div style="float:left; width:50%;">
							<jsp:include page="/extcore/kores/common/include/numberCodeSelect.jsp" flush="true">
								<jsp:param name="inputName" value="productGroup"/>
								<jsp:param name="codeType" value="PRODUCTGROUP"/>
								<jsp:param name="disabled" value="true"/>
						</jsp:include>
						</div>
						<%-- <!-- 상세버튼 -->
						<div style="float:left; width:50%; text-align:right">
							<a href="javascript:toggleAdvSearchConditions()"><img 
								id="advSearchConditionBtn" src="/Windchill/extcore/kores/pdm/images/bt_search_01<%=M.getImg()%>.gif"></a>
						</div> --%>
					</td>
				</tr>
				<tr class="">
					<th><%=M.get("적용요구시점")%></th>
					<td>
						<jsp:include page="/extcore/kores/common/include/rangeDateInput.jsp" flush="true">
							<jsp:param name="preDateId" value="preRequestdate"/>
							<jsp:param name="postDateId" value="postRequestdate"/>
						</jsp:include>
					</td>
					<th><%=M.get("ECO 담당자")%></th>
					<td>
						<jsp:include page="/extcore/kores/common/include/creatorInput.jsp" flush="true">
							<jsp:param name="creatorNameId" value="worker"/>
							<jsp:param name="creatorFullNameId" value="tempworker"/>
						</jsp:include>
					</td>
				</tr>
				<tr class="">
					<th><%=M.get("최초등록일")%></th>
					<td>
						<jsp:include page="/extcore/kores/common/include/rangeDateInput.jsp" flush="true">
							<jsp:param name="preDateId" value="predate"/>
							<jsp:param name="postDateId" value="postdate"/>
						</jsp:include>
					</td>
					<th><%=M.get("변경대상번호")%></th>
					<td>
						<input name="changeTarget" id="changeTarget" size="60" value="${param.changeTarget}" class="input01" style="width: 50%"
							onkeydown="searchECROnKeyDown(event)"/>
					</td>
				</tr>
				<tr class="">
					<th><%=M.get("등록자")%></th>
					<td colspan="">
						<jsp:include page="/extcore/kores/common/include/creatorInput.jsp" flush="true">
							<jsp:param name="creatorNameId" value="creator"/>
							<jsp:param name="creatorFullNameId" value="tempcreator"/>
						</jsp:include>
					</td>
					<th><%=M.get("요청팀")%></th>
					<td>
						<select id="department" name="department" class="select01 w150">
							<option value=""><%=M.get("전체")%></option>
							<option value="<%=M.get("CEO")%>"><%=M.get("CEO")%></option>
							<option value="<%=M.get("원가관리팀")%>"><%=M.get("원가관리팀")%></option>
							<option value="<%=M.get("영업팀")%>"><%=M.get("영업팀")%></option>
							<option value="<%=M.get("제품설계팀")%>"><%=M.get("제품설계팀")%></option>
							<option value="<%=M.get("선행연구해석팀")%>"><%=M.get("선행연구해석팀")%></option>
							<option value="<%=M.get("생산기술팀")%>"><%=M.get("생산기술팀")%></option>
							<option value="<%=M.get("구매개발팀")%>"><%=M.get("구매개발팀")%></option>
							<option value="<%=M.get("품질")%>"><%=M.get("품질")%></option>
							<option value="<%=M.get("품질관리팀")%>"><%=M.get("품질관리팀")%></option>
							<option value="<%=M.get("생산")%>"><%=M.get("생산")%></option>
							<option value="<%=M.get("생산1팀")%>"><%=M.get("생산1팀")%></option>
							<option value="<%=M.get("생산2팀")%>"><%=M.get("생산2팀")%></option>
							<option value="<%=M.get("생산관리팀")%>"><%=M.get("생산관리팀")%></option>

						</select>
						<!-- <input name="department" id="department" size="60" class="input01" style="width: 50%"
								onkeydown="searchECROnKeyDown(event)"/> -->
					</td>
				</tr>
				
				
			</table>
			<!--검색 조건 끝--> 
			
			<!--버튼 영역 시작-->
			<table width="100%" class="pdt10" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
						<input type="button" class="button01" value="<%=M.get("검색")%>" onclick="loadECRGrid()">
						<%if(AuthHandler.getAcl(RoleKey.createECR)){ %>
						<input type="button" class="button01" value="<%=M.get("등록")%>" onclick="createECR()">
						<%} %>
						<input type="button" class="button01" value="<%=M.get("초기화")%>" onclick="resetECRSearchForm()">
						<input type="button" class="button01" value="<%=M.get("엑셀출력")%>" onclick="downloadECRExcel()">
					<% if (popup) { %>
						<input type="button" class="button01" value="<%=M.get("적용")%>" onclick="applyECR()">
						<input type="button" class="button01" value="<%=M.get("닫기")%>" onclick="self.close()">
					<% } %>
					</td>
				</tr>
			</table>
			<!--버튼 영역 끝-->
			
			<!--검색결과 리스트 시작-->
			<div id="ecrGridDiv" class="pdt10">
				<table id="ecrGridTable" border="0" cellpadding="0" cellspacing="0"></table>
			</div>
			<div id="paginate"></div>
			<!--검색결과 리스트 끝-->
		</td>
	</tr>
</table>
<!--contents 끝-->

</form>