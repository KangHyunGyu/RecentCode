package com.e3ps.stagegate;

import com.e3ps.common.impl.OwnPersistable;
import com.ptc.windchill.annotations.metadata.ForeignKeyRole;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedForeignKey;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.MyRole;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.fc.InvalidAttributeException;
import wt.util.WTException;

@GenAsPersistable(interfaces = OwnPersistable.class,serializable=Serialization.EXTERNALIZABLE_BASIC,
properties={
	@GeneratedProperty(name="code", type=String.class), 
	@GeneratedProperty(name="version", type=int.class),
	@GeneratedProperty(name="remark", type=String.class),
	@GeneratedProperty(name="lastVersion", type=boolean.class, initialValue="true"),
},
foreignKeys={
		@GeneratedForeignKey(name="StageGateMasterLink",
			foreignKeyRole=@ForeignKeyRole(name="stageGate", type=StageGate.class),
			myRole=@MyRole(name="objMaster")
		),
})
public class SGObjectMaster extends _SGObjectMaster{
	static final long serialVersionUID = 1;

	public static SGObjectMaster newSGObjectMaster() throws WTException {
		SGObjectMaster instance = new SGObjectMaster();
		instance.initialize();
		return instance;
		
	}
	
	protected void initialize()
            throws WTException {

   }

	@Override
	public void checkAttributes() throws InvalidAttributeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

}
