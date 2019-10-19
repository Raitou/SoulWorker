package dev.spiritworker.util;

import java.net.InetSocketAddress;

public class ServerData {
	private final String name;
	private int id;
	private final String ip;
	private final int port;
	private final InetSocketAddress address;
	
	public ServerData(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.address = new InetSocketAddress(ip, port);
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	
}
