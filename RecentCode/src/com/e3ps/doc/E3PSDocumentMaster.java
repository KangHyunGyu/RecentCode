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

import wt.doc.WTDocumentMaster;
import wt.util.WTException;

import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.Serialization;

/**
 *
 * <p>
 * Use the <code>newE3PSDocumentMaster</code> static factory method(s),
 * not the <code>E3PSDocumentMaster</code> constructor, to construct instances
 * of this class.  Instances must be constructed using the static factory(s),
 * in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=WTDocumentMaster.class,
   serializable=Serialization.EXTERNALIZABLE_BASIC)
public class E3PSDocumentMaster extends _E3PSDocumentMaster {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    E3PSDocumentMaster
    * @exception wt.util.WTException
    **/
   public static E3PSDocumentMaster newE3PSDocumentMaster()
            throws WTException {

      E3PSDocumentMaster instance = new E3PSDocumentMaster();
      instance.initialize();
      return instance;
   }

}
