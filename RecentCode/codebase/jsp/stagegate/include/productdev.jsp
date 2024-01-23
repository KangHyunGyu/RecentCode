<%@page import="com.e3ps.stagegate.bean.SGObjectValueData"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"	%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- WEBIX -->
<link rel="stylesheet" href="/Windchill/jsp/stagegate/stagegateHTML/js/webix/webix.min.css?v=7.4.4" type="text/css">
<script>

$(document).ready(function(){
	$("#uploader_container2").hide();
	
	var viewOnly = "${viewOnly}";
	if(viewOnly === "true"){
		$("button").attr("disabled", true);
		$("button").attr("hidden", true);
	}
	
//	toggleUpdateBtn();
	toggleImgBinding();
	
});


$("button[id='save']").click(function(e){
	var table_name = e.target.dataset.target;	
	
	if(!e.target.classList.contains("property_submit")){
		toggleUpdateBtn(e,"${e3ps:getMessage('저장')}");
		$("#imgDiv").hide();
		$("#uploader_container2").show();
		return;
	}
	if(!confirm("${e3ps:getMessage('저장 하시겠습니까?')}")){
		toggleUpdateBtn(e, "${e3ps:getMessage('수정')}");
		$("#imgDiv").show();
		$("#uploader_container2").hide();
		return;
	}
	
	let itemList = [];	
	let rowList = document.querySelectorAll("#devTable tbody tr");
	
	for(let idx= 1; idx <= 12 ; idx++){
		let row = rowList[idx];
		
		let td = row.querySelectorAll("input[name^='value']");
		let oid = row.querySelector("input[name='oid']");
		let code = row.querySelector("input[name='code']");
		let name = row.querySelector("input[name='name']");
	
		if(td.length ==0 || oid == null){
			continue;
		}
		/* 
		console.log("oid",oid);
		console.log("code",code);
		console.log("name",name); */
		
		let item = new Object();
		
		item.oid = oid.value;
		item.code = code.value;
		item.name = name.value;
		
		for(let el = 0; el < 7; el++){
			let input = td[el];	
			item[input.name] = input.value;	
		}
		itemList.push(item);
	}
	console.log("itemList",itemList);
	
	
	var url = getURLString("/gate/modifyObjectValueList");
	var param = new Object();
	param["list"] = itemList;
	
 	ajaxCallServer(url,param,function(data){
		toggleUpdateBtn(e, "${e3ps:getMessage('수정')}");
		let onTab = document.querySelector("div .tap>ul>li.on");
		loadIncludePage(onTab);
	},true); 
	
});


