package com.cbasile.server_and_client;

import java.io.IOException;

import com.cbasile.server_and_client.iot.IotProtocolHandler;
import com.cbasile.server_and_client.server.Server;

public class ServerMain {

	public static void main(String[] args) throws IOException {
		Server server = new Server(9500, 1, new IotProtocolHandler());
		server.listen();
	}

}
