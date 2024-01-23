package com.e3ps.load;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.inf.container.ExchangeContainer;
import wt.inf.container.OrgContainer;
import wt.inf.container.WTContainerHelper;
import wt.method.RemoteMethodServer;
import wt.org.DirectoryContextProvider;
import wt.org.OrganizationServicesHelper;
import wt.org.WTGroup;
import wt.org.WTOrganization;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

import com.e3ps.admin.EsolutionMenu;
import com.e3ps.admin.service.AdminHelper;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.JExcelUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.WCUtil;

public class EsolutionMenuLoader {

    /**
    * @param args
    */
    public static void main(final String[] args)throws Exception{
        System.out.println("Initializing...");
        setUser("wcadmin", "wcadmin");
        new EsolutionMenuLoader().loadCode();
    }

    public static void setUser(final String id, final String pw)
    {
        RemoteMethodServer.getDefault().setUserName(id);
        RemoteMethodServer.getDefault().setPassword(pw);
    }

    public void loadCode()throws Exception{

    	String filePath ="D:\\ptc\\Windchill_12.1\\Windchill\\loadFiles\\e3ps\\EsolutionMenu.xls";
        File newfile =  new File(filePath);
        Workbook wb = JExcelUtil.getWorkbook(newfile);
        Sheet[] sheets = wb.getSheets();
        		
            int rows = sheets[0].getRows();
            
            System.out.println("rows = " +rows);
            Cell[] cell = sheets[0].getRow(1);
            
            for (int j = 1; j < rows; j++)
            {
            	
	            	cell = sheets[0].getRow(j);
	                String code = StringUtil.checkNull(JExcelUtil.getContent(cell, 0).trim());
	                String name = StringUtil.checkNull(JExcelUtil.getContent(cell, 1).trim());
	                String name_en = StringUtil.checkNull(JExcelUtil.getContent(cell, 2).trim());
	                String href = StringUtil.checkNull(JExcelUtil.getContent(cell, 3).trim());
	                String imgsrc = StringUtil.checkNull(JExcelUtil.getContent(cell, 4).trim());
	                String sort = StringUtil.checkNull(JExcelUtil.getContent(cell, 5).trim());
	                String level = StringUtil.checkNull(JExcelUtil.getContent(cell, 6).trim());
	                String parentCode = StringUtil.checkNull(JExcelUtil.getContent(cell, 7).trim());
	                String alias = StringUtil.checkNull(JExcelUtil.getContent(cell, 8).trim());
                	
                	EsolutionMenu pCode = null;
                	
                	if(parentCode.length()>0 ){
                		pCode  = partParentCode(parentCode);
                    }
                	
                	System.out.println("코드:"+code +", 메뉴이름:"+ name +", 부모코드:" + parentCode);
                	
                	//자신의 코드
                	EsolutionMenu emCode  = EsolutionMenu.newEsolutionMenu();
                    
                	emCode.setCode(code);
                	emCode.setName(name);
                	emCode.setName_en(name_en);
                	emCode.setHref(href);
                	emCode.setImgsrc(imgsrc);
                	emCode.setSort(Integer.parseInt(sort));
                	emCode.setMenuLevel(Integer.parseInt(level));
                    emCode.setParent(pCode);
                    emCode.setAlias(alias);
                    if(Integer.parseInt(level)==2) {
                    	emCode.setAlias(pCode.getAlias());
                    }else if(Integer.parseInt(level)==3) {
                    	emCode.setAlias(pCode.getParent().getAlias());
                    }
                    WTGroup group = createWTGroup(code, name);
                    emCode.setGroup(group);
                	
                    PersistenceHelper.manager.save(emCode);
                    

             }

        System.out.println("###########################");
        System.out.println("EsolutionMenu Loader END.");
        System.out.println("###########################");

    }

    public EsolutionMenu partParentCode(String pCodeID){
    	try{
    		
    		if( pCodeID.length() == 0) return null;
        	
    		QuerySpec select = new QuerySpec(EsolutionMenu.class);
	        select.appendWhere(new SearchCondition(EsolutionMenu.class, EsolutionMenu.CODE, "=", pCodeID), new int[] { 0 });
	        QueryResult result = PersistenceHelper.manager.find(select);
	        
	        if (result.hasMoreElements())
	        {
	        	EsolutionMenu pCode = (EsolutionMenu)result.nextElement();
	        	return pCode;
	        }
	    }
	    catch (QueryException e)
	    {
	        e.printStackTrace();
	    }
	    catch (WTException e)
	    {
	        e.printStackTrace();
	    }
	    
	    return null;
    }
    
    
    public WTGroup createWTGroup(String groupName, String desc) throws Exception {
		
		
		QuerySpec qs = new QuerySpec();
		 int index = qs.appendClassList( WTGroup.class, true );
		 SearchCondition sc = new SearchCondition( WTGroup.class, WTGroup.NAME, SearchCondition.EQUAL, groupName );
		 qs.appendWhere( sc, new int[] {index} );
		 QueryResult qr = PersistenceHelper.manager.find( qs );
		if(qr.size() > 0) return null;
		
		WTGroup group = null;
		String targetOrgName = ConfigImpl.getInstance().getString("org.context.name");
		
		Enumeration orgs = OrganizationServicesHelper.manager.findLikeOrganizations(WTOrganization.NAME, targetOrgName, ((ExchangeContainer) WTContainerHelper.getExchangeRef().getContainer()).getContextProvider());
		WTOrganization targetOrg = null;
		if (orgs.hasMoreElements()){
			targetOrg = (WTOrganization) orgs.nextElement();
			OrgContainer org = WTContainerHelper.service.getOrgContainer(targetOrg);
			DirectoryContextProvider dcp = WTContainerHelper.service.getPublicContextProvider(org,WTGroup.class);
			group = WTGroup.newWTGroup(groupName, dcp);
			group.setContainer(org);
			if(desc.length()>0)group.setDescription(desc);
			group = (WTGroup)OrganizationServicesHelper.manager.createPrincipal(group);
		}
		
		return group;
		
	}
    

}
