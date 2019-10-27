package dev.spiritworker.server.world;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import dev.spiritworker.Constants;
import dev.spiritworker.SpiritWorker;
import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.game.AccessKey;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.character.Skill;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.SkillDef;
import dev.spiritworker.game.inventory.InventorySlotType;
import dev.spiritworker.game.inventory.InventoryTab;
import dev.spiritworker.game.map.District;
import dev.spiritworker.game.map.GameMap;
import dev.spiritworker.game.map.Maze;
import dev.spiritworker.net.packet.PacketBuilder;
import dev.spiritworker.net.packet.PacketOpcodes;
import dev.spiritworker.net.packet.PacketUtils;
import dev.spiritworker.util.FileUtils;
import dev.spiritworker.util.crypto.Crypto;

public class WorldPacketHandler {
	
	public static void handle(WorldSession session, int opcode, ByteBuffer packet) {
		// Auth check
		if (!session.isAuthenticated()) {
			if (opcode == PacketOpcodes.ClientConnectWorldServer) { // Called when the client first connects to the server
				handleClientConnectWorldServer(session, packet);
			}
			return;
		}
		
		if (opcode == PacketOpcodes.ClientKeepAlive) {
			handleClientKeepAlive(session, packet);
			return;
		}
		
		// Map check
		if (session.getCharacter().getMap() == null) {
			return;
		}
		
		// Handle packets
		switch (opcode) {
			case PacketOpcodes.ClientCharacterInfoRequest:
				handleClientInfoRequest(session, packet);
				break;
			case PacketOpcodes.ClientCharacterUpdateSpecialOptionList:
				handleClientCharacterUpdateSpecialOptionList(session, packet);
				break;
			case PacketOpcodes.ClientMovementMove:
				handleClientMovementMove(session, packet);
				break;
			case PacketOpcodes.ClientMovementStop:
				handleClientMovementStop(session, packet);
				break;
			case PacketOpcodes.ClientMovementJump:
				handleClientMovementJump(session, packet);
				break;
			case PacketOpcodes.ClientCancelGestureRequest:
				handleClientMovementCancelGesture(session, packet);
				break;
			case PacketOpcodes.ClientChatNormal:
				handleClientChatNormal(session, packet);
				break;
			case PacketOpcodes.ClientPlayersRequest:
				handleClientRequestPlayers(session);
				break;
			case PacketOpcodes.ClientItemInvenInfo:
				handleClientItemInvenInfo(session);
				break;
			case PacketOpcodes.ClientItemMove:
				handleClientItemMove(session, packet);
				break;
			case PacketOpcodes.ClientItemCombine:
				handleClientItemCombine(session, packet);
				break;
			case PacketOpcodes.ClientItemDivide:
				handleClientItemDivide(session, packet);
				break;
			case PacketOpcodes.ClientItemBreak:
				handleClientItemBreak(session, packet);
				break;
			case PacketOpcodes.ClientItemUse:
				handleClientItemUse(session, packet);
				break;
			case PacketOpcodes.ClientItemUpgrade:
				handleClientItemUpgrade(session, packet);
				break;
			case PacketOpcodes.ClientItemUpdateSlotInfo:
				handleClientItemUpdateSlotInfo(session, packet);
				break;
			case PacketOpcodes.ClientItemDye:
				handleClientItemDye(session, packet);
				break;
			case PacketOpcodes.ClientDoGesture:
				handleClientDoGesture(session, packet);
				break;
			case PacketOpcodes.ClientGestureSlotUpdate:
				handleClientGestureSlotUpdate(session, packet);
				break;
			case PacketOpcodes.ClientAppearancePick:
				handleClientAppearancePick(session, packet);
				break;
			case PacketOpcodes.ClientActivateSkill:
				handleClientActivateSkill(session, packet);
				break;
			case PacketOpcodes.ClientUpgradeSkill:
				handleClientUpgradeSkill(session, packet);
				break;
			case PacketOpcodes.ClientUpgradeSkillModifier:
				handleClientUpgradeSkillModifier(session, packet);
				break;
			case PacketOpcodes.ClientChannelInfo:
				handleClientChannelInfo(session);
				break;
			case PacketOpcodes.ClientCreateMaze:
				handleClientCreateMaze(session, packet);
				break;
			case PacketOpcodes.ClientEnteredMaze:
				handleClientEnteredMaze(session);
				break;
			case PacketOpcodes.ClientLeaveMaze:
				handleClientLeaveMaze(session, packet);
				break;
			case PacketOpcodes.ClientQuitMaze:
				handleClientQuitMaze(session);
				break;
			case PacketOpcodes.ClientMazePortalEnter:
				handleClientMazePortalEnter(session, packet);
				break;
			case PacketOpcodes.ClientResetPositionRequest:
				handleClientResetPositionRequest(session);
				break;
			case PacketOpcodes.ClientRequestLogout: // Called when the client tries to go back to the server list from the character list screen
				handleClientRequestLogout(session, packet);
				break;
			default:
				if (SpiritWorker.getConfig().LOG_PACKETS) {
					SpiritWorker.getLogger().info("Unhandled packet: " + opcode + " Length: " + packet.capacity());
				}
				break;
		}
	}

