package com.e3ps.distribute;

import java.sql.Timestamp;

import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.Serialization;
import wt.enterprise.Managed;
import wt.fc.IdentificationObject;
import wt.inf.container.WTContained;
import wt.org.DirectoryContextProvider;
import wt.org.WTOrganization;
import wt.org.WTPrincipal;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;


@GenAsPersistable(superClass= Managed.class, serializable=Serialization.EXTERNALIZABLE_BASIC, interfaces = {WTContained.class},
	properties={
			@GeneratedProperty(name="distributeNumber", type=String.class, constraints=@PropertyConstraints(upperLimit=200),javaDoc="배포 번호"),
			@GeneratedProperty(name="distributeName", type=String.class, constraints=@PropertyConstraints(upperLimit=200),javaDoc="배포 명"),
			@GeneratedProperty(name="distributeType", type=String.class, constraints=@PropertyConstraints(upperLimit=200),javaDoc="배포 타입"),
			@GeneratedProperty(name="companyId", type=String.class, constraints=@PropertyConstraints(upperLimit=200),javaDoc="협력업체 아이디"),
			@GeneratedProperty(name="companyName", type=String.class, constraints=@PropertyConstraints(upperLimit=200),javaDoc="협력업체 명"),
			@GeneratedProperty(name="downloadCount", type=int.class, javaDoc="다운로드 횟수", initialValue="0"),
			@GeneratedProperty(name="purpose", type=String.class, javaDoc="용도"),
			@GeneratedProperty(name="markingConfirm", type=String.class, javaDoc="표기 확인"),
			@GeneratedProperty(name="fileType", type=String.class, javaDoc="파일 형식"),
			@GeneratedProperty(name="distributeDate", type=Timestamp.class, javaDoc="배포 일자"),
			@GeneratedProperty(name="downloadDeadline", type=Timestamp.class, javaDoc="다운로드 기한"),
			@GeneratedProperty(name="withdraw", type=String.class, javaDoc="회수"),
			@GeneratedProperty(name="description", type=String.class, constraints=@PropertyConstraints(upperLimit=4000), javaDoc="기타 기입 사항"),
	}
)
public class DistributeDocument extends _DistributeDocument{

	static final long serialVersionUID = 1;

	public static DistributeDocument newDistributeDocument() throws WTException{
		DistributeDocument instance = new DistributeDocument();
		instance.initialize();
		return instance;
	}

}
