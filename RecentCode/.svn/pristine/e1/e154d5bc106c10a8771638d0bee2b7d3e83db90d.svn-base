package com.e3ps.project.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.project.ETask;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.service.TemplateHelper;

import wt.fc.PersistenceHelper;
import wt.method.RemoteAccess;

public class TemplateTreeModel implements RemoteAccess {

	private List<TemplateTreeNode> list;
	
	public Map<String, List<TemplateTreeNode>> postMap = new HashMap<>();
	
	public TemplateTreeModel(ScheduleNode rootNode)throws Exception{
		
		int level = 0;
		TemplateTreeNode root = new TemplateTreeNode(level, rootNode, null);

		this.list = getRootList(root);
	}
	
	private List<TemplateTreeNode> getRootList(TemplateTreeNode root) throws Exception {
		List<TemplateTreeNode> list = new ArrayList<>();
		
		list.add(root);
		
		getChildren(list, root);
		
		return list;
	}
	
	private void getList(List<TemplateTreeNode> list, TemplateTreeNode parent) throws Exception {
		
		list.add(parent);
		
		for (TemplateTreeNode node : this.list) {
			if(parent.equals(node.getParent())) {
				getList(list, node);
			}
		}
	}
	
	private void getChildren(List<TemplateTreeNode> list, TemplateTreeNode parent) throws Exception {

		List<ETask> childrenList = TemplateHelper.manager.getTemplateTaskChildren(parent.getOid());
		
		parent.setChildren(childrenList.size() > 0);
		for(ETask task : childrenList) {
			TemplateTreeNode data = new TemplateTreeNode(parent.getLevel() + 1, task, parent);
			
			list.add(data);
			
			getChildren(list, data);
		}
	}
	
	public void setSchedule() throws Exception {
		getPreTask();	 	//선행 테스크 수집
    	
    	setManDay();		//공수 산정
	}
	
	private void getPreTask() throws Exception {
		for (TemplateTreeNode node : this.list) {

			node.setPreTaskId();
			List<String> preTaskList = node.getPreTaskList();
					
			for (String preTaskOid : preTaskList) {
				List<TemplateTreeNode> postList = (List<TemplateTreeNode>) this.postMap.get(preTaskOid);

				if (postList == null)
					postList = new ArrayList<TemplateTreeNode>();
				postList.add(node);
				this.postMap.put(preTaskOid, postList);
			}
		}
	}
	
	public void setManDay()throws Exception{
		
		for(TemplateTreeNode node : this.list) {
			
			ScheduleNode sNode = (ScheduleNode) CommonUtil.getObject(node.getOid());
			
			if(node.isChildren()) {
				double totalManDay = 0;
				List<TemplateTreeNode> list = new ArrayList<>();
				getList(list, node);
				for(TemplateTreeNode child : list) {
					double tempManDay = 0;
					List<String> preTaskList = child.getPreTaskList();
					boolean preTaskCheck = false;
					for(String preTaskOid : preTaskList) {
						preTaskCheck = list.stream().anyMatch(findNode -> preTaskOid.equals(findNode.getOid()));
					}
					if(!child.isChildren() && !preTaskCheck) {
						tempManDay = getTotalManDay(list, child);
					}
					if(totalManDay < tempManDay) {
						totalManDay = tempManDay;
					}
				}
				
				sNode.setManDay(totalManDay);
				PersistenceHelper.manager.save(sNode);
			}
		}
	}
	
	private double getTotalManDay(List<TemplateTreeNode> list, TemplateTreeNode node) throws Exception {

		double totalManDay = node.getOrgNodeManDay();
		
		List<TemplateTreeNode> postList = this.postMap.get(node.getOid());

		if (postList == null || postList.size() == 0)
			return totalManDay;

		double tempManDay = 0;
		for (TemplateTreeNode post : postList) {
			if(list.contains(post)) {
				double manDay = getTotalManDay(list, post);
				if(tempManDay < manDay) {
					tempManDay = manDay;
				}
			}
		}
		totalManDay += tempManDay;
		
		return totalManDay;
	}
	
	private List<List<TemplateTreeNode>> getLevelList(List<TemplateTreeNode> list) throws Exception {
		List<List<TemplateTreeNode>> levelList = new ArrayList<>();
		
		for(TemplateTreeNode node : list){
			ArrayList<TemplateTreeNode> tempList = null;
	        try{
	        	tempList = (ArrayList<TemplateTreeNode>) levelList.get(node.getLevel());
	        	tempList.add(node);
	        }catch(IndexOutOfBoundsException ex){
	        	tempList = new ArrayList<TemplateTreeNode>();
	        	tempList.add(node);
	        	levelList.add(tempList);
	        }
		}
		
        return levelList;
	}
}