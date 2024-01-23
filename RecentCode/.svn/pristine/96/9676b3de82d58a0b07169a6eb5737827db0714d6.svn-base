package com.e3ps.load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.common.drm.DRMService;
import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.common.util.ObjectUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.part.service.PartHelper;
import com.e3ps.part.util.PartPropList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.clients.folder.FolderTaskLogic;
import wt.fc.PersistenceHelper;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleTemplate;
import wt.method.RemoteMethodServer;
import wt.part.PartType;
import wt.part.QuantityUnit;
import wt.part.Source;
import wt.part.WTPart;
import wt.part.WTPartUsageLink;
import wt.vc.views.ViewHelper;
import wt.vc.wip.CheckoutLink;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

public class WtPartMigration {

    /**
    * @param args
    */
    public static void main(final String[] args)throws Exception{
    	System.out.println("################################");
        System.out.println("### WTPart Migration Start ###");
        System.out.println("################################");
        setUser("wcadmin", "wcadmin");
        
        String check = args[0];
        
        new WtPartMigration().loadCode(check, args[1]);
    }

    public static void setUser(final String id, final String pw){
        RemoteMethodServer.getDefault().setUserName(id);
        RemoteMethodServer.getDefault().setPassword(pw);
    }
    
    public WTPart numberCheck(WTPart part, String number) throws Exception{
    	
    	part = PartHelper.manager.getPart(number);
    	
    	return part;
    }
    
