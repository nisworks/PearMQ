package com.nis.pmq.common.exception;

public class PmqServiceException extends PmqException {

	public PmqServiceException() {
		super();
	}

	public PmqServiceException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
	}

	public PmqServiceException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public PmqServiceException(String s) {
		super(s);
	}

	public PmqServiceException(Throwable throwable) {
		super(throwable);
	}

}
