<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>

<div class="product">
	<!-- button -->
	<div class="seach_arm pt10 pb5">
		<div class="leftbt">
			<img class="pointer mb5" onclick="switchSearchMenu(this);" src="/Windchill/jsp/portal/images/minus_icon.png">
		</div>
		<div class="rightbt">
			<button type="button" class="s_bt03" id="switchDetailBtn" onclick="switchDetailBtn();">${e3ps:getMessage('상세검색')}</button>
			<button type="button" class="s_bt03" id="searchBtn" onclick="search();">${e3ps:getMessage('검색')}</button>
			<button type="button" class="s_bt05" id="resetBtn" onclick="reset();">${e3ps:getMessage('초기화')}</button>
		</div>
	</div>
	<!-- //button -->
	<!-- pro_table -->
	<div class="pro_table mr30 ml30">
		<form name="searchForm" id="searchForm" style="margin-bottom: 0px;">
		<input type="hidden" name="callbackName" value="${param.callbackName}"/>
		<textarea style="display:none" name="callbackObj">${param.callbackObj}</textarea>
			<input type="hidden" id="documentOid" name="documentOid" value="">
			<input type="hidden" id="location" name="location" value="">
			<input type="hidden" id="sessionId" name="sessionId" value="">
			<input type="hidden" id="mode" name="mode" value="search">
			<input type="hidden" id="cFolderOid" name="cFolderOid" value="">
			<input type="hidden" id="cFolderPath" name="cFolderPath" value="">
			<table class="mainTable">
				<colgroup>
					<col style="width:13%">
					<col style="width:37%">
					<col style="width:13%">
					<col style="width:37%">
				</colgroup>	
				<tbody>
					<tr>
						<th scope="col">${e3ps:getMessage('제목')}</th>
						<td><input type="text" id="oldcarName" name="oldcarName" class="w95"></td>
						<th scope="col">${e3ps:getMessage('번호')}</th>
						<td><input type="text" id="oldcarNumber" name="oldcarNumber" class="w95"></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('발생일')}</th>
						<td class="calendar">
							<input type="text" class="datePicker w25" id="preOccurrenceDate" name="preOccurrenceDate" readonly>
							~
							<input type="text" class="datePicker w25" id="postOccurrenceDate" name="postOccurrenceDate" readonly>
						</td>
						<th scope="col">${e3ps:getMessage('최종수정일')}</th>
						<td class="calendar">
					        <input type="text" class="datePicker w25" name="predate" id="predate" readonly/>
							~
							<input type="text" class="datePicker w25" name="postdate" id="postdate" readonly/>
						</td>
						
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('차종')}</th>
						<td><select id="occurrenceModel" name="occurrenceModel" class="w50"></select></td>
						<th scope="col">${e3ps:getMessage('발생시점')}</th>
						<td><select id="occurrenceStep" name="occurrenceStep" class="w50"></select></td>
					</tr>
					<tr>
						<th scope="col">${e3ps:getMessage('발생장소')}</th>
						<td><select id="occurrencePlace" name="occurrencePlace" class="w50"></select></td>
						<th scope="col">${e3ps:getMessage('처리상태')}</th>
						<td><select id="oldcarStateS" name="oldcarStateS" class="w50"></select></td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('작성자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="creator" name="creator" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('creator', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('creator');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
						<th scope="col">${e3ps:getMessage('시행자')}</th>
						<td>
							<div class="pro_view">
								<select class="searchUser" id="worker" name="worker" multiple data-width="60%">
								</select>
								<span class="pointer verticalMiddle" onclick="javascript:openUserPopup('worker', 'multi');"><img class="verticalMiddle" src="/Windchill/jsp/portal/images/search_icon2.png"></span>
								<span class="pointer verticalMiddle" onclick="javascript:deleteUser('worker');"><img class="verticalMiddle" src='/Windchill/jsp/portal/images/delete_icon.png'></span>
							</div>
						</td>
					</tr>	
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('관련 부품')}</th>
						<td>
						<div class="pro_view">
							<select class="searchRelatedObject" id="relatedPart" name="relatedPart" multiple data-param="part"></select>
						</div>
						</td>
						<th scope="col">${e3ps:getMessage('버전')}</th>
						<td>
							<input type="radio" id="version" name="version" value="new" checked>
							<label>${e3ps:getMessage('최신 버전')}</label>
							<input type="radio" id="version" name="version" value="all">
							<label>${e3ps:getMessage('모든 버전')}</label>
						</td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('문제공정')}</th>
						<td colspan="3"><input type="text" id="problemProcess" name="problemProcess" class="w95"></td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('문제내용')}</th>
						<td colspan="3"><input type="text" id="problemContent" name="problemContent" class="w95"></td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('문제점 및 원인')}</th>
						<td colspan="3"><input type="text" id="cause" name="cause" class="w95"></td>
					</tr>
					<tr class="switchDetail">
						<th scope="col">${e3ps:getMessage('개선대책 및 처리내용')}</th>
						<td colspan="3"><input type="text" id="improve" name="improve" class="w95"></td>
					</tr>
				</tbody>
			</table>
	</form>	
	</div>
	<!-- //pro_table -->
	<!-- button -->
	<div class="seach_arm pt5 pb5">
		<div class="leftbt">
			<span>
				${e3ps:getMessage('검색 결과')} (<span id="total">0</span>)
			</span>
		</div>
		<div class="rightbt">
			<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0"/>
		</div>
	</div>
	<!-- //button -->
	<!-- table list-->
	<div class="table_list">
		<div id="omBarChartDiv" style="float:left; width:49%;height:250px;"></div>
		<div id="osBarChartDiv" style="float:left; width:49%;height:250px;"></div>
	</div>
	<div class="table_list">
		<div id="opBarChartDiv" style="float:left; width:49%;height:250px;"></div>
		<div id="ocBarChartDiv" style="float:left; width:49%;height:250px;"></div>
	</div>
	<!-- //table list-->
