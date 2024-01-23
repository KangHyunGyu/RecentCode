package com.e3ps.part.editor.bean;

import java.util.List;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.epm.util.EpmUtil;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.util.PartUtil;

import wt.epm.EPMDocument;
import wt.part.WTPart;
import wt.part.WTPartUsageLink;

@SuppressWarnings("serial")
public class BomEditorTreeData implements java.io.Serializable{
    
	private WTPart part;
	private EPMDocument epm;
	
	private String treeId = "";
    private String oid; //모 품목
    private String number;
    private int level;
    private String name;
    private String state;
    private String stateName;
    private String version;
    private String rev;
    private String unit;
    private double quantity = 1;
    private int seq;
    private String parent;
    private String icon;
    
    private String specification;
    private String partType;
    private String linkOid;
    
    private String checkoutState;
    
    private boolean isEpm = false;
    private boolean isWGM = false;
    
    private String cadSync;
    private boolean isOwnerCheckout = false;
    
    private List<BomEditorTreeData> children;
    
	public BomEditorTreeData(WTPart part, WTPartUsageLink link, int level, int seq, String parentTreeId) throws Exception{
		this.part = part;
		this.epm = EpmHelper.manager.getEPMDocument(part);
		
		if(this.epm != null) {
			this.isEpm = true;
			this.isWGM = EpmUtil.isWGM(this.epm);
		}
		
		this.oid = CommonUtil.getOIDString(part);
        this.level = level;
        this.seq = seq;
        
        PartData data = new PartData(part);
        
        this.number = data.getNumber();
        this.name = data.getName();
        
        this.version = data.getVersion();
        this.rev = data.getRev();
        this.state = data.getState();
        this.stateName = data.getStateName();
        this.icon = data.getIcon();
        
        this.checkoutState = data.getCheckoutState();
        this.isOwnerCheckout = PartUtil.isOwnerCheckOut(this.part);
        if(link!=null){
        	this.unit = link.getQuantity().getUnit().toString();
        	this.quantity = link.getQuantity().getAmount();
        	this.linkOid = CommonUtil.getOIDString(link);
        	this.cadSync = link.getCadSynchronized().toString();
        }else {
        	this.unit = part.getDefaultUnit().toString();
        }
        
        this.parent = parentTreeId;
        if(this.parent.length() > 0) {
        	this.treeId = this.parent + "-";
        }
        this.treeId += this.level + "_" + this.seq;
    }

	public String getThumbnail() {
		String thumbnail = EpmUtil.getThumbnail(this.epm);
		
		return thumbnail;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getTreeId() {
		return treeId;
	}
	
	public String getId() {
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

	public List<BomEditorTreeData> getChildren() {
		return children;
	}

	public void setChildren(List<BomEditorTreeData> children) {
		this.children = children;
	}
	
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public String getLinkOid() {
		return linkOid;
	}

	public void setLinkOid(String linkOid) {
		this.linkOid = linkOid;
	}

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public String getCheckoutState() {
		return checkoutState;
	}

	public void setCheckoutState(String checkoutState) {
		this.checkoutState = checkoutState;
	}

	public boolean isEpm() {
		return isEpm;
	}

	public void setEpm(boolean isEpm) {
		this.isEpm = isEpm;
	}

	public boolean isWGM() {
		return isWGM;
	}

	public void setWGM(boolean isWGM) {
		this.isWGM = isWGM;
	}

	public String getCadSync() {
		return cadSync;
	}

	public void setCadSync(String cadSync) {
		this.cadSync = cadSync;
	}

	public boolean isOwnerCheckout() {
		return isOwnerCheckout;
	}

	public void setOwnerCheckout(boolean isOwnerCheckout) {
		this.isOwnerCheckout = isOwnerCheckout;
	}
	
	
	
}
