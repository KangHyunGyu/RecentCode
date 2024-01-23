package com.e3ps.project.beans;

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.e3ps.common.code.NumberCode;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.XmlUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.EProjectNode;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.Holiday;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.key.ProjectKey.STATEKEY;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.pds.StatementSpec;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.util.WTException;
import wt.util.WTProperties;


public class ProjectUtil{
	
	private static final int STATE_BAR_COMPLATE = 1;
	private static final int STATE_BAR_NORMAL = 3;
	private static final int STATE_BAR_EXDELAY = 4;
	private static final int STATE_BAR_DELAY = 5;
	
	public static SimpleDateFormat numberFormat = new SimpleDateFormat("yy");
	public static String xmlTempFile;
	public static Vector<String> vecHoliday;
	static {
		try{
			String tempDir = WTProperties.getLocalProperties().getProperty("wt.home");
			tempDir = tempDir+"\\codebase\\com\\e3ps\\project\\";
			xmlTempFile = tempDir +File.separator+"Holiaday.xml";
			vecHoliday = getHolidayData();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	
	public static String getCurrentYear(){
		return numberFormat.format(new Date());
	}
	
	public static int getInt(Hashtable hash,String key){
		String rr = get(hash,key);
		
		try{
			return Integer.parseInt(rr);
		}catch(Exception ex){}
		
		
		return 0;
	}
	
	public static double getDouble(Hashtable hash,String key){
		String rr = get(hash,key);
		
		try{
			return Double.parseDouble(rr);
		}catch(Exception ex){}
		
		return 0;
	}
	
	public static boolean getBoolean(Hashtable hash,String key){
		String rr = get(hash,key);
		return "true".equals(rr);
	}
 
    public static String get(Hashtable hash, String key){
    	Object o = hash.get(key);
    	
    	if(o instanceof String[]){
    		String[] ss = (String[])hash.get(key);
    		if(ss==null)return null;
    		return ss[0];
    	}
    	else if(o instanceof Vector){
    		Vector v = (Vector)o;
    		return (String)v.get(0);
    	}else if(o instanceof String){
    		return (String)o;
    	}
    	return null;
    	
    }
    
    public static ArrayList getRoleCodeList(){
    	QueryResult qr = CodeHelper.service.getCode("PROJECTROLE");
    	ArrayList list = new ArrayList();
    	while(qr.hasMoreElements()){
    		NumberCode nc = (NumberCode)qr.nextElement();
    		list.add(new String[]{nc.getCode(),nc.getName()});
    	}
    	return list;
    }
    
    public static String numberFormat(double value){
    	DecimalFormat format = new DecimalFormat("###.#");
    	return format.format(value);
    }
    
    /**
     * 휴일 유무 체크
     * @param ca
     * @return
     * @throws Exception
     */
    public static boolean isHoliday(Calendar ca)throws Exception{
    	Calendar ca2 = Calendar.getInstance();
    	TimeZone tz = DateUtil.getTimeZone();
    	ca2.setTimeInMillis(ca.getTimeInMillis());
    	ca2.add(Calendar.MILLISECOND, tz.getRawOffset());
    	
    	int dow = ca2.get(Calendar.DAY_OF_WEEK);
    	if(dow==Calendar.SUNDAY || dow==Calendar.SATURDAY){
    		return true;
    	}
    	
    	Vector<String> holidayVec = getHolidayList();
    	
    	if(holidayVec.contains(DateUtil.getDateString(ca2.getTime(), "d"))){
    		return true;
    	}
    	
    	return false;
    }
    
    
    
    public static int getDurationWithoutHoliday(Timestamp start,Timestamp end)throws Exception{

    	Calendar ca = Calendar.getInstance();
    	ca.setTime(new Date(start.getTime()));

    	Calendar eca = Calendar.getInstance();
    	eca.setTime(new Date(end.getTime()));
    	
    	
    	Set<String> set = new HashSet<String>();
    	LocalDate start_l = start.toLocalDateTime().toLocalDate();
    	LocalDate end_l = end.toLocalDateTime().toLocalDate();
    	
    	int s = start_l.getYear();
    	int e = end_l.getYear();
    	
    	//System.out.println("시작 년도 :  " + s + " , 마지막 년도 : " + e );
    	for(int i = s; i <=e; i++ ) {
    		set = DateUtil.holidaySet(i, set);
    	}
    	
    	int duration = 0;
    	//Vector<String> vec = getHolidayList();
    	
    	while(DateUtil.getDateString(ca.getTime(), "d").compareTo(DateUtil.getDateString(eca.getTime(), "d"))<=0){
    					
			if(isHoliday(ca)){
				duration++;
			}
			String tmp = DateUtil.getDateString(ca.getTime(), "d");
			if(set.contains(tmp)){
				duration++;
			}
			ca.add(Calendar.DATE,1);
		}
    	
    	return duration;
    }
    
    public static Vector<String> getHolidayList(){
    	//Vector<String> vec = new Vector<String>();
    	//vec = getHolidayData();
		/*
    	try {
			QuerySpec query = new QuerySpec(Holiday.class);
			query.appendOrderBy(new OrderBy(new ClassAttribute(Holiday.class, Holiday.HOLIDAY),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec)query);
			while(qr.hasMoreElements()){
				Holiday day = (Holiday) qr.nextElement();
				vec.add(day.getHoliday());
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}
        */
		return vecHoliday;
    	
    }
    
    public static Vector<Holiday> getHolidayObjectList(){
    	Vector<Holiday> vec = new Vector<Holiday>();
		try {
			QuerySpec query = new QuerySpec(Holiday.class);
			query.appendOrderBy(new OrderBy(new ClassAttribute(Holiday.class, Holiday.HOLIDAY),false),new int[]{0});
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec)query);
			while(qr.hasMoreElements()){
				Holiday day = (Holiday) qr.nextElement();
				vec.add(day);
			}
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			e.printStackTrace();
		}
        
		return vec;
    	
    }
    
