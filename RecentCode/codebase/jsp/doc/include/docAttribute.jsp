<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
$(document).ready(function(){
	 $(".datePicker_include").each(function(){
		var id = $(this).attr("id");

		$(this).after("<img class='pointer vm' src='/Windchill/jsp/portal/images/calendar_icon.png' name='" + id + "Btn' id='" + id + "Btn'>");
		
		$("#" + id + "Btn").after("<span class='resetDate pointer' data-remove-target='" + id + "'><img class='vm' src='/Windchill/jsp/portal/images/delete_icon.png'></span>");
        
		$(".resetDate").click(function(){
			var target = $(this).data("remove-target");
			$("#" + target).val("");
		});
		
		var myCalendar = new dhtmlXCalendarObject({
			input:id,
			button:id+"Btn"
		});
		
		myCalendar.setDateFormat("%Y/%m/%d");
		myCalendar.setWeekStartDay(7);
	});
	if("${mode}"=='search'){
		$(".attribute table").css("border-top","none");		 
		$(".attribute").removeClass("mr30 ml30");		 
		var length = ${fn:length(list)};
		if(length > 0){	// 상세검색 버튼 활성/비활성
			$("#switchDetailBtn").attr("class", "s_bt03");
		}else{
			$("#switchDetailBtn").attr("class", "s_bt04 cursorDf");
		}
	 }
	
	// PROCESSDIVISIONCODE endlevel=3
	$("[id=PROCESSDIVISIONCODE]").attr("data-endlevel", "3");
	//공통 : numberCode autocomplete
	$(".searchCode").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/common/getNumberCodeListAutoComplete");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["value"] = params.term;
					param["codeType"] = $(this).data("codetype");
					param["endLevel"] = $(this).data("endlevel");
					
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			              return { id: item.code, text: item.code  +" : " + item.name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Code Info (CODE or NAME)",
			minimumInputLength: 1,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
});
function openCodePopup2(id, codeType) {
	
	var code = $("#" + id).val();
	
	var url = getURLString("/common/openCodePopup") + "?codeType=" + codeType + "&id=" + id + "&code=" + code;
	
	openPopup(url,"openCode", 1000, 600);
}
</script>
<!-- create 이고 list 0개 이상일 때 실행 -->
<c:if test="${mode == 'input' && fn:length(list) != 0}">
<div class="seach_arm pt10 pb5">
	<div class="leftbt">
		<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('속성')}</span>
	</div>
	<div class="rightbt">
	</div>
</div>
</c:if>
<!-- create 또는 search 화면일 때 실행 -->
<c:if test="${mode == 'input' || mode == 'search'}">
 <!-- pro_table -->
<div class="pro_table mr30 ml30 attribute">
	<table class="mainTable">
		<colgroup>
			<col style="width:13%">
			<col style="width:37%">
			<col style="width:13%">
			<col style="width:37%">
		</colgroup>	
		<tbody>
			<c:set var="num" value="1"/>
			<c:forEach items="${list}" var="attributeList" varStatus="index">
			<c:if test="${num%2==1 && mode=='search'}"> <!-- search일 때 tr 열기 -->
				<tr class="switchDetail">
			</c:if>
			<c:if test="${num%2==1 && mode=='input'}"> <!-- input일 때 tr 열기 -->
				<tr>
			</c:if>
				<c:if test="${attributeList.inputType == 'SELECT'}">	<!-- InputType Select 일 때  -->
					<th>${attributeList.name}</th>
					<td>
						<select class="searchCode" id="${attributeList.numberCodeType}" name="${attributeList.numberCodeType}" data-codetype="${attributeList.numberCodeType}" data-endlevel="1" data-width="70%"></select>
						<span class="pointer verticalMiddle" onclick="javascript:openCodePopup2('${attributeList.numberCodeType}', '${attributeList.numberCodeType}');"><img class="vm" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
						<span class="pointer verticalMiddle" onclick="javascript:deleteCode('${attributeList.numberCodeType}');"><img class="vm" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
					</td>
				</c:if>
				<c:if test="${attributeList.inputType == 'DATE'}">	<!-- InputType Date 일 때  -->
					<th>${attributeList.name}</th>
					<td>
				        <input type="text" class="datePicker_include w50" name="${attributeList.code}" id="${attributeList.code}" readonly/>
					</td>
				</c:if>
				<c:if test="${attributeList.inputType == 'TEXT'}">	<!-- InputType Text 일 때  -->
					<th>${attributeList.name}</th>
					<td>
						<input type="text" class="w100" id="${attributeList.code}" name="${attributeList.code}">
					</td>
				</c:if>
			<c:if test="${num%2==1 && index.last}">	<!-- 홀수이고, 마지막 index일 때 -->
				<td colspan="2"></td>
			</c:if>
			<c:if test="${num%2==0}">	<!-- 속성 2개 th,td 들어갔을 때 tr 닫기 -->
				</tr>
			</c:if>
			<c:set var="num" value="${num+1}" />
			</c:forEach>
		</tbody>
	</table>
