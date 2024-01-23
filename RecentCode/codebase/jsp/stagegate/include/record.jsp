<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
function viewRecord(oid){
	var url = getURLString("/gate/viewRecord") + "?oid="+oid;
	openPopup(url,"viewStageGate","1200","600");
}
</script>
<div class="pro_table pt5 pl25 pr25">
	<div class="objectives">
		<div style="display: flex; border-left: none; border-bottom: none; border-right: none; ">
			<table class="mainTable recordTable" style="width: 100%; margin-left:0">
				<colgroup>
					<col style="width:40%">
					<col style="width:20%">
					<col style="width:20%">
					<col style="width:20%">
				</colgroup>
				<thead>
					<tr>
						<th colspan="4" class="tac">
							이력 관리
						</th>
					</tr>
					<tr>
						<th class="tac">
							${e3ps:getMessage('단계')}
						</th>
						<th class="tac">
							${e3ps:getMessage('버전')}
						</th>
						<th class="tac">
							${e3ps:getMessage('등록일')}
						</th>
						<th class="tac">
							${e3ps:getMessage('등록자')}
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="data" items="${list}" varStatus="loop">
						<tr>
							<td><a href='#' onclick="viewRecord('${data.oid }')">${data.remark }</a></td>
							<td>${data.version }</td>
							<td>${data.createDate }</td>
							<td>${data.creator }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
