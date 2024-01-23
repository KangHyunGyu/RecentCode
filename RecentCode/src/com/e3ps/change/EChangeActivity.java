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

package com.e3ps.change;

import java.sql.Timestamp;

import wt.content.ContentHolder;
import wt.fc.Item;
import wt.fc.Persistable;
import wt.inf.container.WTContained;
import wt.util.WTException;

import com.e3ps.common.util.OwnPersistable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

/**
 *
 * <p>
 * Use the <code>newEChangeActivity</code> static factory method(s), not
 * the <code>EChangeActivity</code> constructor, to construct instances
 * of this class.  Instances must be constructed using the static factory(s),
 * in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Item.class,interfaces={OwnPersistable.class, WTContained.class, ContentHolder.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="name", type=String.class),
   @GeneratedProperty(name="step", type=String.class),
   @GeneratedProperty(name="activeState", type=String.class),
   @GeneratedProperty(name="finishDate", type=Timestamp.class),
   @GeneratedProperty(name="ecafinishDate", type=Timestamp.class),
   @GeneratedProperty(name="sortNumber", type=int.class),
   @GeneratedProperty(name="activeType", type=String.class,
      javaDoc="CHIEF , WORKING"),
   @GeneratedProperty(name="description", type=String.class,
   constraints=@PropertyConstraints(upperLimit=4000)),
   @GeneratedProperty(name="comments", type=String.class,
   constraints=@PropertyConstraints(upperLimit=4000))
   },
   foreignKeys={
   @GeneratedForeignKey(name="OrderActivityLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="order", type=Persistable.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="activity")),
   @GeneratedForeignKey(name="ActivityDefinitionLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="definition", type=com.e3ps.change.EChangeActivityDefinition.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="activity"))
   })
public class EChangeActivity extends _EChangeActivity {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    EChangeActivity
    * @exception wt.util.WTException
    **/
   public static EChangeActivity newEChangeActivity()
            throws WTException {
      
      EChangeActivity instance = new EChangeActivity();
      instance.initialize();
      return instance;
   }

}