    public static Calendar setHolidaySkipCalendar(Calendar ca) throws Exception{
    	
    	if(ProjectUtil.isHoliday(ca)){
			ca.add(Calendar.DATE,1);
			setHolidaySkipCalendar(ca);
		}
    	
    	return ca;
    }
    
    public static Timestamp setHolidaySkipTimeStamp(Timestamp date) throws Exception{
    	
    	return setHolidaySkipTimeStamp(date, false);
    }
    
    public static Timestamp setHolidaySkipTimeStamp(Timestamp date,boolean isTemplate) throws Exception{
		
		if(isTemplate) return date;
		
    	Calendar cc = Calendar.getInstance();
    	cc.setTimeInMillis(date.getTime());
    	cc = ProjectUtil.setHolidaySkipCalendar(cc);
    	date = new Timestamp(cc.getTime().getTime());
    	
    	return date;
    }
    
    public static int getDuration(Timestamp start, Timestamp end){
		return DateUtil.getDuration(new Date(start.getTime()),new Date(end.getTime()))+1;
	}
    
    
    public static int getDurationHoliday(Timestamp start, Timestamp end){
    	
    	return getDurationHoliday(start, end, false);
    }
    
	public static int getDurationHoliday(Timestamp start, Timestamp end,boolean isTemplate){
		
		int duration = DateUtil.getDuration(new Date(start.getTime()),new Date(end.getTime()))+1;
		
		if(isTemplate) return duration;
		
		int holiday = 0;
		try {
			holiday = ProjectUtil.getDurationWithoutHoliday(start, end);
			
			//System.out.println("전체  " + duration + " / 공휴일 : " + holiday );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration - holiday;
	}
	
	/**
     * 휴일 Skip시 1일시 증가 (Template은 제외)
     * @param ca
     * @return
     * @throws Exception
     */
    public static  Timestamp setHolidaySkipCalendar(Timestamp date) throws Exception{
		
    	Calendar ca = Calendar.getInstance();
    	ca.setTime(new Date(date.getTime()));

    	
    	
    	if(ProjectUtil.isHoliday(ca)){
			ca.add(Calendar.DATE,1);
			date.setTime(ca.getTime().getTime());
			date = ProjectUtil.setHolidaySkipCalendar(date);
		}
		
		return date;
	}
    
    
    public static Timestamp setPeriodHoliday(Timestamp start,int period){
    	return setPeriodHoliday(start, period, false);
    }
    
    /**
     * 공수 입력시 end Date 공휴일 제외 날짜
     * @param start
     * @param period
     * @return
     */
    public static Timestamp setPeriodHoliday(Timestamp start,int period,boolean isTemplate){
		
    	Timestamp end = DateUtil.convertStartDate(DateUtil.getDateString(start, "d"));;
		try{
			
			if(period ==1) return end;
			
			Calendar startca =Calendar.getInstance();
			startca.setTime(new Date(end.getTime()));
			
			Calendar endca = Calendar.getInstance();
			endca.setTime(new Date(start.getTime()));
			
			for(int i = 0 ; i < period ; i++){
	    		
	    		endca.add(Calendar.DATE,1);
	    		if(isTemplate){
	    			end.setTime(endca.getTime().getTime());
	    			int duration = getDuration(start, end);
	    			
	    			if(duration == period) break;
	    		}else{
	    			endca=checkHoliday(endca);
		    		end.setTime(endca.getTime().getTime());
		    		
		    		int duration = getDurationHoliday(start, end);
		    		if(duration == period) break;
	    		}
	    		
	    		
	    	}
	    	
	    	end.setTime(endca.getTime().getTime());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return end;
	}
    
    public static Calendar checkHoliday(Calendar ca) throws Exception{
		
		if(isHoliday(ca)){
			ca.add(Calendar.DATE,1);
			checkHoliday(ca);
		}
		
		return ca;
	}
    
    public static  int setHolidayCount(Timestamp date,int count) throws Exception{
		
    	Calendar ca = Calendar.getInstance();
    	ca.setTime(new Date(date.getTime()));
    	
    	if(isHoliday(ca)){
			ca.add(Calendar.DATE,1);
			date.setTime(ca.getTime().getTime());
			
			setHolidayCount(date,count);
			count++;
		}
		
		return count;
	}
    
    public static void createHolidayXml(){
    	
    	try{
    		DocumentBuilderFactory docFacctory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFacctory.newDocumentBuilder();
    		
    		Document doc = docBuilder.newDocument();
        	Element NmLoader = doc.createElement("HOLIAYDAY");
    		doc.appendChild(NmLoader);
    		
    		Vector<Holiday> vec =getHolidayObjectList();
    		for(Holiday day : vec){
    			
    			String enable = day.isEnable()? "true" : "false";
    			Element itemAttribute = doc.createElement("DAY");
    			itemAttribute.setAttribute("enable", enable);
    			itemAttribute.setAttribute("key", day.getHoliday());
    			NmLoader.appendChild(itemAttribute);
    			
    		}
    		
    		DOMSource xmlDOM = new DOMSource(doc);
			StreamResult xmlFile = new StreamResult(new File(xmlTempFile));
			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
    
    public static Vector<String> getHolidayData(){
		
    	Vector<String> vec = new Vector<String>();
		try{
			
			File file =new File(xmlTempFile);
			
			Document document = XmlUtil.getxmlPaseing(file);
			
			Element root = (Element) document.getFirstChild();
			
			NodeList list = root.getElementsByTagName("DAY");
			
			for (int i = 0; i < list.getLength(); i++){
				Element ele = (Element) list.item(i);
				
           	 	String key = ele.getAttribute("key");
           	 	String enable = ele.getAttribute("enable");
           	 	if(enable.equals("true")){
           	 		vec.add(key);
           	 	
           	 	}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return vec;
		
	}
    
    public static String getIconUrl(ScheduleNode node){
		
		String url = null;
		
		String state = "";
        
		if(node instanceof ETaskNode){
			ETask tasknode = (ETask)node;
			state = tasknode.getStatus();
		}
		if(node instanceof EProject){
			EProject pjtnode = (EProject)node;
			state = pjtnode.getState().toString();
		}
		
        String sufix = "";
        if(STATEKEY.COMPLETED.equals(state)){
        	sufix = "_complete";
        }else if(STATEKEY.PROGRESS.equals(state)){
        	int delay = ProjectUtil.isDelay(node);
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
        	url = "/Windchill/jsp/project/images/tree/task"+sufix+".gif";
        }
        else{
        	url = "/Windchill/jsp/project/images/tree/task"+sufix+".gif";
        }
        return url;
	}
    
    public static int isDelay(ScheduleNode node) {
		int state = 3;
		try {
			double comp = ProjectUtil.getPreferComp(node);
			double prog = node.getCompletion();
			double preferComplection = (prog / comp) * 100d;
			
			if(comp == 0){
				return STATE_BAR_NORMAL;
			}
			Calendar currentDate = Calendar.getInstance();
			
			Timestamp planEndDate = DateUtil.convertEndDate(DateUtil.getDateString(node.getPlanEndDate(), "a"));
			if(planEndDate.getTime() < currentDate.getTime().getTime()){
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
	}
    
    public static double getPreferComp(ScheduleNode node)throws Exception{
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
			double du = getDurationHoliday(node.getPlanStartDate(),today);
			double planDuration = ProjectUtil.getPlanDurationHoliday(node);
			preferComp = du/planDuration * 100;
		}

		return preferComp;
	}
    
    public static int getPlanDurationHoliday(ScheduleNode node){
		Timestamp st = node.getPlanStartDate();
		Timestamp ed = node.getPlanEndDate();
		int duration = DateUtil.getDuration(new Date(st.getTime()),new Date(ed.getTime()))+1;
		int holiday = 0;
		try {
			holiday = ProjectUtil.getDurationWithoutHoliday(st, ed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration - holiday;
	}
    
    public static String getStateTag(ScheduleNode node)throws Exception{
		String ss = "";
		String barColor = "green";
		String title  = getState(node);
		
		if(node instanceof ETaskNode){
			ETaskNode tasknode = (ETask)node;
			ss = tasknode.getStatus();
		}
		if(node instanceof EProject){
			EProject pjtnode = (EProject)node;
			ss = pjtnode.getState().toString();
		}
		
		if(  STATEKEY.COMPLETED.equals(ss )) {
			barColor = "green";
		}else if( STATEKEY.READY.equals(ss ) || STATEKEY.SIGN.equals(ss ) || STATEKEY.INWORK.equals(ss )){
			barColor = "blank";
		}else if( STATEKEY.PROGRESS.equals(ss )){
			
			int delay = isDelay(node);
        	if(STATE_BAR_EXDELAY == delay){
        		barColor = "yellow";
        	}else if(STATE_BAR_DELAY == delay){
        		barColor = "red";
        	}else{
        		barColor = "blue";
        	}
		}
		StringBuffer result = new StringBuffer();
		
		result.append("<img src='/Windchill/jsp/project/images/project/state_blank_bar.gif'>")
		.append("<img src='/Windchill/jsp/project/images/project/state_"+barColor+"_bar.gif'>")
		.append("<img src='/Windchill/jsp/project/images/project/state_blank_bar.gif'>")
		.append(" "+title+"");
		
		return result.toString();
	}
    
    public static String getState(ScheduleNode node) throws Exception{
    	
		String ss = "";
		if(node instanceof ETaskNode){
			ETaskNode tasknode = (ETask)node;
			ss = tasknode.getStatus();
		}
		if(node instanceof EProject){
			EProject pjtnode = (EProject)node;
			ss = pjtnode.getState().toString();
		}
		
		if(STATEKEY.COMPLETED.equals(ss)){
			return STATEKEY.COMPLETED_KO;
		}else if(STATEKEY.PROGRESS.equals(ss)){
			int delay = ProjectUtil.isDelay(node);
        	if(STATE_BAR_EXDELAY == delay){
        		return STATEKEY.PROGRESS_KO;
        	}else if(STATE_BAR_DELAY == delay){
        		return STATEKEY.DELAY_KO;
        	}else{
        		return STATEKEY.PROGRESS_KO;
        	}
        	
		}else if(STATEKEY.READY.equals(ss)){

//			ProjectRegistApproval approval = getStartLifeCycle();
//			if(approval!=null){
//				String astate = approval.getLifeCycleState().toString();
//				
//				if("APPROVEING".equals(astate) || STATEKEY.APPROVING.equals(astate)){
//					return STATEKEY.APPROVING_KO;
//				}
//			}
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
}