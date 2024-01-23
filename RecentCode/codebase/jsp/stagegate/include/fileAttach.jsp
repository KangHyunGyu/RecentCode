<%@page import="com.e3ps.common.content.FileRequest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
var console = window.console || {log:function(){}};
var count =0;
$(document.body).ready(function(){
	count++;
	${componentName}Obj.pageStart();
	<c:if test="${!empty uploadedList}">
		var ${componentName}uploadedList = ${uploadedList};
		
		${componentName}Upload.setUploadedList(${componentName}uploadedList);
		<c:if test="${'SECONDARY' ne type}">
// 			var file = ${componentName}uploadedList;
// 			var filelocation = file.cashId;
// 			console.log(file);
// 			if(null == filelocation){
// 				$("form[id='"+file.formId+"']").append('<input type="hidden" name="${empty description ? type : description }" id="${empty description ? type : description }" value="${m_fileFullPath}/'+file.name+'/${empty description ? type : description }" />');
// 			}else{
// 				$("form[id='"+file.formId+"']").append('<input type="hidden" name="${empty description ? type : description }" id="${empty description ? type : description }" value="'+file.cacheId+'/'+file.name+'/${empty description ? type : description }" />');
// 			}
// 			$("form[id='"+file.formId+"']").append("<input type='hidden' name='${type}_${description}delocIds' id='${type}_${description}delocIds' value='${description}delocIds_${type}' />");
			
			for( var i = 0; i < ${componentName}uploadedList.length; i++ ){
				var file = ${componentName}uploadedList[i];
				var fileName = file.name;
// 				console.log("primary ->"+fileName);
				$("form[id='"+file.formId+"']").append("<input type='hidden' name='${description}delocIds' id='${description}delocIds' value='"+file.${description}delocId+"' />");
			}

		</c:if>
	
		<c:if test="${'SECONDARY' eq type}">
// 		$('#fileAttachCount').html(${componentName}uploadedList.length);
		if(${componentName}uploadedList.length==0){
			    $("#SECONDARY_uploadQueueBox").css("background-image","url('/Windchill/jsp/component/axisj/ui/bulldog/images/dx-upload5-drop-icon-img.png')");   
			}else{
			    $("#SECONDARY_uploadQueueBox").css("background-image","none");
			}
// 		$('#fileAttachedCount').val(${componentName}uploadedList.length);
		for( var i = 0; i < ${componentName}uploadedList.length; i++ ){
			var file = ${componentName}uploadedList[i];
			var fileName = file.name;
			console.log("secondary ->"+fileName);
			$("form[id='"+file.formId+"']").append("<input type='hidden' name='${description}delocIds' id='${description}delocIds' value='"+file.${description}delocId+"' />");
		}
		</c:if>
	</c:if>
	
});
</script>
<c:choose>
	<c:when test="${'SECONDARY' eq type}">
		<input type='hidden' id='fileAttachedCount' name='fileAttachedCount' value=''>
		<div class="AXUpload5" id="${componentName}UploadBtn" style=' margin-top: 5px;'></div>
	</c:when>
	
	<c:otherwise>
		<div class="AXUpload5" id="${componentName}UploadBtn" style='margin-top: 5px;'></div>
	</c:otherwise>
</c:choose>

<iframe id="${componentName}downloadFrame" name="${componentName}downloadFrame"  style="display:none;"></iframe>

<c:if test="${'SECONDARY' ne type}">
<div class="H10"></div>
	<!-- 이미지만 등록 가능 하도록 하기위해서.  -->
	<c:if test="${'image' eq fileType}">
		<div id="${componentName}uploadQueueBox" class="AXUpload5QueueBox" style="height:180px;width:100%;"></div>
	</c:if>
	<c:if test="${'image' ne fileType}">
	<div id="${componentName}uploadQueueBox" class="AXUpload5QueueBox_list" style="height:${dropHeight}px;width:100%;"></div>
	</c:if>
