package com.e3ps.change;

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

@GenAsPersistable(superClass=Item.class,interfaces={OwnPersistable.class, WTContained.class, ContentHolder.class}, 
   properties={
   @GeneratedProperty(name="changeType", type=String.class),
   @GeneratedProperty(name="partNumber", type=String.class),
   @GeneratedProperty(name="partName", type=String.class),
   @GeneratedProperty(name="drawingNumber", type=String.class),
   @GeneratedProperty(name="stockQuantity", type=String.class),
   @GeneratedProperty(name="orderQuantity", type=String.class),
   @GeneratedProperty(name="sign", type=String.class),
   @GeneratedProperty(name="signDrawing", type=String.class),
   @GeneratedProperty(name="stock", type=String.class),
   @GeneratedProperty(name="applyDate", type=String.class),
   @GeneratedProperty(name="before", type=String.class,
		      constraints=@PropertyConstraints(upperLimit=4000)),
   @GeneratedProperty(name="after", type=String.class,
		      constraints=@PropertyConstraints(upperLimit=4000)),
   @GeneratedProperty(name="procurement", type=String.class),
   @GeneratedProperty(name="logistics", type=String.class),
   @GeneratedProperty(name="etc", type=String.class,
   constraints=@PropertyConstraints(upperLimit=4000))
	},
   foreignKeys={
   @GeneratedForeignKey(myRoleIsRoleA=false, 
      foreignKeyRole=@ForeignKeyRole(name="notice", type=com.e3ps.change.EChangeNotice.class),
      myRole=@MyRole(name="item"))
   })
public class EChangeNoticeItem extends _EChangeNoticeItem {

   static final long serialVersionUID = 1;

   public static EChangeNoticeItem newEChangeNoticeItem()
            throws WTException {

	   EChangeNoticeItem instance = new EChangeNoticeItem();
      instance.initialize();
      return instance;
   }
}
