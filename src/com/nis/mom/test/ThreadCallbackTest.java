package com.nis.mom.test;

import org.junit.Test;

public class ThreadCallbackTest {

	private Object synchObj = new Object();
	
	@Test
	public void test() {
		System.out.println("start");
		stopThread();
		System.out.println("restart");
	}
	
	public void stopThread(){
		final Thread thread = Thread.currentThread();
		new Thread(new Runnable() {
			public void run() {
				
				try {
					System.out.println("delay");
					thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				System.out.println("callback");
				thread.interrupt();
			}
		}).start();
		try {
			System.out.println("sleep");
			thread.sleep(10000);
			
		} catch (InterruptedException e) {
			System.out.println("restarted");
			//e.printStackTrace();
		}
	}

}
