/**
 *  기본 js 기능
 */
/**************************************************************
*                      url 기본 세팅
****************************************************************/
var webContext = "worldex";
var getURLString = function(arg1) {
	var url = "/Windchill/" + webContext + arg1;
	
	return url;
}

function openView(oid, width, height) {
	if(width == null) {
		width = 'full';
		height = 'full';
	}
	var url = getViewURL(oid);
	if(url != "") {
		if( oid.indexOf("EPMDocument") > 0) {
			openPopup(url, oid, 1124, 600);
		}else if( oid.indexOf("WTDocument") > 0){
			openPopup(url, oid, 1124, 600);
		}else if( oid.indexOf("WTPart") > 0){
			openPopup(url, oid, 1124, 600);
		}else if( oid.indexOf("Notice") > 0){
			location.href = url;
		}else if( oid.indexOf("Department") > 0){
			openPopup(url, oid, 700, 300);
		}else if( oid.indexOf("MultiApproval") > 0) {
			openPopup(url, oid, 1024, 600);
		}else{
			openPopup(url, oid, 1024, 600);
		}
	}
}

function getViewURL(oid) {
	
	var url = "";
	if( oid.indexOf("WTDocument") > 0) {
		url = getURLString("/doc/viewDoc");
	} else if( oid.indexOf("EPMDocument") > 0) {
		url = getURLString("/epm/viewEpm");
	} else if( oid.indexOf("WTPart") > 0) {
		url = getURLString("/part/viewPart");
	} else if( oid.indexOf("Notice") > 0) {
		url = getURLString("/workspace/viewNotice");
	} else if( oid.indexOf("Department") > 0) {
		url = getURLString("/department/viewDepartment");
	}else if( oid.indexOf("MultiApproval") > 0) {
		url = getURLString("/multi/viewMulti");
	}
	if(url != "") return url+"?oid="+oid;
	return "";
}