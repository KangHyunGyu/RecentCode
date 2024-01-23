package com.e3ps.change.beans;

import com.e3ps.change.EChangeContents;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;

public class EChangeContentsData {
	
	private String oid;
	private String contents;
	private String name;
	
	public EChangeContentsData(EChangeContents ecc) throws Exception {
		this.oid = WCUtil.getOid(ecc);
		this.name = StringUtil.checkNull(ecc.getName());
		this.contents = StringUtil.checkNull(ecc.getContents());
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
