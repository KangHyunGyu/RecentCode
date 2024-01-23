package com.e3ps.common.code.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.NumberCodeType;

import wt.fc.PagingQueryResult;
import wt.fc.QueryResult;
import wt.method.RemoteInterface;
import wt.query.QuerySpec;
import wt.util.WTException;

@RemoteInterface
public interface CodeService {

	public abstract void saveNumberCodeAction(Map<String, Object> reqMap) throws Exception;
	public abstract void saveNumberCodeAction2(Map<String, Object> reqMap) throws Exception;
	String getValue(String codeType, String code) throws Exception;
	String getValues(String codeType, String code) throws Exception;
	NumberCode getNumberCode(String codeType, String code);
	
	 public QueryResult getCode(String key);
	   
	   public Vector<NumberCode> getCodeVec(String key);
	   
	   public String getName(String key,String code);
	   
	   public String getCode(String key,String name);
	   
	   public QueryResult getChildCode(String key,String parentoid);
	   
	   public QueryResult getTopCode(String key);
	   
	   public QueryResult getCodeNum(String type);
	   
	   public QueryResult getCodeNum(String type, boolean disabled);
	   
	   public NumberCode getTopParent(NumberCode code);
	   
	   public int getCodelevel(NumberCode code ,Integer level);
	   
	   public QueryResult getChildCode(String codeArry);
	   
	   public Vector<NumberCode> getChildCodeVec(String codeType);
	   
	   public Vector<NumberCode> getUsed(String codeType) throws Exception ;
	   public NumberCode getNumberCodeName(String codeType, String name);
	   
	   public Vector getNumberCodeForQuery(String codeType);
	   
	   public HashMap getNumberCode(String codeType);
	   
	   public QueryResult getQueryResult(String codeType, String sortType);
	   
	   public QueryResult getQueryResult(String codeType, String sortType, boolean isValidate);
	   
	   public QueryResult getQueryResult(String codeType, String sortType, boolean isValidate, boolean desc);
	   
	   public ArrayList getTopNumberCode(NumberCodeType type) throws WTException;
	   
	   public ArrayList getChildNumberCode(NumberCode numberCode) throws WTException;
	   
	   public ArrayList getNumberCode(HashMap map) throws WTException;
	   
	   public PagingQueryResult openPagingSession(HashMap map, int start, int size) throws WTException;
	   
	   public PagingQueryResult openPagingSession(QuerySpec spec, int start, int size) throws WTException;
	   
	   public PagingQueryResult fetchPagingSession(int start, int size, long sessionId) throws WTException;
	   
	   public ArrayList ancestorNumberCode(NumberCode child) throws WTException;
	   
	   public void searchAncestorNumberCode(NumberCode child, ArrayList list) throws WTException;
	   
	   public ArrayList descendantsNumberCode(NumberCode parent) throws WTException;
	   
	   public NumberCode saveNumberCode(HashMap map) throws WTException;
	   
	   public boolean deleteNumberCode(NumberCode code) throws WTException;
	   
	   public boolean checkNumberCode(HashMap map) throws WTException;
	   
	   public ArrayList getSubCodeTree(NumberCode parent, String type) throws WTException;
	   
	   public void makeCodeTree(NumberCode parent, ArrayList list) throws WTException;
	   
	   public QuerySpec getCodeQuerySpec(HashMap map) throws WTException;
	   
	   /**
	    * 자동완성을 위해 코드들을 검색한다
	    * @param codeType
	    * @param term
	    * @return
	    * @throws WTException
	    */
	   public List<NumberCode> searchCodesForAutoComplete(String codeType, String term) throws WTException;
	   
	   /**
	    * 지정한 타입의 코드들을 조회한다
	    * @param codeType
	    * @return
	    * @throws WTException
	    */
	   public List<NumberCode> getCodes(String codeType) throws WTException;
	   
	   /**
	    * 지정한 타입의 코드들을 조회한다
	    * @param codeType
	    * @return
	    * @throws WTException
	    */
	   public List<NumberCode> getCodes(String codeType, boolean disabled) throws WTException;
	   
	   
	   public List<NumberCode> getCodes(String codeType, String sort, boolean desc) throws WTException;
	   
	   public List<NumberCode> getCodes(String codeType, String sort, boolean desc, boolean disabled) throws WTException;
	   
	   /**
	    * 코드가 존재하면 true
	    * @param codeType
	    * @param code
	    * @return
	    * @throws WTException
	    */
	   public boolean existsCode(String codeType, String code) throws WTException;
	   
	   /**
	    * NuberCodeData 트리에서 사용할 List Data
	    * @param codeType
	    * @return
	    * @throws WTException
	    */
	   public List<Map<String, Object>> getNumberCodeTree(String codeType) throws Exception;
	   
	   /**
	    * 하위코드들을 조회한다
	    * @param parentCodeType
	    * @param parentCode
	    * @param childCodeType
	    * @return
	    * @throws WTException
	    */
	   public List<NumberCode> getChildCodes(String parentCodeType, String parentCode, String childCodeType) throws WTException;
	   
	   /**
	    * 하위코드들을 조회한다
	    * @param parentNumberCode
	    * @param childCodeType
	    * @return
	    * @throws WTException
	    */
	   public List<NumberCode> getChildCodes(NumberCode parentNumberCode, String childCodeType) throws WTException;
	   
	   /**
	    * 하위코드들을 조회한다
	    * @param parentCodeType
	    * @param parentCode
	    * @param childCodeType
	    * @param sort
	    * @param desc
	    * @return
	    * @throws WTException
	    */
	   public List<NumberCode> getChildCodes(String parentCodeType, String parentCode, String childCodeType, String sort, boolean desc) throws WTException;
	   
	   /**
	    * 하위코드들을 조회한다
	    * @param parentNumberCode
	    * @param childCodeType
	    * @param sort
	    * @param desc
	    * @return
	    * @throws WTException
	    */
	   public List<NumberCode> getChildCodes(NumberCode parentNumberCode, String childCodeType, String sort, boolean desc) throws WTException;

	   String getDescription(String key, String code);

}
