package com.cbasile.server_and_client;

import java.io.IOException;

import com.cbasile.server_and_client.iot.IotProtocolHandler;
import com.cbasile.server_and_client.iot.IotRequest;
import com.cbasile.server_and_client.iot.IotSocketConnection;
import com.cbasile.server_and_client.server.Server;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		int serverPort = 9500;
		Server server = new Server(serverPort, 1, new IotProtocolHandler());
		server.listen();
		new Thread(() -> {
			try {
				IotSocketConnection client = new IotSocketConnection("localhost", serverPort);
				Thread.sleep(200);
				IotRequest request = new IotRequest();
				request.setMessage("register");
				client.sendMsg(request);
				System.out.println(client.getMsg(0));
				
				request.setMessage("list");
				client.sendMsg(request);
				System.out.println(client.getMsg(0));
				client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				IotSocketConnection client = new IotSocketConnection("localhost", serverPort);
				Thread.sleep(300);
				IotRequest request = new IotRequest();
				request.setMessage("bad request");
				client.sendMsg(request);
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
