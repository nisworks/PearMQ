package com.nis.pmq.common.exception;

public class PmqSocketException extends PmqException {

	public PmqSocketException() {
		super();
	}

	public PmqSocketException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
	}

	public PmqSocketException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public PmqSocketException(String s) {
		super(s);
	}

	public PmqSocketException(Throwable throwable) {
		super(throwable);
	}

}
