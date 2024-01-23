package com.e3ps.project.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.ProjectRegistApproval;
import com.e3ps.project.ProjectRegistLink;
import com.e3ps.project.ProjectRole;
import com.e3ps.project.RoleUserLink;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.TaskRoleLink;
import com.e3ps.project.key.ProjectKey.STATEKEY;
import com.e3ps.project.service.ProjectMemberHelper;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.query.QuerySpec;
import wt.query.SearchCondition;


public class ProjectNodeData{
	
	public ScheduleNode node;
	public EProject pjtnode;
	public ETask tasknode;
	ProjectRegistApproval startApproval;
	
	public static final int STATE_BAR_COMPLATE = 1;
	public static final int STATE_BAR_NORMAL = 3;
	public static final int STATE_BAR_EXDELAY = 4;
	public static final int STATE_BAR_DELAY = 5;
	
	/**
	 * 로그인 유저의 pm 여부 확인
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean isManager(WTUser user)throws Exception{
		if(node != null){
			return ProjectMemberHelper.manager.isPM(node,user);
		}
		if(pjtnode != null){
			return ProjectMemberHelper.manager.isPM(pjtnode,user);
		}
		return false;
	}
	
	/**is Owner
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean isOwner(WTUser user)throws Exception{
		if(node instanceof ETaskNode){
			ArrayList list = ProjectMemberHelper.service.getOwner((ETaskNode)node);
			for(int i=0; i< list.size(); i++){
				Object[] o = (Object[])list.get(i);
				RoleUserLink link = (RoleUserLink)o[1];
				
				if(link==null) continue;
				
				WTUser uu = link.getUser();
				
				if(uu.getName().equals(user.getName())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**수정 가능 여부
	 * @return
	 * @throws Exception
	 */
	public boolean isEditable()throws Exception{
		
		if(node instanceof ETaskNode){
			return isTaskEditable();
		}
		return isProjectEditable();
	}
	
	/**프로젝트 수정 가능 여부
	 * @return
	 */
	private boolean isProjectEditable(){
		String state = "";
		if(node instanceof ETaskNode){
			tasknode = (ETask)node;
			state = tasknode.getStatus();
		}
		if(node instanceof EProject){
			pjtnode = (EProject)node;
			state = pjtnode.getState().toString();
		}
		
		if(STATEKEY.COMPLETED.equals(state) || STATEKEY.STOP.equals(state)){
			return false;
		}
		
		return ((EProjectNode)node).isLastVersion();
	}
	
	/**태스크 수정 가능 여부
	 * @return
	 */
	private boolean isTaskEditable(){
		
		ETaskNode task = (ETaskNode)node;
		EProject project = (EProject) task.getProject();
		
		if(!project.isLastVersion()){
			return false;
		}
		String pstate = project.getState().toString();
		
		if(!STATEKEY.PROGRESS.equals(pstate)){
			return false;
		}
		
		String state = task.getStatus();
		
		if(STATEKEY.COMPLETED.equals(state)){
			return false;
		}
		
		return true;
	}
	
	public boolean isTaskManagerModify(){
		
		ETaskNode task = (ETaskNode)node;
		EProject project = (EProject) task.getProject();
		
		if(!project.isLastVersion()){
			return false;
		}
		String pstate = project.getState().toString();
		
		if(!(STATEKEY.READY.equals(pstate) || STATEKEY.MODIFY.equals(pstate))){
			return false;
		}
		
		String state = task.getStatus();
		
		if(STATEKEY.COMPLETED.equals(state)){
			return false;
		}
		
		return true;
	}

	public ProjectNodeData(ScheduleNode node){
		this.node = node;
	}

