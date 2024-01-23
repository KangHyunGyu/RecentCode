<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript"> 
function saveSolution()
{
	if (document.IssueForm.requestDate.value == ''){
		alert('완료 예정일을 입력해 주십시오');
		document.IssueForm.requestDate.focus();
		return;
	}
	if (document.IssueForm.solution.value == ''){
		alert('해결방안을 입력해 주십시오');
		document.IssueForm.solution.focus();
		return;
	}
	
	/* if (!confirm("등록하시겠습니까?")){
		return;
	} */
	if(!confirm("해결방안을 등록하게 되면\n 관리자 또는 재기자만\n 해당 내용을 수정할 수 있습니다.")){
		return;
	}
	
	var url = getURLString("/project/issue/createSolutionAction");
	
	var param = new Object();
	
	var paramArray = $("#IssueForm").serializeArray();
	
	var secondaryList = new Array();

	$(paramArray).each(function(idx, obj){
		if(obj.name == "SECONDARY"){
			secondaryList.push(obj.value);
		}else{
			param[obj.name] = obj.value;
		}
	});
	
	if(secondaryList.length > 0){
		param["SECONDARY"] = secondaryList;
	}
	
	ajaxCallServer(url, param, function(data){
		location.reload();
		if (opener.window.getGridData) {
			opener.window.getGridData();
		}
	}, true);
}
function add_attach_row()
{
	var fileObjs = document.getElementById("attachFile_tbl");
	var addRow =  fileObjs.insertRow();
	addRow.setAttribute('height',20);
	var addCol = addRow.insertCell();
	addCol.innerHTML = "<input type='checkbox' name='secondary_chk'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	addCol.innerHTML += "<input type='file' name='secondary' style='width:80%;border:1 solid #ADAEAD'>";		
}
function delete_table_row()
{
	var tblObj = document.getElementById("attachFile_tbl");
	var chkObj = document.getElementsByName('secondary_chk');
	var count = chkObj.length;
	for (var i=count-1; i>-1; i--)
	{
		if ( chkObj[i].checked ) {
			tblObj.deleteRow(i);
		}
	}
}

function selectDoc() {
    var url = "/Windchill/worldex/document/selectDoc?module=document&mode=selectDoc";

    var attache = window.showModalDialog(url,window,"help=no; scroll=yes; resizable=yes; dialogWidth=1000px; dialogHeight:650px; center:yes");
    if(typeof attache == "undefined" || attache == null) return;
    addDoc(attache);
}


function addDoc(arrObj) {
    var pForm = document.PartDrawingForm;

    if(!arrObj.length) return;

    var subarr;
    var docOid;//
    var docNumber;//
    var docName;//
    var docVersion;//
    var docCheck = true;

    for(var i = 0; i < arrObj.length; i++) {
        docCheck = true;

        subarr = arrObj[i];
        docOid = subarr[0];//
        docNumber = subarr[1];//
        docName = subarr[2];//
        docVersion = subarr[3];//

        if(pForm['docOid']) {
            if(pForm['docOid'].length) {
                for(var j = 0; j < pForm['docOid'].length; j++) {
                    if(pForm['docOid'][j].value == docOid) docCheck = false;
                }
            } else {
                if(pForm['docOid'].value == docOid) docCheck = false;
            }
        }

        if(docCheck) {
            var userRow1 = docTable.children[0].appendChild(innerTempTable.rows[0].cloneNode(true));
            userRow1.childNodes[0].width="8%";
            setHtml(userRow1.childNodes[0], "<input type=\"checkbox\" name=\"DocDelete\"><input type=\"hidden\" name=\"docOid\" value=\""+docOid+"\">");
            setHtml(userRow1.childNodes[1], docNumber);
            setHtml(userRow1.childNodes[2], "<nobr>"+docName+"</nobr>");
            setHtml(userRow1.childNodes[3], docVersion);
        }
    }
}

function deleteDoc() {
    var pForm = document.PartDrawingForm;
	
    if( null == pForm['DocDelete']){
    	return;
    }
    	
    if(pForm['DocDelete'] && pForm['DocDelete'].length) {
        for(var i=pForm['DocDelete'].length-1; i>=0; i--) {
            if(pForm['DocDelete'][i].checked) docTable.deleteRow(i+1);
        }
    }else {
        if(pForm['DocDelete'].checked) docTable.deleteRow(1);
    }
}

