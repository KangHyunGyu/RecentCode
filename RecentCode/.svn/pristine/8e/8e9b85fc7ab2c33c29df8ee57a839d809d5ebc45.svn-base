<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<!-- HTML -->
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/stagegate/stagegateHTML/css/custom.css">
<link rel="stylesheet" type="text/css" href="/Windchill/jsp/stagegate/stagegateHTML/css/style.css">
<script src="/Windchill/jsp/stagegate/stagegateHTML/index.js"></script>
<!-- Excel Export -->
<script src="/Windchill/jsp/stagegate/stagegateHTML/js/excelExport/FileSaver.min.js"></script>
<script src="/Windchill/jsp/stagegate/stagegateHTML/js/excelExport/xlsx.full.min.js"></script>
<script src="/Windchill/jsp/stagegate/stagegateHTML/js/excelExport/exportExcel.js"></script>
<!-- numeraljs -->
<script src="/Windchill/jsp/stagegate/stagegateHTML/js/numeraljs/numeral.min.js"></script>

<script type=text/javascript>
$(document).ready(function(){
	
	loadIncludePage();
	
	var viewOnly = "${viewOnly}";
	if(viewOnly === "true"){
		$("select").attr("disabled", true);
	}
});
function loadIncludePage(tab) {
	if(tab == null) {
		tab = $(".tap>ul>li:first");
	}
	
	$(".tap ul li").removeClass("on");
	
	$(tab).addClass("on");
	
	var url = $(tab).data("url");
	var param = $(tab).data("param");
	
	if(param == null) {
		param = new Object();
	}
	param["oid"] = "${data.oid}";
	console.log(url, param);
	$("#includePage").load(url, param);
}

function selectGate(o){
	o.classList.remove(o.classList.item(0));
	o.classList.add(o.value);
	modifyLight();
}

function modifyLight(){
	var items = [];
	var tdArr = [];
	$("#lightTable tr").each(function(){
		tdArr.push($(this).children());
	});
	for(var i=1; tdArr.length > i; i++){
		var item = [];
		item[0] = tdArr[i].eq(0).children().val();
		item[1] = tdArr[i].eq(1).children().val();
		item[2] = tdArr[i].eq(2).children().val();
		item[3] = tdArr[i].eq(3).children().val();
		item[4] = tdArr[i].eq(4).children().val();
		item[5] = tdArr[i].eq(5).children().val();
		item[6] = tdArr[i].eq(6).children().val();
		item[7] = tdArr[i].eq(7).children().val();
		item[8] = tdArr[i].eq(8).children().val();
		item[9] = tdArr[i].eq(9).children().val();
		item[10] = tdArr[i].eq(10).children().val();
		item[11] = tdArr[i].eq(11).children().val();
		item[12] = tdArr[i].eq(12).children().val();
		item[13] = tdArr[i].eq(13).children().val();
		item[14] = tdArr[i].eq(14).children().val();
		item[15] = tdArr[i].eq(15).children().val();
		item[16] = tdArr[i].eq(16).children().val();
		item[17] = tdArr[i].eq(17).children().val();
		items.push(item);
	}
	var url = getURLString("/gate/modifyLight");
	var param = new Object();
	param["list"] = items;
// 	param = JSON.stringify(param);

	ajaxCallServer(url, param);
	
}

