<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<!-- button -->
<div class="seach_arm2 pt10 pb5">
</div>
<!-- //button -->

<c:if test="${isEdit eq 'true'}">
<input type="hidden" name="actOid" value="${data.oid}"/>
<div class="seach_arm pt20 pb5">
	<div class="leftbt">
	</div>
	<div class="rightbt">
		<input type="button" class="s_bt03" value="활동 내용 저장" onclick="commentCreate();" style="margin-bottom: 10px;">
	</div>
</div>
<div class="pro_table mr30 ml30">
	<table class="mainTable">
		<colgroup>
			<col width="10%">
			<col width="40%">
			<col width="10%">
			<col width="40%">
		</colgroup>
        <tr>
        	<th>${e3ps:getMessage('활동 내용')}</th>
        	<td colspan="3">
        		<textArea id="comments" name="comments"  size="85" class="input01" style="width:98%;height:100px;margin-bottom: 10px;">${data.comment }</textArea>
        	</td>
        </tr>
        <tr>
        	<th>${e3ps:getMessage('첨부파일')}</th>
            <td colspan="3">
               <jsp:include page="${e3ps:getIncludeURLString('/content/include_fileAttach')}" flush="true">
                  <jsp:param name="formId" value="approveForm"/>
                  <jsp:param name="command" value="insert"/>
                  <jsp:param name="btnId" value="createBtn" />
                  <jsp:param name="type" value="SECONDARY" />
               </jsp:include>
            </td> 
		</tr>
    </table>
</div>
</c:if>
<c:if test="${isEdit ne 'true'}">
<div class="pro_table">
	<table class="mainTable">
		<colgroup>
			<col width="10%">
			<col width="40%">
			<col width="10%">
			<col width="40%">
		</colgroup>
		<tr>
            <td colspan="4">
            	<div class="list" id="ecaCommon_grid_wrap">
				</div>
            </td> 
		</tr>
		<tr>
        	<th>${e3ps:getMessage('첨부파일')}</th>
            <td colspan="3">
            	<jsp:include page="${e3ps:getIncludeURLString('/content/include_fileView')}" flush="true">
					 <jsp:param name="oid" value="${oid}"/>
				 </jsp:include>
            </td> 
		</tr>
    </table>
</div>

</c:if>

<script>

if("true" != "${isEdit}"){
	if (!webix.env.touch && webix.env.scrollSize)
		  webix.CustomScroll.init();
	webix.ready(function(){
		var ecaCommon_grid_wrap = webix.ui({
			view:"datatable",
			container:"ecaCommon_grid_wrap",
			id:"ecaCommon_grid_wrap",
			select:"row",
			scroll:"xy",
			sort:"multi",
			yCount: 2,
			tooltip:true,
			resizeColumn:true,
			columns:[
				{ id:"index",   header:"No.",     width:40,	tooltip:false,
					css: "custom_webix_center",		
				},
				{ id:"name",	header:"${e3ps:getMessage('이름')}", 	width:200,
					css:"custom_webix_ellipsis custom_webix_center",		
				},
				{ id:"comment",	header:"${e3ps:getMessage('내용')}", 	fillspace:1.5,
					css:"custom_webix_ellipsis custom_webix_center",		
				},
				{ id:"state",	header:"${e3ps:getMessage('상태')}", 	width:120,
					css:"custom_webix_ellipsis custom_webix_center",		
				},
			],
			on:{
				"data->onStoreUpdated":function(){
		            this.data.each(function(obj, i){
		                obj.index = i+1;
		            });
		        }
			}
		});
		if(!"true" === "${isEdit}"){
			$("#ecaCommon_grid_wrap").hide();
		}
		
		rel_common_getGridData();
	});
}

function rel_common_getGridData(){

	var param = new Object();
	
	param.oid = "${oid}";
	
	var url = getURLString("/change/getActivitySelf");
	ajaxCallServer(url, param, function(data){
		// 그리드 데이터
		var gridData = data.list;
		
		// 그리드에 데이터 세팅
		$$("ecaCommon_grid_wrap").parse(gridData);
	});
}

//필터 초기화
function rel_epm_resetFilter(){
    
}

function rel_epm_xlsxExport() {
	webix.toExcel($$("ecaCommon_grid_wrap"), {
		filename: "xlsxExport",
		rawValues:true,
	});
}

function commentCreate(){
	if($("#comments").val()==null || $("#comments").val()==""){
		alert("활동내용을 입력해주세요");
		return;
	}
	var param = new Object();
	$("#approveForm").attr("action", getURLString("/change/createActivityCommentAdd"));
	formSubmit("approveForm", param, "저장하시겠습니까?", null, true);
}
</script>