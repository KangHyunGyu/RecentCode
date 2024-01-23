/**
 * 
 */
package com.e3ps.dashboard.bean;

import com.e3ps.common.util.CommonUtil;

import wt.enterprise.Managed;

/**
 * @author Administrator
 *
 */
public class DashboardData {
	
	public String oid;
	public String name;
	public String state;
	public String stateStr;
	public String createDate;
	public String creatorFullName;
	
	public DashboardData(String oid) {
		_init((Managed) CommonUtil.getObject(oid));
	}
	public DashboardData(Managed managed) {
		_init(managed);
	}
	
	public void _init(Managed managed) {
		this.oid = CommonUtil.getOIDString(managed);
		this.name = managed.getName();
		this.creatorFullName = managed.getCreatorFullName();
		this.createDate = managed.getCreateTimestamp().toString();
		this.state = managed.getLifeCycleState().toString();
		this.stateStr = managed.getLifeCycleState().getDisplay();
	}
}
