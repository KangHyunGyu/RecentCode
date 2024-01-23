package com.e3ps.distribute.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.distribute.DistributeRegPartToEpmLink;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptc.wvs.server.util.PublishUtils;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.enterprise.BasicTemplateProcessor;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.Persistable;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.representation.Representation;


public class DistributeRegPartToEpmData {
	
		// VIEW DATA
		private String pOid;					// 품목 OID
		private String pIcon;					// 품목 아이콘
		private String partNumber;				// 품목 번호
		private String partName;				// 품목 명
		private String partVersion;				// 품목 버전
		private String partState;				// 품목 상태
		private String disOid;					// 배포문서 OID
		private String disIcon;					// 배포문서 아이콘
		private String disNumber;				// 배포문서 번호 X (EPMDocumentNumber)
		private String disName;					// 배포문서 명
		private String disFileName;				// 배포문서 파일이름
		private String disVersion;				// 배포문서 버전
		private String disState;				// 배포문서 상태
		
		// DISTRIBUTE DATA - EPMDocument
		private String dwgAppOid; 				// 품목 DWG AppData oid
		private String pdfAppOid; 				// 품목 PDF AppData oid
		private String dwgAppFileName; 			// 품목 DWG 파일 이름
		private String pdfAppFileName;			// 품목 PDF 파일 이름
		
		
		private List<ApplicationData> dataList;
		
