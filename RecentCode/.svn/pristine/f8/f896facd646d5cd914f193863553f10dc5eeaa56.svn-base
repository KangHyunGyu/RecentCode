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

@GenAsPersistable(superClass=Item.class,interfaces={OwnPersistable.class, WTContained.class, ContentHolder.class}, 
   properties={
   @GeneratedProperty(name="noticeNumber", type=String.class)
	},
   foreignKeys={
   @GeneratedForeignKey(myRoleIsRoleA=false, 
      foreignKeyRole=@ForeignKeyRole(name="eco", type=com.e3ps.change.EChangeOrder2.class),
      myRole=@MyRole(name="ecn"))
   })
public class EChangeNotice extends _EChangeNotice {

   static final long serialVersionUID = 1;

   public static EChangeNotice newEChangeNotice()
            throws WTException {

	   EChangeNotice instance = new EChangeNotice();
      instance.initialize();
      return instance;
   }
}
