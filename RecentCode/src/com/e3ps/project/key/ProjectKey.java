package com.e3ps.project.key;

import com.e3ps.common.jdf.config.Config;
import com.e3ps.common.jdf.config.ConfigImpl;
import com.e3ps.common.util.WCUtil;

import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.pdmlink.PDMLinkProduct;
import wt.util.WTException;
import wt.util.WTProperties;

public class ProjectKey {
	
	public static String productName = "";
	public static String orgName = "";
	public static WTContainerRef containerRef = null;
	public static String tempDirectoryUrl = "/Windchill/e3psFileTemp/";
	public static String dataStore = "Oracle"; //SQLServer ....
	
	static{
		Config conf = ConfigImpl.getInstance();
		productName = conf.getString("product.context.name");
		orgName = conf.getString("org.context.name");
		
		try {
			PDMLinkProduct e3psProduct = WCUtil.getPDMLinkProduct();
			containerRef = WTContainerRef.newWTContainerRef(e3psProduct);
			//containerRef = WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
		} catch (WTException e1) {
			e1.printStackTrace();
		}
		try {
			dataStore = WTProperties.getLocalProperties().getProperty("wt.db.dataStore");
		} catch (Exception e) {
			dataStore = "Oracle";
			e.printStackTrace();
		}
	}
	
	public static WTContainerRef getWTContainerRef() throws WTException{
		if(containerRef != null){
			return containerRef;
		}
		return WTContainerHelper.service.getByPath("/wt.inf.container.OrgContainer="+orgName+"/wt.pdmlink.PDMLinkProduct="+productName);
	}
	
	public class NUMBERCODEKEY{
		
		public static final String ISSUETYPE = "ISSUETYPE";
		
	}
	
	public class LIFECYCLEKEY {
		
		public static final String LC_Default = "LC_Default";
		public static final String LC_Project = "LC_Project";
		
	}
	
	public class FOLDERKEY {
		
		public static final String DOCUMENT = "/Default/Document";
		public static final String PROJECT = "/Default/Project";
		
	}
	
	public class STATEKEY {
		
		public static final String INTAKE = "INTAKE";
		public static final String INWORK = "INWORK";
		public static final String APPROVING = "APPROVING";
		public static final String RETURN = "RETURN";
		public static final String DEATH = "DEATH";
		public static final String COMPLETED = "COMPLETED";
		public static final String APPROVED = "APPROVED";
		public static final String REWORK = "REWORK";
		public static final String CANCELLED = "CANCELLED";
		
		
		public static final String INWORK_KO = "작업중";
		public static final String COMPLETED_KO = "완료됨";
		public static final String PROGRESS_KO = "진행중";
		public static final String DELAY_KO = "지연중";
		public static final String READY_KO = "준비중";
		public static final String APPROVING_KO = "결재중";
		public static final String SIGN_KO = "결재중";
		public static final String STOP_KO = "중단됨";
		public static final String MODIFY_KO = "수정중";
		public static final String CANCELLED_KO = "취소됨";
		
		//Project
		public static final String READY = "READY";	//준비중
		public static final String SIGN = "SIGN";	//결재중
		public static final String REPORTWRITE = "REPORTWRITE";	//상신중
		public static final String PROGRESS = "PROGRESS";
		public static final String MODIFY = "MODIFY";
		public static final String STOP = "STOP";
		
	    //Output
	    public static final String OUTPUT_STATE_READY = "등록전";			
	    public static final String OUTPUT_STATE_REGIST = "등록됨";	
	    
	}
	
	public class OUTPUTKEY {
		
		public static final String PSO ="PSO";
		public static final String GENERAL ="GENERAL";
		
	}

	public class IssueKey {
		public static final String ISSUE_REQUEST = "요청중"; 
		public static final String ISSUE_CHECK = "검토중"; 
		public static final String ISSUE_COMPLETE = "완료됨";
	}
	
	public class PROJECTKEY {

		public static final String PM = "PR01";
		public static final String PROJECT_START_GUBUN = "프로젝트 시작";
		
	}
	
	public class APPROVALKEY{
		
		public static final String APPROVE_APPROVALLINE= "결재선지정";
		public static final String APPROVE_REQUEST= "기안";
	    public static final String APPROVE_PREAPPROVE= "합의전결재";
	    public static final String APPROVE_DISCUSS= "합의";
	    public static final String APPROVE_POSTAPPROVE= "결재";
	    public static final String APPROVE_PENDENCY= "미결";
	    public static final String APPROVE_UNRECEIVE= "미수신";
	    public static final String APPROVE_NOTIFICATE= "수신";
	    public static final String APPROVE_TEMPSAVE= "임시저장";
	    public static final String APPROVE_CANCELLED= "결재취소";
	    public static final String APPROVE_ENTRUST= "위임";
	    
	    public static final String MASTER_WORKING = "작업중";
	    public static final String MASTER_APPROVING = "결재중";
	    public static final String MASTER_APPROVED = "결재완료";
	    public static final String MASTER_REJECTED = "반려";
	    public static final String MASTER_WITHDRAWAL = "회수";
	    public static final String MASTER_REWORKING = "재작성중";
	    public static final String MASTER_INWORK = "작성중";
	    public static final String MASTER_CANCELLED = "취소";
	    public static final String MASTER_CONFIRM = "확인";
	    
	}
	
	public class MESSAGEKEY{
		public static final String CREATE = "등록되었습니다.";
		public static final String UPDATE = "수정되었습니다.";
		public static final String DELETE = "삭제되었습니다.";
		public static final String REVISE = "개정되었습니다.";
		public static final String CHECKIN = "체크인되었습니다.";
		public static final String CHECKOUT = "체크아웃되었습니다.";
		public static final String CHOUTCANCEL = "체크아웃이 취소 되었습니다.";
		public static final String COMPLETE = "완료되었습니다";
		public static final String COMCANCEL = "완료취소 되었습니다.";
		public static final String LINK = "링크등록 하였습니다.";
		public static final String START = "시작되었습니다.";
		public static final String STOP = "중단되었습니다.";
		public static final String RESTART="재시작 되었습니다.";
		
		public static final String CREATE_ERROR = "\n등록에 실패하였습니다. 관리자에게 문의하세요.";
		public static final String UPDATE_ERROR = "\n수정에 실패하였습니다. 관리자에게 문의하세요.";
		public static final String DELETE_ERROR = "\n삭제에 실패하였습니다. 관리자에게 문의하세요.";
		public static final String REVISE_ERROR = "\n개정에 실패하였습니다. 관리자에게 문의하세요.";
		public static final String CHECKOUT_ERROR = "\n체크아웃에 실패하였습니다. 관리자에게 문의하세요.";
		public static final String CHECKING_ERROR = "\n체크아웃되어 있어서 삭제하실 수 없습니다.";
		public static final String COMPLETE_ERROR = "\n완료처리 중에 오류가 발생하였습니다. 관리자에게 문의하세요.";
		public static final String COMCANCEL_ERROR = "\n완료취소 중에 오류가 발생하였습니다. 관리자에게 문의하세요.";
		public static final String LINK_ERROR = "\n링크에 실패하였습니다. 관리자에게 문의하세요.";
		public static final String STOP_ERROR = "\n중단에 실패하였습니다. 관리자에게 문의하세요.";
		public static final String START_ERROR = "\n시작에 실패하였습니다. 관리자에게 문의하세요.";
		public static final String RESTART_ERROR = "\n재시작에 실패하였습니다. 관리자에게 문의하세요.";
		
	}
}
