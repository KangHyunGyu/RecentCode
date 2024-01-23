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

package com.e3ps.project;

import wt.content.ContentHolder;
import wt.fc.Item;
import wt.util.WTException;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;

/**
 * ??/??? ??
 * <p>
 * Use the <code>newProjectStopHistory</code> static factory method(s),
 * not the <code>ProjectStopHistory</code> constructor, to construct instances
 * of this class.  Instances must be constructed using the static factory(s),
 * in order to ensure proper initialization of the instance.
 * <p>
 *
 *
 * @version   1.0
 **/

@GenAsPersistable(superClass=Item.class, interfaces={ContentHolder.class}, 
   serializable=Serialization.EXTERNALIZABLE_BASIC,
   properties={
   @GeneratedProperty(name="historyComment", type=String.class,
      constraints=@PropertyConstraints(upperLimit=4000)),
   @GeneratedProperty(name="gubun", type=String.class,
      javaDoc="STOP/START")
   },
   foreignKeys={
   @GeneratedForeignKey(name="ProjectStopHistoryLink",
      foreignKeyRole=@ForeignKeyRole(name="project", type=com.e3ps.project.EProject.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="history", cascade=false))
   })
public class ProjectStopHistory extends _ProjectStopHistory {


   static final long serialVersionUID = 1;




   /**
    * Default factory for the class.
    *
    * @return    ProjectStopHistory
    * @exception wt.util.WTException
    **/
   public static ProjectStopHistory newProjectStopHistory()
            throws WTException {

      ProjectStopHistory instance = new ProjectStopHistory();
      instance.initialize();
      return instance;
   }

}
