package com.e3ps.change.beans;

import java.util.List;

import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.RequestOrderLink;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.common.bean.AccessControlData;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.part.bean.PartData;

import wt.enterprise.BasicTemplateProcessor;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.session.SessionHelper;

public class ECRData extends AccessControlData{
	private String oid;
	private String ecrNumber;
	private String name;
	private String customer;
	private String echangeReason;
	private String applyDate;
	private String applyDateName; //적용 요구 시점
	private String specificDate;
	private String description;
	private String state;
	private String createDate;
	private String updateDate;
	private String creator;
	private String icon;
	private String lastApprover;
	
	private String equipmentName;
	private String completeHopeDate;
	
	private EChangeRequest2 ecr;
	public List<PartData> partList;
	
	private boolean isCreateECO;
	
	public boolean isSelect = true;
	
	public ECRData(EChangeRequest2 ecr) throws Exception {
		super(ecr);
		this.ecr = ecr;
		this.oid = WCUtil.getOid(ecr);
		this.ecrNumber = StringUtil.checkNull(ecr.getRequestNumber());
		this.name = StringUtil.checkNull(ecr.getName());
		this.customer = StringUtil.checkNull(ecr.getCustomer());
		this.echangeReason = StringUtil.checkNull(ecr.getEchangeReason());
		this.specificDate = DateUtil.getDateString(ecr.getSpecificDate(),"d");
		this.applyDate = StringUtil.checkNull(ecr.getApplyDate());
		if("outOfStock".equals(this.applyDate)) {
			this.applyDateName = "재고소진 후";
		} else if("immediately".equals(this.applyDate)) {
			this.applyDateName = "즉시";
		} else {
			this.applyDateName = this.specificDate;
		}
		
		this.description = StringUtil.checkNull(ecr.getDescription());
		this.state = StringUtil.checkNull(ecr.getLifeCycleState().getDisplay(MessageUtil.getLocale()));

		this.createDate = DateUtil.getDateString(ecr.getPersistInfo().getCreateStamp(),"d");
		this.updateDate = DateUtil.getDateString(ecr.getPersistInfo().getModifyStamp(),"d");
		this.creator = StringUtil.checkNull(ecr.getOwner().getFullName());
		this.icon = BasicTemplateProcessor.getObjectIconImgTag(ecr);
		WTUser lastAppUser = ApprovalHelper.manager.getLastApprover(ecr);
		this.lastApprover = lastAppUser!=null?lastAppUser.getFullName():"";
		
		this.equipmentName = StringUtil.checkNull(ecr.getEquipmentName());
		this.completeHopeDate = DateUtil.getDateString(ecr.getCompleteHopeDate(),"d");
		
		this.isCreateECO = createECO();
		
	}
	
	//수정 : 작업중 ,반려됨
	public boolean modifyBtn() throws Exception{
		String sessionID = SessionHelper.manager.getPrincipal().getName();
		String creatorID = this.ecr.getCreatorName();
		boolean isModify = (super.isModify() && creatorID.equals(sessionID)) || CommonUtil.isAdmin() ;
		
		return isModify;
	
	}
	//삭제 : 작업중 ,반려됨,
	public boolean deleteBtn() throws Exception{
		String sessionID = SessionHelper.manager.getPrincipal().getName();
		String creatorID = this.ecr.getCreatorName();
		boolean isDelete = (super.isDelete() && creatorID.equals(sessionID)) || CommonUtil.isAdmin() ;
		
		return isDelete;
		
	}
	
	public boolean createECO() throws Exception{
		boolean isCreateECO = false;
		
		String state = getState();
		
		// 승인됨이고 관련 ECO가 없으면 등록 가능.
		QueryResult qr = PersistenceHelper.manager.navigate(ecr,"order",RequestOrderLink.class);
		if("APPROVED".equals(this.ecr.getLifeCycleState().toString()) && qr.size() == 0) {
			isCreateECO = true;
		}
		return isCreateECO;
	}
	
	public void ecrRelatedPartList() throws Exception {
		this.partList = ChangeHelper.manager.getECRPartDataList(this.getOid());
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getEcrNumber() {
		return ecrNumber;
	}

	public void setEcrNumber(String ecrNumber) {
		this.ecrNumber = ecrNumber;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getSpecificDate() {
		return specificDate;
	}

	public void setSpecificDate(String specificDate) {
		this.specificDate = specificDate;
	}

	public String getApplyDateName() {
		return applyDateName;
	}

	public void setApplyDateName(String applyDateName) {
		this.applyDateName = applyDateName;
	}

	public List<PartData> getPartList() {
		return partList;
	}

	public void setPartList(List<PartData> partList) {
		this.partList = partList;
	}

	public String getLastApprover() {
		return lastApprover;
	}

	public void setLastApprover(String lastApprover) {
		this.lastApprover = lastApprover;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public boolean isCreateECO() {
		return isCreateECO;
	}

	public void setCreateECO(boolean isCreateECO) {
		this.isCreateECO = isCreateECO;
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
