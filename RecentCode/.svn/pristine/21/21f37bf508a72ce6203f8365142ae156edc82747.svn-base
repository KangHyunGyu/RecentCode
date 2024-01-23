/**
 *  windchill 관련 서버 통신 기능
 */
/**************************************************************
*                      liceCycle 상태 가져오기
****************************************************************/
function getLifecycleListID(lifecycle,id) {
	
	var url	= getURLString("/common/getLifecycleList");
	
	var param = new Object();
	
	param["lifecycle"] = lifecycle;
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;

	$("#" + id).find("option").remove();
	for(var i=0; i<list.length; i++) {
		
		if(lifecycle == "LC_PART"){
			/*
			if(list[i].key =="RETURN" || list[i].key =="REWORK" || list[i].key =="CANCELLED"){
				continue;
			}
			*/
			$("#"+id).append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
		}else if(lifecycle == "LC_Default"){
			
			if( list[i].key =="REWORK" || list[i].key =="DEATH"){
				continue;
			}
			
			$("#"+id).append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
			
		}else{
			$("#"+id).append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
		}
		
		
	}
	
}

function getLifecycleList(lifecycle) {
	
	
	var url	= getURLString("/common/getLifecycleList");
	
	var param = new Object();
	
	param["lifecycle"] = lifecycle;
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#state").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		
		if(lifecycle == "LC_Default"){
			
			if( list[i].key =="REWORK" ){
				continue;
			}
			
			$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
			
		}else{
			$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
			
		}
	}
	
	return list;
}

function getLifecycleListReport(lifecycle) {
	var url	= getURLString("/common/getLifecycleList");
	
	var param = new Object();
	
	param["lifecycle"] = lifecycle;
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	var serializeKeyList = [];
	var defaultCheckKeyList = [];
	
	var data = {};
	$("#state").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		
		if(lifecycle == 'LC_MDM_Material'){
			
			if(list[i].key == "PROGRESS_APPROVING" || list[i].key == "INWORK"){
				continue;
			}else{
				serializeKeyList.push(list[i].key);
				if(list[i].key == "ECA_ACTIVITY" ||list[i].key == "DICTIONARY_ACTIVITY" ||list[i].key == "AFTER_ACTIVITY" ||list[i].key == "COMPLETION_APPROVING" ){
					defaultCheckKeyList.push(list[i].key);
				}
			}
			$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
		}else if(lifecycle == 'LC_ECR'){
			
			serializeKeyList.push(list[i].key);
			if(list[i].key == "ECA_ACTIVITY" ||list[i].key == "COMPLETION_APPROVING" ){
				defaultCheckKeyList.push(list[i].key);
			}
			
			$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
		}else if(lifecycle == 'LC_ECO_NEW'){
			
			if(list[i].key =="INWORK" ){
				continue;
			}else{
				serializeKeyList.push(list[i].key);
				if(list[i].key == "PROGRESS_APPROVING" ||list[i].key == "DICTIONARY_ACTIVITY" ||list[i].key == "AFTER_ACTIVITY" ||list[i].key == "COMPLETION_APPROVING" ){
					defaultCheckKeyList.push(list[i].key);
				}
			}
			$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
		}else if(lifecycle == 'LC_ECN_NEW'){
			
			serializeKeyList.push(list[i].key);
			if(list[i].key == "ECA_ACTIVITY" ||list[i].key == "COMPLETION_APPROVING" ){
				defaultCheckKeyList.push(list[i].key);
			}
			
			$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
		}else if(lifecycle == 'LC_ECA_PROCESS'){
			console.log(list[i].key);
			serializeKeyList.push(list[i].key);
			if(list[i].key == "READY" ||list[i].key == "INWORK" ){
				defaultCheckKeyList.push(list[i].key);
			}
			
			$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
		}else{
			serializeKeyList.push(list[i].key);
			defaultCheckKeyList.push(list[i].key);
			$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
		}
		
	}
	
	data["serializeKeyList"] = serializeKeyList;
	data["defaultCheckKeyList"] = defaultCheckKeyList;
	$("#state").SumoSelect().sumo.reload();
	
	return data;
}

