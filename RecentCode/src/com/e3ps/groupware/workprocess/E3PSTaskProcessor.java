package com.e3ps.groupware.workprocess;

import wt.workflow.worklist.WfTaskProcessor;

import com.e3ps.common.util.CommonUtil;

public class E3PSTaskProcessor extends WfTaskProcessor{
	
	public boolean isWorkItemOwner()
    {
        boolean flag = false;
        
        try {
           // System.out.println("isAdmin = " + CommonUtil.isAdmin());
			if(CommonUtil.isAdmin()){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return super.isWorkItemOwner();
    }
}
