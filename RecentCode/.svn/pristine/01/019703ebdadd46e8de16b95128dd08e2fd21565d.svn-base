<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="pro_table pt5 pl25 pr25">
	<div class="objectives">
		<div style="display: flex; border-left: none; border-bottom: none; border-right: none; ">
			<table class="mainTable ecrTable" style="width: 100%; margin-left:0">
				<colgroup>
					<col style="width:15%">
					<col style="width:auto">
					<col style="width:9%">
					<col style="width:9%">
					<col style="width:9%">
					<col style="width:9%">
					<col style="width:9%">
					<col style="width:9%">
					<col style="width:9%">
				</colgroup>
				<thead>
					<tr>
						<th colspan="9" class="tac">
							ECR 리스트
						</th>
					</tr>
					<tr>
						<th class="tac">
							${e3ps:getMessage('ECR 번호')}
						</th>
						<th class="tac">
							${e3ps:getMessage('ECR 제목')}
						</th>
						<th class="tac">
							${e3ps:getMessage('차종')}
						</th>
						<th class="tac">
							${e3ps:getMessage('내역')}
						</th>
						<th class="tac">
							${e3ps:getMessage('주관')}
						</th>
						<th class="tac">
							${e3ps:getMessage('작업현황')}
						</th>
						<th class="tac">
							${e3ps:getMessage('등록일')}
						</th>
						<th class="tac">
							${e3ps:getMessage('수정일')}
						</th>
						<th class="tac">
							${e3ps:getMessage('등록자')}
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="data" items="${list}" varStatus="loop">
						<tr>
							<td>${data.orderNumber }</td>
							<td><a href='#' onclick="openView('${data.oid }')">${data.name }</a></td>
							<td>${data.carType }</td>
							<td>${data.changeDesc }</td>
							<td>${data.changeOwner }</td>
							<td>${data.state }</td>
							<td>${data.createDate }</td>
							<td>${data.updateDate }</td>
							<td>${data.tempcreator }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
