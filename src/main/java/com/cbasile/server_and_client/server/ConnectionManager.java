package com.cbasile.server_and_client.server;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cbasile.server_and_client.shared.SocketConnection;

public class ConnectionManager implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);

	private final ReentrantLock lock = new ReentrantLock(true);
	private final List<SocketConnection> connections;
	private final int connectionTimeout;
	private final ProtocolHandler protocolHandler;

	/**
	 * = * @param connectionTimeout is the length of time to wait for a connection
	 * to send a message
	 */
	public ConnectionManager(int connectionTimeout, ProtocolHandler protocolHandler) {
		this.connections = Collections.synchronizedList(new ArrayList<>());
		this.connectionTimeout = connectionTimeout;
		this.protocolHandler = protocolHandler;
	}

	public void addConnection(SocketConnection connection) {
		lock.lock();
		try {
			this.connections.add(connection);
			LOG.debug("New connection registered '{}' ({} total connections)", connection.getRemoteSocketAddress(), this.connections.size());
		} finally {
			lock.unlock();

		}
	}

	public void run() {
		LOG.info("Listening for messages from connections");
		while (true) {
			lock.lock();
			try {
				Iterator<SocketConnection> it = connections.iterator();
				while (it.hasNext()) {
					SocketConnection connection = it.next();
					try {
						String msg = connection.getMsg(this.connectionTimeout);
						if (msg == null) {
							// socket is closed
							LOG.debug("Connection '{}' closed ({} total connections)", connection.getRemoteSocketAddress(), this.connections.size()-1);
							connection.stop();
							it.remove();
						} else {
							LOG.trace("Received message '{}' from '{}'", msg, connection.getRemoteSocketAddress());
							connection.sendMsg(protocolHandler.handleMsg(connection.getRemoteSocketAddress().toString(), msg));
						}
					} catch (SocketTimeoutException e) {
						// do nothing, this means the read did not complete in the timeout
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} finally {
				lock.unlock();
			}
		}
	}

}
