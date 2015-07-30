package com.nis.mom.test;

import org.junit.Test;

import com.nis.mom.client.SocketClient;
import com.nis.mom.common.exception.MomSocketException;
import com.nis.mom.server.SocketServer;

public class SimpleTest {

	@Test
	public void test() throws MomSocketException {
		System.out.println("init server");
		new Thread(new Runnable() {
			public void run() {
				SocketServer server = new SocketServer(666,null);
				server.openSocket();
			}
		}).start();

		System.out.println("init client");
		SocketClient client = new SocketClient("localhost", 666, null);
		client.openSocket("testService");
	}

	@Test
	public void testTimeout() {
		System.out.println("init server timeout");

		SocketServer server = new SocketServer(666,null);
		server.openSocket();
		System.out.println("server timeout");
	}

}
