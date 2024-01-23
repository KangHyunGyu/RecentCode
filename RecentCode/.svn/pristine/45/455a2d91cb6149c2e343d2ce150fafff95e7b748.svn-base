package com.e3ps.change.beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.e3ps.change.DocumentActivityOutput;
import com.e3ps.change.EChangeActivity;
import com.e3ps.change.EChangeOrder2;
import com.e3ps.change.EChangeRequest2;
import com.e3ps.change.EcoPartLink;
import com.e3ps.change.service.ChangeHelper;
import com.e3ps.change.service.ChangeService;
import com.e3ps.common.code.service.CodeHelper;
import com.e3ps.common.message.util.MessageUtil;
import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.DateUtil;
import com.e3ps.common.util.StringUtil;
import com.e3ps.common.util.TypeUtil;
import com.e3ps.common.util.WCUtil;
import com.e3ps.doc.E3PSDocument;
import com.e3ps.doc.service.DocHelper;
import com.e3ps.org.bean.PeopleData;
import com.e3ps.part.service.PartHelper;

import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.query.QuerySpec;
import wt.query.SearchCondition;

public class ECAData {
	private String oid;
	private String name;
	private String state;
	private String type;
	private String dept;
	private String owner;
	private String ownerOid;
	private String finishDate;
	private String ecaFinishDate;
	private String createDate;
	private String modifyDate;
	private String activeType;
	private String imgUrl;
	private String complateDate;
	private String orderState;
	private String title;
	private String outputOid;
	private String stepUrl;
	private String stepIconUrl;
	private String comment;
	private String step;
	private String description;
	private String order;
	private String orderCreator;
	private String orderName;
	private String orderNumber;
	private String orderTitle;
	private String orderOid;
	private int delay;
	public ECAData(EChangeActivity eca) throws Exception {
		this.oid = WCUtil.getOid(eca);
		this.name = StringUtil.checkNull(eca.getName());
		if(StringUtil.checkNull(eca.getDescription()).contains("_"+ChangeService.ACTIVITY_CANCELED)) {
			this.state = StringUtil.checkNull(ChangeHelper.service.getLocaleString(ChangeService.ACTIVITY_CANCELED));
		}else {
			this.state = StringUtil.checkNull(ChangeHelper.service.getLocaleString(eca.getActiveState()));
		}
		this.type = StringUtil.checkNull(CodeHelper.service.getName("EOACTIVETYPE", TypeUtil.stringValue(eca.getActiveType())));
		
		Timestamp fDate = eca.getFinishDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String formattedDate = sdf.format(fDate);
		this.finishDate = formattedDate;
		if(eca.getEcafinishDate() == null) {
			this.ecaFinishDate = "";
		} else {
			Timestamp ecafDate = eca.getEcafinishDate();
			String efDate = sdf.format(ecafDate);
			this.ecaFinishDate = efDate;
		}
		this.createDate = DateUtil.getDateString(eca.getPersistInfo().getCreateStamp(),"d");
		this.modifyDate = DateUtil.getDateString(eca.getPersistInfo().getModifyStamp(),"d");
		WTUser user = (WTUser)eca.getOwner().getObject();
		PeopleData pd = new PeopleData(user);
		this.dept = StringUtil.checkNull(pd.getDepartmentName());
		this.owner = StringUtil.checkNull(pd.getName());
		this.ownerOid = StringUtil.checkNull(pd.getOid());
		this.activeType = StringUtil.checkNull(eca.getActiveType());
		this.comment = StringUtil.checkNull(eca.getComments());
		this.step = StringUtil.checkNull(eca.getStep());
		this.description = StringUtil.checkNull(eca.getDescription());
		this.order = StringUtil.checkNull(CommonUtil.getOIDString(eca.getOrder()));
		EChangeOrder2 eco = (EChangeOrder2) eca.getOrder(); 
		String ecoOid = WCUtil.getOid(eca.getOrder());
		this.orderNumber = StringUtil.checkNull(eco.getOrderNumber());
		this.orderName = StringUtil.checkNull(eco.getName());
		this.orderOid = StringUtil.checkNull(ecoOid);
		this.orderCreator = StringUtil.checkNull(eco.getCreatorFullName());
		
    	setImgUrl(eca);
    	setOutput(eca, this.activeType);
    	setTitle(eca);
	}
	public void setOutput(EChangeActivity eca, String activeType) throws Exception {
		if("DRAWING".equals(activeType)) {
			EChangeOrder2 eco = null;
			if(eca.getOrder() instanceof EChangeOrder2){
				eco = (EChangeOrder2)eca.getOrder();
				this.orderState = eco.getLifeCycleState().getDisplay(MessageUtil.getLocale());
				//eco = (EChangeOrder2)eca.getOrder();
			}
			List<Object> relatedDrawings = new ArrayList<Object>();
			relatedDrawings = ChangeECOSearch.getECOrelatedResultDrawings(eco);
			if(relatedDrawings!=null && relatedDrawings.size()>0){
				Object[] o = (Object[])relatedDrawings.get(0);
	    		EPMDocument epm = (EPMDocument)o[1];
	    		this.outputOid = CommonUtil.getOIDString(epm);
	    		this.title = epm.getName();
	    		if(relatedDrawings.size()>1){
	    			String size = Integer.toString(relatedDrawings.size()-1) ;
	    			this.title = epm.getName()+" 외 " +size+" 건";
	    		}
			}
		}else if("PART".equals(activeType)) {
			QuerySpec qs = new QuerySpec(EcoPartLink.class);
			qs.appendWhere(new SearchCondition(EcoPartLink.class,"roleBObjectRef.key.id","=",
					eca.getOrder().getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
			QueryResult  aqr = PersistenceHelper.manager.find(qs);
			if(aqr.hasMoreElements()){
				EcoPartLink link = (EcoPartLink)aqr.nextElement();	
				WTPartMaster master = link.getPart();
				
				WTPart part = PartHelper.service.getPart(master.getNumber(), link.getVersion());
				this.outputOid = CommonUtil.getOIDString(part);
				this.title = part.getName();
				if(aqr.size() > 1){
					String size = Integer.toString(aqr.size()-1) ;
	    			this.title = part.getName()+" 외 " +size+" 건";
				}
			}
				
		}else if("DOCUMENT".equals(activeType)) {
			List<E3PSDocument> nlist = new ArrayList<E3PSDocument>();
			List<E3PSDocument> olist = new ArrayList<E3PSDocument>();
			QuerySpec qs = new QuerySpec(DocumentActivityOutput.class);
			qs.appendWhere(new SearchCondition(DocumentActivityOutput.class,"activityReference.key.id","=",
					eca.getPersistInfo().getObjectIdentifier().getId()),new int[]{0});
			QueryResult  aqr = PersistenceHelper.manager.find(qs);
			while(aqr.hasMoreElements()){
				DocumentActivityOutput link = (DocumentActivityOutput)aqr.nextElement();	
				E3PSDocument ndoc = (E3PSDocument) DocHelper.manager.getDocument(link.getDocumentNewNumber(),link.getDocumentNewVersion());
				E3PSDocument odoc = (E3PSDocument) DocHelper.manager.getDocument(link.getDocumentOldNumber(),link.getDocumentOldVersion());
				if(ndoc!=null){
					nlist.add(ndoc);
				}
				if(odoc!=null){
					olist.add(odoc);
				}
			}
			String docTitle = "";
			if(nlist.size()>0){ 
				E3PSDocument ndoc = (E3PSDocument)nlist.get(0);
        		this.outputOid = CommonUtil.getOIDString(ndoc);
        		docTitle = "NEW:"+ndoc.getName();
				if(nlist.size() > 1){
					String size = Integer.toString(nlist.size()-1) ;
					docTitle = "NEW:"+ndoc.getName()+" 외 " +size+" 건";
				}
				this.title = docTitle;
			}
			if(olist.size()>0){ 
        		if(nlist.size()>0){ 
        			docTitle += ", ";
        		}
        		E3PSDocument odoc = (E3PSDocument)olist.get(0);
        		docTitle += "OLD:"+odoc.getName();
        		if(olist.size() > 1){
					String size = Integer.toString(olist.size()-1) ;
					docTitle += " 외 " +size+" 건";
				}
        		this.title = docTitle;
			}
		}else if("COMMON".equals(activeType)) {
			this.outputOid = "";
			this.title = StringUtil.checkNull(eca.getComments());
		}
		
	}
	public void setImgUrl(EChangeActivity eca) {
		Timestamp finishDate = eca.getFinishDate();
    	Timestamp toDate = DateUtil.convertDate(DateUtil.getToDay());
    	if(ChangeService.ACTIVITY_WORKING.equals(eca.getActiveState())){
    		
    		if(toDate.getTime() > finishDate.getTime()) {
    			this.imgUrl ="/Windchill/jsp/project/images/tree/task_red.gif";
    			this.stepUrl = "/Windchill/jsp/project/images/"+eca.getStep()+"_DELAY.gif";
    			this.stepIconUrl = "/Windchill/jsp/project/images/step_icon_delay.gif";
    		}else if(toDate.getTime() == finishDate.getTime() || toDate.getTime() < finishDate.getTime() ){
    			this.imgUrl = "/Windchill/jsp/project/images/tree/task_progress.gif";
    			this.stepUrl = "/Windchill/jsp/project/images/"+eca.getStep()+"_COMPLETE.gif";
    			this.stepIconUrl = "/Windchill/jsp/project/images/step_icon_working.gif";
    		} 
    	}else if(ChangeService.ACTIVITY_STANDBY.equals(eca.getActiveState())){
    		this.stepUrl = "/Windchill/jsp/project/images/"+eca.getStep()+"_WAITING.gif";
    		this.stepIconUrl = "/Windchill/jsp/project/images/step_icon_waiting.gif";
    		this.imgUrl = "/Windchill/jsp/project/images/tree/task_ready.gif";
    	}else if(ChangeService.ACTIVITY_APPROVED.equals(eca.getActiveState())){
    		this.stepUrl = "/Windchill/jsp/project/images/"+eca.getStep()+"_COMPLETE.gif";
    		this.stepIconUrl = "/Windchill/jsp/project/images/step_icon_complete.gif";
    		this.complateDate = DateUtil.getDateString(eca.getModifyTimestamp(),"d");
    		this.imgUrl = "/Windchill/jsp/project/images/tree/task_complete.gif";
    	}else if(ChangeService.ACTIVITY_CANCELED.equals(eca.getActiveState()) ||
    			ChangeService.ACTIVITY_CANCELLED.equals(eca.getActiveState())){
    		this.stepUrl = "/Windchill/jsp/project/images/"+eca.getStep()+"_DELAY.gif";
    		this.imgUrl = "/Windchill/jsp/project/images/tree/task_red.gif";
    		this.stepIconUrl = "/Windchill/jsp/project/images/step_icon_delay.gif";
    	}else {
    		this.stepUrl = "/Windchill/jsp/project/images/"+eca.getStep()+"_WAITING.gif";
    		this.imgUrl = "/Windchill/jsp/project/images/tree/task_ready.gif";
    		this.stepIconUrl = "/Windchill/jsp/project/images/step_icon_waiting.gif";
    	}
    	
	}
	
	public void setTitle(EChangeActivity eca) {
		if(eca.getOrder() instanceof EChangeOrder2){
			this.orderTitle = "[" + ((EChangeOrder2)eca.getOrder()).getOrderNumber()
					+ "] " + ((EChangeOrder2)eca.getOrder()).getName();
		}else if(eca.getOrder() instanceof EChangeRequest2){
			this.orderTitle = "[" + ((EChangeRequest2)eca.getOrder()).getRequestNumber()
					+ "] "+((EChangeRequest2)eca.getOrder()).getName();
		}
	}
	
	
	public String getOrderTitle() {
		return orderTitle;
	}
	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStepIconUrl() {
		return stepIconUrl;
	}
	public void setStepIconUrl(String stepIconUrl) {
		this.stepIconUrl = stepIconUrl;
	}
	public String getActiveType() {
		return activeType;
	}
	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}
	public String getOutputOid() {
		return outputOid;
	}
	public void setOutputOid(String outputOid) {
		this.outputOid = outputOid;
	}
	public String getStepUrl() {
		return stepUrl;
	}
	public void setStepUrl(String stepUrl) {
		this.stepUrl = stepUrl;
	}
	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getComplateDate() {
		return complateDate;
	}

	public void setComplateDate(String complateDate) {
		this.complateDate = complateDate;
	}

	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getEcaFinishDate() {
		return ecaFinishDate;
	}
	public void setEcaFinishDate(String ecaFinishDate) {
		this.ecaFinishDate = ecaFinishDate;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderOid() {
		return orderOid;
	}
	public void setOrderOid(String orderOid) {
		this.orderOid = orderOid;
	}
	public String getOrderCreator() {
		return orderCreator;
	}
	public void setOrderCreator(String orderCreator) {
		this.orderCreator = orderCreator;
	}
	public String getOwnerOid() {
		return ownerOid;
	}
	public void setOwnerOid(String ownerOid) {
		this.ownerOid = ownerOid;
	}
	
}
