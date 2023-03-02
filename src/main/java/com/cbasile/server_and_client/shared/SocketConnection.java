package com.cbasile.server_and_client.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SocketConnection {

	private final Socket socket;
	private final PrintWriter out;
	private final BufferedReader in;

	/**
	 * A SocketHolder abstracts the reading and writing streams to simplify IO. In
	 * addition, this constructor is specifically used on ServerSockets waiting for
	 * connections
	 */
	public SocketConnection(ServerSocket serverSocket) throws IOException {
		try {
			this.socket = serverSocket.accept();
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			this.stop();
			throw e;
		}
	}

	/**
	 * A SocketHolder abstracts the reading and writing streams to simplify IO.
	 */
	public SocketConnection(String address, int port) throws UnknownHostException, IOException {
		try {
			this.socket = new Socket(address, port);
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			this.stop();
			throw e;
		}
	}

	public void stop() {
		if (this.out != null)
			this.out.close();
		try {
			if (this.in != null)
				this.in.close();
		} catch (IOException e) {
			// do nothing
		}
		try {
			if (this.socket != null)
				this.socket.close();
		} catch (IOException e) {
			// do nothing
		}
	}

	public SocketAddress getRemoteSocketAddress() {
		return this.socket.getRemoteSocketAddress();
	}

	public String getMsg(int timeout) throws IOException {
		this.socket.setSoTimeout(timeout);
		String msg = this.in.readLine();
		return msg;
	}

	public void sendMsg(String string) {
		this.out.println(string);
	}

}