</div>
 
<script>

webix.ui({
    view:"chart",
    id:"omBarChartDiv",
    container:"omBarChartDiv",
    type:"bar",
    value:"#count#",
    label:function(obj){
    	var html = obj.count;
        html += " - <b>"+obj.percentage+"%</b>";
        return html
     },
    shadow:0,
    preset: "alpha",//preset: "stick",
    xAxis:{
        template:"#codeName#",
        title:"<b style='color: chocolate;'>${e3ps:getMessage('차종')}</b>"
    },
    yAxis:{
      start:0,
      step:1,
      template:function(obj){
        return obj%2 ? "" :obj;
      }
    },
    tooltip:{
      template: "#codeName#"
    },
});

webix.ui({
    view:"chart",
    id:"osBarChartDiv",
    container:"osBarChartDiv",
    type:"bar",
    value:"#count#",
    label:function(obj){
    	console.log(obj);
    	var html = obj.count;
        html += " - <b>"+obj.percentage+"%</b>";
        return html
     },
    shadow:0,
    preset: "alpha",//preset: "stick",
    xAxis:{
        template:"#codeName#",
        title:"<b style='color: chocolate;'>${e3ps:getMessage('발생시점')}</b>"
    },
    yAxis:{
      start:0,
      step:1,
      template:function(obj){
        return obj%2 ? "" :obj;
      }
    },
    tooltip:{
      template: "#codeName#"
    },
});

webix.ui({
    view:"chart",
    id:"opBarChartDiv",
    container:"opBarChartDiv",
    type:"bar",
    value:"#count#",
    label:function(obj){
    	console.log(obj);
    	var html = obj.count;
        html += " - <b>"+obj.percentage+"%</b>";
        return html
     },
    shadow:0,
    preset: "alpha",//preset: "column",
    xAxis:{
        template:"#codeName#",
        title:"<b style='color: chocolate;'>${e3ps:getMessage('발생장소')}</b>"
    },
    yAxis:{
      start:0,
      step:1,
      template:function(obj){
        return obj%2 ? "" :obj;
      }
    },
    tooltip:{
      template: "#codeName#"
    },
});

