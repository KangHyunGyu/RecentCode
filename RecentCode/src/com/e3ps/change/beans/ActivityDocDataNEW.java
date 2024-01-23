package com.e3ps.change.beans;

import com.e3ps.change.DocumentActivityOutput;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.E3PSDocumentMaster;
import com.e3ps.doc.service.DocHelper;

import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.enterprise.BasicTemplateProcessor;
import wt.vc.VersionControlHelper;

public class ActivityDocDataNEW {
	private String link_oid;
	private String n_number;
	private String n_name;
	private String n_version;
	private String n_state;
	private String n_oid;
	private String n_icon;
	private String n_act;
	private String n_button;
	public ActivityDocDataNEW(DocumentActivityOutput link) throws Exception {
		this.link_oid = CommonUtil.getOIDString(link);
		E3PSDocument ndoc = (E3PSDocument) DocHelper.manager.getDocument(link.getDocumentNewNumber(),link.getDocumentNewVersion());
		if(ndoc!=null){
			this.n_oid = CommonUtil.getOIDString(ndoc);
			this.n_number = StringUtil.checkNull(ndoc.getNumber());
			this.n_name = StringUtil.checkNull(ndoc.getName());
			E3PSDocument nlastDoc = (E3PSDocument) DocHelper.manager.getLastDocument((E3PSDocumentMaster)ndoc.getMaster());  
			this.n_version = VersionControlHelper.getVersionIdentifier(ndoc).getSeries().getValue() + "(" + VersionControlHelper.getVersionIdentifier(nlastDoc).getSeries().getValue() + ")";
			this.n_state = ndoc.getState().getState().getDisplay();
			this.n_icon = StringUtil.checkNull(BasicTemplateProcessor.getObjectIconImgTag(ndoc));
			this.n_act = "<a href='JavaScript:deleteActiveDocLink(\"" + link_oid+ "\" , \"" + DocumentActivityOutput.NEW_LINK+ "\")'>"
					+"<img src='/Windchill/jsp/portal/images/delete_icon.png' border=0  title='삭제'></a>";
			this.n_button = "<input type='button' class='s_bt03' value='직접작성' onclick='openCreateDoc2(\"" + n_oid+ "\" , \"" + DocumentActivityOutput.NEW_LINK+ "\", \"" + link_oid+ "\", '')'>"
		    		+"<input type='button' class='s_bt03' value='링크등록' onclick='inputLinkOutput(\"" + n_oid+ "\" , \"" + DocumentActivityOutput.NEW_LINK+ "\", \"" + link_oid+ "\")'>";
		}
	}
	


	public String getN_button() {
		return n_button;
	}


	public void setN_button(String n_button) {
		this.n_button = n_button;
	}


	public String getN_act() {
		return n_act;
	}


	public void setN_act(String n_act) {
		this.n_act = n_act;
	}

	public String getN_icon() {
		return n_icon;
	}

	public void setN_icon(String n_icon) {
		this.n_icon = n_icon;
	}

	public String getLink_oid() {
		return link_oid;
	}
	public void setLink_oid(String link_oid) {
		this.link_oid = link_oid;
	}
	public String getN_number() {
		return n_number;
	}
	public void setN_number(String n_number) {
		this.n_number = n_number;
	}
	public String getN_name() {
		return n_name;
	}
	public void setN_name(String n_name) {
		this.n_name = n_name;
	}
	public String getN_version() {
		return n_version;
	}
	public void setN_version(String n_version) {
		this.n_version = n_version;
	}
	public String getN_state() {
		return n_state;
	}
	public void setN_state(String n_state) {
		this.n_state = n_state;
	}
	public String getN_oid() {
		return n_oid;
	}
	public void setN_oid(String n_oid) {
		this.n_oid = n_oid;
	}
	
}
