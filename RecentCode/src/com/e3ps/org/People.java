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

package com.e3ps.org;

import wt.content.ContentHolder;
import wt.fc.InvalidAttributeException;
import wt.util.WTException;

import com.ptc.windchill.annotations.metadata.Cardinality;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

/**
 * 0 : ?? 1: ???
 * <p>
 * Use the <code>newPeople</code> static factory method(s), not the <code>People</code>
 * constructor, to construct instances of this class.  Instances must be
 * constructed using the static factory(s), in order to ensure proper initialization
 * of the instance.
 * <p>
 *
 *
 * @version   1.0 
 **/

@GenAsPersistable(interfaces={ContentHolder.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="duty", type=String.class,
      constraints=@PropertyConstraints(upperLimit=30)),
   @GeneratedProperty(name="dutyCode", type=String.class,
      constraints=@PropertyConstraints(upperLimit=30)),
   @GeneratedProperty(name="officeTel", type=String.class,
      constraints=@PropertyConstraints(upperLimit=50)),
   @GeneratedProperty(name="homeTel", type=String.class,
      constraints=@PropertyConstraints(upperLimit=50)),
   @GeneratedProperty(name="cellTel", type=String.class,
      constraints=@PropertyConstraints(upperLimit=50)),
   @GeneratedProperty(name="address", type=String.class,
      constraints=@PropertyConstraints(upperLimit=100)),
   @GeneratedProperty(name="priority", type=int.class,
      constraints=@PropertyConstraints(upperLimit=100)),
   @GeneratedProperty(name="password", type=String.class,
      constraints=@PropertyConstraints(upperLimit=30)),
   @GeneratedProperty(name="isDisable", type=boolean.class, initialValue="false"),
   @GeneratedProperty(name="sortNum", type=int.class,
      javaDoc="0"),
   @GeneratedProperty(name="pwChangeDate", type=String.class,
      constraints=@PropertyConstraints(upperLimit=10)),
   @GeneratedProperty(name="name", type=String.class,
      constraints=@PropertyConstraints(upperLimit=100)),
   @GeneratedProperty(name="title", type=String.class,
      javaDoc="?????"),
   @GeneratedProperty(name="nameEn", type=String.class,
      javaDoc="???"),
   @GeneratedProperty(name="chief", type=boolean.class, initialValue="false"),
   @GeneratedProperty(name="ccCode", type=String.class,
   javaDoc="ccCode", constraints=@PropertyConstraints(upperLimit=90)),
   @GeneratedProperty(name="enterDate", type=String.class,javaDoc="입사일"),
   @GeneratedProperty(name="retrireDate", type=String.class,javaDoc="퇴사일"),
   @GeneratedProperty(name="gradeCode", type=String.class,constraints=@PropertyConstraints(upperLimit=30),javaDoc="직위 코드"),
   @GeneratedProperty(name="gradeName", type=String.class,constraints=@PropertyConstraints(upperLimit=30),javaDoc="직위명"),
   @GeneratedProperty(name="id", type=String.class, javaDoc="유저ID"),
   },
   
   foreignKeys={
   @GeneratedForeignKey(name="WTUserPeopleLink",
      foreignKeyRole=@ForeignKeyRole(name="user", type=wt.org.WTUser.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="people", cardinality=Cardinality.ZERO_TO_ONE)),
   @GeneratedForeignKey(name="DepartmentPeopleLink",
      foreignKeyRole=@ForeignKeyRole(name="department", type=com.e3ps.org.Department.class),
      myRole=@MyRole(name="people"))
   })
public class People extends _People {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    People
    * @exception wt.util.WTException
    **/
   public static People newPeople()
            throws WTException {

      People instance = new People();
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
