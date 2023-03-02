package com.cbasile.server_and_client.server;

public class EchoProtocolHandler implements ProtocolHandler {

	@Override
	public String handleMsg(String id, String msg) {
		return String.format("echo '%s'", msg);
	}

}
