<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"	%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<script src="/Windchill/jsp/stagegate/stagegateHTML/modules/gateReviews/gateReviews.js"> </script>
<script>

$(document).ready(function(){
//	toggleUpdateBtn();
	toggleImgBinding();
	
	initEditBtnDisable();
	
	var viewOnly = "${viewOnly}";
	
	if(viewOnly === "true"){
		$("button").attr("disabled", true);
		$("button").attr("hidden", true);
	}
})


function initEditBtnDisable(){
	let btn = $("button[id^='save_'");
	
	//버튼 초기화 >> 1번 버튼 제외한 모든 버튼 비활성화
	for(let i=1; i<btn.length; i++){
		btn[i].disabled = true;
	}
	
	let btnFlag = false;
	
	//1. tidx 번째 테이블의 모든 status 값을 가져옴 
	//2. 테이블 tidx의 모든 요소가 채워진 경우 (tidx+1)번째 버튼 활성화.
	for(let tidx =1; tidx < btn.length; tidx++){
		
		let sel = $("select[class *= reviews"+tidx+"]");
		
		for(let i=0; i< sel.length; i++){
			if(sel[i].classList.contains("signal_R")){
				btnFlag = true;
				break;
			}
			if(sel[i].classList.length != 3){	// If has empty status value
				btnFlag = true;
				break;
			}
		}
		if(btnFlag){
			break;
		}
		btn[tidx].disabled = false;
	}
	
}

$("button[id^='save_']").click(function(e) {
	
	var table_name = e.target.dataset.target;
	
	if(!e.target.classList.contains("property_submit")){
		toggleUpdateBtn_GR(e,"${e3ps:getMessage('저장')}");
		return;
	}
	if(!confirm("${e3ps:getMessage('저장 하시겠습니까?')}")){
		toggleUpdateBtn_GR(e, "${e3ps:getMessage('수정')}");
		return;
	}
	
 	var trList = document.querySelectorAll("#table_"+table_name +" tbody tr  input[class ^='row'],"
										  +"#table_"+table_name +" tbody tr  select[class ^='row']");
										
	var tdArr = [];
	
	//hidded : 3, text : 1, select : 1
	let numOfsaved = 5;
	
	const rowNum = trList.length / numOfsaved;
	
	let itemList = [];
	
	let nextLevel = true;
	for(let idx=0; idx < rowNum; idx++){
		let rowList = document.querySelectorAll("#table_"+table_name +" tbody tr .row" + idx);
		
		let item = new Object();
		for(let el =0;  el< 5; el ++){
			let input = rowList[el];
			item[input.name] = input.value;
			
			if(el == 3 && input.value == "" || input.value == "signal_R"){
				nextLevel = false;
			}
		}
		itemList.push(item);
	}
	
	
	var url = getURLString("/gate/modifyObjectValueList");
	var param = new Object();
	param["list"] = itemList;
	 
	ajaxCallServer(url,param,function(data){
		toggleUpdateBtn_GR(e, "${e3ps:getMessage('수정')}");
		let onTab = document.querySelector("div .tap>ul>li.on");
		loadIncludePage(onTab);
		if(nextLevel){
			var nextUrl = getURLString("/gate/revisionGate");
			var nextParam = new Object();
			nextParam["oid"] = "${oid }";
			nextParam["remark"] = table_name;
			ajaxCallServer(nextUrl,nextParam,function(data){
				if(data.gen){
					location.reload();
				}
			},true);
			
		}
	},true);
	 
});

function selectClassChange(o){
	var list = o.classList;
	let value = o.value;
	
	for(var i = 0; i< list.length ;i++){
		if(list[i].startsWith("signal_")){
			o.classList.remove(o.classList.item(i));
			break;
		}
	}
	if(o.value != "") o.classList.add(o.value);
}

function toEnabled() {
    $("input[type=text]").attr("disabled", false);
    $("select").attr("disabled", false);
}
function toDisabled() {
    $("input[type=text]").attr("disabled", true);
    $("select").attr("disabled", true);
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
// 		param = JSON.stringify(param);
		console.log(param);
		
		ajaxCallServer(url, param, function(data){
			
		}, true);
	});
}

function filePopup(oid){
	let url = getURLString("/gate/filePopup")+"?oid="+oid;
	openPopupCenter(url,"filePopup","550","300");
}


</script>



<input type="hidden" name="objType" value="${objType}">


