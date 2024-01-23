<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script type="text/javascript">
$(document).ready(function(){
	
});

function search() {
		
	var param = new Object();
	
	param.oid = $("#oid").val();
	
	var url = getURLString("/admin/searchObjectAction");
	ajaxCallServer(url, param, function(data){
		if(data.msg == null) {
			$(".switch").show();
			setObjectData(data.data);
		}
	}, true);
}

function reassign() {
	openConfirm("${e3ps:getMessage('Reassign하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = $("#targetOid").val();
		param.lifecycle = $("#lifecycle").val();
		
		var url = getURLString("/admin/reassignObjectAction");
		ajaxCallServer(url, param, function(data){
			getLifecycleList(data.data.lifecycle);
		}, true);
	});
}

function changeState() {
	openConfirm("${e3ps:getMessage('상태를 변경하시겠습니까?')}", function(){
		
		var param = new Object();
		
		param.oid = $("#targetOid").val();
		param.state = $("#state").val();
		param.terminate = $("#terminate").val();
		
		var url = getURLString("/admin/changeStateAction");
		ajaxCallServer(url, param, function(data){
		}, true);
	});
}

function setObjectData(data) {
	var lifecycle = data.lifecycle;
	$("#lifecycle").val(lifecycle);
	$("#targetOid").val(data.targetOid);
	getLifecycleList(lifecycle);
	$("#state").val(data.state);
	
	var content = data.content;
	if(content.length > 0 ) {
		$("#content").html("");
		for(var i=0; i<content.length; i++) {
			var html = "";
			html += "<a target=blank href='" + content[i].url + "'>";
			html += content[i].name;
			html += "</a>";
			
			if(i < (content.length-1)) {
				html += "<br>";	
			}
			
			$("#content").append(html);
		}
	}
}
</script>
<div class="product"> 
<form name="changeObjectForm" id="changeObjectForm" method="post">
	<input type="hidden" id="targetOid" name="targetOid" value="">
	<!-- button -->
	<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title required">* Caution</span>
		</div>
		<div class="rightbt">
		</div>
	</div>
	<!-- //button -->
	<div class="pro_table mr30 ml30">
		<table class="mainTable">
			<colgroup>
				<col style="width:20%">
				<col style="width:80%">
			</colgroup>	
			<tbody>
				<tr>
					<th>OID</th>
					<td>
						<input type="text" class="w60" id="oid" name="oid">
						<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
					</td>
				</tr>
				<tr class="switch" style="display:none;">
					<th>LifeCycleTemplate</th>
					<td>
						<input type="text" class="w60" id="lifecycle" name="lifecycle">
						<button type="button" class="s_bt03" onclick="reassign();">Reassign</button>
					</td>
				</tr>
				<tr class="switch" style="display:none;">
					<th>STATE</th>
					<td>
						<select class="w30" id="state" name="state">
						</select>
						<input type="radio" name="terminate" id="terminate" value="false" checked>FALSE&nbsp;
						<input type="radio" name="terminate" id="terminate" value="true">TRUE&nbsp;
						<button type="button" class="s_bt03" onclick="changeState();">${e3ps:getMessage('상태변경')}</button>
					</td>
				</tr>
				<tr class="switch" style="display:none;">
					<th>첨부파일</th>
					<td id="content">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</form>
</div>
