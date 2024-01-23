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

import wt.org.WTPrincipalReference;

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

@GenAsPersistable(interfaces={ScheduleNode.class},
   properties={
   @GeneratedProperty(name="status", type=String.class,
      constraints=@PropertyConstraints(upperLimit=50)),
   @GeneratedProperty(name="creator", type=WTPrincipalReference.class,
      constraints=@PropertyConstraints(required=true))
   },
   foreignKeys={
   @GeneratedForeignKey(name="ProjectTaskLink",
      foreignKeyRole=@ForeignKeyRole(name="project", type=com.e3ps.project.EProjectNode.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="task"))
   })
public interface ETaskNode extends _ETaskNode {






}
