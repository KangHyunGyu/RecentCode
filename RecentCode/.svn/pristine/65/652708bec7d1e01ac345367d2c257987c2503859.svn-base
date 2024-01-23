<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- WEBIX -->
<script src="/Windchill/jsp/stagegate/stagegateHTML/js/webix/custom.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="/Windchill/jsp/stagegate/stagegateHTML/js/webix/webix.min.css?v=7.4.4" type="text/css">
<link href="/Windchill/jsp/stagegate/stagegateHTML/js/webix/custom.css" rel="stylesheet" type="text/css">
<script src="/Windchill/jsp/stagegate/stagegateHTML/js/webix/webix.js" type="text/javascript" charset="utf-8"></script>

<script type=text/javascript>
$(document).ready(function(){
	toggleImgBinding();
	$("#uploader_container").hide();
	
	var viewOnly = "${viewOnly}";
	if(viewOnly === "true"){
		$("input[type=button]").attr("disabled", true);
		$("input[type=button]").attr("type", "hidden");
	}
	
	if(self.name != 'reload'){
		self.name = 'reload';
		self.location.reload(true);
	}
	else self.name='';
	
});

function getChartData(){
	var params = new Object();
	params["oid"] = "${oid}";
	let data = new Object();
	//data
	let url = getURLString("/gate/getChartData");
	data = ajaxCallServer(url, params);
	
	return data;
}

//webix chart 생성
 renderChart = ({ list }) => {
	 chartCols = [];
	 chartsPanel = document.querySelector(".chartsPanel");
  for (let data of list) {
    chartCols.push({
      rows: [
        {
          template: data.title,
          height: 30,
        },
        {
          view: "chart",
          data: data.values,
          height: 140,
          xAxis: {
            template: "#label#",
          },
          yAxis: {
        	start:data.ystart,
  	        end:data.yend,
  	        step:data.ystep,
          	template: function(value){
          		if(data.yaxis_tem!=null && data.yaxis_tem.length > 0){
          			value = value + data.yaxis_tem;
          		}
			    return value;
			}
            //step: 10,
          },
          series: [
            {
            //data
              type: "line",
              value: "#value#",
              tooltip: {
                template: "#value#",
              },
              item: {
              	borderColor: data.target_color
              },
              line: {
              	width: 5,
              	color: data.target_color
              }
            },
            {
            //target
              type: "line",
              value: data.target,
              
              item: { 
              	radius: 0
              },
              line: {
              	color: data.target_color
              },
              tooltip: {
                template: data.plot,
              },
            },
          ],
        },
      ],
    });
  }

  webix.ui({
    container: "chartsPanel",
    cols: chartCols,
  });
};



function toEnabled() {
    $("input[type=text]").attr("disabled", false);
}
function toDisabled() {
    $("input[type=text]").attr("disabled", true);
}
function modifySummary(){
	$("#summaryForm").attr("action",getURLString("/gate/modifyObjectValue"));
	var param = new Object();
	var msg = "${e3ps:getMessage('저장하시겠습니까?')}";
	toEnabled();
	formSubmit("summaryForm", param, msg, null, true);
	
	toDisabled();
	
}

function test(){
	var params = new Object();
	params["oid"] = "${oid}";
	var data = new Object();
	var url = getURLString("/gate/revisionGate");
	ajaxCallServer(url, params);
}

