<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	
	getMaterialList();
	
	getNumberCodeList("PartType", "PARTTYPE", false, true);
	
	getUnitList();
	
	//팝업 리사이즈
	popupResize();
	
});

function modify(){
	
	if(!checkValidate()) {
		return;
	}
	
	$("#partForm").attr("action",getURLString("/part/modifyPartAttributeAction"));
	
	var param = new Object();
	
	formSubmit("partForm", param, "${e3ps:getMessage('수정하시겠습니까?')}",function(){
		if(opener.window.search) {
			opener.window.search();
		}
	}, true);
}

function checkValidate() {
	
	/* if($("#name").val() == null || $("#name").val() == "") {
		openNotice("${e3ps:getMessage('부품명을 입력하여 주십시오.')}");
		return false;
	} */
	
	/* if($("#unit").val() == null || $("#unit").val() == "") {
		openNotice("${e3ps:getMessage('단위를 선택하여 주십시오.')}");
		return false;
	} */
	
	return true;
}
</script>
<!-- pop -->
<div class="pop">
	<!-- top -->
	<div class="top">
		<h2>${e3ps:getMessage('부품 속성 수정')}</h2>
		<span class="close"><a href="javascript:window.close()"><img src="/Windchill/jsp/portal/images/colse_bt.png" alt="닫기"></a></span>
	</div>
	<!-- //top -->

	<div class="pl25 pr25">
		<div class="seach_arm2 pt10 pb10">
			<div class="leftbt"></div>
			<div class="rightbt">
				<button type="button" class="s_bt03" onclick="javascript:modify()">${e3ps:getMessage('수정')}</button>
				<button type="button" class="s_bt03" onclick="javascript:history.back()">${e3ps:getMessage('뒤로가기')}</button>
				<button type="button" class="s_bt04" onclick="javascript:window.close()">${e3ps:getMessage('닫기')}</button>
			</div>
		</div>
		<form name="partForm" id="partForm" method="post">
			<!-- <input type="hidden" id="location" name="location" value=""> -->
			<input type="hidden" name="oid"  id="oid" value="${part.oid}" />
			<div class="pro_table">
				<table class="mainTable">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>	
					<tbody>
						<tr>
							<th>${e3ps:getMessage('부품 번호')}</th>
							<td>
								${part.number}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('부품 명')}</th>
							<td>
								${part.name}
							</td>
						</tr>
						<tr>
							<th>${e3ps:getMessage('단위')}</th>
							<td>
								${part.unit}
							</td>
						</tr>
					</tbody>
				</table>
				
				<!-- button -->
				<div class="seach_arm2 pt20 pb5">
					<div class="leftbt">
						<h4>${e3ps:getMessage('부품 속성')}</h4>
					</div>
					<div class="rightbt">
					</div>
				</div>
				<!-- //button -->
				<div class="pro_table">
					<table class="mainTable">
						<colgroup>
							<col style="width:20%">
							<col style="width:80%">
						</colgroup>	
						<tbody>
							<tr>
								<th>${e3ps:getMessage('종료일자')}</th>
								<td class="calendar">
									<input type="text" class="datePicker w25" name="ENDDATE" id="ENDDATE" value="${part.attributes.ENDDATE}" readonly/>
								</td>
							</tr>
							<tr>
								<th>${e3ps:getMessage('개요')}</th>
								<td colspan="3" class="pd15">
									<div class="textarea_autoSize">
										<textarea name="Summary" id="Summary" onkeydown="textAreaResize(this)" onkeyup="textAreaResize(this)">${part.attributes.Summary}</textarea>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</form>
	</div>
</div>		
<!-- //pop-->
