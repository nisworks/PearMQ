package com.nis.pmq.common.exception;

public class PmqRuntimeException extends RuntimeException {

	public PmqRuntimeException() {
		super();
	}

	public PmqRuntimeException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
	}

	public PmqRuntimeException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public PmqRuntimeException(String s) {
		super(s);
	}

	public PmqRuntimeException(Throwable throwable) {
		super(throwable);
	}

}