	private static void handleClientMazePortalEnter(WorldSession session, ByteBuffer packet) {
		if (!(session.getCharacter().getMap() instanceof Maze)) {
			return;
		}
		
		packet.getInt();
		packet.getInt();
		int stage = packet.getInt();
		
		((Maze) session.getCharacter().getMap()).onEnterPortal(stage);
	}

	private static void handleClientResetPositionRequest(WorldSession session) {
		if (session.getCharacter().getMap() instanceof District) {
			District district = (District) session.getCharacter().getMap();
			session.getCharacter().getPosition().set(district.getDef().getUnk1(), district.getDef().getUnk2(), 100);
			session.sendPacket(PacketBuilder.sendClientUpdatePosition(session.getCharacter()));
		}
	}

	private static void handleClientActivateSkill(WorldSession session, ByteBuffer packet) {
		// TODO Auto-generated method stub
		int skillId = packet.getInt();
		int characterId = packet.getInt();
		float x = packet.getFloat();
		float y = packet.getFloat();
		float z = packet.getFloat();
		float angle = packet.getFloat();
		
		// Unknowns
		packet.getInt();
		packet.getInt();
		packet.getInt();
		packet.getInt();
		packet.getInt();
		packet.getInt();
		
		// 
		int unk1 = packet.getInt();
		
		session.sendPacket(PacketBuilder.sendClientActivateSkillResponse(unk1));
		
		SkillDef skillDef = SoulWorker.getSkillDefs().get(skillId);
		if (skillDef == null) {
			return;
		}
		
		// Calculate damage TODO move this to a better place
		if (session.getCharacter().getMap() instanceof Maze) {
			((Maze) session.getCharacter().getMap()).onSkillUse(session.getCharacter(), x, y, z, angle, skillDef);
		}
	}
	
	private static void handleClientEnteredMaze(WorldSession session) {
		if (!(session.getCharacter().getMap() instanceof Maze)) {
			return;
		}
		
		session.getCharacter().setLoadStatus(true);
		((Maze) session.getCharacter().getMap()).onEntered(session.getCharacter());
	}

	private static void handleClientQuitMaze(WorldSession session) {
		if (!(session.getCharacter().getMap() instanceof Maze)) {
			return;
		}
		
		District district = session.getServer().getDistrictById(10003);
		district.addCharacter(session.getCharacter());
		session.getCharacter().getPosition().set(10000, 10000, 100);
		
		session.sendPacket(PacketBuilder.sendClientJoinMap(session.getCharacter(), district));
	}
	
	private static void handleClientLeaveMaze(WorldSession session, ByteBuffer packet) {
		if (!(session.getCharacter().getMap() instanceof Maze)) {
			return;
		}
		
		packet.getInt();
		packet.getInt();
		packet.getInt();
		packet.get();
		int mapId = packet.getShort();
		packet.getShort();
		packet.getInt(); // Maze portal id?
		
		District district = session.getServer().getDistrictById(10003);
		district.addCharacter(session.getCharacter());
		session.getCharacter().getPosition().set(10000, 10000, 100);
		
		session.sendPacket(PacketBuilder.sendClientJoinMap(session.getCharacter(), district));
	}

	private static void handleClientCreateMaze(WorldSession session, ByteBuffer packet) {
		// Unknown
		packet.get();
		packet.getInt();
		packet.getInt();
		packet.getInt();
		int mazeId = packet.getShort();
		
		Maze maze = session.getServer().getMazeManager().createMaze(mazeId, session.getCharacter());
	}

	private static void handleClientUpgradeSkill(WorldSession session, ByteBuffer packet) {
		int skill = packet.getInt();
		packet.getInt();
		int unk1 = packet.getInt();
		int unk2 = packet.getInt();
		
		session.getCharacter().getSkills().upgradeSkill(skill, unk1, unk2);
	}
	
