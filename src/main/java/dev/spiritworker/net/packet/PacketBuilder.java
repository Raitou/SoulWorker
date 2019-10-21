package dev.spiritworker.net.packet;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import dev.spiritworker.database.DatabaseHelper;
import dev.spiritworker.game.CharacterStats;
import dev.spiritworker.game.GameCharacter;
import dev.spiritworker.game.GameMap;
import dev.spiritworker.game.Stat;
import dev.spiritworker.game.inventory.BaseInventoryTab;
import dev.spiritworker.game.inventory.InventorySlotType;
import dev.spiritworker.game.inventory.InventoryTab;
import dev.spiritworker.game.inventory.Item;
import dev.spiritworker.game.inventory.ItemEnhanceResult;
import dev.spiritworker.net.packet.opcodes.PacketOpcodes;
import dev.spiritworker.net.packet.util.PacketWriter;
import dev.spiritworker.netty.SoulWorkerSession;
import dev.spiritworker.server.auth.AuthSession;
import dev.spiritworker.server.game.GameSession;
import dev.spiritworker.server.world.WorldServer;
import dev.spiritworker.server.world.WorldSession;
import dev.spiritworker.util.ServerData;

public class PacketBuilder {

	public static byte[] sendClientLoginResult(AuthSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientLoginResult);
		
		p.writeUint32(session.getAccountId()); // User id
		p.writeUint8(1);	// Unknown
		p.writeDirectString8(session.getMac());
		p.writeEmpty(7);
		p.writeUint8(1);
		p.writeString16(session.getUsername()); // User name
		p.writeBytes(new byte[] {(byte) 0xa5, (byte) 0x60, (byte) 0x2f, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});
		
