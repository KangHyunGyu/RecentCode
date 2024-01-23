package com.e3ps.part.comparator.bean;

import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.part.service.BomHelper;
import com.e3ps.part.service.PartHelper;

import wt.enterprise.BasicTemplateProcessor;
import wt.epm.EPMDocument;
import wt.part.WTPart;
import wt.part.WTPartUsageLink;

@SuppressWarnings("serial")
public class CADBomComparatorTreeData implements java.io.Serializable{
    
	private String oid;
	private String icon;
	
	private String treeId = "";
    private String number;
    private String name;
    private String version;
    private String rev;
    private String parent;
   
    private String partNo;
    
    private int difference = 0; //0 : 정상  ,1: ADD, 2 : DELETE, 3: CHANGE (버전(A),수량)
    
    private String linkedPart;
    private String partNoCheck;
    private String isPart;
    private String bomMaked;
    
    private String state; // state(session locale)
	private String stateName;
	
    public CADBomComparatorTreeData() throws Exception{

	}
    
	public CADBomComparatorTreeData(EPMDocument epm, EPMDocument parentEpm, int seq, String parentTreeId) throws Exception{

		this.oid = CommonUtil.getOIDString(epm);
		this.icon = BasicTemplateProcessor.getObjectIconImgTag(epm);
		
		if(parentTreeId != null) {
			this.treeId = parentTreeId + "." + (seq + 1);
			
			this.parent = parentTreeId;
		} else {
			this.treeId = "1";
		}
		
		this.partNo = StringUtil.checkNull(IBAUtil.getAttrValue(epm, "PART_NO"));
		
		this.name = epm.getName().trim();
		this.number = epm.getNumber().trim();
		this.version = epm.getVersionIdentifier().getSeries().getValue().trim();
		String iteration = epm.getIterationIdentifier().getSeries().getValue();
		this.rev = this.version + "." + iteration;
		
		WTPart part = PartHelper.manager.getWTPart(epm);
		if(part != null) {
			this.linkedPart = part.getNumber();
		} else {
			this.linkedPart = MessageUtil.getMessage("없음");
		}
		
		if(this.partNo.length() > 0) {
			boolean isPartNOCheck = EpmHelper.manager.isPartNOCheck(epm.getNumber(), this.partNo);
			if(isPartNOCheck) {
				this.partNoCheck = "동일";
			} else {
				this.partNoCheck = "다름";
			}
			
			WTPart partNoPart = PartHelper.manager.getPart(this.partNo);
			if(partNoPart != null) {
				this.isPart = "부품 있음";
			} else {
				this.isPart = "부품 없음";
			}
		} else {
			this.partNoCheck = "";
			this.isPart = "";
		}
		
		if(parentEpm != null) {
			String parentPartNo = StringUtil.checkNull(IBAUtil.getAttrValue(parentEpm, "PART_NO"));
			WTPart parentPart = PartHelper.manager.getPart(parentPartNo);
			
			if(part != null && parentPart != null) {
				WTPartUsageLink link = BomHelper.manager.getLink(part, parentPart);
				
				if(link != null) {
					this.bomMaked = "존재";
				}
			}
		}
		
		if(this.bomMaked == null || this.bomMaked.length() == 0) {
			if(this.partNo.length() == 0) {
				this.bomMaked = "";
			} else {
				this.bomMaked = "없음";
			}
		}
		
		this.state = StringUtil.checkNull(epm.getLifeCycleState().toString());
		this.stateName = StringUtil.checkNull(epm.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getDifference() {
		return difference;
	}

	public void setDifference(int difference) {
		this.difference = difference;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getLinkedPart() {
		return linkedPart;
	}

	public void setLinkedPart(String linkedPart) {
		this.linkedPart = linkedPart;
	}

	public String getPartNoCheck() {
		return partNoCheck;
	}

	public void setPartNoCheck(String partNoCheck) {
		this.partNoCheck = partNoCheck;
	}

	public String getBomMaked() {
		return bomMaked;
	}

	public void setBomMaked(String bomMaked) {
		this.bomMaked = bomMaked;
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

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public String getIsPart() {
		return isPart;
	}

	public void setIsPart(String isPart) {
		this.isPart = isPart;
	}

}
