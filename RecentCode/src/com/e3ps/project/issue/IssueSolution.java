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

import com.e3ps.project.issue.IssueRequest;
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
 * Use the <code>newIssueSolution</code> static factory method(s), not the
 * <code>IssueSolution</code> constructor, to construct instances of this
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
   @GeneratedProperty(name="solution", type=String.class,
      constraints=@PropertyConstraints(upperLimit=4000)),
   @GeneratedProperty(name="solutionDate", type=Timestamp.class)
   },
   foreignKeys={
   @GeneratedForeignKey(name="SolutionLink",
      foreignKeyRole=@ForeignKeyRole(name="request", type=com.e3ps.project.issue.IssueRequest.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="solution")),
   @GeneratedForeignKey(name="SolutionCreatorLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="creator", type=wt.org.WTUser.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="solution"))
   })
public class IssueSolution extends _IssueSolution {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    IssueSolution
    * @exception wt.util.WTException
    **/
   public static IssueSolution newIssueSolution()
            throws WTException {

      IssueSolution instance = new IssueSolution();
      instance.initialize();
      return instance;
   }

}
