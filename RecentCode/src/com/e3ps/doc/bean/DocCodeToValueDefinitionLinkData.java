package com.e3ps.doc.bean;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.doc.DocCodeToValueDefinitionLink;
import com.e3ps.doc.DocValueDefinition;


public class DocCodeToValueDefinitionLinkData {
	
	private String oid;
	private int sort;
	private boolean active;
	private DocValueDefinition valueDefinition;
	
	//문서 속성 정보
	private String code;
	private String name;
	private String inputType;
	private String valueOid;
	
	public DocCodeToValueDefinitionLinkData(DocCodeToValueDefinitionLink link) {
		this.oid = CommonUtil.getOIDString(link);
		this.active = !link.isDisabled();
		this.sort = link.getSort();
		this.valueDefinition = link.getValueDefiniton();
		
		if(valueDefinition != null){
			this.code = valueDefinition.getCode();
			this.name = valueDefinition.getName();
			this.inputType = valueDefinition.getInputType();
			this.valueOid = CommonUtil.getOIDString(valueDefinition);
		}
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValueOid() {
		return valueOid;
	}

	public void setValueOid(String valueOid) {
		this.valueOid = valueOid;
	}
	
	public String getId() {
		return oid;
	}
}
