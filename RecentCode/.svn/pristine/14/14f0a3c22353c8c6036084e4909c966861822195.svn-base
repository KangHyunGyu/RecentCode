/**
 *  windchill 관련 서버 통신 기능
 */

/**************************************************************
*                      liceCycle 상태 가져오기
****************************************************************/
function getLifecycleList(lifecycle) {
	var url	= getURLString("/common/getLifecycleList");
	
	var param = new Object();
	
	param["lifecycle"] = lifecycle;
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#state").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#state").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
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

/**************************************************************
*                      CodeType 가져오기
****************************************************************/
function getCodeTypeList() {
	var url	= getURLString("/common/getCodeTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#codeType").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#codeType").append("<option value='" + list[i].key + "' data-istree='" + list[i].isTree + "'>" + list[i].value + "</option>");
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

/**************************************************************
*                      Material Type 가져오기
****************************************************************/
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
	var url	= getURLString("/project/getProjectStateList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#pjtState").find("option").remove();
	
	for(var i=0; i<list.length; i++) {
		$("#pjtState").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

/**************************************************************
 *          프로젝트 명(NumberCode:PROJECTCODE) 가져오기
 ****************************************************************/
function getProjectCodeList() {
	var url	= getURLString("/common/getProjectCodeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	
	$("#projectCode").find("option").remove();
	
	$("#projectCode").append("<option value=''>선택</option>");
	
	for(var i=0; i<list.length; i++) {
		$("#projectCode").append("<option value='" + list[i].key + "'>" + list[i].key + " : "+ list[i].value + "</option>");
	}
}

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

function getNumberCodeList(id, codeType, showCode, isBlank, showDescription) {
	var url	= getURLString("/common/getNumberCodeList");
	
	var param = new Object();
	
	param["codeType"] = codeType;

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
*                      ERP 상태 가져오기
****************************************************************/
function getErpStateList() {
	var url	= getURLString("/erp/getErpStateList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	$("#if_status").find("option").remove();
	
	$("#if_status").append("<option value=''>전체</option>");
	for(var i=0; i<list.length; i++) {
		$("#if_status").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}
/**************************************************************
*                     ERP 동기화 버튼
****************************************************************/
function sync(url){
	var url = getURLString("/erp/" + url);
	
	var param = new Object();
	param["startDate"] = $("#precdate").val();
	param["endDate"] = $("#postcdate").val();
	
	openConfirm("동기화 하시겠습니까?", function(){
		ajaxCallServer(url, param, function(data){
			getGridData();
		}, true); 
	});
}
/**************************************************************
 *                     ERP 동기화 버튼 (instance_id)
 ****************************************************************/
function syncView(url, instance_id){
	var url = getURLString("/erp/" + url);
	
	var param = new Object();
	param["instance_id"] = instance_id;
	
	openConfirm("동기화 하시겠습니까?", function(){
		ajaxCallServer(url, param, function(data){
			
		}, true);
	});
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
*                      프로젝트 산출물 인증 타입
****************************************************************/
function getOutputTypeList() {
	var url	= getURLString("/project/getOutputTypeList");
	
	var param = new Object();
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	$("#outputType").find("option").remove();
	
	$("#outputType").append("<option value=''>선택</option>");
	for(var i=0; i<list.length; i++) {
		$("#outputType").append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}

function getOutputStepList(id, key, parentOid) {
	var url	= getURLString("/project/getOutputStepList");
	
	var param = new Object();
	
	param.key = key;
	param.parentOid = parentOid;
	
	var data = ajaxCallServer(url, param, null);
	
	var list = data.list;
	$("#" + id).find("option").remove();
	
	$("#" + id).append("<option value=''>선택</option>");
	for(var i=0; i<list.length; i++) {
		$("#" + id).append("<option value='" + list[i].key + "'>" + list[i].value + "</option>");
	}
}