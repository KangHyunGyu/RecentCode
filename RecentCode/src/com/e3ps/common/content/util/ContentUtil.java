package com.e3ps.common.content.util;

import java.beans.PropertyVetoException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.doc.E3PSDocument;
import com.ptc.wvs.common.ui.VisualizationHelper;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.epm.EPMDocument;
import wt.fc.QueryResult;
import wt.httpgw.URLFactory;
import wt.representation.Representation;
import wt.util.EncodingConverter;
import wt.util.WTException;

public class ContentUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	/**
	 * 
	 * @desc	: Primary 다운로드 URL
	 * @author	: tsuam
	 * @date	: 2019. 9. 19.
	 * @method	: getPrimaryUrl
	 * @return	: String
	 * @param holder
	 * @return
	 * @throws WTException
	 */
	public static String getPrimaryUrl(ContentHolder holder) throws WTException {
		String url = "";

		ApplicationData appData = getPrimaryFile(holder);
		if(appData != null) {
			url = CommonUtil.getURLString("/content/fileDownload") + "?holderOid=" + CommonUtil.getOIDString(holder) + "&appOid=" + CommonUtil.getOIDString(appData);
		}
		
		return url;
	}
	
	/**
	 * 
	 * @desc	: Primary 파일 
	 * @author	: tsuam
	 * @date	: 2019. 9. 19.
	 * @method	: getPrimaryFile
	 * @return	: ApplicationData
	 * @param holder
	 * @return
	 * @throws WTException
	 */
	public static ApplicationData getPrimaryFile(ContentHolder holder)throws WTException{
		
		ContentItem item = null;
		QueryResult result = ContentHelper.service.getContentsByRole (holder,ContentRoleType.PRIMARY );
		while (result.hasMoreElements ()) {
			item = (ContentItem) result.nextElement ();
		}
		
		return (ApplicationData) item;
	}
	
	public static List<ApplicationData> getDistributeFile(ContentHolder holder)throws WTException, PropertyVetoException{
		
		List<ApplicationData> list = new ArrayList<ApplicationData>();
		
		if(holder instanceof EPMDocument) {
			EPMDocument epm = (EPMDocument) holder;
			
//			QueryResult qr = ContentHelper.service.getContentsByRole(holder, ContentRoleType.ADDITIONAL_FILES);
//			while (qr.hasMoreElements()) {
//				ContentItem item = (ContentItem) qr.nextElement();
//				if (item != null) {
//					ApplicationData data = (ApplicationData) item;
//					boolean isAutoCad = (data.getFileName().toUpperCase().lastIndexOf("DWG") > 0);
//					boolean isPdf = (data.getFileName().toUpperCase().lastIndexOf("PDF") > 0);
//					if (isAutoCad) {
//						list.add(data);
//					}else if(isPdf) {
//						list.add(data);
//					}
//				}
//			}
			
			if("PROE".equals(epm.getAuthoringApplication().toString()) || "CADDRAWING".equals(epm.getDocType().toString())) {
				VisualizationHelper visualizationHelper = new VisualizationHelper();
				QueryResult epmReps = visualizationHelper.getRepresentations(epm);
				if (epmReps != null) {
					while (epmReps.hasMoreElements()) {
						Representation representation = (Representation) epmReps.nextElement();
						if("PROE".equals(epm.getAuthoringApplication().toString())) {
							QueryResult result = ContentHelper.service.getContentsByRole(representation, ContentRoleType.ADDITIONAL_FILES);
							while (result.hasMoreElements()) {
								Object appObj = result.nextElement();
								if (appObj instanceof ApplicationData) {
									ApplicationData appData = (ApplicationData) appObj;
									if ((appData.getFileName().toLowerCase()).endsWith("dwg")){
										list.add(appData);
									}
									if ((appData.getFileName().toLowerCase()).endsWith("pdf")){
										list.add(appData);
									}
								}
							}
						}
					}
				}
			}else {
				QueryResult qr = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
				while (qr.hasMoreElements()) {
					Object appObj = qr.nextElement();
					if (appObj instanceof ApplicationData) {
						ApplicationData appData = (ApplicationData) appObj;
						list.add(appData);
					}
				}
			}
			
		}
//		else if(holder instanceof E3PSDocument) {
//			QueryResult qr = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
//			while (qr.hasMoreElements()) {
//				ContentItem item = (ContentItem) qr.nextElement();
//				if (item != null) {
//					ApplicationData data = (ApplicationData) item;
//					list.add(data);
//				}
//			}
//		}
		
		return list;
	}
	
	/**
	 *  
	 * @desc	: Secondary File 리스트
	 * @author	: tsuam
	 * @date	: 2019. 9. 19.
	 * @method	: getSecondaryFile
	 * @return	: List<ApplicationData>
	 * @param holder
	 * @return
	 * @throws WTException
	 */
	public static List<ApplicationData> getSecondaryFile(ContentHolder holder)throws WTException{

		List<ApplicationData> list = new ArrayList<ApplicationData>();
		
		QueryResult qr = ContentHelper.service.getContentsByRole(holder, ContentRoleType.SECONDARY);
		while (qr.hasMoreElements()) {
			ContentItem item = (ContentItem) qr.nextElement();
			if (item != null) {
				ApplicationData data = (ApplicationData) item;
				list.add(data);
			}
		}
		
		return list;
	}
	
	public static String getFileSize(ContentHolder holder) throws WTException{
		String fileSize = "";
		ApplicationData appData = getPrimaryFile(holder);
		if(appData != null){
			fileSize = getFileSizeStr((int)appData.getFileSize());
		}
		return fileSize;
	}
	
	public static String getFileName(ContentHolder holder) throws WTException{
		String fileName = "";
		ApplicationData appData = getPrimaryFile(holder);
		if(appData != null){
			fileName = appData.getFileName();
		}
		return fileName;
	}
	
	public static String getFileSizeStr(int filesize) {
		DecimalFormat df = new DecimalFormat ( ".#" );
		String fSize = "";
		if ( ( filesize > 1024 ) && ( filesize < 1024 * 1024 )) {
			fSize = df.format ( (float) filesize / 1024 ).toString () + " KB";
		} else if (filesize >= 1024 * 1024) {
			fSize = df.format ( (float) filesize / ( 1024 * 1024 ) ).toString ()	+ " MB";
		} else if (filesize < 1024 && filesize > 1) {
			fSize = "1 KB";
		} else {
			fSize = "0 Bytes";
		}
		return fSize;
	}
	public static final String SEPERATER = ":";
	public static String getUploadPath(String uploadFullPath) {
		String result = null;
		if(uploadFullPath!=null) {
			EncodingConverter converter = new EncodingConverter();
			String url = converter.decode(uploadFullPath);
			String[] strings = url.split(SEPERATER);
			
			if(strings.length>=5) {
				result = strings[3] + SEPERATER + strings[4];
			}
		}
		
		return result;
	}
	
	public static String getFileIconStr(ContentHolder holder) throws WTException {
		URLFactory urlFac = new URLFactory();
		String iconStr = "";
		ApplicationData appData = getPrimaryFile(holder);
		if(appData == null) {
			return "";
		}
		String extStr = "";
		String tempFileName = appData.getFileName ();
		int dot = tempFileName.lastIndexOf ( "." );
		if (dot != -1) extStr = tempFileName.substring ( dot + 1 ); // includes
		String path = urlFac.getBaseURL ().getPath () + "jsp/portal/icon/fileicon/";
																								// "."
		if (extStr.equalsIgnoreCase ( "cc" )) iconStr = path + "ed.gif";
		else if (extStr.equalsIgnoreCase ( "exe" )) iconStr = path + "exe.gif";
		else if (extStr.equalsIgnoreCase ( "doc" )) iconStr = path + "doc.gif";
		else if (extStr.equalsIgnoreCase ( "ppt" )) iconStr = path + "ppt.gif";
		else if (extStr.equalsIgnoreCase ( "xls" )) iconStr = path + "xls.gif";
		else if (extStr.equalsIgnoreCase ( "csv" )) iconStr = path + "xls.gif";
		else if (extStr.equalsIgnoreCase ( "txt" )) iconStr = path + "notepad.gif";
		else if (extStr.equalsIgnoreCase ( "mpp" )) iconStr = path + "mpp.gif";
		else if (extStr.equalsIgnoreCase ( "pdf" )) iconStr = path + "pdf.gif";
		else if (extStr.equalsIgnoreCase ( "tif" )) iconStr = path + "tif.gif";
		else if (extStr.equalsIgnoreCase ( "gif" )) iconStr = path + "gif.gif";
		else if (extStr.equalsIgnoreCase ( "jpg" )) iconStr = path + "jpg.gif";
		else if (extStr.equalsIgnoreCase ( "ed" )) iconStr = path + "ed.gif";
		else if (extStr.equalsIgnoreCase ( "zip" )) iconStr = path + "zip.gif";
		else if (extStr.equalsIgnoreCase ( "tar" )) iconStr = path + "zip.gif";
		else if (extStr.equalsIgnoreCase ( "rar" )) iconStr = path + "zip.gif";
		else if (extStr.equalsIgnoreCase ( "jar" )) iconStr = path + "zip.gif";
		else if (extStr.equalsIgnoreCase ( "igs" )) iconStr = path + "epmall.gif";
		else if (extStr.equalsIgnoreCase ( "pcb" )) iconStr = path + "epmall.gif";
		else if (extStr.equalsIgnoreCase ( "asc" )) iconStr = path + "epmall.gif";
		else if (extStr.equalsIgnoreCase ( "dwg" )) iconStr = path + "epmall.gif";
		else if (extStr.equalsIgnoreCase ( "dxf" )) iconStr = path + "epmall.gif";
		else if (extStr.equalsIgnoreCase ( "sch" )) iconStr = path + "epmall.gif";
		else if (extStr.equalsIgnoreCase ( "html" )) iconStr = path + "htm.gif";
		else if (extStr.equalsIgnoreCase ( "htm" )) iconStr = path + "htm.gif";
		/*20150416 pptx xlsx docx 추가 start*/
		else if (extStr.equalsIgnoreCase ( "pptx" )) iconStr = path + "ppt.gif";
		else if (extStr.equalsIgnoreCase ( "xlsx" )) iconStr = path + "xls.gif";
		else if (extStr.equalsIgnoreCase ( "docx" )) iconStr = path + "doc.gif";
		/*20150416 pptx xlsx docx 추가 end*/
		else iconStr = path + "generic.gif";
		iconStr = "<img src='" + iconStr + "' border=0>";
		return iconStr;
	}
	
	public static void main(String[] args) {
		String oid ="com.e3ps.doc.E3PSDocument:149418";
		try{
			E3PSDocument dis = (E3PSDocument)CommonUtil.getObject(oid);
			List<ApplicationData> list = getSecondaryFile((ContentHolder)dis);
			
			for(ApplicationData data : list){
				LOGGER.info("ApplicationData sApp =" + data );
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static ContentItem getPrimaryContent(ContentHolder holder) throws Exception {

		QueryResult result = ContentHelper.service.getContentsByRole(holder, ContentRoleType.PRIMARY);
		
		if(result.hasMoreElements()) {
			return (ContentItem)result.nextElement();
		}
		return null;
			
	}
	
	public boolean isApplicationPrimaryContent(ContentHolder holder) throws Exception {

		ContentItem contentItem = getPrimaryContent(holder);
		return contentItem == null ? null : ApplicationData.class.toString().equals(contentItem.getClass().toString());
	}
}
