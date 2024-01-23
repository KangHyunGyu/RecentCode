package ext.ptc.dpa;

import wt.epm.EPMDocument;

public class CADWorkerFilterE3PS {
	public static Boolean isPositioningAssembly(EPMDocument doc) {
		return Boolean.TRUE;
	}

	public static Boolean isFileSync(EPMDocument doc) {
		return Boolean.TRUE;
	}
}
