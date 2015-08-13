package com.nis.pmq.common;

public class EmptyPersister implements PmqPersister {

	@Override
	public void persistAll(PmqEnvelope envelope) {
		// TODO Auto-generated method stub

	}

	@Override
	public void persistError(PmqEnvelope envelope) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPersistAll(boolean persistAll) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPersistAll() {
		// TODO Auto-generated method stub
		return false;
	}

}