function getLifecycleListToId(id, lifecycle) {
	var url	= getURLString("/common/getLifecycleList");
	
	var param = new Object();
	
	param["lifecycle"] = lifecycle;
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#" + id).find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#" + id).append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                      CAD 타입 가져오기
****************************************************************/
function getCadTypeList() {
	var url	= getURLString("/common/getCadTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#cadType").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#cadType").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

function getCadTypeListForCreate() {
	var url	= getURLString("/common/getCadTypeListForCreate");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#cadType").find("option").remove();
	
	$("#cadType").append("<option value=''></option>");
	
	for(var i=0; i<list.length; i++) {
		$("#cadType").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                      CAD 구분 가져오기
****************************************************************/
function getCadDivisionList() {
	var url	= getURLString("/common/getCadDivisionList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#cadDivision").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#cadDivision").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                      Unit 가져오기
****************************************************************/
function getUnitList() {
	var url	= getURLString("/part/getUnitList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#unit").find("option").remove();
	
	$("#unit").append("<option value=''></option>");
	
	for(var i=0; i<list.length; i++) {
		$("#unit").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

function getAUIUnitList() {
	var url	= getURLString("/part/getUnitList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	return list;
}

function getAUIWeightUnitList(){
	return [];
}

/**************************************************************
*                      CodeType 가져오기
****************************************************************/
function getCodeTypeList() {
	var url	= getURLString("/common/getCodeTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	const list = data.list;
	
	console.log(list);
	
	$("#codeType").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#codeType").append("<option value='" + list[i].key + "' data-istree='" + list[i].isTree + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                      AuthorityGroupType 가져오기
****************************************************************/
function getAuthorityGroupTypeList() {
	var url	= getURLString("/admin/getAuthorityGroupTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	const list = data.list;
	
	console.log(list);
	
	$("#codeType").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#codeType").append("<option value='" + list[i].key + "' data-istree='" + list[i].isTree + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                      AuthList 가져오기
****************************************************************/
function getAuthList() {
	var url	= getURLString("/admin/getAuthList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	const list = data.list;
	
	let authDiv = document.getElementById('authList');
	authDiv.innerHTML = '';
	
	for(var i=0; i<list.length; i++) {
		if(i == 0){
			authDiv.innerHTML += "<label class='mr10'><input type='radio' name='auth' value='"+list[i].key+"' checked/>"+list[i].value+"</label>"
		}else{
			authDiv.innerHTML += "<label class='mr10'><input type='radio' name='auth' value='"+list[i].key+"'/>"+list[i].value+"</label>"
		}
	}
	
}

/**************************************************************
*                      AuthObject 가져오기
****************************************************************/
function getAuthObjectList() {
	var url	= getURLString("/admin/getAuthObjectList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	const list = data.list;
	
	$("#authObject").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#authObject").append("<option value='" + list[i].code + "'>" + list[i].name + "</option>");
	}
}

/**************************************************************
*                      DomainPath 가져오기
****************************************************************/
function getDomainList(id) {
	var url	= getURLString("/admin/getDomainList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	
	let codyTypeValue = document.getElementById(id).value;
	
	const map = data.list;
	
	$("#domainPath").find("option").remove();
	
	let list = map[codyTypeValue];
	
	for(var i=0; i<list.length; i++) {
		$("#domainPath").append("<option value='" + list[i].code + "'>" + list[i].name + "</option>");
	}
}
/**************************************************************
*                      결재 구분 가져오기
****************************************************************/
function getApprovalObjectTypeList() {
	var url	= getURLString("/common/getApprovalObjectTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#objectType").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#objectType").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}


/**************************************************************
*                      결재 역활 가져오기
****************************************************************/
function getApprovalRoleTypeList() {
	var url	= getURLString("/common/getApprovalRoleTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#state").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                      결재 마스터 상태 가져오기
****************************************************************/
function getApprovalStateList() {
	var url	= getURLString("/common/getApprovalStateList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#state").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                      Material(재질) 가져오기
****************************************************************/
/*
function getMaterialList() {
	var url	= getURLString("/part/getMaterialList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#MATERIAL").find("option").remove();
	
	$("#MATERIAL").append("<option value=''></option>");
	
	for(var i=0; i<list.length; i++) {
		$("#MATERIAL").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}
*/
/**************************************************************
*                      Material Type 가져오기
****************************************************************/
/*
function getMaterialTypeList() {
	var url	= getURLString("/part/getMaterialTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#materialType").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#materialType").append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].value + "</option>");
	}
}
*/
/**************************************************************
*                      공지사항 분류 가져오기
****************************************************************/
function getNotifyTypeList() {
	var url	= getURLString("/workspace/getNotifyTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#notifyType").find("option").remove();
	
	$("#notifyType").append("<option value=''></option>");
	
	for(var i=0; i<list.length; i++) {
		$("#notifyType").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                      project 상태 가져오기
****************************************************************/
function getProjectStateList() {
	var url	= getURLString("/project/searchProjectStateList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	//data.list = Object
	var state = data.list.state;
	var stateDisplay = data.list.stateDisplay;
	
	$("#pjtState").find("option").remove();
	
	Array.prototype.forEach.call(state, function(value, idx){
		$("#pjtState").append("<option value='" + value + "'>" + stateDisplay[idx] + "</option>");
	});
	
	return data.list;
}

///**************************************************************
// *                      project 종류 가져오기
// ****************************************************************/
//function getProjectTypeList() {
//	var url	= getURLString("/project/getProjectTypeList");
//	
//	var param = new Object();
//	
//	var data = ajaxCallServer(url, param, null);
//	
//	var list = data.list;
//	
//	$("#pjtType").find("option").remove();
//	
//	for(var i=0; i<list.length; i++) {
//		$("#pjtType").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
//	}
//}

/**************************************************************
 *                      issue 상태 가져오기
 ****************************************************************/
function getIssueStateList() {
	var url	= getURLString("/issue/getIssueStateList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#issueState").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#issueState").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
 *                      issue 유형 가져오기
 ****************************************************************/
function getIssueTypeList() {
	var url	= getURLString("/issue/getIssueTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#issueType").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#issueType").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
 *                      module 종류 가져오기
 ****************************************************************/
function getModuleTypeList() {
	var url	= getURLString("/admin/getModuleList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#module").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#module").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

function getNumberCodeList(id, codeType, showCode, isBlank, showDescription) {
	var url	= getURLString("/common/getNumberCodeList");
	
	var param = new Object();
	
	param["codeType"] = codeType;

	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#" + id).find("option").remove();
	
	if(isBlank == true) {
		$("#" + id).append("<option value='' data-oid=''>"+langChange('선택')+"</option>");
	}
	
	if(showCode == true) {
		if(showDescription == true) {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' data-oid='"+list[i].oid+"' " + list[i].selected + ">" + list[i].key + " : " + list[i].value + " " + list[i].description + "</option>");
			}
		} else {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' data-oid='"+list[i].oid+"' " + list[i].selected + ">" + list[i].key + " : " + list[i].value + "</option>");
			}
		}
	} else {
		if(showDescription == true) {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' data-oid='"+list[i].oid+"' " + list[i].selected + ">" + list[i].value + " " + list[i].description + "</option>");
			}
		} else {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' data-oid='"+list[i].oid+"' " + list[i].selected + ">" + list[i].value + "</option>");
			}
		}
		
	}
	
	return list;
}

function getNumberCodeChildList(id, codeType, parentCode, disabled, showCode, isBlank, showDescription) {
	var url	= getURLString("/common/getNumberCodeChildList");
	
	var param = new Object();
	
	param["codeType"] = codeType;
	param["parentCode"] = parentCode;
	param["disabled"] = disabled;

	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#" + id).find("option").remove();
	
	if(isBlank == true) {
		$("#" + id).append("<option value=''>선택</option>");
	}
	
	if(showCode == true) {
		if(showDescription == true) {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].key + " : " + list[i].value + " " + list[i].description + "</option>");
			}
		} else {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].key + " : " + list[i].value + "</option>");
			}
		}
	} else {
		if(showDescription == true) {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].value + " " + list[i].description + "</option>");
			}
		} else {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].value + "</option>");
			}
		}
		
	}
}

function getNumberCodeListByParent(id, codeType, parentCode, showCode, isBlank, showDescription) {
	var url	= getURLString("/common/getNumberCodeList");
	
	var param = new Object();
	
	param["codeType"] = codeType;
	param["parentCode"] = parentCode;

	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#" + id).find("option").remove();
	
	if(isBlank == true) {
		$("#" + id).append("<option value=''>"+langChange('선택')+"</option>");
	}
	
	if(showCode == true) {
		if(showDescription == true) {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].key + " : " + list[i].value + " " + list[i].description + "</option>");
			}
		} else {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].key + " : " + list[i].value + "</option>");
			}
		}
	} else {
		if(showDescription == true) {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].value + " " + list[i].description + "</option>");
			}
		} else {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].value + "</option>");
			}
		}
		
	}
	
	return list;
}



