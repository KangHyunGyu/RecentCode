package com.e3ps.common.web;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import wt.fc.ObjectVector;
import wt.fc.PagingQueryResult;
import wt.fc.PagingSessionHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTReference;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;

public class PageQueryBroker implements wt.method.RemoteAccess, java.io.Serializable {

	int psize = 10;
	int cpage = 1;
	int total = 0;
	int pageCount = 10;
	int topListCount = 0;
	long sessionid;

	QuerySpec qs;
	HttpServletRequest req;
	PagingQueryResult result;

	String key;

	boolean isAdvanced = false;

	String sessionKeyword;

	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;

	public PageQueryBroker(HttpServletRequest req, QuerySpec spec) {
		this(req, spec, "");
	}

	public PageQueryBroker(HttpServletRequest req, QuerySpec spec, String key) {
		this.req = req;
		this.qs = spec;
		this.key = key;
		String sessionidstring = req.getParameter("sessionid" + key);
		if (sessionidstring == null || sessionidstring.length() == 0)
			sessionidstring = "0";
		sessionid = Long.parseLong(sessionidstring.trim());

		sessionKeyword = req.getParameter("sessionKeyword" + key);
	}
	
	public PageQueryBroker(QuerySpec spec, long sessionid, String sessionKeyword, int cpage, int psize){
		this.qs = spec;
		this.sessionid = sessionid;
		this.sessionKeyword = sessionKeyword;
		this.cpage = cpage;
		this.psize = psize;
	}
	public <T> PageQueryBrokerResult<T> getPageQueryBrokerResult() {
		PageQueryBrokerResult<T> brokerResult = new PageQueryBrokerResult<T>();
		try {
			QueryResult qr = search();
			while (qr.hasMoreElements()) {
				Object element = qr.nextElement();
				brokerResult.add((T)element);
			}
			
			// #. 속성 셋팅
			brokerResult.setPsize(psize);
			brokerResult.setCpage(cpage);
			brokerResult.setTotal(total);
			brokerResult.setPageCount(pageCount);
			brokerResult.setTopListCount(topListCount);
			brokerResult.setSessionid(sessionid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return brokerResult;
	}
	
	
	public void setAdvanced(boolean isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public static PagingQueryResult openPagingSession(int i, int j, StatementSpec qs, boolean isAdvanced) throws Exception {

		if (isAdvanced && !SERVER) {
			Class argTypes[] = new Class[] { int.class, int.class, StatementSpec.class, boolean.class };
			Object args[] = new Object[] { new Integer(i), new Integer(j), qs, new Boolean(isAdvanced) };
			return (PagingQueryResult) wt.method.RemoteMethodServer.getDefault().invoke("openPagingSession",
					"com.e3ps.common.web.PageQueryBroker", null, argTypes, args);
		}

		if (isAdvanced && !qs.isAdvancedQueryEnabled()) {
			qs.setAdvancedQueryEnabled(true);
		}

		return PagingSessionHelper.openPagingSession(i, j, qs);
	}

	public static PagingQueryResult fetchPagingSession(int i, int j, long sessionid, StatementSpec qs, boolean isAdvanced) throws Exception {

		try {
			return PagingSessionHelper.fetchPagingSession(i, j, sessionid);
		} catch (Exception ex) {
			return openPagingSession(i, j, qs, isAdvanced);
		}
	}

	public PagingQueryResult search() throws Exception {
		String pagestring = req.getParameter("tpage" + key);
		if (pagestring != null && pagestring.length() > 0)
			cpage = Integer.parseInt(pagestring);
		String psizestring = req.getParameter("psize" + key);
		if (psizestring != null && psizestring.length() > 0)
			psize = Integer.parseInt(psizestring);
		if (sessionid <= 0) {
			result = openPagingSession(0, psize, qs, isAdvanced);
		} else {
			result = fetchPagingSession((cpage - 1) * psize, psize, sessionid, qs, isAdvanced);
		}
		if (result != null) {
			total = result.getTotalSize();
			sessionid = result.getSessionId();
			topListCount = total - ((cpage - 1) * psize);
		}
		return result;
	}

	public static ArrayList instreamSearch(String classname, String keyword) throws Exception {

		if (!SERVER) {
			Class argTypes[] = new Class[] { String.class, String.class };
			Object args[] = new Object[] { classname, keyword };
			return (ArrayList) wt.method.RemoteMethodServer.getDefault().invoke("instreamSearch",
					"com.e3ps.common.web.PageQueryBroker", null, argTypes, args);
		}

		ArrayList result = new ArrayList();
		/*
		 * // 검색 조건 입력 모든 필드는 쉼표(,) 로 구분 복수개 입력 가능 IndexSearchInput input = new
		 * IndexSearchInput(); input.setSearchString(keyword);
		 * input.setBusinessTypes(classname);
		 * input.setQueryLanguage(Locale.KOREA.getLanguage());
		 * input.setCollections("wblib"); Vector fetchFields = new Vector();
		 * fetchFields.add("ufid"); input.setFetchFields(fetchFields.elements()); // 추가
		 * 검색 조건 필드 // input.setSearchFields(String s); //
		 * input.setContainerReference(String s); // input.setFilterAccess(true);
		 * //input.setOrganizationReference(String s); // input.setResultView(String s);
		 * // input.setMinHits(int i); // input.setMaxDocs(int i); //
		 * input.setQueryPrefs(Hashtable); // input.setSearchTime(int i); //
		 * input.setSortFields(ArrayList arr); // input.setSortFields(String s, String
		 * s1); // input.setSortOrder(ArrayList list); // input.setSpellCheck(true); //
		 * input.setSearchFields(s); //검색 대상 필드 // input.setCollapse(true);
		 * InstreamSearchDelegate del = new InstreamSearchDelegate();
		 * del.setSearchInput(input); Group gReturn = del.search(null); int i =
		 * gReturn.getElementCount(); for ( int j = 0; j < i; j++ ) { Element element =
		 * gReturn.getElementAt(j); Att att = element.getAtt("obid"); if(att!=null){
		 * String oid = att.getValue().toString(); if(oid.startsWith("OR"))continue;
		 * result.add(oid); } }
		 */
		return result;
	}

	public QueryResult searchKeyword(String classname, String keyword) throws Exception {

		if (sessionKeyword != null) {
			keyword = sessionKeyword;
		} else {
			sessionKeyword = keyword;
		}
		String pagestring = req.getParameter("tpage" + key);
		if (pagestring != null && pagestring.length() > 0)
			cpage = Integer.parseInt(pagestring);
		String psizestring = req.getParameter("psize" + key);
		if (psizestring != null && psizestring.length() > 0)
			psize = Integer.parseInt(psizestring);

		ReferenceFactory rf = new ReferenceFactory();

		ObjectVector ifc = new ObjectVector();

		ArrayList list = instreamSearch(classname, keyword);

		total = list.size();

		for (int j = 0; j < list.size(); j++) {
			if (((cpage - 1) * psize < (j + 1)) && ((j + 1) <= cpage * psize)) {
				try {
					String ufid = (String) list.get(j);
					WTReference ref = (WTReference) rf.getReference(ufid);
					Object o = ref.getObject();
					ifc.addElement(new Object[] { new Object[] { o } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		result = new PagingQueryResult();

		result.append(ifc);

		topListCount = total - ((cpage - 1) * psize);

		return result;
	}

	public void setPsize(int psize) {
		this.psize = psize;
	}

	public void setCpage(int cpage) {
		this.cpage = cpage;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setSessionid(long sessionid) {
		this.sessionid = sessionid;
	}

	public void setQuerySpec(QuerySpec qs) {
		this.qs = qs;
	}

	public void setRequest(HttpServletRequest request) {
		this.req = request;
	}

	public void setResult(PagingQueryResult result) {
		this.result = result;
	}

	public int getPsize() {
		return psize;
	}

	public int getCpage() {
		return cpage;
	}

	public int getTotal() {
		return total;
	}

	public int getPageCount() {
		return pageCount;
	}

	public long getSessionid() {
		return sessionid;
	}

	public QuerySpec getQuerySpec() {
		return qs;
	}

	public HttpServletRequest getRequest() {
		return req;
	}

	public PagingQueryResult getResult() {
		return result;
	}

	public int getTopListCount() {
		return topListCount;
	}

	public void setTopListCount(int topListCount) {
		this.topListCount = topListCount;
	}

}
