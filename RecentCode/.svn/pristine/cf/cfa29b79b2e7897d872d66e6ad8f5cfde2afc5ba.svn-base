/**
 * 
 */
package com.e3ps.doc;

import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;

/**
 * @author plmadmin
 *
 */
public class ETCDocumentContentsData {
	
	private String oid;
	private String contents;
	private String name;
	
	public ETCDocumentContentsData(ETCDocumentContents docEtc) throws Exception {
		this.oid = WCUtil.getOid(docEtc);
		this.name = StringUtil.checkNull(docEtc.getName());
		this.contents = StringUtil.checkNull(docEtc.getContents());
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