renderChart(getChartData());
</script>
<script type="text/javascript" charset="utf-8">
webix.ready(function() {
	webix.ui({
		container:"uploader_container",
		view:"form", rows: [
			{ 
				view: "uploader",
				id: "secondaryFile",
				value: '이미지 파일 첨부', 
				name:"secondaryFile",
				link:"mylist",
				multiple:false, 
				autosend:false,
				upload:"/Windchill/jsp/stagegate/include/fileUpload.jsp",
				accept:"image/png, image/gif, image/jpeg",
				urlData:{
			        oid:"${objOid }",
			        formId:"summaryForm",
			        roleType:"PRIMARY",
			    }
			},
			{
			 	view:"list",  id:"mylist", type:"uploader",
				autoheight:true, borderless:true	
			},
			{ view: "button", label: "저장", click: function() {
				$$("secondaryFile").send(function(){
				    var f = $$("secondaryFile").files;
				    var file_id = f.getFirstId();
				    var status = f.getItem(file_id).status; // upload status
				    var fname = f.getItem(file_id).name;    // file name
				    
				    if(status=="server"){
				    	params = new Object();
	 					params["file"] = f.getItem(file_id);
	 					params["oid"] = "${objOid }";
	 					params = JSON.stringify(params);
				    	webix.ajax().headers({
					    	"Content-Type": "application/json; charset=UTF-8"
					    }).post(getURLString("/gate/uploadFile"), params)
					    .then(function(response){
					    	var data = response.json();
							if(data.result){
								webix.message("업로드 성공");
					    	}
					    	return data;
					    });
				    }
				});
				    
			}}
		]
	});
});
</script>
<input type="hidden" name="objType" value="${objType}">
<input type="hidden" name="oid" value="${oid}">
<div class="seach_arm3 pl25 pt5 ">
	<div class="leftbt">
		<img class="pointer content--toggle" src="/Windchill/jsp/portal/images/minus_icon.png" data-target="infomation">
<!-- 		<a onclick="test()">기본 정보</a> -->
		기본 정보
	</div>
</div>

<div class="pro_table pt5 pl25 pr25 ">
	<table class="mainTable infomation" summary="infomation">
		<caption>
			기본 정보
		</caption>
		<colgroup>
			<col style="width:15%">
			<col style="width:20%">
			<col style="width:15%">
			<col style="width:20%">
			<col style="width:auto">
		</colgroup>

		<tbody>
			<tr>
				<th scope="row">
					프로젝트 번호
				</th>

				<td>
					<span id="code">
						${pData.code}
					</span>
				</td>

				<th scope="row">
					프로젝트 명
				</th>

				<td>
					<span id="name">
						${pData.name}
					</span>
				</td>
				<c:if test="${imgUrl eq null}">
               <td rowspan="6" style="border-left:1px solid #dedede;" align="center">
                  <div id="uploader_container" style="width:auto; height: 180px;"></div>
               </td>
               </c:if>
               
               <c:if test="${imgUrl ne null}">
				<td rowspan="6" style="border-left:1px solid #dedede; padding: 0 0 0 0;" align="center">
                   <div id="uploader_container" style="width:auto; height: 180px;"></div>
					<div id="imgDiv"><img style="width:auto; height: 180px;" src="${imgUrl }" alt="Stage-gate" /></div>
				</td>
			   </c:if>
			</tr>

			<tr>
				<th scope="row">
					버전
				</th>

				<td>
					<span id="version">
						${pData.version}
					</span>
				</td>

				<th scope="row">
					개발구분
				</th>

				<td>
					<span id="devType">
						${pData.devTypeName}
					</span>
				</td>
			</tr>

			<tr>
				<th scope="row">
					프로젝트 타입
				</th>

				<td>
					<span id="projectType">
						${pData.projectTypeDisplay}
					</span>
				</td>

				<th scope="row">
					템플릿
				</th>

				<td>
					<span id="templateName">
						${pData.templateName}
					</span>
				</td>
			</tr>

			<tr>
				<th scope="row">
					용도
				</th>

				<td>
					<span id="purpose">
						${pData.purposeName}
					</span>
				</td>

				<th scope="row">
					고객사
				</th>

				<td>
					<span id="customer">
						${pData.customerName}
					</span>
				</td>
			</tr>

			<tr>
				<th scope="row">
					END ITEM
				</th>

				<td>
					<span id="endItem">
						${pData.endItemName}
					</span>
				</td>

				<th scope="row">
					등록자
				</th>

				<td>
					<span id="creator">
						${pData.creatorFullName}
					</span>
				</td>
			</tr>

			<tr>
				<th scope="row">
					최초 등록일
				</th>

				<td>
					<span id="createDate">
						${pData.createDate}
					</span>
				</td>

				<th scope="row">
					수정일
				</th>

				<td>
					<span id="modifyDate">
						${pData.modifyDate}
					</span>
				</td>
			</tr>

		</tbody>
	</table>
</div>

<div class="seach_arm3 pl25 pt5">
	<div class="leftbt">
		<img class="pointer content--toggle" src="/Windchill/jsp/portal/images/minus_icon.png" data-target="usertable">
		구성원
	</div>
