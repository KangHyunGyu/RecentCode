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

import com.e3ps.common.util.OwnPersistable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.content.ContentHolder;
import wt.enterprise.Managed;
import wt.inf.container.WTContained;
import wt.util.WTException;

/**
 *
 * <p>
 * Use the <code>newEChangeOrder2</code> static factory method(s), not the
 * <code>EChangeOrder2</code> constructor, to construct instances of this
 * class.  Instances must be constructed using the static factory(s), in
 * order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Managed.class,interfaces={WTContained.class, OwnPersistable.class, ContentHolder.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="applyDate", type=String.class),		//적용요구시점
   @GeneratedProperty(name="specificDate", type=Timestamp.class),  //특정일시
   @GeneratedProperty(name="name", type=String.class),		//품명
   @GeneratedProperty(name="orderNumber", type=String.class),//번호
   @GeneratedProperty(name="carType", type=String.class), //차종
   @GeneratedProperty(name="changeDesc", type=String.class),//내역
   @GeneratedProperty(name="changeOwner", type=String.class),//주관
   @GeneratedProperty(name="upg", type=String.class),//UPG
   @GeneratedProperty(name="process", type=String.class),
   @GeneratedProperty(name="description", type=String.class),
   @GeneratedProperty(name="echangeReason", type=String.class),
   @GeneratedProperty(name="customer", type=String.class),
   @GeneratedProperty(name="isAutoERP", type=boolean.class, initialValue="false")
   },
   foreignKeys={
   @GeneratedForeignKey(name="EcoCompleteLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="complete", type=com.e3ps.approval.CommonActivity.class),
      myRole=@MyRole(name="eco")),
   @GeneratedForeignKey(name="EcoCompleteWorkLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="completeWork", type=com.e3ps.approval.CommonActivity.class),
      myRole=@MyRole(name="eco"))
   })
public class EChangeOrder2 extends _EChangeOrder2 {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    EChangeOrder2
    * @exception wt.util.WTException
    **/
   public static EChangeOrder2 newEChangeOrder2()
            throws WTException {

      EChangeOrder2 instance = new EChangeOrder2();
      instance.initialize();
      return instance;
   }
}
