package com.nis.pmq.common.exception;

public abstract class PmqException extends Exception {

	public PmqException() {
		super();
	}

	public PmqException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
	}

	public PmqException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public PmqException(String s) {
		super(s);
	}

	public PmqException(Throwable throwable) {
		super(throwable);
	}

}
