package com.e3ps.project.beans;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import com.e3ps.common.util.JExcelUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.project.EProjectTemplate;
import com.e3ps.project.ETask;
import com.e3ps.project.OutputTypeStep;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.key.ProjectKey.FOLDERKEY;
import com.e3ps.project.service.OutputHelper;
import com.e3ps.project.service.TemplateHelper;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteMethodServer;
import wt.pdmlink.PDMLinkProduct;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionContext;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTProperties;


public class TemplateLoader  implements wt.method.RemoteAccess, java.io.Serializable {
	
	public static void main(String[] args)throws Exception{
		
		String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
        String sFilePath = sWtHome + "\\loadFiles\\e3ps\\template.xls" ;
		
        if(args!=null && args.length>0 && args[0].trim().length()>0){
        	sFilePath = args[0];
        }
        setUser();
		new TemplateLoader().loadTemplate(sFilePath);
		
	}
	
	public static void setUser() {
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");

	}
	
	public void loadTemplate(String sFilePath) throws Exception {

		try {

			WTProperties prover = WTProperties.getServerProperties();
			String server = prover.getProperty("wt.cache.master.codebase");

			if (server == null) {
				server = prover.getProperty("wt.server.codebase");
			}
			
			Class argTypes[] = new Class[]{String.class};
			Object args[] = new Object[]{sFilePath};
			
			Object obj = RemoteMethodServer.getInstance(new URL(server + "/"),
					"BackgroundMethodServer").invoke("_loadTemplate",
					TemplateLoader.class.getName(), this, argTypes, args);
			return;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void _loadTemplate(String sFilePath) throws Exception {
		boolean bool = SessionServerHelper.manager.setAccessEnforced(false);
		SessionContext sessioncontext = SessionContext.newContext();

		try {

			SessionHelper.manager.setAdministrator();

			File newfile = new File(sFilePath);
			createTemplate(newfile.getPath());

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			SessionContext.setContext(sessioncontext);
			SessionServerHelper.manager.setAccessEnforced(bool);
		}

	}

	public synchronized void createTemplate(String path)throws Exception{ 
		
		 System.out.println("createTemplate");
		 
		 Transaction trx = new Transaction();

       try {
    	    trx.start();
    	    
			File file = new File(path);
			Workbook wb = JExcelUtil.getWorkbook(file);
	
			Sheet[] sheets = wb.getSheets();

			int count = 1;
				
			for (int i = 0; i < sheets.length; i++) {
				int rows = sheets[i].getRows();

				if ((!JExcelUtil.checkLine(sheets[i].getRow(1), 0))
						|| (!JExcelUtil.checkLine(sheets[i].getRow(1), 2))) {
					continue;
				}
	            
	            Cell[] cell = sheets[i].getRow(1);
	            String templateName = JExcelUtil.getContent(cell, 0).trim();
                String outputType = JExcelUtil.getContent(cell, 2).trim();
                String enabled = JExcelUtil.getContent(cell, 3).trim();
                String description = JExcelUtil.getContent(cell, 4).trim();
                
                Map<String, Object> templateHash = new HashMap<String, Object>();
                
                String soid = WCUtil.getPDMLinkProduct().getPersistInfo().getObjectIdentifier().toString();
                
                templateHash.put("name", templateName);
                templateHash.put("division", soid);
                //templateHash.put("division", product.getPersistInfo().getObjectIdentifier().toString());
                templateHash.put("outputType", outputType);
                templateHash.put("enabled", Boolean.toString("O".equals(enabled)));
                templateHash.put("description", description);
                
                EProjectTemplate template = TemplateHelper.service.save(templateHash);
                
	            Hashtable parents = new Hashtable();
	            
	            parents.put("0", template);
	            
	            Hashtable seqHash = new Hashtable();
	            HashMap temp = new HashMap();
	            
	            
				for (int j = 3; j < rows; j++) {
					System.out.println("rows	==	" + j);
					if (
	                		JExcelUtil.checkLine(sheets[i].getRow(j),0)||
	                		JExcelUtil.checkLine(sheets[i].getRow(j),1)||
	                		JExcelUtil.checkLine(sheets[i].getRow(j),2)
	                )
	                {
	                	cell = sheets[i].getRow(j);
	                    
	                	int depth = 3;
	              
	                    ETask activity = null;
	                    String activityName = null;
	                    int activityDepth = -1;
	                    
	                    for(int k=0; k< depth; k++){
	                    	
	                    	if (!JExcelUtil.checkLine(sheets[i].getRow(j),k)){
	                    		continue;
	                    	}
	                    	
	                    	activityName = JExcelUtil.getContent(cell, k).trim();
	                    	
	                    	ScheduleNode parent = (ScheduleNode)parents.get(Integer.toString(k));
	                    	String poid = parent.getPersistInfo().getObjectIdentifier().toString();
	                    	
	                    	Integer childCount = (Integer)seqHash.get(poid);
	                    	if(childCount==null){
	                    		childCount = new Integer(1);
	                    	}else{
	                    		childCount = new Integer(childCount.intValue()+1);
	                    	}
	                    	seqHash.put(poid,childCount);
	                    	
	                    	Hashtable activityHash = new Hashtable();
		                    activityHash.put("oid",template.getPersistInfo().getObjectIdentifier().toString());
		                    activityHash.put("selectId",poid);
		                    activityHash.put("cname",activityName);
		                    activityHash.put("sort",childCount.toString());
		                    
		                    //activity = (ETask)TemplateHelper.service.createTask(activityHash,false);
		                    activityDepth = k+1;
		                    parents.put(Integer.toString(activityDepth), activity);
	                    }
	                    
	                    String tdescription = JExcelUtil.getContent(cell, 3).trim();
	                    String manDay = JExcelUtil.getContent(cell, 4).trim();
	                    String preTask = JExcelUtil.getContent(cell, 5).trim();
	                    String output = JExcelUtil.getContent(cell, 6).trim();
	                    String outputLocation = JExcelUtil.getContent(cell, 7).trim();
	                    String step = JExcelUtil.getContent(cell, 8).trim();
	                    String role = JExcelUtil.getContent(cell, 9).trim();
	                    
//	                    System.out.println("tdescription : " + tdescription);
//	                    System.out.println("manDay : " + manDay);
//	                    System.out.println("preTask : " + preTask);
//	                    System.out.println("output : " + output);
//	                    System.out.println("outputLocation : " + outputLocation);
//	                    System.out.println("step : " + step);
//	                    System.out.println("role : " + role);
	                    
	                    OutputTypeStep outputStep = getOutputTypeStep(outputType, step);
	                    
	                    Hashtable activityHash = new Hashtable();
	                    activityHash.put("selectId",activity.getParent().getPersistInfo().getObjectIdentifier().toString());
	                    activityHash.put("oid",activity.getPersistInfo().getObjectIdentifier().toString());
	                    activityHash.put("taskname",activityName);
	                    activityHash.put("description",tdescription);
	                    activityHash.put("manDay",manDay);
	                    activityHash.put("sort",Integer.toString(activity.getSort()));
	                    activityHash.put("poid",template.getPersistInfo().getObjectIdentifier().toString());
	                    
	                    //HashMap<String, Object> map = TemplateHelper.service.updateTask(activityHash,false);
	                    //activity = (ETask)map.get("node");
	                    
	                    temp.put(activity.getName(),activity);
	                    
	                    parents.put(Integer.toString(activityDepth), activity);
	                    
	                    if(role!=null && role.trim().length()>0){
	                    	StringTokenizer st = new StringTokenizer(role,",");
		                    String[] roles = new String[st.countTokens()];
		                    int c = 0;
		                    while(st.hasMoreTokens()){
		                    	String roleName = st.nextToken().trim();
		                    	if(roleName!=null){
		                    		roles[c++] = getRoleCode(roleName);
		                    	}
		                    }
		                    
		                    
		                    
		                    Hashtable roleHash = new Hashtable();
		                    roleHash.put("role", roles);
		                    roleHash.put("oid",activity.getPersistInfo().getObjectIdentifier().toString());
	                    	
		                    TemplateHelper.service.editRole(roleHash);
	                    }
	                    
	                    
	                    
	                    if(preTask!=null && preTask.trim().length()>0){
	                    	
	                    	StringTokenizer tokens = new StringTokenizer(preTask,",");
	                    	
	                    	while(tokens.hasMoreElements()){
		                    	ETask pre = (ETask)temp.get(tokens.nextToken().trim());
		                    	
		                    	if(pre!=null){
			                    	Hashtable preHash = new Hashtable();
			                    	preHash.put("oid", template.getPersistInfo().getObjectIdentifier().toString());
			                    	preHash.put("selectChild",activity.getPersistInfo().getObjectIdentifier().toString());
			                    	String[] preTaskList = new String[]{pre.getPersistInfo().getObjectIdentifier().toString()};
			                    	preHash.put("preTask", preTaskList);
			                    	//TemplateHelper.service.setPreTask(preHash,false);
		                    	}
	                    	}
	                    }
	                    if(output!=null && output.trim().length()>0){
	                    	
	                    	StringTokenizer tokens = new StringTokenizer(output,",");
	                    	
	                    	while(tokens.hasMoreElements()){
	                    		String outputName = tokens.nextToken();
	                    		Hashtable outputHash = new Hashtable();
	                    		outputHash.put("oid",activity.getPersistInfo().getObjectIdentifier().toString());
		                    	outputHash.put("name", outputName);
		                    	outputHash.put("description", outputName);
		                    	if(outputStep != null){
		                    		outputHash.put("outputStep", outputStep.getPersistInfo().getObjectIdentifier().toString());
		                    		outputHash.put("outputType", outputType);
		                    	}
		                    	if(outputLocation==null)outputLocation = "";
		                    	outputHash.put("location", FOLDERKEY.DOCUMENT + outputLocation);
		                    	OutputHelper.service.saveOutput(outputHash);
		                    	
	                    	}
	                    }
	                }
	            }
	            template = (EProjectTemplate)PersistenceHelper.manager.refresh(template);
	    	    ProjectTreeModel model = new ProjectTreeModel(template);
	    	    model.setPlanSchedule();
	        }
        trx.commit();
        trx = null;
	
	   } catch(Exception e) {
	       throw e;
	   } finally {
	       if(trx!=null){
	            trx.rollback();
	    }
	   } 
	}
	
	public void getAllDocument(File root, ArrayList hash) {

		File[] list = root.listFiles();
		for (int i = 0; i < list.length; i++) {
			File folder = list[i];

			if (folder.isFile()) {
				hash.add(folder);
			} else {
				getAllDocument(folder, hash);
			}
		}
	}
	
	public String getRoleCode(String roleName) throws Exception {

		ArrayList roleList = ProjectUtil.getRoleCodeList();

		for (int i = 0; i < roleList.size(); i++) {
			String[] s = (String[]) roleList.get(i);
			if (s[0].equals(roleName)) {
				return s[0] + "§" + s[1];
			}
		}
		return null;
	}

	public PDMLinkProduct getProduct(String productName) throws Exception {

		QuerySpec qs = new QuerySpec();
		int idx = qs.appendClassList(PDMLinkProduct.class, true);
		qs.appendWhere(new SearchCondition(PDMLinkProduct.class,
				PDMLinkProduct.NAME, "=", productName), new int[] { 0 });
		QueryResult rs = (QueryResult) PersistenceHelper.manager.find(qs);

		PDMLinkProduct wtProduct = null;
		if (rs.hasMoreElements()) {

			Object o[] = (Object[]) rs.nextElement();
			wtProduct = (PDMLinkProduct) o[0];
		}

		return wtProduct;
	}

	public OutputTypeStep getOutputTypeStep(String type, String step)
			throws Exception {

		if (type == null)
			return null;

		QuerySpec qs = new QuerySpec(OutputTypeStep.class);
		qs.appendWhere(new SearchCondition(OutputTypeStep.class,
				OutputTypeStep.CODE, "=", step), new int[] { 0 });
		QueryResult qr = PersistenceHelper.manager.find(qs);
		if (qr.hasMoreElements()) {
			return (OutputTypeStep) qr.nextElement();
		}
		return null;
	}
}

