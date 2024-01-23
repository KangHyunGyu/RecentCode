package com.e3ps.change;

import wt.fc.Item;
import wt.util.WTException;

import com.e3ps.common.util.OwnPersistable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.Serialization;

@GenAsPersistable(superClass=Item.class,interfaces={OwnPersistable.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="name", type=String.class),
   @GeneratedProperty(name="description", type=String.class),
   @GeneratedProperty(name="outputType", type=String.class),
   @GeneratedProperty(name="documentNewNumber", type=String.class),
   @GeneratedProperty(name="documentNewVersion", type=String.class),
   @GeneratedProperty(name="documentOldNumber", type=String.class),
   @GeneratedProperty(name="documentOldVersion", type=String.class)
   },
   foreignKeys={
   @GeneratedForeignKey(name="DocumentActivityOutputLink",
      foreignKeyRole=@ForeignKeyRole(name="activity", type=EChangeActivity.class),
      myRole=@MyRole(name="output"))
   })

public class DocumentActivityOutput extends _DocumentActivityOutput {


   static final long serialVersionUID = 1;
   
   public static final String OLD_LINK = "OLD";
   public static final String NEW_LINK = "NEW";
   
   public static DocumentActivityOutput newDocumentActivityOutput()
            throws WTException {

	  DocumentActivityOutput instance = new DocumentActivityOutput();
      instance.initialize();
      return instance;
   }
}