function getNumberCodeDataList(id, param, showCode, isBlank, showDescription) {
	var url	= getURLString("/admin/getNumberCodeList");
	
	if(param == null){
		param = new Object();
	}
	
	var data = ajaxCallServer(url, param, null);
	var list = data.list;
	
	return list;
}


/**************************************************************
 *                     AUI 그리드용 NumberCode 
 ****************************************************************/
function getAUINumberCodeList(codeType) {
	var url	= getURLString("/common/getNumberCodeList");
	
	var param = new Object();
	
	param["codeType"] = codeType;

	var data = ajaxCallServer(url, param, null);
	
	var list = data.list; //key,value
	
	var returnList = [];
	for(var i=0; i<list.length; i++) {
		
		var dataMap = new Object();
		
		var key = list[i].key;
		var value =value= "["+list[i].key +"] "+list[i].value
		
		dataMap["key"] = key;
		dataMap["value"] = value; 
		
		returnList[i] = dataMap;
	}
	
	return returnList;
}

/**************************************************************
 *                     AUI 그리드용 Child NumberCode
 ****************************************************************/
function getAUINumberChildCodeList(codeType,parentCode) {
	var url	= getURLString("/common/getNumberCodeList");
	
	var param = new Object();
	
	param["codeType"] = codeType;
	param["parentCode"] = parentCode;
	
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list; //key,value
	
	var returnList = [];
	for(var i=0; i<list.length; i++) {
		
		var dataMap = new Object();
		
		var key = list[i].key;
		var value =value= "["+list[i].key +"] "+list[i].value
		
		dataMap["key"] = key;
		dataMap["value"] = value; 
		
		returnList[i] = dataMap;
	}
	
	return returnList;
}




/**************************************************************
 *                     설계변경 TaskType 가져오기
 ****************************************************************/
