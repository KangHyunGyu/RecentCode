package com.e3ps.project.beans;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;


public  class DefaultProjectTreeNode extends DefaultMutableTreeNode{

	private Vector preTasks = new Vector();
	private Vector afterTasks = new Vector();
	
	public static final int DELTE = -1;
	public static final int MODIFY = 1;
	public static final int ADD = 2;
	public static final int DEFAULT = 0;
	private DefaultProjectTreeNode compareTreeNode;
	
	
	private int compareResult = ADD;
	
	public int getCompareResult(){
		return compareResult;
	}
	
	private void setCompareNode(DefaultProjectTreeNode node){
		this.compareTreeNode = node;
	}
	
	public void setCompareResult(int compareResult){
		this.compareResult = compareResult; 
	}
	
	public DefaultProjectTreeNode(Object userObject) {
		super(userObject);
	}

	private void addAfterTask(DefaultProjectTreeNode node){
		afterTasks.add(node);
	}
	
	public void addPreTask(DefaultProjectTreeNode node){
		preTasks.add(node);
		node.addAfterTask(this);
	}
	
	public Vector getPreTasks(){
		return preTasks;
	}
	
	public Vector getAfterTasks(){
		return afterTasks;
	}
	
	public void validate_completion(){	
	}
	
	public void validate_planSchedule(){	
	}
	
	public void validate_dependency(){	
	}
}
