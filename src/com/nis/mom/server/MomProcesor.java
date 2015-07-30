package com.nis.mom.server;

import java.io.InputStream;
import java.io.OutputStream;

public interface MomProcesor {
	
	public String processRequest(String payload, MomRequest request);

}
