function createCDialogWindow(dialogURL, dialogName, w, h, statusBar, scrollBar) {
	createCDialogWindow(dialogURL, dialogName, w, h, statusBar, scrollBar, null);
}

function createCDialogWindow(dialogURL, dialogName, w, h, statusBar, scrollBar, theForm) {
	if (typeof (_use_wvs_cookie) != "undefined") {
		var cookie_value = document.cookie;
		if (cookie_value != null) {
			var loc = cookie_value.indexOf("wvs_ContainerOid=");
			if (loc >= 0) {
				var subp = cookie_value.substring(loc + 4);
				loc = subp.indexOf(";");
				if (loc >= 0) subp = subp.substring(0, loc);
				dialogURL += "&" + subp;
			}
		}
	}
	else {
		var vm_url = "" + top.document.location;
		if (vm_url != null) {
			var loc = vm_url.indexOf("ContainerOid=");
			if (loc < 0) {
				loc = vm_url.indexOf("/listFiles.jsp?oid=");
				if (loc >= 0) {
					vm_url = "ContainerOid=" + vm_url.substring(loc + 19);
					loc = 0;
				}
			}
			if (loc >= 0) {
				var subp = vm_url.substring(loc);
				loc = subp.indexOf("&");
				if (loc >= 0) subp = subp.substring(0, loc);
				if (dialogURL.indexOf("/blank.jsp?") > 0) {
					loc = dialogURL.indexOf("fname=");
					if (loc >= 0) {
						var fname = dialogURL.substring(loc + 6);
						loc = fname.indexOf("&");
						if (loc >= 0) fname = fname.substring(0, loc);
						try {
							if (document.forms[fname].action.indexOf("ContainerOid=") < 0) {
								document.forms[fname].action += "&" + subp;
							}
						} catch (e) { }
					}
				}
				dialogURL += "&" + subp;
			}
		}
	}

	// Manipulation of the Form information was copied from the submitFormToNewPJLWindow()
	// javascript function in actions.jsfrag
	if (theForm != null) {
		tempAction = theForm.action;
		tempTarget = theForm.target;
		ele = document.createElement("input");
		ele.type = 'hidden';
		ele.name = 'ActionSource';
		ele.value = 'PJLseedObjects';
		theForm.appendChild(ele);
		theForm.action = dialogURL;
		theForm.target = dialogName;
	}

	var newwin = createDialogWindow(dialogURL, dialogName, w, h, statusBar, scrollBar);
	if (theForm != null) {
		theForm.submit();
		theForm.action = tempAction;
		theForm.target = tempTarget;
		theForm.removeChild(ele);
	}
}

function createDialogWindow(dialogURL, dialogName, w, h, statusBar, scrollBar) {
	if (statusBar == null) statusBar = 0;
	if (scrollBar == null) scrollBar = 1;
	var opts = "toolbar=0,location=0,directory=0,status=" + statusBar + ",menubar=0,scrollbars=" + scrollBar + ",resizable=1,width=" + w + ",height=" + h;
	createDialogWindowOptions(dialogURL, dialogName, opts);
}

function createDialogWindowOptions(dialogURL, dialogName, opts) {
	if (opts == "") {
		if (navigator.userAgent.indexOf("Mozilla/4.") >= 0 && navigator.userAgent.indexOf("MSIE") == -1) {
			opts = "toolbar=1,location=1,directory=1,status=1,menubar=1,scrollbars=1,resizable=1";
		}
	}
	if (dialogName.indexOf("VisNav") == 0) opts = opts + ",top=0,left=0";
	if (typeof (checkaddtopv) == "function" && checkaddtopv(dialogURL, dialogName, opts)) return;
	var newwin = wfWindowOpen(dialogURL, dialogName, opts);
	if (typeof (setPVLiteWin) == "function") setPVLiteWin(newwin, dialogName);
	if (newwin != null) newwin.focus();
	return newwin;
}

function wfWindowOpen(url, name, opts) {

	let pvs_form = document.getElementById("pvsForm");
    
	if (pvs_form != null) {
		pvs_form.target = "executeFrame";
		pvs_form.action = url;
		pvs_form.method = "get";
    
		pvs_form.submit();
	}else{
		var popupWin = window.open(url, name, opts);
		setTimeout(function() {
			popupWin.focus();
		}, 1);
		return popupWin;
	}
	
	//location.href = url;
}

function closeDialogWindow() {
	wfWindowClose();
}