function selectClassChange(o){
	o.classList.remove(o.classList.item(0));
	o.classList.add(o.value);
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

function filePopup(){
	let url = getURLString("/gate/filePopup")+"?oid="+"${oid}";
	openPopupCenter(url,"filePopup","550","300");
}
</script>
<script type="text/javascript" charset="utf-8">
webix.ready(function() {
	if(!$$("secondaryFile2")){
		webix.ui({
			container:"uploader_container2",
			view:"form", rows: [
				{ 
					view: "uploader",
					id: "secondaryFile2",
					value: '이미지 파일 첨부', 
					name:"secondaryFile2",
					link:"mylist2",
					multiple:false, 
					autosend:false,
					upload:"/Windchill/jsp/stagegate/include/fileUpload.jsp",
					accept:"image/png, image/gif, image/jpeg",
					urlData:{
				        oid:"${oid }",
				        formId:"summaryForm2",
				        roleType:"PRIMARY",
				    }
				},
				{
				 	view:"list",  id:"mylist2", type:"uploader",
					autoheight:true, borderless:true	
				},
				{ view: "button", label: "저장", click: function() {
					$$("secondaryFile2").send(function(){
					    var f = $$("secondaryFile2").files;
					    var file_id = f.getFirstId();
					    var status = f.getItem(file_id).status; // upload status
					    var fname = f.getItem(file_id).name;    // file name
					    
					    if(status=="server"){
					    	params = new Object();
		 					params["file"] = f.getItem(file_id);
		 					params["oid"] = "${oid }";
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
	}
});
</script>
<input type="hidden" name="objType" value="${objType}">

	<div class="seach_arm3 pl25 pt5 pr25">
       <div class="leftbt">
           <img class="pointer content--toggle" src="/Windchill/jsp/portal//images/minus_icon.png" data-target="product_dev">
           Product Dev.
           
       	   <button class="sm_bt03 property_update" data-target="product_dev" id="save" >${e3ps:getMessage('수정')}</button>
     
   		</div>
   		<div class="rightbt">
			 <img src="/Windchill/netmarkets/images/doc_document.gif" onclick="filePopup()">
		</div>
  	</div>

   <div class="pro_table pt5 pl25 pr25 ">
       <table class="mainTable product_dev" id="devTable">
           <colgroup>
				<col style="width:8%">
		   		<col style="width:auto%">
		   		<col style="width:7%">
              	<col style="width:4%">
		   		<col style="width:7%">
              	<col style="width:4%">
		   		<col style="width:7%">
              	<col style="width:4%">
		   		<col style="width:7%">
              	<col style="width:4%">
		   		<col style="width:7%">
              	<col style="width:4%">
		   		<col style="width:7%">
              	<col style="width:4%">
		   		<col style="width:7%">
              	<col style="width:4%">
           </colgroup>
           <tbody>
           	<tr>
     			<th scope="col" colspan="2"> product metrics</th>
				<th scope="col" class="tac">phase1</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">phase2</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">phase3</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">phase4</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">phase5</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">phase6</th>
				<th scope="col" class="tac"></th>
				<th scope="col" class="tac">Target</th>
				<th scope="col" class="tac"></th>
           	</tr>
           	
       		<c:forEach var="CodeData" items="${sgCodeList}" varStatus="status"> 
           			<tr>
           				<c:choose>
           					<c:when test="${status.count eq 3 }">
           						<td >
           							 Proto.&nbsp;
           						</td>
           						<td >
           							
           							${CodeData.name}
           						</td>
           					</c:when>
           					
           					<c:when test="${status.count >= 4 && status.count <= 6 }">
           						<c:if test="${status.count eq 4}">
	           						<td rowspan="3">
	           							 conform.&nbsp;&nbsp;
	           						</td>
           						</c:if>
           						<td >
           							 ${CodeData.name}
           						</td>
           					</c:when> 
           					
           					<c:when test="${status.count eq 8 }">
           						<td colspan="16">
           							${CodeData.name}
           						</td>
           					</c:when>
           					
           					<c:when test="${status.count >= 9 && status.count <= 12 }">
           						<c:if test="${status.count eq 9}">
	           						<td rowspan="4">
	           							 DVP&R &nbsp;&nbsp;
	           						</td>
           						</c:if>
           						<td >
           							 ${CodeData.name}
           						</td>
           					</c:when> 
           					
           					<c:otherwise>
           						<td colspan="2">
           							 ${CodeData.name}
           						</td>
           					</c:otherwise>
           				</c:choose>
           				
           				<c:if test="${status.count ne 8}">
           					<c:forEach begin="0" end ="6" var="idx">
           					
           					<c:set var="val" value="value${idx}"/>
           					
           						<td style="padding: 0 0 0 0">
           							<c:if test="${CodeData[val] ne ''}">
	           							<input type="text" class="textLFP noticeText" name="value${idx}" 
	           									value ="${CodeData[val]}" disabled
	           									oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
	           							 		 <span style="font-size:10px">%</span>
	           						</c:if>  
           							<c:if test="${CodeData[val] eq ''}">
           								<input type="text" class="textLFP noticeText" name="value${idx}" disabled
           										oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
           								
	           						</c:if>  
           						</td>
          						<td>
         							<input type="hidden" name="oid" value="${CodeData.oid}">
          							<input type="hidden" name="code" value="${CodeData.code}">
        							<input type="hidden" name="name" value="${CodeData.name}">
	           					</td>
           					</c:forEach>
           				</c:if>
           			</tr>
           	</c:forEach>
           </tbody>
       </table>
   </div>
<c:if test="${imgUrl eq null}">
	<div id="uploader_container2" style="width:auto; height: 180px;"></div>
</c:if>
         
<c:if test="${imgUrl ne null}">
	<div id="uploader_container2" style="width:auto; height: 180px;"></div>
	<div id="imgDiv"><img style="width:auto; height: 180px;" src="${imgUrl }" alt="Stage-gate" /></div>
</c:if>