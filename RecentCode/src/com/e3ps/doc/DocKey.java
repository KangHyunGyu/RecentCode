package com.e3ps.doc;

/**
 * @file	: DocKey.java
 * @author	: hckim
 * @date	: 2021.09.13
 * @description : Document 모듈과 관련된 EL String Key를 열거합니다.
 */
public enum DocKey {
	
	MODULE_DOC("DOC", "DOC", "0", null),
	
	//ADMIN 문서타입관리
	//DOCTYPE_GENERAL("General")
	DOCTYPE_CLASS_PROJECT("프로젝트", "프로젝트", "501", null),
	DOCTYPE_CLASS_CHANGE("설계변경", "설계변경", "502", null),
	
	NUMBERCODE_DOCATTRIBUTE("DOCCODETYPE", "DOCCODETYPE", "501", null),
	 
	//Enum DocumentTypeRB
	ENUM_TYPE_GENERAL("$$Document", "문서", "501", null),
	ENUM_TYPE_ORACAD("$$OraCadDoc", "제어문서", "502", null),
	ENUM_TYPE_REFDOC("$$RefDoc", "참조문서", "503", null),
	
	
	//IBA
	IBA_KEY_DOCCODETYPE("DocCodeType", "DocCodeType", "501", null),
	IBA_KEY_DOCATTURIBUTE("DocAttribute", "DocAttribute", "502", null),
	IBA_PROJECTCODE("ProjectCode", "ProjectCode", "503", null);
	
	private final String key;
	private final String display;
	private final String order;
	private final Object[] props;
	
	private DocKey(final String key, final String display ,final String order, final Object[] props){
		this.key = (String)key;
		this.display = (String)display;
		this.order = order;
		this.props = props;
	}
	
	
	public String getKey() {
		return this.key;
	}
	
	public String getDisplay() {
		return this.display;
	}
	
	public String getOrder() {
		return this.order;
	}
	
	public Object[] getProperties() {
		return this.props;
	}

}
