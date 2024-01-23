/**
 * 
 */
package com.e3ps.common.bean;

import com.e3ps.common.util.CommonUtil;

import wt.enterprise.Managed;
import wt.org.WTPrincipalReference;
import wt.session.SessionHelper;
import wt.util.WTException;

/**
 * @author plmadmin
 *
 */
public class ManagedData {
	private boolean isModify;
	private boolean isDelete;
	private boolean isWithdrawn;
	private String lcState;
	protected boolean isCreator;
	
	public ManagedData(String oid){
		Managed lc = (Managed)CommonUtil.getObject(oid);
		
		_ManagedData(lc);
	}

	public ManagedData(Managed lc){
		_ManagedData(lc);
	}
	
	public void _ManagedData(Managed lc) {
		this.lcState = lc.getLifeCycleState().toString();
		
		try {
			WTPrincipalReference owner = SessionHelper.manager.getPrincipalReference();
			this.isCreator = owner.equals(lc.getCreator());
		} catch (WTException e) {
			this.isCreator = false;
			//e.printStackTrace();
		}
	}
	
	//수정 : 작업중 ,반려됨
	public boolean modifyBtn() throws WTException{
		this.isModify = ("INWORK".equals(this.lcState) || "RETURN".equals(this.lcState)) || CommonUtil.isAdmin();
		
		return this.isModify;
	}
	//삭제 : 작업중 ,반려됨, 작성자
	public boolean deleteBtn() throws Exception{
		this.isDelete = ("INWORK".equals(this.lcState) || "RETURN".equals(this.lcState)) && this.isCreator || CommonUtil.isAdmin();
		
		return this.isDelete;
	}
	//폐기 : 작업중 ,반려됨, 작성자
	public boolean withdrawnBtn() throws Exception{
		this.isWithdrawn = ("INWORK".equals(this.lcState) || "RETURN".equals(this.lcState)) && this.isCreator || CommonUtil.isAdmin();
		
		return this.isWithdrawn;
	}
	
	//회수 : 승인중, 작성자
	public boolean recallBtn() throws Exception{
		return "APPROVING".equals(this.lcState) && this.isCreator;
	}

	public String getLcState() {
		return lcState;
	}

	public void setLcState(String lcState) {
		this.lcState = lcState;
	}
}
