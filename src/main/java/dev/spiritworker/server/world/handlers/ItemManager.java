package dev.spiritworker.server.world.handlers;

import dev.spiritworker.Constants;
import dev.spiritworker.game.GameCharacter;
import dev.spiritworker.game.inventory.InventorySlotType;
import dev.spiritworker.game.inventory.InventoryTab;
import dev.spiritworker.game.inventory.Item;
import dev.spiritworker.game.inventory.ItemEnhanceResult;
import dev.spiritworker.net.packet.PacketBuilder;
import dev.spiritworker.server.world.WorldServer;

public class ItemManager {
	private final WorldServer server;
	private final double[] upgradeChances = new double[] {
		0.90, 0.85, 0.80, 0.70, 0.30, 0.20, 0.10, 0.75, 0.5 
	};
	
	public ItemManager(WorldServer server) {
		this.server = server;
	}

	public WorldServer getServer() {
		return server;
	}
	
	public double getUpgradeChanceOf(Item item) {
		return upgradeChances[item.getUpgrades()];
	}
	
	public void upgradeItem(GameCharacter character, int slotType, int slot) {
		InventoryTab tab = character.getInventory().getInventoryTabByType(slotType);
		
		if (tab == null) {
			return;
		}
		
		Item item = tab.getItemAt(slot);
		
		upgradeItem(character, item);
	}
	
	public void upgradeItem(GameCharacter character, Item item) {
		// Sanity check
		if (item == null) {
			return;
		}
		
		// Max upgrade tries reached. TODO get max upgrades from itemRes
		/*
		if (item.getUsedUpgrades() >= item.getItemRes().getMaxUpgrades()) {
			return;
		}
		*/
		
		// Max upgrades reached
		if (item.getUpgrades() >= Constants.MAX_UPGRADES) {
			return;
		}
		
		ItemEnhanceResult result;
		
		if (Math.random() < getUpgradeChanceOf(item)) {
			result = ItemEnhanceResult.SUCCESS;
			item.addUpgrade();
		} else {
			result = ItemEnhanceResult.FAILURE;
		}
		
		item.addUsedUpgrades();
		item.save();
		
		character.getMap().broadcastPacket(PacketBuilder.sendClientItemUpgrade(character, item, result));
	}

	public void increaseInventorySlots(GameCharacter character, int slotType) {
		
		// Get tab by id
		InventoryTab tab = character.getInventory().getInventoryTabByType(slotType);
		
		if (tab == null) {
			return;
		}
		
		// Get ticket
		Item ticket = null;
		
		if (tab.getSlotType().isBankTab()) {
			// Warehouse expansion ticket
			ticket = character.getInventory().getInventoryTabByType(InventorySlotType.PREMIUM).searchItemById(837000002);
		} else {
			// Inventory expansion ticket
			ticket = character.getInventory().getInventoryTabByType(InventorySlotType.PREMIUM).searchItemById(837000001);
		}
		
		// Sanity checks
		if (ticket == null || ticket.getCount() <= 0) {
			return;
		}
		
		// Upgrade
		if (tab.upgrade()) {
			character.getInventory().deleteItem(InventorySlotType.PREMIUM, ticket.getSlot(), 1);
			character.getSession().sendPacket(PacketBuilder.sendClientItemUpdateSlotInfo(tab));
		}
	}

}
