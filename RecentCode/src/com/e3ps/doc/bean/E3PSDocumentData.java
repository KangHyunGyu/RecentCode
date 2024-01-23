/**
 * 
 */
package com.e3ps.doc.bean;

import java.sql.Timestamp;

import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.FolderUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.E3PSDocument;

import wt.util.WTException;

/**
 * @author plmadmin
 *
 */
public class E3PSDocumentData extends RevisionData{
	private String number; 
	private String description; 
	private String spec;
	private String maker;
	private String model;
	private String parentFoid;
	
	private Timestamp downloadDeadline;		// 다운로드 기한
	
	public E3PSDocumentData(String oid) throws Exception {
		super(oid);
		E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
		
		_E3PSDocumentData(doc);
	}
	
	public E3PSDocumentData(E3PSDocument doc) throws Exception {
		super(doc);
		
		_E3PSDocumentData(doc);
	}
	
	public void _E3PSDocumentData(E3PSDocument doc) throws Exception {
		this.number = StringUtil.checkNull(doc.getNumber());
		this.description = StringUtil.checkNull(doc.getDescription());
		this.spec = StringUtil.checkNull(doc.getSpec());
		this.maker = StringUtil.checkNull(doc.getMaker());
		this.model = StringUtil.checkNull(doc.getModel());
		
		// doc 속성 foid
		this.parentFoid = parentFoid(super.getLocation());
	}

	// 부모 foid 찾아서 저장
	public String parentFoid(String folderPath) throws WTException{
		String foid = "";
		String[] location = folderPath.split("/");
		if(location.length > 2){
			foid = FolderUtil.getFolder("/Default/"+location[1]+"/"+location[2])+"";
			if(foid.contains("<")) foid = foid.substring(0, foid.indexOf("<"));
		}
		return foid;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getParentFoid() {
		return parentFoid;
	}

	public void setParentFoid(String parentFoid) {
		this.parentFoid = parentFoid;
	}

	public Timestamp getDownloadDeadline() {
		return downloadDeadline;
	}

	public void setDownloadDeadline(Timestamp downloadDeadline) {
		this.downloadDeadline = downloadDeadline;
	}
	
}
