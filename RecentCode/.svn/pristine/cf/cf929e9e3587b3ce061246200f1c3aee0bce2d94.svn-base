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

package com.e3ps.doc;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.doc.WTDocument;
import wt.util.WTException;
// Preserved unmodeled dependency
// Preserved unmodeled dependency

/**
 *
 * <p>
 * Use the <code>newE3PSDocument</code> static factory method(s), not the
 * <code>E3PSDocument</code> constructor, to construct instances of this
 * class.  Instances must be constructed using the static factory(s), in
 * order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=WTDocument.class,
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
	   @GeneratedProperty(name="spec", type=String.class,
	   constraints=@PropertyConstraints(upperLimit=200),javaDoc="spec"),
	   @GeneratedProperty(name="maker", type=String.class,
	   constraints=@PropertyConstraints(upperLimit=200),javaDoc="Maker"),
	   @GeneratedProperty(name="model", type=String.class,
	   constraints=@PropertyConstraints(upperLimit=200),javaDoc="모델"),
	   @GeneratedProperty(name="docAttribute", type=String.class,
	   constraints=@PropertyConstraints(upperLimit=200),javaDoc="문서 유형"),
   },
   
   foreignKeys={
   @GeneratedForeignKey( /* first defined by: Iterated */
      foreignKeyRole=@ForeignKeyRole(name="master", type=com.e3ps.doc.E3PSDocumentMaster.class, cascade=false,
         constraints=@PropertyConstraints(required=false)),
      myRole=@MyRole(name="iteration", cascade=false)),
   }
   
)
public class E3PSDocument extends _E3PSDocument {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    E3PSDocument
    * @exception wt.util.WTException
    **/
   public static E3PSDocument newE3PSDocument()
            throws WTException {

      E3PSDocument instance = new E3PSDocument();
      instance.initialize();
      return instance;
   }



}
