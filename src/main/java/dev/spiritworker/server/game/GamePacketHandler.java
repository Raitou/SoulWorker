package dev.spiritworker.server.game;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.game.AccessKey;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.inventory.Item;
import dev.spiritworker.net.packet.ErrorCodes;
import dev.spiritworker.net.packet.PacketBuilder;
import dev.spiritworker.net.packet.PacketOpcodes;
import dev.spiritworker.net.packet.PacketUtils;

public class GamePacketHandler {
	
	public static void handle(GameSession session, int opcode, ByteBuffer packet) {
		// Auth check
		if (!session.isAuthenticated()) {
			if (opcode == PacketOpcodes.ClientConnectGameServer) { // Called when the client first connects to the game server
				handleClientConnectGameServer(session, packet);
			} else if (opcode == PacketOpcodes.ClientLogoutServerReq) {
				handleClientLogoutServerReq(session, packet);
			}
			return;
		}
		
		switch (opcode) {
			case PacketOpcodes.ClientCreateCharacter:
				handleClientCreateCharacter(session, packet);
				break;
			case PacketOpcodes.ClientDeleteCharacter:
				handleClientDeleteCharacter(session, packet);
				break;
			case PacketOpcodes.ClientCharacterListRequest:
				handleClientCharacterListRequest(session);
				break;
			case PacketOpcodes.ClientSelectCharacter:
				handleClientSelectCharacter(session, packet);
				break;
			case PacketOpcodes.ClientEnterGameServer: // Called when the client presses play
				handleClientPlayGameRequest(session, packet);
				break;
			case PacketOpcodes.ClientRequestLogout: // Called when the client tries to go back to the server list from the character list screen
				handleClientRequestLogout(session, packet);
				break;
			default:
				SpiritWorker.getLogger().info("Unhandled packet: " + opcode + " Length: " + packet.capacity());
				break;
		}
	}

	private static void handleClientLogoutServerReq(GameSession session, ByteBuffer packet) {
		int accountId = packet.getInt();
		
		// Authenticate
		boolean validated = AccessKey.validate(session, accountId);
		
		// Bad key
		if (!validated) {
			return;
		}
		
		// Set account id
		session.setAccountId(accountId);
		
		// Get characters from database
		session.setCharacterList(DatabaseHelper.getCharacterList(session));
		
		handleClientCharacterListRequest(session);
	}

	private static void handleClientConnectGameServer(GameSession session, ByteBuffer packet) {
		int accountId = packet.getInt();
		int serverId = packet.getShort();
		
		// Authenticate
		boolean validated = AccessKey.validate(session, accountId);
		
		// Bad key
		if (!validated) {
			return;
		}

		// Set account id
		session.setAccountId(accountId);
		
		// Get characters from database
		session.setCharacterList(DatabaseHelper.getCharacterList(session));
		
		// Send packet
		session.sendPacket(PacketBuilder.sendClientConnectGameResponse(session));
		session.sendPacket(PacketBuilder.sendServerDate());
	}

	private static void handleClientCharacterListRequest(GameSession session) {
		session.sendPacket(PacketBuilder.sendClientCharacterList(session));
		session.sendPacket(PacketBuilder.sendServerSpecialOption(session));
	}
	
	private static void handleClientCreateCharacter(GameSession session, ByteBuffer packet) {
		// Character size check
		if (session.getCharacters().size() > 7) {
			return;
		}
		
		// Get packet data
		int unknown = packet.getInt();
		String name = PacketUtils.readString16(packet);
		int type = packet.getShort();
		
		packet.getInt();
		
		short hairStyle = packet.getShort();
		short hairColor = packet.getShort();
		short eyeColor = packet.getShort();
		short skinColor = packet.getShort();
		
		// Unknowns
		packet.getInt();
		packet.getInt();
		int slot = packet.get();
		
		// Sanity checks
		if (type < 1 || type > 7) {
			return;
		}
		
		// Name must be within 2-12 characters
		if (name.length() < 2 || name.length() > 12) {
			return;
		}
		
		// Check if name is in use
		if (DatabaseHelper.isCharacterNameTaken(name)) {
			session.sendPacket(PacketBuilder.sendErrorCode(ErrorCodes.CHARACTER_CREATION_NAME_TAKEN));
			return;
		}
		
		// Create character
		GameCharacter character = DatabaseHelper.createCharacter(session);
		
		if (character == null) {
			return;
		}
		
		// Set details about that character
		character.setName(name);
		character.setType(type);
		character.setHairStyle(hairStyle);
		character.setHairColor(hairColor);
		character.setEyeColor(eyeColor);
		character.setSkinColor(skinColor);
		
		// Save to db
		DatabaseHelper.saveCharacter(character);

		// Add to character list
		session.getCharacters().add(character);
		
		// Add inventory items
		Item soulWeapon = new Item(110011301 + (1000000 * type));
		character.getInventory().getEquippedItems().putItem(0, soulWeapon);
		soulWeapon.save();

		// Send packets
		session.sendPacket(PacketBuilder.sendClientCharacterList(session));
		session.sendPacket(PacketBuilder.sendServerSpecialOption(session));
	}
	
	private static void handleClientDeleteCharacter(GameSession session, ByteBuffer packet) {
		int characterId = packet.getInt();
		
		GameCharacter character = session.getCharacters().stream().filter(c -> c.getId() == characterId).findFirst().orElseGet(null);
		
		// Invalid character
		if (character == null) {
			return;
		}
		
		// Delete from database
		boolean deleted = DatabaseHelper.deleteCharacter(characterId);
		
		if (deleted) {
			// Remove from character list
			session.getCharacters().removeIf(c -> c.getId() == characterId);
			
			// Remove all items
			DatabaseHelper.deleteInventoryAndEquippedItems(character);
			
			session.sendPacket(PacketBuilder.sendClientCharacterList(session));
			session.sendPacket(PacketBuilder.sendServerSpecialOption(session));
		}
	}
	
	private static void handleClientRequestLogout(GameSession session, ByteBuffer packet) {
		// Create access key
		DatabaseHelper.createAccessKey(session);
		
		// Send auth server
		InetSocketAddress address = session.getServer().getAuthServer().getAddress();
		session.sendPacket(PacketBuilder.sendClientRequestLoginServer(session, address.getHostString(), address.getPort()));
	}
	
	private static void handleClientSelectCharacter(GameSession session, ByteBuffer packet) {
		
	}

	private static void handleClientPlayGameRequest(GameSession session, ByteBuffer packet) {
		int id = packet.getInt();
		packet.getInt();	// Skip 4
		packet.get();
		
		GameCharacter character = session.getCharacters().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
		
		// Invalid character
		if (character == null) {
			return;
		}
		
		// Add access key
		DatabaseHelper.createAccessKey(session);
		
		// Send packet
		session.sendPacket(PacketBuilder.sendClientPlayGameResponse(session, session.getServer().getChannelByIndex(0), character));
	}
}
