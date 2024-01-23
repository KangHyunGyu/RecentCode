package com.e3ps.doc.bean;

import java.util.List;
import java.util.Map;

import com.e3ps.approval.service.ApprovalHelper;
//import com.e3ps.change.ECOChange;
import com.e3ps.common.bean.RevisionData;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.content.util.ContentUtil;
import com.e3ps.common.iba.AttributeKey;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.doc.DocKey;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.service.DocHelper;
import com.e3ps.part.bean.PartData;
import com.e3ps.project.EProject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import wt.content.ApplicationData;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.URLData;
import wt.doc.WTDocument;
import wt.doc.WTDocumentDependencyLink;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.part.WTPart;



public class DocData extends RevisionData {
	
	private String number; 
	private String description; 
	
	//문서구분(DocumentTypeRB)
	private String docType;
	private String docTypeCode;
	
	//유형(NumberCode.java)
	private String docAttribute;
	private String docAttributeCode;
	private String docAttributeName;
	
	private String relPartsString;
	
	private String linkOid;
	
	private String refProjectOid;
	private String refProject;
	
	private String refPartOid;
	private String refPart;
	
	// ApplicationData check
	private boolean isPrimaryFile;
	
	// URL Data Info
	private String urlLocation;
	private String urlDisplayName;
	private String lastApprover;
	
	//문서 품목 속성
	private E3PSDocument doc;
	private Map<String, Object> attributes;
	
	
	public DocData(String oid) throws Exception {
		super(oid);
		E3PSDocument doc = (E3PSDocument) CommonUtil.getObject(oid);
		
		_DocumentData(doc, true);
	}
	
	public DocData(WTDocument doc) throws Exception {
		super(doc);
		
		_DocumentData(doc, true);
	}
	
	/**
	 * @author		: hckim
	 * @date		: 2021.10.06
	 * @param doc
	 * @param searchDetail 전체 검색 시 필요없는 데이터
	 * @throws Exception
	 * @description :  
	 */
	public DocData(WTDocument wtDoc, boolean searchDetail) throws Exception{
		super(wtDoc);
		_DocumentData(wtDoc, searchDetail);
	}
	
	
	public void _DocumentData(WTDocument wtDoc, boolean searchDetail) throws Exception {
		
		E3PSDocument doc = (E3PSDocument)wtDoc;
		this.doc = doc;
		this.docType = StringUtil.checkReplaceStr(doc.getDocType().getFullDisplay(), "");
		this.docTypeCode = doc.getDocType().toString();
		this.number = StringUtil.checkNull(doc.getNumber());
		this.description = StringUtil.checkNull(doc.getDescription());

		NumberCode attr = CodeHelper.manager.getNumberCode("DOCCODETYPE", StringUtil.checkNull(doc.getDocAttribute()));
		
		if(attr != null) {
			this.docAttribute = CommonUtil.getOIDString(attr);
			this.docAttributeCode = StringUtil.checkReplaceStr(attr.getCode(), "");
			if(MessageUtil.isLangKor()) {
				this.docAttributeName = StringUtil.checkReplaceStr(attr.getName(), "");
			} else {
				this.docAttributeName = StringUtil.checkReplaceStr(attr.getEngName(), "");
			}
		}
			
		String container = doc.getContainer().getName();
		
		super.setContainer(container);
		
		this.relPartsString = "";
		
		List<EProject> projectData = DocHelper.manager.getDocProjectLink(doc);
		
		//관련 프로젝트 여러개 엮게되면 코드 수정해야함.
		for (EProject project : projectData) {
			this.refProject = project.getCode();
			this.refProjectOid = project.getPersistInfo().getObjectIdentifier().getStringValue();
		}
		
		
		
		//관련 프로젝트 설변 정보 가져오기
		//initEchangeProjectInfoString(doc);
		
		//관련 품목 가져오기(참조문서의 경우 본 문서의 관련품목, 본 도면의 관련품목까지 가져와서 보여준다.)
		getPartsString(doc);
		
		getRefPart(doc);

		ContentItem contentItem = ContentUtil.getPrimaryContent((ContentHolder) doc);
		
		if(contentItem != null) {
			this.isPrimaryFile = ApplicationData.class.toString().equals(contentItem.getClass().toString());
			if(this.isPrimaryFile) {
				//파일 관련 정보 가져오기
				primaryFile();
			} else {
				URLData urlData = (URLData) contentItem;
				this.urlLocation = urlData.getUrlLocation();
				this.urlDisplayName = urlData.getDisplayName();
			}
		}
		
		if(searchDetail && false) {
			searchDetail(doc);
		}
		
		loadAttributes();
		
		WTUser lastAppUser = ApprovalHelper.manager.getLastApprover(doc);
		this.lastApprover = lastAppUser!=null?lastAppUser.getFullName():"";
		
	}
	
