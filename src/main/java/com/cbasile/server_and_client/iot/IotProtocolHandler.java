package com.cbasile.server_and_client.iot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.BiConsumer;

import com.cbasile.server_and_client.server.ProtocolHandler;
import com.google.gson.Gson;

public class IotProtocolHandler implements ProtocolHandler {

	private static final Gson GSON = new Gson();

	private Map<String, IotSession> sessionMap = new HashMap<>();

	@Override
	public String handleMsg(String id, String msg, BiConsumer<String,String> friendlyMsgHandler) {
		IotResponse response = new IotResponse();
		IotSession session = sessionMap.get(id);
		IotRequest request = GSON.fromJson(msg, IotRequest.class);

		if ("register device".equalsIgnoreCase(request.getMessage())) {
			IotSession newSession = new IotSession();
			newSession.setState(IotState.REGISTERED_DEVICE);
			newSession.setAvailable(true);
			newSession.setName(request.getParams().get("name"));
			newSession.setId((new Random()).nextLong());
			sessionMap.put(id, newSession);
			response.setStatusCode(200);
			response.setMessage("successfully registered");
			return GSON.toJson(response);
		} else if ("connect device".equalsIgnoreCase(request.getMessage())) {
			Long idToConnect = Long.parseLong(request.getParams().get("id"));
			for (Entry<String, IotSession> entry : sessionMap.entrySet()) {
				IotSession existingSessions = entry.getValue();
				String existingSessionId = entry.getKey();
				if (existingSessions.getId().equals(idToConnect)) {
					if (existingSessions.getState() != IotState.REGISTERED_DEVICE) {
						response.setStatusCode(400);
						response.setMessage("id is not a registered device");
						return GSON.toJson(response);
					} else if (!existingSessions.isAvailable()) {
						response.setStatusCode(504);
						response.setMessage("device is unavailable");
						return GSON.toJson(response);
					}
					existingSessions.setAvailable(false);
					existingSessions.setLinkedId(id);
					IotSession newSession = new IotSession();
					newSession.setState(IotState.REGISTERED_CONTROLLER);
					newSession.setAvailable(false);
					newSession.setName(request.getParams().get("name"));
					newSession.setId((new Random()).nextLong());
					newSession.setLinkedId(existingSessionId);
					sessionMap.put(id, newSession);
					response.setStatusCode(200);
					response.setMessage("successfully connected device");
					return GSON.toJson(response);
				}
			}
			response.setStatusCode(404);
			response.setMessage("unknown device");
			return GSON.toJson(response);
		} else if ("list".equals(request.getMessage())) {
			List<String> devices = new ArrayList<>();
			for (IotSession existingSessions : sessionMap.values()) {
				if (existingSessions.getState() == IotState.REGISTERED_DEVICE && existingSessions.isAvailable()) {
					devices.add(String.format("%s[%s]", existingSessions.getName(), existingSessions.getId()));
				}
			}
			response.setMessage(String.format("the list of known devices is %s", devices));
			response.setStatusCode(200);
			return GSON.toJson(response);
		} else {
			
			// registered controllers
			if(session.getState() == IotState.REGISTERED_CONTROLLER && !session.isAvailable()) {
				String msgToDevice = request.getMessage();
				
				IotResponse friendlyResponse = new IotResponse();
				friendlyResponse.setMessage(msgToDevice);
				friendlyMsgHandler.accept(session.getLinkedId(), GSON.toJson(friendlyResponse));
				
				response.setMessage("successfully sent message");
				response.setStatusCode(200);
				return GSON.toJson(response);
			}
			// registered device
			else if (session.getState() == IotState.REGISTERED_DEVICE && !session.isAvailable()) {
				String msgToController = request.getMessage();
				
				IotResponse friendlyResponse = new IotResponse();
				friendlyResponse.setMessage(msgToController);
				friendlyMsgHandler.accept(session.getLinkedId(), GSON.toJson(friendlyResponse));
				
				response.setMessage("successfully sent message");
				response.setStatusCode(200);
				return GSON.toJson(response);
			}
			
			response.setMessage("unknown request");
			response.setStatusCode(400);
			return GSON.toJson(response);
		}

	}

}
