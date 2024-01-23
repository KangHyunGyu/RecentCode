
// 엑셀 내보내기(Export);
function exportAs(gridID, type){
	
	// 그리드가 작성한 엑셀, CSV 등의 데이터를 다운로드 처리할 서버 URL을 지시합니다.
	// 서버 사이드 스크립트가 JSP 이라면 export.jsp 로 변환해 주십시오.
	// 스프링 또는 MVC 프레임워크로 프로젝트가 구축된 경우 해당 폴더의 export.jsp 파일을 참고하여 작성하십시오.
	AUIGrid.setProperty(gridID, "exportURL", "/Windchill/jsp/js/AUIGrid/export.jsp");
	
	// 내보내기 실행
	switch(type) {
	case "xlsx":
		AUIGrid.exportToXlsx(gridID, true);
		break;
	case "csv":
		AUIGrid.exportAsCsv(gridID);
		break;
	case "txt":
		AUIGrid.exportAsTxt(gridID);
		break;
	case "xml":
		AUIGrid.exportAsXml(gridID);
		break;
	case "json":
		AUIGrid.exportAsJson(gridID);
		break;
	case "pdf": // AUIGrid.pdfkit.js 파일을 추가하십시오.
		if(!AUIGrid.isAvailabePdf(gridID)) {
			alert("PDF 저장은 HTML5를 지원하는 최신 브라우저에서 가능합니다.(IE는 10부터 가능)");
			return;
		}
		AUIGrid.exportToPdf(gridID, {
			// 폰트 경로 지정
			fontPath : "/Windchill/jsp/js/AUIGrid/pdfkit/jejugothic-regular.ttf"
		});
		break;
	case "object": // array-object 는 자바스크립트 객체임
		var data = AUIGrid.exportAsObject(gridID);
		alert(data);
		break;
	}
};

//AUIGRID 썸네일 렌더러
function thumbnailRenderer(rowIndex, columnIndex, value, headerText, item ,dataField){
	
	if(!value)	return "";
	
	/*var template = "<img class='thumView' style='max-width:100%;height:" + gridRowHeight + ";' ";*/
	var template = "<img class='thumView' style='max-width:100%;height:22px;' ";
	
	if(item.thumbnailWidth && item.thumbnailHeight){
		template += "data-width='" + item.thumbnailWidth + "' data-height='" + item.thumbnailHeight + "'";
	}

	template += " onmouseover='thumbnailView(this, event)' onmouseout='thumbnailHide()' src='" + value + "'/>";
	
	return template; //HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
}

//AUIGRID 이미지 렌더러
function imageRenderer(rowIndex, columnIndex, value, headerText, item ,dataField){
	
	if(!value)	return "";
	
	var template = "<img class='imgView' style='max-width:100%;height:" + gridRowHeight + ";' ";
	
	if(item.thumbnailWidth && item.thumbnailHeight){
		template += "data-width='" + item.imgWidth + "' data-height='" + item.imgHeight + "'";
	}

	template += " onmouseover='imgView(this, event)' onmouseout='imgHide()' onclick='imgClick(this)' src='" + value + "'/>";
	
	return template; //HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
}

//AUIGRID 이미지및 다운로드 DWG,PDF
function imageDWGRenderer(rowIndex, columnIndex, value, headerText, item ,dataField){
	
	if(!value)	return "";
	var appOid = item.dwgAppOid;
	var holderOid = item.oid;
	
	if(!appOid || appOid=="") return "";
	var template = "<img class='imgView' style='max-width:100%;height:20;' ";
	
	if(item.thumbnailWidth && item.thumbnailHeight){
		template += "data-width='30' data-height='30'";
	}
	
	
	//console.log("appOid =" + appOid +",holderOid=" + holderOid);
	template += " onclick=publishDown('"+appOid+"','"+holderOid+"') style='cursor:pointer;' src='" + value + "'/>";
	//console.log(template);
	return template; //HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
}

