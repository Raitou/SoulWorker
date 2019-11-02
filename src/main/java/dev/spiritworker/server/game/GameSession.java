package dev.spiritworker.server.game;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.netty.SoulWorkerSession;
import io.netty.channel.ChannelHandlerContext;

public class GameSession extends SoulWorkerSession {
	private GameServer server;

	private List<GameCharacter> characters;
	
	public GameSession(GameServer server) {
		this.server = server;
		this.characters = new ArrayList<GameCharacter>();
	}
	
	public GameServer getServer() {
		return this.server;
	}
	
	public synchronized List<GameCharacter> getCharacters() {
		return this.characters;
	}
	
	public synchronized void setCharacterList(List<GameCharacter> list) {
		for (GameCharacter character : list) {
			character.getInventory().loadCosmetics();
		}
		this.characters = list;
	}

	@Override
	public void onConnect(ChannelHandlerContext ctx) {
		// Debug
		SpiritWorker.getLogger().info("[GAME] Client connected from " + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString().toLowerCase());
	}

	@Override
	public void onDisconnect(ChannelHandlerContext ctx) {
		// Debug
		SpiritWorker.getLogger().info("[GAME] Client disconnected from " + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString().toLowerCase());
	}

	@Override
	protected void handleMessage(int opcode, ByteBuffer packet) {
		// Log
		if (SpiritWorker.getConfig().LOG_PACKETS) {
			this.logPacket(packet);
		}
		
		// Handle
		GamePacketHandler.handle(this, opcode, packet);
	}
}
