package com.e3ps.change.beans;

import java.util.ArrayList;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;

import com.e3ps.change.EChangeActivityDefinition;
import com.e3ps.common.util.WebUtil;
import com.e3ps.org.Department;
import com.e3ps.org.DepartmentPeopleLink;
import com.e3ps.org.People;

public class ChangeActivityData {
	public EChangeActivityDefinition def;
	public String deptName;
	public ArrayList userlist = new ArrayList();
	
	public ArrayList optionList = new ArrayList();
	
	public ChangeActivityData(EChangeActivityDefinition def){
		this.def = def;
	}
	
	public void add(Department dept)throws Exception{

			deptName = dept.getName();
		
		QueryResult qr2 = null;
		if(dept!=null){
		       qr2 = PersistenceHelper.manager.navigate(dept, "people", DepartmentPeopleLink.class);
		}

        String deptline = "<img src="+WebUtil.getHost()+"extcore/keyang/pdm/images/tree/base.gif border=0> <b>" +  dept.getName() + "</b>";
        
        deptline = deptline.replaceAll("<","[");
        deptline = deptline.replaceAll(">","]");
                
        optionList.add("<option value='"+dept.getName()+"' >"+ deptline  + "</option>");
		
		int count = 0;
		while(qr2!=null && qr2.hasMoreElements()){
			People people = (People)qr2.nextElement();
		    userlist.add(people);
		    
		    String addline = "<img src="+WebUtil.getHost()+"extcore/keyang/pdm/images/tree/join.gif border=0><img src="+WebUtil.getHost()+"extcore/keyang/pdm/images/tree/opened_folder.png border=0>";
		    
		    if(qr2.size()== count+1){

		    	addline = "<img src="+WebUtil.getHost()+"extcore/keyang/pdm/images/tree/joinbottom.gif border=0><img src="+WebUtil.getHost()+"extcore/keyang/pdm/images/tree/opened_folder.png border=0>";
		    }
		    
		    addline += "<font style=display:none><b>"+deptName+"</b>&nbsp;&nbsp;</font>";
		    addline = addline + (people.getDuty()!=null?people.getDuty():"") + people.getName();
		    
		    addline = addline.replaceAll("<","[");
		    addline = addline.replaceAll(">","]");
		    
		    optionList.add("<option value='"+people.getPersistInfo().getObjectIdentifier().toString()+"'   >"+ addline+ "</option>");
		    
		    count++;
		}
	}
}