<c:forEach var="parentData" items="${parentList}" varStatus="status">
	<div class="seach_arm3 pl25 pt5 ">
       <div class="leftbt">
           <img class="pointer content--toggle" src="/Windchill/jsp/portal//images/minus_icon.png" data-target="reviews${status.count}">
           Gate Review ${status.count} reviews
           <button class="sm_bt03 property_update" data-target="reviews${status.count}" id="save_${status.count}" >${e3ps:getMessage('수정')}</button>
       </div>
   </div>

   <div class="pro_table pt5 pl25 pr25 ">
       <table class="mainTable reviews${status.count}" id="table_reviews${status.count}" >
           <colgroup>
				<col style="width:65%">
		   		<col style="width:15%">
              	<col style="width:15%">
              	<col style="width:5%">
           </colgroup>
           <tbody>
           	<tr>
     			<th scope="col"> 실시시기 : 
	     			<c:if test = "${status.count eq 1}"> 프로젝트 수주 후 실시</c:if>
	     			<c:if test = "${status.count eq 2}"> 프로토 툴링 시작하기 전 실시</c:if>
	     			<c:if test = "${status.count eq 3}"> DV완료 후 양산 모델고정 및 양산 툴링 Kick off시 실시</c:if>
	     			<c:if test = "${status.count eq 4}"> 양산 툴링에서 제품 생산 후 실시</c:if>
	     			<c:if test = "${status.count eq 5}"> PPAP/ISIR승인 완료 후 실시 (판가도 확정되면 좋음)</c:if>
	     			<c:if test = "${status.count eq 6}"> 양산완료 후 3개월 후 (현대: 품질 100일 작전 완료 후) 실시</c:if>
     			</th>
				<th scope="col" class="tac">담당자</th>
				<th scope="col" class="tac">Status</th>
				<th scope="col" class="tac">File</th>
           	</tr>
           	
           	<!-- value 초기화   -->
            <c:set var="Rcount" value ="-1"/>
           	
           	<c:forEach var="childData" items="${childList}" >
           	
           		<c:if test="${parentData.code eq childData.parentCode}">
           		
           			<c:set var="Rcount" value ="${Rcount+1}"/>
 					<tr>
           				<td>
           					<input type="hidden" class="row${Rcount}" name="oid" id="cOid" value="${childData.oid }">
           					<input type="hidden" class="row${Rcount}" name="code" value="${childData.code}">
           					<input type="hidden" class="row${Rcount}" name="name" value="${childData.name}">
           					${childData.name }
           				</td>
           				<td rowspan="2">
           					<c:set var="manager" value="manager${status.index}"/>
           					
           					<c:forEach var ="mng" items="${requestScope[manager]}" varStatus="stat">
 								<c:if test="${Rcount eq stat.index}">
    	       						<div class="textCenter">${mng}</div>
           						</c:if>
           					</c:forEach>
          			 	</td>
           				
           			 	<td rowspan="2">
	           				<c:if test="${childData.value0 eq ''}">
	           					<select class="row${Rcount} reviews${status.count}" name ="value0" disabled onchange="selectClassChange(this)">
	           					<option class="none" value="" ></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value0 eq 'signal_G'}">
	           					<select class="row${Rcount} reviews${status.count} signal_G " name ="value0" disabled onchange="selectClassChange(this)" >
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G" selected="selected">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value0 eq 'signal_Y'}">
	           					<select class="row${Rcount} reviews${status.count} signal_Y "  name ="value0" disabled onchange="selectClassChange(this)" >
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y" selected="selected">Y</option>
		           				<option class="signal_R" value="signal_R">R</option>
		           				</select>
	           				</c:if>
	           				<c:if test="${childData.value0 eq 'signal_R'}">
	           					<select class="row${Rcount} reviews${status.count} signal_R " name ="value0" disabled onchange="selectClassChange(this)" >
	           					<option class="none" value=""></option>
	           					<option class="signal_G" value="signal_G">G</option>
		           				<option class="signal_Y" value="signal_Y">Y</option>
		           				<option class="signal_R" value="signal_R" selected="selected">R</option>
		           				</select>
	           				</c:if>
           				</td>
           			</tr>
           			<tr>
           				<td>
           				 	<c:if test="${childData.value1 ne ''}">
	           					<input type="text" class="row${Rcount} textLFN noticeText " name="value1" 
	           					value ="${childData.value1}" disabled>
           				 	</c:if>
           				 	<c:if test="${childData.value1 eq ''}">
           						<input type="text" class="row${Rcount} textLFN noticeText " name="value1" disabled>
           				 	</c:if>
           				 	<td>
	           					<img src="/Windchill/netmarkets/images/doc_document.gif" onclick="filePopup('${childData.oid }')">
	           				</td>
           				</td>
           			</tr>
           			
           		 </c:if>
           		
           	</c:forEach>
           </tbody>
       </table>
   </div>

</c:forEach>
