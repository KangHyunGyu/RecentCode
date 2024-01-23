package com.e3ps.doc;

import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.GeneratedRole;
import com.ptc.windchill.annotations.metadata.Serialization;
import com.ptc.windchill.annotations.metadata.TableProperties;

import wt.fc.InvalidAttributeException;
import wt.fc.ObjectToObjectLink;
import wt.util.WTException;

@GenAsBinaryLink(superClass=ObjectToObjectLink.class, 
serializable=Serialization.EXTERNALIZABLE_BASIC,
properties={
		   @GeneratedProperty(name="disabled", type=boolean.class),
		   @GeneratedProperty(name="sort", type=int.class)
},
roleA=@GeneratedRole(name="docCode", type=DocCodeType.class),
roleB=@GeneratedRole(name="valueDefiniton", type=DocValueDefinition.class),
tableProperties=@TableProperties(tableName="DocCodeToValueDefinitionLink")
)
public class DocCodeToValueDefinitionLink extends _DocCodeToValueDefinitionLink{
static final long serialVersionUID = 1;
	
	public static DocCodeToValueDefinitionLink newDocCodeToNumberCodeLink()
            throws WTException {
		DocCodeToValueDefinitionLink instance = new DocCodeToValueDefinitionLink();
		instance.initialize();
		return instance;
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
