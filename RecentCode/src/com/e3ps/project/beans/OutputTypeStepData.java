package com.e3ps.project.beans;

import java.util.List;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;

public class OutputTypeStepData {
	private String code;
	private String name;
	private String oid;
	private String description;
	private boolean isSeq;			//자동 (true),수동(false)
	private String seqNm;
	private boolean isTree;			//Tree(true),
	private int sort;
	private boolean active;
	
	private String parentCode;
	private String parentName;
	
	private String codeType;
	private String codeTypeName;
	
	private List<OutputTypeStepData> children;
	
	public OutputTypeStepData(OutputTypeStep Ncode) {
		this.code = Ncode.getCode();
		this.name = Ncode.getName();
		this.oid = CommonUtil.getOIDString(Ncode);
		this.description = StringUtil.checkNull(Ncode.getDescription());
		OutputType codeType = Ncode.getOutputType();
		this.isSeq = codeType.getShortDescription().equals("true") ? true : false;
		this.seqNm = codeType.getLongDescription();
		this.isTree = codeType.getAbbreviatedDisplay().equals("true") ? true : false;
		this.sort = Ncode.getSort();
		OutputTypeStep parent = Ncode.getParent();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
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

	public List<OutputTypeStepData> getChildren() {
		return children;
	}

	public void setChildren(List<OutputTypeStepData> children) {
		this.children = children;
	}

	public String getId() {
		return oid;
	}
}
