package com.e3ps.org.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e3ps.common.bean.FolderData;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.FolderUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.Department;
import com.e3ps.org.DepartmentPeopleLink;
import com.e3ps.org.People;
import com.e3ps.org.WTUserPeopleLink;
import com.e3ps.org.bean.DepartmentData;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.folder.SubFolder;
import wt.method.MethodContext;
import wt.org.WTUser;
import wt.pom.DBProperties;
import wt.pom.WTConnection;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceFactory;
import wt.util.WTException;

public class DepartmentHelper {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.ORG.getName());
	public static final DepartmentService service = ServiceFactory.getService(DepartmentService.class);
	
	public static final DepartmentHelper manager = new DepartmentHelper();
	
	/**
	 * @desc	: 부서 코드로 부서 찾기
	 * @author	: shkim
	 * @date	: 2019. 6. 18.
	 * @method	: getDepartment
	 * @return	: Department
	 * @param   : code
	 * @throws  : WTException
	 */
	public Department getDepartment(String code) throws WTException {
		try {
			QuerySpec spec = new QuerySpec(Department.class);
			spec.appendWhere(new SearchCondition(Department.class, Department.CODE, SearchCondition.EQUAL, code), new int[0]);
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				return (Department) qr.nextElement();
			}
		}catch(QueryException e) {
			throw new WTException(e);
		}catch(WTException e) {
			throw new WTException(e);
		}
		return null;
	}
	
	public Department getOLDDepartment(String code) throws WTException {
		try {
			QuerySpec spec = new QuerySpec(Department.class);
			spec.appendWhere(new SearchCondition(Department.class, Department.OLD_CODE, SearchCondition.EQUAL, code), new int[0]);
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				return (Department) qr.nextElement();
			}
		}catch(QueryException e) {
			throw new WTException(e);
		}catch(WTException e) {
			throw new WTException(e);
		}
		return null;
	}
	
	/**
	 * @desc	: 부서 트리
	 * @author	: shkim
	 * @date	: 2019. 6. 17.
	 * @method	: getDepartmentTree
	 * @return	: List<DepartmentData>
	 * @param   : dept
	 * @param   : list
	 * @throws  : Exception
	 */
	public List<DepartmentData> getDepartmentTree(Department dept, List<DepartmentData> list, boolean isAdmin) throws Exception {
		
		DepartmentData data = new DepartmentData(dept);
		list.add(data);
		
		List<Department> childDeptList = getDepartmentList(dept, isAdmin);

		for(Department child : childDeptList) {
			getDepartmentTree(child, list, isAdmin);
		}
		
		return list;
	}
	
	/**
	 * @desc : 하위 부서 쿼리 (RDB)
	 * @author : shkim
	 * @date : 2019. 6. 18.
	 * @method : getDepartmentList
	 * @return : List<Department>
	 * @param : root
	 * @throws : Exception
	 */
	public List<Department> getDepartmentList(Department root, boolean isAdmin) throws Exception {
		List<Department> list = new ArrayList<>();

		MethodContext methodcontext = null;
		WTConnection wtconnection = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();

			StringBuffer sb = new StringBuffer();
			sb.append(
					"			SELECT 								   											    ");
			sb.append(
					"				CONCAT(classnameA2A2, ':', idA2A2) AS OID 												");
			sb.append(
					"			FROM Department 																	");
			sb.append(
					"  			WHERE idA3parentReference = ? 												    ");
			/*
			 * if (!isAdmin) { sb.append(
			 * "  		AND isDisable = 0 															    "
			 * ); }
			 */
			sb.append(
					"			ORDER BY SORT	 															        ");

			// sb.append(" SELECT LEVEL, "); // sb.append(" NAME, "); //
			/*
			 * sb.append(" classnameA2A2 ||':' ||idA2A2 oid, "); // sb.append(" CODE, "); //
			 * sb.append(" SORT, "); // sb.append(" ida2a2 id, "); //
			 * sb.append(" idA3parentReference pid, "); //
			 * sb.append(" CONNECT_BY_ISLEAF AS isleaf "); //
			 * sb.append(" FROM Department "); //
			 * sb.append(" START WITH idA3parentReference = ? "); //
			 * sb.append(" CONNECT BY prior idA2A2 = idA3parentReference "); //
			 * sb.append(" ORDER SIBLINGS BY SORT ");
			 */

			st = con.prepareStatement(sb.toString());
			st.setLong(1, CommonUtil.getOIDLongValue(root));

			rs = st.executeQuery();

			while (rs.next()) {
				String oid = rs.getString("OID");
				Department dept = (Department) CommonUtil.getObject(oid);
				list.add(dept);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
				;
			}
			if (st != null) {
				st.close();
			}
			if (DBProperties.FREE_CONNECTION_IMMEDIATE && !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
		return list;
	}
	
	
	/**
	 * @desc	: 하위 부서 쿼리 (RDB)
	 * @author	: shkim
	 * @date	: 2019. 6. 18.
	 * @method	: getDepartmentList
	 * @return	: List<Department>
	 * @param   : root
	 * @throws  : Exception
	 */
	public List<Department> getDepartmentListOracle(Department root, boolean isAdmin) throws Exception {
		List<Department> list = new ArrayList<>();
		
		MethodContext methodcontext = null;
		WTConnection wtconnection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
		
		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();
			
			StringBuffer sb = new StringBuffer();
			sb.append("			SELECT 								   											    ");
			sb.append("				classnameA2A2 || ':' || idA2A2 OID 												");
			sb.append("			FROM Department 																	");
			sb.append("  			WHERE idA3parentReference = ? 												    ");
			if(!isAdmin){
				sb.append("  		AND isDisable = 0 															    ");
			}
			sb.append("			ORDER BY SORT	 															        ");
			
			
			//sb.append("			SELECT LEVEL, 																		");
			//sb.append("				NAME, 																			");
			//sb.append("				classnameA2A2 ||':' ||idA2A2 oid, 												");
			//sb.append("				CODE, 																			");
			//sb.append("				SORT, 																			");
			//sb.append("				ida2a2 id,																		");
			//sb.append("				idA3parentReference pid, 														");
			//sb.append("  			CONNECT_BY_ISLEAF AS isleaf 													");
			//sb.append("			FROM Department 																	");
			//sb.append("  			START WITH idA3parentReference = ? 												");
			//sb.append("  			CONNECT BY prior idA2A2        = idA3parentReference 							");
			//sb.append("			ORDER SIBLINGS BY SORT	 															");

			st = con.prepareStatement(sb.toString());
			st.setLong(1, CommonUtil.getOIDLongValue(root));
			
			rs = st.executeQuery();
			
			while(rs.next()) {
				String oid = rs.getString("OID");
				Department dept = (Department) CommonUtil.getObject(oid);
				list.add(dept);
			}
			
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			if ( rs != null ) {
                rs.close();;
            }
            if ( st != null ) {
                st.close();
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
		return list;
	}
	
	/**
	 * @desc	: 하위 부서 재귀 호출
	 * @author	: shkim
	 * @date	: 2019. 6. 18.
	 * @method	: getAllChildList
	 * @return	: List<Department>
	 * @param   : dept
	 * @param   : returnList
	 * @throws  : Exception
	 */
	public List<Department> getAllChildList(Department dept, List<Department> returnList) throws Exception {
        try {
            QuerySpec spec = getChildQuerySpec(dept);
            QueryResult qr = PersistenceHelper.manager.find(spec);
            while (qr.hasMoreElements()) {
                Department childDept = (Department) qr.nextElement();
                returnList.add(childDept);
                getAllChildList(childDept, returnList);
            }
        }catch (Exception e) {
            throw new Exception(e);
        }
        return returnList;
    }
	
	public List<DepartmentData> getSubDepartmentList(Department dept) throws WTException {
		List<DepartmentData> list = new ArrayList<DepartmentData>();
			
		try {
			QuerySpec qs = new QuerySpec();
			int idx = qs.addClassList(Department.class, true);
				
			SearchCondition sc = new SearchCondition(Department.class, "parentReference.key.id",
					SearchCondition.EQUAL, CommonUtil.getOIDLongValue(dept));
			qs.appendWhere(sc, new int[] { idx });
				
			/*
			 * qs.appendOrderBy(new OrderBy(new ClassAttribute(SubFolder.class,
			 * SubFolder.NAME), false), new int[] { idx });
			 */
				
			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				Object[] obj = (Object[]) qr.nextElement();
				DepartmentData data = new DepartmentData((Department) obj[0]);
				Department children = getDepartment(data.getCode());
					
				List<DepartmentData> subList = getSubDepartmentList(children);
					
				if(subList.size()>0) {
					data.setChildren(getSubDepartmentList(children));
				}
					
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return list;
	}
	
	/**
	 * @desc	: 하위 부서 쿼리
	 * @author	: shkim
	 * @date	: 2019. 6. 18.
	 * @method	: getChildQuerySpec
	 * @return	: QuerySpec
	 * @param   : dept
	 * @throws  : Exception
	 */
	public QuerySpec getChildQuerySpec(Department dept) throws Exception {
		try {
			QuerySpec spec = new QuerySpec(Department.class);
			spec.appendWhere(new SearchCondition(Department.class, "parentReference.key.id", "=", CommonUtil.getOIDLongValue(dept)), new int[0]);
	
			return spec;
		}catch(Exception e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * @desc : 유저로 부서 찾기
	 * @author : sangylee
	 * @date : 2019. 9. 27.
	 * @method : getDepartment
	 * @return : Department
	 * @param user
	 * @throws Exception
	 */
	public Department getDepartment(WTUser user)throws Exception{
    	Department department = null; 
    	QueryResult qr = PersistenceHelper.manager.navigate(user, "people", WTUserPeopleLink.class);
         if (qr.hasMoreElements())
         {
        	 People people = (People) qr.nextElement();
             QueryResult subQr = PersistenceHelper.manager
                     .navigate(people, "department", DepartmentPeopleLink.class);
             if (subQr.hasMoreElements())
             {
                 department = (Department) subQr.nextElement();
             }
         }
         return department;
         
    }
	
	/**
	 * @desc : 부서 검색
	 * @author : sangylee
	 * @date : 2019. 9. 27.
	 * @method : getSearchDepartmentListAction
	 * @return : List<DepartmentData>
	 * @param reqMap
	 * @throws Exception
	 */
	public List<DepartmentData> getSearchDepartmentListAction(Map<String, Object> reqMap) throws Exception {
		
		List<DepartmentData> list = new ArrayList<>();

		String name = StringUtil.checkNull((String) reqMap.get("name"));
		
		QuerySpec qs = new QuerySpec();

		int ii = qs.addClassList(Department.class, true);
		
		SearchCondition sc = null;
		
		sc = new SearchCondition(Department.class, Department.IS_DISABLE, SearchCondition.IS_FALSE);
		qs.appendWhere(sc, new int[] { ii });

		if(name.length() > 0) {
			if(qs.getConditionCount() > 0) {
				qs.appendAnd();
			}
			sc = new SearchCondition(Department.class, Department.NAME, SearchCondition.LIKE, "%" + name + "%", false);
			qs.appendWhere(sc, new int[] { ii });
		}
		
		qs.appendOrderBy(new OrderBy(new ClassAttribute(Department.class, Department.NAME), false), new int[] { ii });
		
		System.out.println("searchDepartment ::: " + qs);
		QueryResult qr = PersistenceHelper.manager.find(qs);
		
		while(qr.hasMoreElements()) {
			Object[] obj = (Object[]) qr.nextElement();
			DepartmentData dd = new DepartmentData((Department) obj[0]);
			list.add(dd);
		}

		return list;
	}
	
	/**
	 * @desc : 상위 부서 재귀 호출
	 * @author : sangylee
	 * @date : 2019. 10. 11.
	 * @method : getAllParentList
	 * @return : List<Department>
	 * @param dept
	 * @param returnList
	 */
	public List<Department> getAllParentList(Department dept, List<Department> returnList){
		
		if(dept != null) {
			Department parent = (Department) dept.getParent();
			
			if(parent != null) {
				returnList.add(parent);
			}
			
			getAllParentList(parent, returnList);
		} else {
			return returnList;
		}
		
		return returnList;
	}
	
	public Department getDepartmentRoot(String name) throws WTException {
		try {
			QuerySpec spec = new QuerySpec(Department.class);
			spec.appendWhere(new SearchCondition(Department.class, Department.NAME, SearchCondition.EQUAL, name), new int[0]);
			spec.appendAnd();
			spec.appendWhere(new SearchCondition(Department.class, Department.IS_DISABLE, false), new int[0]);
			QueryResult qr = PersistenceHelper.manager.find(spec);
			if (qr.hasMoreElements()) {
				return (Department) qr.nextElement();
			}
		}catch(QueryException e) {
			throw new WTException(e);
		}catch(WTException e) {
			throw new WTException(e);
		}
		return null;
	}
	
	public String getDepartmentRootLocation() throws WTException {
		String rootLocation = "worldexint";
		/*
		Department depart=DepartmentHelper.manager.getDepartmentRoot("E3PS");
		if(depart == null){
			rootLocation = "ROOT";
		}else{
			rootLocation = depart.getCode();
		}
		*/
		return rootLocation;
	}
}
