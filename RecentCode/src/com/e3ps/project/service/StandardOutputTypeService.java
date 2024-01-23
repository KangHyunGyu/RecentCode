package com.e3ps.project.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.introspection.ClassInfo;
import wt.introspection.WTIntrospector;
import wt.method.MethodContext;
import wt.pds.DatabaseInfoUtilities;
import wt.pds.StatementSpec;
import wt.pom.DBProperties;
import wt.pom.Transaction;
import wt.pom.WTConnection;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTException;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.web.ParamUtil;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;

public class StandardOutputTypeService extends StandardManager implements OutputTypeService {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	
	public static StandardOutputTypeService newStandardOutputTypeService() throws Exception {
		final StandardOutputTypeService instance = new StandardOutputTypeService();
		instance.initialize();
		return instance;
	}
	
	/**Output 목록 가져오기
     * @return
     * @throws Exception
     */
	@Override
    public List<Map<String,Object>> getOutputType() throws Exception{
    	OutputType[] output = OutputType.getOutputTypeSet();
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	
    	for(int i=0; i < output.length; i++){
    		Map<String,Object> map = new HashMap<String,Object>();
    		
    		map.put("value", output[i].toString());
    		map.put("name", output[i].getDisplay(Locale.KOREA));
    		list.add(map);
    	}
    	return list;
    }
	
	/**output의 step목록 가져오기
     * @param key
     * @return
     */
	@Override
    public QueryResult getCodeList(String key){
    	return getCodeList(key, null);
	}
    
	@Override
    public QueryResult getCodeList(String key, OutputTypeStep parent){
		try{
			QuerySpec query = new QuerySpec(OutputTypeStep.class);
			query.appendWhere(new SearchCondition(OutputTypeStep.class, "outputType", "=", OutputType.toOutputType(key)), new int[] { 0 });
			
			query.appendAnd();
			if(parent == null){
				query.appendWhere(new SearchCondition(OutputTypeStep.class, "parentReference.key.classname", true), new int[] { 0 });
			}else{
				query.appendWhere(new SearchCondition(OutputTypeStep.class, "parentReference.key.id", "=", parent.getPersistInfo().getObjectIdentifier().getId()), new int[] { 0 });
			}
			
			query.appendOrderBy(new OrderBy(new ClassAttribute(OutputTypeStep.class,OutputTypeStep.CODE),false),new int[]{0});
			
			LOGGER.info("### WSLEE : "+query);
			
			return PersistenceHelper.manager.find(query);
		}catch(Exception ex){
			ex.printStackTrace();
			return new QueryResult();
		}
	}
	
	@Override
	public OutputTypeStep getOutputTypeStep(String outputType, String code)
    {
        if (code == null) { return null; }
        try
        {
            QuerySpec select = new QuerySpec(OutputTypeStep.class);
            select.appendWhere(new SearchCondition(OutputTypeStep.class, "outputType", "=", outputType), new int[] { 0 });
            select.appendAnd();
            select.appendWhere(new SearchCondition(OutputTypeStep.class, "code", "=", code), new int[] { 0 });
            QueryResult result = PersistenceHelper.manager.find(select);
            while (result.hasMoreElements()){
                return (OutputTypeStep) result.nextElement();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
	
	@Override
	public List<Map<String, Object>> getPSOTree(String codeType) throws Exception {
		
		String tableName = "";
		ClassInfo classinfo = WTIntrospector.getClassInfo(OutputTypeStep.class);
		
		if (DatabaseInfoUtilities.isAutoNavigate(classinfo)) {
			tableName = DatabaseInfoUtilities.getBaseTableName(classinfo);
		} else {
			tableName = DatabaseInfoUtilities.getValidTableName(classinfo);
		}

		String parentKeyColumnName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "parentReference.key.id");
		String parentKeyColumnClassName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "parentReference.key.classname"); 
		String numberCodeOIDColumnName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "thePersistInfo.theObjectIdentifier.id");
		String numberCodeOIDColumnClassName = DatabaseInfoUtilities.getValidColumnName(
				classinfo, "thePersistInfo.theObjectIdentifier.classname");

		MethodContext methodcontext = null;
		WTConnection wtconnection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
		try {
			methodcontext = MethodContext.getContext();
			wtconnection = (WTConnection) methodcontext.getConnection();
			Connection con = wtconnection.getConnection();

			StringBuffer sb = new StringBuffer().append("select LEVEL,NAME,")
			.append(numberCodeOIDColumnClassName+"||':'||"+numberCodeOIDColumnName)
			.append(",CODE,SORT,")
			.append(parentKeyColumnClassName+"||':'||"+parentKeyColumnName)
			.append(", (select CODE from " +tableName+ " T2 where T2."+numberCodeOIDColumnName+" = T1."+parentKeyColumnName+
					" AND T2."+numberCodeOIDColumnClassName+" = T1."+parentKeyColumnClassName+") pcode")
			.append(" from "+tableName+" T1")
			//.append(" where (T1.codeType = ?) AND (T1.disabled = ?)")
			.append(" start with ")
			.append(parentKeyColumnName)
			.append("=? connect by prior ")
			.append(numberCodeOIDColumnName)
			.append("=")
			.append(parentKeyColumnName)
			.append(" ORDER SIBLINGS BY SORT");
				
			LOGGER.info("### SQL : " + sb.toString());
			
			st = con.prepareStatement(sb.toString());
			//st.setString(1, codeType);
			//st.setLong(2, 0);
			st.setLong(1, 0);

			rs = st.executeQuery();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				list.add(createRowMap(rs));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
            if ( rs != null ) {
                rs.close();
            }
            if ( st != null ) {
                st.close();
            }
			if (DBProperties.FREE_CONNECTION_IMMEDIATE
					&& !wtconnection.isTransactionActive()) {
				MethodContext.getContext().freeConnection();
			}
		}
	}
	
