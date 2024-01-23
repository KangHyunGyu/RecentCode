package com.e3ps.doc;

import com.e3ps.project.EProject;
import com.ptc.windchill.annotations.metadata.Cardinality;
import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;
import com.ptc.windchill.annotations.metadata.Serialization;
import com.ptc.windchill.annotations.metadata.TableProperties;

import wt.fc.ObjectToObjectLink;
import wt.util.WTException;

@GenAsBinaryLink(superClass = 
ObjectToObjectLink.class, 
serializable = Serialization.EXTERNALIZABLE_BASIC, 
roleA = @GeneratedRole(
	name = "document", 
	type = E3PSDocument.class, 
	cardinality = Cardinality.ONE, 
	cascade = false), 
roleB = @GeneratedRole(
	name = "project", 
	type = EProject.class, 
	cardinality = Cardinality.MANY, 
	cascade = false), 
tableProperties = @TableProperties(tableName = "DocProjectLink"))
public class DocProjectLink extends _DocProjectLink{

	static final long serialVersionUID = 1;

	public static DocProjectLink newDocProjectLink(E3PSDocument doc, EProject pjt) throws WTException {
		DocProjectLink instance = new DocProjectLink();
		instance.initialize(doc, pjt);
		return instance;
	}
	
}
