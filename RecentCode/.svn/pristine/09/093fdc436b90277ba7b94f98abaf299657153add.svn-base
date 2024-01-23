/**
 *  기본 js 기능
 */
var browserChecker = {
	chk : navigator.userAgent.toLowerCase()
}
browserChecker = {
	ie : browserChecker.chk.indexOf('msie') != -1,
	ie6 : browserChecker.chk.indexOf('msie 6') != -1,
	ie7 : browserChecker.chk.indexOf('msie 7') != -1,
	ie8 : browserChecker.chk.indexOf('msie 8') != -1,
	ie9 : browserChecker.chk.indexOf('msie 9') != -1,
	ie10 : browserChecker.chk.indexOf('msie 10') != -1,
	ie11a : browserChecker.chk.indexOf('trident') != -1,
	opera : !!window.opera,
	safari : browserChecker.chk.indexOf('safari') != -1,
	safari3 : browserChecker.chk.indexOf('applewebkir/5') != -1,
	mac : browserChecker.chk.indexOf('mac') != -1,
	chrome : browserChecker.chk.indexOf('chrome') != -1,
	firefox : browserChecker.chk.indexOf('firefox') != -1
}

window.convertNumberFromDate = function(value){
	
	var nDate = new Date();

	value = value.replace(/-/g,"");
	value = value.replace(/\//g,"");
	
	if(isNaN(value)){
		return "Invalid Date";
	}
	
	var yy = "";
	var MM = "";
	var dd = "";

	if (value.length > 6) {
		
		yy = value.left(4).number();
		MM = value.substr(4, 2).number();
		dd = value.substr(6, 2).number();
		
	}else if (value.length > 4) {
		
		yy = "20" + value.left(2).number();
		MM = value.substr(2, 2).number();
		dd = value.substr(4, 2).number();
	}
	else if (value.length > 2) {
		
		yy = nDate.getFullYear();
		MM = value.substr(0, 2).number();
		dd = value.substr(2, 2).number();
	}
	else {
		yy = nDate.getFullYear(); //va.left(4).number();
		MM = nDate.getMonth() + 1;
		dd = value.substr(0, 2).number();
		
		if(MM == 13){
			MM = 12;
		}
	}
	
	if (MM.toString().length < 2){
		MM = 0 + MM.toString();
	}
	
	if (dd.toString().length < 2){
		dd = 0 + dd.toString();
	}
	
	value = yy + "-" + MM + "-" + dd;
	
	var chkValue = new Date(value);
	
	if(chkValue.toString().indexOf("Invalid") >= 0){
		value = "Invalid Date";
	}
	
	return value;
}
var intervalList = new Array();
var progressInterval = new Array();
window.startProgress = function() {
	var intervalId = setInterval(function() {

		var xmlHttp;
		function srvTime(url) {
			if (url == undefined) {
				url =location.origin;
			}
			
			if (window.XMLHttpRequest) {
				xmlHttp = new XMLHttpRequest();
			} else if (window.ActiveXObject) {
				xmlHttp = new ActiveXObject('Msxml2.XMLHTTP');
			} else {
				return null;
			}
			
			xmlHttp.open('HEAD', url, false); //헤더 정보만 받기 위해 HEAD방식으로 요청.
			xmlHttp.setRequestHeader("Content-Type", "text/html");
			xmlHttp.send('');

			return xmlHttp.getResponseHeader("Date"); //받은 헤더정보에서 Date 속성을 리턴.
		}

		var serverTime = new Date(srvTime());

		$("#progressDate").html( "현재 시간 : " + serverTime.toLocaleString());
		
	}, 500);
	
	progressInterval.push(intervalId);

	$("#progressWindow").show();
}

window.endProgress = function() {
	
	for(var i = 0; i < progressInterval.length; i++){
		clearInterval(progressInterval[i]);
	}
	
	progressInterval = new Array();
	
	$("#progressWindow").hide();
}

/**************************************************************
*                      쿠키 생성
****************************************************************/
window.setCookie = function(cName, cValue, cDay) {
	var expire = new Date();
	expire.setDate(expire.getDate() + cDay);
	cookies = cName + '=' + escape(cValue) + '; path=/ '; // 한글 깨짐을 막기위해 escape(cValue)를 합니다.
	if (typeof cDay != 'undefined')
		cookies += ';expires=' + expire.toGMTString() + ';';
	document.cookie = cookies;
}

/**************************************************************
*                      쿠키 가져오기
****************************************************************/
window.getCookie = function(cName) {
	cName = cName + '=';
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cName);
	var cValue = '';
	if (start != -1) {
		start += cName.length;
		var end = cookieData.indexOf(';', start);
		if (end == -1)
			end = cookieData.length;
		cValue = cookieData.substring(start, end);
	}
	return unescape(cValue);
}

