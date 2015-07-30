package com.nis.pmq.common.exception;

public abstract class PmqException extends Exception {

	public PmqException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PmqException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
		// TODO Auto-generated constructor stub
	}

	public PmqException(String s, Throwable throwable) {
		super(s, throwable);
		// TODO Auto-generated constructor stub
	}

	public PmqException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public PmqException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