	private static void handleClientUpgradeSkillModifier(WorldSession session, ByteBuffer packet) {
		int skill = packet.getInt();
		int modifier = packet.getInt();
		int unk1 = packet.getInt();
		int unk2 = packet.getInt();
		
		session.getCharacter().getSkills().upgradeSkillModifier(skill, modifier, unk1, unk2);
	}

	private static void handleClientItemDye(WorldSession session, ByteBuffer packet) {
		int tab = packet.get();
		int slot = packet.getShort();
		int dyeType = packet.get();	 // 1 = regular dye, 2 = random
		int color = packet.getInt();
		
		session.getServer().getItemManager().dyeItem(session.getCharacter(), tab, slot, dyeType, color);
	}

	private static void handleClientAppearancePick(WorldSession session, ByteBuffer packet) {
		short hairStyle = packet.getShort();
		short hairColor = packet.getShort();
		short skinColor = packet.getShort();
		short eyeColor = packet.getShort();
		
		boolean changed = false;
		
		// Detect if a equipped style has changed
		changed = session.getCharacter().setEquippedHairStyle(hairStyle);
		changed = session.getCharacter().setEquippedHairColor(hairColor) || changed;
		changed = session.getCharacter().setEquippedSkinColor(skinColor) || changed;
		changed = session.getCharacter().setEquippedEyeColor(eyeColor) || changed;
		
		if (changed) {
			session.getCharacter().getMap().broadcastPacket(PacketBuilder.sendClientAppearancePick(session.getCharacter()));
		}
	}

	private static void handleClientItemDivide(WorldSession session, ByteBuffer packet) {
		int tab1 = packet.get();
		int itemId = packet.getInt();
		int slot1 = packet.getShort();
		int tab2 = packet.get();
		int slot2 = packet.getShort();
		int count = packet.getShort();
		
		// Sanity check
		if (count <= 0) {
			return;
		}
		
		// Handle
		session.getCharacter().getInventory().divideItem(tab1, slot1, tab2, slot2, count);
	}

	private static void handleClientItemCombine(WorldSession session, ByteBuffer packet) {
		int tab1 = packet.get();
		int item1 = packet.getInt();
		int slot1 = packet.getShort();
		int tab2 = packet.get();
		int item2 = packet.getInt();
		int slot2 = packet.getShort();
		int count = packet.getShort();
		
		// Sanity check
		if (count <= 0) {
			return;
		}
		
		// Handle
		session.getCharacter().getInventory().combineItem(tab1, slot1, tab2, slot2, count);
	}

	private static void handleClientItemUpdateSlotInfo(WorldSession session, ByteBuffer packet) {
		int slotType = packet.get();
		
		session.getServer().getItemManager().increaseInventorySlots(session.getCharacter(), slotType);
	}

	private static void handleClientItemUpgrade(WorldSession session, ByteBuffer packet) {
		// Skip 8
		packet.getInt();
		boolean useAntiDestructions = packet.get() == 1;
		int antiDestructionSlotType = packet.get();
		int antiDestructionSlot = packet.getShort();
		
		int slotType = packet.get();
		int slot = packet.getShort();
		
		session.getServer().getItemManager().upgradeItem(session.getCharacter(), slotType, slot);
	}

	private static void handleClientItemUse(WorldSession session, ByteBuffer packet) {
		int slotType = packet.get();
		int slot = packet.getShort();
		packet.getInt(); // Unknown
		int itemId = packet.getInt();
		int count = packet.get();
		
		session.getCharacter().getInventory().useItem(slotType, slot);
	}

	private static void handleClientItemBreak(WorldSession session, ByteBuffer packet) {
		int slotType = packet.get();
		int slot = packet.getShort();
		
		session.getCharacter().getInventory().deleteItem(slotType, slot, 1);
	}

	private static void handleClientItemMove(WorldSession session, ByteBuffer packet) {
		int slotType = packet.get();
		int itemId = packet.getInt();
		int slot = packet.getShort();
		int slotType2 = packet.get();
		int itemId2 = packet.getInt(); // same as itemid2 ??
		int slot2 = packet.getShort();
		
		session.getCharacter().getInventory().moveItem(slotType, slot, slotType2, slot2);
	}

