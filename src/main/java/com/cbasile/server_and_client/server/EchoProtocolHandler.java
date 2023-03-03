package com.cbasile.server_and_client.server;

import java.util.function.BiConsumer;

public class EchoProtocolHandler implements ProtocolHandler {

	@Override
	public String handleMsg(String id, String msg, BiConsumer<String,String> friendlyMsgHandler) {
		return String.format("echo '%s'", msg);
	}

}
