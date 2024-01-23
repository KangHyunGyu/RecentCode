package com.e3ps.stagegate;

import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.content.ContentHolder;
import wt.enterprise.Managed;
import wt.inf.container.WTContained;
import wt.util.WTException;

@GenAsPersistable(
		superClass=Managed.class, 
		interfaces={ContentHolder.class, WTContained.class}, 
		serializable=Serialization.EXTERNALIZABLE_BASIC,
properties={
		@GeneratedProperty(name="code", type=String.class), // = Project Code
},
foreignKeys={
		@GeneratedForeignKey(name="ProjectStageGateLink",
			foreignKeyRole=@ForeignKeyRole(name="project", type=com.e3ps.project.EProject.class),
			myRole=@MyRole(name="stageGate")
		),
})
public class StageGate extends _StageGate{
	static final long serialVersionUID = 1;

	public static StageGate newStageGate() throws WTException {
		StageGate instance = new StageGate();
		instance.initialize();
		return instance;
	}
}