//AUIGRID 이미지및 다운로드 DWG,PDF
function imagePDFRenderer(rowIndex, columnIndex, value, headerText, item ,dataField){
	
	if(!value)	return "";
	
	var appOid = item.pdfAppOid;
	var holderOid = item.oid;
	
	if(!appOid || appOid=="") return "";
	
	var template = "<img class='imgView' style='max-width:100%;height:20;' ";
	
	if(item.thumbnailWidth && item.thumbnailHeight){
		template += "data-width='20' data-height='20'";
	}

	//template += "  src='" + value + "'/>";
	template += " onclick=publishDown('"+appOid+"','"+holderOid+"') style='cursor:pointer;' src='" + value + "'/>";
	return template; //HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
}


function publishDown(appOid,holderOid){
	
	var url = getURLString("/content/publishDownload")+"?appOid="+appOid+"&holderOid="+holderOid;
	//alert(url);
	//console.log("publishDown = " + url);
	location.href = url;
}


function fileDownloadRenderer(rowIndex, columnIndex, value, headerText, item ,dataField){
	
	if(!value)	return "";
	
	var template = "<a href=javascript:fileDownloadTarget('" + item.downloadUrl + "')>" + item.fileName + "</a>";
	
	return template; //HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
}

function thumbnailView(_this, e){
	
	var imgTarget = $(_this).clone();
	$(imgTarget).removeClass("thumView");
	$(imgTarget).removeAttr("onmouseover");
	$(imgTarget).removeAttr("onmouseout");
	$(imgTarget).removeAttr("onclick");
	
	var offset = $(e.target).offset();
	
	var width = $(imgTarget).data("width");
	var height = $(imgTarget).data("height");
	
	if(!width && !height){
		
		width = $(imgTarget).prop("naturalWidth");
		height = $(imgTarget).prop("naturalHeight");
	}
	
	$(imgTarget).width(width);
	$(imgTarget).height(height);
	
	$("body").append($("<div/>", {
		id: "thumViewWindow",
		style: "position: absolute;top:0;left:0;z-index:999;"
	}));
	
	$("#thumViewWindow").html(imgTarget);
	
	$("#thumViewWindow img").each(function(){
		
		var maxWidth = 300;
		var maxHeight = 300;
	    var ratio = 0;  // Used for aspect ratio
	    
	    width = $(this).width();    // Current image width
	    height = $(this).height();  // Current image height

	    // Check if the current width is larger than the max
	    if(width > maxWidth){
	        ratio = maxWidth / width;   // get ratio for scaling image
	        $(this).css("width", maxWidth); // Set new width
	        $(this).css("height", height * ratio);  // Scale height based on ratio
	        height = height * ratio;    // Reset height to match scaled image
	    }

	    width = $(this).width();    // Current image width
	    height = $(this).height();  // Current image height

	    // Check if current height is larger than max
	    if(height > maxHeight){
	        ratio = maxHeight / height; // get ratio for scaling image
	        $(this).css("height", maxHeight);   // Set new height
	        $(this).css("width", width * ratio);    // Scale width based on ratio
	        width = width * ratio;    // Reset width to match scaled image
	    }
	});
	
	if(offset.top < 300){
		$("#thumViewWindow").css("top", offset.top + 25);
	}else{
		if(height < 300){
			$("#thumViewWindow").css("top", offset.top - height + gridRowHeight);
		}else{
			$("#thumViewWindow").css("top", offset.top - 150);
		}
	}
	
	var windowWidth = $(window).width();
	
	if(windowWidth < offset.left + 300){
		$("#thumViewWindow").css("left", offset.left - 300);
	}else{
		$("#thumViewWindow").css("left", offset.left + 60);
	}
}

