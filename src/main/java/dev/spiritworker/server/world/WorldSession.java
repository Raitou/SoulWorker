package dev.spiritworker.server.world;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.net.packet.PacketOpcodes;
import dev.spiritworker.netty.SoulWorkerSession;
import dev.spiritworker.server.game.GameServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class WorldSession extends SoulWorkerSession {
	private WorldServer server;
	private GameCharacter character;
	
	public WorldSession(WorldServer server) {
		this.server = server;
	}
	
	public WorldServer getServer() {
		return this.server;
	}
	
	public GameCharacter getCharacter() {
		return this.character;
	}
	
	public void setCharacter(GameCharacter character) {
		this.character = character;
	}
	
	@Override
	public boolean isAuthenticated() {
		return super.isAuthenticated() && this.getCharacter() != null;
	}
	
	public GameServer getGameServer() {
		return this.getServer().getGameServer();
	}
	
	@Override
	public void onConnect(ChannelHandlerContext ctx) {
		// Debug
		SpiritWorker.getLogger().info("[WORLD] Client connected from " + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString().toLowerCase());
	}

	@Override
	public void onDisconnect(ChannelHandlerContext ctx) {
		// Debug
		SpiritWorker.getLogger().info("[WORLD] Client disconnected from " + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString().toLowerCase());
		
		// Save character to db
		if (getCharacter() != null) {
			// Remove from map and gameserver
			if (getCharacter().getMap() != null) {
				getCharacter().getMap().removeCharacter(character);
			} 
			getGameServer().removeCharacter(getCharacter());
			// Save to database
			getCharacter().save();
		}
	}

	@Override
	protected void handleMessage(int opcode, ByteBuffer packet) {
		// Log
		if (opcode != PacketOpcodes.ClientKeepAlive && SpiritWorker.getConfig().LOG_PACKETS) {
			ByteBuf b = Unpooled.wrappedBuffer(packet.array());
			this.logPacket(b);
		}
		
		// Handle
		WorldPacketHandler.handle(this, opcode, packet);
	}
}
