package com.e3ps.project.beans;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.project.ETask;
import com.e3ps.project.PrePostLink;

public class ProjectGanttLinkData {
	
	private String id;
	private String source;
	private String target;
	private String type;
	
	//For Set type
	public static final String FINISH_TO_START = "0";
	public static final String START_TO_START = "1";
	public static final String FINISH_TO_FINISH = "2";
	public static final String START_TO_FINISH = "3";
	
	public ProjectGanttLinkData(PrePostLink link) throws Exception {
		ETask preTask = (ETask) link.getPre();
		ETask postTask = (ETask) link.getPost();
		
		String preTaskOid = CommonUtil.getOIDString(preTask);
		String postTaskOid = CommonUtil.getOIDString(postTask);
		
		this.id = CommonUtil.getOIDString(link);
		this.source = preTaskOid;
		this.target = postTaskOid;
		this.type = FINISH_TO_START;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