function imgView(_this, e){
	
	var imgTarget = $(_this).clone();
	$(imgTarget).removeClass("imgView");
	$(imgTarget).removeAttr("onmouseover");
	$(imgTarget).removeAttr("onmouseout");
	$(imgTarget).removeAttr("onclick");
	
	var offset = $(e.target).offset();
	
	var width = $(imgTarget).data("width");
	var height = $(imgTarget).data("height");
	
	if(!width && !height){
		
		width = $(imgTarget).prop("naturalWidth");
		height = $(imgTarget).prop("naturalHeight");
	}
	
	$(imgTarget).width(width);
	$(imgTarget).height(height);
	
	$("body").append($("<div/>", {
		id: "imgViewWindow",
		style: "position: absolute;top:0;left:0;z-index:999;"
	}));
	
	$("#imgViewWindow").html(imgTarget);
	
	$("#imgViewWindow img").each(function(){
		
		var maxWidth = 300;
		var maxHeight = 300;
	    var ratio = 0;  // Used for aspect ratio
	    
	    width = $(this).width();    // Current image width
	    height = $(this).height();  // Current image height

	    // Check if the current width is larger than the max
	    if(width > maxWidth){
	        ratio = maxWidth / width;   // get ratio for scaling image
	        $(this).css("width", maxWidth); // Set new width
	        $(this).css("height", height * ratio);  // Scale height based on ratio
	        height = height * ratio;    // Reset height to match scaled image
	    }

	    width = $(this).width();    // Current image width
	    height = $(this).height();  // Current image height

	    // Check if current height is larger than max
	    if(height > maxHeight){
	        ratio = maxHeight / height; // get ratio for scaling image
	        $(this).css("height", maxHeight);   // Set new height
	        $(this).css("width", width * ratio);    // Scale width based on ratio
	        width = width * ratio;    // Reset width to match scaled image
	    }
	});
	
	if(offset.top < 300){
		$("#imgViewWindow").css("top", offset.top + 25);
	}else{
		if(height < 300){
			$("#imgViewWindow").css("top", offset.top - height + gridRowHeight);
		}else{
			$("#imgViewWindow").css("top", offset.top - 150);
		}
	}
	
	var windowWidth = $(window).width();
	
	if(windowWidth < offset.left + 300){
		$("#imgViewWindow").css("left", offset.left - 300);
	}else{
		$("#imgViewWindow").css("left", offset.left + 60);
	}
}

function thumbnailHide(){
	$("#thumViewWindow").remove();
}

function imgHide(){
	$("#imgViewWindow").remove();
}

function imgClick(_this){
	
	var width = $(_this).prop('naturalWidth');
	var height = $(_this).prop('naturalHeight');

	var url = $(_this).attr("src");
	
	var isChrome = window.chrome;
	
	width = width + 20;
	height = height + 20;
	
	var screenWidth = (screen.availWidth / 2) - (width / 2);
	var screenHeight = (screen.availHeight / 2) - (height / 2);
	
	var popup = open("", "img", "width=" + width + ",height=" + height + ",top=" + screenHeight + ",left=" + screenWidth);
	
	$(popup.window.document).find("body").html("");
	if(popup.window.document.body.clientWidth < width || popup.window.document.body.clientHeight < height){
		var popupWidth = popup.window.document.body.clientWidth;
		var popupRate = (popupWidth - 30) / width;
		
		height = height * popupRate;
		width = width * popupRate
	}
	
	popup.window.document.write("<img src='" + url + "' height='" + height + "' width='" + width + "' style='cursor:pointer;' onclick='self.close()'>");
}

// 셀 포커스 함수 [2022.11.04 shjeong]
function focusOneCell(gridID,rowIndex,columnIndex){
	AUIGrid.setSelectionBlock(gridID, rowIndex, rowIndex, columnIndex, columnIndex);
}

// ID 값으로 rowindex, columnindex 가져오기 [2022.11.04 shjeong]
function getCellIndex(gridID, uid, headerName){
	let rowIndexNum = AUIGrid.rowIdToIndex(gridID, uid);
	let columnIndexNum =AUIGrid.getColumnIndexByDataField(gridID, headerName);
	return {rowIndex:rowIndexNum,columnIndex:columnIndexNum};
}
