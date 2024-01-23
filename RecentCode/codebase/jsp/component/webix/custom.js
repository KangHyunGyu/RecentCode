function custom_webix_thumbnailTemplate(obj, common, value, col, index){
	
	if(!value)	return "";
	
	/*var template = "<img class='thumView' style='max-width:100%;height:" + gridRowHeight + ";' ";*/
	var template = "<img class='thumView' style='max-width:100%;height:22px;' ";
	
	if(obj.thumbnailWidth && obj.thumbnailHeight){
		template += "data-width='" + obj.thumbnailWidth + "' data-height='" + obj.thumbnailHeight + "'";
	}

	template += " onmouseover='thumbnailView(this, event)' onmouseout='thumbnailHide()' src='" + value + "'/>";
	
	return template; //HTML 템플릿 반환..그대도 innerHTML 속성값으로 처리됨
}