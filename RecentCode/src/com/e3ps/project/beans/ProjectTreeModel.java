/**
 * 
 * 
 */

package com.e3ps.project.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import wt.fc.PersistenceHelper;
import wt.fc.ReferenceFactory;
import wt.method.RemoteAccess;

import com.e3ps.common.util.DateUtil;
import com.e3ps.common.workflow.E3PSWorkflowHelper;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.EProjectTemplate;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.ProjectHelper;




public class ProjectTreeModel implements RemoteAccess {

	public ProjectTreeNode root;	// Root Node
	private long rootId;			// Root Node Id
	public ScheduleNode rootNode;	// Root Node
	
	public ArrayList list;			// Tree 
	public ArrayList levelList;		
	public ArrayList trueList;
	
	public HashMap map = new HashMap();	//  String oid : ScheduleNode node
	public HashMap nodeMap = new HashMap();	// String oid : ProjectTreeNode node
	public Hashtable postMap = new Hashtable();	// String oid : ProjectTreeNode postNode
	
	public boolean isPjtComplate = false;
	
	public boolean isTemplate = false;

	public static Timestamp START_DATE;
	

    
    static{
    	try{
    		START_DATE = DateUtil.getTimestampFormat("20000101", "yyyyMMdd");
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
	
	public int size(){
		return list.size();
	}
	
	public ScheduleNode get(int i){
		String[] s = (String[])list.get(i);
		return (ScheduleNode)map.get(s[2]);
	}
	
	public int getLevel(int i){
		String[] s = (String[])list.get(i);
		return Integer.parseInt(s[0]);
	}
	
	public ProjectTreeModel(ScheduleNode rootNode)throws Exception{
		this(rootNode,0);
	}
	
	public ProjectTreeModel(ScheduleNode rootNode, int depth)throws Exception{
		this.rootNode = rootNode;
		rootId = rootNode.getPersistInfo().getObjectIdentifier().getId();
		//list = ProjectDao.manager.getStructure(rootId, depth);
		list = ProjectDao.manager.getStructure(rootId, depth);
		ReferenceFactory rf = new ReferenceFactory();
		
		if(rootNode instanceof EProjectTemplate){
			isTemplate = true;
		}
		
		HashMap temp = new HashMap();
		root = new ProjectTreeNode(rootNode);
		temp.put("0",root );
		
		trueList = new ArrayList(); //dhtmlx
		
		levelList = new ArrayList();
		ArrayList rootList = new ArrayList();
		rootList.add(root);
		levelList.add(rootList);
		
		nodeMap.put(rootNode.getPersistInfo().getObjectIdentifier().toString(), root);
		map.put(rootNode.getPersistInfo().getObjectIdentifier().toString(), rootNode);
		
		for(int i=0; i< list.size(); i++){
        	
            String[] s = (String[])list.get(i);
            String level = s[0];
            String sid = s[2];
            ProjectTreeNode node = new ProjectTreeNode((ScheduleNode)rf.getReference(sid).getObject());

            map.put(s[2], node.getUserObject());
            nodeMap.put(s[2], node);
            temp.put(level, node);
            int thisLevel = Integer.parseInt(level);
            int parentLevel = thisLevel-1;
            ProjectTreeNode parentNode = (ProjectTreeNode)temp.get(Integer.toString(parentLevel));
            parentNode.add(node);

            ArrayList tempList = null;
            try{
            	tempList = (ArrayList)levelList.get(thisLevel);
            	tempList.add(node);
            }catch(IndexOutOfBoundsException ex){
            	tempList = new ArrayList();
            	tempList.add(node);
            	levelList.add(tempList);
            }
		}
	}
	
	public ArrayList setExecSchedule(ScheduleNode node)throws Exception{
		setExecSchedule(node.getPersistInfo().getObjectIdentifier().toString());
		return trueList;
	}
	
	public void setExecSchedule(String oid)throws Exception{
		if(checkExecSchedule(oid)){
			commit();
		}
	}
	

	public void setPlanSchedule()throws Exception{
		setPlanSchedule(null);
	}
	
	public ArrayList setPlanSchedule(Timestamp startDate)throws Exception{
		if(checkPlanSchedule( startDate)){
			commit();
		}
		return trueList;
	}
	
	public void setCompletion()throws Exception{
		if(checkCompletion()){
			commit();
		}
	}
	
	public void setStartDate()throws Exception{
		if(checkStartDate()){
			commit();
		}
	}
	
	/**태스크 완료시
	 * @throws Exception
	 */
	public void setComplete() throws Exception{
		checkComplete();
		checkCompletion(); 	
		commit();		
	}
	
	/**하위 태스크 여부 및 상태확인 후 상위 태스크 및 프로젝트 Complate 설정
	 * @return
	 * @throws Exception
	 */
	public boolean checkComplete() throws Exception{
		
		boolean result = false;
		for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode treeNode = (ProjectTreeNode)tlist.get(j);
				
				if(treeNode.getChildCount()==0)continue;
				
				ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
				
				Enumeration en = treeNode.children();

				boolean isComplete = true;
				Timestamp endDate = null;
				while(en.hasMoreElements()){
				    ProjectTreeNode cc = (ProjectTreeNode)en.nextElement();
				    ScheduleNode cnode = (ScheduleNode)cc.getUserObject();
				    
				    String cstate = "";
					if(cnode instanceof ETaskNode){
						ETask tasknode = (ETask)cnode;
						cstate = tasknode.getStatus();
					}
					if(cnode instanceof EProject){
						EProject pjtnode = (EProject)cnode;
						cstate = pjtnode.getState().toString();
					}
					
				    if(!STATEKEY.COMPLETED.equals(cstate)){
				    	isComplete = false;
				    	break;
				    }
				    
				    Timestamp ed = cnode.getEndDate();
				    if(endDate==null){
				    	endDate = ed;
				    }else if(ed!=null && endDate.getTime() < ed.getTime()){
				    	endDate = ed;
				    }
				}
				if(isComplete){
					node.setCompletion(100);
					node.setEndDate(endDate);
					//node.setStatus(STATEKEY.COMPLETED);
					if(node instanceof ETaskNode){
						((ETaskNode) node).setStatus(STATEKEY.COMPLETED);
					}
					if((node instanceof EProject)||(node instanceof EProjectNode)){
						isPjtComplate = true;
					}
				}
				
				if(treeNode.checkEdit()){
					treeNode.editFlag = true;
					result = true;
				}
			}
		}
		return result;
	}
	