</c:if>
<c:if test="${'SECONDARY' eq type}">
	<div class="H10"></div>
	
	<!-- 이미지만 등록 가능 하도록 하기위해서.  -->
	<c:if test="${'image' eq fileType}">
		<div id="${componentName}uploadQueueBox" class="AXUpload5QueueBox" style="height:180px;width:100%;"></div>
	</c:if>
	<c:if test="${'image' ne fileType}">
	<div id="${componentName}uploadQueueBox" class="AXUpload5QueueBox_list" style="height:${dropHeight}px;width:100%;"></div>
	</c:if>
</c:if>

<script type="text/javascript">
var btnId = "${btnId}";
var masterHost = location.protocol + "//" + location.host+"/Windchill/";
var agent = navigator.userAgent.toLowerCase();		//브라우저 버전 정보
var isIE =  (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1); //IE버전인지 아닌지 확인

var flashUrl = "/Windchill/jsp/axisj/lib/swfupload.swf";
var flash9Url = "/Windchill/jsp/axisj/lib/swfupload_fp9.swf";
var uploadUrl = "/Windchill/jsp/common/include/content/fileUpload.jsp";
var deleteUrl = "/Windchill/jsp/common/include/content/fileDelete.jsp";

$("#${componentName}DownloadFrame").hide();		//프레임 숨기기

var ${componentName}Upload = new AXUpload5();

