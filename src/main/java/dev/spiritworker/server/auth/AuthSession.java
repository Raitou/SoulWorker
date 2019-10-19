package dev.spiritworker.server.auth;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import dev.spiritworker.SpiritWorker;
import dev.spiritworker.netty.SoulWorkerSession;
import io.netty.channel.ChannelHandlerContext;

public class AuthSession extends SoulWorkerSession {
	private AuthServer server;

	private String username;
	private String mac;

	public AuthSession(AuthServer server) {
		this.server = server;
	}
	
	public AuthServer getServer() {
		return this.server;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public void onConnect(ChannelHandlerContext ctx) {
		// Debug
		SpiritWorker.getLogger().info("[AUTH] Client connected from " + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString().toLowerCase());
	}

	@Override
	public void onDisconnect(ChannelHandlerContext ctx) {
		// Debug
		SpiritWorker.getLogger().info("[AUTH] Client disconnected from " + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString().toLowerCase());
	}

	@Override
	protected void handleMessage(int opcode, ByteBuffer packet) {
		AuthPacketHandler.handle(this, opcode, packet);
	}

}