function setHtml( tt, data ) {
    tt.innerHTML = data;
}
</script>

<!-- pop -->
<div class="pop5">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('프로젝트 이슈 상세정보')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png"></a></span>
	</div>
<div class="seach_arm2 pt10 pb5">
	<div class="leftbt"></div>
	<div class="rightbt"></div>
</div>

<form name="IssueForm" id="IssueForm" method="post"  enctype="multipart/form-data"> 

<input type="hidden" name="command" />
<input type="hidden" name="oid" value="${oid}" />
<input type="hidden" name="manager" value="${manager}" />

<div class="seach_arm pt10 pb5">
	<div class="leftbt">
		<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('해결방안 등록')}
	</div>
	<div class="rightbt">
		<button type="button" class="s_bt03" id="searchBtn" onclick="saveSolution();">${e3ps:getMessage('등록')}</button>
		<button type="button" class="s_bt05" id="resetBtn" onclick="self.close();">${e3ps:getMessage('닫기')}</button>
	</div>
</div>

<%-- <jsp:include page="/jsp/project/issue/include/viewIssue.jsp"></jsp:include> --%>

<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<colgroup>
			<col style="width:20%">
			<col style="width:30%">
			<col style="width:20%">
			<col style="width:30%">
		</colgroup>	
		<tbody>
			<tr>
				<th>이슈 </th>
				<td colspan="3">
					&nbsp;${data.name}
				</td>
			</tr>
			<tr>
				<th>타입 </th>
				<td>
					&nbsp;${data.issueType}
				</td>
				<th>작업현황</th>
				<td>
					&nbsp;${data.state}
				</td>
			</tr>
			<tr>
				<th>프로젝트</th>
				<td>
					&nbsp;${pjtName}
				</td>
				<th>태스크</th>
				<td>
					&nbsp;${data.taskName}
				</td>
			</tr>
			<tr>
				<th>제기자</th>
				<td>
					&nbsp;${data.creatorFullName}
				</td>
				<th>제기일</th>
				<td>
					&nbsp;${data.createDate}
				</td>
			</tr>
			<tr>
				<th>담당자</th>
				<td>
					&nbsp;${data.managerFullName}
				</td>
				<th>완료예정일</th>
				<td>
					&nbsp;${requestDate}
				</td>
			</tr>
			<tr height=150>
				<th>이슈 개요</th>
				<td colspan="3" valign="top"  height="100">
					&nbsp;${problem}
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 				<th>참조파일</th> -->
<!-- 				<td colspan="3"> -->
<%-- 					<jsp:include page="/jsp/worldex/common/include/attachedFile.jsp" flush="true"> --%>
<%-- 						<jsp:param name="mode" value="view"/> --%>
<%-- 						<jsp:param name="choid" value="<%=oid %>"/> --%>
<%-- 						<jsp:param name="role" value="secondary"/> --%>
<%-- 					</jsp:include> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
			<tr>
			    <th>답변 제기자</th>
				<td>
				&nbsp;${manager}
			    </td>
			    <th>완료 예정일<font color="red">*</font></th>
				<td class="calendar">
				&nbsp;<input type="text" class="datePicker w40" name="requestDate" id="requestDate" readonly/>
<%-- 				<jsp:include page="/jsp/worldex/common/include/dateInput.jsp" flush="true"> --%>
<%-- 						<jsp:param name="dateId" value="requestDate"/> --%>
<%-- 				</jsp:include> --%>
			    </td>
			</tr>
			<tr height="150">
			    <th>해결방안<font color="red">*</font></th>
			    <td class="pd15" colspan="3">
					<div class="textarea_autoSize">
						<textarea name="solution" id="solution" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)"></textarea>
					</div>
				</td>
			</tr>
			<tr> 
               <th>${e3ps:getMessage('첨부파일')}</th>
               <td colspan="3">
                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
                     <jsp:param name="formId" value="IssueForm"/>
                     <jsp:param name="command" value="insert"/>
                     <jsp:param name="btnId" value="createBtn" />
                     <jsp:param name="oid" value="" />
                  </jsp:include>
               </td>
            </tr>
		</tbody>
	</table>
</div>
</form>
</div>