package com.e3ps.project.beans;

import com.e3ps.common.code.bean.NumberCodeData;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectTemplate;
import com.e3ps.project.OutputType;
import com.e3ps.project.service.ProjectMemberHelper;

import wt.org.WTUser;
import wt.session.SessionHelper;

public class ProjectData {

	private String oid;														// OID
	private String name;													// 프로젝트 명
	private String code;													// 프로젝트 코드/번호
	private String version;													// 프로젝트 버전
	private String projectType;												// 프로젝트 타입
	private String projectTypeDisplay;
	private String templateOid;
	private String templateCode;
	private String templateName;											// 템플릿 명
	private String state;
	private String stateName;
	private String stateTag;												// 프로젝트 상태 태크
	private String creatorFullName;											// 작성자
	private String createDate;												// 작성일 --- 'd'
	private String createDateA;												// 작성일 --- 'a'
	private String modifyDate;												// 수정일
	private String description;												// 설명
	private String planStartDate;											// 계획 시작일
	private String planEndDate;												// 계획 종료일
	private String startDate;												// 실제 시작일
	private String endDate;													// 실제 종료일
	private String completion;												// 진행
	private String preferComp;												// 적정
	private String pmName;													// PM 이름
	private String checkInComment;											// 수정사유
	private int planDuration;
	private int planDurationHoliday; 
	
	private String groupCode;
	private String groupDisplay;
	private String materialCode;
	private String materialDisplay;
	private String levelCode;
	private String levelDisplay;
	
	
	private boolean isWriter;
	
	public ProjectData(EProject project) throws Exception {
		
		this.oid = CommonUtil.getOIDString(project);
		this.name = project.getName();
		this.code = project.getCode();
		this.version = String.valueOf(project.getVersion());
		this.projectType = project.getProjectType();
		this.state = project.getLifeCycleState().toString();
		this.stateName = project.getLifeCycleState().getDisplay();
		this.stateTag = ProjectUtil.getStateTag(project);
		this.creatorFullName = project.getCreator().getFullName();
		this.planStartDate = DateUtil.getDateString(project.getPlanStartDate(),"d");
		this.planEndDate = DateUtil.getDateString(project.getPlanEndDate(),"d");
		this.planDuration = ProjectUtil.getDuration(project.getPlanStartDate(), project.getPlanEndDate());
		this.planDurationHoliday =  ProjectUtil.getDurationHoliday(project.getPlanStartDate(), project.getPlanEndDate());
		this.startDate = DateUtil.getDateString(project.getStartDate(),"d");
		this.endDate = DateUtil.getDateString(project.getEndDate(),"d");
		this.createDate = DateUtil.getDateString(project.getPersistInfo().getCreateStamp(),"d");
		this.modifyDate = DateUtil.getDateString(project.getPersistInfo().getModifyStamp(),"d");
		this.description = project.getDescription();
		this.completion = ProjectUtil.numberFormat(project.getCompletion());
		this.checkInComment = project.getCheckInComment()==null?"":project.getCheckInComment();
		
		this.groupCode = project.getGroupCode();  //PCode : GROUP
		this.materialCode = project.getMaterialCode();  //PCode : MATERIAL
		this.levelCode = project.getLevelCode();	//PCode : LEVEL
		
		if( groupCode != null) {
			NumberCodeData groupData = CodeHelper.manager.getNumberCode("PROJECTNUMBERPROP", project.getGroupCode(), "GROUP");
			this.groupDisplay = groupData.getName();
		}
		
		if( materialCode != null) {
			NumberCodeData materialData = CodeHelper.manager.getNumberCode("PROJECTNUMBERPROP", project.getMaterialCode(), "MATERIAL");
			this.materialDisplay = materialData.getName();
		}
		
		if( levelCode != null) {
			NumberCodeData levelData = CodeHelper.manager.getNumberCode("PROJECTNUMBERPROP", project.getLevelCode(), "LEVEL");
			this.levelDisplay = levelData.getName();
		}
		
		this.isWriter = SessionHelper.getPrincipal().equals(project.getCreator().getPrincipal());
		
		OutputType outputType = OutputType.toOutputType(this.projectType);
		if(outputType != null) {
			this.projectTypeDisplay = outputType.getDisplay();
		}
				
		EProjectTemplate template = project.getTemplate();
		if(template != null) {
			this.templateOid = CommonUtil.getOIDString(template);
			this.templateCode = template.getCode();
			this.templateName = template.getName();
		}
		
		WTUser pm = ProjectMemberHelper.manager.getPM(project);
		if(pm != null) {
			this.pmName = pm.getFullName();
		}
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectTypeDisplay() {
		return projectTypeDisplay;
	}

	public void setProjectTypeDisplay(String projectTypeDisplay) {
		this.projectTypeDisplay = projectTypeDisplay;
	}

	public String getTemplateOid() {
		return templateOid;
	}

	public void setTemplateOid(String templateOid) {
		this.templateOid = templateOid;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStateTag() {
		return stateTag;
	}

	public void setStateTag(String stateTag) {
		this.stateTag = stateTag;
	}

	public String getCreatorFullName() {
		return creatorFullName;
	}

	public void setCreatorFullName(String creatorFullName) {
		this.creatorFullName = creatorFullName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDateA() {
		return createDateA;
	}

	public void setCreateDateA(String createDateA) {
		this.createDateA = createDateA;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}

	public String getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCompletion() {
		return completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}

	public String getPreferComp() {
		return preferComp;
	}

	public void setPreferComp(String preferComp) {
		this.preferComp = preferComp;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public String getCheckInComment() {
		return checkInComment;
	}

	public void setCheckInComment(String checkInComment) {
		this.checkInComment = checkInComment;
	}

	public int getPlanDurationHoliday() {
		return planDurationHoliday;
	}

	public void setPlanDurationHoliday(int planDurationHoliday) {
		this.planDurationHoliday = planDurationHoliday;
	}
	
	public int getPlanDuration() {
		return planDuration;
	}

	public void setPlanDuration(int planDuration) {
		this.planDuration = planDuration;
	}
	
	public boolean isWriter() {
		return isWriter;
	}

	public void setWriter(boolean isWriter) {
		this.isWriter = isWriter;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupDisplay() {
		return groupDisplay;
	}

	public void setGroupDisplay(String groupDisplay) {
		this.groupDisplay = groupDisplay;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialDisplay() {
		return materialDisplay;
	}

	public void setMaterialDisplay(String materialDisplay) {
		this.materialDisplay = materialDisplay;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getLevelDisplay() {
		return levelDisplay;
	}

	public void setLevelDisplay(String levelDisplay) {
		this.levelDisplay = levelDisplay;
	}
	
}