	public boolean checkStartDate() throws Exception{
		
		boolean result = false;
		
		for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode treeNode = (ProjectTreeNode)tlist.get(j);
				
				if(treeNode.getChildCount()==0)continue;
				
				ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
				
				Enumeration en = treeNode.children();

				Timestamp first = null;
				
				while(en.hasMoreElements()){
				    ProjectTreeNode cc = (ProjectTreeNode)en.nextElement();
				    ScheduleNode cnode = (ScheduleNode)cc.getUserObject();

				    Timestamp start = cnode.getStartDate();
				    if(start==null)continue;
				    if(first==null || (first.getTime() > start.getTime())){
				    	first = start;
				    }
				}
				
				node.setStartDate(first);
				
				if(treeNode.checkEdit()){
					treeNode.editFlag = true;
					result = true;
				}
			}
		}
		return result;
	}
	
	
	/**수정 여부
	 * @return
	 * @throws Exception
	 */
	public boolean checkCompletion() throws Exception{
		
		boolean result = false;
		
		for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode treeNode = (ProjectTreeNode)tlist.get(j);
				ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
				
				Enumeration en = treeNode.children();
				int totalDuration=0;
				float totalCompDay=0f;
				while(en.hasMoreElements()){
				    ProjectTreeNode cc = (ProjectTreeNode)en.nextElement();
				    ScheduleNode cnode = (ScheduleNode)cc.getUserObject();

				    ProjectNodeData data = new ProjectNodeData(cnode);
				    int duration = data.getPlanDuration();
				    totalDuration += duration;
				    double comp = cnode.getCompletion();
				    totalCompDay += (duration * comp);
				}
				
				if(totalDuration==0)continue;
				
				float completion = 0;

				if (totalCompDay > 0) {
					completion = (totalCompDay) / totalDuration;
				}
				node.setCompletion(Math.round ((float)completion)) ;
				if(node.getCompletion()!=treeNode.orgCompletion){
					treeNode.editFlag = true;
					result = true;
				}
			}
		}
		return result;
	}
	
	public void initAllPlanDate(Timestamp startDate)throws Exception{
		
		if(startDate==null)startDate = START_DATE;
    	
		for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				
				ScheduleNode obj = (ScheduleNode)node.getUserObject();
				
				BigDecimal bd = new BigDecimal(obj.getManDay());
		    	bd = bd.setScale(0, BigDecimal.ROUND_UP);
	    	    int	duration = bd.intValue();
				
	    	    Calendar startCa = Calendar.getInstance();
	    	    Calendar endCa = Calendar.getInstance();
	    	    
	    	    //Start Date Holiday Skip
	    	    startCa.setTimeInMillis(startDate.getTime());
	    	    startCa = ProjectUtil.setHolidaySkipCalendar(startCa);
				obj.setPlanStartDate(new Timestamp(startCa.getTime().getTime()));
				
				//End Date Holiday Skip
				Timestamp newEndDate = ProjectUtil.setPeriodHoliday(obj.getPlanStartDate(), duration,isTemplate);
				obj.setPlanEndDate(newEndDate);
				
			}
    	}
    }


	
	public void setParentDate()throws Exception{
    	
		for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				
				ScheduleNode obj = (ScheduleNode)node.getUserObject();
				Timestamp start = null;
				Timestamp end   = null;
				
				boolean editFlag = false;
				
				for(int k=0; k<node.getChildCount(); k++){
					ProjectTreeNode child = (ProjectTreeNode)node.getChildAt(k);
					
					
					ScheduleNode cobj = (ScheduleNode)child.getUserObject();
					Timestamp cstart = cobj.getPlanStartDate();
					Timestamp cend   = cobj.getPlanEndDate();
					
					
					
					
					if( start==null || (start.getTime() > cstart.getTime())){
						start = cstart;
						editFlag = true;
					}
					
					if( end==null || (end.getTime() < cend.getTime())){
						end = cend;
						editFlag = true;
					}
					
				}
				
				if(editFlag){
					obj.setPlanStartDate(start);
					obj.setPlanEndDate(end);
				}
				
			}
		}
    }
	
	public void getPreTask()throws Exception{
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				
				node.setPreTaskId();
				
				for(int k=0; k< node.preTask.size(); k++){
					String preOid = (String)node.preTask.get(k);
					ArrayList postList = (ArrayList)postMap.get(preOid);
					if(postList==null)postList = new ArrayList();
					postList.add(node);
					postMap.put(preOid,postList);
				}
			}
		}
    }
	
	public void setManDay()throws Exception{
		
		for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode treeNode = (ProjectTreeNode)tlist.get(j);
				ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
				
				Enumeration en = treeNode.children();
				
				if(treeNode.getChildCount()>0){
				
					double totalManDay = 0;
					while(en.hasMoreElements()){
					    ProjectTreeNode cc = (ProjectTreeNode)en.nextElement();
					    ScheduleNode cnode = (ScheduleNode)cc.getUserObject();
					    totalManDay += cnode.getManDay();
					}
					
					node.setManDay(totalManDay);
				}
				
			}
		}
	}
	
	
	/**
	 *
	 *  
	 *  
	 * @throws Exception
	 */
	public void setPreTaskDate()throws Exception{
		
		Enumeration en = postMap.keys();
		
		while(en.hasMoreElements()){
			String key = (String)en.nextElement();
			ProjectTreeNode preTreeNode = (ProjectTreeNode)nodeMap.get(key);
			
			if(preTreeNode.preTask.size()==0){ 
			
				setPostTreeNode(preTreeNode);
			}
		}
	}
	
	public void setPostTreeNode(ProjectTreeNode treeNode)throws Exception{
			
			ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
			String key = node.getPersistInfo().getObjectIdentifier().toString();
			
			ArrayList postList = (ArrayList)postMap.get(key);
			
			if(postList==null || postList.size()==0) return;
			
			Timestamp send = node.getPlanEndDate();
			
			ArrayList nextList = new ArrayList();
			
			for(int i=0 ; postList!=null && i < postList.size(); i++){
				ProjectTreeNode postNode = (ProjectTreeNode)postList.get(i);
				
				ScheduleNode obj = (ScheduleNode)postNode.getUserObject();
				
				
				Calendar cc = Calendar.getInstance();
				cc.setTimeInMillis(send.getTime());
				cc.add(Calendar.DATE,1);
				
				Timestamp newStartdate = new Timestamp(cc.getTime().getTime());
				newStartdate = ProjectUtil.setHolidaySkipTimeStamp(newStartdate,isTemplate);
				
				if(obj.getPlanStartDate().getTime() <= cc.getTime().getTime()){
					
					int term = ProjectUtil.getDurationHoliday(obj.getPlanStartDate(), obj.getPlanEndDate());//DateUtil.get(new Date(obj.getPlanStartDate().getTime()),new Date(obj.getPlanEndDate().getTime()));
					//int term=DateUtil.getDuration(new Date(obj.getPlanStartDate().getTime()),new Date(obj.getPlanEndDate().getTime()));
					Timestamp newEndDate = ProjectUtil.setPeriodHoliday(newStartdate, term,isTemplate);
					//cc.add(Calendar.DATE, term);
					//Timestamp newEndDate = new Timestamp(cc.getTime().getTime()); 
					obj.setPlanStartDate(newStartdate);
					obj.setPlanEndDate(newEndDate);
					
					nextList.add(postNode);
				}
			}
			
			for(int i=0; i< nextList.size(); i++){
				ProjectTreeNode nextNode = (ProjectTreeNode)nextList.get(i);
				setPostTreeNode(nextNode);
			}
	}
	
	
	public void setStartDate(Timestamp startDate)throws Exception{
		
		int start = DateUtil.getDuration(START_DATE,startDate);
		
		for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				
				ScheduleNode ScheduleNode = (ScheduleNode)node.getUserObject();
				
				Calendar ca = Calendar.getInstance();
				ca.setTime(ScheduleNode.getPlanStartDate());
				ca.add(Calendar.DATE, start);
				ScheduleNode.setPlanStartDate(new Timestamp(ca.getTime().getTime()));
				
				ca.setTime(ScheduleNode.getPlanEndDate());
				ca.add(Calendar.DATE, start);
				ScheduleNode.setPlanEndDate(new Timestamp(ca.getTime().getTime()));
			}
		}
	}
	
	private boolean checkExecSchedule(String oid)throws Exception{
    	
    	boolean result = false;
    	
    	ProjectTreeNode treeNode = (ProjectTreeNode)nodeMap.get(oid);
    	getPreTask();	
    	setPostTreeNode(treeNode);	
    	setParentDate();
    	setManDay();	
    	checkComplete();
		checkCompletion();
		
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				if(!node.editFlag){
					node.editFlag = node.checkPlanEdit();
					if(node.editFlag){
						result = true;
					}
				}else{
					result = true;
				}
			}
    	}
    	
    	return result;
    }

	private boolean checkPlanSchedule(Timestamp startDate)throws Exception{
    	
    	boolean result = false;
    
    	initAllPlanDate(startDate);	//공수 대비 일정 
    	
    	getPreTask();	 	//선행 테스크 수집
    	
    	setPreTaskDate();	//선행 테스크 일정 편집
    	
    	setParentDate();	//상위 테스크 일정 편집
    	
    	setManDay();		//공수 산정
    	
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				node.editFlag = node.checkPlanEdit();
				if(node.editFlag){
					result = true;
				}
				
			}
    	}
    	
    	return result;
    }
    
    /**
     * 
     * 
     * @return
     */
    private ArrayList getNonPreLastNodeList(){
    	ArrayList list = new ArrayList();
    	
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode treeNode = (ProjectTreeNode)tlist.get(j);
				
				if(treeNode.getChildCount()>0){
					continue;
				}
				if(treeNode.preTask.size()>0){
					continue;
				}		
				list.add(treeNode);
			}
    	}
    	return list;
    }

    

    private void setHoliday(ProjectTreeNode treeNode)throws Exception{
    	ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
		String key = node.getPersistInfo().getObjectIdentifier().toString();
		
		Timestamp send = node.getPlanEndDate();
		
		ArrayList postList = (ArrayList)postMap.get(key);
		
		for(int i=0 ; postList!=null && i < postList.size(); i++){
			ProjectTreeNode postNode = (ProjectTreeNode)postList.get(i);

			ScheduleNode obj = (ScheduleNode)postNode.getUserObject();
				
			int term = ProjectUtil.getDurationWithoutHoliday(obj.getPlanStartDate(),obj.getPlanEndDate());
			
			Calendar cc = Calendar.getInstance();
			cc.setTimeInMillis(send.getTime());
			cc.add(Calendar.DATE,1);
			obj.setPlanStartDate(new Timestamp(cc.getTime().getTime()));
				
			cc.add(Calendar.DATE, term);
			obj.setPlanEndDate(new Timestamp(cc.getTime().getTime()));
			
			setHolidayNode(postNode);
			removeHolidayNode(postNode);
		}
    }
    
    private void removeHolidayNode(ProjectTreeNode treeNode)throws Exception{
		
		ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
		String key = node.getPersistInfo().getObjectIdentifier().toString();
		
		removeHoliday(treeNode);
		
		ArrayList postList = (ArrayList)postMap.get(key);
		
		for(int i=0 ; postList!=null && i < postList.size(); i++){
			ProjectTreeNode postNode = (ProjectTreeNode)postList.get(i);
			removeHolidayNode(postNode);
		}
	
	}

    /**
     * 
     * 
     * @throws Exception
     */
    private void setHoliday()throws Exception{
    	
    	ArrayList list = getNonPreLastNodeList();
		for(int i=0; i< list.size(); i++){
			ProjectTreeNode preTreeNode = (ProjectTreeNode)list.get(i);
			setHolidayNode(preTreeNode);
		}
		
		removeHoliday();
    }
    
    private void removeHoliday()throws Exception{
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode treeNode = (ProjectTreeNode)tlist.get(j);
				
				removeHoliday(treeNode);
			}
		}
    }
    
    private void removeHoliday(ProjectTreeNode treeNode)throws Exception{
    	ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
    	Timestamp start = node.getPlanStartDate();
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date(start.getTime()));
		
		while(ProjectUtil.isHoliday(ca)){
			ca.add(Calendar.DATE,1);
		}
		node.setPlanStartDate(new Timestamp(ca.getTimeInMillis()));
		
		
		Timestamp end = node.getPlanEndDate();
		ca.setTime(new Date(end.getTime()));
		while(ProjectUtil.isHoliday(ca)){
			ca.add(Calendar.DATE,-1);
		}
		node.setPlanEndDate(new Timestamp(ca.getTimeInMillis()));
    }
    
    /**
     * 
     * @param treeNode
     * @throws Exception
     */
    
    private void setHolidayNode(ProjectTreeNode treeNode)throws Exception{
		
		ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
		String key = node.getPersistInfo().getObjectIdentifier().toString();
		
		Timestamp pstart = node.getPlanStartDate();
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date(pstart.getTime()));
		
		//주말 주석처리
		/*for(int duration = 0;duration < node.getManDay();duration++){
			
			if(ProjectUtil.isHoliday(ca)){
				duration--;
			}
			
			ca.add(Calendar.DATE,1);
		}
		ca.add(Calendar.DATE,-1);
		*/
		
		node.setPlanEndDate(new Timestamp(ca.getTime().getTime()));
		
		Timestamp send = node.getPlanEndDate();
		
		ArrayList postList = (ArrayList)postMap.get(key);
		
		ArrayList nextList = new ArrayList();
		
		for(int i=0 ; postList!=null && i < postList.size(); i++){
			ProjectTreeNode postNode = (ProjectTreeNode)postList.get(i);

			ScheduleNode obj = (ScheduleNode)postNode.getUserObject();
			
			int term = ProjectUtil.getDurationWithoutHoliday(obj.getPlanStartDate(),obj.getPlanEndDate());
			
			Calendar cc = Calendar.getInstance();
			cc.setTimeInMillis(send.getTime());
			
			if(cc.getTime().getTime() > obj.getPlanStartDate().getTime()){
				
				cc.add(Calendar.DATE,1);
				obj.setPlanStartDate(new Timestamp(cc.getTime().getTime()));
	
				cc.add(Calendar.DATE, term);
				obj.setPlanEndDate(new Timestamp(cc.getTime().getTime()));
				
				nextList.add(postNode);
			}
		}
		
		for(int i=0; i< nextList.size(); i++){
			ProjectTreeNode postNode = (ProjectTreeNode)nextList.get(i);
			setHolidayNode(postNode);
		}
	}
    
    
    /**후행 태스크 시작 이벤트
     * @throws Exception
     */
    public void start()throws Exception{
    	
    	allEditFlagFalse();
    	
    	checkPrestart(root);
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				
				ProjectTreeNode treeNode = (ProjectTreeNode)tlist.get(j);
				if(!treeNode.editFlag)continue;
				if(treeNode.getChildCount()==0)continue;
				
				boolean flag = false;
				
				Enumeration en = treeNode.children();
				while(en.hasMoreElements()){
				    ProjectTreeNode cc = (ProjectTreeNode)en.nextElement();
				    if(cc.editFlag){
				    	flag = true;
				    	break;
				    }
				    
				    ScheduleNode cnode = (ScheduleNode)cc.getUserObject();
				    
				    String state = "";
					if(cnode instanceof ETaskNode){
						ETask tasknode = (ETask)cnode;
						state = tasknode.getStatus();
					}
					if(cnode instanceof EProject){
						EProject pjtnode = (EProject)cnode;
						state = pjtnode.getState().toString();
					}
				    
 				    if(STATEKEY.PROGRESS.equals(state)){
				    	flag = true;
				    	break;
				    }
				    if(STATEKEY.COMPLETED.equals(state)){
				    	flag = true;
				    	break;
				    }
				}
				if(!flag){
					treeNode.editFlag = false;
				}
			}
    	}
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				if(node.editFlag && node.orgStartDate!=null){
					System.out.println("node.orgStartDate : " + node.orgStartDate);
					startNode(node);
				}
			}
    	}
    	
    	//
    }
    
    /**
     * 전체 태스크의 수정상태여부를 false로 변경
     */
    public void allEditFlagFalse(){
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				node.editFlag = false;
			}
    	}
    }
    
    /**태스크 시작 이벤트
     * @param node
     * @return
     * @throws Exception
     */
    private ScheduleNode startNode(ProjectTreeNode node)throws Exception{
    	
    	ScheduleNode obj = (ScheduleNode)node.getUserObject();
		
    	//obj.setStatus(STATEKEY.PROGRESS);
    	if(obj instanceof ETaskNode){
			((ETaskNode) obj).setStatus(STATEKEY.PROGRESS);
		}
		if(obj instanceof EProject){
			//LifeCycleUtil.setLifeCycleState((EProject)obj, STATEKEY.PROGRESS);
			E3PSWorkflowHelper.manager.changeLCState((EProject)obj, STATEKEY.PROGRESS);
		}
		obj.setStartDate(DateUtil.getCurrentTimestamp());
		obj = (ScheduleNode)PersistenceHelper.manager.save(obj);
		node.setUserObject(obj);
		
		if((obj instanceof ETaskNode) && (node.getChildCount()==0)){
			//ProjectMailBrokerHelper broker = new ProjectMailBrokerHelper();
			//broker.taskStart((ETaskNode)obj);
			ProjectMailBroker.manager.taskStart((ETaskNode)obj);
		}
		return obj;
    }
    /**후행 태스크
     * @param treeNode
     * @throws Exception
     */
    private void checkPrestart(ProjectTreeNode treeNode)throws Exception{
    	ScheduleNode node = (ScheduleNode)treeNode.getUserObject();
    	String state = "";
    	if(node instanceof ETaskNode){
			ETask tasknode = (ETask)node;
			state = tasknode.getStatus();
		}
		if(node instanceof EProject){
			EProject pjtnode = (EProject)node;
			state = pjtnode.getState().toString();
		}
		
    	if(STATEKEY.COMPLETED.equals(state))return;
    	//if(!ProjectHelper.service.isPreComplete(node))return;
    	if(STATEKEY.READY.equals(state)){
    		String planStartDate = DateUtil.getDateString(node.getPlanStartDate(),"d");
    		String today = DateUtil.getCurrentDateString("d");
    		if(today.compareTo(planStartDate)>=0 || node.getStartDate() != null){
    			treeNode.editFlag = true;
    		}
    	}
    	
	    Enumeration en = treeNode.children();
	    while(en.hasMoreElements()){
	    	ProjectTreeNode cc = (ProjectTreeNode)en.nextElement();
	    	checkPrestart(cc);
	    }
    }
    
    /**수정한 내용 저장 로직
     * @throws Exception
     */
    public void commit()throws Exception{
    	for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				if(node.editFlag){
					ScheduleNode obj = (ScheduleNode)node.getUserObject();
					if(obj != null){
						trueList.add(obj); //add dhtmlx
					
						Timestamp st = obj.getPlanStartDate();
						Timestamp ed = obj.getPlanEndDate();
						
						if (st.after(ed)){
							obj.setPlanEndDate(st);
			  	    	}
					}
					node.setUserObject(PersistenceHelper.manager.save(obj));
					if(isPjtComplate && obj instanceof EProject){
						EProject pjt = (EProject)obj;
						E3PSWorkflowHelper.manager.changeLCState(pjt, STATEKEY.COMPLETED);
						//LifeCycleUtil.setLifeCycleState(pjt, STATEKEY.COMPLETED);
					}
				}
			}
    	}
    }
	
	public void printTree(){
		for(int i=levelList.size()-1; i>=0; i--){
			ArrayList tlist = (ArrayList)levelList.get(i);
			
			for(int j=0; j< tlist.size(); j++){
				ProjectTreeNode node = (ProjectTreeNode)tlist.get(j);
				ScheduleNode nn = (ScheduleNode)node.getUserObject();
			}
		}
	}
	
	public ProjectTreeNode getRoot(){
		return root;
	}
	
	
}

