package com.e3ps.project.beans;

import java.util.List;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ScheduleNode;

public class NodeTreeData {
	
	protected ScheduleNode node;
	
	private String id;
	private String oid;
	
	private String parentId;
	
	private String name;
	private int sort;
	private int manDay;
	
	List<? extends NodeTreeData> data;
	
	public NodeTreeData(ScheduleNode node) {
		
		this.node = node;
		
		this.oid = CommonUtil.getOIDString(node);
		
		if(node instanceof ETaskNode) {
			this.parentId = CommonUtil.getOIDString(node.getParent());
		}
		this.id = oid;
		
		this.name = node.getName();
		this.sort = node.getSort();
		this.manDay = (int) node.getManDay();
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

	public List<? extends NodeTreeData> getData() {
		return data;
	}

	public void setData(List<? extends NodeTreeData> data) {
		this.data = data;
	}

	public int getManDay() {
		return manDay;
	}

	public void setManDay(int manDay) {
		this.manDay = manDay;
	}
	
}
