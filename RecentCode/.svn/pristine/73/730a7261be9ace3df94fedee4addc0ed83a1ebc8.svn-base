/**
 * 
 */
package com.e3ps.doc;

import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.doc.E3PSDocument;

/**
 * @author plmadmin
 *
 */
public class PDRDocumentContentsData {
	
	private String oid;
	private String contents;
	private String name;
	
	public PDRDocumentContentsData(PDRDocumentContents pdrdc) throws Exception {
		this.oid = WCUtil.getOid(pdrdc);
		this.name = StringUtil.checkNull(pdrdc.getName());
		this.contents = StringUtil.checkNull(pdrdc.getContents());
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
