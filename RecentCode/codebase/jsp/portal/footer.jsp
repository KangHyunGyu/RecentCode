<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 각 화면 개발시 Tag 복사해서 붙여넣고 사용할것 -->    
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core"			%>
<%@ taglib prefix="e3ps" 	uri="/WEB-INF/tlds/e3ps-functions.tld"%>
<script>
var gridHeaderHeight = 40;
var gridRowHeight = 35;
var gridNoDataMessage = "${e3ps:getMessage('출력할 데이터가 없습니다.')}";
$(document).ready(function(){
	
	
	//window.resizeTo( strWidth, window.innerHeight );
	  
	//공통 : webix Resize
	$(window).resize(function(){
		resizeGrid();
	});
	

	// 팝업창일 때 reSize
	if(opener != null){
		//popupResize2();
	}
	
	//일반 창 : subMenu initiallizing
	if(getCookie('subMenuHidden')){
		//switchMenu();
	}
	
	//일반 창 : save selected menu cookie
	$(".linkMenu").click(function(){
		setCookie("menuCookie", $(this).attr("href"), 999999999);
		
	});
	$(".mainMenu").click(function(){
		setCookie("menuCookie", $(this).attr("href"), 999999999);
		
	});
	
	//일반 창 : set background at the selected menu
	var isMenuActive = false;
	$(".linkMenu").each(function(){
		
		var locationArray = location.pathname.split("/");
		var locationName = locationArray[locationArray.length-1] + location.search;
		
		var hrefLocation = $(this).attr("href").substring($(this).attr("href").indexOf("'")+1,$(this).attr("href").lastIndexOf("'"));
		var hrefArray = hrefLocation.split("/");
		var hrefName = hrefArray[hrefArray.length-1];
		
		if(hrefName != "" && locationName == hrefName){
			var menuLocation = "";
			$(".mainMenu").each(function(){
				var mainMenuName = locationArray[locationArray.length-2];
				
				var mainMenuHrefArray = $(this).attr("href").split("/");
				
				if(mainMenuHrefArray.length > 1) {
					var mainMenuHrefName = mainMenuHrefArray[mainMenuHrefArray.length-2];
					
					if(mainMenuName == mainMenuHrefName){
						menuLocation = $(this).children(".menuTitle").text();
					}
				}
			});
			if($(this).hasClass("subLinkMenu")) {
				$(this).css("font-weight", "bold");
// 				$(this).css("color", "#004a9c");
				$(this).css("color", "#74AF2A");
				$(this).parent().parent().slideDown();
				$(this).parent().parent().prev().children().addClass("check_off");
				$(this).parent().parent().prev().children().removeClass("on");
				
				menuLocation = menuLocation + " > " + $(this).parent().parent().prev().children().text() + " > " + $(this).text();
			} else {
				$(this).addClass("in");
				menuLocation = menuLocation + " > " + $(this).text();
			}
			isMenuActive = true;
			
			$("#menuLocation").text(menuLocation);

		}
	});
	
	if(!isMenuActive){
		$(".linkMenu").each(function(){
			
			var locationArray = location.pathname.split("/");
			var locationName = locationArray[locationArray.length-1];
			
			var hrefLocation = $(this).attr("href").substring($(this).attr("href").indexOf("'")+1,$(this).attr("href").lastIndexOf("'"));
			var hrefArray = hrefLocation.split("/");
			var hrefName = hrefArray[hrefArray.length-1];
			
			if(hrefName != "" && locationName == hrefName){
				var menuLocation = "";
				$(".mainMenu").each(function(){
					var mainMenuName = locationArray[locationArray.length-2];
					
					var mainMenuHrefArray = $(this).attr("href").split("/");
					
					if(mainMenuHrefArray.length > 1) {
						var mainMenuHrefName = mainMenuHrefArray[mainMenuHrefArray.length-2];
						
						if(mainMenuName == mainMenuHrefName){
							menuLocation = $(this).children(".menuTitle").text();
						}
					}
				});
				if($(this).hasClass("subLinkMenu")) {
					$(this).css("font-weight", "bold");
// 					$(this).css("color", "#004a9c");
					$(this).css("color", "#74AF2A");
					$(this).parent().parent().slideDown();
					$(this).parent().parent().prev().children().addClass("check_off");
					$(this).parent().parent().prev().children().removeClass("on");
					
					menuLocation = menuLocation + " > " + $(this).parent().parent().prev().children().text() + " > " + $(this).text();
				} else {
					$(this).addClass("in");
					menuLocation = menuLocation + " > " + $(this).text();
				}
				isMenuActive = true;
				
				$("#menuLocation").text(menuLocation);

			}
		});
	}
	
	//공통 : doubleCalendar
	$(".datePicker").each(function(){
		var id = $(this).attr("id");

		$(this).after("<img class='pointer' src='/Windchill/jsp/portal/images/calendar_icon.png' name='" + id + "Btn' id='" + id + "Btn'>");
		$("#" + id + "Btn").css('vertical-align','middle');
		$("#" + id + "Btn").css('margin-left','-1px');
		$("#" + id + "Btn").after("<span class='resetDate pointer' data-remove-target='" + id + "'><img class='verticalMiddle' src='/Windchill/jsp/portal/images/delete_icon.png'></span>");
        
		$(".resetDate").click(function(){
			
			var target = $(this).data("remove-target");
			
			$("#" + target).val("");
		});
		
		var locale = {
			ko: {
				monthShort: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
				months: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
				daysShort: ["일","월","화","수","목","금","토"],
				days: ["일","월","화","수","목","금","토"]
			},
			en: {
				monthShort: ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],
				months: ["January","February","March","April","May","Jun","July","August","September","October","November","December"],
				daysShort: ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],
				days: ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
			}
		}
	
		var myCalendar = new dhtmlXCalendarObject({
			input:id,
			button:id+"Btn"
		});
		
		myCalendar.setDateFormat("%Y/%m/%d");
		myCalendar.setWeekStartDay(7);
		
		if("ENDDATE" == id) {
			var date = new Date();
			date.setDate(date.getDate() + 1);
			myCalendar.setInsensitiveRange(date, null);
		}
	});
	
	//공통 : autocomplete
	$(".searchUser").each(function(){
		var searchUser = $(this);
		
		var width = $(this).data("width");
		var valueToName = $(this).data("valuetoname");
		
		if(width == null) {
			width = "100%";
		}
		
		var url = getURLString("/common/searchUserAction");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					
					var param = new Object();
					param["name"] = params.term;
					param = JSON.stringify(param);
					return param;
					
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	var value = item.oid;
			            	if(valueToName) value = item.name;
			            	var name = item.name;
			            	if(searchUser.attr("id")=="receptionists") oid = item.id;
			              return { id: value,  text: name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert user Info (NAME)",
			minimumInputLength: 1,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
	//공통 : autocomplete
	$(".searchDepartment").each(function(){

		var searchUser = $(this);
		
		var width = $(this).data("width");
		var valueToName = $(this).data("valuetoname");
		
		if(width == null) {
			width = "100%";
		}
		
		var url = getURLString("/common/searchDepartmentAction");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					param["name"] = params.term;
					param = JSON.stringify(param);
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	var value = item.oid;
			            	if(valueToName) value = item.name;
			            	var name = item.name;
			              return { id: value, text: name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert department Info (NAME)",
			minimumInputLength: 1,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
	$(".multiSelect").each(function(){
        
        var id = $(this).attr("id");
        
        $(this).SumoSelect();
        
        $(".sumo_" + id).after("<img class='resetSelect pl5 pointer' data-remove-target='" + id + "' src='/Windchill/jsp/portal/images/delete_icon.png'>");
        
		$(".resetSelect").click(function(){
			
			var target = $(this).data("remove-target");
			
			$("#" + target)[0].sumo.unSelectAll();
		});
	});
	
	//공통 : 관련 Objcet autocomplete
	$(".searchRelatedObject").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/common/searchRelatedObject");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["number"] = params.term;
					param["object"] = $(this).data("param");
					
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	item.id = item.number;
			            	item.text = item.number +" / "+ item.name;
			              return item;
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Object Info (Number)",
			minimumInputLength: 3,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
	//공통 : numberCode autocomplete
	$(".searchCode").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/common/getNumberCodeListAutoComplete");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["value"] = params.term;
					param["codeType"] = $(this).data("codetype");
					param["endLevel"] = $(this).data("endlevel");
					param["roleCode"] = $(this).data("rolecode");
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
		            		return { id: item.code, text: item.code  + " : " + item.name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Code Info (CODE or NAME)",
			minimumInputLength: 1,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
	//공통 : numberCode autocomplete
	$(".searchCode2").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/common/getNumberCodeListAutoComplete");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["value"] = params.term;
					param["codeType"] = $(this).data("codetype");
					param["endLevel"] = $(this).data("endlevel");
					param["roleCode"] = $(this).data("rolecode");
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	if(item.description != "") {
			            		return { id: item.code, text: item.code  + " : " + item.name + " : " + item.description };
			            	}else {
			            		return { id: item.code, text: item.code  + " : " + item.name };
			            	}
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Code Info (CODE or NAME)",
			minimumInputLength: 1,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
	//공통 : 관련 Project autocomplete
	$(".searchRelatedProject").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/project/searchRelatedProject");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["number"] = params.term;
					
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	item.id = item.oid;
			            	item.text = item.code + " : " + item.name;
			              	return item;
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Project Info (Number or Name)",
			minimumInputLength: 2,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
	//공통 : 관련 Project autocomplete
	$(".searchRelatedIssue").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/issue/searchRelatedIssue");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["number"] = params.term;
					if($("#relatedProject").val() != null && $("#relatedProject").val().length > 0) {
						param["pjtNo"] = $("#relatedProject").val();
					}
					
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	item.id = item.issueNumber;
			            	item.text = item.issueNumber + " : " + item.issueName;
			              	return item;
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Issue Info (Number or NAME)",
			minimumInputLength: 3,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
	//공통 : 관련 ECR autocomplete
	$(".searchRelatedECR").each(function(){
		var id = this.id;

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/change/searchECRList");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["value"] = params.term;
					param["id"] = id;
					
					param = JSON.stringify(param);
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	if(searchObj){
			            		searchObj = data.list;
			            	}
			              return { id: item.oid, text: item.name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert ECR Info (Number or Name)",
			minimumInputLength: 3,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	//공통 : 관련 ECO autocomplete
	$(".searchRelatedECO").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/change/searchRelatedECO");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["value"] = params.term;
					
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			              return { id: item.oid, text: item.number  +" : " + item.title };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert ECO Info (Number or TITLE)",
			minimumInputLength: 3,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
	//공통 : 프로젝트 템플릿 autocomplete
	$(".searchTemplate").each(function(){

		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/project/template/searchRelatedTemplate");
		
		$(this).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					var param = new Object();
					
					param["value"] = params.term;
					
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			            	item.id = item.oid;
			            	item.text = item.code + " : " + item.name;
			              	return item;
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Template Info (Number or TITLE)",
			minimumInputLength: 0,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.text;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});

	// 즐겨찾기 여부
	var pathName = window.location.pathname;
	var search = window.location.search;
	var param = new Object();
	param["url"] = pathName+search;
	var url = getURLString("/favorite/isFavorite");
	ajaxCallServer(url, param, function(data){
		if(data.oid != ""){
			$("#favorite").attr("src", "/Windchill/jsp/portal/images/favorites_icon_r.png");
			$("#favorite").data("type", true);
			$("#favorite").data("oid", data.oid);
		}
	}, false);
	
	$("#progressBtn").click(function(){
		endProgress();
	});
});

//일반 창 : subMenu open and close
function switchMenu(){
	
    var smenu = document.getElementById("sub_menu");
    var button = document.getElementById("subMenuArrow");
    
    if('none' == smenu.style.display){
    	
        smenu.style.display="";
        button.src = "/Windchill/jsp/portal/images/s_menu_icon03.png";
        button.title = "서브메뉴 닫기";
        setCookie("subMenuHidden", false, -1);
        $(".bodyDiv").css("min-width", 1341);
    }else{
    	
        smenu.style.display="none";
        button.src = "/Windchill/jsp/portal/images/s_menu_icon04.png";
        button.title = "서브메뉴 열기";
        $(".bodyDiv").css("min-width", 1571);
		setCookie("subMenuHidden", true, 999999999);
		
    }

    resizeGrid();
}

//일반 창 : subMenu hide and show slide
function subMenuSlide(obj){
	if(obj.className == "on") {
		$(obj).removeClass("on");
		$(obj).parent().next().slideDown();
		$(obj).addClass("off");
	} else if(obj.className == "off"){
		$(obj).removeClass("off");
		$(obj).parent().next().slideUp();
		$(obj).addClass("on");
	} else if(obj.className == "check_on"){
		$(obj).removeClass("check_on");
		$(obj).parent().next().slideDown();
		$(obj).addClass("check_off");
	} else if(obj.className == "check_off"){
		$(obj).removeClass("check_off");
		$(obj).parent().next().slideUp();
		$(obj).addClass("check_on");
	}
}

//팝업 창 : popup include open close
function switchPopupDiv(btn){
	
	var bodyDiv = $(btn).parent().parent().parent().next();
	
	if($(btn).attr("src").indexOf("minus") != -1) {
		$(btn).attr("src", "/Windchill/jsp/portal/images/add_icon.png");	
	} else {
		$(btn).attr("src", "/Windchill/jsp/portal/images/minus_icon.png");
	}
	bodyDiv.toggle();
	
	//var rightBtnDiv =  $(btn).parent().parent().next();
	//rightBtnDiv.toggle();
	
	resizeGrid();
}

//검색조건 메뉴 : open close
function switchSearchMenu(btn){
	
	if($(btn).attr("src").indexOf("minus") != -1) {
		$(btn).attr("src", "/Windchill/jsp/portal/images/add_icon.png");	
	} else {
		$(btn).attr("src", "/Windchill/jsp/portal/images/minus_icon.png");
	}
	$(".pro_table").toggle();
	
	resizeGrid();
}

//상세검색조건 메뉴 : open close
function switchDetailBtn(){
	$(".switchDetail").toggle();
	
	resizeGrid();
}

//등록화면  : open close
function switchDiv(btn){
	
	var bodyDiv = $(btn).parent().parent().parent().next();
	
	if($(btn).attr("src").indexOf("minus") != -1) {
		$(btn).attr("src", "/Windchill/jsp/portal/images/add_icon.png");	
	} else {
		$(btn).attr("src", "/Windchill/jsp/portal/images/minus_icon.png");
	}
	bodyDiv.toggle();
	
	resizeGrid();
}

//공통 : 검색조건 리셋
if(window.reset) {
	reset = (function(){
		var _reset = reset;
		
		return function() {
			var result = _reset.apply();
			
			$(".searchUser").each(function(){
				$(this).val("");
				$(this).trigger("change");
			});
			
			$(".searchDepartment").each(function(){
				$(this).val("");
				$(this).trigger("change");
			});
			
			$(".searchCode").each(function(){
				$(this).val("");
				$(this).trigger("change");
			});
			
			$(".searchRelatedProject").each(function(){
				$(this).val("");
				$(this).trigger("change");
			});
			
			$(".searchRelatedObject").each(function(){
				$(this).val("");
				$(this).trigger("change");
			});
			
			$(".searchRelatedECR").each(function(){
				$(this).val("");
				$(this).trigger("change");
			});
			
			$(".searchRelatedECO").each(function(){
				$(this).val("");
				$(this).trigger("change");
			});
			
			$(".multiSelect").each(function(){
				$(this)[0].sumo.unSelectAll();
			});
			
			$(".searchSupplier").each(function(){
				$(this).val("");
				$(this).trigger("change");
			});
			
			return result;
		}
	})();
}

//공통 : textarea 초기화


function textAreaInit() {
	$("textarea").each(function(){
		textAreaResize(this);
	});
}

//공통 : textarea resize
function textAreaResize(obj) {
	obj.style.height = "100px";
	obj.style.height = (12+obj.scrollHeight)+"px";
}

//팝업 : 팝업 리사이즈
function popupResize(){
	
	//textAreaInit
	textAreaInit();
	
	popupResize2();
	
	//var strWidth = window.outerWidth;
	//var strHeight = window.outerHeight;
	
	//IE check
	//var agent = navigator.userAgent.toLowerCase();
	
	/* if((navigator.appName == "Netscape" && navigator.userAgent.search("Trident") != -1) || (agent.indexOf("msie") != -1)){
		strWidth = $(".pop").outerWidth() + (window.outerWidth - window.innerWidth);
	} */
	//strHeight = $(".pop").outerHeight() + (window.outerHeight - window.innerHeight);
	
	//resize 
	//window.resizeTo( strWidth, strHeight);
	
}
//팝업 : 팝업 리사이즈
function popupResize2(){
	setTimeout(function() {
		//console.log("========================================================");
		var conW = $(".pop").innerWidth(); //컨텐트 사이즈
	    var conH = $(".pop").innerHeight();
		
		//console.log("conW : " + conW + ", conH : " + conH);
		
		var winOuterW = window.outerWidth; //브라우저 전체 사이즈
		var winOuterH = window.outerHeight;
		  
		//console.log("winOuterW : " + winOuterW + ", winOuterH : " + winOuterH);
		
		var winOffSetW = window.document.body.offsetWidth; //스크롤 제외한 body영역
		var winOffSetH = window.document.body.offsetHeight;
		  
		//console.log("winOffSetW : " + winOffSetW + ", winOffSetH : " + winOffSetH);
		
		var scrollW = winOuterW - winOffSetW;
		var scrollH = winOuterH - winOffSetH;
		  
		//console.log("scrollW : " + scrollW + ", scrollH : " + scrollH);
		
		winResizeW = conW + scrollW;
		winResizeH = conH + scrollH + 50;
		
		//console.log("winResizeW : " + winResizeW + ", winResizeH : " + winResizeH);
		
		window.resizeTo(winResizeW,winResizeH);
	},200)
}
//공통 : 폴더 토글
function toggleFolderList(toggleBtn) {
	
	var offsetLeft = $(toggleBtn).prop("offsetLeft");
	var offsetTop = $(toggleBtn).prop("offsetTop");
	
	var folderListId = $(toggleBtn).data("folderlistid");
	var folderposition = $(toggleBtn).data("folderposition");
	
	if(folderListId == null) {
		folderListId = "folderList";
	}
	if(folderposition == null) {
		folderposition = "down";
	}
	
	$("#" + folderListId).css("left", offsetLeft + 50);
	if(folderposition == "down") {
		$("#" + folderListId).css("top", offsetTop);	
	} else if(folderposition == "up") {
		$("#" + folderListId).css("bottom", offsetTop);	
	}
	
	$("#" + folderListId).toggle();
	$$($("[id$='_grid_tree']").attr("id")).define("width", $("[id$='_grid_tree']").parent().width());
	$$($("[id$='_grid_tree']").attr("id")).resize();
	
	
	if("축소" == $("[id$='ExpandBtn']").html()){
		$("[id$='ExpandBtn']").click();
	}
}

//공통 : AUIGrid 리사이즈
function resizeGrid(){
	document.querySelectorAll(".list").forEach((ele) => {
     	//$$($(this).attr("id")).adjust();
		AUIGrid.resize('#'+ele.id, ele.offsetWidth, ele.offsetHeight);
 	});
}
	
var intervalList = new Array();
var progressInterval = new Array();
function startProgress() {
	var intervalId = setInterval(function() {
		var xmlHttp;
		function srvTime(url) {
			if (url == undefined) {
				url =location.href;
			}
			
			if (window.XMLHttpRequest) {
				xmlHttp = new XMLHttpRequest();
			} else if (window.ActiveXObject) {
				xmlHttp = new ActiveXObject('Msxml2.XMLHTTP');
			} else {
				return null;
			}
			
			xmlHttp.open("HEAD", url, false); //헤더 정보만 받기 위해 HEAD방식으로 요청.
			xmlHttp.setRequestHeader("Content-Type", "text/html");
			xmlHttp.send("");

			return xmlHttp.getResponseHeader("Date"); //받은 헤더정보에서 Date 속성을 리턴.
		}

		var serverTime = new Date(srvTime());
		
		$("#progressDate").html( "현재 시간 : " + serverTime.toLocaleString());
	}, 500);
	
	progressInterval.push(intervalId);
	
	$("#progressWindow").show();
}

function endProgress() {
	for(var i = 0; i < progressInterval.length; i++){
		clearInterval(progressInterval[i]);
	}
	
	progressInterval = new Array();
	
	$("#progressWindow").hide();
}

function checkApproveLine(){
	
// 	var check = false;
	
// 	var approvalList = $$("app_line_grid_wrap").data.serialize();
// 	if(approvalList.length > 0) {
// 		for(var i = 0; i < approvalList.length; i++) {
// 			var item = approvalList[i];
// 			if(item.roleType == "APPROVE") {
// 				 check = true
// 				 break;
// 			}
// 		}
// 		if(!check) {
// 			openNotice("${e3ps:getMessage('승인자를 지정하세요.')}");
// 		}
		
// 	} else {
// 		openNotice("${e3ps:getMessage('결재선을 지정하세요.')}");
// 	}
// 	return check;

	var check = false;

    var approvalList = AUIGrid.getGridData(app_line_myGridID);
    if (approvalList.length > 0) {
      for (var i = 0; i < approvalList.length; i++) {
        var item = approvalList[i];
        if (item.roleType == "APPROVE") {
          check = true;
          break;
        }
      }
      if (!check) {
        openNotice("${e3ps:getMessage('승인자를 지정하세요.')}");
      }
    } else {
      openNotice("${e3ps:getMessage('결재선을 지정하세요.')}");
    }
    return check;
} 

function setCode(id, item){
	$("#" + id).trigger("change");
	$("#" + id).append("<option value='" + item.code + "' selected>" + item.code + " : "+ item.name + "</option>");
	$("#" + id).val(item.code);
}

function deleteCode(id){
	$("#" + id).val("");
	$("#" + id).empty();
	$("#" + id).trigger("change");
}

function setUser(id, list){
	var oldList = $("#" + id).val();
	$("#" + id).trigger("change");
	for(var i=0; i < list.length; i++) {
		var item = list[i];
		var flag = true;
		if(oldList != null) {
			for(var j=0; j < oldList.length; j++) {
				if(oldList[j] == item.oid) {
					flag = false;
					break;				
				}
			}	
		}
		
		if(flag) {
			$("#" + id).append("<option value='" + item.oid + "' selected>" + item.name + "</option>");
			$("input[name=" + id + "]").val(item.oid);
		}
	}
}

function setPMUser(id, list){
	var oldList = $("#" + id).val();
	$("#" + id).trigger("change");
	for(var i=0; i < list.length; i++) {
		var item = list[i].item;
		var flag = true;
		for(var j=0; j < oldList.length; j++) {
			if(oldList[j] == item.personalId) {
				flag = false;
				break;				
			}
		}
		
		if(flag) {
			$("#" + id).append("<option value='" + item.personalId + "' selected>" + item.name + "</option>");
			$("input[name=" + id + "]").val(item.personalId);
		}
	}
}

function deleteUser(id){
	$("#" + id).val("");
	$("#" + id).empty();
	$("#" + id).trigger("change");
}

function setDepartment(id, item){
	console.log("id : " + id);
	console.log("item" , item);
	$("#" + id).trigger("change");
	$("#" + id).append("<option value='" + item.name + "' selected>" + item.name + "</option>");
	//$("#" + id).val(item);
}

function deleteDepartment(id){
	$("#" + id).val("");
	$("#" + id).empty();
	$("#" + id).trigger("change");
}

</script>
<style>
#progressWindow{
background-color:rgba(128,128,128,0.5);top:0;left:0;
position:absolute;width:100%;height:100%;font-weight:bold;font-size:15px;
color:#ffffff;z-index:9999;display:none;
}
#progressCenter{
position:absolute;top:50%;left:0;right:0;transform:translateY(-50%);text-align:center;
}
</style>
<div id="progressWindow">
	<div id="progressCenter">
		<img src="/Windchill/jsp/portal/images/loding.gif">
		<br>
		<span id="progressDate"></span>
		<br><span class="hide mt15" id="progressSpan">다운로드 진행 중입니다. <img class="pointer vm" id="progressBtn" src="/Windchill/jsp/portal/images/delete_icon.png"></span>
	</div>
</div>