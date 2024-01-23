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

import java.sql.Timestamp;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;

/**
 *
 * @version   1.0
 **/

@GenAsPersistable(
   properties={
   @GeneratedProperty(name="name", type=String.class,
      javaDoc="node ??"),
   @GeneratedProperty(name="planStartDate", type=Timestamp.class,
      javaDoc="?? ???"),
   @GeneratedProperty(name="planEndDate", type=Timestamp.class,
      javaDoc="?? ???"),
   @GeneratedProperty(name="startDate", type=Timestamp.class,
      javaDoc="???"),
   @GeneratedProperty(name="endDate", type=Timestamp.class,
      javaDoc="???"),
   @GeneratedProperty(name="description", type=String.class,
      javaDoc="??",
      constraints=@PropertyConstraints(upperLimit=4000)),
   @GeneratedProperty(name="completion", type=double.class,
      javaDoc="???"),
   @GeneratedProperty(name="manDay", type=double.class,
      javaDoc="????"),
   @GeneratedProperty(name="sort", type=int.class)
   },
   foreignKeys={
   @GeneratedForeignKey(name="ParentChildLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="parent", type=com.e3ps.project.ScheduleNode.class),
      myRole=@MyRole(name="child"))
   })
public interface ScheduleNode extends _ScheduleNode {






}
