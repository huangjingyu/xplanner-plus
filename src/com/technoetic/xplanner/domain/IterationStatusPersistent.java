package com.technoetic.xplanner.domain;

import org.hibernate.type.EnumType;

//FIXME: Rewrite it's just stub for compilation

public class IterationStatusPersistent extends EnumType {
	private static final long serialVersionUID = -7341092190386630551L;

	@Override
	public Class<IterationStatus> returnedClass() {
		return IterationStatus.class;
	}

}
