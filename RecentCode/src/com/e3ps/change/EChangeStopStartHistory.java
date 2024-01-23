package com.e3ps.change;

import java.sql.Timestamp;

import wt.content.ContentHolder;
import wt.fc.Item;
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

@GenAsPersistable(superClass=Item.class,interfaces={OwnPersistable.class, WTContained.class, ContentHolder.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
	   @GeneratedProperty(name="name", type=String.class),
	   @GeneratedProperty(name="stop", type=boolean.class),
	   @GeneratedProperty(name="activeDate", type=Timestamp.class),
	   @GeneratedProperty(name="comments", type=String.class,
	   constraints=@PropertyConstraints(upperLimit=4000))
   },
   foreignKeys={
   @GeneratedForeignKey(myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="eco", type=EChangeOrder2.class),
      myRole=@MyRole(name="stopHistory"))
   })

public class EChangeStopStartHistory extends _EChangeStopStartHistory {


   static final long serialVersionUID = 1;

   public static EChangeStopStartHistory newEChangeStopStartHistory()
            throws WTException {
      
	   EChangeStopStartHistory instance = new EChangeStopStartHistory();
	   instance.initialize();
	   
	   return instance;
   }

}
