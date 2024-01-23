package com.e3ps.epm.util;

import com.e3ps.epm.dnc.NumberRuleDNC;
import com.ptc.windchill.uwgm.proesrv.c11n.DocIdentifier;
import com.ptc.windchill.uwgm.proesrv.c11n.EPMDocumentNamingDelegate;

/**
 * xconfmanager -s wt.services/svc/default/com.ptc.windchill.uwgm.proesrv.c11n.EPMDocumentNamingDelegate/null/wt.epm.EPMDocument/0="com.e3ps.epm.util.E3psCadNamingDelegate/singleton" -t codebase\service.properties -p
 * 위 문구는 xconf 파일까지 수정합니다. 원복 시 참고하십시오.
 * 
 * @author hckim
 */
public class E3psCadNamingDelegate implements EPMDocumentNamingDelegate {

	public E3psCadNamingDelegate() {
	}

	public void validateDocumentIdentifier(DocIdentifier docIdentifier) {
		new NumberRuleDNC(docIdentifier);
	}

}