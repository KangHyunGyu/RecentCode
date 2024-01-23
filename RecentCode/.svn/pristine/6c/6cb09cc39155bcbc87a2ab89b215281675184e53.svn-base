package com.e3ps.change;

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


@GenAsPersistable(superClass=Item.class,
	interfaces={OwnPersistable.class, WTContained.class},
	properties={
		   @GeneratedProperty(name="description", type=String.class)
		   },
	foreignKeys={
		   @GeneratedForeignKey(name="ActivityTargetLink", myRoleIsRoleA=false,
		      foreignKeyRole=@ForeignKeyRole(name="activity", type=com.e3ps.change.EChangeActivity.class,
		         constraints=@PropertyConstraints(required=false)),
		         myRole=@MyRole(name="target")),
		   @GeneratedForeignKey(name="ActivityOldTargetLink", myRoleIsRoleA=false,
		      foreignKeyRole=@ForeignKeyRole(name="objOld", type=wt.vc.Versioned.class,
		         constraints=@PropertyConstraints(required=false)),
		         myRole=@MyRole(name="target")),
		   @GeneratedForeignKey(name="ActivityNewTargetLink", myRoleIsRoleA=false,
		      foreignKeyRole=@ForeignKeyRole(name="objNew", type=wt.vc.Versioned.class,
		      	 constraints=@PropertyConstraints(required=false)),
			     myRole=@MyRole(name="target"))
		   }
)
public class ActivityTarget extends _ActivityTarget {

   static final long serialVersionUID = 1;
   
   public static ActivityTarget newActivityTarget( )
            throws WTException {

	   ActivityTarget instance = new ActivityTarget();
      instance.initialize();
      return instance;
   }

}
