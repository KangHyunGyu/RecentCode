package com.e3ps.part.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.epm.dnc.CadAttributeDNC;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.epm.util.EpmUtil;
import com.e3ps.part.util.PartUtil;

import wt.epm.EPMDocument;
import wt.part.WTPart;
import wt.part.WTPartUsageLink;

@SuppressWarnings("serial")
public class BomTreeData implements java.io.Serializable{
    
	private WTPart part;
	private EPMDocument epm;
	
	private String id;
	private String treeId = "";
    private String oid; //자 품목
    private String number;
    private int level;
    private String name;
    private String state;
    private String stateName;
    private String version;
    private String rev;
    private String revision;
    private String unit;
    private double quantity = 1;
    private int seq;
    private String parent;
    private String icon;
    
    private String linkOid;
    
    private String customer_part_number;
    private String unit_cost;
    private String prod_company_name;
    
  ///
  	private String material;
  	private String category;
  	private String treatment;
  	private String assembleType;
  	///
    
    private String checkoutState;
    private boolean isOwnerCheckout = false;
    
    private List<BomTreeData> children;
    
    private String parentOid;
    private String parentNumber;
    private String parentName;
    private String parentRevision;
    
    private String cadSync;
    
	public BomTreeData(WTPart part, WTPartUsageLink link, int level, int seq, String parentTreeId) throws Exception{
		this.part = part;
		
		this.oid = CommonUtil.getOIDString(part);
        this.level = level;
        this.seq = seq;
        
        PartData data = new PartData(part);
        
        this.number = data.getNumber();
        this.name = data.getName();
        
        this.version = data.getVersion();
        this.rev = data.getRev();
        this.revision = data.getRevision();
        this.state = data.getState();
        this.stateName = data.getStateName();
        this.icon = data.getIcon();
        
        this.checkoutState = data.getCheckoutState();
        this.isOwnerCheckout = PartUtil.isOwnerCheckOut(this.part);
        if(link!=null){
        	this.unit =  part.getDefaultUnit().toString();//link.getQuantity().getUnit().toString();
        	this.quantity = link.getQuantity().getAmount();
        	this.linkOid = CommonUtil.getOIDString(link);
        	WTPart parentPart =(WTPart)link.getRoleAObject();
        	this.parentOid = CommonUtil.getOIDString(parentPart);
        	this.parentNumber = parentPart.getNumber();
        	this.parentName = parentPart.getName();
        	this.parentRevision = parentPart.getVersionIdentifier().getSeries().getValue();
        	
        	this.cadSync = link.getCadSynchronized().toString();
        }else {
        	this.unit = part.getDefaultUnit().toString();
        }
        
        NumberCode materialCode = CodeHelper.manager.getNumberCode("MATERIAL", IBAUtil.getAttrValue(part, "MATERIAL"));
        if(materialCode != null) {
        	this.material = materialCode.getName();
        }
        
        //
        @SuppressWarnings("rawtypes")
		HashMap attrHash = IBAUtil.getAttributes(part);
        this.treatment = (String)attrHash.get(CadAttributeDNC.ATT_TREATMENT.getKey());
		this.material = (String)attrHash.get(CadAttributeDNC.ATT_MATERIAL.getKey());
		this.category = (String)attrHash.get(CadAttributeDNC.ATT_CATEGORY.getKey());
		this.assembleType = (String)attrHash.get(CadAttributeDNC.ASSEMBLE_TYPE.getKey());
        
        
//        this.customer_part_number = IBAUtil.getAttrValue(part, "CUSTOMER_PART_NUMBER");
//        this.unit_cost = IBAUtil.getAttrValue(part, "UNIT_COST");
//        this.prod_company_name = IBAUtil.getAttrValue(part, "PROD_COMPANY_NAME");
        
        
        this.parent = parentTreeId;
        if(this.parent.length() > 0) {
        	this.treeId = this.parent + "-";
        }
        this.treeId += this.level + "_" + this.seq;
        
        this.id = treeId;
    }

	public String getThumbnail() throws Exception{
		this.epm = EpmHelper.manager.getEPMDocument(this.part);
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

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public List<BomTreeData> getChildren() {
		return children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setChildren(List<BomTreeData> children) {
		
		if(this.assembleType != null && (this.assembleType.indexOf("구매품") > -1 || this.assembleType.indexOf("분리불가") > -1)) {
			
		}else {
			this.children = children;
		}
		
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

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getParentOid() {
		return parentOid;
	}

	public void setParentOid(String parentOid) {
		this.parentOid = parentOid;
	}

	public String getParentNumber() {
		return parentNumber;
	}

	public void setParentNumber(String parentNumber) {
		this.parentNumber = parentNumber;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentRevision() {
		return parentRevision;
	}

	public void setParentRevision(String parentRevision) {
		this.parentRevision = parentRevision;
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

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getCustomer_part_number() {
		return customer_part_number;
	}

	public void setCustomer_part_number(String customer_part_number) {
		this.customer_part_number = customer_part_number;
	}

	public String getUnit_cost() {
		return unit_cost;
	}

	public void setUnit_cost(String unit_cost) {
		this.unit_cost = unit_cost;
	}

	public String getProd_company_name() {
		return prod_company_name;
	}

	public void setProd_company_name(String prod_company_name) {
		this.prod_company_name = prod_company_name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getAssembleType() {
		return assembleType;
	}

	public void setAssembleType(String assembleType) {
		this.assembleType = assembleType;
	}
}
