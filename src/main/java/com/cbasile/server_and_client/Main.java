package com.cbasile.server_and_client;

import java.io.IOException;

import com.cbasile.server_and_client.server.EchoProtocolHandler;
import com.cbasile.server_and_client.server.Server;
import com.cbasile.server_and_client.shared.SocketConnection;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		int serverPort = 9500;
		Server server = new Server(serverPort, 1, new EchoProtocolHandler());
		server.listen();
		new Thread(() -> {
			try {
				SocketConnection client = new SocketConnection("localhost", serverPort);
				Thread.sleep(200);
				client.sendMsg("hello from A");
				System.out.println(client.getMsg(0));
				client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				SocketConnection client = new SocketConnection("localhost", serverPort);
				Thread.sleep(300);
				client.sendMsg("hello from B");
				System.out.println(client.getMsg(0));
				client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		Thread.sleep(3000);
		server.stop();
		Thread.sleep(1000);
		System.exit(0);
	}

}
