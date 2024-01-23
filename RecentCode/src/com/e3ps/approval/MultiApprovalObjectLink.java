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

import com.e3ps.approval.MultiApproval;
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
 * Use the <code>newApprovalToObjectLink</code> static factory method(s),
 * not the <code>ApprovalToObjectLink</code> constructor, to construct instances
 * of this class.  Instances must be constructed using the static factory(s),
 * in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsBinaryLink(superClass=ObjectToObjectLink.class, 
   versions={2538346186404157511L},
   roleA=@GeneratedRole(name="multi", type=MultiApproval.class,
      cardinality=Cardinality.ONE),
   roleB=@GeneratedRole(name="obj", type=Persistable.class),
   tableProperties=@TableProperties(tableName="MultiApprovalObjectLink")
)
public class MultiApprovalObjectLink extends _MultiApprovalObjectLink {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @param     approve
    * @param     obj
    * @return    ApprovalToObjectLink
    * @exception wt.util.WTException
    **/
   public static MultiApprovalObjectLink newApprovalToObjectLink( MultiApproval approve, Persistable obj )
            throws WTException {

      MultiApprovalObjectLink instance = new MultiApprovalObjectLink();
      instance.initialize( approve, obj );
      return instance;
   }

   /**
    * Reads the non-transient fields of this class from an external source.
    *
    * @param     input
    * @param     readSerialVersionUID
    * @param     superDone
    * @return    boolean
    * @exception java.io.IOException
    * @exception java.lang.ClassNotFoundException
    **/
   boolean readVersion2538346186404157511L( ObjectInput input, long readSerialVersionUID, boolean superDone )
            throws IOException, ClassNotFoundException {

      if ( !superDone )                                             // if not doing backward compatibility
         super.readExternal( input );                               // handle super class


      return true;
   }

}
