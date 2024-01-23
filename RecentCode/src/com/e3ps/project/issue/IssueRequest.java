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

package com.e3ps.project.issue;

import com.e3ps.project.ETaskNode;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ClassNotFoundException;
import java.lang.Object;
import java.lang.String;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import wt.content.ContentHolder;
import wt.content.HttpContentOperation;
import wt.fc.Item;
import wt.fc.ObjectReference;
import wt.org.WTUser;
import wt.pds.PersistentRetrieveIfc;
import wt.pds.PersistentStoreIfc;
import wt.pom.DatastoreException;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import com.ptc.windchill.annotations.metadata.*;

/**
 *
 * <p>
 * Use the <code>newIssueRequest</code> static factory method(s), not the
 * <code>IssueRequest</code> constructor, to construct instances of this
 * class.  Instances must be constructed using the static factory(s), in
 * order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Item.class, interfaces={ContentHolder.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="name", type=String.class),
   @GeneratedProperty(name="problem", type=String.class,
      constraints=@PropertyConstraints(upperLimit=4000)),
   @GeneratedProperty(name="requestDate", type=Timestamp.class),
   @GeneratedProperty(name="deadLine", type=Timestamp.class),
   @GeneratedProperty(name="state", type=String.class,
      constraints=@PropertyConstraints(upperLimit=50)),
   @GeneratedProperty(name="issueType", type=String.class)
   },
   foreignKeys={
   @GeneratedForeignKey(name="IssueCreatorLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="creator", type=wt.org.WTUser.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="issueRequest")),
   @GeneratedForeignKey(name="IssueManager", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="manager", type=wt.org.WTUser.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="issueRequest")),
   @GeneratedForeignKey(name="TaskRequestLink",
      foreignKeyRole=@ForeignKeyRole(name="task", type=com.e3ps.project.ETaskNode.class),
      myRole=@MyRole(name="request"))
   })
public class IssueRequest extends _IssueRequest {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    IssueRequest
    * @exception wt.util.WTException
    **/
   public static IssueRequest newIssueRequest()
            throws WTException {

      IssueRequest instance = new IssueRequest();
      instance.initialize();
      return instance;
   }

}
