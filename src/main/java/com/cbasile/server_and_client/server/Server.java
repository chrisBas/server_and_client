package com.cbasile.server_and_client.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cbasile.server_and_client.shared.SocketConnection;

public class Server {

	private static final Logger LOG = LoggerFactory.getLogger(Server.class);

	private final ServerSocket serverSocket;
	private boolean continueListening;
	private ConnectionManager connectionManager;

	/**
	 * @param port is the port number for the server to run on
	 * @param connectionTimeout is the length of time that the connection manager will wait for a connection to send a message
	 * */
	public Server(int port, int connectionTimeout, ProtocolHandler protocolHandler) throws IOException {
		LOG.info("Creating server on port '{}'", port);
		this.serverSocket = new ServerSocket(port);
		this.continueListening = true;
		this.connectionManager = new ConnectionManager(connectionTimeout, protocolHandler);
	}

	public void listen() throws IOException {
		LOG.info("Starting up");
		new Thread(this.connectionManager).start();
		Thread thread = new Thread(() -> {
			while (continueListening) {
				SocketConnection client = null;
				try {
					LOG.debug("Waiting for connection...");
					client = new SocketConnection(serverSocket);
					LOG.debug("Received connection '{}'", client.getRemoteSocketAddress());
					connectionManager.addConnection(client);
				} catch(SocketException e) {
					if ("Socket closed".equals(e.getMessage())) {
						LOG.info("Server is stopping normally");
					} else {
						LOG.error("Unknown/Unhandled exception occurred", e);
					}
				} catch (IOException e) {
					LOG.error("Unknown/Unhandled exception occurred", e);
				}
			}
			LOG.info("Server has stopped normally");
		});
		thread.start();
	}

	public void stop() throws IOException {
		continueListening = false;
		serverSocket.close();
	}
}