webix.ui({
    view:"chart",
    id:"ocBarChartDiv",
    container:"ocBarChartDiv",
    type:"bar",
    value:"#count#",
    label:function(obj){
    	console.log(obj);
    	var html = obj.count;
        html += " - <b>"+obj.percentage+"%</b>";
        return html
     },
    shadow:0,
    preset: "alpha",
    xAxis:{
        template:"#codeName#",
        title:"<b style='color: chocolate;'>${e3ps:getMessage('처리상태')}</b>"
    },
    yAxis:{
      start:0,
      step:1,
      template:function(obj){
        return obj%2 ? "" :obj;
      }
    },
    tooltip:{
      template: "#codeName#"
    },
});
$(document).ready(function(){
	//enter key pressed event
	$("#searchForm").keypress(function(e){
		if(e.keyCode==13){
			search();
		}
	});
	
	//lifecycle list
	getLifecycleList("LC_Default");
	
	getNumberCodeList("occurrenceModel", "CARTYPE", false, true);
	getNumberCodeList("occurrenceStep", "OCCURRENCESTEP", false, true);
	getNumberCodeList("occurrencePlace", "OCCURRENCEPLACE", false, true);
	getNumberCodeList("oldcarStateS", "OLDCARSTEP", false, true);
	
	getData();
});

function getData(){
	$$("omBarChartDiv").load(function(params){
		params = new Object();
		params["cTypeName"] = "occurrenceModel";
		params["cType"] = "CARTYPE";
		getFormParams("searchForm", params);
		params = JSON.stringify(params);
		return webix.ajax().headers({
		    	"Content-Type": "application/json; charset=UTF-8"
		    }).post(getURLString("/oldcar/searchOldcarChart"), params)
		    .then(function(response){
		    	var data = response.json();
		    	$("#total").html(data.total);
		    	return data;
		    });
	});
	
	$$("osBarChartDiv").load(function(params){
		params = new Object();
		params["cTypeName"] = "occurrenceStep";
		params["cType"] = "OCCURRENCESTEP";
		getFormParams("searchForm", params);
		params = JSON.stringify(params);
		return webix.ajax().headers({
		    	"Content-Type": "application/json; charset=UTF-8"
		    }).post(getURLString("/oldcar/searchOldcarChart"), params)
		    .then(function(response){
		    	var data = response.json();
		    	return data;
		    });
	});
	
	$$("opBarChartDiv").load(function(params){
		params = new Object();
		params["cTypeName"] = "occurrencePlace";
		params["cType"] = "OCCURRENCEPLACE";
		getFormParams("searchForm", params);
		params = JSON.stringify(params);
		return webix.ajax().headers({
		    	"Content-Type": "application/json; charset=UTF-8"
		    }).post(getURLString("/oldcar/searchOldcarChart"), params)
		    .then(function(response){
		    	var data = response.json();
		    	return data;
		    });
	});
	
	$$("ocBarChartDiv").load(function(params){
		params = new Object();
		params["cTypeName"] = "oldcarStateS";
		params["cType"] = "OLDCARSTEP";
		getFormParams("searchForm", params);
		params = JSON.stringify(params);
		return webix.ajax().headers({
		    	"Content-Type": "application/json; charset=UTF-8"
		    }).post(getURLString("/oldcar/searchOldcarChart"), params)
		    .then(function(response){
		    	var data = response.json();
		    	return data;
		    });
	});
}
//검색
function search(){
	$$("omBarChartDiv").clearAll();
	$$("osBarChartDiv").clearAll();
	$$("opBarChartDiv").clearAll();
	$$("ocBarChartDiv").clearAll();
	$("#sessionId").val("");
	getData();
}

//검색조건 초기화
function reset(){
	var locationDisplay = $("#locationDisplay").val();
	$("#searchForm")[0].reset();
	$("#locationDisplay").val(locationDisplay);
}
</script>