/**************************************************************
*                      컨텐츠 로드
****************************************************************/
window.loadContent = function(targetId, loadUrl, cbMethod) {

	$("#" + targetId).load(loadUrl, function(response, status, xhr) {
		
		if (status == "error") {
			openNotice(xhr.status + " : " + xhr.statusText);
		}
		$("form").attr("onsubmit", "return false");
		
		if(cbMethod){
			cbMethod(response, status, xhr);
		}
	});
}

/**************************************************************
*                      confirm 대용
****************************************************************/
window.openConfirm = function(str, cbMethod) {

	if (str != null && str != "") {

		if(confirm(str)){
			cbMethod();
		}
	}
}

/**************************************************************
*                      alert 대용
****************************************************************/
window.openNotice = function(str) {
	openNotice(str, null, null);
}
/**************************************************************
*                      notice 표시 후 링크 이동
****************************************************************/
window.openNotice = function(str, url, oid) {
	if (str != null && str != "") {
		
		alert(str);
		
		if (url == "close") {
			
			window.close();
			
		} else if (url == "closeAndReload") {
			
			var cnt = 0;
			try{
				$(opener.document).find("iframe").each(function() {

					if ($(this).attr("id") == "downloadFrame")
						return;

					$(this).attr("src", $(this).attr("src"));
					cnt++;
				});

				if (cnt == 0) {
					opener.location.reload();
				}
			}catch(e){
				console.log(e);
			}

			window.close();

		} else if (url == "Reload") {
			
			location.reload();
			
		} else if (url != null) {
			//if(confirm("이동 하시겠습니까?")){
				if (url.indexOf("pLocation") >= 0) {
					
					url = url.substring(url.indexOf(":") + 1, url.length);
					
					if(parent){
						parent.location.href= url;
					}
				}else{
					location.href = url;
				}
			//}
			
		}
	}
}

/**************************************************************
*                      팝업창 오픈(GET)
****************************************************************/
window.openPopup = function(url, target, width, height) {

	if(width == null) {
		width = 1000;
	}
	if(height == null) {
		height = 700;
	}
	if(target == null) {
		target = "popup";
	}
	
	var screenWidth = (screen.availWidth / 2) - (width / 2); 
  	screenWidth += window.screenLeft;  //듀얼 모니터
  	var screenHeight =0; //(screen.availHeight / 2) - (height / 2);
  	screenWidth = 0;
  
	//var screenWidth = (screen.availWidth / 2) - (width / 2);
	//var screenHeight = (screen.availHeight / 2) - (height / 2);
	
	var popup = window.open(url, target,
			"location=no,status=no,resizable=yes,scrollbars=yes,width=" + width
					+ ",height=" + height + ",top=" + screenHeight + ",left="
					+ screenWidth);

	if (window.focus) {
		setTimeout(function() {
			popup.focus();
		}, 0);
	}

	return popup;
}

/**************************************************************
*                      팝업창 센터 오픈(GET)
****************************************************************/
window.openPopupCenter = function(url, target, width, height) {

	if(width == null) {
		width = 1000;
	}
	if(height == null) {
		height = 700;
	}
	if(target == null) {
		target = "popup";
	}
	
	var screenWidth = (screen.availWidth / 2) - (width / 2);
	var screenHeight = (screen.availHeight / 2) - (height / 2);
	
	var popup = window.open(url, target,
			"location=no,status=no,resizable=yes,scrollbars=yes,width=" + width
					+ ",height=" + height + ",top=" + screenHeight + ",left="
					+ screenWidth);

	if (window.focus) {
		setTimeout(function() {
			popup.focus();
		}, 0);
	}

	return popup;
}

/**************************************************************
*                      팝업창 오픈(POST)
****************************************************************/
/*window.openPopup = function(url, target, param, width, height) {

	if(width == null) {
		width = 1000;
	}
	if(height == null) {
		height = 600;
	}
	
	width += 15;
	height += 15;

	var screenWidth = (screen.availWidth / 2) - (width / 2);
	var screenHeight = (screen.availHeight / 2) - (height / 2);

	var popup = window.open("", target,
			"location=no,status=no,resizable=yes,scrollbars=yes,width=" + width
					+ ",height=" + height + ",top=" + screenHeight + ",left="
					+ screenWidth);

	var form = document.createElement("form") ;
    form.setAttribute("method", "post");
    form.setAttribute("action", url);
    form.setAttribute("target", target);
    
    
    for (var key in param) {
    	var input = document.createElement("input");
    	
    	input.type = "hidden";
    	input.name = key;
    	input.id = key;
    	input.value = param[key];
    	
    	form.appendChild(input);
    }
    
    document.body.appendChild(form);

    form.submit();
	
	return popup;
}*/

