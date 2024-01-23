package com.e3ps.project.beans;

import com.e3ps.common.util.DateUtil;
import com.e3ps.project.issue.IssueSolution;

public class SolutionData {
	public String oid;
	public String creator;
	public String createDateD;
	public String solution;
	
	public SolutionData(IssueSolution solution) {
		
		this.oid = solution.getPersistInfo().getObjectIdentifier().toString();
		this.creator = solution.getCreator().getFullName();
		this.createDateD = DateUtil.getDateString(solution.getPersistInfo().getCreateStamp(),"d");
		this.solution = solution.getSolution();
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateDateD() {
		return createDateD;
	}

	public void setCreateDateD(String createDateD) {
		this.createDateD = createDateD;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}
	
	
}