AXConfig.AXUpload5.deleteConfirm = "Are you sure you want to delete?" ;		//삭제하시겠습니까?
AXConfig.AXUpload5.uploadSelectTxt = "";
var fileCount ='${fileCount}';
var ${componentName}Obj = {
		pageStart: function(){
			${componentName}Obj.upload.init();
		},
		upload: {
			init: function(){
				${componentName}Upload.setConfig({
					targetID:"${componentName}UploadBtn",
					isSingleUpload: false,
					targetButtonClass:"Blue",
					uploadFileName:"${type}",
					crossDomain:true,
					buttonTxt:'File Search', //첨부파일
					dropBoxID:"${componentName}uploadQueueBox",
					queueBoxID:"${componentName}uploadQueueBox", // upload queue targetID
					testa:"a",
					<c:if test="${'image' eq fileType}">
					file_types:"image/*",  //audio/*|video/*|image/*|MIME_type (accept)
					flash_file_types:"*.jpg;*.jpeg;*.gif;*.bmp;*.png",
                    flash_file_types_description:"image",
					</c:if>
					
					flash_url : flashUrl,
					flash9_url : flash9Url,
					onClickUploadedItem: function(){ // 업로드된 목록을 클릭했을 때.
					    //특수문자 문제 때문에 post로 parameter 넘기도록 변경
						var url = "${url}${dsecPath}/Windchill/jsp/common/include/content/fileDownload.jsp";
						
					    //var fileLink = "${url}${dsecPath}/Windchill/jsp/common/include/content/fileDownload.jsp?fileName="+ this.saveName.dec()+"&originFileName="+this.name.dec();

					    var form = document.createElement("form");
					    form.setAttribute("method", "post");
					    form.setAttribute("action", url);
					    form.setAttribute("target", "fileDownload");
					    
					    //?fileName="+ this.saveName.dec()+"&originFileName="+this.name.dec()
					    var fileName = document.createElement("input") ;
					    
					    fileName.setAttribute("type", "hidden");
					    fileName.setAttribute("name", "fileName");
					    fileName.setAttribute("value", this.saveName.dec());
					    form.appendChild(fileName);
					    
					    var originFileName = document.createElement("input") ;
					    
					    originFileName.setAttribute("type", "hidden");
					    originFileName.setAttribute("name", "originFileName");
					    originFileName.setAttribute("value", this.name.dec());
					    form.appendChild(originFileName);
					    
						if(this.cacheId.dec() == ""){
							fileLink = location.protocol + "//" + location.host+"/Windchill/"+this.uploadedPath.dec();
						}
						if (isIE) {
							document.body.appendChild(form);
							form.submit();
							document.body.removeChild(form);
							//window.open(fileLink, "_blank", "width=500,height=500");
						}else{
							//$("#${componentName}downloadFrame").attr("src",fileLink);
							document.body.appendChild(form);
							form.submit();
							document.body.removeChild(form);
						}
					},
					//uploadMaxFileSize:(5000000*1024*1024), // 업로드될 개별 파일 사이즈 (클라이언트에서 제한하는 사이즈 이지 서버에서 설정되는 값이 아닙니다.)
					//uploadMaxFileCount:0, // 업로드될 파일갯수 제한 0 은 무제한 싱글모드에선 자동으로 1개
					//uploadMaxFileSize:(1000000*1024*1024), // 업로드될 개별 파일 사이즈 (클라이언트에서 제한하는 사이즈 이지 서버에서 설정되는 값이 아닙니다.) - 100MB
					uploadMaxFileSize:(1024*1024)*500,		//									- 500MB
					uploadMaxComment: ' You can upload up to 500 MB per file',//'파일당 최대' + '100MB' + '까지 업로드 할 수 있습니다.',
					uploadMaxFileCount:${'SECONDARY' eq type ? 0 : 1 }, // 업로드될 파일갯수 제한 0 은 무제한 싱글모드에선 자동으로 1개		- 100개
					uploadUrl: uploadUrl,
					uploadPars:{masterHost : masterHost, userId:'${userName}', userName:'${userName}', roleType : '${type}', formId : '${formId}', description : '${description}' },
					deleteUrl: deleteUrl,
					//deletePars:{userID:'tom', userName:'액시스'},
					//file_types: "image/*|application/acad|application/msword|application/mspowerpoint|application/excel|text/plain|image/tiff",
					//file_types: "text/plain",
					//file_type_checkMsg : "지원하지 않는 파일 형식 입니다. 첨부파일을 삭제후 다시 첨부하세요.", //지원하지 않는 파일 형식 입니다. 첨부파일을 삭제후 다시 첨부하세요.\r\n (ex. *.exe, *.zip, *.txt, *.jsp, *.js ...)
					//html5_file_types : "*.easm;*.wmp;*.zip;*.txt;*.doc;*.docx;*.ppt;*.pptx;*.xls;*.xlsx;*.csv;*.pps;*.rtf;*.jpg;*.jpeg;*.gif;*.bmp;*.tif;*.tiff;*.pdf;*.hwp;*.dwg;*.dxf;*.pcb;*.sch;*.pcbfile;*.schfile;*.catpart;*.catdrawing;*.catproduct;*.model;*.prt;*.stp;*.step;*.igs;*.iges;*.cgm;*.epsi;*.plt;*.ol;*.dwg;*.dxf;*.cgm;*.hex;",
					//flash_file_types: "*.easm;*.wmp;*.zip;*.txt;*.doc;*.docx;*.ppt;*.pptx;*.xls;*.xlsx;*.csv;*.pps;*.rtf;*.jpg;*.jpeg;*.gif;*.bmp;*.tif;*.tiff;*.pdf;*.hwp;*.dwg;*.dxf;*.pcb;*.sch;*.pcbfile;*.schfile;*.catpart;*.catdrawing;*.catproduct;*.model;*.prt;*.stp;*.step;*.igs;*.iges;*.cgm;*.epsi;*.plt;*.ol;*.dwg;*.dxf;*.cgm;*.hex;",
	                //flash_file_types_description:"File",
					fileKeys:{ // 서버에서 리턴하는 json key 정의 (id는 예약어 사용할 수 없음)
						name:"name",
						type:"type",
						saveName:"saveName",
						fileSize:"fileSize",
						uploadedPath:"uploadedPath",
						thumbPath:"thumbUrl",
						roleType : "roleType",
						//delocId : "delocId",
						${description}delocId : "${description}delocId",
						cacheId : "cacheId",
						description : "description"
					},
					onUpload : function(file){
						
						var fileName = file.name;
						//alert('${empty description ? type : description }');
// 						<c:if test="${'SECONDARY' ne type}">
// 							$('input[name="${empty description ? type : description }"]').remove();
// 						</c:if>
						
						$("form[id='${formId}']").append('<input type="hidden" name="${empty description ? type : description }" id="${empty description ? type : description }" value="'+file.cacheId+'/'+file.name+'/${empty description ? type : description }" />');
						//BOM 로드에서 실제 서버에 업로드 된 파일명 검증시 parm으로 전송을 위해
						<c:if test="${'bomLoad' eq moduleType}">
							if(!$("#orgFileName").length){
								$("form[id='${formId}']").append('<input type="hidden" name="orgFileName" id="orgFileName"/>');
								$("#orgFileName").val(file.saveName);
							}else{
								$("#orgFileName").val(file.saveName);
							}
						</c:if>
						//window.console.log($("input[name='${empty description ? type : description }']").val());
					},
					onComplete: function(){
						if(btnId != 'none') {
							$("#" + btnId).attr("disabled", false);
						}
						var list = ${componentName}Upload.getUploadedList("json");
						  var count = Object.keys(list).length;
						//alert(count);
						<c:if test="${'SECONDARY' eq type}">
// 							$('#fileAttachCount').html(($("input[name='${description}delocIds']").length += $("input[name='${empty description ? type : description }']").length));
// 							$('#fileAttachCount').html(count);
							if(count!=0){
							  $("#SECONDARY_uploadQueueBox").css("background-image","none");    
							}
							
						</c:if>
						//$("#uploadCancelBtn").get(0).disabled = true; // 전송중지 버튼 제어
						//is${secType}Processing = false;
					},
					onStart: function(){
						if(btnId != 'none') {
							$("#" + btnId).attr("disabled", true);
						}
						//$("#uploadCancelBtn").get(0).disabled = false; // 전송중지 버튼 제어
						//is${secType}Processing = true;
					},
					onDelete: function(file){
						$('input[name="${empty description ? type : description }"]:input[value="'+file.cacheId+'/'+file.name+'/${empty description ? type : description }"]').remove();
						$("input[name='${empty description ? "" : description}delocIds']:input[value='"+file.${description}delocId+"']").remove();
						//$("input[name='${type}_${empty description ? "" : description}delocIds']:input[value='${description}delocIds_${type}']").remove();
						$("input[name='${type}_${empty description ? "" : description}delocIds']:input[value='${description}delocIds_${type}']").val(file.name);
						<c:if test="${'SECONDARY' eq type}">
							//$('#fileAttachCount').html(($("input[name='${description}delocIds']").length + $("input[name='${empty description ? type : description }']").length));
							var list = ${componentName}Upload.getUploadedList("json");
						    var count = Object.keys(list).length;
// 						  $('#fileAttachCount').html(count);
						  if(count==0){
							    $("#SECONDARY_uploadQueueBox").css("background-image","url('/Windchill/jsp/component/axisj/ui/bulldog/images/dx-upload5-drop-icon-img.png')");   
							}
						</c:if>
						//${type}_${description}delocIds
					},
					onError: function(errorType, extData){
						if(errorType == "html5Support"){
							//dialog.push('The File APIs are not fully supported in this browser.');
						}else if(errorType == "fileSize"){
							// 파일사이즈가 초과된 파일을 업로드 할 수 없습니다. 업로드 목록에서 제외 합니다.
							alert('You can not upload files that exceed the file size. Exclude from upload list.\n(' + extData.name + ' : ' + extData.size.byte() + ')');
						}else if(errorType == "fileCount"){
							// 업로드 갯수 초과 초과된 파일은 업로드 되지 않습니다.
							alert('Files that exceed the upload limit will not be uploaded.');
						}else if(errorType == "-200"){
							// 서버오류가 발생하여 파일업로드를 실패했습니다.
							alert('File upload failed due to server error.');
							secondaryUpload.cancelUpload();
						}
					}
				});
			}
		}
}
</script>