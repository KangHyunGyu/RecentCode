package com.e3ps.doc.bean;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.DocCodeType;

import wt.folder.Folder;

public class DocCodeTypeData {

	private String code, name, engName, description;
	private int sort;
	private boolean active;
	private String oid;
	private Folder folder;
	private String location;
	
	public DocCodeTypeData(DocCodeType Dcode) {
		this.code = Dcode.getCode();
		this.name = Dcode.getName();
		this.engName = Dcode.getEngName();
		this.oid = CommonUtil.getOIDString(Dcode);
		this.description = StringUtil.checkNull(Dcode.getDescription());
		this.sort = Dcode.getSort();
		this.active = !Dcode.isDisabled();
		this.folder = Dcode.getFolder();
		
		if(folder != null) this.location =folder.getFolderPath();
	}
	
	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
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

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getId() {
		return oid;
	}
	
}
