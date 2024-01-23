// #################### 공통 - 윈도우 팝업 관련 ####################

/**
 * GET 방식의 queryString 이 추가된 문자열을 얻는다
 * 
 * @param baseURL
 * @param paramObj
 * @return
 */

var agent = navigator.userAgent.toLowerCase();
var language;

if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
	// IE에서 작동
	  $.ajax({
	    url: "http://ajaxhttpheaders.appspot.com",
	    dataType: 'jsonp',
	    success: function(headers) {
	      language = headers['Accept-Language'];
	      language = language.substr(0,2);
	    }
	  });
}else {
	language = navigator.language
}

function createQueryStringURL(baseURL, paramObj) {

	var url = baseURL;

	var queryString = "";
	if (paramObj != null) {
		for (var attrName in paramObj) {
			if (queryString != "") {
				queryString += "&";
			}
			if (Object.prototype.toString.call(paramObj[attrName]) == "[object Array]") {
				var arrayQueryString = "";
				for (var i = 0; i < paramObj[attrName].length; i++) {
					if (arrayQueryString != "") {
						arrayQueryString += "&";
					}
					arrayQueryString += attrName + "=" + encodeURIComponent(paramObj[attrName][i]);
				}
				queryString += arrayQueryString;
			} else {
				queryString += attrName + "=" + encodeURIComponent(getPopupParamValue(paramObj[attrName]));
			}
		}
	}

	if (queryString != "") {
		if (url.indexOf("?") < 0) {
			url += "?" + queryString;
		} else {
			url += "&" + queryString;
		}
	}

	return url;
}

/**
 * @param sURL 주소 required
 * @param sName Window이름 required
 * @param nWidth 넓이 required
 * @param nHeight 높이 required
 * @param bMoveCenter optional
 * @param bStatus optional
 * @param bScrollbars optional
 * @param bResizable optional
 * @return 새로운 Window 주소값
 */
function openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus,
		bScrollbars, bResizable) {
	var sName = (sName == null) ? "" : sName;
	var sStatus = (bStatus == null) ? "0" : ((bStatus) ? "1" : "0");
	var sScrollbars = (bScrollbars == null) ? "0" : ((bScrollbars) ? "1" : "0");
	var sResizable = (bResizable == null) ? "0" : ((bResizable) ? "1" : "0");

	var sFeatures = "toolbar=0,location=0,menubar=0" + ",status=" + sStatus
			+ ",scrollbars=" + sScrollbars + ",resizable=" + sResizable
			+ ",width=" + nWidth + ",height=" + nHeight;
	if (bMoveCenter != null && bMoveCenter) {
		sFeatures += ",top=" + Math.ceil((window.screen.height - nHeight) / 2);
		sFeatures += ",left=" + Math.ceil((window.screen.width - nWidth) / 2);
	}
	return window.open(sURL, sName, sFeatures);
}

/**
 * 팝업에 파라메터를 추가해서 띄웁니다. 사용법은 아래와 같습니다.
 * 
 * var popupRef = openPopupPro({
 *     url : "/Windchill/extcore/viewCreatePart.jsp",
 *     name : "test",
 *     width : 800,
 *     height : 600,
 *     moveCenter : true,
 *     status : true,
 *     scrollbars : true,
 *     resizable : true,
 *     
 *     method : "post",
 *     param : {
 *         oid : "wt.part.WTPart:12345"
 *     },
 *     callbackName : "opener.callbackMyTest",
 *     callbackObj : {
 *         targetId : "partNumber"
 *     }
 * });
 * 
 * @param config 팝업설정
 * @return 팝업 윈도우 레퍼런스
 */
