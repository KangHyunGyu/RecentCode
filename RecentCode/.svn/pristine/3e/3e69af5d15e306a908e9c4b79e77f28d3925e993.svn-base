package com.e3ps.part.bean;

import java.util.Map;

import com.e3ps.common.service.CommonHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.epm.dnc.CadAttributeDNC;
import com.ibm.icu.text.DecimalFormat;

import wt.part.WTPart;
import wt.part.WTPartUsageLink;

@SuppressWarnings("serial")
public class BomTreeExcelData implements java.io.Serializable{
    
    private String number;
    private int level;
    private String name;
    
    private String unit;
    private String quantity = "1";
    
    private String material;
    private String category;
    private String treatment;
    private String assembleType;
    
    private String method;
    private String specification;
    private String weight;
    private String start_company_name;
    private String start_compnay_manager;
    private String start_company_tell;
    private String start_unit_cost;
    private String start_cost;
    private String prod_company_name;
    private String prod_company_manager;
    private String prod_company_tell;
    private String prod_unit_cost;
    private String prod_cost;
    private String description;
    
	public BomTreeExcelData(WTPart part, WTPartUsageLink link, int level) throws Exception{

		String oid = CommonUtil.getOIDString(part);
		
		this.level = level;
        
        this.number = part.getNumber();
        this.name = part.getName();
        
        if(link!=null){
        	this.unit = part.getDefaultUnit().toString().toUpperCase();//link.getQuantity().getUnit().toString().toUpperCase();
        	DecimalFormat df = new DecimalFormat("#");
        	this.quantity = df.format(link.getQuantity().getAmount());
        }else {
        	this.unit = part.getDefaultUnit().toString().toUpperCase();
        }
        
        Map<String, Object> ibaMap = CommonHelper.manager.getAttributes(oid);
        
        this.material = StringUtil.checkNull((String) ibaMap.get(CadAttributeDNC.ATT_MATERIAL.getKey()));
        this.treatment = StringUtil.checkNull((String) ibaMap.get(CadAttributeDNC.ATT_TREATMENT.getKey()));
        this.category = StringUtil.checkNull((String) ibaMap.get(CadAttributeDNC.ATT_CATEGORY.getKey()));
        this.assembleType = StringUtil.checkNull((String) ibaMap.get(CadAttributeDNC.ASSEMBLE_TYPE.getKey()));
        
        this.method = StringUtil.checkNull((String) ibaMap.get("METHOD"));
        this.specification = StringUtil.checkNull((String) ibaMap.get("SPECIFICATION"));
        this.weight = StringUtil.checkNull((String) ibaMap.get("WEIGHT"));
        this.start_company_name = StringUtil.checkNull((String) ibaMap.get("START_COMPANY_NAME"));
        this.start_compnay_manager = StringUtil.checkNull((String) ibaMap.get("START_COMPANY_MANAGER"));
        this.start_company_tell = StringUtil.checkNull((String) ibaMap.get("START_COMPANY_TELL"));
        this.start_unit_cost = StringUtil.checkNull((String) ibaMap.get("START_UNIT_COST"));
        this.prod_company_name = StringUtil.checkNull((String) ibaMap.get("PROD_COMPANY_NAME"));
        this.prod_company_manager = StringUtil.checkNull((String) ibaMap.get("PROD_COMPANY_MANAGER"));
        this.prod_company_tell = StringUtil.checkNull((String) ibaMap.get("PROD_COMPANY_TELL"));
        this.prod_unit_cost = StringUtil.checkNull((String) ibaMap.get("PROD_UNIT_COST"));
        this.description = StringUtil.checkNull((String) ibaMap.get("DESCRIPTION"));
        if(this.start_unit_cost != null && this.start_unit_cost.length() > 0) {
        	this.start_cost = (Integer.parseInt(this.quantity) * Integer.parseInt(this.start_unit_cost)) + "";
        }
        if(this.prod_unit_cost != null && this.prod_unit_cost.length() > 0) {
        	this.prod_cost = (Integer.parseInt(this.quantity) * Integer.parseInt(this.prod_unit_cost)) + "";
        }
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getStart_company_name() {
		return start_company_name;
	}

	public void setStart_company_name(String start_company_name) {
		this.start_company_name = start_company_name;
	}

	public String getStart_compnay_manager() {
		return start_compnay_manager;
	}

	public void setStart_compnay_manager(String start_compnay_manager) {
		this.start_compnay_manager = start_compnay_manager;
	}

	public String getStart_company_tell() {
		return start_company_tell;
	}

	public void setStart_company_tell(String start_company_tell) {
		this.start_company_tell = start_company_tell;
	}

	public String getStart_unit_cost() {
		return start_unit_cost;
	}

	public void setStart_unit_cost(String start_unit_cost) {
		this.start_unit_cost = start_unit_cost;
	}

	public String getProd_company_name() {
		return prod_company_name;
	}

	public void setProd_company_name(String prod_company_name) {
		this.prod_company_name = prod_company_name;
	}

	public String getProd_company_manager() {
		return prod_company_manager;
	}

	public void setProd_company_manager(String prod_company_manager) {
		this.prod_company_manager = prod_company_manager;
	}

	public String getProd_company_tell() {
		return prod_company_tell;
	}

	public void setProd_company_tell(String prod_company_tell) {
		this.prod_company_tell = prod_company_tell;
	}

	public String getProd_unit_cost() {
		return prod_unit_cost;
	}

	public void setProd_unit_cost(String prod_unit_cost) {
		this.prod_unit_cost = prod_unit_cost;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStart_cost() {
		return start_cost;
	}

	public void setStart_cost(String start_cost) {
		this.start_cost = start_cost;
	}

	public String getProd_cost() {
		return prod_cost;
	}

	public void setProd_cost(String prod_cost) {
		this.prod_cost = prod_cost;
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
