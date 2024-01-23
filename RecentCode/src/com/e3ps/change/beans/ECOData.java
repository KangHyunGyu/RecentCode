package com.e3ps.change.beans;

import java.sql.Timestamp;
import java.util.List;

import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.change.DocumentActivityOutput;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeContents;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EcoProjectLink;
import com.e3ps.change.EcrProjectLink;
import com.e3ps.change.RequestOrderLink;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.change.service.ChangeService;
import com.e3ps.common.bean.AccessControlData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.part.bean.PartData;
import com.e3ps.project.EProject;

import wt.enterprise.BasicTemplateProcessor;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.util.WTException;

public class ECOData extends AccessControlData{
	private EChangeOrder2 eco;
	private String oid;
	private String orderNumber;
	private String name;
	private String state;
	private String createDate;
	private String updateDate;
	private String tempcreator;
	private String project;
	private String projectTitle;
	private String applyDate;
	private String process;
	private String creator;
	private String carType;
	private String changeDesc;
	private String changeOwner;
	private String changeOwnerOid;
	private String finishDate;
	private String upg;
	private String ctCode;
	private String cdCode;
	private String coCode;
	private String icon;
	private String ecrNumber; 
	private String ecrOid; 
	private String ecaOid; 
	private String ecaFinishDate; 
	
	private String customer;
	private String echangeReason;
	private String applyDateName; //적용 요구 시점
	private String specificDate;
	private String description;
	private String ecaState;
	private String lastApprover;
	public List<PartData> partList;
	
	//ECR DATA
	private String equipmentName;
	private String completeHopeDate;
	