	private static void handleClientItemInvenInfo(WorldSession session) {
		// Send equipped inventory first
		session.sendPacket(PacketBuilder.sendClientItemInvenInfo(session.getCharacter().getInventory().getCosmeticItems()));
		session.sendPacket(PacketBuilder.sendClientItemInvenInfo(session.getCharacter().getInventory().getEquippedItems()));
		
		// Send inventory tab data
		for (InventorySlotType slotType : InventorySlotType.getInventoryTabTypes()) {
			InventoryTab tab = session.getCharacter().getInventory().getInventoryTabByType(slotType);
			session.sendPacket(PacketBuilder.sendClientItemOpenSlotInfo(slotType, tab.getCapacity(), tab.getUpgrades())); // Must be sent before actual items
			if (!slotType.isBankTab()) {
				session.sendPacket(PacketBuilder.sendClientItemInvenInfo(tab));
			} else {
				session.sendPacket(PacketBuilder.sendClientItemBankInfo(tab));
			}
		}

		// Note: Required to get character name to show up
		session.sendPacket(PacketBuilder.sendClientCharacterLoadTitle(session)); 
		session.sendPacket(PacketBuilder.sendClientCharacterUpdateTitle(session)); 
		
		// Send appearance data
		if (session.getCharacter().getAppearances().size() > 0) {
			session.sendPacket(PacketBuilder.sendClientAppearanceInfo(session.getCharacter()));
		}
	}

	private static void handleClientRequestPlayers(WorldSession session) {
		session.getCharacter().getMap().getPlayersInfo(session.getCharacter());
		if (session.getCharacter().getMap().getMapId() == 10003) {
			session.trySendPacket(FileUtils.read(SpiritWorker.getConfig().PACKETS_FOLDER + "npcs.packet")); // 0x0422 NPC
		}
	}

	private static void handleClientCharacterUpdateSpecialOptionList(WorldSession session, ByteBuffer packet) {
		session.sendPacket(PacketBuilder.sendClientCharacterUpdateSpecialOptionList(session));
	}

	private static void handleClientKeepAlive(WorldSession session, ByteBuffer packet) {
		int key = packet.getInt();
		session.sendPacket(PacketBuilder.sendClientKeepAlive(key));
	}
	
	private static void handleClientChannelInfo(WorldSession session) {
		session.sendPacket(PacketBuilder.sendClientChannelInfo(session));
	}

	private static void handleClientInfoRequest(WorldSession session, ByteBuffer packet) {
		session.sendPacket(PacketBuilder.sendClientCharacterInfo(session, session.getCharacter()));
		session.sendPacket(PacketBuilder.sendClientSkillsInfo(session.getCharacter()));
		session.sendPacket(PacketBuilder.sendClientGestureSlotUpdate(session.getCharacter()));
		
		// Recalc stats
		session.getCharacter().getStats().recalc();
	}
	
	private static void handleClientRequestLogout(WorldSession session, ByteBuffer packet) {
		int id = packet.getInt();
		packet.getInt();
		boolean logoutToCharacterServer = packet.get() == 1;
		InetSocketAddress address;
		
		// Add access key
		DatabaseHelper.createAccessKey(session);
		
		if (logoutToCharacterServer) {
			address = session.getServer().getGameServer().getAddress();
		} else {
			address = session.getServer().getGameServer().getAuthServer().getAddress();
		}
		
		session.sendPacket(PacketBuilder.sendClientRequestLoginServer(session, address.getHostString(), address.getPort()));
	}

	private static void handleClientConnectWorldServer(WorldSession session, ByteBuffer packet) {
		int accountId = packet.getInt();
		int characterId = packet.getInt();
		
		// Authenticate
		boolean validated = AccessKey.validate(session, accountId);
		
		// Bad key
		if (!validated) {
			return;
		}
		
		// Set id
		session.setAccountId(accountId);
		
		// Load character from db
		GameCharacter character = DatabaseHelper.getCharacterById(session, characterId);
		
		if (character == null) {
			return;
		}
		
		// Set character and register to game server
		session.setCharacter(character);
		session.getGameServer().addCharacter(character);

		// Load inventory
		character.getInventory().loadItems();
		
		// Load skills
		character.loadSkills();
		
		// Join map
		GameMap map = session.getServer().getDistrictById(character.getMapId());
		if (map == null) {
			map = session.getServer().getDefaultDistrict();
			character.setMap(map);
			character.getPosition().set(10000, 10000, 100);
			character.save();
		}
		
		map.addCharacter(character);
		
		// Send packets
		session.sendPacket(PacketBuilder.sendServerDate());
		session.sendPacket(PacketBuilder.sendServerVersion());
		session.sendPacket(PacketBuilder.sendClientEnterGameServerResponse(session));
	}
	