	/**
	 * 문서 품목 속성 로드
	 * @methodName : loadAttributes
	 * @author : hgkang
	 * @date : 2027.07.27
	 * @return : void
	 * @description :
	 */
	public void loadAttributes() throws Exception{
		this.attributes = IBAUtil.getAttributes(this.doc);
		
		
	}

	
	/**
	 * 수정 도메인 권한이 있고, 진행중 문서(작업중,반려됨)
	 * @methodName	: modifyBtn
	 * @author		: tsuam
	 * @date		: 2022.01.12
	 * @return
	 * @description :
	 */
	public boolean modifyBtn() throws Exception{
	
		return super.modifyBtn();
		
	}
	
	/**
	 * 삭제  삭제 권한 부여 ,작업중, 반려됨
	 * @methodName	: deleteBtn
	 * @author		: tsuam
	 * @date		: 2022.01.12
	 * @return
	 * @description :
	 */
	public boolean deleteBtn() throws Exception {
		
		return super.deleteBtn();
		
	}
	
	/**
	 * 개정 개정권한 & 승인됨 & 참조문서가 아닌 (일반문서-제어문서)
	 * @methodName	: reviseBtn
	 * @author		: tsuam
	 * @date		: 2022.01.12
	 * @return
	 * @throws Exception
	 * @description :
	 */
	public boolean reviseBtn() throws Exception{
		
		return super.reviseBtn();
		
	}
	
	//
	private void getPartsString(E3PSDocument doc) throws Exception{
		
		String docType = doc.getDocType().toString();
		if(DocKey.ENUM_TYPE_REFDOC.getKey().equals(docType)) {
			QueryResult result = PersistenceHelper.manager.navigate(doc, WTDocumentDependencyLink.HAS_DEPENDENT_ROLE, WTDocumentDependencyLink.class);
			
			while(result.hasMoreElements()) {
				E3PSDocument parentDoc = (E3PSDocument)result.nextElement(); 
				List<PartData> parts = DocHelper.manager.getRelatedPart(parentDoc);
				
				if(parts.size() > 0) {
					for(PartData part : parts) {
						this.relPartsString += part.getNumber() + "; ";
					}
				}
			}			
		}else {
			List<PartData> parts = DocHelper.manager.getRelatedPart(doc);
			
			if(parts.size() > 0) {
				
				for(PartData part : parts) {
					this.relPartsString += part.getNumber() + "; ";
				}
				
			}
		}
		if(relPartsString.length() > 0) {
			this.relPartsString = this.relPartsString.substring(0, this.relPartsString.lastIndexOf(";"));
		}
	}
	
