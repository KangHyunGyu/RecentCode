package com.e3ps.epm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;

import com.e3ps.common.jdf.config.ConfigEx;
import com.e3ps.common.jdf.config.ConfigExImpl;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.FTPUtil;
import com.ptc.wvs.server.util.PublishUtils;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.HolderToContent;
import wt.epm.EPMAuthoringAppType;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.EPMDocumentType;
import wt.fc.ObjectIdentifier;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.representation.Representation;

public class EpmUtil {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.EPM.getName());
	
	public static String getThumbnail(EPMDocument epm) {
		
		String thumbnail = CommonUtil.getURLString("/epm/getThumbnail") + "?epmOid="+CommonUtil.getOIDString(epm);
		
		return thumbnail;
	}
	
	public static List<Map<String,String>> getCadDivisionList(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		EPMAuthoringAppType[] appType = EPMAuthoringAppType.getEPMAuthoringAppTypeSet();
		
		List<String> useList = getUseCadDivisionList();
		
		for(EPMAuthoringAppType type : appType) {
			if(!type.isSelectable()) continue;
			Map<String,String> map = new HashMap<String,String>();
			
			if(useList.contains(type.toString())) {
				map.put("key", type.toString());
				map.put("value", type.getDisplay());
				list.add(map);
			}
		}
		
		return list;
	}
	
	public static List<String> getUseCadDivisionList(){
		
		List<String> list = new ArrayList<>();
		
		list.add("CATIAV5");
		list.add("OTHER");
		list.add("ACAD");
		
		return list;
	}

	public static List<Map<String,String>> getCadTypeList(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		EPMDocumentType[] appType = EPMDocumentType.getEPMDocumentTypeSet();
		
		List<String> useList = getUseCadTypeList();
		
		for(EPMDocumentType type : appType) {
			if(!type.isSelectable()) continue;
			Map<String,String> map = new HashMap<String,String>();
			
			if(useList.contains(type.toString())) {
				map.put("key", type.toString());
				map.put("value", type.getDisplay());
				list.add(map);
			}
		}
		
		return list;
	}
	
	public static List<String> getUseCadTypeList(){
		
		List<String> list = new ArrayList<>();
		
		list.add("CADASSEMBLY");
		list.add("CADDRAWING");
		list.add("CADCOMPONENT");
		list.add("OTHER");
		
		return list;
	}

	public static List<Map<String,String>> getCadTypeListForCreate(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		EPMDocumentType[] appType = EPMDocumentType.getEPMDocumentTypeSet();
		
		List<String> useList = getUseCadTypeListForCreate();
		
		for(EPMDocumentType type : appType) {
			if(!type.isSelectable()) continue;
			Map<String,String> map = new HashMap<String,String>();
			
			if(useList.contains(type.toString())) {
				map.put("key", type.toString());
				map.put("value", type.getDisplay());
				list.add(map);
			}
		}
		
		return list;
	}
	
	public static List<String> getUseCadTypeListForCreate(){
		
		List<String> list = new ArrayList<>();
		
		list.add("CADASSEMBLY");
		list.add("CADCOMPONENT");
		
		return list;
	}
	
	public static boolean isCreoDrawing(EPMDocument epm){
		
		String application =epm.getAuthoringApplication().toString();
		String cadType = epm.getDocType().toString();
		boolean isCreoDrawing = application.equals("PROE") && cadType.equals("CADDRAWING");
		
		return isCreoDrawing;
	}
	
	public static boolean isWGM(EPMDocument epm){
		
		boolean isWGM = false;
		
		if(epm != null) {
			isWGM = "EPM".equals(epm.getOwnerApplication().toString()) ? true : false;
		}
		
		return isWGM;
	}

	/**
	 * AuthoringType 설정 CAD 응용 프로그램
	 * @param fileName
	 * @return
	 */
	public static String getAuthoringType(String fileName){
		
		String extension = getExtension(fileName);
		extension = extension.toUpperCase();
		String authoringType ="OTHER";
		//orCad(BRD,DSN),SolidWorks(SLDASM,SLDPRT,SLDPRT)AutoCad,기타 -PDF,일러스트
		
		if(extension.equals("PDF")) {
			authoringType = "OTHER";
		}else if (extension.equals("DSN") || extension.equals("BRD") || extension.equals("ZIP")){
			authoringType = "ORCAD";
		}else if (extension.equals("DWG")){
			authoringType = "ACAD";
		}else if (extension.equals("SLDASM") || extension.equals("SLDPRT") || extension.equals("SLDPRT")){
			authoringType = "SOLIDWORKS";
		}else if(extension.equals("PRT")||extension.equals("ASM") ||extension.equals("DRW")){
			authoringType = "PROE";
		}else if(extension.equals("IDW")||extension.equals("IPT") ||extension.equals("IAM")){
			authoringType = "INVENTOR";
		}else{
			authoringType = "OTHER";
		}
		
		return authoringType;
	}
	
	public static String getExtension(String fileName){
		
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}
	
	
	/**
	 * 
	 * @desc	: CadType 별 Template Number
	 * @author	: tsuam
	 * @date	: 2019. 9. 9.
	 * @method	: getTemplateFile
	 * @return	: String
	 * @param cadType
	 * @return
	 * @throws Exception
	 */
	public static String getTemplateNumber(String cadType) throws Exception{
		
		String key ="EPM.TEMPLATE."+cadType;
		
		ConfigExImpl conf = ConfigEx.getInstance("eSolution");
		
		String number = com.e3ps.common.util.StringUtil.checkNull(conf.getString(key)) ;
		
		return number;
		
		
	}
	
	/**
	 * 
	 * @desc	: cach에서 File명 
	 * @author	: plmadmin
	 * @date	: 2019. 9. 10.
	 * @method	: getPrimaryFileName
	 * @return	: String
	 * @param primary
	 * @return
	 * @throws Exception
	 */
	public static File getPrimaryFileName(String primary) throws Exception{
		
		String cacheId = primary.split("/")[0];
		CachedContentDescriptor cacheDs = new CachedContentDescriptor(cacheId);

		File file = new File(cacheDs.getContentIdentity());
		String orgFileName = file.getAbsolutePath();
		String fileDir = file.getParent();
		
		String fileName = primary.split("/")[1];
		
		LOGGER.info("getPrimaryFileName fileName = " + fileName);
		
		return file;
	}
	
	/**
	 * Number 에서 Part_no; P09883.PRT ==> P09883
	 * @desc	:
	 * @author	: tsuam
	 * @date	: 2020. 1. 10.
	 * @method	: getCADToPartNO
	 * @return	: String
	 * @param number
	 * @return
	 */
	public static String getCADToPartNO(String number ){
		
		number = number.substring(0,number.indexOf("."));//this.number;
		
		return number;
		
	}
	
