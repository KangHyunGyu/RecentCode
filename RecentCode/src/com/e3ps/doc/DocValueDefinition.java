package com.e3ps.doc;

import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.Serialization;

import wt.fc.InvalidAttributeException;
import wt.util.WTException;

@GenAsPersistable(
		   serializable=Serialization.EXTERNALIZABLE_BASIC,
		   properties={
			   @GeneratedProperty(name="code", type=String.class),
			   @GeneratedProperty(name="description", type=String.class),
			   @GeneratedProperty(name="name", type=String.class),
			   @GeneratedProperty(name="disabled", type=boolean.class),
			   @GeneratedProperty(name="engName", type=String.class),
			   @GeneratedProperty(name="inputType", type=String.class,javaDoc = "SELECT,DATE,TEXT"),
			   @GeneratedProperty(name="numberCodeType", type=String.class),
			   @GeneratedProperty(name="sort", type=int.class),
		   }
)	
public class DocValueDefinition extends _DocValueDefinition{
	static final long serialVersionUID = 1;
	
	public static DocValueDefinition newDocValueDefinition()
            throws WTException {

		DocValueDefinition instance = new DocValueDefinition();
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
