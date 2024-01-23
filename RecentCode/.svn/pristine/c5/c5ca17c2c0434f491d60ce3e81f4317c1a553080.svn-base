package com.e3ps.stagegate.bean;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.project.EProject;
import com.e3ps.stagegate.SGObjectMaster;
import com.e3ps.stagegate.StageGate;

import wt.util.WTException;

public class StageGateData {
	private String oid;
	private String code;
	private int version;
	private EProject project;
	private SGObjectMaster master;
	private StageGate stageGate;
	private boolean lastVersion;

	public StageGateData(StageGate sg) {
		this.oid = CommonUtil.getOIDString(sg);
		this.code = StringUtil.checkNull(sg.getCode());
		this.project = sg.getProject();
	}
	
	public StageGateData(SGObjectMaster objMaster) throws WTException {
		StageGate sg = objMaster.getStageGate();
		this.stageGate = objMaster.getStageGate();
		this.oid = CommonUtil.getOIDString(objMaster);
		this.code = StringUtil.checkNull(objMaster.getCode());
		this.version = objMaster.getVersion();
		this.project = sg.getProject();
		this.lastVersion = objMaster.isLastVersion();
	}

	
	public boolean isLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(boolean lastVersion) {
		this.lastVersion = lastVersion;
	}

	public StageGate getStageGate() {
		return stageGate;
	}

	public void setStageGate(StageGate stageGate) {
		this.stageGate = stageGate;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public EProject getProject() {
		return project;
	}

	public void setProject(EProject project) {
		this.project = project;
	}

	public SGObjectMaster getMaster() {
		return master;
	}

	public void setMaster(SGObjectMaster master) {
		this.master = master;
	}
	
}