    public void loadCode(String check, String filePath)throws Exception{
    	
    	/* 이미 존재하는 번호 리스트 */
    	List<String> useNumber = new ArrayList<>();
    	
    	/* 부모없는 리스트 */
    	List<String> notParent = new ArrayList<>();
    	
    	/* 로드 불가 */
    	List<String> notLoad = new ArrayList<>();
    	
    	
    	//filePath ="D:\\ptc\\Windchill_12.1\\Windchill\\loadFiles\\e3ps\\testMi.xls";
        File newfile =  new File(filePath);
        
        //newfile = DRMService
        
        Workbook wb = JExcelUtil.getWorkbook(newfile);
        Sheet[] sheets = wb.getSheets();
        		
        int rows = sheets[0].getRows();
            
        System.out.println("FilName : "+ newfile.getName() + " - Excel rows : " +rows);
        Cell[] cell = sheets[0].getRow(1);
        Map<String,String> locationMap = PartPropList.PART_CREATION_DATA.getLocations();
        
        
        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        
        for (int j = 2; j < rows; j++){
        	cell = sheets[0].getRow(j);
        	
        	/* 신규 채번 룰 */
        	String newNumber = StringUtil.checkNull(JExcelUtil.getContent(cell, 26).trim());
        	
        	if(newNumber.isEmpty()|| useNumber.contains(newNumber)) {
        		notLoad.add("Not Number or Num duplication : [" + newNumber + "] , rows : " + (j+1));
        	}else {
        		useNumber.add(newNumber);
        		
        		WTPart part = null;
            	part = numberCheck(part, newNumber);
            	
            	if(part == null) {
            		/* OLD Num (Drawing No) */
                	String oldPartNumber = StringUtil.checkNull(JExcelUtil.getContent(cell, 1).trim());
                	
                	/* OEM NUM (Part No) */
                	String oemNumber = StringUtil.checkNull(JExcelUtil.getContent(cell, 2).trim());
                	
                	/* Description (품목명) */
                	String partName = StringUtil.checkNull(JExcelUtil.getContent(cell, 3).trim());
                	
                	/* Customer */
                	String customer = StringUtil.checkNull(JExcelUtil.getContent(cell, 4).trim());
                	
                	/* Material */
                	String material = StringUtil.checkNull(JExcelUtil.getContent(cell, 5).trim());
                	
                	/* 외경 */
                	String outSize = StringUtil.checkNull(JExcelUtil.getContent(cell, 6).trim());
                	
                	/* 내경 */
                	String inSize = StringUtil.checkNull(JExcelUtil.getContent(cell, 7).trim());
                	
                	/* 두깨 */
                	String thickness = StringUtil.checkNull(JExcelUtil.getContent(cell, 8).trim());
                	
                	/* 홀 Dia */
                	String holeDia = StringUtil.checkNull(JExcelUtil.getContent(cell, 9).trim());
                	
                	/* 홀 갯수 */
                	String holeCount = StringUtil.checkNull(JExcelUtil.getContent(cell, 10).trim());
                	
                	/* Equipment */
                	String equipment = StringUtil.checkNull(JExcelUtil.getContent(cell, 11).trim());
                	
                	/* Resistivity */
                	String resistivity = StringUtil.checkNull(JExcelUtil.getContent(cell, 12).trim());
                	
                	/* Remark */
                	String remark = StringUtil.checkNull(JExcelUtil.getContent(cell, 13).trim());
                	
                	/* Revision */
                	//String revision = StringUtil.checkNull(JExcelUtil.getContent(cell, 14).trim());
                	
                	/* 특이사항 */
                	String desc = StringUtil.checkNull(JExcelUtil.getContent(cell, 15).trim());
                	
                	/* parent 번호 */
                	//String parentNum =  StringUtil.checkNull(JExcelUtil.getContent(cell, 26).trim());
                	
                	
                	/* 채번에 따른 iba 별도 저장 필요 */ 
                	String p_unit = newNumber.substring(0, 1);
                	String p_material = newNumber.substring(1, 2);
                	String p_equipment = newNumber.substring(2 ,3);
                	String p_product = newNumber.substring(3, 4);
                	String p_inch = newNumber.substring(4, 5);
                	
                	/* p_unit 정보에 따른 location 자동 등록. */
                	String location = locationMap.get(p_unit);
                	
                	
                	/* 실제 Save */
                	if("true".equals(check)) {
                		part = WTPart.newWTPart();
                		
                		part.setNumber(newNumber);			//New Part Number
                		part.setName(partName);				//Part Name
                		part.setDefaultUnit(QuantityUnit.getQuantityUnitDefault());
                		part.setPartType(PartType.toPartType("separable"));
                		part.setSource(Source.toSource("make"));
                		
                		ViewHelper.assignToView(part, ViewHelper.service.getView("Design"));
                		
                		WTContainer product = WCUtil.getPDMLinkProduct();
                		WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(product);
                		
                		((WTContained)part).setContainer(product);
                		
                		
                		LifeCycleTemplate tmpLifeCycle = LifeCycleHelper.service.getLifeCycleTemplate("LC_Default", wtContainerRef);
                		part = (WTPart)LifeCycleHelper.setLifeCycle(part, tmpLifeCycle);
                		
                		
                		Folder folder= FolderTaskLogic.getFolder(location, wtContainerRef);
                		FolderHelper.assignLocation((FolderEntry) part, folder);
                		
                		part = (WTPart) PersistenceHelper.manager.save(part);

                		IBAUtil.changeIBAValue(part, "P_UNIT", p_unit, "string");
                		IBAUtil.changeIBAValue(part, "P_MATERIAL", p_material, "string");
                		IBAUtil.changeIBAValue(part, "P_EQUIPMENT", p_equipment, "string");
                		IBAUtil.changeIBAValue(part, "P_PRODUCT", p_product, "string");
                		IBAUtil.changeIBAValue(part, "P_INCH", p_inch, "string");
                		
                		
                		IBAUtil.changeIBAValue(part, "MATERIAL", material, "string");
                		IBAUtil.changeIBAValue(part, "EQUIPMENT", equipment, "string");
                		IBAUtil.changeIBAValue(part, "OEM_PART_NO", oemNumber, "string");
                		IBAUtil.changeIBAValue(part, "OLD_PN", oldPartNumber, "string");
                		
                		IBAUtil.changeIBAValue(part, "RESISTIVITY", resistivity, "string");
                		IBAUtil.changeIBAValue(part, "CUSTOMER", customer, "string");
                		
                		
                		IBAUtil.changeIBAValue(part, "DRAW_NO", newNumber, "string");
                		IBAUtil.changeIBAValue(part, "TITLE", partName, "string");
                		
                		
                		IBAUtil.changeIBAValue(part, "EXTERNAL_DIAMETER", outSize, "string");
                		IBAUtil.changeIBAValue(part, "INTERNAL_DIAMETER", inSize, "string");
                		IBAUtil.changeIBAValue(part, "THICKNESS", thickness, "string");
                		IBAUtil.changeIBAValue(part, "HALL_DIA", holeDia, "string");
                		IBAUtil.changeIBAValue(part, "HALL_COUNT", holeCount, "string");
                		
                		IBAUtil.changeIBAValue(part, "REMARK", remark, "string");
                		IBAUtil.changeIBAValue(part, "DESCRIPTION", desc, "string");
                		
                		
                		/**
                		 * 부모 링크
                		 */
						/*
						 * WTPart parentPart = null; if(!parentNum.isEmpty() && parentNum.length()>0) {
						 * System.out.println("### USER newNumber NUM : " + newNumber); parentPart =
						 * PartHelper.manager.getPart(parentNum); if(parentPart != null) {
						 * System.out.println("### USER PARENT NUM : " + parentPart.getNumber());
						 * if("true".equals(check)) { parentPart = (WTPart)
						 * ObjectUtil.checkout(parentPart); WTPartUsageLink link =
						 * WTPartUsageLink.newWTPartUsageLink(parentPart, part.getMaster()); link =
						 * (WTPartUsageLink) PersistenceHelper.manager.save(link);
						 * System.out.println("### link : " + link); parentPart = (WTPart)
						 * ObjectUtil.checkin(parentPart); } } else { notParent.add(parentNum +
						 * "LOW Num : " + (j+1)); } }
						 */
                	}
            	}else {
            		notLoad.add("Num System use : [" + newNumber + "] , rows : " + (j+1));
            	}
        	}
        	
        	System.out.println("### NEW Number : " + newNumber);
        	
        }// for
        
        System.out.println("");
        System.out.println("##########################################");
        System.out.println("### LOAD SUCCESS COUNT : " + useNumber.size());
        /*for(int kk=0; kk<useNumber.size(); kk++) {
        	System.out.println("### USE Number : " + useNumber.get(kk));
        }*/
        System.out.println("##########################################");
       
        
        
        System.out.println("");
        System.out.println("##########################################");
        System.out.println("### NOT Parent Number Size : " + notParent.size());
        for(int jj=0; jj<notParent.size(); jj++) {
        	System.out.println("### NOT Parent Number : " + notParent.get(jj));
        }
        System.out.println("##########################################");
        
    	
        System.out.println("");
        System.out.println("##########################################");
    	System.out.println("### NOT Load Size : " + notLoad.size());
    	for(int qq=0; qq<notLoad.size(); qq++) {
        	System.out.println("### NOT Load Row Number : " + notLoad.get(qq));
        }
    	System.out.println("##########################################");
    	
    	
    	System.out.println("");
        System.out.println("###########################");
        System.out.println("##### WTPart Migration END #####");
        System.out.println("###########################");
        
        
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        System.out.println("소요시간 : " + secDiffTime);
        
    }
    
    
}
