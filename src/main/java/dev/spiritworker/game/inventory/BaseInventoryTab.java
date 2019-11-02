package dev.spiritworker.game.inventory;

import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.net.packet.PacketBuilder;

public abstract class BaseInventoryTab {
	private final Inventory inventory;
	private final InventorySlotType slotType;
	protected Item[] items;
	
	public BaseInventoryTab(Inventory inventory, InventorySlotType slotType) {
		this.inventory = inventory;
		this.slotType = slotType;
	}
	
	public InventorySlotType getSlotType() {
		return this.slotType;
	}

	public synchronized Item[] getItems() {
		return this.items;
	}

	public Inventory getInventory() {
		return inventory;
	}
	
	public GameCharacter getCharacter() {
		return this.inventory.getCharacter();
	}
	
	public boolean isValidItemSlot(int slot) {
		return slot >= 0 && slot < getItems().length;
	}

	public Item getItemAt(int slot) {
		return getItems()[slot];
	}

	public void putItem(int slot, Item item) {
		putItem(slot, item, true);
	}
	
	public void putItem(int slot, Item item, boolean setOwner) {
		getItems()[slot] = item;
		if (item != null) {
			item.setSlot(getSlotType().getValue(), slot);
			if (setOwner) {
				item.setOwner(getInventory().getCharacter());
			}
		}
	}
	
	public Item searchItemById(int id) {
		for (int i = 0; i < this.items.length; i++) {
			if (getItems()[i] == null) {
				continue;
			}
			Item item = getItems()[i];
			if (item.getItemId() == id) {
				return item;
			}
		}
		return null;
	}
	
	
	public synchronized Item deleteItem(int slot, int count) {
		// Sanity check
		if (!isValidItemSlot(slot)) {
			return null;
		}
		
		// Get item from array and delete
		if (getItems()[slot] != null) {
			Item item = getItems()[slot];
			item.setCount(item.getCount() - count);
			item.save();
			if (item.getCount() <= 0) {
				getItems()[slot] = null;
				// Item fully deleted
				getCharacter().getSession().sendPacket(PacketBuilder.sendClientItemBreak(getSlotType(), slot));
			} else {
				// Item count changed, send updated item count to client
				getCharacter().getSession().sendPacket(PacketBuilder.sendClientItemUpdateCount(item));
			}
			return item;
		}
		return null;
	}
}
