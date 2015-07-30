package com.nis.mom.common.exception;

public abstract class MomException extends Exception {

	public MomException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MomException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
		// TODO Auto-generated constructor stub
	}

	public MomException(String s, Throwable throwable) {
		super(s, throwable);
		// TODO Auto-generated constructor stub
	}

	public MomException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public MomException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
