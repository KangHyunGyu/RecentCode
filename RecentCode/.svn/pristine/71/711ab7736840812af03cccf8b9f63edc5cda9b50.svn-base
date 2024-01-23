package com.e3ps.org.service;

import java.util.List;
import java.util.Map;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.org.Department;
import com.e3ps.org.People;
import com.e3ps.org.WTUserPeopleLink;
import com.e3ps.org.bean.CompanyState;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.OrganizationServicesHelper;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

@SuppressWarnings("serial")
public class StandardPeopleService extends StandardManager implements PeopleService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ORG.getName());
	public static StandardPeopleService newStandardPeopleService() throws Exception {
		final StandardPeopleService instance = new StandardPeopleService();
		instance.initialize();
		return instance;
	}
	
	/**
	 * @desc	: 부서장 설정 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 4.
	 * @method	: onChiefAction
	 * @param   : userOid
	 */
	@Override
	public void setChiefAction(Map<String, Object> reqMap) throws WTException {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			People pp = (People) CommonUtil.getObject((String)reqMap.get("oid"));
			boolean checked = (boolean) reqMap.get("checked"); 
			Department dept = pp.getDepartment();
				
			QuerySpec qs = new QuerySpec(People.class);
	
			qs.appendWhere(new SearchCondition(People.class, "departmentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(dept)), new int[0]);
		    
        	qs.appendAnd();
			qs.appendWhere(new SearchCondition(People.class, "chief", SearchCondition.IS_TRUE), new int[0]);

			QueryResult rt = PersistenceHelper.manager.find(qs);
			while(rt.hasMoreElements()){
				People chiefpp = (People) rt.nextElement();
				
				chiefpp.setChief(false);
				
				PersistenceHelper.manager.modify(chiefpp);
			}
				
			PersistenceHelper.manager.refresh(pp);
			
			
			if(checked) {
				pp.setChief(true);
				PersistenceHelper.manager.save(pp);
			}
				
			trx.commit();
			trx = null;
		} catch (WTPropertyVetoException e) {
			throw new WTException(e);
		}finally {
			if(trx!=null){
				trx.rollback();
			}
		}
	}
	
	/**
	 * @desc	: 직급 설정 Action
	 * @author	: shkim
	 * @date	: 2019. 6. 4.
	 * @method	: onChiefAction
	 * @param   : reqMap
	 */
	@Override
	public void setDutyAction(Map<String, Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			String oid = (String) reqMap.get("oid");
			String dutyName = (String) reqMap.get("dutyName");

			QuerySpec spec = new QuerySpec();
			int idx = spec.addClassList(People.class, true);
			
			spec.appendWhere(new SearchCondition(People.class, "thePersistInfo.theObjectIdentifier.id",
					SearchCondition.EQUAL, CommonUtil.getOIDLongValue(oid)), new int[]{idx});
			System.out.println("spec ::: " + spec);
			QueryResult result = PersistenceHelper.manager.find(spec);

			
			Map<String, String> dutyMap = CompanyState.nameTable;
			String dutyCode = dutyMap.get(dutyName);
			
			while(result.hasMoreElements()) {
				Object[] obj = (Object[]) result.nextElement();
				People people = (People) obj[0];
				
				people.setDutyCode(dutyCode);
				people.setDuty(dutyName);
				
				PersistenceHelper.manager.modify(people);
			}

			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if(trx!=null){
				trx.rollback();
			}
		}
	}
	
	/**
	 * @desc	: 부서 설정
	 * @author	: shkim
	 * @date	: 2019. 6. 20.
	 * @method	: setDepartmentAction
	 * @param reqMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setDepartmentAction(Map<String, Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			String gridName = (String) reqMap.get("gridName");
			String departmentOid = (String) reqMap.get("departmentOid");
			List<Map<String, Object>> items = (List<Map<String, Object>>) reqMap.get("items");
			
			if("#du_grid_wrap".equals(gridName)) {
				Department dept = (Department) CommonUtil.getObject(departmentOid);
				for(Map<String, Object> userInfoMap : items) {
					People pp = (People) CommonUtil.getObject((String) userInfoMap.get("oid"));
					pp.setDepartment(dept);
					PersistenceHelper.manager.modify(pp);
				}
			}else if("#ndu_grid_wrap".equals(gridName)) {
				for(Map<String, Object> userInfoMap : items) {
					People pp = (People) CommonUtil.getObject((String) userInfoMap.get("oid"));
					pp.setDepartment(null);
					PersistenceHelper.manager.modify(pp);
				}
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if(trx!=null){
				trx.rollback();
			}
		}
	}
	
	
	/**
	 * @desc	: WTUser People Sync
	 * @author	: shkim
	 * @date	: 2019. 6. 4.
	 * @method	: onChiefAction
	 * @param   : obj, event
	 */
	@Override
    public void eventListener(Object obj, String event){ 
    	
    	if(obj instanceof WTUser){
			if (event.equals("POST_DISABLE")) {
                syncDelete((WTUser) obj);
            }else if (event.equals("POST_ENABLE")) {
                // 처리 상태 모호...
            }else if (event.equals("POST_DELETE")) {
                syncDelete((WTUser) obj);
            }else if (event.equals("POST_MODIFY")) {
                syncModify((WTUser) obj);
            }else if (event.equals("POST_STORE")) {
                syncStore((WTUser) obj);
            }
		}
    }
    
	/**
	 * @desc	: WTUser 생성 시 People Sync
	 * @author	: shkim
	 * @date	: 2019. 6. 4.
	 * @method	: onChiefAction
	 * @param   : user
	 */
    @Override
    public void syncStore(WTUser user) {
        try {
        	String id = user.getName();
        	People people = PeopleHelper.manager.getIDPeople(id);
        	if(people == null){
        		people = People.newPeople();
        	}
            people.setUser(user);
            people.setName(user.getFullName());
            people.setId(user.getName());
            people.setIsDisable(false);
            
            //20140117 유저 싱크시 부서 추가.
           // people.setDepartment(DepartmentHelper.manager.getDepartment("NULL"));
            
            PersistenceHelper.manager.save(people);
        }catch (WTException e) {
            e.printStackTrace();
        }catch (WTPropertyVetoException e) {
            e.printStackTrace();
        }catch (Exception e){
        	e.printStackTrace();
        }
    }

    /**
	 * @desc	: WTUser 수정 시 People Sync
	 * @author	: shkim
	 * @date	: 2019. 6. 4.
	 * @method	: onChiefAction
	 * @param   : user
	 */
    @Override
    public void syncModify(WTUser user) {
        try{
            QueryResult qr = PersistenceHelper.manager.navigate(user, "people", WTUserPeopleLink.class);
            if (qr.hasMoreElements()) {
                People people = (People) qr.nextElement();
                people.setName(user.getFullName());
                people.setId(user.getName());
            	 people.setIsDisable(user.isDisabled());
                PersistenceHelper.manager.modify(people);
            }else {
                syncStore(user);
            }
        }catch (WTException e) {
            e.printStackTrace();
        }catch (WTPropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @desc	: WTUser 삭제 시 People Sync
	 * @author	: shkim
	 * @date	: 2019. 6. 4.
	 * @method	: onChiefAction
	 * @param   : user
	 */
    @Override
    public void syncDelete(WTUser user) {
        try{	
            QueryResult qr = PersistenceHelper.manager.navigate(user, "people", WTUserPeopleLink.class);
            
            if (qr.hasMoreElements()){
                People people = (People) qr.nextElement();
                people.setIsDisable(true);
                PersistenceHelper.manager.save(people);
                //PersistenceHelper.manager.delete(people);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * @desc	: WTUser / People sync
     * @author	: shkim
     * @date	: 2019. 6. 20.
     * @method	: syncWTUser
     */
    @Override
    public void syncWTUser() {
        try{
            QuerySpec query = new QuerySpec(WTUser.class);
            QueryResult result = PersistenceHelper.manager.find(query);
            WTUser wtuser = null;
            while (result.hasMoreElements()) {
                wtuser = (WTUser) result.nextElement();
                if (!wtuser.isDisabled())
                    syncModify(wtuser);
            }
        }catch (WTException e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void deleteWTUser(WTUser user) {
		try {
			OrganizationServicesHelper.manager.delete(user);
		} catch (WTException e) {
			e.printStackTrace();
		}
	}
    
}