package dev.spiritworker.game.managers;

import dev.spiritworker.net.packet.PacketBuilder;
import dev.spiritworker.server.world.WorldServer;
import dev.spiritworker.server.world.WorldSession;

public class ChatManager {
	private final WorldServer server;
	
	public ChatManager(WorldServer server) {
		this.server = server;
	}

	public WorldServer getServer() {
		return server;
	}
	
	public void handleNormalChat(WorldSession session, String message) {
		if (message.charAt(0) == '!') {
			CommandHandler.handle(session.getCharacter(), message);
			return;
		}
		
		session.getCharacter().getMap().broadcastPacket(PacketBuilder.sendClientChatNormal(session, message));
	}
	
	public void handlePartyChat(WorldSession session, String message) {
		// TODO
	}
	
	public void handleWhisperChat(WorldSession session, String message) {
		
	}
}