	public int getDuration(){
		java.sql.Timestamp st = node.getStartDate();
		java.sql.Timestamp ed = node.getEndDate();
		return DateUtil.getDuration(new Date(st.getTime()),new Date(ed.getTime()))+1;
	}
	public int getDurationHoliday(){
		java.sql.Timestamp st = node.getStartDate();
		java.sql.Timestamp ed = node.getEndDate();
		int duration = DateUtil.getDuration(new Date(st.getTime()),new Date(ed.getTime()))+1;
		int holiday = 0;
		try {
			holiday = ProjectUtil.getDurationWithoutHoliday(st, ed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration - holiday;
	}
	
	public int getPlanDuration(){
		java.sql.Timestamp st = node.getPlanStartDate();
		java.sql.Timestamp ed = node.getPlanEndDate();
		return DateUtil.getDuration(new Date(st.getTime()),new Date(ed.getTime()))+1;
	}
	
	public int getPlanDurationHoliday(){
		java.sql.Timestamp st = node.getPlanStartDate();
		java.sql.Timestamp ed = node.getPlanEndDate();
		int duration = DateUtil.getDuration(new Date(st.getTime()),new Date(ed.getTime()))+1;
		int holiday = 0;
		try {
			holiday = ProjectUtil.getDurationWithoutHoliday(st, ed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration - holiday;
	}
	
	public int getDuration(Timestamp start, Timestamp end){
		return DateUtil.getDuration(new Date(start.getTime()),new Date(end.getTime()))+1;
	}
	public int getDurationHoliday(Timestamp start, Timestamp end){
		int duration = DateUtil.getDuration(new Date(start.getTime()),new Date(end.getTime()))+1;
		int holiday = 0;
		try {
			holiday = ProjectUtil.getDurationWithoutHoliday(start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration - holiday;
	}
	/**
	 * Node의 상태 가져오기.
	 * @return
	 * @throws Exception
	 */
	public String getState() throws Exception{
	
		String ss = "";
		if(node instanceof ETaskNode){
			tasknode = (ETask)node;
			ss = tasknode.getStatus();
		}
		if(node instanceof EProject){
			pjtnode = (EProject)node;
			ss = pjtnode.getState().toString();
		}
		
		if(STATEKEY.COMPLETED.equals(ss)){
			return STATEKEY.COMPLETED_KO;
		}else if(STATEKEY.PROGRESS.equals(ss)){
			int delay = isDelay();
        	if(STATE_BAR_EXDELAY == delay){
        		return STATEKEY.PROGRESS_KO;
        	}else if(STATE_BAR_DELAY == delay){
        		return STATEKEY.DELAY_KO;
        	}else{
        		return STATEKEY.PROGRESS_KO;
        	}
        	
		}else if(STATEKEY.READY.equals(ss)){

			ProjectRegistApproval approval = getStartLifeCycle();
			if(approval!=null){
				String astate = approval.getLifeCycleState().toString();
				
				if("APPROVEING".equals(astate) || STATEKEY.APPROVING.equals(astate)){
					return STATEKEY.APPROVING_KO;
				}
			}
			return STATEKEY.READY_KO;
		}else if(STATEKEY.SIGN.equals(ss)){
			return STATEKEY.SIGN_KO;
		}else if(STATEKEY.STOP.equals(ss)){
			return STATEKEY.STOP_KO;
		}else if(STATEKEY.MODIFY.equals(ss)){
			return STATEKEY.MODIFY_KO;
		}else if(STATEKEY.CANCELLED.equals(ss)){
			return STATEKEY.CANCELLED_KO;
		}else if(STATEKEY.INWORK.equals(ss)){
			return STATEKEY.INWORK_KO;
		}
		return ss;
	}
	
	/**태스크 상태 가져오기
	 * @return
	 * @throws Exception
	 */
	public String getStateTag()throws Exception{
		String ss = "";
		String barColor = "green";
		String title  = getState();
		
		if(node instanceof ETaskNode){
			tasknode = (ETask)node;
			ss = tasknode.getStatus();
		}
		if(node instanceof EProject){
			pjtnode = (EProject)node;
			ss = pjtnode.getState().toString();
		}
		
		if(  STATEKEY.COMPLETED.equals(ss )) {
			barColor = "green";
		}else if( STATEKEY.READY.equals(ss ) || STATEKEY.SIGN.equals(ss ) || STATEKEY.INWORK.equals(ss )){
			barColor = "blank";
		}else if( STATEKEY.PROGRESS.equals(ss )){
			
			int delay = isDelay();
        	if(STATE_BAR_EXDELAY == delay){
        		barColor = "yellow";
        	}else if(STATE_BAR_DELAY == delay){
        		barColor = "red";
        	}else{
        		barColor = "blue";
        	}
		}
		StringBuffer result = new StringBuffer();
		
//		result.append("<table>")
//		.append("<tr title="+title+">")
//		.append("<td>")
		result.append("<img src='/Windchill/jsp/project/images/project/state_blank_bar.gif'>")
//		.append("</td><td>")
		.append("<img src='/Windchill/jsp/project/images/project/state_"+barColor+"_bar.gif'>")
//		.append("</td><td>")
		.append("<img src='/Windchill/jsp/project/images/project/state_blank_bar.gif'>")
//		.append("</td>")
//		.append("<td>")
		.append(" "+title+"");
//		.append("</td>")
//		.append("</tr>")
//		.append("</table>");
		
		return result.toString();
	}
	
	
	public String getRoleName(){
		try{
			if(node instanceof ETaskNode){
				QueryResult qr = PersistenceHelper.manager.navigate(node, "role", TaskRoleLink.class);
				String roleName = "";
				while(qr.hasMoreElements()){
					ProjectRole role = (ProjectRole)qr.nextElement();
					roleName += role.getName()+",";
				}
				if(roleName!=null&& roleName.length()>0)return roleName.substring(0,roleName.length()-1);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "&nbsp;";
	}
	
	/**
	 * 지연 여부
	 * @return
	 */
	public int isDelay(){
		int state = 3;
		try {
			double comp = getPreferComp();
			double prog = node.getCompletion();
			double preferComplection = (prog / comp) * 100d;
			
			if(comp == 0){
				return STATE_BAR_NORMAL;
			}
			Calendar currentDate = Calendar.getInstance();
			if(node.getPlanEndDate().getTime() < currentDate.getTime().getTime()){
				return STATE_BAR_DELAY;
			}
			if(preferComplection > 50){
				state = STATE_BAR_NORMAL;
			}else if(preferComplection > 30){
				state = STATE_BAR_EXDELAY;
			}else{
				state = STATE_BAR_DELAY;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
		/*String ss = "";
		
		if(node instanceof ETaskNode){
			tasknode = (ETask)node;
			ss = tasknode.getStatus();
		}
		if(node instanceof EProject){
			pjtnode = (EProject)node;
			ss = pjtnode.getState().toString();
		}
		
		if(!STATEKEY.PROGRESS.equals(ss))return false;
		
		String today = DateUtil.getCurrentDateString("d");
    	String planEnd = DateUtil.getDateString(node.getPlanEndDate(),"d");
    	
    	if(today.compareTo(planEnd)>0){
    		return true;
    	}*/
		
    	//===== 수정 중 ===
//    	double totalDuration = 0;  
//		double currentDuration = 0; 
//		boolean isDelay = false;
//		int delayState = 0;
//    	Calendar currentDate = Calendar.getInstance();
//    	long time = node.getPlanStartDate().getTime() - currentDate.getTime().getTime();
//    	long duration = (node.getPlanEndDate().getTime() + 0x5265c00L) - node.getPlanStartDate().getTime();
//		long currentGap = 0;
//    	
//		if(time < 0){
//			
//			long etime = currentDate.getTime().getTime() - (node.getPlanEndDate().getTime() + 0x5265c00L);
//			
//			if(etime >= 0){
//				currentGap = duration;
//			}else{
//				currentGap = currentDate.getTime().getTime() - node.getPlanStartDate().getTime();
//			}
//		}
//		
//		currentDuration += currentGap;
//		totalDuration += duration;
//		
//		int childTaskState = getStateBarType(node);
//		
//		if(childTaskState > delayState){
//			delayState = childTaskState;
//		}
//		
//		NumberFormat nf = NumberFormat.getInstance();
//	    nf.setMaximumFractionDigits(2);
//		
//	    double preferComplection = (currentDuration / totalDuration) * 100d;
//		
//	    if(isDelay){
//			// 현재 사용 안함
//			double currentComplection = node.getCompletion();
//			
//			double gap = (double)currentComplection - preferComplection;
//			
//			int pjtState = getDelayType(node, gap);
//			
//			if(pjtState == STATE_BAR_NORMAL){
//		    	return STATE_BAR_NORMAL;
//		    } else if(pjtState == STATE_BAR_DELAY) {
//		    	return STATE_BAR_DELAY;
//		    } else if(pjtState == STATE_BAR_EXDELAY) {
//		    	return STATE_BAR_EXDELAY;
//		    }
//
//		}else{ //이거 탐.
//			if(delayState == STATE_BAR_NORMAL){
//				return STATE_BAR_NORMAL;
//		    } else if(delayState == STATE_BAR_DELAY) {
//		    	return STATE_BAR_DELAY;
//		    } else if(delayState == STATE_BAR_EXDELAY) {
//		    	return STATE_BAR_EXDELAY;
//		    }
//		}
//		return childTaskState;
	}
	
	public String getIconUrl(){
		
		String url = null;
		
		String state = "";
        
		if(node instanceof ETaskNode){
			tasknode = (ETask)node;
			state = tasknode.getStatus();
		}
		if(node instanceof EProject){
			pjtnode = (EProject)node;
			state = pjtnode.getState().toString();
		}
		
        String sufix = "";
        if(STATEKEY.COMPLETED.equals(state)){
        	sufix = "_complete";
        }else if(STATEKEY.PROGRESS.equals(state)){
        	int delay = isDelay();
        	if(STATE_BAR_EXDELAY == delay){
        		sufix = "_orange";
        	}else if(STATE_BAR_DELAY == delay){
        		sufix = "_red";
        	}else{
        		sufix = "_progress";
        	}
        }else{
        	sufix = "_ready";
        }
        
        if(node instanceof EProjectNode){
        	//url = "/Windchill/jsp/project/images/tree/base.gif";
        	url = "/Windchill/jsp/project/images/tree/task"+sufix+".gif";
        }
        else{
        	url = "/Windchill/jsp/project/images/tree/task"+sufix+".gif";
        }
        return url;
	}
	
	/**PM을 가져온다.
	 * @return
	 * @throws Exception
	 */
	public WTUser getPM() throws Exception{
		
		Object[] o = ProjectMemberHelper.service.getPM(node);
		
		if(o==null)return null;
		
		RoleUserLink link = (RoleUserLink)o[1];
		
		if(link != null ){
		
			return link.getUser();
		}
		return null;
	}
	
	public double getPreferComp()throws Exception{
		double preferComp = 0;

		Timestamp today = DateUtil.getCurrentTimestamp();
		String cdate = DateUtil.getDateString(today,"d");
		String sdate = DateUtil.getDateString(node.getPlanStartDate(),"d");
		String edate = DateUtil.getDateString(node.getPlanEndDate(),"d");

		if(cdate.compareTo(sdate) < 0){
			preferComp = 0;
		}else if(cdate.compareTo(edate) >= 0){
			preferComp = 100;
		}else{
			//double du =DateUtil.getDuration(today,node.getPlanStartDate())+1;
			double du = getDurationHoliday(node.getPlanStartDate(),today);
			double planDuration = getPlanDurationHoliday();
			preferComp = du/planDuration * 100;
		}

		return preferComp;
	}
	
	/** 프로젝트 결제객체 가져오기
	 * @return
	 * @throws Exception
	 */
	public ProjectRegistApproval getStartLifeCycle()throws Exception{
		if(startApproval==null && (node instanceof EProject)){
			QueryResult qr = PersistenceHelper.manager.navigate(node,"regist",ProjectRegistLink.class);
			if(qr.hasMoreElements()){
				startApproval = (ProjectRegistApproval)qr.nextElement();
			}
		}else if(startApproval==null && pjtnode != null){
			QueryResult qr = PersistenceHelper.manager.navigate(pjtnode,"regist",ProjectRegistLink.class);
			if(qr.hasMoreElements()){
				startApproval = (ProjectRegistApproval)qr.nextElement();
			}
		}
		return startApproval;
	}
		
	/**
	 * 프로젝트 결재 테이블 상태 확인
	 * @return
	 * @throws Exception
	 */
	public boolean isStartLifeCycleLock()throws Exception{
		ProjectRegistApproval approval = getStartLifeCycle();
		if(approval!=null){
			if("APPROVING".equals(approval.getLifeCycleState().toString())){
				return false;
			}
			if("INWORK".equals(approval.getLifeCycleState().toString())){
				return false;
			}
			if("RETURN".equals(approval.getLifeCycleState().toString())){
				return false;
			}
			if("APPROVED".equals(approval.getLifeCycleState().toString())){
				return true;
			}
			if("SIGN".equals(approval.getLifeCycleState().toString())){
				return false;
			}
		}
		return false;
	}
	
	/**산출물 수정여부 및 담당자 수정여부
	 * @return
	 */
	public boolean isOutputEditable(){

		ETaskNode task = (ETaskNode)node;
		EProject project = (EProject) task.getProject();
		
		if(!project.isLastVersion()){
			return false;
		}
		String pstate = project.getState().toString();
		
		if(STATEKEY.SIGN.equals(pstate)){
			return false;
		}else if(STATEKEY.STOP.equals(pstate)){
			return false;
		}
		
		String state = task.getStatus();
		
		if(STATEKEY.COMPLETED.equals(state)){
			return false;
		}
		
		return true;
	}
	
	/**하위 태스크 가져오기
	 * @return
	 * @throws Exception
	 */
	public Vector getChildTask()throws Exception{
		Vector v = new Vector();
		
		QuerySpec query = new QuerySpec();
		int idx = query.addClassList(ETask.class, true);
		query.appendWhere(new SearchCondition(ETask.class, "parentReference.key.id", SearchCondition.EQUAL, CommonUtil.getOIDLongValue(node)), new int[]{idx});
		
		QueryResult qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()){
			Object o[] = (Object[])qr.nextElement();
			ScheduleNode pre = (ScheduleNode)o[0];
			v.add(pre);
		}
		return v;
	}
	
	/**
	 *  상태에 맞는 int 리턴
	 * @param task
	 * @return
	 */
	public static int getStateBarType(ScheduleNode task){
		
		String state = "";
		
		if(task instanceof ETaskNode){
			ETaskNode tasknode = (ETask)task;
			state = tasknode.getStatus();
		}
		if(task instanceof EProject){
			EProject pjtnode = (EProject)task;
			state = pjtnode.getState().toString();
		}
		
		
		if (STATEKEY.COMPLETED == state){
			return STATE_BAR_COMPLATE;
		}
		
		Calendar currentDate = Calendar.getInstance();
		
		Timestamp taskPlanStartDate = task.getPlanStartDate();
		Timestamp taskPlanEndDate = task.getPlanEndDate();
		
		if(currentDate.getTime().before(taskPlanStartDate)){
			return STATE_BAR_NORMAL;
		}
		
		double differDateGap = getDifferDateGap(task);
		
		if ( differDateGap >= 0) {
			
			return STATE_BAR_NORMAL;
			
		}else{
		
			Calendar today = Calendar.getInstance();
			
			String todayStr = DateUtil.getDateString(today.getTime(), "d");
			
			String planEndDateStr  = DateUtil.getTimeFormat(taskPlanEndDate, "yyyy-MM-dd");
			
			boolean todayBefore = todayStr.compareTo(planEndDateStr) > 0;
			
			if(todayBefore){
				
				return STATE_BAR_DELAY;
				
			}else{
				
				return STATE_BAR_EXDELAY;
			}
		}
	}
	
	/**
	 * Task Gap
	 * @param task
	 * @return
	 */
	public static double getDifferDateGap(ScheduleNode task){
		
		Timestamp taskPlanStartDate = task.getPlanStartDate();
		Timestamp taskPlanEndDate = task.getPlanEndDate();
		
		Calendar currentDate = Calendar.getInstance();
		
		if(currentDate.getTime().before(taskPlanStartDate)){
			return 0;
		}
		
		Calendar startDate = Calendar.getInstance();
	    startDate.setTime(taskPlanStartDate);
	    
	    double compPer = task.getCompletion();
	    
	    int taskDuration = DateUtil.getDuration(taskPlanStartDate, taskPlanEndDate) + 1;//this.schedule.getDuration();
	   
	    double d = compPer/100D;
	    d *= taskDuration * 0x5265c00L;
	    
	    double realLength = startDate.getTimeInMillis() + d;
	    
	    double continueTimeGap = realLength - (double)currentDate.getTime().getTime();
	    
	    double differDateGap = ((double)continueTimeGap) / ((double)0x5265c00L);
	    
	    
		return differDateGap;
	}
	
	/**
	 *  Delay Type
	 * @param task
	 * @param differDateGap
	 * @return
	 */
	private static int getDelayType(ScheduleNode task, double differDateGap){
		
		
		Calendar currentDate = Calendar.getInstance();
		
		Timestamp pjtPlanStartDate = task.getPlanStartDate();
		Timestamp pjtPlanEndDate = task.getPlanEndDate();
		
		if(currentDate.getTime().before(pjtPlanStartDate)){
			return STATE_BAR_NORMAL;
		}
		
		if ( differDateGap >= 0) {	
			return STATE_BAR_NORMAL;
		}else{
			Calendar today = Calendar.getInstance();
			
			String todayStr = DateUtil.getDateString(today.getTime(), "d");
			
			String planEndDateStr  = DateUtil.getTimeFormat(pjtPlanEndDate, "yyyy-MM-dd");
			
			boolean todayBefore = todayStr.compareTo(planEndDateStr) > 0;
			
			if(todayBefore){
				return STATE_BAR_DELAY;
			}else{
				return STATE_BAR_EXDELAY;
			}
		}
	}
}