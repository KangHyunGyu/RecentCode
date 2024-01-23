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

public class ActivityDocDataOLD {
	private String link_oid;
	private String o_number;
	private String o_name;
	private String o_version;
	private String o_state;
	private String o_oid;
	private String o_icon;
	private String o_act;
	private String o_button;
	public ActivityDocDataOLD(DocumentActivityOutput link) throws Exception {
		this.link_oid = CommonUtil.getOIDString(link);
		E3PSDocument odoc = (E3PSDocument) DocHelper.manager.getDocument(link.getDocumentOldNumber(),link.getDocumentOldVersion());
		if(odoc!=null){
			this.o_oid = CommonUtil.getOIDString(odoc);
			this.o_number = StringUtil.checkNull(odoc.getNumber());
			this.o_name = StringUtil.checkNull(odoc.getName());
			E3PSDocument olastDoc = (E3PSDocument) DocHelper.manager.getLastDocument((E3PSDocumentMaster)odoc.getMaster());  
			this.o_version = VersionControlHelper.getVersionIdentifier(odoc).getSeries().getValue() + "(" + VersionControlHelper.getVersionIdentifier(olastDoc).getSeries().getValue() + ")";
			this.o_state = odoc.getState().getState().getDisplay();
			this.o_icon = StringUtil.checkNull(BasicTemplateProcessor.getObjectIconImgTag(odoc));
			this.o_act = "<a href='JavaScript:deleteActiveDocLink(\"" + link_oid+ "\" , \"" + DocumentActivityOutput.OLD_LINK+ "\")'>"
					+"<img src='/Windchill/jsp/portal/images/delete_icon.png' border=0  title='삭제'></a>";
			this.o_button = "<input type='button' class='s_bt03' value='직접작성' onclick='openCreateDoc2(\"" + o_oid+ "\" , \"" + DocumentActivityOutput.OLD_LINK+ "\", \"" + link_oid+ "\", '')'>"
    		+"<input type='button' class='s_bt03' value='링크등록' onclick='inputLinkOutput(\"" + o_oid+ "\" , \"" + DocumentActivityOutput.OLD_LINK+ "\", \"" + link_oid+ "\")'>";
			
		}
	}
	
	
	public String getO_button() {
		return o_button;
	}


	public void setO_button(String o_button) {
		this.o_button = o_button;
	}


	public String getO_act() {
		return o_act;
	}


	public void setO_act(String o_act) {
		this.o_act = o_act;
	}

	public String getO_icon() {
		return o_icon;
	}

	public void setO_icon(String o_icon) {
		this.o_icon = o_icon;
	}

	public String getLink_oid() {
		return link_oid;
	}
	public void setLink_oid(String link_oid) {
		this.link_oid = link_oid;
	}
	public String getO_number() {
		return o_number;
	}
	public void setO_number(String o_number) {
		this.o_number = o_number;
	}
	public String getO_name() {
		return o_name;
	}
	public void setO_name(String o_name) {
		this.o_name = o_name;
	}
	public String getO_version() {
		return o_version;
	}
	public void setO_version(String o_version) {
		this.o_version = o_version;
	}
	public String getO_state() {
		return o_state;
	}
	public void setO_state(String o_state) {
		this.o_state = o_state;
	}
	public String getO_oid() {
		return o_oid;
	}
	public void setO_oid(String o_oid) {
		this.o_oid = o_oid;
	}
}