function getEChangeTaskTypeList(select) {
	var url	= getURLString("/task/getEChangeTaskTypeList");
	
	var param = new Object();
	
	var changeType = $(select).val();
	
	param.changeType = changeType;
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#taskType").find("option").remove();
	
	$("#taskType").append("<option value=''>선택</option>");
	
	for(var i=0; i<list.length; i++) {
		$("#taskType").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
*                     회수 버튼
****************************************************************/
function recallApproval(oid){
	var url = getURLString("/approval/recallAction");
	
	var param = new Object();
	param["oid"] = oid;
	
	openConfirm("회수하시겠습니까?", function(){
		ajaxCallServer(url, param, function(data){
			
		}, true);
	});
}

/**************************************************************
*                    Intellian 추가  START
****************************************************************/


/**************************************************************
*    문서분류 정보 리스트
****************************************************************/
function getDocCodeTypes(){
	
	var url = getURLString("/admin/getDocCodeList");
	
	var data = ajaxCallServer(url, null, null, false);
	var returnData = data.list;
	
	return returnData;
}
/**************************************************************
*    문서분류 정보 리스트2
****************************************************************/
function getDocCodeTypes2(con){
	var url = getURLString("/admin/getDocCodeList");
	
	let param = new Object(); 
	param.docContainer = con;
	
	var data = ajaxCallServer(url, param, null, false);
	var returnData = data.list;
	
	return returnData;
	
}


/**************************************************************
*    문서분류 정보 리스트3
****************************************************************/
function getDocCodeTypes_by_loc(loc, con){
	
	console.log(loc);
	
	var url = getURLString("/admin/getDocCodeList");
	
	let param = new Object(); 
	param.docContainer = con;
	
	var data = ajaxCallServer(url, param, null, false);
	var returnData = data.list;
	
	for(var i=0; i<returnData.length; i++) {
		if(returnData[i].folderPath == loc){		
			$("#docCodeTypeName").val(returnData[i].name);
			$("#docCodeType").val(returnData[i].docCode);
			$("#docCodeTypeOid").val(returnData[i].oid);
			$("#docDirectory").val(returnData[i].folderPath);
		}
	}
	
	console.log(returnData);
	
	return returnData;
	
}

/**************************************************************
*    select box 문서 분류
****************************************************************/
function getDocCodeTypes4(con,docCodeType){
	
	var url = getURLString("/admin/getDocCodeList");
	
	let param = new Object(); 
	param.docContainer = con;
	
	var data = ajaxCallServer(url, param, null, false);
	var returnData = data.list;
	
	$("#"+docCodeType).find("option").remove();
	for(var i=0; i<returnData.length; i++) {
		
		$("#"+docCodeType).append("<option value='" + returnData[i].docCode + "'>[" + returnData[i].docCode+"] "+returnData[i].name + "</option>");
		
	}
	
}

/**************************************************************
*    select box
****************************************************************/
function setCodeInSelectBox(list, selectEle){
	
	$("#"+selectEle).find("option").remove();
	$("#"+selectEle).append("<option value=''>선택</option>");
	for(var i=0; i<list.length; i++) {
		let code = list[i].docCode?list[i].docCode:list[i].code;
		$("#"+selectEle).append("<option value='" + code + "'>[" + code+"] "+list[i].name + "</option>");
	}
	
}


/**************************************************************
*    문서 속성정보 리스트(전체)
****************************************************************/
function getDocAttributes(){
	
	var url = getURLString("/admin/searchDocAttrAction");
	var data = ajaxCallServer(url, null, null, false);
	var returnData = data.list;
	
	if($("#docAttribute").length > 0){
		$("#docAttribute").find("option").remove();
		for(var i=0; i<returnData.length; i++) {
			var option = "<option value='" + returnData[i].code + "'dataLocation='" + returnData[i].description + "'>" + returnData[i].name + "</option>";
			$("#docAttribute").append(option);
			
		}
	}
	console.log("returnData:", returnData);
	return returnData;
	
}
/**************************************************************
*   ECO 상태 리스트(전체)
****************************************************************/
function getECOStateList(){
	
	var url = getURLString("/change/getECOStateList");
	var data = ajaxCallServer(url, null, null, false);
	var returnData = data.list;
	
	if($("#state").length > 0){
		$("#state").find("option").remove();
		for(var i=0; i<returnData.length; i++) {
			var option = "<option value='" + returnData[i] + "'>" + returnData[i] + "</option>";
			$("#state").append(option);
			
		}
	}
	console.log("returnData:", returnData);
	return returnData;
	
}

/**************************************************************
*    문서 속성정보 리스트
****************************************************************/
function showDocAttribute(item){
	
	var oid = item.oid;
	
	var param = new Object();
	param["docTypeOid"] = oid;
	
	var retVal = [];
	
	if(oid!=null){
		var url = getURLString("/admin/searchDocAttrByCodeAction");
		
		retVal = ajaxCallServer(url, param, null, null);
	}
	
	return retVal;
}


/**************************************************************
*    문서 구분 리스트
****************************************************************/
function getDocumentTypes(){
	
	var url = getURLString("/doc/getDocumentTypeList");
	var data = ajaxCallServer(url, null, null, false);
	
	var returnData = data.list;
	var dataKey = Object.keys(returnData);
	
	if(dataKey.length > 0){
		
		$("#docType").find("option").remove();
		for(var i=0; i<dataKey.length; i++) {
			$("#docType").append("<option value='" + dataKey[i] + "'>" + returnData[dataKey[i]] + "</option>");
		}
	}
	return returnData;
	
}


/**************************************************************
*    ECADefinition 활동 그룹
****************************************************************/
function getAUIDActiveGroupTypeList(eoType) {
	var url	= getURLString("/eca/getActiveGroupList");
	
	var param = new Object();
	param["eoType"] = eoType;
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	return list;
}
/**************************************************************
*    ECADefinition STEP 
****************************************************************/
function getAUIStepList() {
	var url	= getURLString("/eca/getStepList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	return list;
}

/**************************************************************
*    Check box 로 새성
****************************************************************/
function addCheckBoxList(id,data){
	
	for(var i=0; i<data.length; i++) {
		$("#"+ id).append("<input type='checkbox' name='"+id+"' id='"+id+"' value='" + data[i].key + "'> "+ data[i].value +"&nbsp;&nbsp;&nbsp;&nbsp; ");
	}
}

/**************************************************************
*    radio box 로 새성
****************************************************************/
function addRadioList(id,data){
	
	for(var i=0; i<data.length; i++) {
		$("#"+ id).append("<input type='radio' name='"+id+"' id='"+id+"' value='" + data[i].key + "'> "+ data[i].value +"   ");
	}
}


/**************************************************************
*    sumoSelect option ADD
****************************************************************/
function  sumoSelectAdd(id,code,value){
	$('#'+id).SumoSelect().sumo.add(code,value);
}

/**************************************************************
*    sumoSelect 전체 option Delete
****************************************************************/
function sumoSlectRemmove(id){
	var num = $('#'+id+' option').length;
	if(num > 0){
		
		for(var i=num; i>=1; i--)
		{
   			$('#'+id).SumoSelect().sumo.remove(i-1);
		}
	}
}



/**************************************************************
*            동적 그리드 : 구성된 레이아웃에서 추가 데이터 필드를 삽입합니다.
****************************************************************/
//function insertLayoutObject(targetColumnLayout, layoutObject){
//	
//	if(targetColumnLayout == null){
//		targetColumnLayout = [];
//	}
//	
//	if(layoutObject != null){
//		
//		var newColumnLayout = [];
//		
//		//var location = index == null ? "last" : index;
//		
//		
//		//if(columnLayout == null || columnLayout.length <= 0){
//		//	
//		//	newColumnLayout.push(layoutObject);
//		//}else{
//		//	
//			Array.prototype.forEach.call(targetColumnLayout, function(layout, idx){
//			
//		//	if(location == idx){
//		//		newColumnLayout.push(layoutObject);
//		//	}
//			
//				newColumnLayout.push(layout);
//			});
//			
//		//	if(location == "last"){
//				newColumnLayout.push(layoutObject);
//		//	}
//		//}
//	
//		targetColumnLayout = newColumnLayout;
//	}
//	
//	return targetColumnLayout;
//}

/**************************************************************
*              동적 그리드 : Grid DataField 초기화
****************************************************************/
function createLayoutObject(obj){
	
	if(isNullEmpty(obj) || isNullEmpty(obj.dataField)){
		alert("정의할 수 없는 데이터 필드. 관리자에게 문의해주십시오.");
		return false;
	}
	
	obj.headerText = isNullEmpty(obj.headerText) ? obj.dataField : obj.headerText;
	obj.width = isNullEmpty(obj.width) ? "*" : obj.width;
	obj.style = isNullEmpty(obj.style) ? "" : obj.style;
	obj.sortValue = isNullEmpty(obj.sortValue) ? "" : obj.sortValue;
	obj.filter = isNullEmpty(obj.filter) ? new Object() : obj.filter;
	obj.renderer = isNullEmpty(obj.renderer) ? new Object() : obj.renderer;
	obj.cellMerge = isNullEmpty(obj.cellMerge) ? false : obj.cellMerge;
	
	return obj;
}

/**************************************************************
*             EO Type List
****************************************************************/

function getEOTypeList(id) {
	var url	= getURLString("/change/getEOType");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#"+id).find("option").remove();
	for(var i=0; i<list.length; i++) {
		$("#"+id).append("<option value='" + list[i].code + "'>" + list[i].value + "</option>");
	}
	
	
}

/**************************************************************
*             금액과 관련된 Element의 값에 쉼표를 붙입니다.
****************************************************************/
function moneyExpression(inputVal){
	
	
	if($(".moneyExpression").length > 0){
		$(".moneyExpression").bind("input", function(element){
			var value = element.target.value;
			value = value.replace(/\,/g, '');
			
			var valLength = value.length;
			
			if(valLength > 3){
				var newValue = value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
				element.target.value = newValue;
			}
		});
		
		Array.prototype.forEach.call($(".moneyExpression"), function(element, idx){
			var value = element.value;
			
			if(value > 3){
				var newValue = value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
				element.value = newValue;
			}
			
		});
	}
	
	
	
	
	
	if(inputVal != null && inputVal.toString().length > 3){
		inputVal = inputVal.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	return inputVal;
}


/**************************************************************
*             금액 관련 값의 쉼표를 제거한다.
****************************************************************/
function moneyTokenEraser(inputVal){
	
	if(inputVal != null && inputVal.toString().length > 0){
		inputVal = inputVal.toString().split(",").join("");
	}
	
	return inputVal;
}



/**************************************************************
*           검색 : 품목 번호 LIKE 검색 
****************************************************************/
function numberSearchOption(){
	
	if($('#likeSearch').is(':checked')){
		
		if($("select[id=relatedPart]").length > 0){
			$("select[id=relatedPart]").removeAttr("name");
		}
		if($("input[id=relatedPart][type=text]").length > 0){
			$("input[id=relatedPart][type=text]").removeAttr("name").attr("name", "relatedPart");
		}
		
		$('#autoPArtSearch').hide();
		$('#textPartSearch').show();
	}else{
		if($("select[id=relatedPart]").length > 0){
			$("select[id=relatedPart]").removeAttr("name").attr("name", "relatedPart");
		}
		if($("input[id=relatedPart][type=text]").length > 0){
			$("input[id=relatedPart][type=text]").removeAttr("name");
		}
		
		$('#autoPArtSearch').show();
		$('#textPartSearch').hide();
	}
	
}
/**************************************************************
*           검색 : 문서 번호 LIKE 검색 
****************************************************************/
function docNumberSearchOption(){
	
	if($('#likeSearch').is(':checked')){
		
		if($("select[id=relatedDoc]").length > 0){
			$("select[id=relatedDoc]").removeAttr("name");
		}
		if($("input[id=relatedDoc][type=text]").length > 0){
			$("input[id=relatedDoc][type=text]").removeAttr("name").attr("name", "relatedDoc");
		}
		
		$('#autoDocSearch').hide();
		$('#textDocSearch').show();
	}else{
		if($("select[id=relatedDoc]").length > 0){
			$("select[id=relatedDoc]").removeAttr("name").attr("name", "relatedDoc");
		}
		if($("input[id=relatedDoc][type=text]").length > 0){
			$("input[id=relatedDoc][type=text]").removeAttr("name");
		}
		
		$('#autoDocSearch').show();
		$('#textDocSearch').hide();
	}
	
}

/**************************************************************
*             사용자 직위리스트 가져오기
****************************************************************/
function getPeopleDuty(componentId){
	
	var url	= getURLString("/admin/getPeopleDuty");
	
	var param = new Object();
	var data = ajaxCallServer(url, param, null);
	var list = data.list;
	
	var returnList = [];
	
	var keys = Object.keys(list);
	for(var i=0; i<keys.length; i++) {
		
		var returnObject = new Object();
		
		var key = keys[i];
		returnObject.key = key;
		
		var value = list[key];
		returnObject.value = value;
		
		if(componentId != null && componentId.length > 0){
			$("#"+componentId).append("<option value='" + key + "'>" + value + "</option>");
		}
		returnList.push(returnObject);
	}
	return returnList;
}


/**************************************************************
*             문서 검색 관련 : 프로젝트, 설변 
****************************************************************/
function initAutocompleteEChangeProject(){
	
	$(".searchEChangeProject").each(function(){
    
		var searchObject = $(this);
		
		var width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		var url = getURLString("/common/searchProjectEChangeAction");
		
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
					
					param["keyword"] = params.term;
					
					param = JSON.stringify(param);
					
					return param;
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			              return { id: item.number, text: "["+item.number  +"] " + item.name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : " Insert Object Info Number",
			minimumInputLength: 3,
			templateResult : function(item) {
				return item.text;
		    },
		    templateSelection: function(item) {
		    	return item.id;
		    },
			escapeMarkup: function(m) {
				return m;
			}
		});
	});
	
}


/**************************************************************
*             파일 아이콘 관련 함수 
****************************************************************/
function getFileIconTag(fileName, styleObject){
	
	/*<img src="/Windchill/jsp/portal/images/icon/fileicon/notepad.gif" border="0">*/
	
	var tagStr = "<img src=\"";
	tagStr += getFileIconUrl(fileName) + "\" border=\"0\"";
	
	
	if(styleObject != null){
		
		tagStr += " style=\"";
		Array.prototype.forEach.call(Object.keys(styleObject), function(styleKey, i){
			tagStr += styleKey + ":" + styleObject[styleKey] + ";";
		});
		tagStr += "\"";
	}
	
	tagStr += ">";
	
	return tagStr;
}

function getFileIconUrl(fileName){
	
	var baseUrl = "/Windchill/jsp/portal/icon/fileicon/"; 
	
	var fileStr = fileName == null ? "" : fileName.toLowerCase().trim();
	
	if((fileStr.match(/./g) || []).length > 0){
		ext = fileStr.substring( fileStr.lastIndexOf(".")+1 );
	}else{
		return baseUrl + "generic.gif";
	}
	
	
	if (ext == "cc"){
		baseUrl += "ed.gif";
	} else if (ext == "exe"){
		baseUrl += "exe.gif";
	} else if (ext == "doc" || ext == "docx"){
		baseUrl += "doc.gif";
	} else if (ext == "ppt" || ext == "pptx"){
		baseUrl += "ppt.gif";
	} else if (ext == "xls" || ext == "xlsx"){
		baseUrl += "xls.gif";
	} else if (ext == "txt"){
		baseUrl += "notepad.gif";
	} else if (ext == "mpp"){
		baseUrl += "mpp.gif";
	} else if (ext == "pdf"){
		baseUrl += "pdf.gif";
	} else if (ext == "tif"){
		baseUrl += "tif.gif";
	} else if (ext == "gif"){
		baseUrl += "gif.gif";
	} else if (ext == "jpg"){
		baseUrl += "jpg.gif";
	} else if (ext == "ed"){
		baseUrl += "ed.gif";
	} else if (ext == "zip" || ext == "tar" || ext == "rar" || ext == "jar"){
		baseUrl += "zip.gif";
	} else if (ext == "igs" || ext == "pcb" || ext == "asc" || ext == "dwg" || ext == "dxf" || ext == "sch"){
		baseUrl += "epmall.gif";
	} else if (ext == "htm" || ext == "html"){
		baseUrl += "htm.gif";
	} else if (ext == "bmp"){
		baseUrl += "bmp.gif";
	}else{
		baseUrl += "generic.gif";
	}
	
	return baseUrl;
}


function langChange(string){
	
	string = string.trim();
	
	var returnLang ='';
	
	var langs = {
		ko : {
			'선택' : '선택',
			'구분이(가) 지정되지 않았습니다.' : '구분이(가) 지정되지 않았습니다.',
			'프로젝트' : '프로젝트',
			'설계변경' : '설계변경'
		},
		en :{
			'선택' : 'Select',
			'구분이(가) 지정되지 않았습니다.' : 'Case Type should be designated.',
			'프로젝트' : 'Project',
			'설계변경' : 'Engineering Change'
		}
	}
	
	var lang = navigator.language || navigator.userLanguage;

	if(lang == 'ko'){
		returnLang = langs.ko[string];
	}else{
		returnLang = langs.en[string];
	}
	
	if(returnLang == '' || returnLang == undefined){
		return string;
	}
	
	return returnLang;
}

function getLangValue(numbercode){
	var lang = navigator.language || navigator.userLanguage;
	
	var returnValue = numbercode.name;
	
	if(lang != 'ko'){
		returnValue = numbercode.engName;
	}
	
	return returnValue;
}


/**************************************************************
*                    제품-Library 리스트
****************************************************************/
function getProductLibraryList() {
	var url	= getURLString("/common/getProductLibraryList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	//$("#container").find("option").remove();
	//console.log(list);
	for(var i=0; i<list.length; i++) {
		
			$("#container").append("<option value='" + list[i].name + "'>" + list[i].name + "</option>");
		
	}
}


///**************************************************************
//*    프로젝트 유형 리스트 : 게이트 유형 중 1레벨 유형(프로젝트 유형)만 가져옴(NumberCode)
//****************************************************************/
function getProjectGateList(selectOpt){
	var param = new Object();
	param.codeType = "GATE";
	param.disabledCheck = true; 
	console.log(param);
	var firstLevelGate = getNumberCodeDataList(null, param, null, null, null);
	
	var flagSelectOption = false;
	if(selectOpt == null){
		flagSelectOption = true
	}
	console.log(flagSelectOption, firstLevelGate);
	if(flagSelectOption){
		Array.prototype.forEach.call(firstLevelGate, function(item, idx){
			console.log(item);
			var itemName = getLangValue(item);
			$("#productGubun").append("<option value='" + item.code + "'>" + itemName + "</option>");
		});
	}
	
	
	return firstLevelGate;
	
}

/**************************************************************
 *                     AUI 그리드용 NumberCode 
 ****************************************************************/
function getDistClassificationCodeList() {
	var url	= getURLString("/dms/getCodeList");
	
	var param = new Object();
	
	param["codeType"] = 'CLASSIFICATION';

	var data = ajaxCallServer(url, param, null);
	
	var returnList = data.list;
	
	returnList = returnList.sort(function (a, b){
		let x = a.key;
		let y = b.key;
		
		if(x < y){
			return -1;
		}
		if(x > y){
			return 1;
		}
		return 0;
	})
	
	return returnList;
}

function setSelectBox(id, list, isBlank, showCode, showDescription) {
	
	$("#" + id).find("option").remove();
	
	if(isBlank) {
		$("#" + id).append("<option value=''>"+langChange('선택')+"</option>");
	}
	
	if(showCode) {
		if(showDescription) {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].key + " : " + list[i].value + " " + list[i].description + "</option>");
			}
		} else {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].key + " : " + list[i].value + "</option>");
			}
		}
	} else {
		if(showDescription) {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].value + " " + list[i].description + "</option>");
			}
		} else {
			for(var i=0; i<list.length; i++) {
				$("#" + id).append("<option value='" + list[i].key + "' " + list[i].selected + ">" + list[i].value + "</option>");
			}
		}
		
	}
	
	return list;
}

