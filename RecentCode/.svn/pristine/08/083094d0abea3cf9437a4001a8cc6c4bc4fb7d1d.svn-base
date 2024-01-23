package com.e3ps.common.mail;

import wt.doc.WTDocument;
import wt.fc.WTObject;
import com.e3ps.common.util.StringUtil;
import com.e3ps.org.bean.PeopleData;

public class MailContentData {

    public String number;
    public String name;
    public String creatorName;
    public String creatorDept;
    public String type;
    
    public MailContentData(){}
    
    public MailContentData(WTObject pbo){
        
        if(pbo instanceof WTDocument){
        	WTDocument doc = (WTDocument) pbo;
            number = doc.getNumber();
            name = doc.getName();
            type = "문서";
            try {
                PeopleData creator = new PeopleData(doc.getCreator().getObject());
                creatorName = StringUtil.checkNull(creator.getName());
                creatorDept = StringUtil.checkNull(creator.getDepartmentName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