		public DistributeRegPartToEpmData(DistributeRegPartToEpmLink link) {
			super();
			
			
			Persistable per = link.getDistributeRegPartToEpm();
			WTPart part = link.getDistributeRegPart().getPart();
			RevisionControlled rev = (RevisionControlled) per;
			
			try {
				// PART DATA 셋팅
				this.pOid = CommonUtil.getOIDString(part);
				this.pIcon = part != null ? BasicTemplateProcessor.getObjectIconImgTag(part) : "";
				this.partNumber = StringUtil.checkNull(part.getNumber());
				this.partName = StringUtil.checkNull(part.getName());
				this.partVersion = StringUtil.checkNull(part.getVersionIdentifier().getValue() + "." + part.getIterationIdentifier().getValue());
				this.partState = StringUtil.checkNull(part.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
				
				this.disOid = CommonUtil.getOIDString(rev);
				this.disIcon = rev != null ? BasicTemplateProcessor.getObjectIconImgTag(rev) : "";
				this.disName = StringUtil.checkNull(rev.getName());
				this.disVersion =  StringUtil.checkNull(rev.getVersionIdentifier().getValue() + "." + rev.getIterationIdentifier().getValue());
				this.disState = StringUtil.checkNull(rev.getLifeCycleState().getDisplay(MessageUtil.getLocale()));
				
				this.dataList = new ArrayList<ApplicationData>();
				
				if(per instanceof EPMDocument) {
					
					EPMDocument epm = (EPMDocument)per;
					
					this.disNumber = StringUtil.checkNull(epm.getNumber());
					
					
					if ("ACAD".equals(epm.getAuthoringApplication().toString()) && "CADDRAWING".equals(epm.getDocType().toString())) {
						QueryResult qr = ContentHelper.service.getContentsByRole(epm, ContentRoleType.PRIMARY);
						
						while(qr.hasMoreElements()){
							ContentItem contentitem = (ContentItem) qr.nextElement();
							ApplicationData drawAppData = (ApplicationData) contentitem;
							dataList.add(drawAppData);
							//wt.content.ApplicationData:4874928
						}
					}
					
					
					
//					if ("ACAD".equals(epm.getAuthoringApplication().toString()) || "CADDRAWING".equals(epm.getDocType().toString())) {
//						Representation representation = PublishUtils.getRepresentation(epm);
//						if (representation != null) {
//							representation = (Representation) ContentHelper.service.getContents(representation);
//							Vector contentList = ContentHelper.getContentList(representation);
//							for (int l = 0; l < contentList.size(); l++) {
//								ContentItem contentitem = (ContentItem) contentList.elementAt(l);
//								if (contentitem instanceof ApplicationData) {
//									ApplicationData drawAppData = (ApplicationData) contentitem;
////									boolean isAdditionalFile = drawAppData.getRole().toString().equals("ADDITIONAL_FILES");//SECONDARY
//									boolean isAdditionalFile = drawAppData.getRole().toString().equals("SECONDARY");
//									boolean isAutoCad = isAdditionalFile && (drawAppData.getFileName().toUpperCase().lastIndexOf("DWG") > 0);
//									boolean isPdf = isAdditionalFile && (drawAppData.getFileName().toUpperCase().lastIndexOf("PDF") > 0);
//									if (isAutoCad) {
//										dataList.add(drawAppData);
//										this.dwgAppOid = CommonUtil.getOIDString(drawAppData);
//										this.dwgAppFileName = drawAppData.getFileName();
//										this.disFileName = drawAppData.getFileName();
//									}else if(isPdf) {
//										dataList.add(drawAppData);
//										this.pdfAppOid = CommonUtil.getOIDString(drawAppData);
//										this.pdfAppFileName = drawAppData.getFileName();
//										if(this.disFileName != null) {
//											this.disFileName += "," + drawAppData.getFileName();
//										}else {
//											this.disFileName = drawAppData.getFileName();
//										}
//									}
//								}
//							}
//						}
//					}else {
//						ContentRoleType roleType = ContentRoleType.PRIMARY;
//						ContentHolder holder = ContentHelper.service.getContents((ContentHolder) epm);
//						
//						QueryResult qr = ContentHelper.service.getContentsByRole(holder, roleType);
//						
//						while (qr.hasMoreElements()) {
//							ContentItem contentitem = (ContentItem) qr.nextElement();
//							if (contentitem != null) {
//								ApplicationData data = (ApplicationData) contentitem;
//								dataList.add(data);
//								this.disFileName = data.getFileName();
//							}
//						}
//					}
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@JsonIgnore
		public List<ApplicationData> getDataList() {
			return dataList;
		}

		public String getpOid() {
			return pOid;
		}

		public void setpOid(String pOid) {
			this.pOid = pOid;
		}

		public String getpIcon() {
			return pIcon;
		}

		public void setpIcon(String pIcon) {
			this.pIcon = pIcon;
		}

		public String getPartNumber() {
			return partNumber;
		}

		public void setPartNumber(String partNumber) {
			this.partNumber = partNumber;
		}

		public String getPartName() {
			return partName;
		}

		public void setPartName(String partName) {
			this.partName = partName;
		}

		public String getPartVersion() {
			return partVersion;
		}

		public void setPartVersion(String partVersion) {
			this.partVersion = partVersion;
		}

		public String getPartState() {
			return partState;
		}

		public void setPartState(String partState) {
			this.partState = partState;
		}

		public String getDisOid() {
			return disOid;
		}

		public void setDisOid(String disOid) {
			this.disOid = disOid;
		}

		public String getDisIcon() {
			return disIcon;
		}

		public void setDisIcon(String disIcon) {
			this.disIcon = disIcon;
		}

		public String getDisNumber() {
			return disNumber;
		}

		public void setDisNumber(String disNumber) {
			this.disNumber = disNumber;
		}

		public String getDisName() {
			return disName;
		}

		public void setDisName(String disName) {
			this.disName = disName;
		}

		public String getDisFileName() {
			return disFileName;
		}

		public void setDisFileName(String disFileName) {
			this.disFileName = disFileName;
		}

		public String getDisVersion() {
			return disVersion;
		}

		public void setDisVersion(String disVersion) {
			this.disVersion = disVersion;
		}

		public String getDisState() {
			return disState;
		}

		public void setDisState(String disState) {
			this.disState = disState;
		}

		public String getDwgAppOid() {
			return dwgAppOid;
		}

		public void setDwgAppOid(String dwgAppOid) {
			this.dwgAppOid = dwgAppOid;
		}

		public String getPdfAppOid() {
			return pdfAppOid;
		}

		public void setPdfAppOid(String pdfAppOid) {
			this.pdfAppOid = pdfAppOid;
		}

		public String getDwgAppFileName() {
			return dwgAppFileName;
		}

		public void setDwgAppFileName(String dwgAppFileName) {
			this.dwgAppFileName = dwgAppFileName;
		}

		public String getPdfAppFileName() {
			return pdfAppFileName;
		}

		public void setPdfAppFileName(String pdfAppFileName) {
			this.pdfAppFileName = pdfAppFileName;
		}

		
}
