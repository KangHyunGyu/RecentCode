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

import com.e3ps.common.util.OwnPersistable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ClassNotFoundException;
import java.lang.Object;
import java.lang.String;
import java.sql.SQLException;

import wt.fc.InvalidAttributeException;
import wt.fc.Item;
import wt.fc.ObjectReference;
import wt.fc.PersistInfo;
import wt.fc.Persistable;
import wt.inf.container.WTContained;
import wt.introspection.ClassInfo;
import wt.introspection.WTIntrospectionException;
import wt.introspection.WTIntrospector;
import wt.org.WTPrincipalReference;
import wt.pds.PersistentRetrieveIfc;
import wt.pds.PersistentStoreIfc;
import wt.pom.DatastoreException;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

import com.ptc.windchill.annotations.metadata.*;

/**
 *
 * <p>
 * Use the <code>newCommonActivity</code> static factory method(s), not
 * the <code>CommonActivity</code> constructor, to construct instances of
 * this class.  Instances must be constructed using the static factory(s),
 * in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Item.class,interfaces={OwnPersistable.class,WTContained.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="gubun", type=String.class),
   @GeneratedProperty(name="title", type=String.class)
   },
   foreignKeys={
   @GeneratedForeignKey(name="ActivityItemLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="item", type=wt.fc.Persistable.class),
      myRole=@MyRole(name="activity"))
   })
public class CommonActivity extends _CommonActivity {


   static final long serialVersionUID = 1;

   /**
    * Default factory for the class.
    *
    * @return    CommonActivity
    * @exception wt.util.WTException
    **/
   public static CommonActivity newCommonActivity()
            throws WTException {

      CommonActivity instance = new CommonActivity();
      instance.initialize();
      return instance;
   }

}
