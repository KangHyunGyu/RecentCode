<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
$(document).ready(function(){
	toggleImgBinding();
	
	var viewOnly = "${viewOnly}";
	if(viewOnly === "true"){
		$("input[type=button]").attr("disabled", true);
		$("input[type=button]").attr("type", "hidden");
	}
	
})


function selectClassChange(o){
	o.classList.remove(o.classList.item(0));
	o.classList.add(o.value);
}
function toEnabled() {
    $("input[type=text]").attr("disabled", false);
    $(".qualitySelect").attr("disabled", false);
}
function toDisabled() {
    $("input[type=text]").attr("disabled", true);
    $(".qualitySelect").attr("disabled", true);
}
function modifyQuality(target){
	openConfirm("${e3ps:getMessage('저장 하시겠습니까?')}", function(){
		
		var items = [];
		var tdArr = [];
		$("#"+target+" tr").each(function(){
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
			items.push(item);
		}
		var url = getURLString("/gate/modifyQuality");
		var param = new Object();
		param["list"] = items;
		
		ajaxCallServer(url, param, function(data){
			
		}, true);
	});
	
}

function viewObject(oid){
	var viewOnly = "${viewOnly}";
	if(viewOnly === "true"){
		return false;
	}
	
	var url = getURLString("/gate/viewObject") + "?oid="+oid;
	openPopup(url,"viewStageGate","700","400");
}

function filePopup(oid){
	let url = getURLString("/gate/filePopup")+"?oid="+oid;
	openPopupCenter(url,"filePopup","550","300");
}

</script>
<div class="signal_info visibility_hidden">
    <div class="pro_table">
        <table class="mainTable" summary="검색조건">
            <caption>검색조건보기</caption>
            <colgroup>
                <col style="width:20%">
                <col style="width:20%">
                <col style="width:20%">
                <col style="width:20%">
                <col style="width:auto">
            </colgroup>
            <tbody>
                <tr>
                    <th scope="col">구분</th>
                    <th scope="col" class="tac">complant</th>
                    <th scope="col" class="tac">Unolar</th>
                    <th scope="col" class="tac">Not compllant</th>
                    <th scope="col" class="tac">but granted</th>
                </tr>
                <tr>
                    <th scope="col">GR1</th>
                    <td class="tac"><button type="button" class="s_bt010">Green</button></td>
                    <td class="tac"><button type="button" class="s_bt011">Yellow</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt011">Yellow</button></td>
                </tr>
                <tr>
                    <th scope="col">GR2</th>
                    <td class="tac"><button type="button" class="s_bt010">Green</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt011">Yellow</button></td>
                </tr>
                <tr>
                    <th scope="col">GR3</th>
                    <td class="tac"><button type="button" class="s_bt010">Green</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt011">Yellow</button></td>
                </tr>
                <tr>
                    <th scope="col">GR4</th>
                    <td class="tac"><button type="button" class="s_bt010">Green</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt011">Yellow</button></td>
                </tr>
                <tr>
                    <th scope="col">GR5</th>
                    <td class="tac"><button type="button" class="s_bt010">Green</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt011">Yellow</button></td>
                </tr>
                <tr>
                    <th scope="col">GR6</th>
                    <td class="tac"><button type="button" class="s_bt010">Green</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt09">Red</button></td>
                    <td class="tac"><button type="button" class="s_bt011">Yellow</button></td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<c:forEach var="parentData" items="${parentList}">
	<div class="seach_arm3 pl25 pt5 ">
       <div class="leftbt">
           <img class="pointer content--toggle" src="/Windchill/jsp/portal//images/minus_icon.png" data-target="${parentData.code}">
           ${parentData.name}
           <img src="/Windchill/jsp/portal//images/help_icon.png" class="help_icon">
           <input type="button" class="sm_bt03 property_update" data-target="${parentData.code}" value="${e3ps:getMessage('수정')}">
       </div>
   </div>

   <div class="pro_table pt5 pl25 pr25 ">
       <table class="mainTable ${parentData.code}" id="${parentData.code}">
           <colgroup>
               <col style="width:17%">
               <col style="width:23%">
               <col style="width:5%">
               <col style="width:5%">
               <col style="width:5%">
               <col style="width:5%">
               <col style="width:5%">
               <col style="width:5%">
               <col style="width:14%">
               <col style="width:5%">
               <col style="width:auto">
               <col style="width:3%">
           </colgroup>
           <tbody>
           	<tr>
           		<th class="tac">구분</th>
           		<th class="tac">what</th>
           		<th class="tac">1</th>
           		<th class="tac">2</th>
           		<th class="tac">3</th>
           		<th class="tac">4</th>
           		<th class="tac">5</th>
           		<th class="tac">6</th>
           		<th class="tac">Action</th>
           		<th class="tac">Who</th>
           		<th class="tac">Date</th>
           		<th class="tac">File</th>
           	</tr>
           	<c:forEach var="childData" items="${childList}">
           		<c:if test="${parentData.code eq childData.parentCode}">
           			<tr>
           				<td>
           				<input type="hidden" name="cOid" id="cOid" value="${childData.oid }">
           				<span onclick="viewObject('${childData.oid }')" >${childData.name }</span>
           				
           				</td>
           				<td><input type="text" class="textLF" disabled value="${childData.value0 }"></td>
           				<td>
	           				<c:if test="${childData.value1 eq ''}">
	           					<select disabled onchange="selectClassChange(this)" class="qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value1 eq 'signal_G'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_G qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value1 eq 'signal_Y'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_Y qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y" selected="selected">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value1 eq 'signal_R'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_R qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R" selected="selected">R</option>
		           				</select>
	           				</c:if>
           				</td>
           				<td>
	           				<c:if test="${childData.value2 eq ''}">
	           					<select disabled onchange="selectClassChange(this)" class="qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value2 eq 'signal_G'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_G qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value2 eq 'signal_Y'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_Y qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y" selected="selected">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value2 eq 'signal_R'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_R qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R" selected="selected">R</option>
		           				</select>
	           				</c:if>
           				</td>
           				<td>
	           				<c:if test="${childData.value3 eq ''}">
	           					<select disabled onchange="selectClassChange(this)" class="qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value3 eq 'signal_G'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_G qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value3 eq 'signal_Y'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_Y qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y" selected="selected">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value3 eq 'signal_R'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_R qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R" selected="selected">R</option>
		           				</select>
	           				</c:if>
           				</td>
           				<td>
	           				<c:if test="${childData.value4 eq ''}">
	           					<select disabled onchange="selectClassChange(this)" class="qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value4 eq 'signal_G'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_G qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value4 eq 'signal_Y'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_Y qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y" selected="selected">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value4 eq 'signal_R'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_R qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R" selected="selected">R</option>
		           				</select>
	           				</c:if>
           				</td>
           				<td>
	           				<c:if test="${childData.value5 eq ''}">
	           					<select disabled onchange="selectClassChange(this)" class="qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value5 eq 'signal_G'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_G qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value5 eq 'signal_Y'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_Y qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y" selected="selected">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value5 eq 'signal_R'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_R qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R" selected="selected">R</option>
		           				</select>
	           				</c:if>
           				</td>
           				<td>
	           				<c:if test="${childData.value6 eq ''}">
	           					<select disabled onchange="selectClassChange(this)" class="qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value6 eq 'signal_G'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_G qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value6 eq 'signal_Y'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_Y qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y" selected="selected">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value6 eq 'signal_R'}">
	           					<select disabled onchange="selectClassChange(this)" class="signal_R qualitySelect">
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R" selected="selected">R</option>
		           				</select>
	           				</c:if>
           				</td>
           				<td><input type="text" class="textLF" disabled value="${childData.value7 }"></td>
           				<td><input type="text" disabled value="${childData.value8 }"></td>
           				<td>
	           				<input type="text" class="datePicker" id="datepick${childData.code}" disabled value="${childData.value9 }">
	           				<img class="dateCalendar" src="/Windchill/jsp/portal/images/calendar_icon.png" name="datepick${childData.code}Btn" id="datepick${childData.code}Btn" style="">
           				</td>
           				<td>
           					 <img src="/Windchill/netmarkets/images/doc_document.gif" onclick="filePopup('${childData.oid }')">
           				</td>
           			</tr>
           		</c:if>
           	</c:forEach>
           </tbody>
       </table>
   </div>
</c:forEach>

<script src="/Windchill/jsp/stagegate/stagegateHTML/modules/quality/quality.js"></script>