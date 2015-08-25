package com.nis.pmq.test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class DeterministicTest {

	MessageDigest md;
	
	@Before
	public void setUp() throws NoSuchAlgorithmException{
		md  = MessageDigest.getInstance("MD5");
	}
	
	@Test	
	public void test3() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println(tokenize("Adam S這dowy",2));
		System.out.println(tokenize("adam S這dowy",2));
		System.out.println(tokenize("adam s這dowy",2));
		System.out.println(tokenize("edam s這dowy",2));
		System.out.println(tokenize("idam s這dowb",2));
		System.out.println(tokenize("idam s這dowy",2));
	}
	
	
	public void test2() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		long milis= System.currentTimeMillis();
		for(int i=0; i<1000; i++){
			System.out.println(tokenize(UUID.randomUUID().toString(),5));
			//tokenize(UUID.randomUUID().toString(),5);
		}
		System.out.println("milis "+(System.currentTimeMillis()-milis));
	}
	
	private int tokenize(String token, int range) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		
		
		byte[] array = md.digest((token+"sdfjdjfamcsiucceamweucrmaeurpcaiotufmpioufp").getBytes("UTF-8"));
		int sum = 0;
		for(int i=0; i<array.length; i++){
			sum = sum^unsignedToBytes(array[i]); 
			
		}
		//System.out.println(sum);
		return (int) (sum % range);
	}

	public static int unsignedToBytes(byte b) {
	    return b & 0xFF;
	}
}
