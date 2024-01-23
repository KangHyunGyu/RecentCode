package com.e3ps.project.beans;

import java.util.List;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.project.EOutput;
import com.e3ps.project.ETask;
import com.e3ps.project.OutputType;
import com.e3ps.project.OutputTypeStep;

public class OutputTypeStepTreeData {
	
	private String id;
	private String oid;
	
	private String parentId;
	
	private String name;
	private int sort;
	
	private String iconUrl;
	
	List<OutputTypeStepTreeData> data;
	List<OutputTypeStepTreeData> outputData;
	
	public OutputTypeStepTreeData(String outputType, String projectOid) {
		
		this.id = outputType;
		this.oid = projectOid;
		OutputType type = OutputType.toOutputType(outputType);
		this.name = type.getDisplay();
		
		this.iconUrl = "/Windchill/jsp/project/images/tree/step.gif";
	}

	public OutputTypeStepTreeData(OutputTypeStep step) {
		
		this.oid = CommonUtil.getOIDString(step);
		
		this.parentId = CommonUtil.getOIDString(step.getParent());
		if(this.parentId == null) {
			this.parentId = step.getOutputType().toString();
		}
		this.id = oid;
		
		this.name = step.getName();
		this.sort = step.getSort();
		
		this.iconUrl = "/Windchill/jsp/project/images/tree/step.gif";
	}
	
	public OutputTypeStepTreeData(ETask task, EOutput output) {
		
		this.oid = CommonUtil.getOIDString(task);
		
		this.parentId = CommonUtil.getOIDString(output.getStep());
		this.id = CommonUtil.getOIDString(output);
		
		this.name = output.getName();
		this.sort = task.getSort();
		
		this.iconUrl = "/Windchill/jsp/project/images/tree/imgfolder.gif";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public List<OutputTypeStepTreeData> getData() {
		return data;
	}

	public void setData(List<OutputTypeStepTreeData> data) {
		this.data = data;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
}
