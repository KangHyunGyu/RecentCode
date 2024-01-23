package com.e3ps.load;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.EProjectTemplate;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.OutputTypeStep;
import com.e3ps.project.PrePostLink;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.beans.TemplateTreeModel;
import com.e3ps.project.key.ProjectKey.FOLDERKEY;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.OutputHelper;
import com.e3ps.project.service.OutputTypeHelper;
import com.e3ps.project.service.TemplateHelper;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import wt.fc.PersistenceHelper;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.pom.Transaction;
import wt.session.SessionHelper;
import wt.util.WTException;

public class TemplateLoader_dnc implements RemoteAccess, Serializable {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.LOAD.getName());
	private static final long serialVersionUID = 1L;
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	
	
	private static Map<String, ETask> taskSavedHash;

	public static void main(String[] args) throws Exception {
		String sWtHome = wt.util.WTProperties.getLocalProperties().getProperty("wt.home", "");
		String sFilePath = sWtHome + "\\loadFiles\\e3ps\\Template_Cowin.xls";
		if (args != null && args.length > 0 && args[0].trim().length() > 0) {
			sFilePath = args[0];
		}
		setUser(args[1], args[2]);
		new TemplateLoader_dnc().load(sFilePath);
	}

	public static void setUser(final String id, final String pw) {
		RemoteMethodServer.getDefault().setUserName(id);
		RemoteMethodServer.getDefault().setPassword(pw);
	}

	public void load(String sFilePath) throws RemoteException, InvocationTargetException {
		if (!SERVER) {
			try {
				Class argTypes[] = new Class[]{String.class};
				Object args[] = new Object[]{sFilePath};
				RemoteMethodServer.getDefault().invoke("load", null, this, argTypes, args);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Transaction trx = new Transaction();
		try {
			trx.start();
			taskSavedHash = new HashMap<>();
			
			File newfile = new File(sFilePath);
			Workbook wb = Workbook.getWorkbook(newfile);
			Sheet sheet = wb.getSheet(0);

			int rows = sheet.getRows();
			
			Cell[] cell = sheet.getRow(1);
			String templateName = getContent(cell, 0).trim();
			String outputType = getContent(cell, 2).trim();
			String enabled = getContent(cell, 3).trim();
			String description = getContent(cell, 5).trim();
			
			Map<String, Object> templateMap = new HashMap<>();
			templateMap.put("name", templateName);
			templateMap.put("outputType", outputType);
			templateMap.put("enabled", Boolean.toString("O".equals(enabled)));
			templateMap.put("description", description);

			EProjectTemplate template = TemplateHelper.service.save(templateMap);
			
			Map<String, Object> parents = new HashMap<>();
			parents.put("0", template);
			
			Map<String, Object> seqMap = new HashMap<>();
			Map<String, Object> temp = new HashMap<>();
			for(int i=3; i<rows; i++) {
				if(checkLine(sheet.getRow(i), 0) || checkLine(sheet.getRow(i), 1) || checkLine(sheet.getRow(i), 2)) {
					cell = sheet.getRow(i);
					ETask task = null;
					String taskName = null;
					int depth = 3;
					
					int parentDepth = 0;
					for(int j=0; j<depth; j++) {
						if(!checkLine(sheet.getRow(i), j)) {
							continue;
						}
						taskName = getContent(cell, j).trim();
						ScheduleNode parent = (ScheduleNode) parents.get(Integer.toString(j));
						String poid = parent.getPersistInfo().getObjectIdentifier().toString();
						
						Integer childCount = (Integer) seqMap.get(poid);
						if(childCount == null) {
							childCount = new Integer(1);
						}else {
							childCount = new Integer(childCount.intValue() + 1);
						}
						seqMap.put(poid, childCount);
						
						//childCount
						Map<String, Object> activityMap = new HashMap<>();
						activityMap.put("oid", parent.getPersistInfo().getObjectIdentifier().toString());
						activityMap.put("name", taskName);
						activityMap.put("sort", childCount.toString());
						
						task = createTask(activityMap);

						parentDepth = j+1;
						parents.put(Integer.toString(parentDepth), task);
					}
					
					String taskDescription = getContent(cell, 3).trim();//상세설명
					String duration = getContent(cell, 4).trim();//기간
//					String taskCode = getContent(cell, 5).trim();//태스크 코드
					String preTask = getContent(cell, 5).trim();//선행 태스크
					String outputNames = getContent(cell, 6).trim();//산출물
					String outputPaths = getContent(cell, 7).trim();//산출물 경로
					String tOutputType = getContent(cell, 8).trim();//산출물타입
					String outputStep = getContent(cell, 10).trim();//산출물 인증타입
					String roles = getContent(cell, 9).trim();//Role
					String taskOid = task.getPersistInfo().getObjectIdentifier().toString();
					Map<String, Object> taskMap = new HashMap<>();
					taskMap.put("oid", taskOid);
					taskMap.put("taskName", taskName);
					taskMap.put("description", taskDescription);
					taskMap.put("manDay", duration);
					
					task = updateTask(taskMap);
					
					// 선행 태스크 Set
					if(preTask != null && preTask.length() > 0) {

						List<PrePostLink> preTaskLinkList = TemplateHelper.manager.getPreTaskLinkList(task);

						for (PrePostLink preTaskLink : preTaskLinkList) {
							PersistenceHelper.manager.delete(preTaskLink);
						}

						ETask prrr = taskSavedHash.get(preTask);

						PrePostLink link = PrePostLink.newPrePostLink(prrr, task);
						PersistenceHelper.manager.save(link);
					}
					
					
//					temp.put(taskCode, task);
//					if(preTask != null && preTask.length() > 0) {
//						Map<String, Object> preMap = new HashMap<>();
//						List<Map<String, Object>> items = new ArrayList<>();
//						for(String pt : preTask.split(",")) {
//							ETask pre = (ETask) temp.get(pt.trim());
//							Map<String, Object> item = new HashMap<>();
//							if(pre != null) {
//								item.put("oid", CommonUtil.getOIDString(pre));
//								items.add(item);
//							}
//						}
//						preMap.put("oid", taskOid);
//						preMap.put("items", items);
//						setPreTask(preMap);
//					}
					
					// 산출물 Set
					if(outputNames != null && outputNames.length() > 0) {
						String[] outputName = outputNames.split("\\|\\|");
						String[] outputPath = outputPaths.split("\\|\\|");
						OutputTypeStep ots = OutputTypeHelper.manager.getOutputTypeStep(tOutputType, outputStep);
						String outputTypeOid = "";
						if(ots != null) {
							outputTypeOid = CommonUtil.getOIDString(ots);
						}
						for(int o=0; o<outputName.length; o++) {
							Map<String, Object> outputMap = new HashMap<>();
							outputMap.put("oid", taskOid);
							outputMap.put("name", outputName[o].trim());
							outputMap.put("outputType", tOutputType);
							outputMap.put("outputStep", outputTypeOid);
							outputMap.put("location", FOLDERKEY.DOCUMENT + outputPath[o].trim());
							OutputHelper.service.saveOutput(outputMap);
						}
					}
					
					// Role Set
					if(roles != null && roles.length() > 0) {
						Map<String, Object> roleMap = new HashMap<>();
						List<Map<String, Object>> items = new ArrayList<>();
						for(String role : roles.split(",")) {
							Map<String, Object> item = null;
							NumberCode nc = null;
//							if("CFT".equals(role)) {
//								for(NumberCodeData data : CodeHelper.manager.getNumberCodeList("PROJECTROLE")) {
//									item = new HashMap<>();
//									item.put("key", data.getCode());
//									item.put("value", data.getName());
//									items.add(item);
//								}
//							}else {
//								item = new HashMap<>();
//								nc = CodeHelper.manager.getNumberCodeByName("PROJECTROLE", role.trim());
//								if(nc != null) {
//									item.put("key", nc.getCode());
//									item.put("value", nc.getName());
//								}
//								items.add(item);
//							}
							item = new HashMap<>();
							nc = CodeHelper.manager.getNumberCodeByName("PROJECTROLE", role.trim());
							if(nc != null) {
								item.put("key", nc.getCode());
								item.put("value", nc.getName());
							}
							items.add(item);
						}
						roleMap.put("oid", taskOid);
						roleMap.put("items", items);
						TemplateHelper.service.editRole(roleMap);
					}
				}
			}
			template = (EProjectTemplate) PersistenceHelper.manager.refresh(template);
			TemplateTreeModel model = new TemplateTreeModel(template);
			model.setSchedule();
			
	        trx.commit();
//			trx.rollback();
	        trx = null;
	        
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
		       if(trx!=null){
		            trx.rollback();
		    }
		   } 
	}

	public boolean checkLine(Cell[] cell, int line) {
		String value = null;
		try {
			value = cell[line].getContents().trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.getMessage();
			return false;
		}
		if (value == null || value.length() == 0)
			return false;
		return true;
	}

	public static String getContent(Cell[] cell, int idx) {
		try {
			String val = cell[idx].getContents();
			if (val == null)
				return "";
			return val.trim();
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return "";
	}
	
	public ETask createTask(Map<String, Object> reqMap) throws Exception {
		
		if (!SERVER) {
			try {
				Class argTypes[] = new Class[]{Map.class};
				Object args[] = new Object[]{reqMap};
				return (ETask) RemoteMethodServer.getDefault().invoke("createTask", null, this, argTypes, args);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ETask newTask = null;

		try {

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String description = StringUtil.checkNull((String) reqMap.get("description"));
			String sort = StringUtil.checkNull((String) reqMap.get("sort"));
			double manDay = ParamUtil.getDouble(reqMap, "manDay");

			ScheduleNode parent = (ScheduleNode) CommonUtil.getObject(oid);

			newTask = ETask.newETask();

			newTask.setDescription(description);

			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(DateUtil.getTimestampFormat("20000101", "yyyyMMdd").getTime());
			Timestamp start = new Timestamp(ca.getTime().getTime());

			BigDecimal bd = new BigDecimal(manDay);
			bd = bd.setScale(0, BigDecimal.ROUND_UP);

			ca.add(Calendar.DATE, bd.intValue() - 1);
			Timestamp end = new Timestamp(ca.getTime().getTime());

			newTask.setPlanStartDate(start);
			newTask.setPlanEndDate(end);
			newTask.setCreator(SessionHelper.manager.getPrincipalReference());
			newTask.setName(name);
			newTask.setParent(parent);
			newTask.setSort(Integer.parseInt(sort));
			newTask.setManDay(manDay);
			newTask.setStatus(STATEKEY.READY);

			if (parent instanceof EProjectNode) {
				newTask.setProject((EProjectNode) parent);
			} else {
				newTask.setProject(((ETaskNode) parent).getProject());
			}

			newTask = (ETask) PersistenceHelper.manager.save(newTask);
			taskSavedHash.put(name, newTask);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
		
		return newTask;
	}
	
	public void setPreTask(Map<String, Object> reqMap) throws Exception {
		
		if (!SERVER) {
			try {
				Class argTypes[] = new Class[]{Map.class};
				Object args[] = new Object[]{reqMap};
				RemoteMethodServer.getDefault().invoke("setPreTask", null, this, argTypes, args);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {

			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");

			ETask task = (ETask) CommonUtil.getObject(oid);

			List<PrePostLink> preTaskLinkList = TemplateHelper.manager.getPreTaskLinkList(task);

			for (PrePostLink preTaskLink : preTaskLinkList) {
				PersistenceHelper.manager.delete(preTaskLink);
			}

			for (Map<String, Object> item : items) {
				String preTaskOid = (String) item.get("oid");

				ETask preTask = (ETask) CommonUtil.getObject(preTaskOid);

				PrePostLink link = PrePostLink.newPrePostLink(preTask, task);
				PersistenceHelper.manager.save(link);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
	}
	
	public ETask updateTask(Map<String, Object> hash) throws Exception {
		
		if (!SERVER) {
			try {
				Class argTypes[] = new Class[]{Map.class};
				Object args[] = new Object[]{hash};
				return (ETask) RemoteMethodServer.getDefault().invoke("updateTask", null, this, argTypes, args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ETask task = null;

		try {
			String oid = ParamUtil.get(hash, "oid");
			String taskName = ParamUtil.get(hash, "taskName");
			String description = StringUtil.checkNull(ParamUtil.get(hash, "description"));
			double manDay = ParamUtil.getDouble(hash, "manDay");

			task = (ETask) CommonUtil.getObject(oid);

			task.setName(taskName);
			task.setManDay(manDay);
			task.setDescription(description);

			task = (ETask) PersistenceHelper.manager.save(task);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return task;
	}
}
