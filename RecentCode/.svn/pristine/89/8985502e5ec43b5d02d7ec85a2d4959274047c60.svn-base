<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<div class="product">
	<form id="templateForm" name="templateForm" method="post">
		<div class="seach_arm pt20 pb5">
			<div class="leftbt">
				<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('직접 등록')}</span>
			</div>
			<div class="rightbt">
				<img class="pointer mb5" id="favorite" data-type="false" data-oid="" onclick="add_favorite();" src="/Windchill/jsp/portal/images/favorites_icon_b.png" border="0" />
				<button type="button" class="s_bt03" onclick="saveTemplate()">${e3ps:getMessage('등록')}</button>
				<button type="button" class="s_bt03" onclick="searchTemplate()">${e3ps:getMessage('기존 템플릿 가져오기')}</button>
			</div>
		</div>
		<div class="pro_table mr30 ml30">
			<table class="mainTable">
				<colgroup>
					<col style="width: 20%">
					<col style="width: 80%">
				</colgroup>
				<tbody>
					<tr>
						<th>${e3ps:getMessage('템플릿 명')}<span class="required">*</span></th>
						<td><input type="text" class="w50" id="name" name="name">
						</td>
					</tr>
					<tr>
						<th>${e3ps:getMessage('활성화 여부')}<span class="required">*</span></th>
						<td><input type=checkbox name="enabled" id="enabled" value=true checked></td>
					</tr>
<!-- 					<tr> -->
<%-- 						<th>${e3ps:getMessage('인증타입')}<span class="required">*</span></th> --%>
<!-- 						<td> -->
<!-- 							<select class="w10" id="outputType" name="outputType"> -->
<!-- 							</select> -->
<%-- 							<font color="red">(${e3ps:getMessage('기존 템플릿 가져오기시 기존 템플릿의 인증타입이 적용됩니다.')})</font> --%>
<!-- 						</td> -->
<!-- 					</tr> -->
					<tr>
						<th>${e3ps:getMessage('설명')}</th>
						<td class="pt5">
							<div class="textarea_autoSize">
								<textarea name="description" id="description"></textarea>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="seach_arm pt20 pb5">
		<div class="leftbt">
			<span class="title"><img class="pointer" onclick="switchDiv(this);" src="/Windchill/jsp/portal/images/minus_icon.png">${e3ps:getMessage('엑셀 파일 사용하여 업로드')}</span>
		</div>
		<div class="rightbt">
			<button type="button" value="template_import" class="s_bt03" onclick="uploadTemplate(this)">${e3ps:getMessage('업로드')}</button>
		</div>
		</div>
		<div class="pro_table mr30 ml30">
			<table class="mainTable">
				<colgroup>
					<col style="width:20%">
					<col style="width:80%">
				</colgroup>	
				<tbody>
				<tr>
	               <th>
	               		${e3ps:getMessage('업로드 엑셀 파일')}<span class="required">*</span>
	               		&nbsp;&nbsp;${e3ps:getMessage('양식(')}<a href="/Windchill/jsp/project/template/WorldexTemplateForm.xls">&nbsp;<img src="/Windchill/jsp/portal/icon/fileicon/xls.gif" title="${e3ps:getMessage('템플릿 다운로드')}" border="0"></a>${e3ps:getMessage(' )')}
	               </th>
	               <td class="primary">
	                  <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
	                     <jsp:param name="formId" value="templateForm"/>
	                     <jsp:param name="command" value="insert"/>
	                     <jsp:param name="type" value="PRIMARY"/>
	                  </jsp:include>
	               </td>
	            </tr>
	        </table>
		</div>
	</form>
	
	<div class="seach_arm pt5 pb5">
		<div class="leftbt"></div>
	</div>
	<!-- table list-->
	<div class="table_list hide" id="searchTemplateDiv">
		<div class="list" id="grid_wrap"></div>
	</div>
</div>
<script>
$(document).ready(function(){
	//lifecycle list
// 	getOutputTypeList();
});

if (!webix.env.touch && webix.env.scrollSize)
	  webix.CustomScroll.init();