</div>

<div class="pro_table pt5 pl25 pr25">
	<table class="mainTable usertable" summary="CORE TEAM">
	<colgroup>
		<col style="width:15%">
		<col style="width:auto">
		<col style="width:15%">
		<col style="width:auto">
		<col style="width:15%">
		<col style="width:auto">
	</colgroup>
	<tbody id="userslist">
	  <c:forEach var="member" items="${members}" varStatus="loop">
	  	<th>${member.roleName}</th>
	  	<td><span>${member.userName}</span></td>
		<c:if test="${loop.count % 3 eq 0}"><tr></c:if>
	  </c:forEach>
	</table>
</div>
<form name="summaryForm" id="summaryForm" method="post">
<input type="hidden" id="secondaryInput" name="secondaryInput">
<div class="seach_arm3 pl25 pt5 ">
	<div class="leftbt">
		<img class="pointer content--toggle" src="/Windchill/jsp/portal/images/minus_icon.png" data-target="objectives">
		Project main objectives
		<input type="button" class="sm_bt03 property_update" value="${e3ps:getMessage('수정')}" id="saveSummary" readonly="readonly">

	</div>
</div>

<div class="pro_table pt5 pl25 pr25">
	<div class="objectives">
		<div style="display: flex; border-left: none; border-bottom: none; border-right: none; ">
			<input type="hidden" id="oid" name="oid" value="${sData.oid }">
			<table class="mainTable" style="width: 30%; margin-left:0">
				<caption>검색조건보기</caption>
				<colgroup>
					<col style="width:35%">
					<col style="width:auto">
				</colgroup>
				
				
				<tbody>
					<tr>
						<th colspan="2" class="tac">
							QUALITY
						</th>
					</tr>
					<tr>
						<th>
							Internal
						</th>
						<td>
							<input type="text" id="interal" name="value0" value="${sData.value0}" disabled 
							oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');"/>
						</td>
					</tr>
					<tr>
						<th>
							Cust. 0 km
						</th>
						<td>
							<input type="text" id="cust" name="value1" value="${sData.value1}" disabled 
							oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');"/>
						</td>
					</tr>
				</tbody>
				
			</table>

			<div style="width: 5%"> </div>

			<table class="mainTable" style="width: 30%; margin:auto; margin-top:0">
				<caption>검색조건보기</caption>
				<colgroup>
					<col style="width:auto">
				</colgroup>
				<tbody>
					<tr>
						<th class="tac">
							WARRANTY (IF APPLICABLE)
						</th>
					</tr>
					<tr>
						<td>
							<input type="text" id="warranty" name="value2" value="${sData.value2 }" disabled />
						</td>
					</tr>
				</tbody>
			</table>

			<div style="width: 5%"> </div>

			<table class="mainTable" style="width: 30%; margin-right:0">
				<caption>검색조건보기</caption>
				<colgroup>
					<col style="width:33%">
					<col style="width:33%">
					<col style="width:auto">
				</colgroup>
				<tbody>
					<tr>
						<th colspan="3" class="tac">
							TIMING
						</th>
					</tr>
					<tr>
						<th class="tac">
							Kick-Off Tool
						</th>
						<th class="tac">
							PPAP
						</th>
						<th class="tac">
							First SOP
						</th>
					</tr>
					<tr>
						<td>
							<input type="text" class="datePicker w125" id="kotool" name="value3" value="${sData.value3 }" disabled />
						</td>
						<td>
							<input type="text" class="datePicker w125" id="ppap" name="value4" value="${sData.value4 }" disabled />
						</td>
						<td>
							<input type="text" class="datePicker w125" id="firstSOP" name="value5" value="${sData.value5 }" disabled />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</form>
<div class="seach_arm3 pl25 pt5 ">
	<div class="leftbt">
		<img class="pointer content--toggle" src="/Windchill/jsp/portal/images/minus_icon.png" data-target="chartsPanel">
		Charts
	</div>
</div>

<div class="pro_table pt5 pl25 pr25">
	<div class="mainTable infomation chartsPanel" id="chartsPanel">
	</div>
</div>
<script src="/Windchill/jsp/stagegate/stagegateHTML/modules/summary/summary.js"></script>