function openPopupPro(config) {
	
	// #. cofig 초기화
	if (config == null) {
		config = {};
	}
	
	var defaultWidth = 400;
	var defaultHeight = 300;
	
	// #. config 인자 초기화
	var url = (config.url == null) ? "" : config.url; 
	var name = (config.name == null || config.name == "") ? "CUSTOM_POPUP" : config.name;
	
	var width = (!isNaN(config.width)) ? config.width : defaultWidth;
	var height = (!isNaN(config.height)) ? config.height : defaultHeight;
	var status = (config.status !== false) ? "1" : "0";
	var scrollbars = (config.scrollbars !== false) ? "1" : "0";
	var resizable = (config.resizable !== false) ? "1" : "0";
	var moveCenter = (config.moveCenter !== false);
	
	var method = (config.method != null && config.method.toLowerCase() == "post") ? "post" : "get";
	var param = (config.param != null) ? config.param : {};
	var callbackName = (config.callbackName != null) ? config.callbackName : "";
	var callbackObj = config.callbackObj;
	
	// #. 팝업 특성 생성
	var popupFeatures = "toolbar=0,location=0,menubar=0" + ",status=" + status
			+ ",scrollbars=" + scrollbars + ",resizable=" + resizable
			+ ",width=" + width + ",height=" + height;
	if (moveCenter) {
		popupFeatures += ",top=" + Math.ceil((window.screen.height - height) / 2);
		popupFeatures += ",left=" + Math.ceil((window.screen.width - width) / 2);
	}
	
	// #. 전체 파라메터 객체 통합
	var allParam = {
		popup : true,
		callbackName : callbackName,
		callbackObj : callbackObj
	};
	for (var attrName in param) {
		allParam[attrName] = param[attrName];
	}
	
	// #. method 에 따라 팝업을 띄우기
	if (method == "post") {
		
		// #. 동적으로 임시 form 생성
		var popupForm = document.createElement("FORM");
		popupForm.method = "post";
		popupForm.action = url;
		
		for (var paramName in allParam) {
			var paramValue = allParam[paramName];
			if (paramValue != null) {
				if (Object.prototype.toString.call(paramValue) == "[object Array]") {
					for (var i = 0; paramValue.length; i++) {
						popupForm.appendChild(createPopupHiddenInput(paramName, paramValue[i]));
					}
				} else {
					popupForm.appendChild(createPopupHiddenInput(paramName, paramValue));
				}
			}
		}
		popupForm = document.body.appendChild(popupForm);
		
		// #. 팝업생성 후 폼 제출
		var popupRef = window.open("", name, popupFeatures);
		popupForm.target = name;
		popupForm.submit();
		
		// #. 임시폼 삭제
		document.body.removeChild(popupForm);
		
		return popupRef;
	} else {
		var targetURL = createQueryStringURL(url, allParam);
		return window.open(targetURL, name, popupFeatures);
	}
}

/**
 * INPUT 태그를 동적으로 생성한다
 * @param name
 * @param value
 */
function createPopupHiddenInput(name, value) {
	var hiddenInput = document.createElement("INPUT");
	hiddenInput.type = "hidden";
	hiddenInput.name = name;
	
	hiddenInput.value = getPopupParamValue(value);
	return hiddenInput;
}

/**
 * 파라메터로 사용될 값을 얻는다 (객체일 경우 JSON 문자열을 리턴한다)
 * @param obj
 * @returns
 */
function getPopupParamValue(obj) {
	if (Object.prototype.toString.call(obj) == "[object Object]") {
		return JSON.stringify(obj);
	} else {
		return obj;
	}
}

/**
 * 함수를 동적으로 호출한다. (IE9 이상에서만 사용)
 * 
 * @param funcName 호출될 함수명 (Ex. opener.setProductByPopup)
 * @param argsArray 호출시 넘겨야할 인자배열
 * @return
 */
function callFunction(funcName, argsArray) {
	try {
		var callbackFunc = eval(funcName);
		if (callbackFunc != null) {
			if (argsArray != null) {
				callbackFunc.apply(this, argsArray);
			} else {
				callbackFunc.apply(this);
			}
		}
	} catch (e) {
		alert(e);
	}
}

