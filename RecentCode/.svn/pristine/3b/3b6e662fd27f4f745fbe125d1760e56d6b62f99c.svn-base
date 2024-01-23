package com.e3ps.doc.bean;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.util.NumberCodeUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

import wt.fc.PersistenceHelper;

public final class DocAttributeData {

	private String code;
	private String name;
	private String oid;
	private int sort;//DocAttributeLink에 의한 sort Data입니다. 외부에서 set
	
	/**
	 * @author		: hckim
	 * @date		: 2021.09.13
	 * @param oid
	 * @description : Numbercode oid Construct
	 */
	public DocAttributeData(String oid) {
		
		NumberCode code = (NumberCode)CommonUtil.getObject(oid);
		initialize(code);
		
	}
	
	/**
	 * @author		: hckim
	 * @date		: 2021.09.13
	 * @param code
	 * @description : NumberCode Object Construct
	 */
	public DocAttributeData(NumberCode code) {
		
		initialize(code);
		
	}
	
	private void initialize(NumberCode code) {
		
		if(code!=null) {
			this.oid = StringUtil.checkNull(PersistenceHelper.getObjectIdentifier(code).getStringValue()); 
			this.code = StringUtil.checkNull(code.getCode());
			this.name = StringUtil.checkNull(NumberCodeUtil.getLangugeValue(code));
		}
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