	private static void handleClientMovementMove(WorldSession session, ByteBuffer packet) {
		int id = packet.getInt();
		
		if (session.getCharacter().getId() != id) {
			return;
		}
		
		packet.getInt(); // Unknown
		int mapId = packet.getShort();
		packet.getShort();
		float x = packet.getFloat();
		float y = packet.getFloat();
		float z = packet.getFloat();
		float angle = packet.getFloat();
		
		// New Position
		float newX = packet.getFloat();
		float newY = packet.getFloat();
				
		float oldX = session.getCharacter().getPosition().getX();
		float oldY = session.getCharacter().getPosition().getY();
		
		float unknown1 = packet.getFloat();
		float unknown2 = packet.getFloat();
		
		session.getCharacter().getPosition().set(newX, newY, z);
		session.getCharacter().setAngle(angle);
		
		/*
		session.getCharacter().getMap().broadcastPacketFrom(
			session.getCharacter(),
			PacketBuilder.sendClientPlayerMovement(session.getCharacter(), oldX, oldZ)
		);
		 */
		
		byte[] copy = new byte[packet.array().length];
		System.arraycopy(packet.array(), 0, copy, 0, packet.array().length);
		copy[6] = 0x02;
		Crypto.xor(copy, 5);
		
		session.getCharacter().getMap().broadcastPacketFrom(
			session.getCharacter(), copy
		);
	}

	private static void handleClientMovementStop(WorldSession session, ByteBuffer packet) {
		int id = packet.getInt();
		
		if (session.getCharacter().getId() != id) {
			return;
		}
		
		packet.getInt(); // Unknown
		packet.getInt(); // Unknown
		// Original position
		float x = packet.getFloat();
		float z = packet.getFloat();
		float y = packet.getFloat();
		
		// Angle
		float angle = packet.getFloat();
		float unknown1 = packet.getFloat();
		
		session.getCharacter().getPosition().set(x, z, y);
		session.getCharacter().setAngle(angle);
		
		/*
		SWLogger.log.info("old X: " + x);
		SWLogger.log.info("old Z: " + z);
		SWLogger.log.info("unk1: " + unknown1);
		*/
		
		/*
		session.getCharacter().getMap().broadcastPacketFrom(
			session.getCharacter(),
			PacketBuilder.sendClientPlayerMovementStop(session.getCharacter())
		);
		*/
		
		byte[] copy = new byte[packet.array().length];
		System.arraycopy(packet.array(), 0, copy, 0, packet.array().length);
		copy[6] = 0x04;
		Crypto.xor(copy, 5);
		
		session.getCharacter().getMap().broadcastPacketFrom(
			session.getCharacter(), copy
		);
	}

	private static void handleClientMovementJump(WorldSession session, ByteBuffer packet) {
		int id = packet.getInt();
		
		if (session.getCharacter().getId() != id) {
			return;
		}
		
		packet.getInt(); // Unknown
		packet.getInt(); // Unknown
		float x = packet.getFloat();
		float z = packet.getFloat();
		float y = packet.getFloat();
		float angle = packet.getFloat();
		
		session.getCharacter().getPosition().set(x, z, y);
		session.getCharacter().setAngle(angle);
		
		/*
		session.getCharacter().getMap().broadcastPacketFrom(
			session.getCharacter(),
			PacketBuilder.sendClientPlayerMovement(session.getCharacter(), PacketOpcodes.ClientPlayerMovementJump)
		);
		*/
		
		byte[] copy = new byte[packet.array().length];
		System.arraycopy(packet.array(), 0, copy, 0, packet.array().length);
		copy[6] = 0x06;
		Crypto.xor(copy, 5);
		
		session.getCharacter().getMap().broadcastPacketFrom(
			session.getCharacter(), copy
		);
	}
	
	private static void handleClientMovementCancelGesture(WorldSession session, ByteBuffer packet) {
		session.getCharacter().getMap().broadcastPacketFrom(
			session.getCharacter(), PacketBuilder.sendClientCancelGesture(session.getCharacter())
		);
	}
	
	private static void handleClientChatNormal(WorldSession session, ByteBuffer packet) {
		int type = packet.get();
		String message = PacketUtils.readString16(packet);
		
		if (message.length() == 0) {
			return;
		}
		
		session.getServer().getChatManager().handleNormalChat(session, message);
	}
	
	private static void handleClientGestureSlotUpdate(WorldSession session, ByteBuffer packet) {
		for (int i = 0; i < Constants.MAX_EMOTE_SLOTS; i++) {
			// TODO sanity check
			session.getCharacter().getEmotes()[i] = packet.getInt();
		}
		
		session.sendPacket(PacketBuilder.sendClientGestureSlotUpdate(session.getCharacter())); 
	}

	private static void handleClientDoGesture(WorldSession session, ByteBuffer packet) {
		int gestureId = packet.getInt();
		
		session.getCharacter().getMap().broadcastPacket(PacketBuilder.sendClientDoGesture(session.getCharacter(), gestureId));
	}
}
