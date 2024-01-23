package com.e3ps.common.drm;

import java.io.Serializable;

import com.e3ps.common.log4j.Log4jPackages;

public class DrmAuth implements Serializable{
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	private String policy;
	
	private String startDate;
	
	private String endDate;
	
	private int authType;
	
//	 * 0:문서 View 권한이 없는 경우.
//	 * 1:관리자이며, 관리자에게 모든 권한 부여함.
//	 * 2:작성자이면, 작성자에게 모든 권한 부여함.
//	 * 3:수정자, 작성자에게 모든 권한 부여함.
//	 * 4:PM, 프로젝트의 산출물 문서이고, 해당 프로젝트의 PM인 경우.
//	 * 5:산출물 View 권한, 프로젝트의 산출물 문서이고 산출물에 View 권한이 설정된 경우이며, 산출물에 View 권한이 있는 경우,
//	 * 6:프로젝트 View 권한, 프로젝트의 산출물 문서이고 산출물에 View 권한이 설정이 없는 경우이며, 해당 프로젝트 View 권한이 있는 경우,
//	 * 7:문서 View 권한, 프로젝트의 산출물 문서가 아니며, 해당 문서 타입에 View 권한이 있는 경우.
	
	public DrmAuth(int authType, String policy, String startDate, String endDate){
		this.authType = authType;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public int getAuthType(){
		return authType;
	}
	
	public String getPolicy(){
		return policy;
	}
	
	public String getStartDate(){
		return startDate;
	}
	
	public String getEndDate(){
		return endDate;
	}

}