/**************************************************************
*                      팝업창 이동(POST)
****************************************************************/
/*window.movePopup = function(url, target, param) {

	var form = document.createElement("form") ;
	form.setAttribute("method", "post");
	form.setAttribute("action", url);
	form.setAttribute("target", target);
  
	for (var key in param) {
		var input = document.createElement("input");
  	
		input.type = "hidden";
		input.name = key;
		input.id = key;
		input.value = param[key];
  	
		form.appendChild(input);
	}
  
	document.body.appendChild(form);

	form.submit();
}*/

/**************************************************************
*                      location 이동
****************************************************************/
window.moveLocation = function(arg1) {
	
	var url = "/Windchill/" + webContext + arg1;
	
	location.href = url;
}

/**************************************************************
*                      string uppercase 이동
****************************************************************/
function strToUpperCase(sobj) {
	$(sobj).val($(sobj).val().toUpperCase());
}

/**************************************************************
*                      이전 날짜 자동 입력
****************************************************************/
function setDate(id){
	var nowDate = new Date();
	var date = nowDate.getTime() - (1 * 24 * 60 * 60 * 1000);
	nowDate.setTime(date);
	
	var toDay = nowDate.getFullYear() + "/" + ("0" + (nowDate.getMonth()+1)).slice(-2) + "/" + ("0"+nowDate.getDate()).slice(-2);
	$("#" + id).val(toDay);
}

/**************************************************************
*                      GET FORM PARAMS (author : sangylee)
****************************************************************/
function getFormParams(formId, params) {
	var arrayParam = $("#" + formId).serializeArray();
	var paramCheck = new Array();
	
	if(params == null){
		params = new Object();
	}

	$.each(arrayParam, function(idx, obj) {

		var thisName = obj.name;
		var thisVal = obj.value;
		var isArray = false;
		
		//배열 파라미터를 하나로 합침
		$.each(params, function(key, value) {
			if (thisName == key) {
				var array = new Array();
				
				if(value instanceof Array) {
					value.push(thisVal);
				} else {
					if(!isEmpty(thisVal)){
						array.push(value);
						array.push(thisVal);
						
						params[thisName] = array;
					}
				}
				isArray = true;
			}
		});

		if (!isArray) {
			params[thisName] = thisVal;
		}
	});
	
	return params;
}

function onlyNum(e){

	var key

	if(window.event) // IE
	{
		key = e.keyCode
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		key = e.which
	}

	 if(!(key==8||key==9||key==13||key==46||key==144||(key>=48&&key<=57)||key==110||key==190)){
		return false;
	}
}

/*************************************************************
					Error Class [2022.11.04 shjeong]
**************************************************************/
class validationError extends Error {
	  constructor(item, ...params) {
	    super(...params);
	    this.item = item;
	    this.name = 'validationError';
	}
}

/*************************************************************
*		스크립트에서 null 체크
**************************************************************/
function isEmpty(str){
    
    if(typeof str == "undefined" || str == null || str == "")
        return true;
    else
        return false ;
}

/*************************************************************
	    엘리먼트 id로 value 가져오기
**************************************************************/
function getElementValue(id){
	return document.getElementById(id).value.trim();
}

function getAllChildElements(parentElement) {
    const childElements = [];

    function traverse(element) {
        const childNodes = element.childNodes;

        for (let i = 0; i < childNodes.length; i++) {
            const node = childNodes[i];

            if (node.nodeType === 1) {
                childElements.push(node);

                traverse(node);
            }
        }
    }

    traverse(parentElement);

    return childElements;
}

/*************************************************************
	     	채번 코드 리스트 뽑아오기
**************************************************************/
function getCreateNumberPropList(codeType) {
	var url	= getURLString("/common/getCreateNumberPropCodeList");
	
	var param = new Object();
	
	param["codeType"] = codeType;

	var data = ajaxCallServer(url, param, null);
	
	var list = data.list; //key,value
	
	return list;
}

function checkboxConfirm(name){
	let result = false;
	document.querySelectorAll('input[name='+name+']').forEach((item) => {
	console.log('item.type',item.type)
	console.log('item.checked',item.checked)
		if((item.type=='checkbox' || item.type=='radio') && item.checked){
			result = true;
		}
	})
	console.log(result)
	return result;
}