function deleteStageGate() {
	openConfirm("${e3ps:getMessage('삭제하시겠습니까?')}", function(){
		let param = new Object();
		param.oid = "${data.oid}";
		let url = getURLString("/gate/deleteGateAction");
		ajaxCallServer(url, param, function(data){
			self.close();
		}, true);
	});
}
</script>
<body>

	<!-- pop -->
	<div class="pop">
		<input type="hidden" name="oid" value="${data.oid}">
		<!-- top -->
		<div class="top">
			<h2>
				Stage-Gate Dashboard – ${pData.code }
			</h2>
			
			<c:if test="${isAuth eq true}">
				<input type="button" class="sm_bt03" value="${e3ps:getMessage('삭제')}" 
				readonly="readonly" onclick="deleteStageGate();" style="position: absolute; text-align: right; right: 50px;">
            </c:if>

			<span class="close">
               <a onclick="self.close();">
					<img src="/Windchill/jsp/portal/images/colse_bt.png">
				</a>
			</span>
			
		</div>
		<!-- //top -->

		<div class="pop_nav">
			▶ 관련 프로젝트: ${pData.code } / ${pData.name }
		</div>

		<!--tap -->
		<div class="tap pt15">
			<ul>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=SUMMARY&viewOnly=${viewOnly}">${e3ps:getMessage('Summary')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=TIMING&viewOnly=${viewOnly}">${e3ps:getMessage('Timing')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=QUALITY&viewOnly=${viewOnly}">${e3ps:getMessage('Quality')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=CSTOP&viewOnly=${viewOnly}">${e3ps:getMessage('C_Stop')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=RISK&viewOnly=${viewOnly}">${e3ps:getMessage('Risk')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=DS&viewOnly=${viewOnly}">${e3ps:getMessage('Deliverable Status')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=REVIEWS&viewOnly=${viewOnly}">${e3ps:getMessage('Gate Reviews')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=PRODUCTDEV&viewOnly=${viewOnly}">${e3ps:getMessage('Product Dev.')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=ECR&viewOnly=${viewOnly}">${e3ps:getMessage('ECR')}</li>
				<li onclick="loadIncludePage(this);" data-url="${e3ps:getURLString('/gate/include_tab')}?objType=RECORD&viewOnly=${viewOnly}">${e3ps:getMessage('이력')}</li>
			</ul>

			<div class="tapbutton">
				<table id="lightTable">
					<tr>
						<th colspan="3" style="font-size:12px">1</th>
						<th colspan="3" style="font-size:12px">2</th>
						<th colspan="3" style="font-size:12px">3</th>
						<th colspan="3" style="font-size:12px">4</th>
						<th colspan="3" style="font-size:12px">5</th>
						<th colspan="3" style="font-size:12px">6</th>
					</tr>
					<tbody>
					<tr>
						<c:forEach var="data" items="${lightList}">
						<td><input type="hidden" name="cOid" id="cOid" value="${data.oid }"></td>
						<td>
						
	           				<c:if test="${data.value1 eq ''}">
	           					<select onchange="selectGate(this)" style="width: 15px; height:30px; border-right-width: 0;">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G"></option>
		           				<option class="signal_Y" value="signal_Y"></option>
		           				<option class="signal_R" value="signal_R"></option>
		           				</select>
	           				</c:if>
	           				<c:if test="${data.value1 eq 'signal_G'}">
	           					<select onchange="selectGate(this)" class="signal_G" style="width: 15px; height:30px; border-right-width: 0;">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected"></option>
		           				<option class="signal_Y" value="signal_Y"></option>
		           				<option class="signal_R" value="signal_R"></option>
		           				</select>
	           				</c:if>
	           				<c:if test="${data.value1 eq 'signal_Y'}">
	           					<select onchange="selectGate(this)" class="signal_Y" style="width: 15px; height:30px; border-right-width: 0;">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G"></option>
		           				<option class="signal_Y" value="signal_Y" selected="selected"></option>
		           				<option class="signal_R" value="signal_R"></option>
		           				</select>
	           				</c:if>
	           				<c:if test="${data.value1 eq 'signal_R'}">
	           					<select onchange="selectGate(this)" class="signal_R" style="width: 15px; height:30px; border-right-width: 0;">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G"></option>
		           				<option class="signal_Y" value="signal_Y"></option>
		           				<option class="signal_R" value="signal_R" selected="selected"></option>
		           				</select>
	           				</c:if>
           				</td>
           				<td>
	           				<c:if test="${data.value2 eq ''}">
	           					<select onchange="selectGate(this)" style="width: 15px; height:30px; border-left-width: 0; margin-right: 5px;">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G"></option>
		           				<option class="signal_Y" value="signal_Y"></option>
		           				<option class="signal_R" value="signal_R"></option>
		           				</select>
	           				</c:if>
	           				<c:if test="${data.value2 eq 'signal_G'}">
	           					<select onchange="selectGate(this)" class="signal_G" style="width: 15px; height:30px; border-left-width: 0; margin-right: 5px;">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected"></option>
		           				<option class="signal_Y" value="signal_Y"></option>
		           				<option class="signal_R" value="signal_R"></option>
		           				</select>
	           				</c:if>
	           				<c:if test="${data.value2 eq 'signal_Y'}">
	           					<select onchange="selectGate(this)" class="signal_Y" style="width: 15px; height:30px; border-left-width: 0; margin-right: 5px;">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G"></option>
		           				<option class="signal_Y" value="signal_Y" selected="selected"></option>
		           				<option class="signal_R" value="signal_R"></option>
		           				</select>
	           				</c:if>
	           				<c:if test="${data.value2 eq 'signal_R'}">
	           					<select onchange="selectGate(this)" class="signal_R" style="width: 15px; height:30px; border-left-width: 0; margin-right: 5px;">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G"></option>
		           				<option class="signal_Y" value="signal_Y"></option>
		           				<option class="signal_R" value="signal_R" selected="selected"></option>
		           				</select>
	           				</c:if>
           				</td>
           				
						</c:forEach>
					</tr>
					</tbody>
				</table>
				
			</div>
		</div>
		<div id="refreshDiv">
			<div class="con" id="includePage">
			</div>
			<br>
		</div>
		
		<!--//tap -->
	</div>
	
</body>

