package com.e3ps.change.beans;

import com.e3ps.change.EChangeStopStartHistory;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

public class EChangeStopStartHistoryData {
	private String oid;
	private String name;
	private String stop;
	private String activeDate;
	private String comments;
	private String owner;
	public EChangeStopStartHistoryData(EChangeStopStartHistory his) {
		this.oid = CommonUtil.getOIDString(his);
		this.name = StringUtil.checkNull(his.getName());
		if(his.isStop()) {
			this.stop = "중단";
		}else {
			this.stop = "재시작";
		}
		this.activeDate = StringUtil.checkNull(his.getActiveDate().toString());
		this.comments = StringUtil.checkNull(his.getComments());
		this.owner = StringUtil.checkNull(his.getOwner().getFullName());
	}
	
	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

}
