package com.e3ps.stagegate.bean;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.stagegate.SGObject;
import com.e3ps.stagegate.SGObjectValue;

import wt.util.WTException;

public class SGObjectValueData {
	private String oid;
	private String division;
	private String parentCode;
	private int seq;
	private boolean isSubTitle;
	private String objType;

	private String code;
	private String name;
	private String value0;
	private String value1;
	private String value2;
	private String value3;
	private String value4;
	private String value5;
	private String value6;
	private String value7;
	private String value8;
	private String value9;
	
	private URL imgUrl;
	
	public SGObjectValueData(SGObjectValue objValue) throws JSONException, WTException {
		this.oid = CommonUtil.getOIDString(objValue);
		this.division = StringUtil.checkNull(objValue.getDivision());
		this.parentCode = StringUtil.checkNull(objValue.getParentCode());
		this.seq = objValue.getSeq();
		this.isSubTitle = objValue.isIsSubTitle();
		SGObject obj = objValue.getObj();
		this.objType = obj.getObjType();
		
		JSONObject jObj = new JSONObject(StringUtil.checkNull(objValue.getDivision()));
		this.code = jObj.getString("code");
	    this.name = jObj.getString("name");
	    this.value0 = jObj.getString("value0");
	    this.value1 = jObj.getString("value1");
	    this.value2 = jObj.getString("value2");
	    this.value3 = jObj.getString("value3");
	    this.value4 = jObj.getString("value4");
	    this.value5 = jObj.getString("value5");
	    this.value6 = jObj.getString("value6");
	    this.value7 = jObj.getString("value7");
	    this.value8 = jObj.getString("value8");
	    this.value9 = jObj.getString("value9");
	    
	}


	public URL getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(URL imgUrl) {
		this.imgUrl = imgUrl;
	}


	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
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

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public boolean isSubTitle() {
		return isSubTitle;
	}

	public void setSubTitle(boolean isSubTitle) {
		this.isSubTitle = isSubTitle;
	}

	public String getValue0() {
		return value0;
	}

	public void setValue0(String value0) {
		this.value0 = value0;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	public String getValue5() {
		return value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	public String getValue6() {
		return value6;
	}

	public void setValue6(String value6) {
		this.value6 = value6;
	}

	public String getValue7() {
		return value7;
	}

	public void setValue7(String value7) {
		this.value7 = value7;
	}

	public String getValue8() {
		return value8;
	}

	public void setValue8(String value8) {
		this.value8 = value8;
	}

	public String getValue9() {
		return value9;
	}

	public void setValue9(String value9) {
		this.value9 = value9;
	}
	
	
}
