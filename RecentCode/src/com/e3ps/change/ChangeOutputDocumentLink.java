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

import com.e3ps.change.ChangeOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ClassNotFoundException;
import java.lang.Object;
import java.lang.String;
import java.sql.SQLException;
import wt.enterprise.Master;
import wt.fc.ObjectToObjectLink;
import wt.pds.PersistentRetrieveIfc;
import wt.pds.PersistentStoreIfc;
import wt.pom.DatastoreException;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import com.ptc.windchill.annotations.metadata.*;

/**
 *
 * <p>
 * Use the <code>newChangeOutputDocumentLink</code> static factory method(s),
 * not the <code>ChangeOutputDocumentLink</code> constructor, to construct
 * instances of this class.  Instances must be constructed using the static
 * factory(s), in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsBinaryLink(superClass=ObjectToObjectLink.class, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="branchIdentifier", type=long.class),
   @GeneratedProperty(name="docClassName", type=String.class)
   },
   roleA=@GeneratedRole(name="master", type=Master.class,
      cardinality=Cardinality.ONE_TO_MANY),
   roleB=@GeneratedRole(name="output", type=ChangeOutput.class,
      cardinality=Cardinality.ONE),
   tableProperties=@TableProperties(tableName="ChangeOutputDocumentLink")
)
public class ChangeOutputDocumentLink extends _ChangeOutputDocumentLink {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @param     master
    * @param     output
    * @return    ChangeOutputDocumentLink
    * @exception wt.util.WTException
    **/
   public static ChangeOutputDocumentLink newChangeOutputDocumentLink( Master master, ChangeOutput output )
            throws WTException {

      ChangeOutputDocumentLink instance = new ChangeOutputDocumentLink();
      instance.initialize( master, output );
      return instance;
   }

}
