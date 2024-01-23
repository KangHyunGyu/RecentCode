package com.e3ps.common.code.bean;

import java.util.List;

import com.e3ps.common.bean.FolderData;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.code.util.NumberCodeUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

public class NumberCodeData {
	
	private String code;
	private String name;
	private String engName;
	private String oid;
	private String description;
	private boolean isSeq;			//자동 (true),수동(false)
	private String seqNm;
	private boolean isTree;			//Tree(true),
	private int sort;
	private boolean active;
	private String displayOptionTag;
	private String localeNameDisplay;
	
	private String parentCode;
	private String parentName;
	
	private String codeType;
	private String codeTypeName;
	
	private List<NumberCodeData> children;
	
	public NumberCodeData(NumberCode Ncode) {
		this.code = Ncode.getCode();
		this.name = Ncode.getName();
		this.engName = Ncode.getEngName();
		this.oid = CommonUtil.getOIDString(Ncode);
		this.description = StringUtil.checkNull(Ncode.getDescription());
		this.localeNameDisplay = NumberCodeUtil.getLangugeValue(this);
		this.displayOptionTag = "[ " + this.code + " ] " + this.localeNameDisplay;
		NumberCodeType codeType = Ncode.getCodeType();
		this.isSeq = codeType.getShortDescription().equals("true") ? true : false;
		this.seqNm = codeType.getLongDescription();
		this.isTree = codeType.getAbbreviatedDisplay().equals("true") ? true : false;
		this.sort = Ncode.getSort();
		this.active = !Ncode.isDisabled();
		NumberCode parent = Ncode.getParent();
		if(parent != null) {
			this.parentCode = parent.getCode();
			this.parentName = parent.getName();
		}
		this.codeType = codeType.toString();
		this.codeTypeName = codeType.getDisplay();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public boolean isSeq() {
		return isSeq;
	}

	public void setSeq(boolean isSeq) {
		this.isSeq = isSeq;
	}

	public String getSeqNm() {
		return seqNm;
	}

	public void setSeqNm(String seqNm) {
		this.seqNm = seqNm;
	}

	public boolean isTree() {
		return isTree;
	}

	public void setTree(boolean isTree) {
		this.isTree = isTree;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCodeTypeName() {
		return codeTypeName;
	}

	public void setCodeTypeName(String codeTypeName) {
		this.codeTypeName = codeTypeName;
	}
	
	public String getId() {
		return oid;
	}

	public List<NumberCodeData> getChildren() {
		return children;
	}


	public void setChildren(List<NumberCodeData> children) {
		this.children = children;
	}

	public String getDisplayOptionTag() {
		return displayOptionTag;
	}

	public void setDisplayOptionTag(String displayOptionTag) {
		this.displayOptionTag = displayOptionTag;
	}

	public String getLocaleNameDisplay() {
		return localeNameDisplay;
	}

	public void setLocaleNameDisplay(String localeNameDisplay) {
		this.localeNameDisplay = localeNameDisplay;
	}
	
}
