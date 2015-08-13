package com.nis.pmq.common;

public interface PmqPersister {

	
	
	public void persistAll(PmqEnvelope envelope);
	public void persistError(PmqEnvelope envelope);
	
	public void setPersistAll(boolean persistAll);
	public boolean isPersistAll();
	
}
