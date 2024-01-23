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


import com.e3ps.common.impl.OwnPersistable;
import com.ptc.windchill.annotations.metadata.ColumnProperties;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;
import com.ptc.windchill.annotations.metadata.StringCase;

import wt.content.ContentHolder;
import wt.fc.Item;
import wt.util.WTException;

/**
 *
 * <p>
 * Use the <code>newMultiApproval</code> static factory method(s), not the
 * <code>MultiApproval</code> constructor, to construct instances of this
 * class.  Instances must be constructed using the static factory(s), in
 * order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Item.class,interfaces = {ContentHolder.class, OwnPersistable.class},serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="objectType", type=String.class),	
   @GeneratedProperty(name="state", type=ApproveStateType.class),
   @GeneratedProperty(name="name", type=String.class),
   @GeneratedProperty(name="number", type=String.class,
   constraints=@PropertyConstraints(stringCase=StringCase.UPPER_CASE, upperLimit=20),
   columnProperties=@ColumnProperties(columnName="mNumber")),
   @GeneratedProperty(name="description", type=String.class,
		constraints=@PropertyConstraints(upperLimit=4000)),
   })
public class MultiApproval extends _MultiApproval {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    MultiApproval
    * @exception wt.util.WTException
    **/
   public static MultiApproval newMultiApproval()
            throws WTException {

      MultiApproval instance = new MultiApproval();
      instance.initialize();
      return instance;
   }

   
}
