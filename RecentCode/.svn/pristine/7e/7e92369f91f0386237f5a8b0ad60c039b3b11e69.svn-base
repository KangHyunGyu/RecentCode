package com.e3ps.approval.bean;

import java.util.ArrayList;

import com.e3ps.approval.ApprovalLineTemplate;
import com.e3ps.common.bean.OwnPersitableData;
import com.e3ps.common.util.CommonUtil;

public class ApprovalLineTemplateData extends OwnPersitableData{
	
	
	
	private String name;
	private ArrayList<String> discussList;
	private ArrayList<String> approveList;
	private ArrayList<String> receiveList;
	
	public ApprovalLineTemplateData(String oid) throws Exception {
		 super(oid);
		 ApprovalLineTemplate temp = (ApprovalLineTemplate)CommonUtil.getObject(oid);
		 _ApprovalLineTemplateData(temp);
		
	}
	
	public ApprovalLineTemplateData(ApprovalLineTemplate temp) throws Exception {
		super(temp);
		_ApprovalLineTemplateData(temp);
		
	}
	
	public void _ApprovalLineTemplateData(ApprovalLineTemplate temp) throws Exception {
		
		this.name = temp.getName();
		this.discussList = (ArrayList<String>) temp.getDiscussList();
		this.approveList = (ArrayList<String>) temp.getApproveList();
		this.receiveList = (ArrayList<String>) temp.getReceiveList();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getDiscussList() {
		return discussList;
	}
	public void setDiscussList(ArrayList<String> discussList) {
		this.discussList = discussList;
	}
	public ArrayList<String> getApproveList() {
		return approveList;
	}
	public void setApproveList(ArrayList<String> approveList) {
		this.approveList = approveList;
	}
	public ArrayList<String> getReceiveList() {
		return receiveList;
	}
	public void setReceiveList(ArrayList<String> receiveList) {
		this.receiveList = receiveList;
	}
	
	
}