</div>
</c:if>

<!-- view 화면일 때 실행 -->
<c:if test="${mode == 'view' && fn:length(list) != 0}">
<!-- button -->
<div class="seach_arm2 pt10 pb10">
	<div class="leftbt"><h4><img class="pointer" onclick="switchPopupDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('속성')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- //button -->
<div class="pro_table">
<table class="mainTable">
	<colgroup>
		<col style="width:20%">
		<col style="width:30%">
		<col style="width:20%">
		<col style="width:30%">
	</colgroup>	
	<tbody>
		<c:set var="num" value="1"/>
		<c:forEach items="${list}" var="attributeList" varStatus="index">
			<c:set var="code" value="${attributeList.code}"/>
			<c:set var="codeName" value="${attributeList.code}Name"/>
			<c:if test="${num%2==1}"> <!-- tr 열기 -->
				<tr>
			</c:if>
			<c:if test="${attributeList.inputType == 'SELECT'}">	<!-- InputType Select 일 때  -->
				<th>${attributeList.name}</th>
				<td>${doc[codeName]}</td>
			</c:if>
			<c:if test="${attributeList.inputType != 'SELECT'}">	<!-- InputType Select 아닐 때  -->
				<th>${attributeList.name}</th>
				<td>${doc[code]}</td>
			</c:if>
			<c:if test="${num%2==1 && index.last}">	<!-- 홀수이고, 마지막 index일 때 -->
				<td colspan="2"></td>
			</c:if>
			<c:if test="${num%2==0}">	<!-- 속성 2개 th,td 들어갔을 때 tr 닫기 -->
				</tr>
			</c:if>
			<c:set var="num" value="${num+1}" />
		</c:forEach>
	</table>
</div>
</c:if>

<!-- modify 화면일 때 실행 -->
<c:if test="${mode == 'modify' && fn:length(list) != 0}">
<!-- button -->
<div class="seach_arm2 pb10 pt10">
	<div class="leftbt"><h4>${e3ps:getMessage('속성')}</h4></div>
	<div class="rightbt"></div>
</div>
<!-- //button -->
<div class="pro_table">
<table class="mainTable">
	<colgroup>
		<col style="width:20%">
		<col style="width:30%">
		<col style="width:20%">
		<col style="width:30%">
	</colgroup>	
	<tbody>
		<c:set var="num" value="1"/>
		<c:forEach items="${list}" var="attributeList" varStatus="index">
		<c:set var="code" value="${attributeList.code}"/>
		<c:set var="codeName" value="${attributeList.code}Name"/>
		<c:if test="${num%2==1}"> <!-- tr 열기 -->
			<tr>
		</c:if>
			<c:if test="${attributeList.inputType == 'SELECT'}">	<!-- InputType Select 일 때  -->
				<th>${attributeList.name}</th>
				<td>
					<select class="searchCode" id="${attributeList.numberCodeType}" name="${attributeList.numberCodeType}" data-codetype="${attributeList.numberCodeType}" data-endlevel="1" data-width="70%">
						<option value="${doc[code]}" >${doc[code]} : ${doc[codeName]}</option>
					</select>
					<span class="pointer verticalMiddle" onclick="javascript:openCodePopup('${attributeList.numberCodeType}', '${attributeList.numberCodeType}');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
					<span class="pointer verticalMiddle" onclick="javascript:deleteCode('${attributeList.numberCodeType}');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
				</td>
			</c:if>
			<c:if test="${attributeList.inputType == 'DATE'}">	<!-- InputType Date 일 때  -->
				<th>${attributeList.name}</th>
				<td>
			        <input type="text" class="datePicker_include w50" name="${attributeList.code}" id="${attributeList.code}" value="${doc[code]}" readonly/>
				</td>
			</c:if>
			<c:if test="${attributeList.inputType == 'TEXT'}">	<!-- InputType Text 일 때  -->
				<th>${attributeList.name}</th>
				<td>
					<input type="text" class="w100" id="${attributeList.code}" name="${attributeList.code}" value="${doc[code]}">
				</td>
			</c:if>
		<c:if test="${num%2==1 && index.last}">	<!-- 홀수이고, 마지막 index일 때 -->
			<td colspan="2"></td>
		</c:if>
		<c:if test="${num%2==0}">	<!-- 속성 2개 th,td 들어갔을 때 tr 닫기 -->
			</tr>
		</c:if>
		<c:set var="num" value="${num+1}" />
		</c:forEach>
	</table>
</div>
</c:if>