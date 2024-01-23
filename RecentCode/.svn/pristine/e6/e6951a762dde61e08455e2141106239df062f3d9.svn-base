package com.e3ps.org.service;

import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.Department;
import com.e3ps.org.People;
import com.e3ps.org.bean.PeopleData;

import wt.fc.PersistenceHelper;
import wt.pom.Transaction;
import wt.services.StandardManager;

@SuppressWarnings("serial")
public class StandardDepartmentService extends StandardManager implements
		DepartmentService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ORG.getName());
	public static StandardDepartmentService newStandardDepartmentService()
			throws Exception {
		final StandardDepartmentService instance = new StandardDepartmentService();
		instance.initialize();
		return instance;
	}
	
	/**
	 * @desc	: 부서 등록 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 12.
	 * @method	: createDepartmentAction
	 * @param   : reqMap
	 * @return  : boolean
	 * @throws  : Exception
	 */
	@Override
	public boolean createDepartmentAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String name = StringUtil.checkNull((String) reqMap.get("name")); //부서 명
			String code = StringUtil.checkNull((String) reqMap.get("code")); //부서 코드
			int sort = Integer.parseInt((String)reqMap.get("sort")); //부서 소트번호
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid")); //상위부서 OID
			
			//부서 코드 중복 확인
			Department checkDept = DepartmentHelper.manager.getDepartment(code);
			if(checkDept != null) {
				return false;
			}
			
			Department dept = Department.newDepartment();
			dept.setName(name);
			dept.setCode(code);
			dept.setSort(sort);
			if(parentOid.length() > 0) { 
				Department parent = (Department) CommonUtil.getObject(parentOid);
				dept.setParent(parent); 
			}
			  
			PersistenceHelper.manager.save(dept);
			
			trx.commit();
			trx = null;
			
			return true;
			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			if(trx != null){
				trx.rollback();
			}
		}
	}
	
	/**
	 * @desc	: 부서 수정 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 13.
	 * @method	: modifyDepartmentAction
	 * @param   : reqMap
	 * @return  : boolean
	 * @throws  : Exception
	 */
	@Override
	public boolean modifyDepartmentAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String oid = StringUtil.checkNull((String) reqMap.get("oid"));
			String name = StringUtil.checkNull((String) reqMap.get("name"));
			String code = StringUtil.checkNull((String) reqMap.get("code"));
			int sort = Integer.parseInt((String) reqMap.get("sort"));
			String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
			
			Department dept = (Department) CommonUtil.getObject(oid);
			
			//부서 코드 중복 확인
			Department checkDept = DepartmentHelper.manager.getDepartment(code);
			if(checkDept != null && !code.equals(dept.getCode())) {
				return false;
			}
			
			dept.setName(name);
			dept.setCode(code);
			dept.setSort(sort);
			if(parentCode.length() > 0) {
				Department pDept = DepartmentHelper.manager.getDepartment(parentCode);
				dept.setParent(pDept);
			}
			
			PersistenceHelper.manager.modify(dept);
			
			trx.commit();
			trx = null;
			
			return true;
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			if(trx != null) {
				trx.rollback();
			}
		}
	}
	
	@Override
	public boolean deleteDepartmentAction(Map<String, Object> reqMap) throws Exception {
		Transaction trx = new Transaction();
		try {
			trx.start();
			
			String deptOid = StringUtil.checkNull((String) reqMap.get("oid"));
			
			Department dept = (Department) CommonUtil.getObject(deptOid);
			
			if(dept != null) {
				List<Department> childList = DepartmentHelper.manager.getDepartmentList(dept, true);
				if(childList.size() > 0) {
					return false;
				}
				
				reqMap.put("departmentOid", deptOid);
				List<PeopleData> peopleList = PeopleHelper.manager.getDeptUserListAction(reqMap);
				
				for(PeopleData data : peopleList) {
					String oid = data.getOid();
					People people = (People) CommonUtil.getObject(oid);
					
					people.setDepartment(null);
					
					PersistenceHelper.manager.modify(people);
				}
				
				PersistenceHelper.manager.delete(dept);
			}
			
			trx.commit();
			trx = null;
			
			return true;
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			if(trx != null) {
				trx.rollback();
			}
		}
	}
}
