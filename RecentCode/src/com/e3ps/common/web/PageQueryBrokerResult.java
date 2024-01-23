package com.e3ps.common.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageQueryBrokerResult<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 목록수 */
	private int psize = 10;
	/** 현재페이지 */
	private int cpage = 1;
	/** 총갯수 */
	private int total = 0;
	/** 페이지 갯수 */
	private int pageCount = 10;
	/** 모름 */
	private int topListCount = 0;
	/** 세션 식별자 */
	private long sessionid = 0L;
	/** 목록 객체들 */
	List<T> list;
	
	/**
	 * 생성자
	 */
	public PageQueryBrokerResult() {
		list = new ArrayList<T>();
	}

	/**
	 * 아이템을 추가한다
	 * @param item
	 */
	public void add(T item) {
		list.add(item);
	}
	
	/**
	 * @return the psize
	 */
	public int getPsize() {
		return psize;
	}

	/**
	 * @param psize the psize to set
	 */
	public void setPsize(int psize) {
		this.psize = psize;
	}

	/**
	 * @return the cpage
	 */
	public int getCpage() {
		return cpage;
	}

	/**
	 * @param cpage the cpage to set
	 */
	public void setCpage(int cpage) {
		this.cpage = cpage;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the topListCount
	 */
	public int getTopListCount() {
		return topListCount;
	}

	/**
	 * @param topListCount the topListCount to set
	 */
	public void setTopListCount(int topListCount) {
		this.topListCount = topListCount;
	}

	/**
	 * @return the sessionid
	 */
	public long getSessionid() {
		return sessionid;
	}

	/**
	 * @param sessionid the sessionid to set
	 */
	public void setSessionid(long sessionid) {
		this.sessionid = sessionid;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}
	
	
}