/********************************************************
 * Project 번호 자동 검색
 * @param {String} elementID  : select element
 * @param {String} product : 사업장 값
 ********************************************************/
function autocompleteProject(elementID, product, property) {
  if (!property) property = "oid";
  var searchObject = $("#" + elementID);

  var width = $(this).data("width");

  if (width == null) {
    width = "100%";
  }
  var url = getURLString("/project/searchRelatedProject");

  searchObject.select2({
    width: width,
    ajax: {
      type: "POST",
      url: url,
      allowClear: true,
      dataType: "JSON",
      contentType: "application/json; charset=UTF-8",
      data: function (params) {
        var param = new Object();

        param["number"] = params.term;
        param["product"] = product;

        param = JSON.stringify(param);

        return param;
      },
      processResults: function (data) {
        return {
          results: $.map(data.list, function (item) {
            item.id = item[property];
            item.text = item.code + " : " + item.name;
            return item;
          }),
        };
      },
      cache: true,
    },
    placeholder: "Insert Project Info (Number or NAME)",
    minimumInputLength: 3,
    templateResult: function (item) {
      return item.text;
    },
    templateSelection: function (item) {
      return item.text;
    },
    escapeMarkup: function (m) {
      return m;
    },
  });
}

/**************************************************************
 *                     Workspace 리스트 가져오기
 ****************************************************************/
