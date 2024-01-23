package com.e3ps.common.code.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.e3ps.common.code.NCodeNCodeLink;
import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;
import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.TypeUtil;
import com.e3ps.common.web.ParamUtil;

import wt.fc.BinaryLink;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.pds.StatementSpec;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.StringSearch;
import wt.services.StandardManager;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

@SuppressWarnings("serial")
public class StandardCodeService extends StandardManager implements CodeService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	public static StandardCodeService newStandardCodeService() throws Exception {
		final StandardCodeService instance = new StandardCodeService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public String getValue(String codeType,String code)throws Exception{
    	NumberCode nc = getNumberCode(codeType,code);
    	if(nc==null){
    		return null;
    	}
    	return nc.getName();
    }
	
	 /**
     * 
     * @param codeType
     * @param code
     * @return
     */
	@Override
    public NumberCode getNumberCode(String codeType, String code) {
    	
        if (code == null) { 
        	return null; 
        }
        
        try {
            QuerySpec select = new QuerySpec(NumberCode.class);
            select.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
            select.appendAnd();
            select.appendWhere(new SearchCondition(NumberCode.class, "code", "=", code), new int[] { 0 });
            QueryResult result = PersistenceHelper.manager.find(select);
            while (result.hasMoreElements()) {
                return (NumberCode) result.nextElement();
            }
        } catch (QueryException e) {
            e.printStackTrace();
        } catch (WTException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveNumberCodeAction(Map<String, Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
			String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			
			List<Map<String, Object>> addedItemList = (List<Map<String, Object>>) reqMap.get("addedItemList");
			List<Map<String, Object>> editedItemList = (List<Map<String, Object>>) reqMap.get("editedItemList");
			List<Map<String, Object>> removedItemList = (List<Map<String, Object>>) reqMap.get("removedItemList");
			
			NumberCode parent = null;
			if(parentCode.length() > 0) {
				parent = (NumberCode) CommonUtil.getObject(parentOid);
				//parent = CodeHelper.manager.getNumberCode(codeType, parentCode);
			}
			
			//추가된 리스트
			for(Map<String, Object> addedItem : addedItemList) {
				
				String code = StringUtil.checkNull((String) addedItem.get("code"));
				String name = StringUtil.checkNull((String) addedItem.get("name"));
				String engName = StringUtil.checkNull((String) addedItem.get("engName"));
				String description = StringUtil.checkNull((String) addedItem.get("description"));

				int sort = 0;
				
				if(addedItem.get("sort") instanceof Integer) {
					sort  = (int)addedItem.get("sort");
				}
				
				if(addedItem.get("sort") instanceof String) {
					sort = ParamUtil.getInt(addedItem, "sort");
				}
				
				boolean disabled = !((boolean) addedItem.get("active"));
				
				if(code.length() > 0 && name.length() > 0) {
					NumberCode nCode = CodeHelper.manager.getNumberCode(codeType, code);
					
					if(nCode == null) {
						nCode = NumberCode.newNumberCode();
						
						nCode.setCode(code);
						nCode.setName(name);
						nCode.setEngName(engName);
						nCode.setDescription(description);
						nCode.setDisabled(disabled);
						nCode.setCodeType(NumberCodeType.toNumberCodeType(codeType));
						nCode.setSort(sort);
						
						if(parent != null) {
							nCode.setParent(parent);
						}
						
						PersistenceHelper.manager.save(nCode);
					}
				}
			}
			
			//수정된 리스트
			for(Map<String, Object> editedItem : editedItemList) {
				
				String code = StringUtil.checkNull((String) editedItem.get("code"));
				String name = StringUtil.checkNull((String) editedItem.get("name"));
				String engName = StringUtil.checkNull((String) editedItem.get("engName"));
				String description = StringUtil.checkNull((String) editedItem.get("description"));
			
				int sort = 0;
				
				if(editedItem.get("sort") instanceof Integer) {
					sort  = (int)editedItem.get("sort");
				}
				
				if(editedItem.get("sort") instanceof String) {
					sort = ParamUtil.getInt(editedItem, "sort");
				}
				
				boolean disabled = !((boolean) editedItem.get("active"));
				
				if(code.length() > 0 && name.length() > 0) {
					NumberCode nCode = CodeHelper.manager.getNumberCode(codeType, code);
					
					nCode.setName(name);
					nCode.setEngName(engName);
					nCode.setDescription(description);
					nCode.setDisabled(disabled);
					nCode.setSort(sort);
					
					if(parent != null) {
						nCode.setParent(parent);
					}
					
					PersistenceHelper.manager.save(nCode);
				}
			}
			
			//삭제된 리스트
			for(Map<String, Object> removedItem : removedItemList) {
				
				String oid = StringUtil.checkNull((String) removedItem.get("oid"));

				NumberCode nCode = (NumberCode) CommonUtil.getObject(oid);
				
				PersistenceHelper.manager.delete(nCode);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveNumberCodeAction2(Map<String, Object> reqMap) throws Exception {
		
		Transaction trx = new Transaction();
		
		try {
			trx.start();
			
			String codeType = StringUtil.checkNull((String) reqMap.get("codeType"));
			String parentCode = StringUtil.checkNull((String) reqMap.get("parentCode"));
			String parentOid = StringUtil.checkNull((String) reqMap.get("parentOid"));
			
			List<Map<String, Object>> editedItemList = (List<Map<String, Object>>) reqMap.get("editedItemList");
			List<Map<String, Object>> removedItemList = (List<Map<String, Object>>) reqMap.get("removedItemList");
			
			NumberCode parent = null;
			if(parentCode.length() > 0) {
				parent = (NumberCode) CommonUtil.getObject(parentOid);
				//parent = CodeHelper.manager.getNumberCode(codeType, parentCode);
			}
			
			//삭제된 리스트
			for(Map<String, Object> removedItem : removedItemList) {
				
				String oid = StringUtil.checkNull((String) removedItem.get("oid"));
				
				NumberCode nCode = (NumberCode) CommonUtil.getObject(oid);
				
				PersistenceHelper.manager.delete(nCode);
			}
			
			//추가된 리스트
			for(Map<String, Object> editedItem : editedItemList) {
				
				String code = StringUtil.checkNull((String) editedItem.get("code"));
				String name = StringUtil.checkNull((String) editedItem.get("name"));
				String engName = StringUtil.checkNull((String) editedItem.get("engName"));
				String description = StringUtil.checkNull((String) editedItem.get("description"));
				
				int sort = 0;
				
				if(editedItem.get("sort") instanceof Integer) {
					sort  = (int)editedItem.get("sort");
				}
				
				if(editedItem.get("sort") instanceof String) {
					sort = ParamUtil.getInt(editedItem, "sort");
				}
				
				/* boolean disabled = !((boolean) editedItem.get("active")); */
				String disabledFlag = String.valueOf(editedItem.get("active"));
				boolean disabled = true;
				//LOGGER.info(disabledFlag);
				if(disabledFlag.equals("0")) {
					disabled = true;
				}else if(disabledFlag.equals("1")) {
					disabled = false;
				}else {
					disabled = !((boolean) editedItem.get("active"));
				}
				
				
				if(code.length() > 0 && name.length() > 0) {
					NumberCode nCode = CodeHelper.manager.getNumberCode(codeType, code);
					
					if(nCode == null) {
						nCode = NumberCode.newNumberCode();
						
						nCode.setCode(code);
						nCode.setName(name);
						nCode.setEngName(engName);
						nCode.setDescription(description);
						nCode.setDisabled(disabled);
						nCode.setCodeType(NumberCodeType.toNumberCodeType(codeType));
						nCode.setSort(sort);
						
						if(parent != null) {
							nCode.setParent(parent);
						}
						
						PersistenceHelper.manager.save(nCode);
					}else {
						nCode.setName(name);
						nCode.setEngName(engName);
						nCode.setDescription(description);
						nCode.setDisabled(disabled);
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

	@Override
	public QueryResult getCode(String key){
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);
			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", key), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.SORT),false),new int[]{0});
			return PersistenceHelper.manager.find(query);
		}catch(Exception ex){
			ex.printStackTrace();
			return new QueryResult();
		}
	}
	
	@Override
	public Vector<NumberCode> getCodeVec(String key){
		Vector<NumberCode> vec = new Vector();
		try{
			QueryResult rt =getCode(key);
			while(rt.hasMoreElements()){
				vec.add((NumberCode)rt.nextElement());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return vec;
	}

	@Override
	public String getName(String key,String code){
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);

			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", key), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "code", "=", code), new int[] { 0 });
			
			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find(query);

			if(qr.hasMoreElements()){
				NumberCode cc = (NumberCode)qr.nextElement();
				return cc.getName();
			}
			return "";
		}
		catch(Exception ex){
			return "";
		}
	}
	
	@Override
	public String getDescription(String key,String code){
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);

			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", key), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "code", "=", code), new int[] { 0 });
			
			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find(query);

			if(qr.hasMoreElements()){
				NumberCode cc = (NumberCode)qr.nextElement();
				
				
				return cc.getDescription();
			}
			return "";
		}
		catch(Exception ex){
			return "";
		}
	}
	
	@Override
	public String getCode(String key,String name){
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);

			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", key), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "name", "=", name), new int[] { 0 });
			//query.appendAnd();
			//query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });

			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find(query);

			if(qr.hasMoreElements()){
				NumberCode cc = (NumberCode)qr.nextElement();
				return cc.getCode();
			}
			return "";
		}
		catch(Exception ex){
			return "";
		}
	}
	
	
	@Override
	public QueryResult getChildCode(String key,String parentoid){
		
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);
			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", key), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class,"parentReference.key.id",SearchCondition.EQUAL,CommonUtil.getOIDLongValue(parentoid)),new int[] {0});
			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			return PersistenceHelper.manager.find(query);
		}catch(Exception ex){
			ex.printStackTrace();
			return new QueryResult();
		}
	
	}
	
	@Override
	public QueryResult getTopCode(String key){
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);
			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", key), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class,"parentReference.key.id",SearchCondition.EQUAL,(long)0),new int[] { 0 });
			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			
			return PersistenceHelper.manager.find(query);
		}catch(Exception ex){
			ex.printStackTrace();
			return new QueryResult();
		}
	}
	
	@Override
	public QueryResult getCodeNum(String type){
	
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);
			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", type), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.SORT),false),new int[]{0});
			return PersistenceHelper.manager.find(query);
		}catch(Exception ex){
			ex.printStackTrace();
			return new QueryResult();
		}
	}
	
	@Override
	public QueryResult getCodeNum(String type, boolean disabled){
	
		try{
			QuerySpec query = new QuerySpec(NumberCode.class);
			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", type), new int[] { 0 });
			
			if( !disabled){
				query.appendAnd();
				query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
			}
			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.SORT),false),new int[]{0});
			return PersistenceHelper.manager.find(query);
		}catch(Exception ex){
			ex.printStackTrace();
			return new QueryResult();
		}
	}
	
	
	@Override
	public NumberCode getTopParent(NumberCode code){
		
		if(code.getParent() != null){
			
			code = getTopParent(code.getParent());
		}
		
		return code;
	}
	
	@Override
	public int getCodelevel(NumberCode code ,Integer level){
		
		if(code.getParent() != null){
			level = getCodelevel(code.getParent(),++level);
		}
		
		return level;
	}
	
	@Override
	public QueryResult getChildCode(String codeArry){
		QueryResult reResult = null;
		String[] tempCode = codeArry.split(",");
		//LOGGER.info("tempCode size :" + tempCode.length);
		String oid 	= tempCode[0];
		String codekey = tempCode[1];
		String codeType ="SBUSINESS";
		
		
		QuerySpec query;
		try {
			query = new QuerySpec(NumberCode.class);
		
			query.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class,"parentReference.key.id",SearchCondition.EQUAL,CommonUtil.getOIDLongValue(oid)),new int[] { 0 });
			
			
			query.appendAnd();
			query.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", "CADATTRIBUTE"), new int[] { 0 });
			query.appendOrderBy(new OrderBy(new ClassAttribute(NumberCode.class,NumberCode.CODE),false),new int[]{0});
			
			reResult = PersistenceHelper.manager.find(query);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reResult;
	}
	
	@Override
	public Vector<NumberCode> getChildCodeVec(String codeType) {
		Vector<NumberCode> vec = new Vector<NumberCode>();
		NumberCode num = null;
		String pCodeOid = "";
		QueryResult pCode = getCode(codeType);
		
		while(pCode.hasMoreElements()) {
			num = (NumberCode)pCode.nextElement();
			pCodeOid = CommonUtil.getOIDString(num);
			NumberCode cNum = null;
			QueryResult cCode = getChildCode(codeType, pCodeOid);
//			if(codeType.equals(AttributeKey.NumberCodeKey.NC_PARTDIVISION ) && num.getCode().equals("T")){
//				vec.add(num);
//			}
			while(cCode.hasMoreElements()) {
				cNum = (NumberCode)cCode.nextElement();
				vec.add(cNum);
			}
			
		}
		return vec;
	}
	
	@Override
	public Vector<NumberCode> getUsed(String codeType) throws Exception {
		Vector<NumberCode> vec = new Vector<NumberCode>();
		QuerySpec query = new QuerySpec();
		int idx = query.appendClassList(NumberCode.class, true);
		SearchCondition sc = new SearchCondition(NumberCode.class, NumberCode.CODE_TYPE, "=", codeType);
		query.appendWhere(sc, new int[]{idx});
		ClassAttribute ca = new ClassAttribute(NumberCode.class, NumberCode.SORT);
		OrderBy order = new OrderBy(ca, false);
		query.appendOrderBy(order, new int[]{idx});
		QueryResult result = PersistenceHelper.manager.find(query);
		while(result.hasMoreElements()) {
			Object[] obj = (Object[])result.nextElement();
			NumberCode code = (NumberCode)obj[0];
			vec.add(code);
		}
		return vec;
	}

	@Override
    public NumberCode getNumberCodeName(String codeType, String name)
    {
        if (name == null) { return null; }
        try
        {
            QuerySpec select = new QuerySpec(NumberCode.class);
            select.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
            select.appendAnd();
            //select.appendWhere(new SearchCondition(NumberCode.class, "name", "=", name), new int[] { 0 });
     	    StringSearch stringsearch = new StringSearch("name");
     	    stringsearch.setValue(name.trim());
    		select.appendWhere(stringsearch.getSearchCondition(NumberCode.class),new int[]{0});
    	   
            QueryResult result = PersistenceHelper.manager.find(select);
           
            while (result.hasMoreElements())
            {
                return (NumberCode) result.nextElement();
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
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }    
	
	@Override
    public Vector getNumberCodeForQuery(String codeType)
    {
        try
        {
            QuerySpec select = new QuerySpec(NumberCode.class);
            select.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
            QueryResult result = PersistenceHelper.manager.find(select);
            Vector vec = new Vector();
            int i = 0;
            while(result.hasMoreElements())
            {
                NumberCode tempCode = (NumberCode)result.nextElement();
                vec.add(i, tempCode);
                i++;
            }
            return vec;
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

	@Override
    public HashMap getNumberCode(String codeType)
    {
        HashMap map = new HashMap();
        try
        {
            QuerySpec select = new QuerySpec(NumberCode.class);
            select.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
            QueryResult result = PersistenceHelper.manager.find(select);

            NumberCode code = null;
            while (result.hasMoreElements())
            {
                code = (NumberCode) result.nextElement();
                map.put(code.getCode(), code.getName());
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
        return map;
    }
    
	@Override
    public QueryResult getQueryResult(String codeType, String sortType)
    {
        if(sortType == null) sortType = "name";
        try
        {
            QuerySpec spec = new QuerySpec(NumberCode.class);
            spec.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
            
            spec.appendAnd();
            spec.appendOpenParen();
        	spec.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
        	spec.appendOr();
        	spec.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_NULL), new int[] { 0 });
        	spec.appendCloseParen();
        	
			spec.appendOrderBy(new OrderBy(new ClassAttribute( NumberCode.class,sortType),false),new int[]{0});
            return PersistenceHelper.manager.find(spec);
        }
        catch (QueryException e)
        {
            e.printStackTrace();
        }
        catch (WTException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
	@Override
    public QueryResult getQueryResult(String codeType, String sortType, boolean isValidate)
    {
    	return getQueryResult(codeType,sortType,isValidate,false);
    }
    
	@Override
    public QueryResult getQueryResult(String codeType, String sortType, boolean isValidate, boolean desc)
    {
        if(sortType == null) 
        	sortType = "name";
        
        try
        {
            QuerySpec spec = new QuerySpec(NumberCode.class);
            spec.appendWhere(new SearchCondition(NumberCode.class, "codeType", "=", codeType), new int[] { 0 });
            
            if(isValidate) {
            	if(spec.getConditionCount() > 0)
            		spec.appendAnd();
            	
            	spec.appendOpenParen();
            	spec.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_FALSE), new int[] { 0 });
            	spec.appendOr();
            	spec.appendWhere(new SearchCondition(NumberCode.class, "disabled", SearchCondition.IS_NULL), new int[] { 0 });
            	spec.appendCloseParen();
            }
            
            //order by
			spec.appendOrderBy(new OrderBy(new ClassAttribute( NumberCode.class,sortType),desc),new int[]{0});
          

            
            return PersistenceHelper.manager.find(spec);
        }
        catch (QueryException e)
        {
            e.printStackTrace();
        }
        catch (WTException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
	@Override
    public ArrayList getTopNumberCode(NumberCodeType type) throws WTException {
		ArrayList list = new ArrayList();
		try {
			if(type == null)
				return list;
			
			HashMap map = new HashMap();
			map.put("type", type.toString());
			map.put("isParent", "false");
			
			QueryResult qr = PersistenceHelper.manager.find(getCodeQuerySpec(map));
			
			Object obj[] = null;
			while(qr.hasMoreElements()) {
				obj = (Object[])qr.nextElement();
				list.add((NumberCode)obj[0]);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;		
	}
	
	@Override
	public ArrayList getChildNumberCode(NumberCode numberCode) throws WTException {
		ArrayList list = new ArrayList();
		try {
			if(numberCode == null)
				return list;
			
			HashMap map = new HashMap();
			map.put("parent", numberCode);
			
			QueryResult qr = PersistenceHelper.manager.find(getCodeQuerySpec(map));
			Object obj[] = null;
			while(qr.hasMoreElements()) {
				obj = (Object[])qr.nextElement();
				list.add((NumberCode)obj[0]);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public ArrayList getNumberCode(HashMap map) throws WTException {
		ArrayList list = new ArrayList();
		try {
			QueryResult qr = PersistenceHelper.manager.find(getCodeQuerySpec(map));
			
			Object obj[] = null;
			while(qr.hasMoreElements()) {
				obj = (Object[])qr.nextElement();
				list.add((NumberCode)obj[0]);				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public PagingQueryResult openPagingSession(HashMap map, int start, int size) throws WTException {
		PagingQueryResult result = null;
		try {
			result = openPagingSession(getCodeQuerySpec(map), start, size);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public PagingQueryResult openPagingSession(QuerySpec spec, int start, int size) throws WTException {
		PagingQueryResult result = null;
		try {
			result = PagingSessionHelper.openPagingSession(start, size, spec);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public PagingQueryResult fetchPagingSession(int start, int size, long sessionId) throws WTException {
		PagingQueryResult result = null;
		try {
			result = PagingSessionHelper.fetchPagingSession(start, size, sessionId);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public ArrayList ancestorNumberCode(NumberCode child) throws WTException {
		ArrayList list = new ArrayList();
		try {
			searchAncestorNumberCode(child, list);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public void searchAncestorNumberCode(NumberCode child, ArrayList list) throws WTException {
		try {
			if(list == null) {
				list = new ArrayList();
			}
			
			if(child.getParent() != null) {
				list.add(0, child.getParent());
				searchAncestorNumberCode(child.getParent(), list);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ArrayList descendantsNumberCode(NumberCode parent) throws WTException {
		return null;
	}
	
	@Override
	public NumberCode saveNumberCode(HashMap map) throws WTException {
		NumberCode numberCode = null;
		try {
			ReferenceFactory rf = new ReferenceFactory();
			
			String oid = map.get("oid")==null? "": ((String)map.get("oid")).trim();
			String code = map.get("code")==null? "":((String)map.get("code")).trim();
			String name = map.get("name")==null? "":((String)map.get("name")).trim();
			String description = map.get("description")==null? "":((String)map.get("description")).trim();
			String type = map.get("type")==null? "":((String)map.get("type")).trim();
			String parentOid = map.get("parentOid")==null? "":((String)map.get("parentOid")).trim();
			
			if(oid.length() > 0) {
				numberCode = (NumberCode)rf.getReference(oid).getObject();
			}
			
			if(numberCode == null) {
				numberCode = NumberCode.newNumberCode();
			}
			
			if(code.length() > 0) {
				numberCode.setCode(code.toUpperCase());
			}
			
			if(name.length() > 0) {
				numberCode.setName(name);
			}
			
			numberCode.setDescription(description);
			
			if(oid.length() == 0 && type.length() > 0) {
				numberCode.setCodeType(NumberCodeType.toNumberCodeType(type));
			}
		/*	
			NumberCode parent = null;
			if((parentOid.trim()).length() > 0) {
				parent = (NumberCode)rf.getReference(parentOid).getObject();	
			}
			numberCode.setParent(parent);
		*/	
			numberCode = (NumberCode)PersistenceHelper.manager.save(numberCode);
			
			if(numberCode.getParent() != null) {
				QueryResult qr = PersistenceHelper.manager.navigate(numberCode, "parent", NCodeNCodeLink.class, false);
				while(qr.hasMoreElements()) {
					NCodeNCodeLink link = (NCodeNCodeLink)qr.nextElement();
					PersistenceHelper.manager.delete(link);
				}
			}
			
			numberCode = (NumberCode)PersistenceHelper.manager.refresh(numberCode);
			
			NumberCode parent = null;
			if((parentOid.trim()).length() > 0) {
				parent = (NumberCode)rf.getReference(parentOid).getObject();
				numberCode.setParent(parent);
				numberCode = (NumberCode)PersistenceHelper.manager.save(numberCode);
				//NCodeNCodeLink link = NCodeNCodeLink.newNCodeNCodeLink(parent, numberCode);
				//link = (NCodeNCodeLink)PersistenceHelper.manager.save(link);
			}
			
			
			//##### ERP �좎룞�쇿뜝占� �좎룞�숈껜 �좎룞�쇿뜝占썲뜝�숈삕... SAP�좎룞�쇿뜝�숈삕 �좎룞�숈껜 �좎룞�쇿뜝�숈삕 �좎룞���좎룞��
			/*
			if("PROCESSDIVISIONCODE".equals(numberCode.getCodeType().toString())) {
				sendStdInfoToERP(null, numberCode.getCodeType().toString());
			}
			*/
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return numberCode;
	}
	
	@Override
	public boolean deleteNumberCode(NumberCode code) throws WTException {
		try {
			ArrayList childs = getChildNumberCode(code);
			if(childs.size() > 0) {
				return false;
			}
			
            QueryResult qr = PersistenceHelper.manager.navigate(code, "ALL", BinaryLink.class, false);
            if (qr.size() == 0)
                PersistenceHelper.manager.delete(code);
            else {
            	while(qr.hasMoreElements()) {
            		Object obj = (Object)qr.nextElement();
            		if( !(obj instanceof NCodeNCodeLink) ) {
            			return false;
            		}
            	}
            }
            
            PersistenceHelper.manager.delete(code); 
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean checkNumberCode(HashMap map) throws WTException {
		try {
			String code = (String)map.get("code");
			String type = (String)map.get("type");
			String parentOid = (String)map.get("parentOid");
			
			if(code == null) code = "";
			if(type == null) type = "";
			if(parentOid == null) parentOid = "";
			
			if(code.length() == 0)
				return false;
			
			if(type.length() == 0)
				return false;
			
			
			HashMap smap = new HashMap();
			smap.put("code", code);
			smap.put("type", type);
			if(parentOid.length() > 0) {
				ReferenceFactory rf = new ReferenceFactory();
				NumberCode parent = (NumberCode)rf.getReference(parentOid).getObject();
				smap.put("parent", parent);
			}
			
			QuerySpec qs = getCodeQuerySpec(smap);
			QueryResult qr = PersistenceHelper.manager.find(qs);
			if(qr.hasMoreElements()) {
				return true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public ArrayList getSubCodeTree(NumberCode parent, String type) throws WTException {
		ArrayList treeList = new ArrayList();
		try {
			ArrayList list = null;
			if(parent == null) {
				list = CodeHelper.service.getTopNumberCode(NumberCodeType.toNumberCodeType(type));
			}
			else {
				list = CodeHelper.service.getChildNumberCode(parent);
			}
			
			if(list != null && list.size() > 0) {
				NumberCode numberCode = null;
				for(int i = 0; i < list.size(); i++) {
					numberCode = (NumberCode)list.get(i);
					
					makeCodeTree(numberCode, treeList);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return treeList;
	}
	
	@Override
	public void makeCodeTree(NumberCode parent, ArrayList list) throws WTException {
		try {
			if(list == null) {
				list = new ArrayList();
			}
			
			list.add(parent);
			
			ArrayList childs = CodeHelper.service.getChildNumberCode(parent);
			NumberCode child = null;
			for(int i = 0; i < childs.size(); i++) {
				child = (NumberCode)childs.get(i);
				
				makeCodeTree(child, list);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public QuerySpec getCodeQuerySpec(HashMap map) throws WTException {
		
		QuerySpec qs = null;
		try {
			String type = (String)map.get("type");
			String name = (String)map.get("name");
			String code = (String)map.get("code");
			String description = (String)map.get("description");
			NumberCode parent = (NumberCode)map.get("parent");
			String isParent = (String)map.get("isParent");
			
			qs = new QuerySpec();
			int i = qs.addClassList(NumberCode.class, true);
			
			
			SearchCondition sc = null;
			if(type != null && type.length() > 0) {
				sc = new SearchCondition(NumberCode.class, "codeType", SearchCondition.EQUAL, NumberCodeType.toNumberCodeType(type));
				qs.appendWhere(sc, new int[] { i });
			}
			
			if(name != null && name.length() > 0) {
				if(qs.getConditionCount() > 0)
					qs.appendAnd();
				
				sc = new SearchCondition(NumberCode.class, "name", SearchCondition.LIKE, "%"+name+"%");
				qs.appendWhere(sc, new int[] { i });
			}
			
			if(code != null && code.length() > 0) {
				if(qs.getConditionCount() > 0)
					qs.appendAnd();
				
				sc = new SearchCondition(NumberCode.class, "code", SearchCondition.EQUAL, code);
				qs.appendWhere(sc, new int[] { i });
			}
			
			if(description != null && description.length() > 0) {
				if(qs.getConditionCount() > 0)
					qs.appendAnd();
				
				sc = new SearchCondition(NumberCode.class, "description", SearchCondition.LIKE, "%"+description+"%");
				qs.appendWhere(sc, new int[] { i });
			}
			
			if(isParent != null && "false".equals(isParent.toLowerCase())) {
				if(qs.getConditionCount() > 0)
					qs.appendAnd();
				
				sc = new SearchCondition(NumberCode.class, "parentReference.key.classname", true);
				qs.appendWhere(sc, new int[] { i });
			}
			else {			
				if(parent != null) {
					if(qs.getConditionCount() > 0)
						qs.appendAnd();
					
					sc = new SearchCondition(NumberCode.class, "parentReference.key.id", 
							SearchCondition.EQUAL, parent.getPersistInfo().getObjectIdentifier().getId());
					qs.appendWhere(sc, new int[] { i });
				}
			}
			
			ClassAttribute ca = new ClassAttribute(NumberCode.class, "code");
			ca.setColumnAlias("wtsort"+String.valueOf(0));
			qs.appendSelect(ca, new int[]{i}, false);
			OrderBy orderby = new OrderBy(ca, false, null);
			qs.appendOrderBy(orderby, new int[]{i});			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return qs;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#searchCodesForAutoComplete(java.lang.String, java.lang.String)
	 */
	@Override
	public List<NumberCode> searchCodesForAutoComplete(String codeType, String term) throws WTException {
		QuerySpec qs = new QuerySpec();
		int index = qs.addClassList(NumberCode.class, true);
		
		// #. codeType 조건
		qs.appendWhere(new SearchCondition(NumberCode.class, 
				NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType),
			new int[] { index });
		
		// #. 뒷쪽 like 검색
		qs.appendAnd();
		qs.appendWhere(new SearchCondition(NumberCode.class, 
				NumberCode.NAME, SearchCondition.LIKE, term + "%"), 
			new int[] { index });
		
		// #. 정렬
		try {
			ClassAttribute ca = new ClassAttribute(NumberCode.class, "code");
			ca.setColumnAlias("wtsort" + String.valueOf(0));
			qs.appendSelect(ca, new int[] { index }, false);
			OrderBy orderby = new OrderBy(ca, false, null);
			qs.appendOrderBy(orderby, new int[] { index });
		} catch (WTPropertyVetoException e) {
			throw new WTException(e);
		}
		
		// #. 조회
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
		
		// #. 결과목록
		List<NumberCode> codes = new ArrayList<NumberCode>();
		while (qr.hasMoreElements()) {
			NumberCode code = (NumberCode)((Object[])qr.nextElement())[0];
			codes.add(code);
		}
		return codes;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#getCodes(java.lang.String)
	 */
	@Override
	public List<NumberCode> getCodes(String codeType) throws WTException {
		return getCodes(codeType, null, false);
	}
	
	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#getCodes(java.lang.String)
	 */
	@Override
	public List<NumberCode> getCodes(String codeType, boolean disabled) throws WTException {
		return getCodes(codeType, null, false, disabled);
	}

	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#getCodes(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public List<NumberCode> getCodes(String codeType, String sort, boolean desc)
			throws WTException {
		try {
			List<NumberCode> codes = new ArrayList<NumberCode>();
			
			QuerySpec qs = new QuerySpec(NumberCode.class);
			int idx = qs.addClassList(NumberCode.class, true);
			
			// #. codeType 조건
			qs.appendWhere(new SearchCondition(NumberCode.class, 
					NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType), 
				new int[] { idx });
			
			// #. disabled 조건
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(NumberCode.class, 
					NumberCode.DISABLED, SearchCondition.IS_FALSE), 
				new int[] { idx });
			
			// #. Order by
			sort = TypeUtil.stringValue(sort);
			if (sort.length() == 0) {
				sort = NumberCode.SORT;
				desc = false;
			}
			ClassAttribute ca = new ClassAttribute(NumberCode.class, sort);
			ca.setColumnAlias("sort0");
			int[] sortIndex = { idx };
			qs.appendSelect(ca, sortIndex, true); // false is selectOnly
			qs.appendOrderBy(new OrderBy(ca, desc), sortIndex);
			
			// #. 쿼리실행
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
			while (qr.hasMoreElements()) {
				NumberCode code = (NumberCode)((Object[])qr.nextElement())[0];
				codes.add(code);
			}
			return codes;
		} catch (WTPropertyVetoException e) {
			throw new WTException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#getCodes(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public List<NumberCode> getCodes(String codeType, String sort, boolean desc, boolean disabled)
			throws WTException {
		try {
			List<NumberCode> codes = new ArrayList<NumberCode>();
			
			QuerySpec qs = new QuerySpec(NumberCode.class);
			int idx = qs.addClassList(NumberCode.class, true);
			
			// #. codeType 조건
			qs.appendWhere(new SearchCondition(NumberCode.class, 
					NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType), 
				new int[] { idx });
			
			// #. disabled 조건
			if( !disabled){
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(NumberCode.class, 
					NumberCode.DISABLED, SearchCondition.IS_FALSE), 
				new int[] { idx });
			}
			// #. Order by
			sort = TypeUtil.stringValue(sort);
			if (sort.length() == 0) {
				sort = NumberCode.SORT;
				desc = false;
			}
			ClassAttribute ca = new ClassAttribute(NumberCode.class, sort);
			ca.setColumnAlias("sort0");
			int[] sortIndex = { idx };
			qs.appendSelect(ca, sortIndex, true); // false is selectOnly
			qs.appendOrderBy(new OrderBy(ca, desc), sortIndex);
			
			// #. 쿼리실행
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
			while (qr.hasMoreElements()) {
				NumberCode code = (NumberCode)((Object[])qr.nextElement())[0];
				codes.add(code);
			}
			return codes;
		} catch (WTPropertyVetoException e) {
			throw new WTException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#existsCode(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean existsCode(String codeType, String code) throws WTException {
		QuerySpec qs = new QuerySpec(NumberCode.class);
		
		qs.appendWhere(new SearchCondition(NumberCode.class, 
				NumberCode.CODE_TYPE, SearchCondition.EQUAL, codeType),
			new int[] { 0 });
		
		qs.appendAnd();
		
		qs.appendWhere(new SearchCondition(NumberCode.class, 
				NumberCode.CODE, SearchCondition.EQUAL, code), 
			new int[] { 0 });
		
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
		
		return (qr.size() > 0);
	}
	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#getChildCodes(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<NumberCode> getChildCodes(String parentCodeType, String parentCode, String childCodeType) throws WTException {
		NumberCode parentNumberCode = this.getNumberCode(parentCodeType, parentCode);
		return getChildCodes(parentNumberCode, childCodeType);
	}
	
	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#getChildCodes(com.e3ps.common.code.NumberCode, java.lang.String)
	 */
	@Override
	public List<NumberCode> getChildCodes(NumberCode parentNumberCode, String childCodeType)
			throws WTException {
		return getChildCodes(parentNumberCode, childCodeType, null, false);
	}

	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#getChildCodes(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public List<NumberCode> getChildCodes(
				String parentCodeType, String parentCode, String childCodeType, String sort, boolean desc) 
			throws WTException {
		NumberCode parentNumberCode = this.getNumberCode(parentCodeType, parentCode);
		return getChildCodes(parentNumberCode, childCodeType, sort, desc);
	}

	@Override
	public List<Map<String, Object>> getNumberCodeTree(String codeType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NumberCode> getChildCodes(NumberCode parentNumberCode, String childCodeType, String sort, boolean desc)
			throws WTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValues(String codeType, String code) throws Exception {
		String value = "";
		if(code.contains(",")) {
			String[] codes = code.split(",");
			for(String a : codes) {
				NumberCode nc = getNumberCode(codeType,a);
				if(nc==null){
		    		return null;
		    	}
				if(!"".equals(value)) {
					value += ",";
				}
				value += nc.getName();
			}
		}else {
			NumberCode nc = getNumberCode(codeType,code);
	    	if(nc==null){
	    		return null;
	    	}
	    	value = nc.getName();
		}
		
    	return value;
	}

	/* (non-Javadoc)
	 * @see com.e3ps.common.code.service.NumberCodeService#getChildCodes(com.e3ps.common.code.NumberCode, java.lang.String, boolean)
	 */
}