public static boolean check_2D_AdditionalFiles(EPMDocument epm) throws Exception {
		
		boolean check = false;
		boolean isAutoCad = false;
		boolean isPdf = false;
		
		if ("PROE".equals(epm.getAuthoringApplication().toString()) || "CADDRAWING".equals(epm.getDocType().toString())) {
			Representation representation = PublishUtils.getRepresentation(epm);
			if (representation != null) {
				representation = (Representation) ContentHelper.service.getContents(representation);
				Vector contentList = ContentHelper.getContentList(representation);
				for (int l = 0; l < contentList.size(); l++) {
					ContentItem contentitem = (ContentItem) contentList.elementAt(l);
					
					System.out.println("contentList :: " + contentList);
					System.out.println("contentitem :: " + contentitem);
					
					if (contentitem instanceof ApplicationData) {
						ApplicationData drawAppData = (ApplicationData) contentitem;
						boolean isAdditionalFile = drawAppData.getRole().toString().equals("ADDITIONAL_FILES");
						
						if(!isAutoCad) {
							isAutoCad = isAdditionalFile && (drawAppData.getUploadedFromPath().startsWith("/dwg"));
						}
						
						if(!isPdf) {
							isPdf = isAdditionalFile && (drawAppData.getUploadedFromPath().startsWith("/pdf"));
						}
					}
				}
				
			}
		}
		
		check = isAutoCad && isPdf;
		
		return check;
	}

public static boolean check_AdditionalFiles(EPMDocument epm) throws Exception {
	
	boolean check = false;
	boolean isAutoCad = false;
	boolean isPdf = false;
	
	
	
	if ("ACAD".equals(epm.getAuthoringApplication().toString()) || "CADDRAWING".equals(epm.getDocType().toString())) {
		
		
		Representation representation = PublishUtils.getRepresentation(epm);
		if (representation != null) {
			representation = (Representation) ContentHelper.service.getContents(representation);
			Vector contentList = ContentHelper.getContentList(representation);
			for (int l = 0; l < contentList.size(); l++) {
				ContentItem contentitem = (ContentItem) contentList.elementAt(l);
				if (contentitem instanceof ApplicationData) {
					ApplicationData drawAppData = (ApplicationData) contentitem;
					boolean isAdditionalFile = drawAppData.getRole().toString().equals("PRIMARY");
					
					if(!isAutoCad) {
						isAutoCad = isAdditionalFile && (drawAppData.getUploadedFromPath().startsWith("/dwg"));
					}
					
					if(!isPdf) {
						isPdf = isAdditionalFile && (drawAppData.getUploadedFromPath().startsWith("/pdf"));
					}
				}
			}
			
		}
		
	}
	
	check = isAutoCad && isPdf;
	
	return check;
}
	
}
