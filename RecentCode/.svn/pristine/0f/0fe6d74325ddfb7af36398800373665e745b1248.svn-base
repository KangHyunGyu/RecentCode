package com.e3ps.approval.bean;

import java.util.List;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.MultiApproval;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.common.bean.OwnPersitableData;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

import wt.fc.Persistable;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.session.SessionHelper;

public class MultiApprovalData extends OwnPersitableData{
	
	private String number;
	private String name;
	private String state;
	private String stateName;
	private String objectType;
	private String objectTypeName;
	private String description;
	
	private boolean userOnGoingApproval;
	private String userOnGoingApprovalOid;
	
	public MultiApprovalData(String oid) throws Exception {
		super(oid);
		MultiApproval multi = (MultiApproval)CommonUtil.getObject(oid);
		_MultiApprovalData(multi);
	}
	
	public MultiApprovalData(MultiApproval multi) throws Exception {
		super(multi);
		_MultiApprovalData(multi);
		
	}
	
	public void _MultiApprovalData(MultiApproval multi) throws Exception {
		
		this.objectType = StringUtil.checkNull(multi.getObjectType()) ;
		
		if(this.objectType.equals("doc")) {
			this.objectTypeName = MessageUtil.getMessage("문서");
		} else if(this.objectType.equals("epm")) {
			this.objectTypeName = MessageUtil.getMessage("도면");
		} else if(this.objectType.equals("part")) {
			this.objectTypeName = MessageUtil.getMessage("부품");
		}
		this.number = multi.getNumber();
		this.name = multi.getName();
		this.state = multi.getState().toString();
		this.stateName = multi.getState().getDisplay(MessageUtil.getLocale());
		this.description = StringUtil.checkNull(multi.getDescription());
		
	}
	
	
	public void userOnGoingApproval() throws Exception{
		
		this.userOnGoingApproval = false;
		
		if(this.getOid() != null && this.getOid().length() > 0) {
			
			Persistable per = CommonUtil.getObject(this.getOid());
			
			List<ApprovalLine> onGoingLine = ApprovalHelper.manager.getOnGoingApprovalFromPersistable(per);
			
			for(ApprovalLine line : onGoingLine) {
				if( SessionHelper.getPrincipal().equals(line.getOwner().getPrincipal()) || CommonUtil.isAdmin() ) {
					this.userOnGoingApproval = true;
					this.userOnGoingApprovalOid = CommonUtil.getOIDString(line);
				}
			}
			
		}
	}
	

	//수정 : 승인중, 작성자
	public boolean modifyBtn() throws Exception{
		WTPrincipalReference ownerRef = SessionHelper.manager.getPrincipalReference();
		WTUser owner = (WTUser) ownerRef.getObject();
		return "APPROVING".equals(this.state) && owner.equals(CommonUtil.getObject(this.getOwnerOid()));
	}
	
	//삭제 : 승인중, 작성자
	public boolean deleteBtn() throws Exception{
		WTPrincipalReference ownerRef = SessionHelper.manager.getPrincipalReference();
		WTUser owner = (WTUser) ownerRef.getObject();
		return "APPROVING".equals(this.state) && owner.equals(CommonUtil.getObject(this.getOwnerOid()));
	}
	
	//회수 : 승인중, 작성자
	public boolean recallBtn() throws Exception{
		WTPrincipalReference ownerRef = SessionHelper.manager.getPrincipalReference();
		WTUser owner = (WTUser) ownerRef.getObject();
		return "APPROVING".equals(this.state) && owner.equals(CommonUtil.getObject(this.getOwnerOid()));
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectTypeName() {
		return objectTypeName;
	}

	public void setObjectTypeName(String objectTypeName) {
		this.objectTypeName = objectTypeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isUserOnGoingApproval() {
		return userOnGoingApproval;
	}

	public void setUserOnGoingApproval(boolean userOnGoingApproval) {
		this.userOnGoingApproval = userOnGoingApproval;
	}

	public String getUserOnGoingApprovalOid() {
		return userOnGoingApprovalOid;
	}

	public void setUserOnGoingApprovalOid(String userOnGoingApprovalOid) {
		this.userOnGoingApprovalOid = userOnGoingApprovalOid;
	}
	
}