function getWorkspaceList(container) {
	var url	= getURLString("/epm/getWorkspaceList");
	
	var param = new Object();
	
	param["container"] = container;

	var data = ajaxCallServer(url, param, null);
	
	var list = data.list; //key,value
	
	var returnList = [];
	
	for(var i=0; i<list.length; i++) {
		
		var dataMap = new Object();
		
		var oid = list[i].oid;
		var name = list[i].name;
		
		dataMap["key"] = oid;
		dataMap["value"] = name; 
		
		returnList[i] = dataMap;
	}
	
	returnList = returnList.sort(function (a, b){
		let x = a.name;
		let y = b.name;
		
		if(x < y){
			return -1;
		}
		if(x > y){
			return 1;
		}
		return 0;
	})
	
	return returnList;
}


document.addEventListener("DOMContentLoaded", (evt) => {
/**************************************************************
 *                     부품 자동 검색
 ****************************************************************/
	$(".autocompletePart").each(function(){
		let width = $(this).data("width");
		
		if(width == null) {
			width = "100%";
		}
		
		var url = getURLString("/part/searchPartListAction");
		
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
			            	if(searchObj){
			            		searchObj = data.list;
			            	}
			              return { id: item.oid, text: item.name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Insert Object Info (Number or NAME)",
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
})


function searchDistributeDoc(id) {
  var searchObject = $("#"+id);
  var width = searchObject.data("width");
  searchObject.find("option").remove();
  if (width == null) {
    width = "100%";
  }
  var url = getURLString("/distribute/searchDistributeNumber");

  searchObject.select2({
    width: width,
    ajax: {
      type: "POST",
      url: url,
      allowClear: true,
      dataType: "JSON",
      contentType: "application/json; charset=UTF-8",
      data: function (params) {
        var param = new Object();
        param["keyword"] = params.term;
        param = JSON.stringify(param);
        return param;
      },
      processResults: function (data) {
        if(objArr)objArr=data.list;
        return {
          results: $.map(data.list, function ( obj, index ) {
            return { id: obj.oid, text: obj.distNumber, obj:obj};
          }),
        };
      },
      cache: true,
    },
    placeholder: "Insert Object Info (Number or Name)",
    minimumInputLength: 3,
    templateResult: function (item) {
      return item.text;
    },
    templateSelection: function (item) {
      distributeDocData = item.obj;
      return item.text;
    },
    escapeMarkup: function (m) {
      return m;
    },
  });
}

function getCompanyUsers(targetEle) {
  var searchObject = $(targetEle);
  var width = searchObject.data("width");

  if (width == null) {
    width = "100%";
  }
  var url = getURLString("/cpc/searchCompanyUsers");

  searchObject.select2({
    width: width,
    ajax: {
      type: "POST",
      url: url,
      allowClear: true,
      dataType: "JSON",
      contentType: "application/json; charset=UTF-8",
      data: function (params) {
        var param = new Object();
        param["keyword"] = params.term;
        param["companyID"] = document.getElementById('distributeCompany').value;
        param = JSON.stringify(param);
        return param;
      },
      processResults: function (data) {
        return {
          results: $.map(data.list, function ( obj, index ) {
            return { id: obj.cpc_id, text: obj.cpc_name +" _ "+obj.cpc_department};
          }),
        };
      },
      cache: true,
    },
    placeholder: "Insert Object Info (ID or Name)",
    minimumInputLength: 1,
    templateResult: function (item) {
      return item.text;
    },
    templateSelection: function (item) {
      return item.text;
    },
    escapeMarkup: function (m) {
      return m;
    },
  });
}

function cpc_companyAutoSearch(id){

		var width = $("#"+id).data("width");
		
		if(width == null) {
			width = "100%";
		}
		
		var url = getURLString("/cpc/companyAutoSearch");
		
		$("#"+id).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					
					var param = new Object();
					param["keyword"] = params.term;
					param = JSON.stringify(param);
					return param;
					
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			              return { id: item.company_id,  text: item.company_name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Enter company name information",
			minimumInputLength: 1,
			allowClear: true ,
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
}	

function cpc_companyUserAutoSearch(id){

		var width = $("#"+id).data("width");
		
		if(width == null) {
			width = "100%";
		}
		
		var url = getURLString("/cpc/companyUserAutoSearch");
		
		$("#"+id).select2({
			width : width,
			ajax : {
				type : "POST",
				url : url,
				allowClear: true,
				dataType : "JSON",
				contentType : "application/json; charset=UTF-8",
				data : function (params) {
					
					var param = new Object();
					param["keyword"] = params.term;
					param = JSON.stringify(param);
					return param;
					
			    },
			    processResults : function(data){
			    	return {
			            results: $.map(data.list, function(item) {
			              return { id: item.id,  text: item.name };
			            })
		          	};
			    },
			    cache: true
			},
			placeholder : "Enter user name information",
			minimumInputLength: 1,
			allowClear: true ,
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
}

/**************************************************************
*                      설계변경활동 Root 가져오기
****************************************************************/
function getRootList() {
	var url	= getURLString("/admin/getRootList");
	
	var param = new Object();
	
	param["changeType"] = $("#changeType").val();
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	$("#root").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#root").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

function searchProjectRoleUser(elementID, oid) {
  var searchObject = $("#" + elementID);
  var width = $(this).data("width");
  if (width == null) {
    width = "100%";
  }
  var url = getURLString("/project/searchProjectRoleUser");

  searchObject.select2({
    width: width,
    ajax: {
      type: "POST",
      url: url,
      allowClear: true,
      dataType: "JSON",
      contentType: "application/json; charset=UTF-8",
      data: function (params) {
        var param = new Object();
        param["keyword"] = params.term;
        param["oid"] = oid;
        param = JSON.stringify(param);
        return param;
      },
      processResults: function (data) {
        return {
          results: $.map(data.list, function (item) {
            item.id = item.peopleOid;
            item.text = item.roleName + " / " + item.duty +" "+item.userName;
            return item;
          }),
        };
      },
      cache: true,
    },
    placeholder: "Insert Project Role User Name",
    minimumInputLength: 1,
    templateResult: function (item) {
      return item.text;
    },
    templateSelection: function (item) {
      return item.text;
    },
    escapeMarkup: function (m) {
      return m;
    },
  });
}


