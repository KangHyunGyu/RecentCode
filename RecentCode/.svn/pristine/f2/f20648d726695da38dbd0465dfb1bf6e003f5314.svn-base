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
import wt.enterprise.Managed;
import wt.inf.container.WTContained;
import wt.util.WTException;

import com.e3ps.common.util.OwnPersistable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.Serialization;

/**
 *
 * <p>
 * Use the <code>newEChangeRequest2</code> static factory method(s), not
 * the <code>EChangeRequest2</code> constructor, to construct instances
 * of this class.  Instances must be constructed using the static factory(s),
 * in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Managed.class,interfaces={OwnPersistable.class, WTContained.class, ContentHolder.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="requestNumber", type=String.class),
   @GeneratedProperty(name="name", type=String.class),
   @GeneratedProperty(name="applyDate", type=String.class),
   @GeneratedProperty(name="specificDate", type=Timestamp.class),
   @GeneratedProperty(name="description", type=String.class),
   @GeneratedProperty(name="echangeReason", type=String.class),
   @GeneratedProperty(name="customer", type=String.class),
   @GeneratedProperty(name="equipmentName", type=String.class),
   @GeneratedProperty(name="completeHopeDate", type=Timestamp.class),
   })
public class EChangeRequest2 extends _EChangeRequest2 {


   static final long serialVersionUID = 1;

   /**
    * Default factory for the class.
    *
    * @return    EChangeRequest2
    * @exception wt.util.WTException
    **/
   public static EChangeRequest2 newEChangeRequest2()
            throws WTException {

      EChangeRequest2 instance = new EChangeRequest2();
      instance.initialize();
      return instance;
   }
}
