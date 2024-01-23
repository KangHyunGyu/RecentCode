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

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;

import wt.iba.value.IBAHolder;

/**
 *
 * @version   1.0
 **/

@GenAsPersistable(interfaces={ScheduleNode.class,IBAHolder.class},
   properties={
   @GeneratedProperty(name="code", type=String.class,
      constraints=@PropertyConstraints(upperLimit=50)),
   @GeneratedProperty(name="version", type=int.class),
   @GeneratedProperty(name="lastVersion", type=boolean.class, initialValue="true"),
   @GeneratedProperty(name="outputType", type=OutputType.class,
      constraints=@PropertyConstraints(required=true))
   },
   foreignKeys={
   @GeneratedForeignKey(name="DivisionLink", myRoleIsRoleA=false,
      foreignKeyRole=@ForeignKeyRole(name="product", type=wt.pdmlink.PDMLinkProduct.class,
         constraints=@PropertyConstraints(required=true)),
      myRole=@MyRole(name="project"))
   })
public interface EProjectNode extends _EProjectNode {






}