		return p.getPacket();
	}
	
	public static byte[] sendClientServerList(AuthSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientServerList);
		
		p.writeUint8(0);
		p.writeUint8(session.getServer().getGameServers().size());	// Server list size
		
		for (ServerData server : session.getServer().getGameServers().values()) {
			p.writeUint16(server.getId()); // Server id
			
			p.writeUint16(server.getAddress().getPort());
			p.writeString8(server.getName());
			p.writeString8(server.getAddress().getHostString());
			
			p.writeUint32(1);
			p.writeUint16(0); // Number of players on the server
			p.writeUint16(0);
			p.writeUint8((int) DatabaseHelper.getCharacterCount(session)); // Number of characters on the server
		}
		
		return p.getPacket();
	}
	
	public static byte[] sendClientOptionLoad(AuthSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientOptionLoad);
		
		p.writeBytes(new byte[] {
			0x36, 0x31, 0x30, 0x31, 0x30, 0x30, 0x30, 0x31,  0x30, 0x30, 0x30, 0x31, 0x31, 0x31, 0x31, 0x30,
			0x30, 0x35, 0x31, 0x30, 0x31, 0x30, 0x30, 0x30,  0x30, 0x31, 0x31, 0x31, 0x20, 0x20, 0x20, 0x20,
			0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,  0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
			0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,  0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x00,
			0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,  0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
		});
		p.writeUint32(session.getAccountId()); // Player id
		//p.writeDirectString8(); // Player saved options
		
		return p.getPacket();
	}

	public static byte[] sendClientServerSelect(AuthSession session, InetSocketAddress address) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientServerSelect);
		
		p.writeString8(address.getHostString());
		p.writeUint16(address.getPort());
		
		return p.getPacket();
	}
	
	public static byte[] sendClientCharacterList(GameSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientCharacterList);
		int selected = 0;
		
		p.writeUint8(session.getCharacters().size()); // Character count
		
		for (int i = 0; i < session.getCharacters().size(); i++) {
			GameCharacter character = session.getCharacters().get(i);
			p.writeUint32(character.getId());
			p.writeString16(character.getName());
			p.writeUint8(character.getType());
			p.writeUint8(0); // Class advancement??
			p.writeUint32(0); // Unknown
			p.writeUint16(character.getHairStyle());
			p.writeUint16(character.getHairColor());
			p.writeUint16(character.getEyeColor());
			p.writeUint16(character.getSkinColor());
			p.writeUint16(character.getEquippedHairStyle());
			p.writeUint16(character.getEquippedHairColor());
			p.writeUint16(character.getEquippedSkinColor());
			p.writeUint16(character.getEquippedEyeColor());
			p.writeUint16(character.getLevel());
			p.writeEmpty(10); // Titles go here??
			
			// Current weapon
			Item weapon = character.getInventory().getWeapon();
			p.writeInt32(weapon != null ? weapon.getItemId() : -1); // Weapon item id
			p.writeUint8(0); // Unknown
			p.writeInt32(-1); // Unknown
			
			// Equipment
			for (int e = 0; e < 13; e++) {
				Item item = character.getInventory().getCosmeticItems().getItemAt(e);
				if (item != null) {
					p.writeInt32(item.getUniqueId()); // ?
					p.writeInt32(Item.UNKNOWN2); // ?
					p.writeInt32(item.getItemId()); // Equipped item id
					p.writeUint32(item.getDyeColor());
				} else {
					p.writeInt32(-1); // ?
					p.writeInt32(-1); // ?
					p.writeInt32(-1); // Equipped item id
					p.writeUint32(0);
				}
				p.writeInt32(-1);
				p.writeInt32(-1);
				p.writeInt32(-1); 
				p.writeUint32(0);
			}
			
			// 
			p.writeEmpty(62);
			p.writeFloat(1f);
			p.writeFloat(1f);
			p.writeEmpty(20);
			
			p.writeUint8(i + 1); // Character slot
			
			// Set selected
			if (selected == 0) {
				selected = character.getId();
			}
		}
		
		p.writeUint32(selected); // Last selected character id
		p.writeEmpty(10); // 10 empty
		
		return p.getPacket();
	}
	
	public static byte[] sendClientRequestLoginServer(SoulWorkerSession session, String address, int port) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientRequestLogout);
		
		p.writeUint32(0);
		p.writeUint32(session.getAccountId());
		p.writeString8(address);
		p.writeUint16(port);
		p.writeUint8(0);
		p.writeUint8(1);
		
		return p.getPacket();
	}

	public static byte[] sendClientConnectGameResponse(GameSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientConnectGameResponse);
		
		p.writeUint8(0);
		p.writeUint32(session.getAccountId());
		
		return p.getPacket();
	}

	public static byte[] sendServerDate() {
		PacketWriter p = new PacketWriter(PacketOpcodes.CurrentServerDate);
		
		LocalDateTime date = LocalDateTime.now();
		p.writeUint64((long) Math.floor(System.currentTimeMillis() / 1000)); // Number of seconds since unix epoch time
		p.writeUint16(date.getYear());
		p.writeUint16(date.getMonthValue());
		p.writeUint16(date.getDayOfMonth());
		p.writeUint16(date.getHour());
		p.writeUint16(date.getMinute());
		p.writeUint16(date.getSecond());
		p.writeBoolean(false); // Daylight savings
		p.writeUint8(0);
		
		return p.getPacket();
	}
	
	public static byte[] sendServerVersion() {
		PacketWriter p = new PacketWriter(PacketOpcodes.CurrentServerVersion);
		
		p.writeUint32(0);
		p.writeUint32(1);
		p.writeUint32(773);
		p.writeUint32(13172);
		
		return p.getPacket();
	}
	
	public static byte[] sendServerSpecialOption(GameSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ServerSpecialOption);
		
		p.writeBoolean(true);
		p.writeBoolean(false); // Enable secondary password
		p.writeBoolean(true);
		p.writeBoolean(false);
		p.writeBoolean(true);
		p.writeBoolean(true);
		p.writeBoolean(false);
		p.writeBoolean(false);
		p.writeBoolean(true);
		p.writeBoolean(true);
		p.writeBoolean(false);
		p.writeBoolean(true);
		p.writeBoolean(false);
		p.writeBoolean(false);
		
		return p.getPacket();
	}
	
	public static byte[] sendErrorCode(int code) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ErrorCode, 2);
		
		p.writeUint16(code);
		
		return p.getPacket();
	}

	public static byte[] sendClientPlayGameResponse(GameSession session, WorldServer worldServer, GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientPlayGameResponse);
		
		p.writeUint32(character.getId());	// Character id
		p.writeUint32(session.getAccountId());	// Session id
		
		p.writeBytes(new byte[] {
			(byte) 0x02, (byte) 0x02, (byte) 0x02, (byte) 0x00, (byte) 0x31, (byte) 0x32, (byte) 0x20, (byte) 0x00, (byte) 0x31, (byte) 0x32, (byte) 0x20, (byte) 0x00,
			(byte) 0x92, (byte) 0xe1, (byte) 0x00, (byte) 0x00
		});
		
		p.writeUint32(152183);	// Unknown
		p.writeUint64(0);	// Unknown
		p.writeString8(worldServer.getAddress().getHostString());
		p.writeUint16(worldServer.getAddress().getPort());
		p.writeInt16(-1);
		p.writeEmpty(36);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientKeepAlive(int unknown) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientKeepAlive);
		
		p.writeUint32(unknown);
		p.writeUint32(0);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientEnterGameServerResponse(WorldSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientEnterGameServerResponse);
		
		p.writeUint32(0);
		p.writeUint8(1);
		p.writeUint16(session.getCharacter().getDistrictId());
		p.writeUint16(101);
		p.writeUint16(256);
		p.writeUint16(session.getCharacter().getDistrictId());
		p.writeUint16(256);
		// coords?
		p.writeFloat(0f);
		p.writeFloat(0f);
		p.writeFloat(100f);
		p.writeFloat(0f);
		p.writeEmpty(5);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientCharacterInfo(WorldSession session, GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientCharacterInfoResponse);
		
		character.writeMainData(p);
		character.writeCosmetics(p);
		character.writeExtraData(p);
		p.writeUint32(character.getExp()); // Exp
		p.writeUint64(character.getMoney()); // Money (Zenny)
		p.writeUint32(0); // Unknown
		p.writeEmpty(8);
		p.writeUint64(character.getBp()); // BP
		p.writeUint64(character.getEther()); // Ether
		p.writeUint64(0); // Unknown
		p.writeString8(String.valueOf(session.getAccountId()));
		p.writeEmpty(15);
		p.writeUint8(1); // Unknown
		
		return p.getPacket();
	}
	
	public static byte[] sendClientChatNormal(WorldSession session, String message) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientChatNormal);
		
		p.writeUint32(session.getCharacter().getId());
		p.writeUint32(1);
		p.writeString16(message);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientCharacterUpdateSpecialOptionList(WorldSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientCharacterUpdateSpecialOptionList);
		
		p.writeUint32(session.getCharacter().getId());
		p.writeUint8(1);
		p.writeUint32(15);
		p.writeUint8(160);
		p.writeUint8(65);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientCharacterLoadTitle(WorldSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientCharacterLoadTitle);

		p.writeUint8(1);
		p.writeUint32(0);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientCharacterUpdateTitle(WorldSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientCharacterUpdateTitle);
		
		p.writeUint32(0);
		p.writeUint32(0);
		p.writeUint32(0);
		p.writeUint32(0);
		p.writeUint8(1);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemInvenInfo(BaseInventoryTab tab) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemInvenInfo);
		
		// Counter
		int count = 0;
		for (Item item : tab.getItems()) {
			if (item == null) {
				continue;
			}
			count++;
		}
		
		p.writeUint32(count); // Count
		
		for (Item item : tab.getItems()) {
			if (item == null) {
				continue;
			}
			item.write(p);
		}
		
		// Footer
		p.writeUint8(0);

		return p.getPacket();
	}
	
	public static byte[] sendClientItemBankInfo(BaseInventoryTab tab) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemBankInfo);
		
		// Counter
		int count = 0;
		for (Item item : tab.getItems()) {
			if (item == null) {
				continue;
			}
			count++;
		}
		
		p.writeUint32(count); // Count
		
		for (Item item : tab.getItems()) {
			if (item == null) {
				continue;
			}
			item.write(p);
		}
		
		p.writeUint8(0);

		return p.getPacket();
	}
	
	public static byte[] sendClientItemOpenSlotInfo(InventorySlotType slotType, int maxSlots) {
		return sendClientItemOpenSlotInfo(slotType, maxSlots, 0);
	}
	
	public static byte[] sendClientItemOpenSlotInfo(InventorySlotType slotType, int maxSlots, int unlocked) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemOpenSlotInfo);
		
		p.writeUint8(1); // Always 1
		p.writeUint8(slotType.getValue());	// Inventory slot type
		p.writeUint16(maxSlots); // Number of Slots unlocked (Max: 384. 48 slots per tab)
		p.writeUint8(unlocked);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemUpdateSlotInfo(InventoryTab tab) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemUpdateSlotInfo);
		
		p.writeUint8(tab.getSlotType().getValue());		// Inventory slot type
		p.writeUint8(tab.getUpgrades());				// Amount of upgrades used
		p.writeUint16(tab.getCapacity());				// Number of Slots unlocked (Max: 384. 48 slots per tab)
		
		
		return p.getPacket();
	}

	public static byte[] sendClientItemBreak(InventorySlotType slotType, int slot) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemBreak);
		
		p.writeUint8(slotType.getValue());	// Inventory slot type
		p.writeUint16(slot); // Slot number
		
		return p.getPacket();
	}

	public static byte[] sendClientItemMove(int slotTypeSrc, int slotSrc, Item itemSrc, int slotTypeDest, int slotDest, Item itemDest) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemMove);
		
		p.writeUint8(1);						// Amount of item objects to move
		p.writeUint8(slotTypeSrc);								// Source tab type
		p.writeInt32(itemSrc != null ? itemSrc.getItemId() : -1);	// Source slot item
		p.writeUint16(slotSrc);									// Source slot number
		p.writeUint8(slotTypeDest);								// Dest tab type
		p.writeInt32(itemDest != null ? itemDest.getItemId() : -1);	// Dest slot item
		p.writeUint16(slotDest);								// Dest slot number
		p.writeEmpty(59);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemDivide(Item itemSrc, Item itemDest) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemDivide);
		
		p.writeInt32(itemSrc.getItemId());	// Source slot item id
		p.writeUint8(itemSrc.getTab());
		p.writeUint16(itemSrc.getSlot());
		p.writeUint16(itemSrc.getCount());
		itemDest.write(p);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemCombine(GameCharacter character, Item itemSrc, Item itemDest) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemCombine);
		
		p.writeUint32(character.getId());
		itemSrc.write(p);
		p.writeUint8(48); // ?
		p.writeUint32(character.getId());
		itemDest.write(p);
		p.writeUint8(2);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemUpdateCount(Item item) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemUpdateCount);
		
		p.writeUint8(0);
		p.writeUint8(item.getTab());
		p.writeUint16(item.getSlot());
		p.writeUint32(item.getCount());
		
		return p.getPacket();
	}

	public static byte[] sendClientItemCreate(Item item) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemCreate);
		
		p.writeUint32(1);
		item.write(p);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemUpgrade(GameCharacter character, Item item, ItemEnhanceResult result) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemUpgrade);
		
		p.writeUint32(character.getId());
		p.writeUint8(result.value());
		p.writeUint32(item.getItemId());
		item.writeMetadata(p);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemUpdate(GameCharacter character, int tab, int slot, Item item) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemUpdate);
		
		p.writeUint32(character.getId());
		p.writeUint32(1); // Amount of items to update
		if (item != null) {
			item.write(p);
		} else {
			Item.writeEmpty(p, tab, slot);
		}
		// Footer
		p.writeUint8(0);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientOpenPackageResult(Item item, List<Item> openedItems) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemUse);
		
		p.writeUint32(item.getItemId());
		p.writeEmpty(28);
		p.writeUint8(openedItems.size());
		for (Item it : openedItems) {
			p.writeUint32(it.getItemId());
			p.writeUint16(it.getCount());
		}
		
		return p.getPacket();
	}
	
	public static byte[] sendClientCharacterUpdate(GameCharacter character, Stat stat) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientCharacterUpdate);
		
		p.writeUint8(0);
		p.writeUint32(character.getId());
		p.writeUint8(1); // Amount of stats to update
		stat.write(p);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientCharacterUpdate(CharacterStats stats) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientCharacterUpdate);
		
		List<Stat> toUpdate = new LinkedList<Stat>();
		
		for (Stat stat : stats.getMap().values()) {
			if (!stat.isUpdated()) {
				continue;
			}
			toUpdate.add(stat);
		}
		
		p.writeUint8(0);
		p.writeUint32(stats.getCharacter().getId());
		p.writeUint8(toUpdate.size()); // Amount of stats to update
		for (Stat stat : toUpdate) {
			stat.write(p);
		}

		return p.getPacket();
	}

	public static byte[] sendClientDoGesture(GameCharacter character, int gestureId) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientDoGesture);
		
		p.writeUint32(character.getId());
		p.writeUint32(gestureId);
		
		p.writeFloat(character.getPosition().getX());
		p.writeFloat(character.getPosition().getZ());
		p.writeFloat(character.getPosition().getY());
		p.writeFloat(character.getAngle());
		p.writeEmpty(4);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientGestureSlotUpdate(GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientGestureSlotUpdate);
		
		for (int emote : character.getEmotes()) {
			p.writeUint32(emote);
		}
		
		return p.getPacket();
	}
	
	public static byte[] sendClientChannelInfo(WorldSession session) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientChannelInfo);
		
		p.writeUint16(session.getCharacter().getDistrictId());
		p.writeUint8(session.getGameServer().getChannelCount());
		
		for (WorldServer channel : session.getGameServer().getChannels()) {
			p.writeUint16(channel.getChannelId());
			if (channel == session.getServer()) {
				p.writeUint8(1);
			} else {
				p.writeUint8(0);
			}
		}
		
		return p.getPacket();
	}

	public static byte[] sendClientPlayerSpawn(GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientPlayerSpawn);
		
		character.writeMainData(p);
		character.writeCosmetics(p);
		character.writeExtraData(p);
		
		return p.getPacket();
	}

	public static byte[] sendClientPlayerRemove(GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientPlayerRemove);
		
		p.writeUint8(1); // Remove count
		p.writeUint32(character.getId());
		
		return p.getPacket();
	}

	public static byte[] sendClientPlayersInfo(GameCharacter character, GameMap gameMap) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientPlayersInfo);
		
		List<GameCharacter> otherPlayers = gameMap.getCharacters().stream().filter(c -> c != character).collect(Collectors.toList());
		
		p.writeUint16(otherPlayers.size());
		for (GameCharacter player : otherPlayers) {
			player.writeMainData(p);
			player.writeCosmetics(p);
			player.writeExtraData(p);
		}
		
		return p.getPacket();
	}
	
	public static byte[] sendClientPlayerMovement(GameCharacter character, float oldX, float oldZ) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientPlayerMovementMove);
		
		p.writeUint32(character.getId());
		p.writeUint32(0);
		p.writeUint16(character.getMap().getMapId());
		p.writeUint16(2);
		p.writeFloat(oldX);
		p.writeFloat(oldZ);
		p.writeFloat(character.getPosition().getY());
		p.writeFloat(character.getAngle());
		p.writeFloat(character.getPosition().getX());
		p.writeFloat(character.getPosition().getZ());
		p.writeEmpty(11);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientPlayerMovementStop(GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientPlayerMovementStop);
		
		p.writeUint32(character.getId());
		p.writeUint32(0);
		p.writeUint16(character.getMap().getMapId());
		p.writeUint16(2);
		p.writeFloat(character.getPosition().getX());
		p.writeFloat(character.getPosition().getZ());
		p.writeFloat(character.getPosition().getY());
		p.writeFloat(character.getAngle());
		p.writeFloat(0);
		p.writeUint8(1);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientPlayerMovementJump(GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientPlayerMovementJump);
		
		p.writeUint32(character.getId());
		p.writeUint32(0);
		p.writeUint16(character.getMap().getMapId());
		p.writeUint16(2);
		p.writeFloat(character.getPosition().getX());
		p.writeFloat(character.getPosition().getZ());
		p.writeFloat(character.getPosition().getY());
		p.writeFloat(character.getAngle());
		p.writeFloat(0);
		p.writeUint8(1);
		
		return p.getPacket();
	}

	public static byte[] sendClientCancelGesture(GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientCancelGesture);
		
		p.writeUint32(character.getId());
		
		return p.getPacket();
	}
	
	public static byte[] sendClientAppearanceInfo(GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientAppearanceInfo);
		
		p.writeUint8(character.getAppearances().size());
		for (int s : character.getAppearances()) {
			p.writeUint16(s);
			p.writeUint64(0);
		}
		
		return p.getPacket();
	}
	
	public static byte[] sendClientAppearancePick(GameCharacter character) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientAppearancePick);
		
		p.writeUint32(character.getId());
		p.writeUint16(character.getEquippedHairStyle());
		p.writeUint16(character.getEquippedHairColor());
		p.writeUint16(character.getEquippedSkinColor());
		p.writeUint16(character.getEquippedEyeColor());
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemUpdateDye(Item item) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemUpdateDye);
		
		p.writeUint32(1); // Amount of items to update
		item.write(p);
		
		return p.getPacket();
	}
	
	public static byte[] sendClientItemDyeResult(GameCharacter character, Item item) {
		PacketWriter p = new PacketWriter(PacketOpcodes.ClientItemDye);
		
		p.writeUint32(item.getUniqueId());
		p.writeUint32(Item.UNKNOWN2);
		p.writeUint32(character.getId());
		p.writeUint32(item.getItemId());
		p.writeUint32(item.getDyeColor());
		p.writeUint32(4); // Unknown
		p.writeUint32(0); // Unknown
		
		return p.getPacket();
	}
}
