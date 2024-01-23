package com.e3ps.load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.common.iba.IBAUtil;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.bomLoader.service.BomLoaderHelper;
import com.e3ps.part.bomLoader.service.BomLoaderService;
import com.e3ps.part.bomLoader.service.StandardBomLoaderService;
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
import wt.part.WTPartHelper;
import wt.part.WTPartUsageLink;
import wt.vc.views.ViewHelper;

public class WtPartMigrationCheck {

    /**
    * @param args
    */
    public static void main(final String[] args)throws Exception{
    	System.out.println("################################");
        System.out.println("### WTPart Migration Check Start ###");
        System.out.println("################################");
        setUser("wcadmin", "wcadmin");
        
        new WtPartMigrationCheck().checkCode(args[0]);
    }

    public static void setUser(final String id, final String pw){
        RemoteMethodServer.getDefault().setUserName(id);
        RemoteMethodServer.getDefault().setPassword(pw);
    }
    
    public WTPart numberCheck(WTPart part, String number) throws Exception{
    	
    	part = PartHelper.manager.getPart(number);
    	
    	return part;
    }
    
    public void checkCode(String filePath)throws Exception{
    	
    	List<String> notCheckNum = new ArrayList<String>();
    	List<String> dataNotEquals = new ArrayList<String>();
    	
    	//filePath ="D:\\ptc\\Windchill_12.1\\Windchill\\loadFiles\\e3ps\\testMiCheck.xls";
        File newfile =  new File(filePath);
        Workbook wb = JExcelUtil.getWorkbook(newfile);
        Sheet[] sheets = wb.getSheets();
        		
        int rows = sheets[0].getRows();
            
        System.out.println("FilName : "+ newfile.getName() + " - Excel rows : " +rows);
        Cell[] cell = sheets[0].getRow(1);
        Map<String, String> locationMap = PartPropList.PART_CREATION_DATA.getLocations();
        
        for (int j = 2; j < rows; j++){
        	cell = sheets[0].getRow(j);
        	
        	/* 신규 채번 룰 */
        	String newNumber = StringUtil.checkNull(JExcelUtil.getContent(cell, 27).trim());
        	
        	System.out.println("### NEW Number : " + newNumber);
        	
        	WTPart part = null;
    		part = numberCheck(part, newNumber);
            	
        	if(part == null) {
        		notCheckNum.add("### SYSTEM NOT Number" + newNumber);
        	}else {
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
            	String parentNum =  StringUtil.checkNull(JExcelUtil.getContent(cell, 26).trim());
            	
            	
            	/* 채번에 따른 iba 별도 저장 필요 */ 
            	String p_unit = newNumber.substring(0, 1);
            	String p_material = newNumber.substring(1, 2);
            	String p_equipment = newNumber.substring(2 ,3);
            	String p_product = newNumber.substring(3, 4);
            	String p_inch = newNumber.substring(4, 5);
            	
            	/* p_unit 정보에 따른 location 자동 등록. */
            	//String location = locationMap.get(p_unit);
            	
            	Map pData = IBAUtil.getAttributes(part);
            	
            	String A_P_UNIT = StringUtil.checkNull((String)pData.get("P_UNIT"));
            	String A_P_MATERIAL = StringUtil.checkNull((String)pData.get("P_MATERIAL"));
            	String A_P_EQUIPMENT = StringUtil.checkNull((String)pData.get("P_EQUIPMENT"));
            	String A_P_PRODUCT = StringUtil.checkNull((String)pData.get("P_PRODUCT"));
            	String A_P_INCH = StringUtil.checkNull((String)pData.get("P_INCH"));
            	
            	String A_CUSTOMER = StringUtil.checkNull((String)pData.get("CUSTOMER"));
            	String A_DRAW_NO = StringUtil.checkNull((String)pData.get("DRAW_NO"));
            	String A_EQUIPMENT = StringUtil.checkNull((String)pData.get("EQUIPMENT"));
            	String A_MATERIAL = StringUtil.checkNull((String)pData.get("MATERIAL"));
            	String A_OEM_PART_NO = StringUtil.checkNull((String)pData.get("OEM_PART_NO"));
            	String A_OLD_PN = StringUtil.checkNull((String)pData.get("OLD_PN"));
            	
            	String A_RESISTIVITY = StringUtil.checkNull((String)pData.get("RESISTIVITY"));
            	String A_EXTERNAL_DIAMETER = StringUtil.checkNull((String)pData.get("EXTERNAL_DIAMETER"));
            	String A_INTERNAL_DIAMETER = StringUtil.checkNull((String)pData.get("INTERNAL_DIAMETER"));
            	String A_THICKNESS = StringUtil.checkNull((String)pData.get("THICKNESS"));
            	
            	String A_HALL_DIA = StringUtil.checkNull((String)pData.get("HALL_DIA"));
            	String A_HALL_COUNT = StringUtil.checkNull((String)pData.get("HALL_COUNT"));
            	String A_REMARK = StringUtil.checkNull((String)pData.get("REMARK"));
            	String A_DESCRIPTION = StringUtil.checkNull((String)pData.get("DESCRIPTION"));

            	
            	
            	if(!p_unit.equals(A_P_UNIT)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", P_UNIT : "+ A_P_UNIT + ", p_unit "+ p_unit); 	}
            	if(!p_material.equals(A_P_MATERIAL)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", P_MATERIAL"); 	}
            	if(!p_equipment.equals(A_P_EQUIPMENT)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", P_EQUIPMENT"); 	}
            	if(!p_product.equals(A_P_PRODUCT)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", P_PRODUCT"); 	}
            	if(!p_inch.equals(A_P_INCH)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", P_INCH"); 	}
            	if(!customer.equals(A_CUSTOMER)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", CUSTOMER"); 	}
            	if(!newNumber.equals(A_DRAW_NO)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", DRAWNO"); 	}
            	if(!equipment.equals(A_EQUIPMENT)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", EQUIPMENT"); 	}
            	if(!material.equals(A_MATERIAL)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", MATERIAL"); 	}
            	if(!oemNumber.equals(A_OEM_PART_NO)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", OEM_PART_NO"); 	}
            	if(!oldPartNumber.equals(A_OLD_PN)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", OLD_PN"); 	}
            	if(!resistivity.equals(A_RESISTIVITY)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", RESISTIVITY"); 	}
            	if(!outSize.equals(A_EXTERNAL_DIAMETER)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", EXTERNAL_DIAMETER"); 	}
            	if(!inSize.equals(A_INTERNAL_DIAMETER)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", INTERNAL_DIAMETER"); 	}
            	if(!thickness.equals(A_THICKNESS)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", THICKNESS"); 	}
            	if(!holeDia.equals(A_HALL_DIA)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", HALL_DIA"); 	}
            	if(!holeCount.equals(A_HALL_COUNT)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", HALL_COUNT"); 	}
            	if(!remark.equals(A_REMARK)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", REMARK"); 	}
            	if(!desc.equals(A_DESCRIPTION)){ dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", DESCRIPTION"); 	}
            	if(!part.getName().equals(partName)) { dataNotEquals.add("LineNumber :  "+ (j+1) +", Num : " +newNumber + ", PART NAME"); 	}
            	
            	WTPart parentPart = PartHelper.manager.getPart(parentNum);
            	if(parentPart != null) {
            		WTPartUsageLink link = StandardBomLoaderService.getUsageLink(part, parentPart);
            		if(link == null) {
            			dataNotEquals.add("LineNumber :  "+ (j+1)+", Num : " +newNumber + ", Not Usage Link : parentNum : " + parentNum );
            		}
            	}
        	}
    		
        	
        }// for
        
        System.out.println("");
        System.out.println("##########################################");
        System.out.println("### dataNotEquals Size : " + dataNotEquals.size());
        for(int kk=0; kk<dataNotEquals.size(); kk++) {
        	System.out.println("### Data Not eq : " + dataNotEquals.get(kk));
        }
        System.out.println("##########################################");
       
        System.out.println("");
        System.out.println("##########################################");
        System.out.println("### notCheckNum Size : " + notCheckNum.size());
        for(int qq=0; qq<notCheckNum.size(); qq++) {
        	System.out.println("### Not System Number : " + notCheckNum.get(qq));
        }
        System.out.println("##########################################");
       
    	
    	System.out.println("");
        System.out.println("###########################");
        System.out.println("##### WTPart Migration Check END #####");
        System.out.println("###########################");
    }
    
    
}
