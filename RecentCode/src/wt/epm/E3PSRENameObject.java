package wt.epm;
import wt.fc.Identified;
import wt.fc.IdentityHelper;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.part.WTPartMasterIdentity;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

import com.e3ps.common.util.CommonUtil;
import com.e3ps.common.util.StringUtil;

public class E3PSRENameObject implements wt.method.RemoteAccess, java.io.Serializable {
	
	
	static final boolean SERVER = wt.method.RemoteMethodServer.ServerFlag;
	public static E3PSRENameObject manager= new E3PSRENameObject();
	
	/**
	 * EPMDocumetn number,name,CadName 변경
	 * @param epm
	 * @param changeNumber
	 * @param changeName
	 * @param changeCadName
	 * @return
	 */
	public void EPMReName(EPMDocument epm,String changeNumber,String changeName, String changeCadName) throws Exception{
		
			changeNumber = StringUtil.checkNull(changeNumber);
			changeName = StringUtil.checkNull(changeName);
			changeCadName = StringUtil.checkNull(changeCadName);
            
			if(changeNumber.length()==0 && changeName.length()==0 && changeCadName.length()==0) return;
			
			Identified identified = (Identified) epm.getMaster();
            EPMDocumentMasterIdentity emid = (EPMDocumentMasterIdentity) identified.getIdentificationObject();

            if(changeNumber.length()>0) emid.setNumber(changeNumber.trim());
            if(changeName.length()>0) emid.setName(changeName.trim());
            if(changeCadName.length()>0) emid.setCADName(changeCadName.trim());
            
            IdentityHelper.service.changeIdentity(identified, emid);
            PersistenceHelper.manager.refresh(epm);
            
            if(changeCadName.length()>0) this.cadNameSpaceChange(epm);
            
        
	}
	
	/**
	 * WTPart number,name 변경
	 * @param part
	 * @param changeNumber
	 * @param changeName
	 * @return
	 */
	public void PartReName(WTPart part,String changeNumber,String changeName)throws Exception{
		
		
        Identified identified = (Identified) part.getMaster();
        WTPartMasterIdentity partid = (WTPartMasterIdentity)identified.getIdentificationObject();
        partid.setNumber(changeNumber.trim());
        partid.setName(changeName.trim());
      
        IdentityHelper.service.changeIdentity(identified, partid);
        PersistenceHelper.manager.refresh(part);
		
	}
	
	/**
	 * EPMCADNamespace CAD Change
	 * @param epm
	 */
	private  void cadNameSpaceChange(EPMDocument epm){
		
		 EPMDocumentMaster master = (EPMDocumentMaster)epm.getMaster();
		 long masterlong = CommonUtil.getOIDLongValue(master); 
		 try {
			QuerySpec spec = new QuerySpec(EPMCADNamespace.class);
			spec.appendWhere(new SearchCondition(EPMCADNamespace.class,"masterReference.key.id",SearchCondition.EQUAL,masterlong));
			
			QueryResult rt =PersistenceHelper.manager.find(spec);
			
			while(rt.hasMoreElements()){
				EPMCADNamespace space =(EPMCADNamespace)rt.nextElement();
				space.setCADName(epm.getCADName());
				space=(EPMCADNamespace)PersistenceHelper.manager.modify(space);
			}
		} catch (WTException e) {
			e.printStackTrace();
		} catch (WTPropertyVetoException e) {
			e.printStackTrace();
		}
	 }
	
	
	
	/**
	 * 확장자 빠진 cad명
	 * @param changeName
	 * @return
	 */
	private String getCadName(String changeName){
		return changeName.substring(0,changeName.indexOf("."));
		
	}
	
	/**
	 * cad명에서 확장자
	 * @param changeName
	 * @return
	 */
	private String getPrefix(String changeName){
		
		return changeName.substring(changeName.indexOf(".")+1);
	}
	
	public String getExtentionName(String changeName) {
		String extName = this.getPrefix(changeName);
		return extName;
	}
	
	
}
