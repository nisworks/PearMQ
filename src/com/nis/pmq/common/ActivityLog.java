package com.nis.pmq.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nis.pmq.common.exception.PmqRuntimeException;

public class ActivityLog implements PmqPersister {

	private String dirName;
	private BufferedWriter all;
	private BufferedWriter error;
	private SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd HH.mm.ss.S");
	private boolean persistAll = false;

	public ActivityLog(String dirName, boolean persistAll) {
		this.dirName = dirName;
		all = createSubdirs("all");
		error = createSubdirs("error");
		this.persistAll = persistAll;
	}
	
	public ActivityLog(String dirName) {
		this(dirName,false);
	}
	
	@Override
	public void persistAll(PmqEnvelope envelope) {
		if(persistAll){
			persist(envelope,all);
		}
	}

	@Override
	public void persistError(PmqEnvelope envelope) {
		persist(envelope,error);
		
	}

	private void persist(PmqEnvelope envelope, BufferedWriter bw) {
		writeToFile(JsonUtil.encode(envelope), envelope.getService(), bw);
	}
	
	private String dateToString(){
		return dt.format(new Date());
	}

	private BufferedWriter createSubdirs(String type) {

		new File(dirName).mkdirs();

		try {
			File file = new File(dirName + "/"+dateToString()+"-"+type+".json");
			
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			return new BufferedWriter(fw);
		} catch (IOException e) {
			throw new PmqRuntimeException(e);
		}

	}

	private void writeToFile(String content, String service, BufferedWriter bw) {

		try {

			bw.write(content + "\r\n");
			//bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isPersistAll() {
		return persistAll;
	}

	public void setPersistAll(boolean persistAll) {
		this.persistAll = persistAll;
	}

}
