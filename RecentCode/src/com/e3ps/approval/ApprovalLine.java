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

import java.sql.Timestamp;

import com.e3ps.common.impl.OwnPersistable;
import com.ptc.windchill.annotations.metadata.Cardinality;
import com.ptc.windchill.annotations.metadata.Changeable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.fc.InvalidAttributeException;
import wt.util.WTException;


/**
 *
 * <p>
 * Use the <code>newApprovalLine</code> static factory method(s), not the
 * <code>ApprovalLine</code> constructor, to construct instances of this
 * class.  Instances must be constructed using the static factory(s), in
 * order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(interfaces = OwnPersistable.class,serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="name", type=String.class),
   @GeneratedProperty(name="stepName", type=String.class),
   @GeneratedProperty(name="historyCheck", type=boolean.class, initialValue="false"),
   @GeneratedProperty(name="startTaskDate", type=Timestamp.class),
   @GeneratedProperty(name="role", type=ApproveRoleType.class,
		   constraints=@PropertyConstraints(changeable=Changeable.VIA_OTHER_MEANS, required=true)),
   @GeneratedProperty(name="seq", type=int.class),
   @GeneratedProperty(name="description", type=String.class,
      		constraints=@PropertyConstraints(upperLimit=4000)),
   @GeneratedProperty(name="approveDate", type=Timestamp.class),
   @GeneratedProperty(name="startDate", type=Timestamp.class),
   @GeneratedProperty(name="state", type=ApproveStateType.class),
   @GeneratedProperty(name="readCheck", type=boolean.class,initialValue="false"),
   @GeneratedProperty(name="last", type=boolean.class, initialValue="true"),
   @GeneratedProperty(name="ver", type=int.class, initialValue="0",javaDoc = "반려 후 재상신 에 따른 버전 관리")
   },
   foreignKeys={
   @GeneratedForeignKey(myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="master", type=com.e3ps.approval.ApprovalMaster.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="line", cardinality=Cardinality.ONE_TO_MANY,
  			cascade = true)),
   })
public class ApprovalLine extends _ApprovalLine {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    ApprovalLine
    * @exception wt.util.WTException
    **/
   public static ApprovalLine newApprovalLine()
            throws WTException {

      ApprovalLine instance = new ApprovalLine();
      instance.initialize();
      return instance;
   }

   /**
    * Supports initialization, following construction of an instance.  Invoked
    * by "new" factory having the same signature.
    *
    * @exception wt.util.WTException
    **/
   protected void initialize()
            throws WTException {

   }

   /**
    * Gets the value of the attribute: IDENTITY.
    * Supplies the identity of the object for business purposes.  The identity
    * is composed of name, number or possibly other attributes.  The identity
    * does not include the type of the object.
    *
    *
    * <BR><BR><B>Supported API: </B>false
    *
    * @deprecated Replaced by IdentityFactory.getDispayIdentifier(object)
    * to return a localizable equivalent of getIdentity().  To return a
    * localizable value which includes the object type, use IdentityFactory.getDisplayIdentity(object).
    * Other alternatives are ((WTObject)obj).getDisplayIdentifier() and
    * ((WTObject)obj).getDisplayIdentity().
    *
    * @return    String
    **/
   public String getIdentity() {

      return null;
   }

   /**
    * Gets the value of the attribute: TYPE.
    * Identifies the type of the object for business purposes.  This is
    * typically the class name of the object but may be derived from some
    * other attribute of the object.
    *
    *
    * <BR><BR><B>Supported API: </B>false
    *
    * @deprecated Replaced by IdentityFactory.getDispayType(object) to return
    * a localizable equivalent of getType().  Another alternative is ((WTObject)obj).getDisplayType().
    *
    * @return    String
    **/
   public String getType() {

      return null;
   }

   @Override
   public void checkAttributes()
            throws InvalidAttributeException {

   }

}
