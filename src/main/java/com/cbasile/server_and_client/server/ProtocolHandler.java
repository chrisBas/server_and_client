package com.cbasile.server_and_client.server;

import java.util.function.BiConsumer;

public interface ProtocolHandler {
	
	public String handleMsg(String id, String msg, BiConsumer<String, String> friendlyMsgHandler);

}