	/**
	 * 결과셋을 맵형태로 변환한다
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> createRowMap(ResultSet rs) throws SQLException {
		Map<String, Object> rowMap = new HashMap<String, Object>();
		rowMap.put("level", rs.getInt(1));
		rowMap.put("name", rs.getString(2));
		rowMap.put("oid", rs.getString(3));
		rowMap.put("code", rs.getString(4));
		rowMap.put("sort", rs.getString(5));
		rowMap.put("poid", rs.getString(6));
		rowMap.put("pcode", rs.getString(7));
		
		return rowMap;
	}
	
	@Override
	public boolean existsCode(String code) throws WTException {
		QuerySpec qs = new QuerySpec(OutputTypeStep.class);
		
		qs.appendWhere(new SearchCondition(OutputTypeStep.class, 
				OutputTypeStep.CODE, SearchCondition.EQUAL, code),
			new int[] { 0 });
		
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
		
		return (qr.size() > 0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void savePSOCodeAction(Map<String, Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
			String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			
			List<Map<String, Object>> editedItemList = (List<Map<String, Object>>) reqMap.get("editedItemList");
			List<Map<String, Object>> removedItemList = (List<Map<String, Object>>) reqMap.get("removedItemList");
			
			OutputTypeStep parent = null;
			if(parentCode.length() > 0) {
				parent = (OutputTypeStep) CommonUtil.getObject(parentOid);
				//parent = CodeHelper.manager.getNumberCode(codeType, parentCode);
			}
			
			//삭제된 리스트
			for(Map<String, Object> removedItem : removedItemList) {
				
				String oid = StringUtil.checkNull((String) removedItem.get("oid"));
				
				OutputTypeStep nCode = (OutputTypeStep) CommonUtil.getObject(oid);
				
				PersistenceHelper.manager.delete(nCode);
			}

			//추가된 리스트
			for(Map<String, Object> editedItem : editedItemList) {
				
				String code = StringUtil.checkNull((String) editedItem.get("code"));
				String name = StringUtil.checkNull((String) editedItem.get("name"));
				String description = StringUtil.checkNull((String) editedItem.get("description"));
				
				int sort = 0;
				
				if(editedItem.get("sort") instanceof Integer) {
					sort  = (int)editedItem.get("sort");
				}
				
				if(editedItem.get("sort") instanceof String) {
					sort = ParamUtil.getInt(editedItem, "sort");
				}
				
				if(code.length() > 0 && name.length() > 0) {
					OutputTypeStep nCode = OutputTypeHelper.manager.getOutputTypeStep(codeType, code);
					
					if(nCode == null) {
						nCode = OutputTypeStep.newOutputTypeStep();
						
						nCode.setCode(code);
						nCode.setName(name);
						nCode.setDescription(description);
						nCode.setOutputType(OutputType.toOutputType(codeType));
						nCode.setSort(sort);
						
						if(parent != null) {
							nCode.setParent(parent);
						}
						
						PersistenceHelper.manager.save(nCode);
					}else {
						nCode.setName(name);
						nCode.setDescription(description);
						nCode.setSort(sort);
						
						if(parent != null) {
							nCode.setParent(parent);
						}
						
						PersistenceHelper.manager.save(nCode);
					}
				}
			}
			
			trx.commit();
			trx = null;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
	}
}
