/* bcwti
 *
 * Copyright (c) 2008 Parametric Technology Corporation (PTC). All Rights
 * Reserved.
 *
 * This software is the confidential and proprietary information of PTC
 * and is subject to the terms of a software license agreement. You shall
 * not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement.
 *
 * ecwti
 */

package com.e3ps.approval;

import com.e3ps.approval.ApprovalMaster;
import java.io.IOException;
import java.io.ObjectInput;
import java.lang.ClassNotFoundException;
import wt.fc.ObjectToObjectLink;
import wt.fc.Persistable;
import wt.util.WTException;
import com.ptc.windchill.annotations.metadata.*;


/**
 *
 * <p>
 * Use the <code>newApprovalObjectLink</code> static factory method(s),
 * not the <code>ApprovalObjectLink</code> constructor, to construct instances
 * of this class.  Instances must be constructed using the static factory(s),
 * in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsBinaryLink(superClass=ObjectToObjectLink.class, serializable=Serialization.EXTERNALIZABLE_BASIC,
   roleA=@GeneratedRole(name="obj", type=Persistable.class,
      cardinality=Cardinality.ONE_TO_MANY),
   roleB=@GeneratedRole(name="appMaster", type=ApprovalMaster.class,
			cascade = true),
   tableProperties=@TableProperties(tableName="ApprovalObjectLink")
)
public class ApprovalObjectLink extends _ApprovalObjectLink {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @param     obj
    * @param     request
    * @return    ApprovalObjectLink
    * @exception wt.util.WTException
    **/
   public static ApprovalObjectLink newApprovalObjectLink( Persistable obj, ApprovalMaster appMaster )
            throws WTException {

      ApprovalObjectLink instance = new ApprovalObjectLink();
      instance.initialize( obj, appMaster );
      return instance;
   }


}