	public ECOData(EChangeOrder2 eco) throws Exception {
		super(eco);
		this.eco = eco;
		this.oid = WCUtil.getOid(eco);
		this.orderNumber = StringUtil.checkNull(eco.getOrderNumber());
		this.name = StringUtil.checkNull(eco.getName());
		this.state = StringUtil.checkNull(eco.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
		this.createDate = DateUtil.getDateString(eco.getPersistInfo().getCreateStamp(),"d");
		this.updateDate = DateUtil.getDateString(eco.getPersistInfo().getModifyStamp(),"d");
		this.tempcreator = StringUtil.checkNull(eco.getOwner().getFullName());
		this.project = StringUtil.checkNull(getRefProject(eco,true));
		this.projectTitle = StringUtil.checkNull(getRefProject(eco,false));
		this.applyDate = eco.getApplyDate() == null ? "" : eco.getApplyDate().toString();
		this.process = StringUtil.checkNull(eco.getProcess());
		this.creator = StringUtil.checkNull(eco.getOwner().getFullName());
		this.carType = StringUtil.checkNull(CodeHelper.manager.getValueSplit("CARTYPE", eco.getCarType(), ","));
		this.changeDesc = StringUtil.checkNull(CodeHelper.manager.getValueSplit("CHANGEDESC", eco.getChangeDesc(), ","));
		this.ctCode = StringUtil.checkNull(eco.getCarType());
		this.cdCode = StringUtil.checkNull(eco.getChangeDesc());
		this.coCode = StringUtil.checkNull(eco.getChangeOwner());
		this.upg = StringUtil.checkNull(eco.getUpg());
		this.icon = BasicTemplateProcessor.getObjectIconImgTag(eco);
		ECAData ecaData = reviseEca(this.oid);
		this.ecaOid = ecaData.getOid();
		this.changeOwner = StringUtil.checkNull(ecaData.getOwner());
		this.changeOwnerOid = StringUtil.checkNull(ecaData.getOwnerOid());
		this.finishDate = StringUtil.checkNull(ecaData.getFinishDate());
		this.ecaState = StringUtil.checkNull(ecaData.getState());
		this.ecaFinishDate = StringUtil.checkNull(ecaData.getEcaFinishDate());
		ECRData ecrData = reviseEcr(this.oid);
		this.ecrNumber = ecrData.getEcrNumber();
		this.ecrOid = ecrData.getOid();
		this.equipmentName = ecrData.getEquipmentName();
		this.completeHopeDate = ecrData.getCompleteHopeDate();
		
		this.echangeReason = StringUtil.checkNull(eco.getEchangeReason());
		this.customer = StringUtil.checkNull(eco.getCustomer());
		this.specificDate = DateUtil.getDateString(eco.getSpecificDate(),"d");
		this.applyDate = StringUtil.checkNull(eco.getApplyDate());
		if("outOfStock".equals(this.applyDate)) {
			this.applyDateName = "재고소진 후";
		} else if("immediately".equals(this.applyDate)) {
			this.applyDateName = "즉시";
		} else {
			this.applyDateName = this.specificDate;
		}
		
		this.description = StringUtil.checkNull(eco.getDescription());
		WTUser lastAppUser = ApprovalHelper.manager.getLastApprover(eco);
		this.lastApprover = lastAppUser!=null?lastAppUser.getFullName():"";
		
	}
	
	public void ecoRelatedPartList() throws Exception {
		this.partList = ChangeHelper.manager.getECOPartDataList(this.getOid());
	}
	
	public ECAData reviseEca(String oid) throws Exception {
		ECAData data = null;
		
		QuerySpec qs = new QuerySpec();
		Class cls = EChangeActivity.class;
		int idx = qs.appendClassList(cls, true);
		SearchCondition sc = new SearchCondition(cls, "orderReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid));
		qs.appendWhere(sc, new int[]{idx});
		qs.appendOrderBy(new OrderBy(new ClassAttribute(cls, EChangeActivity.CREATE_TIMESTAMP), false), new int[] { idx });
		QueryResult aqr = PersistenceHelper.manager.find(qs);
		while(aqr.hasMoreElements()){
			
			Object[] o = (Object[])aqr.nextElement();
			EChangeActivity eca = (EChangeActivity)o[0];
			data = new ECAData(eca);
		}
		
		return data;
	}
	
	public ECRData reviseEcr(String oid) throws Exception {
		ECRData data = null;
		EChangeOrder2 eco = (EChangeOrder2) CommonUtil.getObject(oid);
		QueryResult qr = PersistenceHelper.manager.navigate(eco, "roleB", RequestOrderLink.class);
		while(qr.hasMoreElements()) {
			EChangeRequest2 ecr = (EChangeRequest2) qr.nextElement();
			data = new ECRData(ecr);
		}
		
		return data;
	}
	
	//수정 : 작업중 ,반려됨
	public boolean modifyBtn() throws Exception{
		String sessionID = SessionHelper.manager.getPrincipal().getName();
		String creatorID = this.eco.getCreatorName();
		boolean isModify = (super.isModify() && creatorID.equals(sessionID)) ;
		
		return isModify;
	
	}
	//삭제 : 작업중 ,반려됨,
	public boolean deleteBtn() throws Exception{
		String sessionID = SessionHelper.manager.getPrincipal().getName();
		String creatorID = this.eco.getCreatorName();
		boolean isDelete = (super.isDelete() && creatorID.equals(sessionID)) ;
		
		return isDelete;
		
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getCtCode() {
		return ctCode;
	}

	public void setCtCode(String ctCode) {
		this.ctCode = ctCode;
	}

	public String getCdCode() {
		return cdCode;
	}

	public void setCdCode(String cdCode) {
		this.cdCode = cdCode;
	}

	public String getCoCode() {
		return coCode;
	}

	public void setCoCode(String coCode) {
		this.coCode = coCode;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getChangeDesc() {
		return changeDesc;
	}

	public void setChangeDesc(String changeDesc) {
		this.changeDesc = changeDesc;
	}

	public String getChangeOwner() {
		return changeOwner;
	}

	public void setChangeOwner(String changeOwner) {
		this.changeOwner = changeOwner;
	}

	public String getUpg() {
		return upg;
	}

	public void setUpg(String upg) {
		this.upg = upg;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getTempcreator() {
		return tempcreator;
	}

	public void setTempcreator(String tempcreator) {
		this.tempcreator = tempcreator;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getEchangeReason() {
		return echangeReason;
	}

	public void setEchangeReason(String echangeReason) {
		this.echangeReason = echangeReason;
	}

	public String getApplyDateName() {
		return applyDateName;
	}

	public void setApplyDateName(String applyDateName) {
		this.applyDateName = applyDateName;
	}

	public String getSpecificDate() {
		return specificDate;
	}

	public void setSpecificDate(String specificDate) {
		this.specificDate = specificDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getEcrNumber() {
		return ecrNumber;
	}

	public void setEcrNumber(String ecrNumber) {
		this.ecrNumber = ecrNumber;
	}

	public String getEcrOid() {
		return ecrOid;
	}

	public void setEcrOid(String ecrOid) {
		this.ecrOid = ecrOid;
	}

	public String getEcaState() {
		return ecaState;
	}

	public void setEcaState(String ecaState) {
		this.ecaState = ecaState;
	}

	public String getChangeOwnerOid() {
		return changeOwnerOid;
	}

	public void setChangeOwnerOid(String changeOwnerOid) {
		this.changeOwnerOid = changeOwnerOid;
	}

	public String getEcaOid() {
		return ecaOid;
	}

	public void setEcaOid(String ecaOid) {
		this.ecaOid = ecaOid;
	}
	
	public List<PartData> getPartList() {
		return partList;
	}

	public void setPartList(List<PartData> partList) {
		this.partList = partList;
	}

	public String getEcaFinishDate() {
		return ecaFinishDate;
	}

	public void setEcaFinishDate(String ecaFinishDate) {
		this.ecaFinishDate = ecaFinishDate;
	}

	String getRefProject(EChangeOrder2 eco, boolean flag) throws WTException {
		QueryResult projects = PersistenceHelper.manager.navigate(eco,"project",EcoProjectLink.class);
		String refProject = "";
		if(projects.hasMoreElements()){
			EProject project = (EProject)projects.nextElement();
			if(flag){
				refProject = CommonUtil.getOIDString(project);
			}else {
				refProject = project.getCode()+" : "+project.getName();
			}
			
		}
		return refProject;
	}

	public String getLastApprover() {
		return lastApprover;
	}

	public void setLastApprover(String lastApprover) {
		this.lastApprover = lastApprover;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getCompleteHopeDate() {
		return completeHopeDate;
	}

	public void setCompleteHopeDate(String completeHopeDate) {
		this.completeHopeDate = completeHopeDate;
	}
	
	
	
}
