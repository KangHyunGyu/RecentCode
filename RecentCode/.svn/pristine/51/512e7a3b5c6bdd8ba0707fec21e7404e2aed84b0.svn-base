/**
 * @(#)	DepartmentData.java
 * Copyright (c) e3ps. All rights reserverd
 * 
 * @version 1.00
 * @since jdk 1.8
 * @author shkim
 */

package com.e3ps.org.bean;

import java.util.List;

import com.e3ps.common.bean.FolderData;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.org.Department;

public class DepartmentData {
	
	private String oid;
	private String name;
	private String code;
	private int sort;
	private String parentOid;
	private String parentCode;
	private boolean isDisable;
	
	private List<DepartmentData> children;
	public DepartmentData(String oid) throws Exception {
		this((Department) CommonUtil.getObject(oid));
	}

	public DepartmentData(Department department) throws Exception {
		
		this.oid = CommonUtil.getOIDString(department);
		this.name = department.getName();
		this.code = department.getCode();
		this.sort = department.getSort();
		this.isDisable = department.isIsDisable();
		
		Department parent = (Department) department.getParent();
		if(parent != null) {
			this.parentOid = CommonUtil.getOIDString(parent);
			this.parentCode = parent.getCode();
		}
		
	}

	
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getParentOid() {
		return parentOid;
	}

	public void setParentOid(String parentOid) {
		this.parentOid = parentOid;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	public List<DepartmentData> getChildren() {
		return children;
	}

	public void setChildren(List<DepartmentData> children) {
		this.children = children;
	}
	
	public String getId() {
		return oid;
	}
}