package com.e3ps.admin;

import com.ptc.windchill.annotations.metadata.Cardinality;
import com.ptc.windchill.annotations.metadata.GenAsBinaryLink;
import com.ptc.windchill.annotations.metadata.GeneratedRole;
import com.ptc.windchill.annotations.metadata.OracleTableSize;
import com.ptc.windchill.annotations.metadata.TableProperties;

import wt.enterprise.Master;
import wt.fc.ObjectToObjectLink;
import wt.org.WTUser;
import wt.util.WTException;

/**
*
* <p>
* Use the <code>newMasterACLWTUserLink</code> static factory method(s), not
* the <code>MasterACLWTUserLink</code> constructor, to construct instances of
* this class. Instances must be constructed using the static factory(s), in
* order to ensure proper initialization of the instance.
* <p>
*
*
* @version 1.0
**/
@GenAsBinaryLink(
        superClass = ObjectToObjectLink.class,
        versions = {2538346186404157511L},
        roleA = @GeneratedRole(name = "wtuser", type = WTUser.class),
        roleB = @GeneratedRole(
                name = "master",
                type = Master.class,
                cardinality = Cardinality.ONE),
        tableProperties = @TableProperties(
                tableName = "MasterACLWTUserLink",
                oracleTableSize = OracleTableSize.LARGE))
public class MasterACLWTUserLink extends _MasterACLWTUserLink{

	static final long serialVersionUID = 1;

    /**
     * Default factory for the class.
     *
     * @param wtuser
     * @param master
     * @return MasterACLWTUserLink
     * @exception wt.util.WTException
     **/
    public static MasterACLWTUserLink newMasterACLWTUserLink(WTUser wtuser,
            Master master) throws WTException{
    	MasterACLWTUserLink instance = new MasterACLWTUserLink();
	instance.initialize(wtuser, master);
	return instance;
    }
}
