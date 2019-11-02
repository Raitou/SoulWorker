package dev.spiritworker.server.auth;

import java.nio.ByteBuffer;

import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.game.AccessKey;
import dev.spiritworker.game.AccountData;
import dev.spiritworker.net.packet.PacketBuilder;
import dev.spiritworker.net.packet.PacketOpcodes;
import dev.spiritworker.net.packet.PacketUtils;
import dev.spiritworker.util.ServerData;

public class AuthPacketHandler {
	
	public static void handle(AuthSession session, int opcode, ByteBuffer packet) {
		// Auth check
		if (!session.isAuthenticated()) {
			if (opcode == PacketOpcodes.ClientLoginGameForge) { // Called when the client first connects to the game server
				handleClientLoginGameForge(session, packet);
			} else if (opcode == PacketOpcodes.ClientLogoutServerReq) {
				handleClientLogoutServerListReq(session, packet);
			}
			return;
		}
		
		switch (opcode) {
			case PacketOpcodes.ClientServerListRequest:
				handleClientServerListRequest(session);
				break;
			case PacketOpcodes.ClientServerConnectRequest:
				handleClientServerConnectRequest(session, packet);
				break;
			default:
				// Unhandled packet
				// Logger.log.info("Unhandled packet: " + opcode + " Length: " + packet.capacity());
				break;
		}
	}

	// Login packet using username + password
	private static void handleClientLogin(AuthSession session, ByteBuffer packet) {
		String username = PacketUtils.readString16(packet);
		String password = PacketUtils.readString16(packet);
		String mac = PacketUtils.readString8(packet);
		
		// TODO: Authenticate here
		
		// Send result
		session.sendPacket(PacketBuilder.sendClientLoginResult(session));
	}
	
	private static void handleClientLogoutServerListReq(AuthSession session, ByteBuffer packet) {
		int id = packet.getInt();
		
		// Authenticate
		boolean validated = AccessKey.validate(session, id);
		
		// Bad key
		if (!validated) {
			return;
		}
		
		// Get account
		AccountData account = DatabaseHelper.getAccountById(id);
		
		if (account == null) {
			return;
		}
		
		// Set session
		session.setUsername(account.getUsername());
		session.setAccountId(account.getId());
		
		// Send result
		handleClientServerListRequest(session);
		//session.sendPacket(PacketBuilder.sendClientLoginResult(session));
	}

	// Login packet using auth token
	private static void handleClientLoginGameForge(AuthSession session, ByteBuffer packet) {
		String token = PacketUtils.readString16(packet);
		String mac = PacketUtils.readString8(packet);
		
		if (token == null || mac == null) {
			return;
		}
		
		session.setMac(mac);
		
		// Authenticate here
		AccountData account = DatabaseHelper.getAccountByToken(token);
		
		if (account == null) {
			return;
		}
		
		// TODO Delete auth token
		
		// Set session
		session.setUsername(account.getUsername());
		session.setAccountId(account.getId());
		
		// Send result
		session.sendPacket(PacketBuilder.sendClientLoginResult(session));
	}
	
	private static void handleClientServerListRequest(AuthSession session) {
		session.sendPacket(PacketBuilder.sendClientServerList(session));
		session.sendPacket(PacketBuilder.sendClientOptionLoad(session));
	}
	
	private static void handleClientServerConnectRequest(AuthSession session, ByteBuffer packet) {
		int serverId = packet.get();
		ServerData server = session.getServer().getServerDataById(serverId);
		
		if (server != null) {
			// Add access key
			DatabaseHelper.createAccessKey(session);
			
			// Send packet
			session.sendPacket(PacketBuilder.sendClientServerSelect(session, server.getAddress()));
		}
	}
	
}
