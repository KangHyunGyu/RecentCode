package com.e3ps.project.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.FileUtil;
import com.e3ps.project.EProject;
import com.e3ps.project.ETask;
import com.e3ps.project.ETaskNode;
import com.e3ps.project.PrePostLink;
import com.e3ps.project.ScheduleNode;
import com.e3ps.project.beans.ProjectNodeData;
import com.e3ps.project.beans.ProjectTreeModel;
import com.e3ps.project.beans.ProjectUtil;
import com.e3ps.project.key.ProjectKey;
import com.e3ps.project.key.ProjectKey.STATEKEY;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.pom.Transaction;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.StandardManager;
import wt.util.WTException;

public class StandardTaskEditService extends StandardManager implements wt.method.RemoteAccess,	java.io.Serializable, TaskEditService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.PROJECT.getName());
	private static final long serialVersionUID = 1L;

	public static StandardTaskEditService newStandardTaskEditService() throws WTException {
		StandardTaskEditService instance = new StandardTaskEditService();
		instance.initialize();
		return instance;
	}
	
	@Override
	public String rightClickMenu() throws Exception {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
		stringBuffer.append("<menu>");

		stringBuffer.append("<item id='new' text='추가(최하위)' />");
		stringBuffer.append("<item id='child' text='자식추가' />");
		stringBuffer.append("<item id='next' text='태스크추가(다음)'/>");
		stringBuffer.append("<item id='prev' text='태스크추가(이전)'/>");
		stringBuffer.append("<item id='delete' text='삭제'/>");
		stringBuffer.append("</menu>");

		return stringBuffer.toString();
	}