webix.ready(function(){
	var grid = webix.ui({
		view:"datatable",
		container:"grid_wrap",
		id:"grid_wrap",
		clipboard:"block",
		areaselect: true,
		dragColumn: true,
		scroll:"y",
		sort:"multi",
		yCount: 7,
		tooltip:true,
		resizeColumn:true,
		columns:[
			{ id:"index",   header:"No.",     width:40,      tooltip:false,
				css: "custom_webix_center",		
			},
			{ id:"code", header:[{text:"${e3ps:getMessage('템플릿 번호')}", content:"excelFilter", mode:"text"}] , width:200,sort:"string",
				css: "custom_webix_center custom_webix_ellipsis",			
			},
			{ id:"name", header:"${e3ps:getMessage('템플릿명')}" ,fillspace:true, sort:"string",
				css: "custom_webix_ellipsis",		
			},
			{ id:"output", header:[{text:"${e3ps:getMessage('인증타입')}", content:"excelFilter", mode:"text"}], sort:"string",
				css: "custom_webix_center custom_webix_ellipsis",	
			},
			{ id:"manDay", header:[{text:"${e3ps:getMessage('기간')}", content:"excelFilter", mode:"text"}], sort:"string",
				css: "custom_webix_center custom_webix_ellipsis",	
			},
			{ id:"creator", header:[{text:"${e3ps:getMessage('등록자')}", content:"excelFilter", mode:"text"}], width:200, sort:"string",
				css: "custom_webix_center custom_webix_ellipsis",	
			},
			{ id:"createDate", header:"${e3ps:getMessage('최초등록일')}", sort:"string",
				css: "custom_webix_center custom_webix_ellipsis",		
			},
			{ id:"modifyDate", header:"${e3ps:getMessage('수정일')}", sort:"string",
				css: "custom_webix_center custom_webix_ellipsis",		
			},
			{ id:"button", header:"", tooltip:false,
				css: "custom_webix_center",	
				template:function(obj){
					return "<button class='s_bt07' onclick='templateCopy(\"" + obj.oid + "\")'>COPY</button>";
				}
			}
		],
		on:{
	        "data->onStoreUpdated":function(){
	            this.data.each(function(obj, i){
	                obj.index = i+1;
	            });
	        },
	    },
	});
		  
	webix.event(window, "resize", function(){ grid.adjust(); });
	
	webix.extend($$("grid_wrap"), webix.ProgressBar);
});

function saveTemplate(){
	if(!checkValidate()) {
		return;
	}
	$("#templateForm").attr("action",getURLString("/project/template/createTemplateAction"));
	var param = new Object();
	
	var msg = "${e3ps:getMessage('등록하시겠습니까?')}";
	formSubmit("templateForm", param, msg, function(){}, true);
}

function uploadTemplate(){
	/* if(!checkValidate()) {
		return;
	} */
	var primaryCheck = $("#PRIMARY_uploadQueueBox").find(".AXUploadItem");
	if(primaryCheck.val() == undefined){
		openNotice("${e3ps:getMessage('엑셀양식을 등록하세요.')}");
		return false;
	}
	
	$("#templateForm").attr("action",getURLString("/project/template/uploadTemplateAction"));
	var param = new Object();
	
	var msg = "${e3ps:getMessage('등록하시겠습니까?')}";
	
	formSubmit("templateForm", param, msg, function(){}, true);
}

function checkValidate() {
	
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('템플릿 명을 입력하세요.')}");
		return false;
	}
	
// 	if($("#outputType").val() == null || $("#outputType").val() == "") {
// 		$("#outputType").focus();
// 		openNotice("${e3ps:getMessage('인증타입을 선택하세요.')}");
// 		return false;
// 	}
	
	return true;
}

function checkValidateUpload() {
	
	if($("[id=PRIMARY]").length == 0){
		openNotice("${e3ps:getMessage('주 첨부파일을 추가하세요.')}");
		return false;
	}
	
	return true;
}

function searchTemplate(){
	
	$("#searchTemplateDiv").show();
	
	var param = new Object();
	
	$$("grid_wrap").clearAll();
	$$("grid_wrap").showProgress();
	var url = getURLString("/project/template/searchTemplateAction");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
	
		$$("grid_wrap").clearAll();
		$$("grid_wrap").parse(gridData);
	},false);
}

function templateCopy(oid){
	
	if($("#name").val() == null || $("#name").val() == "") {
		$("#name").focus();
		openNotice("${e3ps:getMessage('템플릿 명을 입력하세요.')}");
		return false;
	}
	
	var msg = "${e3ps:getMessage('등록하시겠습니까?')}";
	
	openConfirm(msg, function(){
		var param = new Object();
		
		param.oid = oid;
		param.name = $("#name").val();
		param.description = $("#description").val();
		param.enabled = $("#enabled").val();
		var url = getURLString("/project/template/copyTemplateAction");
		ajaxCallServer(url, param, function(data){
			
		},true);
	});
}
</script>