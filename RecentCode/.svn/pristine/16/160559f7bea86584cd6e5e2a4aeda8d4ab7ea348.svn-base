package com.e3ps.approval.bean;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.e3ps.approval.ApprovalLine;
import com.e3ps.approval.ApprovalMaster;
import com.e3ps.approval.ApprovalObjectLink;
import com.e3ps.approval.CommonActivity;
import com.e3ps.approval.service.ApprovalHelper;
import com.e3ps.approval.service.ApprovalService;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.beans.ECOData;
import com.e3ps.change.service.ChangeECOHelper;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.change.service.ChangeService;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.workflow.E3PSWorkflowHelper;
import com.e3ps.project.EProject;
import com.e3ps.project.ProjectRegistApproval;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.ProjectHelper;

import wt.doc.WTDocument;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.State;
import wt.util.WTException;

public class ApprovalData {
	
	private String objectType;
	private String roleType;
	private String roleName;
	private String state;
	private String stateName;
	private String objectName;
	private String title;
	private String number;
	private String name;
	private String owner;
	private String ownerFullName;
	private String startDate;
	private String startDateFormat;
	private String objectOid;
	private String draftDate;
	private String description;
	
	private String creatorName;
	
	private boolean isLastApproval;
	
	//배포 업체 정보
	private String supplierId;
	private String supplierName;
	private String orderType;

	private String changeOid;
	public ApprovalData() throws Exception {
		
	}
	public Persistable obj;
    public ApprovalMaster master;
    public ArrayList<ApprovalLine> line = new ArrayList<ApprovalLine>();
    public ArrayList<ApprovalLine> tempLine = new ArrayList<ApprovalLine>();
    public ArrayList<ApprovalLine> allLine = new ArrayList<ApprovalLine>();
    
    public ApprovalData(final Persistable per)throws Exception{
        this.obj = per;
        this.master = ApprovalHelper.service.getApprovalMaster(per);
        setLine();
    }

    public ApprovalData(final ApprovalMaster master)throws WTException{
        this(master,true);
    }

    public ApprovalData(final ApprovalMaster master,final boolean setLine)throws WTException{

        this.master = master;
        QueryResult qr = PersistenceHelper.manager.navigate(master,"obj",ApprovalObjectLink.class);
        if(qr.hasMoreElements()){
            obj = (Persistable)qr.nextElement();
        }

        if(setLine){
            setLine();
        }
    }
    public void setLine()throws WTException{
        if(master!=null){
        	System.out.println("master oid Check == "+CommonUtil.getOIDString(master));
            QueryResult qr = ApprovalHelper.service.getApprovalLine(master);
            System.out.println("Size Check == "+qr.size());
            while(qr.hasMoreElements()){
                ApprovalLine al = (ApprovalLine)qr.nextElement();
//                if(ApprovalService.WORKING_TEMP.equals(al.getName())){
//                    tempLine.add(al);
//                }else{
//                    line.add(al);
//                }
                allLine.add(al);
            }
        }
    }
//    public void changeState(Persistable per, String state){
//    	try {
//	    	if (per instanceof EChangeOrder2) {
//				EChangeOrder2 eco = (EChangeOrder2) per;
//				if("INWORK".equals(state)){
//					eco.setOrderState(ChangeService.ECO_WORKING);
//				}
//				else if("APPROVING".equals(state)){
//					eco.setOrderState(ChangeService.ECO_BEFORE_APPROVING);
//				}
//				PersistenceHelper.manager.modify(eco);
//			}
//    	} catch (Exception e) {
//			e.printStackTrace();
//		}
//    }    
//    
//    public void changeState(String state){
//    	try {
//	    	if (obj instanceof EChangeOrder2) {
//				EChangeOrder2 eco = (EChangeOrder2) obj;
//				if("INWORK".equals(state)){
//					eco.setOrderState(ChangeService.ECO_WORKING);
//				}
//				else if("APPROVING".equals(state)){
////					eco.setOrderState(ChangeService.ECO_BEFORE_APPROVING);
//					eco.setOrderState(ChangeService.ECO_ECA_COMPLETE);
//				}else if("".equals(state)) {
//				}
//				
//				PersistenceHelper.manager.modify(eco);
//				eco = (EChangeOrder2)PersistenceHelper.manager.refresh(eco);
//			}
//    	} catch (Exception e) {
//			e.printStackTrace();
//		}
//    }
    