//	@Override
//	public String loadEditTaskGridXml(String oid) throws Exception {
//
//		EProject root = (EProject) CommonUtil.getObject(oid);
//		ScheduleNode node = (ScheduleNode) root;
//		ProjectNodeData data = new ProjectNodeData(node);
//
//		StringBuffer stringBuffer = new StringBuffer();
//		stringBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
//		stringBuffer.append("<rows>");
//		stringBuffer.append("<head>");
//		stringBuffer
//				.append("<column type='cntr' align='center' width='50'>No.</column>");
//		stringBuffer
//				.append("<column type='tree' align='left' width='200'>이름(Task)</column>");
//		stringBuffer
//				.append("<column type='ed' align='center' width='100'>기간(공수)</column>");
//		stringBuffer
//				.append("<column type='dhxCalendarA' align='center' width='150' id='start'>계획시작일</column>");	//format='%Y-%m-%d'
////		stringBuffer.append("<settings>");
////		stringBuffer.append("    <colwidth>px</colwidth>");
////		stringBuffer.append("</settings>");
//		stringBuffer
//				.append("<column type='dhxCalendarA' align='center' width='150' id='end'>계획종료일</column>");	//format='%Y-%m-%d' 
////		stringBuffer.append("<settings>");
////		stringBuffer.append("    <colwidth>px</colwidth>");
////		stringBuffer.append("</settings>");
//		stringBuffer
//				.append("<column type='ed' align='left' width='*'>선행</column>");
//		stringBuffer.append("</head>");
//
//		stringBuffer
//				.append("<row id='"
//						+ root.getPersistInfo().getObjectIdentifier()
//								.toString() + "' open='1'>");
//
//		String imgu = imgUrl(data);
//		stringBuffer.append("<cell>" + root.getSort() + "</cell>"
//				+ "<cell image='" + imgu + "'>" + root.getName() + "</cell>"
//				+ "<cell>" + data.getPlanDurationHoliday() + "</cell>" + "<cell>"
//				+ DateUtil.getDateString(root.getPlanStartDate(), "d")
//				+ "</cell>" + "<cell>"
//				+ DateUtil.getDateString(root.getPlanEndDate(), "d")
//				+ "</cell>" + "<cell>" + "</cell>");
//
//		stringBuffer.append(getTree(oid));
//		stringBuffer.append("</row>");
//		stringBuffer.append("</rows>");
//
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		org.w3c.dom.Document document = builder.parse(new InputSource(new java.io.StringReader(stringBuffer.toString())));
//		File file = File.createTempFile("PARTLIST", ".xml",
//				FileUtil.getTempDirectory());
//		
//		//XML File Create
//		DOMSource xmlDOM = new DOMSource(document);
//		StreamResult xmlFile = new StreamResult(file);
//		TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);
//
//		String xmlUrl = ProjectKey.tempDirectoryUrl + file.getName();
//		
//		return xmlUrl;
//	}

	@Override
	public String createEditTaskGridXml(String oid) throws Exception {

		EProject root = (EProject) CommonUtil.getObject(oid);
		ScheduleNode node = (ScheduleNode) root;
		ProjectNodeData data = new ProjectNodeData(node);

		File file = File.createTempFile("xml", ".xml", FileUtil.getTempDirectory());

		Document doc = new Document();
		Element rows = new Element("rows");
		Element head = new Element("head");
		Element column1 = new Element("column");
		Element column2 = new Element("column");
		Element column3 = new Element("column");
		Element column4 = new Element("column");
		Element column5 = new Element("column");
		Element column6 = new Element("column");
		Element settings1 = new Element("settings");
		Element settings2 = new Element("settings");
		Element colwidth1 = new Element("colwidth");
		Element colwidth2 = new Element("colwidth");

		rows.addContent(head);
		head.addContent(column1);
		head.addContent(column2);
		head.addContent(column3);
		head.addContent(column4);
		head.addContent(settings1);
		settings1.addContent(colwidth1);
		head.addContent(column5);
		head.addContent(settings2);
		settings2.addContent(colwidth2);
		head.addContent(column6);

		column1.setText("No.");
		column1.setAttribute("width", "50");
		column1.setAttribute("type", "cntr");		
		column1.setAttribute("align", "center");
		
		column2.setText("이름(Task)");
		column2.setAttribute("width", "200");
		column2.setAttribute("type", "tree");
		column2.setAttribute("align", "left");
		
		column3.setText("기간(공수)");
		column3.setAttribute("width", "100");
		column3.setAttribute("type", "ed");
		column3.setAttribute("align", "center");
		
		column4.setText("계획시작일");
		column4.setAttribute("id", "start");
		column4.setAttribute("format", "%Y/%m/%d");
		column4.setAttribute("width", "150");
		column4.setAttribute("type", "dhxCalendarA");
		column4.setAttribute("align", "center");
		
		column5.setText("계획종료일");
		column5.setAttribute("id", "end");
		column5.setAttribute("format", "%Y/%m/%d");
		column5.setAttribute("width", "150");
		column5.setAttribute("type", "dhxCalendarA");
		column5.setAttribute("align", "center");
		
		
		column6.setText("선행");
		column6.setAttribute("width", "*");
		column6.setAttribute("type", "ed");
		column6.setAttribute("align", "left");

		colwidth1.setText("px");
		colwidth2.setText("px");

		Element rootrow = new Element("row");
		rows.addContent(rootrow);
		rootrow.setAttribute("id", CommonUtil.getOIDString(root));
		rootrow.setAttribute("open", "1");
		
		Element rootcell1 = new Element("cell");
		Element rootcell2 = new Element("cell");
		Element rootcell3 = new Element("cell");
		Element rootcell4 = new Element("cell");
		Element rootcell5 = new Element("cell");
		Element rootcell6 = new Element("cell");

		rootrow.addContent(rootcell1);
		rootrow.addContent(rootcell2);
		rootrow.addContent(rootcell3);
		rootrow.addContent(rootcell4);
		rootrow.addContent(rootcell5);
		rootrow.addContent(rootcell6);

		String imgu = imgUrl(data);
		String sort = "0";
		try{
		sort = Integer.toString(root.getSort());
		}catch(Exception e){}
		rootcell1.setText(sort);
		rootcell2.setText(root.getName());
		rootcell2.setAttribute("image", imgu);
		rootcell3.setText(data.getPlanDurationHoliday()+"");	//data.getPlanDuration() + "["+ data.getPlanDurationHoliday() + "]"
		rootcell4.setText(DateUtil.getDateString(root.getPlanStartDate(), "d"));
		rootcell5.setText(DateUtil.getDateString(root.getPlanEndDate(), "d"));
		rootcell6.setText("");

		getTreeXml(oid, rootrow);

		doc.setRootElement(rows);

		String xmlUrl = ProjectKey.tempDirectoryUrl + file.getName();
//		FileOutputStream out = new FileOutputStream("D:\\ptc\\Windchill_11.0\\Windchill\\codebase\\e3psFileTemp\\" + file.getName());
		FileOutputStream out = new FileOutputStream("D:\\ptc\\Windchill_12.1\\Windchill\\codebase\\e3psFileTemp\\" + file.getName());
		//xmlUrl = "D:\ptc\Windchill\Windchill\codebase\e3psFileTemp"
		// xml 파일을 떨구기 위한 경로와 파일 이름 지정해 주기
		XMLOutputter serializer = new XMLOutputter();
		OutputStreamWriter os = new OutputStreamWriter(out, "UTF-8");


		Format f = serializer.getFormat();
		f.setEncoding("UTF-8");
		// encoding 타입을 UTF-8 로 설정
		f.setIndent(" ");
		f.setLineSeparator("\r\n");
		f.setTextMode(Format.TextMode.TRIM);
		serializer.setFormat(f);
		serializer.output(doc, os);
		//while()
		os.flush();
		os.close();

		
		
		LOGGER.info("### aa == "+xmlUrl);
		
		return xmlUrl;
	}

	private String imgUrl(ProjectNodeData data) throws WTException{

		String imgUrl = "";

		try {
			if (STATEKEY.COMPLETED_KO.equals(data.getState())) {
				imgUrl = "task_complete.gif";
			} else if (STATEKEY.PROGRESS_KO.equals(data.getState())
					|| STATEKEY.DELAY_KO.equals(data.getState())) {
				int delay = data.isDelay();
				if (4 == delay) {
					imgUrl = "task_orange.gif";
				} else if (5 == delay) {
					imgUrl = "task_red.gif";
				} else {
					imgUrl = "task_progress.gif";
				}
			} else {
				imgUrl = "task_ready.gif";
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		}
		return imgUrl;
	}

	@Override
	public StringBuffer getTree(String oid) throws Exception {

		StringBuffer sb = new StringBuffer();
		QuerySpec query = new QuerySpec();
		QueryResult qr = null;

		int idx = query.addClassList(ETask.class, true);
		long loid = CommonUtil.getOIDLongValue(oid);

		query.appendWhere(new SearchCondition(ETask.class,
				"parentReference.key.id", SearchCondition.EQUAL, loid),
				new int[] { idx });

		query.appendOrderBy(new OrderBy(
				new ClassAttribute(ETask.class, "sort"), false),
				new int[] { idx });
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,
				"thePersistInfo.theObjectIdentifier.id"), false),
				new int[] { idx });

		qr = PersistenceHelper.manager.find(query);

		while (qr.hasMoreElements()) {

			Object o[] = (Object[]) qr.nextElement();
			ETaskNode task = (ETaskNode) o[0];
			ProjectNodeData data = new ProjectNodeData(task);
			String taskOid = task.getPersistInfo().getObjectIdentifier()
					.toString();

			java.sql.Timestamp st = task.getPlanStartDate();
			java.sql.Timestamp ed = task.getPlanEndDate();

			int duration = data.getPlanDuration();
			int durationHoliday = data.getPlanDurationHoliday();

			String imgu = imgUrl(data);
			String complate = "";

			if (STATEKEY.COMPLETED.equals(task.getStatus())) {
				complate = "text='complate'";
			}

			sb.append("<row id='" + taskOid + "' " + complate + " open='1'>");
			sb.append("<cell>" + task.getSort() + "</cell>" + "<cell image='"
					+ imgu + "'>" + task.getName() + "</cell>" + "<cell>"
					+ duration + " [" + durationHoliday + "]" + "</cell>"
					+ "<cell>"
					+ DateUtil.getDateString(task.getPlanStartDate(), "d")
					+ "</cell>" + "<cell>"
					+ DateUtil.getDateString(task.getPlanEndDate(), "d")
					+ "</cell>");
			sb.append("<cell>");

			QueryResult preQr = PersistenceHelper.manager.navigate(task, "pre",
					PrePostLink.class);
			int a = preQr.size();
			int b = 1;
			while (preQr.hasMoreElements()) {
				ETaskNode sgnode = (ETaskNode) preQr.nextElement();
				sb.append(sgnode.getName());
				if (a != b) {
					sb.append(", ");
				}
				b++;
			}
			sb.append("</cell>");
			sb.append(getTree(taskOid));
			sb.append("</row>");
		}
		return sb;
	}

	@Override
	public StringBuffer getTreeXml(String oid, Element rootrow)
			throws Exception {

		StringBuffer sb = new StringBuffer();
		QuerySpec query = new QuerySpec();
		QueryResult qr = null;

		int idx = query.addClassList(ETask.class, true);
		long loid = CommonUtil.getOIDLongValue(oid);

		query.appendWhere(new SearchCondition(ETask.class,
				"parentReference.key.id", SearchCondition.EQUAL, loid),
				new int[] { idx });

		query.appendOrderBy(new OrderBy(
				new ClassAttribute(ETask.class, "sort"), false),
				new int[] { idx });
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,
				"thePersistInfo.theObjectIdentifier.id"), false),
				new int[] { idx });

		qr = PersistenceHelper.manager.find(query);

		while (qr.hasMoreElements()) {

			Object o[] = (Object[]) qr.nextElement();
			ETaskNode task = (ETaskNode) o[0];
			ProjectNodeData data = new ProjectNodeData(task);
			String taskOid = task.getPersistInfo().getObjectIdentifier()
					.toString();

			int duration = data.getPlanDuration();
			int durationHoliday = data.getPlanDurationHoliday();

			String imgu = imgUrl(data);

			Element row = new Element("row");
			rootrow.addContent(row);
			row.setAttribute("id", taskOid);
			row.setAttribute("open", "1");
			if (STATEKEY.COMPLETED.equals(task.getStatus())) {
				//row.setAttribute("text", "complate");
			}

			Element rootcell1 = new Element("cell");
			Element rootcell2 = new Element("cell");
			Element rootcell3 = new Element("cell");
			Element rootcell4 = new Element("cell");
			Element rootcell5 = new Element("cell");
			Element rootcell6 = new Element("cell");

			row.addContent(rootcell1);
			row.addContent(rootcell2);
			row.addContent(rootcell3);
			row.addContent(rootcell4);
			row.addContent(rootcell5);
			row.addContent(rootcell6);

			int sort = task.getSort();

			String sortStr = "";
			try {
				sortStr = Integer.toString(sort);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WTException(e);
			}

			rootcell1.setText(sortStr);
			rootcell2.setText(task.getName());
			rootcell2.setAttribute("image", imgu);
			rootcell3.setText(durationHoliday+""); //duration + " [" + + "]"
			rootcell4.setText(DateUtil.getDateString(task.getPlanStartDate(),
					"d"));
			rootcell5
					.setText(DateUtil.getDateString(task.getPlanEndDate(), "d"));

			QueryResult preQr = PersistenceHelper.manager.navigate(task, "pre",
					PrePostLink.class);
			int a = preQr.size();
			int b = 1;
			String preName = "";
			while (preQr.hasMoreElements()) {
				ETaskNode sgnode = (ETaskNode) preQr.nextElement();
				preName += sgnode.getName();
				if (a != b) {
					preName += ", ";
				}
				b++;

			}
			rootcell6.setText(preName);
			getTreeXml(taskOid, row);
		}
		return sb;
	}

	/**
	 * cell 수정
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<ETaskNode> editCell(Hashtable hash) throws Exception {

		Transaction trx = new Transaction();

		ArrayList<ETaskNode> list = null;
		ETaskNode task = null;

		try {
			trx.start();
			String soid = (String) ProjectUtil.get(hash, "soid");
			String cellId = (String) ProjectUtil.get(hash, "cellId");
			String pjtOid = (String) ProjectUtil.get(hash, "pjtOid");
			String newVal = (String) ProjectUtil.get(hash, "newVal");

			ReferenceFactory rf = new ReferenceFactory();

			task = (ETaskNode) rf.getReference(soid).getObject();
			if ("1".equals(cellId)) {
				task.setName(newVal);
			} else if ("2".equals(cellId)) {

				Timestamp strDate = task.getPlanStartDate();
				Timestamp newDate = new Timestamp(strDate.getYear(),
						strDate.getMonth(), strDate.getDate(),
						strDate.getHours(), strDate.getMinutes(),
						strDate.getSeconds(), strDate.getNanos());
				// int strDay = strDate.getDate();
				// int change = strDay + Integer.parseInt(newVal) -1;
				// newDate.setDate(change);

				/* 공휴일 제외 S */
				newDate = ProjectUtil.setPeriodHoliday(strDate,
						Integer.parseInt(newVal));

				task.setPlanEndDate(newDate);

			} else if ("3".equals(cellId)) {
				Timestamp date = DateUtil.convertStartDate(newVal);
				/* 공휴일 제외 S */
				date = ProjectUtil.setHolidaySkipTimeStamp(date);
				task.setPlanStartDate(date);

				Timestamp endd = task.getPlanEndDate();
				if (date.after(endd)) {
					/* 공휴일 제외 S */
					date = ProjectUtil.setHolidaySkipTimeStamp(date);
					task.setPlanEndDate(date);
				}

			} else if ("4".equals(cellId)) {
				Timestamp date = DateUtil.convertStartDate(newVal);
				/* 공휴일 제외 S */
				date = ProjectUtil.setHolidaySkipTimeStamp(date);
				task.setPlanEndDate(date);
			}

			task = (ETaskNode) PersistenceHelper.manager.save(task);

			ProjectTreeModel model = new ProjectTreeModel(task.getProject());
			list = model.setExecSchedule(task);

			list.add(task);

			trx.commit();
			trx = null;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return list;
	}

	/**
	 * cell 수정
	 * 
	 * @param hash
	 * @return
	 * @throws Exception
	 */
	
	@Override
	public ArrayList<ETaskNode> getPlan(ArrayList<String> list) throws Exception {

		Transaction trx = new Transaction();
		ArrayList<ETaskNode> reList = new ArrayList<ETaskNode>();
		try {
			trx.start();

			for (int i = 0; i < list.size(); i++) {
				ETaskNode node = (ETaskNode) CommonUtil.getObject((String)list.get(i));

				reList.add(node);

				QueryResult qr = PersistenceHelper.manager.navigate(node,
						"pre", PrePostLink.class);
				while (qr.hasMoreElements()) {
					ETaskNode preNode = (ETaskNode) qr.nextElement();
					reList.add(preNode);
				}
				QueryResult qrp = PersistenceHelper.manager.navigate(node,
						"post", PrePostLink.class);
				while (qrp.hasMoreElements()) {
					ETaskNode postNode = (ETaskNode) qrp.nextElement();
					reList.add(postNode);
				}
			}

			trx.commit();
			trx = null;

		} catch (Exception e) {
			throw e;
		} finally {
			if (trx != null) {
				trx.rollback();
			}
		}
		return reList;
	}
	
	@Override
	public ArrayList<ETaskNode> getPlan(String oid) {
		ArrayList<ETaskNode> reList = new ArrayList<ETaskNode>();
		try {
			ETaskNode node = (ETaskNode) CommonUtil.getObject(oid);
			reList.add(node);
			QueryResult qr = PersistenceHelper.manager.navigate(node, "pre",
					PrePostLink.class);
			while (qr.hasMoreElements()) {
				ETaskNode preNode = (ETaskNode) qr.nextElement();
				reList.add(preNode);
			}
			QueryResult qrp = PersistenceHelper.manager.navigate(node, "post",
					PrePostLink.class);
			while (qrp.hasMoreElements()) {
				ETaskNode postNode = (ETaskNode) qrp.nextElement();
				reList.add(postNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	
	@Override
    public StringBuffer getTreeJsp(String projectOid) throws Exception{
		StringBuffer sb = new StringBuffer();
		QuerySpec query = new QuerySpec();
		QueryResult qr = null;
		
		int idx = query.addClassList(ETask.class, true);
		long loid = CommonUtil.getOIDLongValue(projectOid);
		
		query.appendWhere(new SearchCondition(ETask.class, "parentReference.key.id", SearchCondition.EQUAL, loid), new int[]{idx});
		
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,"sort"), false), new int[] { idx });  
		query.appendOrderBy(new OrderBy(new ClassAttribute(ETask.class,"thePersistInfo.theObjectIdentifier.id"), false), new int[] { idx });  
		
		qr = PersistenceHelper.manager.find(query);
		
		while(qr.hasMoreElements()){
			Object o[] = (Object[])qr.nextElement();
			ETaskNode task = (ETaskNode)o[0];
			ProjectNodeData data = new ProjectNodeData(task);
			String taskOid = task.getPersistInfo().getObjectIdentifier().toString();
			
			java.sql.Timestamp st = task.getPlanStartDate();
			java.sql.Timestamp ed = task.getPlanEndDate();
			int duration = data.getPlanDurationHoliday();
			//DateUtil.getDuration(new Date(st.getTime()),new Date(ed.getTime())) + 1;
			
			String imgu = "";
			String complate = "";
			if(STATEKEY.COMPLETED.equals(task.getStatus())){
				 imgu = "task_complete.gif";
				 complate = "text='complate'";
			}else if(STATEKEY.PROGRESS.equals(task.getStatus())){
				int delay = data.isDelay();
		    	if(4 == delay){
		    		imgu = "task_orange.gif";
		    	}else if(5 == delay){
		    		imgu = "task_red.gif";
		    	}else{
		    		imgu = "task_progress.gif";
		    	}
			}else{
				imgu = "task_ready.gif";
			}
			
			sb.append("<row id='"+ taskOid +"' "+ complate +" open='1'>");
			sb.append("<cell>"+ task.getSort() +"</cell>");
			sb.append("<cell image='"+imgu+"'>"+ task.getName() +"</cell>");
			sb.append("<cell>"+ duration +"</cell>");
			sb.append("<cell>"+ DateUtil.getDateString(task.getPlanStartDate(),"d") +"</cell>");
			sb.append("<cell>"+ DateUtil.getDateString(task.getPlanEndDate(),"d") +"</cell>");
			//sb.append("<cell>"+ DateUtil.getTimeFormat(task.getPlanStartDate(), "dd/MM/yyyy") +"</cell>");
			//sb.append("<cell>"+ DateUtil.getTimeFormat(task.getPlanEndDate(), "dd/MM/yyyy") +"</cell>");
			sb.append("<cell>");
			
			QueryResult preQr = PersistenceHelper.manager.navigate(task,"pre",PrePostLink.class);
			int a = preQr.size();
			int b = 1;
			while(preQr.hasMoreElements()){
				ETaskNode sgnode = (ETaskNode)preQr.nextElement();
				sb.append(sgnode.getName());
				if(a != b){
					sb.append(", ");
				}b++;
			}
			sb.append("</cell>");
			sb.append(getTree(taskOid));
			sb.append("</row>");
		}
		return sb;
	}
	
	@Override
	public boolean isPrePostLink(ETaskNode preTask, ETaskNode postTask) {
		try {
			QuerySpec qs = new QuerySpec(PrePostLink.class);
			qs.appendWhere(new SearchCondition(PrePostLink.class, "roleAObjectRef.key.id", SearchCondition.EQUAL,
					CommonUtil.getOIDLongValue(preTask)), new int[] { 0 });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(PrePostLink.class, "roleBObjectRef.key.id", SearchCondition.EQUAL,
					CommonUtil.getOIDLongValue(postTask)), new int[] { 0 });
			QueryResult qr = PersistenceHelper.manager.find(qs);
			return qr.size() > 0;
		} catch (QueryException e) {
			e.printStackTrace();
		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
    public ScheduleNode isPreTaskNode(String postOid, ScheduleNode preNode) throws WTException{
    	ScheduleNode isPreNode = null;
    	QueryResult qr = PersistenceHelper.manager.navigate(preNode, "pre", PrePostLink.class);
    	while(qr.hasMoreElements()){
    		ScheduleNode n = (ScheduleNode)qr.nextElement();
    		if(postOid.equals(CommonUtil.getOIDString(n))){
    			return preNode;
    		}
    		isPreNode = isPreTaskNode(postOid, n);
    		
    		if(isPreNode != null){
    			break;
    		}
    	}
    	if(isPreNode != null){
    		isPreNode = preNode;
    	}
    	return isPreNode;
    }
}
