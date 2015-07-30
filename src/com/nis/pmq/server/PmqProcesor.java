package com.nis.pmq.server;

import java.io.InputStream;
import java.io.OutputStream;

public interface PmqProcesor {
	
	public String processRequest(String payload, PmqRequest request);

}
