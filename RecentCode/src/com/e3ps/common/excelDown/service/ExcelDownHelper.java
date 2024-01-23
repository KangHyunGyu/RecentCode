/**
 * 
 */
package com.e3ps.common.excelDown.service;

import java.util.List;
import java.util.Map;

import com.e3ps.change.beans.ECOData;
import com.e3ps.change.beans.ECRData;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.doc.bean.E3PSDocumentData;
import com.e3ps.doc.service.DocHelper;
import com.e3ps.epm.bean.EpmData;
import com.e3ps.epm.bean.EpmPartStateData;
import com.e3ps.epm.service.EpmHelper;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.org.service.PeopleHelper;
import com.e3ps.part.bean.PartData;
import com.e3ps.part.service.PartHelper;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.PrePostLink;
import com.e3ps.project.beans.ProjectData;
import com.e3ps.project.beans.ProjectOutputData;
import com.e3ps.project.service.ProjectHelper;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.util.WTException;

public class ExcelDownHelper {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	public static final ExcelDownService service = ServiceFactory.getService(ExcelDownService.class);
	
	public static final ExcelDownHelper manager = new ExcelDownHelper();
	
	/**
     * @param parentTask, project
     * @return ETask
     * @throws WTException
     * @메소드명 : getCheckETaskSub
     * @작성자 : tsjeong
     * @작성일 : 2020. 11. 24
     * @설명 : Task 하위가 존재하는지 확인
     */
    public boolean getCheckETaskSub(ETask parentTask, EProject project) throws WTException{
		boolean flag = true;
		QuerySpec spec = new QuerySpec();
		int idx = spec.appendClassList(ETask.class, true);
		spec.appendWhere(
		        new SearchCondition(ETask.class, "parentReference.key.id", "=", CommonUtil.getOIDLongValue(parentTask)),
		        new int[] {idx});
		spec.appendAnd();
		spec.appendWhere(
		        new SearchCondition(ETask.class, "projectReference.key.id", "=", CommonUtil.getOIDLongValue(project)),
		        new int[] {idx});
		QueryResult qr = PersistenceHelper.manager.find(spec);
		ETask task = null;
		if (qr != null && qr.hasMoreElements()) {
		    while (qr.hasMoreElements()) {
			Object[] objArr = (Object[]) qr.nextElement();
			task = (ETask) objArr[0];
		    }
		}
		if (task != null) {
		    flag = false;
		}
		return flag;
   }
	
	/**
     * @desc : 선행 태스크 가져오기
     * @author : tsjeong
     * @date : 2020. 12. 04.
     * @method : getPreTask
     * @param : task
     * @return : ETask
     */
    public ETask getPreTask(ETask task) throws WTException{
	ETask preTask = null;
	QueryResult qr = PersistenceHelper.manager.navigate(task, "pre", PrePostLink.class, false);
	while (qr.hasMoreElements()) {
	    PrePostLink link = (PrePostLink) qr.nextElement();
	    preTask = (ETask) link.getPre();
	}
	return preTask;
    }
    
    public String getPreTaskList(ETask task) throws WTException{
    	ETask preTask = null;
    	String value = "";
    	QueryResult qr = PersistenceHelper.manager.navigate(task, "pre", PrePostLink.class, false);
    	while (qr.hasMoreElements()) {
    	    PrePostLink link = (PrePostLink) qr.nextElement();
    	    preTask = (ETask) link.getPre();
    	    long pOid = CommonUtil.getOIDLongValue(preTask);
			String preTaskName = String.valueOf(pOid);
			if(value.length() > 0) {
				value = value+","+preTaskName;
			}else {
				value = preTaskName; 
			}
    	}
    	return value;
    }
	public List<E3PSDocumentData> getDocList(Map<String, Object> reqMap) throws Exception {
		return  DocHelper.manager.getDocList(reqMap);
	}
	public List<EpmData> getEpmList(Map<String, Object> reqMap) throws Exception {
		return EpmHelper.manager.getEpmList(reqMap);
	}
	public List<EpmPartStateData> getEpmPartStateList(Map<String, Object> reqMap) throws Exception {
		return EpmHelper.manager.getEpmPartStateList(reqMap);
	}
	public List<PartData> getPartList(Map<String, Object> reqMap) throws Exception{
		return PartHelper.manager.getPartList(reqMap);
	}
//	public List<DistributeDocumentData> getDistributeList(Map<String, Object> reqMap) throws Exception{
//		return DistributeHelper.manager.getDistributeDocList(reqMap);
//	}
//	public List<ReceiptData> getReceiptList(ReceiptData rec) throws Exception {
//		return CPCHelper.manager.getReceiptList(null, rec);
//	}
//	public List<SupplierData> getSupplierList(Map<String, Object> reqMap) throws Exception {
//		return DistributeHelper.manager.getSupplierList(reqMap);
//	}
//	public List<DistributeToPartLinkData> getDistributePartList(Map<String, Object> reqMap) throws Exception {
//		return DistributeHelper.manager.getDistributePartList(reqMap);
//	}
//	public List<DistributeDocumentData> getRegularDistribute(Map<String, Object> reqMap) throws Exception {
//		return DistributeHelper.manager.getRegularDistributeList(reqMap);
//	}
	public List<NumberCodeData> getNumberCodeList(String codeType) throws Exception {
		List<NumberCodeData> list = CodeHelper.manager.getNumberCodeList(codeType);
		return list;
	}
	public List<ECRData> getECRList(Map<String, Object> reqMap) throws Exception {
		return ChangeHelper.manager.getECRList2(reqMap);
	}
	public List<ECOData> getECOList(Map<String, Object> reqMap) throws Exception {
		return ChangeHelper.manager.getECOList2(reqMap);
	}
	public List<ProjectData> getProjectList(Map<String, Object> reqMap) throws Exception {
		return ProjectHelper.manager.getProjectList(reqMap);
	}
	public List<PeopleData> getUserList(Map<String, Object> reqMap) throws Exception {
		return PeopleHelper.manager.getUserListAction(reqMap);
	}
	public List<ProjectOutputData> getProjectOutputList(Map<String, Object> reqMap) throws Exception {
		return ProjectHelper.manager.getProjectOutputList(reqMap);
	}
}
