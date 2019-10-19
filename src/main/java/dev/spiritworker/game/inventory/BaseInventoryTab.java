package dev.spiritworker.game.inventory;

import dev.spiritworker.game.GameCharacter;

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

	public Item[] getItems() {
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
}
