package com.cbasile.server_and_client.iot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import com.cbasile.server_and_client.shared.SocketConnection;
import com.google.gson.Gson;

public class IotSocketConnection extends SocketConnection {
	
	private static final Gson GSON = new Gson();

	public IotSocketConnection(ServerSocket serverSocket) throws IOException {
		super(serverSocket);
	}
	
	public IotSocketConnection(String address, int port) throws UnknownHostException, IOException {
		super(address, port);
	}
	
	public void sendMsg(IotRequest iotRequest) {
		super.sendMsg(GSON.toJson(iotRequest));
	}
	
	public IotResponse getResponse(int timeout) throws IOException {
		String response = this.getMsg(timeout);
		return GSON.fromJson(response, IotResponse.class);
	}

}
