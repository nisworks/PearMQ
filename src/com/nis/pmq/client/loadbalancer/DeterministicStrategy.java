package com.nis.pmq.client.loadbalancer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.nis.pmq.client.ClientRequest;
import com.nis.pmq.client.ServiceMetadata;
import com.nis.pmq.client.SocketClient;
import com.nis.pmq.common.exception.PmqRuntimeException;

public class DeterministicStrategy implements LoadBalancerStrategy {
	
	private String salt = "sdfjdjfamcsiucceamweucrmaeurpcaiotufmpioufp";
	List<SocketClient> list = new ArrayList<SocketClient>();
	MessageDigest md;
	
	

	public DeterministicStrategy() {
		super();
		try {
			md  = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new PmqRuntimeException(e);
		}
	}

	@Override
	public synchronized SocketClient chooseServer(ClientRequest clientRequest, ServiceMetadata socketStatsData) {
		
		if(clientRequest.getDimension()==null){
			throw new PmqRuntimeException("Dimension cannot be empty");
		}
		System.out.println(clientRequest.getService()+" "+list.size());
		int decision = tokenize(clientRequest.getDimension(), list.size());
		return list.get(decision);
	}

	@Override
	public synchronized void registerSocket(SocketClient socket) {
		if(!list.contains(socket)){
			list.add(socket);
		}
		
	}

	@Override
	public synchronized void removeSocket(SocketClient socket) {
		if(!list.contains(socket)){
			list.remove(socket);
		}
	}
	
	
	
	public void setSalt(String salt) {
		this.salt = salt;
	}

	private int tokenize(String token, int range){
				
		try {
			byte[] array = md.digest((token+salt).getBytes("UTF-8"));
			int sum = 0;
			for(int i=0; i<array.length; i++){
				sum = sum^unsignedToBytes(array[i]); 
				
			}
			return (int) (sum % range);
		} catch (UnsupportedEncodingException e) {
			throw new PmqRuntimeException(e);
		}
	}

	public static int unsignedToBytes(byte b) {
	    return b & 0xFF;
	}

}
