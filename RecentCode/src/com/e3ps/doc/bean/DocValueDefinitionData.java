package com.e3ps.doc.bean;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.doc.DocValueDefinition;

public class DocValueDefinitionData {
	private String code;
	private String inputType;
	private String name;
	private String oid;
	private String numberCodeType;

	public DocValueDefinitionData() {
		
	}
	
	public DocValueDefinitionData(DocValueDefinition definition) {
		super();
		this.code = definition.getCode();
		this.inputType = definition.getInputType();
		this.name = definition.getName();
		this.oid = CommonUtil.getOIDString(definition);
		this.numberCodeType = definition.getNumberCodeType();
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getInputType() {
		return inputType;
	}
	
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getNumberCodeType() {
		return numberCodeType;
	}

	public void setNumberCodeType(String numberCodeType) {
		this.numberCodeType = numberCodeType;
	}
	
}