	@JsonIgnore
	private void getRefPart(E3PSDocument doc) throws Exception{
		List<PartData> parts = DocHelper.manager.getRelatedPart(doc);
		if(parts.size() > 0) {
			for(PartData part : parts) {
				this.refPart = part.getNumber();
				this.refPartOid = part.getOid();
			}
		}
		
	}
	
//	private void initEchangeProjectInfoString(WTDocument doc) throws Exception {
//		
//		this.eChangeProjectsNameString = "";
//		this.eChangeProjectsNumberString = "";
//		
//		List<Persistable> perList = DocHelper.manager.getObjectByRefereceDoc(doc);
//		
//		Collections.sort(perList, new Comparator<Persistable>() {
//			@Override
//			public int compare(Persistable o1, Persistable o2) {
//				try {
//					return o1.getClassInfo().getClassname().compareTo(o2.getClassInfo().getClassname());
//				}catch(Exception e) {
//					return -1;
//				}
//			}
//		});
//		
////		for(Persistable per : perList) {
////			
////			if(per instanceof EProject) {
////				EProject project = (EProject)per;
////				this.eChangeProjectsNumberString += project.getCode()+" ";
////				this.eChangeProjectsNameString += project.getName()+" ";
////			}else if(per instanceof ECOChange) {
////				ECOChange eChange = (ECOChange)per;
////				this.eChangeProjectsNumberString += eChange.getEoNumber()+" ";
////				this.eChangeProjectsNameString += eChange.getEoName()+" ";
////			}
////		}
////		
//		
//	}
	
	private void searchDetail(WTDocument doc) {
		try {
			//RevisionData
			super.primaryFile();
			
			if(doc.getDocType() != null) {
				//참조문서는 개정이 불가함
				//this.isRevise = super.reviseBtn() && !doc.getDocType().toString().equals(DocKey.ENUM_TYPE_REFDOC.getKey());
			}
			
		
		}catch(Exception e) {
			e.printStackTrace();
		}
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

	public String getDocAttribute() {
		return docAttribute;
	}

	public void setDocAttribute(String docAttribute) {
		this.docAttribute = docAttribute;
	}

	public String getDocAttributeCode() {
		return docAttributeCode;
	}

	public void setDocAttributeCode(String docAttributeCode) {
		this.docAttributeCode = docAttributeCode;
	}

	public String getDocAttributeName() {
		return docAttributeName;
	}

	public void setDocAttributeName(String docAttributeName) {
		this.docAttributeName = docAttributeName;
	}

	public String getRelPartsString() {
		return relPartsString;
	}

	public void setRelPartsString(String relPartsString) {
		this.relPartsString = relPartsString;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocTypeCode() {
		return docTypeCode;
	}

	public void setDocTypeCode(String docTypeCode) {
		this.docTypeCode = docTypeCode;
	}

	public String getLinkOid() {
		return linkOid;
	}

	public void setLinkOid(String linkOid) {
		this.linkOid = linkOid;
	}

	public String getRefProjectOid() {
		return refProjectOid;
	}

	public void setRefProjectOid(String refProjectOid) {
		this.refProjectOid = refProjectOid;
	}

	public String getRefProject() {
		return refProject;
	}

	public void setRefProject(String refProject) {
		this.refProject = refProject;
	}

	public String getRefPartOid() {
		return refPartOid;
	}

	public void setRefPartOid(String refPartOid) {
		this.refPartOid = refPartOid;
	}

	public String getRefPart() {
		return refPart;
	}

	public void setRefPart(String refPart) {
		this.refPart = refPart;
	}

	public boolean isPrimaryFile() {
		return isPrimaryFile;
	}

	public void setPrimaryFile(boolean isPrimaryFile) {
		this.isPrimaryFile = isPrimaryFile;
	}

	public String getUrlLocation() {
		return urlLocation;
	}

	public void setUrlLocation(String urlLocation) {
		this.urlLocation = urlLocation;
	}

	public String getUrlDisplayName() {
		return urlDisplayName;
	}

	public void setUrlDisplayName(String urlDisplayName) {
		this.urlDisplayName = urlDisplayName;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getLastApprover() {
		return lastApprover;
	}

	public void setLastApprover(String lastApprover) {
		this.lastApprover = lastApprover;
	}
	
	
}