/**
 * 팝업에 사용되는 callbackName, params, callbackObj 이 붙은 파라메터를 생성한다
 * @param baseURL 기본 URL
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function appendCallbackParams(baseURL, callbackName, params, callbackObj) {
	if (params == null) {
		params = {};
	}
	if (callbackName != null && callbackName.length > 0) {
		params.callbackName = callbackName;
	}
	if (callbackObj != null) {
		//var callbackObjJSON = Ext.JSON.encode(callbackObj);
		var callbackObjJSON = JSON.stringify(callbackObj);
		params.callbackObj = callbackObjJSON;
	}
	return createQueryStringURL(baseURL, params);
}

/**
 * 콜백을 수행한다. 주로 popup 에서 사용됨
 * @param callbackName 콜백 함수명
 * @param selectedObj 선택객체
 * @param callbackObj 되돌려받을 객체
 */
function execCallback(callbackName, selectedObj, callbackObj) {
	var callbackFunc = null;
	try {
		if (callbackName != null && callbackName.length > 0) {
			callbackFunc = eval(callbackName);
		}
	} catch (e) {
		// ignore
	}
	if (callbackFunc != null) {
		var popupRef = self;
		callbackFunc.call(this, selectedObj, callbackObj, popupRef);
	}
}

//#################### 공용 공통 팝업 관련 ####################

/**
 * 공용 폴더트리 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonViewFolderTreePopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/worldex/project/viewFolderTree";
	if (params != null) {
		params.popup = "true"; 
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "viewFolderTreePopup";
	var nWidth = 500;
	var nHeight = 550;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 공용 부서트리 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonViewDepartmentTreePopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/portal/viewDepartmentTree";
	if (params != null) {
		params.popup = "true"; 
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "viewDepartmentTreePopup";
	var nWidth = 300;
	var nHeight = 400;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 비밀번호 변경팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonChangePasswordPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/mywork/updatePassword";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "changePasswordPopup";
	var nWidth = 700;
	var nHeight = 250;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

//#################### 공용 검색 팝업 관련 ####################

/**
 * 공용 사용자 검색팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchUserPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/worldex/project/searchUserPopup";
	if (params != null) {
		params.popup = "true"; 
	} else {
		params = { popup : "true" };
	}
	
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchUserPopup";
	var nWidth = 1300;
	var nHeight = 580;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * ERP 주소록 검색팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchErpAddrPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/portal/searchErpAddr";
	if (params != null) {
		params.popup = "true"; 
	} else {
		params = { popup : "true" };
	}
	
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchErpAddrPopup";
	var nWidth = 1000;
	var nHeight = 680;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 공용 부품검색 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchPartPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/worldex/part/searchPart";
	if (params != null) {
		params.popup = "true"; 
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchPartPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 공용 문서검색 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchDocPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/worldex/doc/searchDoc";
	if (params != null) {
		params.popup = "true"; 
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchDocPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 공용 ECR검색 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchECRPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/worldex/change/searchECR";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchECRPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

function openCommonSearchBMPartPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/BM/searchBM";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchBMPartPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

function openCommonSearchBMPartCarPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/BM/searchBM";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchBMPartPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}


/**
 * 공용 Drawing 검색 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchDrawingPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/worldex/epm/searchEpm";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchDrawingPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 공용 기종(프로젝트) 검색 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchProjectPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/worldex/project/searchProject";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchProjectPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * BOM요청서 검색 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchBomPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/part/searchBomReq";
	if (params != null) {
		params.popup = "true"; 
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchBomPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 품목요청서 검색 팝업을 연다
 * @param callbackName 콜백 js 함수명
 * @param params 파라메터
 * @param callbackObj 되돌려받을 js 객체
 */
function openCommonSearchPartReqPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/worldex/part/searchPart";
	if (params != null) {
		params.popup = "true"; 
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "searchPartPopup";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

//#################### 공용 상세 팝업 관련 ####################

/**
 * 결재이력 조회 팝업을 연다
 * @param oid 객체식별자
 */
function openCommonViewApprovalHistory(oid) {
	
	var sURL = "/Windchill/worldex/change/viewApprovalHistory";
	var params = {
		oid : oid,
		popup : "true"
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewApprovalHistory";
	var nWidth = 830;
	var nHeight = 600;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}
/**
 * 다운로드이력 조회 팝업을 연다
 * @param oid 객체식별자
 */
function openCommonViewDownloadHistory(oid) {
	
	var sURL = "/Windchill/kores/portal/viewDownloadHistory";
	var params = {
		oid : oid,
		popup : "true"
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewDownloadHistory";
	var nWidth = 830;
	var nHeight = 600;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 문서 상세조회 팝업을 연다
 * @param oid
 */
function openCommonViewDoc(oid) {
	
	var sURL = "/Windchill/kores/doc/viewDoc";
	var params = {
		oid : oid,
		popup : "true"
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewDoc";
	var nWidth = 900;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 도면이력 조회 팝업을 연다
 * @param oid 객체식별자
 */
function openCommonViewDrawingHistory(oid) {
	
	var sURL = "/Windchill/kores/drawing/viewDrawingHistory";
	var params = {
		oid : oid,
		popup : "true"
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewDrawingHistory";
	var nWidth = 830;
	var nHeight = 600;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * MBOM 보기(BOM 보기) 팝업을 연다
 * @param oid 객체식별자
 */
function openCommonViewBomPopup(oid) {
	
	var sURL = "/Windchill/kores/part/viewBom";
	var params = {
		oid : oid,
		popup : true
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewBom";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * BOM 품목 검색(상위품목, 하위품목) 팝업을 연다
 * @param oid 객체식별자
 * @param type 전개타입 (parent, child)
 */
function openCommonViewBomPartsPopup(oid, type) {
	
	if (type == null) {
		type = "endItem"; // default endItem
	}
	
	var sURL = "/Windchill/kores/part/viewBomParts";
	var params = {
		oid : oid,
		type : type,
		popup : true
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewBomParts";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * BOM 최종품목 검색 팝업을 연다
 * @param oid 객체식별자
 */
function openCommonViewEndItemsPopup(oid) {
	
	var sURL = "/Windchill/kores/part/viewEndItems";
	var params = {
		oid : oid,
		popup : true
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewEndItems";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * BOM 최종품목 검색 팝업을 연다
 * @param oid 객체식별자
 */
function openCommonViewEBomEndItemsPopup(oid) {
	
	var sURL = "/Windchill/kores/part/viewEBomEndItems";
	var params = {
		oid : oid,
		popup : true
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewEBomEndItems";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * EBOM 보기 (CAD 구조 보기) 팝업을 연다
 * @param oid EPMDocument 객체식별자
 */
function openCommonViewCadStructurePopup(oid) {
	
	var sURL = "/Windchill/kores/part/viewEBom";
	var params = {
		oid : oid,
		popup : true
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewEBom";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * ECR 보기 화면을 연다
 * @param oid 부품 객체식별자
 */
function openCommonViewECRPopup(oid, callbackName){
	var sURL = "/Windchill/kores/change/viewECR";
	var params = {
		popup : "true",
		oid : oid
	};
	var callbackObj = null;
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);

	var sName = "viewECR";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * ECO 보기 화면을 연다
 * @param oid 부품 객체식별자
 */
function openCommonViewECOPopup(oid, callbackName){
	var sURL = "/Windchill/kores/change/viewECO";
	var params = {
		popup : "true",
		oid : oid
	};
	var callbackObj = null;
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);

	var sName = "viewECO";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}


/**
 * 품명및규격(표준DESC) 생성 팝업을 연다
 * @param callbackName
 * @param params
 * @param callbackObj
 */
function openCommonGeneratePartNameSpecPopup(callbackName, params, callbackObj) {
	var popupRef = openPopupPro({
		url : "/Windchill/kores/part/generatePartNameSpec",
		name : "generatePartNameSpec",
		width : 1200,
		height : 500,
		moveCenter : true,
		status : true,
		scrollbars : true,
		resizable : true,
		
		method : "post",
		param : params,
		callbackName : callbackName,
		callbackObj : callbackObj
	});
	return popupRef;
}

/**
 * 부품요청서 엔트리 수정창을 띄운다
 * @param callbackName
 * @param params
 * @param callbackObj
 * @returns
 */
function openCommonUpdatePartReqEntryPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/part/updatePartReqEntry";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "updatePartReqEntry";
	var nWidth = 1200;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 메뉴얼
 * @param callbackName
 * @param params
 * @param callbackObj
 * @returns
 */
function openMenualPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/portal/viewMenual";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "viewMenual";
	var nWidth = 700;
	var nHeight = 500;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * viewInstallFile
 * @param callbackName
 * @param params
 * @param callbackObj
 * @returns
 */
function openInstallFilePopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/portal/viewInstallFile";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "viewInstallFile";
	var nWidth = 980;
	var nHeight = 500;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}


/**
 * viewInstallFile
 * @param callbackName
 * @param params
 * @param callbackObj
 * @returns
 */
function openFaqPopup(callbackName, params, callbackObj) {
	var sURL = "/Windchill/kores/portal/viewFaq";
	if (params != null) {
		params.popup = "true";
	} else {
		params = { popup : "true" };
	}
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "viewFaq";
	var nWidth = 700;
	var nHeight = 500;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 협조요청 보기 화면을 연다
 * @param oid 부품 객체식별자
 */
function openCommonViewCooperPopup(oid, callbackName){
	var sURL = "/Windchill/kores/mywork/viewCooperation";
	var params = {
		popup : "true",
		oid : oid
	};
	var callbackObj = null;
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);

	var sName = "viewCooperation";
	var nWidth = 900;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * CreoView 팝업을 연다(oid가 여러개인경우)
 * @param oid EPMDocument 객체식별자
 */
function openCommonViewCreoFromClipboardPopup(oids) {
	
	var sURL = "/Windchill/kores/portal/viewClipboardListCreoView";
	var params = {
		oid : oids,
		popup : true
	};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "viewCreoViewFromClipboard";
	var nWidth = 800;
	var nHeight = 650;
	var bMoveCenter = true;
	var bStatus = false;
	var bScrollbars = false;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * CreoViewWVS창을 연다
 */
function openCreoViewWVSPopup(oid, isState) {
	if(isState=="true"){
		alert("승인완료된 도면이 아닙니다.");
	}
	// #. CreoView창을 연다
	var sURL = "/Windchill/wtcore/jsp/wvs/edrview.jsp?viewIfPublished=1&sendToPublisher=" + oid;
	
	var params = {};
	sURL = createQueryStringURL(sURL, params);
	
	var sName = "";
	var nWidth = 830;
	var nHeight = 600;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}

/**
 * 통보추가 팝업 생성
 * @param oid EChangeOrder2 객체식별자
 */
function openCreateNotify(oid, ecrOid, ecnOid){
	var sURL = "/Windchill/kores/change/createNotifyItem";
	
	var params = {
			oid : oid,
			ecrOid : ecrOid,
			ecnOid : ecnOid,
			popup : true
		};
		sURL = createQueryStringURL(sURL, params);

	var sName = "createNotifyItem";
	var nWidth = 1000;
	var nHeight = 500;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
	
}

/**
 * 양산이관배포 팝업을 연다
 * @param oid EPMDocument 객체식별자
 */
function openproductionDistributeDrawingPopup(oid) {
	
	var sURL = "/Windchill/kores/drawing/viewDistributeDrawing";
	
	var callbackName = "opener.callbackLoadViewDrawing";
	var params = {
		oid : oid,
		popup : true
	};
	var callbackObj = null;
	sURL = appendCallbackParams(sURL, callbackName, params, callbackObj);
	
	var sName = "viewDistributeDrawing";
	var nWidth = 800;
	var nHeight = 700;
	var bMoveCenter = true;
	var bStatus = true;
	var bScrollbars = true;
	var bResizable = true;

	return openWindow(sURL, sName, nWidth, nHeight, bMoveCenter, bStatus, bScrollbars, bResizable);
}