    public void completeAction(final String state, String desc) throws Exception {
		if (obj instanceof EChangeOrder2) {
			EChangeOrder2 eco = (EChangeOrder2) obj;
			ECOData data = new ECOData(eco);
			
			if(ApprovalService.MASTER_APPROVED.equals(state)) {
				if(eco.getLifeCycleState().toString().equals("AFTERAPPROVING") || 
						data.getEcaState().equals(ChangeService.ACTIVITY_APPROVED)) {
					ChangeECOHelper.service.commitECO(null, state, eco);
				} else {
					ChangeECOHelper.service.startActivity(eco);
				}
			} else {
				E3PSWorkflowHelper.manager.changeLCState(eco, "RETURN");
			}
		} else 
		if (obj instanceof EChangeActivity) {
			EChangeActivity eca = (EChangeActivity) obj;
			ChangeECOHelper.service.commitActivity(eca, state, desc);
		} else
		if (obj instanceof EChangeRequest2) {
			EChangeRequest2 ecr = (EChangeRequest2) obj;
			if (ApprovalService.MASTER_APPROVED.equals(state)) { // ECO 승인 됐을경우
				ChangeECOHelper.service.startActivity(ecr);
			} else {
				ChangeHelper.service.commitRequest(ecr, state);
			}
			
		} else if (obj instanceof WTDocument) {
			/*
			if(obj instanceof DrawingDiscard){ //폐기도면 요청일 경우, 폐기 요청 처리자에게 업무 할당
				DrawingDiscard discardDwg = (DrawingDiscard)obj;
				DrawingHelper.service.setDiscardRequestTask(discardDwg);
			} 
			*/
			if (ApprovalService.MASTER_APPROVED.equals(state)) {
				WTDocument doc = (WTDocument) obj;
				// SecurityHelper.manager.commitRequestDoc(doc,state);
				// DocumentHelper.manager.commitOutputDocumnet(doc);
			}
		}else if (obj instanceof CommonActivity) {
			CommonActivity com = (CommonActivity) obj;
			if(ChangeService.ACTIVE_WORK_COMPLETE.equals(com.getGubun())) {
				EChangeOrder2 eco = ChangeHelper.service.getEO(com);
				if (eco != null) { 
					//eco.setOrderState(ChangeService.ECO_COMPLETE);
					PersistenceHelper.manager.modify(eco);
					
					//최종 결재시 ERP전송
					//ERPInterface.send(eco);
				}
			}
			
			if (ChangeService.ACTIVE_WORK_REGIST.equals(com.getGubun())) {
				EChangeOrder2 eco = ChangeHelper.service.getEO(com);
				if (eco != null) {
					if (ApprovalService.MASTER_APPROVED.equals(state)) {
						if (ChangeService.ACTIVE_WORK_LAST_APPROVAL.equals(com.getGubun())){
							// ECO 최종 결재시에만
							
							/* PDM && ERP */
							ChangeECOHelper.service.commitECO(com, state, eco);
//							ERPInterface.send(eco);
						}

					} else if (ApprovalService.MASTER_REJECTED.equals(state)) {
						/* 반려 */
						ChangeECOHelper.service.rejectEco(eco);

					}
				}
			}
			//프로젝트 결재완료시 진행
		}else if(obj instanceof EProject || obj instanceof ProjectRegistApproval){
			ProjectRegistApproval pra = (ProjectRegistApproval) obj;
        	EProject pjt = pra.getProject();
        	ProjectHelper.service.setState(pjt, STATEKEY.PROGRESS);
		}
	}
    
	public String getChangeOid() {
		return changeOid;
	}

	public void setChangeOid(String changeOid) {
		this.changeOid = changeOid;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerFullName() {
		return ownerFullName;
	}

	public void setOwnerFullName(String ownerFullName) {
		this.ownerFullName = ownerFullName;
	}

	

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDateFormat() {
		return startDateFormat;
	}

	public void setStartDateFormat(String startDateFormat) {
		this.startDateFormat = startDateFormat;
	}

	public String getObjectOid() {
		return objectOid;
	}

	public void setObjectOid(String objectOid) {
		this.objectOid = objectOid;
	}

	public String getDraftDate() {
		return draftDate;
	}

	public void setDraftDate(String draftDate) {
		this.draftDate = draftDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public boolean isLastApproval() {
		return isLastApproval;
	}

	public void setLastApproval(boolean isLastApproval) {
		this.isLastApproval = isLastApproval;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	
	

}
