package com.cbasile.server_and_client.iot;

import java.util.HashMap;
import java.util.Map;

import com.cbasile.server_and_client.server.ProtocolHandler;
import com.google.gson.Gson;

public class IotProtocolHandler implements ProtocolHandler {

	private static final Gson GSON = new Gson();

	private Map<String, IotSession> sessionMap = new HashMap<>();

	@Override
	public String handleMsg(String id, String msg) {
		IotResponse response = new IotResponse();
		IotSession session = sessionMap.get(id);
		IotRequest request = GSON.fromJson(msg, IotRequest.class);

		if (session == null) {
			if ("register".equalsIgnoreCase(request.getMessage())) {
				IotSession newSession = new IotSession();
				newSession.setState(IotState.REGISTERED);
				sessionMap.put(id, newSession);
				response.setStatusCode(200);
				response.setMessage("successfully registered");
				return GSON.toJson(response);
			} else {
				response.setMessage("failed to register, try one of the following commands [register]");
				response.setStatusCode(400);
				return GSON.toJson(response);
			}
		} else {
			if(session.getState() == IotState.REGISTERED) {
				if("list".equals(request.getMessage())) {
					// TODO: implement real business logic
					response.setMessage("the list of known devices is [thermostat,stove,doorbell]");
					response.setStatusCode(200);
					return GSON.toJson(response);
				}
			}
			
			response.setMessage("unknown request");
			response.setStatusCode(404);
			return GSON.toJson(response);
		}
	}

}
